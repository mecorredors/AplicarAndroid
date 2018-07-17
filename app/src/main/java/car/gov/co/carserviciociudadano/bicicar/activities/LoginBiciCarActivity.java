package car.gov.co.carserviciociudadano.bicicar.activities;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Validation;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.presenter.BeneficiarioPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewBeneficiario;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class LoginBiciCarActivity extends BaseActivity implements IViewBeneficiario {

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

    BeneficiarioPresenter beneficiarioPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_bici_car);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        beneficiarioPresenter = new BeneficiarioPresenter(this);

        lyIngresar.setVisibility(View.VISIBLE);
        lyRecordarClave.setVisibility(View.GONE);
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
            beneficiarioPresenter.login(txtNumeroID.getText().toString(), txtClave.getText().toString());
        }
    }

    private void recordarClave() {
        if (validarRecordarClave()){
            mostrarProgressDialog("Enviando email ...");
            beneficiarioPresenter.recordarClave(txtNumeroID2.getText().toString(), txtEmail.getText().toString());
        }
    }

    @Override
    public void onSuccess(Beneficiario beneficiario) {
        ocultarProgressDialog();
        beneficiario.guardar();
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void onError(ErrorApi errorApi) {
        ocultarProgressDialog();
        mostrarMensajeDialog(errorApi.getMessage());
    }

    @Override
    public void onSuccessRecordarClave(String mensaje) {
        ocultarProgressDialog();
        mostrarMensajeDialog(mensaje);
        lyIngresar.setVisibility(View.VISIBLE);
        lyRecordarClave.setVisibility(View.GONE);
    }

    @Override
    public void onErrorRecordarClave(ErrorApi error) {
        ocultarProgressDialog();
        mostrarMensajeDialog(error.getMessage());
    }
}
