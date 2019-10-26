package car.gov.co.carserviciociudadano.petcar.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_material);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        addTiposMaterial();

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
    private void guardar(){

        if (Validation.IsEmpty(txtCodigo, lyCodigo)){
            return;
        }

        Contenedor contenedor = new Contenedores().read(txtCodigo.getText().toString());
        if (contenedor == null){
            mostrarMensajeDialog("El contenedor no existe");
            return;
        }
        lblContenedor.setText(contenedor.Direccion + " (" + contenedor.Municipio +")");
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

    }
}
