package com.example.mapserver.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * 瓦片DTO
 *
 * @author 7bin
 **/
@Data
public class TileVectorLayersDTO {
    String id;
    List<String> field;
    int minzoom;
    int maxzoom;
    List<Double> bounds;
//    List<JSONObject> vector_layers; //[{id:"",field:[],min:0,max:22,bounds:[-180,85,180,85]}]
}
