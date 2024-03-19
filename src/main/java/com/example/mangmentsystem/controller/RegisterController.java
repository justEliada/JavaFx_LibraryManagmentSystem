package com.example.mangmentsystem.controller;

import com.example.mangmentsystem.utils.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterController {
    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private Label registrationStatusLabel;

    @FXML
    private Button registerButton;

    private Stage loginStage;

    public void setLoginStage(Stage loginStage) {

        this.loginStage = loginStage;
    }

    @FXML
    void registerButtonOnAction(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        loginStage.close();
        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnection();

            String insertQuery = "INSERT INTO Users (username, password, FirstName, LastName) VALUES ('" +
                    username + "', '" + password + "', '" + firstName + "', '" + lastName + "')";

            Statement statement = connectDB.createStatement();
            int rowsAffected = statement.executeUpdate(insertQuery);

            if (rowsAffected == 1) {
                registrationStatusLabel.setText("Registration successful");
                Stage registerStage = (Stage) registerButton.getScene().getWindow();
                registerStage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mangmentsystem/homepage.fxml"));
                Scene scene = new Scene(loader.load());
                HomePageController homePageController = loader.getController();
                Stage homeStage = new Stage();
                homeStage.setScene(scene);
                homeStage.show();
            } else {
                registrationStatusLabel.setText("Registration failed");
            }

            connectDB.close(); /*Close connection not to put too much info*/
        } catch (SQLException | IOException e) {
            registrationStatusLabel.setText("Error: " + e.getMessage());
        }
    }


    @FXML
    void goBackToLoginAction(ActionEvent event) {
        try {
            Stage registerStage = (Stage) registerButton.getScene().getWindow();
            registerStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mangmentsystem/login.fxml"));
            Scene scene = new Scene(loader.load());
            LoginController loginController = loader.getController();
            loginController.setRegisterStage(loginStage);
            loginStage.setScene(scene);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
