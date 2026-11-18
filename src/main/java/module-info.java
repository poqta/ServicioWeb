module ni.edu.uam.servicioweb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens ni.edu.uam.servicioweb to javafx.fxml;
    opens ni.edu.uam.servicioweb.model to javafx.base;

    exports ni.edu.uam.servicioweb;
    exports ni.edu.uam.servicioweb.model;
}
