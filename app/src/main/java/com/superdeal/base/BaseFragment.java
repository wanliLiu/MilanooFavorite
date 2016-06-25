package com.superdeal.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.superdeal.R;
import com.superdeal.callback.Callback.RetryListener;
import com.superdeal.data.ResultCode;
import com.superdeal.util.NetworkUtil;
import com.superdeal.widget.TroubleView.ActionView;

public class BaseFragment extends Fragment {

    protected Context ctx;

    @InjectView(R.id.root_container)
    protected View CurrentView;

    //容器
    protected View Viewcontainer;
    protected View Errorview, progressView;
    protected boolean isErrorHappen = false, isFistLoadData = true;

    protected BaseActivity activity;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ctx = activity;

        this.activity = (BaseActivity) activity;
    }

//	@Override
//	public View onCreateView(LayoutInflater inflater,
//			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
////		CurrentView = Viewcontainer.findViewById(R.id.root_container);
//		ButterKnife.inject(this, Viewcontainer);
//		
//		return super.onCreateView(inflater, container, savedInstanceState);
//	}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Viewcontainer = view;
        CurrentView = Viewcontainer.findViewById(R.id.root_container);
        ButterKnife.inject(this, Viewcontainer);

        super.onViewCreated(view, savedInstanceState);
    }

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

    @Override
    public void startActivity(Intent intent) {
        activity.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }
}
