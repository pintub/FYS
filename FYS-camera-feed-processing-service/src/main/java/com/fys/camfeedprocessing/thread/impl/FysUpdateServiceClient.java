package com.fys.camfeedprocessing.thread.impl;

import com.fys.camfeedprocessing.bean.ImageFrameParkingSlotBean;
import com.fys.camfeedprocessing.bean.ParkingSlot;
import com.fys.camfeedprocessing.util.LoggerUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public final class FysUpdateServiceClient {

    private static final Logger LOG = LogManager.getLogger(FysUpdateServiceClient.class);
    private final Environment environment;
    private BlockingQueue<ImageFrameParkingSlotBean> parkingDataQueue;
    private int previousProcessedFrameCount;
    private final RestTemplate restTemplate;

    public FysUpdateServiceClient(Environment environment,
                                  RestTemplate restTemplate, BlockingQueue<ImageFrameParkingSlotBean> parkingDataQueue) {
        this.parkingDataQueue = parkingDataQueue;
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

    public void post(){
        ImageFrameParkingSlotBean frameParkingSlotBean = null;
        StopWatch timerStarted = LoggerUtil.createTimerStarted();

        try {
            frameParkingSlotBean = parkingDataQueue.take();
            int frameCount = frameParkingSlotBean.getFrameCount();
            List<ParkingSlot> parkingSlotList = frameParkingSlotBean.getParkingSlotList();

            if(frameCount < previousProcessedFrameCount){//Cause using LinkedBlockingQueue
                LoggerUtil.info(LOG, "FysUpdateServiceClient#post"
                        , "Skipped Frame:" + frameCount + " ,As a later frame:" + previousProcessedFrameCount + " is already processed");
                return;
            }

            LoggerUtil.info(LOG, "FysUpdateServiceClient#post"
                    , "Sending to FYS Service Frame:" + frameCount + " " +
                            ",Parking Numbers: " + parkingSlotList);
            List<Long> parkingIdList = parkingSlotList.stream().map(parkingSlot -> parkingSlot.getId()).collect(Collectors.toList());
            ResponseEntity<Void> response = restTemplate.postForEntity(environment.getProperty("fys.apihost"), parkingIdList, Void.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                LoggerUtil.error(LOG,"FysUpdateServiceClient#post"
                        , "Error Processing Frame while posting to FYS Frame: " + frameCount );
            }
            previousProcessedFrameCount = frameCount;
            LoggerUtil.logTimeAndStop(timerStarted
                    , LOG, "FysUpdateServiceClient#post() For frame:" + frameCount);
        } catch (Exception e) {
            if(frameParkingSlotBean != null){
                LoggerUtil.error(LOG,"FysUpdateServiceClient#post"
                        , "Error Processing Frame while posting to FYS Frame: " + frameParkingSlotBean.getFrameCount(), e);
                LoggerUtil.logTimeAndStop(timerStarted
                        , LOG, "FysUpdateServiceClient#post() For frame:" + frameParkingSlotBean.getFrameCount());
            }
        }
    }
}
