package car.gov.co.carserviciociudadano.parques.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.parques.activities.DetalleParqueActivity;
import car.gov.co.carserviciociudadano.parques.activities.DetalleReservaActivity;
import car.gov.co.carserviciociudadano.parques.activities.IntentHelper;
import car.gov.co.carserviciociudadano.parques.activities.UsuarioActivity;
import car.gov.co.carserviciociudadano.parques.adapter.MisReservasAdapter;
import car.gov.co.carserviciociudadano.parques.adapter.ParquesAdapter;
import car.gov.co.carserviciociudadano.parques.dataaccess.DetalleReservas;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.dataaccess.Usuarios;
import car.gov.co.carserviciociudadano.parques.interfaces.IDetalleReserva;
import car.gov.co.carserviciociudadano.parques.interfaces.IParque;
import car.gov.co.carserviciociudadano.parques.model.DetalleReserva;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;
import car.gov.co.carserviciociudadano.parques.model.Usuario;


public class MisReservasFragment extends BaseFragment {

    @BindView(R.id.lblHeader)   TextView mLblHeader;
    @BindView(R.id.btnConsultar)   Button mBtnConsultar;

    private RecyclerView mRecyclerView;
    MisReservasAdapter mAdaptador;
    List<DetalleReserva> mLstDetalleReservas;
    private ProgressBar mProgressView;
    Usuario mUsuario;
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
        if (mUsuario.getIdUsuario() > 0)
           loadDetalleReservas();
        else{
            mLblHeader.setText("Aun no tiene reservas, Ingrese con su usuario para consultar y realizar reservas ");
            mBtnConsultar.setVisibility(View.VISIBLE);
            mBtnConsultar.setText("INGRESAR");
        }


        return view;
    }

    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(Parques.TAG);
        super.onPause();

    }
    private void loadDetalleReservas(){

        DetalleReservas detalleReservas = new DetalleReservas();
        showProgress(mProgressView,true);

        detalleReservas.list(mUsuario.getLogin(),0, new IDetalleReserva() {
            @Override
            public void onSuccess(List<DetalleReserva> lista) {
                showProgress(mProgressView,false);
                mLstDetalleReservas.clear();
                mLstDetalleReservas.addAll(lista);
                mAdaptador.notifyDataSetChanged();
                mLblHeader.setText(getString(R.string.header_mis_reservas));
                mBtnConsultar.setVisibility(View.GONE);
            }

            @Override
            public void onError(ErrorApi error) {
                showProgress(mProgressView,false);
                mBtnConsultar.setVisibility(View.VISIBLE);
                if (error.getStatusCode() == 404){
                    mLblHeader.setText(getString(R.string.header_sin_reservas));

                }else{
                    mLblHeader.setText(error.getMessage());
                    mBtnConsultar.setText("CONSULTAR");
                }
            }
        });


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
           i.putExtra(UsuarioActivity.ORIGIN, UsuarioActivity.ORIGEN_RESERVA);
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
