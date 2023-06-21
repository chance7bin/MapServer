package com.example.mapserver.mapper.mongo;

import com.example.mapserver.entity.po.ServiceDataVSceneConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceDataVSceneConfigRepository extends MongoRepository<ServiceDataVSceneConfig, String> {

    ServiceDataVSceneConfig findServiceDataVSceneConfigBySceneId(String sceneId);

    ServiceDataVSceneConfig findServiceDataVSceneConfigById(String id);
}
