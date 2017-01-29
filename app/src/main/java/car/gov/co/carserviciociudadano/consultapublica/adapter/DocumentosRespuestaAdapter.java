package car.gov.co.carserviciociudadano.consultapublica.adapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.consultapublica.model.RadicadosDigitalesRespuesta;


/**
 * Created by Olger on 28/11/2016.
 */

public class DocumentosRespuestaAdapter extends RecyclerView.Adapter<DocumentosRespuestaAdapter.PlaceSelectorViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<RadicadosDigitalesRespuesta> datos;

    public static class PlaceSelectorViewHolder
            extends RecyclerView.ViewHolder {


        private TextView lblTipoDoc;
        private TextView lblNumero;
        private TextView lblFecha;
        private TextView lblTipoRespuesta;
        private TextView lblAsunto;


        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);

            lblTipoDoc = (TextView)itemView.findViewById(R.id.lblTipoDoc);
            lblFecha = (TextView)itemView.findViewById(R.id.lblFecha);
            lblNumero = (TextView) itemView.findViewById(R.id.lblNumero);
            lblTipoRespuesta = (TextView) itemView.findViewById(R.id.lblTipoRespuesta);
            lblAsunto = (TextView) itemView.findViewById(R.id.lblAsunto);

        }

        public void bindDocumentos(RadicadosDigitalesRespuesta item) {
            lblTipoDoc.setText(item.getTipoDocumento());
            lblFecha.setText(Utils.toStringLargeFromDate(item.getFechaRadicado()));
            lblNumero.setText(item.getNumeroRadicado());
            lblTipoRespuesta.setText(item.getDescTipoRespuesta());
            lblAsunto.setText(item.getAsunto());
        }
    }

    public DocumentosRespuestaAdapter(List<RadicadosDigitalesRespuesta> datos) {
        this.datos = datos;
    }

    @Override
    public PlaceSelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_documentos_respuesta, viewGroup, false);

        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        PlaceSelectorViewHolder tvh = new PlaceSelectorViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(PlaceSelectorViewHolder viewHolder, int pos) {
        RadicadosDigitalesRespuesta item = datos.get(pos);

        viewHolder.bindDocumentos(item);
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
