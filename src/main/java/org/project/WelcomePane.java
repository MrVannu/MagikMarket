package org.project;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
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

//        // Tell the splitPane to occupy whole space in the VBox
        VBox.setVgrow(rightVerticalSplitPane, Priority.ALWAYS);

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

        // Define HBox for the bottomRightPane
        //HBox bottomBox = new HBox(logOut);
        //bottomRightPane.getChildren().addAll(logOut);

        // Create main line chart
        LineChart<Number, Number> lineChart= createLineChart("nameOfCompany");

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
            In this foreach the Stock CheckBoxes will be defined. Next to the checkboxes a bet button is added.
            A tooltip is created together with the button where the amount of bet money is inserted
         */
        for (String symbol : symbols) {
            HBox checkBoxWithBetButton = new HBox(10);
            Button bet = new Button("Invest"); // Name of the bet buttons
            bet.getStyleClass().add("my-button");

            CheckBox checkBox = new CheckBox(symbol);
            checkBox.getStyleClass().add("checkbox");

            // Checkbox creating a new series for each checkbox
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(symbol); // Set the name of the serie

            // Save the series into the map associated with the checkbox
            checkBoxSeriesMap.put(checkBox, series);

            // Create a TextField and a Submit button within a VBox for user input
            Label userInstruction = new Label("How much do you want to bet?");
            TextField betAmountField = new TextField(); // Define the field to insert the amount of betting
            Button submitBetButton = new Button("Submit");
            Button closeBetButton = new Button("Close");
            HBox inputButtons = new HBox(submitBetButton, closeBetButton);
            VBox userInputBox = new VBox(userInstruction, betAmountField, inputButtons);

            // Create a Tooltip and set the VBox containing the input elements as its graphic
            Tooltip betTooltip = new Tooltip();
            betTooltip.setGraphic(userInputBox);

            // Set the Tooltip to the "Bet" button
            Tooltip.install(bet, betTooltip);

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

            //Placeholders values in case something goes wrong with API call
            String sym = "phSymbol";
            String nameOfCompany = "phNameOfCompany";

            //API CALL!!
            final APIData testObj  = new APIData();

            checkBox.setOnAction(event -> {
                //For limiting of the selection of max checkboxes
                int selectedCount = (int) checkBoxes.stream().filter(CheckBox::isSelected).count();
                //Add a limit of selection of "MAX_SELECTED_CHECKBOXES" (for instance  max 4 check boxes)
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


                //For adding new line
                if (checkBox.isSelected()) {
                    boolean callMade = false;



                    for (CheckBox cb : checkBoxes) {
                        if (cb.isSelected()) {
                            assert false;  //Not to be used
                            testObj.fetchData(cb.getText()); // !WARNING: this line requires API usage
                        }
                    }


                    Stock stock = new Stock(testObj.extractSymbolOfCompany(), testObj.extractNameOfCompany(), testObj.regularMarketDayOpen(), testObj.regularMarketDayHigh(), testObj.regularMarketDayLow(), testObj.regularMarketPreviousClose());
                    //System.out.println(testObj.extractNameOfCompany());
                    stocksCheckedOn.add(stock);
                    callMade = true;


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
                            new XYChart.Data<>(0, (callMade? testObj.regularMarketDayOpen()/1000: 50)),
                            new XYChart.Data<>(1, (callMade? testObj.regularMarketDayHigh()/1000: 50)),
                            new XYChart.Data<>(2, (callMade? testObj.regularMarketPreviousClose()/1000: 200)));


                    // Add the new series to the line chart
                    lineChart.setTitle((testObj==null? nameOfCompany: testObj.extractNameOfCompany()));

                    lineChart.getData().add(newSeries);
                    //bottomRightPane.getChildren().add(lineChart);


                }
                if (!checkBox.isSelected() ) {
                    final String temp = (testObj == null ? nameOfCompany : checkBox.getText()).toUpperCase();
                    lineChart.getData().removeIf(serie -> serie.getName().equals(temp));
                }

            });

            bet.setOnAction(event -> {
                // Handle the "Bet" button action here
                betTooltip.show(bet, bet.getScene().getWindow().getX() + bet.getScene().getX() + bet.getLayoutX() + bet.getWidth(), bet.getScene().getWindow().getY() + bet.getScene().getY() + bet.getLayoutY());
            });

            submitBetButton.setOnAction(event -> {
                if(!betAmountField.getText().isEmpty()) { // If the field is not empty
                    try{
                        // Attempt to parse the text as a double. If parsing is successful, it's a valid number
                        double betAmount = Double.parseDouble(betAmountField.getText());

                        //Decreases the user's amount of money in the GUI label
                        userRegistered.setUserCredit(Double.parseDouble(userRegistered.getUserCredit()) - Double.parseDouble(betAmountField.getText()));
                        //Update the money label
                        moneyLabel.setText(String.valueOf(userRegistered.getUserCredit()));
                        betAmountField.clear();
                        betTooltip.hide();

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
                    catch(NumberFormatException e){
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

            closeBetButton.setOnAction(event -> {
                betTooltip.hide();
            });

            // Add the checkBox into the HBox
            checkBoxWithBetButton.getChildren().addAll(checkBox);

            // Add the bet button into the list
            betButtons.add(bet);

            // Add the checkBox into the list
            checkBoxes.add(checkBox);

            // Add the HBox into the leftPaneBox
            boxBox.getChildren().addAll(checkBoxWithBetButton); // Add the checkBoxes with bet button to the layout

            // Add the bet button into the betButtonsBox
            betButtonsBox.getChildren().addAll(bet);

        } // CLOSE FOREACH

        // Add the checkBox + betButton into the layout
        leftPaneBox.getChildren().addAll(lowerLeftBox);
        VBox.setVgrow(lowerLeftBox, Priority.ALWAYS);

        // Image for the prevision button
        Image arrowGui = new Image("file:src/main/resources/frecciale.png");
        ImageView arrowGuiImg = new ImageView(arrowGui);
        arrowGuiImg.setFitHeight(20);
        arrowGuiImg.setFitWidth(30);

        // Define the prevision button and its box
        Button previsionButton = new Button();
        previsionButton.setGraphic(arrowGuiImg); // Insert img inside button
        HBox previsionBox = new HBox(previsionButton);
        previsionBox.setAlignment(Pos.CENTER);

        // Prevision action
        previsionButton.setOnAction(e->{
            stocksCheckedOn.forEach(stock -> {
                //Algorithm to modify the stocks
            });
            /*try {
                updateDataHistory(1, 1.0, 1.0, 1.0, "test", 1.0,1.0,1.0, "test", "test");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }*/
        });

        // Layout purpose
        VBox.setVgrow(previsionBox, Priority.ALWAYS);

        // Add the button to the leftPaneBox, below the checkboxes
        leftPaneBox.getChildren().addAll(previsionBox);
        leftPaneBox.setSpacing(10); // Define space between the elements inside the box

        // Add the line chart to the bottomRightPane
        bottomRightPane.getChildren().add(lineChart);

        // Define the action of the logOut button
        logOut.setOnAction(e ->{
            primaryStage.setTitle("Login");
            primaryStage.setScene(LoginScene);
        });

        // Define the menu bar in the top of the mainPain
        MenuBar menuBar = new MenuBar();// Create a menu bar

        // Create a "File" menu
        Menu fileMenu = new Menu("File");
        // Create menu items for the "File" menu
        MenuItem instructionsItem = new MenuItem("Instructions");
        instructionsItem.setOnAction(e->{
            AlertField.showSuccessAlert("Information","This is a simulation of an " +
                    "application that can help you look at the real market. You can invest some " +
                    "money in some Stocks. You can have a look at the Stocks by selecting from the " +
                    "checkboxes the Stocks you are interested on. You can select maximum 4 Stocks" +
                    "You have an amount of money that is, by default, 1,000. As much as " +
                    "you invest, the amount of money will decrease. There will be a simulation that " +
                    "will show you if your prediction was correct or not.");
        });
        MenuItem exitItem = new MenuItem("Exit");
        // Exit the application if you click the exit item
        exitItem.setOnAction(e -> {
            System.exit(0); // Exit the application
        });
        // Add menu items to the "File" menu and use a separator before the last element
        fileMenu.getItems().addAll(instructionsItem, new SeparatorMenuItem(), exitItem);

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
        menuBar.getMenus().addAll(fileMenu, editMenu, options);

        // Define the main SplitPane that contains the left and right panes
        SplitPane splitPane = new SplitPane();

        splitPane.getItems().addAll(leftPaneBox, rightPaneBox);
        splitPane.setDividerPositions(0.2, 0.6); // Configure SplitPane dimension

        // Define the mainPane that will contain the main splitPane and the menuBar
        BorderPane mainPane = new BorderPane(splitPane);
        mainPane.setTop(menuBar);

//        mainPane.setMinWidth(1000); // Minimum width for the pane
//        mainPane.setMinHeight(700); // Minimum height for the pane

        // Instantiate the whole scene and define its dimension
        WelcomeScene = new Scene(mainPane, 1400, 900); // <---- Dimension of the WelcomePane
        WelcomeScene.getStylesheets().add("styles.css"); // Reference to the CSS file

        // Define elements and HBox for the topRightPane
        Label username = new Label(userRegistered.getUsername());// Set the username as text of the label

        // Create an ImageView for the user image
        ImageView imgView = new ImageView();
        imgView.setFitWidth(30); // Dimension of the image
        imgView.setFitHeight(30);

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

        // Button for updating an image
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

        // Define an GridPane to better align logOut button. It contains the topBox
        GridPane underGridPane = new GridPane();
        underGridPane.setPadding(new Insets(10));
        underGridPane.setHgap(10);
        underGridPane.setVgap(0);
        underGridPane.setAlignment(Pos.CENTER);

        underGridPane.add(topBox, 0, 0);
//        underGridPane.add(logOut, 6, 0);

        topRightPane.getChildren().addAll(underGridPane);

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