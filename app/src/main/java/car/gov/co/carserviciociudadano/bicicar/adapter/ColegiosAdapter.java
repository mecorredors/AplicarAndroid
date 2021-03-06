package car.gov.co.carserviciociudadano.bicicar.adapter;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.bicicar.model.Colegio;

import static car.gov.co.carserviciociudadano.R.drawable.ic_check_circle_green_24dp;
import static car.gov.co.carserviciociudadano.R.drawable.ic_highlight_off_24dp;
import static car.gov.co.carserviciociudadano.R.drawable.ic_warning_yellow_24dp;

/**
 * Created by Olger on 28/11/2016.
 */

public class ColegiosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<Colegio> datos;
    static ColegiosListener colegiosListener;
    private boolean isTipoSelector = false;

    public void setData(List<Colegio> datos){
        if (this.datos == null)
            this.datos = new ArrayList<>();
        else
            this.datos.clear();

        this.datos.addAll(datos);
        notifyDataSetChanged();
    }
    public void setColegiosListener(ColegiosListener colegiosListener) {
        this.colegiosListener = colegiosListener;
    }

    public static class PlaceViewHolder
            extends RecyclerView.ViewHolder implements CompoundButton.OnClickListener  {

        private TextView lblNombre;
        private TextView lblMunicipio;
        private Button btnEstudiantes;
        private Button btnUbicacion;
        private View lyEstado;
        private ImageView imgEstado;
        private TextView lblEstado;

        public PlaceViewHolder(View itemView) {
            super(itemView);

            lblNombre = itemView.findViewById(R.id.lblNombre);
            lblMunicipio = itemView.findViewById(R.id.lblMunicipio);
            btnEstudiantes = itemView.findViewById(R.id.btnEstudiantes);
            btnUbicacion = itemView.findViewById(R.id.btnUbicacion);
            lyEstado = itemView.findViewById(R.id.lyEstado);
            imgEstado = itemView.findViewById(R.id.imgEstado);
            lblEstado = itemView.findViewById(R.id.lblEstado);

            btnEstudiantes.setOnClickListener(this);
            btnUbicacion.setOnClickListener(this);
        }

        public void bind(Colegio b) {
            lblNombre.setText(b.Nombre);
            lblMunicipio.setText(b.Municipio);

            if (b.Estado == Enumerator.Estado.PENDIENTE_PUBLICAR){
                imgEstado.setImageDrawable(AppCar.getContext().getResources().getDrawable(ic_warning_yellow_24dp));
                lblEstado.setText(AppCar.getContext().getResources().getString(R.string.pendiente_publicar));
            }else if (b.Latitude == 0 || b.Longitude == 0){
                imgEstado.setImageDrawable(AppCar.getContext().getResources().getDrawable(ic_highlight_off_24dp));
                lblEstado.setText(AppCar.getContext().getResources().getString(R.string.sin_ubicacion));
            }else{
                imgEstado.setImageDrawable(AppCar.getContext().getResources().getDrawable(ic_check_circle_green_24dp));
                lblEstado.setText(AppCar.getContext().getResources().getString(R.string.con_ubicacion));
            }

        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnEstudiantes:
                    colegiosListener.onEstudiantes(getAdapterPosition(), view);
                    break;
                case R.id.btnUbicacion:
                    colegiosListener.onUbicacion(getAdapterPosition(),view);
                    break;
            }
        }
    }


    public static class PlaceSelectorViewHolder
            extends RecyclerView.ViewHolder implements CompoundButton.OnClickListener  {

        private TextView lblNombre;
        private TextView lblMunicipio;
        private View lyItem;

        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);

            lblNombre = itemView.findViewById(R.id.lblNombre);
            lblMunicipio = itemView.findViewById(R.id.lblMunicipio);
            lyItem = itemView.findViewById(R.id.lyItem);
            lyItem.setOnClickListener(this);
        }

        public void bind(Colegio b) {
            lblNombre.setText(b.Nombre);
            lblMunicipio.setText(b.Municipio);
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.lyItem:
                    colegiosListener.onSelected(getAdapterPosition(), view);
                    break;
            }
        }
    }

    public ColegiosAdapter(List<Colegio> datos, boolean isTipoSelector) {
        this.datos = datos;
        this.isTipoSelector = isTipoSelector;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (isTipoSelector){
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_colegio_selector, viewGroup, false);
            itemView.setOnClickListener(this);
            return new PlaceSelectorViewHolder(itemView);

        }else {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_colegio, viewGroup, false);
            itemView.setOnClickListener(this);
            return new PlaceViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
        Colegio item = datos.get(pos);
        if (isTipoSelector){
            PlaceSelectorViewHolder holder = (PlaceSelectorViewHolder) viewHolder;
            holder.bind(item);
        }else {
            PlaceViewHolder holder = (PlaceViewHolder) viewHolder;
            holder.bind(item);
        }      
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

    public interface ColegiosListener{
        void onEstudiantes(int position, View view);
        void onUbicacion(int position, View view);
        void onSelected(int position, View view);
    }
}

