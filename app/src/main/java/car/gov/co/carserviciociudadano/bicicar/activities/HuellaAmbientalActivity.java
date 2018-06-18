package car.gov.co.carserviciociudadano.bicicar.activities;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.model.Estadistica;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewReportes;
import car.gov.co.carserviciociudadano.bicicar.presenter.ReportesPresenter;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class HuellaAmbientalActivity extends BaseActivity implements IViewReportes
{
    @BindView(R.id.progressMensual)  ProgressBar progressMensual;
    @BindView(R.id.progressAnual)  ProgressBar progressAnual;

    BarChart barChart;
    PieChart pieChart;
    ReportesPresenter mReportesPresenter;

    List<Estadistica> mLstEstadisticaMensual = new ArrayList<>();
    List<Estadistica> mLstEstadisticaAnual = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huella_ambiental);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        mReportesPresenter = new ReportesPresenter(this);

        obtenerEstadisticas();
        barChart =  findViewById(R.id.barChart);
        pieChart =  findViewById(R.id.pieChart);
    }

    private void mostrarEstadisticaMensual(){
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int index = 0;
        List<Integer> colores = Utils.listColores();
        List<Integer>  barColores = new ArrayList<>();
        for (Estadistica item : mLstEstadisticaMensual){
            entries.add(new BarEntry((float) index, item.KGCO2 ));
            labels.add(item.IDSemana);
            barColores.add(colores.get(index));
            index++;
        }

        BarDataSet set = new BarDataSet(entries, "KG CO2");
        BarData data = new BarData( set );
        data.setBarWidth(0.9f); // set custom bar width
        set.setColors(barColores);
        barChart.setData( data);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
         barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh

    }

    private  void  mostrarEstadisticaAnual(){
        List<Integer> colores = Utils.listColores();
        List<PieEntry> pieEntries = new ArrayList<>();
        int index = 0;
        List<Integer>  pieColor = new ArrayList<>();
        for (Estadistica item : mLstEstadisticaAnual){
            pieEntries.add(new PieEntry(item.KGCO2, item.Mes));
            pieColor.add(colores.get(index));
            index++;
        }

        PieDataSet pieDataset = new PieDataSet(pieEntries, "KG CO2");
        pieDataset.setColors(pieColor);

        PieData pidData = new PieData(pieDataset);
        Description description = new Description();
        Calendar calendar = Calendar.getInstance();
        description.setText("Año " + String.valueOf(calendar.get(Calendar.YEAR)));
        pieChart.setDescription(description);
        pieChart.setData(pidData);
        pieChart.invalidate(); // refresh

    }

    private void obtenerEstadisticas(){
        progressMensual.setVisibility(View.VISIBLE);
        progressAnual.setVisibility(View.VISIBLE);
        mReportesPresenter.getEstadisticaAnual();
        mReportesPresenter.getEstadisticaMensual();
    }

    @Override
    public void onSuccessGranTotal(Estadistica estadistica) {
    }

    @Override
    public void onSuccessEstadistica(List<Estadistica> estadistica) {
    }

    @Override
    public void onErrorGranTotal(ErrorApi errorApi) {

    }

    @Override
    public void onErrorEstadistica(ErrorApi error) {
        progressMensual.setVisibility(View.GONE);
        progressAnual.setVisibility(View.GONE);
        mostrarError(error);

    }

    @Override
    public void onSuccessEstadisticaMesual(List<Estadistica> estadistica) {
        progressMensual.setVisibility(View.GONE);
        mLstEstadisticaMensual.clear();
        mLstEstadisticaMensual.addAll(estadistica);
        mostrarEstadisticaMensual();
    }

    @Override
    public void onSuccessEstadisticaAnual(List<Estadistica> estadistica) {
        progressAnual.setVisibility(View.GONE);
        mLstEstadisticaAnual.clear();
        mLstEstadisticaAnual.addAll(estadistica);
        mostrarEstadisticaAnual();
    }

    private void mostrarError(ErrorApi errorApi){
        if (errorApi.getStatusCode() == 404)
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errorApi.getMessage());
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                obtenerEstadisticas();

            }
        });
        builder.show();
    }
}
