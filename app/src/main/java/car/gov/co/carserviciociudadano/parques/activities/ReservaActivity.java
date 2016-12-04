package car.gov.co.carserviciociudadano.parques.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.dataaccess.Mantenimientos;
import car.gov.co.carserviciociudadano.parques.dataaccess.ParametrosReserva;
import car.gov.co.carserviciociudadano.parques.dataaccess.Reservas;
import car.gov.co.carserviciociudadano.parques.dataaccess.ServicioReservas;
import car.gov.co.carserviciociudadano.parques.interfaces.IMantenimiento;
import car.gov.co.carserviciociudadano.parques.interfaces.IParametro;
import car.gov.co.carserviciociudadano.parques.interfaces.IReserva;
import car.gov.co.carserviciociudadano.parques.interfaces.IServicioReserva;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Mantenimiento;
import car.gov.co.carserviciociudadano.parques.model.ParametroReserva;
import car.gov.co.carserviciociudadano.parques.model.Parque;
import car.gov.co.carserviciociudadano.parques.model.ServicioParque;
import car.gov.co.carserviciociudadano.parques.model.ServicioReserva;

public class ReservaActivity extends BaseActivity {


    private String mNombreServicio;


    TextView mLblNombreParque;
    CustomCalendarView mCalendarioFechaLlegada;
    CustomCalendarView mCalendarioFechaSalida;
    List<Mantenimiento> mLstMatenimientos = new ArrayList<>();
    List<ServicioReserva> mLstEnReservas = new ArrayList<>();
   // List<ParametroReserva> mLstParametros = new ArrayList<>();
    Map<String, String> mapParametros = new HashMap<>();

    private boolean finishLoadMatenimiento = false;
    private boolean finishLoadEnReserva = false;
    EditText mTxtFechaLlegada;
    EditText mTxtFechaSalida;
    Button mBtnPrereserva;
    TextView mLblServicio;
    TextView mLblNroNoches;
    TextView mLblPrecio;
    TextView mLblSubtotal;
    TextView mLblImpuesto;
    TextView mLblFechaDesde;
    TextView mLblFechaHasta;
    View mLyCanasta;
    Button mBtnBorrar;

    ServicioReserva mServicioReserva = new ServicioReserva();
    ServicioParque mServicioParque = new ServicioParque();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);

        findViewsById();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {

            mNombreServicio = bundle.getString(ServicioParque.NOMBRE_SERVICIO,"");
            mLblNombreParque.setText(bundle.getString(Parque.NOMBRE_PARQUE,"")+" "+ mNombreServicio);

            mServicioParque.setIDServiciosParque(bundle.getInt(ServicioParque.ID_SERVICIOS_PARQUE));
            mServicioParque.setImpuestoServicio(bundle.getInt(ServicioParque.IMPUESTO_SERVICIO));
            mServicioParque.setPrecioServicio(bundle.getLong(ServicioParque.PRECIO_SERVICIO));
            mServicioParque.setPrecioCar(bundle.getLong(ServicioParque.PRECIO_CAR));
            mServicioParque.setActivoServicio(bundle.getInt(ServicioParque.ACTIVO_SERVICIO));
            mServicioParque.setIDParque(bundle.getInt(Parque.ID_PARQUE));

        }

        loadDiasMantenimiento();
        loadDiasEnReserva();
        loadParametros();

        mBtnPrereserva.setVisibility(View.GONE);
        mLyCanasta.setVisibility(View.GONE);

        mCalendarioFechaLlegada.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                SimpleDateFormat df = new SimpleDateFormat(Utils.formatoFecha());
                mTxtFechaLlegada.setText(df.format(date));
                mCalendarioFechaLlegada.setVisibility(View.GONE);
                if( !mTxtFechaLlegada.getText().toString().isEmpty()  && !mTxtFechaSalida.getText().toString().isEmpty())
                    mBtnPrereserva.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMonthChanged(Date date) {

            }
        });

        mCalendarioFechaSalida.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                SimpleDateFormat df = new SimpleDateFormat(Utils.formatoFecha());
                mTxtFechaSalida.setText(df.format(date));
                mCalendarioFechaSalida.setVisibility(View.GONE);
                if( !mTxtFechaLlegada.getText().toString().isEmpty()  && !mTxtFechaSalida.getText().toString().isEmpty())
                    mBtnPrereserva.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMonthChanged(Date date) {

            }
        });

    }

    private void loadCalendar(){
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        List<DayDecorator> decorators = new ArrayList<>();
        decorators.add(new ColorDecorator());
        mCalendarioFechaLlegada.setDecorators(decorators);
        mCalendarioFechaLlegada.refreshCalendar(currentCalendar);

        mCalendarioFechaSalida.setDecorators(decorators);
        mCalendarioFechaSalida.refreshCalendar(currentCalendar);
    }

    private  void findViewsById(){
        mLblNombreParque = (TextView) findViewById(R.id.lblNombreParque);
        mCalendarioFechaLlegada = (CustomCalendarView) findViewById(R.id.calendarioFechaLlegada);
        mCalendarioFechaSalida = (CustomCalendarView) findViewById(R.id.calendarioFechaSalida);
        mTxtFechaLlegada = (EditText) findViewById(R.id.txtFechaLlegada);
        mTxtFechaSalida = (EditText) findViewById(R.id.txtFechaSalida);
        mBtnPrereserva = (Button) findViewById(R.id.btnPreReserva);
        mLblServicio = (TextView) findViewById(R.id.lblServicio);
        mLblNroNoches = (TextView) findViewById(R.id.lblNroNoches);
        mLblPrecio = (TextView) findViewById(R.id.lblPrecio);
        mLblSubtotal = (TextView) findViewById(R.id.lblSubTotal);
        mLblImpuesto = (TextView) findViewById(R.id.lblImpuesto);
        mLblFechaDesde = (TextView) findViewById(R.id.lblFechaDesde);
        mLblFechaHasta = (TextView) findViewById(R.id.lblFechaHasta);
        mLyCanasta =  findViewById(R.id.lyCanasta);
        mBtnBorrar = (Button) findViewById(R.id.btnBorrar);

        mTxtFechaLlegada.setOnClickListener(onClickListener);
        mTxtFechaSalida.setOnClickListener(onClickListener);
        mBtnPrereserva.setOnClickListener(onClickListener);
        mBtnBorrar.setOnClickListener(onClickListener);
    }

    private class ColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
