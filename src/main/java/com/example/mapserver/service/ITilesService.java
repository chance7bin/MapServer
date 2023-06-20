package com.example.mapserver.service;

import com.alibaba.fastjson.JSONObject;
import com.example.mapserver.entity.dto.TiandituTilesDTO;
import com.example.mapserver.entity.dto.TilesDTO;

import javax.servlet.http.HttpServletResponse;

/**
 * 瓦片地图 服务层
 *
 * @Author bin
 * @Date 2022/03/25
 */
public interface ITilesService {

    void getMapboxTiles(TilesDTO tilesDTO, HttpServletResponse response);

    void getTerrariumTiles(TilesDTO tilesDTO, HttpServletResponse response);

    JSONObject getMapboxTilesMetadataJson();

    JSONObject getMapboxLibertyJson();

    void testTerrariumTiles(TilesDTO tilesDTO, String mbtilesPath, HttpServletResponse response);

    void getTiandituTiles(TiandituTilesDTO tilesDTO, HttpServletResponse response);


}
