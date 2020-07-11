package com.fys.camfeedprocessing.thread;

import com.fys.camfeedprocessing.thread.impl.OcrDetector;

public class FrameConsumerVacantParkingSlotProducerTask implements Runnable{

    private final OcrDetector ocrDetector;

    public FrameConsumerVacantParkingSlotProducerTask(OcrDetector ocrDetector){
        this.ocrDetector = ocrDetector;
    }

    @Override
    public void run() {
        while (true) {
            ocrDetector.detect();
        }
    }
}
