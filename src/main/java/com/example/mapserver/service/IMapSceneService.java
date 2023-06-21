package com.example.mapserver.service;

import com.example.mapserver.entity.po.GeoDataFile;
import lombok.NonNull;

import java.util.List;

/**
 * @author 7bin
 */
public interface IMapSceneService {

    String getLayerVisualType(String geoType);

    List<Double> getMutiBoundsMinEnvelop(@NonNull List<GeoDataFile> layers);

    List<Double> getCenter(List<Double> bounds);

}
