package com.tcg.mandlebrotgenerator.mandlebrot;

import com.tcg.mandlebrotgenerator.util.ComplexNumber;
import com.tcg.mandlebrotgenerator.util.Helpers;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class MandlebrotZoomThread extends Thread {

    private int pixelWidth;
    private int pixelHeight;
    private int iterations;

    private double centerX;
    private double centerY;
    private double realSize;

    private int fileNum;
    private String directory;

    private Color zeroColor;
    private Color endColor;

    private long totalPixels;
    private long pixelsComplete;

    public final int id;

    private MandlebrotZoomAction zoomCompletion;
    private MandlebrotZoomAction onPostProgress;

    private WritableImage wim;

    private boolean stop;

    public MandlebrotZoomThread(int id, MandlebrotZoomParams mandlebrotZoomParams) {
        this.id = id;
        this.pixelWidth = mandlebrotZoomParams.width;
        this.pixelHeight = mandlebrotZoomParams.height;
        this.iterations = mandlebrotZoomParams.iterations;
        this.centerX = mandlebrotZoomParams.centerX;
        this.centerY = mandlebrotZoomParams.centerY;
        this.realSize = mandlebrotZoomParams.realSize;
        this.fileNum = mandlebrotZoomParams.fileNum;
        this.directory = mandlebrotZoomParams.directory;
        this.zeroColor = mandlebrotZoomParams.zeroColor;
        this.endColor = mandlebrotZoomParams.endColor;
        this.stop = false;

        wim = new WritableImage(this.pixelWidth, this.pixelHeight);

        totalPixels = pixelWidth * pixelHeight;
        pixelsComplete = 0;

    }

    public void setZoomCompletion(MandlebrotZoomAction zoomCompletion) {
        this.zoomCompletion = zoomCompletion;
    }

    public void setOnPostProgress(MandlebrotZoomAction onPostProgress) {
        this.onPostProgress = onPostProgress;
    }

    @Override
    public void run() {
        for (int x = 0; x < pixelWidth && !stop; x++) {
            for (int y = 0; y < pixelHeight && !stop; y++) {
                ComplexNumber z0 = Helpers.mapPixel(x, y, pixelWidth, pixelHeight, centerX, centerY, realSize);

                int mandlebrot = mandlebrot(z0, iterations);
                Color color = zeroColor.interpolate(endColor, (double) mandlebrot / iterations);
                wim.getPixelWriter().setColor(x, y, color);
                pixelsComplete++;
            }
            if(onPostProgress != null) onPostProgress.act(this);
        }
        if(!stop) {
            try {
                File imageFile = new File(directory + String.format("%03d.png", fileNum));
                ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", imageFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (zoomCompletion != null) {
                zoomCompletion.act(this);
            }
        }
    }

    public void stopZoom() {
        this.stop = true;
    }

    private static int mandlebrot(ComplexNumber z0, int maxIterations) {
        double maxAbs = ComplexNumber.of(2, 2).abs() * 2;
        ComplexNumber z = ComplexNumber.of(z0);
        for(int i = 0; i < maxIterations; i++) {
            if(z.abs() > maxAbs) {
                return i;
            }
            z = z.mult(z).add(z0);
        }
        return maxIterations;
    }

    public long getTotalPixels() {
        return totalPixels;
    }

    public long getPixelsComplete() {
        return pixelsComplete;
    }

    @FunctionalInterface
    public interface MandlebrotZoomAction {

        void act(MandlebrotZoomThread thread);

    }

}
