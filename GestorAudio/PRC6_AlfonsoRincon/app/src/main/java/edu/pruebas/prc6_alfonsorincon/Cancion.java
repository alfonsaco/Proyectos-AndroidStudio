package edu.pruebas.prc6_alfonsorincon;

import com.google.gson.annotations.SerializedName;

public class Cancion {
    @SerializedName("nombre")
    private String autor;

    @SerializedName("descripcion")
    private String nombre;

    private String tipo;

    @SerializedName("URI")
    private String URI;

    private String imagen;

    // Constructores
    public Cancion(String autor, String nombre, String tipo, String URI, String imagen) {
        this.autor = autor;
        this.nombre = nombre;
        this.tipo = tipo;
        this.URI = URI;
        this.imagen = imagen;
    }
    public Cancion() {
    }

    // Getters y Setters
    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
