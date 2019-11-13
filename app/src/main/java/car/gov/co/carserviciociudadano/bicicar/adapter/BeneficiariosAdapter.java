package car.gov.co.carserviciociudadano.bicicar.adapter;


import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.List;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;

/**
 * Created by Olger on 28/11/2016.
 */

public class BeneficiariosAdapter extends RecyclerView.Adapter<BeneficiariosAdapter.PlaceSelectorViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<Beneficiario> datos;
    static BeneficiarioListener beneficiarioListener;

    public void setBeneficiarioListener(BeneficiarioListener beneficiarioListener) {
        this.beneficiarioListener = beneficiarioListener;
    }

    public static class PlaceSelectorViewHolder
            extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {


        private TextView lblNombre;
        private TextView lblColegio;
        private TextView lblCurso;
        private CheckBox cheAsistencia;

        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);

            lblNombre = itemView.findViewById(R.id.lblNombre);
            lblColegio = itemView.findViewById(R.id.lblColegio);
            lblCurso = itemView.findViewById(R.id.lblCurso);
            cheAsistencia = itemView.findViewById(R.id.cheAsistencia);

             cheAsistencia.setOnCheckedChangeListener(this);
        }

        public void bind(Beneficiario b) {
            lblNombre.setText(b.Nombres + " " + b.Apellidos);
            lblCurso.setText(b.Curso);
            cheAsistencia.setChecked(b.Selected);
            cheAsistencia.setEnabled(b.Enabled);

        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            beneficiarioListener.onCheckedAsistenacia(getAdapterPosition(), compoundButton, b);
        }

    }

    public BeneficiariosAdapter(List<Beneficiario> datos) {
        this.datos = datos;
    }

    @Override
    public PlaceSelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_beneficiario, viewGroup, false);

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
    }
}

