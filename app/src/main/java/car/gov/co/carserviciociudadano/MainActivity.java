package car.gov.co.carserviciociudadano;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.consultapublica.activities.BankProjectActivity;
import car.gov.co.carserviciociudadano.consultapublica.activities.BuscarExpedienteActivity;
import car.gov.co.carserviciociudadano.consultapublica.activities.TramitesActivity;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.denunciaambiental.activities.DenunciaAmbientalActivity;
import car.gov.co.carserviciociudadano.openweather.activities.WeatherActivity;
import car.gov.co.carserviciociudadano.parques.activities.IntentHelper;
import car.gov.co.carserviciociudadano.parques.activities.MainParques;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.model.Parque;


public class MainActivity extends BaseActivity  {

    @BindView(R.id.toolbar_layout)   CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.txtAppVersion)  TextView txtAppVersion;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCollapsingToolbarLayout.setTitle(" ");
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

      // Intent i = new Intent(this, ComoLLegarActivity.class);
       //startActivity(i);
        if (BuildConfig.DEBUG == false) {
            Log.d("DEBUG", "FALSE");
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Main");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Enumerator.ContentTypeAnalitic.PRINCIPAL);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }else{
            Log.d("DEBUG", "TRUE");
        }
        String versionName = BuildConfig.VERSION_NAME;
        txtAppVersion.setText(getString(R.string.copyright) + " " + versionName);

    }

    @OnClick(R.id.lyMenuDenunciaAmbiental) void denunciaAmbiental(){
        Intent i = new Intent(this, DenunciaAmbientalActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.lyMenuParques) void parques(){
        Intent i = new Intent(this,MainParques.class);
        startActivity(i);
    }

    @OnClick(R.id.lyMenuExpedientes) void expedientes(){
        Intent i = new Intent(this, BuscarExpedienteActivity.class);
        startActivity(i);
    }
    @OnClick(R.id.lyMenuTramites) void tramites(){
        Intent i = new Intent(this, TramitesActivity.class);
        startActivity(i);
    }
    @OnClick(R.id.lyMenuProyectosConfinacion) void proyectoConfinacion(){
        Intent i = new Intent(this, BankProjectActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.lblAyuda) void ayuda(){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:QiTHBaKsTb8"));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=QiTHBaKsTb8"));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

}
