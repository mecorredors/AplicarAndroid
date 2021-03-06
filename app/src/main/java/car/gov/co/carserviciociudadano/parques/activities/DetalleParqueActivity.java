package car.gov.co.carserviciociudadano.parques.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Config;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.openweather.activities.WeatherActivity;
import car.gov.co.carserviciociudadano.openweather.interfaces.IViewOpenWeather;
import car.gov.co.carserviciociudadano.openweather.model.ConditionCodes;
import car.gov.co.carserviciociudadano.openweather.model.CurrentWeather;
import car.gov.co.carserviciociudadano.openweather.model.Forecast;
import car.gov.co.carserviciociudadano.openweather.presenter.OpenWeatherPresenter;
import car.gov.co.carserviciociudadano.parques.adapter.ServiciosParqueAdapter;
import car.gov.co.carserviciociudadano.parques.presenter.ArchivosParquePresenter;
import car.gov.co.carserviciociudadano.parques.presenter.ServiciosParquesPresenter;
import car.gov.co.carserviciociudadano.parques.presenter.IViewServiciosParque;
import car.gov.co.carserviciociudadano.parques.dataaccess.ArchivosParque;
import car.gov.co.carserviciociudadano.parques.presenter.IViewArchivoParque;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.dataaccess.ServiciosParque;
import car.gov.co.carserviciociudadano.parques.model.ArchivoParque;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;
import car.gov.co.carserviciociudadano.parques.model.ServicioParque;

public class DetalleParqueActivity extends BaseActivity  {


    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .showImageOnLoading(R.color.gray)
            .showImageForEmptyUri(R.drawable.sin_foto)
            .showImageOnFail(R.drawable.sin_foto)
            .build();

    DisplayImageOptions optionsLogo = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .showImageOnLoading(R.mipmap.logo_car_pajaro)
            .showImageForEmptyUri(R.mipmap.logo_car_pajaro)
            .showImageOnFail(R.mipmap.logo_car_pajaro)
            .build();

