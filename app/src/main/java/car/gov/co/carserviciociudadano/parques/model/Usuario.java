package car.gov.co.carserviciociudadano.parques.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Olger on 26/11/2016.
 */

public class Usuario {
    private int IdUsuario;
    private String Login;
    private String ClaveUsuario;
    private String NombreCompleto;
    private String EmailUsuario;
    private String TelefonoUsuario;
    private int ActivoUsuario;
    private String LlaveUsuario;
    private int IdPerfil;
    private int NroAccesos;
    private Date FechaUltimoAcceso;
    private String CelularUsuario;
    private int IDMunicipio;
    private String DireccionUsuario;
    private Date FechaHastaActivaCuenta;
    private Date FechaDesdeActivaCuenta;
    private int AplicaRango;
    private String Documento;
    private boolean FuncionarioCar;


    public static final String ID_USUARIO = "IdUsuario";
    public static final String EMAIL_USUARIO = "EmailUsuario";
    public static final String CLAVE_USUARIO = "ClaveUsuario";
    public static final String NOMBRE_COMPLETO = "NombreCompleto";
    public static final String TELEFONO_USUARIO = "TelefonoUsuario";
    public static final String  CELULAR_USUARIO = "CelularUsuario";
    public static final String DOCUMENTO = "Documento";
    public static final String DIRECCION_USUARIO = "DireccionUsuario";
    public static final String ID_MUNICIPIO = "IDMunicipio";

    public  Usuario(){}

    public  Usuario(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                Usuario element = gson.fromJson(json, Usuario.class);

                this.IdUsuario = element.IdUsuario;
                this.Login = element.Login;
                this.ClaveUsuario = element.ClaveUsuario;
                this.NombreCompleto = element.NombreCompleto;
                this.EmailUsuario = element.EmailUsuario;
                this.TelefonoUsuario = element.TelefonoUsuario;
                this.ActivoUsuario = element.ActivoUsuario;
                this.LlaveUsuario = element.LlaveUsuario;
                this.IdPerfil = element.IdPerfil;
                this.NroAccesos = element.NroAccesos;
                this.FechaUltimoAcceso = element.FechaUltimoAcceso;
                this.CelularUsuario = element.CelularUsuario;
                this.IDMunicipio = element.IDMunicipio;
                this.DireccionUsuario = element.DireccionUsuario;
                this.FechaHastaActivaCuenta = element.FechaHastaActivaCuenta;
                this.FechaDesdeActivaCuenta = element.FechaDesdeActivaCuenta;
                this.AplicaRango = element.AplicaRango;
                this.Documento = element.Documento;
                this.FuncionarioCar = element.FuncionarioCar;

            } catch (JsonSyntaxException ex) {
                Log.e("Usuario.json", ex.toString());
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

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getClaveUsuario() {
        return ClaveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        ClaveUsuario = claveUsuario;
    }

    public String getNombreCompleto() {
        return NombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        NombreCompleto = nombreCompleto;
    }

    public String getEmailUsuario() {
        return EmailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        EmailUsuario = emailUsuario;
    }

    public String getTelefonoUsuario() {
        return TelefonoUsuario;
    }

    public void setTelefonoUsuario(String telefonoUsuario) {
        TelefonoUsuario = telefonoUsuario;
    }

    public int getActivoUsuario() {
        return ActivoUsuario;
    }

    public void setActivoUsuario(int activoUsuario) {
        ActivoUsuario = activoUsuario;
    }

    public String getLlaveUsuario() {
        return LlaveUsuario;
    }

    public void setLlaveUsuario(String llaveUsuario) {
        LlaveUsuario = llaveUsuario;
    }

    public int getIdPerfil() {
        return IdPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        IdPerfil = idPerfil;
    }

    public int getNroAccesos() {
        return NroAccesos;
    }

    public void setNroAccesos(int nroAccesos) {
        NroAccesos = nroAccesos;
    }

    public Date getFechaUltimoAcceso() {
        return FechaUltimoAcceso;
    }

    public void setFechaUltimoAcceso(Date fechaUltimoAcceso) {
        FechaUltimoAcceso = fechaUltimoAcceso;
    }

    public String getCelularUsuario() {
        return CelularUsuario;
    }

    public void setCelularUsuario(String celularUsuario) {
        CelularUsuario = celularUsuario;
    }

    public int getIDMunicipio() {
        return IDMunicipio;
    }

    public void setIDMunicipio(int IDMunicipio) {
        this.IDMunicipio = IDMunicipio;
    }

    public String getDireccionUsuario() {
        return DireccionUsuario;
    }

    public void setDireccionUsuario(String direccionUsuario) {
        DireccionUsuario = direccionUsuario;
    }

    public Date getFechaHastaActivaCuenta() {
        return FechaHastaActivaCuenta;
    }

    public void setFechaHastaActivaCuenta(Date fechaHastaActivaCuenta) {
        FechaHastaActivaCuenta = fechaHastaActivaCuenta;
    }

    public Date getFechaDesdeActivaCuenta() {
        return FechaDesdeActivaCuenta;
    }

    public void setFechaDesdeActivaCuenta(Date fechaDesdeActivaCuenta) {
        FechaDesdeActivaCuenta = fechaDesdeActivaCuenta;
    }

    public int getAplicaRango() {
        return AplicaRango;
    }

    public void setAplicaRango(int aplicaRango) {
        AplicaRango = aplicaRango;
    }

    public String getDocumento() {
        return Documento;
    }

    public void setDocumento(String documento) {
        Documento = documento;
    }

    public boolean isFuncionarioCar() {
        return FuncionarioCar;
    }

    public void setFuncionarioCar(boolean funcionarioCar) {
        FuncionarioCar = funcionarioCar;
    }
}
