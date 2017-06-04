package car.gov.co.carserviciociudadano.denunciaambiental.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Validation;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.consultapublica.dataaccesss.Expedientes;
import car.gov.co.carserviciociudadano.denunciaambiental.dataacces.Lugares;
import car.gov.co.carserviciociudadano.denunciaambiental.dataacces.RadicarPQR;
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
  //  @BindView(R.id.lyComentarios)   TextInputLayout lyComentarios;
    @BindView(R.id.lyDatos)   View lyDatos;
    @BindView(R.id.lyDenuncia)   View lyDenuncia;
    @BindView(R.id.pbDepartamento)   ProgressBar pbDepartamento;
    @BindView(R.id.pbMunicipio)   ProgressBar pbMunicipio;
    @BindView(R.id.pbVereda)   ProgressBar pbVereda;
    @BindView(R.id.lyFormulario)   View lyFormulario;
    @BindView(R.id.lyRespuesta)    View lyRespuesta;
    @BindView(R.id.lblRespuesta)   TextView lblRespuesta;

    Denuncia mDenuncia;
    LugaresPresenter mLugaresPresenter;
    RadicarPQRPresener mRadicarPQRPresenter;
    List<Lugar> mLstDepartamentos = new ArrayList<>();
    ArrayAdapter<Lugar> adapterDepartamentos;
    List<Lugar> mLstMunicipios = new ArrayList<>();
    ArrayAdapter<Lugar> adapterMunicipios;
    List<Lugar> mLstVeredas = new ArrayList<>();
    ArrayAdapter<Lugar> adapterVeredas;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia_ambiental2);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        mDenuncia = Denuncia.newInstance();
        cargarDatos();
        spiDepartamento.setOnItemSelectedListener(this);
        spiMunicipio.setOnItemSelectedListener(this);
        mLugaresPresenter = new LugaresPresenter(this);
        mRadicarPQRPresenter = new RadicarPQRPresener(this);
        mLugaresPresenter.obtenerDepartamentos();
        pbDepartamento.setVisibility(View.VISIBLE);

        txtComentarios.setHorizontallyScrolling(false);
            //txtDescription.setImeOptions(EditorInfo.IME_ACTION_DONE);
         txtComentarios.setMaxLines(10);
        ocultarTeclado(lyDenuncia);
    }

    @Override protected void onPause(){
        super.onPause();
        llenarDenuncia();
        mDenuncia.setComentarios("");
        mRadicarPQRPresenter.setmPublicando(false);
        if (mProgressDialog != null) mProgressDialog.dismiss();
        AppCar.VolleyQueue().cancelAll(Lugares.TAG);
        AppCar.VolleyQueue().cancelAll(RadicarPQR.TAG);
    }
    @Override protected void onDestroy(){
        super.onDestroy();
        mRadicarPQRPresenter.setmPublicando(false);
        if (mProgressDialog != null) mProgressDialog.dismiss();
        AppCar.VolleyQueue().cancelAll(Lugares.TAG);
        AppCar.VolleyQueue().cancelAll(RadicarPQR.TAG);
    }

    private boolean validar(){
        boolean res = true;
        if (!cheAnonimo.isChecked()){

            if (Validation.IsEmpty(txtNombre, lyNombre)) res = false;
            if (!Validation.IsValidEmail(txtEmail, lyEmail)) res = false;
            if (txtDireccion.getText().length()> 0){
                if (Validation.IsEmpty(spiDepartamento)) res = false;
                if (Validation.IsEmpty(spiMunicipio)) res = false;
            }
        }

        if (Validation.IsEmpty(txtComentarios)) res = false;
        if (res && txtComentarios.getText().length() < 15){
            res = false;
            mostrarMensajeDialog(getResources().getString(R.string.error_longitud_comentarios));
            txtComentarios.requestFocus();
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
                mDenuncia.setDepartamento(depto.getIDLugar());

            Lugar municipio = (Lugar) spiMunicipio.getSelectedItem();
            if (municipio!= null)
                mDenuncia.setMunicipio(municipio.getIDLugar());

            Lugar vereda = (Lugar) spiVereda.getSelectedItem();
            if (vereda!= null )
                mDenuncia.setVereda(vereda.getIDLugar());
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

    }

    @OnClick(R.id.btnDenunciar) void onDenunciar(){
        if (validar()){
            llenarDenuncia();
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Enviado...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            mRadicarPQRPresenter.publicarImagenes(mDenuncia.getFotos(),mDenuncia.getUsuario());
            // iniciar progress
        }else{
            mostrarMensaje("Los campos marcados son obligatorios");
        }
    }

    @OnClick(R.id.cheAnonimo) void onAnonimo(){
        lyDatos.setVisibility(cheAnonimo.isChecked()== true ? View.GONE : View.VISIBLE);
        if (!cheAnonimo.isChecked() && mLstDepartamentos.size()==0) {
            pbDepartamento.setVisibility(View.VISIBLE);
            mLugaresPresenter.obtenerDepartamentos();
        }
        ocultarTeclado(lyDenuncia);
    }
    @OnClick(R.id.btnCerrar) void onCerrar(){
        setResult(Activity.RESULT_OK, getIntent());
        finish();
    }

    @Override
    public void onSuccessDepartamentos(List<Lugar> lstDepartamentos) {
        pbDepartamento.setVisibility(View.GONE);
        this.mLstDepartamentos = lstDepartamentos;
        adapterDepartamentos = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstDepartamentos);
        adapterDepartamentos.setDropDownViewResource( R.layout.simple_spinner_dropdown_item);
        spiDepartamento.setAdapter(adapterDepartamentos);
        adapterDepartamentos.notifyDataSetChanged();
    }

    @Override
    public void onSuccessMunicipios(List<Lugar> lstMunicipios) {
        pbMunicipio.setVisibility(View.GONE);
        this.mLstMunicipios = lstMunicipios;
        adapterMunicipios = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstMunicipios);
        adapterMunicipios.setDropDownViewResource( R.layout.simple_spinner_dropdown_item);
        spiMunicipio.setAdapter(adapterMunicipios);
        adapterMunicipios.notifyDataSetChanged();
    }

    @Override
    public void onSuccessVeredas(List<Lugar> lstVeredas) {
        pbVereda.setVisibility(View.GONE);
        this.mLstVeredas = lstVeredas;
        adapterVeredas = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstVeredas);
        adapterVeredas.setDropDownViewResource( R.layout.simple_spinner_dropdown_item);
        spiVereda.setAdapter(adapterVeredas);
        adapterVeredas.notifyDataSetChanged();
    }

    @Override
    public void onErrorDepartamentos(ErrorApi errorApi) {
        pbDepartamento.setVisibility(View.GONE);
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
        pbMunicipio.setVisibility(View.GONE);
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
        pbVereda.setVisibility(View.GONE);
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
                if (lugar!=null) {
                    pbMunicipio.setVisibility(View.VISIBLE);
                    mLugaresPresenter.obtenerMunicipios(lugar.getIDLugar());
                }
                break;
            case R.id.spiMunicipio:
                //Lugar depto = (Lugar) parent.getItemAtPosition(position);
                Lugar municipio = (Lugar) spiMunicipio.getSelectedItem();
                if(municipio!=null) {
                    pbVereda.setVisibility(View.VISIBLE);
                    mLugaresPresenter.obtenerVeredas(municipio.getIDLugar());
                }
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
        if (mProgressDialog != null) mProgressDialog.dismiss();

        mDenuncia.getFotos().clear();
        mDenuncia.setLatitude(0);
        mDenuncia.setLongitude(0);
        lyFormulario.setVisibility(View.GONE);
        lyRespuesta.setVisibility(View.VISIBLE);
        lblRespuesta.setText(getResources().getString(R.string.radicar_pqr_ok) +" "+ denunciad.getNumeroRadicado());
    }

    @Override
    public void onErrorImages(String mensaje) {
        if (mProgressDialog != null) mProgressDialog.dismiss();
        if (!mensaje.isEmpty()){
            mostrarMensajeDialog(mensaje);
        }
    }

    @Override
    public void onErrorRadicarPQR(ErrorApi errorApi) {
        if (mProgressDialog != null) mProgressDialog.dismiss();
        mostrarMensajeDialog(errorApi.getMessage());
    }
}