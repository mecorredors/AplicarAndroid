package car.gov.co.carserviciociudadano.petcar.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.petcar.model.Contenedor;
import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by apple on 9/09/18.
 */

public interface ApiContenedor {
    @GET("api/contenedores/get?")
    Call<List<Contenedor>> getContenedores(@Query("id") String id);

    @GET("api/contenedores/get")
    Call<List<Contenedor>> getContenedores();

    @POST("api/contenedores/agregar")
    Call<Contenedor> agregar(@Body Contenedor contenedor);

    @POST("api/contenedores/modificar")
    Call<Contenedor> modificar(@Body Contenedor contenedor);
}
