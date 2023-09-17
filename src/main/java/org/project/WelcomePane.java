package org.project;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;


public class WelcomePane extends APIData implements HistoryManagement { // To use data from api obj
    Scene WelcomeScene;
    private static final int MAX_SELECTED_CHECKBOXES = 4;
    private final String pathUserDB = "src/main/resources/userDB.csv";  // Path to DB for users tracking
    private final String pathDataHistoryDB = "src/main/resources/dataHistoryDB.csv";  // Path to DB for data history
    private short dataToUpdateIndex = 0;
    private ArrayList<String> symbols = new ArrayList<String>();

    // Index of row to be overwritten (most remote in the db)

    private Map<CheckBox, XYChart.Series<Number, Number>> checkBoxSeriesMap;

    //DATA HISTORY MANAGEMENT
    public void updateDataHistory(
            int maxAge,
            double postMarketChangePercent,
            double regularMarketChangePercent,
            double preMarketChange,
            String extractNameOfCompany,
            double regularMarketDayHigh,
            double regularMarketDayLow,
            double regularMarketPreviousClose,
            String symbolOfCompany,
            String extractCurrencySymbol
    ) throws IOException
    {
        // Building the String to be written on DB
        String toWrite = String.valueOf(maxAge) + ", " +
                String.valueOf(postMarketChangePercent) + ", " +
                String.valueOf(regularMarketChangePercent) + ", " +
                String.valueOf(preMarketChange) + ", " +
                extractNameOfCompany + ", " +
                String.valueOf(regularMarketDayHigh) + ", " +
                String.valueOf(regularMarketDayLow) + ", " +
                String.valueOf(regularMarketPreviousClose) + ", " +
                symbolOfCompany + ", " +
                extractCurrencySymbol;


        // Read all lines from the file
        List<String> lines = new ArrayList<>();
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
        //System.out.println(dataToUpdateIndex);
    }


    // This method is yet to be improved providing a switch structure for various type of data are needed
    public double generateNextPrevision(String nameToScanFor, String parameter, short precisionRange){
        List<String[]> linesToSave = new ArrayList<>();
        int prevision;

        try (CSVReader reader = new CSVReader(new FileReader(pathDataHistoryDB))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                // Check whether the name is the wanted one
                if (!nextLine[4].isEmpty() && nextLine[4].equals(nameToScanFor)) {  // "4" is the position of the name of  company into the db
                    linesToSave.add(nextLine);
                }
            }

            Random random = new Random();
            int randomInRange = (random.nextInt(11))+1; // Generates a random integer between 1 (inclusive) and 10 (inclusive)

            prevision = 0;
            for (int k = 0; k < precisionRange; k++) {
                if (k < linesToSave.size()) {
                    String[] currentLine = linesToSave.get(k);
                    prevision += Integer.parseInt(currentLine[2]); // To sum the postMarketChangePercent
                }
            }
            return prevision;


        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }


    public void giveATip(String nameToScanFor, String parameter){
        // To be implemented
    }