//            if (CalendarUtils.isPastDay(dayView.getDate())) {
//                int color = Color.parseColor("#a9afb9");
//                dayView.setBackgroundColor(color);
//            }

            for(Mantenimiento item: mLstMatenimientos){
                if(Utils.isEqualsDate(dayView.getDate(),item.getFecha())){
                    int color = Color.parseColor("#f44336");
                    dayView.setBackgroundColor(color);
                }
            }

            ServicioReservas servicioReserva = new ServicioReservas();
            for(ServicioReserva item: mLstEnReservas){

                List<Date> lstEnReserva = servicioReserva.getAllFechasEnReserva(item);

                for (Date fechaEnReserva: lstEnReserva) {

                    if (item.getEstadoReserva() == Enumerator.ReservaEstado.PRE_RESERVA && (Utils.isEqualsDate(dayView.getDate(), fechaEnReserva) )) {
                        int color = Color.parseColor("#3F51B5");
                        dayView.setBackgroundColor(color);
                    }

                    if (item.getEstadoReserva() == Enumerator.ReservaEstado.RESERVA_APROBADA && (Utils.isEqualsDate(dayView.getDate(), fechaEnReserva) )) {
                        int color = Color.parseColor("#4CAF50");
                        dayView.setBackgroundColor(color);
                    }
                }
            }
        }
    }

    private void loadDiasMantenimiento(){
        Mantenimientos mantenimientos = new Mantenimientos();
        mantenimientos.list(new IMantenimiento() {
            @Override
            public void onSuccess(List<Mantenimiento> lstMantenimientos) {
                mLstMatenimientos.clear();
                mLstMatenimientos.addAll(lstMantenimientos);
                loadCalendar();
            }

            @Override
            public void onError(ErrorApi error) {
                mostrarMensaje(error.getMessage());
            }
        },mServicioParque.getIDServiciosParque());
    }

    private void loadDiasEnReserva(){
        ServicioReservas servicioReservas = new ServicioReservas();
        servicioReservas.list(new IServicioReserva() {
            @Override
            public void onSuccess(List<ServicioReserva> lstServicioReservas) {
                mLstEnReservas.clear();
                mLstEnReservas.addAll(lstServicioReservas);
                loadCalendar();
            }

            @Override
            public void onError(ErrorApi error) {
                mostrarMensaje(error.getMessage());
            }
        },mServicioParque.getIDServiciosParque());
    }

    private void loadParametros(){
        ParametrosReserva parametroReserva = new ParametrosReserva();
        parametroReserva.list(new IParametro() {
            @Override
            public void onSuccess(List<ParametroReserva> lstParametros) {
               // mLstParametros.clear();
               // mLstParametros.addAll(lstParametros);
                mapParametros.clear();
                for (ParametroReserva item: lstParametros){
                    mapParametros.put(item.getLlave(),item.getValor());
                }
            }

            @Override
            public void onError(ErrorApi error) {
                  mostrarMensaje(error.getMessage());
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.txtFechaLlegada:
                    mCalendarioFechaLlegada.setVisibility(View.VISIBLE);
                    mCalendarioFechaSalida.setVisibility(View.GONE);
                    break;
                case R.id.txtFechaSalida:
                    mCalendarioFechaLlegada.setVisibility(View.GONE);
                    mCalendarioFechaSalida.setVisibility(View.VISIBLE);
                    break;
                case R.id.btnPreReserva:
                    prereserva();
                    break;
                case R.id.btnBorrar:
                    mLyCanasta.setVisibility(View.GONE);
                    mBtnPrereserva.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private void prereserva(){

        if(validar()){

            mServicioReserva.setFechaInicialReserva(Utils.convertToDate(mTxtFechaLlegada.getText().toString()));
            mServicioReserva.setFechaFinalReserva(Utils.convertToDate(mTxtFechaSalida.getText().toString()));
            mServicioReserva.setIDServiciosParque(mServicioParque.getIDServiciosParque());

            Reservas reservas = new Reservas();
            reservas.validarDisponibilidad(mServicioReserva,new IReserva() {
                @Override
                public void onSuccess(ServicioReserva servicioReserva) {

                }

                @Override
                public void onSuccess(boolean res) {
                    if (res){

                        llenarCanasta();
                        mBtnPrereserva.setVisibility(View.GONE);
                    }else{
                        mostrarMensajeDialog(getString(R.string.reserva_no_disponible));
                    }
                }

                @Override
                public void onError(ErrorApi error) {
                    mostrarMensajeDialog("No se pudo conectar con el servidor");

                }
            });
        }
    }

    private boolean  validar(){
        boolean res = true;

        if(mTxtFechaLlegada.getText().toString().isEmpty() || mTxtFechaSalida.getText().toString().isEmpty() ) {
            mostrarMensajeDialog("Seleccione fecha de llegada y fecha de salida");
            return false;
        }

        Calendar fechaLlegada = Utils.convertToCalendar(mTxtFechaLlegada.getText().toString());
        Calendar fechaSalida = Utils.convertToCalendar(mTxtFechaSalida.getText().toString());


        if (!fechaLlegada.before(fechaSalida)){
            mostrarMensajeDialog("La fecha de salida debe ser mayor  a la de llegada");
            return false;
        }

        int numDiasReserva = Utils.difDaysDates(fechaLlegada,fechaSalida);
        if ( numDiasReserva > Utils.convertInt(mapParametros.get(ParametroReserva.maximoNroDiasAReservar)) ){
            mostrarMensajeDialog(getString(R.string.maximo_dias) +" "+ mapParametros.get(ParametroReserva.maximoNroDiasAReservar) +" " + getString(R.string.dias) );
            return false;
        }

        Calendar fechaActual = Calendar.getInstance();
        int numDiasInicioReserva = Utils.difDaysDates(fechaActual,fechaSalida);
        if ( numDiasInicioReserva < Utils.convertInt(mapParametros.get(ParametroReserva.minimoNumDiasReserva)) ){
            mostrarMensajeDialog(getString(R.string.fecha_llegada) +" "+ mapParametros.get(ParametroReserva.minimoNumDiasReserva) +" " + getString(R.string.dias_antelacion) );
            return false;
        }

        if ( numDiasInicioReserva > Utils.convertInt(mapParametros.get(ParametroReserva.nroDiasMaximoParaHacerReservas)) ){
            mostrarMensajeDialog(getString(R.string.fecha_inicial) +" "+ mapParametros.get(ParametroReserva.nroDiasMaximoParaHacerReservas) +" " + getString(R.string.dias) );
            return false;
        }

        return res;
    }


    private void llenarCanasta(){

        mLyCanasta.setVisibility(View.VISIBLE);

        int numDiasReserva = Utils.difDaysDates(Utils.convertToCalendar(mTxtFechaLlegada.getText().toString()) , Utils.convertToCalendar(mTxtFechaSalida.getText().toString()));

        //validar si es funcinario car
        long precio = mServicioParque.getPrecioServicio();

        mLblServicio.setText(mNombreServicio);
        mLblPrecio.setText(Utils.formatoMoney(precio));
        mLblImpuesto.setText(String.valueOf(mServicioParque.getImpuestoServicio() ));
        mLblFechaDesde.setText(mTxtFechaLlegada.getText().toString());
        mLblFechaHasta.setText(mTxtFechaSalida.getText().toString());
        mLblNroNoches.setText(String.valueOf(numDiasReserva));

        long subTotal = precio * numDiasReserva;
        mLblSubtotal.setText(Utils.formatoMoney(subTotal));

    }

}
