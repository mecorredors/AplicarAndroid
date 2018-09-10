package car.gov.co.carserviciociudadano.petcar.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.petcar.model.Contenedor;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by apple on 9/09/18.
 */

public interface ApiContenedor {
    @GET("api/contenedores?")
    Call<List<Contenedor>> getContenedores(@Query("id") String id);

    @GET("api/contenedores")
    Call<List<Contenedor>> getContenedores();
}
