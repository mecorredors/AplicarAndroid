package car.gov.co.carserviciociudadano.consultapublica.activities;

import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.consultapublica.adapter.DocumentosRespuestaAdapter;
import car.gov.co.carserviciociudadano.consultapublica.dataaccesss.Tramites;
import car.gov.co.carserviciociudadano.consultapublica.model.RadicadosDigitalesRespuesta;
import car.gov.co.carserviciociudadano.consultapublica.model.Tramite;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.activities.IntentHelper;

public class DocumentosRespuestaActivity extends BaseActivity {

    @BindView(R.id.recycler_view)    RecyclerView mRecyclerView;
    private FirebaseAnalytics mFirebaseAnalytics;
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

        if (BuildConfig.DEBUG == false) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundleAnalitic = new Bundle();
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_ID, "Documentos respuesta");
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_NAME, "Documentos respuesta");
            bundleAnalitic.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Enumerator.ContentTypeAnalitic.CONSULTA_TRAMITE);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleAnalitic);
        }
    }
}
