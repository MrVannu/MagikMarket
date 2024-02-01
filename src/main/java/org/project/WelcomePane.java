package org.project;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;


public class WelcomePane extends APIData { // To use data from api obj
    Scene WelcomeScene;
    private static final int MAX_SELECTED_CHECKBOXES = 4;
    private short dataToUpdateIndex = 0;
    private final ArrayList<String> symbolsOfStock = new ArrayList<>();
    private final APIData testObj  = new APIData();

    //DATA HISTORY MANAGEMENT
    public void updateDataHistory(
            int maxAge,
            double postMarketChangePercent,
            double regularMarketChangePercent,
            double preMarketChange,
            String extractNameOfCompany,
            double regularMarketOpen,
            double regularMarketDayHigh,
            double regularMarketDayLow,
            double regularMarketPreviousClose,
            String symbolOfCompany,
            String extractCurrencySymbol
    ) throws IOException
    {
        // Build the String to be written on DB
        String toWrite = maxAge + ", " +
                postMarketChangePercent + ", " +
                regularMarketChangePercent + ", " +
                preMarketChange + ", " +
                extractNameOfCompany + ", " +
                regularMarketOpen + ", " +
                regularMarketDayHigh + ", " +
                regularMarketDayLow + ", " +
                regularMarketPreviousClose + ", " +
                symbolOfCompany + ", " +
                extractCurrencySymbol;


        // Read all lines from the file
        List<String> lines = new ArrayList<>();
        // Path to DB for data history
        String pathDataHistoryDB = "src/main/resources/dataHistoryDB.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(pathDataHistoryDB))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }



        // Pad the list with empty lines up to 100 elements
        while (lines.size() < 200) {
            lines.add("");
        }

        // Update the data at the specified index
        lines.set(dataToUpdateIndex, toWrite);
        // Write the modified lines back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathDataHistoryDB))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        }

        ++dataToUpdateIndex; // Increment the counter of the most remote index
        if(dataToUpdateIndex>=200) dataToUpdateIndex=0;
    }

    public WelcomePane(Stage primaryStage, Scene LoginScene, User userRegistered){
        super();

        // Define a list of checkBoxes
        List<CheckBox> checkBoxesArrayList = new ArrayList<>();
        List<Button> investmentButtonsArrayList = new ArrayList<>();
        symbolsOfStock.add("amc");
        symbolsOfStock.add("x");
        symbolsOfStock.add("tsla");
        symbolsOfStock.add("kvue");
        symbolsOfStock.add("nio");
        symbolsOfStock.add("f");
        symbolsOfStock.add("googl");
        symbolsOfStock.add("ENL.BE");

        // Define VBox for the right pane of the main SplitPane
        VBox rightPaneBox = new VBox();

        // Define internal panes of the vertical split pane
        StackPane topRightPane = new StackPane();
        StackPane bottomRightPane = new StackPane();

        // Define the vertical SplitPane
        SplitPane rightVerticalSplitPane = new SplitPane();
        rightVerticalSplitPane.setDividerPositions(0.1, 0.6); // Set SplitPane dimension
        rightVerticalSplitPane.getItems().addAll(topRightPane, bottomRightPane); // Add the pane inside the SplitPane
        rightVerticalSplitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);

        // Add the internalVerticalSplitPane to the right pane (of the main splitPane)
        rightPaneBox.getChildren().add(rightVerticalSplitPane);

        // Create line chart
        LineChart<Number, Number> lineChart= LineChartGenerator.createLineChart("Choose a company");

        // Create GridPane to substitute boxBox and betButtonsBox for layout of the checkboxes and the bet buttons
        GridPane checkAndBetGpane = new GridPane();
        checkAndBetGpane.setHgap(10);
        checkAndBetGpane.setVgap(10);
        checkAndBetGpane.setPadding(new Insets(10));
        // Define counter used in the foreach
        int numOfBoxes = 0;

        // Create a HBox to contain both VBoxes
        HBox lowerLeftBox = new HBox(checkAndBetGpane);
        lowerLeftBox.setAlignment(Pos.CENTER);
        lowerLeftBox.setSpacing(20);

        // Define imageView for the logo image and define its dimensions
        ImageView logoImg = new ImageView(new Image("mk.png"));
        logoImg.setFitHeight(100);
        logoImg.setFitWidth(170);
        // Define a Box for the logo
        VBox logoBox = new VBox(logoImg);
        logoBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(logoBox, Priority.ALWAYS);

