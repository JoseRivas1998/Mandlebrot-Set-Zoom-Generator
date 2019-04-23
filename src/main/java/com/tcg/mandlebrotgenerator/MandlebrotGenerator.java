package com.tcg.mandlebrotgenerator;

import com.tcg.mandlebrotgenerator.mandlebrot.MandlebrotZoomParams;
import com.tcg.mandlebrotgenerator.mandlebrot.MandlebrotZoomThread;
import com.tcg.mandlebrotgenerator.util.OptionsPane;
import com.tcg.mandlebrotgenerator.util.ThreadManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MandlebrotGenerator extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        ThreadManager threadManager = new ThreadManager(-0.6506400014677644, 0.372903024192037, 4, 0.95, 1000, "test", 700);
        StackPane mainScene = new StackPane();
        OptionsPane options = new OptionsPane();
        Scene scene = new Scene(mainScene);
        options.setOnPress((threads, centerX, centerY, initialRealSize, iterations, numFrames, directory) -> {
            ThreadManager threadManager = new ThreadManager(threads, centerX, centerY, initialRealSize, 0.9, iterations, directory, numFrames);
            mainScene.getChildren().clear();
            mainScene.getChildren().add(threadManager);
            primaryStage.sizeToScene();
            Platform.runLater(threadManager::start);
        });
        mainScene.getChildren().add(options);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
