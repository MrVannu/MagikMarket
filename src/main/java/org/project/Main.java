package org.project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Title
        Text title = new Text("Login");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Textfields
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");

        // Create a TextField for the username with a maximum length of 15 characters
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username (max 15 characters)");
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 15) {
                usernameField.setText(oldValue); // Prevent entering more than 15 characters
            }
        });

        // Create a PasswordField for the password with a maximum length of 15 characters
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password (max 15 characters)");
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 15) {
                passwordField.setText(oldValue); // Prevent entering more than 15 characters
            }
        });

        // Button -> login
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Authentication logic
            // Db

            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
        });

        // Window layout
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(20));
        layout.setHgap(10);
        layout.setVgap(10);

        layout.add(title, 0, 0, 2, 1);
        layout.add(usernameLabel, 0, 1);
        layout.add(usernameField, 1, 1);
        layout.add(passwordLabel, 0, 2);
        layout.add(passwordField, 1, 2);
        layout.add(loginButton, 1, 3);

        Scene scene = new Scene(layout, 300, 200);

        // Options of window
        primaryStage.setTitle("Login Form");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        //APIData testObj = new APIData();
        //testObj.fetchData();
        //double result = testObj.maxAge();
        //System.out.println(result);

        launch(args);
    }
}



/*import javafx.application.Application;
        import javafx.scene.Scene;
        import javafx.scene.chart.LineChart;
        import javafx.scene.chart.NumberAxis;
        import javafx.scene.chart.XYChart;
        import javafx.scene.control.PasswordField;
        import javafx.scene.control.TextField;
        import javafx.scene.layout.VBox;
        import javafx.stage.Stage;

public class SecureLoginFormWithChartApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Secure Login Form with Chart");

        // Create a TextField for the username with a maximum length of 15 characters
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username (max 15 characters)");
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 15) {
                usernameField.setText(oldValue); // Prevent entering more than 15 characters
            }
        });

        // Create a PasswordField for the password with a maximum length of 15 characters
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password (max 15 characters)");
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 15) {
                passwordField.setText(oldValue); // Prevent entering more than 15 characters
            }
        });

        // Create a line chart with sample data
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Sample Chart");
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Sample Data");
        series.getData().addAll(
                new XYChart.Data<>(1, 10),
                new XYChart.Data<>(2, 15),
                new XYChart.Data<>(3, 8),
                new XYChart.Data<>(4, 20),
                new XYChart.Data<>(5, 12)
        );
        lineChart.getData().add(series);

        // Create a layout and add the components
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(usernameField, passwordField, lineChart);

        // Set the scene
        Scene scene = new Scene(vbox, 500, 400);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}*/
