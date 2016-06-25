package com.superdeal.favorite;

import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.MaterialsDesign.Ldialogs.CustomDialog;
import com.MaterialsDesign.Ldialogs.CustomDialog.ClickListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.superdeal.R;
import com.superdeal.Web.MyWebView;
import com.superdeal.adapter.FavoriteAdapter;
import com.superdeal.base.BaseActivity;
import com.superdeal.base.Constants;
import com.superdeal.bean.GetFavoriteBean;
import com.superdeal.callback.Callback.RetryListener;
import com.superdeal.data.ApiCallBack;
import com.superdeal.data.ApiHelper;
import com.superdeal.data.ApiParams;
import com.superdeal.data.Result;
import com.superdeal.util.UserUtil;

public class DisplayFavoriteActivity extends BaseActivity {

    private List<GetFavoriteBean> favorite;

    private FavoriteAdapter adapter;


    @Override
    protected void getLayoutView() {
        setContentView(R.layout.favorite_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

        ((ListView) CurrentView).setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                ShowDeleteDialog(position);

                return true;
            }

        });

        ((ListView) CurrentView).setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(ctx, MyWebView.class);
                intent.putExtra(Constants.Intent_Title, adapter.getList().get(position).getName());
                intent.putExtra(Constants.Intent_URL, adapter.getList().get(position).getUrl());
                intent.putExtra(Constants.Intent_ID, adapter.getList().get(position).getWid());
                startActivity(intent);
            }
        });
    }

    /**
     * @param mposition
     */
    private void deleteFavorite(final int mposition) {
        showProgress(false);

        ApiParams params = new ApiParams();
        params.put("module", "website");
        params.put("a", "del_collect");
        params.put("id", adapter.getList().get(mposition).getId());
        params.put("uid", UserUtil.getValue(ctx, UserUtil.UserId));
        params.put("langcode", Locale.getDefault().getLanguage());

        ApiHelper.get(ctx, "", params, new ApiCallBack() {

            @Override
            public void receive(Result result) {
                dismissProgress();

                if (result.isSuccess()) {
                    adapter.remove(mposition);
                }
            }
        });
    }

    /**
     * 显示删除对话框
     */
    private void ShowDeleteDialog(final int mposition) {
        CustomDialog.Builder builder = new CustomDialog.Builder(ctx, R.string.delete_title, R.string.Accept);
        builder.content(getString(R.string.delete_content, adapter.getList().get(mposition).getTitle()));
        builder.positiveColor(getResources().getColor(R.color.color_blue));
        builder.negativeText(R.string.Cancel);
        builder.darkTheme(false);

        CustomDialog customDialog = builder.build();
        customDialog.setClickListener(new ClickListener() {

            @Override
            public void onConfirmClick() {
                deleteFavorite(mposition);
            }

            @Override
            public void onCancelClick() {

            }
        });

        customDialog.show();
    }

    @Override
    protected void initData() {

        setTitle(getString(R.string.action_favorite));

        getFavorite();
    }

    /**
     *
     */
    private void getFavorite() {
        FistShowProgress();

        ApiParams params = new ApiParams();
        params.put("module", "website");
        params.put("a", "get_collect");
        params.put("uid", UserUtil.getValue(ctx, UserUtil.UserId));
        params.put("langcode", Locale.getDefault().getLanguage());

        ApiHelper.get(ctx, "", params, new ApiCallBack() {

            @Override
            public void receive(Result result) {

                if (result.isSuccess()) {
                    JSONObject json = JSON.parseObject(result.getObj().toString());
                    if (json.containsKey("data")) {
                        favorite = JSON.parseArray(json.getString("data"), GetFavoriteBean.class);
                        if (favorite != null && favorite.size() > 0) {
                            adapter = new FavoriteAdapter(ctx);
                            adapter.setList(favorite);
                            if (CurrentView instanceof ListView) {
                                ((ListView) CurrentView).setAdapter(adapter);
                            }
                        }
                        doSomeThing();
                    }
                } else {
                    //网络有问题
                    ErrorHappen(result.getCode(), new RetryListener() {

                        @Override
                        public void onRetry() {
                            getFavorite();
                        }
                    });
                }
            }
        });
    }
}
