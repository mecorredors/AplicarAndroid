package car.gov.co.carserviciociudadano.petcar.interfaces;


import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;
import car.gov.co.carserviciociudadano.petcar.model.Visita;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by apple on 9/09/18.
 */

public interface ApiVisita {
    @POST("api/visitas/agregar")
    Call<Visita> agregar(@Body Visita visita);

}
