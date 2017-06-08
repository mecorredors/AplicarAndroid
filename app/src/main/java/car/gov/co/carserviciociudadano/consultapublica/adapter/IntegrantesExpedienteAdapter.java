package car.gov.co.carserviciociudadano.consultapublica.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.consultapublica.model.Expediente;
import car.gov.co.carserviciociudadano.consultapublica.model.Integrante;

/**
 * Created by Olger on 28/11/2016.
 */

public class IntegrantesExpedienteAdapter extends RecyclerView.Adapter<IntegrantesExpedienteAdapter.PlaceSelectorViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<Integrante> datos;

    public static class PlaceSelectorViewHolder   extends RecyclerView.ViewHolder {

        private TextView lblDatosHtml;


        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);
            lblDatosHtml = (TextView)itemView.findViewById(R.id.lblDatosHtml);

        }

        public void bind(Integrante item) {

            StringBuilder html = new StringBuilder();

            html.append("<html> <b>");
            html.append(item.getNombre());
            html.append("</b>");
           if (item.getTipoIdentificacion().equals("NIT"))
               html.append("<br><font color='#1976D2'> "+item.getTipoIdentificacion() +" "+ item.getIdentificacion() + " de "+ item.getExpedicionIdentificacion()+ " </font>" );

            html.append("<br><font color='#757575'>Calidad que actua: "+item.getRol() +"</font>");
            html.append("<br><font color='#1976D2'>Cédula catastral de "+item.getCedulaCatastral() +" " + item.getPredio() + "</font>");
            html.append("<br><font color='#4CAF50'> Dirección </font>"+item.getDireccionPredio() );
            html.append("<br><font color='#4CAF50'> Vereda </font>"+item.getNombreVereda() );
            html.append("<font color='#4CAF50'> Área  </font>"+item.getArea() );
            html.append("</html>");

            lblDatosHtml.setText(Html.fromHtml(html.toString()));
            // lblTipo.setText(item.getTipoExpediente());

        }
    }

    public IntegrantesExpedienteAdapter(List<Integrante> datos) {
        this.datos = datos;
    }

    @Override
    public PlaceSelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_integrante_expediente, viewGroup, false);

        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        PlaceSelectorViewHolder tvh = new PlaceSelectorViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(PlaceSelectorViewHolder viewHolder, int pos) {
        Integrante item = datos.get(pos);
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
