package car.gov.co.carserviciociudadano.bicicar.activities;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.SexaDecimalCoordinate;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.Utils.Validation;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Colegios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Cuencas;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Eventos;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.TiposEvento;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Colegio;
import car.gov.co.carserviciociudadano.bicicar.model.Cuenca;
import car.gov.co.carserviciociudadano.bicicar.model.Evento;
import car.gov.co.carserviciociudadano.bicicar.model.TipoEvento;
import car.gov.co.carserviciociudadano.bicicar.presenter.BeneficiarioPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.CuencasPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.EventoPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewBeneficiario;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewCuenca;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewEvento;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewTipoEvento;
import car.gov.co.carserviciociudadano.bicicar.presenter.TiposEventoPresenter;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.denunciaambiental.dataacces.Elevation;
import car.gov.co.carserviciociudadano.bicicar.model.Lugar;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.ElevationPresenter;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.IViewElevation;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewLugares;
import car.gov.co.carserviciociudadano.bicicar.presenter.LugaresPresenter;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import okhttp3.internal.Util;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

public class EventoActivity extends BaseActivity implements IViewEvento, IViewTipoEvento, IViewBeneficiario, IViewLugares,  AdapterView.OnItemSelectedListener, IViewCuenca , IViewElevation {

    @BindView(R.id.pbTipoEvento)  ProgressBar pbTipoEvento;
    @BindView(R.id.spiTipoEvento)   Spinner spiTipoEvento;

    @BindView(R.id.spiColegio)   Spinner spiColegio;
    @BindView(R.id.txtFechaInicio)  EditText txtFechaInicio;
    @BindView(R.id.txtFechaFin) EditText txtFechaFin;
    @BindView(R.id.calendarioFechaInicio) CustomCalendarView calendarioFechaInicio;
    @BindView(R.id.calendarioFechaFin) CustomCalendarView calendarioFechaFin;
    @BindView(R.id.btnGuardar) Button btnGuardar;
    @BindView(R.id.txtDescripcion)  EditText txtDescripcion;
    @BindView(R.id.txtNombre)  EditText txtNombre;
    @BindView(R.id.lyNombre)  TextInputLayout lyNombre;
    @BindView(R.id.txtDistanciaKM)  EditText txtDistanciaKM;
    @BindView(R.id.lyDistanciaKM)  TextInputLayout lyDistanciaKM;
    @BindView(R.id.txtDuracionMinutos)  EditText txtDuracionMinutos;
    @BindView(R.id.lyDuracionMinutos)  TextInputLayout lyDuracionMinutos;
    @BindView(R.id.lyPrincipal)  View lyPrincipal;
    @BindView(R.id.txtColegio)  EditText txtColegio;
    @BindView(R.id.spiMunicipio)    Spinner spiMunicipio;
    @BindView(R.id.spiVereda)    Spinner spiVereda;
    @BindView(R.id.pbMunicipio)   ProgressBar pbMunicipio;
    @BindView(R.id.pbVereda)   ProgressBar pbVereda;
    @BindView(R.id.spiCuenca)    Spinner spiCuenca;
    @BindView(R.id.pbCuenca)   ProgressBar pbCuenca;
    @BindView(R.id.txtAltitud)   EditText txtAltitud;
    @BindView(R.id.txtLatitud)   EditText txtLatitud;
    @BindView(R.id.txtLongitud)   EditText txtLongitud;
    @BindView(R.id.txtNorte)   EditText txtNorte;
    @BindView(R.id.txtEste)   EditText txtEste;
    @BindView(R.id.lyAltitud)  TextInputLayout lyAltitud;
    @BindView(R.id.lyLatitude)  TextInputLayout lyLatitude;
    @BindView(R.id.lyLongitud)  TextInputLayout lyLongitud;
    @BindView(R.id.lyNorte)  TextInputLayout lyNorte;
    @BindView(R.id.lyEste)  TextInputLayout lyEste;
    @BindView(R.id.txtPredio)  EditText txtPredio;
    @BindView(R.id.btnUbicacionEventoMapa)   Button btnUbicacionEventoMapa;
    @BindView(R.id.btnUbicacionEvento)   Button btnUbicacionEvento;
    @BindView(R.id.pbUbicacion)   ProgressBar pbUbicacion;
    @BindView(R.id.lyUbicacion)   View lyUbicacion;
    @BindView(R.id.txtHoraInicio)   EditText txtHoraInicio;
    @BindView(R.id.txtHoraFin)   EditText txtHoraFin;
    @BindView(R.id.lyHoraInicio)  TextInputLayout lyHoraInicio;
    @BindView(R.id.lyHoraFin)  TextInputLayout lyHoraFin;
    @BindView(R.id.lblPresicion) TextView lblPresicion;

