package controller;

import java.io.File;
import service.DatabaseManager;

/**
 * Esta clase es responsable de gestionar las operaciones relacionadas con las pinturas en el museo.
 * Incluye la validación y el registro de nuevas pinturas en la base de datos.
 */
public class PinturaManager {

    private final DatabaseManager dbManager;

    /**
     * Constructor que inicializa un objeto de la clase DatabaseManager.
     */
    public PinturaManager() {
        this.dbManager = new DatabaseManager();
    }

    /**
     * Agrega una nueva pintura a la base de datos.
     * Primero valida los parámetros proporcionados y la existencia de la imagen,
     * luego inserta la pintura en la base de datos utilizando el DatabaseManager.
     *
     * @param titulo        El título de la pintura.
     * @param autor         El autor de la pintura.
     * @param anio          El año en que fue realizada la pintura.
     * @param descripcion   Una breve descripción de la pintura.
     * @param codigoBarras  El código de barras único de la pintura.
     * @param ubicacion     La ubicación de la pintura en el museo.
     * @param rutaImagen    La ruta del archivo de la imagen de la pintura.
     */
    public void agregarPintura(String titulo, String autor, int anio, String descripcion, String codigoBarras, String ubicacion, String rutaImagen) {
        validarParametros(titulo, autor, anio, descripcion, codigoBarras, ubicacion, rutaImagen);
        validarImagen(rutaImagen);

        try {
            dbManager.insertarPintura(titulo, autor, anio, descripcion, codigoBarras, ubicacion, rutaImagen);
        } catch (Exception e) {
            throw new RuntimeException("Error al insertar la pintura en la base de datos: " + e.getMessage(), e);
        }
    }

    /**
     * Valida los parámetros proporcionados al agregar una pintura.
     * Asegura que los campos no sean nulos o vacíos, y que el año sea válido.
     *
     * @param titulo        El título de la pintura.
     * @param autor         El autor de la pintura.
     * @param anio          El año en que fue realizada la pintura.
     * @param descripcion   Una breve descripción de la pintura.
     * @param codigoBarras  El código de barras único de la pintura.
     * @param ubicacion     La ubicación de la pintura en el museo.
     * @param rutaImagen    La ruta del archivo de la imagen de la pintura.
     * @throws IllegalArgumentException Si algún parámetro es inválido o vacío.
     */
    private void validarParametros(String titulo, String autor, int anio, String descripcion, String codigoBarras, String ubicacion, String rutaImagen) {
        if (titulo == null || titulo.isEmpty()) {
            throw new IllegalArgumentException("El título de la pintura no puede estar vacío.");
        }
        if (autor == null || autor.isEmpty()) {
            throw new IllegalArgumentException("El autor de la pintura no puede estar vacío.");
        }
        if (codigoBarras == null || codigoBarras.isEmpty()) {
            throw new IllegalArgumentException("El código de barras no puede estar vacío.");
        }
        if (ubicacion == null || ubicacion.isEmpty()) {
            throw new IllegalArgumentException("La ubicación no puede estar vacía.");
        }
        if (anio <= 0 || anio > 9999) {
            throw new IllegalArgumentException("El año debe ser un valor positivo y válido.");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new IllegalArgumentException("La descripcion no puede estar vacía!.");
        }
        if (rutaImagen == null || rutaImagen.isEmpty()) {
            throw new IllegalArgumentException("La ruta de la imagen no puede estar vacía.");
        }
    }

    /**
     * Valida que el archivo de imagen especificado exista y sea legible.
     * 
     * @param rutaImagen La ruta del archivo de la imagen de la pintura.
     * @throws IllegalArgumentException Si la imagen no existe o no es válida.
     */
    private void validarImagen(String rutaImagen) {
        File imagenFile = new File(rutaImagen);
        if (!imagenFile.exists()) {
            throw new IllegalArgumentException("La imagen seleccionada no existe: " + rutaImagen);
        }
        if (!imagenFile.isFile() || !imagenFile.canRead()) {
            throw new IllegalArgumentException("El archivo de la imagen no es válido o no se puede leer: " + rutaImagen);
        }
    }
}
