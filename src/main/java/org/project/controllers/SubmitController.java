package org.project.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.project.exceptions.InsufficientCreditException;
import org.project.model.APIData;
import org.project.model.Stock;
import org.project.model.User;
import org.project.util.AlertField;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SubmitController extends Stock { //Invest button + method to invest

    private final HBox checkBoxInsideHBox = new HBox(10);
    private final Button buy = new Button("Buy"); // Define buy button
    private final Button sell = new Button("Sell"); // Define sell button

    public SubmitController(User userRegistered, Stage primaryStage, ArrayList<Stock> stocksCheckedOn, HBox list, String symbol, Label moneyLabel,
                            CheckBox checkBox) {
        super();

        // Define buttons
        buy.getStyleClass().add("button-buy");
        sell.getStyleClass().add("button-sell");

        // Handle the "Buy" button action here
        buy.setOnAction(event -> {
            // Create a custom popup using a Stage
            Stage buyPopup = new Stage();
            buyPopup.initModality(Modality.APPLICATION_MODAL); // Block user interaction with other windows
            buyPopup.initOwner(primaryStage); // Set primaryStage as the parent of popup
            buyPopup.setTitle("Buy Input");
            buyPopup.setWidth(600);
            buyPopup.setHeight(300);

            // Create UI elements for the custom popup and set corresponding layout

            VBox verticalBuyBoxWithInstructions = new VBox(); // VBox with instructions and buyField
            verticalBuyBoxWithInstructions.setAlignment(Pos.CENTER);
            Text buyInstructions = new Text("Insert the amount of stock you would like to buy: ");
            buyInstructions.setStyle("-fx-font-family: 'Helvetica'; -fx-font-weight: bold;");

            TextField buyField = new TextField();
            buyField.setPromptText("Enter value here");

            Button submitBuyAmount = new Button("Submit");
            Button closeBuyPopup = new Button("Close");
            HBox buttonsBuyBox = new HBox(submitBuyAmount, closeBuyPopup); // HBox with all buttons
            buttonsBuyBox.setSpacing(30);

            verticalBuyBoxWithInstructions.getChildren().addAll(buyInstructions, buyField);
            verticalBuyBoxWithInstructions.setSpacing(10);
            VBox buyBoxWithAllComponents = new VBox(verticalBuyBoxWithInstructions, buttonsBuyBox); //VBox with all UI components

            buyBoxWithAllComponents.setSpacing(10);

            // Define Box with elements for the popup
            VBox windowBuyBox = new VBox(buyBoxWithAllComponents);
            windowBuyBox.setPadding(new Insets(10));
            windowBuyBox.setSpacing(10);
            windowBuyBox.setAlignment(Pos.CENTER);

            //Handle submitBuyAmount button
            submitBuyAmount.setOnAction(e->{
                if(!buyField.getText().isEmpty()) { // If the field is not empty
                    try{
                        // Attempt to parse the text as a double. If parsing is successful, it's a valid number
                        Double.parseDouble(buyField.getText());

                        //Decreases the user's amount of money in the GUI label
                        userRegistered.setUserCredit(Double.parseDouble(userRegistered.getUserCredit()) - Double.parseDouble(buyField.getText()));
                        //Update the money label

                        /* In this section a gridPane is created with all the buy movements of the user. Then it is set
                         * into the layout.
                         */
                        moneyLabel.setText(String.valueOf(Double.parseDouble(moneyLabel.getText()) - Double.parseDouble(buyField.getText())));
                        APIData apiResponseObj = new APIData();
                        apiResponseObj.fetchData(symbol);
                        saveStocks(userRegistered.getUsername(),symbol,apiResponseObj.regularMarketDayHigh(),
                                apiResponseObj.regularMarketDayLow(),apiResponseObj.regularMarketDayOpen(),
                                apiResponseObj.regularMarketPreviousClose(),Double.parseDouble(buyField.getText())*(-1),
                                apiResponseObj.regularMarketPrice()*(-1));



                        buyField.clear();
                        buyPopup.close();

                        stocksCheckedOn.forEach(stock -> {
                            if(stock.getName().equals(symbol)) {
                                stock.setInvestedOn();
                                stock.saveStocks(userRegistered.getUsername(), stock.getName(),
                                        stock.getRegularMarketDayHigh(), stock.getRegularMarketDayLow(),
                                        stock.getRegularMarketOpen(), stock.getMarkerPreviousClose(),
                                        stock.getAmountInvested(),stock.getRegularMarketPrice());
                            }
                        });
                    }
                    catch(NumberFormatException er){
                        // Parsing failed, the text is not a valid number
                        // Handle the case where the input is not a number
                        AlertField.showErrorAlert("Invalid input", "Please enter a valid number.");
                        System.out.println("Invalid input. Please enter a valid number.");
                    }
                } else {
                    // The field is empty
                    AlertField.showErrorAlert("Invalid input", "Please enter a invest amount.");
                    System.out.println("Field is empty. Please enter a invest amount.");
                }
            });

            // Handle closeBetPopup button
            closeBuyPopup.setOnAction(e-> buyPopup.close());

            // Create a scene for the custom popup
            Scene betPopupScene = new Scene(windowBuyBox);
            buyPopup.setScene(betPopupScene);

            // Show the custom popup
            buyPopup.showAndWait();

        });// Close buy button handle code segment



        // Handle the "Sell" button action here
        sell.setOnAction(event -> {
            // Create a custom popup using a Stage (invest button)
            Stage sellPopup = new Stage();
            sellPopup.initModality(Modality.APPLICATION_MODAL); // Block user interaction with other windows
            sellPopup.initOwner(primaryStage); // Set primaryStage as the parent of popup
            sellPopup.setTitle("Sell Input");
            sellPopup.setWidth(700);
            sellPopup.setHeight(300);

            // Create UI elements for the custom popup and define corresponding layout
            Button submitSellAmount = new Button("Submit");
            Button closeSellPopup = new Button("Close");
            HBox buttonsSellBox = new HBox(submitSellAmount, closeSellPopup);
            buttonsSellBox.setSpacing(30);

            Label sellInstructions = new Label("Select the percentage you would like to sell, or insert the amount of stock you would like to sell.");
            sellInstructions.setFont(Font.font("Helvetica", FontWeight.NORMAL, 14));

            Label regMarkPriceL = new Label();

            // Define object to get current regularMarketPrice of corresponding Stock to be shown for the user
            APIData regPriceAPI = new APIData();
            regPriceAPI.fetchData(symbol);
            regMarkPriceL.setText(String.valueOf(regPriceAPI.regularMarketPrice()));

            Text regMrkPriceText = new Text("Current market price of " + "\"" + symbol +"\"" + " is → ");
            regMrkPriceText.setFont(Font.font("Helvetica", FontWeight.NORMAL, 14));
            Text regMrkPriceValue = new Text(String.valueOf(regPriceAPI.regularMarketPrice()));
            regMrkPriceValue.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
            regMrkPriceValue.setFill(Color.ORANGE);
            HBox regMrkPriceTextAndValueBox = new HBox(regMrkPriceText,regMrkPriceValue);
            regMrkPriceTextAndValueBox.setAlignment(Pos.CENTER);

            // Create radio buttons for percentage options
            RadioButton radio25 = new RadioButton("25%");
            RadioButton radio50 = new RadioButton("50%");
            RadioButton radio75 = new RadioButton("75%");
            RadioButton radio100 = new RadioButton("100%");

            // Create a ToggleGroup to ensure only one radio button is selected at a time
            ToggleGroup toggleGroup = new ToggleGroup();
            radio25.setToggleGroup(toggleGroup);
            radio50.setToggleGroup(toggleGroup);
            radio75.setToggleGroup(toggleGroup);
            radio100.setToggleGroup(toggleGroup);

            // Create a TextField for entering a specific value
            TextField specificValueField = new TextField();
            specificValueField.setPromptText("Enter value here");

            // Create the layout for the popup
            HBox radioButtonsBox = new HBox(10);
            radioButtonsBox.setPadding(new Insets(10));
            radioButtonsBox.setAlignment(Pos.CENTER);
            radioButtonsBox.getChildren().addAll(radio25, radio50, radio75, radio100);

            // Define Box with elements for the popup
            VBox windowSellBox = new VBox(sellInstructions, regMrkPriceTextAndValueBox, radioButtonsBox, specificValueField, buttonsSellBox);
            windowSellBox.setPadding(new Insets(10));
            windowSellBox.setSpacing(10);
            windowSellBox.setAlignment(Pos.CENTER);

            if(radio25.isSelected() || radio50.isSelected() || radio75.isSelected() || radio100.isSelected()) specificValueField.setDisable(true);

            // Handle submitSellAmount button
            submitSellAmount.setOnAction(e -> {
                //getSumAndPieces(userRegistered.getUsername(), symbol);
                String piecesOwned = String.valueOf(getSumAndPieces(userRegistered.getUsername(), symbol));
                System.out.println(piecesOwned);
                double nrOfStock = Double.parseDouble(piecesOwned);
                System.out.println(nrOfStock);
                if(nrOfStock == 0){
                    throw new InsufficientCreditException();
                }

                double selectedPercentage = 0.00;
                try {
                    if (radio25.isSelected()){
                        selectedPercentage = 25.0;
                        specificValueField.setDisable(true);
                        specificValueField.setText("");
                    }
                    else if (radio50.isSelected()){
                        selectedPercentage = 50.0;
                        specificValueField.setDisable(true);
                        specificValueField.setText("");
                    }
                    else if (radio75.isSelected()){
                        selectedPercentage = 75.0;
                        specificValueField.setDisable(true);
                        specificValueField.setText("");
                    }
                    else if (radio100.isSelected()){
                        selectedPercentage = 100.0;
                        specificValueField.setDisable(true);
                        specificValueField.setText("");
                    }

                    double finalGain;

                    if (selectedPercentage > 0) { // Use percentage value
                        double percentageValue = nrOfStock * (selectedPercentage / 100.0);
                        finalGain = percentageValue * Double.parseDouble(regMarkPriceL.getText());
                    } else { // Use manually entered value
                        specificValueField.setDisable(false);
                        double manualValue = Double.parseDouble(specificValueField.getText());
                        finalGain = manualValue * Double.parseDouble(regMarkPriceL.getText());
                    }

                    double newUserCredit = Double.parseDouble(userRegistered.getUserCredit()) + finalGain;

                    DecimalFormat decimalFormat = new DecimalFormat("#.00");
                    moneyLabel.setText(decimalFormat.format(newUserCredit));

                    userRegistered.setUserCredit(newUserCredit);

                    specificValueField.clear();
                    sellPopup.close();

                    // Save stock information to the database
                    saveStocks(userRegistered.getUsername(), symbol, regPriceAPI.regularMarketDayHigh(),
                            regPriceAPI.regularMarketDayLow(), regPriceAPI.regularMarketDayOpen(),
                            regPriceAPI.regularMarketPreviousClose(), (-1) *finalGain, regPriceAPI.regularMarketPrice());
                }
                catch (NumberFormatException ex) {
                    System.err.println("Invalid input value. Please enter a valid number.");
                }

            });

            // Handle closeBetPopup button
            closeSellPopup.setOnAction(e-> sellPopup.close());

            // Create a scene for the custom popup and show it
            Scene betPopupScene = new Scene(windowSellBox);
            sellPopup.setScene(betPopupScene);
            sellPopup.showAndWait(); // Use showAndWait to wait for user interaction before continuing

        });

        // Add the checkBoxes into the HBox
        checkBoxInsideHBox.getChildren().addAll(checkBox);

    }

    public HBox getBuyAndSell() {
        HBox buyAndSellBox = new HBox(buy,sell);
        buyAndSellBox.setAlignment(Pos.CENTER);
        buyAndSellBox.setSpacing(10);
        return buyAndSellBox;
    }

    public HBox getCheckBoxInsideHBox() {
        return checkBoxInsideHBox;
    }
}