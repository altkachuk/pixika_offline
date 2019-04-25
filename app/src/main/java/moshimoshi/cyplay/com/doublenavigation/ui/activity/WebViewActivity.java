package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.WebViewActivityConfig;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.MenuBaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.component.PlayRetailViewClient;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;

/**
 * Created by romainlebouc on 19/01/2017.
 */

public abstract class WebViewActivity extends MenuBaseActivity {

    @BindView(R.id.webview)
    WebView webView;

    @BindView(R.id.page_progress)
    ProgressBar pageProgress;


    protected PlayRetailViewClient playRetailViewClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        playRetailViewClient = new PlayRetailViewClient();
        webView.setWebViewClient(playRetailViewClient);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                pageProgress.setProgress(progress);
                if (progress == 100) {
                    pageProgress.setVisibility(View.GONE);
                }
            }
        });
        String webviewtitle = this.getIntent().getStringExtra(IntentConstants.EXTRA_WEBVIEW_TITLE);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(webviewtitle);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        String webviewId = this.getIntent().getStringExtra(IntentConstants.EXTRA_WEBVIEW_ID);
        if (configHelper.getWebViewConfig() != null && webviewId != null) {
            WebViewActivityConfig config = configHelper.getWebViewConfig().getWebViewActivityConfig(webviewId);
            webView.loadUrl(config.getStart_url());
            playRetailViewClient.setWhiteListRegexp(config.getHost_whitelist_regexp());
        }

    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
