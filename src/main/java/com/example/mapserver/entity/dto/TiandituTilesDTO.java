package com.example.mapserver.entity.dto;

import com.example.mapserver.entity.enums.LayerEnum;
import lombok.Data;

/**
 * @author 7bin
 */
@Data
public class TiandituTilesDTO extends TilesDTO{
    LayerEnum tile_Layer;
}
