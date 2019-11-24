package car.gov.co.carserviciociudadano.petcar.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.PreferencesApp;
import car.gov.co.carserviciociudadano.bicicar.activities.EventoActivity;
import car.gov.co.carserviciociudadano.bicicar.activities.UbicacionBeneficiarioActivity;
import car.gov.co.carserviciociudadano.bicicar.model.Lugar;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewLugares;
import car.gov.co.carserviciociudadano.bicicar.presenter.LugaresPresenter;
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


public class ContenedoresActivity extends BaseActivity implements IViewContenedor, IViewLugares {
    @BindView(R.id.recycler_view)   RecyclerView recyclerView;
    @BindView(R.id.btnSincronizarDatos) Button btnSincronizarDatos;
    @BindView(R.id.btnCrearContedor) Button btnCrearContedor;
    //@BindView(R.id.txtBuscar) EditText txtBuscar;
   // @BindView(R.id.btnBuscar) ImageButton btnBuscar;
    @BindView(R.id.spiMunicipio) Spinner spiMunicipio;
    @BindView(R.id.lblTitle) TextView lblTitle;

    ContenedoresAdapter mAdaptador;
    List<Contenedor> mLstContenedores = new ArrayList<>();
    List<Contenedor> mLstCopyContenedors = new ArrayList<>();
    List<Lugar> mLstMunicipios = new ArrayList<>();
    ArrayAdapter<Lugar> adapterMunicipios;
    public static final String ULTIMA_BUSQUEDA = "ultima_busqueda";
    public static final int REQUEST_UBICACION = 100;
    boolean publicar = false;
    boolean isSelector = false;
    public static final String  IS_SELECTOR = "is_selectror";

    private ContenedorPresenter contenedorPresenter;
    public static  final int REQUEST_REGISTRAR_MATERIAL = 101;
    public static  final int REQUEST_AGREGAR_CONTENEDOR = 102;
    LugaresPresenter municipiosPresenter;
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
            if (mGestor.getTipoGestor() == Gestor.Tipo.INSTALADOR || mGestor.getTipoGestor() == Gestor.Tipo.INSTALDOR_VISITA  ) {
                btnSincronizarDatos.setVisibility(View.GONE);
                lblTitle.setText(getString(R.string.titulo_crear_contenedor));
            }else{
                btnCrearContedor.setVisibility(View.GONE);
            }
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

        municipiosPresenter = new LugaresPresenter(this);
        mostrarProgressDialog("Cargando");
        municipiosPresenter.obtenerMunicipios();

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
                Lugar municipio = mLstMunicipios.get(i);
                if (municipio != null ){
                    if (mGestor.getTipoGestor() == Gestor.Tipo.INSTALADOR || mGestor.getTipoGestor() == Gestor.Tipo.INSTALDOR_VISITA  ) {
                        obtenerContenedores(municipio.getIDLugar());
                    }else{
                        obtenerContenedoresLocal(municipio.getIDLugar());
                    }
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
        Lugar municipio = (Lugar) spiMunicipio.getSelectedItem();
        if (municipio != null) {
            obtenerContenedores(municipio.getIDLugar());
        }
    }

    @OnClick(R.id.btnCrearContedor) void onCrearContenedor(){
        Intent i = new Intent(ContenedoresActivity.this, ContenedorActivity.class);

        Lugar municipio = (Lugar) spiMunicipio.getSelectedItem();
        if (municipio != null) {
            i.putExtra(Contenedor.IDMUNICIPIO, municipio.getIDLugar());
            i.putExtra(Contenedor.MUNICIPIO, municipio.toString());
        }
        startActivityForResult(i, REQUEST_AGREGAR_CONTENEDOR);
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
        mostrarMensajeDialog(error.getMessage());
        mLstContenedores.clear();
        mLstCopyContenedors.clear();
        mAdaptador.notifyDataSetChanged();
    }

    @Override
    public void onSuccessAgregar(Contenedor contenedor) {

    }

    @Override
    public void onSuccessModificar(Contenedor contenedor) {

    }

    @Override
    public void onErrorAgregar(ErrorApi error) {

    }

    @Override
    public void onErrorModificar(ErrorApi error) {

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int position = recyclerView.getChildAdapterPosition(v);
            Contenedor contenedor = mLstContenedores.get(position);
            if (mGestor.getTipoGestor() == Gestor.Tipo.INSTALADOR || mGestor.getTipoGestor() == Gestor.Tipo.INSTALDOR_VISITA  ) {
                Intent i = new Intent(ContenedoresActivity.this, ContenedorActivity.class);
                i.putExtra(Contenedor.IDCONTENEDOR, contenedor.IDContenedor);
                i.putExtra(Contenedor.IDMUNICIPIO, contenedor.IDMunicipio);

                Lugar municipio = (Lugar) spiMunicipio.getSelectedItem();
                if (municipio != null) {
                    i.putExtra(Contenedor.MUNICIPIO, municipio.toString());
                }
                startActivityForResult(i, REQUEST_AGREGAR_CONTENEDOR);
            }else if  (mGestor.getTipoGestor() == Gestor.Tipo.RECICLADOR ){
                Intent i = new Intent(ContenedoresActivity.this, RegistrarMaterialActivity.class);
                i.putExtra(Contenedor.CODIGO, contenedor.Codigo);
                startActivityForResult(i, REQUEST_REGISTRAR_MATERIAL);
            }

        }
    };

    public int getMunicipiosPosition(String idMunicipio){
        int i = 0;
        for (Lugar item : mLstMunicipios) {
            if (item.getIDLugar().trim().equals(idMunicipio))
                return i;
            i++;
        }
        return 0;
    }

    @Override
    public void onSuccessMunicipios(List<Lugar> lstMunicipios) {
        ocultarProgressDialog();
        lstMunicipios.remove(0);
        mLstMunicipios.clear();
        mLstMunicipios.addAll(lstMunicipios);
        adapterMunicipios.notifyDataSetChanged();

        if (mGestor != null){
            spiMunicipio.setSelection(getMunicipiosPosition(mGestor.IDMunicipio.trim()));
        }
    }

    @Override
    public void onSuccessVeredas(List<Lugar> lstVeredas) {

    }

    @Override
    public void onErrorMunicipios(ErrorApi errorApi) {
        ocultarProgressDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(ContenedoresActivity.this);
        builder.setMessage(errorApi.getMessage());
        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mostrarProgressDialog("Cargando");
                municipiosPresenter.obtenerMunicipios();
                dialog.dismiss();

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

    @Override
    public void onErrorVeredas(ErrorApi errorApi) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_AGREGAR_CONTENEDOR){
                Lugar municipio = (Lugar) spiMunicipio.getSelectedItem();
                if (municipio != null) {
                    obtenerContenedoresLocal(municipio.getIDLugar());
                }

            }
        }
    }
}
