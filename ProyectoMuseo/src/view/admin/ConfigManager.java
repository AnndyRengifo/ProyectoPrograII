package view.admin;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Clase para gestionar la configuración de la aplicación, cargando propiedades desde un archivo
 * de configuración `config.properties`.
 * 
 * Esta clase ofrece métodos para obtener el usuario, la contraseña y el ID del administrador,
 * tanto de forma estática (sin necesidad de instanciar la clase) como a través de una instancia.
 */
public class ConfigManager {

    private Properties properties = new Properties();
    private static final String CONFIG_FILE = "resources/config.properties";

    /**
     * Constructor que carga las propiedades del archivo `config.properties` en la instancia de la clase.
     * Si ocurre un error al cargar el archivo, se imprime la traza de la excepción.
     */
    public ConfigManager(){
        try(FileInputStream input = new FileInputStream(CONFIG_FILE)){
            properties.load(input);
        }catch(IOException e){}
    }

    /**
     * Método estático para obtener el nombre de usuario del administrador desde el archivo de configuración.
     * 
     * @return El nombre de usuario del administrador, o un valor por defecto si ocurre un error.
     */
    public static String getAdminUser() {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            prop.load(input);
            return prop.getProperty("usuario", "defaultUsername");  // Devuelve el valor de la propiedad "usuario"
        } catch (IOException ex) {
            return null; 
        }
    }

    /**
     * Método estático para obtener la contraseña del administrador desde el archivo de configuración.
     * 
     * @return La contraseña del administrador, o un valor por defecto si ocurre un error.
     */
    public static String getAdminPassword() {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            prop.load(input); 
            return prop.getProperty("password", "defaultPassword");  // Devuelve el valor de la propiedad "password"
        } catch (IOException ex) {
            return null; 
        }
    }

    /**
     * Método estático para obtener el ID del administrador desde el archivo de configuración.
     * 
     * @return El ID del administrador, o un valor por defecto si ocurre un error.
     */
    public static String getAdminID() {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            prop.load(input); 
            return prop.getProperty("id", "defaultID");  // Devuelve el valor de la propiedad "id"
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Método de instancia para obtener el nombre de usuario del archivo de configuración.
     * 
     * @return El nombre de usuario configurado, o null si no se encuentra.
     */
    public String getUsuario(){
        return properties.getProperty("usuario");
    }

    /**
     * Método de instancia para obtener la contraseña del archivo de configuración.
     * 
     * @return La contraseña configurada, o null si no se encuentra.
     */
    public String getPassword(){
        return properties.getProperty("password"); 
    }

    /**
     * Método de instancia para obtener el ID configurado en el archivo de configuración.
     * 
     * @return El ID configurado, o null si no se encuentra.
     */
    public String getId() {
        return properties.getProperty("id");
    }
}
