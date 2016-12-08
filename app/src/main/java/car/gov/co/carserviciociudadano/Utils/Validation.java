package car.gov.co.carserviciociudadano.Utils;

/**
 * Created by Olger on 06/12/2016.
 */


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.drawable.Drawable;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;


public class Validation {

    protected static int SalePriceMinValue = 3999999;
    protected static int RentalPriceMinValue = 29999;
    protected static int VacationPriceMinValue = 14999;
    protected static int RentalParkingPriceMinValue = 9999;



    public static boolean IsValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean IsValidEmail(EditText campo) {
        if(!Validation.IsValidEmail(campo.getText().toString())){

            campo.setError(AppCar.getContext().getResources().getString(R.string.email_incorrecto));
            return  false;
        }
        return true;
    }

    public static boolean IsValidPassword(String psw){
        String PASSWORD_PATTERN = "[A-Za-z0-9]";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(psw);
        return matcher.matches();
    }

    public static boolean IsValidPassword(EditText campo){
        if (campo.getText().toString().length() < 4 ) {
            campo.setError(AppCar.getContext().getResources().getString(R.string.error_contrasena));
            return  false;
        }
        return true;
    }



    public static boolean IsEmpty(EditText campo){
        if(campo.getText().toString().trim().isEmpty()){
            campo.requestFocus();
            campo.setError(AppCar.getContext().getResources().getString(R.string.error_campo_obligatorio));
            return true;
        }
        else
            campo.setError(null);
        return false;
    }



    public static boolean IsChecked(CheckBox campo){
        if(!campo.isChecked()){

            campo.setError("Debe marcar el campo");
            return false;
        }
        return true;
    }

    public static boolean IsPhone(String phone){

        if(phone.length() < 7 || phone.length()>10) return false;
        if (phone.length()>= 7 && phone.length() < 10) return true;
        if (phone.length() == 10 && phone.startsWith("3")) return true;
        return false;

    }

    public static boolean IsPhone(EditText campo){
        if(!Validation.IsPhone(campo.getText().toString())){
            campo.setError(AppCar.getContext().getResources().getString(R.string.error_telefono));
            return false;
        }
        return true;
    }

    public static boolean IsEmpty(Spinner spinner){
        if(spinner.getSelectedItemPosition()<=0)
        {
            TextView errorText = (TextView)spinner.getSelectedView();

            if (errorText != null)
                errorText.setError(AppCar.getContext().getResources().getString(R.string.error_campo_obligatorio));
            return true;
        }
        return false;
    }

    public static boolean IsChecked(RadioButton campo) {
        if(!campo.isChecked()){
            campo.setError(AppCar.getContext().getResources().getString(R.string.error_campo_obligatorio));
            return false;
        }
        return true;
    }

    public static void setErrorMessage(EditText campo, int stringMessage) {
        campo.requestFocus();
        campo.setError(AppCar.getContext().getResources().getString(stringMessage));
    }
}