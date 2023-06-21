package com.example.mapserver.service;

import com.example.mapserver.entity.dto.ApiResponse;
import com.example.mapserver.entity.dto.UploadShapefileDTO;

/**
 * @author 7bin
 */
public interface IGeoDataFileService {

    ApiResponse create(UploadShapefileDTO uploadShapefileDTO);

    ApiResponse getList();

    ApiResponse getFields(String geoId);

    ApiResponse getUniqueValues(String ptName, String field, String method);
}
