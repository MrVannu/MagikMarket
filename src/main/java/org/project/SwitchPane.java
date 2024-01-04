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
        Label test = new Label("Siukk");
        list.getChildren().add(test);
        toggle = !toggle;
    }
}
