package com.fys.camfeedprocessing.util;

import com.fys.camfeedprocessing.bean.ImageFrameParkingSlotBean;

import java.util.Comparator;

public class ImageFrameParkingSlotBeanComparator implements Comparator<ImageFrameParkingSlotBean> {

    @Override
    public int compare(ImageFrameParkingSlotBean bean1, ImageFrameParkingSlotBean bean2) {
        return Integer.compare(bean1.getFrameCount(), bean2.getFrameCount());
    }
}
