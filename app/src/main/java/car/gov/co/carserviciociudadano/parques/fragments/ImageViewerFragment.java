package car.gov.co.carserviciociudadano.parques.fragments;

/**
 * Created by Olger on 21/01/2017.
 */

import java.io.FileNotFoundException;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.parques.model.ArchivoParque;
//import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageViewerFragment extends BaseFragment  {

    ImageView Image;
    private boolean IsUrl;
    // private int MultimediaType;
    //String ImagePath="";
    ProgressBar ImageProgress;
    Button btnPlayVideo;
    ArchivoParque _Media= new ArchivoParque();
   // PhotoViewAttacher mAttacher;
    String ImagePath = "";
    public static Fragment newInstance(ArchivoParque media){
        ImageViewerFragment img= new ImageViewerFragment();
        Bundle bundle= new Bundle();
        bundle.putString(ArchivoParque.PATH, media.getArchivoParque());
        img.setArguments(bundle);
        return img;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_viewer, container, false);

        Bundle bundle= getArguments();
        if(bundle != null ){
            _Media.setArchivoParque(bundle.getString(ArchivoParque.PATH));
            ImagePath = _Media.getArchivoParque();

        }

        Image= (ImageView) view.findViewById(R.id.Image);
        ImageProgress=(ProgressBar) view.findViewById(R.id.image_progress);

        if (ImagePath != null && !ImagePath.isEmpty()) {
            LoadImage();
        } else  {
            LoadImageNoPhoto();
        }
        return view;
    }

    private void LoadImageNoPhoto() {
        ImageProgress.setVisibility(View.GONE);
        Image.setImageResource(R.drawable.sin_foto);
    }

    private void LoadImage() {

        if (!ImagePath.isEmpty()){
            ImageProgress.setVisibility(View.VISIBLE);

            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .cacheInMemory(false)
                    .showImageOnLoading(R.color.gray2)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .delayBeforeLoading(0)
                    .build();
            // targetSize = new ImageSize(180, 150); // result Bitmap will be fit to this size
            ImageLoader.getInstance().loadImage(ImagePath, defaultOptions, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String arg0, View arg1) {
                    ImageProgress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    ImageProgress.setVisibility(View.GONE);
                    Image.setImageBitmap(loadedImage);
                    //mAttacher = new PhotoViewAttacher(Image);
                }

                @Override
                public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                    ImageProgress.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String arg0, View arg1) {
                    ImageProgress.setVisibility(View.GONE);
                }
            });

        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

    }

    @Override
    public void onResume(){
        super.onResume();



    }

    @Override
    public void onPause(){
        super.onPause();
    }

}