package car.gov.co.carserviciociudadano.parques.adapter;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
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
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.model.DetalleReserva;
import car.gov.co.carserviciociudadano.parques.model.Parque;

/**
 * Created by Olger on 28/11/2016.
 */

public class MisReservasAdapter extends RecyclerView.Adapter<MisReservasAdapter.PlaceSelectorViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<DetalleReserva> datos;

    public static class PlaceSelectorViewHolder
            extends RecyclerView.ViewHolder {


        private TextView lblTitulo;
        private TextView lblFecha;
        private TextView lblEstado;
        private TextView lblTotal;


        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);

            lblTitulo = (TextView)itemView.findViewById(R.id.lblTitulo);
            lblFecha = (TextView)itemView.findViewById(R.id.lblFecha);
            lblEstado = (TextView) itemView.findViewById(R.id.lblEstado);
            lblTotal = (TextView) itemView.findViewById(R.id.lblTotal);

        }

        public void bindParque(DetalleReserva r) {
            lblTitulo.setText(r.getNombreParque());
            lblFecha.setText(Utils.toStringLargeFromDate(r.getFechaSistemaReserva()));
            lblEstado.setText(r.getEstadoNombre());
            lblTotal.setText(Utils.formatoMoney(r.getTotalValorReserva()));
        }
    }

    public MisReservasAdapter(List<DetalleReserva> datos) {
        this.datos = datos;
    }

    @Override
    public PlaceSelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_mis_reservas, viewGroup, false);

        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        PlaceSelectorViewHolder tvh = new PlaceSelectorViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(PlaceSelectorViewHolder viewHolder, int pos) {
        DetalleReserva item = datos.get(pos);

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
