package car.gov.co.carserviciociudadano.consultapublica.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.consultapublica.fragments.DocumentosExpedienteFragment;
import car.gov.co.carserviciociudadano.consultapublica.fragments.ExpedienteFragment;
import car.gov.co.carserviciociudadano.consultapublica.model.Expediente;
import car.gov.co.carserviciociudadano.common.BaseActivity;


public class ExpedienteActivity extends BaseActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int mIdExpediente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expediente);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        if(b != null ) {
            mIdExpediente = b.getInt(Expediente.ID_EXPEDIENTE);

            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
        }else{
            mostrarMensaje(" IDExpediente es 0 ");
        }

        if (BuildConfig.DEBUG == false) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundleAnalitic = new Bundle();
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_ID, "Expediente");
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_NAME, "Expediente");
            bundleAnalitic.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Enumerator.ContentTypeAnalitic.CONSULTA_EXPEDIENTE);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleAnalitic);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> mFragments;
        String [] mTitulo = {"Resumen","Documentos"};
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<Fragment>();
            mFragments.add(ExpedienteFragment.newInstance(mIdExpediente));
            mFragments.add(DocumentosExpedienteFragment.newInstance(mIdExpediente));
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

}
