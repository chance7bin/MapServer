package com.example.mapserver.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 7bin
 * @date 2023/06/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoDataFile {

    @Id
    private String id;
    private String md5;
    private Map<String, String> nameList;
    private String userId;
    private String ptName;      // 在postgres中的表名
    private String originalName;  //去后缀的名字
    private String displayName;
    private int size;
    private String path;        //存储路径
    private List<String> unZipFilesPath;
    private int downloadNum;      // 下载次数
    private Date date;
    private String dataType;   //数据类型，MultiPolygon、Point...
    private Integer srid;      //空间参考
    private String code;       //字符集编码
    private List<Double> bounds;       //包络线范围
    private List<Double> center;
    private String tileJSONId;
}
