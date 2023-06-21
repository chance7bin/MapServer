package com.example.mapserver.entity.po;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;

/**
 * @author 7bin
 */
@Data
public class ServiceDataVSceneTool {
    @Id
    private String id;
    private String name;
    private String label;
    private String description;
    private Binary toolImg;
}
