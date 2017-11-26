package car.gov.co.carserviciociudadano.consultapublica.model;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Olger on 25/11/2017.
 */

public class BankProjectDocument {
    private String Title;
    private String RegistrationCode;
    private Date RegistrationDate;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getRegistrationCode() {
        return RegistrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        RegistrationCode = registrationCode;
    }

    public Date getRegistrationDate() {
        return RegistrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        RegistrationDate = registrationDate;
    }
    public  BankProjectDocument() {
    }
    public  BankProjectDocument(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                BankProjectDocument element = gson.fromJson(json, BankProjectDocument.class);

                this.Title = element.Title;
                this.RegistrationCode = element.RegistrationCode;
                this.RegistrationDate = element.RegistrationDate;

            } catch (JsonSyntaxException ex) {
                Log.e("BankProjectDocument", ex.toString());
                Crashlytics.logException(ex);
            }
        }
    }

    @Override public String toString(){
        Gson gson = new Gson();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(gson.toJson(this));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject.toString();
    }
}
