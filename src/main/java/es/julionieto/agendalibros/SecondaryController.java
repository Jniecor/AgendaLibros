package es.julionieto.agendalibros;

import es.julionieto.agendalibros.entities.Editorial;
import es.julionieto.agendalibros.entities.Libro;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import javax.persistence.Query;

public class SecondaryController implements Initializable{
    
    private Libro libro;
    private boolean nuevoLibro;
    
    private static final String CARPETA_FOTOS = "Fotos";
    
    @FXML
    private TextField textFieldTitulo;
    @FXML
    private TextField textFieldAutor;
    @FXML
    private TextField textFieldISBN;
    @FXML
    private TextField textFieldPrecio;
    @FXML
    private DatePicker datePickerFechaPublicacion;
    @FXML
    private ComboBox<Editorial> comboBoxEditorial;
    @FXML
    private ImageView imageViewFoto;

    @Override
    public void initialize(URL url, ResourceBundle rb){
    }
    
    public void setLibro(Libro libro, boolean nuevoLibro) {
        App.em.getTransaction().begin();
        if (!nuevoLibro){
            this.libro = App.em.find(Libro.class, libro.getAutor());
        } else {
            this.libro = libro;
        }
        this.nuevoLibro = nuevoLibro;
        mostrarDatos();
    }
    
    private void mostrarDatos(){
        textFieldTitulo.setText(libro.getTitulo());
        textFieldAutor.setText(libro.getAutor());
        textFieldISBN.setText(libro.getIsbn());
        
        if (libro.getPrecio() != null){
            textFieldPrecio.setText(String.valueOf(libro.getPrecio()));
        }
        
        if (libro.getFechaPublicacion() != null){
            Date date = libro.getFechaPublicacion();
            Instant instant = date.toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            LocalDate localDate = zdt.toLocalDate();
            datePickerFechaPublicacion.setValue(localDate);
            
            Query queryEditorialFindAll = App.em.createNamedQuery("Editoral.findAll");
            List<Editorial> listEditorial = queryEditorialFindAll.getResultList();
            
            comboBoxEditorial.setItems(FXCollections.observableList(listEditorial));
            if (libro.getEditorial() != null){
                comboBoxEditorial.setValue(libro.getEditorial());
            }
            comboBoxEditorial.setCellFactory((ListView<Editorial> l) -> new ListCell<Editorial>() {
                @Override
                protected void updateItem(Editorial editorial, boolean empty) {
                    super.updateItem(editorial, empty);
                    if (editorial == null || empty){
                        setText("");
                    } else {
                        setText(editorial.getCodigo() + "-" + editorial.getNombre());
                    }               
                }
            });
            comboBoxEditorial.setConverter(new StringConverter<Editorial>(){
                @Override
                public String toString(Editorial editorial) {
                    if (editorial == null){
                        return null;
                    } else {
                        return editorial.getCodigo() + "-" + editorial.getNombre();
                    }
                }
                
                @Override
                public Editorial fromString(String userId){
                    return null;
                }
            });
            
            if (libro.getFoto() != null){
                String imageFileName = libro.getFoto();
                File file = new File(CARPETA_FOTOS+"/"+imageFileName);
                if (file.exists()){
                    Image image = new Image(file.toURI().toString());
                    imageViewFoto.setImage(image);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No se encuentra la imagen");
                    alert.showAndWait();
                }
            }
                
        }
    }
    
    
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}