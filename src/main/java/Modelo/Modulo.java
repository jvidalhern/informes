package Modelo;

/** 
 * Esta clase define los atributos y métodos get/set para los objetos Cliente,
 * que se definen por el nombre, el apellidos y el télefono del mismo.
 *
 * @author Juan José Vidal Hernández
 * @version 19-05-2024
 */
public class Modulo {
    
    private int idModulo;
    private String nombreModulo;

    
    public String getNombreModulo() {
        return nombreModulo;
    }
    
    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }
    
    public int getIdModulo() {
        return idModulo;
    }
    
    public void setIdModulo(int idModulo) {
        this.idModulo = idModulo;
    }

}
