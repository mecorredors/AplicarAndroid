package car.gov.co.carserviciociudadano.parques.activities;

import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.dataaccess.Usuarios;
import car.gov.co.carserviciociudadano.parques.fragments.MisReservasFragment;
import car.gov.co.carserviciociudadano.parques.fragments.ParquesFragment;
import car.gov.co.carserviciociudadano.parques.model.Usuario;

public class MainParques extends BaseActivity {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private FirebaseAnalytics mFirebaseAnalytics;
    private ViewPager mViewPager;
    private Usuario mUsuario;
    MenuItem mMenuEditar;
    MenuItem mMenuCambiarContrasena;
    MenuItem mMenuCerrarSesion;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_parques);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        mUsuario = new Usuarios().leer();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        TextView tab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab.setText("PARQUES");
        tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_parque_24, 0, 0);

        tabLayout.getTabAt(0).setCustomView(tab);

        TextView tabReservas = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabReservas.setText("MIS RESERVAS");
        tabReservas.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_hotel_black_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabReservas);

        Intent intent = getIntent();
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            String  sHost = intent.getData().getHost();
            if (sHost.equals("mis_reservas")){
                TabLayout.Tab tabMisReservas = tabLayout.getTabAt(1);
                tabMisReservas.select();
            }
        }

        if (BuildConfig.DEBUG == false) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Main Parques");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Main Parques");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Enumerator.ContentTypeAnalitic.PARQUES);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mMenuEditar = menu.findItem(R.id.item_editar_datos);
        mMenuCambiarContrasena = menu.findItem(R.id.item_cambiar_contrasena);
        mMenuCerrarSesion = menu.findItem(R.id.item_cerrar_sesion);

        boolean visibleStatus =  (mUsuario.getIdUsuario() > 0) ;
        mMenuEditar.setVisible(visibleStatus);
        mMenuCambiarContrasena.setVisible(visibleStatus);
        mMenuCerrarSesion.setVisible(visibleStatus);

        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_parques, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
//
//        if(id== android.R.id.home){
//            super.onBackPressed();
//        }

        if (id == R.id.item_mi_cuenta || id == R.id.item_editar_datos) {

                  Intent i = new Intent(this, UsuarioActivity.class);
                  i.putExtra(UsuarioActivity.ORIGIN, UsuarioActivity.ORIGEN_MANIN_PARQUES);
                  startActivityForResult(i, 0);

            return true;
        }
        if (id == R.id.item_cambiar_contrasena) {
            if (mUsuario.getIdUsuario()>0) {
                Intent i = new Intent(this, CambiarContrasenaActivity.class);
                startActivity(i);
                return true;
            }else{
                mostrarMensaje("Debe iniciar con su cuenta de usuario");
            }
        }

        if (id == R.id.item_cerrar_sesion) {
            mUsuario = new Usuario();
            new Usuarios().guardar(mUsuario);
            mostrarMensaje(getString(R.string.sesion_finalizada));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_parques, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> mFragments;
        String [] mTitulo = {"Parques","Mis reservas"};
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<Fragment>();
            mFragments.add(ParquesFragment.newInstance());
            mFragments.add(MisReservasFragment.newInstance());
        }

        @Override
        public Fragment getItem(int position) {
           return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
           return  mTitulo[position];
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mSectionsPagerAdapter.getItem(1).onActivityResult(requestCode,resultCode,data);

        mUsuario = new Usuarios().leer();
        invalidateOptionsMenu();
      /*  if (mUsuario.getIdUsuario() > 0 ){
            mMenuEditar.setVisible(true);
            mMenuCambiarContrasena.setVisible(true);
            mMenuCerrarSesion.setVisible(true);
        }*/
    }
}
