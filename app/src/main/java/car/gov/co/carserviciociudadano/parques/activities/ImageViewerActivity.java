package car.gov.co.carserviciociudadano.parques.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.CirclePageIndicator;
import car.gov.co.carserviciociudadano.parques.adapter.ImageViewerPagerAdapter;
import car.gov.co.carserviciociudadano.parques.businessrules.BRArchivosParque;
import car.gov.co.carserviciociudadano.parques.interfaces.IArchivoParque;
import car.gov.co.carserviciociudadano.parques.interfaces.PageIndicator;
import car.gov.co.carserviciociudadano.parques.model.ArchivoParque;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;

public class ImageViewerActivity extends BaseActivity {

    ViewPager _PagerImages;
    ImageViewerPagerAdapter _AdapterImages;
    PageIndicator mIndicatorImages;

    List<ArchivoParque> mLstMedias= new ArrayList<>();
    private int mIdParque;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            mIdParque = bundle.getInt(Parque.ID_PARQUE);
        }

        _PagerImages = (ViewPager) this.findViewById(R.id.pagerImages);
        _AdapterImages = new ImageViewerPagerAdapter(getSupportFragmentManager());
        _PagerImages.setAdapter(_AdapterImages);


        mIndicatorImages = (CirclePageIndicator)findViewById(R.id.indicatorImages);
        mIndicatorImages.setViewPager(this._PagerImages);

        loadArchivosParque();

    }

    private void loadArchivosParque(){
        BRArchivosParque archivosParque = new BRArchivosParque();
        archivosParque.list(new IArchivoParque() {
            @Override
            public void onSuccess(List<ArchivoParque> lstArchivosParque) {

                mLstMedias.clear();
                for(ArchivoParque item : lstArchivosParque)
                    if (item.getIDTipoArchivo()==1) mLstMedias.add(item);

                _AdapterImages.addAllImageViewer(mLstMedias);
                _AdapterImages.notifyDataSetChanged();
                mIndicatorImages.notifyDataSetChanged();
//                if(medias.size() > _Position){
//                    _PagerImages.setCurrentItem(_Position);
//                    mIndicatorImages.ShowButtonLeftRight(_Position);
            }

            @Override
            public void onError(ErrorApi error) {

            }
        },mIdParque);
    }
}
