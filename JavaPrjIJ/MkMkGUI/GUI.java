import com.apple.eawt.Application;

public class GUI extends Application{

    public void start(Stage primaryStage){
        Scene scene = new Scene(new Panel(), 700, 500);
        primaryStage.setTitle("PizzaGUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}