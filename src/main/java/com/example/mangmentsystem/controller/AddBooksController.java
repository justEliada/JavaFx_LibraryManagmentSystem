package com.example.mangmentsystem.controller;

import com.example.mangmentsystem.utils.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddBooksController {
    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private ComboBox<String> genreComboBox;

    @FXML
    private TextField publishingIdField;

    @FXML
    private Button addBookButton;

    @FXML
    private Label statusLabel;


    /*Add Books*/
    @FXML
    void handleAddBook(ActionEvent event) {
        String title = titleField.getText();
        String author = authorField.getText();
        String genre = genreComboBox.getValue();
        int publishingId = Integer.parseInt(publishingIdField.getText());
        // Connecting to the db
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO books (title, author, genre, publishing_id) VALUES (?, ?, ?, ?)");
            System.out.println("Publishing ID: " + publishingId);

            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, genre);
            statement.setInt(4, publishingId);
            int rowsAffected = statement.executeUpdate();
            connection.close();

            if (rowsAffected > 0) {
                statusLabel.setText("Book added successfully.");
            } else {
                statusLabel.setText("Failed to add book.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Failed to add book: " + e.getMessage());
        }
    }

    /*Go back to Homepage*/
    @FXML
    private void handleGoBack(ActionEvent event) {
        try {

            Parent addButtonView = FXMLLoader.load(getClass().getResource("/com/example/mangmentsystem/homepage.fxml"));
            Scene addButtonScene = new Scene(addButtonView);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(addButtonScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Quit Application*/
    @FXML
    void handleQuit(ActionEvent event) {

        System.exit(0);
    }
}
