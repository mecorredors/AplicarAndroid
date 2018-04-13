package car.gov.co.carserviciociudadano.openweather.interfaces;

import car.gov.co.carserviciociudadano.openweather.model.CurrentWeather;
import car.gov.co.carserviciociudadano.openweather.model.Forecast;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by INTEL on 11/04/2018.
 */

public interface IOpenWeather {
    void onSuccessCurrentWeather(CurrentWeather currentWeather);
    void onSuccessForecast5Day3Hour(Forecast forecast);
    void onSuccessForecast16Daily(Forecast forecast);
    void onError(ErrorApi errorApi);
}
