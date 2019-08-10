package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Eventos;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.TiposEvento;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IEvento;
import car.gov.co.carserviciociudadano.bicicar.model.Evento;
import car.gov.co.carserviciociudadano.bicicar.model.TipoEvento;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class EventoPresenter implements IEvento {
    IViewEvento iViewEvento;

    public EventoPresenter(IViewEvento iViewEvento){
        this.iViewEvento = iViewEvento;
    }

    public void obtenerPublicoActual(){
        new Eventos().obtenerPublicoActual(this);
    }

    public Evento read(int idEvento){
        return new Eventos().read(idEvento);
    }

    public void publicar(Evento evento) {
        new Eventos().publicar(evento, this);
    }

    public void modificar(Evento evento) {
        new Eventos().modificar(evento, this);
    }

    public void eliminar(Evento evento) {
        if (Utils.isOnline(AppCar.getContext()) && evento.Estado == Enumerator.Estado.PENDIENTE_PUBLICAR ) {
            new Eventos().eliminar(evento, this);
        }else{
            onSuccessEliminar(evento);
        }

    }

    public boolean update(Evento evento){
        return  new Eventos().update(evento);
    }
    public boolean insert(Evento evento){
        evento.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR;
        return  new Eventos().insert(evento);
    }
    public List<Evento> listLocal(int estado){
        return new Eventos().list(estado);
    }

    @Override
    public void onSuccess(Evento evento) {
        iViewEvento.onSuccess(evento);
    }

    @Override
    public void onSuccessModificar(Evento evento) {
        iViewEvento.onSuccessModificar(evento);
    }

    @Override
    public void onSuccessEliminar(Evento evento) {
        new Eventos().delete(evento.IDEvento);
        iViewEvento.onSuccessEliminar(evento);
    }

    @Override
    public void onSuccessPublicoActual(Evento evento) {
        Evento item = read(evento.IDEvento);
        if (item == null){
            insert(evento);
        }else{
            evento.Estado = item.Estado;
            update(evento);
        }
        iViewEvento.onSuccessPublicoActual(evento);
    }

    @Override
    public void onError(ErrorApi error) {
        iViewEvento.onErrorEvento(error);
    }

    @Override
    public void onErrorPublicoActual(ErrorApi error) {
        iViewEvento.onErrorPublicoActual(error);
    }
}
