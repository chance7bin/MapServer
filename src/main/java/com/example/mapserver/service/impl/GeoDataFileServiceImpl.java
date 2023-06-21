package com.example.mapserver.service.impl;

import cn.hutool.core.util.IdUtil;
import com.example.mapserver.entity.dto.ApiResponse;
import com.example.mapserver.entity.dto.ChildrenDataDTO;
import com.example.mapserver.entity.dto.ReturnGeoDataDTO;
import com.example.mapserver.entity.dto.UploadShapefileDTO;
import com.example.mapserver.entity.po.Catalog;
import com.example.mapserver.entity.po.GeoDataFile;
import com.example.mapserver.mapper.mongo.CatalogRepository;
import com.example.mapserver.mapper.mongo.GeoDataFileRepository;
import com.example.mapserver.mapper.postgres.ShpProcessRepository;
import com.example.mapserver.service.IGeoDataFileService;
import com.example.mapserver.utils.FileUtils;
import com.example.mapserver.utils.unZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.util.*;

/**
 * @author 7bin
 */
@Service
@Slf4j
public class GeoDataFileServiceImpl implements IGeoDataFileService {

    @Value("${resourcesPath}")
    private String resourcesRoot;

    @Autowired
    private ShpProcessRepository shpProcessRepository;

