package negocio;

import enums.TipoUnidad;
import modelo.UnidadBiblioteca;

public class ArbolBiblioteca {
    private NodoUnidad raiz;

    public ArbolBiblioteca() {
    }

    public ArbolBiblioteca(UnidadBiblioteca raiz) {
        if (raiz == null || raiz.getTipo() != TipoUnidad.BIBLIOTECA) {
            throw new IllegalArgumentException("La raíz debe ser una unidad de tipo BIBLIOTECA.");
        }
        this.raiz = new NodoUnidad(raiz);
    }

    public NodoUnidad getRaiz() {
        return raiz;
    }

    public boolean agregar(String codigoPadre, UnidadBiblioteca nueva) {
        if (raiz == null || codigoPadre == null || nueva == null
                || buscar(nueva.getCodigo()) != null) {
            return false;
        }
        NodoUnidad padre = buscar(codigoPadre);
        if (padre == null || !padre.getDato().getTipo().puedeTenerHijo(nueva.getTipo())) {
            return false;
        }
        padre.agregarHijo(new NodoUnidad(nueva));
        return true;
    }

    public NodoUnidad buscar(String codigo) {
        if (codigo == null) {
            return null;
        }
        return buscar(raiz, codigo.trim());
    }

    private NodoUnidad buscar(NodoUnidad nodo, String codigo) {
        if (nodo == null) {
            return null;
        }
        if (nodo.getDato().getCodigo().equalsIgnoreCase(codigo)) {
            return nodo;
        }
        return buscarHijoPorCodigo(nodo, codigo, 0);
    }

    private NodoUnidad buscarHijoPorCodigo(NodoUnidad nodo, String codigo, int indice) {
        if (indice >= nodo.getHijos().size()) {
            return null;
        }
        NodoUnidad encontrado = buscar(nodo.getHijos().get(indice), codigo);
        return encontrado != null ? encontrado : buscarHijoPorCodigo(nodo, codigo, indice + 1);
    }

    public int altura() {
        return altura(raiz);
    }

    private int altura(NodoUnidad nodo) {
        if (nodo == null || nodo.getHijos().isEmpty()) {
            return 0;
        }
        return 1 + alturaHijos(nodo, 0, 0);
    }

    private int alturaHijos(NodoUnidad nodo, int indice, int mayor) {
        if (indice >= nodo.getHijos().size()) {
            return mayor;
        }
        int alturaHijo = altura(nodo.getHijos().get(indice));
        return alturaHijos(nodo, indice + 1, Math.max(mayor, alturaHijo));
    }

    public int contarHojas() {
        return contarHojas(raiz);
    }

    private int contarHojas(NodoUnidad nodo) {
        if (nodo == null) {
            return 0;
        }
        if (nodo.getHijos().isEmpty()) {
            return 1;
        }
        return contarHojasHijos(nodo, 0);
    }

    private int contarHojasHijos(NodoUnidad nodo, int indice) {
        if (indice >= nodo.getHijos().size()) {
            return 0;
        }
        return contarHojas(nodo.getHijos().get(indice))
                + contarHojasHijos(nodo, indice + 1);
    }

    public int nivel(String codigo) {
        if (codigo == null) {
            return -1;
        }
        return nivel(raiz, codigo.trim(), 0);
    }

    private int nivel(NodoUnidad nodo, String codigo, int profundidad) {
        if (nodo == null) {
            return -1;
        }
        if (nodo.getDato().getCodigo().equalsIgnoreCase(codigo)) {
            return profundidad;
        }
        return nivelHijos(nodo, codigo, profundidad + 1, 0);
    }

    private int nivelHijos(NodoUnidad nodo, String codigo, int profundidad, int indice) {
        if (indice >= nodo.getHijos().size()) {
            return -1;
        }
        int encontrado = nivel(nodo.getHijos().get(indice), codigo, profundidad);
        return encontrado >= 0 ? encontrado : nivelHijos(nodo, codigo, profundidad, indice + 1);
    }

    public String mostrarJerarquia() {
        StringBuilder resultado = new StringBuilder();
        mostrarJerarquia(raiz, 0, resultado);
        return resultado.toString();
    }

    private void mostrarJerarquia(NodoUnidad nodo, int nivel, StringBuilder resultado) {
        if (nodo == null) {
            return;
        }
        agregarSangria(resultado, nivel);
        resultado.append(nodo.getDato().getCodigo()).append(' ')
                .append(nodo.getDato().getNombre()).append(System.lineSeparator());
        mostrarJerarquiaHijos(nodo, nivel + 1, 0, resultado);
    }

    private void mostrarJerarquiaHijos(NodoUnidad nodo, int nivel, int indice,
            StringBuilder resultado) {
        if (indice >= nodo.getHijos().size()) {
            return;
        }
        mostrarJerarquia(nodo.getHijos().get(indice), nivel, resultado);
        mostrarJerarquiaHijos(nodo, nivel, indice + 1, resultado);
    }

    private void agregarSangria(StringBuilder resultado, int nivel) {
        if (nivel == 0) {
            return;
        }
        resultado.append("  ");
        agregarSangria(resultado, nivel - 1);
    }

    public int contarLibrosTotal() {
        return contarLibrosTotal(raiz);
    }

    private int contarLibrosTotal(NodoUnidad nodo) {
        if (nodo == null) {
            return 0;
        }
        int libros = nodo.getCatalogo() == null ? 0 : nodo.getCatalogo().contar();
        return libros + contarLibrosHijos(nodo, 0);
    }

    private int contarLibrosHijos(NodoUnidad nodo, int indice) {
        if (indice >= nodo.getHijos().size()) {
            return 0;
        }
        return contarLibrosTotal(nodo.getHijos().get(indice))
                + contarLibrosHijos(nodo, indice + 1);
    }
}
