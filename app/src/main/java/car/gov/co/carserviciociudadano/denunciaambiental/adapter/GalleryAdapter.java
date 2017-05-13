package car.gov.co.carserviciociudadano.denunciaambiental.adapter;

/**
 * Created by Olger on 13/05/2017.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.denunciaambiental.model.CustomGallery;


public class GalleryAdapter extends BaseAdapter {

    private LayoutInflater infalter;
    private ArrayList<CustomGallery> data = new ArrayList<CustomGallery>();

    private boolean isActionMultiplePick;
    public boolean shouldRequestThumb = true;
    AnimateFirstDisplayListener animateDisplay = new AnimateFirstDisplayListener();

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(false)
            .cacheOnDisk(false)
            .showImageOnLoading(R.color.gray2)
            .showImageForEmptyUri(R.drawable.sin_foto)
            .showImageOnFail(R.drawable.sin_foto)
            .delayBeforeLoading(0)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    private final int ImageSize=200;
    private final ImageFetcher fetcher = new ImageFetcher();
    private final Bitmap mPlaceHolderBitmap;

    public GalleryAdapter(Context c) {
        infalter = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Bitmap tmpHolderBitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.sin_foto);
        mPlaceHolderBitmap = Bitmap.createScaledBitmap(tmpHolderBitmap, ImageSize, ImageSize, false);
        if (tmpHolderBitmap != mPlaceHolderBitmap) {
            tmpHolderBitmap.recycle();
            tmpHolderBitmap = null;
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CustomGallery getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setMultiplePick(boolean isMultiplePick) {
        this.isActionMultiplePick = isMultiplePick;
    }

    public void selectAll(boolean selection) {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).isSeleted = selection;

        }
        notifyDataSetChanged();
    }

    public boolean isAllSelected() {
        boolean isAllSelected = true;

        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).isSeleted) {
                isAllSelected = false;
                break;
            }
        }

        return isAllSelected;
    }

    public boolean isAnySelected() {
        boolean isAnySelected = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted) {
                isAnySelected = true;
                break;
            }
        }

        return isAnySelected;
    }

    public ArrayList<CustomGallery> getSelected() {
        ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted) {
                dataT.add(data.get(i));
            }
        }

        return dataT;
    }

    public int getCountSelected(){
        int total=0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted) {
                total++;
            }
        }
        return total;
    }

    public void addAll(ArrayList<CustomGallery> files) {

        try {
            this.data.clear();
            this.data.addAll(files);

        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();
    }

    public boolean changeSelection(View v, int position,int maxItemCount) {
        boolean res=true;

        if(!data.get(position).isButton){
            if (data.get(position).isSeleted) {
                data.get(position).isSeleted = false;
            } else {
                if(getCountSelected() <= maxItemCount)
                    data.get(position).isSeleted = true;
                else
                    res=false;

            }

            if(data.get(position).isSeleted)
                ((ViewHolder) v.getTag()).frmOpacity.setVisibility(View.VISIBLE);
            else
                ((ViewHolder) v.getTag()).frmOpacity.setVisibility(View.GONE);

            ((ViewHolder) v.getTag()).imgQueueMultiSelected.setSelected(data.get(position).isSeleted);
        }

        return res;
    }

    public void setCustomGallery(CustomGallery gallery,int position){
        data.add(position ,gallery);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final ViewHolder holder;
        final BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize =3;

        if (convertView == null  ) {

            convertView = infalter.inflate(R.layout.item_gallery, null);
            holder = new ViewHolder();
            holder.imgQueue = (ImageView) convertView.findViewById(R.id.imgQueue);

            holder.imgQueueMultiSelected = (ImageView) convertView.findViewById(R.id.imgQueueMultiSelected);
            holder.frmOpacity = (FrameLayout) convertView.findViewById(R.id.frmOpacity);
            holder.frmBtnPhoto = (FrameLayout) convertView.findViewById(R.id.frmBtnPhoto);
            holder.frmQueue = (FrameLayout) convertView.findViewById(R.id.frmQueue);
            holder.btnPhoto = (ImageView) convertView.findViewById(R.id.btnPhoto );


            if (isActionMultiplePick) {
                holder.imgQueueMultiSelected.setVisibility(View.VISIBLE);
                holder.frmOpacity.setVisibility(View.VISIBLE);

            } else {
                holder.imgQueueMultiSelected.setVisibility(View.GONE);
                holder.frmOpacity.setVisibility(View.GONE);
            }

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imgQueue.setTag(position);

        try {
            if(!data.get(position).isButton){

//					   ImageLoader.getInstance().displayImage("file://" +pathThumbnail, holder.imgQueue,options,animateDisplay);

                holder.imgQueue.setImageBitmap(null);

                if (shouldRequestThumb)
                    fetcher.fetch(data.get(position).idBitmap, holder.imgQueue, ImageSize);


            }


            if (isActionMultiplePick ) {

                if(data.get(position).isSeleted)
                    holder.frmOpacity.setVisibility(View.VISIBLE);
                else
                    holder.frmOpacity.setVisibility(View.GONE);

                holder.imgQueueMultiSelected.setSelected(data.get(position).isSeleted);


                if(data.get(position).isButton) {
                    holder.frmBtnPhoto.setVisibility(View.VISIBLE);
                    holder.btnPhoto.setVisibility(View.VISIBLE);
                    holder.frmQueue.setVisibility(View.GONE);

                }else{
                    holder.frmBtnPhoto.setVisibility(View.GONE);
                    holder.btnPhoto.setVisibility(View.GONE);
                    holder.frmQueue.setVisibility(View.VISIBLE);
                }

            }

        } catch (Exception e) {
            Log.e("GalleryAdapter.getView",e.toString());
        }


        return convertView;
    }



    public class ViewHolder {
        ImageView imgQueue;
        ImageView imgQueueMultiSelected;
        FrameLayout frmOpacity;
        FrameLayout frmBtnPhoto;
        FrameLayout frmQueue;
        ImageView btnPhoto;
    }

    public void clearCache() {
//		imageLoader.clearDiskCache();
//		imageLoader.clearMemoryCache();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 750);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
