package com.example.mapserver.entity.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 7bin
 */
@Data
public class CreateServiceDataVSceneToolDTO {
    private String name;
    private String label;
    private String description;
    private MultipartFile toolImg;
}
