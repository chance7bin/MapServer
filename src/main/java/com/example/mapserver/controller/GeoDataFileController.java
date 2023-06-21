package com.example.mapserver.controller;

import com.example.mapserver.entity.dto.ApiResponse;
import com.example.mapserver.entity.dto.UploadShapefileDTO;
import com.example.mapserver.service.IGeoDataFileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 地理数据文件 控制层
 *
 * @author 7bin
 * @date 2023/06/20
 */
@RestController
@RequestMapping("/geofile")
public class GeoDataFileController {


    @Autowired
    private IGeoDataFileService geoDataFileService;

    @ApiOperation("上传文件")
    @PostMapping
    public ApiResponse uploadGeoDataFile(UploadShapefileDTO uploadShapefileDTO) {
        return geoDataFileService.create(uploadShapefileDTO);
    }

    @GetMapping(value = "/list")
    public ApiResponse getFilesInfoInCatalog() {
        return geoDataFileService.getList();
    }


    @ApiOperation("字段")
    @GetMapping("/getFields")
    public ApiResponse getFields(@RequestParam("geoId") String geoId) {
        return geoDataFileService.getFields(geoId);
    }


    @ApiOperation("获取字段唯一值数组")
    @GetMapping("/getUniqueValues")
    public ApiResponse getUniqueValues(@RequestParam("ptName") String ptName,
                                       @RequestParam("field") String field,
                                       @RequestParam("method") String method) {
        return geoDataFileService.getUniqueValues(ptName, field, method);
    }

}
