package car.gov.co.carserviciociudadano.bicicar.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public static class PlaceSelectorViewHolder
            extends RecyclerView.ViewHolder {


        private TextView lblSerial;
        private TextView lblNombre;
        private CardView lyItemLogTrayecto;

        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);
            lblSerial = itemView.findViewById(R.id.lblSerial);
            lblNombre = itemView.findViewById(R.id.lblNombre);
            lyItemLogTrayecto = itemView.findViewById(R.id.lyItemLogTrayecto);

        }

        public void bindParque(LogTrayecto a) {

            if (a.Label != null && !a.Label.isEmpty()){
                lblSerial.setText(a.Label);
                lblNombre.setVisibility(View.GONE);
                lyItemLogTrayecto.setCardElevation(1);
                lyItemLogTrayecto.setCardBackgroundColor(AppCar.getContext().getResources().getColor(R.color.blue60));
            }else {
                lyItemLogTrayecto.setCardElevation(0);
                lyItemLogTrayecto.setCardBackgroundColor(AppCar.getContext().getResources().getColor(R.color.background));
                lblSerial.setText(a.Serial + " Rin: " + a.TamanioRin);
                if (a.Nombre != null && !a.Nombre.isEmpty()) {
                    lblNombre.setVisibility(View.VISIBLE);
                    lblNombre.setText(a.Nombre);
                } else {
                    lblNombre.setVisibility(View.GONE);
                }
            }
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

