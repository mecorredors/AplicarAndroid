package car.gov.co.carserviciociudadano.bicicar.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

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
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.Utils.Validation;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Colegios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Eventos;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.TiposEvento;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Colegio;
import car.gov.co.carserviciociudadano.bicicar.model.Evento;
import car.gov.co.carserviciociudadano.bicicar.model.TipoEvento;
import car.gov.co.carserviciociudadano.bicicar.presenter.BeneficiarioPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.ColegiosPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.EventoPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewBeneficiario;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewColegio;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewEvento;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewTipoEvento;
import car.gov.co.carserviciociudadano.bicicar.presenter.TiposEventoPresenter;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


public class EventoActivity extends BaseActivity implements IViewEvento, IViewTipoEvento, IViewBeneficiario, IViewColegio {

    @BindView(R.id.pbTipoEvento)  ProgressBar pbTipoEvento;
    @BindView(R.id.spiTipoEvento)   Spinner spiTipoEvento;
    @BindView(R.id.pbColegio)  ProgressBar pbColegio;
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

    TiposEventoPresenter tiposEventoPresenter;
    EventoPresenter eventoPresenter;
    ColegiosPresenter colegiosPresenter;
    BeneficiarioPresenter beneficiarioPresenter;
    List<TipoEvento>  mLstTiposEvento = new ArrayList<>();
    ArrayAdapter<TipoEvento> mAdapterTiposEvento;
    List<Colegio>  mLstColegios = new ArrayList<>();
    ArrayAdapter<Colegio> mAdapterColegios;
    Evento mEvento = new Evento();
    Beneficiario mBeneficiarioLogin;
    int mIdEvento;
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

        mLstColegios.add(new Colegio ("Colegios"));
        mAdapterColegios = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstColegios);
        mAdapterColegios.setDropDownViewResource( R.layout.simple_spinner_dropdown_item);
        spiColegio.setAdapter(mAdapterColegios);

        tiposEventoPresenter = new TiposEventoPresenter(this);
        eventoPresenter = new EventoPresenter(this);
        colegiosPresenter = new ColegiosPresenter(this);
        beneficiarioPresenter = new BeneficiarioPresenter(this);
        obtenerDatos();

        btnGuardar.setOnClickListener(onClickListener);
        txtFechaInicio.setOnClickListener(onClickListener);
        txtFechaFin.setOnClickListener(onClickListener);

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
                }else{
                    lyDistanciaKM.setVisibility(View.GONE);
                    lyDuracionMinutos.setVisibility(View.GONE);
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
        }
    }

    private  void obtenerDatos(){
        pbTipoEvento.setVisibility(View.VISIBLE);
        pbColegio.setVisibility(View.VISIBLE);
        tiposEventoPresenter.list();
        colegiosPresenter.list();

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

    @Override
    public void onPause(){
        super.onPause();
        AppCar.VolleyQueue().cancelAll(TiposEvento.TAG);
        AppCar.VolleyQueue().cancelAll(Colegios.TAG);
        AppCar.VolleyQueue().cancelAll(Beneficiarios.TAG);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        AppCar.VolleyQueue().cancelAll(TiposEvento.TAG);
        AppCar.VolleyQueue().cancelAll(Colegios.TAG);
        AppCar.VolleyQueue().cancelAll(Beneficiarios.TAG);
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
    public void onErrorEvento(ErrorApi error) {
        ocultarProgressDialog();
        mostrarMensajeDialog(error.getMessage());
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

    }
    @Override
    public void onSuccessColegios(List<Colegio> lstColegios){
        pbColegio.setVisibility(View.GONE);
        mAdapterColegios.clear();
        lstColegios.add(0, new Colegio("Seleccion un colegio"));
        mAdapterColegios.addAll(lstColegios);
        mAdapterColegios.notifyDataSetChanged();

        if (mIdEvento > 0){
            spiColegio.setSelection(getColegiosPosition(lstColegios));

        }
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

    @Override
    public void onSuccess(Colegio colegio){

    }

    @Override
    public void onErrorColegios(ErrorApi errorApi) {
        pbColegio.setVisibility(View.GONE);
        mostrarErrorDatos(errorApi);
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

            }
        }
    };

private  void obtenerBeneficiarios(){
    if (validar()) {
        Colegio colegio = mAdapterColegios.getItem(spiColegio.getSelectedItemPosition());
        beneficiarioPresenter.list(colegio.IDColegio);
        mostrarProgressDialog("Descargando estudiantes");
    }
}
    private boolean validar(){
        boolean res = true;

        if (Validation.IsEmpty(txtFechaFin)) res = false;
        if (Validation.IsEmpty(txtFechaInicio)) res = false;
        if (Validation.IsEmpty(spiTipoEvento)) res = false;
        if (Validation.IsEmpty(spiColegio)) res = false;
        if (Validation.IsEmpty(txtNombre, lyNombre)) res = false;
        if (Validation.IsEmpty(txtDescripcion)) res = false;

        TipoEvento tipoEvento = (TipoEvento) spiTipoEvento.getSelectedItem();

        if (tipoEvento != null && tipoEvento.Recorrido) {
            if (Validation.IsEmpty(txtDistanciaKM, lyDistanciaKM)) res = false;
            if (Validation.IsEmpty(txtDuracionMinutos, lyDuracionMinutos)) res = false;

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

        return res;
    }

    private void guardar(){
        TipoEvento tipoEvento =  (TipoEvento) spiTipoEvento.getSelectedItem();

        mEvento.IDTipoEvento =  tipoEvento.IDTipoEvento; // mAdapterTiposEvento.getItem(spiTipoEvento.getSelectedItemPosition()).IDTipoEvento;
        mEvento.IDColegio =  mAdapterColegios.getItem(spiColegio.getSelectedItemPosition()).IDColegio;
        mEvento.Nombre = txtNombre.getText().toString();
        mEvento.Descripcion = txtDescripcion.getText().toString();
        mEvento.UsuarioCreacion = "Android";
        mEvento.IDResponsable = mBeneficiarioLogin.IDBeneficiario;
        mEvento.Participantes = 0;
        if (tipoEvento != null && tipoEvento.Recorrido) {
         mEvento.DistanciaKm =   Utils.convertFloat(txtDistanciaKM.getText().toString());
         mEvento.DuracionMinutos =   Utils.convertFloat(txtDuracionMinutos.getText().toString());
        }

        mEvento.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR;
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


}
