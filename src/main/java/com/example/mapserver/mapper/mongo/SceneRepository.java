package com.example.mapserver.mapper.mongo;

import com.example.mapserver.entity.po.Scene;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SceneRepository extends MongoRepository<Scene, String> {
    List<Scene> findAllByUserId(String userId);

    Scene findSceneBySceneId(String sceneId);
}
