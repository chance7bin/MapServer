package com.example.mapserver.entity.dto;

import lombok.Data;

/**
 * @author 7bin
 */
@Data
public class TilesDTO {
    int zoom_level;
    int tile_column;
    int tile_row;
}
