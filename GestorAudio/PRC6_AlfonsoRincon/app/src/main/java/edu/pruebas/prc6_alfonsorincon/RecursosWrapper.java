package edu.pruebas.prc6_alfonsorincon;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
    ESTA CLASE SE USA PARA OBTENER LOS DATOS DEL JSON
 */
public class RecursosWrapper {
    @SerializedName("recursos_list")
    private List<Cancion> listaRecursos;

    public List<Cancion> getListaRecursos() {
        return listaRecursos;
    }

    public void setListaRecursos(List<Cancion> listaRecursos) {
        this.listaRecursos = listaRecursos;
    }
}
