package com.fys.camfeedprocessing.bean;

import java.util.List;

public final class ImageFrameParkingSlotBean {
    private int frameCount;
    private List<ParkingSlot> parkingSlotList;

    public ImageFrameParkingSlotBean(int frameCount, List<ParkingSlot> parkingSlotList) {
        this.frameCount = frameCount;
        this.parkingSlotList = parkingSlotList;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public List<ParkingSlot> getParkingSlotList() {
        return parkingSlotList;
    }
}
