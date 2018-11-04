package car.gov.co.carserviciociudadano.bicicar.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.bicicar.adapter.BeneficiariosAdapter;
import car.gov.co.carserviciociudadano.bicicar.adapter.LogTrayectoAdapter;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.LogTrayectos;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.presenter.BeneficiarioPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewBeneficiario;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class BeneficiariosActivity extends BaseActivity implements IViewBeneficiario, BeneficiariosAdapter.BeneficiarioListener{
    @BindView(R.id.recycler_view)   RecyclerView recyclerView;
    @BindView(R.id.progressBar)   ProgressBar progressBar;
    @BindView(R.id.btnGuardarAsistencia) Button btnGuardarAsistencia;

    Beneficiario mBeneficiarioLogin;
    BeneficiariosAdapter mAdaptador;
    List<Beneficiario> mLstBeneficiarios = new ArrayList<>();
    BeneficiarioPresenter beneficiarioPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiarios);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        mBeneficiarioLogin  = Beneficiarios.readBeneficio();
        if (mBeneficiarioLogin != null)
            bar.setTitle( mBeneficiarioLogin.Nombres + " " + mBeneficiarioLogin.Apellidos);

        recyclerView.setHasFixedSize(true);
        mAdaptador = new BeneficiariosAdapter(mLstBeneficiarios);
        mAdaptador.setBeneficiarioListener(this);
        recyclerView.setAdapter(mAdaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        beneficiarioPresenter = new BeneficiarioPresenter(this);
        obtenerBeneficiarios();
        activarBoton();
    }

    private  void obtenerBeneficiarios(){
        progressBar.setVisibility(View.VISIBLE);
        beneficiarioPresenter.listarItems(mBeneficiarioLogin.Curso);
    }

   /* @OnClick(R.id.btnEliminar) void onEliminar(){
        new LogTrayectos().DeleteAll();
        Intent i = new Intent();
        this.setResult(RESULT_OK , i);
        finish();
    }*/

    @OnClick(R.id.btnGuardarAsistencia) void onGuardarAsistencia(){
        AlertDialog.Builder builder = new AlertDialog.Builder(BeneficiariosActivity.this);
        builder.setMessage("Seguro desea guardar la asitencia");
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                guardarAsistencia();

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }

    private  void guardarAsistencia(){
        for (Beneficiario b : mLstBeneficiarios){
            if (b.Selected)
                beneficiarioPresenter.GuardarLogTrayecto(b, mBeneficiarioLogin);
        }

        Intent i = new Intent();
        this.setResult(RESULT_OK , i);
        finish();

    }

    private void activarBoton(){
        int count = 0;
        for (Beneficiario b : mLstBeneficiarios) {
            if (b.Selected)
                count ++;
        }

        btnGuardarAsistencia.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSuccess(Beneficiario beneficiario) {

    }

    @Override
    public void onSuccess(List<Beneficiario> lstBeneficiarios) {
        progressBar.setVisibility(View.GONE);
        mLstBeneficiarios.clear();
        mLstBeneficiarios.addAll(lstBeneficiarios);
        mAdaptador.notifyDataSetChanged();
    }

    @Override
    public void onError(ErrorApi errorApi) {

    }

    @Override
    public void onErrorListarItems(ErrorApi errorApi) {
        progressBar.setVisibility(View.GONE);
        if (errorApi.getStatusCode() == 404)
            mostrarMensajeDialog(errorApi.getMessage());
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(BeneficiariosActivity.this);
            builder.setMessage(errorApi.getMessage());
            builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    obtenerBeneficiarios();
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    @Override
    public void onSuccessRecordarClave(String mensaje) {

    }

    @Override
    public void onErrorRecordarClave(ErrorApi error) {

    }

    @Override
    public void onCheckedAsistenacia(int position, CompoundButton compoundButton, boolean b) {
        Beneficiario beneficiario = mLstBeneficiarios.get(position);
        if (beneficiario != null){
            beneficiario.Selected = b;
        }
        activarBoton();
    }
}
