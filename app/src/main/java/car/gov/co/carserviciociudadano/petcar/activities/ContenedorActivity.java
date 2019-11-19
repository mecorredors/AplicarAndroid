package car.gov.co.carserviciociudadano.petcar.activities;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.Utils.Validation;
import car.gov.co.carserviciociudadano.bicicar.activities.UbicacionBeneficiarioActivity;
import car.gov.co.carserviciociudadano.bicicar.model.TipoEvento;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.denunciaambiental.dataacces.Elevation;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.ElevationPresenter;
import car.gov.co.carserviciociudadano.denunciaambiental.presenter.IViewElevation;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Contenedores;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Gestores;
import car.gov.co.carserviciociudadano.petcar.dataaccess.TiposMaterial;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;
import car.gov.co.carserviciociudadano.petcar.model.Gestor;
import car.gov.co.carserviciociudadano.petcar.model.TipoMaterial;
import car.gov.co.carserviciociudadano.petcar.presenter.ContenedorPresenter;
import car.gov.co.carserviciociudadano.petcar.presenter.IViewContenedor;

public class ContenedorActivity extends BaseActivity implements IViewElevation, IViewContenedor {
    @BindView(R.id.txtFechaInstalacion) EditText txtFechaInstalacion;
    @BindView(R.id.txtCodigo) EditText txtCodigo;
    @BindView(R.id.txtDireccion) EditText txtDireccion;
    @BindView(R.id.lblMunicipio) TextView lblMunicipio;
    @BindView(R.id.txtLatitud) EditText txtLatitud;
    @BindView(R.id.txtLongitud) EditText txtLongitud;
    @BindView(R.id.txtAltitud) EditText txtAltitud;
    @BindView(R.id.lyCodigo) TextInputLayout lyCodigo;
    @BindView(R.id.lyDireccion) TextInputLayout lyDireccion;
    @BindView(R.id.calendarioFechaInstalacion) CustomCalendarView calendarioFechaInstalacion;
    @BindView(R.id.lyPrincipal)  View lyPrincipal;
    @BindView(R.id.btnUbicacion) Button btnUbicacion;
    @BindView(R.id.btnUbicacionMapa) Button btnUbicacionMapa;
    @BindView(R.id.pbUbicacion) ProgressBar pbUbicacion;
    @BindView(R.id.lblPresicion) TextView lblPresicion;
    @BindView(R.id.lyTiposMaterial) LinearLayout lyTiposMaterial;

