package app;

import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoUnidad;

public final class DatosPrueba {
    private DatosPrueba() {
    }

    public static ArbolBiblioteca crearBibliotecaInicial() {
        ArbolBiblioteca biblioteca = new ArbolBiblioteca(
                new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));
        biblioteca.agregar("BIB", new UnidadBiblioteca("SW", "Software", TipoUnidad.AREA));
        biblioteca.agregar("BIB", new UnidadBiblioteca("TI",
                "Tecnologías de la Información", TipoUnidad.AREA));
        biblioteca.agregar("BIB", new UnidadBiblioteca("TEL",
                "Telecomunicaciones", TipoUnidad.AREA));
        biblioteca.agregar("SW", new UnidadBiblioteca("SW-E1",
                "Estante 1 de Software", TipoUnidad.ESTANTE));
        biblioteca.agregar("SW", new UnidadBiblioteca("SW-E2",
                "Estante 2 de Software", TipoUnidad.ESTANTE));
        biblioteca.agregar("TI", new UnidadBiblioteca("TI-E1",
                "Estante 1 de TI", TipoUnidad.ESTANTE));

        insertarCodigos(biblioteca.buscar("SW-E1").getCatalogo(),
                new int[] {60, 35, 80, 20, 45, 70, 90, 40, 75});
        insertarCodigos(biblioteca.buscar("SW-E2").getCatalogo(), new int[] {15, 10, 25});
        insertarCodigos(biblioteca.buscar("TI-E1").getCatalogo(), new int[] {50});
        return biblioteca;
    }

    public static CatalogoABB crearCatalogoSW_E1() {
        CatalogoABB catalogo = new CatalogoABB();
        insertarCodigos(catalogo, new int[] {60, 35, 80, 20, 45, 70, 90, 40, 75});
        return catalogo;
    }

    public static void insertarCodigos(CatalogoABB catalogo, int[] codigos) {
        insertarCodigos(catalogo, codigos, 0);
    }

    private static void insertarCodigos(CatalogoABB catalogo, int[] codigos, int indice) {
        if (indice >= codigos.length) {
            return;
        }
        int codigo = codigos[indice];
        catalogo.insertar(new Libro(codigo, "Libro " + codigo, "Autor desconocido", 2026));
        insertarCodigos(catalogo, codigos, indice + 1);
    }

    public static NodoUnidad buscarEstanteSW_E1(ArbolBiblioteca biblioteca) {
        return biblioteca.buscar("SW-E1");
    }
}
