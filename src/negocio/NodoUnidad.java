package negocio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import enums.TipoUnidad;
import modelo.UnidadBiblioteca;

public class NodoUnidad {
    private final UnidadBiblioteca dato;
    private final List<NodoUnidad> hijos = new ArrayList<>();
    private final CatalogoABB catalogo;

    public NodoUnidad(UnidadBiblioteca dato) {
        if (dato == null) {
            throw new IllegalArgumentException("El nodo debe contener una unidad.");
        }
        this.dato = dato;
        this.catalogo = dato.getTipo() == TipoUnidad.ESTANTE ? new CatalogoABB() : null;
    }

    public UnidadBiblioteca getDato() {
        return dato;
    }

    public List<NodoUnidad> getHijos() {
        return Collections.unmodifiableList(hijos);
    }

    public void agregarHijo(NodoUnidad hijo) {
        if (hijo == null) {
            throw new IllegalArgumentException("El hijo no puede ser null.");
        }
        hijos.add(hijo);
    }

    public CatalogoABB getCatalogo() {
        return catalogo;
    }
}
