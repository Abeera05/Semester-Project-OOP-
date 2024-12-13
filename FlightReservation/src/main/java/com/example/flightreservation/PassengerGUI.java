package com.example.flightreservation;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class PassengerGUI extends Application {

    private int numberOfPassengers;
    private List<Passenger> passengers;
    private Flight selectedFlight;
    private GridPane grid;

    public PassengerGUI(Flight selectedFlight) {
        this.selectedFlight = selectedFlight;
        this.passengers = new ArrayList<>();
    }

    public PassengerGUI() {
    }

    @Override
    public void start(Stage primaryStage) {
        grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(15);
        grid.setAlignment(Pos.CENTER);

        Image image = new Image(getClass().getResource("/com/example/pass1.jpg").toExternalForm());
        if (image.isError()) {
            System.out.println("Error loading image.");
        } else {
            BackgroundImage backgroundImage = new BackgroundImage(
                    image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, true, true)
            );
            grid.setBackground(new Background(backgroundImage));
        }

        Label passCountLabel = new Label("Select number of passengers:");
        passCountLabel.setFont(new Font("Arial", 24));
        passCountLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24px; -fx-text-fill: black;");

        ComboBox<Integer> passComboBox = new ComboBox<>();
        passComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        passComboBox.setValue(1);
        passComboBox.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");

        grid.add(passCountLabel, 0, 0);
        grid.add(passComboBox, 1, 0);

        Button showFormButton = new Button("Show Form");
        showFormButton.setStyle("-fx-background-color: #000080; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 5px;");
        showFormButton.setOnAction(e -> {
            numberOfPassengers = passComboBox.getValue();
            displayForm(primaryStage);
        });

        grid.add(showFormButton, 1, 1);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #008080; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 5px;");
        backButton.setOnAction(e -> {
            FlightDetailsGUI flightDetailsGUI = new FlightDetailsGUI();
            Stage flightDetailsStage = new Stage();
            flightDetailsGUI.start(flightDetailsStage);
        });

        grid.add(backButton, 0, 1);

        Scene scene = new Scene(grid, 1000, 700);
        primaryStage.setTitle("Passenger Details");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void displayForm(Stage primaryStage) {
        grid.getChildren().clear();
        List<TextField[]> fieldsList = new ArrayList<>();

        for (int i = 0; i < numberOfPassengers; i++) {
            int colOffset = i * 2;

            Label label = new Label("Details of person " + (i + 1));
            label.setFont(new Font("Arial", 20));
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: black;");
            grid.add(label, colOffset, 0);

            TextField nameField = createStyledTextField();
            TextField emailField = createStyledTextField();
            TextField phoneField = createStyledTextField();
            TextField passportField = createStyledTextField();
            TextField nationalityField = createStyledTextField();
            TextField addressField = createStyledTextField();

            fieldsList.add(new TextField[]{nameField, emailField, phoneField, passportField, nationalityField, addressField});

            // Bold label styles
            Label nameLabel = new Label("Name:");
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            grid.add(nameLabel, colOffset, 1);
            grid.add(nameField, colOffset + 1, 1);

            Label emailLabel = new Label("Email:");
            emailLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            grid.add(emailLabel, colOffset, 2);
            grid.add(emailField, colOffset + 1, 2);

            Label phoneLabel = new Label("Phone:");
            phoneLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            grid.add(phoneLabel, colOffset, 3);
            grid.add(phoneField, colOffset + 1, 3);

            Label passportLabel = new Label("Passport Number:");
            passportLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            grid.add(passportLabel, colOffset, 4);
            grid.add(passportField, colOffset + 1, 4);

            Label nationalityLabel = new Label("Nationality:");
            nationalityLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            grid.add(nationalityLabel, colOffset, 5);
            grid.add(nationalityField, colOffset + 1, 5);

            Label addressLabel = new Label("Address:");
            addressLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            grid.add(addressLabel, colOffset, 6);
            grid.add(addressField, colOffset + 1, 6);
        }

        Button nextButton = new Button("Next");
        nextButton.setStyle("-fx-background-color: #000080; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 5px;");
        nextButton.setOnAction(e -> {
            if (ifFieldsFilled(fieldsList)) {
                passengers.clear();
                for (TextField[] fields : fieldsList) {
                    Passenger passenger = new Passenger(
                            fields[0].getText(),
                            fields[1].getText(),
                            fields[2].getText(),
                            fields[3].getText(),
                            fields[4].getText(),
                            fields[5].getText()
                    );
                    passengers.add(passenger);
                }

                SeatMapGUI seatMapGUI = new SeatMapGUI(selectedFlight, passengers);
                primaryStage.close();
                seatMapGUI.start(new Stage());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill all fields before proceeding.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        grid.add(nextButton, 1, numberOfPassengers * 7 + 1);
    }

    private boolean ifFieldsFilled(List<TextField[]> fieldsList) {
        for (TextField[] fields : fieldsList) {
            for (TextField field : fields) {
                if (field.getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private TextField createStyledTextField() {
        TextField textField = new TextField();
        textField.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-radius: 5px;");
        return textField;
    }
}
