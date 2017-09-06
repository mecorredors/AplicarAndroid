package car.gov.co.carserviciociudadano.parques.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.FechaDialogo;
import car.gov.co.carserviciociudadano.Utils.ImageUtil;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.Utils.Validation;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.presenter.BancosPresenter;
import car.gov.co.carserviciociudadano.parques.presenter.IViewBancos;
import car.gov.co.carserviciociudadano.parques.dataaccess.Abonos;
import car.gov.co.carserviciociudadano.parques.interfaces.IAbono;
import car.gov.co.carserviciociudadano.parques.model.Abono;
import car.gov.co.carserviciociudadano.parques.model.Banco;
import car.gov.co.carserviciociudadano.parques.model.DetalleReserva;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


public class AbonoActivity extends BaseActivity {

    @BindView(R.id.spiBanco)   Spinner mSpiBanco;
    @BindView(R.id.activity_abono)  View mActivity_abono;
    @BindView(R.id.txtFechaConsignacion) EditText mTxtFechaConsignacion;
    @BindView(R.id.txtValor)     EditText mTxtValor;
    @BindView(R.id.txtNroConsignacion)     EditText mTxtNroConsignacion;
    @BindView(R.id.txtObservaciones)     EditText mTxtObservaciones;
    @BindView(R.id.btnCapturarComprobante)   Button mBtnCapturarComprobante;
    @BindView(R.id.btnSeleccionarComprobante)   Button mBtnSeleccionarComprobante;
    @BindView(R.id.lyImgComprobante)   View mLyImgComprobante;
    @BindView(R.id.imgComprobante)    ImageView mImgComprobante;
    @BindView(R.id.btnBorrarImagen)    ImageButton mBtnBorrarImagen;

    @BindView(R.id.inputLyFechaConsignacion)  TextInputLayout mInputLyFechaConsignacion;
    @BindView(R.id.inputLyNroConsignacion)  TextInputLayout mInputLyNroConsignacion;
    @BindView(R.id.inputLyObservaciones)  TextInputLayout mInputLyObservaciones;
    @BindView(R.id.inputLyValor)  TextInputLayout mInputLyValor;

    private FirebaseAnalytics mFirebaseAnalytics;
    List<Banco> mLstBancos = new ArrayList<>();
    ArrayAdapter<Banco> adapterBancos;

    public static final  String TAG = "Abono";
    public final static int GALLERY_INTENT_CALLED = 1;
    public final static int GALLERY_KITKAT_INTENT_CALLED = 2;
    public final static int REQUEST_CODE_PHOTO = 3;

    public  static final  int MY_PERMISSION_READ_EXTERNAL_STORAGE = 11;
    public  final static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    Abono mAbono;
    ProgressDialog mProgressDialog;

    SubirArchivoAsincrona mSubirArchivoAsincrono;

    private long mIdReserva;