        // Define leftPaneBox as VBox and define logoBox inside
        VBox leftPaneBox = new VBox(10,logoBox);
        leftPaneBox.setAlignment(Pos.CENTER);

        // Define a FlowPane to hold the elements inside the underGridPane(GridPane),see declaration below
        FlowPane topBox = new FlowPane(); // Use FlowPane to adjust the space as needed
        topBox.setAlignment(Pos.CENTER);
        topBox.setHgap(10);
        topBox.setVgap(10);

        // Create a label for the amount of money
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double userCredit = userRegistered.getUserCredit() != null ? Double.parseDouble(userRegistered.getUserCredit()) : 0.0;
        Label moneyLabel = new Label(decimalFormat.format(userCredit));
        moneyLabel.getStyleClass().add("money-label");


        ArrayList<Stock> stocksCheckedOn = new ArrayList<>();

        // Define investmentBox for showing the investments
        HBox investmentBox = new HBox();
        investmentBox.setAlignment(Pos.CENTER);


        // Define HBox for hBoxList of stocks
        HBox hBoxList = new HBox();
        hBoxList.setAlignment(Pos.CENTER);



        // Create pie chart
        PieChart pieChart = new PieChart();

        for (String symbol : symbolsOfStock) {
            CheckBox checkBox = new CheckBox(symbol);
            checkBox.getStyleClass().add("checkbox");

            // Checkbox creating a new series for each checkbox
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(symbol); // Set the name of the series



            // Handle CheckBox action foreach checkBox
            checkBox.setOnAction(event -> {
                //For limiting of the selection of max checkboxes
                int selectedCount = (int) checkBoxesArrayList.stream().filter(CheckBox::isSelected).count();
                //Add a limit of selection of "MAX_SELECTED_CHECKBOXES" (for instance  max 4 check boxes)
                if (selectedCount > MAX_SELECTED_CHECKBOXES) {
                    checkBox.setSelected(false);

                    //Disable the not selected checkboxes when limit is reached
                    for (CheckBox cb : checkBoxesArrayList) {
                        if (!cb.isSelected()) {
                            cb.setDisable(true);
                        }
                    }
                } else {
                    checkBoxesArrayList.forEach(cb -> cb.setDisable(false));
                }

                //For adding new line
                if (checkBox.isSelected()) {

                        for (CheckBox cb : checkBoxesArrayList) {
                            if (cb.isSelected()) {
                                // assert false;  //Not to be used
                                testObj.fetchData(cb.getText()); // !WARNING: this line requires API usage
                            }
                        }

                        try {
                            // Placeholders for testing
                            updateDataHistory(1, 3.0, 8.0, 11.0, "name", 1.8,21.0,17.5,1.0, "name1", "name2");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }


                        // Define the lineChart with all the data
                        // Create a new series for this checkBox
                        XYChart.Series<Number, Number> lineChartSeries = new XYChart.Series<>();

                        // Define caption; if testObj is null then placeHolderString as name, otherwise SymbolOfCompany as name of the serie
                        lineChartSeries.setName((testObj.extractSymbolOfCompany()));


                    //Randomizes the random points
                    List<Integer> xs = new ArrayList<>();
                    xs.add(4);xs.add(8);xs.add(12);xs.add(16);xs.add(20);
                    Collections.shuffle(xs);


                    lineChartSeries.getData().addAll(
                            //(testObj==null? nameOfCompany: testObj.extractNameOfCompany())
                            new XYChart.Data<>(0, (testObj.regularMarketDayOpen())),
                            new XYChart.Data<>(xs.get(0), ((testObj.regularMarketDayHigh()*10 + testObj.regularMarketDayOpen())/1.8)),
                            new XYChart.Data<>(xs.get(1), (testObj.regularMarketDayHigh()*10)),
                            new XYChart.Data<>(xs.get(2), ((testObj.regularMarketDayHigh()*10 + testObj.regularMarketDayLow())/2)),
                            new XYChart.Data<>(xs.get(3), (testObj.regularMarketDayLow()/10)),
                            new XYChart.Data<>(xs.get(4), ((testObj.regularMarketDayLow()*10 + testObj.regularMarketPreviousClose())/2.2)),
                            new XYChart.Data<>(24, (testObj.regularMarketPreviousClose()))
                    );


                        Stock stock = new Stock(testObj.extractSymbolOfCompany(), testObj.extractNameOfCompany(), testObj.regularMarketDayOpen(), xs.get(0), xs.get(1), xs.get(2), testObj.extractAverageDailyVolume3MonthFmt(), testObj.regularMarketPrice());
                        stocksCheckedOn.add(stock);

                        // Create a new PieChart.Data object
                        PieChart.Data newData = new PieChart.Data( symbol, stock.getVolume() );

                        // Update the pie chart with the modified data
                        pieChart.getData().add(newData);

                        // Add the new series to the line chart
                        lineChart.setTitle((testObj.extractNameOfCompany()));
                        // Define chart title; if object is null then name placeholder name, otherwise nameOfCompany
                        lineChart.setTitle((testObj.extractNameOfCompany()));

                        // Add the data (points) to the line chart
                        lineChart.getData().add(lineChartSeries);

                        // Define the barChart with all the data
                        // Create a new series for this checkBox
                        XYChart.Series<String, Number> barChartSeries = new XYChart.Series<>();
                        // Set the title
                        barChartSeries.setName((testObj.extractSymbolOfCompany()));

                        // Data management for barChart
                        // Extract symbol and average daily volume (formatted) from JSON response
                        String stringSymbol = testObj.extractSymbolOfCompany();
                        String averageDailyVolumeFmt = testObj.extractAverageDailyVolume3MonthFmt();
                        // Output data to console
                        System.out.println("DATA:"+averageDailyVolumeFmt);
                        // If string value is not empty then parse with the right format
                        double averageDailyVolume = 0.0;
                        if(!averageDailyVolumeFmt.isEmpty()) averageDailyVolume = testObj.parseFormattedValue(averageDailyVolumeFmt);
                        else System.out.println("Empty averageDailyVolumeFmt!");

                        // Define the bar chart and enter the manipulated data
                        barChartSeries.getData().add(
                                new XYChart.Data<>(stringSymbol, averageDailyVolume)
                        );
                    } // Close if

                // If checkbox is not selected then remove the line from the chart
                if (!checkBox.isSelected() ) {
                    // Take the symbol of the deselected checkBox
                    final String temp = (checkBox.getText()).toUpperCase();
                    // Remove the series of data with the same symbol of the checkBox
                    lineChart.getData().removeIf(seriesLine -> seriesLine.getName().equals(temp));

                    //Remove the series of data with the same symbol of the checkBox
                    pieChart.getData().removeIf(seriesPie -> seriesPie.getName().equals(temp));
                    lineChart.setTitle("");

                }
            }); // Closing checkBox action

            // Define instance of SubmitControl
            SubmitControl submitControl = new SubmitControl(userRegistered, primaryStage, stocksCheckedOn, symbolsOfStock, hBoxList, symbol, moneyLabel, checkBox, pieChart);

            // Add the bet button into the ArrayList
            investmentButtonsArrayList.add(submitControl.getBet());

            // Add the checkBox into the ArrayList
            checkBoxesArrayList.add(checkBox);

            //Using the foreach to increment the counter to insert the checkboxes and buttons
            checkAndBetGpane.add(submitControl.getCheckBoxInsideHBox(), 0, numOfBoxes);
            checkAndBetGpane.add(submitControl.getBuyAndSell(), 1, numOfBoxes);
            numOfBoxes++;
        }

