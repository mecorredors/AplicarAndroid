package car.gov.co.carserviciociudadano.parques.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;
import com.stacktips.view.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Config;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.dataaccess.Mantenimientos;
import car.gov.co.carserviciociudadano.parques.dataaccess.ParametrosReserva;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.dataaccess.Reservas;
import car.gov.co.carserviciociudadano.parques.dataaccess.ServicioReservas;
import car.gov.co.carserviciociudadano.parques.dataaccess.ServiciosParque;
import car.gov.co.carserviciociudadano.parques.dataaccess.Usuarios;
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
import car.gov.co.carserviciociudadano.parques.model.Usuario;

public class ReservaActivity extends BaseActivity {

    @BindView(R.id.txtFechaLlegada) EditText mTxtFechaLlegada;
    @BindView(R.id.txtFechaSalida) EditText mTxtFechaSalida;
    @BindView(R.id.btnPreReserva) Button mBtnPrereserva;
    @BindView(R.id.lblServicio) TextView mLblServicio;
    @BindView(R.id.lblNroNoches) TextView mLblNroNoches;
    @BindView(R.id.lblPrecio) TextView mLblPrecio;
    @BindView(R.id.lblSubTotal) TextView mLblSubtotal;
    @BindView(R.id.lblImpuesto) TextView mLblImpuesto;
    @BindView(R.id.lblFechaDesde) TextView mLblFechaDesde;
    @BindView(R.id.lblFechaHasta) TextView mLblFechaHasta;
    @BindView(R.id.lyCanasta) View mLyCanasta;
    @BindView(R.id.btnBorrar) Button mBtnBorrar;
    @BindView(R.id.lblNombreParque) TextView mLblNombreParque;
    @BindView(R.id.calendarioFechaLlegada) CustomCalendarView mCalendarioFechaLlegada;
    @BindView(R.id.calendarioFechaSalida) CustomCalendarView mCalendarioFechaSalida;
    @BindView(R.id.lyDatosUsuario) View mLyDatosUsuario;
    @BindView(R.id.btnDatosUsuario) Button mBtnDatosUsuario;
    @BindView(R.id.btnReservar) Button mBtnReserva;
    @BindView(R.id.btnCerrar) Button mBtnCerrar;
    @BindView(R.id.lyRespuestaOk) View mLyRespuestaOk;
    @BindView(R.id.lblRespuestaOk) TextView mLblRespuestaOk;
    @BindView(R.id.lblNroCuenta) TextView mLblNroCuenta;
    @BindView(R.id.cheAceptoPoliticas)    CheckBox mCheAceptoPoliticas;
    @BindView(R.id.lyPoliticas)    LinearLayout mLyPoliticas;


    List<Mantenimiento> mLstMatenimientos = new ArrayList<>();
    List<ServicioReserva> mLstEnReservas = new ArrayList<>();
    Map<String, String> mapParametros = new HashMap<>();
    ServicioReserva mServicioReserva = new ServicioReserva();
    ServicioParque mServicioParque = new ServicioParque();
    Parque mParque = new Parque();
    Usuario mUsuario;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        mTxtFechaLlegada.setOnClickListener(onClickListener);
        mTxtFechaSalida.setOnClickListener(onClickListener);
        mBtnPrereserva.setOnClickListener(onClickListener);
        mBtnBorrar.setOnClickListener(onClickListener);
        mBtnCerrar.setOnClickListener(onClickListener);
        mBtnReserva.setOnClickListener(onClickListener);
        mBtnDatosUsuario.setOnClickListener(onClickListener);
        mLyPoliticas.setOnClickListener(onClickListener);

        mUsuario = new Usuarios().leer();

        mParque = (Parque) IntentHelper.getObjectForKey(Parques.TAG);
        mServicioParque = (ServicioParque) IntentHelper.getObjectForKey(ServiciosParque.TAG);
        mLblNombreParque.setText(mParque.getNombreParque() +" "+ mServicioParque.getNombreServicio());
        loadCalendar();
        loadDiasMantenimiento();
        loadDiasEnReserva();
        loadParametros();

        mBtnPrereserva.setVisibility(View.GONE);
        mLyCanasta.setVisibility(View.GONE);
        mLyRespuestaOk.setVisibility(View.GONE);

        mCalendarioFechaLlegada.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {

                if (!Utils.isEqualsDate(date,mServicioReserva.getFechaInicialReserva()))  {
                     mLyCanasta.setVisibility(View.GONE);
                     mLyRespuestaOk.setVisibility(View.GONE);
                    mServicioReserva.setFechaInicialReserva(date);
                    mTxtFechaLlegada.setText(Utils.toStringFromDate(date));
                    if (!mTxtFechaLlegada.getText().toString().isEmpty() && !mTxtFechaSalida.getText().toString().isEmpty())
                        mBtnPrereserva.setVisibility(View.VISIBLE);
                }
                mCalendarioFechaLlegada.setVisibility(View.GONE);
            }

