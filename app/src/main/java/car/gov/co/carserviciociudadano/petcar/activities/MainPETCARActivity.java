package car.gov.co.carserviciociudadano.petcar.activities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.common.APIClient;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.consultapublica.adapter.BankProjectAdapter;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.adapter.MunicipiosAdapter;
import car.gov.co.carserviciociudadano.petcar.fragments.MapPETCARFragment;
import car.gov.co.carserviciociudadano.petcar.model.Municipio;
import car.gov.co.carserviciociudadano.petcar.presenter.IViewMunicipios;
import car.gov.co.carserviciociudadano.petcar.presenter.MunicipiosPresenter;

public class MainPETCARActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, IViewMunicipios {


    DrawerLayout drawer;
    @BindView(R.id.recyclerMunicipios) RecyclerView mRecyclerMunicipios;
    @BindView(R.id.lyMunicipios) View mLyMunicipios;
    @BindView(R.id.lyGuia) View mLyGuia;
    @BindView(R.id.menu_bar) BottomNavigationViewEx menu_bar;

    MunicipiosAdapter mAdaptador;
    List<Municipio> mLstMunicipios;
    MunicipiosPresenter municipiosPresenter;
    MapPETCARFragment mMapPETCARFragment;
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mLstMunicipios = new ArrayList<>();
        mAdaptador = new MunicipiosAdapter( mLstMunicipios);
        mAdaptador.setOnClickListener(this);
        mRecyclerMunicipios.setAdapter(mAdaptador);
        mRecyclerMunicipios.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerMunicipios.setItemAnimator(new DefaultItemAnimator());
        municipiosPresenter = new MunicipiosPresenter(this);
        mLyMunicipios.setVisibility(View.GONE);
        mLyGuia.setVisibility(View.GONE);

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

        if (id == R.id.nav_enviar) {
            mostrarMensaje("Próximamente, en desarrollo");
        } else if (id == R.id.nav_usuario) {
            mostrarMensaje("Próximamente, en desarrollo");
        }else if (id == R.id.nav_co2) {
            mostrarMensaje("Próximamente, en desarrollo");
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}