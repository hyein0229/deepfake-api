package Deepfake.deepfakeapi.controller;

import java.io.File;

public class VideoConvertor {

    public void getImageFrames(File video) throws Exception {
        double plusSize = 0.5;
        int threadSize = 8;

        VideoThread[] videoThreads = new VideoThread[threadSize];

        for(int i=0; i<videoThreads.length; i++){
            videoThreads[i] = new VideoThread(video, threadSize, i, plusSize);
            videoThreads[i].start();
        }

        boolean isContinued = true;
        while(isContinued){
            Thread.sleep(1000);

            isContinued = false;
            for(int i=0; i<threadSize; i++){
                if(videoThreads[i].isAlive()){
                    isContinued = true;
                }
            }
        }
    }
}
