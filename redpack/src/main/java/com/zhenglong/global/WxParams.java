package com.zhenglong.global;

import com.sun.crypto.provider.HmacMD5;
import com.zhenglong.util.StringUtils;
import sun.security.provider.MD5;

import java.security.MessageDigest;
import java.util.*;

/**
 * @author Lion
 * @描述
 * @date 2019/5/13
 */
public class WxParams {
    public static final String IP = "47.23.21.21";
    public static final String AppId = "wx80b502bea1a49aa9";
    public static final String AppSecret = "36e5ae18a3056ca4f1c189045d2fbad9";
    public static final String MerchantId = "1495347032";//商户号
    public static final String GrantType_AccessToken = "client_credential";
    public static final String NONCE_STR = "zhenglongredpack20190513";//支付时随机字符串
    String PARTNER_TRADE_NO;//商户订单号
    public static final String CHECK_NAME_NO_CHECK = "NO_CHECK";//校验用户姓名选项 不校验真实姓名
    public static final String CHECK_NAMW_FORCE_CHENK = "FORCE_CHENK";//校验用户姓名选项 强校验真实姓名
    public static final String KEY="zlrp"+AppId+MerchantId;

    public static final String AppIdTest = "wx5e8894ce912eb926";
    public static final String AppSecretTest = "239816cc3fdbb554889fe575a105b38b";

    /**
     * 获取商户订单号  建议在获取到的字符串后自行拼接后缀
     *
     * @return
     */
    public static String getPartnerTradeNo() {
        return StringUtils.dateToStr(new Date(), "yyyyMMddHHmmss");
    }

    public static String getPayRequestXml(String partnerTradeNo, String openId,
                                          int amount, String desc) {
        String xml = "<xml>" +
                "<mch_appid>" + AppId + "</mch_appid>" +
                "<mchid>" + MerchantId + "</mchid>" +
                "<nonce_str>" + NONCE_STR + "</nonce_str>" +
                "<partner_trade_no>" + partnerTradeNo + "</partner_trade_no>" +
                "<openid>" + openId + "</openid>" +
                "<check_name>" + CHECK_NAME_NO_CHECK + "</check_name>" +
                "<amount>" + amount + "</amount>" +
                "<desc>" + desc + "</desc>" +
                "<spbill_create_ip>" + IP + "</spbill_create_ip>" +
                "<sign>" + getSign(partnerTradeNo,openId,amount,desc) + "</sign>" +
                "</xml>";
        return xml;
    }

    private static String getSign(String partnerTradeNo, String openId,
                                  int amount, String desc) {
        Map<String,Object> params=new HashMap<>();
        params.put("mch_appid",AppId);
        params.put("mchid",MerchantId);
        params.put("nonce_str",NONCE_STR);
        params.put("partner_trade_no",partnerTradeNo);
        params.put("openid",openId);
        params.put("check_name",CHECK_NAME_NO_CHECK);
        params.put("amount",amount);
        params.put("desc",desc);
        params.put("spbill_create_ip",IP);
        return paymentSign(params,KEY);
    }

    public static void main(String[] args) {
        System.out.println(getPayRequestXml("sdadasd","o-xasdqsc1sa",1000,"来自正龙通讯"));
    }
    private static String paymentSign(Map<String, Object> params, String key) {
        List<String> strings = new ArrayList<>(params.keySet());
        strings.sort(Comparator.naturalOrder());
        //拼接字符串
        String a = "";
        for (String s : strings) {
            a = a + (s + "=" + params.get(s)) + "&";
        }
        String signTempStr = a + "key=" + key;
        String signStr = MD5(signTempStr).toUpperCase();
        return signStr;
    }

    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
