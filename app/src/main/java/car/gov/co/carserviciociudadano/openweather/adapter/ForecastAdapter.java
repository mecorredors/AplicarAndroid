package car.gov.co.carserviciociudadano.openweather.adapter;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import car.gov.co.carserviciociudadano.Utils.Config;
import car.gov.co.carserviciociudadano.openweather.model.ItemForecast;

/**
 * Created by Olger on 28/11/2016.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.PlaceSelectorViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<ItemForecast> datos;



    public static class PlaceSelectorViewHolder
            extends RecyclerView.ViewHolder {


        private TextView lblFecha;
        private TextView lblWeatherCondition;
        private TextView lblWeather;
        private TextView lblHumedad;
        private TextView lblViento;
        private ImageView icoWeather;
        private ImageView icoNextPage;


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.color.gray)
                .showImageForEmptyUri(R.color.gray)
                .showImageOnFail(R.color.gray)
                .build();

        AnimateFirstDisplayListener aniList = new AnimateFirstDisplayListener();

        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);

            lblFecha = (TextView)itemView.findViewById(R.id.lblFecha);
            lblWeatherCondition = (TextView)itemView.findViewById(R.id.lblWeatherCondition);
            lblWeather = (TextView) itemView.findViewById(R.id.lblWeather);
            lblHumedad = (TextView) itemView.findViewById(R.id.lblHumedad);
            lblViento = (TextView) itemView.findViewById(R.id.lblViento);
            icoWeather = (ImageView) itemView.findViewById(R.id.icoWeather);
            icoNextPage = (ImageView) itemView.findViewById(R.id.icoNextPage);

        }

        public void bindParque(ItemForecast p) {

            lblFecha.setText(p.fecha);
            lblWeather.setText(p.temp);
            lblWeatherCondition.setText(p.condition);
            lblHumedad.setText(p.humidity);
            lblViento.setText(p.windSpeed);
            String urlIcon = Config.OpenWeatherIcon + p.icon + ".png";
            ImageLoader.getInstance().displayImage(urlIcon, icoWeather, options);
            icoNextPage.setVisibility(p.iconNextPage ? View.VISIBLE : View.GONE);
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

    public ForecastAdapter(List<ItemForecast> datos) {
        this.datos = datos;
    }

    @Override
    public PlaceSelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_forecast, viewGroup, false);

        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        PlaceSelectorViewHolder tvh = new PlaceSelectorViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(PlaceSelectorViewHolder viewHolder, int pos) {
        ItemForecast item = datos.get(pos);

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
