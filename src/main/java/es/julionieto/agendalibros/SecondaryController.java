package es.julionieto.agendalibros;

import es.julionieto.agendalibros.entities.Editorial;
import es.julionieto.agendalibros.entities.Libro;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javax.persistence.Query;
import javax.persistence.RollbackException;

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
    @FXML
    private HBox rootSecondary;

    @Override
    public void initialize(URL url, ResourceBundle rb){
    }
    
    public void setLibro(Libro libro, boolean nuevoLibro) {
        App.em.getTransaction().begin();
        if (!nuevoLibro){
            this.libro = App.em.find(Libro.class, libro.getId());
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
            
            Query queryEditorialFindAll = App.em.createNamedQuery("Editorial.findAll");
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
    
    @FXML
    private void onActionButtonCancelar(ActionEvent event){
        App.em.getTransaction().rollback();
        
        try{
            App.setRoot("Primary");
        } catch (IOException ex){
            Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @FXML
    private void onActionButtonGuardar(ActionEvent event){
        boolean errorFormato=false;
        
        libro.setTitulo(libro.getTitulo());
        libro.setAutor(libro.getAutor());
        libro.setIsbn(libro.getIsbn());
        
        if (!textFieldPrecio.getText().isEmpty()){
            try{
                libro.setPrecio(Integer.valueOf(textFieldPrecio.getText()));
            } catch(NumberFormatException ex){
                errorFormato = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Precio no valido");
            }
        }
        
        if (datePickerFechaPublicacion.getValue() != null){
            LocalDate localDate = datePickerFechaPublicacion.getValue();
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();            
            Date date = Date.from(instant);
            libro.setFechaPublicacion(date);
        } else {
            libro.setFechaPublicacion(null);
        }
        
        libro.setEditorial(comboBoxEditorial.getValue());
        
        if (!errorFormato){
            try{
                if (libro.getId()==null){
                    System.out.println("Guardando nuevo libro en BD");
                    App.em.persist(libro);
                } else{
                    System.out.println("Actualizando libro en BD");
                    App.em.merge(libro);
                }
                App.em.getTransaction().commit();
                
                App.setRoot("primary");
            } catch (RollbackException ex){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("No se han podido guardar los cambios. "+
                        "Compruebe que los datos cumplen con los requisitos");
                alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
            } catch (IOException ex){
                Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        
    }
    
    @FXML
    private void onActionButtonExaminar(ActionEvent event) throws IOException{
        File carpertaFotos = new File(CARPETA_FOTOS);
        if(!carpertaFotos.exists()){
            carpertaFotos.mkdir();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes (jpg, png)", "*.jpg","*.png"),
                 new FileChooser.ExtensionFilter("Todos los archivos", ".")
        );
        
        File file = fileChooser.showOpenDialog(rootSecondary.getScene().getWindow());
        if (file != null){
            try{
                Files.copy(file.toPath(), new File(CARPETA_FOTOS + "/" + file.getName()).toPath());
                libro.setFoto(file.getName());
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
            } catch (FileAlreadyExistsException ex){
                Alert alert = new Alert(Alert.AlertType.WARNING,"Nombre de archivo duplicado");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onActionButtonSuprimirFoto(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar supresion ed imagen");
        alert.setHeaderText("¿Desea SUPRIMIR el archivo asociado a la imagen, \n"
                + "quitar la foto pero MANTENER el archivo, \no CANCELAR la operación?");
        alert.setContentText("Elija a opción deseada");
        
        ButtonType buttonTypeEliminar = new ButtonType("Suprimir");
        ButtonType buttonTypeMantener = new ButtonType("Mantener");
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(buttonTypeEliminar, buttonTypeMantener, buttonTypeCancel);
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == buttonTypeEliminar) {
            String imageFileName = libro.getFoto();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if(file.exists()){
                file.delete();
            }
            libro.setFoto(null);
            imageViewFoto.setImage(null);
        } else if (result.get() == buttonTypeMantener){
            libro.setFoto(null);
            imageViewFoto.setImage(null);
        }
    }
    
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

}