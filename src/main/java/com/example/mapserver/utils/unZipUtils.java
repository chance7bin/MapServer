package com.example.mapserver.utils;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class unZipUtils {
    /**
     * 保存zip文件到本地并调用解压方法并返回解压出的文件的路径集合
     *
     * @param file 文件
     * @return list //解压出的文件的路径合集
     */
    public static Logger logger = Logger.getLogger(unZipUtils.class.getName());
//    public static String batchadd(String srcFile, String destFile) {
//        /*
//         *创建临时文件夹
//         * 解压文件
//         */
//        String shpPath = "";
//        List<String> list = new ArrayList<>();
//        try {
//            File zipFile = new File(srcFile);
//            list = unZipFiles(zipFile, destFile);//解压文件，获取文件路径
//            shpPath = selectShpfile(list);
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.info("解压执行失败");
//        }
//        return shpPath;
//    }

    /**
     * zip解压
     *
     * @param srcFilePath zip源文件
     * @param destDirPath 解压后的目标文件夹
     * @return list 解压文件的路径合集
     * @throws RuntimeException 解压失败会抛出运行时异常
     */
    public static List<String> unZipFiles(String srcFilePath, String destDirPath) throws RuntimeException {
        File srcFile = new File(srcFilePath);
        List<String> list = new ArrayList<>();
        long start = System.currentTimeMillis();
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
        }
        // 开始解压
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(srcFile);
            Enumeration<?> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                logger.info("解压" + entry.getName());
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    String dirPath = destDirPath + entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    File targetFile = new File(destDirPath + entry.getName());
                    // 保证这个文件的父文件夹必须要存在
                    list.add(destDirPath + entry.getName());
                    if (!targetFile.getParentFile().exists()) {
                        logger.info("父文件不存在");
                    }
                    targetFile.createNewFile();
                    // 将压缩文件内容写入到这个文件中
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    // 关流顺序，先打开的后关闭
                    fos.close();
                    is.close();
                }
            }
            long end = System.currentTimeMillis();
            logger.info("解压完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("unzip error from ZipUtils", e);
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * @param filePath 临时文件的删除
     *                 删除文件夹里面子目录
     *                 再删除文件夹
     */
    public static void deleteFiles(String filePath) {
        File file = new File(filePath);
        if ((!file.exists()) || (!file.isDirectory())) {
            logger.info("file not exist");
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (filePath.endsWith(File.separator)) {
                temp = new File(filePath + tempList[i]);
            } else {
                temp = new File(filePath + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                deleteFiles(filePath + "\\" + tempList[i]);
            }
        }
        // 空文件的删除
        file.delete();
    }

    public static String selectShpfile(List<String> list) {
        String shp = "";
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (next.toString().indexOf(".shp") != -1) {
                shp = next.toString();
                break;
            }
        }
        return shp;
    }
}
