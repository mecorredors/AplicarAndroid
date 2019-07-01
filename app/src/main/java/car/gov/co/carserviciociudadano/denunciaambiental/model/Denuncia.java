package car.gov.co.carserviciociudadano.denunciaambiental.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.jakewharton.disklrucache.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.Utils.Utils;

/**
 * Created by Olger on 14/05/2017.
 */

public class Denuncia {

    private String NumeroRadicado;
    private boolean Anonimo;
    private String Cedula;
    private String Nombre;
    private String Email;
    private String Direccion;
    private String Departamento;
    private String Municipio;
    private String Vereda;
    private String MunicipioQueja;
    private String VeredaQueja;
    private String DesUbicacionQueja;
    private String Telefono;
    private String Comentarios;
    private double Latitude;
    private double Longitude;
    private double Norte;
    private double Este;
    private double Altitud;

    private List<Foto> Fotos ;
  //  private List<ArchivoAdjunto> ArchivoAdjunto ;

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

    public String getDepartamento() {
        return Departamento;
    }

    public void setDepartamento(String departamento) {
        Departamento = departamento;
    }

    public String getMunicipio() {
        return Municipio;
    }

    public void setMunicipio(String municipio) {
        Municipio = municipio;
    }

    public String getVereda() {
        return Vereda;
    }

    public void setVereda(String vereda) {
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

    public String getNumeroRadicado() {
        return NumeroRadicado;
    }

    public void setNumeroRadicado(String numeroRadicado) {
        NumeroRadicado = numeroRadicado;
    }

    public String getMunicipioQueja() {
        return MunicipioQueja;
    }

    public void setMunicipioQueja(String municipioQueja) {
        MunicipioQueja = municipioQueja;
    }

    public String getVeredaQueja() {
        return VeredaQueja;
    }

    public void setVeredaQueja(String veredaQueja) {
        VeredaQueja = veredaQueja;
    }

    public String getDesUbicacionQueja() {
        return DesUbicacionQueja;
    }

    public void setDesUbicacionQueja(String desUbicacionQueja) {
        DesUbicacionQueja = desUbicacionQueja;
    }

    public List<Foto> getFotos() {
        if (this.Fotos == null) return  new ArrayList<>();
        return Fotos;
    }

    public void setFotos(List<Foto> fotos) {
        Fotos = fotos;
    }

    public double getNorte() {
        return Norte;
    }

    public void setNorte(double norte) {
        Norte = norte;
    }

    public double getEste() {
        return Este;
    }

    public void setEste(double este) {
        Este = este;
    }

    public String getEsteString(){
        BigDecimal bd = new BigDecimal(this.Este);
        bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }
    public String getNorteString(){
        BigDecimal bd = new BigDecimal(this.Norte);
        bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }

    public String getUsuario(){
        if (this.Cedula != null && !this.Cedula.trim().isEmpty() && !this.Anonimo)
            return this.Cedula.trim();
        else if (Anonimo){
            return  "ANONIMO" + Utils.getFechaActual();
        }else {
            return "SIN_CEDULA" + Utils.getFechaActual();
        }
    }

    public  Denuncia() {
    }

    public  Denuncia(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();
                Denuncia element = gson.fromJson(json, Denuncia.class);

                this.NumeroRadicado = element.getNumeroRadicado();
                this.Anonimo = element.isAnonimo();
                this.Cedula = element.getCedula();
                this.Nombre = element.getNombre();
                this.Email = element.getEmail();
                this.Direccion = element.getDireccion();
                this.Departamento = element.getDepartamento();
                this.Municipio = element.getMunicipio();
                this.Vereda = element.getVereda();
                this.Telefono = element.getTelefono();
                this.Comentarios = element.getComentarios();
                this.Latitude = element.getLatitude();
                this.Longitude = element.getLongitude();
                this.Altitud = element.getAltitud();

            } catch (JsonSyntaxException ex) {
                Log.e("Lugar.json", ex.toString());
            }
        }
    }

    public JSONObject toJSONObject(){

        Gson gson = new Gson();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(gson.toJson(this));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }

}
