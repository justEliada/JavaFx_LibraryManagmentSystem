module com.example.mangmentsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Add the opens directive for the package containing the Book class
    opens com.example.mangmentsystem.models to javafx.base, javafx.fxml;
    opens com.example.mangmentsystem to javafx.fxml;
    exports com.example.mangmentsystem;
    exports com.example.mangmentsystem.controller;
    opens com.example.mangmentsystem.controller to javafx.fxml;
}