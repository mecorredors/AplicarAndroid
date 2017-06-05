package car.gov.co.carserviciociudadano.denunciaambiental.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.SexaDecimalCoordinate;
import car.gov.co.carserviciociudadano.common.LocationBaseGoogleApiActivity;
import car.gov.co.carserviciociudadano.denunciaambiental.adapter.GallerySelectedAdapter;
import car.gov.co.carserviciociudadano.denunciaambiental.dataacces.Elevation;
import car.gov.co.carserviciociudadano.denunciaambiental.dataacces.Lugares;
import car.gov.co.carserviciociudadano.denunciaambiental.model.ArchivoAdjunto;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Foto;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Denuncia;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.ElevationPresenter;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.IViewElevation;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.IViewIdLugarXCoordenada;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.LugarXCoordendaPresenter;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class DenunciaAmbientalActivity extends LocationBaseGoogleApiActivity implements OnMapReadyCallback, IViewElevation, IViewIdLugarXCoordenada{
    @BindView(R.id.lyInicial)  LinearLayout lyInicial;
    @BindView(R.id.lyDenuncia)  LinearLayout lyDenuncia;
    @BindView(R.id.gridGallery)    GridView gridView;
    @BindView(R.id.btnSiguiente)   Button btnSiguiente;

    private GoogleMap mMap;
    LatLng miPosicion = new LatLng(0, 0);
    private static final String LOGTAG = "Denuncia ambiental";
    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_GALLERY = 102;
    private static final int PETICION_DENUCIA_PARTE_2 = 105;
    GallerySelectedAdapter mAdapter;
   // private List<Foto> mFotos = new ArrayList<>();
    ElevationPresenter mElevationPresenter;
    LugarXCoordendaPresenter mLugarXCoordenadaPresenter;
    private Denuncia mDenuncia;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia_ambiental);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDenuncia = Denuncia.newInstance();

        mAdapter = new GallerySelectedAdapter(this, new ArrayList<>(mDenuncia.getFotos()));
        mAdapter.SetOnItemClickListener(listenerGallerySelected);
        gridView.setAdapter(mAdapter);
        setNumPhotosSelect();
        mElevationPresenter = new ElevationPresenter(this);
        mLugarXCoordenadaPresenter = new LugarXCoordendaPresenter(this);


    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.getUiSettings().setZoomControlsEnabled(true);
       // mMap.setPadding(0,0,0,80);

      //  enableLocationUpdates();  // para obtener posicion de gps, si no se activa solo se obtine la ultima ubicacion conocida

        startGoogleApiClient(); //

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);

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
        AppCar.VolleyQueue().cancelAll(Lugares.TAG);
        AppCar.VolleyQueue().cancelAll(Elevation.TAG);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if (mProgressDialog != null) mProgressDialog.dismiss();
        AppCar.VolleyQueue().cancelAll(Lugares.TAG);
        AppCar.VolleyQueue().cancelAll(Elevation.TAG);
    }

    private void moveCamara() {
      //  mMap.addMarker(new MarkerOptions().position(miPosicion).title(getResources().getString(R.string.ubicacion_denuncia)));
        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(miPosicion, 10);
        mMap .moveCamera(camUpd1);
    }


    @Override
    public void onLastLocation(Location loc) {
        if (loc != null && mDenuncia.getLatitude() == 0 && mDenuncia.getLongitude() == 0 ) {
            miPosicion = new LatLng(loc.getLatitude(), loc.getLongitude());
           // insertarMarcador();
            moveCamara();
            Log.i(LOGTAG, "Ultima ubicacion conocidad");
        } else if (mDenuncia.getLongitude() != 0 && mDenuncia.getLongitude() != 0) {
            miPosicion = new LatLng(mDenuncia.getLatitude(),mDenuncia.getLongitude());
            moveCamara();

        }else{
            Log.e(LOGTAG, "Aun no hay ultima localizacion");
        }
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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

    @OnClick(R.id.lyAgregarFotos) void onAgregarFotos() {
        openGalleryPhotos();
    }
    @OnClick(R.id.imgAgregarFotos) void onImgAgregarFotos() {
        openGalleryPhotos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == PETICION_GALLERY){
                String[] all_path = data.getStringArrayExtra(GalleryActivity.ALL_PATH);
                int[] all_ids = data.getIntArrayExtra(GalleryActivity.ALL_THUMB_IDS);

                mAdapter.AddPhotos(all_path);
                setNumPhotosSelect();
            }else if (requestCode == PETICION_DENUCIA_PARTE_2){
                finish();
            }
        }
    }

    private void openGalleryPhotos(){
        int size = mAdapter.getItemCount();
        if (size <= GalleryActivity.MAX_PHOTOS) {

            CameraPosition camPos = mMap.getCameraPosition();
            miPosicion  = new LatLng(camPos.target.latitude,camPos.target.longitude);
            mDenuncia.setLatitude(miPosicion.latitude);
            mDenuncia.setLongitude(miPosicion.longitude);

            Intent i = new Intent(this, GalleryActivity.class);
            i.putExtra(GalleryActivity.ITEM_COUNT, size);

            String[] datos = new String[size];
            for (int t = 0; t < size; t++)
                datos[t] =  mAdapter.getPhotos().get(t).getPath();

            i.putExtra(GalleryActivity.PATH_PHOTOS_SELECT, datos);
            startActivityForResult(i, PETICION_GALLERY);
        } else {
            mostrarMensaje(getResources().getString(R.string.max_photos));
        }
    }

    GallerySelectedAdapter.OnItemClickListener listenerGallerySelected = new GallerySelectedAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            Foto foto = mAdapter.getPhotos().get(position);
            if (foto.getType() == Enumerator.TipoFoto.BOTON_AGREGAR_MAS) {
                openGalleryPhotos();
            }
        }

        @Override
        public void onCloseClick(View view, int position) {
            mAdapter.RemoveItem(position);
            setNumPhotosSelect();

        }

    };

    private void setNumPhotosSelect() {
        if (mAdapter.getItemCount() <= 1) {
            lyInicial.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            btnSiguiente.setVisibility(View.GONE);
        }
        else {
            lyInicial.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            btnSiguiente.setVisibility(View.VISIBLE);
        }
    }

    private void llenarDenuncia(){
        mDenuncia.getFotos().clear();
        if (mAdapter.getItemCount() > 1) {
            List<Foto> lstFotos = new ArrayList<>(mAdapter.getPhotos());
            lstFotos.remove(0);
            mDenuncia.setFotos(lstFotos);
        }
        CameraPosition camPos = mMap.getCameraPosition();
        miPosicion  = new LatLng(camPos.target.latitude,camPos.target.longitude);
        mDenuncia.setLatitude(miPosicion.latitude);
        mDenuncia.setLongitude(miPosicion.longitude);

        SexaDecimalCoordinate sexaDecimalCoordinate = new SexaDecimalCoordinate(mDenuncia.getLatitude(),mDenuncia.getLongitude());
        sexaDecimalCoordinate.ConvertToFlatCoordinate();
        mDenuncia.setNorte(sexaDecimalCoordinate.get_coorPlanaNorteFinal());
        mDenuncia.setEste(sexaDecimalCoordinate.get_coorPlanaEsteFinal());

    }

    @OnClick(R.id.btnSiguiente) void onSiguiente(){
        llenarDenuncia();
        if (mDenuncia.getFotos().size() == 0){
            mostrarMensaje("Es necesario ingresar alguna foto");
        }else if (mDenuncia.getLatitude() == 0 && mDenuncia.getLongitude() == 0){
                mostrarMensaje(getResources().getString(R.string.validacion_ubicacion));
        }else{
            obtenterLugarXCoordenda();
        }
    }

    private void obtenterLugarXCoordenda(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(getResources().getString(R.string.verificando_ubicacion));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mLugarXCoordenadaPresenter.getId(mDenuncia.getNorteString(),mDenuncia.getEsteString());
    }

    @Override
    public void onSuccessIdLugarXCoordenada(String idLugar) {
        Log.d("id lugar x coorde", idLugar);
       if (idLugar != null &&  !idLugar.isEmpty() && !idLugar.equals("0")){
           mDenuncia.setMunicipioQueja(idLugar);
           mElevationPresenter.getElevation(mDenuncia.getLatitude(),mDenuncia.getLongitude());
       }else{
           if (mProgressDialog != null) mProgressDialog.dismiss();
           mostrarMensajeDialog(getResources().getString(R.string.error_juridiccion_car));
       }
    }

    @Override
    public void onErrorIdLugarXCoordenada(ErrorApi errorApi) {
        if (mProgressDialog != null) mProgressDialog.dismiss();
        Snackbar.make(lyDenuncia,errorApi.getMessage(), Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green))
                .setAction("REINTENTAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        obtenterLugarXCoordenda();
                    }
                })
                .show();
    }

    @Override
    public void onSuccessElevation(double elevation) {
        if (mProgressDialog != null) mProgressDialog.dismiss();
        mDenuncia.setAltitud(elevation);
        Intent i = new Intent(this, DenunciaAmbiental2Activity.class);
        startActivityForResult(i,PETICION_DENUCIA_PARTE_2);
    }

    @Override
    public void onErrrorElevation(int statusCode) {
        if (mProgressDialog != null) mProgressDialog.dismiss();
        Snackbar.make(lyDenuncia, getResources().getString(R.string.error_load_elevation), Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green))
                .setAction("REINTENTAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mElevationPresenter.getElevation(mDenuncia.getLatitude(),mDenuncia.getLongitude());
                    }
                })
                .show();
    }

}
