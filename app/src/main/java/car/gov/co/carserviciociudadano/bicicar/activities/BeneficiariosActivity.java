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
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.bicicar.adapter.BeneficiariosAdapter;
import car.gov.co.carserviciociudadano.bicicar.adapter.LogTrayectoAdapter;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Asistentes;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Eventos;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.LogTrayectos;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.TiposEvento;
import car.gov.co.carserviciociudadano.bicicar.model.Asistente;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Evento;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.model.TipoEvento;
import car.gov.co.carserviciociudadano.bicicar.presenter.AsistentesPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.BeneficiarioPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewAsistente;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewBeneficiario;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class BeneficiariosActivity extends BaseActivity implements IViewBeneficiario, BeneficiariosAdapter.BeneficiarioListener  {
    @BindView(R.id.recycler_view)   RecyclerView recyclerView;
    @BindView(R.id.btnGuardarAsistencia) Button btnGuardarAsistencia;

    Beneficiario mBeneficiarioLogin;
    BeneficiariosAdapter mAdaptador;
    List<Beneficiario> mLstBeneficiarios = new ArrayList<>();
    BeneficiarioPresenter beneficiarioPresenter;
    private Evento mEvento;
    private TipoEvento mTipoEvento;
    private AsistentesPresenter asistentesPresenter;
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


        Bundle b = getIntent().getExtras();
        if (b != null){
            int idEvento = b.getInt(Evento.ID_EVENTO,0);
            mEvento = new Eventos().read(idEvento);
            mTipoEvento = new TiposEvento().read(mEvento.IDTipoEvento);
        }

        recyclerView.setHasFixedSize(true);
        mAdaptador = new BeneficiariosAdapter(mLstBeneficiarios);
        mAdaptador.setBeneficiarioListener(this);
        recyclerView.setAdapter(mAdaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        beneficiarioPresenter = new BeneficiarioPresenter(this);
        asistentesPresenter = new AsistentesPresenter(iViewAsistente);

        List<Beneficiario> lstBeneficiarios;
        if (mEvento != null) {
            lstBeneficiarios = beneficiarioPresenter.listLocal( mEvento.IDColegio);
            beneficiarioPresenter.desabilitarYaRegistrados(lstBeneficiarios, mEvento.IDEvento);
            asistentesPresenter.desabilitarYaRegistrados(lstBeneficiarios, mEvento.IDEvento);
        }else{
            //cuando registra asistencia el lider grupo
            lstBeneficiarios = beneficiarioPresenter.listLocal(mBeneficiarioLogin.Curso, mBeneficiarioLogin.IDColegio);
            beneficiarioPresenter.desabilitarYaRegistrados(lstBeneficiarios, 0);
        }
        if (lstBeneficiarios.size() > 0){
            onSuccess(lstBeneficiarios);
        }else{
            obtenerBeneficiarios();
        }

        activarBoton();
    }

    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(Beneficiarios.TAG);
        super.onPause();

    }
    private  void obtenerBeneficiarios(){
        mostrarProgressDialog("Descargando estudiantes");
        if (mEvento != null) {
            beneficiarioPresenter.list(mEvento.IDColegio);
        }else{
            beneficiarioPresenter.list(mBeneficiarioLogin.Curso, mBeneficiarioLogin.IDColegio);
        }
    }

    @OnClick(R.id.btnGuardarAsistencia) void onGuardarAsistencia(){
        guardarAsistencia();
    }

    private  void guardarAsistencia(){
        boolean actualizarEstadoEvento = false;
        for (Beneficiario b : mLstBeneficiarios){
            Asistentes asistentes = new Asistentes();
            if (b.Selected && b.Enabled) {
               if (mEvento != null && mTipoEvento != null){
                   if (mTipoEvento.Recorrido){
                       beneficiarioPresenter.GuardarLogTrayecto(b, mBeneficiarioLogin, mEvento);
                   }else{
                        asistentes.insert(new Asistente(mEvento.IDEvento, b.IDBeneficiario, Enumerator.Estado.PENDIENTE_PUBLICAR));
                   }
                   actualizarEstadoEvento = true;
               }else {
                   beneficiarioPresenter.GuardarLogTrayecto(b, mBeneficiarioLogin);
               }
            }
        }

        if (actualizarEstadoEvento){
            mEvento.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR;
            new Eventos().update(mEvento);
        }

        Intent i = new Intent();
        this.setResult(RESULT_OK , i);
        finish();
    }

    private void activarBoton(){
        int count = 0;
        for (Beneficiario b : mLstBeneficiarios) {
            if (b.Selected && b.Enabled)
                count ++;
        }

        btnGuardarAsistencia.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSuccess(Beneficiario beneficiario) {

    }

    @Override
    public void onSuccess(List<Beneficiario> lstBeneficiarios) {
        ocultarProgressDialog();
        mLstBeneficiarios.clear();
        mLstBeneficiarios.addAll(lstBeneficiarios);
        mAdaptador.notifyDataSetChanged();
    }

    @Override
    public void onError(ErrorApi errorApi) {

    }

    @Override
    public void onErrorListarItems(ErrorApi errorApi) {
        ocultarProgressDialog();
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

    IViewAsistente iViewAsistente = new IViewAsistente() {
        @Override
        public void onSuccess(int idEvento) {
        }

        @Override
        public void onErrorAsistente(ErrorApi error) {
        }

        @Override
        public void onError(ErrorApi error) {
        }
    };

}
