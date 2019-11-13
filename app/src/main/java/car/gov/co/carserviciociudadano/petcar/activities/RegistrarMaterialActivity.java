package car.gov.co.carserviciociudadano.petcar.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.ActionBar;

import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Calendar;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Validation;
import car.gov.co.carserviciociudadano.bicicar.activities.EscanearQRActivity;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Contenedores;
import car.gov.co.carserviciociudadano.petcar.dataaccess.MaterialesRecogidos;
import car.gov.co.carserviciociudadano.petcar.dataaccess.TiposMaterial;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;
import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;
import car.gov.co.carserviciociudadano.petcar.model.TipoMaterial;


public class RegistrarMaterialActivity extends BaseActivity {
    @BindView(R.id.txtCodigo) EditText txtCodigo;
    @BindView(R.id.lyCodigo) TextInputLayout lyCodigo;
    @BindView(R.id.lblContenedor)  TextView lblContenedor;
    @BindView(R.id.lyTiposMaterial) LinearLayout lyTiposMaterial;

    private static final int ZXING_CAMERA_PERMISSION = 1;
    private static final int REQUEST_CODE_SCANNER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_material);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        if (b != null){
            String codigo = b.getString(Contenedor.CODIGO , "");
            txtCodigo.setText(codigo);
            getContenedor(codigo);
        }

        addTiposMaterial();

    }

    private Contenedor getContenedor(String codigo){
        Contenedor contenedor = new Contenedores().read(txtCodigo.getText().toString());
        if (contenedor == null){
            mostrarMensajeDialog("El contenedor " + codigo  + " no se encuentra, descargue los contenedores del municipio ");
            return null;
        }else{
            lblContenedor.setText(contenedor.Direccion + " (" + contenedor.Municipio +")");
        }
        return  contenedor;
    }

    private void addTiposMaterial(){
        List<TipoMaterial> lstTiposMaterial = new TiposMaterial().list();
        if (lstTiposMaterial.size() == 0 ){
            mostrarMensajeDialog("Debe iniciar sesión y descargar los tipos de material");
            return;
        }

        for (TipoMaterial item : lstTiposMaterial){
            CheckBox checkBox = new CheckBox(this);
            checkBox.setChecked(false);
            checkBox.setId(item.IDTipoMaterial);
            checkBox.setText(item.Nombre + " (" + item.Descripcion + ")");
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            /*checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String msg = "You have " + (isChecked ? "checked" : "unchecked") + " this Check it Checkbox.";
                    mostrarMensaje(msg);
                }
            });*/

            lyTiposMaterial.addView(checkBox);
        }
    }


    @OnClick(R.id.btnGuardar) void onGuardar(){
        guardar();
    }
    @OnClick(R.id.btnEscanearCodigo) void onEscanearCodigo(){
        abrirEscaner();
    }
    private void guardar(){

        if (Validation.IsEmpty(txtCodigo, lyCodigo)){
            return;
        }

        Contenedor contenedor = getContenedor(txtCodigo.getText().toString());
        if (contenedor == null){
            return;
        }

        Calendar calendar = Calendar.getInstance();


        MaterialRecogido materialRecogido = new MaterialRecogido();
        materialRecogido.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR;
        materialRecogido.Comentarios = "";
        materialRecogido.FechaLecturaQR = calendar.getTime();
        materialRecogido.IDContenedor = contenedor.IDContenedor;
        MaterialesRecogidos materialesRecogidos = new MaterialesRecogidos();
        int numMateriales = 0;
        for (int i = 0; i < lyTiposMaterial.getChildCount(); i++){
            CheckBox checkBox = (CheckBox) lyTiposMaterial.getChildAt(i);
            if (checkBox.isChecked()){
                numMateriales++;
                materialRecogido.IDTipoMaterial = checkBox.getId();
                if (!materialesRecogidos.insert(materialRecogido)){
                    mostrarMensajeDialog("Error al guardar material recogido");
                    return;
                }
            }
        }

        if (numMateriales == 0){
            mostrarMensajeDialog("Seleccione los materiales recogidos");
        }

        finish();
    }

    private void abrirEscaner(){
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},
                    ZXING_CAMERA_PERMISSION);
        } else {
            Intent i = new Intent(this, EscanearQRActivity.class);
            startActivityForResult(i,   REQUEST_CODE_SCANNER);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    abrirEscaner();
                } else {
                    mostrarMensaje("Se necesita permiso de camara para registrar actividad");
                }
                return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCANNER) {
            if (resultCode == Activity.RESULT_OK) {
                String datosEscaner = data.getStringExtra(EscanearQRActivity.ESCANER_DATOS);
                if (datosEscaner != null && !datosEscaner.trim().equals("")) {
                    txtCodigo.setText(datosEscaner.trim());
                    getContenedor(datosEscaner.trim());
                }else{
                    mostrarMensajeDialog("Codigo QR es incorrecto");
                }
            }
        }
    }
}
