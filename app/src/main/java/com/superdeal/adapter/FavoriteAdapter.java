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
import com.superdeal.bean.GetFavoriteBean;
import com.squareup.picasso.Picasso;

public class FavoriteAdapter extends BaseListAdapter<GetFavoriteBean> {

    public FavoriteAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.favorite_item, null);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GetFavoriteBean temp = getList().get(position);

        holder.mTitle.setText(temp.getName());
        holder.mContent.setText(temp.getTitle());
        Picasso.with(parent.getContext()).load(Constants.ICON + temp.getLogo()).into(holder.micon);

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.icon)
        ImageView micon;

        @InjectView(R.id.content)
        TextView mContent;

        @InjectView(R.id.title)
        TextView mTitle;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
            view.setTag(this);
        }
    }
}
