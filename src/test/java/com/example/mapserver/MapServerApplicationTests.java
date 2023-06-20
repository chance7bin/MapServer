package com.example.mapserver;

import com.example.mapserver.utils.FileUtils;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSShapefileDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStoreManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
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
        for (String file : files1) {
            System.out.println(file);
        }

    }

    @Test
    void publish() {
        publishShp();
    }

    /**
     * 将shapefile文件发布为geoserver服务
     *
     * @return
     */
    public void publishShp() {
        String url = "http://172.21.212.240:8008/geoserver";    //geoserver的地址
        String un = "admin";         //geoserver的账号
        String pw = "geoserver";     //geoserver的密码

        String workspace = "shapefile";     //工作区名称
        String storename = "test";     //数据源名称
        String layername = "bus_point";     //发布的图层名称，此名称必须和压缩包的名称一致

        //shp文件压缩包，必须是zip压缩包，且shp文件(.shp、.dbf、.shx等)外层不能有文件夹，且压缩包名称需要与shp图层名称一致
        String zipFilePath = "E:\\GIS_Data\\chengdu\\bus_point.zip";

        //  1、获取geoserver连接对象
        GeoServerRESTManager manager = null;

        try {
            manager = new GeoServerRESTManager(new URL(url), un, pw);
            System.out.println("连接geoserver服务器成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("geoserver服务器连接失败");
            return;
        }

        GeoServerRESTReader reader = manager.getReader();
        GeoServerRESTPublisher publisher = manager.getPublisher();
        GeoServerRESTStoreManager storeManager = manager.getStoreManager();

        //  2、判断是否有工作区，没有则创建
        boolean b2 = reader.existsWorkspace(workspace);
        if (!b2) {
            boolean b = publisher.createWorkspace(workspace);
            if (!b) {
                System.out.println("工作区创建失败");
                return;
            }
        }

        //  3、判断是否有数据源，没有则创建
        //  4、发布图层，如果存在就不发布
        //  创建数据源 和 发布图层服务可以一步进行
        RESTDataStore datastore = reader.getDatastore(workspace, storename);
        RESTLayer layer = reader.getLayer(workspace, layername);
        if (layer == null && datastore == null) {
            File file = new File(zipFilePath);
            // 进行发布；参数依次为：工作区名称、数据源名称、图层名称、shp文件压缩文件对象、坐标系
            boolean b = false;
            try {
                b = publisher.publishShp(workspace, storename, layername, file, GeoServerRESTPublisher.DEFAULT_CRS);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (!b) {
                System.out.println("shp图层发布失败");
            } else {
                System.out.println("shp图层发布成功");
            }
        }

    }


}
