package Deepfake.deepfakeapi.controller;

import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.File;

public class VideoThread extends Thread {
    private int threadNo;
    private int threadSize;
    private double plusSize;
    private File video;

    public VideoThread(File video, int threadSize, int threadNo, double plusSize){
        this.video = video;
        this.threadSize = threadSize;
        this.threadNo = threadNo;
        this.plusSize = plusSize;
    }

    public void run(){
        FrameGrab frameGrab;

        try{
            frameGrab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(video));
            for(int i=0; i < 120; i++){
                if(i % threadSize == threadNo){
                    double startSec = i * plusSize;
                    System.out.println(threadNo + " " + startSec);

                    int frameCnt = 1;
                    frameGrab.seekToSecondPrecise(startSec);

                    for(int j=0; j<frameCnt; j++){
                        Picture picture = frameGrab.getNativeFrame();

                        BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
                        ImageIO.write(bufferedImage, "png", new File("C:/Users/HAYOUNG LEE/Desktop/deepfake-api/files/video/frame" + i + ".png"));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
