package org.project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;

public class SubmitControl extends Stock{ //Invest button + method to invest

    private HBox checkBoxInsideHBox = new HBox(10);
    private Button bet = new Button("Invest"); // Define bet button
    private Button buy = new Button("Buy"); // Define buy button
    private Button sell = new Button("Sell"); // Define sell button

    public SubmitControl(User userRegistered, Stage primaryStage, ArrayList<Stock> stocksCheckedOn, ArrayList<String> symbols, FlowPane list, String symbol, Label moneyLabel, CheckBox checkBox, PieChart pieChart) {
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
                        moneyLabel.setText(String.valueOf(userRegistered.getUserCredit()));
                        stocksCheckedOn.forEach(stock -> {
                            if(stock.getSymbol().equals(symbols))
                                stock.setAmountBet(stock.getAmountBetted()+Double.parseDouble(betField.getText()));
                        });
                        betField.clear();
                        betPopup.close();
                        /*if (stocksBetOn.stream().anyMatch(stock -> {
                            return stock.getName().equals(symbol);
                        })) */
                        //updateListOfBetStockLabel(stocksBetOn, listOfBetStock);


                        miniBox.getChildren().add(new Label());
                        miniBox.getChildren().add(new Label());


                        list.getChildren().add(new Label(symbol+" amount bet on"+", "+betField.getText()));


                        stocksCheckedOn.forEach(stock -> {
                            if(stock.getName().equals(symbol)) {
                                stock.setInvestedOn(true);
                                stock.saveStocks(userRegistered.getUsername(), stock.getName(),
                                        stock.getRegularMarketDayHigh(), stock.getRegularMarketDayLow(),
                                        stock.getRegularMarketOpen(), stock.getMarkerPreviousClose(),
                                        stock.getAmountBetted());
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
                            moneyLabel.setText(String.valueOf(userRegistered.getUserCredit()));
                            stocksCheckedOn.forEach(stock -> {
                                if(stock.getSymbol().equals(symbols))
                                    stock.setAmountBet(stock.getAmountBetted()+Double.parseDouble(buyField.getText()));
                            });

                            /*if (stocksBetOn.stream().anyMatch(stock -> {
                                return stock.getName().equals(symbol);
                            })) */

                            //updateListOfBetStockLabel(stocksBetOn, listOfBetStock);
                            Label upArrowLabel = new Label("\u2191");
                            upArrowLabel.setFont(Font.font(48));
//                            upArrowLabel.setFill(javafx.scene.paint.Color.GREEN);

                            list.getChildren().add(new Label(symbol +" "+upArrowLabel+" "+ buyField.getText()));

                            buyField.clear();
                            buyPopup.close();

                            stocksCheckedOn.forEach(stock -> {
                                if(stock.getName().equals(symbol)) {
                                    stock.setInvestedOn(true);
                                    stock.saveStocks(userRegistered.getUsername(), stock.getName(),
                                            stock.getRegularMarketDayHigh(), stock.getRegularMarketDayLow(),
                                            stock.getRegularMarketOpen(), stock.getMarkerPreviousClose(),
                                            stock.getAmountBetted());
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
                buyPopup.showAndWait(); // Use showAndWait to wait for user interaction before continuing

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
                    Label instruction = new Label("Amount you would like to sell");
                    TextField sellField = new TextField();
                    Button submitSellAmount = new Button("Submit");
                    Button closeSellPopul = new Button("Close");
                    HBox buttonsSellBox = new HBox(submitSellAmount, closeSellPopul);
                    buttonsSellBox.setSpacing(30);

                    // Define Box with elements for the popup (invest button)
                    VBox windowSellBox = new VBox(instruction, sellField, buttonsSellBox);
                    windowSellBox.setPadding(new Insets(10));
                    windowSellBox.setSpacing(10);
                    windowSellBox.setAlignment(Pos.CENTER);

                    //Handle submitSellAmount button
                    submitSellAmount.setOnAction(e->{
                        if(!sellField.getText().isEmpty()) { // If the field is not empty
                            try{
                                // Attempt to parse the text as a double. If parsing is successful, it's a valid number
                                Double.parseDouble(sellField.getText());

                                //Decreases the user's amount of money in the GUI label
                                userRegistered.setUserCredit(Double.parseDouble(userRegistered.getUserCredit()) - Double.parseDouble(sellField.getText()));
                                //Update the money label
                                moneyLabel.setText(String.valueOf(userRegistered.getUserCredit()));
                                stocksCheckedOn.forEach(stock -> {
                                    if(stock.getSymbol().equals(symbols))
                                        stock.setAmountBet(stock.getAmountBetted()+Double.parseDouble(sellField.getText()));
                                });
                                sellField.clear();
                                sellPopup.close();
                                    /*if (stocksBetOn.stream().anyMatch(stock -> {
                                        return stock.getName().equals(symbol);
                                    })) */
                                //updateListOfBetStockLabel(stocksBetOn, listOfBetStock);
                                list.getChildren().remove(new Label(symbol));
                                stocksCheckedOn.forEach(stock -> {
                                    if(stock.getName().equals(symbol)) {
                                        stock.setInvestedOn(true);
                                        stock.saveStocks(userRegistered.getUsername(), stock.getName(),
                                                stock.getRegularMarketDayHigh(), stock.getRegularMarketDayLow(),
                                                stock.getRegularMarketOpen(), stock.getMarkerPreviousClose(),
                                                stock.getAmountBetted());
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
                    closeSellPopul.setOnAction(e->{
                        sellPopup.close();
                    });

                    // Create a scene for the custom popup
                    Scene betPopupScene = new Scene(windowSellBox);
                    sellPopup.setScene(betPopupScene);

                    // Show the custom popup
                    sellPopup.showAndWait(); // Use showAndWait to wait for user interaction before continuing

                });// Close buy button handle code segment


        // Add the checkBoxes into the HBox
        checkBoxInsideHBox.getChildren().addAll(checkBox);



    }

    public HBox getBuyAndSell() {
        HBox buyAndSellBox = new HBox(buy,sell);
        buyAndSellBox.setAlignment(Pos.CENTER);
        buyAndSellBox.setSpacing(10);
        return buyAndSellBox;
    }

    public Button getBet() { return bet; }

    public HBox getCheckBoxInsideHBox() {
        return checkBoxInsideHBox;
    }
}
