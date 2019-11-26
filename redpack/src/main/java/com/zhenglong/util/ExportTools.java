package com.zhenglong.util;

import com.zhenglong.global.GlobalCode;
import com.zhenglong.redpack.entity.MallUser;
import com.zhenglong.redpack.entity.RedPackMall;
import com.zhenglong.redpack.entity.WxUserInfo;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExportTools {
    public static void exportCsv(String exportPath, String fileName, List<WxUserInfo> wxUserInfos) throws IOException {
        File file=new File(exportPath,fileName+"转换后.csv");
        if (!file.exists()) {
            file.createNewFile();
        }else {
            file.delete();
            file.createNewFile();
        }
        try (FileWriter writer = new FileWriter(file);
             BufferedWriter out = new BufferedWriter(writer)
        ) {
            out.write("头像,昵称,国家,省,市,是否订阅,订阅时间,openId,性别,备注,\r\n");
            for (WxUserInfo wxUserInfo: wxUserInfos) {
                String line="";
                out.write(line); // \r\n即为换行
                out.flush(); // 把缓存区内容压入文件
            }
        } catch (IOException e) {
            throw new IOException("导出异常");
        }
    }


    //Web 导出Csv文件
    public static void downloadCsvFile(String fileTitle, HttpServletResponse response,
                                       String[] titles, String[] propertys,
                                       List<?> list){
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            //ExcelUtils.exportExcel2007(titles,headMaps,jas,null,0,os);
            exportCsv(os,titles,propertys,list);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            String fileName= fileTitle+".csv";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName).getBytes(), "iso-8859-1"));
            response.setContentLength(content.length);
            ServletOutputStream outputStream = response.getOutputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            byte[] buff = new byte[8192];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bis.close();
            bos.close();
            outputStream.flush();
            outputStream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void exportCsv(ByteArrayOutputStream os, String[] titles,
                                     String[] propertys, List<T> list) throws IOException, IllegalArgumentException, IllegalAccessException{

        //构建输出流，同时指定编码
        OutputStreamWriter ow = new OutputStreamWriter(os, "utf-8");
        //csv文件是逗号分隔，除第一个外，每次写入一个单元格数据后需要输入逗号
        ow.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF }));
        for(String title : titles){
            ow.write("\""+title+"\"");
            ow.write(",");
        }
        //写完文件头后换行
        ow.write("\r\n");
        //写内容
        for(Object obj : list){
            String value="";
            //利用反射获取所有字段
            Field[] fields = obj.getClass().getDeclaredFields();
            for(String property : propertys){
                for(Field field : fields){
                    //设置字段可见性
                    field.setAccessible(true);
                    if(property.equals(field.getName())){
                        value=field.get(obj)!=null?field.get(obj).toString():" ";
                        ow.write("\""+value+"\"");
                        ow.write(",");
                        continue;
                    }
                }
            }
            //写完一行换行
            ow.write("\r\n");
        }
        ow.flush();
        ow.close();
    }

    public static List<String> importCsv(File file) {
        List<String> dataList=new ArrayList<>();
        BufferedReader br=null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "");
                dataList.add(line);
            }
        }catch (Exception e) {
        }finally{
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        dataList.remove(0);//移除标题行
        return dataList;
    }

    public static List<MallUser> importMallUser(File file) {
        List<String> dataList=importCsv(file);
        String titleLine=dataList.get(0);
        String[] titles= titleLine.split(",").length==1?titleLine.split("\t"):titleLine.split(",");
        int idxMallId=-1;
        int idxSuperMallId=-1;
        int idxNickName=-1;
        int idxCreateTime=-1;
        int idxCommission=-1;
        for (int i=0; i<titles.length; i++) {
            if ("分销商ID".equals(titles[i].trim())) idxMallId=i;
            if ("分销上级".equals(titles[i].trim())) idxSuperMallId=i;
            if ("昵称".equals(titles[i].trim())) idxNickName=i;
            if ("创建时间".equals(titles[i].trim())) idxCreateTime=i;
            if ("分销商分佣状态".equals(titles[i].trim())) idxCommission=i;
        }
        List<MallUser> mallUsers=new ArrayList<>();
        if (idxMallId==-1 || idxSuperMallId==-1 || idxNickName==-1 || idxCreateTime==-1 || idxCommission ==-1)
            return mallUsers;
        String[] datas;
        //去除标题行
        dataList.remove(0);
        for (String line: dataList) {
            System.out.println(line);
            datas=line.split(",").length==1?line.split("\t"):line.split(",");
            if (datas.length==5) {
                MallUser mallUser = new MallUser();
                mallUser.setStatus(GlobalCode.AStatus.UNMATCHED);
                mallUser.setMallId(datas[idxMallId].trim());
                mallUser.setNickName(datas[idxNickName].trim());
                mallUser.setCreateTime(datas[idxCreateTime].trim());
                mallUser.setCommission(GlobalCode.Commission.getCommission(datas[idxCommission].trim()));
                mallUser.setSuperMallId(StringUtils.subString(datas[idxSuperMallId].trim(),
                        ":", "_", false));
                mallUsers.add(mallUser);
            }
        }
        return mallUsers;
    }

    public static List<RedPackMall> importRedPackMall(File targetFile,Integer recordId) {
        List<String> dataList=importCsv(targetFile);
        List<RedPackMall> redPackMalls=new ArrayList<>();
        String[] titles=dataList.get(0).split(",");
        int idxMallId=-1;
        int idxAmount=-1;
        for (int i=0; i<titles.length; i++) {
            if ("用户名".equals(titles[i].trim())) idxMallId=i;
            if ("金额".equals(titles[i].trim())) idxAmount=i;
        }
        if (idxAmount==-1 || idxMallId==-1) return redPackMalls;
        //移除标题行
        dataList.remove(0);
        String[] datas;
        for (String line: dataList) {
            RedPackMall redPackMall=new RedPackMall();
            datas=line.split(",");
            redPackMall.setMallId(StringUtils.subString(datas[idxMallId],":","_",false));
            redPackMall.setAmount(datas[idxAmount]);
            redPackMall.setRecordId(recordId);
            redPackMalls.add(redPackMall);
        }
        return redPackMalls;
    }
}
