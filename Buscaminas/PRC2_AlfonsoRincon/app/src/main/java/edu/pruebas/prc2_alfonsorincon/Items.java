package edu.pruebas.prc2_alfonsorincon;
/*
    CLASE PARA CREAR EL SPINNER EN LA CLASE "ADAPTER"
 */
public class Items {
    private String texto;
    private int imagen;

    // Constructores
    public Items(String texto, int imagen) {
        this.texto = texto;
        this.imagen = imagen;
    }
    public Items() {

    }

    // Getters y Setters
    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }
    public int getImagen() {
        return imagen;
    }
    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
