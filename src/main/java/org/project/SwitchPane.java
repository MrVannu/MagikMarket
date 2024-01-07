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

    public void showOtherView(User userRegistered, HBox list) {
        list.getChildren().clear();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(40);
        gridPane.setVgap(20);

        Label[] labels = new Label[]{
                new Label("SYMBOL"),
                new Label("CURRENT PRICE"),
                new Label("PIECES OWNED"),
                new Label("AVERAGE BUY PRICE"),
                new Label("AMC"),
                new Label("X"),
                new Label("TSLA"),
                new Label("KVUE"),
                new Label("NIO"),
                new Label("NVDA"),
                new Label("JNJ"),
                new Label("AMD"),
                new Label("F"),
                new Label("GOOGL"),
                new Label("ENLBE")
        };

        // Adding labels to the grid
        for (int i = 0; i < labels.length; i++) {
            gridPane.add(labels[i], i % 4, i / 4);
        }

        // Set texts
        for (int i = 4; i < labels.length; i++) {
            Label symbol = labels[i];
            Label currentPrice = labels[i + 1];
            Label piecesOwned = labels[i + 2];
            Label averageBuyPrice = labels[i + 3];

            currentPrice.setText("X");
            piecesOwned.setText("X");
            averageBuyPrice.setText("X");
        }

        // Adding the GridPane to the list
        list.getChildren().add(gridPane);
        toggle = !toggle;
    }

}
