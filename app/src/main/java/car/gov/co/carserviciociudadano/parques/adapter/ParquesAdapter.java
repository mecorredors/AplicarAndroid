package car.gov.co.carserviciociudadano.parques.adapter;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.parques.model.Parque;

/**
 * Created by Olger on 28/11/2016.
 */

public class ParquesAdapter extends RecyclerView.Adapter<ParquesAdapter.PlaceSelectorViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<Parque> datos;

    public static class PlaceSelectorViewHolder
            extends RecyclerView.ViewHolder {


        private TextView lblTitulo;
        private ImageView imagen;
        private FrameLayout lyNombre;
        private FrameLayout lyFooter;
        private TextView lblFooter;

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.color.gray)
                .showImageForEmptyUri(R.drawable.sin_foto)
                .showImageOnFail(R.drawable.sin_foto)
                .build();

        AnimateFirstDisplayListener aniList = new AnimateFirstDisplayListener();

        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);

            lblTitulo = (TextView)itemView.findViewById(R.id.lblTitulo);
            lyNombre = (FrameLayout) itemView.findViewById(R.id.lyNombre);
            lyFooter = (FrameLayout) itemView.findViewById(R.id.lyFooter);
            lblFooter = (TextView)itemView.findViewById(R.id.lblFooter);
            imagen = (ImageView) itemView.findViewById(R.id.imagen);

        }

        public void bindParque(Parque p) {
            lblTitulo.setText(p.getNombreParque());
            lyNombre.setBackgroundColor(p.getColor());
            lyFooter.setBackgroundColor(p.getColorFooter());
            ImageLoader.getInstance().displayImage(p.getUrlArchivoParque(), imagen, options, aniList);

            if (p.getIDParque() == 81 || p.getIDParque() == 83)  //embalce del neusa y hato
                lblFooter.setText("RESERVAR");
            else
                lblFooter.setText("CONOCE");
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

    public ParquesAdapter(List<Parque> datos) {
        this.datos = datos;
    }

    @Override
    public PlaceSelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_parques, viewGroup, false);

        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        PlaceSelectorViewHolder tvh = new PlaceSelectorViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(PlaceSelectorViewHolder viewHolder, int pos) {
        Parque item = datos.get(pos);

        viewHolder.bindParque(item);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }
}
