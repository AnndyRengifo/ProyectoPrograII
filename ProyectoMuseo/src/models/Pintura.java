package models;

public class Pintura {
    private String titulo;
    private String autor;
    private int anio;
    private String codigoBarras;
    private String descripcion;
    private String ubicacion;
    private String imagen;

    /**
     * Constructor que inicializa todos los atributos de la pintura
     * @param titulo
     * @param autor
     * @param anio
     * @param descripcion
     * @param ubicacion
     * @param codigoBarras
     * @param imagen
     */
    public Pintura(String titulo, String autor, int anio, String descripcion, String ubicacion, String codigoBarras, String imagen) {
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.codigoBarras = codigoBarras;
        this.imagen = imagen;
    }

    /**
     * Getters y Setters del Constructor
     * @return
     */
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
