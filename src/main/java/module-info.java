module com.example.cg4_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jimStlMeshImporterJFX;

    opens com.example.cg4_1 to javafx.fxml;
    exports com.example.cg4_1;
}