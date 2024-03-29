package org.project.view;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.project.controllers.PrevisionController;
import org.project.controllers.SubmitController;
import org.project.exceptions.AmountNotAllowedException;
import org.project.model.APIData;
import org.project.model.Stock;
import org.project.model.User;
import org.project.util.AlertField;
import org.project.util.LineChartGenerator;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;


public class WelcomePane extends APIData { // Extends APIData to use data from the API
    Scene WelcomeScene;
    private static final int MAX_SELECTED_CHECKBOXES = 4;
    private final APIData testObj  = new APIData();

    public WelcomePane(Stage primaryStage, Scene LoginScene, User userRegistered){
        super();

        // Define a list of checkBoxes
        List<CheckBox> checkBoxesArrayList = new ArrayList<>();
        ArrayList<String> symbolsOfStock = new ArrayList<>();
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

        /*
         * Define the vertical SplitPane to contain the topRightPane (that will contain the user's transactions list on the left and
         * the pie chart on the right), and the bottomRightPane that will contain the lineChart. This SplitPane is inserted
         * on the rightPane of the mainSplitPane (defined on the Main Scenes Definition Section).
         */
        SplitPane rightVerticalSplitPane = new SplitPane();
        rightVerticalSplitPane.setDividerPositions(0.1, 0.6); // Set SplitPane dimension
        rightVerticalSplitPane.getItems().addAll(topRightPane, bottomRightPane); // Add the pane inside the SplitPane
        rightVerticalSplitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);

        // Add the internalVerticalSplitPane to the right pane (of the main splitPane)
        rightPaneBox.getChildren().add(rightVerticalSplitPane);

        // Create line chart with title
        LineChart<Number, Number> lineChart= LineChartGenerator.createLineChart("Choose a company");

        // Create GridPane to show into a grid the checkBoxes and relative sell and buy buttons
        GridPane checkBoxesGridPane = new GridPane();
        checkBoxesGridPane.setHgap(10);
        checkBoxesGridPane.setVgap(10);
        checkBoxesGridPane.setPadding(new Insets(10));
        // Define counter used when inserting checkBoxes into gridPane
        int numOfBoxes = 0;

        // Create a HBox to contain gridPane with checkBoxes
        HBox lowerLeftBox = new HBox(checkBoxesGridPane);
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

        // Create a label to show the user's amount of money on the top of the application
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double userCredit = userRegistered.getUserCredit() != null ? Double.parseDouble(userRegistered.getUserCredit()) : 0.0;
        Label moneyLabel = new Label(decimalFormat.format(userCredit));
        moneyLabel.getStyleClass().add("money-label");

        // Define array to insert the stocks that will be checked by the user
        ArrayList<Stock> stocksCheckedOn = new ArrayList<>();


        // Define box to contain hBoxUserTransactionHistoryList for layout purposes
        HBox investmentBox = new HBox();
        investmentBox.setAlignment(Pos.CENTER);
        // Define HBox to contain a list of the user's transaction history of the stocks
        HBox hBoxUserTransactionHistoryList = new HBox();
        hBoxUserTransactionHistoryList.setAlignment(Pos.CENTER);

        // Create pie chart
        PieChart pieChart = new PieChart();


