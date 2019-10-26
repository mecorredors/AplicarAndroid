package car.gov.co.carserviciociudadano.petcar.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.PreferencesApp;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.petcar.adapter.ContenedoresAdapter;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Contenedores;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;


public class ContenedoresActivity extends BaseActivity {
    @BindView(R.id.recycler_view)   RecyclerView recyclerView;
    @BindView(R.id.btnSincronizarDatos) Button btnSincronizarDatos;
    @BindView(R.id.txtBuscar) EditText txtBuscar;
    @BindView(R.id.btnBuscar) ImageButton btnBuscar;

    ContenedoresAdapter mAdaptador;
    List<Contenedor> mLstContenedors = new ArrayList<>();
    List<Contenedor> mLstCopyContenedors = new ArrayList<>();

    public static final String ULTIMA_BUSQUEDA = "ultima_busqueda";
    public static final int REQUEST_UBICACION = 100;
    boolean publicar = false;
    boolean isSelector = false;
    public static final String  IS_SELECTOR = "is_selectror";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenedores);
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

        mLstContenedors.addAll(new Contenedores().list(null));

        mAdaptador = new ContenedoresAdapter(mLstContenedors);
        recyclerView.setAdapter(mAdaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


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


    @Override
    public void onPause() {
        super.onPause();

    }

    private void filter(String text){
        mLstContenedors.clear();
        if (text != null && text.trim().length() > 0) {
            String filter = text.trim().toLowerCase();

            for (Contenedor item : mLstCopyContenedors) {
                if (item.Municipio.toLowerCase().contains(filter) || item.Direccion.toLowerCase().contains(filter)) {
                    mLstContenedors.add(item);
                }
            }
            if (mLstContenedors.size() > 0) {
                PreferencesApp.getDefault(PreferencesApp.WRITE).putString(ULTIMA_BUSQUEDA, filter).commit();
            }

        }else{
            mLstContenedors.addAll(mLstCopyContenedors);
        }
        mAdaptador.notifyDataSetChanged();
        txtBuscar.clearFocus();
        InputMethodManager in = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(txtBuscar.getWindowToken(), 0);

    }

    private  void obtenerContenedors(){

    }


    @OnClick(R.id.btnSincronizarDatos) void onSincronizarDatos(){
        publicar = true;
        obtenerContenedors();
    }
    @OnClick(R.id.btnBuscar) void onBuscar(){
        filter(txtBuscar.getText().toString());
    }

}
