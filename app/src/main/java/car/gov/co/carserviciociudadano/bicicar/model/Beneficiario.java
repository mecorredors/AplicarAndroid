package car.gov.co.carserviciociudadano.bicicar.model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Beneficiario {

    public int IDBeneficiario;
    public String TipoNumeroID;
    public String  NumeroID;
    public String IDMunicipioVive ;
    public String IDVeredaVive ;
    public int IDColegio;
    public int IDEstado;
    public String Apellidos;
    public String Nombres;
    public Date FechaNacimiento;
    public String Genero;
    public String Curso;
    public String NombreAcudiente;
    public String TelefonoContacto;
    public float Estatura;
    public float Peso;
    public String RH;
    public String IDFoto;
    public float DistanciaKm;
    public int IDPerfil;
    public Date FechaEstado;
    public String ClaveAPP;
    public Date FechaCreacion;
    public String UsuarioCreacion;
    public String UsuarioModificacion;
    public Date FechaModificacion;

    @Override public String toString(){
        Gson gson = new Gson();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(gson.toJson(this));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject.toString();
    }
}
