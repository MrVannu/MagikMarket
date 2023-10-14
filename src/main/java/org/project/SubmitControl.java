package org.project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;

public class SubmitControl {

    private HBox checkBoxInsideHBox = new HBox(10);
    private Button bet = new Button("Invest"); // Name of the bet buttons

    public SubmitControl(User userRegistered, Stage primaryStage, ArrayList<Stock> stocksCheckedOn, ArrayList<String> symbols, FlowPane topBox, String symbol, Label moneyLabel, CheckBox checkBox) {
        bet.getStyleClass().add("my-button");
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
                        stocksCheckedOn.forEach(stock -> {
                            if(stock.getSymbol().equals(symbols))
                                stock.setAmountBet(stock.getAmountBet()+Double.parseDouble(betField.getText()));
                        });
                        betField.clear();
                        betPopup.close();
                        /*if (stocksBetOn.stream().anyMatch(stock -> {
                            return stock.getName().equals(symbol);
                        })) */
                        //updateListOfBetStockLabel(stocksBetOn, listOfBetStock);
                        topBox.getChildren().add(new Label(symbol));
                        stocksCheckedOn.forEach(stock -> {
                            if(stock.getName().equals(symbol)) {
                                stock.setInvestedOn(true);
                                stock.saveStocks(userRegistered.getUsername(), stock.getName(),
                                        stock.getRegularMarketDayHigh(), stock.getRegularMarketDayLow(),
                                        stock.getRegularMarketOpen(), stock.getMarkerPreviousClose(),
                                        stock.getAmountBet());
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
        });

        // Add the checkBoxes into the HBox
        checkBoxInsideHBox.getChildren().addAll(checkBox);

    }

    public Button getBet() {
        return bet;
    }

    public HBox getCheckBoxInsideHBox() {
        return checkBoxInsideHBox;
    }
}
