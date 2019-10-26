package car.gov.co.carserviciociudadano.petcar.activities;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.Utils.Validation;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.petcar.dataaccess.MaterialesRecogidos;
import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;

public class RegistrarKilosYFotosMaterialActivity extends BaseActivity {
    @BindView(R.id.lblContenedor) TextView lblContenedor;
    @BindView(R.id.lblTipoMaterial) TextView lblTipoMaterial;
    @BindView(R.id.lyComentarios) TextInputLayout lyComentarios;
    @BindView(R.id.lyKilos) TextInputLayout lyKilos;
    @BindView(R.id.txtComentarios) EditText txtComentarios;
    @BindView(R.id.txtKilos) EditText txtKilos;

    MaterialRecogido mMaterialRecogido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_kilos_yfotos_material);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);


        Bundle b = getIntent().getExtras();
        if (b != null){
            int idMaterialRecogido = b.getInt(MaterialRecogido.ID);
            mMaterialRecogido = new MaterialesRecogidos().read(idMaterialRecogido);
            if (mMaterialRecogido == null){
                mostrarMensajeDialog("No se encontró el material recogido, ingrese nuevamente");
                return;
            }
        }

        if (mMaterialRecogido.getContenedor() != null) {
            lblContenedor.setText( mMaterialRecogido.getContenedor().Direccion + " " + mMaterialRecogido.getContenedor().Municipio != null ? mMaterialRecogido.getContenedor().Direccion  : "Sin dirección");

        }else{
            lblContenedor.setText("Sin contenedor");
        }
        if (mMaterialRecogido.getTipoMaterial() != null) {
            if (mMaterialRecogido.getTipoMaterial().Descripcion != null) {
                lblTipoMaterial.setText(mMaterialRecogido.getTipoMaterial().Nombre + " (" + mMaterialRecogido.getTipoMaterial().Descripcion + ")");
            }else {
                lblTipoMaterial.setText(mMaterialRecogido.getTipoMaterial().Nombre );
            }
        }else{
            lblTipoMaterial.setText("No hay tipo  material");
        }

        lblContenedor.setText(mMaterialRecogido.getContenedor().Direccion + " (" + mMaterialRecogido.getContenedor().Municipio + ")") ;
        lblTipoMaterial.setText(mMaterialRecogido.getTipoMaterial().Nombre );
        txtComentarios.setText(mMaterialRecogido.Comentarios);
        txtKilos.setText(String.valueOf(mMaterialRecogido.Kilos));

    }


    @OnClick(R.id.btnGuardar) void onGuardar(){
        guardar();
    }

    private void guardar(){
        if (Validation.IsEmpty(txtKilos , lyKilos)) {
            return;
        }

        double kilos = Utils.convertDouble(txtKilos.getText().toString());
        if (kilos <= 0) {
            mostrarMensajeDialog("La cantidad de kilos debe ser mayor a 0");
            return;
        }

        mMaterialRecogido.Kilos = kilos;
        mMaterialRecogido.Comentarios = txtComentarios.getText().toString();
        if (!new MaterialesRecogidos().guardar(mMaterialRecogido)){
            mostrarMensajeDialog("Error al guardar los datos");
        }else{
           onBackPressed();
        }

    }

}
