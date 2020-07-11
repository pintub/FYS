package com.fys.camfeedprocessing.service;

import com.fys.camfeedprocessing.bean.ImageFrame;
import com.fys.camfeedprocessing.bean.ImageFrameParkingSlotBean;
import com.fys.camfeedprocessing.thread.FrameConsumerVacantParkingSlotProducerTask;
import com.fys.camfeedprocessing.thread.FrameProducerTask;
import com.fys.camfeedprocessing.thread.VacantParkingSlotConsumerTask;
import com.fys.camfeedprocessing.thread.impl.CamFeedToFrameProducer;
import com.fys.camfeedprocessing.thread.impl.FysUpdateServiceClient;
import com.fys.camfeedprocessing.thread.impl.OcrDetector;
import com.fys.camfeedprocessing.util.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public final class CamFeedProcessor {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    public void process(String camFeedFileName, FileType fileType) throws InterruptedException {

        BlockingQueue<ImageFrame> frameQueue = new LinkedBlockingQueue(1024);
        /*BlockingQueue<ImageFrameParkingSlotBean> parkingDataQueue =
                new PriorityBlockingQueue(1024, new ImageFrameParkingSlotBeanComparator());*/
        BlockingQueue<ImageFrameParkingSlotBean> parkingDataQueue =
                new LinkedBlockingQueue(1024);

        int ocrThreads = Integer.parseInt(env.getProperty("srv.ocrThreads"));

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
                .newFixedThreadPool(ocrThreads + 1 + 1);

        executor.submit(new FrameProducerTask(new CamFeedToFrameProducer(env
                , camFeedFileName, fileType, frameQueue)));

        executor.submit(new VacantParkingSlotConsumerTask(
                new FysUpdateServiceClient(env, restTemplate, parkingDataQueue)));

        for(int count = 0;  count < ocrThreads; count++) {
            executor.submit(new FrameConsumerVacantParkingSlotProducerTask(
                    new OcrDetector(env, restTemplate, frameQueue, parkingDataQueue)));
            if(count != ocrThreads - 1){
                //Give previous thread some head-start so that ordered frames will be queued to parkingDataQueue
                //As a safety net ordering issue is handled in FysUpdateServiceClient#Send
                //Thread.sleep(Integer.parseInt(env.getProperty("srv.ocrThreadAvgTime")));
                Thread.sleep(300);
            }
        }

    }

}
