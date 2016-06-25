
package com.superdeal.data;

import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.http.Header;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.superdeal.R;
import com.superdeal.base.Constants;
import com.superdeal.util.MLog;
import com.superdeal.util.NetworkUtil;

public class ApiHelper {

    private static final String TAG = "ApiHelper";

    private static final String CODE = "err_code";

    private static final String MESSAGE = "err_msg";

    /**
     * 访问接口
     *
     * @param url      接口相对路径
     * @param url      实体类
     * @param params   参数
     * @param callBack 回调接口
     */
    public static void get(final Context ctx, final String url, final ApiParams params, final ApiCallBack callBack) {

        if (!NetworkUtil.isNetworkAvailable(ctx)) {
            Result result = new Result(ResultCode.NETWORK_TROBLE, ctx.getString(R.string.error_network_Unavailable));
            if (callBack != null) {
                callBack.receive(result);
            }
        } else {
            MLog.e(TAG, "调用接口:" + url);

            String paramsStr = "";
            if (params != null) {
                // 解析组合参数
                StringBuffer bufferParams = new StringBuffer();

                bufferParams.append("?");

                Iterator<Entry<String, Object>> iter = params.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<String, Object> entry = iter.next();
                    Object key = entry.getKey();
                    Object val = entry.getValue();

                    MLog.e(TAG, "参数" + key + ":" + val);

                    bufferParams.append(key).append("=").append(val).append("&");
                }

                // 删除最后一个&
                bufferParams.deleteCharAt(bufferParams.length() - 1);
                paramsStr = bufferParams.toString();

            }


            String absUrl;
            if (TextUtils.isEmpty(url)) {
                absUrl = Constants.SERVER + paramsStr;
            } else {
                absUrl = Constants.SERVER + url + paramsStr;
            }
            absUrl.trim();


            MLog.i(TAG, "调用接口 #" + url + "# 全路径:\r\n" + absUrl);

            HttpUtil.post(absUrl, new AsyncHttpResponseHandler() {

                private Result result;// 返回结果

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    try {
                        String content = new String(responseBody);
                        MLog.e(TAG, "调用接口 #" + url + "# 返回:\r\n" + content);

                        JSONObject json = JSON.parseObject(content);
                        if (json.getInteger(CODE) == 0) {

                            result = new Result(ResultCode.RESULT_OK, content);
                        } else {
                            result = new Result(ResultCode.RESULT_FAILED, json.getString(MESSAGE));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        result = new Result(ResultCode.RESULT_FAILED, ctx.getString(R.string.FAILED));
                    }
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

                    MLog.e(TAG, "调用接口  #" + url + "# 返回:\r\n" + error.getMessage());
                    result = new Result(ResultCode.RESULT_FAILED, ctx.getString(R.string.FAILED));
                }

                @Override
                public void onFinish() {
                    super.onFinish();

                    if (callBack != null && result != null) {
                        callBack.receive(result);
                    }
                }
            });
        }
    }
}
