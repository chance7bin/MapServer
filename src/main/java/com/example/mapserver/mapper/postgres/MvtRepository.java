package com.example.mapserver.mapper.postgres;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


/**
 * mvt数据层
 *
 * @author 7bin
 */
@Slf4j
@Repository
public class MvtRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public byte[] getMvtFromDefaultPg(String sql) {
        try {
            byte[] reByte = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getBytes("st_asmvt"));
            return reByte;
        } catch (Exception e) {
            log.error("默认数据库瓦片获取失败" + e.getMessage());
            return null;
        }
    }
}
