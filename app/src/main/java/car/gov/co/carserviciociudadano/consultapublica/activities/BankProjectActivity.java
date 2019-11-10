package car.gov.co.carserviciociudadano.consultapublica.activities;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

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
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.consultapublica.adapter.BankProjectAdapter;
import car.gov.co.carserviciociudadano.consultapublica.dataaccesss.BankProjects;
import car.gov.co.carserviciociudadano.consultapublica.interfaces.IBankProyects;
import car.gov.co.carserviciociudadano.consultapublica.model.BankProjectDocument;
import car.gov.co.carserviciociudadano.consultapublica.model.BankProjectItem;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class BankProjectActivity extends BaseActivity {

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.txtCodigo)  EditText txtCodigo;
    @BindView(R.id.activity_bank_project) View activity_bank_project;
    @BindView(R.id.progressView) ProgressBar progressView;

    private FirebaseAnalytics mFirebaseAnalytics;
    BankProjectAdapter mAdaptador;
    List<BankProjectDocument> mLstBankProjectDocument;
    BankProjectItem mBankProjectItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_project);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);


        mLstBankProjectDocument = new ArrayList<>();
        mAdaptador = new BankProjectAdapter(mBankProjectItem, mLstBankProjectDocument);
        mAdaptador.setOnClickListener(onClickListener);
        mRecyclerView.setAdapter(mAdaptador);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (BuildConfig.DEBUG == false) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundleAnalitic = new Bundle();
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_ID, "Proyecto Cofinanciación");
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_NAME, "Proyecto Cofinanciación");
            bundleAnalitic.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Enumerator.ContentTypeAnalitic.CONSULTA_PROYECTO_COFIN);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleAnalitic);
        }
    }

    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(BankProjects.TAG);
        super.onPause();
    }
    @Override
    public void onDestroy() {
        AppCar.VolleyQueue().cancelAll(BankProjects.TAG);
        super.onDestroy();
    }
    @OnClick(R.id.btnBuscar) void onBuscar(){
        buscar();
    }


    private  boolean validar(){

        if (txtCodigo.getText().toString().trim().isEmpty()){
            mostrarMensaje(getString(R.string.ingrese_datos_busqueda),activity_bank_project);
            return false;
        }
        return  true;
    }

    private void buscar(){
        ocultarTeclado(activity_bank_project);

        if(validar()) {
            showProgress(progressView, true);

            new BankProjects().getProject(txtCodigo.getText().toString().trim(), new IBankProyects() {
                @Override
                public void onSuccessProyect(BankProjectItem backProyectItem) {
                    mLstBankProjectDocument.clear();
                    mAdaptador.setBankProjectItem(backProyectItem);
                    mLstBankProjectDocument.add(0, new BankProjectDocument());
                    mAdaptador.notifyDataSetChanged();
                    obtenerDocumentos(backProyectItem.getProjectID());
                }

                @Override
                public void onSuccessDocuments(List<BankProjectDocument> lstBankProyectDocuments) {

                }

                @Override
                public void onError(ErrorApi error) {
                    showProgress(progressView, false);
                    String mensaje = error.getStatusCode() == 404 ? getString(R.string.no_hay_proyecto_cofinanciacion) : error.getMessage();

                    mostrarMensaje(mensaje, activity_bank_project);
                    mLstBankProjectDocument.clear();
                    mAdaptador.notifyDataSetChanged();
                }
            });

        }
    }

    private void obtenerDocumentos(int proyectId){
        new BankProjects().getDocuments(proyectId, new IBankProyects() {
            @Override
            public void onSuccessProyect(BankProjectItem backProyectItem) {

            }

            @Override
            public void onSuccessDocuments(List<BankProjectDocument> lstBankProyectDocuments) {
                showProgress(progressView, false);
                mLstBankProjectDocument.addAll(lstBankProyectDocuments );
                mAdaptador.notifyDataSetChanged();
            }

            @Override
            public void onError(ErrorApi error) {
                showProgress(progressView, false);
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = mRecyclerView.getChildAdapterPosition(v);
            Log.d("posicion",String.valueOf(position));
        }
    };
}
