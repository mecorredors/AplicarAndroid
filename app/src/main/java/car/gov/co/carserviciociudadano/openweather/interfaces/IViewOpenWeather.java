package car.gov.co.carserviciociudadano.openweather.interfaces;

import car.gov.co.carserviciociudadano.common.IView;
import car.gov.co.carserviciociudadano.openweather.model.CurrentWeather;
import car.gov.co.carserviciociudadano.openweather.model.Forecast;

/**
 * Created by INTEL on 11/04/2018.
 */

public interface IViewOpenWeather extends IView {
    void onSuccessCurrentWeather(CurrentWeather currentWeather);
    void onSuccessForecast16Daily(Forecast forecast);
    void onSuccessForecast5Day3Hour(Forecast forecast);

}