        // Add the checkBox + betButton into the layout
        leftPaneBox.getChildren().addAll(lowerLeftBox);
        // Let previsionBox occupy all the space inside the VBox it is inserted
        VBox.setVgrow(lowerLeftBox, Priority.ALWAYS);

        // Define object of the prevision class to show UI components of the prevision
        PrevisionComponent previsionComponent = new PrevisionComponent(stocksCheckedOn);

        // Define switchPane object of the hBoxList
        SwitchPane switchPane = new SwitchPane(hBoxList);

        // Define switch button
        Button switchHistory = new Button("Switch history");
        switchHistory.setOnAction(e->{
            if(switchPane.toggle)
                switchPane.showStocks(userRegistered, hBoxList);
            else
                switchPane.showOtherView(userRegistered, hBoxList);
        });


        // To set both the buttons of the same width
        switchHistory.setMaxWidth(100);

        // Define the VBox in the lower left part of the UI containing the previsionButton and switchHistoryButton
        VBox previsionAndSwitchButton = new VBox(previsionComponent.getPrevisionHBox(), switchHistory);
        previsionAndSwitchButton.setSpacing(10);
        previsionAndSwitchButton.setAlignment(Pos.CENTER);
        previsionAndSwitchButton.setPadding(new Insets(0, 0, 50,0)); // Padding to move the button higher

        // Add the Box with prevision button to the leftPaneBox (it is below the checkboxes)
        leftPaneBox.getChildren().addAll(previsionAndSwitchButton);
        leftPaneBox.setSpacing(10);

