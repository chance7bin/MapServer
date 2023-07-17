package com.example.mapserver.controller;

import com.example.mapserver.entity.dto.ApiResponse;
import com.example.mapserver.service.IGeoServerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 使用GeoServer生成的瓦片服务
 *
 * @author 7bin
 * @date 2023/06/19
 */
@Api(tags = "地图瓦片接口_geoserver")
@RestController
@RequestMapping(value = "/geoserver")
@Slf4j
public class GeoServerController {


    @Autowired
    IGeoServerService geoServerService;


    @ApiOperation(value = "shp生成瓦片服务")
    @PostMapping("/wms/publish/{sfname}")
    public ApiResponse publishTilesByShp(@PathVariable(value = "sfname") String sfname) {

        return ApiResponse.success((Object) geoServerService.publishTilesByShp(sfname));

    }

    @ApiOperation(value = "根据工作空间和图层名称返回wms服务地址")
    @GetMapping("/wms/{workspace}/{layerName}")
    public ApiResponse getTilesByShp(
        @PathVariable(value = "layerName") String layerName, @PathVariable(value = "workspace") String workspace) {

        return ApiResponse.success((Object) geoServerService.getWMSByLayerName(workspace, layerName));

    }

}
