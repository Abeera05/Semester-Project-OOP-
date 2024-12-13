package com.example.flightreservation;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Labels and Fields
        Label titleLabel = new Label("Login");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: black; -fx-font-weight: bold;"); // Set title color to black
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-text-fill: black;");
        TextField usernameField = new TextField();
        usernameField.setPrefWidth(150);  // Set the preferred width of the username field
        usernameField.setMaxWidth(150);
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-text-fill: black;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefWidth(150);  // Set the preferred width of the password field
        passwordField.setMaxWidth(150);
        // Buttons
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 14px;"); // Set login button to black background with white text

        Button resetPasswordButton = new Button("Reset Password");
        resetPasswordButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: black; -fx-font-style: italic;");

        // Button Actions
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = Authentication.validateLogin(username, password);

            if (role != null) {
                messageLabel.setText("Login successful. Role: " + role);
                if (role.equals("admin")) {
                    new AdminPanel().start(new Stage());
                } else if (role.equals("passenger")) {
                    new FlightDetailsGUI().start(new Stage());
                }
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        });

        resetPasswordButton.setOnAction(e -> openResetPasswordUI()); // Open Reset Password Window

        // Layout for login form
        VBox loginForm = new VBox(10, titleLabel, usernameLabel, usernameField, passwordLabel, passwordField, loginButton, resetPasswordButton, messageLabel);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-padding: 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        loginForm.setPrefWidth(300);
        loginForm.setMaxWidth(300);
        loginForm.setPrefHeight(450); // Set a fixed height for the form
        loginForm.setMaxHeight(450);

        StackPane root = new StackPane();
//        String resourcePath = getClass().getResource("/com/example/sp24bse126/background.jpg").toExternalForm();
//        root.setStyle("-fx-background-image: url('" + resourcePath + "'); -fx-background-size: cover; -fx-background-position: center;");

        // Add login form to the center of the screen
        root.getChildren().add(loginForm);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openResetPasswordUI() {
        Stage resetStage = new Stage();
        Label titleLabel = new Label("Reset Password");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-weight: bold;");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPrefWidth(150);  // Set the preferred width of the username field
        usernameField.setMaxWidth(150);

        Label oldPasswordLabel = new Label("Old Password:");
        PasswordField oldPasswordField = new PasswordField();
        oldPasswordField.setPrefWidth(150);  // Set the preferred width of the password field
        oldPasswordField.setMaxWidth(150);
        Label newPasswordLabel = new Label("New Password:");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPrefWidth(150);  // Set the preferred width of the password field
        newPasswordField.setMaxWidth(150);
        Button resetButton = new Button("Reset Password");
        resetButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");

        Label resetMessageLabel = new Label();
        resetMessageLabel.setStyle("-fx-text-fill: black;");

        resetButton.setOnAction(e -> {
            String username = usernameField.getText();
            String oldPassword = oldPasswordField.getText();
            String newPassword = newPasswordField.getText();

            boolean success = Authentication.resetPassword(username, oldPassword, newPassword);
            if (success) {
                resetMessageLabel.setText("Password reset successfully!");
            } else {
                resetMessageLabel.setText("Invalid username or old password.");
            }
        });

        VBox resetLayout = new VBox(10, titleLabel, usernameLabel, usernameField, oldPasswordLabel, oldPasswordField, newPasswordLabel, newPasswordField, resetButton, resetMessageLabel);
        resetLayout.setAlignment(Pos.CENTER);
        resetLayout.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-padding: 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Scene resetScene = new Scene(resetLayout, 400, 400);

        resetStage.setTitle("Reset Password");
        resetStage.setScene(resetScene);
        resetStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

