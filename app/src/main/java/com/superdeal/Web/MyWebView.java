package com.superdeal.Web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.superdeal.R;
import com.superdeal.base.BaseActivity;
import com.superdeal.base.Constants;
import com.superdeal.data.ApiCallBack;
import com.superdeal.data.ApiHelper;
import com.superdeal.data.ApiParams;
import com.superdeal.data.Result;
import com.superdeal.login.UseFacebookLoginActitiy;
import com.superdeal.util.UserUtil;

import java.net.URLEncoder;
import java.util.Locale;

import butterknife.InjectView;

public class MyWebView extends BaseActivity {

    @InjectView(R.id.webViewDisplay)
    WebView myWebView;

    @InjectView(R.id.loadprogress)
    ProgressBar loading;

//	private DynamicShareActionProvider mShareActionProvider;

    private String CurrentUrl = "";
    private String CurrentTitle = "";

    private boolean isLoadingOkay = false;
    private boolean haveFavorite = false;

    @Override
    protected void getLayoutView() {
        setContentView(R.layout.webview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initView() {

        // 设置支持javascript
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //清除缓存
        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(ctx);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        myWebView.clearCache(true);
        myWebView.clearHistory();
    }

    @Override
    protected void initListener() {

        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onReceivedTitle(WebView view, String title) {
                CurrentTitle = title;
            }

            @Override
            public void onProgressChanged(WebView view, final int newProgress) {
                loading.setProgress(newProgress > 0 ? newProgress : 5);
                if (newProgress == 100)
                    loading.setVisibility(View.GONE);
                else {
                    loading.postInvalidate();
                    loading.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {
//        			haveFavorite = FavoriteUtil.isHaveTheFavorite(ctx, getbean());
                    isLoadingOkay = true;
                    invalidateOptionsMenu();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                try {
                    CurrentUrl = url;
//	                    loading.setVisibility(View.VISIBLE);
                    isLoadingOkay = false;
                    if (haveFavorite) {
                        haveFavorite = false;
                    }
                    invalidateOptionsMenu();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void initData() {

        setTitle(getIntent().getStringExtra(Constants.Intent_Title));

        CurrentUrl = getIntent().getStringExtra(Constants.Intent_URL);

        myWebView.loadUrl(CurrentUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
//        if (isLoadingOkay) {
//        	menu.findItem(R.id.action_favorite).setEnabled(true);
//        	menu.findItem(R.id.action_share).setEnabled(true);
//		}else {
//        	menu.findItem(R.id.action_favorite).setEnabled(false);
//        	menu.findItem(R.id.action_share).setEnabled(false);
//		}
//        
        if (haveFavorite) {
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorite_white_36dp);
        } else {
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorite_outline_white_36dp);
        }
//		mShareActionProvider = (DynamicShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_share));
//		mShareActionProvider.setShareDataType("text/plain");
//		mShareActionProvider.setOnShareIntentUpdateListener(new DynamicShareActionProvider.OnShareIntentUpdateListener() {
//
//            @Override
//            public Bundle onShareIntentExtrasUpdate() {
////                Bundle extras = new Bundle();
////                extras.putString(Intent.EXTRA_TEXT, CurrentUrl);
//                return null;
//            }
//
//			@Override
//			public String getShareContent() {
//				return CurrentUrl;
//			}
//        });
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (myWebView.canGoBack()) {
                myWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (myWebView.canGoBack()) {
                    myWebView.goBack();
                    return true;
                }
                break;
            case R.id.action_home:
                onBackPressed();
                break;
//		case R.id.action_share:
//			{
//				Intent shareIntent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
//				shareIntent.setType("text/plain"); // 分享发送的数据类型
//				shareIntent.putExtra(Intent.EXTRA_TEXT, CurrentUrl); // 分享的内容
//				startActivity(shareIntent);// 目标应用选择对话框的标题
//			}
//			break;
            case R.id.action_favorite: {
                if (UserUtil.getLoginStaus(ctx)) {
                    addFavorite();
                } else {
                    startActivityForResult(new Intent(ctx, UseFacebookLoginActitiy.class), RESULT_action);
                }
            }
            break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 添加收藏
     */
    private void addFavorite() {
        showProgress(false);

        ApiParams params = new ApiParams();
        params.put("module", "website");
        params.put("a", "save_collect");
        params.put("title", URLEncoder.encode(CurrentTitle));
        params.put("uid", UserUtil.getValue(ctx, UserUtil.UserId));
        params.put("wid", getIntent().getIntExtra(Constants.Intent_ID, 0));
        params.put("url", URLEncoder.encode(CurrentUrl));
        params.put("langcode", Locale.getDefault().getLanguage());

        ApiHelper.get(ctx, "", params, new ApiCallBack() {

            @Override
            public void receive(Result result) {

                dismissProgress();
                if (result.isSuccess()) {
                    haveFavorite = true;
                    invalidateOptionsMenu();
                    MyToast(R.string.loading_success);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);

        if (arg1 == RESULT_OK) {

            if (arg0 == RESULT_action) {
                addFavorite();
            }
        }

    }
}
