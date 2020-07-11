package com.fys.camfeedprocessing.thread;

import com.fys.camfeedprocessing.thread.impl.CamFeedToFrameProducer;

public class FrameProducerTask implements Runnable{

    private CamFeedToFrameProducer camFeedToFrameProducer;

    public FrameProducerTask(CamFeedToFrameProducer camFeedToFrameProducer) {
        this.camFeedToFrameProducer = camFeedToFrameProducer;
    }

    @Override
    public void run() {
        camFeedToFrameProducer.convert();
    }
}