    @Autowired
    private GeoDataFileRepository geoDataFileRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    @Override
    public ApiResponse create(UploadShapefileDTO uploadShapefileDTO) {

        //1、入postgres：文件解压  →   shp →   save
        List<String> unZipFiles = new ArrayList<>();
        String srcFilePath = "";
        String tableName = "";
        GeoDataFile geoDataFile = null;
        try {
            String md5 = DigestUtils.md5DigestAsHex(uploadShapefileDTO.getFile().getInputStream());
            String originalFilename = uploadShapefileDTO.getFile().getOriginalFilename();
            int size = (int) uploadShapefileDTO.getFile().getSize();
            String originalFilenameWithoutPrefix = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            String filePrefix = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
            String fileType;
            String fileTotalName = uploadShapefileDTO.getFileName() + filePrefix;
            String ShpId = IdUtil.objectId();
            tableName = originalFilenameWithoutPrefix + "_" + ShpId;
            GeoDataFile findOneByMd5 = geoDataFileRepository.findOneByMd5(md5);
            if (!Objects.isNull(findOneByMd5)) {
                return new ApiResponse(201, "此文件已在你的仓库！不可重复上传");
            }
            //生成本地目录
            //TODO:封装成工具
            String currentCatalogId = uploadShapefileDTO.getCatalogId();
            String currentParentId = "";
            String catalogPath = "";
            LinkedList<String> catalogIdList = new LinkedList<>();
            catalogIdList.addFirst(uploadShapefileDTO.getCatalogId());
            while ((currentParentId = catalogRepository.getCatalogById(currentCatalogId).getParentId()).compareTo("-1") != 0) {
                currentCatalogId = currentParentId;
                catalogIdList.addFirst(currentCatalogId);
            }
            Iterator<String> it = catalogIdList.iterator();
            while (it.hasNext()) {
                catalogPath = catalogPath + "/" + it.next();
            }
            String uploadPath = resourcesRoot + "/upload";
            File geoFilePath = new File(resourcesRoot + "/upload");
            if (!geoFilePath.exists()) {
                geoFilePath.mkdirs();
            }

            boolean isUploaded = FileUtils.uploadSingleFile(uploadShapefileDTO.getFile(), uploadPath, originalFilename);
            srcFilePath = uploadPath + "/" + originalFilename;
            unZipFiles = unZipUtils.unZipFiles(srcFilePath, uploadPath + "/");
            String shpPath = unZipUtils.selectShpfile(unZipFiles);
            fileType = shpPath.substring(shpPath.lastIndexOf(".") + 1);
            if (!isUploaded || !shpPath.contains(".shp")) {
                throw new Exception("上传文件失败或Shapefile文件不符合规范！");
            }
            //2、入postgres，到这一步说明zip上传成功且shp文件解压完毕，可以入库
            Boolean isSave2Pg = shpProcessRepository.shp2pgsql(shpPath, tableName, uploadShapefileDTO.getSrid().toString(), uploadShapefileDTO.getCode());
            if (!isSave2Pg) {
                throw new Exception("空间数据存入postgresql失败！！！");
            }
            //3、在mongo中记录文件信息
            geoDataFile = new GeoDataFile();
            HashMap<String, String> nameList = new HashMap<>();
            String dataType = shpProcessRepository.getShpType(tableName);
            nameList.put(uploadShapefileDTO.getCatalogId(), originalFilename);
            List<Double> bounds = shpProcessRepository.getShpBox2D(tableName);
            geoDataFile.setId(ShpId);
            geoDataFile.setNameList(nameList);
            geoDataFile.setSize(size);
            geoDataFile.setDate(new Date());
            geoDataFile.setMd5(md5);
            geoDataFile.setDownloadNum(0);
            geoDataFile.setPath(srcFilePath);
            geoDataFile.setUnZipFilesPath(unZipFiles);
            geoDataFile.setPtName(tableName);
            geoDataFile.setOriginalName(originalFilename);
            geoDataFile.setDisplayName(uploadShapefileDTO.getFileName());
            geoDataFile.setDataType(shpProcessRepository.getShpType(tableName));
            geoDataFile.setUserId(uploadShapefileDTO.getUserId());
            geoDataFile.setBounds(bounds);
            geoDataFile.setCenter(this.getCenter(bounds));
            geoDataFile.setCode(uploadShapefileDTO.getCode());
            geoDataFile.setSrid(shpProcessRepository.getSRID(tableName));
            GeoDataFile isInsert = geoDataFileRepository.insert(geoDataFile);
            //TODO:封装
            Catalog fileParentCatalog = catalogRepository.getCatalogById(uploadShapefileDTO.getCatalogId());
            fileParentCatalog.setTotal(fileParentCatalog.getTotal() + 1);
            ChildrenDataDTO fileProfileData = new ChildrenDataDTO(ShpId, uploadShapefileDTO.getFileName(), fileType, dataType, new Date());
            List<ChildrenDataDTO> children = fileParentCatalog.getChildren();
            if (children == null) {
                children = new ArrayList<>();
            }
            children.add(fileProfileData);
            fileParentCatalog.setChildren(children);
            Catalog updatedCatolog = catalogRepository.save(fileParentCatalog);
            if (Objects.isNull(isInsert) || Objects.isNull(updatedCatolog)) {
                throw new Exception("创建文件记录失败");
            }
            return new ApiResponse(200, "文件上传成功！", ShpId);
        } catch (Exception e) {
            unZipFiles.forEach(file -> FileUtils.deleteFile(file));  //删除解压文件
            FileUtils.deleteFile(srcFilePath);  //删除上传的zip

            if (geoDataFile != null) {
                geoDataFileRepository.delete(geoDataFile);
            }

            shpProcessRepository.deletePgTable(tableName);
            return new ApiResponse(201, e.getMessage());
        }

    }

    private List<Double> getCenter(List<Double> bounds) {
        ArrayList<Double> center = new ArrayList<>();
        center.add((bounds.get(0) + bounds.get(2)) / 2);
        center.add((bounds.get(1) + bounds.get(3)) / 2);
        return center;
    }

    @Override
    public ApiResponse getList() {
        List<GeoDataFile> geoDataFiles = geoDataFileRepository.findAll();
        ArrayList<ReturnGeoDataDTO> returnGeoDataDTOS = new ArrayList<>();
        geoDataFiles.forEach(geoFile -> returnGeoDataDTOS.add(new ReturnGeoDataDTO(geoFile.getId(), geoFile.getDisplayName())));
        return new ApiResponse(200, "返回数据成功！", returnGeoDataDTOS);
    }

    @Override
    public ApiResponse getFields(String geoId) {
        GeoDataFile oneById = geoDataFileRepository.findOneById(geoId);
        List<String> fields = shpProcessRepository.getFields(oneById.getPtName());
        return new ApiResponse(200, "成功", fields);
    }

    @Override
    public ApiResponse getUniqueValues(String ptName, String field, String method) {
        return shpProcessRepository.getUniqueValues(ptName, field, method);
    }
}
