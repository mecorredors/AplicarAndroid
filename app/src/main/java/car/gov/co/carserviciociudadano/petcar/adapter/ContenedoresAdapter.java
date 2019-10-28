package car.gov.co.carserviciociudadano.petcar.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;

/**
 * Created by Olger on 28/11/2016.
 */

public class ContenedoresAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<Contenedor> datos;


    public void setData(List<Contenedor> datos){
        if (this.datos == null)
            this.datos = new ArrayList<>();
        else
            this.datos.clear();

        this.datos.addAll(datos);
        notifyDataSetChanged();
    }


    public static class PlaceViewHolder
            extends RecyclerView.ViewHolder implements CompoundButton.OnClickListener  {

        private TextView lblDireccion;
        private TextView lblMunicipio;
        private TextView lblCodigo;


        public PlaceViewHolder(View itemView) {
            super(itemView);

            lblDireccion = itemView.findViewById(R.id.lblDireccion);
            lblMunicipio = itemView.findViewById(R.id.lblMunicipio);
            lblCodigo = itemView.findViewById(R.id.lblCodigo);
        }

        public void bind(Contenedor b) {
            lblDireccion.setText(b.Direccion);
            lblMunicipio.setText(b.Municipio);
            lblCodigo.setText(b.Codigo);
        }


        @Override
        public void onClick(View view) {

        }
    }


    public ContenedoresAdapter(List<Contenedor> datos) {
        this.datos = datos;

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_contenedor, viewGroup, false);
            itemView.setOnClickListener(this);
            return new PlaceViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
        Contenedor item = datos.get(pos);
        PlaceViewHolder holder = (PlaceViewHolder) viewHolder;
        holder.bind(item);

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

