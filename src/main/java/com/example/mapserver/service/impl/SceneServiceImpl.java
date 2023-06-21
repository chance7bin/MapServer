package com.example.mapserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.mapserver.entity.dto.ApiResponse;
import com.example.mapserver.entity.dto.CreateSceneDTO;
import com.example.mapserver.entity.dto.SaveSceneDTO;
import com.example.mapserver.entity.dto.SaveServuceDataVSceneConfigDTO;
import com.example.mapserver.entity.po.Scene;
import com.example.mapserver.entity.po.ServiceDataVSceneConfig;
import com.example.mapserver.mapper.mongo.SceneRepository;
import com.example.mapserver.service.ISceneService;
import com.example.mapserver.service.IServiceDataVSceneService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.lang.Integer.parseInt;

/**
 * @author 7bin
 */
@Service
@Slf4j
public class SceneServiceImpl implements ISceneService {

    @Autowired
    private SceneRepository sceneRepository;

    @Autowired
    private IServiceDataVSceneService serviceDataVSceneService;


    @Override
    public ApiResponse createScene(CreateSceneDTO createSceneDTO) {
        Scene scene = new Scene();
        scene.setSceneId(UUID.randomUUID().toString());
        scene.setSceneName(createSceneDTO.getName());
        scene.setSceneType(createSceneDTO.getType());
        scene.setDataSet(createSceneDTO.getDataSet());
        scene.setToolSet(createSceneDTO.getToolSet());
        scene.setUserId(createSceneDTO.getUserId());
        scene.setEditNum(0);
        Date date = new Date();
        String newData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        scene.setCreateTime(newData);
        scene.setLastUpdatedTime(newData);
        scene.setSceneImg(null);
        sceneRepository.insert(scene);
        switch (createSceneDTO.getType()) {
            case "visualization_service":
                return serviceDataVSceneService.createServiceDataVConfig(scene);
            default:
                return new ApiResponse(201, "未知的错误！");
        }
    }

    @Override
    public ApiResponse getSceneList() {
        List<Scene> sceneList = sceneRepository.findAll();
        return new ApiResponse(200, "获取成功！", sceneList);
    }

    @Override
    public ApiResponse getSceneConfig(String sceneType, String sceneId) {
        switch (sceneType) {
            case "visualization_service":
                ServiceDataVSceneConfig serviceDataVSceneConfig = serviceDataVSceneService.getServiceDataVSceneConfigBySceneId(sceneId);
                return new ApiResponse(200, "获取成功", serviceDataVSceneConfig);
            default:
                return new ApiResponse(201, "获取失败");
        }
    }


    @Override
    public ApiResponse saveScene(SaveSceneDTO saveSceneDTO, Object sceneConfig) {
        Scene scene = sceneRepository.findSceneBySceneId(saveSceneDTO.getSceneId());
        try {
            // scene.setSceneImg(new Binary(saveSceneDTO.getSceneImg().getBytes()));
            scene.setEditNum(parseInt(saveSceneDTO.getEditNum()));
            scene.setLastUpdatedTime(saveSceneDTO.getLastUpdatedTime());
            sceneRepository.save(scene);
            switch (saveSceneDTO.getSceneType()) {
                case "visualization_service":
                    serviceDataVSceneService.saveSceneConfig((SaveServuceDataVSceneConfigDTO) sceneConfig);
                    break;
                default:
                    return new ApiResponse(201, "保存失败");
            }
            return new ApiResponse(200, "场景保存成功！");
            // } catch (IOException e) {
        } catch (Exception e) {
            return new ApiResponse(201, e.getMessage());
        }
    }

}
