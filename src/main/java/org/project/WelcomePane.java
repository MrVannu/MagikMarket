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
            rightVerticalSplitPane.setDividerPositions(0, 0.6); // Set SplitPane dimension
            rightVerticalSplitPane.getItems().addAll(topRightPane, bottomRightPane); // Add the pane inside the SplitPane
            rightVerticalSplitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);

            // Add the internalVerticalSplitPane to the right pane (of the main splitPane)
            rightPaneBox.getChildren().add(rightVerticalSplitPane);

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


        LineChart<Number, Number> lineChart= createLineChart("nameOfCompany");


        // Define VBox for the left split pane and insert checkBoxes inside
        // VBox topLeftPane = new  VBox(10); //Box for other purpose
        VBox leftPaneBox = new VBox(10);// Box to contain the
        leftPaneBox.setAlignment(Pos.CENTER_LEFT);
        leftPaneBox.setPadding(new Insets(10));

        // Define an HBox to hold the elements inside the underGridPane(GridPane),see declaration below
        FlowPane topBox = new FlowPane(); // Use FlowPane to adjust the space as needed
        topBox.setAlignment(Pos.CENTER);
        topBox.setHgap(10);
        topBox.setVgap(10);

        // Create a label for the amount of money
        Label moneyLabel = new Label(Double.toString(userRegistered.getUserCredit())); // Replace with the actual amount
        moneyLabel.getStyleClass().add("money-label"); // You can define a CSS class for styling

        ArrayList<Stock>stocksBetOn = new ArrayList<>();
        Label listOfBetStock= new Label() ;

        /*
            In this foreach the Stock CheckBoxes will be defined. Next to the checkboxes a bet button is added.
            A tooltip is created together with the button where the amount of bet money is inserted
         */
        for (String symbol : symbols) {
            HBox checkBoxWithBetButton = new HBox(10);
            Button bet = new Button("Invest"); // Name of the bet buttons

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

            //Placeholders values in case something goes wrong with API call
            String sym = "phSymbol";
            String nameOfCompany = "phNameOfCompany";

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
                        updateDataHistory(1, 1.0, 1.0, 1.0, "", 1.0,1.0,1.0, "", "");
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
                if(!betAmountField.getText().isEmpty()) { // If the field is not empty
                    //Decreases the user's amount of money in the GUI label
                    userRegistered.setUserCredit(userRegistered.getUserCredit() - Double.parseDouble(betAmountField.getText()));
                    //Update the money label
                    moneyLabel.setText(Double.toString(userRegistered.getUserCredit()));
                    betAmountField.clear();
                    betTooltip.hide();
                    stocksBetOn.add(new Stock(symbol, nameOfCompany));

                    /*if (stocksBetOn.stream().anyMatch(stock -> {
                        return stock.getName().equals(symbol);
                    })) */
                        //updateListOfBetStockLabel(stocksBetOn, listOfBetStock);
                        topBox.getChildren().add(new Label(symbol));

                } else {
                    // Handle the error when you do not insert a number into the betAmountField, or it is empty

                    // Handling the error when you do not insert a number into the betAmountField, or it is empty

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
            leftPaneBox.getChildren().addAll(checkBoxWithBetButton);

        } // CLOSE FOREACH

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
                            AlertField.showSuccessAlert("Information","");
                        });
//                    MenuItem openItem = new MenuItem("Open");
//                    MenuItem saveItem = new MenuItem("Save");
                    MenuItem exitItem = new MenuItem("Exit");
                        // Exit the application if you click the exit item
                        exitItem.setOnAction(e -> {
                            System.exit(0); // Exit the application
                        });
                // Add menu items to the "File" menu and use a separator before the last element
                fileMenu.getItems().addAll(instructionsItem, new SeparatorMenuItem(), exitItem);

                // Create an "Edit" menu
                Menu editMenu = new Menu("Edit");
                    // Create menu items for the "Edit" menu
                    MenuItem cutItem = new MenuItem("Cut");
                    MenuItem copyItem = new MenuItem("Copy");
                    MenuItem pasteItem = new MenuItem("Paste");
                // Add menu items to the "Edit" menu
                editMenu.getItems().addAll(cutItem, copyItem, pasteItem);

                // Create a "LogOut" menu
                Menu logOutMenu = new Menu("LogOut");
                    MenuItem logOutItem = new MenuItem("Log out");
                        logOutItem.setOnAction(e->{
                            primaryStage.setTitle("Start App");
                            primaryStage.setScene(LoginScene);
                        });
                // Add menu items to the "LogOut" menu
                logOutMenu.getItems().addAll(logOutItem);

            // Add menus to the menu bar
            menuBar.getMenus().addAll(fileMenu, editMenu, logOutMenu);

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
        WelcomeScene = new Scene(mainPane, 1200, 700); // <---- Dimension of the WelcomePane
        WelcomeScene.getStylesheets().add("styles.css"); // Reference to the CSS file0

        // Define elements and HBox for the topRightPane
        Label username = new Label("Username");
//        username.setText(""); // Set the username as text of the label

        // Create an ImageView for the user image
        ImageView imgView = new ImageView();
        imgView.setFitWidth(80);
        imgView.setFitHeight(80);

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
        profilePic.setAlignment(Pos.CENTER);

        // Label for the stocks bet on
        String stringOfBetStocks = " Stocks bet on:\n";

        // Label with the list of Stock the user bet
        listOfBetStock = new Label(stringOfBetStocks);

        // Add all the elements into the tobBox
        topBox.getChildren().addAll(profilePic,username, moneyLabel, listOfBetStock);

        // Define an GridPane to better align logOut button. It contains the topBox
        GridPane underGridPane = new GridPane();
        underGridPane.setPadding(new Insets(10));
        underGridPane.setHgap(10);
        underGridPane.setVgap(0);
        underGridPane.setAlignment(Pos.CENTER);

        underGridPane.add(topBox, 0, 0);
        underGridPane.add(logOut, 6, 0);

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
