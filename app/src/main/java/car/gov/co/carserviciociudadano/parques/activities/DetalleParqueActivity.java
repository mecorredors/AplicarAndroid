package car.gov.co.carserviciociudadano.parques.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.List;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.parques.adapter.ServiciosParqueAdapter;
import car.gov.co.carserviciociudadano.parques.businessrules.BRServiciosParques;
import car.gov.co.carserviciociudadano.parques.dataaccess.ArchivosParque;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.dataaccess.ServiciosParque;
import car.gov.co.carserviciociudadano.parques.interfaces.IArchivoParque;
import car.gov.co.carserviciociudadano.parques.interfaces.IServicioParque;
import car.gov.co.carserviciociudadano.parques.model.ArchivoParque;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;
import car.gov.co.carserviciociudadano.parques.model.ServicioParque;

public class DetalleParqueActivity extends BaseActivity {


    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .showImageOnLoading(R.color.gray)
            .showImageForEmptyUri(R.drawable.sin_foto)
            .showImageOnFail(R.drawable.sin_foto)
            .build();

//    private int mIdParque;
//    private String mNombreParque;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_parque);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        findViewsById();

        Bundle bundle = getIntent().getExtras();
        mParque = new Parque();
        if(bundle != null){
            mParque.setIDParque(bundle.getInt(Parque.ID_PARQUE));
            ImageLoader.getInstance().displayImage(bundle.getString(Parque.URL_ARCHIVO_PARQUE), mImagen, options);
            mParque.setNombreParque(bundle.getString(Parque.NOMBRE_PARQUE));
            mParque.setDetalleCuenta(bundle.getString(Parque.DETALLE_CUENTA));
            mParque.setPoliticasParque(bundle.getString(Parque.POLITICAS_PARQUE));
            mCollapsingToolbarLayout.setTitle(mParque.getNombreParque());
            mLblObservaciones.setText(bundle.getString(Parque.OBSERVACIONES_PARQUE));

        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLstServiciosParque = new ArrayList<>();
        mAdaptador = new ServiciosParqueAdapter(mLstServiciosParque);
        mAdaptador.setOnClickListener(onClickListener);

        mRecyclerView.setAdapter(mAdaptador);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        loadArchivosParque();
        loadServiciosParque();

      //  mProgressView = (ProgressBar) view.findViewById(R.id.progressView);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(ArchivosParque.TAG);
        AppCar.VolleyQueue().cancelAll(ServiciosParque.TAG);
        super.onPause();

    }
    private void findViewsById(){
        mImagen = (ImageView) findViewById(R.id.imagen);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbarLayout.setTitle(getTitle());
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        mLblObservaciones = (TextView) findViewById(R.id.lblObservaciones) ;
        mLblArchivoObservaciones = (TextView) findViewById(R.id.lblArchivoObservaciones) ;
        mLblTitulo = (TextView) findViewById(R.id.lblTitulo) ;
        mApp_bar = (AppBarLayout) findViewById(R.id.app_bar) ;
        mProgressView = (ProgressBar) findViewById(R.id.progressView);
    }

    private void loadArchivosParque(){
        ArchivosParque archivosParque = new ArchivosParque();
        archivosParque.list(new IArchivoParque() {
            @Override
            public void onSuccess(List<ArchivoParque> lstArchivosParque) {
                if(lstArchivosParque.size()>0)
                mLblArchivoObservaciones.setText(lstArchivosParque.get(0).getObservacionesArchivo());
            }

            @Override
            public void onError(ErrorApi error) {

            }
        },mParque.getIDParque());
    }

    private void loadServiciosParque(){
        showProgress(mProgressView,true);
        BRServiciosParques serviciosParque = new BRServiciosParques();
        serviciosParque.list(new IServicioParque() {
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
                Snackbar.make(mRecyclerView, error.getMessage(), Snackbar.LENGTH_LONG)
                        //.setActionTextColor(Color.CYAN)
                        .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green) )
                        .setAction("REINTENTAR", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loadServiciosParque();
                            }
                        })
                        .show();
            }
        },mParque.getIDParque());
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = mRecyclerView.getChildAdapterPosition(v);
            ServicioParque servicio = mLstServiciosParque.get(position);

            Intent i = new Intent(getApplicationContext(), ReservaActivity.class);
            i.putExtra(Parque.ID_PARQUE,mParque.getIDParque());
            i.putExtra(Parque.NOMBRE_PARQUE,mParque.getNombreParque());
            i.putExtra(Parque.DETALLE_CUENTA,mParque.getDetalleCuenta());
            i.putExtra(Parque.POLITICAS_PARQUE,mParque.getPoliticasParque());
            i.putExtra(ServicioParque.ID_SERVICIOS_PARQUE,servicio.getIDServiciosParque());
            i.putExtra(ServicioParque.NOMBRE_SERVICIO,servicio.getNombreServicio());
            i.putExtra(ServicioParque.PRECIO_SERVICIO,servicio.getPrecioServicio());
            i.putExtra(ServicioParque.IMPUESTO_SERVICIO,servicio.getImpuestoServicio());
            i.putExtra(ServicioParque.DESCUENTO_SERVICIO,servicio.getDescuentoServicio());
            i.putExtra(ServicioParque.ACTIVO_SERVICIO,servicio.getActivoServicio());
            i.putExtra(ServicioParque.PRECIO_CAR,servicio.getPrecioCar());


            startActivity(i);
        }
    };

}
