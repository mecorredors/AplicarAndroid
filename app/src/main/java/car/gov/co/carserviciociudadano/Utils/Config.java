package car.gov.co.carserviciociudadano.Utils;

/**
 * Created by Olger on 26/11/2016.
 */

public class Config {

    public static String ServerParques = "http://192.168.0.13/parques/";    //local
  //  public static String ServerParques = "http://192.168.1.53/parques/";    //pruebas car

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
    public static String API_PARQUES_USUARIO_LOGIN =  ServerParques + "api/usuario/login/";
    public static String API_PARQUES_MUNICIPIOS =  ServerParques + "api/catalogo/municipios/";
    public static String API_PARQUES_BANCOS =  ServerParques + "api/catalogo/bancos/";
    public static String API_PARQUES_PARAMETROS =  ServerParques + "api/catalogo/parametros/";
    public static String API_PARQUES_CANCELAR_RESERVA =  ServerParques + "api/reserva/cancelarreserva/";
    public static String API_PARQUES_VALIDAR_RESERVA =  ServerParques + "api/reserva/validarreserva/";

}
