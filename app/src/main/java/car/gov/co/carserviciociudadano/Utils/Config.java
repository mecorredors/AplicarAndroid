package car.gov.co.carserviciociudadano.Utils;

/**
 * Created by Olger on 26/11/2016.
 */

public class Config {

    public static String OpenWeatherIcon = "http://openweathermap.org/img/w/";    //icono openweathermap

    public static String API_PARQUES_PARQUES =  Server.ServerParques + "api/parque/parques/";
    public static String API_PARQUES_ARCHIVOS =  Server.ServerParques + "api/parque/archivos/";
    public static String API_PARQUES_MANTENIMIENTOS =  Server.ServerParques + "api/parque/mantenimientos/";
    public static String API_PARQUES_SERVICIOS =  Server.ServerParques + "api/parque/servicios/";
    public static String API_PARQUES_SERVICIOS_EN_RESERVA =  Server.ServerParques + "api/reserva/serviciosenreserva/";
    public static String API_PARQUES_RESERVA_DETALLE =  Server.ServerParques + "api/reserva/detalle/";
    public static String API_PARQUES_RESERVA =  Server.ServerParques + "api/reserva/reserva/";
    public static String API_PARQUES_ABONOS =  Server.ServerParques + "api/reserva/abonos/";
    public static String API_PARQUES_INGRESAR_ABONO =  Server.ServerParques + "api/reserva/ingresarAbono/";
    public static String API_PARQUES_PUBLICAR_IMAGENES =  Server.ServerParques + "api/reserva/publicarimagenes/";
    public static String API_PARQUES_USUARIO_INSERTAR =  Server.ServerParques + "api/usuario/insertar";
    public static String API_PARQUES_USUARIO_ACTUALIZAR =  Server.ServerParques + "api/usuario/actualizar";
    public static String API_PARQUES_USUARIO_RECUPERAR_CONTRASENA =  Server.ServerParques + "api/usuario/recuperarcontrasena";
    public static String API_PARQUES_USUARIO_LOGIN =  Server.ServerParques + "api/usuario/login/";
    public static String API_PARQUES_USUARIO_CAMBIAR_CONTRASENA =  Server.ServerParques + "api/usuario/cambiarcontrasena/";
    public static String API_PARQUES_MUNICIPIOS =  Server.ServerParques + "api/catalogo/municipios/";
    public static String API_PARQUES_BANCOS =  Server.ServerParques + "api/catalogo/bancos/";
    public static String API_PARQUES_PARAMETROS =  Server.ServerParques + "api/catalogo/parametros/";
    public static String API_PARQUES_CANCELAR_RESERVA =  Server.ServerParques + "api/reserva/cancelarreserva/";
    public static String API_PARQUES_VALIDAR_RESERVA =  Server.ServerParques + "api/reserva/validarreserva/";
    public static String API_VISITA_TENICA_LOGIN =  Server.ServerSIDCAR + "api/visitatecnica/login/";
    public static String API_CONSULTA_PUBLICA_TRAMITES =  Server.ServerSIDCAR + "api/consultapublica/tramites/";

    public static String API_BUSCAR_EXPEDIENTE =  Server.ServerSAE + "api/expediente/buscar/";
    public static String API_OBTENER_EXPEDIENTE =  Server.ServerSAE + "api/expediente/obtenerexpediente/";
    public static String API_EXPEDIENTE_DOCUMENTOS =  Server.ServerSAE + "api/expediente/obtenerdocumentos/";

    public static String API_SIDCAR_LUGARES =  Server.ServerSIDCAR + "api/catalogos/listarlugares/";
    public static String API_SIDCAR_ID_LUGAR_X_COORDENADA =  Server.ServerSIDCAR + "api/catalogos/obteneridmunicipioxcoordenada/";

    public static String API_SIDCAR_RADICARPQR =  Server.ServerSIDCAR + "api/radicados/radicarpqr/";
    public static String API_SIDCAR_RADICARPQR_IMAGENES =  Server.ServerSIDCAR + "api/radicados/publicarimagenestemporal/";

    public static String API_BANKPROJECT_GET_PROJECT =  Server.ServerSIDCAR + "api/bankproject/getproject/";
    public static String API_BANKPROJECT_GET_DOCUMENTS =  Server.ServerSIDCAR + "api/bankproject/getdocuments/";


    public static String API_BICICAR_LOGIN =  Server.ServerBICICAR() + "api/beneficiarios/login/";
    public static String API_BICICAR_OBTENER_BENEFICIARIO =  Server.ServerBICICAR() + "api/beneficiarios/obtenerItem/";
    public static String API_BICICAR_LISTAR_BENEFICIARIOS =  Server.ServerBICICAR() + "api/beneficiarios/listaritems/";
    public static String API_BICICAR_LOG_TRAYECTO =  Server.ServerBICICAR() + "api/beneficiarios/logTrayecto/";
    public static String API_BICICAR_REPORTES_GRAN_TOTAL =  Server.ServerBICICAR() + "api/reportes/getgrantotal/";
    public static String API_BICICAR_REPORTES_ESTADISTICA =  Server.ServerBICICAR() + "api/reportes/getestadistica/";
    public static String API_BICICAR_REPORTES_ESTADISTICA_PERSONA =  Server.ServerBICICAR() + "api/reportes/getestadisticapersona/";
    public static String API_BICICAR__OBTENER_BICICLETA =  Server.ServerBICICAR() + "api/beneficiarios/obtenerbicicleta/";
    public static String API_BICICAR__RECORDAR_CLAVE =  Server.ServerBICICAR() + "api/beneficiarios/recordarclave/";
    public static String API_BICICAR_OBTENER_RUTAS =  Server.ServerBICICAR() + "api/beneficiarios/obtenerRutas/";
    public static String API_BICICAR_RUTA =  Server.ServerBICICAR() + "api/beneficiarios/ruta/";
    public static String API_BICICAR_CATALOGO_OBTENER_NIVELES =  Server.ServerBICICAR() + "api/catalogos/ObtenerNiveles/";
    public static String API_BICICAR_CATALOGO_OBTENER_COLEGIOS =  Server.ServerBICICAR() + "api/catalogos/ObtenerColegios/";
    public static String API_BICICAR_CATALOGO_ACTUALIZAR_COLEGIO =  Server.ServerBICICAR() + "api/catalogos/ActualizarColegio/";
    public static String API_BICICAR_BENEFICIARIO_ACTUALIZAR =  Server.ServerBICICAR() + "api/beneficiarios/Actualizar/";

}
