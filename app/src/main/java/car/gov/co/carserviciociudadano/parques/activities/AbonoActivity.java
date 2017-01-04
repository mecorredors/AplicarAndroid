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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.FechaDialogo;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.Utils.Validation;
import car.gov.co.carserviciociudadano.parques.dataaccess.Abonos;
import car.gov.co.carserviciociudadano.parques.dataaccess.Bancos;
import car.gov.co.carserviciociudadano.parques.dataaccess.Reservas;
import car.gov.co.carserviciociudadano.parques.interfaces.IAbono;
import car.gov.co.carserviciociudadano.parques.interfaces.IBanco;
import car.gov.co.carserviciociudadano.parques.model.Abono;
import car.gov.co.carserviciociudadano.parques.model.Banco;
import car.gov.co.carserviciociudadano.parques.model.DetalleReserva;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Municipio;
import car.gov.co.carserviciociudadano.parques.model.ParametroReserva;
import car.gov.co.carserviciociudadano.parques.model.ServicioParque;

public class AbonoActivity extends BaseActivity {



    @BindView(R.id.spiBanco)   Spinner mSpiBanco;
    @BindView(R.id.activity_abono)  View mActivity_abono;
    @BindView(R.id.txtFechaConsignacion) EditText mTxtFechaConsignacion;
    @BindView(R.id.txtArchivo)     EditText mTxtArchivo;
    @BindView(R.id.txtValor)     EditText mTxtValor;
    @BindView(R.id.txtNroConsignacion)     EditText mTxtNroConsignacion;
    @BindView(R.id.txtObservaciones)     EditText mTxtObservaciones;

    List<Banco> mLstBancos = new ArrayList<>();
    ArrayAdapter<Banco> adapterBancos;

    public static final  String TAG = "Abono";
    public final static int GALLERY_INTENT_CALLED = 1;
    public final static int GALLERY_KITKAT_INTENT_CALLED = 2;

    public  static final  int MY_PERMISSION_READ_EXTERNAL_STORAGE = 11;

    Abono mAbono;
    ProgressDialog mProgressDialog;

    SubirArchivoAsincrona mSubirArchivoAsincrono;

    private long mIdReserva;
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
    @OnClick(R.id.txtArchivo) void onArchivo() {
        if (Build.VERSION.SDK_INT <19){
            Intent intent = new Intent();
            intent.setType("image/jpeg");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.seleccionar_comprobante)),GALLERY_INTENT_CALLED);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/jpeg");
            startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
        }
    }

    @OnClick(R.id.btnGuardar) void onGuardar() {
       guardar();
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

            String id = originalUri.getLastPathSegment().split(":")[1];
            final String[] imageColumns = {MediaStore.Images.Media.DATA };
            final String imageOrderBy = null;

            Uri uri = getUri();
            String selectedImagePath = "path";

            Cursor imageCursor = managedQuery(uri, imageColumns,
                    MediaStore.Images.Media._ID + "="+id, null, imageOrderBy);

            if (imageCursor.moveToFirst()) {
                selectedImagePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }

            mTxtArchivo.setText(selectedImagePath);

        }else if(resultCode == RESULT_OK) {
            Uri originalUri = data.getData();
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(originalUri, projection, null, null, null);
            cursor.moveToFirst();

            Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null
            cursor.close();

            mTxtArchivo.setText(picturePath);
        }
    }



    // By using this method get the Uri of Internal/External Storage for Media
    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    private void loadBancos(){

        Bancos bancos = new Bancos();
        bancos.list(new IBanco() {
            @Override
            public void onSuccess(List<Banco> lstBancos) {
                mLstBancos.clear();
                mLstBancos.addAll(lstBancos);
                mLstBancos.add(0,new Banco(0,"Banco"));
                adapterBancos = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, mLstBancos);
                adapterBancos.setDropDownViewResource( android.R.layout.select_dialog_singlechoice);
                mSpiBanco.setAdapter(adapterBancos);
                adapterBancos.notifyDataSetChanged();

               // mSpiBanco.setSelection(adapterBancos.getPosition(new Municipio(mUsuario.getIDMunicipio(),"")));
                //mSpiMunicipio.setOnItemSelectedListener(this);
            }

            @Override
            public void onError(ErrorApi error) {
                mostrarMensaje("No fue posible obtener los bancos",mActivity_abono );

            }
        });

    }


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

        res = !Validation.IsEmpty(mTxtFechaConsignacion) && res;
        res = !Validation.IsEmpty(mTxtValor) && res;
        res = !Validation.IsEmpty(mTxtNroConsignacion) && res;
        res = !Validation.IsEmpty(mTxtObservaciones) && res;
        res = !Validation.IsEmpty(mTxtArchivo) && res;
        res = !Validation.IsEmpty(mSpiBanco) && res;

        long valor = Utils.convertLong(mTxtValor.getText().toString());
        if (valor == 0) {
            mTxtValor.setError(AppCar.getContext().getResources().getString(R.string.error_campo_obligatorio));
            res = false;
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
        mAbono.setComprobanteAbono(mTxtArchivo.getText().toString());
        Banco banco = (Banco) mSpiBanco.getSelectedItem();
        if (banco != null)
            mAbono.setBanco(banco.getDetalleBanco());
    }
    private void limpiar(){
        mTxtFechaConsignacion.setText("");
        mTxtValor.setText("");
        mTxtNroConsignacion.setText("");
        mTxtObservaciones.setText("");
        mTxtArchivo.setText("");
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




}
