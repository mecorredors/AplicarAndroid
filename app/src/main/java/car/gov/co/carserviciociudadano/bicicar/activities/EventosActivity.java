package car.gov.co.carserviciociudadano.bicicar.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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
import car.gov.co.carserviciociudadano.Utils.Utils;
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
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewTipoEvento;
import car.gov.co.carserviciociudadano.bicicar.presenter.LogTrayectoPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.TiposEventoPresenter;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class EventosActivity extends BaseActivity implements EventoAdapter.EventosListener, IViewEvento, IViewAsistente, IViewTipoEvento {
    @BindView(R.id.recycler_view)  RecyclerView recyclerView;
    EventoAdapter mAdaptador;
    List<Evento> mLstEventos = new ArrayList<>();
    public static final int REQUEST_CREAR_EVENTO = 100;
    public static final int REQUEST_REGISTRAR_ASISTENCIA = 101;
    public static final int REQUEST_AGREGAR_FOTOS = 102;
    public static final int REQUEST_PUBLICAR_EVENTO = 103;
    EventoPresenter eventoPresenter;
    AsistentesPresenter asistentesPresenter;
    TiposEventoPresenter tiposEventoPresenter;
    int idEventoTemporal;
    Beneficiario mBeneficiarioLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        ButterKnife.bind(this);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        mBeneficiarioLogin  = Beneficiarios.readBeneficio();
        asistentesPresenter = new AsistentesPresenter(this);
        tiposEventoPresenter = new TiposEventoPresenter(this);
        eventoPresenter = new EventoPresenter(this);
        recyclerView.setHasFixedSize(true);
        mAdaptador = new EventoAdapter(mLstEventos);
        mAdaptador.setEventosListener(this);
        recyclerView.setAdapter(mAdaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        cargarEventos();
        obtenerDatos();
    }

    private void obtenerDatos(){
        if (Utils.isOnline(this)) {
            mostrarProgressDialog("Obteniendo evento publico actual ");
            eventoPresenter.obtenerPublicoActual();
            tiposEventoPresenter.list();
        }
    }

    private void mensajeReintentar(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                obtenerDatos();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void cargarEventos(){
        List<Evento> eventos = new Eventos().list(Enumerator.Estado.TODOS);
        mLstEventos.clear();
        mLstEventos.addAll(eventos);
        mAdaptador.notifyDataSetChanged();

    }

    @Override
    public void onPause(){
        super.onPause();
        AppCar.VolleyQueue().cancelAll(LogTrayectos.TAG);
        AppCar.VolleyQueue().cancelAll(Asistentes.TAG);
        AppCar.VolleyQueue().cancelAll(TiposEvento.TAG);
        AppCar.VolleyQueue().cancelAll(Eventos.TAG);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        AppCar.VolleyQueue().cancelAll(LogTrayectos.TAG);
        AppCar.VolleyQueue().cancelAll(Asistentes.TAG);
        AppCar.VolleyQueue().cancelAll(TiposEvento.TAG);
        AppCar.VolleyQueue().cancelAll(Eventos.TAG);

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
            TipoEvento tipoEvento = evento.getTipoEvento();
            if (tipoEvento == null) {
                mostrarMensajeDialog("No fue posible obtener los tips de eventos, vuelva a ingresar");
                return;
            }
            if (!tipoEvento.Recorrido || tipoEvento.Publico) {
                publicar(evento);
            } else {
                Intent i = new Intent(this, PublicarEventoActivity.class);
                i.putExtra(Evento.ID_EVENTO, evento.IDEvento);
                startActivityForResult(i, REQUEST_PUBLICAR_EVENTO);
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

        Evento evento = mLstEventos.get(position);
        if (evento != null) {

            if (evento.getTipoEvento() == null){
                mostrarMensajeDialog("No fue posible obtener los tipos de eventos, vuelva a ingresar");
                return;
            }

            if (evento.getTipoEvento().Publico && evento.IDResponsable != mBeneficiarioLogin.IDBeneficiario){
                mostrarMensajeDialog("Usted no es el responsable de este evento, pero puede registrar asistencia  y tomar fotos");
                return;
            }

            Intent intent = getIntent();
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
        ocultarProgressDialog();
        actualizarEstadoEvento(evento.IDEvento);
    }
    @Override
    public void onSuccessEliminar(Evento evento) {
        ocultarProgressDialog();
        cargarEventos();

    }

    @Override
    public void onSuccessPublicoActual(Evento evento) {
        ocultarProgressDialog();
        cargarEventos();
    }

    @Override
    public void onErrorEvento(ErrorApi error) {
        ocultarProgressDialog();
        mostrarMensajeDialog(error.getMessage());
    }

    @Override
    public void onErrorPublicoActual(ErrorApi error) {
        AppCar.VolleyQueue().cancelAll(TiposEvento.TAG);
        AppCar.VolleyQueue().cancelAll(Eventos.TAG);
        ocultarProgressDialog();
        if  (error.getStatusCode() == 404){
            mostrarMensaje(error.getMessage());
        }else {
            mensajeReintentar(error.getMessage());
        }
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
            Evento evento = eventoPresenter.read(idEventoTemporal);
            eventoPresenter.modificar(evento);
        }

        @Override
        public void onErrorArchivoAdjunto(String mensaje) {
            ocultarProgressDialog();
            if (!mensaje.isEmpty()){
                mostrarMensajeDialog(mensaje);
            }
        }
    };

    @Override
    public void onSuccessTipoEvento(List<TipoEvento> lstTiposEvento) {
        ocultarProgressDialog();
    }

    @Override
    public void onErrorTiposEvento(ErrorApi error) {
        AppCar.VolleyQueue().cancelAll(TiposEvento.TAG);
        AppCar.VolleyQueue().cancelAll(Eventos.TAG);
        ocultarProgressDialog();
        mensajeReintentar(error.getMessage());
    }
}
