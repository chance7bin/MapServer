package com.example.mapserver.entity.dto;

import com.example.mapserver.entity.enums.LayerEnum;
import lombok.Data;

/**
 * @Description
 * @Author bin
 * @Date 2022/05/02
 */
@Data
public class TiandituTilesDTO extends TilesDTO{
    LayerEnum tile_Layer;
}
