package car.gov.co.carserviciociudadano.petcar.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONException;

import java.util.List;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
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
public class MapPETCARFragment extends Fragment implements OnMapReadyCallback, IViewContenedor {
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
    public MapPETCARFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapPETCARFragment.
     */
    // TODO: Rename and change types and number of parameters
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
            MapsInitializer.initialize(this.getActivity());
            mMapView = (MapView) rootView.findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
            mContenedorPresenter = new ContenedorPresenter(this);
        }
        catch (InflateException e){
            Log.e(TAG, "Inflate exception");
        }
        return rootView;
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
        mClusterManager = new ClusterManager<>(getActivity(), mMap);
        mMap.setOnCameraIdleListener(mClusterManager);

        mContenedorPresenter.getContenedores();
    }

    @Override
    public void onError(ErrorApi error) {

    }

    @Override
    public void onSuccess(List<Contenedor> lstContenedores) {

       if (lstContenedores != null && lstContenedores.size() > 0) {
           mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lstContenedores.get(0).Latitude, lstContenedores.get(0).Longitude), 8));
           mClusterManager.clearItems();
           mClusterManager.addItems(lstContenedores);
       }

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
