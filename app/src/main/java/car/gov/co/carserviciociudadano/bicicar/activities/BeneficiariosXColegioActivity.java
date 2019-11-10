package car.gov.co.carserviciociudadano.bicicar.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.bicicar.adapter.BeneficiariosXColegioAdapter;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.presenter.BeneficiarioPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewBeneficiario;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class BeneficiariosXColegioActivity extends BaseActivity implements IViewBeneficiario, BeneficiariosXColegioAdapter.BeneficiarioListener, View.OnClickListener {
    @BindView(R.id.recycler_view)   RecyclerView recyclerView;
    @BindView(R.id.btnSincronizarDatos) Button btnSincronizarDatos;

    Beneficiario mBeneficiarioLogin;
    BeneficiariosXColegioAdapter mAdaptador;
    List<Beneficiario> mLstBeneficiarios = new ArrayList<>();
    BeneficiarioPresenter beneficiarioPresenter;
    private int mIdColegio;
    public static final String  ID_COLEGIO = "ID_COLEGIO";
    boolean publicar = false;
    public static final int REQUEST_UBICACION = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiarios_x_colegio);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        mBeneficiarioLogin  = Beneficiarios.readBeneficio();
        //if (mBeneficiarioLogin != null)
          //  bar.setTitle( mBeneficiarioLogin.Nombres + " " + mBeneficiarioLogin.Apellidos);

        recyclerView.setHasFixedSize(true);
        mAdaptador = new BeneficiariosXColegioAdapter(mLstBeneficiarios);
        mAdaptador.setBeneficiarioListener(this);
        mAdaptador.setOnClickListener(this);
        recyclerView.setAdapter(mAdaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        beneficiarioPresenter = new BeneficiarioPresenter(this);

        Bundle b = getIntent().getExtras();
        if (b != null){
            mIdColegio = b.getInt(ID_COLEGIO , 0);
        }

        if (mIdColegio > 0){
            List<Beneficiario> lstBeneficiarios = beneficiarioPresenter.listLocal(mIdColegio);
            if (lstBeneficiarios.size() > 0){
                onSuccess(lstBeneficiarios);
            }else{
                obtenerBeneficiarios();
            }
        }

    }

    private void actualizarEstudiantesLocal(){
        List<Beneficiario> lstBeneficiarios = beneficiarioPresenter.listLocal(mIdColegio);
        if (lstBeneficiarios.size() > 0){
            onSuccess(lstBeneficiarios);
        }
    }

    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(Beneficiarios.TAG);
        super.onPause();

    }
    private  void obtenerBeneficiarios(){
        mostrarProgressDialog("Descargando estudiantes");
        if (mIdColegio > 0)
            beneficiarioPresenter.list(mIdColegio);
        else
            beneficiarioPresenter.list(mBeneficiarioLogin.Curso, mBeneficiarioLogin.IDColegio);
    }

   /* @OnClick(R.id.btnEliminar) void onEliminar(){
        new LogTrayectos().DeleteAll();
        Intent i = new Intent();
        this.setResult(RESULT_OK , i);
        finish();
    }*/

    @OnClick(R.id.btnSincronizarDatos) void onSincronizarDatos(){
        publicar = true;
        obtenerBeneficiarios();
    }


    @Override
    public void onSuccess(Beneficiario beneficiario) {
        ocultarProgressDialog();
        publicar = false;
        actualizarEstudiantesLocal();
    }

    @Override
    public void onSuccess(List<Beneficiario> lstBeneficiarios) {
        ocultarProgressDialog();
        mLstBeneficiarios.clear();
        mLstBeneficiarios.addAll(lstBeneficiarios);
        mAdaptador.notifyDataSetChanged();

        if (publicar){
            mostrarProgressDialog("Publicando datos");
            beneficiarioPresenter.publicar();
        }
    }

    @Override
    public void onError(ErrorApi errorApi) {
        publicar = false;
        ocultarProgressDialog();
        mostrarMensajeDialog(errorApi.getMessage());
    }

    @Override
    public void onErrorListarItems(ErrorApi errorApi) {
        ocultarProgressDialog();
        if (errorApi.getStatusCode() == 404)
            mostrarMensajeDialog(errorApi.getMessage());
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(BeneficiariosXColegioActivity.this);
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
    }

    @Override
    public void onUbicacion(int position, View view) {
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        Beneficiario beneficiario = mLstBeneficiarios.get(position);
        if (beneficiario != null) {
            Intent i = new Intent(this, UbicacionBeneficiarioActivity.class);
            i.putExtra(Beneficiario.ID_BENEFICIARIO, beneficiario.IDBeneficiario);
            startActivityForResult(i, REQUEST_UBICACION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_UBICACION){
            actualizarEstudiantesLocal();
        }
    }
}