        /*
         * Section with a foreach to define and handle action of each checkBox.
         */
        for (String symbol : symbolsOfStock) {
            CheckBox checkBox = new CheckBox(symbol);
            checkBox.getStyleClass().add("checkbox");

            // Checkbox creating a new series for each checkbox
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(symbol); // Set the name of the series

            // Handle CheckBox action foreach checkBox
            checkBox.setOnAction(event -> {
                // For limiting the selection of more than one checkbox (i.e. max 4 check boxes)
                int selectedCount = (int) checkBoxesArrayList.stream().filter(CheckBox::isSelected).count();
                if (selectedCount > MAX_SELECTED_CHECKBOXES) { // If limit of selection is greater than "MAX_SELECTED_CHECKBOXES"
                    checkBox.setSelected(false);

                    // Disable the not selected checkboxes when limit is reached
                    for (CheckBox cb : checkBoxesArrayList) {
                        if (!cb.isSelected()) {
                            cb.setDisable(true);
                        }
                    }
                } else {
                    checkBoxesArrayList.forEach(cb -> cb.setDisable(false));
                }

                // Showing the data on the lineChart and pieChart when the checkBox is selected
                if (checkBox.isSelected()) {

                        for (CheckBox cb : checkBoxesArrayList) {
                            if (cb.isSelected()) {
                                testObj.fetchData(cb.getText()); // !WARNING: this line requires API usage
                            }
                        }


                        // Define the lineChart with all the data
                        // Create a new series for this checkBox
                        XYChart.Series<Number, Number> lineChartSeries = new XYChart.Series<>();

                        // Define caption; if testObj is null then placeHolderString as name, otherwise SymbolOfCompany as name of the series
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
                        PieChart.Data newData = new PieChart.Data(symbol, stock.getVolume());

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
                        double averageDailyVolume = 0.0;
                        if(!averageDailyVolumeFmt.isEmpty()) averageDailyVolume = testObj.parseFormattedValue(averageDailyVolumeFmt);

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
            }); // Close checkBox action

            // Define instance of SubmitControl and pass all parameters
            SubmitController submitController = new SubmitController(userRegistered, primaryStage, stocksCheckedOn, hBoxUserTransactionHistoryList, symbol, moneyLabel, checkBox);

            // Add the checkBox into the ArrayList
            checkBoxesArrayList.add(checkBox);

            //Using the foreach to increment the counter to insert the checkboxes and buy + sell buttons
            checkBoxesGridPane.add(submitController.getCheckBoxInsideHBox(), 0, numOfBoxes);
            checkBoxesGridPane.add(submitController.getBuyAndSell(), 1, numOfBoxes);
            numOfBoxes++;

        } // Close for each

        // Insert lineChart into layout
        rightVerticalSplitPane.getItems().addAll(lineChart);

        // Add the checkBoxes to layout
        leftPaneBox.getChildren().addAll(lowerLeftBox);
        VBox.setVgrow(lowerLeftBox, Priority.ALWAYS); // Let lowerLeftBox occupy all the space inside the VBox


        /*
         * Section with logic and layout code for the Prevision and Switch History buttons.
         * This section handles the functionality related to switching to the prediction view and switching
         * between different user's transaction historical views.
         */
            // Define instance of the prevision class to show UI components of prevision
            PrevisionController previsionController = new PrevisionController(stocksCheckedOn);

            // Define switchPane instance to get formatted data to show into layout
            SwitchPane switchPane = new SwitchPane();
            switchPane.showStocks(userRegistered, hBoxUserTransactionHistoryList);
            // Define switch button to switch between the user's transaction historical views
            Button switchHistory = new Button("Switch pane");
            switchHistory.setOnAction(e->{
                if(switchPane.toggle) {
                    switchPane.showOtherView(userRegistered, hBoxUserTransactionHistoryList);

                }
                else {
                    switchPane.showStocks(userRegistered, hBoxUserTransactionHistoryList);
                }
            });
            // Set both the buttons of the same width
            switchHistory.setMaxWidth(100);

            // Define the VBox in the lower left part of the UI containing the previsionButton and switchHistoryButton
            VBox previsionAndSwitchButton = new VBox(previsionController.getPrevisionHBox(), switchHistory);
            previsionAndSwitchButton.setSpacing(10);
            previsionAndSwitchButton.setAlignment(Pos.CENTER);
            previsionAndSwitchButton.setPadding(new Insets(0, 0, 50,0)); // Padding to move the button higher

            // Add the Box with prevision button to the leftPaneBox (it is below the checkboxes)
            leftPaneBox.getChildren().addAll(previsionAndSwitchButton);
            leftPaneBox.setSpacing(10);


        /*
         * Section with logic and layout code to insert the defined list of the user's transactions history and pie chart
         * to the layout.
         */
            // Set title to pie chart
            pieChart.setTitle("Avg. 3 Month Volume");

            // Add the list of data that the user invested on
            investmentBox.getChildren().add(hBoxUserTransactionHistoryList);

            // Define a SplitPane for inserting PieChart and InvestmentBox
            SplitPane investmentAndPieSplitPane = new SplitPane();
            investmentAndPieSplitPane.getItems().addAll(hBoxUserTransactionHistoryList,pieChart); //investmentBox,
            investmentAndPieSplitPane.setDividerPositions(0.735);

            // Add the line chart to the bottomRightPane
            bottomRightPane.getChildren().addAll(investmentAndPieSplitPane);



        /*
         * Section with definition of the components, logic and layout of the menuBar. In this section the following items
         * are defined:
         * popup showing the instruction, the menu item to exit the application, the menu item to modify the current amount of the user
         * (with relative action handler),
         *
         */
            // Define the menu bar in the top of the mainPain
            MenuBar menuBar = new MenuBar();// Create a menu bar

            // Create instruction string
            String instructions = """
                    This application is a simulation designed to provide insights into the real stock market. Users can buy and sell stocks, and the home page offers a comprehensive overview of their transactions. By selecting up to four stocks from the checkboxes, users can closely monitor their chosen investments.\s
                    Every user begins with a default amount of $1,000, which can be modified in the edit menu. The invested amount directly affects the user's available funds. The simulation shows a prediction of the chosen stocks (by choosing the checkbox).\s
                     Clicking the 'Prevision' button introduces random data changes to the chart, simulating market fluctuations. A decrease in the chart results in a loss, while an increase indicates a profit.\s
                     The Pie chart provides insights into the market volume of a selected stock, while the line chart illustrates the stock's performance over time. This data is crucial for understanding the liquidity and trading activity of the stock, aiding users in making informed investment decisions.\s
                     For more details have a look at the ReadMe""";

            // Create a "Info" menu
            Menu infoMenu = new Menu("Info");
            // Create menu items for the "File" menu
            MenuItem instructionsItem = new MenuItem("Instructions");
            instructionsItem.setOnAction(e-> showInstructionsDialog(instructions));
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
            MenuItem amountItem = new MenuItem("Set new amount");

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
                                popup.close();
                            }
                        } catch (NumberFormatException k) {
                            newCredit.setText(""); // Clean out
                        }
                    } else {
                        System.out.println("ERR: No valid input");
                        throw new AmountNotAllowedException();
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




        /*
         * Main Scenes Definition Section:
         * - mainSplitPane: SplitPane containing leftPaneBox (with the application logo, checkBoxes and prevision & switchHistory buttons)
         *       and rightPaneBox (containing the rightVerticalSplitPane).
         * - mainPane: BorderPane with mainSplitPane and menuBar
         * - WelcomeScene: Scene with mainPane and dimension of the scene
         */
            SplitPane mainSplitPane = new SplitPane();
            mainSplitPane.getItems().addAll(leftPaneBox, rightPaneBox);
            mainSplitPane.setDividerPositions(0.15, 0.6); // Configure SplitPane dimension

            BorderPane mainPane = new BorderPane(mainSplitPane);
            mainPane.setTop(menuBar);

            WelcomeScene = new Scene(mainPane, 1900, 900); // <---- Dimension of the WelcomeScene
            WelcomeScene.getStylesheets().add("styles.css"); // Reference to the CSS file




        /*
         * Top Right Pane Elements:
         * - User information display: Username label and image with style
         * - Image selection functionality with FileChooser to insert image
         * - UI layout for profile picture and image insert/update button
         * - Display of user's available amount
         * - Separators for visual separation
         * - Final insert of the elements into topRightPane
         */
            Label username = new Label(userRegistered.getUsername());
            username.setTextFill(Color.rgb(255, 164, 94));

            Label textUsername = new Label("Username: ");
            textUsername.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
            textUsername.setTextFill(Color.rgb(92, 49, 144));

            ImageView imgView = new ImageView();
            imgView.setFitWidth(30); // Width of the image
            imgView.setFitHeight(30); // Height of the image

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
            );



        HBox profilePic = gethBox(primaryStage, fileChooser, imgView);

        Label amountLabel = new Label("Your amount: ");
            amountLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
            amountLabel.setTextFill(Color.rgb(92, 49, 144));

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

            topBox.getChildren().addAll(profilePic, separator1, textUsername, username, separator2, moneyBox);
            topRightPane.getChildren().addAll(topBox);





        /*
         *Section to define alert triggered when closing the application
         */

            primaryStage.setOnCloseRequest(event -> {
                event.consume();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Exit the application");
                alert.setHeaderText("Are you sure you want to close the current session?");
                alert.setContentText("Your data are already saved! \n\n\n Click on \"Ok\" to exit");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        System.out.println("\nGoodbye!");
                        primaryStage.close();
                    }
                });
            });


    }

    private static HBox gethBox(Stage primaryStage, FileChooser fileChooser, ImageView imgView) {
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
            }
        });

        // Define elements for the UI
        HBox profilePic = new HBox(imgView, selectImageButton);
        profilePic.setAlignment(Pos.CENTER);
        profilePic.setSpacing(10);
        return profilePic;
    }

    private void showInstructionsDialog(String instructions) {
        Stage instructionsStage = new Stage();
        instructionsStage.setTitle("Instructions");

        Label text = new Label(instructions);
        text.setWrapText(true); // Set wrapText to true for line breaks
        text.setStyle("-fx-font-family: 'Helvetica'; -fx-font-size: 14; -fx-padding: 10px; -fx-text-alignment: justify;");

        Label title = new Label("Instructions");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        VBox vbox = new VBox(title, text);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);

        Scene scene = new Scene(vbox, 600, 400);
        instructionsStage.setScene(scene);

        instructionsStage.show();
    }


    public Scene getScene(){
        return WelcomeScene;
    }

}