package models;

public class Usuario {
    private final String id;
    private final String nombre;
    private final String rol;
    private final String username;

    /**
     * Constructor que representa un usuario en el sistema
     * @param id
     * @param nombre
     * @param rol
     * @param username
     */
    public Usuario(String id, String nombre, String rol, String username) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.username = username;
    }

    /**
     * Getters y Setters de el constructor
     * @return
     */
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRol() {
        return rol;
    }

    public String getUsername(){
        return username;
    }

    /**
     * Metodo toString que devuelve una representacion en texto del objeto Usuario
     */
    @Override
    public String toString() {
        return "ID: " + id + ", Nombre: " + nombre + ", Rol: " + rol + ", Username" + username;
    }
}
