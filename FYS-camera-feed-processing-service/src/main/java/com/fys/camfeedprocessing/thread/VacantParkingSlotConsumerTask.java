package com.fys.camfeedprocessing.thread;

import com.fys.camfeedprocessing.thread.impl.FysUpdateServiceClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VacantParkingSlotConsumerTask implements Runnable{

    private final FysUpdateServiceClient fysUpdateServiceClient;
    private static final Logger LOG = LogManager.getLogger(VacantParkingSlotConsumerTask.class);

    public VacantParkingSlotConsumerTask(FysUpdateServiceClient fysUpdateServiceClient) {
        this.fysUpdateServiceClient = fysUpdateServiceClient;
    }

    @Override
    public void run() {
        while (true) {
            fysUpdateServiceClient.post();
        }
    }
}
