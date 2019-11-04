package car.gov.co.carserviciociudadano.petcar.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.Utils.Validation;
import car.gov.co.carserviciociudadano.bicicar.adapter.FotosBicicarAdapter;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.ArchivosAdjuntos;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Eventos;
import car.gov.co.carserviciociudadano.bicicar.model.ArchivoAdjunto;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.denunciaambiental.activities.GalleryActivity;
import car.gov.co.carserviciociudadano.petcar.adapter.FotosPetCarAdapter;
import car.gov.co.carserviciociudadano.petcar.dataaccess.AdjuntosPetCar;
import car.gov.co.carserviciociudadano.petcar.dataaccess.MaterialesRecogidos;
import car.gov.co.carserviciociudadano.petcar.model.AdjuntoPetCar;
import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;

public class RegistrarKilosYFotosMaterialActivity extends BaseActivity {
    @BindView(R.id.lblContenedor) TextView lblContenedor;
    @BindView(R.id.lblTipoMaterial) TextView lblTipoMaterial;
    @BindView(R.id.lyComentarios) TextInputLayout lyComentarios;
    @BindView(R.id.lyKilos) TextInputLayout lyKilos;
    @BindView(R.id.txtComentarios) EditText txtComentarios;
    @BindView(R.id.txtKilos) EditText txtKilos;
    @BindView(R.id.lyInicial)   LinearLayout lyInicial;
    @BindView(R.id.gridGallery)  GridView gridView;

    MaterialRecogido mMaterialRecogido;
    FotosPetCarAdapter mAdapter;
    List<AdjuntoPetCar> mlstFotos = new ArrayList<>();
    public static  final int MAX_PHOTOS = 10;
    private static final int PETICION_GALLERY = 102;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_kilos_yfotos_material);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);


        Bundle b = getIntent().getExtras();
        if (b != null){
            int id = b.getInt(MaterialRecogido.ID);
            mMaterialRecogido = new MaterialesRecogidos().read(id);
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

        if (mMaterialRecogido.Kilos > 0) {
            txtKilos.setText(String.valueOf(mMaterialRecogido.Kilos));
        }


        mlstFotos.addAll(new AdjuntosPetCar().list(mMaterialRecogido.Id, Enumerator.Estado.TODOS));
        mAdapter = new FotosPetCarAdapter(this, new ArrayList<>(mlstFotos));
        mAdapter.SetOnItemClickListener(listenerGallerySelected);
        gridView.setAdapter(mAdapter);


        setNumPhotosSelect();

    }

    @OnClick(R.id.btnEliminar) void onEliminar(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Seguro desea eliminar el material " + mMaterialRecogido.getTipoMaterial().Nombre + " ?");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               eliminar();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void eliminar(){
        new MaterialesRecogidos().delete(mMaterialRecogido.Id);
        setResult(RESULT_OK);
        onBackPressed();
    }

    @OnClick(R.id.btnGuardar) void onGuardar(){
        guardar();
    }

 /*   @OnClick(R.id.btnAddFoto) void onAddFoto() {
        openGalleryPhotos();
    }*/

    @OnClick(R.id.lyAgregarFotos) void onAgregarFotos() {
        openGalleryPhotos();
    }
    @OnClick(R.id.imgAgregarFotos) void onImgAgregarFotos() {
        openGalleryPhotos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == PETICION_GALLERY){
                String[] all_path = data.getStringArrayExtra(GalleryActivity.ALL_PATH);
                int[] all_ids = data.getIntArrayExtra(GalleryActivity.ALL_THUMB_IDS);

                mAdapter.AddPhotos(all_path, mMaterialRecogido.Id);
                guardarFotos();
                setNumPhotosSelect();
                actualizarEstado();
            }
        }
    }
    private void actualizarEstado(){
        if (mMaterialRecogido != null){
            if (mMaterialRecogido.Estado == Enumerator.Estado.PUBLICADO) {
                mMaterialRecogido.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR_FOTOS;
                new MaterialesRecogidos().update(mMaterialRecogido);
            }

        }
    }
    private void guardarFotos(){
        if (mAdapter.getItemCount() > 1) {

            for (AdjuntoPetCar item : mAdapter.getPhotos()) {
                if (item.Type == Enumerator.TipoFoto.FOTO && item.Id == 0){
                    new AdjuntosPetCar().insert(item);
                }
            }
        }
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
            setResult(RESULT_OK);
            onBackPressed();
        }

    }

    private void openGalleryPhotos(){
        int size = mAdapter.getItemCount();
        if (size <= MAX_PHOTOS) {

            Intent i = new Intent(this, GalleryActivity.class);
            i.putExtra(GalleryActivity.ITEM_COUNT, size);
            i.putExtra(GalleryActivity.MAX_PHOTOS_EXTRA, MAX_PHOTOS);

            String[] datos = new String[size];
            for (int t = 0; t < size; t++)
                datos[t] =  mAdapter.getPhotos().get(t).Path;

            i.putExtra(GalleryActivity.PATH_PHOTOS_SELECT, datos);
            startActivityForResult(i, PETICION_GALLERY);
        } else {
            mostrarMensaje(getResources().getString(R.string.max_photos_bicicar));
        }
    }

    FotosPetCarAdapter.OnItemClickListener listenerGallerySelected = new FotosPetCarAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            AdjuntoPetCar foto = mAdapter.getPhotos().get(position);
            if (foto.Type == Enumerator.TipoFoto.BOTON_AGREGAR_MAS) {
                openGalleryPhotos();
            }
        }

        @Override
        public void onCloseClick(View view, int position) {

            if (position > 0) {
                AdjuntoPetCar archivoAdjunto = mAdapter.getPhotos().get(position);
                if (archivoAdjunto != null && archivoAdjunto.Id > 0) {
                    new AdjuntosPetCar().delete(archivoAdjunto.Id);
                }
            }
            mAdapter.RemoveItem(position);
            setNumPhotosSelect();

        }
    };

    private void setNumPhotosSelect() {
        if (mAdapter.getItemCount() <= 1) {
            lyInicial.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        }
        else {
            lyInicial.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }
    }
}