        // Set title to pie chart
        pieChart.setTitle("Avg. 3 Month Volume");

        // Layout
        // Define a box to insert all the charts
        VBox chartsBox = new VBox(10);
        chartsBox.getChildren().addAll(lineChart);

        // Add the list of data that the user bet on, and stuff like that, to the layout
        investmentBox.getChildren().add(hBoxList);

        // Define a SplitPane for inserting PieChart and InvestmentBox
        SplitPane investmentAndPieSplitPane = new SplitPane();
        investmentAndPieSplitPane.getItems().addAll(investmentBox,pieChart); //investmentBox,
        investmentAndPieSplitPane.setDividerPositions(0.7);

        rightVerticalSplitPane.getItems().addAll(chartsBox);

        // Add the barChart to the chartsBox
        chartsBox.getChildren().addAll();

        // Add the line chart to the bottomRightPane
        bottomRightPane.getChildren().addAll(investmentAndPieSplitPane);

        // Define the menu bar in the top of the mainPain
        MenuBar menuBar = new MenuBar();// Create a menu bar

        // Create a "Info" menu
        Menu infoMenu = new Menu("Info");
        // Create menu items for the "File" menu
        MenuItem instructionsItem = new MenuItem("Instructions");
        instructionsItem.setOnAction(e-> AlertField.showSuccessAlert("Information","This is a simulation of an " +
                "application that can help you having a look at the real market. You can invest some " +
                "money in some Stocks. You can have a look at the Stocks by selecting from the " +
                "checkboxes the Stocks you are interested on. You can select maximum 4 Stocks" +
                "You have an amount of money that is, by default, 1,000 (you can modify it form the edit" +
                "menu). As much as you invest, the amount of money will decrease. There will be " +
                "a simulation that will show you if your prediction was correct or not." +
                "When clicking the Prevision button some random data change the chart. This is a way" +
                "to simulate some kind of changes. When the chart has decreased you loose money. When the" +
                "chart has increased you win money." +
                "The bar chart will help you having a look at the market volume of a selected Stock. This data " +
                "is very important as it provides insights into the liquidity and trading activity of the " +
                "stock, which can be valuable for making informed investment decisions."));
        MenuItem exitItem = new MenuItem("Exit");
        // Exit the application if you click the exit item
        exitItem.setOnAction(e -> {
            System.exit(0); // Exit the application
        });
        // Add menu items into the "Info" menu and use a separator before the last element
        infoMenu.getItems().addAll(instructionsItem, new SeparatorMenuItem(), exitItem);

        // Create an "Edit" menu
        Menu editMenu = new Menu("Edit");
        // Create menu item for the "Edit" menu
        // Create amountItem where the user can change his amount
        MenuItem amountItem = new MenuItem("Amount");

