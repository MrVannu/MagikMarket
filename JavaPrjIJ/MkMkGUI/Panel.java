/*
Student Code Ethics
Students are expected to maintain the highest standards of academic integrity. Work that is not of the student's own creation will receive no credit. Remember that you cannot give or receive unauthorized aid on any assignment, quiz, or exam. A student cannot use the ideas of another and declare it as his or her own. Students are required to properly cite the original source of the ideas and information used in his or her work.
*/
import java.util.ArrayList;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Panel extends VBox{

	private Button pOrderButton,delButton,detailsButton;
	private TextField smallText, mediumText ,largeText,idText;
	private Label sizesL,smallL,mediumL,largeL,idL,activeL,deliveredL,emptyL,
					lastDetailL,totOrderCostL;
	private ArrayList<PizzaOrder> orders = new ArrayList<PizzaOrder>();
	private ArrayList<String> sizes = new ArrayList<String>();
	private ArrayList<Integer> quantities = new ArrayList<Integer>();
	private ArrayList<PizzaOrder> delivered = new ArrayList<PizzaOrder>();
	
	public OrderPanel() {
		
		//labels
		sizesL = new Label("Pizza sizes");
		smallL = new Label("Small");
		mediumL = new Label("Medium");
		largeL = new Label("Large");
		idL = new Label("Order id");
		activeL = new Label("Active orders: []");
		deliveredL = new Label("Delivered: []");
		emptyL = new Label("");
		lastDetailL = new Label("");
		totOrderCostL = new Label("");
		sizesL.setAlignment(Pos.CENTER);
		smallL.setAlignment(Pos.CENTER);
		mediumL.setAlignment(Pos.CENTER);
		largeL.setAlignment(Pos.CENTER);
		idL.setAlignment(Pos.CENTER);
		activeL.setAlignment(Pos.CENTER);
		deliveredL.setAlignment(Pos.CENTER);
		lastDetailL.setAlignment(Pos.CENTER_LEFT);
		totOrderCostL.setAlignment(Pos.CENTER_LEFT);
		
		//text fields
		smallText = new TextField("0");
		smallText.setPrefWidth(50);
		mediumText = new TextField("0");
		mediumText.setPrefWidth(50);
		largeText = new TextField("0");
		largeText.setPrefWidth(50);
		idText = new TextField("0");
		idText.setPrefWidth(200);
		
		//buttons
		pOrderButton = new Button("Place the order!");
		pOrderButton.setOnAction(this::buttonIsPressed);
		delButton = new Button("Delivered");
		delButton.setOnAction(this::buttonIsPressed);
		detailsButton = new Button("Details");
		detailsButton.setOnAction(this::buttonIsPressed);
		
		//1ST LEFT BOX---------------------------
		HBox sizeLabel = new HBox(sizesL);
		HBox small = new HBox(smallL,smallText);
		HBox medium = new HBox(mediumL,mediumText);
		HBox large = new HBox(largeL,largeText);
		HBox pOrderLabel = new HBox(pOrderButton);
		sizeLabel.setAlignment(Pos.CENTER_LEFT);
		small.setAlignment(Pos.CENTER);
		medium.setAlignment(Pos.CENTER);
		large.setAlignment(Pos.CENTER);
		pOrderLabel.setAlignment(Pos.CENTER);
		sizeLabel.setSpacing(10);
		small.setSpacing(10);
		medium.setSpacing(10);
		large.setSpacing(10);
		pOrderLabel.setSpacing(10);
		//VBOX OF THE 1ST LEFT BOX
		VBox leftSide = new VBox(sizeLabel,small,medium,large,pOrderLabel);
		leftSide.setAlignment(Pos.CENTER);
		leftSide.setSpacing(10);
		
		//2ND RIGHT BOX---------------------------
		HBox idLabel = new HBox(idL);
		HBox idTextField = new HBox(idText);
		HBox deliveredButton = new HBox(delButton);
		HBox detailsButtonBox = new HBox(detailsButton);
		HBox empty = new HBox(emptyL);
		idLabel.setAlignment(Pos.CENTER_LEFT);
		idTextField.setAlignment(Pos.CENTER_LEFT);
		deliveredButton.setAlignment(Pos.CENTER_LEFT);
		detailsButtonBox.setAlignment(Pos.CENTER_LEFT);
		//VBOX OF THE 2ND RIGHT BOX
		VBox rightSide = new VBox(idLabel,idTextField,deliveredButton,detailsButtonBox,empty);
		rightSide.setAlignment(Pos.CENTER);
		rightSide.setSpacing(10);
		
		//BIG CONTENITORS FOR 1ST/2ND_BOX---------------------------
		HBox upperArea = new HBox(leftSide,rightSide);
		upperArea.setAlignment(Pos.CENTER);
		upperArea.setSpacing(100);
		HBox lowerArea = new HBox(activeL);
		lowerArea.setAlignment(Pos.CENTER);
		HBox lowerlowerArea = new HBox(deliveredL);
		lowerlowerArea.setAlignment(Pos.CENTER);
		HBox details = new HBox(lastDetailL);
		details.setAlignment(Pos.CENTER);
		
		
		setAlignment(Pos.CENTER);
		setSpacing(20);
		getChildren().addAll(upperArea,lowerArea,lowerlowerArea,details);
	}
	
	public void buttonIsPressed (ActionEvent event) {
		Random rand = new Random();
	
		
		//If the button pOrder button is pressed: 
		if(event.getSource()==pOrderButton){
			PizzaOrder o1 = new PizzaOrder();//The new object will kind of "substitute" the old one
			o1.setOrderId(rand.nextInt(999999));
			for(PizzaOrder o : orders) {//It is preventing 1 time same nr, but not multiples
				if(o1.equals(o)) {
					o1.setOrderId(rand.nextInt(999999));
				}
			}
				if(smallText.getText().equals("0")){//if the label small has 1 or more pizza
				//then set the size small on sizes array, and get the value in small textfield.
				}
				else{
					sizes.add(0,"small");
					quantities.add(0,Integer.parseInt(smallText.getText()));
					}
				if(mediumText.getText().equals("0")){}
				else {
					sizes.add(0,"medium");
					quantities.add(0,Integer.parseInt(mediumText.getText()));
				}
				if(largeText.getText().equals("0")){}
				else {
					sizes.add(0,"large");
					quantities.add(0,Integer.parseInt(largeText.getText()));
				}
			orders.add(o1);	
			PizzeriaAPI.placeOrder(o1,sizes,quantities);
			activeL.setText("Active orders: "+ orders);
			lastDetailL.setText("");
		}//close if pOrderBotton is pressed
			
		
		//if delivered button is pressed
			if(event.getSource()==delButton) {
				for(PizzaOrder o:orders) {
					if(o.getOrderId()==Integer.parseInt(idText.getText())){
						PizzeriaAPI.updateDelivery(o);
						}
					else {
						System.out.println("ERROR! The OrderId in input has not been found on the orders.");
					}
					}
				for(PizzaOrder oo:PizzeriaAPI.sweepOrders(orders)) {
					delivered.add(oo);
					deliveredL.setText("Delivered orders: "+delivered);
					activeL.setText("Active orders: "+orders);
				}
				for(int i=0;i<delivered.size();i++) {
					PizzaOrder nr = delivered.get(i);
					PizzaOrder nr2 = orders.get(i);
					if(nr.equals(nr2)) {
						orders.remove(i);
					}
					deliveredL.setText("Delivered orders: "+delivered);
					activeL.setText("Active orders: "+orders);
				}
				lastDetailL.setText("");
			}//closing if delButton is pressed
			
			
			if(event.getSource()==detailsButton) {
				for(PizzaOrder o:orders) {
					int id = Integer.parseInt(idText.getText());
						if(o.getOrderId()==id) {
						lastDetailL.setText("Details: Order ID: "+id
							+"\ntotal order cost: "+o.getTotalOrderCost()
							+ ",number of ordered pizzas: "+o.getPizzaCount());
						}
				}
				for(PizzaOrder o:delivered) {
					int id = Integer.parseInt(idText.getText());
						if(o.getOrderId()==id) {
							lastDetailL.setText("Details: Order ID: "+id
								+"\ntotal order cost: "+o.getTotalOrderCost()
						    	+ ",number of ordered pizzas: "+o.getPizzaCount());
							}
				}
				
			}//close detailsButton is pressed

		}//close method
		
	}//close class
	


