package car.gov.co.carserviciociudadano.bicicar.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.SexaDecimalCoordinate;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Colegios;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Colegio;
import car.gov.co.carserviciociudadano.common.LocationBaseGoogleApiActivity;
import car.gov.co.carserviciociudadano.denunciaambiental.activities.DenunciaAmbiental2Activity;
import car.gov.co.carserviciociudadano.denunciaambiental.adapter.GallerySelectedAdapter;
import car.gov.co.carserviciociudadano.denunciaambiental.dataacces.Elevation;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.ElevationPresenter;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.IViewElevation;


public class UbicacionBeneficiarioActivity extends LocationBaseGoogleApiActivity implements OnMapReadyCallback, IViewElevation{

    @BindView(R.id.lyPrincipal) LinearLayout lyPrincipal;
    @BindView(R.id.lblNombre)  TextView lblNombre;
    @BindView(R.id.lblColegio)  TextView lblColegio;
    @BindView(R.id.lblCurso)  TextView lblCurso;
    @BindView(R.id.lblEmail)  TextView lblEmail;
    @BindView(R.id.lblTelefonosContacto)  TextView lblTelefonosContacto;
    @BindView(R.id.lblTelefonosEmergecia)  TextView lblTelefonosEmergecia;
    @BindView(R.id.btnGuardar)   Button btnGuardar;
    @BindView(R.id.lyDatos)   View lyDatos;

    private FirebaseAnalytics mFirebaseAnalytics;
    private GoogleMap mMap;
    LatLng miPosicion = new LatLng(0, 0);
    private static final String LOGTAG = "Ubicacion beneficiario";
   // private static final int PETICION_PERMISO_LOCALIZACION_DENUNCIA = 111;
    private static final int PETICION_GALLERY = 102;
    private static final int PETICION_DENUCIA_PARTE_2 = 105;
    private static final int PETICION_GOOGLE_PLACES = 106;
    GallerySelectedAdapter mAdapter;
   // private List<Foto> mFotos = new ArrayList<>();
    ElevationPresenter mElevationPresenter;
    //Beneficiario mBeneficiario;
    Beneficiario mBeneficiario;
    private ProgressDialog mProgressDialog;
    private int mIdBeneficiario;
    private int mIdColegio;
    Colegio mColegio;
    public static final  String RETORNAR_UBICACION = "retornar_ubicacion";
    public static final  String LATITUDE = "latitude";
    public static final  String LONGITUDE = "longitude";

