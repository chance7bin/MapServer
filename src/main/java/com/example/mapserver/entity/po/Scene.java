package com.example.mapserver.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

/**
 * @author 7bin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Scene {
    @Id
    private String sceneId;
    private String sceneName;
    private String sceneType;
    private String createTime;
    private String lastUpdatedTime;
    private List<Map<String, String>> dataSet;
    private List<Map<String, Object>> toolSet;
    private String userId;
    private Integer editNum;
    private Binary sceneImg;
}
