package com.tcg.mandlebrotgenerator.mandlebrot;

import javafx.scene.paint.Color;

public final class MandlebrotZoomParams {

    public int width = 2000;
    public int height = 2000;
    public int iterations = 1000;

    public double centerX = 0;
    public double centerY = 0;
    public double realSize = 4f;

    public int fileNum = 0;
    public String directory;

    public Color zeroColor = Color.BLACK;
    public Color endColor = Color.GREEN;

}
