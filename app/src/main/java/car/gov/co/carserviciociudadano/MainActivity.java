package car.gov.co.carserviciociudadano;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.Utils.Config;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.activities.MainBicicarActivity;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.consultapublica.activities.BankProjectActivity;
import car.gov.co.carserviciociudadano.consultapublica.activities.BuscarExpedienteActivity;
import car.gov.co.carserviciociudadano.consultapublica.activities.TramitesActivity;
import car.gov.co.carserviciociudadano.denunciaambiental.activities.DenunciaAmbientalActivity;
import car.gov.co.carserviciociudadano.parques.activities.MainParques;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;
import car.gov.co.carserviciociudadano.parques.presenter.IViewParque;
import car.gov.co.carserviciociudadano.parques.presenter.ParquePresenter;
import car.gov.co.carserviciociudadano.petcar.activities.MainPETCARActivity;


public class MainActivity extends BaseActivity implements IViewParque {

    @BindView(R.id.toolbar_layout)   CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.txtAppVersion)  TextView txtAppVersion;
    @BindView(R.id.lyItemsConsultas) View lyItemsConsultas;
    @BindView(R.id.lyMenuConsultas)  CardView lyMenuConsultas;
    @BindView(R.id.lyMenuSuAporteAmbiental)  CardView lyMenuSuAporteAmbiental;
    @BindView(R.id.lyItemsSuAporteAmbiental)  View lyItemsSuAporteAmbiental;

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

       // Intent i = new Intent(this, ConfigActivity.class);
       // startActivity(i);

       //  Intent i = new Intent(this, EventosActivity.class);
        // startActivity(i);

        lyItemsConsultas.setVisibility(View.GONE);
        lyItemsSuAporteAmbiental.setVisibility(View.GONE);
        obtenerParques();
    }

    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(Parques.TAG);
        super.onPause();
    }
    @Override
    public void onDestroy() {
        AppCar.VolleyQueue().cancelAll(Parques.TAG);
        super.onDestroy();
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

    @OnClick(R.id.lyMenuSuAporteAmbiental) void onMenuSuAporteAmbiental(){
        if (lyItemsSuAporteAmbiental.getVisibility() == View.VISIBLE){
            lyItemsSuAporteAmbiental.setVisibility(View.GONE);
        }else{
            lyItemsSuAporteAmbiental.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.lyBiciCAR) void bicicar(){
        Intent i = new Intent(this, MainBicicarActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.lyCicloReciclo) void cicloReciclo(){
          Intent i = new Intent(this, MainPETCARActivity.class);
         startActivity(i);
    }

    /**
     * Obtener parques para que se guarde en cache por 20 dias
     */
    private void obtenerParques(){
        String url = Config.API_PARQUES_PARQUES;
        final String keycache = url + BuildConfig.VERSION_CODE;
        if (!Utils.existeCache(keycache) ) {
            ParquePresenter parquePresenter = new ParquePresenter(this);
            parquePresenter.list();
        }
    }


    @Override
    public void onSuccess(List<Parque> lstParques) {

    }

    @Override
    public void onError(ErrorApi error) {

    }
}
