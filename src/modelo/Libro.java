package modelo;

public class Libro {
    private final int codigo;
    private String titulo;
    private String autor;
    private int anio;

    public Libro(int codigo, String titulo, String autor, int anio) {
        validarCodigo(codigo);
        validarDatos(titulo, autor, anio);
        this.codigo = codigo;
        this.titulo = titulo.trim();
        this.autor = autor.trim();
        this.anio = anio;
    }

    public static void validarCodigo(int codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código debe ser un entero positivo.");
        }
    }

    public static void validarDatos(String titulo, String autor, int anio) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío.");
        }
        if (anio < 1900 || anio > 2026) {
            throw new IllegalArgumentException("El año debe estar entre 1900 y 2026.");
        }
    }

    public int getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        validarDatos(titulo, autor, anio);
        this.titulo = titulo.trim();
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        validarDatos(titulo, autor, anio);
        this.autor = autor.trim();
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        validarDatos(titulo, autor, anio);
        this.anio = anio;
    }

    @Override
    public String toString() {
        return codigo + " - " + titulo + " | " + autor + " | " + anio;
    }
}
