module Controlador {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;
    requires java.desktop; 

    opens Controlador to javafx.fxml;
    exports Controlador;
}