    public WelcomePane(Stage primaryStage, Scene LoginScene, User userRegistered){
        super();

        // Define a list of checkBoxes
        List<CheckBox> checkBoxesArrayList = new ArrayList<>();
        List<Button> betButtonsArrayList = new ArrayList<>();
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

//        // Tell the splitPane to occupy the whole space in the VBox
//        VBox.setVgrow(rightVerticalSplitPane, Priority.ALWAYS);

        // Define logOut button
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

        // Create main line chart
        LineChart<Number, Number> lineChart= createLineChart("Choose a company");

        // Define layout for the left split pane
            //Define leftPaneBox to be a VBox
            VBox leftPaneBox = new VBox(10);
            leftPaneBox.setAlignment(Pos.CENTER_LEFT);
            leftPaneBox.setPadding(new Insets(10));
            // Create a Box to contain the checkBoxes
            VBox boxBox = new VBox(10);
            // Create a Box to contain the bet button
            VBox betButtonsBox = new VBox(13);
            // Create a HBox to contain both VBoxes
            HBox lowerLeftBox = new HBox(boxBox,betButtonsBox);
            lowerLeftBox.setSpacing(20);
            // Define imageView for the logo image and define its dimensions
            ImageView logoImg = new ImageView(new Image("mk.png"));
            logoImg.setFitHeight(100);
            logoImg.setFitWidth(170);

            // Define a Box for the logo
            VBox logoBox = new VBox(logoImg);
            logoBox.setAlignment(Pos.CENTER);
            VBox.setVgrow(logoBox, Priority.ALWAYS);

            // Add the logoBox to the layout
            leftPaneBox.getChildren().add(logoBox);

        // Define a FlowPane to hold the elements inside the underGridPane(GridPane),see declaration below
        FlowPane topBox = new FlowPane(); // Use FlowPane to adjust the space as needed
        topBox.setAlignment(Pos.CENTER);
        topBox.setHgap(10);
        topBox.setVgap(10);

        // Create a label for the amount of money
        Label moneyLabel = new Label(userRegistered.getUserCredit()); // Replace with the actual amount
        moneyLabel.getStyleClass().add("money-label"); // You can define a CSS class for styling

        ArrayList<Stock> stocksCheckedOn = new ArrayList<>();
        Label listOfBetStock = new Label() ;

    /*
       In this foreach the Stock CheckBoxes are defined. Next to the checkboxes a button is added that permits the
       user to invest an amount of money to a specific Stock.
       A custom popup appears where the amount of bet money is inserted
    */
        for (String symbol : symbols) {
            HBox checkBoxInsideHBox = new HBox(10);
            Button bet = new Button("Invest"); // Name of the bet buttons
            bet.getStyleClass().add("my-button");

            CheckBox checkBox = new CheckBox(symbol);
            checkBox.getStyleClass().add("checkbox");

            // Checkbox creating a new series for each checkbox
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(symbol); // Set the name of the serie

            // Save the series into the map associated with the checkbox
            checkBoxSeriesMap.put(checkBox, series);

            // Placeholders values in case something goes wrong with API call
            String sym = "phSymbol";
            String nameOfCompany = "phNameOfCompany";

            //API CALL!!
            final APIData testObj  = new APIData();

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
                    boolean callMade = false;

                    for (CheckBox cb : checkBoxesArrayList) {
                        if (cb.isSelected()) {
                            assert false;  //Not to be used
                            testObj.fetchData(cb.getText()); // !WARNING: this line requires API usage
                        }
                    }


                    Stock stock = new Stock(testObj.extractSymbolOfCompany(), testObj.extractNameOfCompany(), testObj.regularMarketDayOpen(), testObj.regularMarketDayHigh(), testObj.regularMarketDayLow(), testObj.regularMarketPreviousClose());
                    //System.out.println(testObj.extractNameOfCompany());
                    stocksCheckedOn.add(stock);
                    callMade = false;


                    //String aa= String.valueOf(p1);
                    //double p2 = testObj.postMarketChangePercent();
                    //String bb = String.valueOf(p2);

                    //test.setVisible(false);


                    try {
                        // Placeholders for testing
                        updateDataHistory(1, 1.0, 1.0, 1.0, "", 1.0,1.0,1.0, "", "");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    //});

                    // Create a new series for this checkbox
                    XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();
                    newSeries.setName((testObj==null? nameOfCompany: testObj.extractSymbolOfCompany()));
                    //nameOfCompany= testObj.extractNameOfCompany();

                    newSeries.getData().addAll(
                            //(testObj==null? nameOfCompany: testObj.extractNameOfCompany())
                            new XYChart.Data<>(0, (callMade? testObj.regularMarketDayOpen(): 50)),
                            new XYChart.Data<>(1, (callMade? testObj.regularMarketDayHigh()*10: 50)),
                            new XYChart.Data<>(2, (callMade? testObj.regularMarketDayLow()/10: 50)),
                            new XYChart.Data<>(3, (callMade? testObj.regularMarketPreviousClose(): 200)));

                    // Add the new series to the line chart
                    lineChart.setTitle((testObj==null? nameOfCompany: testObj.extractNameOfCompany()));

                    lineChart.getData().add(newSeries);
                    //bottomRightPane.getChildren().add(lineChart);

                }
                if (!checkBox.isSelected() ) {
                    final String temp = (testObj == null ? nameOfCompany : checkBox.getText()).toUpperCase();
                    lineChart.getData().removeIf(serie -> serie.getName().equals(temp));
                }

            }); // Closing checkBox action

