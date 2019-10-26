package car.gov.co.carserviciociudadano.petcar.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.parques.activities.ComoLLegarActivity;
import car.gov.co.carserviciociudadano.parques.activities.IntentHelper;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.fragments.BaseFragment;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;
import car.gov.co.carserviciociudadano.petcar.presenter.ContenedorPresenter;
import car.gov.co.carserviciociudadano.petcar.presenter.IViewContenedor;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapPETCARFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapPETCARFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapPETCARFragment extends BaseFragment implements OnMapReadyCallback, IViewContenedor, ClusterManager.OnClusterClickListener<Contenedor>, ClusterManager.OnClusterInfoWindowClickListener<Contenedor>, ClusterManager.OnClusterItemClickListener<Contenedor>, ClusterManager.OnClusterItemInfoWindowClickListener<Contenedor>  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "Map_PETCAR";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    private OnFragmentInteractionListener mListener;

    private GoogleMap mMap;
    MapView mMapView;
    private ContenedorPresenter mContenedorPresenter;
    private ClusterManager<Contenedor> mClusterManager;
    private static final LatLng initialPosition = new LatLng(4.758345, -74.156071);

    private Unbinder unbinder;
    @BindView(R.id.lyDetalle) View mLyDetalle;
    @BindView(R.id.lblDireccion) TextView mLblDireccion;
    @BindView(R.id.lblMunicipio) TextView mLblMunicipio;
    @BindView(R.id.imgFoto) ImageView mImgFoto;
    @BindView(R.id.imgClose) ImageButton mImgClose;

    LatLng latLngSelected = new LatLng(0,0);

    private class ContenedorRenderer extends DefaultClusterRenderer<Contenedor> {
        private final IconGenerator mIconGenerator = new IconGenerator(getContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getContext());
       // private final ImageView mImageView;
       // private final ImageView mClusterImageView;
       // private final int mDimension;

        public ContenedorRenderer() {
            super(getContext(), mMap, mClusterManager);

           /* View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);*/

           // mImageView = new ImageView(getContext());
          //  mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            //mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
           // int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
           // mImageView.setPadding(padding, padding, padding, padding);
           // mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(Contenedor contenedor, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
           // Drawable drawable = getResources().getDrawable(R.mipmap.ic_icon_map_test);

           // mImageView.setImageResource(R.mipmap.ic_map_petcar_2);
           // Bitmap icon = mIconGenerator.makeIcon();

            Drawable d =  getResources().getDrawable(R.mipmap.ic_map_petcar_2);
            Bitmap icon = ((BitmapDrawable)d).getBitmap();

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(contenedor.Direccion);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Contenedor> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
          /*  List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (Person p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Drawable drawable = getResources().getDrawable(p.profilePhoto);
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));*/
          super.onBeforeClusterRendered(cluster, markerOptions);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }


    public MapPETCARFragment() {
        // Required empty public constructor
    }


    public static MapPETCARFragment newInstance(String param1, String param2) {
        MapPETCARFragment fragment = new MapPETCARFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static MapPETCARFragment newInstance() {
        MapPETCARFragment fragment = new MapPETCARFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        try {
            rootView = inflater.inflate(R.layout.fragment_map_petcar, container, false);
            unbinder = ButterKnife.bind(this, rootView);
            MapsInitializer.initialize(this.getActivity());
            mMapView = (MapView) rootView.findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
            mContenedorPresenter = new ContenedorPresenter(this);
        }
        catch (InflateException e){
            Log.e(TAG, "Inflate exception");
        }

        mLyDetalle.setVisibility(View.GONE);

        return rootView;
    }

    public void obtenerContenedores(){

        obtenerContenedores(null);
    }
    public void obtenerContenedores(String idMunicipio){
        mostrarProgressDialog("Cargando");
        if (idMunicipio == null)
            mContenedorPresenter.getContenedores();
        else
            mContenedorPresenter.getContenedores(idMunicipio);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState); mMapView.onSaveInstanceState(outState);
    }
    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap != null) {
            return;
        }
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 8));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mClusterManager = new ClusterManager<>(getActivity(), mMap);
        mMap.setOnCameraIdleListener(mClusterManager);

        mClusterManager.setRenderer(new ContenedorRenderer());
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        obtenerContenedores();
    }

    @Override
    public void onErrorContenedores(ErrorApi error) {
        ocultarProgressDialog();
        mostrarError(error);
    }

    @Override
    public void onSuccessContenedores(List<Contenedor> lstContenedores) {
        ocultarProgressDialog();
        if (isAdded()) {
            if (lstContenedores != null && lstContenedores.size() > 0) {

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lstContenedores.get(0).Latitude, lstContenedores.get(0).Longitude), 8));
                mClusterManager.clearItems();
                mClusterManager.addItems(lstContenedores);
                mClusterManager.cluster();
            }
        }
    }


    @Override
    public boolean onClusterClick(Cluster<Contenedor> cluster) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        final LatLngBounds bounds = builder.build();

        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Contenedor> cluster) {

    }

    @Override
    public boolean onClusterItemClick(Contenedor contenedor) {

        mLyDetalle.setVisibility(View.VISIBLE);
        mLyDetalle.startAnimation(AnimationUtils.loadAnimation(AppCar.getContext(), R.anim.slide_up));
        mLblDireccion.setText(contenedor.Direccion);
        mLblMunicipio.setText(contenedor.Municipio);
        latLngSelected = new LatLng(contenedor.Latitude, contenedor.Longitude);
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Contenedor contenedor) {

    }

    private void mostrarError(ErrorApi errorApi){
        if (errorApi.getStatusCode() == 404)
            return;

        if (isAdded()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(errorApi.getMessage());
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    obtenerContenedores();

                }
            });
            builder.show();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void cerrarDetalle(){
        mLyDetalle.setVisibility(View.GONE);
    }

    @OnClick(R.id.imgClose) void onCerrarDetalle(View view) {
        cerrarDetalle();
        mLyDetalle.startAnimation(AnimationUtils.loadAnimation(AppCar.getContext(), R.anim.slide_dow));

    }
    @OnClick(R.id.btnComoLlegar) void onComoLLegar() {
        Parque parque = new Parque();
        parque.setNombreParque(mLblDireccion.getText().toString());
        parque.setLatitude(latLngSelected.latitude);
        parque.setLongitude(latLngSelected.longitude);

        IntentHelper.addObjectForKey(parque, Parques.TAG);
        Intent i = new Intent(getActivity(), ComoLLegarActivity.class);
        startActivity(i);

    }
}
