package car.gov.co.carserviciociudadano.openweather.model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by INTEL on 11/04/2018.
 */

public class Forecast {
    public String cod;
    public double message;
    public int cnt;
    public java.util.List<List> list;
    public City city;
    public static final String TAG = "tag_forecast";
    public static final String TAG_DAY = "tag_day_forecast";


    public JSONObject toJSONObject(){

        Gson gson = new Gson();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(gson.toJson(this));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }
}
