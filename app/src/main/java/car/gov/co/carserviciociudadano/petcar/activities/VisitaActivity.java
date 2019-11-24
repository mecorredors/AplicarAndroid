package car.gov.co.carserviciociudadano.petcar.activities;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
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
import car.gov.co.carserviciociudadano.denunciaambiental.activities.GalleryActivity;
import car.gov.co.carserviciociudadano.petcar.adapter.FotosPetCarAdapter;
import car.gov.co.carserviciociudadano.petcar.dataaccess.AdjuntosPetCar;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Contenedores;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Visitas;
import car.gov.co.carserviciociudadano.petcar.model.AdjuntoPetCar;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;
import car.gov.co.carserviciociudadano.petcar.model.Visita;

public class VisitaActivity extends BaseActivity {
    @BindView(R.id.txtCodigo) EditText txtCodigo;
    @BindView(R.id.lyCodigo) TextInputLayout lyCodigo;
    @BindView(R.id.lblContenedor) TextView lblContenedor;
    @BindView(R.id.txtComentarios) EditText txtComentarios;
    @BindView(R.id.lyInicial) LinearLayout lyInicial;
    @BindView(R.id.gridGallery) GridView gridView;

    FotosPetCarAdapter mAdapter;
    List<AdjuntoPetCar> mlstFotos = new ArrayList<>();
    public static  final int MAX_PHOTOS = 10;
    private static final int PETICION_GALLERY = 102;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private static final int REQUEST_CODE_SCANNER = 2;
    Visita mVisita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visita);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        if (b != null){
            int id = b.getInt(Visita.ID , 0);
            if (id > 0){
                mVisita = new Visitas().read(id);
                txtCodigo.setText(mVisita.getContenedor().Codigo);
                txtComentarios.setText(mVisita.Comentarios);
                mlstFotos.addAll(new AdjuntosPetCar().listVisita(id, Enumerator.Estado.TODOS));
            }
        }

        if (mVisita == null){
            mVisita = new Visita();
        }


        mAdapter = new FotosPetCarAdapter(this, new ArrayList<>(mlstFotos));
        mAdapter.SetOnItemClickListener(listenerGallerySelected);
        gridView.setAdapter(mAdapter);


        setNumPhotosSelect();

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

    @OnClick(R.id.btnGuardar) void onGuardar(){
       if (guardar()){
           setResult(RESULT_OK);
           finish();
       }
    }
    @OnClick(R.id.btnEscanearCodigo) void onEscanearCodigo(){
        abrirEscaner();
    }

    @OnClick(R.id.lyAgregarFotos) void onAgregarFotos() {
        if (guardar()) {
            openGalleryPhotos();
        }
    }
    @OnClick(R.id.imgAgregarFotos) void onImgAgregarFotos() {
        if (guardar()) {
            openGalleryPhotos();
        }
    }

    private boolean guardar(){

        if (Validation.IsEmpty(txtCodigo, lyCodigo)){
            return false;
        }
        if (Validation.IsEmpty(txtComentarios)){
            return false;
        }

        Contenedor contenedor = getContenedor(txtCodigo.getText().toString());
        if (contenedor == null){
            return false;
        }

        Calendar calendar = Calendar.getInstance();

        if (mVisita.Id == 0) {
            mVisita.FechaLecturaQR = calendar.getTime();
            mVisita.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR;
        }
        mVisita.IDContenedor = contenedor.IDContenedor;
        mVisita.Comentarios = txtComentarios.getText().toString();

        new Visitas().guardar(mVisita);
        return  true;
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_SCANNER) {

                String datosEscaner = data.getStringExtra(EscanearQRActivity.ESCANER_DATOS);
                if (datosEscaner != null && !datosEscaner.trim().equals("")) {
                    txtCodigo.setText(datosEscaner.trim());
                    getContenedor(datosEscaner.trim());
                } else {
                    mostrarMensajeDialog("Codigo QR es incorrecto");
                }

            } else if (requestCode == PETICION_GALLERY) {
                String[] all_path = data.getStringArrayExtra(GalleryActivity.ALL_PATH);
                int[] all_ids = data.getIntArrayExtra(GalleryActivity.ALL_THUMB_IDS);

                mAdapter.AddPhotos(all_path, 0, mVisita.Id);
                if (mVisita != null && mVisita.Id > 0) {
                    guardarFotos();
                    setNumPhotosSelect();
                    actualizarEstado();
                }
            }
        }
    }

    private void actualizarEstado(){
        if (mVisita != null){
            if (mVisita.Estado == Enumerator.Estado.PUBLICADO) {
                mVisita.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR_FOTOS;
                new Visitas().update(mVisita);
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