    private String mSelectedImagePath = "";
    private  Uri mOutputFileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abono);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            mIdReserva = bundle.getLong(DetalleReserva.ID_RESERVA , 0);
        }

        loadBancos();

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED  ) {

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE  },  MY_PERMISSION_READ_EXTERNAL_STORAGE );
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundleAnalitic = new Bundle();
        bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_ID, "Abono");
        bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_NAME, "Abono");
        bundleAnalitic.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Enumerator.ContentTypeAnalitic.PARQUES);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleAnalitic);
    }

    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(Abonos.TAG);
        if(mSubirArchivoAsincrono!= null ) mSubirArchivoAsincrono.onCancelled();
        super.onPause();

    }

    @OnClick(R.id.txtFechaConsignacion) void onFechaConsignacion() {
        FechaDialogo fechaDialogo = new FechaDialogo();
        fechaDialogo.listerFecha=listenerFecha;
        fechaDialogo.show(getSupportFragmentManager(), getResources().getString(R.string.fecha_consignacion));
    }

    @OnClick(R.id.btnSeleccionarComprobante) void onSeleccionarComprobante() {
        if (Build.VERSION.SDK_INT <19){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.seleccionar_comprobante)),GALLERY_INTENT_CALLED);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
        }
    }

    @OnClick(R.id.btnBorrarImagen) void onBorrarImagen() {
        mSelectedImagePath = "";
        mLyImgComprobante.setVisibility(View.GONE);
        mBtnCapturarComprobante.setVisibility(View.VISIBLE);
        mBtnSeleccionarComprobante.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnCapturarComprobante) void onCapturarComprobante(){
       tomarFoto();
    }

    @OnClick(R.id.btnGuardar) void onGuardar() {
       guardar();
    }





    // By using this method get the Uri of Internal/External Storage for Media
    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    private void loadBancos(){
        BancosPresenter bancos = new BancosPresenter(iViewBancos);
        bancos.list();
    }

    IViewBancos iViewBancos = new IViewBancos() {
        @Override
        public void onSuccess(List<Banco> lstBancos) {
            mLstBancos.clear();
            mLstBancos.addAll(lstBancos);
            mLstBancos.add(0,new Banco(0,"Banco"));
            adapterBancos = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstBancos);
            adapterBancos.setDropDownViewResource( R.layout.simple_spinner_dropdown_item);
            mSpiBanco.setAdapter(adapterBancos);
            adapterBancos.notifyDataSetChanged();

            // mSpiBanco.setSelection(adapterBancos.getPosition(new Municipio(mUsuario.getIDMunicipio(),"")));
            //mSpiMunicipio.setOnItemSelectedListener(this);
        }

        @Override
        public void onError(ErrorApi error) {

            Snackbar.make(mLyImgComprobante, error.getMessage(), Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green) )
                    .setAction("REINTENTAR", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            loadBancos();
                        }
                    })
                    .show();

        }
    };

    FechaDialogo.OnListerFecha listenerFecha= new FechaDialogo.OnListerFecha() {
        @Override
        public void onDataset(DatePicker view, int year, int month, int day) {

            Calendar c = Calendar.getInstance();
            c.set(year,month,day);
            mTxtFechaConsignacion.setText(Utils.toStringFromDate(c.getTime()));
        }
        @Override
        public void onDatasetCalcel(){
           // btnFecha.setChecked(false);
        }
    };


    private boolean  validar(){
        boolean res = true;

        res = !Validation.IsEmpty(mTxtFechaConsignacion,mInputLyFechaConsignacion) && res;
        res = !Validation.IsEmpty(mTxtValor, mInputLyValor) && res;
        res = !Validation.IsEmpty(mTxtNroConsignacion, mInputLyNroConsignacion) && res;
        res = !Validation.IsEmpty(mTxtObservaciones, mInputLyObservaciones) && res;
        res = !Validation.IsEmpty(mSpiBanco) && res;


       if (Utils.isAfterDay(Utils.convertToDate(mTxtFechaConsignacion.getText().toString()))){
           mostrarMensajeDialog(getString(R.string.error_fecha_mayor));
           res = false;
           return res;
       }

        long valor = Utils.convertLong(mTxtValor.getText().toString());
        if (valor == 0) {
            mTxtValor.setError(AppCar.getContext().getResources().getString(R.string.error_campo_obligatorio));
            res = false;
        }

        if (!( mSelectedImagePath!= null && !mSelectedImagePath.isEmpty())){
            res = false;
            mostrarMensajeDialog(getString(R.string.error_seleccionar_comprobante));
        }


        return res;
    }

    private  void fillAbono(){
        mAbono = new Abono();
        mAbono.setIDReserva(mIdReserva);
        mAbono.setFechaAbono(Utils.convertToDate(mTxtFechaConsignacion.getText().toString()));
        mAbono.setValorAbono(Utils.convertLong(mTxtValor.getText().toString()));
        mAbono.setNroConsignacion(mTxtNroConsignacion.getText().toString());
        mAbono.setObservacionesAbono(mTxtObservaciones.getText().toString());
        mAbono.setComprobanteAbono(mSelectedImagePath);
        Banco banco = (Banco) mSpiBanco.getSelectedItem();
        if (banco != null)
            mAbono.setBanco(banco.getDetalleBanco());
    }
    private void limpiar(){
        mTxtFechaConsignacion.setText("");
        mTxtValor.setText("");
        mTxtNroConsignacion.setText("");
        mTxtObservaciones.setText("");
        mSelectedImagePath = "";
    }

    private void guardar(){
        if (validar()){
            fillAbono();

            mostrarProgressDialog();
            mSubirArchivoAsincrono  = new SubirArchivoAsincrona();
            mSubirArchivoAsincrono.execute();
        }
    }


    class SubirArchivoAsincrona extends AsyncTask<Void, Integer, Boolean> {

        Abonos abonos = new Abonos();

        @Override
        protected Boolean doInBackground(Void... params) {
            int statusCode = 0;
            if (mAbono != null ) {
                    for (int i = 0; i < 2; i++) {//intentar 2 veces
                        statusCode = abonos.publicarImagen(mAbono);
                        if (statusCode == 200) {
                            break;
                        }
                    }
            }

            return (statusCode == 200);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            if (mProgressDialog != null) mProgressDialog.setProgress(progreso);
        }

        @Override
        protected void onPreExecute() {
            if (mProgressDialog != null) mProgressDialog.setMessage("Enviando archivo");

        }

        @Override
        protected void onPostExecute(final Boolean result) {

            if(result) {
                abonos.insert(mAbono, new IAbono() {
                    @Override
                    public void onSuccess(List<Abono> lstAbono) {

                    }

                    @Override
                    public void onSuccess() {
                        if (mProgressDialog != null) mProgressDialog.dismiss();
                        limpiar();

                        AlertDialog.Builder builder = new AlertDialog.Builder(AbonoActivity.this);

                        builder.setMessage("La consignación fue enviada correctamente");

                        builder.setPositiveButton("Acepar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent i = getIntent();
                                setResult(Activity.RESULT_OK,i);
                                finish();
                            }
                        });

                        builder.show();

                    }

                    @Override
                    public void onError(ErrorApi error) {
                        if (mProgressDialog != null) mProgressDialog.dismiss();
                        mostrarMensajeDialog(error.getMessage());
                    }
                });

            }else{
                if (mProgressDialog != null) mProgressDialog.dismiss();
                mostrarMensajeDialog("Error al subir el arhivo");
            }

        }

        @Override
        protected void onCancelled() {
            if (mProgressDialog != null) mProgressDialog.dismiss();
        }
    }

    private void mostrarProgressDialog(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Enviando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }



    private void tomarFoto() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this,  android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }else {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                if (CreateFile()) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri);
                    startActivityForResult(takePictureIntent, REQUEST_CODE_PHOTO);
                }
            }
        }
    }

    private boolean CreateFile(){
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)){
            mostrarMensaje("Error externa storag no encontrado");
            return false;
        }
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mostrarMensaje("Error externa storag solo lectura");
            return false;
        }

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ServicioCiudanoCAR/";
        File newdir = new File(dir);
        newdir.mkdirs();

        String imageFileName = "Imagen_" + Utils.getFechaActual() + "_.jpg";
        String file = dir+imageFileName;
      //  mImagen.setNombre(imageFileName);

        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {
            return false;
        }

        mOutputFileUri = Uri.fromFile(newfile);
        mSelectedImagePath = mOutputFileUri.getPath();
        return true;
    }


    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_KITKAT_INTENT_CALLED && resultCode == RESULT_OK){

            Uri originalUri = data.getData();

            //  final int takeFlags = data.getFlags()  &   (Intent.FLAG_GRANT_READ_URI_PERMISSION    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            final int takeFlags =    (Intent.FLAG_GRANT_READ_URI_PERMISSION    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            // Check for the freshest data.
            //     getContentResolver().takePersistableUriPermission(originalUri, takeFlags);

            getContentResolver().takePersistableUriPermission(originalUri,takeFlags);

    /* now extract ID from Uri path using getLastPathSegment() and then split with ":"
    then call get Uri to for Internal storage or External storage for media I have used getUri()
    */
            String [] uriSplit = originalUri.getLastPathSegment().split(":");
            if (uriSplit.length > 1) {
                String id =  uriSplit[1];
                final String[] imageColumns = {MediaStore.Images.Media.DATA};
                final String imageOrderBy = null;

                Uri uri = getUri();
                // String selectedImagePath = "path";

                Cursor imageCursor = managedQuery(uri, imageColumns,
                        MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);

                if (imageCursor.moveToFirst()) {
                    mSelectedImagePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }
            }else{
                mostrarMensaje("No se pudo leer el archivo, seleccione otra imagen");
            }

            //  mTxtArchivo.setText(selectedImagePath);

        }else if(requestCode == GALLERY_INTENT_CALLED && resultCode == RESULT_OK) {
            Uri originalUri = data.getData();
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(originalUri, projection, null, null, null);
            cursor.moveToFirst();

            Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));

            int columnIndex = cursor.getColumnIndex(projection[0]);
            mSelectedImagePath = cursor.getString(columnIndex); // returns null
            cursor.close();

            // mTxtArchivo.setText(picturePath);
        }else  if (requestCode == REQUEST_CODE_PHOTO) {
            if (resultCode == RESULT_OK) {
               // newPath
                ImageUtil.Image image = ImageUtil.scaledBitmap(mSelectedImagePath, Enumerator.NAME_DIRECTORY_IMAGES);
                if (image != null && !image.getPath().isEmpty()) {
                    File file = new File(mSelectedImagePath);
                    file.delete();
                    mSelectedImagePath = image.getPath();
                }
            } else if (resultCode == RESULT_CANCELED) {
                File file = new File(mSelectedImagePath);
                file.delete();
                mSelectedImagePath = "";
            } else {
                mSelectedImagePath = "";
            }
        }



        if (resultCode == RESULT_OK && (requestCode == GALLERY_INTENT_CALLED  || requestCode == GALLERY_KITKAT_INTENT_CALLED || requestCode == REQUEST_CODE_PHOTO ) ){
            if (mSelectedImagePath != null && !mSelectedImagePath.isEmpty()) {
                mLyImgComprobante.setVisibility(View.VISIBLE);
                mBtnCapturarComprobante.setVisibility(View.GONE);
                mBtnSeleccionarComprobante.setVisibility(View.GONE);
                Bitmap bitmap = BitmapFactory.decodeFile(mSelectedImagePath);
                mImgComprobante.setImageBitmap(bitmap);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0   && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tomarFoto();
                } else {
                    mostrarMensaje("Permiso denegado para guardar archivos");
                }
                return;
            }
        }
    }

}
