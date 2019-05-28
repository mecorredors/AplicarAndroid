package car.gov.co.carserviciociudadano.bicicar.model;

import android.database.Cursor;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import car.gov.co.carserviciociudadano.Utils.PreferencesApp;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.common.ModelBase;
import car.gov.co.carserviciociudadano.parques.model.Usuario;

public class Beneficiario extends ModelBase {

    public int IDBeneficiario;
    public String TipoNumeroID;
    public String  NumeroID;
    public String IDMunicipioVive ;
    public String IDVeredaVive ;
    public int IDColegio;
    public int IDEstado;
    public int IDPedagogo;
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
    public int IDBicicleta;
    public int Serial;
    public int Rin;
    public String Email;
    public String LinkFoto;
    public String DescPerfil;
    public String Eps;
    public String PersonaEmergecia;
    public String TelefonoEmergencia;
    public String Direccion;
    public double Latitude;
    public double Longitude;
    public double Norte;
    public double Este;
    public double Estado;
    // otras propiedades
    public boolean Selected;
    public boolean Enabled = true;


    public static final String BICICAR_USUARIO = "bicicar_usuario";


    public static final String TABLE_NAME = "Beneficiarios";

    public static final String ID_BENEFICIARIO = "IDBeneficiario";
    public static final String TIPO_NUMERO_ID ="TipoNumeroID";
    public static final String NUMERO_ID = "NumeroID";
    public static final String ID_MUNICIPIO_VIVE = "IDMunicipioVive";
    public static final String ID_VEREDA = "IDVereda";
    public static final String ID_COLEGIO = "IDColegio";
    public static final String ID_ESTADO = "IDEstado";
    public static final String ID_PEDAGOGO = "IDPedagogo";
    public static final String APELLIDOS = "Apellidos";
    public static final String NOMBRES = "Nombres";
    public static final String FECHA_NACIMIENTO = "FechaNacimiento";
    public static final String GENERO = "Genero";
    public static final String CURSO = "Curso";
    public static final String NOMBRE_ACUDIENTE = "NombreAcudiente";
    public static final String TELEFONO_CONTACTO = "TelefonoContacto";
    public static final String ESTATURA = "Estatura";
    public static final String PESO = "Peso";
    public static final String R_H = "RH";
    public static final String ID_FOTO = "IDFoto";
    public static final String DISTANCIA_KM = "DistanciaKM";
    public static final String ID_PERFIL = "IDPerfil";
    public static final String CLAVE_APP = "ClaveApp";
    public static final String EMAIL = "Email";
    public static final String FECHA_ESTADO ="FechaEstado";
    public static final String FECHA_CREACION = "FechaCreacion";
    public static final String FECHA_MODIFICACION = "FechaModificacion";
    public static final String USUARIO_MODIFICACION = "UsuarioModificacion";
    public static final String DIRECCION = "Direccion";
    public static final String PERSONA_EMERGENCIA = "PersonaEmergencia";
    public static final String TELEFONO_EMERGENCIA = "TelefonoEmergencia";
    public static final String EPS = "EPS";
    public static final String ID_BICICLETA = "IDBicicleta";
    public static final String LINK_FOTO = "LinkFoto";
    public static final String DESC_PERFIL = "DescPerfil";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String NORTE = "Norte";
    public static final String ESTE = "Este";
    public static final String ESTADO = "Estado";

    public  Beneficiario(){
    }