    TiposEventoPresenter tiposEventoPresenter;
    EventoPresenter eventoPresenter;
    BeneficiarioPresenter beneficiarioPresenter;
    List<TipoEvento>  mLstTiposEvento = new ArrayList<>();
    ArrayAdapter<TipoEvento> mAdapterTiposEvento;
    List<Lugar> mLstMunicipios = new ArrayList<>();
    ArrayAdapter<Lugar> adapterMunicipios;
    List<Lugar> mLstVeredas = new ArrayList<>();
    ArrayAdapter<Lugar> adapterVeredas;
    LugaresPresenter mLugaresPresenter;
    List<Cuenca> mLstCuencas = new ArrayList<>();
    ArrayAdapter<Cuenca> adapterCuenca;
    CuencasPresenter mCuencasPresenter;
    ElevationPresenter mElevationPresenter;
    Evento mEvento;
    Beneficiario mBeneficiarioLogin;
    int mIdEvento;
    private static final int REQUEST_COLEGIOS = 100;
    private static final int REQUEST_UBICACION = 101;
    private static final String ID_CUNDINAMARCA = "25";
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    private static final String AM = " a.m.";
    private static final String PM = " p.m.";


    private LocationManager mLocationManager;
    boolean isRunning = false;
    private float mAccuracy = Float.MAX_VALUE;
    boolean gps_enabled=false;
    boolean network_enabled=false;
    private  boolean isRunningElevation = false;
    public  final static int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Bundle b =  getIntent().getExtras();
        if (b != null){
            mIdEvento = b.getInt(Evento.ID_EVENTO, 0);
            mEvento = new Eventos().read(mIdEvento);
        }else{
            mEvento = new Evento();
        }

