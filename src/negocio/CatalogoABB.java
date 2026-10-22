package negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import enums.TipoRecorrido;
import modelo.Libro;

public class CatalogoABB {
    private NodoLibro raiz;

    public NodoLibro getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoLibro raiz) {
        this.raiz = raiz;
    }

    public boolean insertar(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser null.");
        }
        if (buscar(libro.getCodigo()) != null) {
            return false;
        }
        raiz = insertar(raiz, libro);
        return true;
    }

    private NodoLibro insertar(NodoLibro nodo, Libro libro) {
        if (nodo == null) {
            return new NodoLibro(libro);
        }
        if (libro.getCodigo() < nodo.getDato().getCodigo()) {
            nodo.setIzquierdo(insertar(nodo.getIzquierdo(), libro));
        } else {
            nodo.setDerecho(insertar(nodo.getDerecho(), libro));
        }
        return nodo;
    }

    public Libro buscar(int codigo) {
        return buscar(raiz, codigo);
    }

    private Libro buscar(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        if (codigo == nodo.getDato().getCodigo()) {
            return nodo.getDato();
        }
        return codigo < nodo.getDato().getCodigo()
                ? buscar(nodo.getIzquierdo(), codigo)
                : buscar(nodo.getDerecho(), codigo);
    }

    public int contar() {
        return contar(raiz);
    }

    private int contar(NodoLibro nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contar(nodo.getIzquierdo()) + contar(nodo.getDerecho());
    }

    public List<Libro> recorrer(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de recorrido es obligatorio.");
        }
        List<Libro> libros = new ArrayList<>();
        recorrer(raiz, tipo, libros);
        return libros;
    }

    private void recorrer(NodoLibro nodo, TipoRecorrido tipo, List<Libro> libros) {
        if (nodo == null) {
            return;
        }
        if (tipo == TipoRecorrido.PREORDEN) {
            libros.add(nodo.getDato());
        }
        recorrer(nodo.getIzquierdo(), tipo, libros);
        if (tipo == TipoRecorrido.INORDEN) {
            libros.add(nodo.getDato());
        }
        recorrer(nodo.getDerecho(), tipo, libros);
        if (tipo == TipoRecorrido.POSORDEN) {
            libros.add(nodo.getDato());
        }
    }

    public String trazaRecorrido(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de recorrido es obligatorio.");
        }
        StringBuilder traza = new StringBuilder();
        trazar(raiz, tipo, 0, traza);
        StringJoiner secuencia = new StringJoiner(" ");
        for (Libro libro : recorrer(tipo)) {
            secuencia.add(String.valueOf(libro.getCodigo()));
        }
        if (traza.length() > 0) {
            traza.append(System.lineSeparator());
        }
        traza.append("Secuencia ").append(tipo).append(": ").append(secuencia);
        return traza.toString();
    }

    private void trazar(NodoLibro nodo, TipoRecorrido tipo, int nivel, StringBuilder traza) {
        if (nodo == null) {
            agregarLinea(traza, nivel, "null -> regresa");
            return;
        }
        int codigo = nodo.getDato().getCodigo();
        agregarLinea(traza, nivel, "entra " + codigo);
        if (tipo == TipoRecorrido.PREORDEN) {
            agregarLinea(traza, nivel, "VISITA " + codigo);
        }
        trazar(nodo.getIzquierdo(), tipo, nivel + 1, traza);
        if (tipo == TipoRecorrido.INORDEN) {
            agregarLinea(traza, nivel, "VISITA " + codigo);
        }
        trazar(nodo.getDerecho(), tipo, nivel + 1, traza);
        if (tipo == TipoRecorrido.POSORDEN) {
            agregarLinea(traza, nivel, "VISITA " + codigo);
        }
    }

    private void agregarLinea(StringBuilder traza, int nivel, String texto) {
        if (traza.length() > 0) {
            traza.append(System.lineSeparator());
        }
        agregarSangria(traza, nivel);
        traza.append(texto);
    }

    private void agregarSangria(StringBuilder texto, int nivel) {
        if (nivel == 0) {
            return;
        }
        texto.append("  ");
        agregarSangria(texto, nivel - 1);
    }

    public boolean modificar(int codigo, String titulo, String autor, int anio) {
        Libro existente = buscar(codigo);
        if (existente == null) {
            return false;
        }
        Libro actualizado = new Libro(codigo, titulo, autor, anio);
        NodoLibro nodo = buscarNodo(raiz, codigo);
        nodo.setDato(actualizado);
        return true;
    }

    private NodoLibro buscarNodo(NodoLibro nodo, int codigo) {
        if (nodo == null || nodo.getDato().getCodigo() == codigo) {
            return nodo;
        }
        return codigo < nodo.getDato().getCodigo()
                ? buscarNodo(nodo.getIzquierdo(), codigo)
                : buscarNodo(nodo.getDerecho(), codigo);
    }

    public boolean eliminar(int codigo) {
        boolean[] eliminado = {false};
        raiz = eliminar(raiz, codigo, eliminado);
        return eliminado[0];
    }

    private NodoLibro eliminar(NodoLibro nodo, int codigo, boolean[] eliminado) {
        if (nodo == null) {
            return null;
        }
        if (codigo < nodo.getDato().getCodigo()) {
            nodo.setIzquierdo(eliminar(nodo.getIzquierdo(), codigo, eliminado));
            return nodo;
        }
        if (codigo > nodo.getDato().getCodigo()) {
            nodo.setDerecho(eliminar(nodo.getDerecho(), codigo, eliminado));
            return nodo;
        }
        eliminado[0] = true;
        if (nodo.getIzquierdo() == null) {
            return nodo.getDerecho();
        }
        if (nodo.getDerecho() == null) {
            return nodo.getIzquierdo();
        }
        NodoLibro sucesor = minimo(nodo.getDerecho());
        nodo.setDato(sucesor.getDato());
        nodo.setDerecho(eliminarMinimo(nodo.getDerecho()));
        return nodo;
    }

    private NodoLibro minimo(NodoLibro nodo) {
        if (nodo.getIzquierdo() == null) {
            return nodo;
        }
        return minimo(nodo.getIzquierdo());
    }

    private NodoLibro eliminarMinimo(NodoLibro nodo) {
        if (nodo.getIzquierdo() == null) {
            return nodo.getDerecho();
        }
        nodo.setIzquierdo(eliminarMinimo(nodo.getIzquierdo()));
        return nodo;
    }

    public int contarEnRango(int a, int b) {
        if (a > b) {
            return 0;
        }
        return contarEnRango(raiz, a, b);
    }

    private int contarEnRango(NodoLibro nodo, int a, int b) {
        if (nodo == null) {
            return 0;
        }
        int codigo = nodo.getDato().getCodigo();
        if (codigo < a) {
            return contarEnRango(nodo.getDerecho(), a, b);
        }
        if (codigo > b) {
            return contarEnRango(nodo.getIzquierdo(), a, b);
        }
        return 1 + contarEnRango(nodo.getIzquierdo(), a, b)
                + contarEnRango(nodo.getDerecho(), a, b);
    }

    public List<Integer> caminoBusqueda(int codigo) {
        List<Integer> camino = new ArrayList<>();
        caminoBusqueda(raiz, codigo, camino);
        return camino;
    }

    private boolean caminoBusqueda(NodoLibro nodo, int codigo, List<Integer> camino) {
        if (nodo == null) {
            return false;
        }
        camino.add(nodo.getDato().getCodigo());
        if (codigo == nodo.getDato().getCodigo()) {
            return true;
        }
        return codigo < nodo.getDato().getCodigo()
                ? caminoBusqueda(nodo.getIzquierdo(), codigo, camino)
                : caminoBusqueda(nodo.getDerecho(), codigo, camino);
    }

    public boolean esABBValido() {
        return esABBValido(raiz, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean esABBValido(NodoLibro nodo, long minimo, long maximo) {
        if (nodo == null) {
            return true;
        }
        int codigo = nodo.getDato().getCodigo();
        return codigo > minimo && codigo < maximo
                && esABBValido(nodo.getIzquierdo(), minimo, codigo)
                && esABBValido(nodo.getDerecho(), codigo, maximo);
    }

    public static CatalogoABB desdePreorden(int[] preorden) {
        if (preorden == null) {
            throw new IllegalArgumentException("El preorden no puede ser null.");
        }
        CatalogoABB catalogo = new CatalogoABB();
        int[] indice = {0};
        catalogo.raiz = construirDesdePreorden(preorden, indice, Long.MIN_VALUE, Long.MAX_VALUE);
        if (indice[0] != preorden.length) {
            throw new IllegalArgumentException("La secuencia no es un preorden válido de un ABB.");
        }
        return catalogo;
    }

    private static NodoLibro construirDesdePreorden(int[] valores, int[] indice,
            long minimo, long maximo) {
        if (indice[0] >= valores.length) {
            return null;
        }
        int codigo = valores[indice[0]];
        if (codigo <= minimo || codigo >= maximo) {
            return null;
        }
        Libro.validarCodigo(codigo);
        indice[0]++;
        NodoLibro nodo = new NodoLibro(new Libro(codigo, "Libro " + codigo,
                "Autor desconocido", 2026));
        nodo.setIzquierdo(construirDesdePreorden(valores, indice, minimo, codigo));
        nodo.setDerecho(construirDesdePreorden(valores, indice, codigo, maximo));
        return nodo;
    }
}
