package com.example.mangmentsystem.controller;

import com.example.mangmentsystem.utils.DatabaseConnection;
import com.example.mangmentsystem.utils.SessionManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    private Stage registerStage;

    @FXML
    private Button loginButton;

    public void setRegisterStage(Stage registerStage) {
        this.registerStage = registerStage;
    }

    @FXML
    private Button exitButton;

    @FXML
    private Button signUpButton;

    @FXML
    private Label retryLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;
    public void exitButtonOnAction(ActionEvent e){
       /* Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();*/
        Platform.exit();
    }

    public void loginButtonOnAction(ActionEvent e) {
        if (!usernameTextField.getText().isBlank() && !passwordField.getText().isBlank()) {
            System.out.println("Login button clicked!");
            validateLogin();
        } else {
            retryLabel.setText("Please enter credentials");
        }
    }

    public void validateLogin() {
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();

        String verifyLogin = "SELECT idUsers FROM Users WHERE username=? AND password=?";

        try {
            PreparedStatement statement = connectDB.prepareStatement(verifyLogin);
            statement.setString(1, usernameTextField.getText());
            statement.setString(2, passwordField.getText());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("idUsers");
                SessionManager.getInstance().setCurrentUserId(userId);
                retryLabel.setText("Login Successful");
                // Load homepage if login successful
                loadHomePage();
            } else {
                retryLabel.setText("Invalid. Please try again");
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connectDB != null) {
                    connectDB.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }




    /*Homepage Navigation*/
    @FXML
    void loadHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mangmentsystem/homepage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading homepage.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /*Go to signup action*/
    @FXML
    void signUpButtonOnAction(ActionEvent event) {
        try {
            System.out.println("Loading register.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mangmentsystem/register.fxml"));
            Parent root = loader.load();
            RegisterController registerController = loader.getController();
            registerController.setLoginStage((Stage) signUpButton.getScene().getWindow());
            /*Debugging*/
            System.out.println("Creating scene...");
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            System.out.println("Register scene displayed.");
        } catch (IOException e) {
            System.out.println("Error loading register.fxml: " + e.getMessage());
            e.printStackTrace();
        }

    }


}






