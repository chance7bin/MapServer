package com.example.mapserver.service;

/**
 * geoserver服务器 服务层
 *
 * @author 7bin
 */
public interface IGeoServerService {

    String publishTilesByShp(String sfname);

    String getWMSByLayerName(String workspace, String layerName);
}
