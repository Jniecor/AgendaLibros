package es.julionieto.agendalibros.entities;

import java.io.Serializable;
import java.util.Date;

@javax.persistence.Entity
@javax.persistence.Table(name = "LIBRO")
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = "Libro.findAll", query = "SELECT p FROM Libro p"),
    @javax.persistence.NamedQuery(name = "Libro.findById", query = "SELECT p FROM Libro p WHERE p.id = :id"),
    @javax.persistence.NamedQuery(name = "Libro.findByTitulo", query = "SELECT p FROM Libro p WHERE p.titulo = :titulo"),
    @javax.persistence.NamedQuery(name = "Libro.findByAutor", query = "SELECT p FROM Libro p WHERE p.autor = :autor"),
    @javax.persistence.NamedQuery(name = "Libro.findByEditorial", query = "SELECT p FROM Libro p WHERE p.editorial = :editorial"),
    @javax.persistence.NamedQuery(name = "Libro.findByIsbn", query = "SELECT p FROM Libro p WHERE p.isbn = :isbn"),
    @javax.persistence.NamedQuery(name = "Libro.findByFechaPublicacion", query = "SELECT p FROM Libro p WHERE p.fechaPublicacion = :fechaPublicacion"),
    @javax.persistence.NamedQuery(name = "Libro.findByPrecio", query = "SELECT p FROM Libro p WHERE p.precio = :precio"),
    @javax.persistence.NamedQuery(name = "Libro.findByFoto", query = "SELECT p FROM Libro p WHERE p.foto = :foto")})
public class Libro implements Serializable {

    private static final long serialVersionUID = 1L;
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "ID")
    private Integer id;
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "TITULO")
    private String titulo;
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "AUTOR")
    private String autor;
//    @javax.persistence.Column(name = "EDITORIAL")
//    private String editorial;
    @javax.persistence.Column(name = "ISBN")
    private String isbn;
    @javax.persistence.Column(name = "FECHA_PUBLICACION")
    @javax.persistence.Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaPublicacion;
    @javax.persistence.Column(name = "PRECIO")
    private Integer precio;
    @javax.persistence.Column(name = "FOTO")
    private String foto;
    @javax.persistence.JoinColumn(name = "EDITORIAL", referencedColumnName = "ID")
    @javax.persistence.ManyToOne
    private Editorial editorial;

    public Libro() {
    }

    public Libro(Integer id) {
        this.id = id;
    }

    public Libro(Integer id, String titulo, String autor) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Libro)) {
            return false;
        }
        Libro other = (Libro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.julionieto.agendalibros.entities.Libro[ id=" + id + " ]";
    }
    
}
