package car.gov.co.carserviciociudadano.bicicar.dataaccess;

import android.util.Log;
import java.util.List;
import car.gov.co.carserviciociudadano.bicicar.interfaces.ApiLugaresBicicar;
import car.gov.co.carserviciociudadano.bicicar.interfaces.ILugar;
import car.gov.co.carserviciociudadano.bicicar.model.Lugar;
import car.gov.co.carserviciociudadano.common.APIClient;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Lugares {

    public  static void  getVeredas(final String idMunicipio, final ILugar iLugar){


        ApiLugaresBicicar api = APIClient.getClient().create(ApiLugaresBicicar.class);
        Call<List<Lugar>> call = api.getVeredas(idMunicipio);

        call.enqueue(new Callback<List<Lugar>>() {
            @Override
            public void onResponse(Call<List<Lugar>> call, Response<List<Lugar>> response) {

                if (response.code() == 200) {

                    iLugar.onSuccessVeredas(response.body());
                } else {
                    iLugar.onErrorVeredas(new ErrorApi(response));
                }

            }

            @Override
            public void onFailure(Call<List<Lugar>> call, Throwable t) {
                call.cancel();
                iLugar.onErrorVeredas(new ErrorApi(t));
                Log.d("item error", t.toString());
            }
        });
    }

    public  static void  getMunicipios(final ILugar iLugar){


        ApiLugaresBicicar api = APIClient.getClient().create(ApiLugaresBicicar.class);
        Call<List<Lugar>> call = api.getMunicipios();

        call.enqueue(new Callback<List<Lugar>>() {
            @Override
            public void onResponse(Call<List<Lugar>> call, Response<List<Lugar>> response) {

                if (response.code() == 200) {

                    iLugar.onSuccessMunicipios(response.body());
                } else {
                    iLugar.onErrorMunicipios(new ErrorApi(response));
                }

            }

            @Override
            public void onFailure(Call<List<Lugar>> call, Throwable t) {
                call.cancel();
                iLugar.onErrorMunicipios(new ErrorApi(t));
                Log.d("item error", t.toString());
            }
        });
    }
}
