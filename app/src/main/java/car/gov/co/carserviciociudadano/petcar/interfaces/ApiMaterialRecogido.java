package car.gov.co.carserviciociudadano.petcar.interfaces;


import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by apple on 9/09/18.
 */

public interface ApiMaterialRecogido {
    @POST("api/materialesrecogidos/agregar")
    Call<MaterialRecogido> agregar(@Body MaterialRecogido materialRecogido);

}
