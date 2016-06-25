
package com.superdeal.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.loopj.android.http.RequestParams;
import com.superdeal.util.MLog;

public class ApiParams extends HashMap<String, Object> {

    static final String TAG = "ApiParams";

    /**
     *
     */
    private static final long serialVersionUID = -7496154977689738187L;

    public RequestParams map2RequestParams() {

        RequestParams p = new RequestParams();

        Iterator iter = this.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();

            MLog.e(TAG, "参数" + key + ":" + val);

            p.put((String) key, val);
        }

        return p;
    }

}
