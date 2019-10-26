package car.gov.co.carserviciociudadano.Utils;

/**
 * Created by Olger on 04/12/2016.
 */

public class Enumerator {


    public  static class ReservaEstado{
        public static final int PRE_RESERVA = 0;
        public static final int RESERVA_APROBADA = 1;

    }

    /**
     * Numero de dias en cache
     */
    public static class CacheNumDias{
        public static final int PARQUES = 1;
        public static final int SERVICIOS_PARQUE = 1;
        public static final int MUNICIPIOS = 1;
        public static final int BANCOS = 1;
        public static final int ARCHIVOS_PARQUE = 1;
    }
    public static class CacheTimeInMilliSeconds{
        public static final long PARQUES =  20 * 24 * 3600 * 1000; // 20 dias;
        public static final int SERVICIOS_PARQUE = 300000;
        public static final int MUNICIPIOS = 300000;
        public static final int BANCOS = 300000;
        public static final long ARCHIVOS_PARQUE = 20 * 24 * 3600 * 1000; // 20 dias;
        public static final int PETCAR = 300000;
    }

    public static class TipoArchivoParque{
        public static final String TAG = "TypoArchivoParque";
        public static final int PRINCIPAL_Y_GALERIA = 0;
        public static final int PRINCIPAL = 1;
        public static final int PDF = 2;
        public static final int GALERIA = 3;
        public static final int MAP_PHOTO = 4;
        public static final int LOGO = 5;
    }

    public static class TipoFoto{
        public static final int BOTON_AGREGAR_MAS = 0;
        public static final int FOTO = 1;

    }

    public static class ContentTypeAnalitic{
        public static final String PRINCIPAL = "Principal";
        public static final String PARQUES = "Parques";
        public static final String DENUNCIA_AMBIENTAL = "Denuncia ambiental";
        public static final String CONSULTA_EXPEDIENTE = "Consulta expediente";
        public static final String CONSULTA_TRAMITE = "Consulta tramite";
        public static final String CONSULTA_PROYECTO_COFIN = "Consulta proyecto cofin";
        public static final String BICICAR = "Bicicar";

    }

    public  static class Estado{
        public static final int PENDIENTE_PUBLICAR = 1;
        public static final int PUBLICADO = 0;
        public static final int TODOS = -1;
        public static final int EDICION = 2;


    }

    public static class BicicarPerfil{
        public static final int BENEFICIARIO = 1;
        public static final int LIDER_GRUPO = 2;
        public static final int PEDAGOGO = 3;
        public static final int BENEFICIARIO_APP = 4;
        public static final int EVENTO = 5;


    }

    public static final int TIMEOUT_PUBLISH_IMAGES = 40000;
    public static final String NAME_DIRECTORY_IMAGES = "CAR-Servicio-Ciudadano";

    public  static class TipoPersona{
        public static final int RECOLECTOR = 1;
        public static final int INSPECTOR = 2;

    }
}
