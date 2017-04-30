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

    public static class TipoArchivoParque{
        public static final String TAG = "TypoArchivoParque";
        public static final int PRINCIPAL_Y_GALERIA = 0;
        public static final int PRINCIPAL = 1;
        public static final int PDF = 2;
        public static final int GALERIA = 3;
        public static final int MAP_PHOTO = 4;
    }
}
