package car.gov.co.carserviciociudadano.consultapublica.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.consultapublica.model.Expediente;

/**
 * Created by Olger on 28/11/2016.
 */

public class ExpedienteAdapter extends RecyclerView.Adapter<ExpedienteAdapter.PlaceSelectorViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<Expediente> datos;

    public static class PlaceSelectorViewHolder   extends RecyclerView.ViewHolder {

        private TextView lblNumero;
        private TextView lblTipo;
        private TextView lblFecha;
        private TextView lblOficina;
        private TextView lblSolicitanteInfractor;

        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);

            lblNumero = (TextView)itemView.findViewById(R.id.lblNumero);
            lblFecha = (TextView)itemView.findViewById(R.id.lblFecha);
            lblTipo = (TextView) itemView.findViewById(R.id.lblTipo);
            lblOficina = (TextView) itemView.findViewById(R.id.lblOficina);
            lblSolicitanteInfractor = (TextView) itemView.findViewById(R.id.lblSolicitanteInfractor);

        }

        public void bind(Expediente item) {
            lblTipo.setText(item.getTipoExpediente());
            lblFecha.setText(Utils.toStringLargeFromDate(item.getFechaCreacion()));
            lblNumero.setText(String.valueOf(item.getIDExpediente()));
            lblOficina.setText(item.getProvincia());
            lblSolicitanteInfractor.setText(item.getIntegrante());
        }
    }

    public ExpedienteAdapter(List<Expediente> datos) {
        this.datos = datos;
    }

    @Override
    public PlaceSelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_expediente, viewGroup, false);

        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        PlaceSelectorViewHolder tvh = new PlaceSelectorViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(PlaceSelectorViewHolder viewHolder, int pos) {
        Expediente item = datos.get(pos);
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
}
