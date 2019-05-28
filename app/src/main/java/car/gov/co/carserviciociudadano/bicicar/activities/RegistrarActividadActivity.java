package car.gov.co.carserviciociudadano.bicicar.activities;


import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.PreferencesApp;
import car.gov.co.carserviciociudadano.Utils.SexaDecimalCoordinate;
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
import car.gov.co.carserviciociudadano.bicicar.services.LocationMonitoringService;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.common.Notifications;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;



public class RegistrarActividadActivity extends BaseActivity implements IViewBeneficiario, IViewLogTrayecto, LogTrayectoAdapter.LogTrayectoListener {

    private static final int ZXING_CAMERA_PERMISSION = 1;
    private static final int REQUEST_CODE_SCANNER = 2;
    private static final int REQUEST_CODE_BENEFICIARIOS = 3;
    private static final int REQUEST_MIS_DATOS = 4;
    private static final int REQUEST_HISTORIAL_TRAYECTOS = 5;

    @BindView(R.id.lblSerial) TextView lblSerial;
    @BindView(R.id.lblRin) TextView lblRin;
    @BindView(R.id.lblNombre) TextView lblNombre;
    @BindView(R.id.lyDatosQR) View lyDatosQR;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.btnPublicar) Button btnPublicar;
    @BindView(R.id.btnEscanearCodigo) Button btnEscanearCodigo;
    @BindView(R.id.btnBeneficiarios) Button btnBeneficiarios;
    @BindView(R.id.txtDistanciaKM) EditText txtDistanciaKM;
    @BindView(R.id.txtTiempo) EditText txtTiempo;
    @BindView(R.id.inputLyDistanciaKM) TextInputLayout inputLyDistanciaKM;
    @BindView(R.id.lyRegistrarMiRecorrido) View lyRegistrarMiRecorrido;
    @BindView(R.id.lblDuracion) TextView lblDuracion;
    @BindView(R.id.lblDistancia) TextView lblDistancia;
    @BindView(R.id.btnIniciar) Button btnIniciar;
    @BindView(R.id.btnDetener) Button btnDetener;
    @BindView(R.id.btnAgregarMiRecorrido) Button btnAgregarMiRecorrido;
    @BindView(R.id.btnPausa) Button btnPausa;
    @BindView(R.id.lyInfoRecorrido) View lyInfoRecorrido;
    @BindView(R.id.lyIngresarRecorrido) View lyIngresarRecorrido;
    @BindView(R.id.lyContenedor) View lyContenedor;
    @BindView(R.id.lyBotonesAsistencia) View lyBonesAsistencia;
    @BindView(R.id.menu_bar) BottomNavigationViewEx menu_bar;

    LogTrayectoAdapter mAdaptador;
    List<LogTrayecto> mLstLogTrayectos = new ArrayList<>();
    Beneficiario mBeneficiario = null;
    Beneficiario mBeneficiarioLogin;
    String ruta = "";
    float distancia = 0;
    float tiempo_en_minutos = 0;
    long tiempoMillis = 0;
    int minutos = 0;
    int segundos = 0;

    float distanciaEvento09 = 0;
    long tiempoMillisEvento09 = 0;

    private List<Beneficiario> lstBeneficiarios;
    private int mPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_actividad);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        mBeneficiarioLogin  = Beneficiarios.readBeneficio();
        if (mBeneficiarioLogin == null) return;
        bar.setDisplayHomeAsUpEnabled(true);
        if (mBeneficiarioLogin != null)
            bar.setTitle( mBeneficiarioLogin.Nombres + " " + mBeneficiarioLogin.Apellidos);

        lyDatosQR.setVisibility(View.GONE);
        lyRegistrarMiRecorrido.setVisibility(View.GONE);
        lyIngresarRecorrido.setVisibility(View.GONE);
        lyInfoRecorrido.setVisibility(View.GONE);
        btnDetener.setVisibility(View.GONE);
        btnPausa.setVisibility(View.GONE);
        btnAgregarMiRecorrido.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        mAdaptador = new LogTrayectoAdapter(mLstLogTrayectos);
        mAdaptador.setLogTrayectoListener(this);
        recyclerView.setAdapter(mAdaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        menu_bar.enableShiftingMode(false);
        menu_bar.enableItemShiftingMode(false);
        menu_bar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        obtenerItemsActividad();

        if (mBeneficiarioLogin.IDPerfil == Enumerator.BicicarPerfil.PEDAGOGO || mBeneficiarioLogin.IDPerfil == Enumerator.BicicarPerfil.BENEFICIARIO_APP || mBeneficiarioLogin.IDPerfil == Enumerator.BicicarPerfil.EVENTO){
            lyBonesAsistencia.setVisibility(View.GONE);
            lyRegistrarMiRecorrido.setVisibility(View.VISIBLE);
            boolean isInPause = PreferencesApp.getDefault(PreferencesApp.READ).getBoolean(LocationMonitoringService.EXTRA_IN_PAUSE, false);
            btnPausa.setText(isInPause ? "Continuar" : "Pausa");
            if (isInPause){
                tiempoMillis = PreferencesApp.getDefault(PreferencesApp.READ).getLong(LocationMonitoringService.EXTRA_TIEMPO_MILLIS_IN_PAUSE , (long) 0);
                distancia = PreferencesApp.getDefault(PreferencesApp.READ).getFloat(LocationMonitoringService.EXTRA_DISTANCIA_IN_PAUSE ,  0);
                mostrarTiempoyDistancia();
            }

            if (mBeneficiarioLogin.IDPerfil == Enumerator.BicicarPerfil.EVENTO){
                btnPausa.setVisibility(View.GONE);
            }

        }else{
            Intent i = new Intent(this, BeneficiariosActivity.class);
            startActivityForResult(i, REQUEST_CODE_BENEFICIARIOS);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                        tiempoMillis = intent.getLongExtra(LocationMonitoringService.EXTRA_TIEMPO_MILLIS,0) ;
                        distancia = intent.getFloatExtra(LocationMonitoringService.EXTRA_DISTANCIA, 0);
                        tiempoMillisEvento09 = intent.getLongExtra(LocationMonitoringService.EXTRA_TIEMPO_EVENTO_09,0) ;
                        distanciaEvento09 = intent.getFloatExtra(LocationMonitoringService.EXTRA_DISTANCIA_EVENTO_09, 0);

                        mostrarTiempoyDistancia();

                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );

    }
    private void mostrarTiempoyDistancia(){
        segundos = (int) (tiempoMillis / 1000);
        minutos = segundos / 60;
        segundos = segundos % 60;
        tiempo_en_minutos = Utils.round (2, tiempoMillis / (float)60000.0);
        lblDuracion.setText(String.format("%d:%02d", minutos, segundos));
        lblDistancia.setText(String.valueOf(Utils.round(2,(distancia/1000))));

        btnPausa.setVisibility(View.VISIBLE);
        btnIniciar.setVisibility(View.GONE);
        lblDuracion.setVisibility(View.VISIBLE);
        lyInfoRecorrido.setVisibility(View.VISIBLE);
        btnDetener.setVisibility(View.VISIBLE);

        if (mBeneficiarioLogin.IDPerfil == Enumerator.BicicarPerfil.EVENTO){
            btnPausa.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdaptador != null)
            mAdaptador.setLogTrayectoListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bicicar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_cerrar_sesion ) {
            cerrarSesion();
            return true;
        }else if (id == R.id.item_historial ) {
            Intent i = new Intent(this, HistorialTrayectosActivity.class);
            startActivityForResult(i, REQUEST_HISTORIAL_TRAYECTOS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cerrarSesion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.seguro_cerrar_sesion);

        builder.setPositiveButton(R.string.cerrar_sesion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PreferencesApp preferencesApp = new PreferencesApp(PreferencesApp.WRITE, PreferencesApp.BICIAR_NAME);
                preferencesApp.putString(Beneficiario.BICICAR_USUARIO, null);
                preferencesApp.commit();

                new Beneficiarios().DeleteAll();

                RegistrarActividadActivity.super.onBackPressed();

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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent i;
            switch (item.getItemId()) {
                case R.id.item_eventos:
                    return true;
                case R.id.item_colegios:
                    i = new Intent(RegistrarActividadActivity.this, ColegiosActivity.class);
                    startActivity(i);
                    return true;
                case R.id.item_ubicacion_beneficiarios:
                    i = new Intent(RegistrarActividadActivity.this, UbicacionBeneficiarioActivity.class);
                    startActivity(i);
                    return true;

            }
            return false;
        }
    };

    @OnClick(R.id.btnPausa)void onPausa() {

        boolean isInPause = PreferencesApp.getDefault(PreferencesApp.READ).getBoolean(LocationMonitoringService.EXTRA_IN_PAUSE, false);
        if (isInPause){
            continuar();
        }else {
            PreferencesApp.getDefault(PreferencesApp.WRITE).putLong(LocationMonitoringService.EXTRA_TIEMPO_MILLIS_IN_PAUSE, tiempoMillis).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putFloat(LocationMonitoringService.EXTRA_DISTANCIA_IN_PAUSE, distancia).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putBoolean(LocationMonitoringService.EXTRA_IN_PAUSE, true).commit();

            stopService(new Intent(RegistrarActividadActivity.this, LocationMonitoringService.class));
            mAlreadyStartedService = false;
            btnPausa.setText("Continuar");

            Notifications.showNotification("PAUSA: " + " Distancia: " + Utils.round(2,(distancia/1000)) + " Kms: Duración "+ minutos + ":" + segundos, RegistrarActividadActivity.class.getSimpleName() );

        }

    }

    @OnClick(R.id.btnBeneficiarios) void onBeneficiarios(){
        Intent i = new Intent(this, BeneficiariosActivity.class);
        startActivityForResult(i, REQUEST_CODE_BENEFICIARIOS);
    }
    @OnClick(R.id.btnAgregar) void onAgregar(){

        mBeneficiarioLogin  = Beneficiarios.readBeneficio();

        LogTrayecto logTrayecto = new LogTrayecto();
        logTrayecto.Serial = lblSerial.getText().toString();
        logTrayecto.TamanioRin = lblRin.getText().toString();
        if (!lblNombre.getText().toString().isEmpty())
            logTrayecto.Nombre = lblNombre.getText().toString();
        logTrayecto.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR;
        logTrayecto.Fecha = Calendar.getInstance().getTime();

        if (mBeneficiario != null){
            logTrayecto.IDBeneficiario = mBeneficiario.IDBeneficiario;

            logTrayecto.IDBicicleta = mBeneficiario.IDBicicleta;
        }
        if (mBeneficiarioLogin != null)
            logTrayecto.IDBeneficiarioRegistro = mBeneficiarioLogin.IDBeneficiario;

        if (new LogTrayectos().Insert(logTrayecto)) {
            lyDatosQR.setVisibility(View.GONE);

            obtenerItemsActividad();

        }
        ocultarTeclado(lyContenedor);
    }
    @OnClick(R.id.btnAgregarMiRecorrido) void onAgregarMiRecorrido(){

        inputLyDistanciaKM.setError("");

        if (Utils.convertFloat(txtDistanciaKM.getText().toString()) == 0){
            inputLyDistanciaKM.setError("Ingrese un valor");
            return;
        }

        agregarMiRecorrido(Utils.convertFloat(txtDistanciaKM.getText().toString()),Utils.convertFloat(txtTiempo.getText().toString()),"");

    }

    @OnClick(R.id.btnMisDatos) void onMisDatos(){
        Intent i = new Intent(this, EstadisticaPersonaActivity.class);
        startActivityForResult(i, REQUEST_MIS_DATOS);

    }
    @OnClick(R.id.btnIniciar) void onIniciar(){
        startStep1();

    }
    @OnClick(R.id.btnDetener) void onDetener(){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Finalizar recorrido?");

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        builder.setPositiveButton("Finalizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBeneficiarioLogin  = Beneficiarios.readBeneficio();
                ruta = PreferencesApp.getDefault(PreferencesApp.READ).getString(LocationMonitoringService.EXTRA_RUTA);

                double latitude_punto_a = 0;
                double longitude_punto_a = 0;
                double latitude_punto_b = 0;
                double longitude_punto_b = 0;
                if (ruta != null && !ruta.isEmpty()) {
                    List<LatLng> latLngs = PolyUtil.decode(ruta);
                    if (latLngs.size() > 0){
                        latitude_punto_a = latLngs.get(0).latitude;
                        longitude_punto_a = latLngs.get(0).longitude;
                        latitude_punto_b = latLngs.get(latLngs.size() - 1).latitude;
                        longitude_punto_b = latLngs.get(latLngs.size() - 1).longitude;
                    }
                }

                stopService(new Intent(RegistrarActividadActivity.this, LocationMonitoringService.class));
                mAlreadyStartedService = false;

                NotificationManager notificationManager = (NotificationManager) AppCar.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();

                if (mBeneficiarioLogin.IDPerfil == Enumerator.BicicarPerfil.EVENTO){
                    tiempo_en_minutos = Utils.round (2, tiempoMillisEvento09 / (float)60000.0);
                    distancia = distanciaEvento09;
                }
                agregarMiRecorrido(Utils.round(2, (distancia / 1000)), tiempo_en_minutos, ruta, latitude_punto_a, longitude_punto_a, latitude_punto_b, longitude_punto_b);

                PreferencesApp.getDefault(PreferencesApp.WRITE).putFloat(LocationMonitoringService.EXTRA_DISTANCIA_IN_PAUSE, 0).commit();
                PreferencesApp.getDefault(PreferencesApp.WRITE).putLong(LocationMonitoringService.EXTRA_TIEMPO_MILLIS_IN_PAUSE, 0).commit();
                PreferencesApp.getDefault(PreferencesApp.WRITE).putBoolean(LocationMonitoringService.EXTRA_IN_PAUSE, false).commit();

                dialog.dismiss();


            }
        });

        builder.show();


    }

    @OnClick(R.id.btnEscanearCodigo) void onEscaner(){
        abrirEscaner();

       //   String datos = "Fecha ingreso:43193 - Marca:CORLEONE - Estado:NUEVO/ Serial:JSY17092119 - Color:Verde BiciCAR - Tamaño Rin:24 - N° Ide CAR:00001 / Municipio:ANAPOIMA";
        // obtenerDatos(datos);
    }
    @OnClick(R.id.btnPublicar) void onPublicar(){
        publicar();
    }

    private void publicar(){
        mBeneficiarioLogin  = Beneficiarios.readBeneficio();
        mostrarProgressDialog("Publicando ...");
        LogTrayectoPresenter logTrayectoPresenter = new LogTrayectoPresenter(this);
        logTrayectoPresenter.publicar(mBeneficiarioLogin.IDBeneficiario);
    }

    private void agregarMiRecorrido(float distancia, float minutos, String ruta) {
        agregarMiRecorrido(distancia, minutos, ruta,0,0,0,0);
    }
    private void agregarMiRecorrido(float distancia, float minutos, String ruta, double latitudePuntoA, double longitudePuntoA, double latitudePuntoB, double longitudePuntoB){

        mBeneficiarioLogin  = Beneficiarios.readBeneficio();

        if (distancia > 0) {
            LogTrayecto logTrayecto = LogTrayectoPresenter.agregarMiRecorrido(distancia, minutos, ruta, latitudePuntoA, longitudePuntoA, latitudePuntoB, longitudePuntoB);
            if (logTrayecto != null) {
                obtenerItemsActividad();
                if (mBeneficiarioLogin.IDPerfil == Enumerator.BicicarPerfil.EVENTO){
                    publicar();
                }else {
                    if (ruta != null && !ruta.isEmpty())
                        verRutaMapa(logTrayecto);
                }
            }
        }


        lyInfoRecorrido.setVisibility(View.GONE);
        //lyIngresarRecorrido.setVisibility(View.VISIBLE);

        this.tiempo_en_minutos = 0;
        this.distancia = 0;
        txtDistanciaKM.setText("");
        txtTiempo.setText("");
        //btnAgregarMiRecorrido.setVisibility(View.VISIBLE);
        btnIniciar.setVisibility(View.VISIBLE);
        btnDetener.setVisibility(View.GONE);
        btnPausa.setVisibility(View.GONE);
        ocultarTeclado(lyContenedor);
        ocultarTeclado(inputLyDistanciaKM);

    }

    private  void obtenerItemsActividad(){

        if (mBeneficiarioLogin == null) return;

        mLstLogTrayectos.clear();
        List<LogTrayecto> items = new LogTrayectos().List(Enumerator.Estado.PENDIENTE_PUBLICAR, mBeneficiarioLogin.IDBeneficiario);

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

        if (mLstLogTrayectos.size() > 0 && mLstLogTrayectos.get(0).Label.contains("Hoy"))
            mLstLogTrayectos.get(0).TotalItems = totalHoy;


        btnPublicar.setVisibility(mLstLogTrayectos.size() == 0 ? View.GONE :View.VISIBLE);
        mAdaptador.notifyDataSetChanged();

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

            case REQUEST_PERMISSIONS_REQUEST_CODE:

                if (grantResults.length <= 0) {
                    // If img_user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.");
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                    startStep3();

                } else {
                    // Permission denied.

                    // Notify the img_user via a SnackBar that they have rejected a core permission for the
                    // app, which makes the Activity useless. In a real app, core permissions would
                    // typically be best requested during a welcome-screen flow.

                    // Additionally, it is important to remember that a permission might have been
                    // rejected without asking the img_user for permission (device policy or "Never ask
                    // again" prompts). Therefore, a img_user interface affordance is typically implemented
                    // when permissions are denied. Otherwise, your app could appear unresponsive to
                    // touches or interactions which have required permissions.
                    showSnackbar(R.string.permiso_ubicacion_denegado,
                            R.string.configuracion, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                }
                break;


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
        }else if (requestCode == REQUEST_CODE_BENEFICIARIOS && resultCode == RESULT_OK){
           obtenerItemsActividad();
       }else if (requestCode == REQUEST_MIS_DATOS && resultCode == RESULT_OK){
           obtenerItemsActividad();
       }else if (requestCode == REQUEST_HISTORIAL_TRAYECTOS && resultCode == RESULT_OK){
           obtenerItemsActividad();
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
                mBeneficiario = null;
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
        mBeneficiario = beneficiario;
        lblNombre.setVisibility(View.VISIBLE);
        lblNombre.setText(beneficiario.Nombres + " " + beneficiario.Apellidos);

    }

    @Override
    public void onSuccess(List<Beneficiario> lstBeneficiarios) {
        this.lstBeneficiarios = lstBeneficiarios;
    }

    @Override
    public void onError(ErrorApi errorApi) {
        ocultarProgressDialog();
        if (errorApi.getStatusCode() == 404){
            mostrarMensajeDialog(errorApi.getMessage());
        }

    }

    @Override
    public void onErrorListarItems(ErrorApi errorApi) {

    }

    @Override
    public void onSuccessRecordarClave(String mensaje) {

    }

    @Override
    public void onErrorRecordarClave(ErrorApi error) {

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




    /////////////

    private static final String TAG = RegistrarActividadActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private boolean mAlreadyStartedService = false;

    /**
     * Step 1: Check Google Play services
     */
    private void startStep1() {

        //Check whether this user has installed Google play service which is being used by Location updates.
        if (isGooglePlayServicesAvailable()) {

            //Passing null to indicate that it is executing for the first time.
            startStep2(null);

        } else {
            mostrarMensaje("Play services no disponible");
        }
    }


    /**
     * Step 2: Check & Prompt Internet connection
     */
    private Boolean startStep2(DialogInterface dialog) {
       /* ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            promptInternetConnect();
            return false;
        }


        if (dialog != null) {
            dialog.dismiss();
        }*/

        //Yes there is active internet connection. Next check Location is granted by user or not.

        if (checkPermissions()) { //Yes permissions are granted by the user. Go to the next step.
            startStep3();
        } else {  //No user has not granted the permissions yet. Request now.
            requestPermissions();
        }
        return true;
    }

    /**
     * Show A Dialog with button to refresh the internet state.
     */
    private void promptInternetConnect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarActividadActivity.this);
        builder.setTitle("Verificar conección");
        builder.setMessage("Verificar conección");

        String positiveText = "Reintentar";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Block the Application Execution until user grants the permissions
                        if (startStep2(dialog)) {

                            //Now make sure about location permission.
                            if (checkPermissions()) {

                                //Step 2: Start the Location Monitor Service
                                //Everything is there to start the service.
                                startStep3();
                            } else if (!checkPermissions()) {
                                requestPermissions();
                            }
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Step 3: Start the Location Monitor Service
     */
    private void startStep3() {

        //And it will be keep running until you close the entire application from task manager.
        //This method will executed only once.

        if (!mAlreadyStartedService ) {

            lyIngresarRecorrido.setVisibility(View.GONE);
            lyInfoRecorrido.setVisibility(View.VISIBLE);
            btnIniciar.setVisibility(View.GONE);
            btnDetener.setVisibility(View.VISIBLE);
          //  btnPausa.setVisibility(View.VISIBLE);
            btnPausa.setText("Pausa");

            PreferencesApp.getDefault(PreferencesApp.WRITE).putFloat(LocationMonitoringService.EXTRA_DISTANCIA , 0).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putLong(LocationMonitoringService.EXTRA_START_TIME , System.currentTimeMillis()).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putString(LocationMonitoringService.EXTRA_RUTA , "").commit();

            PreferencesApp.getDefault(PreferencesApp.WRITE).putFloat(LocationMonitoringService.EXTRA_DISTANCIA_IN_PAUSE, 0).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putLong(LocationMonitoringService.EXTRA_TIEMPO_MILLIS_IN_PAUSE, 0).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putBoolean(LocationMonitoringService.EXTRA_IN_PAUSE, false).commit();

            PreferencesApp.getDefault(PreferencesApp.WRITE).putLong(LocationMonitoringService.EXTRA_TIEMPO_EVENTO_09, 0).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putFloat(LocationMonitoringService.EXTRA_DISTANCIA_EVENTO_09, 0).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putString(LocationMonitoringService.EXTRA_ACTIVIDAD , RegistrarActividadActivity.class.getSimpleName()).commit();

            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);

            mAlreadyStartedService = true;

        }
    }

    private void continuar(){
        if (!mAlreadyStartedService ) {

            lyIngresarRecorrido.setVisibility(View.GONE);
            lyInfoRecorrido.setVisibility(View.VISIBLE);
            btnIniciar.setVisibility(View.GONE);
            // btnAgregarMiRecorrido.setVisibility(View.GONE);
            btnDetener.setVisibility(View.VISIBLE);
            btnPausa.setVisibility(View.VISIBLE);
            btnPausa.setText("Pausa");

            PreferencesApp.getDefault(PreferencesApp.WRITE).putBoolean(LocationMonitoringService.EXTRA_IN_PAUSE, false).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putFloat(LocationMonitoringService.EXTRA_DISTANCIA , 0).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putLong(LocationMonitoringService.EXTRA_START_TIME , System.currentTimeMillis()).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putString(LocationMonitoringService.EXTRA_ACTIVIDAD , RegistrarActividadActivity.class.getSimpleName()).commit();

            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);

            mAlreadyStartedService = true;

        }
    }

    /**
     * Return the availability of GooglePlayServices
     */
    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }


    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionState2 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * Start permissions requests.
     */
    private void requestPermissions() {

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);


        // Provide an additional rationale to the img_user. This would happen if the img_user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale || shouldProvideRationale2) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permiso_ubicacion,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(RegistrarActividadActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the img_user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(RegistrarActividadActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }


    @Override
    public void onVerRuta(int position, View view) {
        LogTrayecto logTrayecto = mLstLogTrayectos.get(position);
        verRutaMapa(logTrayecto);

    }

    @Override
    public void onEliminar(int position, View view) {
        eliminar(position);
    }

    private void verRutaMapa(LogTrayecto logTrayecto){
        Intent i = new Intent(this, RutaMapaActivity.class);
        i.putExtra(LogTrayecto.RUTA , logTrayecto.Ruta);
        i.putExtra(LogTrayecto.DURACION_MINUTOS, logTrayecto.DuracionMinutos);
        i.putExtra(LogTrayecto.DISTANCIA_KM, logTrayecto.DistanciaKm);
        startActivity(i);
    }

    private  void eliminar(final int position){
        mPosition = position;

        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarActividadActivity.this);

        builder.setMessage("¿Eliminar trayecto?");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LogTrayecto logTrayecto = mLstLogTrayectos.get(mPosition);
                new LogTrayectos().Delete(logTrayecto.IDLogTrayecto);
                mLstLogTrayectos.remove(mPosition);
                mAdaptador.notifyDataSetChanged();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

}
