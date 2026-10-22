package app;

import java.util.Arrays;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoLibro;

public final class PruebasBiblioteca {
    private static int total;
    private static int exitosas;

    private PruebasBiblioteca() {
    }

    public static void main(String[] args) {
        total = 0;
        exitosas = 0;
        ejecutar("biblioteca con solo la raíz", PruebasBiblioteca::probarBibliotecaVacia);
        ejecutar("estructura inicial", PruebasBiblioteca::probarEstructuraInicial);
        ejecutar("niveles y búsqueda sin distinguir mayúsculas",
                PruebasBiblioteca::probarNiveles);
        ejecutar("reglas de inserción y códigos únicos", PruebasBiblioteca::probarInsercionUnidades);
        ejecutar("recorridos del estante SW-E1", PruebasBiblioteca::probarRecorridos);
        ejecutar("duplicado, camino y rango", PruebasBiblioteca::probarRetos);
        ejecutar("eliminación de hoja, un hijo y dos hijos",
                PruebasBiblioteca::probarEliminaciones);
        ejecutar("modificación conserva código y valida cambios",
                PruebasBiblioteca::probarModificacion);
        ejecutar("operaciones sobre libro inexistente", PruebasBiblioteca::probarInexistente);
        ejecutar("validaciones de datos de libro", PruebasBiblioteca::probarValidaciones);
        ejecutar("ABB inválido por nieto mal ubicado", PruebasBiblioteca::probarABBInvalido);
        ejecutar("reconstrucción directa desde preorden", PruebasBiblioteca::probarDesdePreorden);
        ejecutar("inserción de tres códigos demostrativos",
                PruebasBiblioteca::probarCodigosDemostrativos);
        System.out.println(exitosas + "/" + total + " pruebas OK");
        if (exitosas != total) {
            System.exit(1);
        }
    }

    private static void ejecutar(String nombre, Runnable prueba) {
        total++;
        try {
            prueba.run();
            exitosas++;
            System.out.println("OK  " + nombre);
        } catch (RuntimeException | AssertionError error) {
            System.out.println("FALLO  " + nombre + ": " + error.getMessage());
        }
    }

