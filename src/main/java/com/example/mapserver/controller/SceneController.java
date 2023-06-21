package com.example.mapserver.controller;

import com.example.mapserver.entity.dto.ApiResponse;
import com.example.mapserver.entity.dto.CreateSceneDTO;
import com.example.mapserver.entity.dto.SaveSceneDTO;
import com.example.mapserver.entity.dto.SaveServuceDataVSceneConfigDTO;
import com.example.mapserver.service.ISceneService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 7bin
 * @date 2023/06/19
 */
@Api(value = "/scene", tags = {""})
@RestController
@RequestMapping("/scene")
public class SceneController {

    @Autowired
    private ISceneService sceneService;

    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body", dataType = "CreateSceneDTO", name = "createSceneDTO", value = "", required = true)
    })
    @ApiOperation(value = "", notes = "", httpMethod = "POST")
    @PostMapping
    public ApiResponse createScene(@RequestBody CreateSceneDTO createSceneDTO) {
        return sceneService.createScene(createSceneDTO);
    }

    @ApiOperation(value = "", notes = "", httpMethod = "GET")
    @GetMapping(value = "/list")
    public ApiResponse getSceneList() {
        return sceneService.getSceneList();
    }

    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", dataType = "string", name = "sceneType", value = "", required = true),
        @ApiImplicitParam(paramType = "query", dataType = "string", name = "sceneId", value = "", required = true)
    })
    @ApiOperation(value = "", notes = "", httpMethod = "GET")
    @GetMapping("/config")
    public ApiResponse getSceneConfig(@RequestParam("sceneType") String sceneType,
                                      @RequestParam("sceneId") String sceneId) {
        return sceneService.getSceneConfig(sceneType, sceneId);
    }


    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", dataType = "SaveSceneDTO", name = "saveSceneDTO", value = "", required = true),
        @ApiImplicitParam(paramType = "query", dataType = "string", name = "sceneConfig", value = "", required = true)
    })
    @ApiOperation(value = "", notes = "", httpMethod = "POST")
    @PostMapping(value = "/save")
    public ApiResponse saveScene(SaveSceneDTO saveSceneDTO, @RequestParam("sceneConfig") String sceneConfig) {
        System.out.println("sceneConfig = " + sceneConfig);
        ObjectMapper objectMapper = new ObjectMapper();
        SaveServuceDataVSceneConfigDTO saveServuceDataVSceneConfigDTO = null;
        try {
            saveServuceDataVSceneConfigDTO = objectMapper.readValue(sceneConfig, SaveServuceDataVSceneConfigDTO.class);
            System.out.println("saveServuceDataVSceneConfigDTO = " + saveServuceDataVSceneConfigDTO);
            System.out.println("testData = " + saveSceneDTO);
            return sceneService.saveScene(saveSceneDTO, saveServuceDataVSceneConfigDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        System.out.println("sceneImg = " + sceneImg);
//        System.out.println("lastUpdatedTime = " + lastUpdatedTime);
//        System.out.println("editNum = " + editNum);
    }
}
