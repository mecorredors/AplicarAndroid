package car.gov.co.carserviciociudadano.parques.adapter;

/**
 * Created by Olger on 21/01/2017.
 */

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.parques.fragments.ImageViewerFragment;
import car.gov.co.carserviciociudadano.parques.model.ArchivoParque;


public class ImageViewerPagerAdapter extends FragmentStatePagerAdapter {

    List<ArchivoParque> _lstMedias;

    FragmentManager _fm;
    Fragment mPrimaryItem;

    public ImageViewerPagerAdapter(FragmentManager fm) {

        super(fm);
        this._lstMedias = new ArrayList<>();
        this._fm = fm;

    }

    public void addAll(List<ArchivoParque> lstmMedias) {
        this._lstMedias.addAll(lstmMedias);


    }

    public void addAllImageViewer(List<ArchivoParque> lstmMedias) {
        this._lstMedias.addAll(lstmMedias);
        int size = lstmMedias.size();

    }

    public List<ArchivoParque> getMedias() {
        return this._lstMedias;
    }

    @Override
    public Fragment getItem(int position) {

        return ImageViewerFragment.newInstance(_lstMedias.get(position));

    }

    @Override
    public int getCount() {

        return this._lstMedias.size();

    }

    public void RemoveAll() {
        if (_lstMedias != null)
            this._lstMedias.clear();

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}