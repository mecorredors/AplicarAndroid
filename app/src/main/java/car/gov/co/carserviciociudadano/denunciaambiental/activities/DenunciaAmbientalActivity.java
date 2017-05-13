package car.gov.co.carserviciociudadano.denunciaambiental.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.common.LocationBaseGoogleApiActivity;
import car.gov.co.carserviciociudadano.denunciaambiental.adapter.GallerySelectedAdapter;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Foto;

public class DenunciaAmbientalActivity extends LocationBaseGoogleApiActivity implements OnMapReadyCallback {
    @BindView(R.id.lyInicial)  LinearLayout lyInicial;
    @BindView(R.id.gridGallery)    GridView gridView;

    private GoogleMap mMap;
    LatLng miPosicion = new LatLng(0, 0);
    private static final String LOGTAG = "Denuncia ambiental";
    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_GALLERY = 102;
    GallerySelectedAdapter mAdapter;
    public ArrayList<Foto> mFotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia_ambiental);
        ButterKnife.bind(this);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mAdapter = new GallerySelectedAdapter(this, mFotos);
        mAdapter.SetOnItemClickListener(listenerGallerySelected);
        gridView.setAdapter(mAdapter);
        setNumPhotosSelect();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        //mapa.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
       // mMap.setPadding(0,0,0,80);
        startGoogleApiClient();
      //  enableLocationUpdates();
        mMap.setMyLocationEnabled(true);

         if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);

        } else {
            mMap.setMyLocationEnabled(true);
        }

    }

    private void moveCamara() {
      //  mMap.addMarker(new MarkerOptions().position(miPosicion).title(getResources().getString(R.string.ubicacion_denuncia)));
        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(miPosicion, 10);
        mMap .moveCamera(camUpd1);
    }


    @Override
    public void onLastLocation(Location loc) {
        if (loc != null) {
            miPosicion = new LatLng(loc.getLatitude(), loc.getLongitude());
           // insertarMarcador();
            moveCamara();
            Log.i(LOGTAG, "Ultima ubicacion conocidad");
        } else {
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
            }
        }
    }

    private void openGalleryPhotos(){
        int size = mAdapter.getItemCount();
        if (size <= GalleryActivity.MAX_PHOTOS) {
            Intent i = new Intent(this, GalleryActivity.class);
            i.putExtra(GalleryActivity.ITEM_COUNT, size);

            String[] datos = new String[size];
            for (int t = 0; t < size; t++)
                datos[t] =  mAdapter.Photos.get(t).getPath();

            i.putExtra(GalleryActivity.PATH_PHOTOS_SELECT, datos);
            startActivityForResult(i, PETICION_GALLERY);
        } else {
            mostrarMensaje(getResources().getString(R.string.max_photos));
        }
    }

    GallerySelectedAdapter.OnItemClickListener listenerGallerySelected = new GallerySelectedAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            Foto foto = mAdapter.Photos.get(position);
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
           // linearBtnNext.setBackgroundColor(getResources().getColor(R.color.button_publish_disabled));
        }
        else {
            lyInicial.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
           // linearBtnNext.setBackgroundColor(getResources().getColor(R.color.button_publish));
        }
    }
}
