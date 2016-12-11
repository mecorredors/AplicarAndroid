package car.gov.co.carserviciociudadano;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.parques.activities.BaseActivity;
import car.gov.co.carserviciociudadano.parques.activities.MainParques;


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

    }

    @OnClick(R.id.lyMenuParques) void parques(){
        Intent i = new Intent(this,MainParques.class);
        startActivity(i);
    }

    @OnClick(R.id.lyMenuExpedientes) void expedientes(){
       mostrarMensaje("No implementado");
    }
    @OnClick(R.id.lyMenuTramites) void tramites(){
       mostrarMensaje("No implementado");
    }
}
