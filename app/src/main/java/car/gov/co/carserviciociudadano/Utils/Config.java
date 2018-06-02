package car.gov.co.carserviciociudadano.Utils;

/**
 * Created by Olger on 26/11/2016.
 */

public class Config {

    public static String ServerParques = "https://parques.car.gov.co/";    //pruebas car
    public static String ServerSIDCAR = "https://sidcar.car.gov.co/";    //produccion sidcar
    public static String ServerSAE = "https://sae.car.gov.co/";    //produccion sae

    public static String OpenWeatherIcon = "http://openweathermap.org/img/w/";    //icono openweathermap

    //public static String ServerParques = "http://192.168.1.53/PARQUES/";    //pruebas car
    //public static String ServerSIDCAR = "http://192.168.1.53/SIDCARNET/";    //pruebas sidcar
    //public static String ServerSAE = "http://192.168.1.53/SAE/";    //pruebas sae

   // public static String ServerParques = "http://192.168.0.11/parques/";    //LOCAL car
   // public static String ServerSIDCAR = "http://192.168.0.11/SIDCAR/";    //LOCAL sidcar
    //  public static String ServerSAE = "http://192.168.0.11/SAE/";    //LOCAL sidcar
      public static String ServerBICICAR = "http://192.168.0.11/BICICAR/";    //LOCAL sidcar


    public static String API_PARQUES_PARQUES =  ServerParques + "api/parque/parques/";
    public static String API_PARQUES_ARCHIVOS =  ServerParques + "api/parque/archivos/";
    public static String API_PARQUES_MANTENIMIENTOS =  ServerParques + "api/parque/mantenimientos/";
    public static String API_PARQUES_SERVICIOS =  ServerParques + "api/parque/servicios/";
    public static String API_PARQUES_SERVICIOS_EN_RESERVA =  ServerParques + "api/reserva/serviciosenreserva/";
    public static String API_PARQUES_RESERVA_DETALLE =  ServerParques + "api/reserva/detalle/";
    public static String API_PARQUES_RESERVA =  ServerParques + "api/reserva/reserva/";
    public static String API_PARQUES_ABONOS =  ServerParques + "api/reserva/abonos/";
    public static String API_PARQUES_INGRESAR_ABONO =  ServerParques + "api/reserva/ingresarAbono/";
    public static String API_PARQUES_PUBLICAR_IMAGENES =  ServerParques + "api/reserva/publicarimagenes/";
    public static String API_PARQUES_USUARIO_INSERTAR =  ServerParques + "api/usuario/insertar";
    public static String API_PARQUES_USUARIO_ACTUALIZAR =  ServerParques + "api/usuario/actualizar";
    public static String API_PARQUES_USUARIO_RECUPERAR_CONTRASENA =  ServerParques + "api/usuario/recuperarcontrasena";
    public static String API_PARQUES_USUARIO_LOGIN =  ServerParques + "api/usuario/login/";
    public static String API_PARQUES_USUARIO_CAMBIAR_CONTRASENA =  ServerParques + "api/usuario/cambiarcontrasena/";
    public static String API_PARQUES_MUNICIPIOS =  ServerParques + "api/catalogo/municipios/";
    public static String API_PARQUES_BANCOS =  ServerParques + "api/catalogo/bancos/";
    public static String API_PARQUES_PARAMETROS =  ServerParques + "api/catalogo/parametros/";
    public static String API_PARQUES_CANCELAR_RESERVA =  ServerParques + "api/reserva/cancelarreserva/";
    public static String API_PARQUES_VALIDAR_RESERVA =  ServerParques + "api/reserva/validarreserva/";
    public static String API_VISITA_TENICA_LOGIN =  ServerSIDCAR + "api/visitatecnica/login/";
    public static String API_CONSULTA_PUBLICA_TRAMITES =  ServerSIDCAR + "api/consultapublica/tramites/";

    public static String API_BUSCAR_EXPEDIENTE =  ServerSAE + "api/expediente/buscar/";
    public static String API_OBTENER_EXPEDIENTE =  ServerSAE + "api/expediente/obtenerexpediente/";
    public static String API_EXPEDIENTE_DOCUMENTOS =  ServerSAE + "api/expediente/obtenerdocumentos/";

    public static String API_SIDCAR_LUGARES =  ServerSIDCAR + "api/catalogos/listarlugares/";
    public static String API_SIDCAR_ID_LUGAR_X_COORDENADA =  ServerSIDCAR + "api/catalogos/obteneridmunicipioxcoordenada/";

    public static String API_SIDCAR_RADICARPQR =  ServerSIDCAR + "api/radicados/radicarpqr/";
    public static String API_SIDCAR_RADICARPQR_IMAGENES =  ServerSIDCAR + "api/radicados/publicarimagenestemporal/";

    public static String API_BANKPROJECT_GET_PROJECT =  ServerSIDCAR + "api/bankproject/getproject/";
    public static String API_BANKPROJECT_GET_DOCUMENTS =  ServerSIDCAR + "api/bankproject/getdocuments/";


    public static String API_BICICAR_LOGIN =  ServerBICICAR + "api/beneficiarios/login/";
    public static String API_BICICAR_OBTENER_ITEM =  ServerBICICAR + "api/beneficiarios/obtenerItem/";
    public static String API_BICICAR_LOG_TRAYECTO =  ServerBICICAR + "api/beneficiarios/logTrayecto/";
}
