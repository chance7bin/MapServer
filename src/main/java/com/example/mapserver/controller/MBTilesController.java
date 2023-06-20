package com.example.mapserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.mapserver.entity.dto.ApiResponse;
import com.example.mapserver.entity.dto.TiandituTilesDTO;
import com.example.mapserver.entity.dto.TilesDTO;
import com.example.mapserver.entity.enums.LayerEnum;
import com.example.mapserver.service.IGeoServerService;
import com.example.mapserver.service.ITilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 使用mbtiles生成的瓦片服务
 *
 * @Description
 * @Author bin
 * @Date 2022/03/25
 */
@Api(tags = "地图瓦片接口_mbtiles")
@RestController
@RequestMapping(value = "/mbtiles")
@Slf4j
public class MBTilesController {

    @Autowired
    ITilesService tilesService;

    @ApiOperation(value = "得到天地图瓦片")
    @GetMapping("/tianditu/{layer}/{z}/{x}/{y}")
    public void getTiandituTiles(
        @ApiParam(name = "layer", value = "加载的图层") @PathVariable LayerEnum layer,
        @ApiParam(name = "z", value = "zoom_level") @PathVariable int z,
        @ApiParam(name = "x", value = "tile_column") @PathVariable int x,
        @ApiParam(name = "y", value = "tile_row") @PathVariable int y ,
        HttpServletResponse response){


        // int zoom_level = z;
        // int tile_column = x;
        // int tile_row = y;

        TiandituTilesDTO tilesDTO = new TiandituTilesDTO();
        tilesDTO.setTile_column(x);
        tilesDTO.setTile_row(y);
        // tilesDTO.setTile_row((int)(Math.pow(2,z)-1-y));
        tilesDTO.setZoom_level(z);
        tilesDTO.setTile_Layer(layer);

        tilesService.getTiandituTiles(tilesDTO, response);

    }




    @ApiOperation(value = "得到mapbox瓦片" )
    @GetMapping("/mapbox/{z}/{x}/{y}.pbf")
    public void getMapboxTiles(
        @ApiParam(name = "z", value = "zoom_level") @PathVariable int z,
        @ApiParam(name = "x", value = "tile_column") @PathVariable int x,
        @ApiParam(name = "y", value = "tile_row") @PathVariable int y ,
        HttpServletResponse response){


        TilesDTO tilesDTO = new TilesDTO();
        tilesDTO.setTile_column(x);
        tilesDTO.setTile_row((int)(Math.pow(2,z)-1-y));
        tilesDTO.setZoom_level(z);

        tilesService.getMapboxTiles(tilesDTO, response);

    }


    // "https://api.maptiler.com/tiles/v3/tiles.json?key=XAapkmkXQpx839NCfnxD"
    @ApiOperation(value = "得到mapbox的tiles.json")
    @GetMapping("/mapbox/metadata/tiles.json")
    public JSONObject getMapboxTilesMetadataJson(){

        return tilesService.getMapboxTilesMetadataJson();

    }


    @ApiOperation(value = "得到mapbox的osm_liberty.json")
    @GetMapping("/mapbox/liberty.json")
    public JSONObject getMapboxLibertyJson(){

        return tilesService.getMapboxLibertyJson();

    }


}
