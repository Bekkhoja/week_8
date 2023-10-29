module com.example.week_8 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires org.json;



    opens com.example.week_8 to javafx.fxml;
    exports com.example.week_8;
}