package car.gov.co.carserviciociudadano;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.bicicar.activities.MainBicicarActivity;
import car.gov.co.carserviciociudadano.bicicar.activities.RegistrarActividadActivity;

import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.consultapublica.activities.BankProjectActivity;
import car.gov.co.carserviciociudadano.consultapublica.activities.BuscarExpedienteActivity;
import car.gov.co.carserviciociudadano.consultapublica.activities.TramitesActivity;
import car.gov.co.carserviciociudadano.denunciaambiental.activities.DenunciaAmbientalActivity;
import car.gov.co.carserviciociudadano.parques.activities.MainParques;
import car.gov.co.carserviciociudadano.petcar.activities.MainPETCARActivity;


public class MainActivity extends BaseActivity  {

    @BindView(R.id.toolbar_layout)   CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.txtAppVersion)  TextView txtAppVersion;
    @BindView(R.id.lyItemsConsultas) View lyItemsConsultas;
    @BindView(R.id.lyMenuConsultas)  CardView lyMenuConsultas;

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

      //  Intent i = new Intent(this, MainPETCARActivity.class);
       // startActivity(i);

       lyItemsConsultas.setVisibility(View.GONE);

    }



    @OnClick(R.id.lyMenuDenunciaAmbiental) void denunciaAmbiental(){
        Intent i = new Intent(this, DenunciaAmbientalActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.lyMenuParques) void parques(){
        Intent i = new Intent(this,MainParques.class);
        startActivity(i);
    }

    @OnClick(R.id.lyExpedientes) void expedientes(){
        Intent i = new Intent(this, BuscarExpedienteActivity.class);
        startActivity(i);
    }
    @OnClick(R.id.lyTramites) void tramites(){
        Intent i = new Intent(this, TramitesActivity.class);
        startActivity(i);
    }
    @OnClick(R.id.lyProyectosConfinacion) void proyectoConfinacion(){
        Intent i = new Intent(this, BankProjectActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.lyMenuConsultas) void onMenuConsultas(){
        if (lyItemsConsultas.getVisibility() == View.VISIBLE){
            lyItemsConsultas.setVisibility(View.GONE);
        }else{
            lyItemsConsultas.setVisibility(View.VISIBLE);
        }
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

    @OnClick(R.id.lyMenuBicicar) void bicicar(){
        Intent i = new Intent(this, MainBicicarActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.lyMenuPETCAR) void petcar(){
          Intent i = new Intent(this, MainPETCARActivity.class);
         startActivity(i);
    }



}
