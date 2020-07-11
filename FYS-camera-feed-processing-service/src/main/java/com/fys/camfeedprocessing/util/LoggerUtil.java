package com.fys.camfeedprocessing.util;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.Logger;

public final class LoggerUtil {
    public static StopWatch createTimerStarted() {
        StopWatch watch = new StopWatch();
        watch.start();
        return watch;
    }

    public static void logTimeAndStop(final StopWatch watch, final Logger logger, final String apiName) {
        if (watch != null) {
            watch.stop();
            logger.info("{API= " + apiName + "," + "CPU_TIME= " + watch.getTime() + "MilliSecs}");
        }
    }

    public static void info(final Logger logger, String api, String message){
        logger.info(getLog(api, message));
    }

    public static void info(int frameCount, final Logger logger, String api, String message){
        logger.info(getLog(frameCount, api, message));
    }

    public static void info(final Logger logger, String api, String message, Throwable e){
        logger.error(getLog(api, message), e);
    }

    public static void error(final Logger logger, String api, String message, Throwable e){
        logger.error(getLog(api, message), e);
    }

    private static String getLog(String api, String message){
        return "{API= " + api + "," + "MESSAGE= " + message + "}";
    }

    private static String getLog(int frameCount, String api, String message){
        return "{API= " + api + "," + "FRAME= " + frameCount + "," + "MESSAGE= " + message + "}";
    }

    public static void error(final Logger logger, String api, String message) {
        logger.error(getLog(api, message));
    }

    public static void error(int frameCount, final Logger logger, String api, String message) {
        logger.error(getLog(frameCount, api, message));
    }
}
