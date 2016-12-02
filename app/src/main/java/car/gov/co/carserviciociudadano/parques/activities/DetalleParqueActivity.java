package car.gov.co.carserviciociudadano.parques.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.parques.dataaccess.ArchivosParque;
import car.gov.co.carserviciociudadano.parques.interfaces.IArchivoParque;
import car.gov.co.carserviciociudadano.parques.model.ArchivoParque;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;

public class DetalleParqueActivity extends BaseActivity {


    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .showImageOnLoading(R.color.gray)
            .showImageForEmptyUri(R.drawable.sin_foto)
            .showImageOnFail(R.drawable.sin_foto)
            .build();

    private int mIdParque;
    ImageView mImagen;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    TextView mLblObservaciones;
    TextView mLblArchivoObservaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_parque);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewsById();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mIdParque = bundle.getInt(Parque.ID_PARQUE);
            ImageLoader.getInstance().displayImage(bundle.getString(Parque.URL_ARCHIVO_PARQUE), mImagen, options);
            mCollapsingToolbarLayout.setTitle(bundle.getString(Parque.NOMBRE_PARQUE));
            mLblObservaciones.setText(bundle.getString(Parque.OBSERVACIONES_PARQUE));
        }
        loadArchivosParque();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void findViewsById(){
        mImagen = (ImageView) findViewById(R.id.imagen);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbarLayout.setTitle(getTitle());
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        mLblObservaciones = (TextView) findViewById(R.id.lblObservaciones) ;
        mLblArchivoObservaciones = (TextView) findViewById(R.id.lblArchivoObservaciones) ;
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
        },mIdParque);
    }

}