    DisplayImageOptions optionsIcoWeather = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .showImageOnLoading(R.color.white)
            .showImageForEmptyUri(R.color.white)
            .showImageOnFail(R.color.white)
            .build();

//    private int mIdParque;
//    private String mNombreParque;
    private FirebaseAnalytics mFirebaseAnalytics;
    ImageView mImagen;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    AppBarLayout mApp_bar;
    TextView mLblObservaciones;
    TextView mLblArchivoObservaciones;
    TextView mLblTitulo;
    private RecyclerView mRecyclerView;
    ServiciosParqueAdapter mAdaptador;
    List<ServicioParque> mLstServiciosParque;
    ProgressBar mProgressView;
    private Parque mParque;
    TextView mLblVerFotos;
    Button mBtnComoLlegar;
    Button mBtnMapasDelParque;
    ImageView mImgLogoParque;
    NestedScrollView nestedScrollView;
    ImageView mIcoWeather;
    TextView mLblWeather;
    TextView mLblWeatherCondition;
    View mLyWeather;
    ConditionCodes conditionCodes;
    OpenWeatherPresenter openWeatherPresenter;
    //cambio melba 3
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_parque);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        findViewsById();

        mParque = new Parque();
        mParque = (Parque) IntentHelper.getObjectForKey(Parques.TAG);
        if (mParque != null && mParque.getNombreParque() != null && mParque.getUrlArchivoParque() != null) {
            ImageLoader.getInstance().displayImage(mParque.getUrlArchivoParque(), mImagen, options);

            mCollapsingToolbarLayout.setTitle(mParque.getNombreParque());
            mLblObservaciones.setText(mParque.getObservacionesParque());

            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLstServiciosParque = new ArrayList<>();
            mAdaptador = new ServiciosParqueAdapter(mLstServiciosParque);
            mAdaptador.setOnClickListener(onClickListener);

            mRecyclerView.setAdapter(mAdaptador);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

            loadArchivosParque();
            loadServiciosParque();

            nestedScrollView.getParent().requestChildFocus(nestedScrollView, nestedScrollView); // para subir el scroll

            conditionCodes = new ConditionCodes();
            openWeatherPresenter = new OpenWeatherPresenter(iViewOpenWeather);
            openWeatherPresenter.currentWeather(mParque.getLatitude(), mParque.getLongitude());

            if (BuildConfig.DEBUG == false) {
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(mParque.getNombreParque()));
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Detalle Parque");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Enumerator.ContentTypeAnalitic.PARQUES);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        }else{
            finish();
        }
    }

   public void onDestroy(){
       super.onDestroy();
      // IntentHelper.remove(Parques.TAG);
   }
    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(ArchivosParque.TAG);
        AppCar.VolleyQueue().cancelAll(ServiciosParque.TAG);
        super.onPause();

    }
    @Override
    public void onBackPressed(){

        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();

    }
    private void findViewsById(){
        mImagen = (ImageView) findViewById(R.id.imagen);
        mImgLogoParque = (ImageView) findViewById(R.id.imgLogoParque);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbarLayout.setTitle(getTitle());
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        mLblObservaciones = (TextView) findViewById(R.id.lblObservaciones) ;
        mLblArchivoObservaciones = (TextView) findViewById(R.id.lblArchivoObservaciones) ;
        mLblTitulo = (TextView) findViewById(R.id.lblTitulo) ;
        mApp_bar = (AppBarLayout) findViewById(R.id.app_bar) ;
        mProgressView = (ProgressBar) findViewById(R.id.progressView);
        mLblVerFotos = (TextView) findViewById(R.id.lblVerFotos);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        mLblVerFotos.setOnClickListener(onClickListener);
        mImagen.setOnClickListener(onClickListener);
        mImgLogoParque.setOnClickListener(onClickListener);
        mLblWeather = (TextView) findViewById(R.id.lblWeather);
        mLblWeatherCondition = (TextView) findViewById(R.id.lblWeatherCondition);
        mIcoWeather = (ImageView) findViewById(R.id.icoWeather);
        mLyWeather =  findViewById(R.id.lyWeather);
        mLyWeather.setVisibility(View.GONE);
        mLyWeather.setOnClickListener(onClickListener);
        mBtnComoLlegar = (Button) findViewById(R.id.btnComoLlegar);
        mBtnComoLlegar.setOnClickListener(onClickListener);
        mBtnMapasDelParque = (Button) findViewById(R.id.btnMapasDelParque);
        mBtnMapasDelParque.setOnClickListener(onClickListener);
    }

    private void loadArchivosParque(){
        ArchivosParquePresenter archivosParque = new ArchivosParquePresenter(iViewArchivoParque);
         archivosParque.list(mParque.getIDParque(), Enumerator.TipoArchivoParque.PRINCIPAL_Y_GALERIA);
    }

    IViewArchivoParque iViewArchivoParque = new IViewArchivoParque() {
        @Override
        public void onSuccess(List<ArchivoParque> lstArchivosParque,ArchivoParque imagenPrincipal,ArchivoParque logoParque,  int count) {
           if (count>0){
               mLblVerFotos.setText(count + " FOTOS");
               mLblArchivoObservaciones.setText(imagenPrincipal.getObservacionesArchivo());
               AnimateFirstDisplayListener aniList = new AnimateFirstDisplayListener();
               if(logoParque!=null )
                  ImageLoader.getInstance().displayImage(logoParque.getArchivoParque(), mImgLogoParque, optionsLogo,aniList);
           }
        }
        @Override
        public void onError(ErrorApi error) {
        }
    };

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 750);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    private void loadServiciosParque(){
        showProgress(mProgressView,true);
        ServiciosParquesPresenter serviciosParque = new ServiciosParquesPresenter(iViewServiciosParque);
        serviciosParque.list(mParque.getIDParque());
    }

    IViewServiciosParque iViewServiciosParque =  new IViewServiciosParque() {
        @Override
        public void onSuccess(List<ServicioParque> lista) {
            showProgress(mProgressView,false);
            mLstServiciosParque.clear();
            mLstServiciosParque.addAll(lista);
            mAdaptador.notifyDataSetChanged();

        }

        @Override
        public void onError(ErrorApi error) {
            showProgress(mProgressView,false);
            if (error.getStatusCode() != 404) {
                Snackbar.make(mRecyclerView, error.getMessage(), Snackbar.LENGTH_LONG)
                        //.setActionTextColor(Color.CYAN)
                        .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green))
                        .setAction("REINTENTAR", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loadServiciosParque();
                            }
                        })
                        .show();
            }
        }
    };

    IViewOpenWeather iViewOpenWeather = new IViewOpenWeather() {
        @Override
        public void onSuccessCurrentWeather(CurrentWeather currentWeather) {
            mLyWeather.setVisibility(View.VISIBLE);
            if (currentWeather != null) {
                mLblWeather.setText(String.valueOf(currentWeather.main.getTempRound()) + " " + getResources().getString(R.string.grados));

                if (currentWeather.weather != null && currentWeather.weather.size() > 0) {
                    mLblWeatherCondition.setText(conditionCodes.getName(currentWeather.weather.get(0).id));
                    String urlIcon = Config.OpenWeatherIcon + currentWeather.weather.get(0).icon + ".png";
                    ImageLoader.getInstance().displayImage(urlIcon, mIcoWeather, optionsIcoWeather);
                }
            }
        }

        @Override
        public void onSuccessForecast16Daily(Forecast forecast) {

        }

        @Override
        public void onSuccessForecast5Day3Hour(Forecast forecast) {

        }

        @Override
        public void onError(ErrorApi error) {
            mLyWeather.setVisibility(View.GONE);

            Snackbar.make(mRecyclerView, "Error pronostico de tiempo", Snackbar.LENGTH_LONG)
                    //.setActionTextColor(Color.CYAN)
                    .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green))
                    .setAction("REINTENTAR", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openWeatherPresenter.currentWeather(mParque.getLatitude(), mParque.getLongitude());
                        }
                    })
                    .show();

        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id== R.id.lblVerFotos || id == R.id.imagen || id == R.id.imgLogoParque){
                Intent j = new Intent(getApplicationContext(),ImageViewerActivity.class);
                j.putExtra(Parque.ID_PARQUE,mParque.getIDParque());
                j.putExtra(Enumerator.TipoArchivoParque.TAG, Enumerator.TipoArchivoParque.PRINCIPAL_Y_GALERIA);
                startActivity(j);
            }else if (id == R.id.btnComoLlegar) {
                Intent i = new Intent(DetalleParqueActivity.this, ComoLLegarActivity.class);
                IntentHelper.addObjectForKey(mParque, Parques.TAG);
                startActivity(i);

            }else if (id == R.id.btnMapasDelParque){
                Intent j = new Intent(getApplicationContext(),ImageViewerActivity.class);
                j.putExtra(Parque.ID_PARQUE,mParque.getIDParque());
                j.putExtra(Enumerator.TipoArchivoParque.TAG, Enumerator.TipoArchivoParque.MAP_PHOTO);
                startActivity(j);

            }else if (id == R.id.lyWeather){
                IntentHelper.getInstance().addObjectForKey(mParque, Parques.TAG);
                Intent i = new Intent(getApplicationContext(), WeatherActivity.class);
                startActivity(i);

            }else {
                int position = mRecyclerView.getChildAdapterPosition(v);
                ServicioParque servicio = mLstServiciosParque.get(position);

                Intent i = new Intent(getApplicationContext(), ReservaActivity.class);
                IntentHelper.addObjectForKey(servicio, ServiciosParque.TAG);
                IntentHelper.addObjectForKey(mParque, Parques.TAG);

                startActivity(i);
            }
        }
    };

}
