package car.gov.co.carserviciociudadano.consultapublica.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.consultapublica.model.Documento;
import car.gov.co.carserviciociudadano.consultapublica.model.Expediente;

/**
 * Created by Olger on 28/11/2016.
 */

public class DocumentoExpedienteAdapter extends RecyclerView.Adapter<DocumentoExpedienteAdapter.PlaceSelectorViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<Documento> datos;

    public static class PlaceSelectorViewHolder   extends RecyclerView.ViewHolder {

        private TextView lblNumero;
        private TextView lblTipo;
        private TextView lblFecha;
        private TextView lblDocumento;


        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);

            lblNumero = (TextView)itemView.findViewById(R.id.lblNumero);
            lblFecha = (TextView)itemView.findViewById(R.id.lblFecha);
            lblTipo = (TextView) itemView.findViewById(R.id.lblTipo);
            lblDocumento = (TextView) itemView.findViewById(R.id.lblDocumento);


        }

        public void bind(Documento item) {
            lblTipo.setText(item.getTipoDocumento());
            lblFecha.setText(Utils.toStringLargeFromDate(item.getFecha()));
            lblNumero.setText(String.valueOf(item.getNumeroDocumento()));
            lblDocumento.setText(item.getDocumento());
        }
    }

    public DocumentoExpedienteAdapter(List<Documento> datos) {
        this.datos = datos;
    }

    @Override
    public PlaceSelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_documento_expediente                                                                                                                          , viewGroup, false);

        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        PlaceSelectorViewHolder tvh = new PlaceSelectorViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(PlaceSelectorViewHolder viewHolder, int pos) {
        Documento item = datos.get(pos);
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
