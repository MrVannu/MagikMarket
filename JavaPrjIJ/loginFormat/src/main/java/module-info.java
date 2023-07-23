module com.example.loginformat {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.loginformat to javafx.fxml;
    exports com.example.loginformat;
}