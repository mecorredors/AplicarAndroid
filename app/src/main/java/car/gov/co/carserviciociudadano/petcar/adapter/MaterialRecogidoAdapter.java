package car.gov.co.carserviciociudadano.petcar.adapter;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;

import static car.gov.co.carserviciociudadano.R.drawable.ic_check_circle_green_24dp;
import static car.gov.co.carserviciociudadano.R.drawable.ic_warning_yellow_24dp;

/**
 * Created by Olger on 28/11/2016.
 */

public class MaterialRecogidoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<MaterialRecogido> datos;


    public void setData(List<MaterialRecogido> datos){
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
        private TextView lblTipoMaterial;
        private TextView lblComentarios;
        private TextView lblKilos;
        private TextView lblEstado;
        private ImageView icoEstado;



        public PlaceViewHolder(View itemView) {
            super(itemView);

            lblDireccion = itemView.findViewById(R.id.lblDireccion);
            lblMunicipio = itemView.findViewById(R.id.lblMunicipio);
            lblTipoMaterial = itemView.findViewById(R.id.lblTipoMaterial);
            lblComentarios = itemView.findViewById(R.id.lblComentarios);
            lblKilos = itemView.findViewById(R.id.lblKilos);
            lblEstado = itemView.findViewById(R.id.lblEstado);
            icoEstado = itemView.findViewById(R.id.icoEstado);

        }

        public void bind(MaterialRecogido b) {
            if (b.getContenedor() != null) {
                lblDireccion.setText(b.getContenedor().Municipio != null ? b.getContenedor().Direccion  : "Sin direcci??n");
                lblMunicipio.setText(b.getContenedor().Municipio);
            }else{
                lblDireccion.setText("Sin contenedor");
            }
            if (b.getTipoMaterial() != null) {
                if (b.getTipoMaterial().Descripcion != null) {
                    lblTipoMaterial.setText(b.getTipoMaterial().Nombre + " (" + b.getTipoMaterial().Descripcion + ")");
                }else {
                    lblTipoMaterial.setText(b.getTipoMaterial().Nombre );
                }
            }else{
                lblTipoMaterial.setText("No hay tipo  material");
            }
            if (b.Comentarios != null && !b.Comentarios.equals("")) {
                lblComentarios.setVisibility(View.VISIBLE);
                lblComentarios.setText(b.Comentarios);
            }else{
                lblComentarios.setVisibility(View.GONE);
            }

            lblKilos.setText(String.valueOf(b.Kilos) + " Kilos");
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


    public MaterialRecogidoAdapter(List<MaterialRecogido> datos) {
        this.datos = datos;

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_material_recogido, viewGroup, false);
            itemView.setOnClickListener(this);
            return new PlaceViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
        MaterialRecogido item = datos.get(pos);
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

