package car.gov.co.carserviciociudadano.openweather.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stacktips.view.utils.CalendarUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Config;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import butterknife.ButterKnife;
import car.gov.co.carserviciociudadano.openweather.adapter.ForecastAdapter;
import car.gov.co.carserviciociudadano.openweather.interfaces.IViewOpenWeather;
import car.gov.co.carserviciociudadano.openweather.model.ConditionCodes;
import car.gov.co.carserviciociudadano.openweather.model.CurrentWeather;
import car.gov.co.carserviciociudadano.openweather.model.Forecast;
import car.gov.co.carserviciociudadano.openweather.model.ItemForecast;
import car.gov.co.carserviciociudadano.openweather.model.List;
import car.gov.co.carserviciociudadano.openweather.presenter.OpenWeatherPresenter;
import car.gov.co.carserviciociudadano.parques.activities.IntentHelper;
import car.gov.co.carserviciociudadano.parques.activities.ReservaActivity;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.dataaccess.ServiciosParque;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;
import car.gov.co.carserviciociudadano.parques.model.ServicioParque;


public class WeatherActivity extends BaseActivity implements IViewOpenWeather, View.OnClickListener {

    @BindView(R.id.lyWeather)   LinearLayout lyWeather;
    @BindView(R.id.lblWeather)  TextView lblWeather;
    @BindView(R.id.lblWeatherCondition)  TextView lblWeatherCondition;
    @BindView(R.id.lblHumedad)  TextView lblHumedad;
    @BindView(R.id.lblViento)  TextView lblViento;
    @BindView(R.id.lblMax)  TextView lblMax;
    @BindView(R.id.lblMin)  TextView lblMin;
    @BindView(R.id.lblFecha)  TextView lblFecha;
    @BindView(R.id.icoWeather) ImageView icoWeather;
    @BindView(R.id.progressView)  ProgressBar progressView;

    @BindView(R.id.ly1)   View ly1;
    @BindView(R.id.ly2)   View ly2;
    @BindView(R.id.ly3)   View ly3;
    @BindView(R.id.ly4)   View ly4;
    @BindView(R.id.ly5)   View ly5;

    @BindView(R.id.icoWeather1) ImageView icoWeather1;
    @BindView(R.id.icoWeather2) ImageView icoWeather2;
    @BindView(R.id.icoWeather3) ImageView icoWeather3;
    @BindView(R.id.icoWeather4) ImageView icoWeather4;
    @BindView(R.id.icoWeather5) ImageView icoWeather5;

    @BindView(R.id.lbl1)  TextView lbl1;
    @BindView(R.id.lbl2)  TextView lbl2;
    @BindView(R.id.lbl3)  TextView lbl3;
    @BindView(R.id.lbl4)  TextView lbl4;
    @BindView(R.id.lbl5)  TextView lbl5;

    @BindView(R.id.lblDay1)  TextView lblDay1;
    @BindView(R.id.lblDay2)  TextView lblDay2;
    @BindView(R.id.lblDay3)  TextView lblDay3;
    @BindView(R.id.lblDay4)  TextView lblDay4;
    @BindView(R.id.lblDay5)  TextView lblDay5;

    OpenWeatherPresenter openWeatherPresenter;
    private Parque mParque;
    ConditionCodes conditionCodes;

    private RecyclerView mRecyclerView;
    ForecastAdapter mAdaptador;
    java.util.List<ItemForecast> mLstItemForecast;


    DisplayImageOptions optionsIcoWeather = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .showImageOnLoading(R.color.white)
            .showImageForEmptyUri(R.color.white)
            .showImageOnFail(R.color.white)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        openWeatherPresenter = new OpenWeatherPresenter(this);
        mParque = new Parque();
        conditionCodes = new ConditionCodes();
        mParque = (Parque) IntentHelper.getObjectForKey(Parques.TAG);

        ly1.setVisibility(View.GONE);
        ly2.setVisibility(View.GONE);
        ly3.setVisibility(View.GONE);
        ly4.setVisibility(View.GONE);
        ly5.setVisibility(View.GONE);

