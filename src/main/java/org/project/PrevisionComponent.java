package org.project;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrevisionComponent{
    // Define the prevision button and its box
    public Button previsionButton = new Button();


    // Define Prevision button and its layout
    // Define image for the button
    private  Image arrowGui = new Image("file:src/main/resources/frecciale.png");
    private ImageView arrowGuiImg = new ImageView(arrowGui);


    // Define button name
    String previsionString = "Prevision";

    // Define HBox with button name and button image
    public HBox content = new HBox(3);

    public HBox getPrevisionBox() {
        return previsionBox;
    }

    // Define box to insert prevision button and animation
    private HBox previsionBox = new HBox(previsionButton);

    // This method is yet to be improved providing a switch structure for various type of data are needed
    public ArrayList<Double> generateNextPrevision(String nameToScanFor, LoginControl loginControl){
        List<String> openRates = new ArrayList<>();
        List<String> closingRates = new ArrayList<>();
        List<String> highestRates = new ArrayList<>();
        List<String> lowestRates = new ArrayList<>();
        ArrayList<Double> returnValues = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(loginControl.getPathUserDB()))) {
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

    public PrevisionComponent(ArrayList<Stock> stocksCheckedOn, Stage primaryStage, Label moneyLabel){
            previsionButton.setGraphic(content); // Insert img inside button
            arrowGuiImg.setFitHeight(10);
            arrowGuiImg.setFitWidth(15);

            content.getChildren().addAll(new Text(previsionString), arrowGuiImg);
            content.setAlignment(Pos.CENTER);
            previsionBox.setAlignment(Pos.CENTER);

            // Handle prevision button action
            previsionButton.setOnAction(e->{

                // Check if stock has been invested

                // Popup for new graph
                Stage previsionStage = new Stage();
                previsionStage.initModality(Modality.APPLICATION_MODAL); // Block user interaction with other windows
                previsionStage.initOwner(primaryStage); // Set primaryStage as the parent of popup
                previsionStage.setTitle("Bet Input");
                previsionStage.setWidth(800);
                previsionStage.setHeight(500);

                // Define a line chart
                LineChart<Number, Number> lineChartPrevision= LineChartGenerator.createLineChart("Prevision");

                System.out.println("Stocks checked on: \n");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setHeight(500);
                alert.setWidth(500);
                String alertString="";
                String particleString="";
                for (Stock stock:stocksCheckedOn) {

                    XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();
                    newSeries.setName(stock.getName());
                    //nameOfCompany= testObj.extractNameOfCompany();

                    Random rnd = new Random();
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    final double PARTICLE= (rnd.nextDouble(21)+1)/10;
                    particleString+= stock.getName()+" --->  "+ decimalFormat.format(PARTICLE)+"%";



                    System.out.println("before MOD"+stock.getRegularMarketOpen()+stock.getMarketPreviousClose());

                    double previousValue= stock.getMarkerPreviousClose();
                    //Algorithm to modify the stocks to be implemented
                    stock.setRegularMarketOpen(stock.getRegularMarketOpen()*PARTICLE);
                    stock.setRegularMarketDayHigh(stock.getRegularMarketDayHigh()*PARTICLE);
                    stock.setRegularMarketDayLow(stock.getRegularMarketDayLow()*PARTICLE);
                    stock.setMarketPreviousClose(stock.getMarketPreviousClose()*PARTICLE);
                    //System.out.println("after MOD"+stock.getRegularMarketOpen()+stock.getMarketPreviousClose());


                    double addedValue = (stock.getMarketPreviousClose()-previousValue)*10;
                    //Adding arrow depending if lose or gain
                    particleString+= (addedValue>0? "↗\n": "↘\n");
                    if(stock.getMarketPreviousClose()<previousValue){
                        //To change money amount
                        //moneyLabel.setText(decimalFormat.format(Double.parseDouble(moneyLabel.getText())+addedValue));

                        alertString += "The stock is going down!" +" You could lose "
                            +decimalFormat.format(addedValue)+" by betting on "+stock.getName()+"\n";
                        //alert.showAndWait();
                    } else if (stock.getMarketPreviousClose()>previousValue) {
                        //alert.setTitle("YOU DID WELL!");
                        alert.setHeaderText(null);
                        alertString += ("Your intuition was right!" +" You could gain "+
                                decimalFormat.format(addedValue)+" by betting on "+stock.getName()+"\n");

                    }else{}

                    newSeries.getData().addAll(
                            //(testObj==null? nameOfCompany: testObj.extractNameOfCompany())
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
                    // Create UI elements for the custom popup

                    // Use showAndWait to wait for user interaction before continuing


                };
                alert.setContentText(alertString+"\t \n\n "+particleString);
                alert.showAndWait();
                alert.setContentText("");
                Button closePrevisionPopup = new Button("Close");
                HBox previsionPopupBox = new HBox(closePrevisionPopup);
                previsionPopupBox.setSpacing(30);
                //HBox Strin

                // Define Box with elements for the popup
                VBox windowBetBox = new VBox(lineChartPrevision, closePrevisionPopup);
                windowBetBox.setPadding(new Insets(10));
                windowBetBox.setSpacing(10);
                windowBetBox.setAlignment(Pos.CENTER);

                // Handle close button inside the popup
                closePrevisionPopup.setOnAction(ex->{
                    previsionStage.close();
                });

                // Create a scene for the custom popup
                Scene previsionScene = new Scene(windowBetBox);
                previsionStage.setScene(previsionScene);

                // Show the custom popup
                previsionStage.showAndWait();
                /*try {
                    updateDataHistory(1, 1.0, 1.0, 1.0, "test", 1.0,1.0,1.0, "test", "test");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }*/
            });
        }
}
