package es.julionieto.agendalibros;

import es.julionieto.agendalibros.entities.Libro;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.Query;

public class PrimaryController implements Initializable {

    private Libro libroSeleccionado;
    @FXML
    private TableView<Libro> tableViewLibros;
    @FXML
    private TableColumn<Libro, String> columnTitulo;
    @FXML
    private TableColumn<Libro, String> columnAutor;
    @FXML
    private TableColumn<Libro, String> columnIsbn;
    @FXML
    private TableColumn<Libro, String> columnEditorial;
    @FXML
    private TextField textFieldTitulo;
    @FXML
    private TextField textFieldAutor;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        columnTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        columnAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        columnIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        columnEditorial.setCellValueFactory(
                cellData -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    if (cellData.getValue().getEditorial() != null) {
                        property.setValue(cellData.getValue().getEditorial().getNombre());
                    }
                    return property;
                });
        tableViewLibros.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->{
                    libroSeleccionado = newValue;
                    if (libroSeleccionado != null) {
                        textFieldTitulo.setText(libroSeleccionado.getTitulo());
                        textFieldAutor.setText(libroSeleccionado.getAutor());
                    } else {
                        textFieldTitulo.setText("");
                        textFieldAutor.setText("");
                    }
                });
        cargarTodosLibros();
    }
    
    private void cargarTodosLibros(){
        Query queryLibroFindAll = App.em.createNamedQuery("Libro.findAll");
        List<Libro> listLibro = queryLibroFindAll.getResultList();
        System.out.print("a" + listLibro.size());
        tableViewLibros.setItems(FXCollections.observableArrayList(listLibro));
    }
    
    
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
