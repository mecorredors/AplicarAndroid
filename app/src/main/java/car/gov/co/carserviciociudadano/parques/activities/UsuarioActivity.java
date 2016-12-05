package car.gov.co.carserviciociudadano.parques.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.parques.dataaccess.Usuarios;
import car.gov.co.carserviciociudadano.parques.interfaces.IUsuario;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Usuario;

public class UsuarioActivity extends BaseActivity {

    EditText mTxtEmail;
    EditText mTxtClave;
    Button mBtmLogin;
    Button mBtnCrearCuenta;
    View mLyLogin;
    ProgressBar mProgressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        findViewsById();


    }

    private void findViewsById(){
        mTxtClave = (EditText) findViewById(R.id.txtClave);
        mTxtEmail = (EditText) findViewById(R.id.txtEmail);
        mBtmLogin = (Button) findViewById(R.id.btnLogin);
        mBtnCrearCuenta = (Button) findViewById(R.id.btnCrearCuenta);
        mLyLogin = findViewById(R.id.lyLogin);
        mProgressView = (ProgressBar) findViewById(R.id.progressView);

        mBtmLogin.setOnClickListener(onClickListener);
        mBtnCrearCuenta.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.btnLogin:
                    login();
                    break;
            }
        }
    };

    private void login() {
        if (validarLogin()) {
            showProgress(mProgressView,true);
            final Usuarios usuarios = new Usuarios();
            usuarios.login(mTxtEmail.getText().toString().trim(), mTxtClave.getText().toString().trim(), new IUsuario() {
                @Override
                public void onSuccess(Usuario usuario) {
                    usuarios.guardar(usuario);
                    showProgress(mProgressView,false);
                    mostrarMensaje("Login correcto",mLyLogin);
                }

                @Override
                public void onError(ErrorApi error) {
                    showProgress(mProgressView,false);
                    mostrarMensajeDialog(error.getMessage() + " " + error.getStatusCode());
                }
            });
        }
    }

    private boolean validarLogin(){
        boolean res = true;
        if (mTxtEmail.getText().toString().isEmpty()) {
            mostrarMensaje("Ingrese un email", mLyLogin);
            mTxtEmail.setError("Ingrese un email");
            res = false;
        }
         if (mTxtClave.getText().toString().isEmpty()){
            mostrarMensaje("Ingrese una valor",mLyLogin);
            mTxtClave.setError("Ingrese un valor");
             res = false;
        }
        return res;
    }


}
