package com.example.mapserver.entity.po;

import com.example.mapserver.entity.dto.ChildrenDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * @author 7bin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catalog {

    @Id
    String id;
    String name;
    String parentId;
    List<ChildrenDataDTO> children;    // 子文件和子文件夹
    int total;      // 文件数
    String userId;
    int level;
    Date date;
}
