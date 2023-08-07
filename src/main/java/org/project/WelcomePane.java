package org.project;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


public class WelcomePane {
    public WelcomePane(){
        GridPane layoutStartApp = new GridPane();
        Scene WelcomeScene = new Scene(layoutStartApp, 500, 300);
        Text example = new Text("Test for pane");
    }
}
