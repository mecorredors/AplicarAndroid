package car.gov.co.carserviciociudadano.parques.businessrules;

import car.gov.co.carserviciociudadano.parques.dataaccess.ServiciosParque;
import car.gov.co.carserviciociudadano.parques.interfaces.IServicioParque;

/**
 * Created by Olger on 02/12/2016.
 */

public class BRServiciosParques {
    public void list(final IServicioParque iServicioParque, int idParque  )
    {
        ServiciosParque serviciosParque = new ServiciosParque();
        serviciosParque.list(iServicioParque,idParque);
    }
}
