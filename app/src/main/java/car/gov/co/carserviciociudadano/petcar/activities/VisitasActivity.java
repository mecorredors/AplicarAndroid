package car.gov.co.carserviciociudadano.petcar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Asistentes;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Eventos;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.LogTrayectos;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.TiposEvento;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.adapter.VisitasAdapter;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Visitas;
import car.gov.co.carserviciociudadano.petcar.interfaces.IViewAdjuntoPetCar;
import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;
import car.gov.co.carserviciociudadano.petcar.model.Visita;
import car.gov.co.carserviciociudadano.petcar.presenter.AdjuntoPetCarPresenter;
import car.gov.co.carserviciociudadano.petcar.presenter.IViewVisita;
import car.gov.co.carserviciociudadano.petcar.presenter.VisitaPresenter;

public class VisitasActivity extends BaseActivity  implements IViewVisita, IViewAdjuntoPetCar {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.rbPendiente) RadioButton rbPendiente;
    @BindView(R.id.rbPublicado) RadioButton rbPublicado;
    @BindView(R.id.btnPublicar) Button btnPublicar;


    VisitasAdapter mAdaptador;
    List<Visita> mLstVisita = new ArrayList<>();

    public static final int REQUEST_VISITA  = 101;

    VisitaPresenter VisitaPresenter;
    AdjuntoPetCarPresenter adjuntoPetCarPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitas);
        ButterKnife.bind(this);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        mAdaptador = new VisitasAdapter(mLstVisita);
        recyclerView.setAdapter(mAdaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdaptador.setOnClickListener(onClickListener);
        adjuntoPetCarPresenter = new AdjuntoPetCarPresenter(this);
        obtenerVisitas();
        if (mLstVisita.size() == 0) {
            Intent i = new Intent(this, VisitaActivity.class);
            startActivityForResult(i, REQUEST_VISITA);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        obtenerVisitas();

    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @OnClick({R.id.rbPendiente, R.id.rbPublicado}) void onRadioButtonClick(){
        obtenerVisitas();
    }

    @OnClick(R.id.btnCrearVisita) void onCrearVisita() {
        Intent i = new Intent(this, VisitaActivity.class);
        startActivityForResult(i, REQUEST_VISITA);
    }

    private  void obtenerVisitas(){

        String where = "";
        if (rbPendiente.isChecked()){
            where = Visita.ESTADO + " = " + Enumerator.Estado.PENDIENTE_PUBLICAR + " or " + Visita.ESTADO + " = " + Enumerator.Estado.PENDIENTE_PUBLICAR_FOTOS;
        }else{
            where =  Visita.ESTADO + " = " + Enumerator.Estado.PUBLICADO;
        }

        mLstVisita.clear();
        mLstVisita.addAll(new Visitas().list(where));
        mAdaptador.notifyDataSetChanged();

       btnPublicar.setVisibility((rbPendiente.isChecked() && mLstVisita.size() > 0) ? View.VISIBLE : View.GONE );
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int position = recyclerView.getChildAdapterPosition(v);
            Visita Visita = mLstVisita.get(position);

            Intent i = new Intent(VisitasActivity.this, VisitaActivity.class);
            i.putExtra(Visita.ID, Visita.Id);
            startActivityForResult(i,REQUEST_VISITA);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_VISITA){
                obtenerVisitas();
            }
        }*/
    }

    public  void publicarAdjuntosPetcar(){
        List<Visita> lstVisita = new Visitas().list(Enumerator.Estado.PENDIENTE_PUBLICAR_FOTOS);

        if (lstVisita.size() > 0) {
            adjuntoPetCarPresenter.publicarArchivosAdjuntos(lstVisita.get(0));
        }else{
            ocultarProgressDialog();
            mostrarMensajeDialog("Visita publicado correctamente");
            obtenerVisitas();
        }
    }

    @OnClick(R.id.btnPublicar) void onPublicar(){
        mostrarProgressDialog("Publicando Visita");
        VisitaPresenter = new VisitaPresenter(this);
        VisitaPresenter.publicar();
    }

    @Override
    public void onSuccessPublicarVisita() {
        ocultarProgressDialog();
        mostrarProgressDialog("Publicando fotos");
        publicarAdjuntosPetcar();
    }

    @Override
    public void onErrorPublicarVisita(ErrorApi error) {
        ocultarProgressDialog();
        mostrarMensajeDialog(error.getMessage() + ", No fue posible publicar visita");
        obtenerVisitas();
    }

    @Override
    public void onErrorValidacion(String mensaje){
        ocultarProgressDialog();
        mostrarMensajeDialog(mensaje);
    }

    @Override
    public void onSuccessArchivosAdjunto(MaterialRecogido materialesRecogido) {

    }
    @Override
    public void onSuccessArchivosAdjunto(Visita Visita) {
        Visita.Estado = Enumerator.Estado.PUBLICADO;
        if (new Visitas().update(Visita)) {
            publicarAdjuntosPetcar();
        }else {
            onErrorValidacion("Error al guardar localmente despues de publicar adjunto");
        }

    }

    @Override
    public void onErrorArchivoAdjunto(String mensaje) {
        ocultarProgressDialog();
        mostrarMensajeDialog(mensaje);
    }
}

