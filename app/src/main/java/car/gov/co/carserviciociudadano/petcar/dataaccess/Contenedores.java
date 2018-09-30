package car.gov.co.carserviciociudadano.petcar.dataaccess;

import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;

import java.util.List;

import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.common.APIClient;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.interfaces.ApiContenedor;
import car.gov.co.carserviciociudadano.petcar.interfaces.IContenedor;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by apple on 9/09/18.
 */

public class Contenedores {

    public  static void  getContenedores(String idMunicipio, final IContenedor iContenedor){
        ApiContenedor apiContenedor = APIClient.getClient().create(ApiContenedor.class);
        Call<List<Contenedor>> call = apiContenedor.getContenedores(idMunicipio);

        call.enqueue(new Callback<List<Contenedor>>() {
            @Override
            public void onResponse(Call<List<Contenedor>> call, Response<List<Contenedor>> response) {

                if (response.code() == 200) {

                    iContenedor.onSuccess(response.body());
                } else {
                    iContenedor.onError(new ErrorApi(response));
                }

            }

            @Override
            public void onFailure(Call<List<Contenedor>> call, Throwable t) {
                call.cancel();
                iContenedor.onError(new ErrorApi(t));
                Log.d("item error", t.toString());
            }
        });
    }

    public  static void  getContenedores(final IContenedor iContenedor){
        ApiContenedor apiContenedor = APIClient.getClient().create(ApiContenedor.class);
        Call<List<Contenedor>> call = apiContenedor.getContenedores();

        call.enqueue(new Callback<List<Contenedor>>() {
            @Override
            public void onResponse(Call<List<Contenedor>> call, Response<List<Contenedor>> response) {

                if (response.code() == 200) {
                    iContenedor.onSuccess(response.body());
                }else{
                    iContenedor.onError(new ErrorApi(response));
                }

            }

            @Override
            public void onFailure(Call<List<Contenedor>> call, Throwable t) {
                call.cancel();
                iContenedor.onError(new ErrorApi(t));
                Log.d("item error", t.toString());
            }
        });

    }

}
