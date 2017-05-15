package car.gov.co.carserviciociudadano.denunciaambiental.adapter;

/**
 * Created by Olger on 13/05/2017.
 */

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Context;
import android.graphics.Bitmap;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Foto;

public class GallerySelectedAdapter extends  ArrayAdapter<Foto> {

    private static class ViewHolder {
        public ImageView imgPhoto;
        public ImageButton imgButClose;
    }


    DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
            .cacheOnDisk(false)
            .cacheInMemory(true)
            .showImageOnLoading(R.color.gray2)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .delayBeforeLoading(0)
            .build();


    private List<Foto> Photos;

    OnItemClickListener mItemClickListener;

    public void AddPhotos(String[] all_path){
        AddPhotos(toListPhotos(all_path));

    }

    public void AddPhotos(ArrayList<Foto> lstMedias){

        if(getItemCount() == 0 )
            setButtonAddMore();

        Photos.addAll(lstMedias);
        notifyDataSetChanged();
    }

    public void setButtonAddMore(){

        Foto itemButton = new Foto();
        itemButton.setType(Enumerator.TipoFoto.BOTON_AGREGAR_MAS);
        Photos.add(0,itemButton);
    }

    public ArrayList<Foto> toListPhotos(String[] all_path){

        ArrayList<Foto> dataList = new ArrayList<>();
        for (String string : all_path) {
            if(!Exists(string))	{
                Foto item = new Foto();
                item.setPath(string);
                item.setType(Enumerator.TipoFoto.FOTO);
                item.setEstado(0);
                dataList.add(item);
            }
        }

        return dataList;
    }

    private boolean Exists(String Path){

        int size=getItemCount();
        for (int i = 0; i < size; i++) {
            if(Photos.get(i).getType() != Enumerator.TipoFoto.BOTON_AGREGAR_MAS && Photos.get(i).getPath().equals(Path))
                return true;
        }
        return false;
    }


    public GallerySelectedAdapter(Context context, List<Foto> photos) {
        super(context, R.layout.item_gallery_selected, photos);
        Photos=photos;
        // initImageLoader();
        setButtonAddMore();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_gallery_selected, parent, false);

            viewHolder.imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
            viewHolder.imgButClose=(ImageButton) convertView.findViewById(R.id.imgButClose);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(Photos.get(position).getType()== Enumerator.TipoFoto.BOTON_AGREGAR_MAS){
            viewHolder.imgPhoto.setImageResource(R.drawable.ic_photo_camera_gray_80dp);
            viewHolder.imgButClose.setVisibility(View.GONE);
        }else{


            ImageSize targetSize = new ImageSize(180, 150); // result Bitmap will be fit to this size
            ImageLoader.getInstance().loadImage("file://" + Photos.get(position).getPath(), targetSize, defaultOptions, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String arg0, View arg1) {

                }
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    viewHolder.imgPhoto.setImageBitmap(loadedImage);
                }
            });

            //    ImageLoader.getInstance().displayImage("file://" + Photos.get(position).getPath(),viewHolder.imgPhoto);

            //   ImageLoader.getInstance().displayImage("file://" +Photos.get(position).getPath(), viewHolder.imgPhoto,options);

            viewHolder.imgButClose.setVisibility(View.VISIBLE);
            viewHolder.imgButClose.setTag(position);

            viewHolder.imgButClose.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mItemClickListener != null) {
                        mItemClickListener.onCloseClick(v, position);
                    }
                }
            });
        }

        viewHolder.imgPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, position);
                }
            }
        });

        return convertView;
    }

    public void RemoveItem(int position){
        if (getItemCount() > position) {
            Photos.remove(position);
            notifyDataSetChanged();
        }
    }

    public void RemoveAll(){
        Photos.clear();
        setButtonAddMore();
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
        public void onCloseClick(View view , int position);

    }
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public int getItemCount() {
        return Photos.size();
    }

    public List<Foto> getPhotos() {
      return Photos;
    }
}
