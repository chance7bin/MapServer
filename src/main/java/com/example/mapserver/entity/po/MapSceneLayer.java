package com.example.mapserver.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author 7bin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapSceneLayer {
    private String id;
    private String layerName;
    private String dataType;
    private String mvtUrl;
    private String type;
    private Map<String, Object> layout;
    private Map<String, Object> paint;
    private String sourceLayer;
}
