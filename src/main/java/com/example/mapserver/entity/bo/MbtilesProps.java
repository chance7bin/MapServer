package com.example.mapserver.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Connection;

/**
 * @Description
 * @Author bin
 * @Date 2022/06/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MbtilesProps {

    int level; //层级
    int start;  //数据库开始col
    int end;  //数据库结束col
    Connection connection;   //数据库连接

}
