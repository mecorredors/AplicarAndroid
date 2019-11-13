package car.gov.co.carserviciociudadano.parques.adapter;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.parques.model.ServicioParque;

/**
 * Created by Olger on 28/11/2016.
 */

public class ServiciosParqueAdapter extends RecyclerView.Adapter<ServiciosParqueAdapter.PlaceSelectorViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<ServicioParque> datos;

    public static class PlaceSelectorViewHolder
            extends RecyclerView.ViewHolder {

        private TextView lblTitulo;
        private TextView lblDescripcion;
        private TextView lblPrecio;


        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);

            lblTitulo = (TextView)itemView.findViewById(R.id.lblTitulo);
            lblDescripcion = (TextView)itemView.findViewById(R.id.lblDescripcion);
            lblPrecio = (TextView)itemView.findViewById(R.id.lblPrecio);
        }

        public void bindParque(ServicioParque p) {
            lblTitulo.setText(p.getNombreServicio());
            lblDescripcion.setText(p.getDescripcionServicio());
            lblPrecio.setText(p.getPrecioConFormato());
        }
    }

    public ServiciosParqueAdapter(List<ServicioParque> datos) {
        this.datos = datos;
    }

    @Override
    public PlaceSelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_servicios_parque, viewGroup, false);

        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        PlaceSelectorViewHolder tvh = new PlaceSelectorViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(PlaceSelectorViewHolder viewHolder, int pos) {
        ServicioParque item = datos.get(pos);

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
