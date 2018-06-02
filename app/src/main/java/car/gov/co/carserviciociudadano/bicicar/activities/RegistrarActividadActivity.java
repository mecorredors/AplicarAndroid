package car.gov.co.carserviciociudadano.bicicar.activities;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.adapter.LogTrayectoAdapter;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.LogTrayectos;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.presenter.BeneficiarioPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewBeneficiario;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewLogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.presenter.LogTrayectoPresenter;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


public class RegistrarActividadActivity extends BaseActivity implements IViewBeneficiario, IViewLogTrayecto {

    private static final int ZXING_CAMERA_PERMISSION = 1;
    private static final int REQUEST_CODE_SCANNER = 2;

    @BindView(R.id.lblSerial) TextView lblSerial;
    @BindView(R.id.lblRin) TextView lblRin;
    @BindView(R.id.lblNombre) TextView lblNombre;
    @BindView(R.id.lyDatosQR) View lyDatosQR;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.btnPublicar) Button btnPublicar;


    LogTrayectoAdapter mAdaptador;
    List<LogTrayecto> mLstLogTrayectos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_actividad);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        lyDatosQR.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        mAdaptador = new LogTrayectoAdapter(mLstLogTrayectos);
       // mAdaptador.setOnClickListener(onClickListener);
        recyclerView.setAdapter(mAdaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        obtenerItemsActividad();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick(R.id.btnAgregar) void onAgregar(){

        LogTrayecto actividad = new LogTrayecto();
        actividad.Serial = lblSerial.getText().toString();
        actividad.TamanioRin = lblRin.getText().toString();
        if (!lblNombre.getText().toString().isEmpty())
            actividad.Nombre = lblNombre.getText().toString();
        actividad.Estado = Enumerator.BicicarLogTrayecto.PENDIENTE_PUBLICAR;
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DAY_OF_MONTH, -1);
        actividad.Fecha = ca.getTime();//Calendar.getInstance().getTime();
        if (new LogTrayectos().Insert(actividad)) {
            lyDatosQR.setVisibility(View.GONE);

            obtenerItemsActividad();
        }

    }

    private  void obtenerItemsActividad(){
        mLstLogTrayectos.clear();
        List<LogTrayecto> items = new LogTrayectos().List(Enumerator.BicicarLogTrayecto.PENDIENTE_PUBLICAR);

        Calendar fechaActual  = Calendar.getInstance();
        int day = 0;
        int totalHoy = 0;
        for (LogTrayecto item : items){
            Calendar fechaItem = Utils.convertToCalendar(item.Fecha);

            if (fechaItem.get(Calendar.DAY_OF_MONTH) == fechaActual.get(Calendar.DAY_OF_MONTH)){
                totalHoy++;
            }

            if (day !=  fechaItem.get(Calendar.DAY_OF_MONTH)){
                day = fechaItem.get(Calendar.DAY_OF_MONTH);
                LogTrayecto actividad = new LogTrayecto();
                if (day == fechaActual.get(Calendar.DAY_OF_MONTH)){
                    actividad.Label = "Hoy " + Utils.getDayOfWeek(fechaItem) ;
                }else {
                    actividad.Label = Utils.getDayOfWeek(fechaItem) + " " + Utils.toStringLargeFromDate(item.Fecha);
                }
                mLstLogTrayectos.add(actividad);
            }
            mLstLogTrayectos.add(item);
        }

        if (mLstLogTrayectos.size() > 0)
            mLstLogTrayectos.get(0).Label =  mLstLogTrayectos.get(0).Label + ", total: " + totalHoy;

        btnPublicar.setVisibility(mLstLogTrayectos.size() == 0 ? View.GONE :View.VISIBLE);
        mAdaptador.notifyDataSetChanged();

    }

    @OnClick(R.id.btnEscanearCodigo) void onEscaner(){
       // abrirEscaner();

        String datos = "Fecha ingreso:43193 - Marca:CORLEONE - Estado:NUEVO/ Serial:JSY17092119 - Color:Verde BiciCAR - Tamaño Rin:24 - N° Ide CAR:00001 / Municipio:ANAPOIMA";
        obtenerDatos(datos);
    }
    @OnClick(R.id.btnPublicar) void onPublicar(){
        mostrarProgressDialog("Publicando ...");
        LogTrayectoPresenter logTrayectoPresenter = new LogTrayectoPresenter(this);
        Beneficiario beneficiario = Beneficiarios.readBeneficio();
        logTrayectoPresenter.publicar(beneficiario.IDBeneficiario);
    }

    private void abrirEscaner(){
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},
                    ZXING_CAMERA_PERMISSION);
        } else {
            Intent i = new Intent(this, EscanearQRActivity.class);
            startActivityForResult(i,   REQUEST_CODE_SCANNER);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    abrirEscaner();
                } else {
                    mostrarMensaje("Se necesita permiso de camara para registrar actividad");
                }
                return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if (requestCode == REQUEST_CODE_SCANNER){
            if (resultCode == Activity.RESULT_OK){
                String datosEscaner = data.getStringExtra(EscanearQRActivity.ESCANER_DATOS);
                Log.d("escaner", datosEscaner);
                obtenerDatos(datosEscaner);
            }
        }
    }

    private void obtenerDatos(String datosEscaner){
        try {
            if (datosEscaner == null && datosEscaner.isEmpty())
                mostrarMensajeDialog("No se encontraron datos");

            lyDatosQR.setVisibility(View.VISIBLE);
            int index = datosEscaner.indexOf("Serial");
            if (index > 0) {
                String serial = datosEscaner.substring(index);
                index = serial.indexOf("-");
                if (index > 0) {
                    serial = serial.substring(0, index);
                    lblSerial.setText(serial.replace("Serial", "").replace(":", "").trim());
                }
            }

            index = datosEscaner.indexOf("Rin");
            if (index > 0) {
                String rin = datosEscaner.substring(index);
                index = rin.indexOf("-");
                if (index > 0) {
                    rin = rin.substring(0, index);
                    lblRin.setText(rin.replace("Rin", "").replace(":", "").trim());
                }
            }

            lblNombre.setVisibility(View.GONE);
            if (Utils.isOnline(this)) {
                mostrarProgressDialog("Consultando");
                BeneficiarioPresenter beneficiarioPresenter = new BeneficiarioPresenter(this);
                beneficiarioPresenter.obtenerItem(lblSerial.getText().toString(), lblRin.getText().toString());
            }
        }catch (IndexOutOfBoundsException ex){
            mostrarMensajeDialog("Error al leer:" + datosEscaner );
        }
    }

    @Override
    public void onSuccess(Beneficiario beneficiario) {
        ocultarProgressDialog();
        lblNombre.setVisibility(View.VISIBLE);
        lblNombre.setText(beneficiario.Nombres + " " + beneficiario.Apellidos);
    }

    @Override
    public void onError(ErrorApi errorApi) {
        ocultarProgressDialog();
        if (errorApi.getStatusCode() == 404){
            mostrarMensajeDialog(errorApi.getMessage());
        }

    }

    @Override
    public void onSuccessLogTrayecto() {
        ocultarProgressDialog();
        obtenerItemsActividad();
        mostrarMensajeDialog("Los datos fueron publicados correctamente");
    }

    @Override
    public void onErrorLogTrayecto(ErrorApi errorApi) {
        ocultarProgressDialog();
        obtenerItemsActividad();
        mostrarMensajeDialog(errorApi.getMessage());
    }
}
