package com.example.mapserver.entity.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 7bin
 */
@Data
public class CreateSceneDTO {
    private String name;
    private String type;
    private String userId;
    private List<Map<String, String>> dataSet;
    private List<Map<String, Object>> toolSet;
}
