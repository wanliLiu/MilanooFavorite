package com.superdeal.data;

/**
 * @author star_Yang
 * @version V1.0
 * @Title: Result.java
 * @Package com.palmlink.weedu.model
 * @Description: TODO(请求返回结果)
 * @date 2014-1-5 下午9:49:36
 */
public class Result {

    /**
     * 成功失败
     */
    private ResultCode success;

    private Object obj;

    public Result(ResultCode success, Object obj) {
        this.success = success;
        this.obj = obj;
    }

    public boolean isSuccess() {
        return success == ResultCode.RESULT_OK;
    }

    public ResultCode getCode() {
        return success;
    }

    public void setSuccess(ResultCode success) {
        this.success = success;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

}
