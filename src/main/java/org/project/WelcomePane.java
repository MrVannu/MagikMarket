package org.project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class WelcomePane {
    private static final int MAX_SELECTED_CHECKBOXES = 4;
    Scene WelcomeScene;
    private final String pathUserDB = "src/main/resources/userDB.csv";  // Path to DB for users tracking
    private ArrayList<String> symbols = new ArrayList<String>();
    private final String pathDataHistoryDB = "src/main/resources/dataHistoryDB.csv";  // Path to DB for data history

    // Index of row to be overwritten (most remote in the db)
    private short dataToUpdateIndex = 0;
    private Map<CheckBox, XYChart.Series<Number, Number>> checkBoxSeriesMap;

    public WelcomePane(Stage primaryStage, Scene LoginScene, User userRegistered){
        super();


        // Defining a list of checkBoxes
        List<CheckBox> checkBoxes = new ArrayList<>();
        List<Button> betButtons = new ArrayList<>();
        symbols.add("amc");
        symbols.add("x");
        symbols.add("tsla");
        symbols.add("kvue");
        symbols.add("nio");
        symbols.add("nvda");
        symbols.add("jnj");
        symbols.add("amd");
        symbols.add("f");
        symbols.add("googl");
        symbols.add("ENL.BE");

        checkBoxSeriesMap = new HashMap<>();

        // Defining VBox for the right split pane
        VBox rightPaneBox = new VBox();

            // Defining internal panes of the vertical split pane
            StackPane topRightPane = new StackPane();
            StackPane bottomRightPane = new StackPane();

            // Defining the vertical SplitPane
            SplitPane internalVerticalSplitPane = new SplitPane();
            internalVerticalSplitPane.setDividerPositions(0.1); // Configuring SplitPane dimension
            internalVerticalSplitPane.getItems().addAll(topRightPane, bottomRightPane);
            internalVerticalSplitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);
            // Adding the new verticalSplitPane to the right pane (of the horizontal splitPane)
            rightPaneBox.getChildren().add(internalVerticalSplitPane);

            VBox.setVgrow(internalVerticalSplitPane, Priority.ALWAYS);


        // Defining logOut button
        Button logOut = new  Button();
        logOut.setOnAction(e->{
            primaryStage.setTitle("Start App");
            primaryStage.setScene(LoginScene);
        });

        // Create an ImageView for the image
        Image logoutImage = new Image("file:src/main/resources/logoutImg.png");
        ImageView logoutImageView = new ImageView(logoutImage);
        logoutImageView.setFitWidth(24); // Set the desired width
        logoutImageView.setFitHeight(24); // Set the desired height

        logOut.setGraphic(logoutImageView);

        // Defining HBox for the bottomRightPane
        //HBox bottomBox = new HBox(logOut);
        //bottomRightPane.getChildren().addAll(logOut);


        LineChart<Number, Number> lineChart= createLineChart("nameOfCompany");


        // Defining VBox for the left split pane and inserting checkBoxes inside
        // VBox topLeftPane = new  VBox(10); //Box for other purpose
        VBox bottomLeftPane = new  VBox(10);//Box for the checkboxes
        VBox leftPaneBox = new VBox(bottomLeftPane);
        leftPaneBox.setAlignment(Pos.CENTER_LEFT);
        leftPaneBox.setPadding(new Insets(10));

        // Defining an HBox to hold the elements inside the underGridPane(GridPane),see declaration below
        FlowPane topBox = new FlowPane(); // Adjust the spacing as needed
        topBox.setAlignment(Pos.CENTER);
        topBox.setHgap(10);
        topBox.setVgap(10);

        // Create a label for the amount of money
        Label moneyLabel = new Label(Double.toString(userRegistered.getUserCredit())); // Replace with the actual amount
        moneyLabel.getStyleClass().add("money-label"); // You can define a CSS class for styling

        ArrayList<Stock>stocksBettedOn = new ArrayList<>();
        Label listOfBettedStock= new Label() ;

        // Creating checkboxes
        for (String symbol : symbols) {
            HBox checkBoxWithBetButton = new HBox(10);
            Button bet = new Button("Bet");

            CheckBox checkBox = new CheckBox(symbol);
            checkBox.getStyleClass().add("checkbox");

            // Checkbox creating a new serie for each checkbox
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(symbol); // Set the name of the serie

            // Saving the serie into the map associated with the checkbox
            checkBoxSeriesMap.put(checkBox, series);

            // Create a TextField and a Submit button within a VBox for user input
            Label userInstruction = new Label("How much do you want to bet?");
            TextField betAmountField = new TextField();
            Button submitBetButton = new Button("Submit");
            Button closeBetButton = new Button("Close");
            HBox inputButtons = new HBox(submitBetButton, closeBetButton);
            VBox userInputBox = new VBox(userInstruction, betAmountField, inputButtons);

            // Create a Tooltip and set the VBox containing the input elements as its graphic
            Tooltip betTooltip = new Tooltip();
            betTooltip.setGraphic(userInputBox);

            // Set the Tooltip to the "Bet" button
            Tooltip.install(bet, betTooltip);

            //Placeholders values in case something goes wrong with API call
            String sym = "phSymbol";
            String nameOfCompany = "phNameOfCompany";


            checkBox.setOnAction(event -> {
                //For limiting of the selection of max checboxes
                int selectedCount = (int) checkBoxes.stream().filter(CheckBox::isSelected).count();
                //Adding a limit of selection of "MAX_SELECTED_CHECKBOXES" (for instance  max 4 check boxes)
                if (selectedCount > MAX_SELECTED_CHECKBOXES) {
                    checkBox.setSelected(false);

                    //Disable the not selected checkboxes when limit is reached
                    for (CheckBox cb : checkBoxes) {
                        if (!cb.isSelected()) {
                            cb.setDisable(true);
                        }
                    }
                } else {
                    checkBoxes.forEach(cb -> cb.setDisable(false));
                }

                APIData testObj = null;
                //For adding new line
                if (checkBox.isSelected()) {

                    boolean callMade = false;

                    // API CALL!!
                            /*testObj = new APIData(symbol);
                            testObj.fetchData(); // !WARNING: this line requires API usage
                            //nameOfCompany = testObj.extractNameOfCompany();

                            Stock stock = new Stock(testObj.extractSymbolOfCompany(), testObj.extractNameOfCompany());
                            System.out.println(testObj.extractNameOfCompany());
                            callMade = true;*/


                    //String aa= String.valueOf(p1);
                    //double p2 = testObj.postMarketChangePercent();
                    //String bb = String.valueOf(p2);

                    //test.setVisible(false);


                    try {
                        updateDataHistory(pathDataHistoryDB,sym, (testObj==null? nameOfCompany: testObj.extractNameOfCompany()));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    //});

                    // Create a new series for this checkbox
                    XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();
                    newSeries.setName((testObj==null? nameOfCompany: testObj.extractNameOfCompany()));


                    newSeries.getData().addAll(
                            //(testObj==null? nameOfCompany: testObj.extractNameOfCompany())
                            new XYChart.Data<>(1, (callMade? testObj.regularMarketDayLow(): 50)),
                            new XYChart.Data<>(2, (callMade? testObj.regularMarketDayHigh(): 50)),
                            new XYChart.Data<>(3, (callMade? testObj.regularMarketPreviousClose(): 200)));


                    // Add the new series to the line chart
                    lineChart.setTitle((testObj==null? nameOfCompany: testObj.extractNameOfCompany()));

                    lineChart.getData().add(newSeries);
                    //bottomRightPane.getChildren().add(lineChart);


                } else {
                    final String temp = (testObj==null? nameOfCompany: testObj.extractNameOfCompany());
                    lineChart.getData().removeIf(serie -> serie.getName().equals(temp));
                }
            });

            bet.setOnAction(event -> {
                // Handle the "Bet" button action here
                betTooltip.show(bet, bet.getScene().getWindow().getX() + bet.getScene().getX() + bet.getLayoutX() + bet.getWidth(), bet.getScene().getWindow().getY() + bet.getScene().getY() + bet.getLayoutY());
            });


            submitBetButton.setOnAction(event -> {
                if(!betAmountField.getText().isEmpty()) {//Decreases the user's amount of money
                    userRegistered.setUserCredit(userRegistered.getUserCredit() - Double.valueOf(betAmountField.getText()));
                    //Update the money label
                    moneyLabel.setText(Double.toString(userRegistered.getUserCredit()));
                    betAmountField.clear();
                    betTooltip.hide();
                    stocksBettedOn.add(new Stock(symbol, nameOfCompany));

                    /*if (stocksBettedOn.stream().anyMatch(stock -> {
                        return stock.getName().equals(symbol);
                    })) */
                        //updateListOfBettedStockLabel(stocksBettedOn, listOfBettedStock);
                        topBox.getChildren().add(new Label(symbol));

                }
            });

            closeBetButton.setOnAction(event -> {
                betTooltip.hide();
            } );



            checkBoxWithBetButton.getChildren().addAll(checkBox, bet);
            checkBoxWithBetButton.setAlignment(Pos.CENTER_LEFT);

            betButtons.add(bet);
            checkBoxes.add(checkBox);

            //topLeftPane.getChildren().addAll();
            bottomLeftPane.getChildren().addAll(checkBoxWithBetButton);

        } // CLOSING FOR


        bottomRightPane.getChildren().add(lineChart);
        logOut.setOnAction(e ->{
            primaryStage.setTitle("Login");
            primaryStage.setScene(LoginScene);
        });

            VBox.setVgrow(leftPaneBox, Priority.ALWAYS);

        // Defining SplitPane that contains the left and right panes (that are VBoxes)
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftPaneBox, rightPaneBox);
        splitPane.setDividerPositions(0.2); // Configuring SplitPane dimension
        splitPane.setMinWidth(1000); // Minimum width for the pane
        splitPane.setMinHeight(700); // Minimum height for the pane

        // Instantiating the whole scene and defining its dimension
        WelcomeScene = new Scene(splitPane, 1000, 700); // <---- Dimention of the WelcomePane
        WelcomeScene.getStylesheets().add("styles.css");// Reference to the CSS file0

        // Defining elements and HBox for the topRightPane
        Label username = new Label("Username");
        // Create an ImageView for the user image
        ImageView imgView = new ImageView();
        imgView.setFitWidth(80);
        imgView.setFitHeight(80);

        // Create a FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        // Create a button to trigger the file selection
        Image userImage = new Image("file:src/main/resources/userImg.png");
        ImageView userView = new ImageView(userImage);
        userView.setFitWidth(24); // Set the desired width
        userView.setFitHeight(24); // Set the desired height

        Button selectImageButton = new Button();
        selectImageButton.setGraphic(userView);
        selectImageButton.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                // Load the selected image and display it in the ImageView
                Image image = new Image(selectedFile.toURI().toString());
                imgView.setImage(image);
                //selectImageButton.setText("\nModify");
            }
        });

        // Create a layout for the UI
        HBox profilePic = new HBox(selectImageButton, imgView);

        //Label for the stocks betted on
        String stringOfBettedStocks = " Stocks betted on:\n";

        listOfBettedStock = new Label(stringOfBettedStocks);

        topBox.getChildren().addAll(profilePic,username, moneyLabel, listOfBettedStock);

        // Defining an GridPane to better align logOut button. It contains the topBox
        GridPane underGridPane = new GridPane();
        underGridPane.setPadding(new Insets(10));
        underGridPane.setHgap(10);
        underGridPane.setVgap(0);
        underGridPane.setAlignment(Pos.CENTER);

        underGridPane.add(topBox, 0, 0);
        underGridPane.add(logOut, 6, 0);

        topRightPane.getChildren().addAll(underGridPane);

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


        return lineChart;
    }

    public Scene getScene(){
        return WelcomeScene;
    }
}