    public Beneficiario(Cursor c)
    {
        if(c.getColumnIndex(ID_BENEFICIARIO)>=0) this.IDBeneficiario = c.getInt(c.getColumnIndex(ID_BENEFICIARIO));
        if(c.getColumnIndex(TIPO_NUMERO_ID)>=0) this.TipoNumeroID = c.getString(c.getColumnIndex(TIPO_NUMERO_ID));
        if(c.getColumnIndex(NUMERO_ID)>=0) this.NumeroID = c.getString((c.getColumnIndex(NUMERO_ID)));
        if(c.getColumnIndex(ID_MUNICIPIO_VIVE)>=0) this.IDMunicipioVive = c.getString(c.getColumnIndex(DISTANCIA_KM));
        if(c.getColumnIndex(ID_VEREDA)>=0) this.IDVeredaVive = c.getString(c.getColumnIndex(ID_VEREDA));
        if(c.getColumnIndex(ID_COLEGIO)>=0) this.IDColegio = c.getInt(c.getColumnIndex(ID_COLEGIO));
        if(c.getColumnIndex(ID_ESTADO)>=0) this.IDEstado = c.getInt(c.getColumnIndex(ID_ESTADO));
        if(c.getColumnIndex(ID_PEDAGOGO)>=0) this.IDPedagogo = c.getInt(c.getColumnIndex(ID_PEDAGOGO));
        if(c.getColumnIndex(APELLIDOS)>=0) this.Apellidos = c.getString(c.getColumnIndex(APELLIDOS));
        if(c.getColumnIndex(NOMBRES)>=0) this.Nombres = c.getString(c.getColumnIndex(NOMBRES));
        if(c.getColumnIndex(FECHA_NACIMIENTO)>=0) FechaNacimiento = Utils.convertToDateSQLLite(c.getString(c.getColumnIndex(FECHA_NACIMIENTO)));
        if(c.getColumnIndex(GENERO)>=0) this.Genero = c.getString(c.getColumnIndex(GENERO));
        if(c.getColumnIndex(CURSO)>=0) this.Curso = c.getString(c.getColumnIndex(CURSO));
        if(c.getColumnIndex(NOMBRE_ACUDIENTE)>=0) this.NombreAcudiente = c.getString(c.getColumnIndex(NOMBRE_ACUDIENTE));
        if(c.getColumnIndex(TELEFONO_CONTACTO)>=0) this.TelefonoContacto = c.getString(c.getColumnIndex(TELEFONO_CONTACTO));
        if(c.getColumnIndex(ESTATURA)>=0) this.Estatura = c.getFloat(c.getColumnIndex(ESTATURA));
        if(c.getColumnIndex(PESO)>=0) this.Peso = c.getFloat(c.getColumnIndex(PESO));
        if(c.getColumnIndex(R_H)>=0) this.RH = c.getString(c.getColumnIndex(R_H));
        if(c.getColumnIndex(ID_FOTO)>=0) this.IDFoto = c.getString(c.getColumnIndex(ID_FOTO));
        if(c.getColumnIndex(DISTANCIA_KM)>=0) this.DistanciaKm = c.getFloat(c.getColumnIndex(DISTANCIA_KM));
        if(c.getColumnIndex(ID_PERFIL)>=0) this.IDPerfil = c.getInt(c.getColumnIndex(ID_PERFIL));
        if(c.getColumnIndex(CLAVE_APP)>=0) this.ClaveAPP = c.getString(c.getColumnIndex(CLAVE_APP));
        if(c.getColumnIndex(EMAIL)>=0) this.Email = c.getString(c.getColumnIndex(EMAIL));
        if(c.getColumnIndex(FECHA_ESTADO)>=0) this.FechaEstado = Utils.convertToDateSQLLite(c.getString(c.getColumnIndex(FECHA_ESTADO)));
        if(c.getColumnIndex(FECHA_CREACION)>=0) this.FechaCreacion =  Utils.convertToDateSQLLite(c.getString(c.getColumnIndex(FECHA_CREACION)));
        if(c.getColumnIndex(USUARIO_MODIFICACION)>=0) this.UsuarioModificacion = c.getString(c.getColumnIndex(USUARIO_MODIFICACION));
        if(c.getColumnIndex(DIRECCION)>=0) this.Direccion = c.getString(c.getColumnIndex(DIRECCION));
        if(c.getColumnIndex(PERSONA_EMERGENCIA)>=0) this.PersonaEmergecia = c.getString(c.getColumnIndex(PERSONA_EMERGENCIA));
        if(c.getColumnIndex(TELEFONO_EMERGENCIA)>=0) this.TelefonoEmergencia = c.getString(c.getColumnIndex(TELEFONO_EMERGENCIA));
        if(c.getColumnIndex(EPS)>=0) this.Eps = c.getString(c.getColumnIndex(EPS));
        if(c.getColumnIndex(ID_BICICLETA)>=0) this.IDBicicleta = c.getInt(c.getColumnIndex(ID_BICICLETA));
        if(c.getColumnIndex(LINK_FOTO)>=0) this.LinkFoto = c.getString(c.getColumnIndex(LINK_FOTO));
        if(c.getColumnIndex(DESC_PERFIL)>=0) this.DescPerfil = c.getString(c.getColumnIndex(DESC_PERFIL));
        if (c.getColumnIndex(LATITUDE) >= 0) this.Latitude = c.getDouble(c.getColumnIndex(LATITUDE));
        if (c.getColumnIndex(LONGITUDE) >= 0) this.Longitude = c.getDouble(c.getColumnIndex(LONGITUDE));
        if (c.getColumnIndex(NORTE) >= 0) this.Norte = c.getDouble(c.getColumnIndex(NORTE));
        if (c.getColumnIndex(ESTE) >= 0) this.Este = c.getDouble(c.getColumnIndex(ESTE));
        if (c.getColumnIndex(ESTADO) >= 0) this.Estado = c.getInt(c.getColumnIndex(ESTADO));
    }

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

    public  void guardar(){
        PreferencesApp preferencesApp = new PreferencesApp(PreferencesApp.WRITE, PreferencesApp.BICIAR_NAME);
        preferencesApp.putString(BICICAR_USUARIO, toString());
        preferencesApp.commit();
    }


}
