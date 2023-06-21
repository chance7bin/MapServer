package com.example.mapserver.service;

import javax.servlet.http.HttpServletResponse;

/**
 * 使用pg源发布服务
 *
 * @author 7bin
 */
public interface IPgService {

    void getMvt(int zoom, int x, int y, String tableName, HttpServletResponse response);

}
