package zy.example.com.webview;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import static android.view.KeyEvent.KEYCODE_BACK;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Log.MainActivity";

    private WebView wvView;
    private ProgressBar progressBar;
    private TextView tvProgress;
    private TextView tvState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wvView = findViewById(R.id.wv_web);
        progressBar = findViewById(R.id.progressBar);
        tvProgress = findViewById(R.id.tv_progress);
        tvState = findViewById(R.id.tv_state);
        initBaidu();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initBaidu() {
        wvView.loadUrl("https://www.baidu.com");
        WebSettings webSettings = wvView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        wvView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.i(TAG, "onPageStarted: start");
                tvState.setText("开始加载");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "onPageFinished: endUrl=" + url);
                tvState.setText("结束加载");
            }
        });
        wvView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.i(TAG, "onProgressChanged: newProgress=" + newProgress);
                tvProgress.setText(String.valueOf(newProgress));
                progressBar.setProgress(newProgress);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_BACK && wvView.canGoBack()) {
            Log.i(TAG, "onKeyDown: goBack");
            wvView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (wvView != null) {
            wvView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            wvView.clearHistory();
            ViewGroup parent = (ViewGroup) wvView.getParent();
            parent.removeView(wvView);
            wvView.destroy();
            wvView = null;
        }
        super.onDestroy();
    }
}
