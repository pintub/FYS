package com.fys.camfeedprocessing.thread.impl;

import com.fys.camfeedprocessing.bean.ImageFrame;
import com.fys.camfeedprocessing.bean.ImageFrameParkingSlotBean;
import com.fys.camfeedprocessing.bean.ParkingSlot;
import com.fys.camfeedprocessing.bean.ocr.OcrDetectionResponse;
import com.fys.camfeedprocessing.util.LoggerUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public final class OcrDetector {

    public static final String IMAGE_BASE64_PREFIX = "data:image/png;base64,";
    public static final String PARKING_SLOT_DELIMITER = "P";

    private static final Logger LOG = LogManager.getLogger(OcrDetector.class);

    private final Environment env;
    private final RestTemplate restTemplate;
    private final BlockingQueue<ImageFrameParkingSlotBean> parkingDataQueue;
    private BlockingQueue<ImageFrame> frameQueue;

    private static MultiValueMap<String, String> STATIC_FORM_DATA_MAP;

    static {
        STATIC_FORM_DATA_MAP = new LinkedMultiValueMap<>();
        STATIC_FORM_DATA_MAP.add("language", "eng");
        STATIC_FORM_DATA_MAP.add("isCreateSearchablePdf", "false");
        STATIC_FORM_DATA_MAP.add("isSearchablePdfHideTextLayer", "false");
        STATIC_FORM_DATA_MAP.add("OCREngine","2");
    }

    public OcrDetector(Environment env, RestTemplate restTemplate,
                       BlockingQueue<ImageFrame> frameQueue, BlockingQueue<ImageFrameParkingSlotBean> parkingDataQueue) {
        this.env = env;
        this.restTemplate = restTemplate;
        this.frameQueue = frameQueue;
        this.parkingDataQueue = parkingDataQueue;
    }

    //Only For Testing
    public OcrDetector(){
        env = null;
        restTemplate = null;
        parkingDataQueue = null;
    }

    public void detect() {

        StopWatch timerStarted = LoggerUtil.createTimerStarted();
        ImageFrame imageFrame = null;


        try {
            imageFrame = frameQueue.take();
            int frameCount = imageFrame.getFrameCount();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>(STATIC_FORM_DATA_MAP);
            map.add("apikey", env.getProperty("ocr.apikey"));
            map.add("base64Image", new StringBuilder().append(IMAGE_BASE64_PREFIX).append(imageFrame.getBase64EncodedString()).toString());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<OcrDetectionResponse> response =
                    restTemplate.postForEntity(env.getProperty("ocr.apihost"), request, OcrDetectionResponse.class);

            if (response.getStatusCode() == HttpStatus.OK
                    && !response.getBody().getIsErroredOnProcessing()) {
                List<Integer> parkingSlotNumbers = getParkingSlotNumbers(frameCount,
                        response.getBody().getParsedText());
                LoggerUtil.info(LOG, "OcrDetector#detect", "Parking Slot Numbers For Frame:"
                        + frameCount + " -> " + parkingSlotNumbers);

                parkingDataQueue.put(new ImageFrameParkingSlotBean(frameCount
                        , ParkingSlot.createParkingSlots(parkingSlotNumbers)));

                LoggerUtil.logTimeAndStop(timerStarted
                        , LOG, "OcrDetector#detect() For frame:" + frameCount);
            }
        }catch (Exception e) {
            if(imageFrame != null) {
                LoggerUtil.error(LOG,"OcrDetector#detect"
                        , "Error Processing Frame while OCR Detect in OcrDetector#detect() For Frame: " + imageFrame.getFrameCount(), e);
                LoggerUtil.logTimeAndStop(timerStarted
                        , LOG, "OcrDetector#detect() For frame:" + imageFrame.getFrameCount());
            }
        }
    }

    private List<Integer> getParkingSlotNumbers(int frameCount, List<String> ocrParsedTexts){
        List<Integer> iSlots = new ArrayList<>();
        for(String ocrParsedText : ocrParsedTexts) {
            ocrParsedText = ocrParsedText.toUpperCase();
            String slots[] = ocrParsedText.split(PARKING_SLOT_DELIMITER);
            for (String slot : slots) {
                String modifiedSlot = doHackToGetParkingSlotNumber(slot);
                try {
                    int iSlot = Integer.parseInt(modifiedSlot);
                    iSlots.add(iSlot);
                } catch (NumberFormatException e) {
                    LoggerUtil.info(LOG, "OcrDetector#getParkingSlotNumbers", "Frame:" + frameCount
                            + " Ignoring the String From OCR ParsedText: " + slot + ", Please analyse to make Parking slot number readable");
                    continue;
                }
            }
        }
        return iSlots;
    }

    public static void main(String[] args) {
        System.out.println("Slots: " + new OcrDetector().getParkingSlotNumbers(1,
                Arrays.asList("2P\\n3P 4P 5P 6P 7P\\n9P 10P 11P 12P S 14P\\n8P")));
    }

    private String doHackToGetParkingSlotNumber(String slot){
        slot = slot.trim();
        slot = slot.replaceAll("[^0-9]+", " ");
        List<String> stringList = Arrays.asList(slot.trim().split(" "));
        //slot = slot.replace("H", "4");
        //slot = slot.replace("G", "6");
        return stringList.get(stringList.size()-1);
    }
}
