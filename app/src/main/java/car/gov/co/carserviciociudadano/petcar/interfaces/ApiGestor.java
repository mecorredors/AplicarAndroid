package car.gov.co.carserviciociudadano.petcar.interfaces;

import car.gov.co.carserviciociudadano.bicicar.model.RespuestaApi;
import car.gov.co.carserviciociudadano.petcar.model.Gestor;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by apple on 9/09/18.
 */

public interface ApiGestor {
    @GET("api/gestores/Login?")
    Call<Gestor> login(@Query("identificacion") String identificacion, @Query("claveApp") String claveApp);
    @GET("api/gestores/CambiarClave?")
    Call<Gestor> cambiarClave(@Query("identificacion") String identificacion, @Query("claveApp") String claveApp, @Query("nuevaClave") String nuevaClave);
    @POST("api/gestores/recordarClave")
    Call<RespuestaApi> recordarClave(@Body Gestor gestor);
}
