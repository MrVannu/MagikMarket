package org.project;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class WelcomePane {
    Scene WelcomeScene;
    private final String pathUserDB = "src/main/resources/userDB.csv";  // Path to DB for users tracking
    private ArrayList<String> symbols = new ArrayList<String>();


    private final String pathDataHistoryDB = "src/main/resources/dataHistoryDB.csv";  // Path to DB for data history

    // Index of row to be overwritten (most remote in the db)
    private short dataToUpdateIndex = 0;
    public WelcomePane(Stage primaryStage, Scene LoginScene){
        GridPane layoutStartApp = new GridPane();
        WelcomeScene = new Scene(layoutStartApp, 500, 300);
        Text example = new Text("Test for pane");
        Button logOut = new  Button("LogOut");

        symbols.add("amc");
        symbols.add("ape");
        symbols.add("x");
        symbols.add("tsla");
        symbols.add("kvue");
        symbols.add("nio");
        symbols.add("nvda");
        symbols.add("jnj");
        symbols.add("amd");
        symbols.add("f");
        symbols.add("googl");

        layoutStartApp.setPadding(new Insets(20));
        layoutStartApp.setHgap(10);
        layoutStartApp.setVgap(10);
        layoutStartApp.add(example,0,1);
        layoutStartApp.add(logOut, 0,0);
        logOut.setOnAction(e ->{
            primaryStage.setTitle("Login");
            primaryStage.setScene(LoginScene);
        });

        Button test = new Button("Test");
        layoutStartApp.add(test,0,4);
        test.setOnAction(e->{
            APIData testObj = new APIData(symbols.get(1));
            testObj.fetchData(); // WARNING: this line requires API usage

            String p1 = testObj.extractSymbolOfCompany();
            String nameOfCompany = testObj.extractNameOfCompany();
            //String aa= String.valueOf(p1);

            //double p2 = testObj.postMarketChangePercent();
            //String bb = String.valueOf(p2);
            LineChart<Number, Number> lineChart = createLineChart(nameOfCompany);
            layoutStartApp.add(lineChart, 1, 1); //
            try {
                updateDataHistory(pathDataHistoryDB,p1,nameOfCompany);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


    }


    // Data history management
    // Method to update data history database
    public void updateDataHistory(String pathToUse, String a, String b) throws IOException {
        String toWrite = a + "," + b;

        // Read all lines from the file
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToUse))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        // Pad the list with empty lines up to 20 elements
        while (lines.size() < 20) {
            lines.add("");
        }

        // Update the data at the specified index
        lines.set(dataToUpdateIndex, toWrite);
        // Write the modified lines back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToUse))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        }

        ++dataToUpdateIndex; // Increment the counter of the most remote index
        if(dataToUpdateIndex>=20)dataToUpdateIndex=0;
        System.out.println(dataToUpdateIndex);
    }

    //Method to add Line Chart
    private LineChart<Number, Number> createLineChart(String nameOfCompany) {
        // Create X and Y axes for the line chart
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        // Create the line chart with axes
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(nameOfCompany);

        // Create a data series for the line chart
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Data History");

        // Add data points to the series (for demonstration)
        series.getData().add(new XYChart.Data<>(1, 10));
        series.getData().add(new XYChart.Data<>(2, 20));
        series.getData().add(new XYChart.Data<>(3, 15));
        series.getData().add(new XYChart.Data<>(4, 25));
        series.getData().add(new XYChart.Data<>(5, 18));

        // Add the series to the line chart
        lineChart.getData().add(series);

        return lineChart;
    }

    public Scene getScene(){
        return WelcomeScene;
    }
}
