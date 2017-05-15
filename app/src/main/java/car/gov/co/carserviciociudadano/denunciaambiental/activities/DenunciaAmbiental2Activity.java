package car.gov.co.carserviciociudadano.denunciaambiental.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import butterknife.ButterKnife;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.common.BaseActivity;

public class DenunciaAmbiental2Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia_ambiental2);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
    }
}
