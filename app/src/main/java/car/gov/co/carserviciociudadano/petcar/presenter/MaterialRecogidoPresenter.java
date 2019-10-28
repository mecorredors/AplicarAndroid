package car.gov.co.carserviciociudadano.petcar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Asistentes;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Gestores;
import car.gov.co.carserviciociudadano.petcar.dataaccess.MaterialesRecogidos;
import car.gov.co.carserviciociudadano.petcar.interfaces.IMaterialRecogido;
import car.gov.co.carserviciociudadano.petcar.model.Gestor;
import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;

public class MaterialRecogidoPresenter {
    IViewMaterialRecogido iViewMaterialRecogido;

    public MaterialRecogidoPresenter(IViewMaterialRecogido iViewMaterialRecogido){
        this.iViewMaterialRecogido = iViewMaterialRecogido;
    }


    public void publicar(){
        Gestor gestor = new Gestores().getLogin();
        if (gestor == null){
            iViewMaterialRecogido.onErrorValidacion("No se encontró gestor, inicie sesión");
            return;
        }

        List<MaterialRecogido> lstMaterialRecogido = new MaterialesRecogidos().list(Enumerator.Estado.PENDIENTE_PUBLICAR);

        if (lstMaterialRecogido.size() == 0){
            iViewMaterialRecogido.onErrorValidacion("No hay material pendiente de publicar");
            return;
        }

        for (MaterialRecogido item : lstMaterialRecogido){
            if (item.Kilos == 0) {
                iViewMaterialRecogido.onErrorValidacion("Los kilos recogidos debe ser mayor a 0 para " + item.getTipoMaterial().Nombre) ;
                return;
            }
        }

        publicar(gestor);
    }

    public void publicar(final Gestor gestor) {

        List<MaterialRecogido> lstMaterialRecogido = new MaterialesRecogidos().list(Enumerator.Estado.PENDIENTE_PUBLICAR);


        final MaterialesRecogidos materialesRecogidosData = new MaterialesRecogidos();
        if (lstMaterialRecogido.size() > 0) {
            MaterialRecogido item = lstMaterialRecogido.get(0);
            item.UsuarioCreacion = gestor.Identificacion;
            item.IDGestor = gestor.IDGestor;

            materialesRecogidosData.publicar(item, new IMaterialRecogido() {
                @Override
                public void onSuccessPublicarMaterial(MaterialRecogido materialRecogido) {
                    materialRecogido.Estado = Enumerator.Estado.PUBLICADO;

                    if (materialesRecogidosData.guardar(materialRecogido)){
                        publicar(gestor);
                    }else{
                        iViewMaterialRecogido.onErrorPublicarMaterial(new ErrorApi(0, "Material publicado pero error al guardar localmente"));
                    }

                }

                @Override
                public void onErrorPublicarMaterial(ErrorApi error) {
                    iViewMaterialRecogido.onErrorPublicarMaterial(error);
                }
            });

        }else{
            iViewMaterialRecogido.onSuccessPublicarMaterial();
        }
    }

}
