package com.zhenglong.util;

import com.zhenglong.global.ResponseCode;
import com.zhenglong.redpack.entity.Result;

/**
 * @author Lion
 * @描述
 * @date 2019/5/11
 */
public class ResponseUtil {
    public static Result success() {
        Result result=new Result(ResponseCode.SUCCESS,"请求成功",null,0);
        return result;
    }

    public static Result success(Object data) {
        Result result=new Result(ResponseCode.SUCCESS,"请求成功",data,0);
        return result;
    }

    public static Result error(String eMsg) {
        Result result=new Result(ResponseCode.ERROR,eMsg,null,0);
        return result;
    }

    public static Result unlogin() {
        Result result=new Result(ResponseCode.UNLOGIN,"未登录,请重新登录",null,0);
        return result;
    }

}
