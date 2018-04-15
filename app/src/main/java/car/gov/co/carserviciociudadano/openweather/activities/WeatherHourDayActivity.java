package car.gov.co.carserviciociudadano.openweather.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Config;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.openweather.adapter.ForecastAdapter;
import car.gov.co.carserviciociudadano.openweather.interfaces.IViewOpenWeather;
import car.gov.co.carserviciociudadano.openweather.model.ConditionCodes;
import car.gov.co.carserviciociudadano.openweather.model.CurrentWeather;
import car.gov.co.carserviciociudadano.openweather.model.Forecast;
import car.gov.co.carserviciociudadano.openweather.model.ItemForecast;
import car.gov.co.carserviciociudadano.openweather.model.List;
import car.gov.co.carserviciociudadano.openweather.presenter.OpenWeatherPresenter;
import car.gov.co.carserviciociudadano.parques.activities.IntentHelper;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;


public class WeatherHourDayActivity extends BaseActivity implements IViewOpenWeather {
    @BindView(R.id.lyWeather)  LinearLayout lyWeather;
    @BindView(R.id.progressView)   ProgressBar progressView;
    OpenWeatherPresenter openWeatherPresenter;
    private Parque mParque;
    ConditionCodes conditionCodes;

    private RecyclerView mRecyclerView;
    ForecastAdapter mAdaptador;
    java.util.List<ItemForecast> mLstItemForecast;
    Calendar fecha;
    public static final String TAG_FECHA = "tag_fecha";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_hour_day);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);


        openWeatherPresenter = new OpenWeatherPresenter(this);
        mParque = new Parque();
        conditionCodes = new ConditionCodes();
        mParque = (Parque) IntentHelper.getObjectForKey(Parques.TAG);
        fecha = (Calendar) IntentHelper.getObjectForKey(TAG_FECHA);

        if (mParque != null) {
            bar.setTitle(Utils.getDayOfWeek(fecha) + " " + Utils.toStringLargeFromDate(fecha.getTime())  );

            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLstItemForecast = new ArrayList<>();
            mAdaptador = new ForecastAdapter(mLstItemForecast);


            mRecyclerView.setAdapter(mAdaptador);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

            loadWeather();

        }

    }

    private void loadWeather() {
        progressView.setVisibility(View.VISIBLE);
        openWeatherPresenter.forecast5Day3Hour(mParque.getLatitude(), mParque.getLongitude());

    }

    @Override
    public void onError(ErrorApi error) {
        progressView.setVisibility(View.GONE);
        Snackbar.make(lyWeather, error.getMessage(), Snackbar.LENGTH_LONG)
                //.setActionTextColor(Color.CYAN)
                .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green))
                .setAction("REINTENTAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadWeather();
                    }
                })
                .show();

    }

    @Override
    public void onSuccessCurrentWeather(CurrentWeather currentWeather) {

    }

    @Override
    public void onSuccessForecast16Daily(Forecast forecast) {

    }

    @Override
    public void onSuccessForecast5Day3Hour(Forecast forecast) {
        progressView.setVisibility(View.GONE);
        if (forecast != null){
            if (forecast.list != null & forecast.list.size() > 0){
                mLstItemForecast.clear();
                for (List item : forecast.list){
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(item.dt * 1000);
                    Log.d("txthora", item.dt_txt);
                    if (c.get(Calendar.DAY_OF_MONTH) == fecha.get(Calendar.DAY_OF_MONTH)){
                        ItemForecast itemForecast = new ItemForecast();
                        itemForecast.icon = item.weather.get(0).icon;
                        itemForecast.condition = conditionCodes.getName(item.weather.get(0).id);
                        itemForecast.temp = item.main.getTempRound() + " " + getResources().getString(R.string.grados);
                        itemForecast.humidity = item.main.humidity + " " + getResources().getString(R.string.porciento);
                        itemForecast.windSpeed = item.wind.speed + " " + getResources().getString(R.string.unidad_viento);
                        itemForecast.fecha = Utils.getHour(c.getTimeInMillis()) ;
                        itemForecast.iconNextPage = false ;

                        mLstItemForecast.add(itemForecast);
                    }
                }

                mAdaptador.notifyDataSetChanged();

            }
        }

    }
}
