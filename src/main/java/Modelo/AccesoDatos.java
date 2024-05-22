/*
 * Desarrollo de Interfaces  - DI05 Tarea
 */
package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Alert;

/**
 * **************************************************************
 * Hace referencia a la clase AccesoDatos.
 *
 * Esta tiene métodos estáticos que realizan distintas acciones ordenadas por la
 * clase JFrame de Ventana. Los métodos estáticos son: getConnection,
 * buscarClientes, buscarEventos, nuevoCliente, nuevoEvento y comprobarCliente.
 *
 * @author Juan José Vidal Hernández
 * @version 19-05-2024
 */
public class AccesoDatos {

    //Atributos para la conexión a la BBDD
    private static String url = "jdbc:mysql://localhost:3306/informes?serverTimezone=UTC";
    private static String user = "root";
    private static String password = "admin";

    /**
     * Obtiene una conexión a la base de datos.
     *
     * Este método estático devuelve una conexión a la base de datos utilizando
     * la URL, nombre de usuario y contraseña proporcionados. Utiliza la clase
     * DriverManager para cargar el controlador JDBC y establecer la conexión.
     * Si no puede establecer la conexión, muestra una alerta con el mensaje de
     * error y retorna null.
     *
     * @return Una conexión a la base de datos, o null si no se puede establecer
     * la conexión.
     */
    public static Connection getConnection() {

        Connection connection = null;

        try {
            //Usamos clase DriverManager para emplear el controlador JDBC.
            connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("ALERTA");
            alert.setContentText(ex.toString());
            alert.show();
        }

        return connection;
    }

    /**
     * Busca y devuelve una lista de alumnos desde la base de datos.
     *
     * Este método estático realiza una consulta a la tabla "alumnos" de la base
     * de datos y devuelve una lista de objetos Alumno, donde cada objeto
     * representa un registro de la tabla. La consulta selecciona los campos
     * id_alumno, nombre, apellidos, nombre_usuario y correo y los ordena por
     * id_alumno en orden ascendente. Si la consulta tiene éxito, crea un objeto
     * Alumno para cada fila del resultado y lo agrega a la lista de alumnos. Si
     * ocurre un error al ejecutar la consulta, muestra una alerta con el
     * mensaje de error y retorna una lista vacía.
     *
     * @return Una lista de objetos Alumno obtenidos desde la base de datos, o
     * una lista vacía si ocurre un error.
     */
    public static ArrayList<Alumno> buscarAlumnos() {

        ArrayList<Alumno> alumnos = new ArrayList<Alumno>();
        Connection con = getConnection();

        try {

            //Seleccionamos las siguientes columnas de la tabla "clientes".
            PreparedStatement pstmt = con.prepareStatement("SELECT id_alumno, nombre, "
                    + "apellidos, nombre_usuario, correo from alumnos "
                    + "order by id_alumno asc");
            //Obtenemos resultado ejectutando la consulta de la declaración.
            ResultSet rs = pstmt.executeQuery();

            //Mientras el resultado contega más filas...
            while (rs.next()) {
                //Se instancia objeto de tipo Cliente.
                Alumno alumno = new Alumno();
                //Asigna codigo_cliente a alumno con la columna "id_alumno".
                alumno.setIdAlumno(rs.getInt("id_alumno"));
                //Asigna codigo_cliente a alumno con la columna "nombre".
                alumno.setNombre(rs.getString("nombre"));
                //Asigna Origen a alumno con la columna "apellidos".
                alumno.setApellidos(rs.getString("apellidos"));
                //Asigna Destino a alumno con la columna "nombre_usuario".
                alumno.setNombreUsuario(rs.getString("nombre_usuario"));
                //Asigna Destino a alumno con la columna "correo".
                alumno.setCorreo(rs.getString("correo"));

                //Añade objeto alumno a la lista de clientes.
                alumnos.add(alumno);
            }

            //Cerramos la conexión
            con.close();

        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ALERTA");
            alert.setContentText(ex.toString());
            alert.show();
        }

        return alumnos;
    }

    /**
     * Busca y devuelve una lista de módulos desde la base de datos.
     *
     * Este método estático realiza una consulta a la tabla "modulos" de la base
     * de datos y devuelve una lista de objetos Modulo, donde cada objeto
     * representa un registro de la tabla. La consulta selecciona los campos
     * id_modulo y nombre_modulo y los ordena por id_modulo en orden ascendente.
     * Si la consulta tiene éxito, crea un objeto Modulo para cada fila del
     * resultado y lo agrega a la lista de módulos. Si ocurre un error al
     * ejecutar la consulta, muestra una alerta con el mensaje de error y
     * retorna una lista vacía.
     *
     * @return Una lista de objetos Modulo obtenidos desde la base de datos, o
     * una lista vacía si ocurre un error.
     */
    public static ArrayList<Modulo> buscarModulos() {

        ArrayList<Modulo> modulos = new ArrayList<Modulo>();
        Connection con = getConnection();

        try {

            //Se Selecciona las siguientes columnas de la tabla "clientes"
            PreparedStatement pstmt = con.prepareStatement("SELECT id_modulo, "
                    + "nombre_modulo from modulos "
                    + "order by id_modulo asc");
            //Se Obtiene el resultado ejectutando la consulta de la declaración
            ResultSet rs = pstmt.executeQuery();

            //Mientras el resultado contega más filas
            while (rs.next()) {
                //Se instancia objeto de tipo Cliente
                Modulo modulo = new Modulo();
                //Se asigna codigo_cliente a alumno con la columna "id_modulo".
                modulo.setIdModulo(rs.getInt("id_modulo"));
                //Se asigna codigo_cliente a alumno con la columna "nombre_modulo".
                modulo.setNombreModulo(rs.getString("nombre_modulo"));

                //Se añade objeto alumno a la lista de clientes
                modulos.add(modulo);
            }

            //Se cierra la conexión
            con.close();

        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("ALERTA");
            alert.setContentText(ex.toString());
            alert.show();
        }

        return modulos;
    }
}
