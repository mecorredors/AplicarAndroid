package car.gov.co.carserviciociudadano.bicicar.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.PreferencesApp;
import car.gov.co.carserviciociudadano.Utils.Validation;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.presenter.BeneficiarioPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewBeneficiario;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class CambiarPasswordActivity extends BaseActivity implements IViewBeneficiario {
    @BindView(R.id.lyTxtClaveActual) TextInputLayout lyTxtClaveActual;
    @BindView(R.id.lyTxtNuevaClave) TextInputLayout lyTxtNuevaClave;
    @BindView(R.id.lyTxtConfirmarClave) TextInputLayout lyTxtConfirmarClave;
    @BindView(R.id.txtClaveActual) EditText txtClaveActual;
    @BindView(R.id.txtNuevaClave) EditText txtNuevaClave;
    @BindView(R.id.txtConfirmarClave) EditText txtConfirmarClave;
    Beneficiario mBeneficiarioLogin;

    public  static  final  String CAMBIAR_CLAVE_1_VEZ = "debe_cambiar_clave";
    BeneficiarioPresenter mBeneficiarioPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_password);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        mBeneficiarioLogin  = Beneficiarios.readBeneficio();
        mBeneficiarioPresenter = new BeneficiarioPresenter(this);
        if (PreferencesApp.getDefault(PreferencesApp.READ).getBoolean(CAMBIAR_CLAVE_1_VEZ, true))
        {
            txtClaveActual.setText(mBeneficiarioLogin.ClaveAPP);
            PreferencesApp.getDefault(PreferencesApp.WRITE).putBoolean(CAMBIAR_CLAVE_1_VEZ, false).commit();
            txtNuevaClave.requestFocus();
        }

    }

    private  boolean validar(){
        boolean res = true;
        if (Validation.IsEmpty(txtClaveActual, lyTxtClaveActual)) res = false;
        if (Validation.IsEmpty(txtNuevaClave, lyTxtNuevaClave)) res = false;
        if (Validation.IsEmpty(txtConfirmarClave, lyTxtConfirmarClave)) res = false;

        if (!txtNuevaClave.getText().toString().equals(txtConfirmarClave.getText().toString())){
            mostrarMensajeDialog(getString(R.string.error_contrasena2));
            return false;
        }

        return res;
    }

    @OnClick(R.id.btnGuardar) void  onGuardar(){
        if (validar()) {
            mostrarProgressDialog("Cambiado contraseña");
            mBeneficiarioPresenter.cambiarPassword(mBeneficiarioLogin.NumeroID, txtClaveActual.getText().toString(), txtNuevaClave.getText().toString());
        }
    }

    @Override
    public void onSuccess(Beneficiario beneficiario) {
        ocultarProgressDialog();
        beneficiario.guardar();

        AlertDialog.Builder builder = new AlertDialog.Builder(CambiarPasswordActivity.this);

        builder.setMessage("La contraseña fue cambiada correctamente");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_OK , getIntent());
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void onSuccess(List<Beneficiario> lstBeneficiarios) {

    }

    @Override
    public void onError(ErrorApi errorApi) {
        ocultarProgressDialog();
        mostrarMensajeDialog(errorApi.getMessage());
    }

    @Override
    public void onErrorListarItems(ErrorApi errorApi) {

    }

    @Override
    public void onSuccessRecordarClave(String mensaje) {

    }

    @Override
    public void onErrorRecordarClave(ErrorApi error) {

    }
}
