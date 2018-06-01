package car.gov.co.carserviciociudadano.bicicar.activities;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
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
    BeneficiarioPresenter beneficiarioPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_bici_car);
        ButterKnife.bind(this);
        beneficiarioPresenter = new BeneficiarioPresenter(this);

    }

    @OnClick(R.id.btnLogin) void onLogin() {
        login();
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

    private void login() {
        if (validarLogin()){
            beneficiarioPresenter.login(txtNumeroID.getText().toString(), txtClave.getText().toString());
        }
    }

    @Override
    public void onSuccessLogin(Beneficiario beneficiario) {
        mostrarMensajeDialog("login correcto "+ beneficiario.Nombres + " " + beneficiario.Apellidos);
        beneficiario.guardar();
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void onErrorLogin(ErrorApi errorApi) {
        mostrarMensajeDialog(errorApi.getMessage());
    }
}
