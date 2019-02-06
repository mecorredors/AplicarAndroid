package car.gov.co.carserviciociudadano.bicicar.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.bicicar.model.Ruta;

/**
 * Created by Olger on 28/11/2016.
 */

public class RutasAdapter extends RecyclerView.Adapter<RutasAdapter.PlaceSelectorViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<Ruta> datos;
    static   RutaListener rutaListener;
    public void setRutaListener(RutaListener rutaListener){
        this.rutaListener = rutaListener;
    }
    public static class PlaceSelectorViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {



        private TextView lblNombre;

        private Button btnVerRuta;
        private Button btnEliminar;
        private TextView lblDistancia;
        private TextView lblDuracion;

        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);

            lblNombre = itemView.findViewById(R.id.lblNombre);
            btnVerRuta = itemView.findViewById(R.id.btnVerRuta);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            lblDistancia = itemView.findViewById(R.id.lblDistancia);
            lblDuracion = itemView.findViewById(R.id.lblDuracion);
            btnVerRuta.setOnClickListener(this);
            btnEliminar.setOnClickListener(this);
        }

        public void bind(Ruta a) {

            btnVerRuta.setVisibility(View.GONE);


              if (a.DuracionMinutos > 0) {
                  lblDuracion.setVisibility(View.VISIBLE);
                  int segundos = (int) (a.DuracionMinutos * 60);
                  int minutos = segundos / 60;
                  segundos = segundos % 60;
                  lblDuracion.setText(String.format("%d:%02d", minutos, segundos));
              }else{
                  lblDuracion.setVisibility(View.GONE);
              }

                if (a.DistanciaKM > 0) {
                    lblDistancia.setVisibility(View.VISIBLE);
                    lblDistancia.setText(a.DistanciaKM + " km.");
                }else{
                    lblDistancia.setVisibility(View.GONE);
                }

                if (a.Nombre != null && !a.Nombre.isEmpty()) {
                    lblNombre.setVisibility(View.VISIBLE);
                    lblNombre.setText(a.Nombre);
                } else {
                    lblNombre.setVisibility(View.GONE);
                }

                if (a.RutaTrayecto != null && !a.RutaTrayecto.isEmpty()){
                    btnVerRuta.setVisibility(View.VISIBLE);
                }


        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btnVerRuta)
                rutaListener.onVerRuta(getAdapterPosition(), view);
            else  if (view.getId() == R.id.btnEliminar)
                rutaListener.onEliminar(getAdapterPosition(), view);
        }
    }

    public RutasAdapter(List<Ruta> datos) {
        this.datos = datos;
    }

    @Override
    public PlaceSelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_log_trayecto, viewGroup, false);

        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        PlaceSelectorViewHolder tvh = new PlaceSelectorViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(PlaceSelectorViewHolder viewHolder, int pos) {
        Ruta item = datos.get(pos);
        viewHolder.bind(item);
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

    public interface RutaListener{
        void onVerRuta(int position, View view);
        void onEliminar(int position, View view);

    }
}

