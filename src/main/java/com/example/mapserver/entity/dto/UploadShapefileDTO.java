package com.example.mapserver.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 7bin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadShapefileDTO {
    private MultipartFile file;
    private String fileName;
    private Integer srid;
    private String code;
    private String userId;
    private String catalogId;
}