        mBeneficiarioLogin  = Beneficiarios.readBeneficio();
        mLstTiposEvento.add(new TipoEvento ("Tipos evento"));
        mAdapterTiposEvento = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstTiposEvento);
        mAdapterTiposEvento.setDropDownViewResource( R.layout.simple_spinner_dropdown_item);
        spiTipoEvento.setAdapter(mAdapterTiposEvento);

        mLstMunicipios.add(new Lugar("","Municipio"));
        adapterMunicipios = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstMunicipios);
        adapterMunicipios.setDropDownViewResource( R.layout.simple_spinner_dropdown_item);
        spiMunicipio.setAdapter(adapterMunicipios);

        mLstVeredas.add(new Lugar("","Vereda"));
        adapterVeredas = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstVeredas);
        adapterVeredas.setDropDownViewResource( R.layout.simple_spinner_dropdown_item);
        spiVereda.setAdapter(adapterVeredas);

        mLstCuencas.add(new Cuenca(0,"Cuenca"));
        adapterCuenca = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstCuencas);
        adapterCuenca.setDropDownViewResource( R.layout.simple_spinner_dropdown_item);
        spiCuenca.setAdapter(adapterCuenca);

        mCuencasPresenter = new CuencasPresenter(this);
        mLugaresPresenter = new LugaresPresenter(this);
        tiposEventoPresenter = new TiposEventoPresenter(this);
        eventoPresenter = new EventoPresenter(this);
        beneficiarioPresenter = new BeneficiarioPresenter(this);
        mElevationPresenter = new ElevationPresenter(this);

        obtenerDatos();

        txtColegio.setOnClickListener(onClickListener);
        btnGuardar.setOnClickListener(onClickListener);
        btnUbicacionEventoMapa.setOnClickListener(onClickListener);
        btnUbicacionEvento.setOnClickListener(onClickListener);
        txtFechaInicio.setOnClickListener(onClickListener);
        txtFechaFin.setOnClickListener(onClickListener);
        spiMunicipio.setOnItemSelectedListener(this);
        txtHoraInicio.setOnClickListener(onClickListener);
        txtHoraFin.setOnClickListener(onClickListener);

        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        calendarioFechaInicio.refreshCalendar(currentCalendar);
        calendarioFechaFin.refreshCalendar(currentCalendar);

        calendarioFechaInicio.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                txtFechaInicio.setText(Utils.toStringFromDate(date));
                calendarioFechaInicio.setVisibility(View.GONE);
                mEvento.FInicio = date;
            }

            @Override
            public void onMonthChanged(Date date) {

            }
        });

        calendarioFechaFin.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                txtFechaFin.setText(Utils.toStringFromDate(date));
                calendarioFechaFin.setVisibility(View.GONE);
                mEvento.FFin = date;
            }

            @Override
            public void onMonthChanged(Date date) {

            }
        });

        spiTipoEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TipoEvento tipoEvento = mLstTiposEvento.get(i);
                if (tipoEvento != null && tipoEvento.Recorrido){
                    lyDistanciaKM.setVisibility(View.VISIBLE);
                    lyDuracionMinutos.setVisibility(View.VISIBLE);
                    lyUbicacion.setVisibility(View.GONE);
                }else{
                    lyDistanciaKM.setVisibility(View.GONE);
                    lyDuracionMinutos.setVisibility(View.GONE);
                    lyUbicacion.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (mIdEvento > 0) {
            txtNombre.setText(mEvento.Nombre);
            txtDescripcion.setText(mEvento.Descripcion);
            txtDistanciaKM.setText(String.valueOf(mEvento.DistanciaKm));
            txtDuracionMinutos.setText(String.valueOf(mEvento.DuracionMinutos));
            calendarioFechaInicio.refreshCalendar(Utils.convertToCalendar(mEvento.FInicio));
            calendarioFechaFin.refreshCalendar(Utils.convertToCalendar(mEvento.FFin));
            txtFechaInicio.setText(Utils.toStringFromDate(mEvento.FInicio));
            txtFechaFin.setText(Utils.toStringFromDate(mEvento.FFin));
            spiTipoEvento.setEnabled(false);

            txtHoraInicio.setText(mEvento.HoraInicio);
            txtHoraFin.setText(mEvento.HoraFin);
            txtPredio.setText(mEvento.Predio);
            txtLatitud.setText(String.valueOf(mEvento.Latitud));
            txtLongitud.setText(String.valueOf(mEvento.Longitud));
            txtNorte.setText(String.valueOf(mEvento.Norte));
            txtEste.setText(String.valueOf(mEvento.Este));
            txtAltitud.setText(String.valueOf(mEvento.Altitud));


            Colegio colegio = new Colegios().read(mEvento.IDColegio);
            if (colegio != null) {
                txtColegio.setText("(" + mEvento.IDColegio + ") " + colegio.Nombre);
            }
        }
    }

    private  void obtenerDatos(){
        if (Utils.isOnline(this)) {
            pbTipoEvento.setVisibility(View.VISIBLE);
            tiposEventoPresenter.list();

            pbMunicipio.setVisibility(View.VISIBLE);
            mLugaresPresenter.obtenerMunicipios();

            pbCuenca.setVisibility(View.VISIBLE);
            mCuencasPresenter.getCuencas();
        }
    }

    private void obtenerHoraInicio(){

        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minutos = calendar.get(Calendar.MINUTE);

        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = AM;
                } else {
                    AM_PM = PM;
                }
                //Muestro la hora con el formato deseado
                txtHoraInicio.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + AM_PM);
                mEvento.HoraInicio = hourOfDay + DOS_PUNTOS + minute ;

            }

        },  hora,  minutos, false);

        recogerHora.show();
    }

    private void obtenerHoraFin(){

        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minutos = calendar.get(Calendar.MINUTE);

        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }

                txtHoraFin.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                mEvento.HoraFin = hourOfDay + DOS_PUNTOS + minute;

            }

        },  hora,  minutos, false);

        recogerHora.show();
    }


    @Override
    public void onSuccessTipoEvento(List<TipoEvento> lstTiposEvento){
        pbTipoEvento.setVisibility(View.GONE);
        mAdapterTiposEvento.clear();
        mAdapterTiposEvento.addAll(lstTiposEvento);
        mAdapterTiposEvento.notifyDataSetChanged();

        if (mIdEvento > 0) {
            spiTipoEvento.setSelection(getTipoEventoPosition(lstTiposEvento));
        }
    }
    public int getTipoEventoPosition(List<TipoEvento> lstTiposEvento){
        int i = 0;
        for (TipoEvento item : lstTiposEvento) {
            if (item.IDTipoEvento == mEvento.IDTipoEvento)
               return i;
            i++;
        }
        return 0;
    }

    public int getMunicipiosPosition(String idMunicipio){
        int i = 0;
        for (Lugar item : mLstMunicipios) {
            if (item.getIDLugar().equals(idMunicipio))
                return i;
            i++;
        }
        return 0;
    }

    public int getCuencaPosition(int idCuenca){
        int i = 0;
        for (Cuenca item : mLstCuencas) {
            if (item.IDCuenca ==  idCuenca)
                return i;
            i++;
        }
        return 0;
    }

    public int getVeredaPosition(String idVereda){
        int i = 0;
        for (Lugar item : mLstVeredas) {
            if (item.getIDLugar().equals(idVereda))
                return i;
            i++;
        }
        return 0;
    }

    @Override
    public void onPause(){
        super.onPause();
        AppCar.VolleyQueue().cancelAll(Colegios.TAG);
        AppCar.VolleyQueue().cancelAll(Beneficiarios.TAG);


    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        AppCar.VolleyQueue().cancelAll(TiposEvento.TAG);
        AppCar.VolleyQueue().cancelAll(Beneficiarios.TAG);
        AppCar.VolleyQueue().cancelAll(Cuencas.TAG);
        AppCar.VolleyQueue().cancelAll(Elevation.TAG);
        cancel();
    }

    @Override
    public void onSuccess(Evento evento){
        ocultarProgressDialog();
        if (eventoPresenter.insert(evento)) {
            mostrarMensajeEventoCreado();
        }else{
            mostrarMensajeDialog("Error al guardar evento en el tel??fono");
        }
    }

    @Override
    public void onSuccessModificar(Evento evento) {
        ocultarProgressDialog();
        if (eventoPresenter.update(evento)) {
            mostrarMensajeEventoCreado();
        }else{
            mostrarMensajeDialog("Error al guardar evento en el tel??fono");
        }
    }

    @Override
    public void onSuccessEliminar(Evento evento) {

    }

    @Override
    public void onSuccessPublicoActual(Evento evento) {

    }

    @Override
    public void onErrorEvento(ErrorApi error) {
        ocultarProgressDialog();
        mostrarMensajeDialog(error.getMessage());
    }

    @Override
    public void onErrorPublicoActual(ErrorApi error) {

    }

    @Override
    public void onErrorTiposEvento(ErrorApi error) {
        mostrarErrorDatos(error);
    }
    @Override
    public void onSuccess(Beneficiario beneficiario){

    }
    @Override
    public void onSuccess(List<Beneficiario> lstBeneficiarios){
        ocultarProgressDialog();
        guardar();
    }
    @Override
    public void onErrorListarItems(ErrorApi errorApi){
        ocultarProgressDialog();
        if(errorApi.getStatusCode() == 404){
            guardar();
        }else {
            mostrarMensajeDialog("Error al obtener estudiantes " + errorApi.getMessage());
        }

    }
    @Override
    public void onSuccessRecordarClave(String mensaje){}
    @Override
    public void onErrorRecordarClave(ErrorApi error){}
    @Override
    public void onError(ErrorApi error) {
        ocultarProgressDialog();
        mostrarMensajeDialog(error.getMessage());
    }

    /// Iview lugares

    @Override
    public void onSuccessMunicipios(List<Lugar> lstMunicipios) {
        adapterMunicipios.clear();
        adapterMunicipios.addAll(lstMunicipios);
        adapterMunicipios.notifyDataSetChanged();
        spiMunicipio.setSelection(0);
        pbMunicipio.setVisibility(View.GONE);

        if (mIdEvento > 0){
            spiMunicipio.setSelection(getMunicipiosPosition(mEvento.IDMunicipio));
        }
    }

    @Override
    public void onSuccessVeredas(List<Lugar> lstVeredas) {
        adapterVeredas.clear();
        adapterVeredas.addAll(lstVeredas);
        adapterVeredas.notifyDataSetChanged();
        spiVereda.setSelection(0);
        pbVereda.setVisibility(View.GONE);
        if (mIdEvento > 0){
            spiVereda.setSelection(getVeredaPosition(mEvento.IDVereda));
        }
    }


    @Override
    public void onErrorMunicipios(ErrorApi errorApi) {
        errorApi.setMessage(getResources().getString(R.string.error_load_municipios));
        mostrarErrorDatos(errorApi);
    }

    @Override
    public void onErrorVeredas(ErrorApi errorApi) {
        pbVereda.setVisibility(View.GONE);
        if (errorApi.getStatusCode() >= 500 ){
            Snackbar.make(lyPrincipal,getResources().getString(R.string.error_load_veredas), Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green) )
                    .setAction("REINTENTAR", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Lugar municipio = (Lugar) spiMunicipio.getSelectedItem();
                            if (municipio!= null)
                                mLugaresPresenter.obtenerVeredas(municipio.getIDLugar());
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spiMunicipio:
                Lugar municipio = (Lugar) spiMunicipio.getSelectedItem();
                if(municipio!=null && !municipio.getIDLugar().equals("")) {
                    pbVereda.setVisibility(View.VISIBLE);
                    mLugaresPresenter.obtenerVeredas(municipio.getIDLugar());
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public int getColegiosPosition(List<Colegio> lstColegios){
        int i = 0;
        for (Colegio item : lstColegios) {
            if (item.IDColegio == mEvento.IDColegio)
                return i;
            i++;
        }
        return i;
    }


    private  void mostrarErrorDatos(ErrorApi error){
        AppCar.VolleyQueue().cancelAll(TiposEvento.TAG);
        AppCar.VolleyQueue().cancelAll(Cuencas.TAG);

        pbCuenca.setVisibility(View.GONE);
        pbMunicipio.setVisibility(View.GONE);
        pbTipoEvento.setVisibility(View.GONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(EventoActivity.this);
        builder.setMessage(error.getMessage());
        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                obtenerDatos();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.txtFechaInicio:
                    calendarioFechaInicio.setVisibility(View.VISIBLE);
                    calendarioFechaFin.setVisibility(View.GONE);
                    ocultarTeclado(lyPrincipal);
                    break;
                case R.id.txtFechaFin:
                    calendarioFechaInicio.setVisibility(View.GONE);
                    calendarioFechaFin.setVisibility(View.VISIBLE);
                    ocultarTeclado(lyPrincipal);
                    break;
                case R.id.btnGuardar:
                    preGuaradar();
                    break;
                case R.id.txtColegio:
                    Intent i = new Intent(EventoActivity.this, ColegiosActivity.class);
                    i.putExtra(ColegiosActivity.IS_SELECTOR, true);
                    startActivityForResult(i , REQUEST_COLEGIOS);
                    break;
                case R.id.btnUbicacionEvento:
                    localizacion();
                    break;
                case R.id.btnUbicacionEventoMapa:
                    cancel();
                    Intent j = new Intent(EventoActivity.this , UbicacionBeneficiarioActivity.class);
                    j.putExtra(UbicacionBeneficiarioActivity.RETORNAR_UBICACION , true);
                    startActivityForResult(j , REQUEST_UBICACION);
                    break;
                case R.id.txtHoraInicio:
                    obtenerHoraInicio();
                    break;
                case R.id.txtHoraFin:
                    obtenerHoraFin();
                    break;
            }
        }
    };

    private  void obtenerBeneficiarios(){
        beneficiarioPresenter.list(mEvento.IDColegio);
        mostrarProgressDialog("Descargando estudiantes");
    }

    private boolean validar(){
        boolean res = true;

        if (Validation.IsEmpty(txtFechaFin)) res = false;
        if (Validation.IsEmpty(txtFechaInicio)) res = false;
        if (mIdEvento == 0 || (mIdEvento > 0 && mLstTiposEvento.size() > 1) ) { // no se valida en edicion si no hay internet y no pudo obtener tipo
            if (Validation.IsEmpty(spiTipoEvento)) res = false;
        }
        if (Validation.IsEmpty(txtNombre, lyNombre)) res = false;
        if (Validation.IsEmpty(txtDescripcion)) res = false;
        if (Validation.IsEmpty(txtColegio)) res = false;

        if (mEvento.IDColegio <= 0){
            mostrarMensajeDialog("Seleccione un colegio");
            return false;
        }

        TipoEvento tipoEvento = (TipoEvento) spiTipoEvento.getSelectedItem();

        if (tipoEvento != null && tipoEvento.Recorrido) {
            if (Validation.IsEmpty(txtDistanciaKM, lyDistanciaKM)) res = false;
            if (Validation.IsEmpty(txtDuracionMinutos, lyDuracionMinutos)) res = false;

        }
        if (Validation.IsEmpty(txtHoraInicio, lyHoraInicio)) res = false;
        if (Validation.IsEmpty(txtHoraFin, lyHoraFin)) res = false;
        if (mIdEvento == 0 || (mIdEvento > 0 && mLstMunicipios.size() > 1) ) {
            if (Validation.IsEmpty(spiMunicipio)) res = false;
        }

        if (mEvento.HoraInicio == null){
            mostrarMensajeDialog("Seleccione hora de inicio");
            return false;
        }
        if (mEvento.HoraFin == null){
            mostrarMensajeDialog("Seleccione hora de finalizaci??n");
            return false;
        }

        Calendar fechaActual = Calendar.getInstance();

        Calendar fechaInicio = Utils.convertToCalendar(mEvento.FInicio);
        Calendar fechaFin = Utils.convertToCalendar(mEvento.FFin);

        if ( !fechaActual.before(fechaInicio) && !Utils.isEqualsDate(fechaActual, fechaInicio)) {
            mostrarMensajeDialog("La fecha de inicio debe ser mayor o igual a la fecha actual");
            return false;
        }

        if (!fechaInicio.before(fechaFin) && !Utils.isEqualsDate(fechaInicio,fechaFin) ){
            mostrarMensajeDialog("La fecha inicio debe ser mayor  a la de fin");
            return false;
        }

        if (Utils.isEqualsDate(fechaInicio, fechaFin)) {
            int horaInicio = Utils.convertInt(txtHoraInicio.getText().toString().replace(DOS_PUNTOS,"").replace(AM,"").replace(PM,"").trim());
            int horaFin = Utils.convertInt(txtHoraFin.getText().toString().replace(DOS_PUNTOS,"").replace(AM,"").replace(PM,"").trim());
            if (horaFin <= horaInicio){
                mostrarMensajeDialog("La hora de inicio debe ser menor a la hora final");
                return false;
            }
        }

        return res;
    }

    private void preGuaradar(){
        cancel();
        if (validar()) {
            if (mIdEvento == 0 || (mLstMunicipios.size() > 1 && mLstCuencas.size() > 1)) {
                obtenerBeneficiarios();
            } else {
                if (eventoPresenter.update(mEvento)) {
                    mostrarMensajeEventoCreado();
                } else {
                    mostrarMensajeDialog("Error al guardar evento en el tel??fono");
                }
            }
        }
    }
    private void guardar(){
        TipoEvento tipoEvento =  (TipoEvento) spiTipoEvento.getSelectedItem();

        mEvento.IDTipoEvento =  tipoEvento.IDTipoEvento; // mAdapterTiposEvento.getItem(spiTipoEvento.getSelectedItemPosition()).IDTipoEvento;
        mEvento.Nombre = txtNombre.getText().toString();
        mEvento.Descripcion = txtDescripcion.getText().toString();
        mEvento.UsuarioCreacion = "Android";
        mEvento.IDResponsable = mBeneficiarioLogin.IDBeneficiario;
        mEvento.Participantes = 0;
        if (tipoEvento != null && tipoEvento.Recorrido) {
            mEvento.DistanciaKm = Utils.convertFloat(txtDistanciaKM.getText().toString());
            mEvento.DuracionMinutos = Utils.convertFloat(txtDuracionMinutos.getText().toString());
        }

        if (mLstMunicipios.size() > 1) {
            Lugar municipio = (Lugar) spiMunicipio.getSelectedItem();
            if (municipio != null) {
                mEvento.IDMunicipio = municipio.getIDLugar();
            }
        }

        if (mLstVeredas.size() > 1) {

            Lugar vereda = (Lugar) spiVereda.getSelectedItem();
            if (vereda != null) {
                mEvento.IDVereda = vereda.getIDLugar();
            }
        }

        if (mLstCuencas.size() > 1) {
            Cuenca cuenca = (Cuenca) spiCuenca.getSelectedItem();
            if (cuenca != null) {
                mEvento.IDCuenca = cuenca.IDCuenca;
            }
        }

        mEvento.Predio = txtPredio.getText().toString();

        mEvento.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR;

        String[] horaInicio = mEvento.HoraInicio.split(DOS_PUNTOS);
        if (horaInicio.length > 0) {
            Calendar fechaInicio = Utils.convertToCalendar(mEvento.FInicio);
            fechaInicio.set(Calendar.HOUR_OF_DAY, Utils.convertInt(horaInicio[0]));
            if (horaInicio.length > 1)
                fechaInicio.set(Calendar.MINUTE, Utils.convertInt(horaInicio[1]));

            fechaInicio.set(Calendar.SECOND, 0);
            mEvento.FInicio  = fechaInicio.getTime();
        }

        String[] horaFin = mEvento.HoraFin.split(DOS_PUNTOS);
        if (horaInicio.length > 0) {
            Calendar fechaFin = Utils.convertToCalendar(mEvento.FFin);
            fechaFin.set(Calendar.HOUR_OF_DAY, Utils.convertInt(horaFin[0]));
            if (horaInicio.length > 1)
                fechaFin.set(Calendar.MINUTE, Utils.convertInt(horaFin[1]));

            fechaFin.set(Calendar.SECOND, 0);
            mEvento.FFin  = fechaFin.getTime();
        }

        if (mIdEvento > 0){
            eventoPresenter.modificar(mEvento);
            mostrarProgressDialog("Modificando evento");
        }else {
            eventoPresenter.publicar(mEvento);
            mostrarProgressDialog("Creando evento");
        }

    }

    private void mostrarMensajeEventoCreado(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EventoActivity.this);
        builder.setMessage(mIdEvento > 0 ? "El evento fue modificado correctamente" : "El evento fue creado correctamente");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(Activity.RESULT_OK, getIntent());
                finish();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_COLEGIOS){
                mEvento.IDColegio = data.getIntExtra(Colegio.ID_COLEGIO, 0);
                txtColegio.setText("(" + mEvento.IDColegio + ") " + data.getStringExtra(Colegio.NOMBRE));
                Colegio colegio = new Colegios().read(mEvento.IDColegio);
                spiMunicipio.setSelection(getMunicipiosPosition(colegio.IDMunicipio));
            }else if (requestCode == REQUEST_UBICACION){

                mAccuracy = Float.MAX_VALUE;
                setResult(data.getDoubleExtra(UbicacionBeneficiarioActivity.LATITUDE, 0), data.getDoubleExtra(UbicacionBeneficiarioActivity.LONGITUDE, 0), 0,0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0   && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    localizacion();
                } else {
                    mostrarMensaje("Permiso denegado para obtener ubicacion");
                }
                return;
            }
        }
    }

    @Override
    public void onSuccessCuencas(List<Cuenca> lstCuencas) {
        adapterCuenca.clear();
        adapterCuenca.addAll(lstCuencas);
        adapterCuenca.notifyDataSetChanged();
        spiCuenca.setSelection(0);
        pbCuenca.setVisibility(View.GONE);
        if (mIdEvento > 0){
            spiCuenca.setSelection(getCuencaPosition(mEvento.IDCuenca));
        }
    }

    @Override
    public void onErrorCuencas(ErrorApi errorApi) {
        errorApi.setMessage(getResources().getString(R.string.error_load_cuencas));
        mostrarErrorDatos(errorApi);
    }

    @Override
    public void onSuccessElevation(double elevation) {
        isRunningElevation = false;
        ocultarProgressDialog();
        mEvento.Altitud = Utils.round(2 ,elevation);
        txtAltitud.setText(String.valueOf( mEvento.Altitud ));
    }

    @Override
    public void onErrrorElevation(int statusCode) {
        ocultarProgressDialog();
        isRunningElevation = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(EventoActivity.this);
        builder.setMessage(getResources().getString(R.string.error_load_elevation));
        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mElevationPresenter.getElevation(mEvento.Latitud,mEvento.Longitud);
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();

    }


    /**
     * Obtener ubicacion sin internet
     */

    private void localizacion(){
        // if (mTipo == Enumeradores.Tipo.VisitProperty) return;

        if (isRunning) {
            cancel();
        } else {
            mAccuracy = Float.MAX_VALUE;
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            try {
                gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }
            try {
                network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
            }

            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION ,  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                        MY_PERMISSIONS_ACCESS_FINE_LOCATION );

            }else {

                if (gps_enabled == false) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }

                if (gps_enabled) {
                    btnUbicacionEvento.setText("Detener");
                    pbUbicacion.setVisibility(View.VISIBLE);
                    isRunning = true;
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListenerGps);
                    Log.d(" gps enabled", "gps ejecutadi");
                }
                if (network_enabled) {
                    btnUbicacionEvento.setText("Detener");
                    pbUbicacion.setVisibility(View.VISIBLE);
                    isRunning = true;
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListenerNetwork);
                    Log.d("network enable", "ejecutado");
                }
            }
        }
    }
    public void cancel()
    {
        if (isRunning) {
            isRunning = false;
            isRunningElevation = false;
            btnUbicacionEvento.setText(getString(R.string.ubicacion));
            pbUbicacion.setVisibility(View.GONE);

            if (mLocationManager != null) {
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("permiso", "Permiso denegado");
                    return;
                }

                mLocationManager.removeUpdates(locationListenerGps);
                mLocationManager.removeUpdates(locationListenerNetwork);

            }
            AppCar.VolleyQueue().cancelAll(Elevation.TAG);
        }
    }

    private void setResult(double latitude, double longitude, double altitud, float accuracy){
        if (accuracy < mAccuracy ) {

            mEvento.Latitud = Utils.round(7 , latitude);
            mEvento.Longitud = Utils.round(7 ,longitude);
            txtLatitud.setText(String.valueOf(mEvento.Latitud));
            txtLongitud.setText(String.valueOf(mEvento.Longitud));
            SexaDecimalCoordinate sexaDecimalCoordinate = new SexaDecimalCoordinate(mEvento.Latitud, mEvento.Longitud);
            sexaDecimalCoordinate.ConvertToFlatCoordinate();
            mEvento.Norte = String.valueOf(Utils.round(0, sexaDecimalCoordinate.get_coorPlanaNorteFinal())).replace(".0","");
            mEvento.Este = String.valueOf(Utils.round(0, sexaDecimalCoordinate.get_coorPlanaEsteFinal())).replace(".0","");
            txtNorte.setText(mEvento.Norte);
            txtEste.setText(mEvento.Este);

            lblPresicion.setText("Presici??n" + String.valueOf(accuracy));
            mAccuracy = accuracy;

            if (altitud != 0) {
                txtAltitud.setText(String.valueOf(altitud));
            }
        }

        //consulta altitud en api google elevation
        if (isRunningElevation == false && txtAltitud.getText().length() == 0) {
            if (altitud != 0){
                txtAltitud.setText(String.valueOf(altitud));
            }
            isRunningElevation = true;
            mostrarProgressDialog("Obteniendo Altitud", true);
            mElevationPresenter.getElevation(mEvento.Latitud , mEvento.Longitud);
        }
    }


    final LocationListener locationListenerGps = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            setResult(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy());
          //  resultLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(AppCar.getContext(), "Gps is turned on!! ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            Toast.makeText(AppCar.getContext(), "Gps is turned off!! ", Toast.LENGTH_SHORT).show();
        }
    };

    final LocationListener locationListenerNetwork = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            setResult(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(AppCar.getContext(), "Gps is turned on!! ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            Toast.makeText(AppCar.getContext(), "Gps is turned off!! ", Toast.LENGTH_SHORT).show();
        }
    };

}
