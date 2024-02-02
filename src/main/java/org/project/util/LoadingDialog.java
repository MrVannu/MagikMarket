package org.project.util;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.concurrent.CompletableFuture;

public class LoadingDialog {
    private static final String LOADING_MESSAGE = "LOADING";

    public static void showLoadingAlert(Stage primaryStage) {
        Alert loadingAlert = new Alert(Alert.AlertType.NONE, LOADING_MESSAGE, ButtonType.CANCEL);
        loadingAlert.initStyle(javafx.stage.StageStyle.UNDECORATED);

        StackPane stackPane = new StackPane();
        ProgressIndicator progressIndicator = new ProgressIndicator();

        stackPane.getChildren().addAll(progressIndicator);
        loadingAlert.getDialogPane().setContent(stackPane);

        stackPane.setStyle("-fx-background-color: white; -fx-padding: 10px;");
        progressIndicator.setStyle("-fx-progress-color: #3498db;");

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), stackPane);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        loadingAlert.show();

        // Get the primary screen dimensions
        double centerX = primaryStage.getX() + primaryStage.getWidth() / 2;
        double centerY = primaryStage.getY() + primaryStage.getHeight() / 2;
        loadingAlert.setX(centerX - loadingAlert.getWidth() / 2);
        loadingAlert.setY(centerY - loadingAlert.getHeight() / 2);

        fadeTransition.play();


        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(2000);

                Platform.runLater(() -> {
                    fadeTransition.setOnFinished(event -> {
                        loadingAlert.setResult(ButtonType.CANCEL);
                        loadingAlert.close();
                    });
                    fadeTransition.setRate(-1.0);
                    fadeTransition.play();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
