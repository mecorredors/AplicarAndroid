package car.gov.co.carserviciociudadano.petcar.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Config;
import car.gov.co.carserviciociudadano.Utils.PreferencesApp;
import car.gov.co.carserviciociudadano.Utils.Server;
import car.gov.co.carserviciociudadano.bicicar.activities.WebViewBicicarActivity;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.adapter.MunicipiosAdapter;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Gestores;
import car.gov.co.carserviciociudadano.petcar.fragments.MapPETCARFragment;
import car.gov.co.carserviciociudadano.petcar.model.Gestor;
import car.gov.co.carserviciociudadano.petcar.model.Municipio;
import car.gov.co.carserviciociudadano.petcar.presenter.IViewMunicipios;
import car.gov.co.carserviciociudadano.petcar.presenter.MunicipiosPresenter;

public class MainPETCARActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, IViewMunicipios {

    Menu navMenu;
    NavigationView navigationView;
    DrawerLayout drawer;
    TextView lblUsuario;
    @BindView(R.id.recyclerMunicipios) RecyclerView mRecyclerMunicipios;
    @BindView(R.id.lyMunicipios) View mLyMunicipios;
    @BindView(R.id.lyGuia) View mLyGuia;
    @BindView(R.id.menu_bar) BottomNavigationViewEx menu_bar;

    MunicipiosAdapter mAdaptador;
    List<Municipio> mLstMunicipios;
    MunicipiosPresenter municipiosPresenter;
    MapPETCARFragment mMapPETCARFragment;
    Gestor mGestor;
    public static final int REQUEST_LOGIN = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_petcar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        drawer =  findViewById(R.id.drawer_layout);


        //bnve.enableAnimation(false);
        menu_bar.enableShiftingMode(false);
        menu_bar.enableItemShiftingMode(false);
        menu_bar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

       openMap();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navMenu = navigationView.getMenu();
        lblUsuario = navigationView.getHeaderView(0).findViewById(R.id.lblUsuario);


        configMenuLateral();
        cambiarClave();

        mLstMunicipios = new ArrayList<>();
        mAdaptador = new MunicipiosAdapter( mLstMunicipios);
        mAdaptador.setOnClickListener(this);
        mRecyclerMunicipios.setAdapter(mAdaptador);
        mRecyclerMunicipios.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerMunicipios.setItemAnimator(new DefaultItemAnimator());
        municipiosPresenter = new MunicipiosPresenter(this);
        mLyMunicipios.setVisibility(View.GONE);
        mLyGuia.setVisibility(View.GONE);

