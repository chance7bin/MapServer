package com.example.mapserver.controller;

import com.example.mapserver.entity.dto.ApiResponse;
import com.example.mapserver.entity.dto.CreateServiceDataVSceneToolDTO;
import com.example.mapserver.service.IServiceDataVSceneToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 7bin
 */
@RestController
@RequestMapping("/serviceDataVSceneTool")
public class ServiceDataVSceneToolController {

    @Autowired
    private IServiceDataVSceneToolService serviceDataVSceneToolService;

    @PostMapping
    public ApiResponse create(CreateServiceDataVSceneToolDTO createServiceDataVSceneToolDTO) {
        return serviceDataVSceneToolService.create(createServiceDataVSceneToolDTO);
    }

    @GetMapping("/list")
    public ApiResponse getServiceDataVSceneToolList() {
        return serviceDataVSceneToolService.getServiceDataVSceneToolList();
    }

}
