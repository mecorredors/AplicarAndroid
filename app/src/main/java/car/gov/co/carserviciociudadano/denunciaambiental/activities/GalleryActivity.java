package car.gov.co.carserviciociudadano.denunciaambiental.activities;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.R;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.denunciaambiental.adapter.GalleryAdapter;
import car.gov.co.carserviciociudadano.denunciaambiental.model.CustomGallery;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AbsListView.OnScrollListener;


import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

public class GalleryActivity extends BaseActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    GridView gridGallery;
    Handler handler;
    GalleryAdapter adapter;
    ImageView imgNoMedia;
    Button btnGalleryOk;

    Uri outputFileUri;
    public final static int RESULT_CODE_PHOTO=100;
    // LinearLayout llBottomContainer;
    int _MaxItemCount;
    String[] _PathPhotosSelect;
    public  final static String ITEM_COUNT="item_count";
    public  final static String PATH_PHOTOS_SELECT="list_photos_select";
    public final static String ALL_PATH="all_path";
    public final static String ALL_THUMB_IDS="all_thumb_ids";
    private String _TempPath;
    public final static String TEMP_PATH="temp_path";
    public final static String GALLERY_LIST="gallery_list";
    private boolean SaveGallerySelected=true;
    ArrayList<CustomGallery> _GalleryList = new ArrayList<CustomGallery>();
    public static final int MAX_PHOTOS = 10;

    public  static final  int MY_PERMISSION_EXTERNAL_STORAGE = 11;
    public  final static int MY_PERMISSIONS_CAMERA = 12;
    public int mMaxPhotos;
    public final static String MAX_PHOTOS_EXTRA = "max_photos";
    public final static String ONLY_PHOTOS = "only_photos";
    private boolean mOnlyPhotos = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            int itemCount = extras.getInt(ITEM_COUNT);
            mMaxPhotos = extras.getInt(MAX_PHOTOS_EXTRA , MAX_PHOTOS);

            _MaxItemCount = mMaxPhotos - itemCount;

            _PathPhotosSelect = extras.getStringArray(PATH_PHOTOS_SELECT);
            mOnlyPhotos = extras.getBoolean(ONLY_PHOTOS, false);
            extras.remove(PATH_PHOTOS_SELECT);
        }

        if (savedInstanceState != null) {
            _TempPath = savedInstanceState.getString(TEMP_PATH);

            try {
                if (savedInstanceState.getSerializable(GALLERY_LIST) != null)
                    _GalleryList = (ArrayList<CustomGallery>) savedInstanceState.getSerializable(GALLERY_LIST);
            }catch (BadParcelableException ex){
                Crashlytics.logException(ex);
            }
            savedInstanceState.remove(TEMP_PATH);
            savedInstanceState.remove(GALLERY_LIST);

        }

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED  ) {

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE  },  MY_PERMISSION_EXTERNAL_STORAGE );
        }else {
            init();
        }

        if (mOnlyPhotos){
            TakePhoto();
        }

        if (BuildConfig.DEBUG == false) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundleAnalitic = new Bundle();
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_ID, "Gallery denuncia");
            bundleAnalitic.putString(FirebaseAnalytics.Param.ITEM_NAME, "Gallery denuncia");
            bundleAnalitic.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Enumerator.ContentTypeAnalitic.DENUNCIA_AMBIENTAL);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleAnalitic);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        SaveGallerySelected=true;
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        setResultPhotos();
        super.onBackPressed();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
