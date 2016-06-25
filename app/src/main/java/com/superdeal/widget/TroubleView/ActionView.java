package com.superdeal.widget.TroubleView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.superdeal.callback.Callback.RetryListener;

public class ActionView extends FrameLayout {

    private Context ctx;

    public ActionView(Context context) {
        this(context, null);
    }

    public ActionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ctx = context;
    }

    /**
     * 添加特定布局内容
     *
     * @param layoutId 需要显示的布局内容
     * @param handid   需要操作的id
     * @param listener 产生回掉
     */
    public void addErrorView(int layout, int id, final RetryListener listener) {
        // Inflate the layout.
        LayoutInflater inflater = LayoutInflater.from(ctx);
        ViewGroup root = (ViewGroup) inflater.inflate(layout, this, false);
        addView(root);

        root.findViewById(id).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.onRetry();
                }
            }
        });
    }

    /**
     * @param layout
     */

    public void addProgressView(int layout) {
        // Inflate the layout.
        LayoutInflater inflater = LayoutInflater.from(ctx);
        ViewGroup root = (ViewGroup) inflater.inflate(layout, this, false);
        addView(root);
    }


}
