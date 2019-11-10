package car.gov.co.carserviciociudadano.consultapublica.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.consultapublica.adapter.DocumentoExpedienteAdapter;
import car.gov.co.carserviciociudadano.consultapublica.dataaccesss.Documentos;
import car.gov.co.carserviciociudadano.consultapublica.interfaces.IDocumento;
import car.gov.co.carserviciociudadano.consultapublica.model.Documento;
import car.gov.co.carserviciociudadano.consultapublica.model.Expediente;
import car.gov.co.carserviciociudadano.parques.fragments.BaseFragment;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


public class DocumentosExpedienteFragment extends BaseFragment {

    @BindView(R.id.recycler_view)   RecyclerView mRecyclerView;
    @BindView(R.id.fragment_documento)   View fragment_documento;
    @BindView(R.id.progressView) ProgressBar progressView;
    @BindView(R.id.lblStatus)   TextView lblEstatus;
    @BindView(R.id.btnReintentar)   Button btnReintentar;

    private OnFragmentInteractionListener mListener;
    private int mIdExpediente;
    DocumentoExpedienteAdapter mAdaptador;
    List<Documento> mLstDocumentos;
    public DocumentosExpedienteFragment() {

    }


    public static DocumentosExpedienteFragment newInstance(int idExpediente) {
        DocumentosExpedienteFragment fragment = new DocumentosExpedienteFragment();
        Bundle args = new Bundle();
        args.putInt(Expediente.ID_EXPEDIENTE, idExpediente);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIdExpediente = getArguments().getInt(Expediente.ID_EXPEDIENTE,0);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_documentos_expediente, container, false);
        ButterKnife.bind(this, view);
        mLstDocumentos = new ArrayList<>();
        mAdaptador = new DocumentoExpedienteAdapter(mLstDocumentos);
        mRecyclerView.setAdapter(mAdaptador);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        obtenerDocumentos();
        return  view;

    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(Documentos.TAG);
        super.onPause();

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }



    @OnClick(R.id.btnReintentar) void onReintentar() {
        obtenerDocumentos();
    }

    private void obtenerDocumentos(){
        showProgress(progressView,true);
        lblEstatus.setVisibility(View.GONE);
        btnReintentar.setVisibility(View.GONE);
        new Documentos().list(mIdExpediente, new IDocumento() {
            @Override
            public void onSuccess(List<Documento> lstDocumentos) {
                mLstDocumentos.clear();
                mLstDocumentos.addAll(lstDocumentos);
                mAdaptador.notifyDataSetChanged();
                showProgress(progressView,false);
            }

            @Override
            public void onError(ErrorApi error) {
                showProgress(progressView,false);
                lblEstatus.setVisibility(View.VISIBLE);
                lblEstatus.setText(error.getMessage());

                if (error.getStatusCode() == 404)
                    lblEstatus.setText("No hay documentos");
                else
                    btnReintentar.setVisibility(View.VISIBLE);


            }
        });
    }

}
