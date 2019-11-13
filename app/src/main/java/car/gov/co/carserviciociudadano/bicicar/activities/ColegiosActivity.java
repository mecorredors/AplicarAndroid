package car.gov.co.carserviciociudadano.bicicar.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.PreferencesApp;
import car.gov.co.carserviciociudadano.bicicar.adapter.ColegiosAdapter;;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Colegios;
import car.gov.co.carserviciociudadano.bicicar.model.Colegio;
import car.gov.co.carserviciociudadano.bicicar.presenter.ColegiosPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewColegio;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class ColegiosActivity extends BaseActivity implements IViewColegio, ColegiosAdapter.ColegiosListener{
    @BindView(R.id.recycler_view)   RecyclerView recyclerView;
    @BindView(R.id.btnSincronizarDatos) Button btnSincronizarDatos;
    @BindView(R.id.txtBuscar) EditText txtBuscar;
    @BindView(R.id.btnBuscar) ImageButton btnBuscar;

    ColegiosAdapter mAdaptador;
    List<Colegio> mLstColegios = new ArrayList<>();
    List<Colegio> mLstCopyColegios = new ArrayList<>();
    ColegiosPresenter colegioPresenter;
    public static final String ULTIMA_BUSQUEDA = "ultima_busqueda";
    public static final int REQUEST_UBICACION = 100;
    boolean publicar = false;
    boolean isSelector = false;
    public static final String  IS_SELECTOR = "is_selectror";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colegios);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        if (b != null){
            isSelector = b.getBoolean(IS_SELECTOR, false);
            if (isSelector){
                btnSincronizarDatos.setVisibility(View.GONE);
            }
        }

        recyclerView.setHasFixedSize(true);
        mAdaptador = new ColegiosAdapter(mLstColegios, isSelector);
        mAdaptador.setColegiosListener(this);
        recyclerView.setAdapter(mAdaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        colegioPresenter = new ColegiosPresenter(this);

        List<Colegio> lstColegios =  colegioPresenter.listLocal();
        if (lstColegios.size() > 0){
            onSuccessColegios(lstColegios);
            btnSincronizarDatos.setVisibility(View.VISIBLE);
            String ultimaBusqueda = PreferencesApp.getDefault(PreferencesApp.READ).getString(ULTIMA_BUSQUEDA,null);
            if (ultimaBusqueda != null){
                txtBuscar.setText(ultimaBusqueda);
                filter(ultimaBusqueda);
            }

        }else {
            obtenerColegios();
            btnSincronizarDatos.setVisibility(View.GONE);
        }

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
    private void actualizarColegiosLocal(){
        List<Colegio> lstColegios =  colegioPresenter.listLocal();
        if (lstColegios.size() > 0) {
            onSuccessColegios(lstColegios);
        }
    }

    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(Colegios.TAG);
        super.onPause();

    }

    private void filter(String text){
        mLstColegios.clear();
        if (text != null && text.trim().length() > 0) {
            String filter = text.trim().toLowerCase();

            for (Colegio item : mLstCopyColegios) {
                if (item.Municipio.toLowerCase().contains(filter) || item.Nombre.toLowerCase().contains(filter)) {
                    mLstColegios.add(item);
                }
            }
            if (mLstColegios.size() > 0) {
                PreferencesApp.getDefault(PreferencesApp.WRITE).putString(ULTIMA_BUSQUEDA, filter).commit();
            }

        }else{
            mLstColegios.addAll(mLstCopyColegios);
        }
        mAdaptador.notifyDataSetChanged();
        txtBuscar.clearFocus();
        InputMethodManager in = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(txtBuscar.getWindowToken(), 0);

    }

    private  void obtenerColegios(){
        mostrarProgressDialog("Descargando datos");
        colegioPresenter.list();
    }


    @OnClick(R.id.btnSincronizarDatos) void onSincronizarDatos(){
        publicar = true;
        obtenerColegios();
    }
    @OnClick(R.id.btnBuscar) void onBuscar(){
        filter(txtBuscar.getText().toString());
    }


    @Override
    public void onSuccessColegios(List<Colegio> lstColegios) {
        ocultarProgressDialog();
        mLstColegios.clear();
        mLstColegios.addAll(lstColegios);
        mLstCopyColegios = new ArrayList<>(mLstColegios);
       // Collections.copy(mLstCopyColegios ,mLstColegios );
        mAdaptador.notifyDataSetChanged();
        if (publicar){
            mostrarProgressDialog("Publicando datos");
            colegioPresenter.publicar();
        }
    }

    @Override
    public void onSuccess(Colegio colegio) {
        ocultarProgressDialog();
        publicar = false;
        actualizarColegiosLocal();
    }


    @Override
    public void onErrorColegios(ErrorApi errorApi) {
        ocultarProgressDialog();
        if (errorApi.getStatusCode() == 404)
            mostrarMensajeDialog(errorApi.getMessage());
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ColegiosActivity.this);
            builder.setMessage(errorApi.getMessage());
            builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    obtenerColegios();
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
    public void onEstudiantes(int position, View view) {
        Intent i = new Intent(this, BeneficiariosXColegioActivity.class);
        Colegio colegio = mLstColegios.get(position);
        if (colegio != null) {
            i.putExtra(BeneficiariosXColegioActivity.ID_COLEGIO, colegio.IDColegio);
        }
        startActivity(i);
    }

    @Override
    public void onUbicacion(int position, View view) {
        Colegio colegio = mLstColegios.get(position);
        if (colegio != null) {
            Intent i = new Intent(this, UbicacionBeneficiarioActivity.class);
            i.putExtra(Colegio.ID_COLEGIO, colegio.IDColegio);
            startActivityForResult(i, REQUEST_UBICACION);
        }
    }
    @Override
    public void onSelected(int position, View view) {
        Colegio colegio = mLstColegios.get(position);
        if (colegio != null) {
            Intent i = getIntent();
            i.putExtra(Colegio.ID_COLEGIO, colegio.IDColegio);
            i.putExtra(Colegio.NOMBRE, colegio.Nombre);
            setResult(RESULT_OK, i);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_UBICACION){
            actualizarColegiosLocal();
        }
    }
}
