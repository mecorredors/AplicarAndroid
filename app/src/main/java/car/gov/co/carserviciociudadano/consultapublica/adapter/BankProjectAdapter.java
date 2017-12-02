package car.gov.co.carserviciociudadano.consultapublica.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.consultapublica.model.BankProjectDocument;
import car.gov.co.carserviciociudadano.consultapublica.model.BankProjectItem;
import car.gov.co.carserviciociudadano.consultapublica.model.Documento;

/**
 * Created by Olger on 28/11/2016.
 */

public class BankProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<BankProjectDocument> datos;
    private BankProjectItem bankProjectItem;

    public void setBankProjectItem(BankProjectItem bankProjectItem) {
        this.bankProjectItem = bankProjectItem;
    }

    public static class ProjectItemViewHolder   extends RecyclerView.ViewHolder {

        private TextView lblCodigo;
        private TextView lblNombre;
        private TextView lblDescripcion;
        private TextView lblTema;
        private TextView lblRadicadoSidCar;
        private TextView lblFechaRegistro;
        private TextView lblEstadoActual;
        private TextView lblFechaEstadoActual;



        public ProjectItemViewHolder(View itemView) {
            super(itemView);

            lblCodigo = (TextView)itemView.findViewById(R.id.lblCodigo);
            lblNombre = (TextView)itemView.findViewById(R.id.lblNombre);
            lblDescripcion = (TextView) itemView.findViewById(R.id.lblDescripcion);
            lblTema = (TextView) itemView.findViewById(R.id.lblTema);
            lblRadicadoSidCar = (TextView) itemView.findViewById(R.id.lblRadicadoSidCar);
            lblFechaRegistro = (TextView) itemView.findViewById(R.id.lblFechaRegistro);
            lblEstadoActual = (TextView) itemView.findViewById(R.id.lblEstadoActual);
            lblFechaEstadoActual = (TextView) itemView.findViewById(R.id.lblFechaEstadoActual);



        }

        public void bind(BankProjectItem item) {
            lblCodigo.setText(item.getRegistrationCode());
            lblNombre.setText(item.getName());
            lblDescripcion.setText(item.getDescription() );
            lblTema.setText(item.getTopicName());
            lblRadicadoSidCar.setText(item.getSidcarNumber());
            lblFechaRegistro.setText( Utils.toStringLargeFromDate(item.getRegistrationDate()));
            lblEstadoActual.setText(item.getStatusName());
            lblFechaEstadoActual.setText( Utils.toStringLargeFromDate(item.getStatusDate()));
        }
    }

    public static class DocumentViewHolder   extends RecyclerView.ViewHolder {

        private TextView lblTituloDocumentos;
        private TextView lblCodigo;
        private TextView lblFecha;
        private TextView lblTitulo;

        public DocumentViewHolder(View itemView) {
            super(itemView);

            lblTituloDocumentos = (TextView)itemView.findViewById(R.id.lblTituloDocumentos);
            lblCodigo = (TextView)itemView.findViewById(R.id.lblCodigo);
            lblFecha = (TextView)itemView.findViewById(R.id.lblFecha);
            lblTitulo = (TextView) itemView.findViewById(R.id.lblTitulo);
        }

        public void bind(BankProjectDocument item, int position) {
            lblTituloDocumentos.setVisibility(position == 1? View.VISIBLE : View.GONE );
            lblCodigo.setText(item.getRegistrationCode());
            lblFecha.setText(Utils.toStringLargeFromDate(item.getRegistrationDate()));
            lblTitulo.setText(item.getTitle() );
         }
    }

    public BankProjectAdapter(BankProjectItem bankProjectItem, List<BankProjectDocument> datos) {
        this.bankProjectItem = bankProjectItem;
        this.datos = datos;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0: 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == 0) {
            View  itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_bank_project_item, viewGroup, false);

            ProjectItemViewHolder vh = new ProjectItemViewHolder(itemView);
            itemView.setOnClickListener(this);
            return vh;
        }else{
            View  itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_bank_document, viewGroup, false);

            itemView.setOnClickListener(this);
            DocumentViewHolder vh = new DocumentViewHolder(itemView);

            return vh;
        }


        //android:background="?android:attr/selectableItemBackground"


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int pos) {

       if (pos == 0){
           ProjectItemViewHolder projectItemViewHolder = (ProjectItemViewHolder) viewHolder;
           projectItemViewHolder.bind(bankProjectItem);
       }else {
           DocumentViewHolder documentViewHolder = (DocumentViewHolder) viewHolder;
           documentViewHolder.bind( datos.get(pos), pos);
       }
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
