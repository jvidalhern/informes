package Modelo;

/** 
 * Esta clase define los atributos y métodos get/set para los objetos Cliente,
 * que se definen por el nombre, el apellidos y el télefono del mismo.
 *
 * @author Juan José Vidal Hernández
 * @version 19-05-2024
 */
public class Alumno {

    //Atributos de la clase
    private int idAlumno;
    private String nombre;
    private String apellidos;
    private String nombreUsuario;
    private String correo;
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellidos() {
        return apellidos;
    }
   
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
   
    public int getIdAlumno() {
        return idAlumno;
    }
    
    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
