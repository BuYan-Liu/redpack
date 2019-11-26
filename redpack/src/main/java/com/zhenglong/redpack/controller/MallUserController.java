package com.zhenglong.redpack.controller;

import com.zhenglong.redpack.entity.MallUser;
import com.zhenglong.redpack.entity.Result;
import com.zhenglong.redpack.service.MallUserService;
import com.zhenglong.util.ExportTools;
import com.zhenglong.util.ResponseUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Lion
 * @描述
 * @date 2019/5/11
 */
@RestController
@RequestMapping("/mallUser")
public class MallUserController {
    @Resource
    MallUserService mallUserService;

//    @CrossOrigin
    @RequestMapping("/mallUsers/{currentPage}/{size}/{status}/{mallId}/{nickName}")
    public Result getMallUserList(@PathVariable int currentPage, @PathVariable int size, @PathVariable Integer[] status,
                                  @PathVariable String mallId,@PathVariable String nickName) {
        List<MallUser> mallUserList =mallUserService.getListWithStatus(status,currentPage,size,mallId,nickName);
        Result result=ResponseUtil.success(mallUserList);
        int totalCount=mallUserService.getTotalCountWithStatus(status,mallId,nickName);
        result.setTotalCount(totalCount);
        return result;
    }

    @CrossOrigin
    @RequestMapping("/import")
    public Result upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        String path=request.getServletContext().getRealPath("import");
        File targetFile=new File(path+"import.csv");
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }
        file.transferTo(targetFile);
        List<MallUser> mallUsers=ExportTools.importMallUser(targetFile);
        mallUserService.addList(mallUsers);
        return ResponseUtil.success();
    }
}
