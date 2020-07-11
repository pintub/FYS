package com.fys.camfeedprocessing.thread.impl;

import com.fys.camfeedprocessing.bean.ImageFrame;
import com.fys.camfeedprocessing.util.FileType;
import com.fys.camfeedprocessing.util.LoggerUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.core.env.Environment;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.concurrent.BlockingQueue;

public final class CamFeedToFrameProducer {

    private static final Logger LOG = LogManager.getLogger(CamFeedToFrameProducer.class);

    private final Environment env;
    private final String fileName;
    private final FileType fileType;
    private BlockingQueue<ImageFrame> frameQueue;

    public CamFeedToFrameProducer(Environment env, String fileName, FileType fileType
            , BlockingQueue<ImageFrame> frameQueue) {
        this.env = env;
        this.fileName = fileName;
        this.fileType = fileType;
        this.frameQueue = frameQueue;
    }

    public void convert(){
        try(FFmpegFrameGrabber frameGrabber = getfFmpegFrameGrabber();) {
            long fileStartTime = System.currentTimeMillis();

            //frameGrabber.start();
            doPollingTillRtspSeverStarts(frameGrabber);

            String destinationPath = getOutputPath();
            double frameRate = frameGrabber.getFrameRate();
            long frameLatencyMilliSec = (long) (1000 / frameRate);
            int avgParkingTimeSeconds = Integer.parseInt(env.getProperty("srv.avgParkingTime"));
            int frameCounter = 0;
            Java2DFrameConverter javaConverter = new Java2DFrameConverter();

            Frame frame;
            while ((frame = frameGrabber.grabImage()) != null) {

                StopWatch frameWatch = LoggerUtil.createTimerStarted();

                ++frameCounter;

                //Take frames {1, 1+X , !+2X....}, where X=avgParkingTimeSeconds * frameRate
                if (frameCounter % (avgParkingTimeSeconds * frameRate) != 1) {
                    sleep(frameWatch, frameLatencyMilliSec);
                    continue;
                }
                //BufferedImage image = Java2DFrameUtils.toBufferedImage(frame);
                BufferedImage image = javaConverter.getBufferedImage(frame);

                //Not Required ,as now using grabImage()
            /*if(image == null){
                continue;
            }*/

                String encodedImage = Base64.getEncoder().encodeToString(toByteArray(image));
                //TODO Remove
                String outputFileName = "Frame-" + frameCounter + "-"
                        + fileStartTime + "-" + (System.currentTimeMillis() - fileStartTime) + ".png";
                ImageIO.write(image, "png", new File(destinationPath + "/" + outputFileName));
                //LoggerUtil.info(LOG, "CamFeedToFrameProducer#convert", outputFileName + " Encoded String->" + encodedImage);
                //TODO Remove END

                frameQueue.put(new ImageFrame(frameCounter, encodedImage));

                LoggerUtil.logTimeAndStop(frameWatch, LOG, "CamFeedToFrameProducer#convert() For Each Frame:" + frameCounter);
                sleep(frameWatch, frameLatencyMilliSec);
            }
        } catch (Exception e){
            LoggerUtil.error(LOG, "CamFeedToFrameProducer#convert", "Error Processing File: " + fileName, e);
        }
    }

    private FFmpegFrameGrabber getfFmpegFrameGrabber() throws IOException {
        FFmpegFrameGrabber frameGrabber;
        if (fileType == FileType.RTSP_STREAM) {
            frameGrabber = new FFmpegFrameGrabber(fileName);
            frameGrabber.setOption("listen", "1");
            frameGrabber.setFormat("rtsp");
        } else {
            frameGrabber = new FFmpegFrameGrabber(getInputStream(fileName, fileType));
            frameGrabber.setFormat("mp4");
        }
        return frameGrabber;
    }

    private String getOutputPath() {
        return env.getProperty("test.frameDir");
    }

    private byte[] toByteArray(BufferedImage image) throws IOException {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write( image, "png", outputStream);
            outputStream.flush();
            return outputStream.toByteArray();
        }
    }

    private File getResource(String fileName){
        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("Cam Feed is not found!");
        }
        return new File(resource.getFile());
    }

    private InputStream getInputStream(String fileName, FileType fileType) throws IOException {
        InputStream inputStream;
        if(fileType == FileType.LOCAL_FILE) {
            File file = getResource(fileName);
            inputStream = new FileInputStream(file);
        } else {
            URL url = new URL(fileName);
            URLConnection urlConnection = url.openConnection();
            inputStream = urlConnection.getInputStream();
        }
        return inputStream;
    }

    //Sleep thread to delay frame read based on frame-rate
    private void sleep(StopWatch watch, long frameLatencyMilliSec) throws InterruptedException {
        /*long timeMilliSec = watch.getTime();
        if (timeMilliSec < frameLatencyMilliSec) {
            Thread.sleep(frameLatencyMilliSec - timeMilliSec);
        }*/
    }

    private void doPollingTillRtspSeverStarts(FrameGrabber frameGrabber){
        boolean hasStarted = false;
        while (!hasStarted) {
            try {
                frameGrabber.start();
                hasStarted = true;
            } catch (FrameGrabber.Exception e) {
                LoggerUtil.error(LOG, "CamFeedToFrameProducer#doPollingTillRtspSeverStarts",
                        "RTSP not started, Keep polling...");
            } finally {
                if(hasStarted){
                    break;
                }
            }
        }
    }
}
