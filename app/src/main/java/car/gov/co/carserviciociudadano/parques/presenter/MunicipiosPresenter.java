package car.gov.co.carserviciociudadano.parques.presenter;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.dataaccess.Municipios;
import car.gov.co.carserviciociudadano.parques.interfaces.IMunicipio;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Municipio;

/**
 * Created by Olger on 15/01/2017.
 */

public class MunicipiosPresenter {
    IViewMunicipios iViewMunicipios;
    public MunicipiosPresenter(IViewMunicipios iViewMunicipios){
        this.iViewMunicipios = iViewMunicipios;
    }
    public void list()    {
        Municipios municipios = new Municipios();
         municipios.list(iMunicipio);
    }

    IMunicipio iMunicipio = new IMunicipio() {
        @Override
        public void onSuccess(List<Municipio> lstMunicipios) {
            iViewMunicipios.onSuccess(lstMunicipios);
        }

        @Override
        public void onError(ErrorApi error) {
            iViewMunicipios.onError(error);
        }
    };
}