//
//        if(id== android.R.id.home){
//            super.onBackPressed();
//        }

        if (id == R.id.item_listo) {
            setResultPhotos();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }




    private void init() {

        handler = new Handler();
        gridGallery = (GridView) findViewById(R.id.gridGallery);
        adapter = new GalleryAdapter(getApplicationContext());

        gridGallery.setOnItemClickListener(mItemMulClickListener);
        adapter.setMultiplePick(true);

        gridGallery.setAdapter(adapter);
        imgNoMedia = (ImageView) findViewById(R.id.imgNoMedia);

        btnGalleryOk = (Button) findViewById(R.id.btnGalleryOk);
        btnGalleryOk.setOnClickListener(mOkClickListener);

        gridGallery.setOnScrollListener(new OnScrollListener() {

            private int lastFirstItem = 0;
            private long timestamp = System.currentTimeMillis();

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    adapter.shouldRequestThumb = true;
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                float dt = System.currentTimeMillis() - timestamp;
                if (firstVisibleItem != lastFirstItem) {
                    double speed = 1 / dt * 1000;
                    lastFirstItem = firstVisibleItem;
                    timestamp = System.currentTimeMillis();

                    // Limitarlo si vamos a m???s de una p???gina por segundo...
                    adapter.shouldRequestThumb = speed < visibleItemCount;
                }
            }
        });

        if(_GalleryList == null || _GalleryList.size()==0)
            getGalleryPhotos();

        adapter.addAll(_GalleryList);
        checkImageStatus();
    }

    private void checkImageStatus() {
        if (adapter.isEmpty()) {
            imgNoMedia.setVisibility(View.VISIBLE);
        } else {
            imgNoMedia.setVisibility(View.GONE);
        }
    }


    private void  setResultPhotos(){
        if (adapter != null) {
            ArrayList<CustomGallery> selected = adapter.getSelected();

            String[] allPath = new String[selected.size()];
            int[] allThumbIds = new int[selected.size()];

            for (int i = 0; i < allPath.length; i++) {
                allPath[i] = selected.get(i).sdcardPath;
                allThumbIds[i] = selected.get(i).idBitmap;
            }

            Intent data = new Intent();
            data.putExtra(ALL_PATH, allPath);
            data.putExtra(ALL_THUMB_IDS, allThumbIds);

            setResult(RESULT_OK, data);

            finish();
        }
    }

    View.OnClickListener mOkClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            setResultPhotos();
        }
    };
    AdapterView.OnItemClickListener mItemMulClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> l, View v, int position, long id) {

            if(position==0){
                if( adapter.getCountSelected() <= _MaxItemCount)
                    TakePhoto();
                else
                    mostrarMensaje(getResources().getString(R.string.max_photos));
            }else{
                if(!adapter.changeSelection(v, position,_MaxItemCount))
                    mostrarMensaje(getResources().getString(R.string.max_photos));

            }

        }
    };


    private void TakePhoto() {

        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_CAMERA);

        } else {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                if (CreateFile()) {
                    SaveGallerySelected = false;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(takePictureIntent, RESULT_CODE_PHOTO);
                }
            }
        }
    }

    private boolean CreateFile(){
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Car_a_mi_servicio/";
        File newdir = new File(dir);
        newdir.mkdirs();


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        String file = dir+imageFileName+".jpg";


        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {
            Log.e("galleryActivity", e.toString());
            return false;

        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            outputFileUri = FileProvider.getUriForFile(AppCar.getContext(), BuildConfig.APPLICATION_ID + ".provider",newfile);
        else
            outputFileUri = Uri.fromFile(newfile);

       // _TempPath=outputFileUri.getPath();
        _TempPath=file;
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == RESULT_CODE_PHOTO) {
            if (resultCode == RESULT_OK) {
                setCustomGallery();

            } else if (resultCode == RESULT_CANCELED) {

                File file = new File(_TempPath);
                file.delete();
                if (mOnlyPhotos){
                    finish();
                }
            } else {
                // Image capture failed, advise user
            }
        }
    }

    private void setCustomGallery(){
        final CustomGallery cg= new CustomGallery();
        cg.isSeleted=true;
        cg.isButton=false;
        cg.sdcardPath=_TempPath;

        int idBitmap = getIdThumnail();
        if (idBitmap > 0) {
            cg.idBitmap = idBitmap;
            if (adapter != null) {
                adapter.setCustomGallery(cg, 1);
            }
        }else {// modo alternativo para obtener el id del bitmap, solo si falla el anterior
            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{_TempPath}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            int id = getIdThumnailSleep();
                            cg.idBitmap = id;
                            if (adapter != null && uri != null) {
                                adapter.setCustomGallery(cg, 1);
                            }
                        }
                    });
        }

        setResultPhotos();
    }

    /**
     * Se realiza varios intentos de obtener id ya que la foto capturada puede tardas unos milesegundos hasta que este disponible en el query.
     * SendBroadCast() es asyncrono y no sabemos cuando termine su ejecucion.
     * @return id thumnail
     */
    private int getIdThumnail(){

        SendBroadCast(_TempPath);

        for(int i=0;i< 10;i++){
            int id=	getIdThumnailSleep();
            if(id>0) return id;
        }
        return 0;
    }

    private int getIdThumnailSleep(){

        SystemClock.sleep(500);//esperar medio segundo hasta volver a intentar obtener id de imagen en miniatura
        int imageId=0;
        try{

            String[] projection = new String[] { MediaStore.MediaColumns._ID };
            String selection = MediaStore.MediaColumns.DATA + "=?";
            String[] selectionArgs = new String[] { _TempPath };
            Cursor cursor = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst())
            {
                imageId = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));

            }else{
                return 0;
            }
            cursor.close();
        }
        catch(Exception ex){Log.e("GalleryActivity",ex.toString());

        }
        return imageId;
    }

    /**
     *
     * @param path ruta de la foto capturada
     *
     * Metodo asyncrono
     * Permite actualizar la galeria de fotos de android, crea sus miniaturas y las agrega a media.
     */
    private void  SendBroadCast(String path){

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

    }


    AdapterView.OnItemClickListener mItemSingleClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> l, View v, int position, long id) {
            CustomGallery item = adapter.getItem(position);
            Intent data = new Intent().putExtra("single_path", item.sdcardPath);
            setResult(RESULT_OK, data);
            finish();
        }
    };



    private ArrayList<CustomGallery> getGalleryPhotos() {
        if(_GalleryList==null) _GalleryList= new ArrayList<CustomGallery>();

        _GalleryList.clear();
        try {
            final String[] columns = { MediaColumns.DATA, BaseColumns._ID, BaseColumns._ID };
            final String orderBy = BaseColumns._ID;

            Cursor imagecursor =  getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,	null, null, orderBy);

            if (imagecursor != null && imagecursor.getCount() > 0) {

                while (imagecursor.moveToNext()) {
                    CustomGallery item = new CustomGallery();
                    int image_column_index = imagecursor.getColumnIndex(BaseColumns._ID);
                    int id = imagecursor.getInt(image_column_index);

//					  Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
//			                    getApplicationContext().getContentResolver(), id,
//			                    MediaStore.Images.Thumbnails.MICRO_KIND, null);


                    int dataColumnIndex = imagecursor.getColumnIndex(MediaColumns.DATA);

                    item.sdcardPath = imagecursor.getString(dataColumnIndex);
                    item.idBitmap=id;

                    if(!Exist(item.sdcardPath)){
                        _GalleryList.add(item);
                    }
                }

            }
            imagecursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        CustomGallery cg= new CustomGallery();
        cg.isSeleted=false;
        cg.isButton=true;
        _GalleryList.add(cg);
        Collections.reverse(_GalleryList);

        return _GalleryList;
    }

    private boolean Exist(String path){
        int size= _PathPhotosSelect.length ;
        for(int i=0;i<size;i++){
            if(_PathPhotosSelect[i] != null && _PathPhotosSelect[i].equals(path))
                return true;
        }
        return false;
    }

    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(TEMP_PATH,_TempPath ); //cuando se cambia de orientacion landscape al tomar foto, se debe guardar el path.

        if( _GalleryList!=null )
            outState.putSerializable(GALLERY_LIST,_GalleryList);
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_EXTERNAL_STORAGE: {
                if (grantResults.length > 0   && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    mostrarMensaje("Permiso denegado para guardar archivos");
                }
                return;
            }
            case MY_PERMISSIONS_CAMERA: {
                if (grantResults.length > 0   && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TakePhoto();
                } else {
                    mostrarMensaje("Permiso denegado para tomar fotos");
                }
                return;
            }
        }
    }
}
