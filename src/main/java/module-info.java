module com.example.exchangeratesapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.example.exchangeratesapp to javafx.fxml;
    exports com.example.exchangeratesapp;
}