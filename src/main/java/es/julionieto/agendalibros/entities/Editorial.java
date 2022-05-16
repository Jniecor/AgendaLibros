package es.julionieto.agendalibros.entities;

import java.io.Serializable;
import java.util.Collection;

@javax.persistence.Entity
@javax.persistence.Table(name = "EDITORIAL")
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = "Editorial.findAll", query = "SELECT p FROM Editorial p"),
    @javax.persistence.NamedQuery(name = "Editorial.findById", query = "SELECT p FROM Editorial p WHERE p.id = :id"),
    @javax.persistence.NamedQuery(name = "Editorial.findByCodigo", query = "SELECT p FROM Editorial p WHERE p.codigo = :codigo"),
    @javax.persistence.NamedQuery(name = "Editorial.findByNombre", query = "SELECT p FROM Editorial p WHERE p.nombre = :nombre")})
public class Editorial implements Serializable {

    private static final long serialVersionUID = 1L;
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "ID")
    private Integer id;
    @javax.persistence.Column(name = "CODIGO")
    private String codigo;
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "NOMBRE")
    private String nombre;
    @javax.persistence.OneToMany(mappedBy = "provincia")
    private Collection<Libro> libroCollection;

    public Editorial() {
    }

    public Editorial(Integer id) {
        this.id = id;
    }

    public Editorial(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Collection<Libro> getLibroCollection() {
        return libroCollection;
    }

    public void setLibroCollection(Collection<Libro> libroCollection) {
        this.libroCollection = libroCollection;
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
        if (!(object instanceof Editorial)) {
            return false;
        }
        Editorial other = (Editorial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.julionieto.agendalibros.entities.Editorial[ id=" + id + " ]";
    }
    
}
