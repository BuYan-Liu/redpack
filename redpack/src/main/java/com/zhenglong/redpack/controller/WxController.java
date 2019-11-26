package com.zhenglong.redpack.controller;

import com.zhenglong.global.WxParams;
import com.zhenglong.redpack.entity.WxUserInfo;
import com.zhenglong.redpack.entity.Result;
import com.zhenglong.redpack.entity.AccessTokenPo;
import com.zhenglong.redpack.service.WxService;
import com.zhenglong.util.ExportTools;
import com.zhenglong.util.ResponseUtil;
import com.zhenglong.util.StringUtils;
import com.zhenglong.util.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lion
 * @描述
 * @date 2019/5/13
 */
@Controller
@RequestMapping("/wx")
public class WxController {
    private final String token = WxParams.NONCE_STR;
    private static Logger log = LoggerFactory.getLogger(WxController.class);
    @Autowired
    WxService wxService;
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/signature")
    public void signature(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("开始签名校验");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        System.out.println("接收到的参数如下：signature="+signature+"timestamp="+timestamp
        +"nonce="+nonce+"echostr="+echostr);
        ArrayList<String> array = new ArrayList<String>();
        array.add(signature);
        array.add(timestamp);
        array.add(nonce);
        //排序
        String sortString = WxUtil.sort(token, timestamp, nonce);
        //加密
        String mytoken = WxUtil.SHA1(sortString);
        //校验签名
        if (mytoken != null && mytoken != "" && mytoken.equals(signature)) {
            System.out.println("签名校验通过。");
            response.getWriter().print(echostr); //如果检验成功输出echostr，微信服务器接收到此输出，才会确认检验完成。
        } else {
            System.out.println("签名校验失败。");
            throw new Exception();
        }
    }

    @GetMapping("/users")
    @ResponseBody
    public Result getUserList(HttpServletResponse response) {
        List<WxUserInfo> wxUserInfoList=wxService.getWxUserList(restTemplate);
        if (wxUserInfoList==null) return ResponseUtil.error("没有获取到公众号用户数据");
        wxService.saveUsers(wxUserInfoList);
        return ResponseUtil.success();
    }

    @RequestMapping("/users/export")
    @ResponseBody
    public void exportUserList(HttpServletResponse response) {
        List<WxUserInfo> wxUserInfoList;
        wxUserInfoList=wxService.getUserList();
        String[] titles={"头像","昵称","openId","国家","省","市","是否订阅","订阅时间","性别","备注"};
        String[] propertys={"headimgurl","nickname","openid","country","province","city","subscribe",
                "subscribe_time","sex","remark"};
        ExportTools.downloadCsvFile("公众号用户信息",response,titles,propertys,wxUserInfoList);
    }

    @GetMapping("/update/record")
    @ResponseBody
    public Result updateRecord() {
        String time=wxService.getUpdateRecord();
        if (StringUtils.isEmpty(time)) return ResponseUtil.success("暂无更新记录");
        return ResponseUtil.success(time);
    }

}
