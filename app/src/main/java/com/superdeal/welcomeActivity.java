package com.superdeal;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.superdeal.base.BaseActivity;
import com.superdeal.base.Constants;
import com.superdeal.bean.WebsiteTagBean;
import com.superdeal.data.ApiCallBack;
import com.superdeal.data.ApiHelper;
import com.superdeal.data.ApiParams;
import com.superdeal.data.Result;

public class welcomeActivity extends BaseActivity {

    private List<WebsiteTagBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void getLayoutView() {
        setContentView(R.layout.welcome_bg);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        getCategoryList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                timer.cancel();
                // 首页广告最多两个
                Intent intent = new Intent(ctx, MainActivity.class);
                if (list == null || list.size() == 0) {
                    intent.putExtra("listData", "");
                } else {
                    intent.putExtra("listData", JSON.toJSONString(list));
                }
                startActivity(intent);
                finish();
            }
        }, Constants.DelayTime);
    }

    /**
     * 获取分类数据内容
     */
    private void getCategoryList() {
        ApiParams params = new ApiParams();
        params.put("module", "website");
        params.put("a", "get_websitetag_list");
        params.put("tid", 1);
        params.put("langcode", Locale.getDefault().getLanguage());

        ApiHelper.get(ctx, "", params, new ApiCallBack() {

            @Override
            public void receive(Result result) {

                if (result.isSuccess()) {

                    JSONObject json = JSON.parseObject(result.getObj().toString());
                    if (json.containsKey("data")) {
                        list = JSON.parseArray(json.getString("data"), WebsiteTagBean.class);
                    }
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
}
