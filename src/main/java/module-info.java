module es.julionieto.agendalibros {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.instrument;
    requires java.persistence;
    requires java.sql;
    
    opens es.julionieto.agendalibros.entities;
    opens es.julionieto.agendalibros to javafx.fxml;
    exports es.julionieto.agendalibros;
}