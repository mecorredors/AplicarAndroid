package car.gov.co.carserviciociudadano.petcar.interfaces;

import car.gov.co.carserviciociudadano.petcar.model.Gestor;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by apple on 9/09/18.
 */

public interface ApiGestor {
    @GET("api/gestores/Login?")
    Call<Gestor> login(@Query("identificacion") String identificacion, @Query("claveApp") String claveApp);

}
