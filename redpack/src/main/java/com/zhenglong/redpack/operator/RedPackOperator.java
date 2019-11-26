package com.zhenglong.redpack.operator;

import com.zhenglong.redpack.entity.PaymentResult;
import com.zhenglong.redpack.entity.RedPackImportRecord;
import com.zhenglong.redpack.entity.RedPackMall;
import com.zhenglong.redpack.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Lion
 * @描述
 * @date 2019/5/19
 */
@Mapper
public interface RedPackOperator {
    Integer saveRocord(RedPackImportRecord redPackImportRocord);

    void saveRedPackMall(RedPackMall redPackMall);

    int countByMallId(String mallId);

    Integer getLastRecordId();

    List<RedPackMall> listByRecordId(Integer recordId);

    User getUserWithMallId(String mallId);

    List<RedPackImportRecord> getRecordList();

    void savePaymentResult(PaymentResult paymentResult);

    void updatePaymentResultId(@Param("resultId") Integer resultId, @Param("id") Long id);

    void updateImportRecordSended(@Param("recordId") Integer recordId, @Param("sended") int sended);

    int getSended(Integer recordId);

    PaymentResult getPaymentResult(Integer paymentResultId);
}
