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
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.parques.activities.DetalleParqueActivity;
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
        else
            mostrarMensaje("Aún no tienes reservas");

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
            }

            @Override
            public void onError(ErrorApi error) {
                showProgress(mProgressView,false);
                Snackbar.make(mRecyclerView, error.getMessage(), Snackbar.LENGTH_INDEFINITE)
                        .setActionTextColor(ContextCompat.getColor(getContext(), R.color.green) )
                        .setAction("REINTENTAR", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loadDetalleReservas();
                            }
                        })
                        .show();
            }
        });


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int position = mRecyclerView.getChildAdapterPosition(v);
            DetalleReserva item = mLstDetalleReservas.get(position);

            Intent i = new Intent(getActivity(), DetalleParqueActivity.class);
//            i.putExtra(Parque.ID_PARQUE,item.getIDParque());
            startActivity(i);

        }
    };
}
