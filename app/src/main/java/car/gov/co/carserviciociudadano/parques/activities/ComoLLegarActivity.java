package car.gov.co.carserviciociudadano.parques.activities;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.Language;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.common.LocationBaseGoogleApiActivity;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.model.Parque;



public class ComoLLegarActivity extends LocationBaseGoogleApiActivity implements OnMapReadyCallback  {

    @BindView(R.id.lblDuracion)   TextView lblDuracion;
    @BindView(R.id.lblDistancia)  TextView lblDistancia;
    @BindView(R.id.progressView)
    ProgressBar progressView;

    private Parque mParque;
    private GoogleMap mapa;

    LatLng destinoLatLng; //= new LatLng(5.141814, -73.971206);
    //  LatLng miPosicion = new LatLng(4.710483, -74.102274);
    LatLng miPosicion = new LatLng(0, 0);

    private int colorMejorRuta = 0xFF1976D2;
    private int colorRutaAlterna = 0xFF757575;

    public final static int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 2;
    private static final String LOGTAG = "android-localizacion";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_como_llegar);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        mParque = (Parque) IntentHelper.getObjectForKey(Parques.TAG);
        destinoLatLng = new LatLng(mParque.getLatitude(),mParque.getLongitude());
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        //Construcción cliente API Google
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapa = map;
        //mapa.getUiSettings().setMapToolbarEnabled(false);
        mapa.getUiSettings().setZoomControlsEnabled(true);

        insertarMarcador();
        moverCamara();

        startGoogleApiClient();
        enableLocationUpdates();

      //  getPosition();

       /* if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);

        } else {
            mapa.setMyLocationEnabled(true);
        }
*/

        mapa.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location arg0) {
                miPosicion = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                // mapa.addMarker(new MarkerOptions().position(miPosicion).title("Mi posición").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location_blue_16dp)));
                dibujarRuta();

               // mapa.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
            }
        });

    }



    private void insertarMarcador() {
        mapa.addMarker(new MarkerOptions().position(destinoLatLng).title(mParque.getNombreParque()));


    }

    private void moverCamara() {
        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(destinoLatLng, 10);
        mapa.moveCamera(camUpd1);
    }

    @OnClick(R.id.btnComoLlegar)
    void onComoLLegar() {
        // Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");
        Uri gmmIntentUri = Uri.parse("google.navigation:q=5.141814, -73.971206");
        //  Uri gmmIntentUri = Uri.parse("geo:0,0?q=5.141814, -73.971206(Represa del Neusa)");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }

   /* @Override
    protected void resultLocation(Location location) {
        miPosicion = new LatLng(location.getLatitude(), location.getLongitude());
        // mapa.addMarker(new MarkerOptions().position(miPosicion).title("Mi posición").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location_blue_16dp)));
        dibujarRuta();
        Log.d("latitude ", "" + location.getLatitude());

    }*/

    private void dibujarRuta() {
        progressView.setVisibility(View.VISIBLE);
        GoogleDirection.withServerKey("AIzaSyBFDLpORhJMBRMWDxb_4D2YrAcIA1LY3bM")
                .from(miPosicion)
                .to(destinoLatLng)
                .transportMode(TransportMode.DRIVING)
                .alternativeRoute(true)
                .language(Language.SPANISH)
                //.avoid(AvoidType.TOLLS) //evitar peajes
                //.avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        progressView.setVisibility(View.GONE);
                        String status = direction.getStatus();
                        Log.d("direction ", "Status: " + status + " " + direction.getErrorMessage());
                        String duracion = "";
                        String distancia = "";

                        if (direction.isOK()) {
                            Route route1 = direction.getRouteList().get(0);
                            Leg leg1 = route1.getLegList().get(0);
                            Info durationInfo1 = leg1.getDuration();
                            duracion = durationInfo1.getText();
                            distancia = leg1.getDistance().getText();
                            Log.d("duration", durationInfo1.getText() + " valor: " + durationInfo1.getValue());
                            Log.d("distacia", leg1.getDistance().getText());
                            Route route2;
                            if (direction.getRouteList().size() > 1) {
                                route2 = direction.getRouteList().get(1);
                                Leg leg2 = route2.getLegList().get(0);
                                Info durationInfo2 = leg2.getDuration();

                                if (Utils.convertLong(durationInfo1.getValue()) < Utils.convertLong(durationInfo2.getValue())) {
                                    dibujarRuta(route2, colorRutaAlterna);
                                    dibujarRuta(route1, colorMejorRuta);

                                } else {
                                    dibujarRuta(route1, colorRutaAlterna);
                                    dibujarRuta(route2, colorMejorRuta);
                                    duracion = durationInfo2.getText();
                                    distancia = leg2.getDistance().getText();
                                }
                            } else {
                                dibujarRuta(route1, colorMejorRuta);
                            }

                            lblDistancia.setText(distancia);
                            lblDuracion.setText(duracion);
                            Log.d("routes ", "routes: " + direction.getRouteList().size());

                        } else {
                            mostrarMensaje(status + " " + direction.getErrorMessage() + " No fue posible obtener la ruta");
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        progressView.setVisibility(View.GONE);
                        mostrarMensaje(" No fue posible obtener la ruta");
                    }
                });
    }

    private void dibujarRuta(Route route, int color) {
        Leg leg = route.getLegList().get(0);
        ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
        PolylineOptions polylineOptions = DirectionConverter.createPolyline(ComoLLegarActivity.this, directionPositionList, 6, color);
        mapa.addPolyline(polylineOptions);
    }

   /*  @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
       switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED  ) {
                    try {
                        mapa.setMyLocationEnabled(true);
                    }catch (SecurityException ex){
                    }
                } else {
                    mostrarMensaje("Permiso denegado para obtener ubicación");
                }
                return;
            }
        }
    }
*/


    @Override
    public void onLastLocation(Location loc) {
        if (loc != null) {
            miPosicion = new LatLng(loc.getLatitude(), loc.getLongitude());
            // mapa.addMarker(new MarkerOptions().position(miPosicion).title("Mi posición").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location_blue_16dp)));
            dibujarRuta();
            Log.i(LOGTAG, "Ultima ubicacion conocidad");
        } else {
            Log.e(LOGTAG, "Aun no hay ultima localizacion");
        }
    }


    @Override
    public void onLocationChanged(Location loc) {
         if (loc != null) {
             miPosicion = new LatLng(loc.getLatitude(), loc.getLongitude());
             // mapa.addMarker(new MarkerOptions().position(miPosicion).title("Mi posición").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location_blue_16dp)));
             dibujarRuta();
             disableLocationUpdates();
             Log.i(LOGTAG, "Recibida nueva ubicación!");
         } else {
             Log.e(LOGTAG, "No hay localizacion");
         }
    }

}
