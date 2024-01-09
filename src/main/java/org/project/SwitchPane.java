package org.project;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import java.util.List;

public class SwitchPane extends Stock{
    public boolean toggle = false;
    private final APIData apiDataObject = new APIData();


    public SwitchPane (HBox hBoxList){
    }


    // Show user's stock buy/sell operation
    public void showStocks(User userRegistered, HBox list) {
        List<List<String>> theList = getSavedStocks(userRegistered.getUsername());
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Horizontal space between columns
        list.getChildren().clear();

        int rowIndex = 0;
        for (List<String> outEl : theList) {
            int columnIndex = 0;
            for (String innerEl : outEl) {
                Text text = new Text(innerEl);
                gridPane.add(text, columnIndex, rowIndex);
                columnIndex++;
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
    private void fetchUpdatesRealTimeBoard(APIData obj, String symbol, Label currentPriceLabel, Label piecesOwnedLabel,
                                           Label averageBuyPriceLabel, String username) {
        obj.fetchData(symbol.toLowerCase());

        currentPriceLabel.setText(String.valueOf(obj.regularMarketPrice()));
        piecesOwnedLabel.setText(String.valueOf(getSumAndPieces(username, symbol.toLowerCase())));
        averageBuyPriceLabel.setText(String.valueOf(getAverageOfPurchased(username, symbol)));
    }

    // Show the real time stocks board
    public void showOtherView (User userRegistered, HBox list){
        list.getChildren().clear();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(40);
        gridPane.setVgap(20);

        Label symbolName = new Label("SYMBOL");
        Label currentPrice = new Label("CURRENT PRICE");
        Label piecesOwned = new Label("PIECES OWNED");
        Label averageBuyPrice = new Label("AVERAGE BUY PRICE");

        Label AMC_name = new Label("AMC");
        Label AMC_currentPrice = new Label("Please");
        Label AMC_piecesOwned = new Label("Click on");
        Label AMC_averageBuyPrice = new Label("Refresh");

        Label X_name = new Label("X");
        Label X_currentPrice = new Label("n/d");
        Label X_piecesOwned = new Label("n/d");
        Label X_averageBuyPrice = new Label("n/d");

        Label TSLA_name = new Label("TSLA");
        Label TSLA_currentPrice = new Label("n/d");
        Label TSLA_piecesOwned = new Label("n/d");
        Label TSLA_averageBuyPrice = new Label("n/d");

        Label KVUE_name = new Label("KVUE");
        Label KVUE_currentPrice = new Label("n/d");
        Label KVUE_piecesOwned = new Label("n/d");
        Label KVUE_averageBuyPrice = new Label("n/d");

        Label NIO_name = new Label("NIO");
        Label NIO_currentPrice = new Label("n/d");
        Label NIO_piecesOwned = new Label("n/d");
        Label NIO_averageBuyPrice = new Label("n/d");

        Label F_name = new Label("F");
        Label F_currentPrice = new Label("n/d");
        Label F_piecesOwned = new Label("n/d");
        Label F_averageBuyPrice = new Label("n/d");

        Label GOOGL_name = new Label("GOOGL");
        Label GOOGL_currentPrice = new Label("n/d");
        Label GOOGL_piecesOwned = new Label("n/d");
        Label GOOGL_averageBuyPrice = new Label("n/d");

        Label ENLBE_name = new Label("ENLBE");
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
        Button refresh = new Button("Refresh");
        gridPane.add(refresh, 4, 10);


        // Refresh button -> updates the labels' value
        refresh.setOnAction(e->{

            fetchUpdatesRealTimeBoard(apiDataObject, "AMC", AMC_currentPrice, AMC_piecesOwned,
                    AMC_averageBuyPrice, userRegistered.getUsername());

            fetchUpdatesRealTimeBoard(apiDataObject, "X", X_currentPrice, X_piecesOwned,
                    X_averageBuyPrice, userRegistered.getUsername());

            fetchUpdatesRealTimeBoard(apiDataObject, "TSLA", TSLA_currentPrice, TSLA_piecesOwned,
                    TSLA_averageBuyPrice, userRegistered.getUsername());

            fetchUpdatesRealTimeBoard(apiDataObject, "KVUE", KVUE_currentPrice, KVUE_piecesOwned,
                    KVUE_averageBuyPrice, userRegistered.getUsername());

            fetchUpdatesRealTimeBoard(apiDataObject, "NIO", NIO_currentPrice, NIO_piecesOwned,
                    NIO_averageBuyPrice, userRegistered.getUsername());

            fetchUpdatesRealTimeBoard(apiDataObject, "F", F_currentPrice, F_piecesOwned,
                    F_averageBuyPrice, userRegistered.getUsername());

            fetchUpdatesRealTimeBoard(apiDataObject, "GOOGL", GOOGL_currentPrice, GOOGL_piecesOwned,
                    GOOGL_averageBuyPrice, userRegistered.getUsername());

            fetchUpdatesRealTimeBoard(apiDataObject, "ENL.BE", ENLBE_currentPrice, ENLBE_piecesOwned,
                    ENLBE_averageBuyPrice, userRegistered.getUsername());

        });

        // Adding the GridPane to the list
        list.getChildren().add(gridPane);

        // Switch for the view
        toggle = !toggle;

    }


}
