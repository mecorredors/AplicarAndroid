package car.gov.co.carserviciociudadano.petcar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.PreferencesApp;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.adapter.ContenedoresAdapter;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Contenedores;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Gestores;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;
import car.gov.co.carserviciociudadano.petcar.model.Gestor;
import car.gov.co.carserviciociudadano.petcar.model.Municipio;
import car.gov.co.carserviciociudadano.petcar.presenter.ContenedorPresenter;
import car.gov.co.carserviciociudadano.petcar.presenter.IViewContenedor;
import car.gov.co.carserviciociudadano.petcar.presenter.IViewMunicipios;
import car.gov.co.carserviciociudadano.petcar.presenter.MunicipiosPresenter;


public class ContenedoresActivity extends BaseActivity implements IViewContenedor, IViewMunicipios {
    @BindView(R.id.recycler_view)   RecyclerView recyclerView;
    @BindView(R.id.btnSincronizarDatos) Button btnSincronizarDatos;
    //@BindView(R.id.txtBuscar) EditText txtBuscar;
   // @BindView(R.id.btnBuscar) ImageButton btnBuscar;
    @BindView(R.id.spiMunicipio) Spinner spiMunicipio;

    ContenedoresAdapter mAdaptador;
    List<Contenedor> mLstContenedores = new ArrayList<>();
    List<Contenedor> mLstCopyContenedors = new ArrayList<>();
    List<Municipio> mLstMunicipios = new ArrayList<>();
    ArrayAdapter<Municipio> adapterMunicipios;
    public static final String ULTIMA_BUSQUEDA = "ultima_busqueda";
    public static final int REQUEST_UBICACION = 100;
    boolean publicar = false;
    boolean isSelector = false;
    public static final String  IS_SELECTOR = "is_selectror";

    private ContenedorPresenter contenedorPresenter;
    public static  final int REQUEST_REGISTRAR_MATERIAL = 101;
    MunicipiosPresenter municipiosPresenter;
    Gestor mGestor;
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


        mAdaptador = new ContenedoresAdapter(mLstContenedores);
        recyclerView.setAdapter(mAdaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdaptador.setOnClickListener(onClickListener);

        mGestor = new Gestores().getLogin();
        if (mGestor != null) {
            obtenerContenedoresLocal(mGestor.IDMunicipio);
        }
        //txtBuscar.setImeActionLabel("Buscar", KeyEvent.KEYCODE_SEARCH);
       // txtBuscar.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
       /* txtBuscar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    filter(txtBuscar.getText().toString().trim());
                    handled = true;
                }
                return handled;
            }
        });*/

        municipiosPresenter = new MunicipiosPresenter(this);
        mostrarProgressDialog("Cargando");
        municipiosPresenter.getMunicipios();

        adapterMunicipios = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstMunicipios);
        adapterMunicipios.setDropDownViewResource( R.layout.simple_spinner_dropdown_item);
        spiMunicipio.setAdapter(adapterMunicipios);

        onItemSelecteMunicipio();
    }


   private void onItemSelecteMunicipio(){
        spiMunicipio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // txtBuscar.setText("");
                Municipio municipio = mLstMunicipios.get(i);
                if (municipio != null ){
                    obtenerContenedoresLocal(municipio.ID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

    }

   private void filter(String text){
        mLstContenedores.clear();
        if (text != null && text.trim().length() > 0) {
            String filter = text.trim().toLowerCase();

            for (Contenedor item : mLstCopyContenedors) {
                if (item.Municipio.toLowerCase().contains(filter) || item.Direccion.toLowerCase().contains(filter)) {
                    mLstContenedores.add(item);
                }
            }
            if (mLstContenedores.size() > 0) {
                PreferencesApp.getDefault(PreferencesApp.WRITE).putString(ULTIMA_BUSQUEDA, filter).commit();
            }

        }else{
            mLstContenedores.addAll(mLstCopyContenedors);
        }
        mAdaptador.notifyDataSetChanged();
     //   txtBuscar.clearFocus();
       // InputMethodManager in = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
       // in.hideSoftInputFromWindow(txtBuscar.getWindowToken(), 0);

    }

    private  void obtenerContenedoresLocal(String idMunicipio){
        mLstContenedores.clear();
        mLstContenedores.addAll(new Contenedores().listByMunicipio(idMunicipio));
        mAdaptador.notifyDataSetChanged();
    }
    private  void obtenerContenedores(String idMunicipio){
        mostrarProgressDialog("Descargando contenedores");
        contenedorPresenter = new ContenedorPresenter(this);
        contenedorPresenter.getContenedores(idMunicipio);
    }


    @OnClick(R.id.btnSincronizarDatos) void onSincronizarDatos(){
        publicar = true;
        Municipio municipio = (Municipio) spiMunicipio.getSelectedItem();
        if (municipio != null) {
            obtenerContenedores(municipio.ID);
        }
    }
   /* @OnClick(R.id.btnBuscar) void onBuscar(){
        filter(txtBuscar.getText().toString());
    }*/

    @Override
    public void onSuccessContenedores(List<Contenedor> lstContenedores) {
        ocultarProgressDialog();
        Contenedores contenedores =  new Contenedores();
        for (Contenedor item : lstContenedores){
            contenedores.guardar(item);
        }
        
        mLstContenedores.clear();
        mLstContenedores.addAll(lstContenedores);        
        mAdaptador.notifyDataSetChanged();
    }

    @Override
    public void onErrorContenedores(ErrorApi error) {
        ocultarProgressDialog();
        mostrarProgressDialog(error.getMessage());
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int position = recyclerView.getChildAdapterPosition(v);
            Contenedor contenedor = mLstContenedores.get(position);

            Intent i = new Intent(ContenedoresActivity.this, RegistrarMaterialActivity.class);
            i.putExtra(Contenedor.CODIGO, contenedor.Codigo);
            startActivityForResult(i,REQUEST_REGISTRAR_MATERIAL);

        }
    };

    @Override
    public void onSuccess(List<Municipio> lstMunicipios) {
        ocultarProgressDialog();
        lstMunicipios.remove(0);
        mLstMunicipios.clear();
        mLstMunicipios.addAll(lstMunicipios);
        adapterMunicipios.notifyDataSetChanged();

        if (mGestor != null){
            spiMunicipio.setSelection(getMunicipiosPosition(mGestor.IDMunicipio.trim()));
        }

    }

    public int getMunicipiosPosition(String idMunicipio){
        int i = 0;
        for (Municipio item : mLstMunicipios) {
            if (item.ID.trim().equals(idMunicipio))
                return i;
            i++;
        }
        return 0;
    }


    @Override
    public void onError(ErrorApi errorApi) {
        ocultarProgressDialog();
        mostrarMensaje(errorApi.getMessage());
    }
}
