package com.example.mapserver.service.impl;

import com.example.mapserver.exception.ServiceException;
import com.example.mapserver.service.IGeoServerService;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStoreManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

/**
 * @author 7bin
 */
@Slf4j
@Service
public class GeoServerServiceImpl implements IGeoServerService {

    @Autowired(required = false)
    @Qualifier("geoServerManager")
    Optional<GeoServerRESTManager> manager;

    @Value("${resourcesPath}")
    private String resourcesPath;

    @Value("${geoserver.url}")
    private String url;

    @Value("${geoserver.workspace}")
    private String workspace;

    @Override
    public String publishTilesByShp(String sfname) {

        return publishShp(sfname);

    }

    @Override
    public String getWMSByLayerName(String workspace, String layerName) {
        return getWMSUrl(workspace, layerName);
    }


    /**
     * 将shapefile文件发布为geoserver服务
     *
     * @param sfname shp压缩文件名称 必须是zip压缩包
     * @return {@link String}
     * @author 7bin
     **/
    public String publishShp(String sfname) {
        // String url = "http://172.21.212.240:8008/geoserver";    //geoserver的地址
        // String un = "admin";         //geoserver的账号
        // String pw = "geoserver";     //geoserver的密码

        // String workspace = "shapefile";     //工作区名称
        // String storename = "test";     //数据源名称（与图层名称相同）
        // String layername = "bus_point";     //发布的图层名称，此名称必须和压缩包的名称一致
        // 如果不是zip压缩包，抛出异常
        if (!sfname.endsWith(".zip")) {
            throw new ServiceException("文件格式必须是zip包");
        }

        //shp文件压缩包，必须是zip压缩包，且shp文件(.shp、.dbf、.shx等)外层不能有文件夹，且压缩包名称需要与shp图层名称一致
        String zipFilePath = resourcesPath + "/zip/" + sfname;

        String storename = sfname.substring(0, sfname.lastIndexOf("."));
        String layername = storename;

        if (!manager.isPresent()) {
            // log.error("GeoServerRESTManager 未初始化");
            throw new ServiceException("GeoServerRESTManager 未初始化");
        }
        GeoServerRESTManager geoServerRESTManager = manager.get();
        GeoServerRESTReader reader = geoServerRESTManager.getReader();
        GeoServerRESTPublisher publisher = geoServerRESTManager.getPublisher();
        GeoServerRESTStoreManager storeManager = geoServerRESTManager.getStoreManager();

        //  2、判断是否有工作区，没有则创建
        boolean existsWorkspace = reader.existsWorkspace(workspace);
        if (!existsWorkspace) {
            boolean b = publisher.createWorkspace(workspace);
            if (!b) {
                // log.error("工作区创建失败");
                throw new ServiceException("工作区创建失败");
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
                if (!b) {
                    // log.error(workspace + ":" + storename + "发布失败");
                    throw new ServiceException(workspace + ":" + storename + "发布失败");
                } else {
                    log.info(workspace + ":" + storename + "发布成功");
                }
            } catch (FileNotFoundException e) {
                throw new ServiceException(e.getMessage());
            }
        } else {
            // log.error(workspace + ":" + storename + "已存在");
            throw new ServiceException(workspace + ":" + storename + "已存在");
        }

        // String wmsUrl = url + "/" + workspace + "/wms?service=WMS&version=1.1.0&request=GetMap&layers=" + workspace + ":" + layername + "&styles=&bbox={bbox-epsg-3857}&width=256&height=256&srs=EPSG:3857&format=image/png&TRANSPARENT=TRUE";

        return getWMSUrl(workspace, layername);

    }

    private String getWMSUrl(String workspace, String layerName) {
        String wmsUrl = url + "/" + workspace + "/wms?service=WMS&version=1.1.0&request=GetMap&layers=" + workspace + ":" + layerName + "&styles=&bbox={bbox-epsg-3857}&width=256&height=256&srs=EPSG:3857&format=image/png&TRANSPARENT=TRUE";
        return wmsUrl;
    }
}
