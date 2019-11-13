package car.gov.co.carserviciociudadano.Utils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.common.BaseActivity;

public class ConfigActivity extends BaseActivity {

    @BindView(R.id.txtServerBicicar) TextView txtServerBicicar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);


        txtServerBicicar.setText( PreferencesApp.getDefault(PreferencesApp.READ).getString("SERVER_BICICAR", Server.ServerBICICAR));
    }


    @OnClick(R.id.btnGuardar) void onGuardar(){
        PreferencesApp.getDefault(PreferencesApp.WRITE).putString("SERVER_BICICAR", txtServerBicicar.getText().toString()).commit();

      mostrarMensajeDialog("Debe cerrar el app y volver a ingresar para tomar los cambios de url");
    }

    @OnClick(R.id.btnCerrar) void onCerrar(){
        super.onBackPressed();
    }
}
