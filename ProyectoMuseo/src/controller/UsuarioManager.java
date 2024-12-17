package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Usuario;
import service.DatabaseConnection;

/**
 * La clase UsuarioManager gestiona las operaciones relacionadas con los usuarios en la base de datos.
 * Permite agregar, buscar, modificar, eliminar y obtener una lista de usuarios.
 */
public class UsuarioManager {

    /**
     * Agrega un nuevo usuario a la base de datos.
     * 
     * Este método valida que los campos necesarios no estén vacíos y verifica que el ID
     * o el nombre de usuario no existan ya en la base de datos. Si todo es válido, el usuario
     * se inserta en la base de datos.
     * 
     * @param idNuevoUsuario El ID del nuevo usuario.
     * @param nombreUsuario El nombre del nuevo usuario.
     * @param rolUsuario El rol del nuevo usuario.
     * @param usernameUsuario El nombre de usuario para el login.
     * @param passwordUsuario La contraseña del usuario.
     * @return Un mensaje que indica el éxito o el error de la operación.
     */
    public String agregarUsuario(String idNuevoUsuario, String nombreUsuario, String rolUsuario, String usernameUsuario, String passwordUsuario) {
        if (idNuevoUsuario.isEmpty() || nombreUsuario.isEmpty() || rolUsuario.isEmpty() || usernameUsuario.isEmpty() || passwordUsuario.isEmpty()) {
            return "Error: Todos los campos deben ser completados.";
        }
    
        Connection conn = DatabaseConnection.connect();
        if (conn == null) {
            return "Error: Conexión a la base de datos no disponible.";
        }
    
        String sqlCheck = "SELECT COUNT(*) FROM Usuarios WHERE id = ? OR username = ?";
        try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
            pstmtCheck.setString(1, idNuevoUsuario);
            pstmtCheck.setString(2, usernameUsuario);
            ResultSet rsCheck = pstmtCheck.executeQuery();
    
            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                return "Error: El ID o el nombre de usuario ya existen.";
            }
    
            String sqlInsert = "INSERT INTO Usuarios (id, nombre, rol, username, password) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                pstmtInsert.setString(1, idNuevoUsuario);
                pstmtInsert.setString(2, nombreUsuario);
                pstmtInsert.setString(3, rolUsuario);
                pstmtInsert.setString(4, usernameUsuario);
                pstmtInsert.setString(5, passwordUsuario);
                pstmtInsert.executeUpdate();
                return "Usuario agregado correctamente.";
            } catch (SQLException e) {
                return "Error al insertar usuario: " + e.getMessage();
            }
        } catch (SQLException e) {
            return "Error al verificar ID o username: " + e.getMessage();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Busca un usuario por su ID o nombre de usuario.
     * 
     * Este método realiza una búsqueda en la base de datos utilizando el ID o el nombre de usuario
     * proporcionado como criterio de búsqueda. Si se encuentra el usuario, retorna un objeto {@link Usuario}.
     * 
     * @param criterio El ID o nombre de usuario a buscar.
     * @return Un objeto {@link Usuario} si se encuentra, o null si no se encuentra.
     */
    public Usuario buscarUsuario(String criterio) {
        Connection conn = DatabaseConnection.connect();
        if (conn == null) {
            System.out.println("Error: Conexión a la base de datos no disponible.");
            return null;
        }

        String sqlBuscar = "SELECT * FROM Usuarios WHERE id = ? OR username = ?";
        try (PreparedStatement pstmtBuscar = conn.prepareStatement(sqlBuscar)) {
            pstmtBuscar.setString(1, criterio);
            pstmtBuscar.setString(2, criterio);
            ResultSet rs = pstmtBuscar.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("rol"),
                        rs.getString("username")
                );
            } else {
                System.out.println("Usuario no encontrado.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
            return null;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Modifica los detalles de un usuario existente en la base de datos.
     * 
     * Este método permite actualizar el nombre, el rol y el nombre de usuario de un usuario existente
     * utilizando su ID. La contraseña no se puede modificar con este método.
     * 
     * @param id El ID del usuario a modificar.
     * @param nombre El nuevo nombre del usuario.
     * @param rol El nuevo rol del usuario.
     * @param username El nuevo nombre de usuario.
     * @return true si la modificación fue exitosa, false si hubo algún error.
     */
    public boolean modificarUsuario(String id, String nombre, String rol, String username) {
        Connection conn = DatabaseConnection.connect();
        if (conn == null) {
            System.out.println("Error: Conexión a la base de datos no disponible.");
            return false;
        }
    
        String sqlModificar = "UPDATE Usuarios SET nombre = ?, rol = ?, username = ? WHERE id = ?";
        try (PreparedStatement pstmtModificar = conn.prepareStatement(sqlModificar)) {
            pstmtModificar.setString(1, nombre);
            pstmtModificar.setString(2, rol);
            pstmtModificar.setString(3, username);
            pstmtModificar.setString(4, id);
            int filasActualizadas = pstmtModificar.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar usuario: " + e.getMessage());
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Elimina un usuario de la base de datos.
     * 
     * Este método elimina un usuario de la base de datos utilizando su ID.
     * 
     * @param id El ID del usuario a eliminar.
     * @return true si la eliminación fue exitosa, false si hubo algún error.
     */
    public boolean eliminarUsuario(String id) {
        Connection conn = DatabaseConnection.connect();
        if (conn == null) {
            System.out.println("Error: Conexión a la base de datos no disponible.");
            return false;
        }

        String sqlEliminar = "DELETE FROM Usuarios WHERE id = ?";
        try (PreparedStatement pstmtEliminar = conn.prepareStatement(sqlEliminar)) {
            pstmtEliminar.setString(1, id);
            int filasEliminadas = pstmtEliminar.executeUpdate();
            return filasEliminadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Obtiene todos los usuarios de la base de datos.
     * 
     * Este método recupera una lista de todos los usuarios registrados en la base de datos.
     * 
     * @return Una lista de objetos {@link Usuario}.
     */
    public List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = DatabaseConnection.connect();
        if (conn == null) {
            System.out.println("Error: Conexión a la base de datos no disponible.");
            return usuarios;
        }

        String sql = "SELECT * FROM Usuarios";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario(
                    rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getString("rol"),
                    rs.getString("username")
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener usuarios: " + e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
        return usuarios;
    }

}
