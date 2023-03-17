module com.example.fxandmysql {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.fxandmysql to javafx.fxml;
    exports com.example.fxandmysql;
}