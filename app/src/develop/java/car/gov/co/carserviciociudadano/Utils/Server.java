package car.gov.co.carserviciociudadano.Utils;

/**
 * Created by apple on 3/06/18.
 */

public class Server {

    public static String ServerParques = "https://parques.car.gov.co/";    //pruebas car
    public static String ServerSIDCAR = "https://sidcar.car.gov.co/";    //produccion sidcar
    public static String ServerSAE = "https://sae.car.gov.co/";    //produccion sae

    public static String ServerBICICAR = "http://192.168.0.19/BICICAR/";    //LOCAL sidcar
   // public static String ServerBICICAR = "https://bicicar.car.gov.co/";    //LOCAL sidcar
    public static String ServerAplicar = "http://192.168.0.19/BICICAR/";

    public static String ServerBICICAR(){
       // return PreferencesApp.getDefault(PreferencesApp.READ).getString("SERVER_BICICAR", ServerBICICAR);
        return ServerBICICAR;
    }

    //public static String ServerParques = "http://192.168.1.53/PARQUES/";    //pruebas car
    //public static String ServerSIDCAR = "http://192.168.1.53/SIDCARNET/";    //pruebas sidcar
    //public static String ServerSAE = "http://192.168.1.53/SAE/";    //pruebas sae

    // public static String ServerParques = "http://192.168.0.11/parques/";    //LOCAL car
    // public static String ServerSIDCAR = "http://192.168.0.11/SIDCAR/";    //LOCAL sidcar
    //  public static String ServerSAE = "http://192.168.0.11/SAE/";    //LOCAL sidcar
}