    Contenedor mContenedor;
    Gestor mGestor;
    ElevationPresenter mElevationPresenter;
    ContenedorPresenter mContenedorPresenter;
    private static final int REQUEST_UBICACION = 101;
    public  final static int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 2;
    private float mAccuracy = Float.MAX_VALUE;
    private  boolean isRunningElevation = false;
    private LocationManager mLocationManager;
    boolean isRunning = false;
    boolean gps_enabled=false;
    boolean network_enabled=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenedor);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        mGestor = new Gestores().getLogin();
        mElevationPresenter = new ElevationPresenter(this);
        mContenedorPresenter = new ContenedorPresenter(this);

        Bundle b = getIntent().getExtras();
        if (b != null){
            int idContenedor = b.getInt(Contenedor.IDCONTENEDOR, 0);
            String idMunicipio = b.getString(Contenedor.IDMUNICIPIO, "");
            String municipio = b.getString(Contenedor.MUNICIPIO, "");
            lblMunicipio.setText( municipio);
            if (idContenedor > 0 ){
                mContenedor = new Contenedores().read(idContenedor);
                txtCodigo.setText(mContenedor.Codigo);
                txtDireccion.setText(mContenedor.Direccion);
                txtLatitud.setText(String.valueOf(mContenedor.Latitude));
                txtLongitud.setText(String.valueOf(mContenedor.Longitude));
                txtAltitud.setText(String.valueOf(mContenedor.Altitud));
                txtFechaInstalacion.setText(Utils.toStringFromDate(mContenedor.FechaInstalacion));
                txtCodigo.setEnabled(false);
                txtFechaInstalacion.setEnabled(false);
            }else{
                mContenedor = new Contenedor();
                mContenedor.IDMunicipio = idMunicipio;
                mContenedor.Municipio = municipio;
                txtCodigo.setText(idMunicipio.trim());
                addTiposMaterial();
            }

        }

        txtFechaInstalacion.setOnClickListener(onClickListener);
        btnUbicacion.setOnClickListener(onClickListener);
        btnUbicacionMapa.setOnClickListener(onClickListener);

        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        calendarioFechaInstalacion.refreshCalendar(currentCalendar);

        calendarioFechaInstalacion.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                txtFechaInstalacion.setText(Utils.toStringFromDate(date));
                calendarioFechaInstalacion.setVisibility(View.GONE);
                mContenedor.FechaInstalacion = date;
            }

            @Override
            public void onMonthChanged(Date date) {

            }
        });
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

    private boolean validar(){
        boolean res = true;


        if (Validation.IsEmpty(txtCodigo, lyCodigo)) res = false;
        if (Validation.IsEmpty(txtDireccion, lyDireccion)) res = false;
        if (Validation.IsEmpty(txtFechaInstalacion)) res = false;
        if (Validation.IsEmpty(txtLatitud)) res = false;
        if (Validation.IsEmpty(txtLongitud)) res = false;

        if (txtCodigo.getText().toString().trim().length() != 9){
            mostrarMensajeDialog("El código debe tener 9 caracteres");
            return false;
        }


        if ( mContenedor.FechaInstalacion == null) {
            mostrarMensajeDialog("Seleccione fecha de instalación");
            return false;
        }

        if (mContenedor.Latitude == 0 || mContenedor.Longitude == 0){
            mostrarMensajeDialog("Seleccione ubicación del contenedor");
            return false;
        }

        if (mContenedor.IDContenedor == 0) {
            int numMateriales = 0;
            String materiales = "";
            for (int i = 0; i < lyTiposMaterial.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) lyTiposMaterial.getChildAt(i);
                if (checkBox.isChecked()) {
                    numMateriales++;
                    materiales += "," + checkBox.getId();

                }
            }

            if (numMateriales == 0) {
                mostrarMensajeDialog("Seleccione tipos de materiales");
                return false;
            }

            mContenedor.IDMaterialLista = materiales.substring(1);
        }

        return res;
    }

    @OnClick(R.id.btnGuardar) void onGuardar(){
        guardar();
    }

    private void guardar(){
        if (validar()){
            mContenedor.Codigo = txtCodigo.getText().toString();
            mContenedor.Direccion = txtDireccion.getText().toString();
            if (mContenedor.IDContenedor > 0) {
                mContenedor.UsuarioModificacion = mGestor.Identificacion;
            }
            else {
                mContenedor.UsuarioCreacion = mGestor.Identificacion;
            }
            mostrarProgressDialog("Guardando");
            mContenedorPresenter.publicar(mContenedor);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.txtFechaInstalacion:
                    calendarioFechaInstalacion.setVisibility(View.VISIBLE);
                    ocultarTeclado(lyPrincipal);
                    break;
                case R.id.btnGuardar:

                    break;
                case R.id.btnUbicacion:
                    localizacion();
                    break;
                case R.id.btnUbicacionMapa:
                    cancel();
                    Intent j = new Intent(ContenedorActivity.this , UbicacionBeneficiarioActivity.class);
                    j.putExtra(UbicacionBeneficiarioActivity.RETORNAR_UBICACION , true);
                    j.putExtra(UbicacionBeneficiarioActivity.LATITUDE, mContenedor.Latitude);
                    j.putExtra(UbicacionBeneficiarioActivity.LONGITUDE, mContenedor.Longitude);

                    startActivityForResult(j , REQUEST_UBICACION);
                    break;
            }
        }
    };

    @Override
    public void onSuccessElevation(double elevation) {
        isRunningElevation = false;
        ocultarProgressDialog();
        mContenedor.Altitud = Utils.round(2 ,elevation);
        txtAltitud.setText(String.valueOf( mContenedor.Altitud ));
    }

    @Override
    public void onErrrorElevation(int statusCode) {
        ocultarProgressDialog();
        isRunningElevation = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.error_load_elevation));
        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mElevationPresenter.getElevation(mContenedor.Latitude,mContenedor.Longitude);
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_UBICACION){

                mAccuracy = Float.MAX_VALUE;
                setResult(data.getDoubleExtra(UbicacionBeneficiarioActivity.LATITUDE, 0), data.getDoubleExtra(UbicacionBeneficiarioActivity.LONGITUDE, 0), 0,0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0   && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    localizacion();
                } else {
                    mostrarMensaje("Permiso denegado para obtener ubicacion");
                }
                return;
            }
        }
    }


    /**
     * Obtener ubicacion sin internet
     */

    private void localizacion(){
        // if (mTipo == Enumeradores.Tipo.VisitProperty) return;

        if (isRunning) {
            cancel();
        } else {
            mAccuracy = Float.MAX_VALUE;
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            try {
                gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }
            try {
                network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
            }

            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION ,  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                        MY_PERMISSIONS_ACCESS_FINE_LOCATION );

            }else {

                if (gps_enabled == false) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }

                if (gps_enabled) {
                    btnUbicacion.setText("Detener");
                    pbUbicacion.setVisibility(View.VISIBLE);
                    isRunning = true;
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListenerGps);
                    Log.d(" gps enabled", "gps ejecutadi");
                }
                if (network_enabled) {
                    btnUbicacion.setText("Detener");
                    pbUbicacion.setVisibility(View.VISIBLE);
                    isRunning = true;
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListenerNetwork);
                    Log.d("network enable", "ejecutado");
                }
            }
        }
    }
    public void cancel()
    {
        if (isRunning) {
            isRunning = false;
            isRunningElevation = false;
            btnUbicacion.setText(getString(R.string.ubicacion));
            pbUbicacion.setVisibility(View.GONE);

            if (mLocationManager != null) {
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("permiso", "Permiso denegado");
                    return;
                }

                mLocationManager.removeUpdates(locationListenerGps);
                mLocationManager.removeUpdates(locationListenerNetwork);

            }
            AppCar.VolleyQueue().cancelAll(Elevation.TAG);
        }
    }

    private void setResult(double latitude, double longitude, double altitud, float accuracy){
        if (accuracy < mAccuracy ) {

            mContenedor.Latitude = Utils.round(7 , latitude);
            mContenedor.Longitude = Utils.round(7 ,longitude);
            txtLatitud.setText(String.valueOf(mContenedor.Latitude));
            txtLongitud.setText(String.valueOf(mContenedor.Longitude));

            lblPresicion.setText("Presición" + String.valueOf(accuracy));
            mAccuracy = accuracy;

            if (altitud != 0) {
                txtAltitud.setText(String.valueOf(altitud));
            }
        }

        //consulta altitud en api google elevation
        if (isRunningElevation == false && txtAltitud.getText().length() == 0) {
            if (altitud != 0){
                txtAltitud.setText(String.valueOf(altitud));
            }
            isRunningElevation = true;
            mostrarProgressDialog("Obteniendo Altitud", true);
            mElevationPresenter.getElevation(mContenedor.Latitude , mContenedor.Longitude);
        }
    }


    final LocationListener locationListenerGps = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            setResult(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy());
            //  resultLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(AppCar.getContext(), "Gps is turned on!! ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            Toast.makeText(AppCar.getContext(), "Gps is turned off!! ", Toast.LENGTH_SHORT).show();
        }
    };

    final LocationListener locationListenerNetwork = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            setResult(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(AppCar.getContext(), "Gps is turned on!! ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            Toast.makeText(AppCar.getContext(), "Gps is turned off!! ", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onSuccessContenedores(List<Contenedor> lstContenedores) {

    }

    @Override
    public void onErrorContenedores(ErrorApi error) {

    }

    @Override
    public void onSuccessAgregar(Contenedor contenedor) {
        ocultarProgressDialog();
        mostrarMensajePublicado("El contenedor fue creado correctamente");
    }

    @Override
    public void onSuccessModificar(Contenedor contenedor) {
        mostrarMensajePublicado("El contenedor fue modificado correctamente");
    }

    @Override
    public void onErrorAgregar(ErrorApi error) {
        ocultarProgressDialog();
        mostrarMensajeDialog(error.getMessage());
    }

    @Override
    public void onErrorModificar(ErrorApi error) {
        ocultarProgressDialog();
        mostrarMensajeDialog(error.getMessage());
    }

    private void mostrarMensajePublicado(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                setResult(RESULT_OK);
                finish();

            }
        });

        builder.show();
    }
}
