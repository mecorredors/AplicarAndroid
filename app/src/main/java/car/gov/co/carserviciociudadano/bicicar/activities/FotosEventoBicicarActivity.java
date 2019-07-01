package car.gov.co.carserviciociudadano.bicicar.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.bicicar.adapter.FotosBicicarAdapter;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.ArchivosAdjuntos;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Eventos;
import car.gov.co.carserviciociudadano.bicicar.model.ArchivoAdjunto;
import car.gov.co.carserviciociudadano.bicicar.model.Evento;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.denunciaambiental.activities.GalleryActivity;


public class FotosEventoBicicarActivity extends BaseActivity {
    @BindView(R.id.lyInicial)  LinearLayout lyInicial;
    @BindView(R.id.lyDenuncia)  LinearLayout lyDenuncia;
    @BindView(R.id.gridGallery)    GridView gridView;

    private FirebaseAnalytics mFirebaseAnalytics;
    private static final String LOGTAG = "Fotos evento bicicar";
    private static final int PETICION_GALLERY = 102;

    FotosBicicarAdapter mAdapter;
    List<ArchivoAdjunto> mlstFotos = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private Evento mEvento;
    public static  final int MAX_PHOTOS = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotos_evento_bicicar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);


        Bundle b = getIntent().getExtras();
        if (b != null){
            int idEvento = b.getInt(Evento.ID_EVENTO,0);
            mEvento = new Eventos().read(idEvento);
        }

        mlstFotos.addAll(new ArchivosAdjuntos().list(mEvento.IDEvento));
        mAdapter = new FotosBicicarAdapter(this, new ArrayList<>(mlstFotos));
        mAdapter.SetOnItemClickListener(listenerGallerySelected);
        gridView.setAdapter(mAdapter);


        setNumPhotosSelect();


        if (BuildConfig.DEBUG == false) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundleAnalitic = new Bundle();
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_ID, "Fotos evento bicicar");
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_NAME, "Fotos evento bicicar");
            bundleAnalitic.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Enumerator.ContentTypeAnalitic.BICICAR);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleAnalitic);
        }
    }



    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mProgressDialog != null) mProgressDialog.dismiss();

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if (mProgressDialog != null) mProgressDialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        setResult(RESULT_OK, getIntent());

        if(id== android.R.id.home){
            if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                getSupportFragmentManager().popBackStack();
            else
                super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        setResult(RESULT_OK, getIntent());
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @OnClick(R.id.btnAddFoto) void onAddFoto() {
        openGalleryPhotos();
    }

    @OnClick(R.id.lyAgregarFotos) void onAgregarFotos() {
        openGalleryPhotos();
    }
    @OnClick(R.id.imgAgregarFotos) void onImgAgregarFotos() {
        openGalleryPhotos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == PETICION_GALLERY){
                String[] all_path = data.getStringArrayExtra(GalleryActivity.ALL_PATH);
                int[] all_ids = data.getIntArrayExtra(GalleryActivity.ALL_THUMB_IDS);

                mAdapter.AddPhotos(all_path, mEvento.IDEvento);
                guardarFotos();
                setNumPhotosSelect();
                actualizarEstadoEvento();
            }
        }
    }

    private void guardarFotos(){
        if (mAdapter.getItemCount() > 1) {

            for (ArchivoAdjunto item : mAdapter.getPhotos()) {
                if (item.Type == Enumerator.TipoFoto.FOTO && item.Id == 0){
                    new ArchivosAdjuntos().insert(item);
                }
            }
        }
    }

    private void actualizarEstadoEvento(){
        if (mEvento != null){
            mEvento.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR;
            new Eventos().update(mEvento);
        }
    }

    private void openGalleryPhotos(){
        int size = mAdapter.getItemCount();
        if (size <= MAX_PHOTOS) {

            Intent i = new Intent(this, GalleryActivity.class);
            i.putExtra(GalleryActivity.ITEM_COUNT, size);
            i.putExtra(GalleryActivity.MAX_PHOTOS_EXTRA, MAX_PHOTOS);

            String[] datos = new String[size];
            for (int t = 0; t < size; t++)
                datos[t] =  mAdapter.getPhotos().get(t).Path;

            i.putExtra(GalleryActivity.PATH_PHOTOS_SELECT, datos);
            startActivityForResult(i, PETICION_GALLERY);
        } else {
            mostrarMensaje(getResources().getString(R.string.max_photos_bicicar));
        }
    }

    FotosBicicarAdapter.OnItemClickListener listenerGallerySelected = new FotosBicicarAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            ArchivoAdjunto foto = mAdapter.getPhotos().get(position);
            if (foto.Type == Enumerator.TipoFoto.BOTON_AGREGAR_MAS) {
                openGalleryPhotos();
            }
        }

        @Override
        public void onCloseClick(View view, int position) {

            if (position > 0) {
                ArchivoAdjunto archivoAdjunto = mAdapter.getPhotos().get(position);
                if (archivoAdjunto != null && archivoAdjunto.Id > 0) {
                    new ArchivosAdjuntos().delete(archivoAdjunto.Id);
                }
            }
            mAdapter.RemoveItem(position);
            setNumPhotosSelect();

        }
    };

    private void setNumPhotosSelect() {
        if (mAdapter.getItemCount() <= 1) {
            lyInicial.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        }
        else {
            lyInicial.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }
    }

    private void llenarDenuncia(){
        if (mAdapter.getItemCount() > 1) {
            List<ArchivoAdjunto> lstFotos = new ArrayList<>(mAdapter.getPhotos());
            lstFotos.remove(0);

        }

    }

}
