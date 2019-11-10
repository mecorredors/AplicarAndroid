package car.gov.co.carserviciociudadano.petcar.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Validation;
import car.gov.co.carserviciociudadano.bicicar.model.RespuestaApi;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Contenedores;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Gestores;
import car.gov.co.carserviciociudadano.petcar.dataaccess.TiposMaterial;
import car.gov.co.carserviciociudadano.petcar.interfaces.IGestor;
import car.gov.co.carserviciociudadano.petcar.interfaces.ITiposMaterial;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;
import car.gov.co.carserviciociudadano.petcar.model.Gestor;
import car.gov.co.carserviciociudadano.petcar.model.TipoMaterial;
import car.gov.co.carserviciociudadano.petcar.presenter.ContenedorPresenter;
import car.gov.co.carserviciociudadano.petcar.presenter.GestorPresenter;
import car.gov.co.carserviciociudadano.petcar.presenter.IViewContenedor;
import car.gov.co.carserviciociudadano.petcar.presenter.TiposMaterialPresenter;

public class LoginPetCarActivity extends BaseActivity implements IGestor, IViewContenedor, ITiposMaterial {

    @BindView(R.id.txtNumeroID)  EditText txtNumeroID;
    @BindView(R.id.txtClave)   EditText txtClave;
    @BindView(R.id.inputLyNumeroID)  TextInputLayout inputLyNumeroID;
    @BindView(R.id.inputLyClave)  TextInputLayout inputLyClave;
    @BindView(R.id.lyIngresar) View lyIngresar;
    @BindView(R.id.lyRecordarClave) View lyRecordarClave;
    @BindView(R.id.txtNumeroID2)  EditText txtNumeroID2;
    @BindView(R.id.txtEmail)   EditText txtEmail;
    @BindView(R.id.inputLyNumeroID2)  TextInputLayout inputLyNumeroID2;
    @BindView(R.id.inputLyEmail)  TextInputLayout inputLyEmail;

    GestorPresenter gestorPresenter;
    ContenedorPresenter contenedorPresenter;
    TiposMaterialPresenter tiposMaterialPresenter;
    Gestor mGestor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pet_car);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        gestorPresenter = new GestorPresenter(this);
        contenedorPresenter = new ContenedorPresenter(this);
        tiposMaterialPresenter = new TiposMaterialPresenter(this);

        lyIngresar.setVisibility(View.VISIBLE);
        lyRecordarClave.setVisibility(View.GONE);
    }

    void obtenerContenedores(){
        if (mGestor != null) {
            mostrarProgressDialog("Descargando contenedores");
            contenedorPresenter.getContenedores(mGestor.IDMunicipio);
        }
    }
    void obtenerTiposMaterial(){
        mostrarProgressDialog("Descargando tipos material");
        tiposMaterialPresenter.getTiposMaterial();
    }

    @OnClick(R.id.btnLogin) void onLogin() {
        login();
    }

    @OnClick(R.id.btnRecordarClave) void onRecordarClave() {
       recordarClave();
    }

    @OnClick(R.id.btnOlvidasteContrasena) void onOlvidasteContrasena() {
        lyIngresar.setVisibility(View.GONE);
        lyRecordarClave.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnIngresar) void onIngresar() {
        lyIngresar.setVisibility(View.VISIBLE);
        lyRecordarClave.setVisibility(View.GONE);
    }

    private boolean validarLogin(){
        boolean res = true;
        inputLyNumeroID.setErrorEnabled(false);
        inputLyClave.setErrorEnabled(false);
        if (txtNumeroID.getText().toString().isEmpty()) {
            inputLyNumeroID.setError("Ingrese número de identificación");
            res = false;
        }
        if (txtClave.getText().toString().isEmpty()){
            inputLyClave.setError("Ingrese una clave");
            res = false;
        }
        return res;
    }

    private boolean validarRecordarClave(){
        boolean res = true;
        inputLyNumeroID2.setErrorEnabled(false);
        inputLyEmail.setErrorEnabled(false);
        if (txtNumeroID2.getText().toString().isEmpty()) {
            inputLyNumeroID2.setError("Ingrese número de identificación");
            res = false;
        }
        if (txtEmail.getText().toString().isEmpty()){
            inputLyEmail.setError("Ingrese su email registrado");
            res = false;
        }else if (!Validation.IsValidEmail(txtEmail.getText().toString())){
            inputLyEmail.setError("Ingrese un email válido");
            res = false;
        }
        return res;
    }

    private void login() {
        if (validarLogin()){
            mostrarProgressDialog("Ingresando ...");
            gestorPresenter.login(txtNumeroID.getText().toString(), txtClave.getText().toString());
        }
    }

    private void recordarClave() {
        if (validarRecordarClave()){
            mostrarProgressDialog("Enviando email ...");
            gestorPresenter.recordarClave(txtNumeroID2.getText().toString(), txtEmail.getText().toString());
        }
    }

    @Override
    public void onSuccessLoging(Gestor gestor) {
        ocultarProgressDialog();
        this.mGestor = gestor;
        if (new Gestores().guardar(gestor)){
            obtenerTiposMaterial();
        }else{
            mostrarMensajeDialog("Error al guardar gestor en base de datos local");
        }


    }

    @Override
    public void onErrorLoging(ErrorApi error) {
        ocultarProgressDialog();
        mostrarMensajeDialog(error.getMessage());
    }

    @Override
    public void onSuccessCambiarClave(Gestor gestor) {

    }

    @Override
    public void onErrorCambiarClave(ErrorApi error) {

    }

    @Override
    public void onSuccessRercordarClave(RespuestaApi respuestaApi) {
        ocultarProgressDialog();
        mostrarMensajeDialog(respuestaApi.Mensaje);
        lyIngresar.setVisibility(View.VISIBLE);
        lyRecordarClave.setVisibility(View.GONE);
    }

    @Override
    public void onErrorRecordarClave(ErrorApi error) {
        ocultarProgressDialog();
        mostrarMensajeDialog(error.getMessage());
    }

    @Override
    public void onSuccessContenedores(List<Contenedor> lstContenedores) {
        ocultarProgressDialog();
        Contenedores contenedores =  new Contenedores();
        for (Contenedor item : lstContenedores){
            contenedores.guardar(item);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Login OK y contenedores del municipio descargados");

        builder.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                setResult(RESULT_OK);
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void onErrorContenedores(ErrorApi error) {
        ocultarProgressDialog();
        if (error.getStatusCode() == 404){
            mostrarMensajeDialog("No hay contenedores en el municipio");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(error.getMessage() + " No fue posible obtener los contenedores");
        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                obtenerContenedores();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onSuccessTiposMaterial(List<TipoMaterial> lstTiposMaterial) {
        ocultarProgressDialog();
        obtenerContenedores();
        TiposMaterial tiposMaterial = new TiposMaterial();
        for (TipoMaterial item : lstTiposMaterial){
            tiposMaterial.guardar(item);
        }
    }

    @Override
    public void onErrorTiposMaterial(ErrorApi error) {
        ocultarProgressDialog();
        if (error.getStatusCode() == 404){
            mostrarMensajeDialog("No hay tipos materiales");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(error.getMessage() + " No fue posible obtener tipos material" );
        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                obtenerTiposMaterial();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
