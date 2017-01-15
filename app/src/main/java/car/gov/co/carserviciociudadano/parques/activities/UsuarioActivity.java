package car.gov.co.carserviciociudadano.parques.activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import car.gov.co.carserviciociudadano.parques.businessrules.BRMunicipios;
import car.gov.co.carserviciociudadano.parques.dataaccess.Municipios;
import car.gov.co.carserviciociudadano.parques.dataaccess.Usuarios;
import car.gov.co.carserviciociudadano.parques.interfaces.IMunicipio;
import car.gov.co.carserviciociudadano.parques.interfaces.IUsuario;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Municipio;
import car.gov.co.carserviciociudadano.parques.model.Usuario;

public class UsuarioActivity extends BaseActivity {


    @BindView(R.id.txtEmail) EditText mTxtEmail;
    @BindView(R.id.txtClave) EditText mTxtClave;
    @BindView(R.id.btnLogin) Button mBtmLogin;
    @BindView(R.id.btnCrearCuenta) Button mBtnCrearCuenta;
    @BindView(R.id.lyLogin)  View mLyLogin;
    @BindView(R.id.activity_usuario)  View mActvityUsuario;
    @BindView(R.id.progressView) ProgressBar mProgressView;

    @BindView(R.id.txtEmailUsuario) EditText mTxtEmailUsuario;
    @BindView(R.id.txtClaveUsuario) EditText mTxtClaveUsuario;
    @BindView(R.id.txtClaveUsuario2) EditText mTxtClaveUsuario2;
    @BindView(R.id.txtNombre) EditText mTxtNombre;
    @BindView(R.id.txtDocumento) EditText mTxtDocumento;
    @BindView(R.id.txtTelefono) EditText mTxtTelefono;
    @BindView(R.id.txtCelular) EditText mTxtCelular;
    @BindView(R.id.txtDireccion) EditText mTxtDireccion;
    @BindView(R.id.spiMunicipio) Spinner mSpiMunicipio;
    @BindView(R.id.lyUsuario)  View mLyUsuario;
    @BindView(R.id.lblFuncionarioCar)    TextView mLblFuncionarioCar;
    @BindView(R.id.inputLayoutEmail)    TextInputLayout mInputLayoutEmail;

