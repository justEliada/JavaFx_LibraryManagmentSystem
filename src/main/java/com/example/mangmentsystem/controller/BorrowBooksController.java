package com.example.mangmentsystem.controller;

import com.example.mangmentsystem.models.Book;
import com.example.mangmentsystem.utils.DatabaseConnection;
import com.example.mangmentsystem.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class BorrowBooksController {

    @FXML
    private TableView<Book> borrowedBooksTableView;
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book, String> authorCol;
    @FXML
    private TableColumn<Book, String> genreCol;
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private Label borrowStatusLabel;

    private ObservableList<Book> borrowedBooks = FXCollections.observableArrayList();

    public void initialize() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

        fetchBorrowedBooks();
    }

    private void fetchBorrowedBooks() {
        borrowedBooks.clear();
        SessionManager sessionManager = SessionManager.getInstance();
        int loggedInUserId = sessionManager.getCurrentUserId();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT books.* FROM books JOIN borrow ON books.id = borrow.book_id WHERE borrow.user_id = ?")) {
            stmt.setInt(1, loggedInUserId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    borrowedBooks.add(new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("genre"),
                            true
                    ));
                }
                borrowedBooksTableView.setItems(borrowedBooks);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGoBack(ActionEvent event) {
        try {
            Parent addButtonView = FXMLLoader.load(getClass().getResource("/com/example/mangmentsystem/homepage.fxml"));
            Scene addButtonScene = new Scene(addButtonView);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(addButtonScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void borrowBook(ActionEvent event) {
        String title = titleField.getText();
        String author = authorField.getText();

        try {
            int bookId = getBookId(title, author);
            if (bookId != -1) {
                if (!isBookAlreadyBorrowed(bookId)) {
                    insertBorrowRecord(bookId);
                    fetchBorrowedBooks();
                    borrowStatusLabel.setText(""); // Clear the status label
                } else {
                    borrowStatusLabel.setText("Book is already borrowed.");
                }
            } else {
                System.out.println("Book not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isBookAlreadyBorrowed(int bookId) throws SQLException {
        String checkBorrowQuery = "SELECT * FROM borrow WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkBorrowStatement = conn.prepareStatement(checkBorrowQuery)) {
            checkBorrowStatement.setInt(1, bookId);
            try (ResultSet rs = checkBorrowStatement.executeQuery()) {
                return rs.next(); // book is already borrowed
            }
        }
    }

    private int getBookId(String title, String author) throws SQLException {
        String selectBookIdQuery = "SELECT id FROM books WHERE title = ? AND author = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement selectBookIdStatement = conn.prepareStatement(selectBookIdQuery)) {
            selectBookIdStatement.setString(1, title);
            selectBookIdStatement.setString(2, author);
            try (ResultSet rs = selectBookIdStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1;
    }

    private void insertBorrowRecord(int bookId) throws SQLException {
        String insertBorrowQuery = "INSERT INTO borrow (book_id, user_id, borrow_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement borrowStatement = conn.prepareStatement(insertBorrowQuery)) {
            borrowStatement.setInt(1, bookId);
            borrowStatement.setInt(2, SessionManager.getInstance().getCurrentUserId());
            borrowStatement.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            borrowStatement.executeUpdate();
        }
    }


}
