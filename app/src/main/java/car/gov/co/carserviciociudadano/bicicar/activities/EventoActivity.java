package car.gov.co.carserviciociudadano.bicicar.activities;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import car.gov.co.carserviciociudadano.bicicar.model.UbicacionBeneficiario;
import car.gov.co.carserviciociudadano.bicicar.presenter.BeneficiarioPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.ColegiosPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.CuencasPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.EventoPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewBeneficiario;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewColegio;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewCuenca;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewEvento;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewTipoEvento;
import car.gov.co.carserviciociudadano.bicicar.presenter.TiposEventoPresenter;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.denunciaambiental.dataacces.Elevation;
import car.gov.co.carserviciociudadano.denunciaambiental.dataacces.Lugares;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Lugar;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.ElevationPresenter;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.IViewElevation;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.IViewLugares;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.LugaresPresenter;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Municipio;


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
    @BindView(R.id.btnUbicacionEvento)   Button btnUbicacionEvento;
    @BindView(R.id.lyUbicacion)   View lyUbicacion;
    @BindView(R.id.txtHoraInicio)   EditText txtHoraInicio;
    @BindView(R.id.txtHoraFin)   EditText txtHoraFin;
    @BindView(R.id.lyHoraInicio)  TextInputLayout lyHoraInicio;
    @BindView(R.id.lyHoraFin)  TextInputLayout lyHoraFin;

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
    Evento mEvento = new Evento();
    Beneficiario mBeneficiarioLogin;
    int mIdEvento;
    private static final int REQUEST_COLEGIOS = 100;
    private static final int REQUEST_UBICACION = 101;
    private static final String ID_CUNDINAMARCA = "25";
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
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
        obtenerCuencas();

        txtColegio.setOnClickListener(onClickListener);
        btnGuardar.setOnClickListener(onClickListener);
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
                txtColegio.setText(colegio.Nombre);
            }
        }
    }

    private void obtenerCuencas(){
        pbCuenca.setVisibility(View.VISIBLE);
        mCuencasPresenter.getCuencas();
    }
    private  void obtenerDatos(){
        pbTipoEvento.setVisibility(View.VISIBLE);
        tiposEventoPresenter.list();

        pbMunicipio.setVisibility(View.VISIBLE);
        mLugaresPresenter.obtenerMunicipios(ID_CUNDINAMARCA);

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
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                txtHoraInicio.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
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
        return i;
    }

    public int getMunicipiosPosition(String idMunicipio){
        int i = 0;
        for (Lugar item : mLstMunicipios) {
            if (item.getIDLugar().equals(idMunicipio))
                return i;
            i++;
        }
        return i;
    }

    public int getCuencaPosition(int idCuenca){
        int i = 0;
        for (Cuenca item : mLstCuencas) {
            if (item.IDCuenca ==  idCuenca)
                return i;
            i++;
        }
        return i;
    }

    public int getVeredaPosition(String idVereda){
        int i = 0;
        for (Lugar item : mLstVeredas) {
            if (item.getIDLugar().equals(idVereda))
                return i;
            i++;
        }
        return i;
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
        AppCar.VolleyQueue().cancelAll(Colegios.TAG);
        AppCar.VolleyQueue().cancelAll(Beneficiarios.TAG);
        AppCar.VolleyQueue().cancelAll(Lugares.TAG);
        AppCar.VolleyQueue().cancelAll(Cuencas.TAG);
        AppCar.VolleyQueue().cancelAll(Elevation.TAG);
    }

    @Override
    public void onSuccess(Evento evento){
        ocultarProgressDialog();
        if (eventoPresenter.insert(evento)) {
            mostrarMensajeEventoCreado();
        }else{
            mostrarMensajeDialog("Error al guardar evento en el teléfono");
        }
    }

    @Override
    public void onSuccessModificar(Evento evento) {
        ocultarProgressDialog();
        if (eventoPresenter.update(evento)) {
            mostrarMensajeEventoCreado();
        }else{
            mostrarMensajeDialog("Error al guardar evento en el teléfono");
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
        pbTipoEvento.setVisibility(View.GONE);
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
            mostrarMensajeDialog(errorApi.getMessage());
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
    public void onSuccessDepartamentos(List<Lugar> lstDepartamentos) {

    }

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
    public void onErrorDepartamentos(ErrorApi errorApi) {

    }

    @Override
    public void onErrorMunicipios(ErrorApi errorApi) {
        pbMunicipio.setVisibility(View.GONE);
        Snackbar.make(lyPrincipal, getResources().getString(R.string.error_load_municipios), Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green))
                .setAction("REINTENTAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mLugaresPresenter.obtenerMunicipios(ID_CUNDINAMARCA);
                    }
                })
                .show();

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
                                mLugaresPresenter.obtenerMunicipios(municipio.getIDLugar());
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
                if(municipio!=null) {
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
                    obtenerBeneficiarios();
                    break;
                case R.id.txtColegio:
                    Intent i = new Intent(EventoActivity.this, ColegiosActivity.class);
                    i.putExtra(ColegiosActivity.IS_SELECTOR, true);
                    startActivityForResult(i , REQUEST_COLEGIOS);
                    break;
                case R.id.btnUbicacionEvento:
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
    if (validar()) {
        beneficiarioPresenter.list(mEvento.IDColegio);
        mostrarProgressDialog("Descargando estudiantes");
    }
}
    private boolean validar(){
        boolean res = true;

        if (Validation.IsEmpty(txtFechaFin)) res = false;
        if (Validation.IsEmpty(txtFechaInicio)) res = false;
        if (Validation.IsEmpty(spiTipoEvento)) res = false;
        if (Validation.IsEmpty(txtNombre, lyNombre)) res = false;
        if (Validation.IsEmpty(txtDescripcion)) res = false;
        if (Validation.IsEmpty(txtColegio)) res = false;

        TipoEvento tipoEvento = (TipoEvento) spiTipoEvento.getSelectedItem();

        if (tipoEvento != null && tipoEvento.Recorrido) {
            if (Validation.IsEmpty(txtDistanciaKM, lyDistanciaKM)) res = false;
            if (Validation.IsEmpty(txtDuracionMinutos, lyDuracionMinutos)) res = false;

        }else{
            if (Validation.IsEmpty(txtLatitud, lyLatitude)) res = false;
            if (Validation.IsEmpty(txtLongitud, lyLongitud)) res = false;
            if (Validation.IsEmpty(txtNorte, lyNorte)) res = false;
            if (Validation.IsEmpty(txtEste, lyEste)) res = false;
            if (Validation.IsEmpty(txtAltitud, lyAltitud)) res = false;
        }
        if (Validation.IsEmpty(txtHoraInicio, lyHoraInicio)) res = false;
        if (Validation.IsEmpty(txtHoraFin, lyHoraFin)) res = false;
        if (Validation.IsEmpty(spiCuenca)) res = false;
        if (Validation.IsEmpty(spiMunicipio)) res = false;


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

        return res;
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

        Lugar municipio = (Lugar) spiMunicipio.getSelectedItem();
        if (municipio != null) {
            mEvento.IDMunicipio = municipio.getIDLugar();
        }
        Lugar vereda = (Lugar) spiVereda.getSelectedItem();
        if (vereda != null) {
            mEvento.IDVereda = vereda.getIDLugar();
        }
        Cuenca cuenca = (Cuenca) spiCuenca.getSelectedItem();
        if (cuenca != null) {
            mEvento.IDCuenca = cuenca.IDCuenca;
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
                txtColegio.setText(data.getStringExtra(Colegio.NOMBRE));
                Colegio colegio = new Colegios().read(mEvento.IDColegio);
                spiMunicipio.setSelection(getMunicipiosPosition(colegio.IDMunicipio));
            }else if (requestCode == REQUEST_UBICACION){
                mEvento.Latitud = Utils.round(7 , data.getDoubleExtra(UbicacionBeneficiarioActivity.LATITUDE, 0));
                mEvento.Longitud = Utils.round(7 ,data.getDoubleExtra(UbicacionBeneficiarioActivity.LONGITUDE, 0));
                txtLatitud.setText(String.valueOf(mEvento.Latitud));
                txtLongitud.setText(String.valueOf(mEvento.Longitud));
                SexaDecimalCoordinate sexaDecimalCoordinate = new SexaDecimalCoordinate(mEvento.Latitud, mEvento.Longitud);
                sexaDecimalCoordinate.ConvertToFlatCoordinate();
                mEvento.Norte = String.valueOf(Utils.round(0, sexaDecimalCoordinate.get_coorPlanaNorteFinal())).replace(".0","");
                mEvento.Este = String.valueOf(Utils.round(0, sexaDecimalCoordinate.get_coorPlanaEsteFinal())).replace(".0","");
                txtNorte.setText(mEvento.Norte);
                txtEste.setText(mEvento.Este);
                mostrarProgressDialog("Obteniendo Altitud", true);
                mElevationPresenter.getElevation(mEvento.Latitud , mEvento.Longitud);
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
        pbCuenca.setVisibility(View.GONE);
        Snackbar.make(lyPrincipal, getResources().getString(R.string.error_load_municipios), Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green))
                .setAction("REINTENTAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        obtenerCuencas();
                    }
                })
                .show();
    }

    @Override
    public void onSuccessElevation(double elevation) {
        ocultarProgressDialog();
        mEvento.Altitud = Utils.round(2 ,elevation);
        txtAltitud.setText(String.valueOf( mEvento.Altitud ));
    }

    @Override
    public void onErrrorElevation(int statusCode) {
        ocultarProgressDialog();
        Snackbar.make(lyPrincipal, getResources().getString(R.string.error_load_elevation), Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green))
                .setAction("REINTENTAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         mElevationPresenter.getElevation(mEvento.Latitud,mEvento.Longitud);
                    }
                })
                .show();
    }
}
