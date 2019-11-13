package car.gov.co.carserviciociudadano.parques.activities;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Validation;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.dataaccess.Usuarios;
import car.gov.co.carserviciociudadano.parques.interfaces.IUsuario;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Usuario;

public class CambiarContrasenaActivity extends BaseActivity {
    @BindView(R.id.activity_cambiar_contrasena)   View mActivity_cambiar_contrasena;
    @BindView(R.id.progressView)  ProgressBar mProgressView;
    @BindView(R.id.txtContrasenaActual)  EditText mTxtContrasenaActual;
    @BindView(R.id.txtNuevaContrasena)  EditText mTxtNuevaContrasena;
    @BindView(R.id.txtConfirmarContrasena)  EditText mTxtConfirmarContrasena;
    @BindView(R.id.inputLyContrasenaActual)   TextInputLayout mInputLyContrasenaActual;
    @BindView(R.id.inputLyNuevaContrasena)  TextInputLayout mInputLyNuevaContrasena;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        if (BuildConfig.DEBUG == false) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundleAnalitic = new Bundle();
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_ID, "Cambiar contrasena");
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_NAME, "Cambiar contrasena");
            bundleAnalitic.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Enumerator.ContentTypeAnalitic.PARQUES);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleAnalitic);
        }
    }

    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(Usuarios.TAG);
        super.onPause();
    }

    @OnClick(R.id.btnActualizar) void onActualizar() {
        actualizar();
    }

    private boolean validar(){
        boolean res = true;
        res = !Validation.IsEmpty(mTxtContrasenaActual, mInputLyContrasenaActual) && res;
        res = !Validation.IsEmpty(mTxtNuevaContrasena, mInputLyNuevaContrasena) && res;


        if (mTxtConfirmarContrasena.getText().toString().trim().length() < 4){
            mostrarMensajeDialog(getString(R.string.validar_contrasena));
            return  false;
        }

        if (!mTxtNuevaContrasena.getText().toString().equals(mTxtConfirmarContrasena.getText().toString())){
            mostrarMensajeDialog(getString(R.string.contrasena_no_coincide));
            return  false;
        }
        return res;
    }

    private void actualizar(){
        if (validar()){
          Usuario  usuario = new Usuarios().leer();
          final  Usuarios usuarios = new Usuarios();

          usuario.setClaveUsuario(mTxtContrasenaActual.getText().toString());
          usuario.setNuevaClaveUsuario(mTxtNuevaContrasena.getText().toString());

            showProgress(mProgressView,true);
            usuarios.cambiarContrasena(usuario, new IUsuario() {
                @Override
                public void onSuccess(Usuario usuario) {
                    showProgress(mProgressView,false);
                    usuario.setClaveUsuario(usuario.getNuevaClaveUsuario());
                    usuarios.guardar(usuario);
                    mostrarMensaje(getString(R.string.contrasen_actualizada));
                    finish();
                }

                @Override
                public void onSuccessSIDCAR(boolean value) {

                }

                @Override
                public void onError(ErrorApi error) {
                    showProgress(mProgressView,false);
                     mostrarMensajeDialog(error.getMessage());
                }
            });

        }
    }

}
