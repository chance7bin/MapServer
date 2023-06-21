package com.example.mapserver.mapper.mongo;

import com.example.mapserver.entity.po.GeoDataFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeoDataFileRepository extends MongoRepository<GeoDataFile, String> {
    GeoDataFile findOneByMd5AndUserId(String md5, String userId);

    GeoDataFile findOneByMd5(String md5);

    GeoDataFile findOneById(String id);

    List<GeoDataFile> findAllByUserId(String userId);

}
