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

public class BankProjectItem {
    private int ProjectID;
    private String RegistrationCode;
    private String Name;
    private String MunicipalityID;
    private int StatusID;
    private String StatusName;
    private String SidcarNumber;
    private Date StatusDate;
    private Date RegistrationDate;
    private Double AmountCAR;
    private Double AmountMunicipality;
    private Double AmountOther;
    private String Description;
    private String TopicName;

    public int getProjectID() {
        return ProjectID;
    }

    public void setProjectID(int projectID) {
        ProjectID = projectID;
    }

    public String getRegistrationCode() {
        return RegistrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        RegistrationCode = registrationCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMunicipalityID() {
        return MunicipalityID;
    }

    public void setMunicipalityID(String municipalityID) {
        MunicipalityID = municipalityID;
    }

    public int getStatusID() {
        return StatusID;
    }

    public void setStatusID(int statusID) {
        StatusID = statusID;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }

    public String getSidcarNumber() {
        return SidcarNumber;
    }

    public void setSidcarNumber(String sidcarNumber) {
        SidcarNumber = sidcarNumber;
    }

    public Date getStatusDate() {
        return StatusDate;
    }

    public void setStatusDate(Date statusDate) {
        StatusDate = statusDate;
    }

    public Date getRegistrationDate() {
        return RegistrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        RegistrationDate = registrationDate;
    }

    public Double getAmountCAR() {
        return AmountCAR;
    }

    public void setAmountCAR(Double amountCAR) {
        AmountCAR = amountCAR;
    }

    public Double getAmountMunicipality() {
        return AmountMunicipality;
    }

    public void setAmountMunicipality(Double amountMunicipality) {
        AmountMunicipality = amountMunicipality;
    }

    public Double getAmountOther() {
        return AmountOther;
    }

    public void setAmountOther(Double amountOther) {
        AmountOther = amountOther;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTopicName() {
        return TopicName;
    }

    public void setTopicName(String topicName) {
        TopicName = topicName;
    }

    public  BankProjectItem(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                BankProjectItem element = gson.fromJson(json, BankProjectItem.class);

                this.ProjectID = element.ProjectID;
                this.RegistrationCode = element.RegistrationCode;
                this.Name = element.Name;
                this.MunicipalityID = element.MunicipalityID;
                this.StatusID = element.StatusID;
                this.StatusName = element.StatusName;
                this.SidcarNumber = element.SidcarNumber;
                this.StatusDate = element.StatusDate;
                this.RegistrationDate = element.RegistrationDate;
                this.AmountCAR = element.AmountCAR;
                this.AmountMunicipality = element.AmountMunicipality;
                this.AmountOther = element.AmountOther;
                this.Description = element.Description;
                this.TopicName = element.TopicName;

            } catch (JsonSyntaxException ex) {
                Log.e("BankProjectItem.json", ex.toString());
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
