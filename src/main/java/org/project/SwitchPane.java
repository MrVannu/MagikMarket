package org.project;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SwitchPane extends Stock{
    public boolean toggle = false;
    private final APIData apiDataObject = new APIData();


    public SwitchPane (){}

    // Show user's stock buy/sell operation
    public void showStocks(User userRegistered, HBox list) {
        list.getChildren().clear();

        List<List<String>> theList = getSavedStocks(userRegistered.getUsername());
        GridPane gridPane = new GridPane();
        gridPane.setHgap(25); // Horizontal space between columns
        gridPane.setVgap(20); // Vertical space between rows

        // Define headers for each column
        Text stock = new Text("User");
        Text stock1 = new Text("Stock name");
        Text stock2 = new Text("Regular Market Day High");
        Text stock3 = new Text("Regular Market Day Low");
        Text stock4 = new Text("Regular Market Open");
        Text stock5 = new Text("Market Previous Close");
        Text dollarsSpent = new Text("Money spent/earned");
        Text dateOfTransaction = new Text("Transaction date");
        Text pieceOfStockGotten = new Text("Amount held ");

        // Define style for each column header
        stock.setStyle("-fx-font-weight: bold;");
        stock1.setStyle("-fx-font-weight: bold;");
        stock2.setStyle("-fx-font-weight: bold;");
        stock3.setStyle("-fx-font-weight: bold;");
        stock4.setStyle("-fx-font-weight: bold;");
        stock5.setStyle("-fx-font-weight: bold;");
        dollarsSpent.setStyle("-fx-font-weight: bold;");
        dateOfTransaction.setStyle("-fx-font-weight: bold;");
        pieceOfStockGotten.setStyle("-fx-font-weight: bold;");

        // Insert the column's header into the gridPane
        gridPane.add(stock,0,0);
        gridPane.add(stock1,1,0);
        gridPane.add(stock2,2,0);
        gridPane.add(stock3,3,0);
        gridPane.add(stock4,4,0);
        gridPane.add(stock5,5,0);
        gridPane.add(dollarsSpent,6,0);
        gridPane.add(dateOfTransaction,7,0);
        gridPane.add(pieceOfStockGotten,8,0);

        // Define a decimal format object to format the numbers of the grid pane
        DecimalFormat decimalFormat = new DecimalFormat("#.####");

        int rowIndex = 1;
        for (List<String> outEl : theList) {
            int columnIndex = 0;
            for (String innerEl : outEl) {
                Text text = new Text(innerEl);
                gridPane.add(text, columnIndex, rowIndex);
                columnIndex++;

                if(columnIndex==9){
                    double number = Double.parseDouble(text.getText());
                    text.setText(decimalFormat.format(number));
                }
            }
            rowIndex++;
        }

        list.getChildren().add(gridPane);
        this.toggle = !this.toggle;
    }

    // Add the labels to the gridPane
    private void addStockInfoToGrid(GridPane gridPane, Label nameLabel, Label priceLabel, Label piecesLabel, Label averageLabel, int row) {
        gridPane.add(nameLabel, 0, row);
        gridPane.add(priceLabel, 1, row);
        gridPane.add(piecesLabel, 2, row);
        gridPane.add(averageLabel, 3, row);
    }

    // Fetch data to update values into the gridPane
    public void fetchUpdatesRealTimeBoard(APIData obj, String symbol, Label currentPriceLabel, Label piecesOwnedLabel,
                                          Label averageBuyPriceLabel, String username) {
        obj.fetchData(symbol.toLowerCase());

        String currentPrice = String.valueOf(obj.regularMarketPrice());
        String piecesOwned = String.valueOf(getSumAndPieces(username, symbol.toLowerCase()));
        String averageBuyPrice = String.valueOf(getAverageOfPurchased(username, symbol));

        Platform.runLater(() -> {
            currentPriceLabel.setText(currentPrice);
            piecesOwnedLabel.setText(piecesOwned);
            averageBuyPriceLabel.setText(averageBuyPrice);

            // Set color based on gain possibility
            setColorOfLabelsBasingOnGainPossibility(currentPriceLabel, new Label(averageBuyPrice));
        });
    }


    private void setColorOfLabelsBasingOnGainPossibility(Label actualPrice, Label purchasedPrice){
        if(Double.parseDouble(actualPrice.getText()) > Double.parseDouble(purchasedPrice.getText())){
            actualPrice.setStyle("-fx-text-fill: darkgreen; -fx-background-color: rgba(144, 238, 144, 0.3);");
        }
        else if (Double.parseDouble(actualPrice.getText()) < Double.parseDouble(purchasedPrice.getText())){
            actualPrice.setStyle("-fx-text-fill: darkred; -fx-background-color: rgba(255, 99, 71, 0.3);");
        }
        else {
            actualPrice.setStyle("-fx-text-fill: darkgray; -fx-background-color: rgba(169, 169, 169, 0.3);");
        }
    }

    // Show the real time stocks board
    public void showOtherView (User userRegistered, HBox list){
        list.getChildren().clear();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(40);
        gridPane.setVgap(20);

        Label symbolName = new Label("SYMBOL");
        symbolName.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        Label currentPrice = new Label("CURRENT PRICE");
        currentPrice.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        Label piecesOwned = new Label("PIECES OWNED");
        piecesOwned.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        Label averageBuyPrice = new Label("AVERAGE BUY PRICE");
        averageBuyPrice.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label AMC_name = new Label("AMC");
        AMC_name.setStyle("-fx-font-weight: bold;");
        Label AMC_currentPrice = new Label("Please");
        Label AMC_piecesOwned = new Label("Click on");
        Label AMC_averageBuyPrice = new Label("Refresh (⟳)");

        Label X_name = new Label("X");
        X_name.setStyle("-fx-font-weight: bold;");
        Label X_currentPrice = new Label("n/d");
        Label X_piecesOwned = new Label("n/d");
        Label X_averageBuyPrice = new Label("n/d");

        Label TSLA_name = new Label("TSLA");
        TSLA_name.setStyle("-fx-font-weight: bold;");
        Label TSLA_currentPrice = new Label("n/d");
        Label TSLA_piecesOwned = new Label("n/d");
        Label TSLA_averageBuyPrice = new Label("n/d");

        Label KVUE_name = new Label("KVUE");
        KVUE_name.setStyle("-fx-font-weight: bold;");
        Label KVUE_currentPrice = new Label("n/d");
        Label KVUE_piecesOwned = new Label("n/d");
        Label KVUE_averageBuyPrice = new Label("n/d");

        Label NIO_name = new Label("NIO");
        NIO_name.setStyle("-fx-font-weight: bold;");
        Label NIO_currentPrice = new Label("n/d");
        Label NIO_piecesOwned = new Label("n/d");
        Label NIO_averageBuyPrice = new Label("n/d");

        Label F_name = new Label("F");
        F_name.setStyle("-fx-font-weight: bold;");
        Label F_currentPrice = new Label("n/d");
        Label F_piecesOwned = new Label("n/d");
        Label F_averageBuyPrice = new Label("n/d");

        Label GOOGL_name = new Label("GOOGL");
        GOOGL_name.setStyle("-fx-font-weight: bold;");
        Label GOOGL_currentPrice = new Label("n/d");
        Label GOOGL_piecesOwned = new Label("n/d");
        Label GOOGL_averageBuyPrice = new Label("n/d");

        Label ENLBE_name = new Label("ENLBE");
        ENLBE_name.setStyle("-fx-font-weight: bold;");
        Label ENLBE_currentPrice = new Label("n/d");
        Label ENLBE_piecesOwned = new Label("n/d");
        Label ENLBE_averageBuyPrice = new Label("n/d");


        // Define stocks' information
        Label[] nameLabels = {symbolName, AMC_name, X_name, TSLA_name, KVUE_name, NIO_name, F_name, GOOGL_name, ENLBE_name};

        Label[] priceLabels = {currentPrice, AMC_currentPrice, X_currentPrice, TSLA_currentPrice, KVUE_currentPrice,
                NIO_currentPrice, F_currentPrice, GOOGL_currentPrice, ENLBE_currentPrice};

        Label[] piecesLabels = {piecesOwned, AMC_piecesOwned, X_piecesOwned, TSLA_piecesOwned, KVUE_piecesOwned,
                NIO_piecesOwned, F_piecesOwned, GOOGL_piecesOwned, ENLBE_piecesOwned};

        Label[] averageLabels = {averageBuyPrice, AMC_averageBuyPrice, X_averageBuyPrice, TSLA_averageBuyPrice,
                KVUE_averageBuyPrice, NIO_averageBuyPrice, F_averageBuyPrice, GOOGL_averageBuyPrice, ENLBE_averageBuyPrice};


        // Fill the Pane
        for (int i = 0; i < nameLabels.length; i++) {
            addStockInfoToGrid(gridPane, nameLabels[i], priceLabels[i], piecesLabels[i], averageLabels[i], i);
        }

        // Define refresh button
        Button refresh = new Button("\u27F3"); // This unicode is ⟳
        refresh.setStyle(
                "-fx-font-size: 1.5em; "+
                        "-fx-text-fill: #ffffff; "+
                        "-fx-background-color: #5c3190; "+
                        "-fx-background-radius: 10; "+
                        "-fx-min-width: 2px; "+
                        "-fx-min-height: 2px;"
        );
        gridPane.add(refresh, 4, 0);

        // Refresh button -> updates the labels' value
        refresh.setOnAction(e -> {
            ExecutorService executorServiceObj = Executors.newFixedThreadPool(9); // Number of stocks

            List<Future<?>> futures = new ArrayList<>();

            String[] symbols = {"AMC", "X", "TSLA", "KVUE", "NIO", "F", "GOOGL", "ENL.BE"};

            for (int i = 0; i < symbols.length; i++) {
                final int index = i;
                Future<?> future = executorServiceObj.submit(() -> {
                    Platform.runLater(() -> {
                        fetchUpdatesRealTimeBoard(apiDataObject, symbols[index],
                                priceLabels[index + 1], piecesLabels[index + 1], averageLabels[index + 1],
                                userRegistered.getUsername());
                    });
                });

                futures.add(future);
            }

            executorServiceObj.shutdown();


            // Wait for all threads finish their execution
            try {
                for (Future<?> future : futures) {
                    future.get();
                }
            }
            catch (InterruptedException | ExecutionException exception) {
                exception.printStackTrace();
            }
        });



        // Adding the GridPane to the list
        list.getChildren().add(gridPane);
        toggle = !toggle;
    }


}
