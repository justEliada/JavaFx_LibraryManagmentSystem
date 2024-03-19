package com.example.mangmentsystem.controller;

import com.example.mangmentsystem.models.Book;
import com.example.mangmentsystem.models.PublishingCompany;
import com.example.mangmentsystem.utils.DatabaseConnection;
import com.example.mangmentsystem.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Book> bookTableView;
    @FXML
    private TableView<PublishingCompany> publishingCompanyTableView;
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book, String> authorCol;
    @FXML
    private TableColumn<Book, String> genreCol;
    @FXML
    private TableColumn<PublishingCompany, String> companyNameCol;
    @FXML
    private TableColumn<PublishingCompany, String> addressCol;
    @FXML
    private TableColumn<PublishingCompany, String> streetCol;

    private ObservableList<Book> books = FXCollections.observableArrayList();
    private ObservableList<PublishingCompany> publishingCompanies = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        companyNameCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        streetCol.setCellValueFactory(new PropertyValueFactory<>("street"));

        fetchBooksByGenre("");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

    }

    /*Search for book*/
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        ObservableList<Book> filteredBooks = FXCollections.observableArrayList();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchText) || book.getAuthor().toLowerCase().contains(searchText)) {
                filteredBooks.add(book);
            }
        }

        bookTableView.setItems(filteredBooks);
    }

    /*Show all books*/
    @FXML
    private void handleAllBooks() {
        fetchBooksByGenre("");
        toggleTableViews(true);
    }

    /*SHow books based on genre*/
    @FXML
    private void handleGenreSelection(javafx.event.ActionEvent event) {
        String genre = ((javafx.scene.control.MenuItem)event.getSource()).getText();
        fetchBooksByGenre(genre);
        toggleTableViews(true);
    }

    /*Show all the publishing companies*/
    @FXML
    private void handleAllPublishingCompanies() {
        fetchAllPublishingCompanies();
        toggleTableViews(false);
    }

    /*Method for getting genre*/
    private void fetchBooksByGenre(String genre) {
        books.clear();
        String query = genre.isEmpty() ? "SELECT * FROM books WHERE available = TRUE" : "SELECT * FROM books WHERE genre = '" + genre + "' AND available = TRUE";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        true // Assume all fetched books are available
                ));
            }
            bookTableView.setItems(books);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleBorrowBook(ActionEvent event) {
        try {
            Parent addButtonView = FXMLLoader.load(getClass().getResource("/com/example/mangmentsystem/borrowbooks.fxml"));
            Scene addButtonScene = new Scene(addButtonView);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(addButtonScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Method for getting publishing companies*/
    private void fetchAllPublishingCompanies() {
        publishingCompanies.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM publishing")) {
            while (rs.next()) {
                publishingCompanies.add(new PublishingCompany(
                        rs.getInt("id"),
                        rs.getString("company_name"),
                        rs.getString("address"),
                        rs.getString("street")));
            }
            publishingCompanyTableView.setItems(publishingCompanies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Toggle Action*/
    private void toggleTableViews(boolean showBooks) {
        bookTableView.setVisible(showBooks);
        publishingCompanyTableView.setVisible(!showBooks);
    }

    /*Go to add book*/
    @FXML
    private void handleNavigateButtonAction(ActionEvent event) {
        try {
            Parent addButtonView = FXMLLoader.load(getClass().getResource("/com/example/mangmentsystem/addbooks.fxml"));
            Scene addButtonScene = new Scene(addButtonView);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(addButtonScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Go to Delete book*/
    @FXML
    private void handleNavigateButtonAction2(ActionEvent event) {
        try {
            Parent addButtonView = FXMLLoader.load(getClass().getResource("/com/example/mangmentsystem/deletebook.fxml"));
            Scene addButtonScene = new Scene(addButtonView);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(addButtonScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Quit aplication*/
    @FXML
    void handleQuit(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        redirectToLoginPage();
    }

    private void redirectToLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mangmentsystem/login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
