package org.project.controllers;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.project.controllers.LoginController;
import org.project.model.Stock;
import org.project.util.LineChartGenerator;

import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrevisionController {

    // Define the prevision button and its box
    public Button previsionButton = new Button();


    // Define Prevision button and its layout
    // Define image for the button
    private final Image arrowButtonImage = new Image("file:src/main/resources/frecciale.png");


    // Define button name
    String previsionForStockChange = "   Prevision   ";

    // Define HBox with button name and button image
    public HBox previsionContentHBox = new HBox(3);

    public HBox getPrevisionHBox() {
        return previsionHBox;
    }

    // Define box to insert prevision button and animation
    private HBox previsionHBox = new HBox(previsionButton);

    public ArrayList<Double> generateNextPrevision(String nameToScanFor, LoginController loginController){
        List<String> openRates = new ArrayList<>();
        List<String> closingRates = new ArrayList<>();
        List<String> highestRates = new ArrayList<>();
        List<String> lowestRates = new ArrayList<>();
        ArrayList<Double> returnValues = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(loginController.getPathUserDB()))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                // Check whether the name is the wanted one
                if (!nextLine[4].isEmpty() && nextLine[4].equals(nameToScanFor)) {  // "4" is the position of the name of  company into the db
                    if(!nextLine[5].isEmpty() && !nextLine[5].equals("101")) openRates.add(nextLine[5]); // Gets opening value
                    if(!nextLine[8].isEmpty() && !nextLine[8].equals("101")) closingRates.add(nextLine[8]); // Gets closing value
                    if(!nextLine[6].isEmpty() && !nextLine[6].equals("101")) highestRates.add(nextLine[6]); // Gets closing value
                    if(!nextLine[7].isEmpty() && !nextLine[7].equals("101")) lowestRates.add(nextLine[7]); // Gets closing value
                }
            }

            Random random = new Random();
            int precisionRange = (random.nextInt(101))+4; // Generates a random integer

            short counterOpeningValues = 0;
            short counterClosingValues = 0;
            short counterHighestValues = 0;
            short counterLowestValues = 0;

            double openValuesAverage = 0.0;
            double closeValuesAverage = 0.0;
            double highestValuesAverage = 0.0;
            double lowestValuesAverage = 0.0;

            for (int k = 0; k <= precisionRange; k++) {
                if (k < openRates.size() && k < closingRates.size() && k < highestRates.size() && k < lowestRates.size()) {
                    openValuesAverage += Double.parseDouble(openRates.get(k));
                    closeValuesAverage += Double.parseDouble(closingRates.get(k));
                    highestValuesAverage += Double.parseDouble(highestRates.get(k));
                    lowestValuesAverage += Double.parseDouble(lowestRates.get(k));

                    counterOpeningValues++;
                    counterClosingValues++;
                    counterHighestValues++;
                    counterLowestValues++;
                }
                else if(k > openRates.size()) --precisionRange;
            }

            returnValues.add(0, openValuesAverage/counterOpeningValues);
            returnValues.add(1, highestValuesAverage/counterHighestValues);
            returnValues.add(2, lowestValuesAverage/counterLowestValues);
            returnValues.add(3, closeValuesAverage/counterClosingValues);

            return returnValues;

        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public PrevisionController(ArrayList<Stock> stocksCheckedOn){
            previsionButton.setGraphic(previsionContentHBox); // Insert img inside button
        ImageView arrowButtonView = new ImageView(arrowButtonImage);
        arrowButtonView.setFitHeight(10);
            arrowButtonView.setFitWidth(15);

            previsionContentHBox.getChildren().addAll(new Text(previsionForStockChange), arrowButtonView);
            previsionContentHBox.setAlignment(Pos.CENTER);
            previsionHBox.setAlignment(Pos.CENTER);

            Text particleLabel= new Text("");
            // Handle prevision button action
            previsionButton.setOnAction(e->{
                particleLabel.setText("");
                // Popup for new graph
                Stage previsionStage = new Stage();
                previsionStage.initModality(Modality.APPLICATION_MODAL); // Block user interaction with other windows
                //previsionStage.initOwner(primaryStage); // Set primaryStage as the parent of popup
                previsionStage.setTitle("Prevision");
                previsionStage.setWidth(800);
                previsionStage.setHeight(500);
                LineChart<Number, Number> lineChartPrevision = null;

                if(!stocksCheckedOn.isEmpty()) {
                    lineChartPrevision = LineChartGenerator.createLineChart("Prevision");
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Select at least a stock in the left hand corner");
                    alert.showAndWait();
                }

                HBox changedStocksHBox = new HBox();

                String stockChangeString="";
                for (Stock stock:stocksCheckedOn) {

                    XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();
                    newSeries.setName(stock.getName());

                    Random rnd = new Random();
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    final double MODIFIERSTOCK= (rnd.nextDouble(21)+1)-10;
                    stockChangeString+= stock.getName()+" --->  "+ decimalFormat.format(MODIFIERSTOCK)+"%";
                    double previousValue= stock.getMarkerPreviousClose();
                    double addedValue = ((stock.getMarketPreviousClose()+MODIFIERSTOCK)-(previousValue));

                    //Adding arrow depending if lose or gain
                    stockChangeString+= (addedValue>0? "↗   \n": "↘   \n");


                    System.out.println("before MOD"+stock.getRegularMarketOpen()+stock.getMarketPreviousClose());

                    //Modifier to modify the stocks to be implemented
                    stock.setRegularMarketOpen(stock.getRegularMarketOpen()*MODIFIERSTOCK);
                    stock.setRegularMarketDayHigh(stock.getRegularMarketDayHigh()*MODIFIERSTOCK);
                    stock.setRegularMarketDayLow(stock.getRegularMarketDayLow()*MODIFIERSTOCK);
                    stock.setMarketPreviousClose(stock.getMarketPreviousClose()*MODIFIERSTOCK);



                    particleLabel.setText(stockChangeString);
                    //This is to ensure there are no repeated stocks
                    changedStocksHBox.getChildren().remove(particleLabel);
                    changedStocksHBox.getChildren().add(particleLabel);
                    newSeries.getData().addAll(
                            new XYChart.Data<>(0, stock.getRegularMarketOpen()),
                            new XYChart.Data<>(1,  (stock.getRegularMarketDayHigh()*10 + stock.getRegularMarketOpen())/1.8),
                            new XYChart.Data<>(2,  stock.getRegularMarketDayHigh()*10),
                            new XYChart.Data<>(3,  (stock.getRegularMarketDayHigh()*10 + stock.getRegularMarketDayLow())/2),
                            new XYChart.Data<>(4,  stock.getRegularMarketDayLow()/10),
                            new XYChart.Data<>(5,  (stock.getRegularMarketDayLow()*10 + stock.getMarketPreviousClose())/2.2),
                            new XYChart.Data<>(6,  stock.getMarkerPreviousClose())
                    );
                    lineChartPrevision.getData().add(newSeries);
                    System.out.println(stock.getName());

                }
                Button closePrevisionPopup = new Button("Close");
                HBox previsionPopupBox = new HBox(closePrevisionPopup);
                previsionPopupBox.setSpacing(30);
                VBox windowBetBox= new VBox();

                try {
                    // Define Box with elements for the popup
                    windowBetBox = new VBox(changedStocksHBox, lineChartPrevision, closePrevisionPopup);
                    windowBetBox.setPadding(new Insets(10));
                    windowBetBox.setSpacing(10);
                    windowBetBox.setAlignment(Pos.CENTER);
                }catch (NullPointerException f){}

                // Handle close button inside the popup
                closePrevisionPopup.setOnAction(ex->{
                    previsionStage.close();
                });


                // Create a scene for the custom popup
                Scene previsionScene = new Scene(windowBetBox);
                previsionStage.setScene(previsionScene);


                // Show the custom popup
                if(stocksCheckedOn.size()>=1) previsionStage.showAndWait();

            });

        }

}
