package com.example.mangmentsystem.controller;

import com.example.mangmentsystem.utils.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteBookController {
    @FXML
    private TextField titleField;

    @FXML
    private TextField publishingCompanyField;

    @FXML
    private Label statusLabel;

    @FXML
    void handleDeleteBook(ActionEvent event) {

        // Get info of book we want to delete
        String title = titleField.getText();
        String publishingCompany = publishingCompanyField.getText();

        // Delete book from the db
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM books WHERE title = ? AND author = ?");
            statement.setString(1, title);
            statement.setString(2, publishingCompany);

            int rowsAffected = statement.executeUpdate();
            connection.close();

            if (rowsAffected > 0) {
                statusLabel.setText("Book deleted successfully.");
            } else {
                statusLabel.setText("No book found!.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Failed to delete book: " + e.getMessage());
        }
    }

    /*Go Back to homepage*/
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

}
