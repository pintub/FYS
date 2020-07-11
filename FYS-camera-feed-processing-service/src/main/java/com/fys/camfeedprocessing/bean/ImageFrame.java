package com.fys.camfeedprocessing.bean;

public final class ImageFrame {

    private int frameCount;
    private String base64EncodedString;

    public ImageFrame(int frameCount, String base64EncodedString){
        this.frameCount = frameCount;
        this.base64EncodedString = base64EncodedString;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public String getBase64EncodedString() {
        return base64EncodedString;
    }
}
