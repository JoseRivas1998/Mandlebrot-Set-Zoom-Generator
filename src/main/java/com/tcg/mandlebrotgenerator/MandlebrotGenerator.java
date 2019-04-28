package com.tcg.mandlebrotgenerator;

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
        StackPane mainScene = new StackPane();
        OptionsPane options = new OptionsPane();
        Scene scene = new Scene(mainScene);
        options.setOnPress((threads, centerX, centerY, initialRealSize, sizeScale, iterations, numFrames, directory) -> {
            ThreadManager threadManager = new ThreadManager(threads, centerX, centerY, initialRealSize, sizeScale, iterations, directory, numFrames);
            mainScene.getChildren().clear();
            mainScene.getChildren().add(threadManager);
            primaryStage.sizeToScene();
            primaryStage.setOnCloseRequest(e -> threadManager.stopAll());
            threadManager.setZoomCompeteListener((threadManager1, canceled) -> {
                primaryStage.setOnCloseRequest(null);
                mainScene.getChildren().clear();
                mainScene.getChildren().add(options);
                primaryStage.sizeToScene();
            });
            Platform.runLater(threadManager::start);
        });
        mainScene.getChildren().add(options);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
