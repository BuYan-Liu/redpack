package com.zhenglong.redpack.controller;

import com.zhenglong.redpack.entity.RedPackImportRecord;
import com.zhenglong.redpack.entity.Result;
import com.zhenglong.redpack.entity.User;
import com.zhenglong.redpack.service.RedPackService;
import com.zhenglong.util.ExportTools;
import com.zhenglong.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Lion
 * @描述
 * @date 2019/5/19
 */
@RestController
@RequestMapping("/redpack")
public class RedPackController {

    @Autowired
    RedPackService redPackService;

    @CrossOrigin
    @RequestMapping("/import")
    public Result importFileUnMatch(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        File targetFile=targetFile(request,file);
        if (redPackService.importRedPackMall(targetFile)) return ResponseUtil.success();
        return ResponseUtil.error("导入失败");
    }

    @CrossOrigin
    @RequestMapping("/info/{recordId}/{commission}")
    public Result info(@PathVariable Integer recordId, @PathVariable boolean commission, HttpServletRequest request) {
        if (recordId==null || recordId==0) {
            //获取最近一次导入记录
            recordId=redPackService.getLastRecordId();
        }
        Map<String,Object> data = redPackService.getListWithRecord(recordId,commission);
        List<RedPackImportRecord> recordList=redPackService.getRecordList();
        data.put("recordList", recordList);
        return ResponseUtil.success(data);
    }

    @CrossOrigin
    @RequestMapping("/sendpack/{recordId}")
    public Result sendPack(@PathVariable Integer recordId) throws Exception {
        List<User> users=redPackService.sendPack(recordId);
        if (users!=null) return ResponseUtil.success(users);
        return ResponseUtil.error("本次记录已进行过发送红包操作");
    }

    @CrossOrigin
    @RequestMapping("/paymentresult/{recordId}/{resultCode}")
    public Result paymentResult(@PathVariable Integer recordId,@PathVariable String resultCode) {
        List<User> users=redPackService.getPaymentResult(recordId);
        if (resultCode!=null && !resultCode.isEmpty() && !"all".equals(resultCode)) {
            List<User> usersFilter=new ArrayList<>();
            for (User user: users) {
                if (resultCode.equals(user.getPaymentResult().getResultCode())) {
                    usersFilter.add(user);
                }
            }
            return ResponseUtil.success(usersFilter);
        }
        return ResponseUtil.success(users);
    }

    @CrossOrigin
    @RequestMapping("/export/{recordId}/{commission}")
    public void export(@PathVariable Integer recordId, @PathVariable boolean commission,HttpServletResponse response) {
        if (recordId==null || recordId==0) {
            //获取最近一次导入记录
            recordId=redPackService.getLastRecordId();
        }
        Map<String,Object> data = redPackService.getListWithRecord(recordId,commission);
        List<User> successData= (List<User>) data.get("successData");
        if (successData!=null) {
            String[] titles = {"昵称", "openId", "分销商Id", "金额"};
            String[] propertys = {"nickName", "openId", "mallId", "amount"};
            ExportTools.downloadCsvFile("红包用户信息", response, titles, propertys, successData);
        }
    }


    @RequestMapping("/records")
    public Result recordList() {
        List<RedPackImportRecord> recordList=redPackService.getRecordList();
        if (recordList!=null) return ResponseUtil.success(recordList);
        return ResponseUtil.success();
    }

    private File targetFile(HttpServletRequest request,MultipartFile file) throws IOException {
        String path=request.getServletContext().getRealPath("import");
        File targetFile=new File(path+"readpackmall.csv");
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }
        file.transferTo(targetFile);
        return targetFile;
    }
}