            // Handle the "Bet" button action here
            bet.setOnAction(event -> {
                // Create a custom popup using a Stage
                Stage betPopup = new Stage();
                betPopup.initModality(Modality.APPLICATION_MODAL); // Block user interaction with other windows
                betPopup.initOwner(primaryStage); // Set primaryStage as the parent of popup
                betPopup.setTitle("Bet Input");
                betPopup.setWidth(400);
                betPopup.setHeight(200);

                // Create UI elements for the custom popup
                Label instruction = new Label("Set the bet amount");
                TextField betField = new TextField();
                Button submitBetAmount = new Button("Submit");
                Button closeBetPopup = new Button("Close");
                HBox buttonsBetBox = new HBox(submitBetAmount, closeBetPopup);
                buttonsBetBox.setSpacing(30);

                // Define Box with elements for the popup
                VBox windowBetBox = new VBox(instruction, betField, buttonsBetBox);
                windowBetBox.setPadding(new Insets(10));
                windowBetBox.setSpacing(10);
                windowBetBox.setAlignment(Pos.CENTER);

                    //Handle submitBetAmount button
                    submitBetAmount.setOnAction(e->{
                        if(!betField.getText().isEmpty()) { // If the field is not empty
                            try{
                                // Attempt to parse the text as a double. If parsing is successful, it's a valid number
                                Double.parseDouble(betField.getText());

                                //Decreases the user's amount of money in the GUI label
                                userRegistered.setUserCredit(Double.parseDouble(userRegistered.getUserCredit()) - Double.parseDouble(betField.getText()));
                                //Update the money label
                                moneyLabel.setText(String.valueOf(userRegistered.getUserCredit()));
                                betField.clear();
                                betPopup.close();
                        /*if (stocksBetOn.stream().anyMatch(stock -> {
                            return stock.getName().equals(symbol);
                        })) */
                                //updateListOfBetStockLabel(stocksBetOn, listOfBetStock);
                                topBox.getChildren().add(new Label(symbol));
                                stocksCheckedOn.forEach(stock -> {
                                    if(stock.getName().equals(symbol))
                                        stock.setInvestedOn(true);
                                });
                            }
                            catch(NumberFormatException er){
                                // Parsing failed, the text is not a valid number
                                // Handle the case where the input is not a number
                                AlertField.showAlert("Invalid input", "Please enter a valid number.");
                                System.out.println("Invalid input. Please enter a valid number.");
                            }
                        } else {
                            // The field is empty
                            AlertField.showAlert("Invalid input", "Please enter a bet amount.");
                            System.out.println("Field is empty. Please enter a bet amount.");
                        }
                    });

                    // Handle closeBetPopup button
                    closeBetPopup.setOnAction(e->{
                        betPopup.close();
                    });

                // Create a scene for the custom popup
                Scene betPopupScene = new Scene(windowBetBox);
                betPopup.setScene(betPopupScene);

                // Show the custom popup
                betPopup.showAndWait(); // Use showAndWait to wait for user interaction before continuing
            });

            // Add the checkBoxes into the HBox
            checkBoxInsideHBox.getChildren().addAll(checkBox);

            // Add the bet button into the ArrayList
            betButtonsArrayList.add(bet);

            // Add the checkBox into the ArrayList
            checkBoxesArrayList.add(checkBox);

            // Add the HBox into the VBox for vertical layout
            boxBox.getChildren().addAll(checkBoxInsideHBox);// boxBox is previously added into lowerLeftBox

