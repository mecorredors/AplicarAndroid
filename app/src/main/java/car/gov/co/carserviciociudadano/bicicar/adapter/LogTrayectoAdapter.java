package car.gov.co.carserviciociudadano.bicicar.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;

/**
 * Created by Olger on 28/11/2016.
 */

public class LogTrayectoAdapter extends RecyclerView.Adapter<LogTrayectoAdapter.PlaceSelectorViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<LogTrayecto> datos;
    static   LogTrayectoListener logTrayectoListener;
    public void setLogTrayectoListener(LogTrayectoListener logTrayectoListener){
        this.logTrayectoListener = logTrayectoListener;
    }
    public static class PlaceSelectorViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView lblSerial;
        private TextView lblNombre;
        private CardView lyItemLogTrayecto;
        private Button btnVerRuta;
        private View lyTitulo;
        private View lyItem;
        private TextView lblTitulo;
        private TextView lblTotal;
        private TextView lblDistancia;
        private TextView lblDuracion;

        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);
            lblSerial = itemView.findViewById(R.id.lblSerial);
            lblNombre = itemView.findViewById(R.id.lblNombre);
            lyItemLogTrayecto = itemView.findViewById(R.id.lyItemLogTrayecto);
            btnVerRuta = itemView.findViewById(R.id.btnVerRuta);
            lyTitulo = itemView.findViewById(R.id.lyTitulo);
            lyItem = itemView.findViewById(R.id.lyItem);
            lblTitulo = itemView.findViewById(R.id.lblTitulo);
            lblTotal = itemView.findViewById(R.id.lblTotal);
            lblDistancia = itemView.findViewById(R.id.lblDistancia);
            lblDuracion = itemView.findViewById(R.id.lblDuracion);
            btnVerRuta.setOnClickListener(this);
        }

        public void bind(LogTrayecto a) {

            btnVerRuta.setVisibility(View.GONE);
            if (a.Label != null && !a.Label.isEmpty()){
                lyItem.setVisibility(View.GONE);
                lyTitulo.setVisibility(View.VISIBLE);
                lblTitulo.setText(a.Label);
                lblTotal.setText(a.TotalItems > 0 ? "Total hoy: " + String.valueOf(a.TotalItems) : "");
                lblNombre.setVisibility(View.GONE);
                lyItemLogTrayecto.setCardElevation(1);
                lyItemLogTrayecto.setCardBackgroundColor(AppCar.getContext().getResources().getColor(R.color.blue60));
            }else {
                lyItem.setVisibility(View.VISIBLE);
                lyTitulo.setVisibility(View.GONE);
                lyItemLogTrayecto.setCardElevation(0);
                lyItemLogTrayecto.setCardBackgroundColor(AppCar.getContext().getResources().getColor(R.color.background));

              if (a.Serial != null) {
                  lblSerial.setText(a.Serial + " Rin: " + a.TamanioRin);
                  lblSerial.setVisibility(View.VISIBLE);
              }
              else {
                  lblSerial.setVisibility(View.GONE);
              }

              if (a.DuracionMinutos > 0) {
                  lblDuracion.setVisibility(View.VISIBLE);
                  lblDuracion.setText(a.DuracionMinutos + " Min.");
              }else{
                  lblDuracion.setVisibility(View.GONE);
              }

                if (a.DuracionMinutos > 0) {
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

                if (a.Ruta != null && !a.Ruta.isEmpty()){
                    btnVerRuta.setVisibility(View.VISIBLE);
                }

            }
        }

        @Override
        public void onClick(View view) {
            logTrayectoListener.onVerRuta(getAdapterPosition(), view);
        }
    }

    public LogTrayectoAdapter(List<LogTrayecto> datos) {
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
        LogTrayecto item = datos.get(pos);
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

    public interface LogTrayectoListener{
        void onVerRuta(int position, View view);
    }
}

