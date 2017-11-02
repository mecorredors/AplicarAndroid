package car.gov.co.carserviciociudadano.parques.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import car.gov.co.carserviciociudadano.R;

public class AyudaPagoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda_pago);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
    }
}
