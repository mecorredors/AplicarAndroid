package car.gov.co.carserviciociudadano.bicicar.activities;


import android.Manifest;
import android.app.NotificationManager;
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
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.PreferencesApp;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.adapter.RutasAdapter;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.LogTrayectos;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Rutas;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.model.Nivel;
import car.gov.co.carserviciociudadano.bicicar.model.Ruta;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewNivel;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewRutas;
import car.gov.co.carserviciociudadano.bicicar.presenter.NivelesPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.RutasPresenter;
import car.gov.co.carserviciociudadano.bicicar.services.LocationMonitoringService;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.common.Notifications;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


public class CrearRutaActivity extends BaseActivity implements   IViewRutas, IViewNivel, RutasAdapter.RutaListener {

    private static final int ZXING_CAMERA_PERMISSION = 1;
    private static final int REQUEST_CODE_SCANNER = 2;
    private static final int REQUEST_CODE_BENEFICIARIOS = 3;
    private static final int REQUEST_MIS_DATOS = 4;
    private static final int REQUEST_HISTORIAL_TRAYECTOS = 5;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.btnPublicar) Button btnPublicar;
    @BindView(R.id.lyRegistrarMiRecorrido) View lyRegistrarMiRecorrido;
    @BindView(R.id.lblDuracion) TextView lblDuracion;
    @BindView(R.id.lblDistancia) TextView lblDistancia;
    @BindView(R.id.btnIniciar) Button btnIniciar;
    @BindView(R.id.btnDetener) Button btnDetener;
    @BindView(R.id.btnPausa) Button btnPausa;
    @BindView(R.id.lyInfoRecorrido) View lyInfoRecorrido;
    @BindView(R.id.lyContenedor) View lyContenedor;

    @BindView(R.id.txtNombre) EditText txtNombre;
    @BindView(R.id.txtDescripcion) EditText txtDescripcion;
    @BindView(R.id.inputLyNombre) TextInputLayout inputLyNombre;
    @BindView(R.id.spiNivel) Spinner mSpiNivel;
    @BindView(R.id.lblDescripcion) TextView lblDescripcion;

    RutasPresenter rutasPresenter;
    NivelesPresenter nivelesPresenter;
    Ruta mRuta;
    String ruta;
    RutasAdapter mAdaptador;
    List<Ruta> mLstRutas = new ArrayList<>();
    Beneficiario mBeneficiarioLogin;
    float distancia = 0;
    float tiempo_en_minutos = 0;
    long tiempoMillis = 0;
    int minutos = 0;
    int segundos = 0;

    float distanciaEvento09 = 0;
    long tiempoMillisEvento09 = 0;

    private int mPosition;
    public static final float DISTANCIA_MINIMA_MTS = 300;
    List<Nivel> mLstNiveles = new ArrayList<>();
    ArrayAdapter<Nivel> adapterNiveles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ruta);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        mBeneficiarioLogin  = Beneficiarios.readBeneficio();
        if (mBeneficiarioLogin == null) return;
        bar.setDisplayHomeAsUpEnabled(true);
        //if (mBeneficiarioLogin != null)
          //  bar.setTitle( mBeneficiarioLogin.Nombres + " " + mBeneficiarioLogin.Apellidos);


        rutasPresenter = new RutasPresenter(this);
        List<Ruta> lstRutas = new Rutas().List(Enumerator.Estado.EDICION , mBeneficiarioLogin.IDBeneficiario);
        if (lstRutas.size() > 0){
            mRuta = lstRutas.get(0);
            txtNombre.setText(mRuta.Nombre);
            txtDescripcion.setText(mRuta.Descripcion);
        }

        nivelesPresenter = new NivelesPresenter(this);
        obtenerNiveles();

        lyRegistrarMiRecorrido.setVisibility(View.GONE);
        lyInfoRecorrido.setVisibility(View.GONE);
        btnDetener.setVisibility(View.GONE);
        btnPausa.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        mAdaptador = new RutasAdapter(mLstRutas);
        mAdaptador.setRutaListener(this);
        recyclerView.setAdapter(mAdaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        obtenerRutas();

        if (mBeneficiarioLogin.IDPerfil == Enumerator.BicicarPerfil.PEDAGOGO || mBeneficiarioLogin.IDPerfil == Enumerator.BicicarPerfil.BENEFICIARIO_APP || mBeneficiarioLogin.IDPerfil == Enumerator.BicicarPerfil.EVENTO){

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
        lblDescripcion.setVisibility(View.GONE);

        if (mBeneficiarioLogin.IDPerfil == Enumerator.BicicarPerfil.EVENTO){
            btnPausa.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdaptador != null)
            mAdaptador.setRutaListener(this);
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

                CrearRutaActivity.super.onBackPressed();

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

    @OnClick(R.id.btnPausa)void onPausa() {

        boolean isInPause = PreferencesApp.getDefault(PreferencesApp.READ).getBoolean(LocationMonitoringService.EXTRA_IN_PAUSE, false);
        if (isInPause){
            continuar();
        }else {
            PreferencesApp.getDefault(PreferencesApp.WRITE).putLong(LocationMonitoringService.EXTRA_TIEMPO_MILLIS_IN_PAUSE, tiempoMillis).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putFloat(LocationMonitoringService.EXTRA_DISTANCIA_IN_PAUSE, distancia).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putBoolean(LocationMonitoringService.EXTRA_IN_PAUSE, true).commit();

            stopService(new Intent(CrearRutaActivity.this, LocationMonitoringService.class));
            mAlreadyStartedService = false;
            btnPausa.setText("Continuar");

            Notifications.showNotification("PAUSA: " + " Distancia: " + Utils.round(2,(distancia/1000)) + " Kms: Duración "+ minutos + ":" + segundos , CrearRutaActivity.class.getSimpleName() );

        }

    }


    @OnClick(R.id.btnIniciar) void onIniciar(){
        if (txtNombre.getText().toString().trim().length() == 0){
            inputLyNombre.setError("Ingrese un nombre para la ruta");
            mostrarMensajeDialog("Ingrese un nombre para la ruta");
            return;
        }
        if (mRuta == null) {
            mRuta = new Ruta();
        }

        Nivel nivel = (Nivel) mSpiNivel.getSelectedItem();
        if (nivel != null && nivel.IDNivel > 0){
            mRuta.IDNivel = nivel.IDNivel;
        }else{
            mostrarMensajeDialog("Seleccione el invel de la ruta");
            return;
        }

        mRuta.Nombre = txtNombre.getText().toString();
        mRuta.Descripcion = txtDescripcion.getText().toString();
        mRuta.IDBeneficiario = mBeneficiarioLogin.IDBeneficiario;
        mRuta.Estado = Enumerator.Estado.EDICION;
        rutasPresenter.guardarRuta(mRuta);


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

                stopService(new Intent(CrearRutaActivity.this, LocationMonitoringService.class));
                mAlreadyStartedService = false;

                NotificationManager notificationManager = (NotificationManager) AppCar.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();

                if (mBeneficiarioLogin.IDPerfil == Enumerator.BicicarPerfil.EVENTO){
                    tiempo_en_minutos = Utils.round (2, tiempoMillisEvento09 / (float)60000.0);
                    distancia = distanciaEvento09;
                }

                if (distancia >= DISTANCIA_MINIMA_MTS) {
                    mRuta.RutaTrayecto = ruta;
                    mRuta.DistanciaKM = (distancia / 1000);
                    mRuta.DuracionMinutos = tiempo_en_minutos;
                    mRuta.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR;
                    rutasPresenter.guardarRuta(mRuta);
                    txtDescripcion.setText("");
                    txtNombre.setText("");
                    mSpiNivel.setSelection(0);
                    mRuta = null;
                    obtenerRutas();
                    publicar();
                }else{
                    mostrarMensajeDialog("La distancia recorrida es muy corta, debe ser mínimo de " + DISTANCIA_MINIMA_MTS + " mts");
                }
                PreferencesApp.getDefault(PreferencesApp.WRITE).putFloat(LocationMonitoringService.EXTRA_DISTANCIA_IN_PAUSE, 0).commit();
                PreferencesApp.getDefault(PreferencesApp.WRITE).putLong(LocationMonitoringService.EXTRA_TIEMPO_MILLIS_IN_PAUSE, 0).commit();
                PreferencesApp.getDefault(PreferencesApp.WRITE).putBoolean(LocationMonitoringService.EXTRA_IN_PAUSE, false).commit();

                dialog.dismiss();


                lyInfoRecorrido.setVisibility(View.GONE);

                tiempo_en_minutos = 0;
                distancia = 0;
                btnIniciar.setVisibility(View.VISIBLE);
                btnDetener.setVisibility(View.GONE);
                btnPausa.setVisibility(View.GONE);
                ocultarTeclado(lyContenedor);

            }
        });

        builder.show();


    }


    @OnClick(R.id.btnPublicar) void onPublicar(){
        publicar();
    }

    private void publicar(){
        mBeneficiarioLogin  = Beneficiarios.readBeneficio();
        mostrarProgressDialog("Publicando ...");
        rutasPresenter.publicar(mBeneficiarioLogin.IDBeneficiario);

    }

    private void obtenerNiveles(){
        mostrarProgressDialog("Cargando ...");
        nivelesPresenter.getNiveles();
    }

    private  void obtenerRutas(){

        if (mBeneficiarioLogin == null) return;

        mLstRutas.clear();
        List<Ruta> lstRutas  = new Rutas().List(Enumerator.Estado.PENDIENTE_PUBLICAR, mBeneficiarioLogin.IDBeneficiario);
        mLstRutas.addAll(lstRutas);
        btnPublicar.setVisibility(mLstRutas.size() == 0 ? View.GONE :View.VISIBLE);
        lblDescripcion.setVisibility(mLstRutas.size() == 0 ? View.VISIBLE :View.GONE);
        mAdaptador.notifyDataSetChanged();


    }

    private void abrirEscaner(){
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
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

      if (requestCode == REQUEST_CODE_BENEFICIARIOS && resultCode == RESULT_OK){
          obtenerRutas();
       }else if (requestCode == REQUEST_MIS_DATOS && resultCode == RESULT_OK){
          obtenerRutas();
       }else if (requestCode == REQUEST_HISTORIAL_TRAYECTOS && resultCode == RESULT_OK){
          obtenerRutas();
       }
    }


    /////////////

    private static final String TAG = CrearRutaActivity.class.getSimpleName();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(CrearRutaActivity.this);
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
            PreferencesApp.getDefault(PreferencesApp.WRITE).putString(LocationMonitoringService.EXTRA_ACTIVIDAD , CrearRutaActivity.class.getSimpleName()).commit();

            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);

            mAlreadyStartedService = true;

        }
    }

    private void continuar(){
        if (!mAlreadyStartedService ) {

            lyInfoRecorrido.setVisibility(View.VISIBLE);
            btnIniciar.setVisibility(View.GONE);
            btnDetener.setVisibility(View.VISIBLE);
            btnPausa.setVisibility(View.VISIBLE);
            btnPausa.setText("Pausa");

            PreferencesApp.getDefault(PreferencesApp.WRITE).putBoolean(LocationMonitoringService.EXTRA_IN_PAUSE, false).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putFloat(LocationMonitoringService.EXTRA_DISTANCIA , 0).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putLong(LocationMonitoringService.EXTRA_START_TIME , System.currentTimeMillis()).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putString(LocationMonitoringService.EXTRA_ACTIVIDAD , CrearRutaActivity.class.getSimpleName()).commit();

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
                Manifest.permission.ACCESS_FINE_LOCATION);

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
                        Manifest.permission.ACCESS_FINE_LOCATION);

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
                            ActivityCompat.requestPermissions(CrearRutaActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the img_user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(CrearRutaActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
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
        Ruta ruta = mLstRutas.get(position);
        verRutaMapa(ruta);

    }

    @Override
    public void onEliminar(int position, View view) {
        eliminar(position);
    }

    private void verRutaMapa(Ruta ruta){
        Intent i = new Intent(this, RutaMapaActivity.class);
        i.putExtra(LogTrayecto.RUTA , ruta.RutaTrayecto);
        i.putExtra(LogTrayecto.DURACION_MINUTOS, ruta.DuracionMinutos);
        i.putExtra(LogTrayecto.DISTANCIA_KM, ruta.DistanciaKM);
        startActivity(i);
    }

    private  void eliminar(final int position){
        mPosition = position;

        AlertDialog.Builder builder = new AlertDialog.Builder(CrearRutaActivity.this);

        builder.setMessage("¿Eliminar ruta?");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Ruta ruta = mLstRutas.get(mPosition);
                new Rutas().Delete(ruta.Id);
                mLstRutas.remove(mPosition);
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

    @Override
    public void onSuccess(List<Ruta> lstRutas) {

    }

    @Override
    public void onSuccess() {
        ocultarProgressDialog();
        obtenerRutas();
        mostrarMensajeDialog("Los datos fueron publicados correctamente");
    }

    @Override
    public void onError(ErrorApi errorApi) {
        ocultarProgressDialog();
        obtenerRutas();
        mostrarMensajeDialog(errorApi.getMessage());
    }

    @Override
    public void onSuccessNiveles(List<Nivel> lstNiveles) {
        ocultarProgressDialog();
        mLstNiveles.clear();
        mLstNiveles.addAll(lstNiveles);
        mLstNiveles.add(0,new Nivel(0,"Nivel ruta"));
        adapterNiveles = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstNiveles);
        adapterNiveles.setDropDownViewResource( R.layout.simple_spinner_dropdown_item);
        mSpiNivel.setAdapter(adapterNiveles);
        adapterNiveles.notifyDataSetChanged();

        if (mRuta != null && mRuta.IDNivel >0) {
            mSpiNivel.setSelection(adapterNiveles.getPosition(new Nivel(mRuta.IDNivel, "")));
        }
    }
    @Override
    public void onErrorNivel(ErrorApi errorApi) {
        ocultarProgressDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(CrearRutaActivity.this);
        builder.setMessage(errorApi.getMessage());
        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               obtenerNiveles();

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
