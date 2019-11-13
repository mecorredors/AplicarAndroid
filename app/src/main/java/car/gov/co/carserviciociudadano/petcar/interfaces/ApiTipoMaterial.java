package car.gov.co.carserviciociudadano.petcar.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.petcar.model.TipoMaterial;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiTipoMaterial {
    @GET("api/materialesrecogidos/obtenerTiposMaterial")
    Call<List<TipoMaterial>> getTiposMaterial();
}
