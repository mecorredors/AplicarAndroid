package car.gov.co.carserviciociudadano.parques.adapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.model.Abono;
import car.gov.co.carserviciociudadano.parques.model.Banco;
import car.gov.co.carserviciociudadano.parques.model.DetalleReserva;

/**
 * Created by Olger on 28/11/2016.
 */

public class AbonosAdapter extends RecyclerView.Adapter<AbonosAdapter.PlaceSelectorViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<Abono> datos;

    public static class PlaceSelectorViewHolder
            extends RecyclerView.ViewHolder {


        private TextView lblValor;
        private TextView lblFecha;
        private TextView lblBanco;
        private TextView lblNroConsignacion;


        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);

            lblValor = (TextView)itemView.findViewById(R.id.lblValor);
            lblFecha = (TextView)itemView.findViewById(R.id.lblFecha);
            lblBanco = (TextView) itemView.findViewById(R.id.lblBanco);
            lblNroConsignacion = (TextView) itemView.findViewById(R.id.lblNroConsignacion);

        }

        public void bindParque(Abono a) {
            lblValor.setText(Utils.formatoMoney(a.getValorAbono()));
            lblFecha.setText(Utils.toStringLargeFromDate(a.getFechaAbono()));
            lblBanco.setText(a.getBanco());
            lblNroConsignacion.setText("Nro. " + a.getNroConsignacion()  );
        }
    }

    public AbonosAdapter(List<Abono> datos) {
        this.datos = datos;
    }

    @Override
    public PlaceSelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_abonos, viewGroup, false);

        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        PlaceSelectorViewHolder tvh = new PlaceSelectorViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(PlaceSelectorViewHolder viewHolder, int pos) {
        Abono item = datos.get(pos);

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
