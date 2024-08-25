module com.example.ninemensmorris {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.corgi.ninemensmorris to javafx.fxml;
    exports com.corgi.ninemensmorris;
}