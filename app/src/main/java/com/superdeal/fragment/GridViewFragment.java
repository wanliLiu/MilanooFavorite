package com.superdeal.fragment;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import butterknife.InjectView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.superdeal.R;
import com.superdeal.Web.MyWebView;
import com.superdeal.adapter.GridViewAdapter;
import com.superdeal.base.BaseFragment;
import com.superdeal.base.Constants;
import com.superdeal.bean.WebsiteTagBean;
import com.superdeal.callback.Callback.RetryListener;
import com.superdeal.data.ApiCallBack;
import com.superdeal.data.ApiHelper;
import com.superdeal.data.ApiParams;
import com.superdeal.data.Result;
import com.viewpagerindictor.CirclePageIndicatorGridViewPager;
import com.viewpagerindictor.viewpager.DraggableGridViewPager;
import com.viewpagerindictor.viewpager.DraggableGridViewPager.OnRearrangeListener;

public class GridViewFragment extends BaseFragment {

    private static final String TAG = GridViewFragment.class.getSimpleName();
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String CategoryId = "listData";

    private List<WebsiteTagBean> list;
    private GridViewAdapter adapter;

    @InjectView(R.id.gridViewPager)
    DraggableGridViewPager mGridViewPager;

    @InjectView(R.id.app_cfindicator)
    CirclePageIndicatorGridViewPager indicator;

    public static GridViewFragment newInstance(String data) {
        GridViewFragment fragment = new GridViewFragment();
        Bundle args = new Bundle();
        args.putString(CategoryId, data);
        fragment.setArguments(args);
        return fragment;
    }

    // region Constructors
    public static GridViewFragment newInstance(Bundle extras) {
        GridViewFragment fragment = new GridViewFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    public static GridViewFragment newInstance() {
        GridViewFragment fragment = new GridViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        String temp = getArguments().getString(CategoryId);
        if (!TextUtils.isEmpty(temp)) {
            list = JSON.parseArray(temp, WebsiteTagBean.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gridview_display, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        indicator.setVisibility(View.INVISIBLE);

        InitListenter();

        if (list != null && list.size() > 0) {
            adapter = new GridViewAdapter(ctx);
            adapter.setList(list);
            mGridViewPager.setAdapter(adapter);

            if (mGridViewPager.getPageCount() > 1) {
                indicator.setViewPager(mGridViewPager);
                indicator.setVisibility(View.VISIBLE);
                indicator.setCurrentItem(0);
            }
        } else {
            getCategoryList();
        }
    }

    /**
     *
     */
    private void InitListenter() {

        mGridViewPager.setOnRearrangeListener(new OnRearrangeListener() {
            @Override
            public void onRearrange(int oldIndex, int newIndex) {
                Log.i(TAG, "OnRearrangeListener.onRearrange from=" + oldIndex + ", to=" + newIndex);
//				WebsiteTagBean Object = (WebsiteTagBean)adapter.getItem(oldIndex);
//				adapter.remove(oldIndex);
//				adapter.set(newIndex, Object);
//				adapter.notifyDataSetChanged();
            }
        });

        mGridViewPager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!TextUtils.isEmpty(adapter.getList().get(position).getUrl())) {
                    Intent intent = new Intent(getActivity(), MyWebView.class);
                    intent.putExtra(Constants.Intent_Title, adapter.getList().get(position).getName());
                    intent.putExtra(Constants.Intent_URL, adapter.getList().get(position).getUrl());
                    intent.putExtra(Constants.Intent_ID, adapter.getList().get(position).getId());
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 获取分类数据内容
     */
    private void getCategoryList() {
        FistShowProgress();

        ApiParams params = new ApiParams();
        params.put("module", "website");
        params.put("a", "get_websitetag_list");
        params.put("tid", 1);
        params.put("langcode", Locale.getDefault().getLanguage());

        ApiHelper.get(getActivity(), "", params, new ApiCallBack() {

            @Override
            public void receive(Result result) {

                if (result.isSuccess()) {

                    JSONObject json = JSON.parseObject(result.getObj().toString());
                    if (json.containsKey("data")) {
                        list = JSON.parseArray(json.getString("data"), WebsiteTagBean.class);
                        if (list != null && list.size() > 0) {

                            doSomeThing();
                            adapter = new GridViewAdapter(ctx);
                            adapter.setList(list);
                            mGridViewPager.setAdapter(adapter);

                            if (mGridViewPager.getPageCount() > 1) {
                                indicator.setViewPager(mGridViewPager);
                                indicator.setVisibility(View.VISIBLE);
                                indicator.setCurrentItem(0);
                            }
                        }
                    }

                } else {
                    //网络有问题
                    ErrorHappen(result.getCode(), new RetryListener() {

                        @Override
                        public void onRetry() {
                            getCategoryList();
                        }
                    });
                }
            }
        });

    }
}
