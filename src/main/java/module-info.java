module com.example.uasproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.uasproject to javafx.fxml;
    exports com.example.uasproject;
    exports com.example.uasproject.classes;
    opens com.example.uasproject.classes to javafx.fxml;
}