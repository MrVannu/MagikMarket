package org.project.util;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.util.concurrent.CompletableFuture;


public class LoadingDialog {
    private static final String LOADING_MESSAGE = "LOADING";

    public static void showLoadingAlert() {
        Stage loadingStage = new Stage(StageStyle.UNDECORATED);
        loadingStage.initModality(Modality.APPLICATION_MODAL);

        Alert loadingAlert = new Alert(Alert.AlertType.NONE, LOADING_MESSAGE, ButtonType.CANCEL);
        loadingAlert.initStyle(StageStyle.UNDECORATED);

        StackPane stackPane = new StackPane();
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle("-fx-progress-color: black;");

        Rectangle background = new Rectangle(200, 100);
        background.setFill(Color.WHITE);

        stackPane.getChildren().addAll(background, progressIndicator);
        stackPane.setAlignment(Pos.CENTER);

        loadingAlert.getDialogPane().setContent(stackPane);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), stackPane);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        centerOnScreen(loadingStage);

        CompletableFuture<Void> loadingTask = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        loadingAlert.show();
        fadeTransition.play();

        loadingTask.thenRunAsync(() -> {
            Platform.runLater(() -> {
                fadeTransition.setOnFinished(event -> {
                    loadingAlert.setResult(ButtonType.CANCEL);
                    loadingAlert.close();
                });
                fadeTransition.setRate(-1.0);
                fadeTransition.play();
            });
        });
    }

    private static void centerOnScreen(Stage stage) {
        Screen primaryScreen = Screen.getPrimary();
        double centerX = primaryScreen.getVisualBounds().getMinX() + primaryScreen.getVisualBounds().getWidth() / 2;
        double centerY = primaryScreen.getVisualBounds().getMinY() + primaryScreen.getVisualBounds().getHeight() / 2;
        stage.setX(centerX - stage.getWidth() / 2);
        stage.setY(centerY - stage.getHeight() / 2);
    }
}
