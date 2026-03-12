module org.example._1237333_21222344_client {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.clientModel;
    opens org.example.clientModel to javafx.fxml;
    exports org.example.clientController;
    opens org.example.clientController to javafx.fxml;
}