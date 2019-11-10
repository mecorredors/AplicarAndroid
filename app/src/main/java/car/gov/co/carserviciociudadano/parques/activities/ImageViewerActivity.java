package car.gov.co.carserviciociudadano.parques.activities;


import android.os.Bundle;
import androidx.appcompat.app.ActionBar;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.CirclePageIndicator;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.common.ViewPagerExtend;
import car.gov.co.carserviciociudadano.parques.adapter.ImageViewerPagerAdapter;
import car.gov.co.carserviciociudadano.parques.presenter.ArchivosParquePresenter;
import car.gov.co.carserviciociudadano.parques.presenter.IViewArchivoParque;
import car.gov.co.carserviciociudadano.parques.interfaces.PageIndicator;
import car.gov.co.carserviciociudadano.parques.model.ArchivoParque;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;

public class ImageViewerActivity extends BaseActivity implements  IViewArchivoParque {

    private FirebaseAnalytics mFirebaseAnalytics;
    ViewPagerExtend _PagerImages;
    ImageViewerPagerAdapter _AdapterImages;
    PageIndicator mIndicatorImages;

    List<ArchivoParque> mLstMedias= new ArrayList<>();
    private int mIdParque;
    private int mTypoArchivoParque;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            mIdParque = bundle.getInt(Parque.ID_PARQUE);
            mTypoArchivoParque = bundle.getInt(Enumerator.TipoArchivoParque.TAG);
        }

        _PagerImages = (ViewPagerExtend) this.findViewById(R.id.pagerImages);
        _AdapterImages = new ImageViewerPagerAdapter(getSupportFragmentManager());
        _PagerImages.setAdapter(_AdapterImages);


        mIndicatorImages = (CirclePageIndicator)findViewById(R.id.indicatorImages);
        mIndicatorImages.setViewPager(this._PagerImages);

        loadArchivosParque();

        if (BuildConfig.DEBUG == false) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundleAnalitic = new Bundle();
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_ID, "Image Viewer");
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_NAME, "Image Viewer");
            bundleAnalitic.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Enumerator.ContentTypeAnalitic.PARQUES);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleAnalitic);
        }
    }

    private void loadArchivosParque() {
        ArchivosParquePresenter archivosParque = new ArchivosParquePresenter(this);
        archivosParque.list(mIdParque, mTypoArchivoParque);
    }

    public void onSuccess(List<ArchivoParque> lstArchivosParque, ArchivoParque archivoParque, ArchivoParque logoParque,int count) {
        mLstMedias.clear();
        mLstMedias.addAll(lstArchivosParque);
        _AdapterImages.addAllImageViewer(mLstMedias);
        _AdapterImages.notifyDataSetChanged();
        mIndicatorImages.notifyDataSetChanged();
    }

    @Override
    public void onError(ErrorApi error) {

    }

}