    Usuario mUsuario;
    List<Municipio> mLstMunicipios = new ArrayList<>();
    ArrayAdapter<Municipio> adapterMunicipios;
    ProgressDialog progressDialog;
    public static final String ORIGIN = "origin";
    private int mOrigin = 0;
    public static final int ORIGEN_RESERVA = 10;
    public static final int ORIGEN_MANIN_PARQUES = 11;
    private boolean isInsert = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle!= null){
            mOrigin = bundle.getInt(ORIGIN,0);
        }


        loadMunicipios();
        init();
    }

    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(Usuarios.TAG);
        AppCar.VolleyQueue().cancelAll(Municipios.TAG);
        super.onPause();

    }

    @Override
    public void onBackPressed(){

        if (mOrigin == ORIGEN_MANIN_PARQUES) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_usuario, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_cerrar_sesion:
                new Usuarios().guardar(new Usuario());
                init();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){
        mUsuario = new Usuarios().leer();
        if(mUsuario.getIdUsuario() == 0 || (mUsuario.getIdUsuario() > 0 && mUsuario.isFuncionarioCar()  && !mUsuario.isLoginSIDCAR())) {
            mLyLogin.setVisibility(View.VISIBLE);
            mLyUsuario.setVisibility(View.GONE);
            ocultarTeclado(mLyLogin);

            if (mUsuario.getIdUsuario() > 0 && !mUsuario.isLoginSIDCAR()){
                mLblFuncionarioCar.setVisibility(View.VISIBLE);
                mLblFuncionarioCar.setText(mUsuario.getNombreCompleto() + " "+ getString(R.string.login_funcionario_car));
                mInputLayoutEmail.setHint("Login SIDCAR");
                mTxtEmail.setText("");
                mTxtEmail.setHint("");
                mTxtClave.setText("");
            }else{
                mLblFuncionarioCar.setVisibility(View.GONE);
                mTxtEmail.setHint("Email");
            }
        }
        else {
            mLyUsuario.setVisibility(View.VISIBLE);
            mLyLogin.setVisibility(View.GONE);
            mTxtClaveUsuario.setVisibility(View.GONE);
            mTxtClaveUsuario2.setVisibility(View.GONE);
            mTxtEmailUsuario.setVisibility(View.GONE);
            loadUsuario();
            ocultarTeclado(mLyUsuario);
        }

    }

    private void loadUsuario(){
        if (mUsuario != null && mUsuario.getIdUsuario() > 0 ){
            mTxtEmailUsuario.setText(mUsuario.getEmailUsuario());
            mTxtTelefono.setText(mUsuario.getTelefonoUsuario());
            mTxtCelular.setText(mUsuario.getCelularUsuario());
            mTxtNombre.setText(mUsuario.getNombreCompleto());
            mTxtDocumento.setText(mUsuario.getDocumento());
            mTxtDireccion.setText(mUsuario.getDireccionUsuario());
            if(adapterMunicipios != null)
                mSpiMunicipio.setSelection(adapterMunicipios.getPosition(new Municipio(mUsuario.getIDMunicipio(),"")));
        }
    }

    private void loadMunicipios(){

        BRMunicipios municipios = new BRMunicipios();
        municipios.list(new IMunicipio() {
            @Override
            public void onSuccess(List<Municipio> lstMunicipios) {
                mLstMunicipios.clear();
                mLstMunicipios.addAll(lstMunicipios);
                mLstMunicipios.add(0,new Municipio(0,"Municipio"));
                adapterMunicipios = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstMunicipios);
                adapterMunicipios.setDropDownViewResource( android.R.layout.select_dialog_singlechoice);
                mSpiMunicipio.setAdapter(adapterMunicipios);
                adapterMunicipios.notifyDataSetChanged();

                mSpiMunicipio.setSelection(adapterMunicipios.getPosition(new Municipio(mUsuario.getIDMunicipio(),"")));
                //mSpiMunicipio.setOnItemSelectedListener(this);
            }

            @Override
            public void onError(ErrorApi error) {
                mostrarMensaje("No fue posible obtener los municipios",mActvityUsuario );

            }
        });

    }

    @OnClick(R.id.btnGuardar) void onGuardar() {
        guardar();
    }
    @OnClick(R.id.btnLogin) void onLogin() {
        login();
    }
    @OnClick(R.id.btnCrearCuenta) void onCrearCuenta() {
        mLyLogin.setVisibility(View.GONE);
        mLyUsuario.setVisibility(View.VISIBLE);
        mTxtClaveUsuario.setVisibility(View.VISIBLE);
        mTxtClaveUsuario2.setVisibility(View.VISIBLE);
        mTxtEmailUsuario.setVisibility(View.VISIBLE);
    }

    private void login() {
        ocultarTeclado(mLyLogin);
        if (validarLogin()) {
            showProgress(mProgressView,true);
            Usuarios usuarios = new Usuarios();
            if (mUsuario.getIdUsuario() > 0 &&  mUsuario.isFuncionarioCar() ) {
                usuarios.loginSIDCAR (mTxtEmail.getText().toString().trim(), mTxtClave.getText().toString().trim(), iUsuarioLogin);
            }else{
                usuarios.login(mTxtEmail.getText().toString().trim(), mTxtClave.getText().toString().trim(), iUsuarioLogin);
            }
        }
    }

    IUsuario iUsuarioLogin = new IUsuario() {
       final Usuarios usuarios = new Usuarios();

        @Override
        public void onSuccess(Usuario usuario) {
            usuarios.guardar(usuario);
            mUsuario = usuario;
            showProgress(mProgressView,false);


            if (usuario.isFuncionarioCar()){
                init();
                mostrarMensajeDialog(mUsuario.getNombreCompleto() + " "+ getString(R.string.login_funcionario_car));
            }else {
                if (mOrigin == ORIGEN_RESERVA) {
                    Intent intent = new Intent();
                    intent.putExtra(Usuario.ID_USUARIO, mUsuario.getIdUsuario());
                    intent.putExtra(Usuario.FUNCIONARIO_CAR, mUsuario.isFuncionarioCar());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    init();
                }
            }

        }

        @Override
        public void onError(ErrorApi error) {
            showProgress(mProgressView,false);
            if (error.getStatusCode() == 404)
                mostrarMensajeDialog("Usuario o clave incorrecto");
            else
                mostrarMensajeDialog(error.getMessage());
        }

        @Override
        public void onSuccessSIDCAR(boolean value){
            showProgress(mProgressView,false);
            if (value){
                mUsuario.setLoginSIDCAR(true);
                usuarios.guardar(mUsuario);

                if (mOrigin == ORIGEN_RESERVA) {
                    Intent intent = new Intent();
                    intent.putExtra(Usuario.ID_USUARIO, mUsuario.getIdUsuario());
                    intent.putExtra(Usuario.FUNCIONARIO_CAR, mUsuario.isFuncionarioCar());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    init();
                }
            }else{
                mostrarMensajeDialog("Usuario o clave de SIDCAR incorrecto");
            }
        }
    };


    private boolean validarLogin(){
        boolean res = true;
        if (mTxtEmail.getText().toString().isEmpty()) {
            mostrarMensaje("Ingrese el email o login", mLyLogin);
            mTxtEmail.setError("Ingrese el email o login");
            res = false;
        }
         if (mTxtClave.getText().toString().isEmpty()){
            mostrarMensaje("Ingrese una valor",mLyLogin);
            mTxtClave.setError("Ingrese un valor");
             res = false;
        }
        return res;
    }

    private boolean validarDatosUsuario(boolean isNewUser){
        boolean res = true;

       res = Validation.IsValidEmail(mTxtEmailUsuario);
        if (isNewUser) {
            res = Validation.IsValidPassword(mTxtClaveUsuario) && res;
            if (!mTxtClaveUsuario.getText().toString().equals(mTxtClaveUsuario2.getText().toString())){
                res = false;
                mTxtClaveUsuario2.setError(getString(R.string.error_contrasena2));
            }
        }
        res = !Validation.IsEmpty(mTxtNombre) && res;
        res = Validation.IsPhone(mTxtCelular) && res;
        res = Validation.IsPhone(mTxtTelefono) && res;
        res = !Validation.IsEmpty(mTxtDireccion) && res;
        res = !Validation.IsEmpty(mTxtDocumento) && res;
        res = !Validation.IsEmpty(mSpiMunicipio) && res;

        return res;
    }

    private void fillUsuario(boolean isNewUser){
        if (isNewUser){
            mUsuario.setClaveUsuario(mTxtClaveUsuario.getText().toString().trim());
            mUsuario.setLogin(mTxtEmailUsuario.getText().toString().trim());
            mUsuario.setEmailUsuario(mTxtEmailUsuario.getText().toString().trim());
        }
        mUsuario.setNombreCompleto(mTxtNombre.getText().toString());
        mUsuario.setDocumento(mTxtDocumento.getText().toString());
        mUsuario.setDireccionUsuario(mTxtDireccion.getText().toString());
        mUsuario.setTelefonoUsuario(mTxtTelefono.getText().toString());
        mUsuario.setCelularUsuario(mTxtCelular.getText().toString());

        Municipio municipio = (Municipio) mSpiMunicipio.getSelectedItem();
        if (municipio != null)
           mUsuario.setIDMunicipio(municipio.getIDMunicipio());
    }

    private void mostrarProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        //progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    private void guardar(){
        ocultarTeclado(mLyUsuario);
        if(validarDatosUsuario(mUsuario.getIdUsuario() == 0)) {
            fillUsuario(mUsuario.getIdUsuario() == 0);
            Usuarios usuarios = new Usuarios();
            mostrarProgressDialog();
            if (mUsuario.getIdUsuario() == 0) {
                isInsert = true;
                usuarios.insert(iUsuario, mUsuario);
            }
            else {
                isInsert = false;
                usuarios.update(iUsuario, mUsuario);
            }
        }
    }

    public static final int ERROR_CONRASENA = 1;
    IUsuario iUsuario = new IUsuario() {
        @Override
        public void onSuccess(Usuario usuario) {

            if (isInsert){
                if(progressDialog!=null)progressDialog.dismiss();
                mUsuario.setIdUsuario(usuario.getIdUsuario());
                mostrarMensaje("Usuario creado correctamente");
                new Usuarios().guardar(usuario);
                if (usuario.isFuncionarioCar()){
                    mostrarMensajeDialog(mUsuario.getNombreCompleto() + " "+ getString(R.string.login_funcionario_car));
                    init();
                } else  if(mOrigin == ORIGEN_RESERVA){
                        Intent intent = new Intent();
                        intent.putExtra(Usuario.ID_USUARIO,usuario.getIdUsuario());
                        intent.putExtra(Usuario.FUNCIONARIO_CAR,usuario.isFuncionarioCar());
                        setResult(Activity.RESULT_OK,intent);
                        finish();
                }
            }else{
                if(progressDialog!=null)progressDialog.dismiss();
                mostrarMensaje("Datos del usuario fueron modificados ");

            }
            new Usuarios().guardar(usuario);
            init();
        }

        @Override
        public void onError(ErrorApi error) {
            if(progressDialog!=null)progressDialog.dismiss();
            if (error.getStatusCode()== 300 && error.getCode() == ERROR_CONRASENA){
                new Usuarios().guardar(new Usuario());
                mostrarMensajeDialog(error.getMessage());
                init();
            }else {
                mostrarMensajeDialog(error.getMessage());
            }
        }
        @Override
        public void onSuccessSIDCAR(boolean value){

        }
    };

}