    private static void verificar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    private static void probarBibliotecaVacia() {
        ArbolBiblioteca biblioteca = new ArbolBiblioteca(
                new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));
        verificar(biblioteca.altura() == 0, "La raíz sola debe tener altura 0.");
        verificar(biblioteca.contarHojas() == 1, "La raíz sola debe ser una hoja.");
    }

    private static void probarEstructuraInicial() {
        ArbolBiblioteca biblioteca = DatosPrueba.crearBibliotecaInicial();
        verificar(biblioteca.altura() == 2, "Altura esperada: 2.");
        verificar(biblioteca.contarHojas() == 4, "Hojas esperadas: 4.");
        verificar(biblioteca.contarLibrosTotal() == 13, "Libros esperados: 13.");
    }

    private static void probarNiveles() {
        ArbolBiblioteca biblioteca = DatosPrueba.crearBibliotecaInicial();
        verificar(biblioteca.nivel("BIB") == 0, "Nivel de BIB.");
        verificar(biblioteca.nivel("sw") == 1, "Nivel de sw.");
        verificar(biblioteca.nivel("SW-E2") == 2, "Nivel de SW-E2.");
        verificar(biblioteca.nivel("XYZ") == -1, "XYZ debe estar ausente.");
    }

    private static void probarInsercionUnidades() {
        ArbolBiblioteca biblioteca = DatosPrueba.crearBibliotecaInicial();
        verificar(!biblioteca.agregar("BIB",
                new UnidadBiblioteca("MAL", "Estante inválido", TipoUnidad.ESTANTE)),
                "Una biblioteca no puede tener estantes directamente.");
        verificar(!biblioteca.agregar("BIB",
                new UnidadBiblioteca("sw", "Código repetido", TipoUnidad.AREA)),
                "Los códigos repetidos no se permiten.");
    }

    private static void probarRecorridos() {
        CatalogoABB catalogo = DatosPrueba.crearCatalogoSW_E1();
        verificar(codigos(catalogo, TipoRecorrido.PREORDEN)
                .equals("60 35 20 45 40 80 70 75 90"), "Preorden incorrecto.");
        verificar(codigos(catalogo, TipoRecorrido.INORDEN)
                .equals("20 35 40 45 60 70 75 80 90"), "Inorden incorrecto.");
        verificar(codigos(catalogo, TipoRecorrido.POSORDEN)
                .equals("20 40 45 35 75 70 90 80 60"), "Posorden incorrecto.");
        verificar(catalogo.trazaRecorrido(TipoRecorrido.PREORDEN)
                .endsWith(System.lineSeparator()
                        + "Secuencia PREORDEN: 60 35 20 45 40 80 70 75 90"),
                "La secuencia debe aparecer en una línea final separada.");
    }

    private static String codigos(CatalogoABB catalogo, TipoRecorrido tipo) {
        StringBuilder resultado = new StringBuilder();
        for (Libro libro : catalogo.recorrer(tipo)) {
            if (resultado.length() > 0) {
                resultado.append(' ');
            }
            resultado.append(libro.getCodigo());
        }
        return resultado.toString();
    }

    private static void probarRetos() {
        CatalogoABB catalogo = DatosPrueba.crearCatalogoSW_E1();
        verificar(!catalogo.insertar(new Libro(60, "Duplicado", "Autor", 2020)),
                "Insertar código repetido debe devolver false.");
        verificar(catalogo.caminoBusqueda(42).equals(Arrays.asList(60, 35, 45, 40)),
                "Camino de búsqueda incorrecto.");
        verificar(catalogo.contarEnRango(40, 75) == 5, "Rango esperado: 5.");
        verificar(catalogo.esABBValido(), "El catálogo inicial debe ser un ABB válido.");
    }

    private static void probarEliminaciones() {
        CatalogoABB catalogo = DatosPrueba.crearCatalogoSW_E1();
        verificar(catalogo.insertar(new Libro(42, "Libro 42", "Autor", 2020)),
                "No se pudo insertar 42.");
        verificar(catalogo.eliminar(20), "No se eliminó la hoja 20.");
        verificar(catalogo.eliminar(70), "No se eliminó el nodo 70 de un hijo.");
        verificar(catalogo.eliminar(60), "No se eliminó la raíz con dos hijos.");
        verificar(codigos(catalogo, TipoRecorrido.PREORDEN)
                .equals("75 35 45 40 42 80 90"), "Preorden final incorrecto.");
    }

    private static void probarInexistente() {
        CatalogoABB catalogo = DatosPrueba.crearCatalogoSW_E1();
        verificar(catalogo.buscar(999) == null, "La búsqueda ausente debe devolver null.");
        verificar(!catalogo.modificar(999, "No existe", "Autor", 2020),
                "Modificar un código ausente debe devolver false.");
        verificar(!catalogo.eliminar(999), "Eliminar un código ausente debe devolver false.");
    }

    private static void probarModificacion() {
        CatalogoABB catalogo = DatosPrueba.crearCatalogoSW_E1();
        verificar(catalogo.modificar(35, "Estructuras", "Ada Lovelace", 2020),
                "El libro existente debe modificarse.");
        Libro actualizado = catalogo.buscar(35);
        verificar(actualizado.getCodigo() == 35
                && actualizado.getTitulo().equals("Estructuras")
                && actualizado.getAutor().equals("Ada Lovelace")
                && actualizado.getAnio() == 2020, "Los datos modificados no coinciden.");
    }

    private static void probarValidaciones() {
        boolean lanzoExcepcion = false;
        try {
            new Libro(0, "", "Autor", 1899);
        } catch (IllegalArgumentException esperado) {
            lanzoExcepcion = true;
        }
        verificar(lanzoExcepcion, "Un libro inválido debe lanzar IllegalArgumentException.");
    }

    private static void probarABBInvalido() {
        CatalogoABB catalogo = new CatalogoABB();
        NodoLibro raiz = new NodoLibro(new Libro(50, "50", "Autor", 2020));
        raiz.setIzquierdo(new NodoLibro(new Libro(30, "30", "Autor", 2020)));
        raiz.setDerecho(new NodoLibro(new Libro(70, "70", "Autor", 2020)));
        raiz.getIzquierdo().setDerecho(new NodoLibro(new Libro(60, "60", "Autor", 2020)));
        catalogo.setRaiz(raiz);
        verificar(!catalogo.esABBValido(), "El nieto 60 viola el límite del nodo 50.");
    }

    private static void probarDesdePreorden() {
        CatalogoABB reconstruido = CatalogoABB.desdePreorden(
                new int[] {60, 35, 20, 45, 40, 80, 70, 75, 90});
        verificar(codigos(reconstruido, TipoRecorrido.PREORDEN)
                .equals("60 35 20 45 40 80 70 75 90"), "Reconstrucción incorrecta.");
        verificar(reconstruido.esABBValido(), "El árbol reconstruido debe ser válido.");
    }

    private static void probarCodigosDemostrativos() {
        CatalogoABB catalogo = DatosPrueba.crearCatalogoSW_E1ConCodigos(11, 22, 33);
        verificar(catalogo.contar() == 12, "Se esperaban 12 libros.");
        verificar(codigos(catalogo, TipoRecorrido.PREORDEN)
                .equals("60 35 20 11 22 33 45 40 80 70 75 90"),
                "Preorden demostrativo incorrecto.");
        verificar(catalogo.esABBValido(), "El ABB demostrativo debe ser válido.");
    }
}
