package com.example.mapserver.mapper.mongo;

import com.example.mapserver.entity.po.ServiceDataVSceneTool;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceDataVSceneToolRepository extends MongoRepository<ServiceDataVSceneTool, String> {

    ServiceDataVSceneTool findServiceDataVSceneToolById(String id);
}
