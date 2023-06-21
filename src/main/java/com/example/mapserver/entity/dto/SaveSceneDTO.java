package com.example.mapserver.entity.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 7bin
 */
@Data
public class SaveSceneDTO {

    private String sceneId;

    private MultipartFile sceneImg;

    private String sceneType;

    private String lastUpdatedTime;

    private String editNum;

//    private SaveServuceDataVSceneConfigDTO sceneConfig;
}
