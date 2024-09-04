module org.example.student {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.student to javafx.fxml;
    opens org.example.controller to javafx.fxml;
    opens org.example.model to javafx.base;

    exports org.example.student;
    exports org.example.controller;
    exports org.example.model;
    exports org.example.db;
}
