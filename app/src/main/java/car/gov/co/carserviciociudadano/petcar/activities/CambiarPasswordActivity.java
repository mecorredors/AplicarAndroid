package car.gov.co.carserviciociudadano.petcar.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.PreferencesApp;
import car.gov.co.carserviciociudadano.Utils.Validation;
import car.gov.co.carserviciociudadano.bicicar.model.RespuestaApi;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Gestores;
import car.gov.co.carserviciociudadano.petcar.interfaces.IGestor;
import car.gov.co.carserviciociudadano.petcar.model.Gestor;
import car.gov.co.carserviciociudadano.petcar.presenter.GestorPresenter;

public class CambiarPasswordActivity extends BaseActivity implements IGestor {
    @BindView(R.id.lyTxtClaveActual) TextInputLayout lyTxtClaveActual;
    @BindView(R.id.lyTxtNuevaClave) TextInputLayout lyTxtNuevaClave;
    @BindView(R.id.lyTxtConfirmarClave) TextInputLayout lyTxtConfirmarClave;
    @BindView(R.id.txtClaveActual) EditText txtClaveActual;
    @BindView(R.id.txtNuevaClave) EditText txtNuevaClave;
    @BindView(R.id.txtConfirmarClave) EditText txtConfirmarClave;


    public  static  final  String CAMBIAR_CLAVE_1_VEZ_PETCAR = "debe_cambiar_clave_petcar";
    GestorPresenter mGestorPresenter;
    Gestor mGestor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_password);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        mGestor = new Gestores().getLogin();
        mGestorPresenter = new GestorPresenter(this);
        if (PreferencesApp.getDefault(PreferencesApp.READ).getBoolean(CAMBIAR_CLAVE_1_VEZ_PETCAR, true))
        {
            PreferencesApp.getDefault(PreferencesApp.WRITE).putBoolean(CAMBIAR_CLAVE_1_VEZ_PETCAR, false).commit();
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
            mGestorPresenter.cambiarClave(mGestor.Identificacion, txtClaveActual.getText().toString(), txtNuevaClave.getText().toString());
        }
    }


    @Override
    public void onSuccessLoging(Gestor gestor) {

    }

    @Override
    public void onErrorLoging(ErrorApi error) {

    }

    @Override
    public void onSuccessCambiarClave(Gestor gestor) {
        ocultarProgressDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(CambiarPasswordActivity.this);

        builder.setMessage("La contraseña fue cambiada correctamente");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // setResult(RESULT_OK , getIntent());
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void onErrorCambiarClave(ErrorApi error) {
        ocultarProgressDialog();
        mostrarMensajeDialog(error.getMessage());
    }

    @Override
    public void onSuccessRercordarClave(RespuestaApi respuestaApi) {

    }

    @Override
    public void onErrorRecordarClave(ErrorApi error) {

    }
}