    private boolean mRetornarUbicacion = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion_beneficiario);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle b = getIntent().getExtras();
        if (b != null){
            mIdBeneficiario = b.getInt(Beneficiario.ID_BENEFICIARIO , 0);
            mIdColegio = b.getInt(Colegio.ID_COLEGIO , 0);
            mRetornarUbicacion = b.getBoolean(RETORNAR_UBICACION, false);

            double latitude = b.getDouble(LATITUDE, 0);
            double longitude = b.getDouble(LONGITUDE, 0);
            if (latitude != 0 && longitude != 0){
                miPosicion = new LatLng(latitude, longitude);
            }
        }

        if (mRetornarUbicacion){
            lyDatos.setVisibility(View.GONE);
        }else {

            if (mIdColegio > 0) {
                mColegio = new Colegios().read(mIdColegio);
                lblNombre.setText(mColegio.Municipio);
                lblColegio.setText(mColegio.Nombre);
                miPosicion = new LatLng(mColegio.Latitude, mColegio.Longitude);
            } else {
                if (mIdBeneficiario > 0) {
                    mBeneficiario = new Beneficiarios().Read(mIdBeneficiario);
                } else {
                    mBeneficiario = Beneficiarios.readBeneficio();
                }
                if (mBeneficiario != null) {
                    lblNombre.setText(mBeneficiario.Nombres + " " + mBeneficiario.Apellidos);

                    Colegio colegio = new Colegios().read(mBeneficiario.IDColegio);
                    if (colegio != null) {
                        lblColegio.setText("Colegio: " + String.valueOf(colegio.Nombre + " " + colegio.Municipio));
                    } else {
                        lblColegio.setText("IDColegio: " + String.valueOf(mBeneficiario.IDColegio));
                    }
                    lblCurso.setText("Curso: " + mBeneficiario.Curso);
                    lblEmail.setText("Email " + mBeneficiario.Email);
                    lblTelefonosContacto.setText("Tel Contacto: " + mBeneficiario.TelefonoContacto);
                    lblTelefonosEmergecia.setText("Tel Emergencia: " + mBeneficiario.TelefonoEmergencia);

                    miPosicion = new LatLng(mBeneficiario.Latitude, mBeneficiario.Longitude);
                }
            }

        }


        mElevationPresenter = new ElevationPresenter(this);

        Places.initialize(getApplicationContext(), getString(R.string.api_key_map));
        PlacesClient placesClient = Places.createClient(this);

        if (BuildConfig.DEBUG == false) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundleAnalitic = new Bundle();
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_ID, "Ubicacion beneficiario");
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_NAME, "Ubicacion beneficiario");
            bundleAnalitic.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Enumerator.ContentTypeAnalitic.BICICAR);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleAnalitic);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.getUiSettings().setZoomControlsEnabled(true);
       // mMap.setPadding(0,0,0,80);
      //  enableLocationUpdates();  // para obtener posicion de gps, si no se activa solo se obtine la ultima ubicacion conocida
        moveCamara();
        startGoogleApiClient(); //

        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);

        } else {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mProgressDialog != null) mProgressDialog.dismiss();
        AppCar.VolleyQueue().cancelAll(Elevation.TAG);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if (mProgressDialog != null) mProgressDialog.dismiss();
        AppCar.VolleyQueue().cancelAll(Elevation.TAG);
    }

    private void moveCamara() {
      //  mMap.addMarker(new MarkerOptions().position(miPosicion).title(getResources().getString(R.string.ubicacion_denuncia)));
       if(mMap != null) {
           CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(miPosicion, 13);
           mMap.moveCamera(camUpd1);
       }
    }


    @Override
    public void onLastLocation(Location loc) {
        if (loc != null && miPosicion.latitude == 0 && miPosicion.longitude == 0 ) {
            miPosicion = new LatLng(loc.getLatitude(), loc.getLongitude());
            moveCamara();
            Log.i(LOGTAG, "Ultima ubicacion conocidad");
        } else if ( mBeneficiario != null && mBeneficiario.Longitude != 0 && mBeneficiario.Longitude != 0) {
            miPosicion = new LatLng(mBeneficiario.Latitude,mBeneficiario.Longitude);
            moveCamara();
        }else if ( mColegio != null && mColegio.Longitude != 0 && mColegio.Longitude != 0) {
            miPosicion = new LatLng(mColegio.Latitude,mColegio.Longitude);
            moveCamara();
        }else{
            Log.e(LOGTAG, "Aun no hay ultima localizacion");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
       // Log.e(LOGTAG, "Principal permi result " + requestCode + " grant "+ ( grantResults.length >0? grantResults[0]: " nada" ) );
        switch (requestCode) {
            case PETICION_PERMISO_LOCALIZACION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED  ) {
                    try {
                        mMap.setMyLocationEnabled(true);
                    }catch (SecurityException ex){
                    }
                } else {
                    mostrarMensaje("Permiso denegado para obtener ubicación");
                }
                return;
            }

        }
    }

    @OnClick(R.id.btnBuscar) void onBuscar() {
        if (mBeneficiario != null) {
            mBeneficiario.Latitude = miPosicion.latitude;
            mBeneficiario.Longitude = miPosicion.longitude;
        }

        if (mColegio != null) {
            mColegio.Latitude = miPosicion.latitude;
            mColegio.Longitude = miPosicion.longitude;
        }

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("CO")
                .build(this);
        startActivityForResult(intent, PETICION_GOOGLE_PLACES);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundleAnalitic = new Bundle();
        bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_ID, "Buscar lugar places");
        bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_NAME, "Buscar lugar places");
        bundleAnalitic.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Enumerator.ContentTypeAnalitic.BICICAR);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleAnalitic);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode ==   PETICION_GOOGLE_PLACES) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                miPosicion = place.getLatLng();
                Log.i("places", "Place: " + place.getName() + ", " + place.getId());
                moveCamara();
                mostrarMensaje(place.getName().toString());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("places", status.getStatusMessage());
                Crashlytics.logException(new Exception("UbicacionBeneficiario.onActivityResult Google Place Status: "+ status.getStatusMessage()));
                Log.i("google places", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }



    private void llenarUbicacionBeneficiario(){
        if (mMap!= null) {
            CameraPosition camPos = mMap.getCameraPosition();
            miPosicion = new LatLng(camPos.target.latitude, camPos.target.longitude);
            mBeneficiario.Latitude = miPosicion.latitude;
            mBeneficiario.Longitude = miPosicion.longitude;

        }
        SexaDecimalCoordinate sexaDecimalCoordinate = new SexaDecimalCoordinate(mBeneficiario.Latitude, mBeneficiario.Longitude);
        sexaDecimalCoordinate.ConvertToFlatCoordinate();
        mBeneficiario.Norte = (sexaDecimalCoordinate.get_coorPlanaNorteFinal());
        mBeneficiario.Este = (sexaDecimalCoordinate.get_coorPlanaEsteFinal());
    }

    private  void guardarBeneficiario(){
        llenarUbicacionBeneficiario();
        if (mBeneficiario.IDBeneficiario == 0){
            mostrarMensaje("Es necesario id usuario, debe iniciar sesión con su biciusuario");
        }else if (mBeneficiario.Latitude == 0 && mBeneficiario.Longitude == 0){
            mostrarMensaje(getResources().getString(R.string.validacion_ubicacion));
        }else{
            mBeneficiario.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR;
            if (mIdBeneficiario > 0) {
                new Beneficiarios().Update(mBeneficiario);
            }else{
                mBeneficiario.guardar();
            }
            mostrarMensajeDialog("Ubicación guardada correctamente ");
        }
    }

    @Override protected void mostrarMensajeDialog(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(mensaje);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            setResult(Activity.RESULT_OK);
            onBackPressed();

            }
        });
        builder.show();
    }
    private void llenarUbicacionColegio(){
        if (mMap!= null) {
            CameraPosition camPos = mMap.getCameraPosition();
            miPosicion = new LatLng(camPos.target.latitude, camPos.target.longitude);
            mColegio.Latitude = miPosicion.latitude;
            mColegio.Longitude = miPosicion.longitude;

        }
        SexaDecimalCoordinate sexaDecimalCoordinate = new SexaDecimalCoordinate(mColegio.Latitude, mColegio.Longitude);
        sexaDecimalCoordinate.ConvertToFlatCoordinate();
        mColegio.Norte = (sexaDecimalCoordinate.get_coorPlanaNorteFinal());
        mColegio.Este = (sexaDecimalCoordinate.get_coorPlanaEsteFinal());
    }
    private  void guardarColegio(){
        llenarUbicacionColegio();
        if (mColegio.Latitude == 0 && mColegio.Longitude == 0){
            mostrarMensaje(getResources().getString(R.string.validacion_ubicacion));
        }else{
            mColegio.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR;

            new Colegios().update(mColegio);

            mostrarMensajeDialog("Ubicación guardada correctamente");
        }
    }
    @OnClick(R.id.btnGuardar) void btnGuardar(){
        if (mRetornarUbicacion){
            if (mMap!= null) {
                CameraPosition camPos = mMap.getCameraPosition();
                miPosicion = new LatLng(camPos.target.latitude, camPos.target.longitude);

                Intent i = getIntent();
                i.putExtra(LATITUDE , miPosicion.latitude);
                i.putExtra(LONGITUDE, miPosicion.longitude);
                setResult(RESULT_OK , i);
                finish();
            }
        }else {
            if (mIdColegio > 0) {
                guardarColegio();
            } else {
                guardarBeneficiario();
            }
        }

    }


    @Override
    public void onSuccessElevation(double elevation) {
        if (mProgressDialog != null) mProgressDialog.dismiss();
        //mDenuncia.setAltitud(elevation);
        Intent i = new Intent(this, DenunciaAmbiental2Activity.class);
        startActivityForResult(i,PETICION_DENUCIA_PARTE_2);
    }

    @Override
    public void onErrrorElevation(int statusCode) {
        if (mProgressDialog != null) mProgressDialog.dismiss();
        Snackbar.make(lyPrincipal, getResources().getString(R.string.error_load_elevation), Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green))
                .setAction("REINTENTAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // mElevationPresenter.getElevation(mDenuncia.getLatitude(),mDenuncia.getLongitude());
                    }
                })
                .show();
    }

}
