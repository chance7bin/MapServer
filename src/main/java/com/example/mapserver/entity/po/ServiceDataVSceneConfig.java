package com.example.mapserver.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @author 7bin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDataVSceneConfig {
    @Id
    private String id;
    private String sceneId;
    private Double bearing;
    private Double pitch;
    private Double zoom;
    private List<Double> sceneEnvelop;
    private List<Double> center;
    private List<ServiceDataVSceneTool> sceneToolLab;
    private List<MapSceneLayer> sceneLayerGroup;    //["layerId1","layerId2","layerId3","layerId4"]
}
