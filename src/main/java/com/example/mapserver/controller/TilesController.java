package com.example.mapserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.mapserver.entity.dto.TilesDTO;
import com.example.mapserver.service.ITilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author bin
 * @Date 2022/03/25
 */
@Api(tags = "地图瓦片接口")
@RestController
@RequestMapping(value = "/tiles")
@Slf4j
public class TilesController {

    @Autowired
    ITilesService tilesService;





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

    @ApiOperation(value = "得到terrarium瓦片" )
    @GetMapping("/terrarium/{z}/{x}/{y}")
    public void getTerrariumTiles(
        @ApiParam(name = "z", value = "zoom_level") @PathVariable int z,
        @ApiParam(name = "x", value = "tile_column") @PathVariable int x,
        @ApiParam(name = "y", value = "tile_row") @PathVariable int y ,
        HttpServletResponse response){


        TilesDTO tilesDTO = new TilesDTO();
        tilesDTO.setTile_column(x);
        // tilesDTO.setTile_row(y);
        tilesDTO.setTile_row((int)(Math.pow(2,z)-1-y));
        tilesDTO.setZoom_level(z);

        tilesService.getTerrariumTiles(tilesDTO, response);

    }

    @ApiOperation(value = "测试瓦片" )
    @PostMapping("/test/terrarium/{z}/{x}/{y}")
    public void testTerrariumTiles(
        @ApiParam(name = "z", value = "zoom_level") @PathVariable int z,
        @ApiParam(name = "x", value = "tile_column") @PathVariable int x,
        @ApiParam(name = "y", value = "tile_row") @PathVariable int y ,
        @RequestBody String mbtilesPath,
        // @ApiParam(name = "path", value = "path") @PathVariable String path,
        HttpServletResponse response){


        TilesDTO tilesDTO = new TilesDTO();
        tilesDTO.setTile_column(x);
        // tilesDTO.setTile_row(y);
        tilesDTO.setTile_row((int)(Math.pow(2,z)-1-y));
        tilesDTO.setZoom_level(z);

        tilesService.testTerrariumTiles(tilesDTO, mbtilesPath, response);

    }


    // "https://api.maptiler.com/tiles/v3/tiles.json?key=XAapkmkXQpx839NCfnxD"
    @ApiOperation(value = "得到mapbox元数据json" )
    @GetMapping("/mapbox/metadata/tiles.json")
    public JSONObject getMapboxTilesMetadataJson(){

        return tilesService.getMapboxTilesMetadataJson();

    }



    @ApiOperation(value = "得到mapbox元数据json" )
    @GetMapping("/mapbox/liberty.json")
    public JSONObject getMapboxLibertyJson(){

        return tilesService.getMapboxLibertyJson();

    }


}