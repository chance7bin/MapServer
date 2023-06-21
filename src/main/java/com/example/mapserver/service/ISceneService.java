package com.example.mapserver.service;

import com.example.mapserver.entity.dto.ApiResponse;
import com.example.mapserver.entity.dto.CreateSceneDTO;
import com.example.mapserver.entity.dto.SaveSceneDTO;

/**
 * @author 7bin
 */
public interface ISceneService {

    ApiResponse createScene(CreateSceneDTO createSceneDTO);

    ApiResponse getSceneList();

    ApiResponse getSceneConfig(String sceneType, String sceneId);


    ApiResponse saveScene(SaveSceneDTO saveSceneDTO, Object sceneConfig);


}
