package car.gov.co.carserviciociudadano.bicicar.interfaces;

import java.util.List;
import car.gov.co.carserviciociudadano.bicicar.model.Lugar;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiLugaresBicicar {
    @GET("api/catalogos/ObtenerVeredas?")
    Call<List<Lugar>> getVeredas(@Query("idMunicipio") String idMunicipio);

    @GET("api/catalogos/ObtenerMunicipios")
    Call<List<Lugar>> getMunicipios();
}
