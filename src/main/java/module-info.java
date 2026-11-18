module ni.edu.uam.servicioweb {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.edu.uam.servicioweb to javafx.fxml;
    exports ni.edu.uam.servicioweb;
}