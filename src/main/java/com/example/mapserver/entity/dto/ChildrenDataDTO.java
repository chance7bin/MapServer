package com.example.mapserver.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 7bin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildrenDataDTO {
    private String id;
    private String name;
    private String fileType;
    private String dataType;
    private Date date;
}
