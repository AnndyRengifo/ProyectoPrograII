package service;

import java.sql.*;

/**
 * La clase DatabaseConnection proporciona un método para conectar a la base de datos SQLite
 * utilizada en el proyecto. Utiliza el controlador JDBC para establecer la conexión
 * con la base de datos del museo.
 */
public class DatabaseConnection {

    /**
     * Establece una conexión con la base de datos SQLite.
     * 
     * Este método utiliza el controlador JDBC para abrir una conexión a la base de datos
     * situada en el archivo "museo.db", que se encuentra en la carpeta "database".
     * Si la conexión es exitosa, retorna un objeto {@link Connection}, de lo contrario,
     * imprime un mensaje de error y retorna null.
     * 
     * @return Un objeto {@link Connection} si la conexión es exitosa, o null si ocurre un error.
     */
    public static Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:database/museo.db");
        } catch (SQLException e) {
            System.out.println("Error de conexión a la base de datos: " + e.getMessage());
            return null;
        }
    }
}