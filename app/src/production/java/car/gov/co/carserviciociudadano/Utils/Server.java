package car.gov.co.carserviciociudadano.Utils;

/**
 * Created by apple on 3/06/18.
 */

public class Server {
    public static String ServerParques = "https://parques.car.gov.co/";    //pruebas car
    public static String ServerSIDCAR = "https://sidcar.car.gov.co/";    //produccion sidcar
    public static String ServerSAE = "https://sae.car.gov.co/";    //produccion sae

    public static String ServerBICICAR = "http://bicicar.car.gov.co/";    //LOCAL sidcar
    public static String ServerBICICAR(){
        return  ServerBICICAR;
    }
}
