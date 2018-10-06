package car.gov.co.carserviciociudadano.parques.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.parques.activities.DetalleReservaActivity;
import car.gov.co.carserviciociudadano.parques.activities.IntentHelper;
import car.gov.co.carserviciociudadano.parques.activities.UsuarioActivity;
import car.gov.co.carserviciociudadano.parques.adapter.MisReservasAdapter;
import car.gov.co.carserviciociudadano.parques.dataaccess.DetalleReservas;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.dataaccess.Usuarios;
import car.gov.co.carserviciociudadano.parques.interfaces.IDetalleReserva;
import car.gov.co.carserviciociudadano.parques.model.DetalleReserva;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Usuario;


public class MisReservasFragment extends BaseFragment {

    @BindView(R.id.lblHeader)   TextView mLblHeader;
    @BindView(R.id.btnConsultar)   Button mBtnConsultar;

    private RecyclerView mRecyclerView;
    MisReservasAdapter mAdaptador;
    List<DetalleReserva> mLstDetalleReservas;
    private ProgressBar mProgressView;
    Usuario mUsuario;
    private FirebaseAnalytics mFirebaseAnalytics;
    public MisReservasFragment() {

    }

    public static MisReservasFragment newInstance() {
        MisReservasFragment fragment = new MisReservasFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_mis_reservas, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLstDetalleReservas = new ArrayList<>();
        mAdaptador = new MisReservasAdapter(mLstDetalleReservas);

        mAdaptador.setOnClickListener(onClickListener);

        mRecyclerView.setAdapter(mAdaptador);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mProgressView = (ProgressBar) view.findViewById(R.id.progressView);

        Usuarios usuarios = new Usuarios();
        mUsuario = usuarios.leer();

        loadDetalleReservas();

        if (BuildConfig.DEBUG == false) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
            Bundle bundleAnalitic = new Bundle();
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_ID, "Mis reservas");
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_NAME, "Mis reservas");
            bundleAnalitic.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Enumerator.ContentTypeAnalitic.PARQUES);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleAnalitic);
        }
        return view;
    }

    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(DetalleReservas.TAG);
        super.onPause();
    }
    @Override
    public void onDestroy() {
        AppCar.VolleyQueue().cancelAll(DetalleReservas.TAG);
        super.onDestroy();
    }
    private void loadDetalleReservas() {

        if (mUsuario.getIdUsuario() > 0){

            DetalleReservas detalleReservas = new DetalleReservas();
            showProgress(mProgressView, true);

            detalleReservas.list(mUsuario.getLogin(), 0, new IDetalleReserva() {
                @Override
                public void onSuccess(List<DetalleReserva> lista) {
                    if (isAdded()) {
                        showProgress(mProgressView, false);
                        mLstDetalleReservas.clear();
                        mLstDetalleReservas.addAll(lista);
                        mAdaptador.notifyDataSetChanged();
                        mLblHeader.setText(getString(R.string.header_mis_reservas));
                        mBtnConsultar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(ErrorApi error) {
                    if (isAdded()) {
                        showProgress(mProgressView, false);
                        mBtnConsultar.setVisibility(View.VISIBLE);
                        mBtnConsultar.setText("CONSULTAR");
                        if (error.getStatusCode() == 404) {
                            mLblHeader.setText(getString(R.string.header_sin_reservas));
                            mLstDetalleReservas.clear();
                            mAdaptador.notifyDataSetChanged();
                        } else {
                            mLblHeader.setText(error.getMessage());
                        }
                    }
                }
            });
        } else{
            mLblHeader.setText("Aun no tiene reservas, Ingrese con su usuario para consultar y realizar reservas ");
            mBtnConsultar.setVisibility(View.VISIBLE);
            mBtnConsultar.setText("INGRESAR");
            mLstDetalleReservas.clear();
            mAdaptador.notifyDataSetChanged();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int position = mRecyclerView.getChildAdapterPosition(v);
            DetalleReserva item = mLstDetalleReservas.get(position);

            IntentHelper.addObjectForKey(item,DetalleReservas.TAG);
            Intent i = new Intent(getActivity(), DetalleReservaActivity.class);
            startActivity(i);

        }
    };

    @OnClick(R.id.btnConsultar) void onConsultar() {

       if (mUsuario.getIdUsuario() > 0 ) {
           loadDetalleReservas();
       }else{
           Intent i = new Intent(getActivity(), UsuarioActivity.class);
           i.putExtra(UsuarioActivity.ORIGIN, UsuarioActivity.ORIGEN_MANIN_PARQUES);
           startActivityForResult(i,0);
       }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mUsuario = new Usuarios().leer();
        loadDetalleReservas();

    }
}