        // Handle actions when the "Amount" menu item is clicked
        amountItem.setOnAction(e -> {
            // Create a custom popup using a Stage
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL); // Block user interaction with other windows
            popup.initOwner(primaryStage); // Set primaryStage as the parent of popup
            popup.setTitle("Amount Input");
            popup.setWidth(400);
            popup.setHeight(200);

            // Create UI elements for the custom popup
            Label instr = new Label("Set your new amount");
            TextField newCredit = new TextField();
            Button submitCredit = new Button("Submit");
            Button closePopup = new Button("Close");
            HBox inputBox = new HBox(submitCredit, closePopup);
            inputBox.setSpacing(30);

            // Define Box with elements for the popup
            VBox windowBox = new VBox(instr, newCredit, inputBox);
            windowBox.setPadding(new Insets(10));
            windowBox.setSpacing(10);
            windowBox.setAlignment(Pos.CENTER);

            // Handle actions when the "Submit" button is clicked
            submitCredit.setOnAction(submitEvent -> {
                String nativeData = newCredit.getText();
                String regex = "^\\d+(\\.\\d{1,2})?$"; // Regex to check if user insert a number

                if (nativeData.matches(regex)) {
                    try {
                        double newAmount = Double.parseDouble(nativeData);
                        if (newAmount > 10000.0) {
                            // If amount is too large
                            AlertField.showErrorAlert("Big amount", "The amount is too large. Choose another amount");
                            newCredit.setText("");
                        } else {
                            // New label value
                            userRegistered.setUserCredit(newAmount);
                            moneyLabel.setText(String.valueOf(newAmount));
                            //System.out.println("User input: " + newAmount);

                            // Close
                            popup.close();
                        }
                    } catch (NumberFormatException k) {
                        //System.out.println("Input non valid: " + nativeData);
                        newCredit.setText(""); // Clean out
                    }
                } else {
                    System.out.println("ERR: No valid input");
                    throw new AmountNotAllowed();
                }
            });


            // Handle actions when the "Close" button is clicked
            closePopup.setOnAction(closeEvent -> {
                // Close the custom popup without processing the input
                popup.close();
            });

            // Create a scene for the custom popup
            Scene popupScene = new Scene(windowBox);
            popup.setScene(popupScene);

            // Show the custom popup
            popup.showAndWait(); // Use showAndWait to wait for user interaction before continuing
        });

        // Add menu items to the "Edit" menu
        editMenu.getItems().addAll(amountItem);

        // Create a "LogOut" menu
        Menu options = new Menu("Options");
        MenuItem logOutItem = new MenuItem("Log out");
        logOutItem.setOnAction(e->{
            primaryStage.setTitle("Start App");
            primaryStage.setScene(LoginScene);
        });

        // Add menu items to the "LogOut" menu
        options.getItems().addAll(logOutItem);

        // Add menus to the menu bar
        menuBar.getMenus().addAll(infoMenu, editMenu, options);

        // Define the parent Scene and Panes
        // Define the main SplitPane that contains the left and right panes
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftPaneBox, rightPaneBox);
        splitPane.setDividerPositions(0.2, 0.6); // Configure SplitPane dimension

        // Define the first pane that will contain the main splitPane and the menuBar. It will contain everything
        BorderPane mainPane = new BorderPane(splitPane);
        mainPane.setTop(menuBar);

        // Instantiate the whole scene and define its dimension
        WelcomeScene = new Scene(mainPane, 1900, 900); // <---- Dimension of the WelcomePane
        WelcomeScene.getStylesheets().add("styles.css"); // Reference to the CSS file

        // Define elements and HBox for the topRightPane
        // Define username label to show username from the DB
        Label username = new Label(userRegistered.getUsername());
        username.setTextFill(Color.rgb(255, 164, 94));

        // Define the text of the username
        Label textUsername = new Label("Username: ");
        textUsername.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        textUsername.setTextFill(Color.rgb(92, 49, 144));

        // Create an ImageView for the user image
        ImageView imgView = new ImageView();
        imgView.setFitWidth(30); // Dimension of the image
        imgView.setFitHeight(30); // Dimension of the image

        // Create a FileChooser so that the user can insert an image
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        // Create a button to trigger the file selection and an image view
        Image userImage = new Image("file:src/main/resources/userImg.png");
        ImageView userView = new ImageView(userImage);
        userView.setFitWidth(24); // Set the desired width for the image
        userView.setFitHeight(24); // Set the desired height for the image

        // Section for updating the image
        Button selectImageButton = new Button();
        selectImageButton.setGraphic(userView);
        selectImageButton.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                // Load the selected image and display it in the ImageView
                Image image = new Image(selectedFile.toURI().toString());
                imgView.setImage(image);
            }
        });

        // Create a layout for the UI
        HBox profilePic = new HBox(imgView, selectImageButton);
        profilePic.setAlignment(Pos.CENTER);
        profilePic.setSpacing(10);

        Label amountLabel = new Label("Your amount: ");
        amountLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        amountLabel.setTextFill(Color.rgb(92, 49, 144));


        // Layout to visualize the money
        HBox moneyBox = new HBox(amountLabel, moneyLabel);
        moneyBox.setAlignment(Pos.CENTER);

        Separator separator1 = new Separator();
        separator1.setOrientation(Orientation.VERTICAL);
        separator1.setPrefHeight(20);
            Separator separator2 = new Separator();
            separator2.setOrientation(Orientation.VERTICAL);
            separator2.setPrefHeight(20);
                Separator separator3 = new Separator();
                separator3.setOrientation(Orientation.VERTICAL);
                separator3.setPrefHeight(20);

        // Add all the elements into the tobBox
        topBox.getChildren().addAll(profilePic, separator1, textUsername, username, separator2, moneyBox);

        // Add the topBot into the StackPane
        topRightPane.getChildren().addAll(topBox);

        primaryStage.setOnCloseRequest(event -> {
            event.consume();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit the application");
            alert.setHeaderText("Are you sure you want to close the current session?");
            alert.setContentText("Your data are already saved! \n\n\n Click on \"Leave\" to exit");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    System.out.println("\nGoodbye!");
                    primaryStage.close();
                }
            });
        });


    }

    public Scene getScene(){
        return WelcomeScene;
    }



}