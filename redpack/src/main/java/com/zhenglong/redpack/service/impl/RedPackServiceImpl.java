package com.zhenglong.redpack.service.impl;

import com.zhenglong.global.GlobalCode;
import com.zhenglong.global.WxParams;
import com.zhenglong.redpack.entity.*;
import com.zhenglong.redpack.operator.MallUserOperator;
import com.zhenglong.redpack.operator.RedPackOperator;
import com.zhenglong.redpack.service.RedPackService;
import com.zhenglong.util.ExportTools;
import com.zhenglong.util.StringUtils;
import com.zhenglong.util.XmlUtil;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lion
 * @描述
 * @date 2019/5/19
 */
@Service
public class RedPackServiceImpl implements RedPackService {
    @Resource
    RedPackOperator redPackOperator;
    @Resource
    MallUserOperator mallUserOperator;

    @Autowired
    RestTemplate restTemplate;

    @Transactional
    @Override
    public boolean importRedPackMall(File file) {
        if (file!=null) {
            RedPackImportRecord record=new RedPackImportRecord();
            record.setDate(StringUtils.dateToStr(null,null));
            record.setRemark("红包记录导入");
            redPackOperator.saveRocord(record);
            List<RedPackMall> redPackMalls= ExportTools.importRedPackMall(file,record.getId());
            for (RedPackMall redPackMall: redPackMalls) {
                redPackOperator.saveRedPackMall(redPackMall);
            }
            return true;
        }
        return false;
    }

    @Override
    public Integer getLastRecordId() {
        Integer recordId=redPackOperator.getLastRecordId();
        return recordId;
    }

    @Override
    public Map<String, Object> getListWithRecord(Integer recordId, boolean commission) {
        Map<String,Object> data=new HashMap<>();
        List<RedPackMall> redPackMalls=redPackOperator.listByRecordId(recordId);
        List<User> successData=new ArrayList<>();
        List<MallUser> failData=new ArrayList<>();
        List<RedPackMall> realMallIds=new ArrayList<>();
        List<RedPackMall> failMallIds=new ArrayList<>();
        for (RedPackMall redPackMall: redPackMalls) {
            //无需验证是否具有分佣权限
            if (!commission) {
                realMallIds.add(redPackMall);
            }else {
                //需要验证是否具有分佣权限
                String realMallId=getRealMallId(redPackMall.getMallId());
                if (StringUtils.isEmpty(realMallId)) {
                    failMallIds.add(redPackMall);
                }else {
                    redPackMall.setMallId(realMallId);
                    realMallIds.add(redPackMall);
                }
            }
        }
        //拉取成功匹配的用户信息
        for (RedPackMall realMall: realMallIds) {
            User user=redPackOperator.getUserWithMallId(realMall.getMallId());
            if (user!=null) {
                user.setAmount(realMall.getAmount());
                successData.add(user);
            }else {
                failMallIds.add(realMall);
            }
        }
        //拉取匹配失败商城用户信息
        for (RedPackMall failMall: failMallIds) {
            MallUser mallUser=mallUserOperator.getWithMallId(failMall.getMallId());
            if (mallUser!=null) {
                mallUser.setAmount(failMall.getAmount());
                failData.add(mallUser);
            }
        }
        data.put("successData",successData);
        data.put("failData",failData);
        return data;
    }

    /**
     * 递归获取具有分佣权限的mallId
     * 1.查询出所有商城用户信息
     * 查找规则：
     * a.若具有分佣权限，直接返回该信息，
     * b.若不具有则查找其上级
     *  i.若无上级则为失败信息
     *  ii.若上级具有分佣权限，返回该上级信息
     *  iii.若上级也不具有分佣权限，则继续向上查找，若上级不具有上级，则最低等级信息为失败信息，以此类推
     * @param mallId
     * @return 返回null表明该商城id匹配失败
     */
    private String getRealMallId(String mallId) {
        MallUser mallUser=mallUserOperator.getWithMallId(mallId);
        if (mallUser==null) return null;
        if (mallUser.getCommission()==GlobalCode.Commission.COMMISSION) {
            return mallId;
        }else {//不具备分佣权限
            if (StringUtils.isEmpty(mallUser.getSuperMallId())) {
                //如果上级为空则为失败信息,返回Null
                return null;
            }else {
                return getRealMallId(mallUser.getSuperMallId());
            }
        }
    }

    @Override
    public List<RedPackImportRecord> getRecordList() {
        return redPackOperator.getRecordList();
    }

    @Transactional
    @Override
    public List<User> sendPack(Integer recordId) throws DocumentException {
        //查询本次是否已进行过发送操作
        if (redPackOperator.getSended(recordId)>1) {
            return null;
        }
        String partnerTradeNo=WxParams.getPartnerTradeNo();
        List<User> users= (List<User>) getListWithRecord(recordId,false).get("successData");
        if (users!=null) {
            for (int i=0; i<users.size(); i++) {
                int amount=StringUtils.yuanToFen(users.get(i).getAmount());
                String requestXml= WxParams.getPayRequestXml(partnerTradeNo+i,
                        users.get(i).getOpenId(),amount,"来自正龙通讯");
//                String responseXml=restTemplate.postForObject(HttpParams.getPaymentUrl(),requestXml,String.class);
                String responseXml="<xml>" +
                        "<return_code><![CDATA[SUCCESS]]></return_code>" +
                        "<return_msg><![CDATA[]]></return_msg>" +
                        "<mch_appid><![CDATA[wxec38b8ff840bd989]]></mch_appid>" +
                        "<mchid><![CDATA[10013274]]></mchid>" +
                        "<device_info><![CDATA[]]></device_info>" +
                        "<nonce_str><![CDATA[lxuDzMnRjpcXzxLx0q]]></nonce_str>" +
                        "<result_code><![CDATA[SUCCESS]]></result_code>" +
                        "<partner_trade_no><![CDATA[10013574201505191526582441]]></partner_trade_no>" +
                        "<payment_no><![CDATA[1000018301201505190181489473]]></payment_no>" +
                        "<payment_time><![CDATA[2015-05-19 15：26：59]]></payment_time>" +
                        "</xml>";
                if (responseXml!=null) {
                    PaymentResult paymentResult=XmlUtil.getXmlAttribute(responseXml);
                    redPackOperator.savePaymentResult(paymentResult);
                    redPackOperator.updatePaymentResultId(paymentResult.getId(),users.get(i).getRedPackMallId());
                    users.get(i).setPaymentResult(paymentResult);
                }
            }
            //修改导入记录中是否发送码为已进行发送
            redPackOperator.updateImportRecordSended(recordId, GlobalCode.RedPackStatus.SENDED);
        }
        return users;
    }

    @Override
    public List<User> getPaymentResult(Integer recordId) {
        if (recordId==null || recordId==0 ||redPackOperator.getSended(recordId)==1) {
            return null;
        }
        List<User> users=new ArrayList<>();
        List<RedPackMall> redPackMalls=redPackOperator.listByRecordId(recordId);
        for (RedPackMall redPackMall: redPackMalls) {
            User user=redPackOperator.getUserWithMallId(redPackMall.getMallId());
            if (user!=null) {
                user.setAmount(redPackMall.getAmount());
                user.setRedPackMallId(redPackMall.getId());
                PaymentResult paymentResult=redPackOperator.getPaymentResult(redPackMall.getPaymentResultId());
                user.setPaymentResult(paymentResult);
                users.add(user);
            }
        }
        return users;
    }
}
