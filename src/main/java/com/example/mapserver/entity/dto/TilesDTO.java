package com.example.mapserver.entity.dto;

import lombok.Data;

/**
 * @Description
 * @Author bin
 * @Date 2021/12/09
 */
@Data
public class TilesDTO {
    int zoom_level;
    int tile_column;
    int tile_row;

}
