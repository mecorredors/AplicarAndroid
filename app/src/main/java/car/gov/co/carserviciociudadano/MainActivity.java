package car.gov.co.carserviciociudadano;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.Utils.Security;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.consultapublica.activities.TramitesActivity;
import car.gov.co.carserviciociudadano.consultapublica.dataaccesss.Tramites;
import car.gov.co.carserviciociudadano.consultapublica.interfaces.ITramite;
import car.gov.co.carserviciociudadano.consultapublica.model.Tramite;
import car.gov.co.carserviciociudadano.parques.activities.BaseActivity;
import car.gov.co.carserviciociudadano.parques.activities.MainParques;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mCollapsingToolbarLayout.setTitle(getTitle());
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

//        Intent i = new Intent(this,MainParques.class);
//        startActivity(i);

//        Intent i = new Intent(this,UsuarioActivity.class);
//        startActivity(i);


        new Tramites().list("04091100001", new ITramite() {
            @Override
            public void onSuccess(Tramite tramite) {
                Log.d(Tramites.TAG, tramite.toString());
            }

            @Override
            public void onError(ErrorApi error) {
                Log.d(Tramites.TAG, error.toString());
            }
        });



    }

    @OnClick(R.id.lyMenuParques) void parques(){
        Intent i = new Intent(this,MainParques.class);
        startActivity(i);
    }

    @OnClick(R.id.lyMenuExpedientes) void expedientes(){
       mostrarMensaje("No implementado");
    }
    @OnClick(R.id.lyMenuTramites) void tramites(){
       Intent i = new Intent(this, TramitesActivity.class);
        startActivity(i);
    }
}
