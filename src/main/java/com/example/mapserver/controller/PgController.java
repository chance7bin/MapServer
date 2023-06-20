package com.example.mapserver.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 使用PostGIS生成的瓦片服务
 *
 * @author 7bin
 * @date 2023/06/19
 */
@Api(tags = "地图瓦片接口_pg")
@RestController
@RequestMapping(value = "/pg")
@Slf4j
public class PgController {


}