        mGestor = new Gestores().getLogin();
        if (mGestor.TipoGestor == 0){
            mostrarMensajeDialog("Es necesario cerrar sesión y volver a ingresar para verificar su perfil");
        }

    }
    private void cambiarClave(){
        mGestor = new Gestores().getLogin();
        if (mGestor != null) {
            if (PreferencesApp.getDefault(PreferencesApp.READ).getBoolean(CambiarPasswordActivity.CAMBIAR_CLAVE_1_VEZ_PETCAR, true)) {
                Intent i = new Intent(this, CambiarPasswordActivity.class);
                startActivity(i);
            }
        }
    }
    private void configMenuLateral(){
        mGestor = new Gestores().getLogin();
        if (mGestor != null){

            navMenu.findItem(R.id.nav_login).setVisible(false);
            navMenu.findItem(R.id.nav_cerrar_sesion).setVisible(true);
            navMenu.findItem(R.id.nav_cambiar_clave).setVisible(true);
            lblUsuario.setText(mGestor.NombreCompleto);
            navMenu.findItem(R.id.nav_contenedores).setVisible(true);
          if (mGestor.getTipoGestor() == Gestor.Tipo.INSTALADOR ||  mGestor.getTipoGestor() == Gestor.Tipo.INSTALDOR_VISITA){
              navMenu.findItem(R.id.nav_publicar_material).setVisible(false);
              navMenu.findItem(R.id.nav_registrar_material).setVisible(false);
              navMenu.findItem(R.id.nav_reporte).setVisible(false);
          }else {

              navMenu.findItem(R.id.nav_publicar_material).setVisible(true);
              navMenu.findItem(R.id.nav_registrar_material).setVisible(true);
              navMenu.findItem(R.id.nav_reporte).setVisible(true);

          }

        }else{
            navMenu.findItem(R.id.nav_login).setVisible(true);
            navMenu.findItem(R.id.nav_cerrar_sesion).setVisible(false);
            navMenu.findItem(R.id.nav_publicar_material).setVisible(false);
            navMenu.findItem(R.id.nav_contenedores).setVisible(false);
            navMenu.findItem(R.id.nav_registrar_material).setVisible(false);
            navMenu.findItem(R.id.nav_cambiar_clave).setVisible(false);
            navMenu.findItem(R.id.nav_reporte).setVisible(false);
            lblUsuario.setText("");
        }


    }

    private void obtenerMunicipios(){
        mLyMunicipios.setVisibility(View.VISIBLE);
        mLyMunicipios.startAnimation(AnimationUtils.loadAnimation(AppCar.getContext(), R.anim.slide_up));
        municipiosPresenter.getMunicipios();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();
            finish();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_petcar, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {



            switch (item.getItemId()) {
                case R.id.item_mapa:
                    if (mLyMunicipios.getVisibility() == View.VISIBLE) {
                        mLyMunicipios.setVisibility(View.GONE);
                        mLyMunicipios.startAnimation(AnimationUtils.loadAnimation(AppCar.getContext(), R.anim.slide_dow));
                        mMapPETCARFragment.cerrarDetalle();
                    }else if (mLyGuia.getVisibility() == View.VISIBLE) {
                        mLyGuia.setVisibility(View.GONE);
                        mLyGuia.startAnimation(AnimationUtils.loadAnimation(AppCar.getContext(), R.anim.slide_dow));
                        mMapPETCARFragment.cerrarDetalle();
                    }else
                        openMap();
                    return true;
                case R.id.item_municipio:
                    mLyGuia.setVisibility(View.GONE);
                    if (mLyMunicipios.getVisibility() == View.VISIBLE) {
                        mLyMunicipios.setVisibility(View.GONE);
                        mLyMunicipios.startAnimation(AnimationUtils.loadAnimation(AppCar.getContext(), R.anim.slide_dow));
                    }else
                        obtenerMunicipios();

                    mMapPETCARFragment.cerrarDetalle();

                    return true;
                case R.id.item_guia:
                    mMapPETCARFragment.cerrarDetalle();
                    mLyMunicipios.setVisibility(View.GONE);
                    if (mLyGuia.getVisibility() == View.VISIBLE) {
                        mLyGuia.setVisibility(View.GONE);
                        mLyGuia.startAnimation(AnimationUtils.loadAnimation(AppCar.getContext(), R.anim.slide_dow));
                    }else {
                        mLyGuia.setVisibility(View.VISIBLE);
                        mLyGuia.startAnimation(AnimationUtils.loadAnimation(AppCar.getContext(), R.anim.slide_up));
                    }

                    return true;
                case R.id.item_mas:
                    mLyGuia.setVisibility(View.GONE);
                    mLyMunicipios.setVisibility(View.GONE);
                    mMapPETCARFragment.cerrarDetalle();
                    drawer.openDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        }
    };


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle menu_bar_petcar view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            loginPetCar();
        } else if (id == R.id.nav_cerrar_sesion) {
           cerrarSesion();
        }else if (id == R.id.nav_co2) {
            mostrarMensaje("Próximamente, en desarrollo");
        }else if (id == R.id.nav_contenedores) {
            if (mGestor != null) {
                Intent i = new Intent(this, ContenedoresActivity.class);
                startActivity(i);
            }else{
                loginPetCar();
            }
        }else if (id == R.id.nav_registrar_material) {
            if (mGestor != null) {
            Intent i = new Intent(this, RegistrarMaterialActivity.class);
            startActivity(i);
            }else{
                loginPetCar();
            }
        }else if (id == R.id.nav_publicar_material) {
            if (mGestor != null) {
            Intent i = new Intent(this, PublicarMaterialActivity.class);
            startActivity(i);
            }else{
                loginPetCar();
            }
        }else if (id == R.id.nav_cambiar_clave) {
            if (mGestor != null) {
                Intent i = new Intent(this, CambiarPasswordActivity.class);
                startActivity(i);
            }else{
                loginPetCar();
            }
        }else if (id == R.id.nav_reporte) {
            if (mGestor != null) {
                Intent i = new Intent(this, WebViewBicicarActivity.class);
                i.putExtra(WebViewBicicarActivity.URL, Server.ServerBICICAR() + "Modulos/PETCAR/ReportePublico?IDGestor="+mGestor.IDGestor);
                i.putExtra(WebViewBicicarActivity.TITULO, "Reporte de material recogido");
                startActivity(i);
            }else{
                loginPetCar();
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private  void loginPetCar(){
        Intent i = new Intent(this, LoginPetCarActivity.class);
        startActivityForResult(i , REQUEST_LOGIN);
    }

    private void cerrarSesion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainPETCARActivity.this);

        builder.setMessage("¿Seguro desea cerrar sesión?");

        builder.setPositiveButton(R.string.cerrar_sesion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (mGestor != null){
                    new Gestores().delete(mGestor.IDGestor);
                    configMenuLateral();
                }
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
    private void openMap() {

        mMapPETCARFragment  =  MapPETCARFragment.newInstance();
        mMapPETCARFragment.setRetainInstance(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, mMapPETCARFragment, "MAP_PETCAR")
                .addToBackStack(null).commit();
    }

    @Override
    public void onClick(View view) {
        mLyMunicipios.setVisibility(View.GONE);
        int position = mRecyclerMunicipios.getChildAdapterPosition(view);
        Municipio municipio = mLstMunicipios.get(position);
        if (municipio != null){
            mMapPETCARFragment.obtenerContenedores(municipio.ID);
        }
    }

    @Override
    public void onSuccess(List<Municipio> lstMunicipios) {
        mLstMunicipios.clear();
        mLstMunicipios.addAll(lstMunicipios);
        mAdaptador.notifyDataSetChanged();
    }

    @Override
    public void onError(ErrorApi errorApi) {
        mostrarMensaje(errorApi.getMessage());
    }
    @OnClick(R.id.imgClose) void onCerrarMunicipios() {
        mLyMunicipios.setVisibility(View.GONE);
        mLyMunicipios.startAnimation(AnimationUtils.loadAnimation(AppCar.getContext(), R.anim.slide_dow));
    }
    @OnClick(R.id.imgClose2) void onCerrarGuia() {
        mLyGuia.setVisibility(View.GONE);
        mLyGuia.startAnimation(AnimationUtils.loadAnimation(AppCar.getContext(), R.anim.slide_dow));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ){
            if (requestCode == REQUEST_LOGIN){
                configMenuLateral();
                drawer.openDrawer(Gravity.LEFT);
                cambiarClave();
            }
        }
    }
}
