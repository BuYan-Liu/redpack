package com.zhenglong.util;

import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lion
 * @描述
 * @date 2019/6/21
 */
public class FtpUtil {
    FtpClient ftpClient;

    /**
     * 链接Ftp服务器
     * @param url
     * @param port
     * @param username
     * @param password
     * @return FtpClient
     */
    public static FtpClient connectFtpServer(String url, int port, String username, String password)
            throws IOException, FtpProtocolException {
        FtpClient ftp=null;
        SocketAddress address=new InetSocketAddress(url,port);
        ftp=FtpClient.create();
        ftp.connect(address);
        ftp.login(username,password.toCharArray());
        ftp.setBinaryType();
        return ftp;
    }

    public static List<String> read(String file,FtpClient ftpClient) throws IOException, FtpProtocolException {
        List<String> lines=new ArrayList<>();
        InputStream fis=ftpClient.getFileStream(file);
        String line;
        BufferedReader reader=new BufferedReader(new InputStreamReader(fis));
        while ((line=reader.readLine())!=null) {
            lines.add(line);
        }
        return lines;
    }
}
