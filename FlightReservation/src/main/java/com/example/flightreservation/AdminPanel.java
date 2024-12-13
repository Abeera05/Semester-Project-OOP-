package com.example.flightreservation;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.util.converter.LocalDateStringConverter;
import java.time.format.DateTimeFormatter;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.time.LocalDate;

public class AdminPanel extends Application {

    private FlightDatabase flightDatabase;
    private TableView<Flight> flightTable;

    @Override
    public void start(Stage primaryStage) {
        flightDatabase = new FlightDatabase();
        flightDatabase.loadFlightsFromFile("flight.txt");

        flightTable = createFlightTable(); // Initialize the flightTable
        flightTable.setItems(FXCollections.observableArrayList(flightDatabase.getFlights())); // Populate table with loaded data

        // Create buttons for the admin actions
        Button addButton = new Button("Add Flight");
        Button deleteButton = new Button("Delete Flight");
        Button viewButton = new Button("View Flights");

        // Create a container for the buttons
        HBox buttonBox = new HBox(10, addButton, deleteButton, viewButton);
        buttonBox.setStyle("-fx-padding: 10; -fx-alignment: center;");

        // Create a container for dynamic content
        StackPane contentArea = new StackPane();

        contentArea.getChildren().add(flightTable);

        // Button actions
        addButton.setOnAction(e -> {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(createFlightForm());
        });

        deleteButton.setOnAction(e -> {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(createDeleteForm());
        });

        viewButton.setOnAction(e -> {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(flightTable); // Use the flightTable
        });

        // Main layout
        VBox layout = new VBox(10, buttonBox, contentArea);
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setTitle("Admin Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Create form to add a flight
    private VBox createFlightForm() {
        VBox formLayout = new VBox(10);
        Label flightIDLabel = new Label("Flight ID:");
        TextField flightIDField = new TextField();
        Label fromLabel = new Label("From:");
        ComboBox<String> fromComboBox = new ComboBox<>();
        fromComboBox.getItems().addAll("Karachi", "Lahore", "Islamabad", "Peshawar");
        Label toLabel = new Label("To:");
        ComboBox<String> toComboBox = new ComboBox<>();
        toComboBox.getItems().addAll("Karachi", "Lahore", "Islamabad", "Peshawar");

        Label departureLabel = new Label("Departure Time:");
        DatePicker departureDatePicker = new DatePicker(LocalDate.now()); // Set default to today's date
        departureDatePicker.setConverter(new LocalDateStringConverter());

        Label arrivalLabel = new Label("Arrival Time:");
        DatePicker arrivalDatePicker = new DatePicker(LocalDate.now()); // Set default to today's date
        arrivalDatePicker.setConverter(new LocalDateStringConverter());

        Button addFlightButton = new Button("Add Flight");

        // Add flight action
        addFlightButton.setOnAction(e -> {
            try {
                String flightID = flightIDField.getText().trim();
                String fromCity = fromComboBox.getValue();
                String toCity = toComboBox.getValue();
                LocalDate departureTime = departureDatePicker.getValue();
                LocalDate arrivalTime = arrivalDatePicker.getValue();

                // Validate input
                if (flightID.isEmpty() || fromCity == null || toCity == null || departureTime == null || arrivalTime == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "All fields must be filled.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }

                Flight newFlight = new Flight(flightID, fromCity, toCity, departureTime, arrivalTime);
                flightDatabase.addFlight(newFlight); // Add to database
                flightTable.setItems(FXCollections.observableArrayList(flightDatabase.getFlights())); // Refresh table

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Flight added successfully!", ButtonType.OK);
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error adding flight.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        formLayout.getChildren().addAll(flightIDLabel, flightIDField, fromLabel, fromComboBox, toLabel, toComboBox,
                departureLabel, departureDatePicker, arrivalLabel, arrivalDatePicker, addFlightButton);
        return formLayout;
    }

    // Create form to delete a flight
    private VBox createDeleteForm() {
        VBox deleteFormLayout = new VBox(10);
        Label deleteFlightLabel = new Label("Enter Flight ID to delete:");
        TextField deleteFlightIDField = new TextField();
        Button deleteFlightButton = new Button("Delete Flight");

        // Delete flight action
        deleteFlightButton.setOnAction(e -> {
            String flightID = deleteFlightIDField.getText();
            boolean success = flightDatabase.deleteFlight(flightID);
            if (success) {
                flightTable.setItems(FXCollections.observableArrayList(flightDatabase.getFlights())); // Refresh table
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Flight Deleted");
                alert.setContentText("Flight with ID " + flightID + " has been deleted.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Flight Not Found");
                alert.setContentText("No flight found with ID " + flightID + ".");
                alert.showAndWait();
            }
        });

        deleteFormLayout.getChildren().addAll(deleteFlightLabel, deleteFlightIDField, deleteFlightButton);
        return deleteFormLayout;
    }

    // Create the flight table
    private TableView<Flight> createFlightTable() {
        TableView<Flight> table = new TableView<>();

        TableColumn<Flight, String> flightIdColumn = new TableColumn<>("Flight ID");
        flightIdColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getFlightId()));

        TableColumn<Flight, String> sourceColumn = new TableColumn<>("Source");
        sourceColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getSource()));

        TableColumn<Flight, String> destinationColumn = new TableColumn<>("Destination");
        destinationColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDestination()));

        TableColumn<Flight, String> departureColumn = new TableColumn<>("Departure");
        departureColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDepartureTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        TableColumn<Flight, String> arrivalColumn = new TableColumn<>("Arrival");
        arrivalColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getArrivalTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        table.getColumns().addAll(flightIdColumn, sourceColumn, destinationColumn, departureColumn, arrivalColumn);

        // Populate table with data
        ObservableList<Flight> flightObservableList = FXCollections.observableArrayList(flightDatabase.getFlights());
        table.setItems(flightObservableList);

        return table;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
