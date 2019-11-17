package car.gov.co.carserviciociudadano.petcar.presenter;

import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.dataaccess.Municipios;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Municipio;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Contenedores;
import car.gov.co.carserviciociudadano.petcar.interfaces.IContenedor;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;

/**
 * Created by apple on 9/09/18.
 */

public class ContenedorPresenter implements IContenedor {

    public static final String CONTENEDORES_CACHE = "contenedores_cache";
    public List<Contenedor> mLstContenedores;
    public String keycache = "";
    IViewContenedor iview;
    public  ContenedorPresenter(IViewContenedor iview){
        this.iview = iview;
        keycache = CONTENEDORES_CACHE + BuildConfig.VERSION_CODE;
    }

    public void publicar(Contenedor contenedor){
        if (contenedor.IDContenedor > 0) {
            new Contenedores().modificar(contenedor, this);
        }else{
            new Contenedores().agregar(contenedor, this);
        }

    }

    public void getContenedores(String idMunicipio){
        ContenedorPresenter contenedorPresenter = getFromCache(keycache);

        if (contenedorPresenter != null ){

            List<Contenedor> lstContenedores = new ArrayList<>();
            for (Contenedor item : contenedorPresenter.mLstContenedores){

                if (item.IDMunicipio.equals(idMunicipio)){
                    lstContenedores.add(item);
                }
            }
            if (mLstContenedores.size() == 0)
                iview.onErrorContenedores(new ErrorApi(404, "No hay contenedores en este municipio"));
            else
                iview.onSuccessContenedores(mLstContenedores);
        }else {
            Contenedores.getContenedores(idMunicipio, this);
        }

    }

    public void getContenedores(){

        ContenedorPresenter contenedorPresenter = ContenedorPresenter.getFromCache(keycache);
        if (contenedorPresenter != null ){
            iview.onSuccessContenedores(contenedorPresenter.mLstContenedores);
        }else {
            Contenedores.getContenedores(this);
        }
    }

    public static ContenedorPresenter getFromCache(String keycache){
        ContenedorPresenter  contenedorPresenter  = null;
        if (Utils.existeCache(keycache)) {
            try {
                contenedorPresenter = Reservoir.get(keycache, ContenedorPresenter.class);
            } catch (Exception ex) {
                Log.e(CONTENEDORES_CACHE, ex.toString());
            }
        }
        return contenedorPresenter;
    }


    @Override
    public void onErrorContenedores(ErrorApi error) {
        iview.onErrorContenedores(error);
    }

    @Override
    public void onSuccessAgregar(Contenedor contenedor) {
        contenedor.Estado = Enumerator.Estado.PUBLICADO;
        new Contenedores().guardar(contenedor);
        iview.onSuccessAgregar(contenedor);
    }

    @Override
    public void onSuccessModificar(Contenedor contenedor) {
        contenedor.Estado = Enumerator.Estado.PUBLICADO;
        new Contenedores().guardar(contenedor);
        iview.onSuccessModificar(contenedor);
    }

    @Override
    public void onErrorAgregar(ErrorApi error) {
        iview.onErrorAgregar(error);
    }

    @Override
    public void onErrorModificar(ErrorApi error) {
        iview.onErrorModificar(error);
    }

    @Override
    public void onSuccessContenedores(List<Contenedor> lstContenedores) {
        try {
            Reservoir.put(keycache, ContenedorPresenter.this,  Enumerator.CacheTimeInMilliSeconds.PETCAR);
        } catch (Exception e) {
            Log.e(CONTENEDORES_CACHE, " guardar cache " + e.toString());
        }

        iview.onSuccessContenedores(lstContenedores);
    }
}
