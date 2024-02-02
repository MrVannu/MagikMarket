package org.project.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private final Button bet = new Button("Invest"); // Define bet button
    private final Button buy = new Button("Buy"); // Define buy button
    private final Button sell = new Button("Sell"); // Define sell button

    public SubmitController(User userRegistered, Stage primaryStage, ArrayList<Stock> stocksCheckedOn, HBox list, String symbol, Label moneyLabel,
                            CheckBox checkBox) {
        super();
        bet.getStyleClass().add("my-button");
        buy.getStyleClass().add("button-buy");
        sell.getStyleClass().add("button-sell");

        // Handle the "Bet" button action here
        bet.setOnAction(event -> {
            // Create a custom popup using a Stage (invest button)
            Stage betPopup = new Stage();
            betPopup.initModality(Modality.APPLICATION_MODAL); // Block user interaction with other windows
            betPopup.initOwner(primaryStage); // Set primaryStage as the parent of popup
            betPopup.setTitle("Bet Input");
            betPopup.setWidth(400);
            betPopup.setHeight(200);

            // Create UI elements for the custom popup (invest button)
            Label instruction = new Label("Set the bet amount");
            TextField betField = new TextField();
            Button submitBetAmount = new Button("Submit");
            Button closeBetPopup = new Button("Close");
            HBox buttonsBetBox = new HBox(submitBetAmount, closeBetPopup);
            buttonsBetBox.setSpacing(30);

            // Define Box with elements for the popup (invest button)
            VBox windowBetBox = new VBox(instruction, betField, buttonsBetBox);
            windowBetBox.setPadding(new Insets(10));
            windowBetBox.setSpacing(10);
            windowBetBox.setAlignment(Pos.CENTER);

            // Define mini Box where to show the stock invested on
            HBox miniBox = new HBox();
            miniBox.setAlignment(Pos.CENTER);

            //Handle submitBetAmount button
            submitBetAmount.setOnAction(e->{
                if(!betField.getText().isEmpty()) { // If the field is not empty
                    try{
                        // Attempt to parse the text as a double. If parsing is successful, it's a valid number
                        Double.parseDouble(betField.getText());

                        //Decreases the user's amount of money in the GUI label
                        userRegistered.setUserCredit(Double.parseDouble(userRegistered.getUserCredit()) - Double.parseDouble(betField.getText()));
                        //Update the money label
                        DecimalFormat decimalFormat = new DecimalFormat("#.00");
                        moneyLabel.setText(decimalFormat.format(userRegistered.getUserCredit()));



                        betField.clear();
                        betPopup.close();


                        miniBox.getChildren().add(new Label());
                        miniBox.getChildren().add(new Label());


                        list.getChildren().add(new Label(symbol+" amount bet on"+", "+betField.getText()));


                        stocksCheckedOn.forEach(stock -> {
                            if(stock.getName().equals(symbol)) {
                                stock.setInvestedOn(true);
                                stock.saveStocks(userRegistered.getUsername(), stock.getName(),
                                        stock.getRegularMarketDayHigh(), stock.getRegularMarketDayLow(),
                                        stock.getRegularMarketOpen(), stock.getMarkerPreviousClose(),
                                        stock.getAmountBetted(), stock.getRegularMarketPrice());
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
                    AlertField.showErrorAlert("Invalid input", "Please enter a bet amount.");
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

        });// Close bet button handle code segment


        // Handle the "Buy" button action here
        buy.setOnAction(event -> {
            // Create a custom popup using a Stage (invest button)
            Stage buyPopup = new Stage();
            buyPopup.initModality(Modality.APPLICATION_MODAL); // Block user interaction with other windows
            buyPopup.initOwner(primaryStage); // Set primaryStage as the parent of popup
            buyPopup.setTitle("Buy Input");
            buyPopup.setWidth(400);
            buyPopup.setHeight(200);

            // Create UI elements for the custom popup (invest button)
            Label instruction = new Label("Stock you would like to buy");
            TextField buyField = new TextField();
            Button submitBuyAmount = new Button("Submit");
            Button closeBuyPopup = new Button("Close");
            HBox buttonsBuyBox = new HBox(submitBuyAmount, closeBuyPopup);
            buttonsBuyBox.setSpacing(30);

            // Define Box with elements for the popup (invest button)
            VBox windowBuyBox = new VBox(instruction, buyField, buttonsBuyBox);
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


                        APIData apiResponseObj = new APIData();
                        apiResponseObj.fetchData(symbol);
                        saveStocks(userRegistered.getUsername(),symbol,apiResponseObj.regularMarketDayHigh(),
                                apiResponseObj.regularMarketDayLow(),apiResponseObj.regularMarketDayOpen(),
                                apiResponseObj.regularMarketPreviousClose(),Double.parseDouble(buyField.getText())*(-1),
                                apiResponseObj.regularMarketPrice()*(-1));

                        List<List<String>> theList = getSavedStocks(userRegistered.getUsername());
                        GridPane gridPane = new GridPane();
                        gridPane.setHgap(10); // Horizontal space between columns
                        list.getChildren().clear();

                        int rowIndex = 0;
                        for (List<String> outEl : theList) {
                            int columnIndex = 0;
                            outEl.set(0,"");
                            for (String innerEl : outEl) {
                                Text text = new Text(innerEl);
                                gridPane.add(text, columnIndex, rowIndex);
                                columnIndex++;
                            }
                            rowIndex++;
                        }


                        list.getChildren().add(gridPane);

                        buyField.clear();
                        buyPopup.close();

                        stocksCheckedOn.forEach(stock -> {
                            if(stock.getName().equals(symbol)) {
                                stock.setInvestedOn(true);
                                stock.saveStocks(userRegistered.getUsername(), stock.getName(),
                                        stock.getRegularMarketDayHigh(), stock.getRegularMarketDayLow(),
                                        stock.getRegularMarketOpen(), stock.getMarkerPreviousClose(),
                                        stock.getAmountBetted(),stock.getRegularMarketPrice());
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
                    AlertField.showErrorAlert("Invalid input", "Please enter a bet amount.");
                    System.out.println("Field is empty. Please enter a bet amount.");
                }
            });

            // Handle closeBetPopup button
            closeBuyPopup.setOnAction(e->{
                buyPopup.close();
            });

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
            sellPopup.setWidth(400);
            sellPopup.setHeight(200);

            // Create UI elements for the custom popup (invest button)
            Button submitSellAmount = new Button("Submit");
            Button closeSellPopul = new Button("Close");
            HBox buttonsSellBox = new HBox(submitSellAmount, closeSellPopul);
            buttonsSellBox.setSpacing(30);


            Label instructions = new Label("Select the percentage you would like to sell, or insert the amount of stock you would like to sell");
            Label regMarkPriceL = new Label();
            APIData regPriceAPI = new APIData();
            regPriceAPI.fetchData(symbol);
            regMarkPriceL.setText(String.valueOf(regPriceAPI.regularMarketPrice()));

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
            specificValueField.setPromptText("Enter value");

            // Create the layout for the popup
            HBox radioButtonsBox = new HBox(10);
            radioButtonsBox.setPadding(new Insets(10));
            radioButtonsBox.getChildren().addAll(radio25, radio50, radio75, radio100);

            // Define Box with elements for the popup (invest button)
            VBox windowSellBox = new VBox(instructions, regMarkPriceL, radioButtonsBox, specificValueField, buttonsSellBox);
            windowSellBox.setPadding(new Insets(10));
            windowSellBox.setSpacing(10);
            windowSellBox.setAlignment(Pos.CENTER);

            //Handle submitSellAmount button
            submitSellAmount.setOnAction(e -> {
                double nrOfStock = getSumAndPieces(userRegistered.getUsername(), symbol);

                if(nrOfStock == 0){
                    //System.out.println("NO STOCKS OWNED BY THE CURRENT USER");
                    throw new InsufficientCreditException();
                }

                double selectedPercentage = 0.00;
                try {
                    if (radio25.isSelected()) selectedPercentage = 25.0;
                    else if (radio50.isSelected()) selectedPercentage = 50.0;
                    else if (radio75.isSelected()) selectedPercentage = 75.0;
                    else if (radio100.isSelected()) selectedPercentage = 100.0;

                    double finalGain;

                    if (selectedPercentage > 0) {
                        // Use percentage value
                        double percentageValue = nrOfStock * (selectedPercentage / 100.0);
                        finalGain = percentageValue * Double.parseDouble(regMarkPriceL.getText());
                    } else {
                        // Use manually entered value
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
                            regPriceAPI.regularMarketPreviousClose(), (-1) * finalGain, regPriceAPI.regularMarketPrice());
                }
                catch (NumberFormatException ex) {
                    System.err.println("Invalid input value. Please enter a valid number.");
                }

            });





            // Handle closeBetPopup button
            closeSellPopul.setOnAction(e->{
                sellPopup.close();
            });

            // Create a scene for the custom popup
            Scene betPopupScene = new Scene(windowSellBox);
            sellPopup.setScene(betPopupScene);

            // Show the custom popup
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
    public void showStocks(User userRegistered, HBox list){
        List<List<String>> theList = getSavedStocks(userRegistered.getUsername());
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Horizontal space between columns
        list.getChildren().clear();

        int rowIndex = 0;
        for (List<String> outEl : theList) {
            int columnIndex = 0;
            outEl.set(0,"");
            for (String innerEl : outEl) {
                Text text = new Text(innerEl);
                gridPane.add(text, columnIndex, rowIndex);
                columnIndex++;
            }
            rowIndex++;
        }
        // LIST.REVERT to be implemented by luca :)


        list.getChildren().add(gridPane);


    }

    public Button getBet() { return bet; }

    public HBox getCheckBoxInsideHBox() {
        return checkBoxInsideHBox;
    }
}