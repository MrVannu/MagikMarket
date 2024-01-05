package org.project;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.List;

public class SwitchPane extends Stock{

    private HBox hBoxList;
    public boolean toggle= true;
    public SwitchPane (HBox hBoxList){
        this.hBoxList=hBoxList;
    }

    public void showStocks(User userRegistered, HBox list) {
        List<List<String>> theList = getSavedStocks(userRegistered.getUsername());
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Horizontal space between columns
        list.getChildren().clear();

        int rowIndex = 0;
        for (List<String> outEl : theList) {
            int columnIndex = 0;
            outEl.set(0, "");
            for (String innerEl : outEl) {
                Text text = new Text(innerEl);
                gridPane.add(text, columnIndex, rowIndex);
                columnIndex++;
            }
            rowIndex++;
        }

        list.getChildren().add(gridPane);
        toggle = !toggle;

    }

    public void showOtherView (User userRegistered, HBox list){
        list.getChildren().clear();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(40);
        gridPane.setVgap(20);


        Label symbolName = new Label("SYMBOL");
        Label currentTime = new Label("CURRENT PRICE");
        Label piecesOwned = new Label("PIECES OWNED");
        Label averageBuyPrice = new Label("AVERAGE BUY PRICE");

        Label AMC_name = new Label("AMC");
        Label AMC_currentPrice = new Label("0");
        Label AMC_piecesOwned = new Label("0");
        Label AMC_averageBuyPrice = new Label("0");

        Label X_name = new Label("X");
        Label X_currentPrice = new Label("0");
        Label X_piecesOwned = new Label("0");
        Label X_averageBuyPrice = new Label("0");

        Label TSLA_name = new Label("TSLA");
        Label TSLA_currentPrice = new Label("0");
        Label TSLA_piecesOwned = new Label("0");
        Label TSLA_averageBuyPrice = new Label("0");

        Label KVUE_name = new Label("KVUE");
        Label KVUE_currentPrice = new Label("0");
        Label KVUE_piecesOwned = new Label("0");
        Label KVUE_averageBuyPrice = new Label("0");

        Label NIO_name = new Label("NIO");
        Label NIO_currentPrice = new Label("0");
        Label NIO_piecesOwned = new Label("0");
        Label NIO_averageBuyPrice = new Label("0");

        Label NVDA_name = new Label("NVDA");
        Label NVDA_currentPrice = new Label("0");
        Label NVDA_piecesOwned = new Label("0");
        Label NVDA_averageBuyPrice = new Label("0");

        Label JNJ_name = new Label("JNJ");
        Label JNJ_currentPrice = new Label("0");
        Label JNJ_piecesOwned = new Label("0");
        Label JNJ_averageBuyPrice = new Label("0");

        Label AMD_name = new Label("AMD");
        Label AMD_currentPrice = new Label("0");
        Label AMD_piecesOwned = new Label("0");
        Label AMD_averageBuyPrice = new Label("0");

        Label F_name = new Label("F");
        Label F_currentPrice = new Label("0");
        Label F_piecesOwned = new Label("0");
        Label F_averageBuyPrice = new Label("0");

        Label GOOGL_name = new Label("GOOGL");
        Label GOOGL_currentPrice = new Label("0");
        Label GOOGL_piecesOwned = new Label("0");
        Label GOOGL_averageBuyPrice = new Label("0");

        Label ENLBE_name = new Label("ENLBE");
        Label ENLBE_currentPrice = new Label("0");
        Label ENLBE_piecesOwned = new Label("0");
        Label ENLBE_averageBuyPrice = new Label("0");



        // Adding labels to the grid
        gridPane.add(symbolName, 0, 0);
        gridPane.add(currentTime, 1, 0);
        gridPane.add(piecesOwned, 2, 0);
        gridPane.add(averageBuyPrice, 3, 0);

        gridPane.add(AMC_name, 0, 1);
        gridPane.add(AMC_currentPrice, 1, 1);
        gridPane.add(AMC_piecesOwned, 2, 1);
        gridPane.add(AMC_averageBuyPrice, 3, 1);

        gridPane.add(X_name, 0, 2);
        gridPane.add(X_currentPrice, 1, 2);
        gridPane.add(X_piecesOwned, 2, 2);
        gridPane.add(X_averageBuyPrice, 3, 2);

        gridPane.add(TSLA_name, 0, 3);
        gridPane.add(TSLA_currentPrice, 1, 3);
        gridPane.add(TSLA_piecesOwned, 2, 3);
        gridPane.add(TSLA_averageBuyPrice, 3, 3);

        gridPane.add(KVUE_name, 0, 4);
        gridPane.add(KVUE_currentPrice, 1, 4);
        gridPane.add(KVUE_piecesOwned, 2, 4);
        gridPane.add(KVUE_averageBuyPrice, 3, 4);

        gridPane.add(NIO_name, 0, 5);
        gridPane.add(NIO_currentPrice, 1, 5);
        gridPane.add(NIO_piecesOwned, 2, 5);
        gridPane.add(NIO_averageBuyPrice, 3, 5);

        gridPane.add(NVDA_name, 0, 6);
        gridPane.add(NVDA_currentPrice, 1, 6);
        gridPane.add(NVDA_piecesOwned, 2, 6);
        gridPane.add(NVDA_averageBuyPrice, 3, 6);

        gridPane.add(JNJ_name, 0, 7);
        gridPane.add(JNJ_currentPrice, 1, 7);
        gridPane.add(JNJ_piecesOwned, 2, 7);
        gridPane.add(JNJ_averageBuyPrice, 3, 7);

        gridPane.add(AMD_name, 0, 8);
        gridPane.add(AMD_currentPrice, 1, 8);
        gridPane.add(AMD_piecesOwned, 2, 8);
        gridPane.add(AMD_averageBuyPrice, 3, 8);

        gridPane.add(F_name, 0, 9);
        gridPane.add(F_currentPrice, 1, 9);
        gridPane.add(F_piecesOwned, 2, 9);
        gridPane.add(F_averageBuyPrice, 3, 9);

        gridPane.add(GOOGL_name, 0, 10);
        gridPane.add(GOOGL_currentPrice, 1, 10);
        gridPane.add(GOOGL_piecesOwned, 2, 10);
        gridPane.add(GOOGL_averageBuyPrice, 3, 10);

        gridPane.add(ENLBE_name, 0, 11);
        gridPane.add(ENLBE_currentPrice, 1, 11);
        gridPane.add(ENLBE_piecesOwned, 2, 11);
        gridPane.add(ENLBE_averageBuyPrice, 3, 11);



        // Set texts
        AMC_currentPrice.setText("X");
        AMC_piecesOwned.setText("X");
        AMC_averageBuyPrice.setText("X");


        X_currentPrice.setText("X");
        X_piecesOwned.setText("X");
        X_averageBuyPrice.setText("X");


        TSLA_currentPrice.setText("X");
        TSLA_piecesOwned.setText("X");
        TSLA_averageBuyPrice.setText("X");


        KVUE_currentPrice.setText("X");
        KVUE_piecesOwned.setText("X");
        KVUE_averageBuyPrice.setText("X");

        NIO_currentPrice.setText("X");
        NIO_piecesOwned.setText("X");
        NIO_averageBuyPrice.setText("X");

        NVDA_currentPrice.setText("X");
        NVDA_piecesOwned.setText("X");
        NVDA_averageBuyPrice.setText("X");

        JNJ_currentPrice.setText("X");
        JNJ_piecesOwned.setText("X");
        JNJ_averageBuyPrice.setText("X");

        AMD_currentPrice.setText("X");
        AMD_piecesOwned.setText("X");
        AMD_averageBuyPrice.setText("X");

        F_currentPrice.setText("X");
        F_piecesOwned.setText("X");
        F_averageBuyPrice.setText("X");

        GOOGL_currentPrice.setText("X");
        GOOGL_piecesOwned.setText("X");
        GOOGL_averageBuyPrice.setText("X");

        ENLBE_currentPrice.setText("X");
        ENLBE_piecesOwned.setText("X");
        ENLBE_averageBuyPrice.setText("X");


        // Adding the GridPane to the list
        list.getChildren().add(gridPane);
        toggle = !toggle;
    }
}
