package car.gov.co.carserviciociudadano.bicicar.activities;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.bicicar.adapter.BeneficiariosAdapter;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Asistentes;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Colegios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Eventos;
import car.gov.co.carserviciociudadano.bicicar.model.Asistente;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Colegio;
import car.gov.co.carserviciociudadano.bicicar.model.Evento;
import car.gov.co.carserviciociudadano.bicicar.presenter.AsistentesPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.BeneficiarioPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewAsistente;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewBeneficiario;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class BeneficiariosActivity extends BaseActivity implements IViewBeneficiario, BeneficiariosAdapter.BeneficiarioListener  {
    @BindView(R.id.recycler_view)   RecyclerView recyclerView;
    @BindView(R.id.btnGuardarAsistencia) Button btnGuardarAsistencia;
    @BindView(R.id.txtBuscar) EditText txtBuscar;
    @BindView(R.id.btnBuscar) ImageButton btnBuscar;
    @BindView(R.id.spiColegio) Spinner spiColegio;
    @BindView(R.id.lySpiColegios)    View lySpiColegios;
    Beneficiario mBeneficiarioLogin;
    BeneficiariosAdapter mAdaptador;
    List<Beneficiario> mLstBeneficiarios = new ArrayList<>();
    List<Beneficiario> mLstCopyBeneficiarios = new ArrayList<>();
    List<Colegio> mLstColegios = new ArrayList<>();
    ArrayAdapter<Colegio> adapterColegios;
    BeneficiarioPresenter beneficiarioPresenter;
    private Evento mEvento;
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
        lySpiColegios.setVisibility(View.GONE);
        if (mEvento != null) {
            if (mEvento.getTipoEvento().Publico){

                lySpiColegios.setVisibility(View.VISIBLE);

                mLstColegios.addAll(new Colegios().conEstudiates());
                adapterColegios = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstColegios);
                adapterColegios.setDropDownViewResource( R.layout.simple_spinner_dropdown_item);
                spiColegio.setAdapter(adapterColegios);

                lstBeneficiarios = mLstColegios.size() > 0 ? beneficiarioPresenter.listLocal(mLstColegios.get(0).IDColegio) : beneficiarioPresenter.listAllLocal();
                onItemSelecteColegio();

                if (mLstColegios.size() == 0){
                    mostrarMensajeDialog("Debe descargar instituciones y estudiantes para registrar asistencia de otras instituciones");
                }

            }else {
                lstBeneficiarios = beneficiarioPresenter.listLocal(mEvento.IDColegio);
            }
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

        txtBuscar.setImeActionLabel("Buscar", KeyEvent.KEYCODE_SEARCH);
        txtBuscar.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        txtBuscar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    filter(txtBuscar.getText().toString().trim());
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void onItemSelecteColegio(){
        spiColegio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txtBuscar.setText("");
                Colegio colegio = mLstColegios.get(i);
                if (colegio != null ){
                    List<Beneficiario> lstBeneficiarios =  beneficiarioPresenter.listLocal(colegio.IDColegio);
                    asistentesPresenter.desabilitarYaRegistrados(lstBeneficiarios, mEvento.IDEvento);
                    onSuccess(lstBeneficiarios);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(Beneficiarios.TAG);
        super.onPause();

    }

    @Override
    public void onBackPressed(){
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id== android.R.id.home){
            if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                getSupportFragmentManager().popBackStack();
            else
                this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void filter(String text){
        if (btnGuardarAsistencia.getVisibility() == View.VISIBLE){
            mostrarMensajeDialog("Debe guardar los seleccionados antes de hacer otra búsqueda ");
            return;
        }

        mLstBeneficiarios.clear();
        if (text != null && text.trim().length() > 0) {
            String filter = text.trim().toLowerCase();

            for (Beneficiario item : mLstCopyBeneficiarios) {
                if (item.Nombres.toLowerCase().contains(filter) || item.Apellidos.toLowerCase().contains(filter)) {
                    mLstBeneficiarios.add(item);
                }
            }
           // if (mLstColegios.size() > 0) {
             //   PreferencesApp.getDefault(PreferencesApp.WRITE).putString(ULTIMA_BUSQUEDA, filter).commit();
           // }

        }else{
            mLstBeneficiarios.addAll(mLstCopyBeneficiarios);
        }
        mAdaptador.notifyDataSetChanged();
        txtBuscar.clearFocus();
        InputMethodManager in = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(txtBuscar.getWindowToken(), 0);

    }

    private  void obtenerBeneficiarios(){
        mostrarProgressDialog("Descargando estudiantes");
        if (mEvento != null) {
            beneficiarioPresenter.list(mEvento.IDColegio);
        }else{
            beneficiarioPresenter.list(mBeneficiarioLogin.Curso, mBeneficiarioLogin.IDColegio);
        }
    }

    @OnClick(R.id.btnBuscar) void onBuscar(){
        filter(txtBuscar.getText().toString());
    }

    @OnClick(R.id.btnGuardarAsistencia) void onGuardarAsistencia(){
        guardarAsistencia();
    }

    private  void guardarAsistencia(){
        boolean actualizarEstadoEvento = false;
        for (Beneficiario b : mLstBeneficiarios){
            Asistentes asistentes = new Asistentes();
            if (b.Selected && b.Enabled) {
               if (mEvento != null && mEvento.getTipoEvento() != null){
                   if (!mEvento.getTipoEvento().Recorrido || mEvento.getTipoEvento().Publico){
                       asistentes.insert(new Asistente(mEvento.IDEvento, b.IDBeneficiario, Enumerator.Estado.PENDIENTE_PUBLICAR));
                   }else{
                       beneficiarioPresenter.GuardarLogTrayecto(b, mBeneficiarioLogin, mEvento);
                   }
                   actualizarEstadoEvento = true;

               }else {
                   beneficiarioPresenter.GuardarLogTrayecto(b, mBeneficiarioLogin);
               }

               b.Enabled = false;
            }
        }

        if (actualizarEstadoEvento){
            mEvento.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR;
            new Eventos().update(mEvento);
        }

        if (mEvento != null && mEvento.getTipoEvento() != null){
            beneficiarioPresenter.desabilitarYaRegistrados(mLstCopyBeneficiarios, mEvento.IDEvento);
            asistentesPresenter.desabilitarYaRegistrados(mLstCopyBeneficiarios, mEvento.IDEvento);
            mAdaptador.notifyDataSetChanged();
            mostrarMensaje("Asistencia guardada");
        }else {
            setResult(RESULT_OK);
            finish();
        }


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
        mLstCopyBeneficiarios = new ArrayList<>(mLstBeneficiarios);
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
