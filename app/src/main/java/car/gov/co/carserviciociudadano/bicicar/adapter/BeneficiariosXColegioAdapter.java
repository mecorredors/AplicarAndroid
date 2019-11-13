package car.gov.co.carserviciociudadano.bicicar.adapter;


import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;

import static car.gov.co.carserviciociudadano.R.drawable.ic_check_circle_green_24dp;
import static car.gov.co.carserviciociudadano.R.drawable.ic_highlight_off_24dp;
import static car.gov.co.carserviciociudadano.R.drawable.ic_warning_yellow_24dp;

/**
 * Created by Olger on 28/11/2016.
 */

public class BeneficiariosXColegioAdapter extends RecyclerView.Adapter<BeneficiariosXColegioAdapter.PlaceSelectorViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<Beneficiario> datos;
    static BeneficiarioListener beneficiarioListener;

    public void setBeneficiarioListener(BeneficiarioListener beneficiarioListener) {
        this.beneficiarioListener = beneficiarioListener;
    }

    public static class PlaceSelectorViewHolder
            extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {


        private TextView lblNombre;
        private ImageView imgEstado;
        private TextView lblEstado;

        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);

            lblNombre = itemView.findViewById(R.id.lblNombre);
            imgEstado = itemView.findViewById(R.id.imgEstado);
            lblEstado = itemView.findViewById(R.id.lblEstado);
        }

        public void bind(Beneficiario b) {
            String nombre = b.Nombres + " " + b.Apellidos;
            if (nombre.trim().equals(""))
                lblNombre.setText("Sin definir");
            else
                lblNombre.setText(nombre);

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
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            beneficiarioListener.onCheckedAsistenacia(getAdapterPosition(), compoundButton, b);
        }

        @Override
        public void onClick(View view) {
            beneficiarioListener.onUbicacion(getAdapterPosition() , view);
        }
    }

    public BeneficiariosXColegioAdapter(List<Beneficiario> datos) {
        this.datos = datos;
    }

    @Override
    public PlaceSelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_beneficiario_x_colegio, viewGroup, false);

        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        PlaceSelectorViewHolder tvh = new PlaceSelectorViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(PlaceSelectorViewHolder viewHolder, int pos) {
        Beneficiario item = datos.get(pos);
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

    public interface BeneficiarioListener{
        void onCheckedAsistenacia(int position, CompoundButton compoundButton, boolean b);
        void onUbicacion(int position, View view);
    }
}

