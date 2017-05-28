package car.gov.co.carserviciociudadano.denunciaambiental.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Validation;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.denunciaambiental.model.ArchivoAdjunto;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Denuncia;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Foto;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Lugar;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.IViewLugares;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.IViewRadicarPQR;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.LugaresPresenter;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.RadicarPQRPresener;
import car.gov.co.carserviciociudadano.parques.model.Banco;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class DenunciaAmbiental2Activity extends BaseActivity implements IViewLugares,IViewRadicarPQR, AdapterView.OnItemSelectedListener {

    @BindView(R.id.cheAnonimo)   CheckBox cheAnonimo;
    @BindView(R.id.txtNombre)    EditText txtNombre;
    @BindView(R.id.lyNombre)    TextInputLayout lyNombre;
    @BindView(R.id.txtCedula)    EditText txtCedula;
    @BindView(R.id.lyCedula)    TextInputLayout lyCedula;
    @BindView(R.id.txtEmail)    EditText txtEmail;
    @BindView(R.id.lyEmail)    TextInputLayout lyEmail;
    @BindView(R.id.txtDireccion)    EditText txtDireccion;
    @BindView(R.id.lyDireccion)    TextInputLayout lyDireccion;
    @BindView(R.id.spiDepartamento)    Spinner spiDepartamento;
    @BindView(R.id.spiMunicipio)    Spinner spiMunicipio;
    @BindView(R.id.spiVereda)    Spinner spiVereda;
    @BindView(R.id.txtTelefono)    EditText txtTelefono;
    @BindView(R.id.lyTelefono)    TextInputLayout lyTelefono;
    @BindView(R.id.txtComentarios)    EditText txtComentarios;
    @BindView(R.id.lyComentarios)   TextInputLayout lyComentarios;
    @BindView(R.id.lyDatos)   View lyDatos;
    @BindView(R.id.lyDenuncia)   View lyDenuncia;

    Denuncia mDenuncia;
    LugaresPresenter mLugaresPresenter;
    RadicarPQRPresener mRadicarPQRPresenter;
    List<Lugar> mLstDepartamentos = new ArrayList<>();
    ArrayAdapter<Lugar> adapterDepartamentos;
    List<Lugar> mLstMunicipios = new ArrayList<>();
    ArrayAdapter<Lugar> adapterMunicipios;
    List<Lugar> mLstVeredas = new ArrayList<>();
    ArrayAdapter<Lugar> adapterVeredas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia_ambiental2);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        ocultarTeclado(lyDenuncia);

        mDenuncia = Denuncia.newInstance();
        cargarDatos();
        spiDepartamento.setOnItemSelectedListener(this);
        spiMunicipio.setOnItemSelectedListener(this);
        mLugaresPresenter = new LugaresPresenter(this);
        mRadicarPQRPresenter = new RadicarPQRPresener(this);
        mLugaresPresenter.obtenerDepartamentos();

    }

    @Override protected void onPause(){
        super.onPause();
        llenarDenuncia();
    }

    private boolean validar(){
        boolean res = true;
        if (cheAnonimo.isChecked()){
            res = Validation.IsEmpty(txtComentarios, lyComentarios);
        }else {
            res = res && !Validation.IsEmpty(txtNombre, lyNombre);
            res = res && !Validation.IsEmpty(txtEmail, lyEmail);
            res = res && !Validation.IsEmpty(txtComentarios, lyComentarios);
        }
        return res;
    }

    private void llenarDenuncia(){
        if (cheAnonimo.isChecked()){
            limpiarDatos();
            mDenuncia.setAnonimo(true);
            mDenuncia.setComentarios(txtComentarios.getText().toString());
        }else{
            limpiarDatos();
            mDenuncia.setAnonimo(false);
            mDenuncia.setEmail(txtEmail.getText().toString());
            mDenuncia.setCedula(txtCedula.getText().toString());
            mDenuncia.setNombre(txtNombre.getText().toString());
            mDenuncia.setDireccion(txtDireccion.getText().toString());
            mDenuncia.setTelefono(txtTelefono.getText().toString());
            mDenuncia.setComentarios(txtComentarios.getText().toString());

            Lugar depto = (Lugar) spiDepartamento.getSelectedItem();
            if (depto != null)
                mDenuncia.setMunicipio(depto.getIDLugar());

            Lugar municipio = (Lugar) spiMunicipio.getSelectedItem();
            if (municipio!= null)
                mDenuncia.setMunicipio(municipio.getIDLugar());

            Lugar vereda = (Lugar) spiVereda.getSelectedItem();
            if (vereda!= null)
                mDenuncia.setMunicipio(vereda.getIDLugar());
        }
    }
    private  void limpiarDatos(){
        mDenuncia.setNombre("");
        mDenuncia.setCedula("");
        mDenuncia.setEmail("");
        mDenuncia.setDireccion("");
        mDenuncia.setTelefono("");
        mDenuncia.setComentarios("");
        mDenuncia.setDepartamento("");
        mDenuncia.setMunicipio("");
        mDenuncia.setVereda("");
        mDenuncia.setVeredaQueja("");
        mDenuncia.setMunicipioQueja("");
        mDenuncia.setDesUbicacionQueja("");
    }



    private void cargarDatos(){
        txtNombre.setText(mDenuncia.getNombre());
        txtCedula.setText(mDenuncia.getCedula());
        txtDireccion.setText(mDenuncia.getDireccion());
        txtEmail.setText(mDenuncia.getEmail());
        txtTelefono.setText(mDenuncia.getTelefono());
        txtComentarios.setText(mDenuncia.getComentarios());
    }

    @OnClick(R.id.btnDenunciar) void onDenunciar(){
        if (validar()){
            llenarDenuncia();
            mRadicarPQRPresenter.publicarImagenes(mDenuncia.getFotos(),mDenuncia.getUsuario());
            // iniciar progress
        }else{
            mostrarMensaje("Los campos marcados son obligatorios");
        }
    }

    @OnClick(R.id.cheAnonimo) void onAnonimo(){
        lyDatos.setVisibility(cheAnonimo.isChecked()== true ? View.GONE : View.VISIBLE);
        if (!cheAnonimo.isChecked() && mLstDepartamentos.size()==0)
            mLugaresPresenter.obtenerDepartamentos();
    }

    @Override
    public void onSuccessDepartamentos(List<Lugar> lstDepartamentos) {
        this.mLstDepartamentos = lstDepartamentos;
        adapterDepartamentos = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstDepartamentos);
        adapterDepartamentos.setDropDownViewResource( R.layout.simple_spinner_dropdown_item);
        spiDepartamento.setAdapter(adapterDepartamentos);
        adapterDepartamentos.notifyDataSetChanged();
    }

    @Override
    public void onSuccessMunicipios(List<Lugar> lstMunicipios) {
        this.mLstMunicipios = lstMunicipios;
        adapterMunicipios = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstMunicipios);
        adapterMunicipios.setDropDownViewResource( R.layout.simple_spinner_dropdown_item);
        spiMunicipio.setAdapter(adapterMunicipios);
        adapterMunicipios.notifyDataSetChanged();
    }

    @Override
    public void onSuccessVeredas(List<Lugar> lstVeredas) {
        this.mLstVeredas = lstVeredas;
        adapterVeredas = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstVeredas);
        adapterVeredas.setDropDownViewResource( R.layout.simple_spinner_dropdown_item);
        spiVereda.setAdapter(adapterVeredas);
        adapterVeredas.notifyDataSetChanged();
    }

    @Override
    public void onErrorDepartamentos(ErrorApi errorApi) {
        if (!cheAnonimo.isChecked()) {
            Snackbar.make(lyDenuncia, getResources().getString(R.string.error_load_deptos), Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green))
                    .setAction("REINTENTAR", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mLugaresPresenter.obtenerDepartamentos();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onErrorMunicipios(ErrorApi errorApi) {
        if (!cheAnonimo.isChecked()) {
            Snackbar.make(lyDenuncia, getResources().getString(R.string.error_load_municipios), Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green))
                    .setAction("REINTENTAR", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Lugar depto = (Lugar) spiDepartamento.getSelectedItem();
                            if (depto != null)
                                mLugaresPresenter.obtenerMunicipios(depto.getIDLugar());
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onErrorVeredas(ErrorApi errorApi) {
     if (errorApi.getStatusCode() >= 500 && !cheAnonimo.isChecked()){
         Snackbar.make(lyDenuncia,getResources().getString(R.string.error_load_veredas), Snackbar.LENGTH_INDEFINITE)
                 .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green) )
                 .setAction("REINTENTAR", new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         Lugar municipio = (Lugar) spiMunicipio.getSelectedItem();
                         if (municipio!= null)
                         mLugaresPresenter.obtenerMunicipios(municipio.getIDLugar());
                     }
                 })
                 .show();
     }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spiDepartamento:
                Lugar lugar = (Lugar) parent.getItemAtPosition(position);
                if (lugar!=null)
                mLugaresPresenter.obtenerMunicipios(lugar.getIDLugar());
                break;
            case R.id.spiMunicipio:
                //Lugar depto = (Lugar) parent.getItemAtPosition(position);
                Lugar municipio = (Lugar) spiMunicipio.getSelectedItem();
                if(municipio!=null)
                mLugaresPresenter.obtenerVeredas(municipio.getIDLugar());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSuccessImages(List<Foto> lstArchivoAdjunto) {
        mDenuncia.setFotos(lstArchivoAdjunto);
        mRadicarPQRPresenter.radicarPQR(mDenuncia);
    }

    @Override
    public void onSuccessRadicarPQR(Denuncia denunciad) {
        mostrarMensajeDialog(getResources().getString(R.string.radicar_pqr_ok) +" "+ denunciad.getNumeroRadicado());
    }

    @Override
    public void onErrorImages(String mensaje) {
        if (!mensaje.isEmpty()){
            mostrarMensajeDialog(mensaje);
        }
    }

    @Override
    public void onErrorRadicarPQR(ErrorApi errorApi) {
        mostrarMensajeDialog(errorApi.getMessage());
    }
}
