package com.example.mapserver.service.impl;

import com.example.mapserver.entity.po.GeoDataFile;
import com.example.mapserver.service.IMapSceneService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 7bin
 */
@Service
@Slf4j
public class MapSceneServiceImpl implements IMapSceneService {

    @Override
    public List<Double> getMutiBoundsMinEnvelop(@NonNull List<GeoDataFile> layers) {
        List<Double> mutiBoundsMinEnvelop = layers.get(0).getBounds();
        layers.forEach(layer -> {
            List<Double> bounds = layer.getBounds();
            if (bounds.get(0) < mutiBoundsMinEnvelop.get(0)) {
                mutiBoundsMinEnvelop.set(0, bounds.get(0));
            }
            if (bounds.get(1) < mutiBoundsMinEnvelop.get(1)) {
                mutiBoundsMinEnvelop.set(1, bounds.get(1));
            }
            if (bounds.get(2) > mutiBoundsMinEnvelop.get(2)) {
                mutiBoundsMinEnvelop.set(2, bounds.get(2));
            }
            if (bounds.get(3) > mutiBoundsMinEnvelop.get(3)) {
                mutiBoundsMinEnvelop.set(3, bounds.get(3));
            }
        });
        return mutiBoundsMinEnvelop;
    }

    @Override
    public List<Double> getCenter(List<Double> bounds) {
        ArrayList<Double> center = new ArrayList<>();
        center.add((bounds.get(0) + bounds.get(2)) / 2);
        center.add((bounds.get(1) + bounds.get(3)) / 2);
        return center;
    }

    @Override
    public String getLayerVisualType(String geoType) {
        switch (geoType) {
            case "MULTILINESTRING":
                return "line";
            case "POINT":
                return "circle";
            case "MULTIPOLYGON":
                return "fill";
            default:
                return null;
        }
    }
}
