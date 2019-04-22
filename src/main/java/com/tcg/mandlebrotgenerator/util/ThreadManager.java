package com.tcg.mandlebrotgenerator.util;

import com.tcg.mandlebrotgenerator.mandlebrot.MandlebrotZoomParams;
import com.tcg.mandlebrotgenerator.mandlebrot.MandlebrotZoomThread;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;

public class ThreadManager {

    private Queue<MandlebrotZoomParams> mandlebrotZoomParamsQueue;
    private double centerX;
    private double centerY;
    private double initialRealSize;
    private double sizeScale;
    private int iterations;
    private int numFrames;
    private String directory;

    private static final int NUM_THREADS = 4;
    private MandlebrotZoomThread[] threads;

    public ThreadManager(double centerX, double centerY, double initialRealSize, double sizeScale, int iterations, String directory, int numFrames) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.initialRealSize = initialRealSize;
        this.sizeScale = sizeScale;
        this.iterations = iterations;
        this.directory = directory + File.separator;
        this.numFrames = numFrames;
        generateQueue();
        threads = new MandlebrotZoomThread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            dequeueAndRun(i);
        }
    }

    private void generateQueue() {
        mandlebrotZoomParamsQueue = new ArrayDeque<>();
        double currentRealSize = initialRealSize;
        for (int i = 0; i < numFrames; i++) {
            MandlebrotZoomParams params = new MandlebrotZoomParams();
            params.centerX = this.centerX;
            params.centerY = this.centerY;
            params.iterations = this.iterations;
            params.directory = this.directory;
            params.fileNum = i;
            params.realSize = currentRealSize;
            currentRealSize *= sizeScale;
            mandlebrotZoomParamsQueue.add(params);
        }
    }

    private void dequeueAndRun(int index) {
        if(mandlebrotZoomParamsQueue.isEmpty()) return;
        MandlebrotZoomParams params = mandlebrotZoomParamsQueue.remove();
        System.out.printf("Enqueuing image %d into thread %d, queue size: %d\n", params.fileNum, index, mandlebrotZoomParamsQueue.size());
        MandlebrotZoomThread mandThread = new MandlebrotZoomThread(index, params);
        mandThread.setZoomCompletion(thread -> {
            System.out.printf("Thread %d is done!\n", thread.id);
            dequeueAndRun(thread.id);
        });
        mandThread.setOnPostProgress(thread -> {
            double percentage = ((double) thread.getPixelsComplete() / thread.getTotalPixels()) * 100;
//            System.out.printf("%d: %.2f%%\n", thread.id, percentage);
        });
        threads[index] = mandThread;
        mandThread.start();
    }

}