        if (mParque != null && mParque.getNombreParque() != null ) {
            bar.setTitle(mParque.getNombreParque());

            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLstItemForecast = new ArrayList<>();
            mAdaptador = new ForecastAdapter(mLstItemForecast);
            mAdaptador.setOnClickListener(this);

            mRecyclerView.setAdapter(mAdaptador);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

            loadWeather();
            lblFecha.setText("Hoy: " + Utils.getFechaActualCorta());
        }

    }

    private void loadWeather(){
        progressView.setVisibility(View.VISIBLE);
        openWeatherPresenter.currentWeather(mParque.getLatitude(), mParque.getLongitude());
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
        progressView.setVisibility(View.GONE);
        if (currentWeather != null) {
            lblWeather.setText(String.valueOf(currentWeather.main.getTempRound()) + " " + getResources().getString(R.string.grados));
            lblMax.setText(String.valueOf(currentWeather.main.getTempMaxRound()) + " " + getResources().getString(R.string.grados));
            lblMin.setText(String.valueOf(currentWeather.main.getTempMinRound()) + " " + getResources().getString(R.string.grados));
            lblHumedad.setText(String.valueOf(currentWeather.main.humidity) + " " + getResources().getString(R.string.porciento));
            lblViento.setText(String.valueOf(currentWeather.wind.speed) + " " + getResources().getString(R.string.unidad_viento));

            if (currentWeather.weather != null && currentWeather.weather.size() > 0) {
                lblWeatherCondition.setText(conditionCodes.getName(currentWeather.weather.get(0).id));
                String urlIcon = Config.OpenWeatherIcon + currentWeather.weather.get(0).icon + ".png";
                ImageLoader.getInstance().displayImage(urlIcon, icoWeather, optionsIcoWeather);
            }
        }
    }

    @Override
    public void onSuccessForecast16Daily(Forecast forecast) {
       //se necesita pagar licencia
    }

    @Override
    public void onSuccessForecast5Day3Hour(Forecast forecast) {
        if (forecast != null){
            String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            if (forecast.list != null & forecast.list.size() > 0){
                for (int i = 0; i < 5; i++) {
                    if (forecast.list.size() > i) {
                        String urlIcon = Config.OpenWeatherIcon + forecast.list.get(i).weather.get(0).icon + ".png";
                        setVisibleLyDay(i, urlIcon, forecast.list.get(i).dt);
                    }
                }

                mLstItemForecast.clear();
                for (List item : forecast.list){
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(item.dt * 1000);
                    Log.d("txthora", item.dt_txt);
                    if (c.get(Calendar.HOUR_OF_DAY) == 12){
                        ItemForecast itemForecast = new ItemForecast();
                        itemForecast.icon = item.weather.get(0).icon;
                        itemForecast.condition = conditionCodes.getName(item.weather.get(0).id);
                        itemForecast.temp = item.main.getTempRound() + " " + getResources().getString(R.string.grados);
                        itemForecast.humidity = item.main.humidity + " " + getResources().getString(R.string.porciento);
                        itemForecast.windSpeed = item.wind.speed + " " + getResources().getString(R.string.unidad_viento);
                        itemForecast.fecha = Utils.getDayOfWeek(c) + " 12 p.m" ;
                        itemForecast.fechaCalendar = c;
                        mLstItemForecast.add(itemForecast);
                    }
                }

                mAdaptador.notifyDataSetChanged();

            }
        }
    }


    private void setVisibleLyDay(int index, String urlIcon, long dt){
        String day = "";

        long timeMillis = dt  * 1000;
      //  Date fecha = new Date(timeMillis);
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(timeMillis);

        Calendar c2 = Calendar.getInstance();


        if (c1.get(Calendar.DAY_OF_MONTH) != c2.get(Calendar.DAY_OF_MONTH)){
            day = getResources().getString(R.string.manana);
        }

        String hora = Utils.getHour(timeMillis);



        switch (index){
            case 0:
                ly1.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(urlIcon, icoWeather1, optionsIcoWeather);
                lbl1.setText(hora);
                lblDay1.setText(day);
                break;
            case 1:
                ly2.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(urlIcon, icoWeather2, optionsIcoWeather);
                lbl2.setText(hora);
                lblDay2.setText(day);
                break;
            case 2:
                ly3.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(urlIcon, icoWeather3, optionsIcoWeather);
                lbl3.setText(hora);
                lblDay3.setText(day);
                break;
            case 3:
                ly4.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(urlIcon, icoWeather4, optionsIcoWeather);
                lbl4.setText(hora);
                lblDay4.setText(day);
                break;
            case 4:
                ly5.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(urlIcon, icoWeather5, optionsIcoWeather);
                lbl5.setText(hora);
                lblDay5.setText(day);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int position = mRecyclerView.getChildAdapterPosition(view);
        ItemForecast itemForecast = mLstItemForecast.get(position);

        Intent i = new Intent(getApplicationContext(), WeatherHourDayActivity.class);
        IntentHelper.addObjectForKey(mParque, Parques.TAG);
        IntentHelper.addObjectForKey(itemForecast.fechaCalendar, WeatherHourDayActivity.TAG_FECHA);

        startActivity(i);
    }
}
