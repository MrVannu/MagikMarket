package org.project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WelcomePane {
    private static final int MAX_SELECTED_CHECKBOXES = 4;
    Scene WelcomeScene;
    private final String pathUserDB = "src/main/resources/userDB.csv";  // Path to DB for users tracking
    private ArrayList<String> symbols = new ArrayList<String>();
    private final String pathDataHistoryDB = "src/main/resources/dataHistoryDB.csv";  // Path to DB for data history

    // Index of row to be overwritten (most remote in the db)
    private short dataToUpdateIndex = 0;
    private Map<CheckBox, XYChart.Series<Number, Number>> checkBoxSeriesMap;
    public WelcomePane(Stage primaryStage, Scene LoginScene){

        // Defining a list of checkBoxes
        List<CheckBox> checkBoxes = new ArrayList<>();
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

        checkBoxSeriesMap = new HashMap<>();

    // Creating checkboxes
        for (String symbol : symbols) {
            CheckBox checkBox = new CheckBox(symbol);
            checkBox.getStyleClass().add("checkbox");

            // Checkbox creating a new serie for each checkbox
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(symbol); // Set the name of the serie

            // Saving the serie into the map associated with the checkbox
            checkBoxSeriesMap.put(checkBox, series);

            checkBox.setOnAction(event -> {
                //For limiting of the selection of max checboxes
                int selectedCount = (int) checkBoxes.stream().filter(CheckBox::isSelected).count();
                //Adding a limit of selection of "MAX_SELECTED_CHECKBOXES" (for instance  max 4 check boxes)
                if (selectedCount > MAX_SELECTED_CHECKBOXES) {
                    checkBox.setSelected(false);
                }

                //For adding new line
                if (checkBox.isSelected()) {

                        // Adding data into the serie when the checkbox is selected
                        // Add here your data
                    
                        series.getData().add(new XYChart.Data<>(1, 10));
                        series.getData().add(new XYChart.Data<>(2, 20));
                        series.getData().add(new XYChart.Data<>(3, 100));
                        series.getData().add(new XYChart.Data<>(4, 30));

                    } else {
                        // Remove data from the serie when it is deselected
                        series.getData().clear();
                    }
            });

            checkBoxes.add(checkBox);
        }

        // Defining VBox for the right split pane
        VBox rightPane = new VBox();

        // Defining internal panes of the vertical split pane
        StackPane topInternalPane = new StackPane(new Label("Top Internal Pane"));
        StackPane bottomInternalPane = new StackPane(new Label("Bottom Internal Pane"));

        // Defining the new vertical SplitPane
        SplitPane internalVerticalSplitPane = new SplitPane();
        internalVerticalSplitPane.setDividerPositions(0.1); // Configuring SplitPane dimension
        internalVerticalSplitPane.getItems().addAll(topInternalPane, bottomInternalPane);
        internalVerticalSplitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);
        // Adding the new verticalSplitPane to the right pane (of the horizontal splitPane)
        rightPane.getChildren().add(internalVerticalSplitPane);

        VBox.setVgrow(internalVerticalSplitPane, Priority.ALWAYS);


        // Defining VBox for the left split pane and inserting checkBoxes inside
        VBox leftPane = new VBox(10);
        leftPane.setAlignment(Pos.CENTER_LEFT);
        leftPane.setPadding(new Insets(10));
        leftPane.getChildren().addAll(checkBoxes);

        VBox.setVgrow(leftPane, Priority.ALWAYS);
        // Defining SplitPane that contains the left and right panes (that are VBoxes)
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftPane, rightPane);
        splitPane.setDividerPositions(0.2); // Configuring SplitPane dimension

        // Instantiating the whole scene as Scene and defining its dimension
        WelcomeScene = new Scene(splitPane, 800, 600);
        WelcomeScene.getStylesheets().add("styles.css");// Reference to the CSS file

        // Defining logOut button
        Button logOut = new  Button("LogOut");

        // Defining HBox for the bottomInternalPane
        HBox bottomBox = new HBox(logOut);
        bottomInternalPane.getChildren().addAll(bottomBox);

        logOut.setOnAction(e ->{
            primaryStage.setTitle("Login");
            primaryStage.setScene(LoginScene);
        });

        // Defining test button
        Button test = new Button("Test");
        bottomBox.getChildren().add(test);// Adding the button to the rightPane

        test.setOnAction(e->{
            // API CALL!!
//                APIData testObj = new APIData(symbols.get(1));
//                testObj.fetchData(); // !WARNING: this line requires API usage
//                String p1 = testObj.extractSymbolOfCompany();
//                String nameOfCompany = testObj.extractNameOfCompany();

            //String aa= String.valueOf(p1);
            //double p2 = testObj.postMarketChangePercent();
            //String bb = String.valueOf(p2);

            test.setVisible(false);

            LineChart<Number, Number> lineChart = createLineChart("company");
            bottomInternalPane.getChildren().add(lineChart);

            try {
                updateDataHistory(pathDataHistoryDB,"symbolOfCompany","nameOfCompany");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

    } // Closing WelcomePane


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

        lineChart.getData().addAll(checkBoxSeriesMap.values());

        // Create a data series for the line chart
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Data History");

        // Add data points to the series (for demonstration)
//        series.getData().add(new XYChart.Data<>(1, 10));
//        series.getData().add(new XYChart.Data<>(2, 20));
//        series.getData().add(new XYChart.Data<>(3, 15));
//        series.getData().add(new XYChart.Data<>(4, 25));
//        series.getData().add(new XYChart.Data<>(5, 18));

        // Add the series to the line chart
        lineChart.getData().add(series);

        return lineChart;
    }

    public Scene getScene(){
        return WelcomeScene;
    }
}
