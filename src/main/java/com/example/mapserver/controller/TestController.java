package com.example.mapserver.controller;

import com.example.mapserver.entity.dto.ApiResponse;
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
 * @author 7bin
 * date 2023/06/19
 */
@Api(tags = "测试接口")
@RestController
@RequestMapping(value = "/test")
@Slf4j
public class TestController {

    @Autowired
    ITilesService tilesService;

    @GetMapping("/hello")
    public ApiResponse hello() {
        return ApiResponse.success();
    }

    // @ApiOperation(value = "得到terrarium瓦片" )
    // @GetMapping("/terrarium/{z}/{x}/{y}")
    // public void getTerrariumTiles(
    //     @ApiParam(name = "z", value = "zoom_level") @PathVariable int z,
    //     @ApiParam(name = "x", value = "tile_column") @PathVariable int x,
    //     @ApiParam(name = "y", value = "tile_row") @PathVariable int y ,
    //     HttpServletResponse response){
    //
    //
    //     TilesDTO tilesDTO = new TilesDTO();
    //     tilesDTO.setTile_column(x);
    //     // tilesDTO.setTile_row(y);
    //     tilesDTO.setTile_row((int)(Math.pow(2,z)-1-y));
    //     tilesDTO.setZoom_level(z);
    //
    //     tilesService.getTerrariumTiles(tilesDTO, response);
    //
    // }

    @ApiOperation(value = "测试瓦片")
    @PostMapping("/test/terrarium/{z}/{x}/{y}")
    public void testTerrariumTiles(
        @ApiParam(name = "z", value = "zoom_level") @PathVariable int z,
        @ApiParam(name = "x", value = "tile_column") @PathVariable int x,
        @ApiParam(name = "y", value = "tile_row") @PathVariable int y,
        @RequestBody String mbtilesPath,
        // @ApiParam(name = "path", value = "path") @PathVariable String path,
        HttpServletResponse response) {


        TilesDTO tilesDTO = new TilesDTO();
        tilesDTO.setTile_column(x);
        // tilesDTO.setTile_row(y);
        tilesDTO.setTile_row((int) (Math.pow(2, z) - 1 - y));
        tilesDTO.setZoom_level(z);

        tilesService.testTerrariumTiles(tilesDTO, mbtilesPath, response);

    }

}
