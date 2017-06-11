package car.gov.co.carserviciociudadano.consultapublica.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.consultapublica.adapter.ExpedienteAdapter;
import car.gov.co.carserviciociudadano.consultapublica.dataaccesss.Expedientes;
import car.gov.co.carserviciociudadano.consultapublica.interfaces.IExpediente;
import car.gov.co.carserviciociudadano.consultapublica.model.Expediente;
import car.gov.co.carserviciociudadano.consultapublica.model.ExpedienteResumen;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class BuscarExpedienteActivity extends BaseActivity {

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.txtNroExpediente)  EditText txtNroExpediente;
    @BindView(R.id.txtNombre)  EditText txtNuombre;
    @BindView(R.id.txtIdUsuario)  EditText txtIdUsuario;
    @BindView(R.id.activity_buscar_expediente) View activity_buscar_expediente;
    @BindView(R.id.progressView) ProgressBar progressView;

    private FirebaseAnalytics mFirebaseAnalytics;
    ExpedienteAdapter mAdaptador;
    List<Expediente> mLstExpedientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_expediente);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);


        mLstExpedientes = new ArrayList<>();
        mAdaptador = new ExpedienteAdapter(mLstExpedientes);
        mAdaptador.setOnClickListener(onClickListener);
        mRecyclerView.setAdapter(mAdaptador);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundleAnalitic = new Bundle();
        bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_NAME, "Buscar expediente");
        bundleAnalitic.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Enumerator.ContentTypeAnalitic.CONSULTA_EXPEDIENTE);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleAnalitic);
    }

    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(Expedientes.TAG);
        super.onPause();
    }
    @OnClick(R.id.btnBuscar) void onBuscar(){
        buscar();
    }


    private  boolean validar(){
        int numero = Utils.convertInt(txtNroExpediente.getText().toString());

        if (numero == 0 && txtNuombre.getText().toString().trim().isEmpty() && txtIdUsuario.getText().toString().trim().isEmpty() ){
            mostrarMensaje(getString(R.string.ingrese_datos_busqueda),activity_buscar_expediente);
            return false;
        }

        if (numero == 0 && txtIdUsuario.getText().toString().trim().isEmpty() && txtNuombre.getText().toString().trim().length() < 4 ) {
            mostrarMensaje("Nombre de usuario muy corto",activity_buscar_expediente);
            return false;
        }

        return  true;
    }

    private void buscar(){
        ocultarTeclado(activity_buscar_expediente);

        if(validar()) {
            showProgress(progressView, true);
            new Expedientes().list(Utils.convertInt(txtNroExpediente.getText().toString()), txtNuombre.getText().toString().trim(), txtIdUsuario.getText().toString(), new IExpediente() {
                @Override
                public void onSuccess(List<Expediente> lstExpediente) {
                    showProgress(progressView, false);
                    mLstExpedientes.clear();
                    mLstExpedientes.addAll(lstExpediente);
                    mAdaptador.notifyDataSetChanged();
                }

                @Override
                public void onSuccess(ExpedienteResumen ExpedienteResumen) {

                }

                @Override
                public void onError(ErrorApi error) {
                    showProgress(progressView, false);
                    String mensaje = error.getStatusCode() == 404 ? getString(R.string.no_hay_expediente) : error.getMessage();

                    mostrarMensaje(mensaje, activity_buscar_expediente);
                    mLstExpedientes.clear();
                    mAdaptador.notifyDataSetChanged();
                }
            });
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int position = mRecyclerView.getChildAdapterPosition(v);
            Expediente expediente = mLstExpedientes.get(position);

            Intent i = new Intent(getApplicationContext(), ExpedienteActivity.class);
            i.putExtra(Expediente.ID_EXPEDIENTE,expediente.getIDExpediente());
            startActivity(i);

        }
    };
}
