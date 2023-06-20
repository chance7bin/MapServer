package com.example.mapserver.config;

import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

/**
 * GeoServer配置
 *
 * @author 7bin
 * @date 2023/06/17
 */
@Slf4j
@Configuration
public class GeoServerConfig {

    @Value("${geoserver.url}")
    private String url;

    @Value("${geoserver.username}")
    private String username;

    @Value("${geoserver.password}")
    private String password;

    // @ConditionalOnProperty(value = "map.server.type", havingValue = "geoserver")
    @Bean(name = "geoServerManager")
    GeoServerRESTManager geoServerManager() {
        // String url = "http://172.21.212.240:8008/geoserver";    //geoserver的地址
        // String un = "admin";         //geoserver的账号
        // String pw = "geoserver";     //geoserver的密码

        //  1、获取geoserver连接对象
        GeoServerRESTManager manager = null;

        try {
            manager = new GeoServerRESTManager(new URL(url), username, password);
            log.info("geoserver服务器连接成功");
        } catch (Exception e) {
            log.error("geoserver服务器连接失败");
        }

        return manager;
    }

}
