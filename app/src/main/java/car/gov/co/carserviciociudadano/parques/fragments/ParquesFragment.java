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

import com.anupcowkur.reservoir.Reservoir;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.parques.activities.DetalleParqueActivity;
import car.gov.co.carserviciociudadano.parques.activities.IntentHelper;
import car.gov.co.carserviciociudadano.parques.adapter.ParquesAdapter;
import car.gov.co.carserviciociudadano.parques.presenter.ParquePresenter;
import car.gov.co.carserviciociudadano.parques.presenter.IViewParque;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.dataaccess.ServiciosParque;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;


public class ParquesFragment extends BaseFragment implements  IViewParque {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerView;
    ParquesAdapter mAdaptador;
    List<Parque> mLstParques;
    private ProgressBar mProgressView;
    public ParquesFragment() {
        // Required empty public constructor
    }


    public static ParquesFragment newInstance() {
        ParquesFragment fragment = new ParquesFragment();
       // Bundle args = new Bundle();
       // args.putString(ARG_PARAM1, param1);
     //   fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_parques, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLstParques = new ArrayList<>();
        mAdaptador = new ParquesAdapter(mLstParques);

        mAdaptador.setOnClickListener(onClickListener);

        mRecyclerView.setAdapter(mAdaptador);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mProgressView = (ProgressBar) view.findViewById(R.id.progressView);
        loadParques();

        return view;
    }

    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(Parques.TAG);
        super.onPause();

    }
    private void loadParques(){
        ParquePresenter parques = new ParquePresenter(this);
        showProgress(mProgressView,true);
        parques.list();
    }

   // IViewParque iViewParque =   new IViewParque() {
        @Override
        public void onSuccess(List<Parque> lstParques) {
            showProgress(mProgressView,false);
            mLstParques.clear();
            mLstParques.addAll(lstParques);
            mAdaptador.notifyDataSetChanged();
            removeCacheServicios();
        }

        @Override
        public void onError(ErrorApi error) {
            showProgress(mProgressView,false);
            Snackbar.make(mRecyclerView, error.getMessage(), Snackbar.LENGTH_INDEFINITE)
                    //.setActionTextColor(Color.CYAN)
                    .setActionTextColor(ContextCompat.getColor(getContext(), R.color.green) )
                    .setAction("REINTENTAR", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            loadParques();
                        }
                    })
                    .show();

        }
   // };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int position = mRecyclerView.getChildAdapterPosition(v);
            Parque parque = mLstParques.get(position);

            Intent i = new Intent(getActivity(), DetalleParqueActivity.class);
            IntentHelper.addObjectForKey(parque, Parques.TAG);

            startActivityForResult(i,0);

        }
    };

    private void removeCacheServicios(){
        try {
            for (Parque item : mLstParques) {
                Reservoir.delete( ServiciosParque.TAG+item.getIDParque());
            }
        } catch (Exception e) {
            //failure
        }
    }
}