            @Override
            public void onMonthChanged(Date date) {

            }
        });

        mCalendarioFechaSalida.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                if (!Utils.isEqualsDate(date,mServicioReserva.getFechaFinalReserva()))  {
                    mLyCanasta.setVisibility(View.GONE);
                    mLyRespuestaOk.setVisibility(View.GONE);
                    mServicioReserva.setFechaFinalReserva(date);
                    mTxtFechaSalida.setText(Utils.toStringFromDate(date));
                    if (!mTxtFechaLlegada.getText().toString().isEmpty() && !mTxtFechaSalida.getText().toString().isEmpty())
                        mBtnPrereserva.setVisibility(View.VISIBLE);
                }
                mCalendarioFechaSalida.setVisibility(View.GONE);
            }

            @Override
            public void onMonthChanged(Date date) {

            }
        });

    }

    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(Mantenimientos.TAG);
        AppCar.VolleyQueue().cancelAll(ServicioReservas.TAG);
        AppCar.VolleyQueue().cancelAll(ParametrosReserva.TAG);
        AppCar.VolleyQueue().cancelAll(Reservas.TAG);
        super.onPause();

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


    private class ColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            if (CalendarUtils.isPastDay(dayView.getDate()) && Utils.isCurrentMonth(dayView.getDate())   ) {
                int color = Color.parseColor("#E9E9E9");
                dayView.setBackgroundColor(color);
                dayView.setTextColor(Color.parseColor("#DCDCDC"));

            }

            for(Mantenimiento item: mLstMatenimientos){
                if(Utils.isEqualsDate(dayView.getDate(),item.getFecha())){
                    dayView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.mantenimiento));
                }
            }

            ServicioReservas servicioReserva = new ServicioReservas();
            for(ServicioReserva item: mLstEnReservas){

                List<Date> lstEnReserva = servicioReserva.getAllFechasEnReserva(item);

                for (Date fechaEnReserva: lstEnReserva) {

                    if (item.getEstadoReserva() == Enumerator.ReservaEstado.PRE_RESERVA && (Utils.isEqualsDate(dayView.getDate(), fechaEnReserva) )) {
                        dayView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.prereserva));
                        dayView.setClickable(false);
                        dayView.setEnabled(false);
                    }

                    if (item.getEstadoReserva() == Enumerator.ReservaEstado.RESERVA_APROBADA && (Utils.isEqualsDate(dayView.getDate(), fechaEnReserva) )) {
                        dayView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.reservada));
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
                if (error.getStatusCode() >= 500) {
                    mostrarMensaje(error.getMessage());
                }
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
                if (error.getStatusCode() >= 500) {
                    mostrarMensaje(error.getMessage());
                }
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
                Snackbar.make(mLyCanasta, error.getMessage(), Snackbar.LENGTH_INDEFINITE)
                        .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green) )
                        .setAction("REINTENTAR", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loadParametros();
                            }
                        })
                        .show();
            }
        });
    }

    public static final int REQUEST_CODE_USUARIO = 10;
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
                case R.id.btnDatosUsuario:
                    Intent i = new Intent( ReservaActivity.this, UsuarioActivity.class);
                    i.putExtra(UsuarioActivity.ORIGIN, UsuarioActivity.ORIGEN_RESERVA);
                    startActivityForResult(i,REQUEST_CODE_USUARIO);
                    break;
                case R.id.btnCerrar:
                    finish();
                    break;
                case R.id.btnReservar:
                    reservar();
                    break;
                case R.id.lyPoliticas:
                    Intent j = new Intent(ReservaActivity.this,WebViewActivity.class);
                    String url = Config.ServerParques + "PaginaWeb/terminos.aspx?IDParque="+ mParque.getIDParque();
                    j.putExtra(WebViewActivity.URL,url);
                    j.putExtra(WebViewActivity.TITULO,getString(R.string.title_terminos));
                    startActivity(j);
                    break;
            }
        }
    };

    private void prereserva(){

        if(validar()){

            mServicioReserva.setIDServiciosParque(mServicioParque.getIDServiciosParque());
            mostrarProgressDialog();
            Reservas reservas = new Reservas();
            reservas.validarDisponibilidad(mServicioReserva,iReserva);
        }
    }

    private boolean  validar(){
        boolean res = true;
        Calendar fechaActual = Calendar.getInstance();
        int diaDeSemana = fechaActual.get(Calendar.DAY_OF_WEEK) - 1;
        if ( !( diaDeSemana >=  Utils.convertInt(mapParametros.get(ParametroReserva.reservasDiaSemanaDesde)) && diaDeSemana <=  Utils.convertInt(mapParametros.get(ParametroReserva.reservasDiaSemanaHasta)))){
            mostrarMensajeDialog(mapParametros.get(ParametroReserva.reservasDiaMensaje));
            return false;
        }

        if(mTxtFechaLlegada.getText().toString().isEmpty() || mTxtFechaSalida.getText().toString().isEmpty() ) {
            mostrarMensajeDialog("Seleccione fecha de llegada y fecha de salida");
            return false;
        }

        Calendar fechaLlegada = Utils.convertToCalendar(mServicioReserva.getFechaInicialReserva());
        Calendar fechaSalida = Utils.convertToCalendar(mServicioReserva.getFechaFinalReserva());

        if (!fechaLlegada.before(fechaSalida) || Utils.isEqualsDate(fechaLlegada,fechaSalida) ){
            mostrarMensajeDialog("La fecha de salida debe ser mayor  a la de llegada");
            return false;
        }

        int numDiasReserva = Utils.difDaysDates(fechaLlegada,fechaSalida) ;
        if ( numDiasReserva > Utils.convertInt(mapParametros.get(ParametroReserva.maximoNroDiasAReservar)) ){
            mostrarMensajeDialog(getString(R.string.maximo_dias) +" "+ mapParametros.get(ParametroReserva.maximoNroDiasAReservar) +" " + getString(R.string.dias) );
            return false;
        }

        int numDiasInicioReserva = Utils.difDaysDates(fechaActual,fechaLlegada) -1 ;

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

        Calendar fechaLlegada = Utils.convertToCalendar(mServicioReserva.getFechaInicialReserva());
        Calendar fechaSalida = Utils.convertToCalendar(mServicioReserva.getFechaFinalReserva());

        int numDiasReserva = Utils.difDaysDates(fechaLlegada ,fechaSalida);

        //validar si es funcinario car
        long precio = 0;
        if (mUsuario.isFuncionarioCar())
            precio = mServicioParque.getPrecioCar();
        else
            precio = mServicioParque.getPrecioServicio();


        mLblServicio.setText(mServicioParque.getNombreServicio());
        mLblPrecio.setText("Precio: " + Utils.formatoMoney(precio));
        mLblImpuesto.setText("Impuesto: " + String.valueOf(mServicioParque.getImpuestoServicio() ));
        mLblFechaDesde.setText("Desde: " + Utils.toStringLargeFromDate(mServicioReserva.getFechaInicialReserva()));
        mLblFechaHasta.setText("Hasta: " + Utils.toStringLargeFromDate(mServicioReserva.getFechaFinalReserva()));
        mLblNroNoches.setText("Nro noches: " + String.valueOf(numDiasReserva));

        long subTotal = precio * numDiasReserva;
        mLblSubtotal.setText("Sub total: " + Utils.formatoMoney(subTotal));



        if (mUsuario.getIdUsuario() == 0 || ( mUsuario.getIdUsuario() > 0 && mUsuario.isFuncionarioCar() && !mUsuario.isLoginSIDCAR())){
            mLyDatosUsuario.setVisibility(View.VISIBLE);
            mBtnReserva.setVisibility(View.GONE);
        }else{
            mLyDatosUsuario.setVisibility(View.GONE);
            mBtnReserva.setVisibility(View.VISIBLE);
        }

        mServicioReserva.setCantidadReserva(numDiasReserva);
        mServicioReserva.setPrecioReserva(precio);
        mServicioReserva.setTotalValorReserva(subTotal);
        mServicioReserva.setImpuestoReserva(mServicioParque.getImpuestoServicio());

    }


    private void reservar(){
        if (mCheAceptoPoliticas.isChecked()) {
            Reservas reservas = new Reservas();
            mServicioReserva.setIDUsuario(mUsuario.getIdUsuario());
            mServicioReserva.setIDParque(mServicioParque.getIDParque());
            mServicioReserva.setEstadoReserva(0);
            mServicioReserva.setOrigenReserva("ANDROID");

            mostrarProgressDialog();
            reservas.insert(mServicioReserva, iReserva);
        }else{
            mCheAceptoPoliticas.setError("Campo requerido");
        }
    }
    private void mostrarProgressDialog(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Cargando..");
        //progressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    private String reservaRealizadaHTML(long id){
        return   getString(R.string.reserva_creada) + " "+ " <b>" + id +"</b>" +" "+ getString(R.string.reserva_creada2) ;
    }

    IReserva iReserva = new IReserva() {
        @Override
        public void onSuccess(ServicioReserva servicioReserva) {
            if (mProgressDialog != null) mProgressDialog.dismiss();
            mLyCanasta.setVisibility(View.GONE);
            mLyRespuestaOk.setVisibility(View.VISIBLE);
            mLblRespuestaOk.setText(Html.fromHtml(reservaRealizadaHTML(servicioReserva.getIDReserva())));
            mLblNroCuenta.setText(mParque.getDetalleCuenta().trim());
        }

        @Override
        public void onSuccess(boolean res) {
            if (mProgressDialog != null) mProgressDialog.dismiss();
            if (res){
                llenarCanasta();
                mBtnPrereserva.setVisibility(View.GONE);

            }else{
                mostrarMensajeDialog(getString(R.string.reserva_no_disponible));
            }
        }

        @Override
        public void onError(ErrorApi error) {
           if (mProgressDialog != null) mProgressDialog.dismiss();
            mostrarMensajeDialog(error.getMessage() + " error "+error.getCode() );
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_USUARIO && resultCode == Activity.RESULT_OK){
            mUsuario = new Usuarios().leer();
            llenarCanasta();
        }
    }
}
