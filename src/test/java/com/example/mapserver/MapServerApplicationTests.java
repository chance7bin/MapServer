package com.example.mapserver;

import com.example.mapserver.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MapServerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void test() throws UnknownHostException {

        InetAddress addr = InetAddress.getLocalHost();
        System.out.println("Local HostAddress: "+addr.getHostAddress());
            String hostname = addr.getHostName();
        System.out.println("Local host name: "+hostname);

    }


    //生成cmd下载代码
    @Test
    void downloadTxtFunc() throws IOException {

        for (int i = 0; i <= 10; i++) {
            int layer = i; //层级
            int pNum = 1; //人数
            // String txtOutputDir = "E:/mapServer/cmdTxt_14/" + layer;  //txt输出路径
            String txtOutputDir = "E:/mapServer/cmdTxt";  //txt输出路径
            // String pngOutputDir = "E:/mapServer/download_data/";  //下载瓦片输出路径
            String pngOutputDir = "E:/mapServer/download_data/terrarium/";  //下载瓦片输出路径

            getDownloadTxt(layer, pNum, txtOutputDir, pngOutputDir);
        }

    }

    void getDownloadTxt(int layer, int pNum, String txtOutputDir, String pngOutputDir) throws IOException {

        int dirNum = (int)Math.pow(2, layer); //该层级下的文件夹数量
        int perDirNum; //每个人分到的文件夹数量
        perDirNum = dirNum / pNum;
        // String layerDir = "11/109"; //下载的层级文件夹
        String layerDir; //下载的层级文件夹
        int lastDirNum = dirNum % pNum == 0 ? perDirNum : perDirNum + dirNum % pNum; //最后一个人下载的文件夹数量

        //判断上级文件夹是否存在，不存在的话创建一个新的
        FileUtils.createDir(txtOutputDir);

        //除最后一个人外的其他人
        for (int i = 0; i < pNum - 1; i++) {

            for (int j = 0; j < perDirNum; j++) {
                layerDir = layer + "/" + (i * perDirNum + j);
                //打开缓冲流
                BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(txtOutputDir + "/cmd_" + layer + "_" + (i * perDirNum + 0) + "-" + (i * perDirNum + perDirNum - 1) + ".txt",true)));
                //写文件
                out.write(getCmdString(layerDir, pngOutputDir));
                //关流
                out.close();
            }
            // System.out.println("------------------------");
        }
        //最后一个人

        for (int j = 0; j < lastDirNum; j++) {
            layerDir = layer + "/" + ((pNum - 1) * perDirNum + j);
            //打开缓冲流
            BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(txtOutputDir + "\\cmd_" + layer + "_" + ((pNum - 1) * perDirNum + 0) + "-" + ((pNum - 1) * perDirNum + lastDirNum - 1) + ".txt",true)));
            //写文件
            out.write(getCmdString(layerDir, pngOutputDir));
            //关流
            out.close();
        }

    }

    String getCmdString(String layerDir, String pngOutputDir){
        // String cmd = "aws s3 cp --no-sign-request --recursive s3://elevation-tiles-prod/terrarium/11/109 D:/data/terrarium/11/109";
        String cmd = "aws s3 cp --no-sign-request --recursive s3://elevation-tiles-prod/terrarium/";
        cmd = cmd + layerDir + " " + pngOutputDir + layerDir + "\r\n";
        // System.out.print(cmd);
        return cmd;
    }

    @Value("${terrariumPath}")
    private String terrariumPath;

    @Test
    void loadTerrariumMbtilesPath() {

        List<String> files = FileUtils.getFiles(terrariumPath);
        for(String file:files)
        {
            System.out.println(file);
        }
        System.out.println("--------------------------------");
        List<String> files1 = new ArrayList<>();
        FileUtils.getFilesContainChild(terrariumPath, files1);
        for(String file:files1)
        {
            System.out.println(file);
        }

    }

}