            // Add the bet button into the betButtonsBox
            betButtonsBox.getChildren().addAll(bet);

        } // CLOSE FOREACH

        // Add the checkBox + betButton into the layout
        leftPaneBox.getChildren().addAll(lowerLeftBox);
        // Let previsionBox occupy all the space inside the VBox it is inserted
        VBox.setVgrow(lowerLeftBox, Priority.ALWAYS);

    // Define Prevision button and its layout
                // Define image for the button
                Image arrowGui = new Image("file:src/main/resources/frecciale.png");
                ImageView arrowGuiImg = new ImageView(arrowGui);
                arrowGuiImg.setFitHeight(10);
                arrowGuiImg.setFitWidth(15);

                // Define button name
                String previsionString = "Prevision";

            // Define HBox with button name and button image
            HBox content = new HBox(3);
            content.getChildren().addAll(new Text(previsionString), arrowGuiImg);
            content.setAlignment(Pos.CENTER);

            // Define the prevision button and its box
            Button previsionButton = new Button();
            previsionButton.setGraphic(content); // Insert img inside button
            HBox previsionBox = new HBox(previsionButton);
            previsionBox.setAlignment(Pos.CENTER);

            // Handle prevision button action
            previsionButton.setOnAction(e->{

            // Check if stock has been invested


            // Popup for new graph
                Stage previsionStage = new Stage();
                previsionStage.initModality(Modality.APPLICATION_MODAL); // Block user interaction with other windows
                previsionStage.initOwner(primaryStage); // Set primaryStage as the parent of popup
                previsionStage.setTitle("Bet Input");
                previsionStage.setWidth(800);
                previsionStage.setHeight(500);

                // Define a line chart
                LineChart<Number, Number> lineChartPrevision= createLineChart("Prevision");

                // Create UI elements for the custom popup

                Button closePrevisionPopup = new Button("Close");
                HBox previsionPopupBox = new HBox(closePrevisionPopup);
                previsionPopupBox.setSpacing(30);

                // Define Box with elements for the popup
                VBox windowBetBox = new VBox(lineChartPrevision, closePrevisionPopup);
                windowBetBox.setPadding(new Insets(10));
                windowBetBox.setSpacing(10);
                windowBetBox.setAlignment(Pos.CENTER);

                // Handle close button inside the popup
                            closePrevisionPopup.setOnAction(ex->{
                                previsionStage.close();
                            });

                // Create a scene for the custom popup
                Scene previsionScene = new Scene(windowBetBox);
                previsionStage.setScene(previsionScene);

                // Show the custom popup
                previsionStage.showAndWait(); // Use showAndWait to wait for user interaction before continuing

            System.out.println("Stocks checked on: \n");
            stocksCheckedOn.forEach(stock -> {
                final double  MODIFIER = 2;
                //Algorithm to modify the stocks to be implemented
                stock.setRegularMarketOpen(stock.getRegularMarketOpen() * MODIFIER);
                stock.setRegularMarketDayHigh(stock.getRegularMarketDayHigh() * MODIFIER);
                stock.setRegularMarketDayLow(stock.getRegularMarketDayLow() * MODIFIER);
                stock.setMarketPreviousClose(stock.getMarkerPreviousClose() * MODIFIER);

                System.out.println(stock.getName());
                });
                /*try {
                    updateDataHistory(1, 1.0, 1.0, 1.0, "test", 1.0,1.0,1.0, "test", "test");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }*/
            });

        // Let previsionBox occupy all the space inside the VBox it is inserted
        VBox.setVgrow(previsionBox, Priority.ALWAYS);

        // Add the Box with prevision button to the leftPaneBox (it is below the checkboxes)
        leftPaneBox.getChildren().addAll(previsionBox);
        leftPaneBox.setSpacing(10); // Define space between the elements inside the box

    //Define PieChart for other datas
        PieChart.Data[] pieChartData = {
                new PieChart.Data("USD", 25000000),
                new PieChart.Data("EUR", 18000000),
                new PieChart.Data("JPY", 15000000),
                // Aggiungi altre valute e i rispettivi volumi
        };

        // Create pie chart
        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(pieChartData);

        // Set title to pie chart
        pieChart.setTitle("Distribution of Currency Volume");

        //Create a box to insert line and pie chart
        VBox chartsBox = new VBox(10);
        chartsBox.getChildren().addAll(lineChart, pieChart);

        // Add the line chart to the bottomRightPane
        bottomRightPane.getChildren().add(chartsBox);

        // Define the action of the logOut button
        logOut.setOnAction(e ->{
            primaryStage.setTitle("Login");
            primaryStage.setScene(LoginScene);
        });

    // Define the menu bar
        // Define the menu bar in the top of the mainPain
        MenuBar menuBar = new MenuBar();// Create a menu bar

            // Create a "Info" menu
            Menu infoMenu = new Menu("Info");
                    // Create menu items for the "File" menu
                    MenuItem instructionsItem = new MenuItem("Instructions");
                    instructionsItem.setOnAction(e->{
                        AlertField.showSuccessAlert("Information","This is a simulation of an " +
                                "application that can help you having a look at the real market. You can invest some " +
                                "money in some Stocks. You can have a look at the Stocks by selecting from the " +
                                "checkboxes the Stocks you are interested on. You can select maximum 4 Stocks" +
                                "You have an amount of money that is, by default, 1,000 (you can modify it form the edit" +
                                "menu). As much as you invest, the amount of money will decrease. There will be " +
                                "a simulation that will show you if your prediction was correct or not.");
                    });
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
                                        AlertField.showAlert("Big amount", "The amount is too large. Choose another amount");
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
                                    //System.out.println("Input non valido: " + nativeData);
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
        WelcomeScene = new Scene(mainPane, 1400, 900); // <---- Dimension of the WelcomePane
        WelcomeScene.getStylesheets().add("styles.css"); // Reference to the CSS file

    // Define elements and HBox for the topRightPane
        // Define username label to show username from the DB
        Label username = new Label(userRegistered.getUsername());

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
                //selectImageButton.setText("\nModify");
            }
        });

        // Create a layout for the UI
        HBox profilePic = new HBox(imgView, selectImageButton);
        profilePic.setAlignment(Pos.CENTER);
        profilePic.setSpacing(10);

        // Label for the stocks bet on
        String stringOfBetStocks = " Stocks bet on:\n";

        // Label with the list of Stock the user bet
        listOfBetStock = new Label(stringOfBetStocks);

        // Add all the elements into the tobBox
        topBox.getChildren().addAll(profilePic, username, moneyLabel, listOfBetStock);

        // Add the topBot into the StackPane
        topRightPane.getChildren().addAll(topBox);

    } // Close WelcomePane


    // Data history management
    // Method to update data history database


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