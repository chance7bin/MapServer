package com.example.mapserver.service;

/**
 * geoserver服务器 服务层
 *
 * @author 7bin
 * @date 2023/06/17
 */
public interface IGeoServerService {

    String publishTilesByShp(String sfname);

    String getWMSByLayerName(String workspace, String layerName);
}
