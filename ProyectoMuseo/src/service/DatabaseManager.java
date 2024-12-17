package service;

import java.io.*;
import java.sql.*;
import java.util.*;
import models.*;

public class DatabaseManager {
    
    private final Connection connection;
    private static DatabaseManager instance;
    
    /**
     * Constructor de la clase DatabaseManager
     * Establece la conexion con la base de datos utilizando la clase DatabaseConnection
     */
    public DatabaseManager() {
        this.connection = DatabaseConnection.connect();
        if (this.connection != null) {
            System.out.println("Conexión establecida con éxito.");
        } else {
            System.out.println("Error al establecer la conexión.");
        }
        
    }

    /**
     * Método estático para obtener una instancia única de DatabaseManager (Singleton).
     * Si la instancia no ha sido creada, la crea de manera segura (synchronized).
     * 
     * @return instancia de DatabaseManager.
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager(); 
        }
        return instance;
    }
    
    /**
     * Carga la configuración del archivo config.properties y devuelve un mapa con los valores.
     * 
     * @return Mapa con los valores de usuario, contraseña e ID.
     */
    public static Map<String, String> cargarConfiguracion() {
        Map<String, String> config = new HashMap<>();
        try (InputStream input = new FileInputStream("resources/config/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            config.put("usuario", prop.getProperty("usuario"));
            config.put("password", prop.getProperty("password"));
            config.put("id", prop.getProperty("id"));
        } catch (IOException ex) {
            System.out.println("Error al leer el archivo config.properties: " + ex.getMessage());
        }
        return config;
    }

    /**
     * Inicializa la base de datos creando las tablas necesarias si no existen.
     * Además, inserta un usuario por defecto en la tabla Usuarios si no hay registros.
     */
    public static void initializeDatabase() {
        try (Connection conn = DatabaseConnection.connect()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
    
                String createUsuariosTable = """
                    CREATE TABLE IF NOT EXISTS Usuarios (
                        id CHAR(13) PRIMARY KEY,
                        nombre TEXT NOT NULL,
                        rol TEXT NOT NULL,
                        username TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL
                    )""";
                stmt.execute(createUsuariosTable);
    
                String createPinturasTable = """
                    CREATE TABLE IF NOT EXISTS Pinturas (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        titulo TEXT NOT NULL,
                        autor TEXT NOT NULL,
                        anio INTEGER NOT NULL,
                        descripcion TEXT,
                        codigo_barras TEXT NOT NULL,
                        ubicacion TEXT,
                        imagen TEXT
                    )""";
                stmt.execute(createPinturasTable);
    
                String createVisitantesTable = """
                    CREATE TABLE IF NOT EXISTS Visitantes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nombre TEXT NOT NULL,
                        apellido TEXT NOT NULL,
                        fecha_visita TEXT NOT NULL
                    )""";
                stmt.execute(createVisitantesTable);
    
                crearUsuarioPorDefecto();
    
                System.out.println("Tablas creadas e inicializadas correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }
    
    /**
     * Crea un usuario por defecto en la base de datos utilizando los valores del archivo config.properties.
     */
    public static void crearUsuarioPorDefecto() {
        Map<String, String> config = cargarConfiguracion();
        
        String usuarioConfig = config.get("usuario");
        String passwordConfig = config.get("password");
        String idConfig = config.get("id");

        if (usuarioConfig == null || usuarioConfig.isEmpty() ||
            passwordConfig == null || passwordConfig.isEmpty() ||
            idConfig == null || idConfig.isEmpty()) {
            
            System.out.println("Error: Usuario, contraseña o ID en config.properties están vacíos o no existen.");
            return;
        }

        String checkQuery = "SELECT COUNT(*) FROM Usuarios";
        String insertQuery = "INSERT INTO Usuarios (id, nombre, rol, username, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmtCheck = conn.prepareStatement(checkQuery);
             PreparedStatement pstmtInsert = conn.prepareStatement(insertQuery)) {

            ResultSet rs = pstmtCheck.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {  
                String nombre = "Dueño Museo";
                String rol = "administrador Principal";

                pstmtInsert.setString(1, idConfig);
                pstmtInsert.setString(2, nombre);
                pstmtInsert.setString(3, rol);
                pstmtInsert.setString(4, usuarioConfig);
                pstmtInsert.setString(5, passwordConfig);

                pstmtInsert.executeUpdate();
                System.out.println("Usuario principal insertado correctamente desde configuración.");
            } else {
                System.out.println("La tabla de usuarios ya contiene registros.");
            }

        } catch (SQLException e) {
            System.out.println("Error al crear usuario por defecto: " + e.getMessage());
        }
    }

    /**
     * Verifica si las credenciales (usuario y contraseña) proporcionadas son válidas en la base de datos.
     * 
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @return true si las credenciales son válidas, false de lo contrario.
     */
    public static boolean verificarCredenciales(String username, String password) {
        String sql = "SELECT * FROM Usuarios WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            return rs.next(); 
        } catch (SQLException e) {
            System.out.println("Error al verificar credenciales: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si las credenciales son válidas utilizando el ID del usuario.
     * 
     * @param id ID del usuario.
     * @return true si las credenciales son válidas, false de lo contrario.
     */
    public static boolean verificarCredencialesPorId(String id) {
        String sql = "SELECT * FROM Usuarios WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            return rs.next(); 
        } catch (SQLException e) {
            System.out.println("Error al verificar credenciales por ID: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza la información de una pintura en la base de datos.
     * 
     * @param codigoBarrasActual Código de barras actual de la pintura a actualizar.
     * @param nuevoCodigoBarras Nuevo código de barras de la pintura.
     * @param nuevoTitulo Nuevo título de la pintura.
     * @param nuevoAutor Nuevo autor de la pintura.
     * @param nuevoAnio Nuevo año de la pintura.
     * @param descripcion Descripción de la pintura.
     * @param ubicacion Ubicación de la pintura.
     * @return true si la pintura fue actualizada correctamente, false en caso contrario.
     */
    public boolean actualizarPintura(String codigoBarrasActual, String nuevoCodigoBarras, String nuevoTitulo, String nuevoAutor, int nuevoAnio, String descripcion, String ubicacion) {
        String query = "UPDATE pinturas SET codigo_barras = ?, titulo = ?, autor = ?, anio = ?, descripcion = ?, ubicacion = ? WHERE codigo_barras = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, nuevoCodigoBarras);
            stmt.setString(2, nuevoTitulo);
            stmt.setString(3, nuevoAutor);
            stmt.setInt(4, nuevoAnio);
            stmt.setString(5, descripcion);
            stmt.setString(6, ubicacion);
            stmt.setString(7, codigoBarrasActual);
    
            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0; 
        } catch (SQLException e) {
            System.out.println("Error al actualizar la pintura: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una pintura de la base de datos utilizando su código de barras.
     * 
     * @param codigoBarras Código de barras de la pintura a eliminar.
     * @return true si la pintura fue eliminada correctamente, false en caso contrario.
     */
    public boolean eliminarPintura(String codigoBarras) {
        String query = "DELETE FROM Pinturas WHERE codigo_barras = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, codigoBarras);
    
            int filasEliminadas = stmt.executeUpdate();
            return filasEliminadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar la pintura: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserta una nueva pintura en la base de datos.
     * 
     * @param titulo Título de la pintura.
     * @param autor Autor de la pintura.
     * @param anio Año de la pintura.
     * @param descripcion Descripción de la pintura.
     * @param codigoBarras Código de barras de la pintura.
     * @param ubicacion Ubicación de la pintura.
     * @param imagenPath Ruta del archivo de la imagen de la pintura.
     */
    public void insertarPintura(String titulo, String autor, int anio, String descripcion, String codigoBarras, String ubicacion, String imagenPath) {
        File imagenFile = new File(imagenPath);
        if (!imagenFile.exists()) {
            System.out.println("La imagen no existe.");
            return; 
        }
    
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Pinturas (titulo, autor, anio, codigo_barras, descripcion, ubicacion, imagen) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
    
            try (FileInputStream fis = new FileInputStream(imagenFile)) {
                ps.setString(1, titulo);
                ps.setString(2, autor);
                ps.setInt(3, anio);
                ps.setString(4, codigoBarras);
                ps.setString(5, descripcion);
                ps.setString(6, ubicacion);
                ps.setBinaryStream(7, fis, (int) imagenFile.length());
    
                ps.executeUpdate();
            } catch (IOException e) {
                System.out.println("Error al leer el archivo de imagen: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar pintura: " + e.getMessage());
        }
    }
    
   /**
     * Inserta un usuario en la base de datos utilizando un ID de 13 caracteres.
     * 
     * @param nombre Nombre del usuario.
     * @param rol Rol del usuario.
     * @param username Nombre de usuario.
     * @param password Contraseña del usuario.
     * @param id ID del usuario.
     */
   public void insertarUsuario(String nombre, String rol, String username, String password, String id) {
        String sql = "INSERT INTO Usuarios (id, nombre, rol, username, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);  
            pstmt.setString(2, nombre);
            pstmt.setString(3, rol);
            pstmt.setString(4, username);
            pstmt.setString(5, password);
            pstmt.executeUpdate();
            System.out.println("Usuario insertado correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
        }
    }

    /**
     * Obtiene la contraseña almacenada en el archivo config.properties.
     * 
     * @return La contraseña obtenida desde el archivo config.properties.
     */
    public String obtenerContraseñaConfig() {
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream("resources/config/config.properties");
            properties.load(fileInputStream);

            return properties.getProperty("password");
        } catch (IOException e) {
        }
        return null;
    }
    
    /**
     * Obtiene la lista de usuarios desde la base de datos (sin la contraseña).
     * 
     * @return Lista de objetos Usuario.
     */
    public List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT id, nombre, rol FROM Usuarios"; 

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            while (rs.next()) {
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                String rol = rs.getString("rol");

                Usuario usuario = new Usuario(id, nombre, rol, null);
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener usuarios: " + e.getMessage());
        }
        return usuarios;
    }
    
    /**
     * Obtiene el usuario logueado desde la base de datos utilizando su nombre de usuario.
     * 
     * @param username Nombre de usuario.
     * @return El objeto Usuario correspondiente al usuario logueado.
     */
    public static Usuario obtenerUsuarioLogueado(String username) {
        String sql = "SELECT id, nombre, rol FROM Usuarios WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                String rol = rs.getString("rol");
                Usuario usuario = new Usuario(id, nombre, rol, username);
                return usuario;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el usuario logueado: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene una pintura desde la base de datos utilizando su código de barras.
     * 
     * @param codigoBarrasParametro Código de barras de la pintura.
     * @return Objeto Pintura correspondiente al código de barras proporcionado.
     */
    public static Pintura obtenerPinturaPorCodigoBarras(String codigoBarrasParametro) {
        String query = "SELECT titulo, autor, anio, descripcion, ubicacion, codigo_barras, imagen FROM Pinturas WHERE codigo_barras = ?";
        Pintura pintura = null;
    
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, codigoBarrasParametro);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                pintura = new Pintura(
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getInt("anio"),
                    rs.getString("descripcion"),
                    rs.getString("ubicacion"),
                    rs.getString("codigo_barras"),
                    rs.getString("imagen")
                );
            }
        } catch (SQLException e) {
        }
        return pintura;
    }
    
    /**
     * Obtiene todas las pinturas desde la base de datos.
     * 
     * @return Lista de objetos Pintura.
     */
    public List<Pintura> obtenerTodasLasPinturas() {
        List<Pintura> pinturas = new ArrayList<>();
        String query = "SELECT titulo, autor, anio, descripcion, ubicacion, codigo_barras, imagen FROM Pinturas";
    
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            while (rs.next()) {
                Pintura pintura = new Pintura(query, query, 0, query, query, query, query);
                pintura.setTitulo(rs.getString("titulo"));
                pintura.setAutor(rs.getString("autor"));
                pintura.setAnio(rs.getInt("anio"));
                pintura.setDescripcion(rs.getString("descripcion"));
                pintura.setUbicacion(rs.getString("ubicacion"));
                pintura.setCodigoBarras(rs.getString("codigo_barras"));
                pintura.setImagen(rs.getString("imagen")); 
                pinturas.add(pintura);
            }
        } catch (SQLException e) {
        }
    
        return pinturas;
    }


}
