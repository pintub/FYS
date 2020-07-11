# FYS-camera-feed-processing-service
Analyses the parking slot live video feed and tells if a slot is vacant or not

# KNOWN ISSUE:
To debug javaCV dependency issue, Add Sys property: -Dorg.bytedeco.javacpp.logger.debug=true

# SETUP:
- Create C:\FYS\logs<BR/>
- Create C:\FYS\VideoFrames_TODO_DELETE<BR/>
- Add ffmpeg-4.2.1-1.5.2-windows-x86_64.jar to java.library.path(i.e. Env variable Path)<BR/>
- For Video Stream, Use VLC as RTSP-server out of downloaded video (Please refer: https://www.youtube.com/watch?v=kfCKSIrIGlY)

# OPEN ISSUE:
- With More OCR Thread, and No(Less) sleep time between them,FYS Svc Client Frames post out-of-Order, cause OCR API response time varies(Can be handled with Data-loss,few intermediate frames will be skipped) ,More Real-Time<BR/>
**VS**<BR/>
With More OCR Thread, and More sleep time between them,FYS Svc Client post Frames in-Order,Less Real-Time<BR/>
**VS**<BR/>
1 OCR Thread, Lag between FYS Svc call and Frame Reading is too much

 For Now, Going with First Approach(4 threads + 300 Millisec sleep time between OCR threads)


- OCR Detection Delay/Inefficiency Issue