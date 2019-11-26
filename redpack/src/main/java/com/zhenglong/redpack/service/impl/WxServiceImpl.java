package com.zhenglong.redpack.service.impl;

import com.zhenglong.redpack.entity.WxUserInfo;
import com.zhenglong.redpack.entity.AccessTokenPo;
import com.zhenglong.redpack.operator.WxOperator;
import com.zhenglong.redpack.service.WxService;
import com.zhenglong.util.StringUtils;
import com.zhenglong.util.WxUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Lion
 * @描述
 * @date 2019/5/14
 */
@Service
public class WxServiceImpl implements WxService {

    @Resource
    WxOperator wxOperator;

    @Transactional
    @Override
    public void saveAccessToken(AccessTokenPo accessToken) {
        wxOperator.clear();
        wxOperator.saveAccessToken(accessToken);
    }

    @Override
    public AccessTokenPo getAccessToken(RestTemplate restTemplate) {
        AccessTokenPo accessTokenPo = wxOperator.getAccessToken();
        long currentTime=new Date().getTime()/1000;
        if (accessTokenPo==null ||
                (currentTime-accessTokenPo.getTime())>Long.parseLong(accessTokenPo.getExpiresIn())) {
            accessTokenPo=WxUtil.getAccessToken(restTemplate);
            if (accessTokenPo==null || StringUtils.isEmpty(accessTokenPo.getAccessToken())) return null;
            saveAccessToken(accessTokenPo);
        }
        return accessTokenPo;
    }

    @Override
    public List<WxUserInfo> getWxUserList(RestTemplate restTemplate) {
        AccessTokenPo accessTokenPo=getAccessToken(restTemplate);
        if (accessTokenPo==null) return null;
        List<String> openIds=WxUtil.getOpenId(restTemplate,accessTokenPo.getAccessToken());
        if (openIds==null) return null;
        List<WxUserInfo> wxUserInfoList=WxUtil.batchGetWxUserInfo(accessTokenPo.getAccessToken(),openIds,restTemplate);
        return wxUserInfoList;
    }

    @Transactional
    @Override
    public void saveUsers(List<WxUserInfo> wxUserInfoList) {
        if (wxUserInfoList.size()>0) {
            for (WxUserInfo wxUserInfo: wxUserInfoList) {
                //判断是否存在
                if (wxOperator.countByOpenId(wxUserInfo.getOpenid())>0) {
                    //存在则更新昵称和头像
                    wxOperator.updateWxUser(wxUserInfo.getNickname(),wxUserInfo.getHeadimgurl(),
                            wxUserInfo.getRemark(),wxUserInfo.getSubscribe(),wxUserInfo.getSubscribe_time(),
                            wxUserInfo.getOpenid());
                }else {
                    //不存在则插入
                    wxOperator.addWxUserFromTencent(wxUserInfo);
                }
            }
            //插入更新记录
            wxOperator.saveUpdateRecord(new Date().getTime()/1000);
        }
    }

    @Override
    public List<WxUserInfo> getUserList() {
        return wxOperator.getUserList();
    }
    @Override
    public String getUpdateRecord() {
        Long timeStamp = wxOperator.getUpdateRecord();
        return WxUtil.timeStamp2Date(timeStamp+"",null);
    }
}
