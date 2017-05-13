package car.gov.co.carserviciociudadano.consultapublica.activities;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.consultapublica.adapter.DocumentosRespuestaAdapter;
import car.gov.co.carserviciociudadano.consultapublica.dataaccesss.Tramites;
import car.gov.co.carserviciociudadano.consultapublica.model.RadicadosDigitalesRespuesta;
import car.gov.co.carserviciociudadano.consultapublica.model.Tramite;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.activities.IntentHelper;

public class DocumentosRespuestaActivity extends BaseActivity {

    @BindView(R.id.recycler_view)    RecyclerView mRecyclerView;
    DocumentosRespuestaAdapter mAdaptador;
    List<RadicadosDigitalesRespuesta> mLstDocumentosRespuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentos_respuesta);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);


        Tramite tramite = (Tramite) IntentHelper.getObjectForKey(Tramites.TAG);
        mLstDocumentosRespuesta = new ArrayList<>();
        mLstDocumentosRespuesta.addAll(tramite.getRadicadosDigitalesRespuesta());
        mAdaptador = new DocumentosRespuestaAdapter(mLstDocumentosRespuesta);

        mRecyclerView.setAdapter(mAdaptador);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }
}
