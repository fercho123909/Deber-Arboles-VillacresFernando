package app;

import java.util.List;
import java.util.Scanner;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoUnidad;

public final class Main {
    private final Scanner scanner = new Scanner(System.in);
    private ArbolBiblioteca biblioteca = new ArbolBiblioteca();

    private Main() {
    }

    public static void main(String[] args) {
        new Main().ejecutar();
    }

    private void ejecutar() {
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcion = leerEntero("Opción: ");
            try {
                switch (opcion) {
                    case 1:
                        biblioteca = DatosPrueba.crearBibliotecaInicial();
                        System.out.println("Estructura inicial cargada.");
                        break;
                    case 2:
                        System.out.print(biblioteca.mostrarJerarquia());
                        break;
                    case 3:
                        agregarUnidad();
                        break;
                    case 4:
                        buscarUnidad();
                        break;
                    case 5:
                        mostrarEstadisticas();
                        break;
                    case 6:
                        operarLibro();
                        break;
                    case 7:
                        mostrarRecorridos();
                        break;
                    case 8:
                        ejecutarRetos();
                        break;
                    case 9:
                        continuar = false;
                        System.out.println("Hasta luego.");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (IllegalArgumentException error) {
                System.out.println("Datos inválidos: " + error.getMessage());
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n=== CATÁLOGO DE LA BIBLIOTECA FISEI ===");
        System.out.println("1. Cargar estructura inicial");
        System.out.println("2. Mostrar jerarquía");
        System.out.println("3. Agregar unidad");
        System.out.println("4. Buscar unidad");
        System.out.println("5. Consultar altura, hojas, nivel y total de libros");
        System.out.println("6. Insertar, buscar, modificar o eliminar libro");
        System.out.println("7. Mostrar recorridos con traza");
        System.out.println("8. Ejecutar retos del ABB");
        System.out.println("9. Salir");
    }

    private void agregarUnidad() {
        String padre = leerTexto("Código del padre: ");
        String codigo = leerTexto("Código de la nueva unidad: ");
        String nombre = leerTexto("Nombre: ");
        TipoUnidad tipo = leerTipoUnidad();
        boolean agregada = biblioteca.agregar(padre,
                new UnidadBiblioteca(codigo, nombre, tipo));
        System.out.println(agregada ? "Unidad agregada." : "No se pudo agregar la unidad.");
    }

    private TipoUnidad leerTipoUnidad() {
        System.out.println("Tipo: 1. BIBLIOTECA  2. AREA  3. ESTANTE");
        int opcion = leerEntero("Tipo de unidad: ");
        switch (opcion) {
            case 1:
                return TipoUnidad.BIBLIOTECA;
            case 2:
                return TipoUnidad.AREA;
            case 3:
                return TipoUnidad.ESTANTE;
            default:
                throw new IllegalArgumentException("Tipo de unidad no válido.");
        }
    }

    private void buscarUnidad() {
        String codigo = leerTexto("Código de unidad: ");
        NodoUnidad nodo = biblioteca.buscar(codigo);
        System.out.println(nodo == null ? "Unidad no encontrada." : nodo.getDato());
    }

    private void mostrarEstadisticas() {
        String codigo = leerTexto("Código para consultar el nivel (vacío para omitir): ");
        System.out.println("Altura: " + biblioteca.altura());
        System.out.println("Hojas: " + biblioteca.contarHojas());
        if (!codigo.trim().isEmpty()) {
            System.out.println("Nivel de " + codigo + ": " + biblioteca.nivel(codigo));
        }
        System.out.println("Total de libros: " + biblioteca.contarLibrosTotal());
    }

    private void operarLibro() {
        String codigoEstante = leerTexto("Código del estante: ");
        NodoUnidad estante = biblioteca.buscar(codigoEstante);
        if (estante == null || estante.getCatalogo() == null) {
            System.out.println("No existe un estante con ese código.");
            return;
        }
        CatalogoABB catalogo = estante.getCatalogo();
        System.out.println("1. Insertar  2. Buscar  3. Modificar  4. Eliminar  5. Ver cantidad");
        int opcion = leerEntero("Operación: ");
        switch (opcion) {
            case 1:
                Libro nuevo = leerLibro();
                System.out.println(catalogo.insertar(nuevo) ? "Libro insertado."
                        : "Ya existe un libro con ese código.");
                break;
            case 2:
                mostrarLibro(catalogo.buscar(leerEntero("Código: ")));
                break;
            case 3:
                int codigoModificar = leerEntero("Código a modificar: ");
                String titulo = leerTexto("Nuevo título: ");
                String autor = leerTexto("Nuevo autor: ");
                int anio = leerEntero("Nuevo año: ");
                System.out.println(catalogo.modificar(codigoModificar, titulo, autor, anio)
                        ? "Libro modificado." : "Libro no encontrado.");
                break;
            case 4:
                System.out.println(catalogo.eliminar(leerEntero("Código a eliminar: "))
                        ? "Libro eliminado." : "Libro no encontrado.");
                break;
            case 5:
                System.out.println("Libros en el estante: " + catalogo.contar());
                break;
            default:
                System.out.println("Operación no válida.");
        }
    }

    private Libro leerLibro() {
        int codigo = leerEntero("Código positivo: ");
        String titulo = leerTexto("Título: ");
        String autor = leerTexto("Autor: ");
        int anio = leerEntero("Año (1900-2026): ");
        return new Libro(codigo, titulo, autor, anio);
    }

    private void mostrarLibro(Libro libro) {
        System.out.println(libro == null ? "Libro no encontrado." : libro);
    }

    private void mostrarRecorridos() {
        CatalogoABB catalogo = seleccionarCatalogo();
        if (catalogo == null) {
            return;
        }
        for (TipoRecorrido tipo : TipoRecorrido.values()) {
            System.out.println("\n" + catalogo.trazaRecorrido(tipo));
        }
    }

    private void ejecutarRetos() {
        CatalogoABB catalogo = seleccionarCatalogo();
        if (catalogo == null) {
            return;
        }
        int a = leerEntero("Límite inferior del rango: ");
        int b = leerEntero("Límite superior del rango: ");
        System.out.println("Libros en rango: " + catalogo.contarEnRango(a, b));
        int codigo = leerEntero("Código para camino de búsqueda: ");
        System.out.println("Camino: " + catalogo.caminoBusqueda(codigo));
        System.out.println("ABB válido: " + catalogo.esABBValido());
        System.out.println("1. Reconstruir desde preorden (opcional)  0. Volver");
        if (leerEntero("Opción: ") == 1) {
            int cantidad = leerEntero("Cantidad de códigos: ");
            if (cantidad < 0) {
                throw new IllegalArgumentException("La cantidad no puede ser negativa.");
            }
            int[] preorden = leerCodigos(cantidad);
            CatalogoABB reconstruido = CatalogoABB.desdePreorden(preorden);
            System.out.println("Árbol reconstruido: "
                    + codigos(reconstruido.recorrer(TipoRecorrido.PREORDEN)));
        }
    }

    private CatalogoABB seleccionarCatalogo() {
        String codigo = leerTexto("Código del estante: ");
        NodoUnidad estante = biblioteca.buscar(codigo);
        if (estante == null || estante.getCatalogo() == null) {
            System.out.println("No existe un estante con ese código.");
            return null;
        }
        return estante.getCatalogo();
    }

    private int[] leerCodigos(int cantidad) {
        int[] codigos = new int[cantidad];
        leerCodigos(codigos, 0);
        return codigos;
    }

    private void leerCodigos(int[] codigos, int indice) {
        if (indice >= codigos.length) {
            return;
        }
        codigos[indice] = leerEntero("Código " + (indice + 1) + ": ");
        leerCodigos(codigos, indice + 1);
    }

    private String codigos(List<Libro> libros) {
        StringBuilder resultado = new StringBuilder();
        for (Libro libro : libros) {
            if (resultado.length() > 0) {
                resultado.append(' ');
            }
            resultado.append(libro.getCodigo());
        }
        return resultado.toString();
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return Integer.parseInt(texto.trim());
            } catch (NumberFormatException error) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
    }
}
