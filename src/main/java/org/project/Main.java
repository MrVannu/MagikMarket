package org.project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Titolo
        Text title = new Text("Welcome to Magik Market");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Bottoni
        Button button1 = new Button("First");
        Button button2 = new Button("Second");

        // Layout
        StackPane layout = new StackPane();
        layout.getChildren().addAll(title, button1, button2);

        // Scena
        Scene scene = new Scene(layout, 400, 300);

        // Impostazioni della finestra
        primaryStage.setTitle("Interface JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
