package car.gov.co.carserviciociudadano.bicicar.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.bicicar.adapter.EventoAdapter;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Asistentes;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Eventos;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.LogTrayectos;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.TiposEvento;
import car.gov.co.carserviciociudadano.bicicar.model.ArchivoAdjunto;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Evento;
import car.gov.co.carserviciociudadano.bicicar.model.TipoEvento;
import car.gov.co.carserviciociudadano.bicicar.presenter.ArchivoAdjuntoPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.AsistentesPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.EventoPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewArchivoAdjunto;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewAsistente;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewEvento;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewLogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.presenter.LogTrayectoPresenter;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class EventosActivity extends BaseActivity implements EventoAdapter.EventosListener, IViewEvento, IViewAsistente {
    @BindView(R.id.recycler_view)  RecyclerView recyclerView;
    EventoAdapter mAdaptador;
    List<Evento> mLstEventos = new ArrayList<>();
    public static final int REQUEST_CREAR_EVENTO = 100;
    public static final int REQUEST_REGISTRAR_ASISTENCIA = 101;
    public static final int REQUEST_AGREGAR_FOTOS = 102;
    public static final int REQUEST_PUBLICAR_EVENTO = 103;
    EventoPresenter eventoPresenter;
    AsistentesPresenter asistentesPresenter;
    int idEventoTemporal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        ButterKnife.bind(this);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        asistentesPresenter = new AsistentesPresenter(this);
        eventoPresenter = new EventoPresenter(this);
        recyclerView.setHasFixedSize(true);
        mAdaptador = new EventoAdapter(mLstEventos);
        mAdaptador.setEventosListener(this);
        recyclerView.setAdapter(mAdaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        cargarEventos();
    }

    private void cargarEventos(){
        List<Evento> eventos = new Eventos().list(Enumerator.Estado.TODOS);
        mLstEventos.clear();
        mLstEventos.addAll(eventos);
        mAdaptador.notifyDataSetChanged();

        if (eventos.size() == 0){
            Intent i = new Intent(this, EventoActivity.class);
            startActivityForResult(i, REQUEST_CREAR_EVENTO);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        AppCar.VolleyQueue().cancelAll(LogTrayectos.TAG);
        AppCar.VolleyQueue().cancelAll(Asistentes.TAG);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        AppCar.VolleyQueue().cancelAll(LogTrayectos.TAG);
        AppCar.VolleyQueue().cancelAll(Asistentes.TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        setResult(RESULT_OK, getIntent());

        if(id== android.R.id.home){
            if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                getSupportFragmentManager().popBackStack();
            else
                super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        setResult(RESULT_OK, getIntent());
        super.onBackPressed();
    }

    public void publicar(Evento evento){
        mostrarProgressDialog("Publicando asistencia");
        asistentesPresenter.publicar(evento.IDEvento);
    }

    @Override
    public void onAsistentes(int position, View view) {
        Intent i = new Intent(this, BeneficiariosActivity.class);
        Evento evento = mLstEventos.get(position);
        if (evento != null) {
            i.putExtra(Evento.ID_EVENTO, evento.IDEvento);
            startActivityForResult(i, REQUEST_REGISTRAR_ASISTENCIA);
        }
    }

    @Override
    public void onPublicar(int position, View view) {

        Evento evento = mLstEventos.get(position);
        idEventoTemporal = evento.IDEvento;
        if (evento != null) {
            TipoEvento tipoEvento = new TiposEvento().read(evento.IDTipoEvento);
            if (tipoEvento != null && tipoEvento.Recorrido) {
                Intent i = new Intent(this, PublicarEventoActivity.class);
                i.putExtra(Evento.ID_EVENTO, evento.IDEvento);
                startActivityForResult(i, REQUEST_PUBLICAR_EVENTO);
            } else {
                publicar(evento);
            }
        }
    }


    @Override
    public void onEliminar(final int position, View view) {
        final Evento evento = mLstEventos.get(position);
        if (evento != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(EventosActivity.this);
            builder.setMessage("Seguro desea eliminar el evento " + evento.Nombre + "?");
            builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mostrarProgressDialog("Eliminando");
                    eventoPresenter.eliminar(evento);

                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }
    @OnClick(R.id.btnCrearEvento) void onCrearEvento() {
        Intent i = new Intent(this, EventoActivity.class);
        startActivityForResult(i, REQUEST_CREAR_EVENTO);
    }

    @Override
    public void onFotos(int position, View view) {
        Intent i = new Intent(this, FotosEventoBicicarActivity.class);
        Evento evento = mLstEventos.get(position);
        if (evento != null) {
            i.putExtra(Evento.ID_EVENTO, evento.IDEvento);
            startActivityForResult(i, REQUEST_AGREGAR_FOTOS);
        }

    }

    @Override
    public void onIniciar(int position, View view) {
        Intent intent = getIntent();
        Evento evento = mLstEventos.get(position);
        if (evento != null) {
            intent.putExtra(Evento.ID_EVENTO, evento.IDEvento);
            setResult(RESULT_OK , intent);
            finish();
        }
    }
    @Override
    public void onModificar(int position, View view) {
        Evento evento = mLstEventos.get(position);
        if (evento != null) {
            Intent i = new Intent(this, EventoActivity.class);
            i.putExtra(Evento.ID_EVENTO, evento.IDEvento);
            startActivityForResult(i, REQUEST_CREAR_EVENTO);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CREAR_EVENTO || requestCode == REQUEST_AGREGAR_FOTOS || requestCode == REQUEST_REGISTRAR_ASISTENCIA ){
                cargarEventos();
            }else if (requestCode == REQUEST_PUBLICAR_EVENTO){
                Beneficiario beneficiarioLogin  = Beneficiarios.readBeneficio();
                mostrarProgressDialog("Publicando trayectos");
                LogTrayectoPresenter logTrayectoPresenter = new LogTrayectoPresenter(iViewLogTrayecto);
                logTrayectoPresenter.publicar(beneficiarioLogin.IDBeneficiario, idEventoTemporal);
            }
        }
    }

    @Override
    public void onSuccess(Evento evento) {

    }

    @Override
    public void onSuccessModificar(Evento evento) {
    }
    @Override
    public void onSuccessEliminar(Evento evento) {
        ocultarProgressDialog();
        cargarEventos();

    }

    @Override
    public void onErrorEvento(ErrorApi error) {
        ocultarProgressDialog();
        mostrarMensajeDialog(error.getMessage());
    }

    @Override
    public void onError(ErrorApi error) {
        ocultarProgressDialog();
        mostrarMensajeDialog(error.getMessage());
    }

    @Override
    public void onSuccess(int idEvento) {
        ocultarProgressDialog();
        publicarArchivosAdjuntos(idEvento);

    }

    @Override
    public void onErrorAsistente(ErrorApi error) {
        ocultarProgressDialog();
        mostrarMensajeDialog(error.getMessage());
    }


    IViewLogTrayecto iViewLogTrayecto = new IViewLogTrayecto() {
        @Override
        public void onSuccessLogTrayecto() {
            ocultarProgressDialog();
            publicarArchivosAdjuntos(idEventoTemporal);
        }

        @Override
        public void onErrorLogTrayecto(ErrorApi errorApi) {
            ocultarProgressDialog();
            mostrarMensajeDialog(errorApi.getMessage());
        }
    };

    private  void actualizarEstadoEvento(int idEvento){
        Evento evento = eventoPresenter.read(idEvento);
        if (evento != null){
            evento.Estado = Enumerator.Estado.PUBLICADO;
            eventoPresenter.update(evento);
            cargarEventos();
        }

        mostrarMensajeDialog("Datos de evento publicados correctamente");
    }

    private void publicarArchivosAdjuntos(int idEvento){
        mostrarProgressDialog("Publicando fotos");
        ArchivoAdjuntoPresenter archivoAdjuntoPresenter = new ArchivoAdjuntoPresenter(iViewArchivoAdjunto);
        archivoAdjuntoPresenter.publicarArchivosAdjuntos(idEvento);
    }

    IViewArchivoAdjunto iViewArchivoAdjunto = new IViewArchivoAdjunto() {
        @Override
        public void onSuccessArchivosAdjunto(List<ArchivoAdjunto> lstArchivoAdjunto) {
            ocultarProgressDialog();
            actualizarEstadoEvento(idEventoTemporal);
        }

        @Override
        public void onErrorArchivoAdjunto(String mensaje) {
            ocultarProgressDialog();
            if (!mensaje.isEmpty()){
                mostrarMensajeDialog(mensaje);
            }
        }
    };
}
