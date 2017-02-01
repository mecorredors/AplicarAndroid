package car.gov.co.carserviciociudadano.consultapublica.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.consultapublica.adapter.DocumentosRespuestaAdapter;
import car.gov.co.carserviciociudadano.consultapublica.adapter.ExpedienteAdapter;
import car.gov.co.carserviciociudadano.consultapublica.dataaccesss.Expedientes;
import car.gov.co.carserviciociudadano.consultapublica.dataaccesss.Tramites;
import car.gov.co.carserviciociudadano.consultapublica.interfaces.IExpediente;
import car.gov.co.carserviciociudadano.consultapublica.model.Expediente;
import car.gov.co.carserviciociudadano.consultapublica.model.ExpedienteResumen;
import car.gov.co.carserviciociudadano.consultapublica.model.RadicadosDigitalesRespuesta;
import car.gov.co.carserviciociudadano.parques.activities.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class BuscarExpedienteActivity extends BaseActivity {

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.txtNroExpediente)  EditText txtNroExpediente;
    @BindView(R.id.txtNombre)  EditText txtNuombre;
    @BindView(R.id.txtIdUsuario)  EditText txtIdUsuario;
    @BindView(R.id.activity_buscar_expediente) View activity_buscar_expediente;
    @BindView(R.id.progressView) ProgressBar progressView;

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

        mRecyclerView.setAdapter(mAdaptador);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
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
                    mostrarMensaje(error.getMessage(), activity_buscar_expediente);
                    mLstExpedientes.clear();
                    mAdaptador.notifyDataSetChanged();
                }
            });
        }
    }
}
