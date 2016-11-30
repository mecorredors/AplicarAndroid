package car.gov.co.carserviciociudadano;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import car.gov.co.carserviciociudadano.parques.activities.MainParques;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.interfaces.IParque;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Parques par = new Parques();
        par.list(new IParque() {
            @Override
            public void onSuccess(List<Parque> lstParques) {
                for (Parque p: lstParques){
                    Log.d("par ",p.getNombreParque() +" "+ p.getIDParque());
                }
            }

            @Override
            public void onError(ErrorApi errorApi) {

            }
        });


        Intent i = new Intent(this,MainParques.class);
        startActivity(i);

    }
}
