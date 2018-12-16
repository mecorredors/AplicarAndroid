package car.gov.co.carserviciociudadano.bicicar.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.activities.ComoLLegarActivity;

public class RutaMapaActivity extends BaseActivity implements OnMapReadyCallback {

    @BindView(R.id.lblDuracion) TextView lblDuracion;
    @BindView(R.id.lblDistancia) TextView lblDistancia;
    private GoogleMap mapa;
    String ruta;
    float distancia = 0;
    float duracionMinutos = 0;

    private int colorRuta = 0xFF1976D2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_mapa);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            ruta = bundle.getString(LogTrayecto.RUTA, "");
            duracionMinutos = bundle.getFloat(LogTrayecto.DURACION_MINUTOS, 0);
            distancia = bundle.getFloat(LogTrayecto.DISTANCIA_KM, 0);
            lblDistancia.setText(String.valueOf(distancia));

            int segundos = (int) (duracionMinutos * 60);
            int minutos = segundos / 60;
            segundos = segundos % 60;

            lblDuracion.setText(String.format("%d:%02d", minutos, segundos));
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapa = map;
        //mapa.getUiSettings().setMapToolbarEnabled(false);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        dibujarRuta();

    }

    private void dibujarRuta() {

        ArrayList<LatLng> directionPositionList = (ArrayList<LatLng>) PolyUtil.decode(ruta);
        PolylineOptions polylineOptions = DirectionConverter.createPolyline(RutaMapaActivity.this, directionPositionList, 6, colorRuta);
        mapa.addPolyline(polylineOptions);


        if (directionPositionList.size() > 0){
            CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(directionPositionList.get(0), 16);
            mapa.addMarker(new MarkerOptions().position(directionPositionList.get(0)).title("Inicio").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_initial_map)));

            mapa.addMarker(new MarkerOptions().position(directionPositionList.get(directionPositionList.size() - 1)).title("Destino").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_finish_map)));

            mapa.moveCamera(camUpd1);


        }
    }
}
