package com.example.flightreservation;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.util.List;

public class ReservationGUI extends Application {
    private Flight flight;
    private List<Passenger> passengers;
    private List<Seat> selectedSeats;

    private TextField resNumField;
    private TextField flightIdField;
    private VBox delResFields;

//    private MediaPlayer backgroundMusic; // MediaPlayer to play background music

    public ReservationGUI(Flight flight, List<Passenger> passengers, List<Seat> selectedSeats) {
        this.flight = flight;
        this.passengers = passengers;
        this.selectedSeats = selectedSeats;
    }

    @Override
    public void start(Stage primaryStage) {

        BackgroundImage Image = new BackgroundImage(
                new Image(getClass().getResource("/com/example/reservation.jpg").toExternalForm()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true)
        );
        Background background = new Background(Image);

        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setBackground(background);

        Label headerLabel = new Label("Reservation Summary");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        mainLayout.getChildren().add(headerLabel);

        VBox ticketLayout = createTicketLayout();
        mainLayout.getChildren().add(ticketLayout);

        // Buttons for completing or deleting reservations
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button resButton = new Button("Complete Reservation");
        resButton.setStyle("-fx-background-color: #000080; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 5px;");
        resButton.setOnAction(e -> makeReservation());

        Button deleteResButton = new Button("Delete Reservation");
        deleteResButton.setStyle("-fx-background-color: #000080; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 5px;");
        deleteResButton.setOnAction(e -> showDeleteFields());

        buttonBox.getChildren().addAll(resButton, deleteResButton);
        mainLayout.getChildren().add(buttonBox);

        // Delete Reservation Fields
        delResFields = createDelFields();
        mainLayout.getChildren().add(delResFields);

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #008080; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 5px;");
        backButton.setOnAction(e -> {
            SeatMapGUI seatMapGUI = new SeatMapGUI(flight, passengers);
            primaryStage.close();
            Stage seatMapStage = new Stage();
            seatMapGUI.start(seatMapStage);
        });

        mainLayout.getChildren().add(backButton);

        Scene scene = new Scene(mainLayout, 1000, 700);
        primaryStage.setTitle("Reservation Details");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createTicketLayout() {
        VBox ticketLayout = new VBox(10);
        ticketLayout.setAlignment(Pos.CENTER);
        ticketLayout.setPadding(new Insets(15));
        ticketLayout.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-border-radius: 10px; " +
                "-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10px;");

        Label flightDetails = new Label("Flight Details:\n" +
                "Flight ID: " + flight.getFlightId() +
                "\nSource: " + flight.getSource() +
                "\nDestination: " + flight.getDestination() +
                "\nDeparture: " + flight.getDepartureTime() +
                "\nArrival: " + flight.getArrivalTime());

        StringBuilder passengerDetails = new StringBuilder("Passengers:\n");
        for (Passenger passenger : passengers) {
            passengerDetails.append(passenger.getFirstName()).append("\n");
        }
        Label passengersLabel = new Label(passengerDetails.toString());

        StringBuilder seatDetails = new StringBuilder("Selected Seats:\n");
        for (Seat seat : selectedSeats) {
            seatDetails.append(seat.getSeatID()).append(" - ").append(seat.getSeatClass()).append("\n");
        }
        Label seatsLabel = new Label(seatDetails.toString());

        ticketLayout.getChildren().addAll(flightDetails, passengersLabel, seatsLabel);
        return ticketLayout;
    }

    private VBox createDelFields() {
        VBox deleteFields = new VBox(10);
        deleteFields.setAlignment(Pos.CENTER);
        deleteFields.setVisible(false);

        Label resNoLabel = new Label("Enter Reservation Number:");
        resNumField = new TextField();
        resNumField.setPromptText("Reservation Number");

        Label flightIdLabel = new Label("Enter Flight ID:");
        flightIdField = new TextField();
        flightIdField.setPromptText("Flight ID");

        Button confirmDelButton = new Button("Confirm Delete");
        confirmDelButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        confirmDelButton.setOnAction(e -> deleteReservation());

        deleteFields.getChildren().addAll(resNoLabel, resNumField, flightIdLabel, flightIdField, confirmDelButton);
        return deleteFields;
    }

    private void makeReservation() {
        try {
            ReservationManager reservationManager = new ReservationManager();
            reservationManager.makeReservation(flight, selectedSeats, passengers);
//            playReservationSound();  // Play sound after reservation is made
            showConfirmationAlert("Reservation completed successfully!");
        } catch (IllegalArgumentException e) {
            showErrorAlert("Failed to make reservation: " + e.getMessage());
        }
    }

//    private void playReservationSound() {
//        // Play the sound if not already playing
//        if (backgroundMusic == null) {
//            String musicFile = getClass().getResource("/com/example/notification.mp3").toString();
//            Media media = new Media(musicFile);
//            backgroundMusic = new MediaPlayer(media);
//            backgroundMusic.play();
//        }
//    }

    private void showDeleteFields() {
        delResFields.setVisible(true);
    }

    private void deleteReservation() {
        String reservationNumber = resNumField.getText();
        String flightId = flightIdField.getText();

        if (reservationNumber.isEmpty() || flightId.isEmpty()) {
            showErrorAlert("Please enter both reservation number and flight ID.");
            return;
        }

        ReservationManager reservationManager = new ReservationManager();
        reservationManager.deleteReservation(reservationNumber, flightId);
        showConfirmationAlert("Reservation deleted successfully.");
        delResFields.setVisible(false);
    }

    private void showConfirmationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
