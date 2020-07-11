package com.fys.camfeedprocessing;

import com.fys.camfeedprocessing.service.CamFeedProcessor;
import com.fys.camfeedprocessing.util.FileType;
import com.fys.camfeedprocessing.util.LoggerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FysCameraFeedProcessingServiceStarter implements CommandLineRunner {

    private static final Logger LOG = LogManager.getLogger(FysCameraFeedProcessingServiceStarter.class);

    @Autowired
    private CamFeedProcessor processor;

    /**
     * @param args
     * @throws Exception
     * Invokes CamFeedProcessor
     */
    @Override
    public void run(String... args) throws Exception {
        LoggerUtil.info(LOG,"FysCameraFeedProcessingServiceStarter#run", "Started");

        //Local File
        //processor.process("FinalVid.mp4", FileType.LOCAL_FILE);

        //HTTP File
        //processor.process("https://dev.exiv2.org/attachments/download/372/3D_L0064.MP4", FileType.HTTP_FILE);

        //RTSP liveStream
        processor.process("rtsp://localhost:8554/stream", FileType.RTSP_STREAM);
    }
}
