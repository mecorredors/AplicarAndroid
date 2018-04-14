package car.gov.co.carserviciociudadano.openweather.model;

import retrofit2.Converter;

/**
 * Created by INTEL on 11/04/2018.
 */

public class Main {
    public double temp;
    public double temp_min;
    public double temp_max;
    public double pressure;
    public double sea_level;
    public double grnd_level;
    public int humidity;
    public double temp_kf;

    public int getTempRound(){
        return (int) Math.round(temp);
    }

    public int getTempMaxRound(){
        return (int) Math.round(temp_max);
    }

    public int getTempMinRound(){
        return (int) Math.round(temp_min);
    }
}
