package com.example.mapserver.service;

import com.example.mapserver.entity.dto.ApiResponse;
import com.example.mapserver.entity.dto.CreateServiceDataVSceneToolDTO;

/**
 * @author 7bin
 * @date 2023/06/20
 */
public interface IServiceDataVSceneToolService {

    ApiResponse create(CreateServiceDataVSceneToolDTO createServiceDataVSceneToolDTO);

    ApiResponse getServiceDataVSceneToolList();
}
