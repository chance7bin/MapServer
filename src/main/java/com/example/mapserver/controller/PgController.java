package com.example.mapserver.controller;

import com.example.mapserver.service.IPgService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 使用PostGIS生成的瓦片服务
 *
 * @author 7bin
 * @date 2023/06/19
 */
@Api(tags = "地图瓦片接口_pg")
@RestController
@RequestMapping()
@Slf4j
public class PgController {

    @Autowired
    private IPgService pgService;

    @GetMapping(value = "/mvt/{tableName}/{zoom}/{x}/{y}.pbf")
    public void getMvt(@PathVariable("tableName") String tableName,
                       @PathVariable("zoom") int zoom,
                       @PathVariable("x") int x,
                       @PathVariable("y") int y,
                       HttpServletResponse response) {

        pgService.getMvt(zoom, x, y, tableName, response);
    }


}
