package com.superdeal.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.superdeal.R;
import com.superdeal.base.BaseListAdapter;
import com.superdeal.base.Constants;
import com.superdeal.bean.WebsiteTagBean;
import com.squareup.picasso.Picasso;

public class GridViewAdapter extends BaseListAdapter<WebsiteTagBean> {


    public GridViewAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_item, null);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        WebsiteTagBean bean = (WebsiteTagBean) getItem(position);

        holder.mTitle.setText(bean.getName());

        Picasso.with(ctx).load(Constants.ICON + bean.getLogo()).into(holder.mimage);

        return convertView;
    }

    class ViewHolder {

        @InjectView(R.id.text)
        TextView mTitle;

        @InjectView(R.id.image)
        ImageView mimage;

        private ViewHolder(View view) {

            ButterKnife.inject(this, view);
//			mimage.setMaskedColor(ctx.getResources().getColor(R.color.color_gray));

            view.setTag(this);
        }
    }
}
