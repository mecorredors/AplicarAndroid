package car.gov.co.carserviciociudadano.petcar.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.petcar.model.Visita;

import static car.gov.co.carserviciociudadano.R.drawable.ic_check_circle_green_24dp;
import static car.gov.co.carserviciociudadano.R.drawable.ic_warning_yellow_24dp;

/**
 * Created by Olger on 28/11/2016.
 */

public class VisitasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<Visita> datos;

    public void setData(List<Visita> datos){
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
        private TextView lblComentarios;
        private TextView lblEstado;
        private ImageView icoEstado;



        public PlaceViewHolder(View itemView) {
            super(itemView);

            lblDireccion = itemView.findViewById(R.id.lblDireccion);
            lblMunicipio = itemView.findViewById(R.id.lblMunicipio);
            lblComentarios = itemView.findViewById(R.id.lblComentarios);
            lblEstado = itemView.findViewById(R.id.lblEstado);
            icoEstado = itemView.findViewById(R.id.icoEstado);

        }

        public void bind(Visita b) {
            if (b.getContenedor() != null) {
                lblDireccion.setText(b.getContenedor().Municipio != null ? b.getContenedor().Direccion  : "Sin dirección");
                lblMunicipio.setText(b.getContenedor().Municipio);
            }else{
                lblDireccion.setText("Sin contenedor");
            }

            if (b.Comentarios != null && !b.Comentarios.equals("")) {
                lblComentarios.setVisibility(View.VISIBLE);
                lblComentarios.setText(b.Comentarios);
            }else{
                lblComentarios.setVisibility(View.GONE);
            }

            if (b.Estado == Enumerator.Estado.PENDIENTE_PUBLICAR ) {
                lblEstado.setText("Pendiente publicar");
                icoEstado.setImageDrawable(AppCar.getContext().getResources().getDrawable(ic_warning_yellow_24dp));
            }else if ( b.Estado == Enumerator.Estado.PENDIENTE_PUBLICAR_FOTOS) {
                lblEstado.setText("Pendiente publicar fotos");
                icoEstado.setImageDrawable(AppCar.getContext().getResources().getDrawable(ic_warning_yellow_24dp));
            }else{
                lblEstado.setText("Publicado");
                icoEstado.setImageDrawable(AppCar.getContext().getResources().getDrawable(ic_check_circle_green_24dp));
            }


        }

        @Override
        public void onClick(View view) {

        }
    }


    public VisitasAdapter(List<Visita> datos) {
        this.datos = datos;

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_visita, viewGroup, false);
            itemView.setOnClickListener(this);
            return new PlaceViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
        Visita item = datos.get(pos);
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

