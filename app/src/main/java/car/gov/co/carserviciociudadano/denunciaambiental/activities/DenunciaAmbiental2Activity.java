package car.gov.co.carserviciociudadano.denunciaambiental.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Validation;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Denuncia;

public class DenunciaAmbiental2Activity extends BaseActivity {

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

    Denuncia mDenuncia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia_ambiental2);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        mDenuncia = Denuncia.newInstance();
        cargarDatos();
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
            mDenuncia.setComentarios(txtComentarios.getText().toString());
        }else{

            mDenuncia.setEmail(txtEmail.getText().toString());
            mDenuncia.setCedula(txtCedula.getText().toString());
            mDenuncia.setNombre(txtNombre.getText().toString());
            mDenuncia.setDireccion(txtDireccion.getText().toString());
            mDenuncia.setTelefono(txtTelefono.getText().toString());
            mDenuncia.setComentarios(txtComentarios.getText().toString());
        }
    }
    private  void limpiarDatos(){
        mDenuncia.setNombre("");
        mDenuncia.setCedula("");
        mDenuncia.setEmail("");
        mDenuncia.setDireccion("");
        mDenuncia.setTelefono("");
        mDenuncia.setComentarios("");
        mDenuncia.setDepartamento(0);
        mDenuncia.setCiudad(0);
        mDenuncia.setVereda(0);
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
            mostrarMensaje("La denuncia fue enviada");
        }else{
            mostrarMensaje("Los campos marcados son obligatorios");
        }
    }

    @OnClick(R.id.cheAnonimo) void onAnonimo(){
            lyDatos.setVisibility(cheAnonimo.isChecked()== true ? View.GONE : View.VISIBLE);
    }

}
