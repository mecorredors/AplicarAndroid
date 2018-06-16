package car.gov.co.carserviciociudadano.bicicar.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.adapter.LogTrayectoAdapter;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.LogTrayectos;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.common.BaseActivity;

public class HistorialTrayectosActivity extends BaseActivity implements LogTrayectoAdapter.LogTrayectoListener {
    @BindView(R.id.recycler_view)  RecyclerView recyclerView;
    LogTrayectoAdapter mAdaptador;
    List<LogTrayecto> mLstLogTrayectos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_trayectos);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        obtenerItemsActividad();
        recyclerView.setHasFixedSize(true);
        mAdaptador = new LogTrayectoAdapter(mLstLogTrayectos);
        mAdaptador.setLogTrayectoListener(this);
        recyclerView.setAdapter(mAdaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    private  void obtenerItemsActividad(){
       Beneficiario beneficiarioLogin  = Beneficiarios.readBeneficio();
        mLstLogTrayectos.clear();
        List<LogTrayecto> items = new LogTrayectos().List(Enumerator.BicicarLogTrayecto.TODOS , beneficiarioLogin.IDBeneficiario);

        Calendar fechaActual  = Calendar.getInstance();
        int day = 0;
        int totalHoy = 0;
        for (LogTrayecto item : items){
            Calendar fechaItem = Utils.convertToCalendar(item.Fecha);

            if (fechaItem.get(Calendar.DAY_OF_MONTH) == fechaActual.get(Calendar.DAY_OF_MONTH)){
                totalHoy++;
            }

            if (day !=  fechaItem.get(Calendar.DAY_OF_MONTH)){
                day = fechaItem.get(Calendar.DAY_OF_MONTH);
                LogTrayecto actividad = new LogTrayecto();
                if (day == fechaActual.get(Calendar.DAY_OF_MONTH)){
                    actividad.Label = "Hoy " + Utils.getDayOfWeek(fechaItem) ;
                }else {
                    actividad.Label = Utils.getDayOfWeek(fechaItem) + " " + Utils.toStringLargeFromDate(item.Fecha);
                }
                mLstLogTrayectos.add(actividad);
            }
            mLstLogTrayectos.add(item);
        }

        if (mLstLogTrayectos.size() > 0 && mLstLogTrayectos.get(0).Label.contains("Hoy"))
            mLstLogTrayectos.get(0).TotalItems = totalHoy;




    }

    @Override
    public void onVerRuta(int position, View view) {
        LogTrayecto logTrayecto = mLstLogTrayectos.get(position);
        Intent i = new Intent(this, RutaMapaActivity.class);
        i.putExtra(LogTrayecto.RUTA , logTrayecto.Ruta);
        i.putExtra(LogTrayecto.DURACION_MINUTOS, logTrayecto.DuracionMinutos);
        i.putExtra(LogTrayecto.DISTANCIA_KM, logTrayecto.DistanciaKm);
        startActivity(i);
    }
}
