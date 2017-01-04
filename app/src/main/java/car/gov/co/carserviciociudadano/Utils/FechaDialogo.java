package car.gov.co.carserviciociudadano.Utils;

/**
 * Created by Olger on 04/01/2017.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Olger on 21/02/2015.
 */
public class FechaDialogo extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public OnListerFecha listerFecha;
    private int mYear;
    private  int mMonth;
    private int mDay;


//    public FechaDialogo(Calendar c){
//        mYear = c.get(Calendar.YEAR);
//        mMonth = c.get(Calendar.MONTH);
//        mDay = c.get(Calendar.DAY_OF_MONTH);
//    }
    public FechaDialogo(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        // Create a new instance of DatePickerDialog and return it

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    // datePickerPlugin.success(new PluginResult(PluginResult.Status.OK, ""), callBackId);
                    // dateDialog.hide();
                    listerFecha.onDatasetCalcel();
                    dismiss();
                }
            }
        });
        return  datePickerDialog;
        //    return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        listerFecha.onDataset(view,year,month,day);

    }

    public interface OnListerFecha{

        public void onDataset(DatePicker view, int year, int month, int day);
        public void onDatasetCalcel();

    }
}
