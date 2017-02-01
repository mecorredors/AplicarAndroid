package car.gov.co.carserviciociudadano.parques.activities;

import android.os.Bundle;

import car.gov.co.carserviciociudadano.R;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class PagoElectronicoActivity extends BaseActivity {
    private ProgressBar progress;
    WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_electronico);

        ActionBar bar =  getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Long mAdvertId;
        String mAdvertisingType;

        myWebView = (WebView) findViewById(R.id.webView);

        progress = (ProgressBar) findViewById(R.id.progressBar);

        myWebView.setWebViewClient(new myWebClient());
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.getSettings().setJavaScriptEnabled(true);

        myWebView.loadUrl("https://www.pagosvirtualesavvillas.com.co/personal/pagos/4363");

    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            progress.setVisibility(View.GONE);
        }
    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume(){
        super.onResume();
    }
    public void setValue(int progress) {
        this.progress.setProgress(progress);
    }
}