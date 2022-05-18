package es.julionieto.agendalibros;

import es.julionieto.agendalibros.entities.Libro;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
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
    
    public void setLibro(Libro libro) {
        App.em.getTransaction().begin();
        this.libro = libro;
        mostrarDatos();
    }    
    
    @FXML
    private void onActionButtonGuardar(ActionEvent event){
        if (libroSeleccionado != null){
            libroSeleccionado.setTitulo(textFieldTitulo.getText());
            libroSeleccionado.setAutor(textFieldAutor.getText());
            App.em.getTransaction().begin();
            App.em.merge(libroSeleccionado);
            App.em.getTransaction().commit();
            
            int numFilaSeleccionada = tableViewLibros.getSelectionModel().getSelectedIndex();
            tableViewLibros.getItems().set(numFilaSeleccionada, libroSeleccionado);
            TablePosition pos = new TablePosition(tableViewLibros, numFilaSeleccionada, null);
            tableViewLibros.getFocusModel().focus(pos);
            tableViewLibros.requestFocus();
        }
    }
    
    @FXML
    private void onActionButtonNuevo(ActionEvent event) {
        try {
            App.setRoot("secondary");
            SecondaryController secondaryController = (SecondaryController)App.fxmlLoader.getController();
            libroSeleccionado = new Libro();
            secondaryController.setLibro(libroSeleccionado, true);
        } catch (IOException ex){
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @FXML
    private void onActionButtonEditar(ActionEvent event) {
    }

    @FXML
    private void onActionButtonSuprimir(ActionEvent event) {
        if (libroSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar");
            alert.setHeaderText("¿Desea suprimir el siguiente registro?");
            alert.setContentText(libroSeleccionado.getTitulo()+" "+libroSeleccionado.getAutor());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                App.em.getTransaction().begin();
                App.em.merge(libroSeleccionado);
                App.em.getTransaction().commit();
                tableViewLibros.getItems().remove(libroSeleccionado);
                tableViewLibros.getFocusModel().focus(null);
                tableViewLibros.requestFocus();
            } else {
                int numFilaSeleccionada = tableViewLibros.getSelectionModel().getSelectedIndex();
                tableViewLibros.getItems().set(numFilaSeleccionada, libroSeleccionado);
                TablePosition pos = new TablePosition(tableViewLibros, numFilaSeleccionada, null);
                tableViewLibros.getFocusModel().focus(pos);
                tableViewLibros.requestFocus();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Atención");
            alert.setHeaderText("Debe seleccionar un registro");
            alert.showAndWait();
        }
        
    }

    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
