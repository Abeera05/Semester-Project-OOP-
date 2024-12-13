package com.example.flightreservation;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FlightDetailsGUI extends Application {
    private static FlightDatabase flightDatabase;
    private Flight selectedFlight;
    private ObservableList<Flight> searchResults;

    public FlightDetailsGUI() {
        searchResults = FXCollections.observableArrayList();
    }


    public static void setFlightManager(FlightDatabase Database) {
        flightDatabase = Database;
    }

    @Override
    public void start(Stage primaryStage) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(getClass().getResource("/com/example/airplane-window.jpg").toExternalForm()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true)
        );
        Background background = new Background(backgroundImage);

        Label sourceLabel = new Label("Source:");
        sourceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: white;");

        TextField sourceField = new TextField();
        sourceField.setPrefHeight(40); // Increase the height for thickness
        sourceField.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 5px;");

        Label destinationLabel = new Label("Destination:");
        destinationLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: white;");

        TextField destinationField = new TextField();
        destinationField.setPrefHeight(40); // Increase the height for thickness
        destinationField.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 5px;");

        Label depdateLabel = new Label("Select Departure Date:");
        depdateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: white;");

        DatePicker depDatePicker = new DatePicker();
        depDatePicker.setPrefHeight(40); // Increase the height for thickness
        depDatePicker.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 5px;");

        Label arrdateLabel = new Label("Select Arrival Date:");
        arrdateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: white;");

        DatePicker arrDatePicker = new DatePicker();
        arrDatePicker.setPrefHeight(40); // Increase the height for thickness
        arrDatePicker.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 5px;");

        Button searchButton = new Button("Search Flights");
        searchButton.setStyle("-fx-background-color: #000080; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 5px;");
        TableView<Flight> flightTable = createFlightTable(dateFormatter);

        Button nextButton = new Button("Next");
        nextButton.setStyle("-fx-background-color: #000080; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 5px;");
        nextButton.setDisable(true);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #d21438; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 5px;");
        backButton.setOnAction(e -> {
            LoginUI loginUI = new LoginUI();
            loginUI.start(new Stage());
            primaryStage.close();
        });

        // Search button action
        searchButton.setOnAction(e -> {
            String source = sourceField.getText();
            String destination = destinationField.getText();
            LocalDate departureDate = depDatePicker.getValue();
            LocalDate arrivalDate = arrDatePicker.getValue();

            if (source.isBlank() || destination.isBlank() || departureDate == null || arrivalDate == null) {
                showErrorMessage("Please fill all fields.");
            } else {
                try {
                    List<Flight> results = flightDatabase.searchFlights(source, destination, departureDate, arrivalDate);
                    if (results.isEmpty()) {
                        showErrorMessage("No flights found for the given criteria.");
                    } else {
                        searchResults.setAll(results);
                        nextButton.setDisable(false);
                    }
                } catch (Exception ex) {
                    showErrorMessage("An error occurred while searching for flights.");
                }
            }
        });

        // Next button action
        nextButton.setOnAction(e -> {
            if (selectedFlight != null) {
                PassengerGUI passengerGUI = new PassengerGUI(selectedFlight);
                passengerGUI.start(new Stage());
                primaryStage.close();
            } else {
                showErrorMessage("Please select a flight.");
            }
        });

        HBox formBox = new HBox(10, sourceLabel, sourceField, destinationLabel, destinationField);
        formBox.setAlignment(Pos.CENTER);
        HBox dateBox = new HBox(10, depdateLabel, depDatePicker, arrdateLabel, arrDatePicker);
        dateBox.setAlignment(Pos.CENTER);

        VBox.setMargin(formBox, new Insets(20, 0, 10, 0));
        VBox.setMargin(dateBox, new Insets(20, 0, 20, 0));
        VBox controlsBox = new VBox(10, formBox, dateBox, searchButton);
        controlsBox.setAlignment(Pos.CENTER);

        // Place buttons side by side
        HBox navBox = new HBox(10, backButton, nextButton);
        navBox.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(10, controlsBox, flightTable, navBox);
        mainLayout.setBackground(background);
        mainLayout.setStyle("-fx-padding: 10; -fx-alignment: center;");

        Scene scene = new Scene(mainLayout, 1000, 700);
        primaryStage.setTitle("Flight Search");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<Flight> createFlightTable(DateTimeFormatter dateFormatter) {
        TableView<Flight> table1 = new TableView<>();
        table1.setItems(searchResults);

        // transparent bnane k liye
        table1.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);");

        TableColumn<Flight, String> flightIdColumn = new TableColumn<>("Flight ID");
        flightIdColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getFlightId()));

        TableColumn<Flight, String> sourceColumn = new TableColumn<>("Source");
        sourceColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getSource()));

        TableColumn<Flight, String> destinationColumn = new TableColumn<>("Destination");
        destinationColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDestination()));

        TableColumn<Flight, String> departureColumn = new TableColumn<>("Departure");
        departureColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDepartureTime().format(dateFormatter)));

        TableColumn<Flight, String> arrivalColumn = new TableColumn<>("Arrival");
        arrivalColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getArrivalTime().format(dateFormatter)));

        table1.getColumns().addAll(flightIdColumn, sourceColumn, destinationColumn, departureColumn, arrivalColumn);

        table1.setRowFactory(e -> {
            TableRow<Flight> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    selectedFlight = row.getItem();
                }
            });
            return row;
        });

        return table1;
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        try {
            FlightDatabase manager = new FlightDatabase();
            manager.loadFlightsFromFile("flights.txt");
            setFlightManager(manager);
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
