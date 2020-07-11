package com.fys.camfeedprocessing.exception;

public class CamFeedProcessingServiceException extends Exception {

    enum ExceptionCode{
        CAM_FEED_NOT_FOUND_EXCEPTION,
        CAM_FEED_TO_FRAME_CONVERSION_FAILED,
        OCR_DETECTION_FAILED,
        OCR_PARSED_TEXT_READ_FAILED,
        FYS_UPDATE_SERVICE_CALL_FAILED;
    }
}
