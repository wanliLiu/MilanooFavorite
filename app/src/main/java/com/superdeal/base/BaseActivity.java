package com.superdeal.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import butterknife.ButterKnife;

import com.superdeal.R;
import com.superdeal.callback.Callback.RetryListener;
import com.superdeal.data.ResultCode;
import com.superdeal.util.NetworkUtil;
import com.superdeal.widget.LoadingDialog;
import com.superdeal.widget.TroubleView.ActionView;

public abstract class BaseActivity extends ActionBarActivity {

    protected static final int RESULT_action = 32;

    /**
     * context
     */
    protected Context ctx;
    protected View CurrentView;

    //容器
    protected View Viewcontainer;
    protected View Errorview, progressView;
    protected boolean isErrorHappen = false, isFistLoadData = true;

    /**
     * 加载进度条
     */
    private LoadingDialog loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ctx = this;

        getLayoutView();
        Viewcontainer = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        CurrentView = findViewById(R.id.root_container);
        ButterKnife.inject(this);

        initView();
        initListener();
        initData();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.quiet_fixedly, R.anim.scale_out);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.scale_in, R.anim.quiet_fixedly);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.scale_in, R.anim.quiet_fixedly);
    }

    /**
     * @param title
     */
    protected void setTitle(String title) {

        getSupportActionBar().setTitle(title);
    }

    protected abstract void getLayoutView();

    /**
     * 初始化界面
     */
    protected abstract void initView();

    /**
     * 初始化事件监听
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 根据出错类型自动显示相应的提示界面
     *
     * @param code
     * @param layout
     * @param id
     * @param listener
     */
    protected void ErrorHappen(ResultCode code, RetryListener listener) {

        if (code == ResultCode.NETWORK_TROBLE) {
            ErrorHappen(R.layout.network_trouble, R.id.btnRetry, listener);
        } else {
            ErrorHappen(R.layout.error_trouble, R.id.btnRetry, listener);
        }
    }

    /**
     * 网络出错的时候需要用到
     *
     * @param layout
     * @param id
     * @param listener
     */
    protected void ErrorHappen(int layout, int id, RetryListener listener) {

        dissFistShowProgress();

        CurrentView.setVisibility(View.GONE);

        ActionView error = new ActionView(ctx);
        error.addErrorView(layout, id, listener);

        Errorview = error;
        //保障是FrameLayout的实例
        if (Errorview instanceof FrameLayout) {
            ((FrameLayout) Viewcontainer).addView(Errorview);
        }

        isErrorHappen = true;

    }

    /**
     * 错误消失
     */
    protected void ErrorDeal() {

        if (isErrorHappen) {
            if (Errorview != null) {
                if (Errorview instanceof FrameLayout) {
                    ((FrameLayout) Viewcontainer).removeView(Errorview);
                    Errorview = null;
                }
            }

            isErrorHappen = false;

            CurrentView.setVisibility(View.VISIBLE);
        }
    }

    protected void FistShowProgress() {
        FistShowProgress(R.layout.progress_view);
    }

    /**
     * 第一次加载的时候出现加载动画框
     */
    protected void FistShowProgress(int layout) {
        ErrorDeal();

        if (isFistLoadData) {
            CurrentView.setVisibility(View.GONE);

            ActionView progress = new ActionView(ctx);
            progress.addProgressView(layout);

            progressView = progress;
            //保障是FrameLayout的实例
            if (progressView instanceof FrameLayout) {
                ((FrameLayout) Viewcontainer).addView(progressView);
            }
        }

    }

    /**
     *
     */
    protected void dissFistShowProgress() {
        if (isFistLoadData) {
            isFistLoadData = false;
            if (progressView != null) {
                if (progressView instanceof FrameLayout) {
                    ((FrameLayout) Viewcontainer).removeView(progressView);
                    progressView = null;
                }
            }

            CurrentView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取网络数据成功之后做点什么
     */
    protected void doSomeThing() {

        if (NetworkUtil.isNetworkAvailable(ctx)) {
            dissFistShowProgress();
        }
        ErrorDeal();
    }

    /**
     * 显示Toast
     *
     * @param text 文本内容
     */
    protected void MyToast(String text) {
        Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示Toast
     *
     * @param resId string资源id
     */
    protected void MyToast(int resId) {
        Toast.makeText(ctx, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示加载进度条
     */
    public void showProgress(boolean canCancle) {

        try {
            if (!isFinishing()) {
                if (loadDialog == null) {
                    loadDialog = new LoadingDialog(ctx);
                    if (!canCancle) {
                        loadDialog.setCanceledOnTouchOutside(false);
                        loadDialog.setCancelable(false);
                    }
                }
                if (!loadDialog.isShowing()) {
                    loadDialog.show();
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 停止显示加载进度条
     */
    public void dismissProgress() {

        try {
            if (!isFinishing()) {
                if (loadDialog != null && loadDialog.isShowing()) {
                    loadDialog.dismiss();
                    loadDialog = null;
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
