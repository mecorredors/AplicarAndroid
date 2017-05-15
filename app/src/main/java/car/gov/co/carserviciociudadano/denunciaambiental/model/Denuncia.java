package car.gov.co.carserviciociudadano.denunciaambiental.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olger on 14/05/2017.
 */

public class Denuncia {

    private boolean Anonimo;
    private String Cedula;
    private String Nombre;
    private String Email;
    private String Direccion;
    private int Departamento;
    private int Ciudad;
    private int Vereda;
    private String Telefono;
    private String Comentarios;
    private double Latitude;
    private double Longitude;
    private double Altitud;

    private List<Foto> Fotos ;

    private static Denuncia instance = null;

    public static synchronized Denuncia newInstance(){
        if (instance == null)
            instance =  new Denuncia();

        return instance;
    }


    public boolean isAnonimo() {
        return Anonimo;
    }

    public void setAnonimo(boolean anonimo) {
        Anonimo = anonimo;
    }

    public String getCedula() {
        return Cedula;
    }

    public void setCedula(String cedula) {
        Cedula = cedula;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public int getDepartamento() {
        return Departamento;
    }

    public void setDepartamento(int departamento) {
        Departamento = departamento;
    }

    public int getCiudad() {
        return Ciudad;
    }

    public void setCiudad(int ciudad) {
        Ciudad = ciudad;
    }

    public int getVereda() {
        return Vereda;
    }

    public void setVereda(int vereda) {
        Vereda = vereda;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getComentarios() {
        return Comentarios;
    }

    public void setComentarios(String comentarios) {
        Comentarios = comentarios;
    }

    public List<Foto> getFotos() {
       if (Fotos == null)  return new ArrayList<>();

        return Fotos;
    }

    public void setFotos(List<Foto> fotos) {
        Fotos = fotos;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getAltitud() {
        return Altitud;
    }

    public void setAltitud(double altitud) {
        Altitud = altitud;
    }
}
