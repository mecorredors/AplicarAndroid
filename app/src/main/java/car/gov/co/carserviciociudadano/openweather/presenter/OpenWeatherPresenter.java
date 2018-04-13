package car.gov.co.carserviciociudadano.openweather.presenter;

import car.gov.co.carserviciociudadano.openweather.dataacces.OpenWeather;
import car.gov.co.carserviciociudadano.openweather.interfaces.IOpenWeather;
import car.gov.co.carserviciociudadano.openweather.interfaces.IViewOpenWeather;
import car.gov.co.carserviciociudadano.openweather.model.CurrentWeather;
import car.gov.co.carserviciociudadano.openweather.model.Forecast;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by INTEL on 11/04/2018.
 */

public class OpenWeatherPresenter implements IOpenWeather {
    IViewOpenWeather iViewOpenWeather;

    public OpenWeatherPresenter(IViewOpenWeather iViewOpenWeather){
        this.iViewOpenWeather = iViewOpenWeather;
    }

    public void currentWeather(double latitude, double longitude){
        new OpenWeather().currentWeather(latitude, longitude, this);
    }

    public void forecast5Day3Hour(double latitude, double longitude){
        new OpenWeather().forecast(latitude, longitude,true, this);
    }

    public void forecast16Daily(double latitude, double longitude){
        new OpenWeather().forecast(latitude, longitude,false, this);
    }

    @Override
    public void onSuccessCurrentWeather(CurrentWeather currentWeather) {
        iViewOpenWeather.onSuccessCurrentWeather(currentWeather);
    }

    @Override
    public void onSuccessForecast5Day3Hour(Forecast forecast) {
        iViewOpenWeather.onSuccessForecast5Day3Hour(forecast);
    }

    @Override
    public void onSuccessForecast16Daily(Forecast forecast) {
        iViewOpenWeather.onSuccessForecast16Daily(forecast);
    }

    @Override
    public void onError(ErrorApi errorApi) {
        iViewOpenWeather.onError(errorApi);
    }
}
