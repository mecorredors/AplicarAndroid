package car.gov.co.carserviciociudadano.bicicar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.google.zxing.Result;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class EscanearQRActivity extends BaseActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    public static final String ESCANER_DATOS = "escaner_datos";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escanear_qr);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        ViewGroup contentFrame =  findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

      //  mScannerView.stopCamera();
        Intent i = getIntent();
        i.putExtra(ESCANER_DATOS, result.getText());
        setResult(Activity.RESULT_OK,i);
        finish();
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(EscanearQRActivity.this);
            }
        }, 2000);
        */
    }
}
