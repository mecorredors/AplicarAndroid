package car.gov.co.carserviciociudadano.bicicar.adapter;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.ArchivosAdjuntos;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.TiposEvento;
import car.gov.co.carserviciociudadano.bicicar.model.ArchivoAdjunto;
import car.gov.co.carserviciociudadano.bicicar.model.Evento;
import car.gov.co.carserviciociudadano.bicicar.model.TipoEvento;

import static car.gov.co.carserviciociudadano.R.drawable.ic_check_circle_green_24dp;
import static car.gov.co.carserviciociudadano.R.drawable.ic_highlight_off_24dp;
import static car.gov.co.carserviciociudadano.R.drawable.ic_warning_yellow_24dp;

/**
 * Created by Olger on 28/11/2016.
 */

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.PlaceSelectorViewHolder>  implements View.OnClickListener {
    private View.OnClickListener listener;
    private List<Evento> datos;
    static EventosListener EventosListener;

    public void setData(List<Evento> datos){
        if (this.datos == null)
            this.datos = new ArrayList<>();
        else
            this.datos.clear();

        this.datos.addAll(datos);
        notifyDataSetChanged();
    }
    public void setEventosListener(EventosListener EventosListener) {
        this.EventosListener = EventosListener;
    }

    public static class PlaceSelectorViewHolder
            extends RecyclerView.ViewHolder implements CompoundButton.OnClickListener  {

        private TextView lblNombre;
        private TextView lblDescripcion;
        private TextView lblTipo;
        private TextView lblFecha;
        private View lyEstado;
        private ImageView imgEstado;
        private TextView lblEstado;
        private ImageView imgAgregarFotos;
        private View lyAsistentes;
        private View lyPublicar;
        private View lyEliminar;
        private View lyAgregarFotos;
        private View lyIniciar;
        private View lyModificar;

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(false)
                .cacheInMemory(true)
                .showImageOnLoading(R.color.gray2)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .delayBeforeLoading(0)
                .build();

        public PlaceSelectorViewHolder(View itemView) {
            super(itemView);

            lblNombre = itemView.findViewById(R.id.lblNombre);
            lblDescripcion = itemView.findViewById(R.id.lblDescripcion);
            lblTipo = itemView.findViewById(R.id.lblTipo);
            lblFecha = itemView.findViewById(R.id.lblFecha);
            lyEstado = itemView.findViewById(R.id.lyEstado);
            imgEstado = itemView.findViewById(R.id.imgEstado);
            lblEstado = itemView.findViewById(R.id.lblEstado);
            lyAsistentes = itemView.findViewById(R.id.lyAsistentes);
            lyPublicar = itemView.findViewById(R.id.lyPublicar);
            lyEliminar = itemView.findViewById(R.id.lyEliminar);
            imgAgregarFotos = itemView.findViewById(R.id.imgAgregarFotos);
            lyAgregarFotos = itemView.findViewById(R.id.lyAgregarFotos);
            lyIniciar = itemView.findViewById(R.id.lyIniciar);
            lyModificar = itemView.findViewById(R.id.lyModificar);

            lyAsistentes.setOnClickListener(this);
            lyPublicar.setOnClickListener(this);
            lyEliminar.setOnClickListener(this);
            lyAgregarFotos.setOnClickListener(this);
            lyIniciar.setOnClickListener(this);
            lyModificar.setOnClickListener(this);

        }

        public void bind(Evento item) {
            lblNombre.setText(item.Nombre);
            lblDescripcion.setText(item.Descripcion);
            lblFecha.setText(Utils.toStringFromDate(item.FInicio) + " | " + Utils.toStringFromDate(item.FFin)) ;
            TipoEvento tipoEvento = new TiposEvento().read(item.IDTipoEvento);
            if (tipoEvento != null) {
                lblTipo.setText(tipoEvento.Nombre + (tipoEvento.Recorrido ?  " | Con recorrido"  : ""));
                lyIniciar.setVisibility(tipoEvento.Recorrido ? View.VISIBLE : View.GONE);
                lyModificar.setVisibility(tipoEvento.Publico ? View.GONE : View.VISIBLE);
                lyEliminar.setVisibility(tipoEvento.Publico ? View.GONE : View.VISIBLE);

            }

            if (item.Estado == Enumerator.Estado.PENDIENTE_PUBLICAR){
                imgEstado.setImageDrawable(AppCar.getContext().getResources().getDrawable(ic_warning_yellow_24dp));
                lblEstado.setText(AppCar.getContext().getResources().getString(R.string.pendiente_publicar));
            }else{
                imgEstado.setImageDrawable(AppCar.getContext().getResources().getDrawable(ic_check_circle_green_24dp));
                lblEstado.setText(AppCar.getContext().getResources().getString(R.string.publicado));
            }

            List<ArchivoAdjunto> lstFotos = new ArchivosAdjuntos().list(item.IDEvento);
            if (lstFotos.size() > 0) {
                ImageSize targetSize = new ImageSize(280, 240); // result Bitmap will be fit to this size
                ImageLoader.getInstance().loadImage("file://" + lstFotos.get(0).Path, targetSize, defaultOptions, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {
                    }
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        imgAgregarFotos.setImageBitmap(loadedImage);
                    }
                });
            }else{
                imgAgregarFotos.setImageResource(R.drawable.ic_photo_camera_gray_80dp);
            }

        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.lyAsistentes:
                    EventosListener.onAsistentes(getAdapterPosition(), view);
                    break;
                case R.id.lyPublicar:
                    EventosListener.onPublicar(getAdapterPosition(),view);
                    break;
                case R.id.lyEliminar:
                    EventosListener.onEliminar(getAdapterPosition(),view);
                    break;
                case R.id.lyAgregarFotos:
                    EventosListener.onFotos(getAdapterPosition(),view);
                    break;
                case R.id.lyIniciar:
                    EventosListener.onIniciar(getAdapterPosition(),view);
                    break;
                case R.id.lyModificar:
                    EventosListener.onModificar(getAdapterPosition(),view);
                    break;
            }
        }
    }

    public EventoAdapter(List<Evento> datos) {
        this.datos = datos;
    }

    @Override
    public PlaceSelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_evento, viewGroup, false);

        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        PlaceSelectorViewHolder tvh = new PlaceSelectorViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(PlaceSelectorViewHolder viewHolder, int pos) {
        Evento item = datos.get(pos);
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

    public interface EventosListener{
        void onAsistentes(int position, View view);
        void onPublicar(int position, View view);
        void onEliminar(int position, View view);
        void onFotos(int position, View view);
        void onIniciar(int position, View view);
        void onModificar(int position, View view);

    }
}

