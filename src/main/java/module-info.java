
module ni.edu.uam.servicioweb {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;

    opens ni.edu.uam.servicioweb to javafx.fxml;

    opens ni.edu.uam.servicioweb.controller
            to javafx.fxml;

    opens ni.edu.uam.servicioweb.model
            to javafx.base, org.hibernate.orm.core;

    exports ni.edu.uam.servicioweb;
    exports ni.edu.uam.servicioweb.model;
}
