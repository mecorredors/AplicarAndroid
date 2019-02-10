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
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.itextpdf.text.pdf.fonts.otf.Language;

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
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.model.Ruta;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewLogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.presenter.LogTrayectoPresenter;
import car.gov.co.carserviciociudadano.bicicar.services.LocationMonitoringService;
import car.gov.co.carserviciociudadano.bicicar.services.SeguirRutaService;

import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.common.Notifications;
import car.gov.co.carserviciociudadano.parques.activities.IntentHelper;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class SeguirRutaActivity extends BaseActivity implements OnMapReadyCallback, IViewLogTrayecto {

    @BindView(R.id.lblDuracion) TextView lblDuracion;
    @BindView(R.id.lblDistancia) TextView lblDistancia;
    @BindView(R.id.btnIniciar) Button btnIniciar;
    @BindView(R.id.btnDetener) Button btnDetener;
    @BindView(R.id.btnPausa) Button btnPausa;
    @BindView(R.id.lyInfoRecorrido) View lyInfoRecorrido;

    float distancia = 0;
    float tiempo_en_minutos = 0;
    long tiempoMillis = 0;
    int minutos = 0;
    int segundos = 0;
    private GoogleMap mapa;
    private int colorRuta = 0xFF1976D2;
    double latitude;
    double longitude;
    Marker marker;
    boolean primerMovimientoCamara = false;

    public final static  String RUTA = "ruta";
    private Ruta mRuta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguir_ruta);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mRuta = (Ruta) IntentHelper.getObjectForKey(RUTA);

        btnPausa.setVisibility(View.GONE);
        btnDetener.setVisibility(View.GONE);

        boolean isInPause = PreferencesApp.getDefault(PreferencesApp.READ).getBoolean(SeguirRutaService.EXTRA_IN_PAUSE, false);
        btnPausa.setText(isInPause ? "Continuar" : "Pausa");
        if (isInPause){
            tiempoMillis = PreferencesApp.getDefault(PreferencesApp.READ).getLong(SeguirRutaService.EXTRA_TIEMPO_MILLIS_IN_PAUSE , (long) 0);
            distancia = PreferencesApp.getDefault(PreferencesApp.READ).getFloat(SeguirRutaService.EXTRA_DISTANCIA_IN_PAUSE ,  0);
            mostrarTiempoyDistancia();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                        tiempoMillis = intent.getLongExtra(SeguirRutaService.EXTRA_TIEMPO_MILLIS,0) ;
                        distancia = intent.getFloatExtra(SeguirRutaService.EXTRA_DISTANCIA, 0);
                        latitude = intent.getDoubleExtra(SeguirRutaService.EXTRA_LATITUDE, 0);
                        longitude = intent.getDoubleExtra(SeguirRutaService.EXTRA_LONGITUDE, 0);

                        mostrarTiempoyDistancia();

                    }
                }, new IntentFilter(SeguirRutaService.ACTION_LOCATION_BROADCAST)
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapa != null && latitude != 0){
            CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16);
            mapa.moveCamera(camUpd1);
        }
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

        if (mapa != null) {
            LatLng  posicion = new LatLng(latitude, longitude);
            if (marker != null){
                marker.remove();
            }

           marker = mapa.addMarker(new MarkerOptions().position(posicion).title("Mi Ubicación").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_initial_map)));

            if (!primerMovimientoCamara){
                if (mapa != null && latitude != 0){
                    CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16);
                    mapa.moveCamera(camUpd1);
                    primerMovimientoCamara = true;
                }
            }
        }

    }

    @OnClick(R.id.btnPausa)void onPausa() {

        boolean isInPause = PreferencesApp.getDefault(PreferencesApp.READ).getBoolean(SeguirRutaService.EXTRA_IN_PAUSE, false);
        if (isInPause){
            continuar();
        }else {
            PreferencesApp.getDefault(PreferencesApp.WRITE).putLong(SeguirRutaService.EXTRA_TIEMPO_MILLIS_IN_PAUSE, tiempoMillis).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putFloat(SeguirRutaService.EXTRA_DISTANCIA_IN_PAUSE, distancia).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putBoolean(SeguirRutaService.EXTRA_IN_PAUSE, true).commit();

            stopService(new Intent(SeguirRutaActivity.this, SeguirRutaService.class));
            mAlreadyStartedService = false;
            btnPausa.setText("Continuar");

            Notifications.showNotificationSeguirRuta("PAUSA: " + " Distancia: " + Utils.round(2,(distancia/1000)) + " Kms: Duración "+ minutos + ":" + segundos );

        }

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

              String  ruta = PreferencesApp.getDefault(PreferencesApp.READ).getString(SeguirRutaService.EXTRA_RUTA);

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

                stopService(new Intent(SeguirRutaActivity.this, SeguirRutaService.class));
                mAlreadyStartedService = false;

                NotificationManager notificationManager = (NotificationManager) AppCar.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();


                agregarMiRecorrido(Utils.round(2, (distancia / 1000)), tiempo_en_minutos, ruta, latitude_punto_a, longitude_punto_a, latitude_punto_b, longitude_punto_b);

                PreferencesApp.getDefault(PreferencesApp.WRITE).putFloat(SeguirRutaService.EXTRA_DISTANCIA_IN_PAUSE, 0).commit();
                PreferencesApp.getDefault(PreferencesApp.WRITE).putLong(SeguirRutaService.EXTRA_TIEMPO_MILLIS_IN_PAUSE, 0).commit();
                PreferencesApp.getDefault(PreferencesApp.WRITE).putBoolean(SeguirRutaService.EXTRA_IN_PAUSE, false).commit();

                dialog.dismiss();


            }
        });

        builder.show();

    }
    private void agregarMiRecorrido(float distancia, float minutos, String ruta, double latitudePuntoA, double longitudePuntoA, double latitudePuntoB, double longitudePuntoB){
        Beneficiario mBeneficiarioLogin =  Beneficiarios.readBeneficio();
        if (distancia > 0 && mBeneficiarioLogin != null) {
            LogTrayecto logTrayecto = LogTrayectoPresenter.agregarMiRecorrido(distancia, minutos, ruta, latitudePuntoA, longitudePuntoA, latitudePuntoB, longitudePuntoB);
            if (logTrayecto != null) {
                publicar();
            }
        }


    //    lyInfoRecorrido.setVisibility(View.GONE);

        this.tiempo_en_minutos = 0;
        this.distancia = 0;
        //btnAgregarMiRecorrido.setVisibility(View.VISIBLE);
        btnIniciar.setVisibility(View.VISIBLE);
        btnDetener.setVisibility(View.GONE);
        btnPausa.setVisibility(View.GONE);

    }

    private void publicar(){
        Beneficiario mBeneficiarioLogin  = Beneficiarios.readBeneficio();
        mostrarProgressDialog("Publicando ...");
        LogTrayectoPresenter logTrayectoPresenter = new LogTrayectoPresenter(this);
        logTrayectoPresenter.publicar(mBeneficiarioLogin.IDBeneficiario);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SeguirRutaActivity.this);
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

            PreferencesApp.getDefault(PreferencesApp.WRITE).putFloat(SeguirRutaService.EXTRA_DISTANCIA , 0).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putLong(SeguirRutaService.EXTRA_START_TIME , System.currentTimeMillis()).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putString(SeguirRutaService.EXTRA_RUTA , "").commit();

            PreferencesApp.getDefault(PreferencesApp.WRITE).putFloat(SeguirRutaService.EXTRA_DISTANCIA_IN_PAUSE, 0).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putLong(SeguirRutaService.EXTRA_TIEMPO_MILLIS_IN_PAUSE, 0).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putBoolean(SeguirRutaService.EXTRA_IN_PAUSE, false).commit();


            Intent intent = new Intent(this, SeguirRutaService.class);
            startService(intent);

            mAlreadyStartedService = true;

        }
    }

    private void continuar(){
        if (!mAlreadyStartedService ) {

            lyInfoRecorrido.setVisibility(View.VISIBLE);
            btnIniciar.setVisibility(View.GONE);
            // btnAgregarMiRecorrido.setVisibility(View.GONE);
            btnDetener.setVisibility(View.VISIBLE);
            btnPausa.setVisibility(View.VISIBLE);
            btnPausa.setText("Pausa");

            PreferencesApp.getDefault(PreferencesApp.WRITE).putBoolean(SeguirRutaService.EXTRA_IN_PAUSE, false).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putFloat(SeguirRutaService.EXTRA_DISTANCIA , 0).commit();
            PreferencesApp.getDefault(PreferencesApp.WRITE).putLong(SeguirRutaService.EXTRA_START_TIME , System.currentTimeMillis()).commit();

            Intent intent = new Intent(this, SeguirRutaService.class);
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
                            ActivityCompat.requestPermissions(SeguirRutaActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the img_user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(SeguirRutaActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {

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
    public void onMapReady(GoogleMap map) {
        mapa = map;
        //mapa.getUiSettings().setMapToolbarEnabled(false);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        dibujarRuta();

        if (latitude != 0 && longitude != 0){
            CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16);
            mapa.moveCamera(camUpd1);
        }

    }

    private void dibujarRuta() {

       // String rutas [] = {"caw[`bhcM??L?DCLAHDF@N?LGRCFFD?LP","a}v[pbhcMHLFJFHBDHHJTFLDDDDJPNNFL","uyv[dghcM?DIDGLGHUXABOJCBOJGBQGOKIE","_~v[lihcMGCOICAMIEIGMKOIAEEMSKMCAGKDK","saw[fehcMHOJIHODQ?CBM" };

        List<String> rutas = new ArrayList<>();
        if (mRuta == null) {
            rutas.add("w`f[v`acM???^FFPDtA\\PF\\DN?f@B|APRBJJPPNH^TXNRJLFHBNFd@NNFFBhDvAl@BD@rAV`@DTD`@Nl@NCHIVhEf@f@BD?v@VVHfAFx@FB@j@R");
            rutas.add("erd[lracMrAFDB`A`@JHf@Rh@LdAt@^?|ET`@EZ\\f@CFC|DWh@JT@H@TDN@V@l@HB@qD`AEBiA|AYP}ExB{@NGFGD[P");
            rutas.add("cdd[pcbcMg@`@KJYPODGBOJmKdHa@XEBaA~@s@d@y@`@_Ap@s@TEDCFQJSJo@XWPYTg@`@UJ]Rk@Z[V_@TYP]R[V_@Ts@d@c@Tk@^mDdC");
            rutas.add("ove[dhccMQJ[PMHUN]PIDWNEBu@b@a@Ts@h@yItFWPKFOLSNm@`@IDe@T_@T[Tg@V_@Tg@Z]NcAn@OJo@Zk@Xi@^e@XgAn@GDq@^m@Zk@Tu@b@");
            rutas.add("ofg[vhdcMGBC@ULKJQNSLm@\\c@Vw@h@WLg@X_@To@Z_@Tc@X{@z@EDa@p@q@vA{@bCEh@AFC\\Mh@IZGVOl@CFIZC\\[dACDGRI\\K\\M\\Mn@CZ?CIPU`@[DC?MHKNEFSd@S`ACFK\\M`@");
            rutas.add("eeh[zuecMO`@Qd@[~@GNa@n@]f@]b@a@b@k@f@i@ZuA|@IFk@\\k@Xy@d@e@Vo@Xo@`@m@\\g@V]T]Ta@V_@Ta@T]V_@Te@Tk@VmA~@e@NEDk@Xo@\\i@Xi@Ri@Xq@\\");
            rutas.add("eri[dyfcM[Nm@X{@d@y@b@ED_Af@OHc@TYNa@R[Re@V]R]T]Pa@R]Ra@Rs@\\]TWLq@\\YNe@Pa@Xk@Xk@^i@R}@j@OJc@Pc@RQHC?[@A@SBI?KCIYHQ@E@SAYKYOYY_@Ye@]c@Ua@{@kB");
            rutas.add("k_k[xcgcM_@]KSMQa@g@U_@S_@Ua@]c@c@_ASS_@q@OWk@o@Wg@i@u@MUi@u@K[g@q@]c@a@e@QKEKCAK?K?KBCB[PMBEAAOE_@EOk@cAAUCW?EMe@GKQ]IKGKACSa@IKCCKIGEa@WCCe@YIGCAUG[Km@U_@OMEUI]M");
            rutas.add("icl[z{ecMu@[[Iq@WKGg@[KGq@OWGOA[GGAo@SKEUGKEMEa@My@U{@WMECIGEQIG?GEcAa@OIWIe@Qu@OeAHo@Cs@Dm@JOBc@XAJEFEFQNIFEHJOHCNI@GEQEMEMKOIMAAKCKHKDIDQPDCPIJExAg@`@e@D[OQ}@_@iBs@mAa@");
        }else{
            rutas.add(mRuta.RutaTrayecto);
        }
       int i = 0;

       for (String ruta : rutas) {
           ArrayList<LatLng> directionPositionList = (ArrayList<LatLng>) PolyUtil.decode(ruta);
           PolylineOptions polylineOptions = DirectionConverter.createPolyline(SeguirRutaActivity.this, directionPositionList, 6, colorRuta);
           mapa.addPolyline(polylineOptions);
           int size = directionPositionList.size();

           if (i == 0) {
               mapa.addMarker(new MarkerOptions().position(directionPositionList.get(0)).title("Inicio").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_initial_map)));
               CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(directionPositionList.get(0), 16);
               mapa.moveCamera(camUpd1);

            }

            int km = i + 1;
            if (i < rutas.size() -1 ){

                 MarkerOptions mo = new MarkerOptions().position(directionPositionList.get(size-1)).title("Km: " + km).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_position_map));
                 Marker marker = mapa.addMarker(mo);
                 mo.anchor(0f, 0.5f);
                 marker.showInfoWindow();

            }
            if (i == rutas.size() -1 ){
                float totalKilometros = mRuta != null ? mRuta.DistanciaKM : km;
                mapa.addMarker(new MarkerOptions().position(directionPositionList.get(size-1)).title("Fin recorrido Km: " + totalKilometros).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_finish_map)).visible(true)).showInfoWindow();
            }
            i++;

       }

    }

    @Override
    public void onSuccessLogTrayecto() {
        ocultarProgressDialog();
        mostrarMensajeDialog("Los datos fueron publicados correctamente");
    }

    @Override
    public void onErrorLogTrayecto(ErrorApi errorApi) {
        ocultarProgressDialog();
        mostrarMensajeDialog(errorApi.getMessage());
    }
}
