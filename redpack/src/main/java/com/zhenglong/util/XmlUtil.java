package com.zhenglong.util;


import com.zhenglong.redpack.entity.PaymentResult;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author Lion
 * @描述
 * @date 2019/5/20
 */
public class XmlUtil {
    public static PaymentResult getXmlAttribute(String xml) throws DocumentException {
        Document doc;
        PaymentResult result=new PaymentResult();
        // 将字符串转为XML
        doc = DocumentHelper.parseText(xml);
        // 获取根节点
        Element rootElt = doc.getRootElement();
        String returnCode=rootElt.element("return_code").getText();
        if ("SUCCESS".equals(returnCode)) {
            result.setReturnCode(returnCode);
            String resultCode=rootElt.elementText("result_code");
            result.setResultCode(resultCode);
            if ("SUCCESS".equals(resultCode)) {
                result.setPartnerTradeNo(rootElt.elementText("partner_trade_no"));
                result.setPaymentNo(rootElt.elementText("payment_no"));
                result.setPaymentTime(rootElt.elementText("payment_time"));
            }else {
                result.setErrCode(rootElt.elementText("err_code"));
                result.setErrCodeDesc(rootElt.elementText("err_code_des"));
            }
        }
        return result;
    }

    public static void main(String[] args) throws DocumentException {
        String xml="<xml>" +
                "" +
                "<return_code><![CDATA[SUCCESS]]></return_code>" +
                "" +
                "<return_msg><![CDATA[]]></return_msg>" +
                "" +
                "<mch_appid><![CDATA[wxec38b8ff840bd989]]></mch_appid>" +
                "" +
                "<mchid><![CDATA[10013274]]></mchid>" +
                "" +
                "<device_info><![CDATA[]]></device_info>" +
                "" +
                "<nonce_str><![CDATA[lxuDzMnRjpcXzxLx0q]]></nonce_str>" +
                "" +
                "<result_code><![CDATA[SUCCESS]]></result_code>" +
                "" +
                "<partner_trade_no><![CDATA[10013574201505191526582441]]></partner_trade_no>" +
                "" +
                "<payment_no><![CDATA[1000018301201505190181489473]]></payment_no>" +
                "" +
                "<payment_time><![CDATA[2015-05-19 15：26：59]]></payment_time>" +
                "" +
                "</xml>";
        getXmlAttribute(xml);
    }
}
