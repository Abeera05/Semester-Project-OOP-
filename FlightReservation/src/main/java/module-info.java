module com.example.flightreservation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.flightreservation to javafx.fxml;
    exports com.example.flightreservation;
}