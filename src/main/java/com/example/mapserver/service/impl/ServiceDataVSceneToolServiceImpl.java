package com.example.mapserver.service.impl;

import com.example.mapserver.entity.dto.ApiResponse;
import com.example.mapserver.entity.dto.CreateServiceDataVSceneToolDTO;
import com.example.mapserver.entity.po.ServiceDataVSceneTool;
import com.example.mapserver.mapper.mongo.ServiceDataVSceneToolRepository;
import com.example.mapserver.service.IServiceDataVSceneToolService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author 7bin
 */
@Service
@Slf4j
public class ServiceDataVSceneToolServiceImpl implements IServiceDataVSceneToolService {

    @Autowired
    ServiceDataVSceneToolRepository serviceDataVSceneToolRepository;

    @Override
    public ApiResponse create(CreateServiceDataVSceneToolDTO createServiceDataVSceneToolDTO) {
        ServiceDataVSceneTool serviceDataVSceneTool = new ServiceDataVSceneTool();
        serviceDataVSceneTool.setId(UUID.randomUUID().toString());
        serviceDataVSceneTool.setName(createServiceDataVSceneToolDTO.getName());
        serviceDataVSceneTool.setLabel(createServiceDataVSceneToolDTO.getLabel());
        serviceDataVSceneTool.setDescription(createServiceDataVSceneToolDTO.getDescription());
        try {
            // serviceDataVSceneTool.setToolImg(new Binary(createServiceDataVSceneToolDTO.getToolImg().getBytes()));
            serviceDataVSceneToolRepository.save(serviceDataVSceneTool);
            return new ApiResponse(200, "创建工具成功！");
            // } catch (IOException e) {
        } catch (Exception e) {
            return new ApiResponse(201, "创建工具失败！");
        }
    }

    @Override
    public ApiResponse getServiceDataVSceneToolList() {
        List<ServiceDataVSceneTool> toolList = serviceDataVSceneToolRepository.findAll();
        return new ApiResponse(200, "获取成功！", toolList);
    }

}
