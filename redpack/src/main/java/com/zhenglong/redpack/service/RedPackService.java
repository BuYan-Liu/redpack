package com.zhenglong.redpack.service;

import com.zhenglong.redpack.entity.RedPackImportRecord;
import com.zhenglong.redpack.entity.User;
import org.dom4j.DocumentException;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface RedPackService {
    boolean importRedPackMall(File file);

    Integer getLastRecordId();

//    Map<String, Object> getListWithRecord(Integer recordId);

    Map<String, Object> getListWithRecord(Integer recordId, boolean commission);

    List<RedPackImportRecord> getRecordList();

    List<User> sendPack(Integer recordId) throws DocumentException;

    List<User> getPaymentResult(Integer recordId);
}
