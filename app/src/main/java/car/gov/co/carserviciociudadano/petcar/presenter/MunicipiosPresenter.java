package car.gov.co.carserviciociudadano.petcar.presenter;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Contenedores;
import car.gov.co.carserviciociudadano.petcar.interfaces.IContenedor;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;
import car.gov.co.carserviciociudadano.petcar.model.Municipio;

/**
 * Created by apple on 23/09/18.
 */

public class MunicipiosPresenter implements IContenedor {

    IViewMunicipios iView;

    public MunicipiosPresenter(IViewMunicipios iView){
        this.iView = iView;
    }

    public void getMunicipios(){

        String keycache  = ContenedorPresenter.CONTENEDORES_CACHE + BuildConfig.VERSION_CODE;
        ContenedorPresenter contenedorPresenter = ContenedorPresenter.getFromCache(keycache);
        if (contenedorPresenter != null ){
            onSuccessContenedores(contenedorPresenter.mLstContenedores);
        }else {
            Contenedores.getContenedores(this);
        }
    }

    @Override
    public void onErrorContenedores(ErrorApi error) {
        iView.onError(error);
    }

    @Override
    public void onSuccessAgregar(Contenedor contenedor) {

    }

    @Override
    public void onSuccessModificar(Contenedor contenedor) {

    }

    @Override
    public void onErrorAgregar(ErrorApi error) {

    }

    @Override
    public void onErrorModificar(ErrorApi error) {

    }

    @Override
    public void onSuccessContenedores(List<Contenedor> lstContenedores) {
        if (lstContenedores != null && lstContenedores.size() > 0){
            List<Municipio> lstMunicipios = new ArrayList<>();
            lstMunicipios.add(new Municipio(null , "Ver todos"));
            for (Contenedor item: lstContenedores){
                Municipio municipio = findMunicipio(lstMunicipios, item.IDMunicipio);
                if (municipio == null)
                    lstMunicipios.add(new Municipio(item.IDMunicipio, item.Municipio));

            }

            for(Municipio municipio: lstMunicipios) {
                municipio.Count = countContenedores(lstContenedores, municipio.ID);
            }

            iView.onSuccess(lstMunicipios);
        }else{
            iView.onError(new ErrorApi(404, "No hay municipios"));
        }
    }

    private Municipio findMunicipio(List<Municipio> lstMunicipios, String idMunicipio){

        if (idMunicipio == null) return  null;

        if (lstMunicipios != null && lstMunicipios.size()> 0) {
            for (Municipio item : lstMunicipios) {
                if (item.ID != null && item.ID.equals(idMunicipio))
                    return item;
            }
        }
        return  null;
    }

    private int countContenedores(List<Contenedor> lstContenedores, String idMunicipio){

        if (idMunicipio == null) return  0;
        int count = 0;
        if (lstContenedores != null && lstContenedores.size()> 0) {
            for (Contenedor item : lstContenedores) {
                if (item.IDMunicipio != null && item.IDMunicipio.equals(idMunicipio)){
                    count++;
                }
            }
        }
        return  count;
    }
}
