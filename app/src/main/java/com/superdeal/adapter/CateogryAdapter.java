package com.superdeal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.superdeal.base.BaseListAdapter;
import com.superdeal.bean.CategoryBean;

public class CateogryAdapter extends BaseListAdapter<CategoryBean> {

    public CateogryAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView title = (TextView) convertView;

        if (title == null) {
            title = (TextView) mInflater.inflate(android.R.layout.simple_list_item_activated_1, null);
        }

        String name = getList().get(position).getName();
        if (TextUtils.isEmpty(name)) {
            title.setText("Test" + position);
        } else {
            title.setText(name);
        }

        return title;
    }
}
