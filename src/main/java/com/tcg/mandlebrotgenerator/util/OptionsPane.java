package com.tcg.mandlebrotgenerator.util;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class OptionsPane extends GridPane {

    private TextField threadsField;
    private int threads;

    private TextField centerXField;
    private double centerX;

    private TextField centerYField;
    private double centerY;

    private TextField initialRealSizeField;
    private double initialRealSize;

    private TextField iterationsField;
    private int iterations;

    private TextField numFramesField;
    private int numFrames;

    private OnStartPressListener onPress;

    public OptionsPane() {
        super();
        setHgap(5);
        setVgap(5);
        setPadding(new Insets(10));

        initThreadsField();
        initCenterXField();
        initCenterYField();
        initInitialRealSizeField();
        initIterationsField();
        initNumFramesField();

        Button button = new Button("Start");
        button.setOnAction(event -> {
            if(onPress != null) {
                onPress.onStartPress(this.threads, this.centerX, this.centerY, this.initialRealSize, this.iterations, this.numFrames, "test");
            }
        });
        add(button, 0, 6);
    }

    private void initNumFramesField() {
        this.numFrames = 100;
        this.numFramesField = new TextField(String.valueOf(numFrames));
        this.numFramesField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    this.numFrames = Integer.parseInt(newValue);
                } catch (NumberFormatException ignored) {
                }
                this.numFramesField.setText(String.valueOf(this.numFrames));
            } else {
                this.numFrames = 100;
            }
        });
        add(new Label("Number of Frames:"), 0 , 5);
        add(numFramesField, 1, 5);
    }

    private void initIterationsField() {
        this.iterations = 255;
        this.iterationsField = new TextField(String.valueOf(iterations));
        this.iterationsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    this.iterations = Integer.parseInt(newValue);
                } catch (NumberFormatException ignored) {
                }
                this.iterationsField.setText(String.valueOf(this.iterations));
            } else {
                this.iterations = 255;
            }
        });
        add(new Label("Iterations:"), 0 , 4);
        add(iterationsField, 1, 4);
    }

    private void initInitialRealSizeField() {
        this.initialRealSize = 4.0;
        this.initialRealSizeField = new TextField(String.valueOf(initialRealSize));
        this.initialRealSizeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    this.initialRealSize = Double.parseDouble(newValue);
                } catch (NumberFormatException ignored) {
                }
                this.initialRealSizeField.setText(String.valueOf(this.initialRealSize));
            } else {
                this.initialRealSize = 4.0;
            }
        });
        add(new Label("Initial Size:"), 0, 3);
        add(this.initialRealSizeField, 1, 3);
    }

    private void initCenterYField() {
        this.centerY = 0.0;
        this.centerYField = new TextField(String.valueOf(centerY));
        this.centerYField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    this.centerY = Double.parseDouble(newValue);
                } catch (NumberFormatException ignored) {
                }
                this.centerYField.setText(String.valueOf(this.centerY));
            } else {
                this.centerY = 0;
            }
        });
        add(new Label("Center Y:"), 0, 2);
        add(this.centerYField, 1, 2);
    }

    private void initCenterXField() {
        this.centerX = 0.0;
        this.centerXField = new TextField(String.valueOf(centerX));
        this.centerXField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    this.centerX = Double.parseDouble(newValue);
                } catch (NumberFormatException ignored) {
                }
                this.centerXField.setText(String.valueOf(this.centerX));
            } else {
                this.centerX = 0;
            }
        });
        add(new Label("Center X:"), 0, 1);
        add(this.centerXField, 1, 1);
    }

    private void initThreadsField() {
        this.threads = 3 * Runtime.getRuntime().availableProcessors() / 4;
        this.threadsField = new TextField(String.valueOf(threads));
        threadsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    this.threads = Integer.parseInt(newValue);
                } catch (NumberFormatException ignored) {
                }
                this.threadsField.setText(String.valueOf(threads));
            } else {
                this.threads = 3 * Runtime.getRuntime().availableProcessors() / 4;
            }
        });
        add(new Label("Threads:"), 0, 0);
        add(this.threadsField, 1, 0);
    }

    public void setOnPress(OnStartPressListener onPress) {
        this.onPress = onPress;
    }

    @FunctionalInterface
    public interface OnStartPressListener {
        void onStartPress(int threads, double centerX, double centerY, double initialRealSize, int iterations, int numFrames, String directory);
    }

}
