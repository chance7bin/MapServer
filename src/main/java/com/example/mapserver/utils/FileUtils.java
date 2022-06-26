package com.example.mapserver.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @Description
 * @Author bin
 * @Date 2022/05/12
 */
@Slf4j
public class FileUtils {

    //GZIP解压
    public static byte[] gzipUncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            log.error("gzip uncompress error.", e);
        }

        return out.toByteArray();
    }


    public static Map readJson(File file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(file, Map.class);
        } catch (Exception e){
            log.error("read json error.", e);
            return null;
        }
    }


    public static void createDir(String path){
        File file =new File(path);    //如果文件夹不存在则创建
        if (!file.exists()) {
            file.mkdirs();
            log.debug("文件夹创建成功");
        } else {
            log.debug("文件夹已存在");
        }
    }


    //获得路径下的文件列表
    public static List<String> getFiles(String path)
    {
        File file = new File(path);
        File[] files = file.listFiles();
        List<String> filesPath = new ArrayList<>();
        for (File f : files) {
            filesPath.add(f.getAbsolutePath());
        }
        return filesPath;
    }

    //获得路径下的文件列表(含子目录)
    public static void getFilesContainChild(String path, List<String> filesPath)
    {
        File file = new File(path);
        // 如果这个路径是文件夹
        if (file.isDirectory()) {
            // 获取路径下的所有文件
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 如果还是文件夹 递归获取里面的文件 文件夹
                filesPath.add(files[i].getAbsolutePath());
                if (files[i].isDirectory()) {
                    // System.out.println("目录：" + files[i].getAbsolutePath());
                    // filesPath.add(files[i].getAbsolutePath());
                    //继续读取文件夹里面的所有文件
                    getFilesContainChild(files[i].getAbsolutePath(), filesPath);
                } else {
                    // filesPath.add(files[i].getAbsolutePath());
                    // System.out.println("文件：" + files[i].getAbsolutePath());
                }
            }
        } else {
            // System.out.println("文件：" + file.getAbsolutePath());
            filesPath.add(file.getAbsolutePath());
        }
    }

    /**
     * 得到文件名(不包含后缀)
     * @param path
     * @return java.lang.String
     * @Author bin
     **/
    public static String getFilenameNoSuffix(String path){
        File file = new File(path);
        String name = file.getName();
        int i = name.lastIndexOf(".");
        String outputName = name.substring(0,i);
        // System.out.println(outputName);
        return outputName;
    }

    /**
     * 得到文件的文件类型
     * @param path 文件所在路径
     * @return java.lang.String
     * @Author bin
     **/
    public static String getFileType(String path){
        String[] split = path.split("\\.");
        return split[split.length - 1];
    }

}
