package car.gov.co.carserviciociudadano.parques.model;

import android.util.Log;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;

/**
 * Created by Olger on 27/11/2016.
 */

public class ErrorApi {
    private int StatusCode;
    private int Code;
    private String Message;

    public int getCode() {
        return Code;
    }
    public void setCode(int code) {
        Code = code;
    }
    public String getMessage() {
        return Message;
    }
    public void setMessage(String message) {
        Message = message;
    }
    public int getStatusCode() {
        return StatusCode;
    }
    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }

    // constructor objeto response apatir de un error de volley
    public ErrorApi(VolleyError vError){
        if(vError != null )
            Log.e("Error ", vError.toString() );
        if(vError!=null && vError.networkResponse != null){
            this.StatusCode=vError.networkResponse.statusCode;

            String responseBody="";
            try {
                responseBody = new String(vError.networkResponse.data, "utf-8");

                if(responseBody.contains("Message")){
                    JSONObject jsonObject = new JSONObject( responseBody );

                    Gson gson= new Gson();
                    ErrorApi er= gson.fromJson(jsonObject.toString(), ErrorApi.class);
                    this.Message=er.getMessage();
                    this.Code=er.getCode();
                }else{
                    this.Message=responseBody;
                }

            } catch ( JSONException e ) {
                this.Message=responseBody;
            } catch (UnsupportedEncodingException error){
                this.Message=vError.getMessage();
            }
        }else{
            this.StatusCode=500;
            this.Message="Servicio no disponible";
        }

        if (this.StatusCode == 404) this.Message = AppCar.getContext().getString(R.string.error_404);
        if (this.StatusCode == 405) this.Message = AppCar.getContext().getString(R.string.error_405);

    }

    public ErrorApi(){
       setMessage("");
    }
    public ErrorApi(JSONException ex){
        StatusCode = 0;
        Message = ex.toString();
        Log.e("Errror ",ex.toString());
    }
    public ErrorApi(JsonSyntaxException ex){
        StatusCode = 0;
        Message = ex.toString();
        Log.e("Errror ",ex.toString());
    }
    public void setMessageJson(String message){

        try {

            if(message.contains("Message")){
                //	JSONObject jsonObject = new JSONObject( message );
                Gson gson= new Gson();
                ErrorApi er= gson.fromJson(message, ErrorApi.class);
                this.Message=er.getMessage();
                this.Code=er.getCode();
            }else{
                this.Message=message;
            }

        } catch ( JsonSyntaxException e ) {
            this.Message=message;
        }

    }
}
