package com.example.flightreservation;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.List;

public class SeatMapGUI extends Application {
    private static final int ROWS = 18;
    private static final int COLS = 6;

    private int maxSeats;
    private final Seat[][] seats = new Seat[ROWS][COLS];
    private SeatSelectionHandler seatSelectionHandler = new SeatSelectionHandler();
    private Flight selectedFlight;
    private List<Passenger> passengers;

    private GridPane seatGrid;
    private Text selectionStatus;
    private Text totalPrice;

    public SeatMapGUI(Flight selectedFlight, List<Passenger> passengers) {
        this.selectedFlight = selectedFlight;
        this.passengers = passengers;
        this.maxSeats = passengers.size();
    }

    @Override
    public void start(Stage primaryStage) {
        seatGrid = createSeatGrid();
        VBox bookingSection = createBooking(primaryStage);
        VBox seatInfoMap = seatMap();

        HBox mainLayout = new HBox(20);
        mainLayout.setPadding(new Insets(10));
        mainLayout.getChildren().addAll(seatGrid, bookingSection, seatInfoMap);

        Image Image = new Image(getClass().getResource("/com/example/sky.jpg").toExternalForm());
        BackgroundImage background = new BackgroundImage(Image,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true));
        mainLayout.setBackground(new Background(background));

        Scene scene = new Scene(mainLayout, 1000, 700);
        primaryStage.setTitle("Seat Selection and Price Calculation");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createSeatGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                String seatID = (row + 1) + "" + (char) ('A' + col);
                SeatClass seatClass;

                if (row < 3) {
                    seatClass = SeatClass.BUSINESS;
                } else if (row < 8) {
                    seatClass = SeatClass.PREMIUM_ECONOMY;
                } else {
                    seatClass = SeatClass.ECONOMY;
                }

                Seat seat = new Seat(seatID, seatClass);
                seats[row][col] = seat;

                Button seatButton = createSeatButton(seat);
                if (col == 2) {
                    GridPane.setMargin(seatButton, new Insets(0, 20, 0, 0));
                }
                grid.add(seatButton, col, row);
            }
        }

        return grid;
    }

    private Button createSeatButton(Seat seat) {
        Button seatButton = new Button(seat.getSeatID());
        updateSButtonStyle(seat, seatButton);

        seatButton.setOnAction(e -> {
            if (seat.isBooked()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "This seat is already booked.", ButtonType.OK);
                alert.showAndWait();
            } else {
                if (!seat.isSelected() && seatSelectionHandler.getSelectedSeats().size() >= maxSeats) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "You can select only " + maxSeats + " seats.", ButtonType.OK);
                    alert.showAndWait();
                } else {
                    seatSelectionHandler.seatSelection(seat);
                    updateSButtonStyle(seat, seatButton);
                    updatestatus();
                }
            }
        });

        return seatButton;
    }

    private VBox createBooking(Stage primaryStage) {
        VBox bookingSection = new VBox(20);
        bookingSection.setPadding(new Insets(20));

        selectionStatus = new Text("Select your seats");
        selectionStatus.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: white");
        totalPrice = new Text("Total Price: $0.0");
        totalPrice.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button bookButton = new Button("Complete Reservation");
        bookButton.setStyle("-fx-background-color: #000080; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5px;");
        bookButton.setOnAction(e -> {
            if (!seatSelectionHandler.getSelectedSeats().isEmpty()) {
                ReservationGUI reservationGUI = new ReservationGUI(selectedFlight, passengers, seatSelectionHandler.getSelectedSeats());
                Stage reservationStage = new Stage();
                reservationGUI.start(reservationStage);
                primaryStage.close();

                updateSeatButtons();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select at least one seat.", ButtonType.OK);
                alert.showAndWait();
            }
        });
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #008080; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5px;");
        backButton.setOnAction(e -> {
            PassengerGUI passengerGUI = new PassengerGUI();
            Stage passengerStage = new Stage();
            passengerGUI.start(passengerStage);
            primaryStage.close();
        });
        bookingSection.getChildren().addAll(selectionStatus, totalPrice, bookButton, backButton);
        return bookingSection;
    }

    private void updatestatus() {
        StringBuilder status = new StringBuilder("Selected Seats: ");
        for (Seat selectedSeat : seatSelectionHandler.getSelectedSeats()) {
            status.append(selectedSeat.getSeatID()).append(" ");
        }
        selectionStatus.setText(status.toString().trim());
        totalPrice.setText("Total Price: $" + calculatePrice());
        totalPrice.setStyle("-fx-font-size: 18px; -fx-fill: white");
    }

    private void updateSButtonStyle(Seat seat, Button seatButton) {
        if (seat.isBooked()) {
            seatButton.setStyle("-fx-background-color: gray; -fx-font-size: 14px;");
        } else if (seat.isSelected()) {
            seatButton.setStyle("-fx-background-color: pink; -fx-font-size: 14px;");
        } else {
            switch (seat.getSeatClass()) {
                case BUSINESS:
                    seatButton.setStyle("-fx-background-color: #FFD700; -fx-font-size: 14px;");
                    break;
                case PREMIUM_ECONOMY:
                    seatButton.setStyle("-fx-background-color: lightblue; -fx-font-size: 14px;");
                    break;
                case ECONOMY:
                    seatButton.setStyle("-fx-background-color: lightgreen; -fx-font-size: 14px;");
                    break;
            }
        }
    }

    private void updateSeatButtons() {
        seatGrid.getChildren().forEach(e -> {
            if (e instanceof Button seatButton) {
                String seatId = seatButton.getText();
                Seat seat = findSeatid(seatId);
                if (seat != null) {
                    updateSButtonStyle(seat, seatButton);
                }
            }
        });
    }

    private Seat findSeatid(String seatId) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (seats[row][col].getSeatID().equals(seatId)) {
                    return seats[row][col];
                }
            }
        }
        return null;
    }

    private double calculatePrice() {
        double businessPrice = 1000.0;
        double premiumEconomyPrice = 500.0;
        double economyPrice = 350.0;
        double total = 0.0;

        for (Seat seat : seatSelectionHandler.getSelectedSeats()) {
            switch (seat.getSeatClass()) {
                case BUSINESS:
                    total += businessPrice;
                    break;
                case PREMIUM_ECONOMY:
                    total += premiumEconomyPrice;
                    break;
                case ECONOMY:
                    total += economyPrice;
                    break;
            }
        }

        return total;
    }

    private VBox seatMap() {
        VBox infoMap = new VBox(20);
        infoMap.setPadding(new Insets(20));

        Text infoTitle = new Text("Seat Class Information");
        infoTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: white");
        infoMap.getChildren().add(infoTitle);

        infoMap.getChildren().add(createInfoBox("Business Class", "gold"));
        infoMap.getChildren().add(createInfoBox("Premium Economy Class", "lightblue"));
        infoMap.getChildren().add(createInfoBox("Economy Class", "lightgreen"));

        return infoMap;
    }

    private HBox createInfoBox(String label, String color) {
        HBox infoBox = new HBox(10);
        infoBox.setPadding(new Insets(5));

        Button colorBox = new Button();
        colorBox.setStyle("-fx-background-color:  " + color + "; -fx-min-width: 30px; -fx-min-height: 30px;");

        Text classLabel = new Text(label);
        classLabel.setStyle("-fx-font-size: 16px; -fx-fill: white");
        infoBox.getChildren().addAll(colorBox, classLabel);

        return infoBox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
