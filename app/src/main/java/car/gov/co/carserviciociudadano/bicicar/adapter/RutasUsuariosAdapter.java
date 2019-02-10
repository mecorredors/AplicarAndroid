package car.gov.co.carserviciociudadano.bicicar.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.model.Ruta;

/**
 * Created by Olger on 28/11/2016.
 */

public class RutasUsuariosAdapter extends RecyclerView.Adapter<RutasUsuariosAdapter.PlaceSelectorViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<Ruta> datos;

    public static class PlaceSelectorViewHolder
            extends RecyclerView.ViewHolder  {



        private TextView lblNombre;
        private TextView lblDistancia;
        private TextView lblDescripcion;
        private TextView lblNivel;
        private TextView lblNombreUsuarioCreacion;
        private TextView lblFechaCreacion;


        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);

            lblNombre = itemView.findViewById(R.id.lblNombre);
            lblDistancia = itemView.findViewById(R.id.lblDistancia);
            lblDescripcion = itemView.findViewById(R.id.lblDescripcion);
            lblNivel = itemView.findViewById(R.id.lblNivel);
            lblNombreUsuarioCreacion = itemView.findViewById(R.id.lblNombreUsuarioCreacion);
            lblFechaCreacion = itemView.findViewById(R.id.lblFechaCreacion);

        }

        public void bind(Ruta a) {

            int segundos = (int) (a.DuracionMinutos * 60);

            lblDistancia.setText(a.DistanciaKM + " Kms.  " + Utils.horasMinutosSegundos(segundos));
            lblNombre.setText(a.Nombre.toUpperCase());

            if (a.Descripcion != null && !a.Descripcion.isEmpty()) {
                lblDescripcion.setVisibility(View.VISIBLE);
                lblDescripcion.setText(a.Descripcion);
            } else {
                lblDescripcion.setVisibility(View.GONE);
            }

            lblNivel.setText(a.NombreNivel);
            lblNombreUsuarioCreacion.setText("Creado por: " + a.NombreUsuarioCreacion);
            lblFechaCreacion.setText(Utils.toStringLargeFromDate(a.FechaCreacion));
        }

    }

    public RutasUsuariosAdapter(List<Ruta> datos) {
        this.datos = datos;
    }

    @Override
    public PlaceSelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_rutas_usuarios, viewGroup, false);

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

}

