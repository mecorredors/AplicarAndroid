package car.gov.co.carserviciociudadano.consultapublica.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.consultapublica.model.BankProjectDocument;
import car.gov.co.carserviciociudadano.consultapublica.model.BankProjectItem;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 28/01/2017.
 */

public interface IBankProyects {
    void onSuccessProyect(BankProjectItem backProyectItem);
    void onSuccessDocuments(List<BankProjectDocument> lstBankProyectDocuments);
    void onError(ErrorApi error);
}
