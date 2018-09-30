package car.gov.co.carserviciociudadano.petcar.adapter;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
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
import car.gov.co.carserviciociudadano.petcar.model.Municipio;


/**
 * Created by Olger on 28/11/2016.
 */

public class MunicipiosAdapter extends RecyclerView.Adapter<MunicipiosAdapter.PlaceSelectorViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<Municipio> datos;

    public static class PlaceSelectorViewHolder
            extends RecyclerView.ViewHolder {

        private TextView lblNombre;
        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);

            lblNombre = (TextView)itemView.findViewById(R.id.lblNombre);
        }

        public void bindMunicipio(Municipio item) {
            lblNombre.setText(item.Nombre + " (" + item.Count + ")");

        }

    }

    public MunicipiosAdapter(List<Municipio> datos) {
        this.datos = datos;
    }

    @Override
    public PlaceSelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_municipio, viewGroup, false);

        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        PlaceSelectorViewHolder tvh = new PlaceSelectorViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(PlaceSelectorViewHolder viewHolder, int pos) {
        Municipio item = datos.get(pos);

        viewHolder.bindMunicipio(item);
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
