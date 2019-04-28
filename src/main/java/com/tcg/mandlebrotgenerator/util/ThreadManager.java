package com.tcg.mandlebrotgenerator.util;

import com.tcg.mandlebrotgenerator.mandlebrot.MandlebrotZoomParams;
import com.tcg.mandlebrotgenerator.mandlebrot.MandlebrotZoomThread;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;

public class ThreadManager extends GridPane {

    private Queue<MandlebrotZoomParams> mandlebrotZoomParamsQueue;
    private double centerX;
    private double centerY;
    private double initialRealSize;
    private double sizeScale;
    private int iterations;
    private int numFrames;
    private String directory;

    private int completeThreads;

    private final int NUM_THREADS;
    private MandlebrotZoomThread[] threads;

    private ProgressBar[] progressBars;
    private Label[] imageNumbers;
    private ProgressBar completeProgressBar;
    private Label completeProgressLabel;

    long startTime;

    private ZoomCompeteListener zoomCompeteListener;

    public ThreadManager(double centerX, double centerY, double initialRealSize, double sizeScale, int iterations, String directory, int numFrames) {
        this(3 * Runtime.getRuntime().availableProcessors() / 4, centerX, centerY, initialRealSize, sizeScale, iterations, directory, numFrames);
    }

    public ThreadManager(int numThreads, double centerX, double centerY, double initialRealSize, double sizeScale, int iterations, String directory, int numFrames) {
        super();
        this.NUM_THREADS = Math.min(numThreads, numFrames);
        this.centerX = centerX;
        this.centerY = centerY;
        this.initialRealSize = initialRealSize;
        this.sizeScale = sizeScale;
        this.iterations = iterations;
        this.directory = directory + File.separator;
        this.numFrames = numFrames;
        this.completeThreads = 0;

        startTime = System.currentTimeMillis();

        setHgap(5);
        setVgap(5);
        setPadding(new Insets(10));

        initProgressBars();
        generateQueue();

        Button cancel = new Button();
        cancel.setOnAction(event -> {
            stopAll();
            if(zoomCompeteListener != null) {
                zoomCompeteListener.onComplete(this, true);
            }
        });

        threads = new MandlebrotZoomThread[NUM_THREADS];
    }

    public void start() {
        for (int i = 0; i < NUM_THREADS; i++) {
            dequeueAndRun(i);
        }
    }

    private void initProgressBars() {
        progressBars = new ProgressBar[NUM_THREADS];
        imageNumbers = new Label[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            progressBars[i] = new ProgressBar();
            imageNumbers[i] = new Label("Image 000");
            add(new Label("Thread " + i), 0, i);
            add(progressBars[i], 1, i);
            add(imageNumbers[i], 2, i);
        }
        completeProgressLabel = new Label();
        completeProgressBar = new ProgressBar();
        updateCompleteProgressBar();
        add(new Label("Overall:"), 0, NUM_THREADS);
        add(completeProgressBar, 1, NUM_THREADS);
        add(completeProgressLabel, 2, NUM_THREADS);
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
        if (mandlebrotZoomParamsQueue.isEmpty()) {
            completeThreads++;
            if(completeThreads == NUM_THREADS) {
                if(zoomCompeteListener != null){
                    System.out.println("Test");
                    Platform.runLater(() -> zoomCompeteListener.onComplete(this, false));
                }
            }
            return;
        }
        MandlebrotZoomParams params = mandlebrotZoomParamsQueue.remove();
        if (params.fileNum == 50) {
            double time = System.currentTimeMillis() - startTime;
            time /= 1000;
            System.out.println("Time to 50: " + time);
        }
        MandlebrotZoomThread mandThread = new MandlebrotZoomThread(index, params);
        mandThread.setZoomCompletion(thread -> {
            dequeueAndRun(thread.id);
            updateCompleteProgressBar();
        });
        mandThread.setOnPostProgress(thread -> {
            updateProgressBar(thread.id);
        });
        threads[index] = mandThread;
        updateImageLabel(index, params.fileNum);
        mandThread.start();
    }

    private void updateImageLabel(int index, int fileNum) {
        Platform.runLater(() -> {
            imageNumbers[index].setText(String.format("Image %03d", fileNum));
        });
    }

    public void updateProgressBar(int index) {
        Platform.runLater(() -> {
            MandlebrotZoomThread zoomThread = threads[index];
            progressBars[index].progressProperty().setValue(
                    ((double) zoomThread.getPixelsComplete() / zoomThread.getTotalPixels())
            );
        });
    }

    private void updateCompleteProgressBar() {
        Platform.runLater(() -> {
            int workingThreads = NUM_THREADS - completeThreads;
            double framesCompleted = this.numFrames - mandlebrotZoomParamsQueue.size() - workingThreads;
            completeProgressBar.setProgress(framesCompleted / this.numFrames);
            completeProgressLabel.setText(String.format("%d/%d", Math.max((int) framesCompleted, 0), this.numFrames));
        });
    }

    public void stopAll() {
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i].stopZoom();
        }
        mandlebrotZoomParamsQueue.clear();
    }

    public void setZoomCompeteListener(ZoomCompeteListener zoomCompeteListener) {
        this.zoomCompeteListener = zoomCompeteListener;
    }

    @FunctionalInterface
    public interface ZoomCompeteListener {
        void onComplete(ThreadManager threadManager, boolean canceled);
    }

}
