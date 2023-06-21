package com.example.mapserver.entity.dto;

import com.example.mapserver.entity.po.MapSceneLayer;
import lombok.Data;

import java.util.List;

/**
 * @author 7bin
 */
@Data
public class SaveServuceDataVSceneConfigDTO {
    private String id;
    private Double bearing;
    private Double pitch;
    private Double zoom;
    private List<Double> center;
    private List<Double> sceneEnvelop;
    private List<MapSceneLayer> sceneLayerGroup;
}
