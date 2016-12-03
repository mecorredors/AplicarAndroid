package car.gov.co.carserviciociudadano.parques.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;
import com.stacktips.view.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.parques.dataaccess.ServiciosParque;
import car.gov.co.carserviciociudadano.parques.model.Parque;
import car.gov.co.carserviciociudadano.parques.model.ServicioParque;

public class ReservaActivity extends BaseActivity {

    private int mIdParque;
    private int mIdServicioParque;

    TextView mLblNombreParque;
    CustomCalendarView mCalendarioFechaLlegada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);

        findViewsById();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            mIdParque = bundle.getInt(Parque.ID_PARQUE);
            mIdServicioParque = bundle.getInt(ServicioParque.ID_SERVICIOS_PARQUE);
            mLblNombreParque.setText(bundle.getString(Parque.NOMBRE_PARQUE,"")+" "+ bundle.getString(ServicioParque.NOMBRE_SERVICIO,""));
        }

      loadCalendar();

    }

    private void loadCalendar(){
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        List<DayDecorator> decorators = new ArrayList<>();
        decorators.add(new DisabledColorDecorator());
        mCalendarioFechaLlegada.setDecorators(decorators);
        mCalendarioFechaLlegada.refreshCalendar(currentCalendar);
    }

    private  void findViewsById(){
        mLblNombreParque = (TextView) findViewById(R.id.lblNombreParque);
        mCalendarioFechaLlegada = (CustomCalendarView) findViewById(R.id.calendarioFechaLlegada);
    }

    private class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
//            if (CalendarUtils.isPastDay(dayView.getDate())) {
//                int color = Color.parseColor("#a9afb9");
//                dayView.setBackgroundColor(color);
//            }

            Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
             currentCalendar.set(2016,12,10);
            Calendar currentCalendar2 = Calendar.getInstance(Locale.getDefault());
            currentCalendar2.setTime( dayView.getDate());


           if ( currentCalendar.get(Calendar.DAY_OF_MONTH) == (currentCalendar2.get(Calendar.DAY_OF_MONTH))) {
                int color = Color.parseColor("#ff0000");
                dayView.setBackgroundColor(color);
            }
        }
    }

}
