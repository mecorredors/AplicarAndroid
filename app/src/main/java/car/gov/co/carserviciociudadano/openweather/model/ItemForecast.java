package car.gov.co.carserviciociudadano.openweather.model;

import android.content.Intent;

import java.util.Calendar;

/**
 * Created by INTEL on 14/04/2018.
 */

public class ItemForecast {

    public Integer id;
    public String fecha;
    public String icon;
    public String temp;
    public String condition;
    public String humidity;
    public  String windSpeed;
    public Calendar fechaCalendar;
    public boolean iconNextPage = true;

}
