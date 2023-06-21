package com.example.mapserver.service;

import com.example.mapserver.entity.dto.ApiResponse;
import com.example.mapserver.entity.dto.SaveServuceDataVSceneConfigDTO;
import com.example.mapserver.entity.po.Scene;
import com.example.mapserver.entity.po.ServiceDataVSceneConfig;

/**
 * @author 7bin
 */
public interface IServiceDataVSceneService {

    ApiResponse createServiceDataVConfig(Scene scene);

    ServiceDataVSceneConfig getServiceDataVSceneConfigBySceneId(String sceneId);

    void saveSceneConfig(SaveServuceDataVSceneConfigDTO sceneConfig);

}
