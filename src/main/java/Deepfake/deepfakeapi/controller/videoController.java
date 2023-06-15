package Deepfake.deepfakeapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.jcodec.api.FrameGrab;
import org.jcodec.codecs.h264.H264Decoder;
import org.jcodec.common.Codec;
import org.jcodec.common.io.NIOUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;

@Slf4j
@Controller
public class VideoController {
    @Async
    public void videoFrame(String filePath, String filename) throws Exception{
        /*
                동영상의 경우 이미지 추출
        */
        VideoConvertor videoConvertor = new VideoConvertor();
        File videoSource = new File(filePath);
        double videoDuration = extractDuration(videoSource);
        videoConvertor.getImageFrames(videoDuration, videoSource, filename);
    }

    public double extractDuration(File videoSource) throws IOException {
        try{
            FrameGrab frameGrab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(videoSource));
            double durationInSeconds = frameGrab.getVideoTrack().getMeta().getTotalDuration();
            log.info("video duration: {} seconds", durationInSeconds);
            return durationInSeconds;
        }catch(Exception e){
            log.warn("duration extraction fail");
        }
        return 0;
    }
}
