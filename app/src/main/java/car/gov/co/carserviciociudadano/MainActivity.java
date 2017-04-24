package car.gov.co.carserviciociudadano;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

import com.anupcowkur.reservoir.Reservoir;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.consultapublica.activities.BuscarExpedienteActivity;
import car.gov.co.carserviciociudadano.consultapublica.activities.ExpedienteActivity;
import car.gov.co.carserviciociudadano.consultapublica.activities.TramitesActivity;
import car.gov.co.carserviciociudadano.consultapublica.dataaccesss.Documentos;
import car.gov.co.carserviciociudadano.consultapublica.dataaccesss.Expedientes;
import car.gov.co.carserviciociudadano.consultapublica.interfaces.IDocumento;
import car.gov.co.carserviciociudadano.consultapublica.interfaces.IExpediente;
import car.gov.co.carserviciociudadano.consultapublica.model.Documento;
import car.gov.co.carserviciociudadano.consultapublica.model.Expediente;
import car.gov.co.carserviciociudadano.consultapublica.model.ExpedienteResumen;
import car.gov.co.carserviciociudadano.parques.activities.BaseActivity;
import car.gov.co.carserviciociudadano.parques.activities.ComoLLegarActivity;
import car.gov.co.carserviciociudadano.parques.activities.MainParques;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar_layout)   CollapsingToolbarLayout mCollapsingToolbarLayout;


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

      // Intent i = new Intent(this, ComoLLegarActivity.class);
       //startActivity(i);

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
}
