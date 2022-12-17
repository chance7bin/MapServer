package com.example.mapserver.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.mapserver.config.SqliteConfig;
import com.example.mapserver.entity.bo.MbtilesProps;
import com.example.mapserver.entity.dto.TiandituTilesDTO;
import com.example.mapserver.entity.dto.TilesDTO;
import com.example.mapserver.service.ITilesService;
import com.example.mapserver.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author bin
 * @Date 2022/03/25
 */
@Service
@Slf4j
public class TilesServiceImpl implements ITilesService {


    @Resource(name="mapboxConnection")
    Connection mapboxConnection;


    // @Resource(name="terrariumConnection_0_10")
    @Autowired(required = false)
    @Qualifier(value = "terrariumConnection_0_10")
    Connection terrariumConnection_0_10;

    @Resource(name="terrariumConnection")
    Map<Integer, List<MbtilesProps>> terrariumConnection;

    // @Resource(name="terrariumConnection_11")
    // Connection terrariumConnection_11;
    //
    // @Resource(name="terrariumConnection_12")
    // Connection terrariumConnection_12;
    //
    // @Resource(name="terrariumConnection_13")
    // Connection terrariumConnection_13;
    //
    // @Resource(name="terrariumConnection_14")
    // Connection terrariumConnection_14;
    //
    // @Resource(name="terrariumConnection_15")
    // Connection terrariumConnection_15;

    @Value("${deployIpAndPort}")
    String deployIpAndPort;

    @Value("${resourcePath}")
    String resourcePath;


    @Resource(name="imgConnection")
    Connection imgConnection;

    @Resource(name="cvaConnection")
    Connection cvaConnection;

    @Resource(name="ciaConnection")
    Connection ciaConnection;

    @Resource(name="vecConnection")
    Connection vecConnection;


    @Override
    public void getMapboxTiles(TilesDTO tilesDTO, HttpServletResponse response) {

        queryMbtilesWithUncompress(tilesDTO, mapboxConnection, response);

    }

    private void queryMbtilesWithUncompress(TilesDTO tilesDTO, Connection connection, HttpServletResponse response){
        try {
            Statement statement = connection.createStatement();
            // 得到结果集
            String sql = "SELECT * FROM tiles WHERE zoom_level = "+ tilesDTO.getZoom_level() +
                " AND tile_column = "+ tilesDTO.getTile_column() +
                " AND tile_row = "+ tilesDTO.getTile_row() ;
            ResultSet rs = statement.executeQuery(sql);
            if(rs.next()) {
                byte[] imgByte = (byte[]) rs.getObject("tile_data");
                // 由于mapbox只能加载未压缩的pbf格式数据，
                // 但直使用tippecanoe或mbutils生成的pbf是经过gzip压缩的数据
                // [不执行解压缩，mapbox加载数据会报：“Unimplemented type: 3” 错误]，
                // 所以需要解压缩
                byte[] bytes = FileUtils.gzipUncompress(imgByte);
                InputStream is = new ByteArrayInputStream(bytes);
                OutputStream os = response.getOutputStream();
                try {
                    int count = 0;
                    byte[] buffer = new byte[1024 * 1024];
                    while ((count = is.read(buffer)) != -1) {
                        os.write(buffer, 0, count);
                    }
                    os.flush();
                } catch (IOException e) {
                    // e.printStackTrace();
                } finally {
                    os.close();
                    is.close();
                }
            }
            else{
                log.debug("sql: {}",sql);
                log.debug("未找到瓦片!");
            }
            rs.close();
            //statement在每次执行之后都要关了
            statement.close();
        }catch (Exception e){
            // e.printStackTrace();
        }
    }

    @Override
    public void getTerrariumTiles(TilesDTO tilesDTO, HttpServletResponse response) {

        Connection connection = null;

        int zoom_level = tilesDTO.getZoom_level();

        if (zoom_level <= 10){
            connection = terrariumConnection_0_10;
        } else {
            List<MbtilesProps> mbtilesProps = terrariumConnection.get(zoom_level);
            try {
                for (MbtilesProps mbtilesProp : mbtilesProps) {
                    int start = mbtilesProp.getStart();
                    int end = mbtilesProp.getEnd();
                    if (tilesDTO.getTile_column() >= start && tilesDTO.getTile_column() <= end){
                        connection = mbtilesProp.getConnection();
                        break;
                    }
                }
            } catch (Exception e){
                log.debug("error range: {} - {} - {}", zoom_level, tilesDTO.getTile_column(), tilesDTO.getTile_row());
            }
        }

        // switch (tilesDTO.getZoom_level()){
        //     case 11:{
        //         List<MbtilesProps> mbtilesProps = terrariumConnection.get(11);
        //         for (MbtilesProps mbtilesProp : mbtilesProps) {
        //             int start = mbtilesProp.getStart();
        //             int end = mbtilesProp.getEnd();
        //             if (tilesDTO.getTile_column() >= start && tilesDTO.getTile_column() <= end){
        //                 connection = mbtilesProp.getConnection();
        //                 break;
        //             }
        //         }
        //         break;
        //     }
        //     case 12:{
        //         List<MbtilesProps> mbtilesProps = terrariumConnection.get(12);
        //         connection = terrariumConnection_12;
        //         break;
        //     }
        //     case 13:{
        //         List<MbtilesProps> mbtilesProps = terrariumConnection.get(13);
        //         connection = terrariumConnection_13;
        //         break;
        //     }
        //     case 14:{
        //         List<MbtilesProps> mbtilesProps = terrariumConnection.get(14);
        //         connection = terrariumConnection_14;
        //         break;
        //     }
        //     // case 15:{
        //     //     List<MbtilesProps> mbtilesProps = terrariumConnection.get(15);
        //     //     connection = terrariumConnection_15;
        //     //     break;
        //     // }
        //     default:{
        //         connection = terrariumConnection_0_10;
        //     }
        // }


        if (connection != null){
            queryMbtiles(tilesDTO, connection, response);
        }

    }

    @Override
    public JSONObject getMapboxTilesMetadataJson() {

        JSONObject result = new JSONObject();
        
        try {
            Statement statement = mapboxConnection.createStatement();
            // 得到结果集
            String sql = "SELECT * FROM metadata";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String name = (String) rs.getObject("name");
                String value = (String) rs.getObject("value");

                JSONObject jsonObject = formatMetadata(name, value);

                result.put(jsonObject.getString("name"),jsonObject.get("value"));

            }
            rs.close();
            //statement在每次执行之后都要关了
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        result.put("tiles", Arrays.asList("http://" + deployIpAndPort + "/tiles/mapbox/{z}/{x}/{y}.pbf"));
        // result.put("tiles", Arrays.asList("https://api.maptiler.com/tiles/v3/{z}/{x}/{y}.pbf?key=XAapkmkXQpx839NCfnxD"));

        return result;
    }

    @Override
    public JSONObject getMapboxLibertyJson() {

        try {
            // File file = ResourceUtils.getFile("classpath:static/osm_liberty.json");
            // String classesPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

            // log.info("classesPath:" + classesPath);
            File file = ResourceUtils.getFile(resourcePath + "/osm_liberty.json");

            // File file = ResourceUtils.getFile(classesPath + "static/osm_liberty.json");
            Map map = FileUtils.readJson(file);
            JSONObject jsonObject = new JSONObject(map);

            String sourceUrl = "http://" + deployIpAndPort + "/tiles/mapbox/metadata/tiles.json";

            ((Map)((Map) jsonObject.get("sources")).get("openmaptiles")).put("url", sourceUrl);

            return jsonObject;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public void testTerrariumTiles(TilesDTO tilesDTO, String mbtilesPath, HttpServletResponse response) {

        try {
            Connection connection = SqliteConfig.getConnection("jdbc:sqlite:" + mbtilesPath);
            queryMbtiles(tilesDTO, connection, response);
            connection.close();
        } catch (Exception e) {
            log.info("test error");
        }


    }

    @Override
    public void getTiandituTiles(TiandituTilesDTO tilesDTO, HttpServletResponse response) {

        Connection connection;

        switch (tilesDTO.getTile_Layer()){
            case img_c:{
                connection = imgConnection;
                break;
            }
            case cva_c:{
                connection = cvaConnection;
                break;
            }
            case cia_c:{
                connection = ciaConnection;
                break;
            }
            case vec_c:{
                connection = vecConnection;
                break;
            }

            default:
                throw new IllegalStateException("Unexpected value: " + tilesDTO.getTile_Layer());
        }

        queryMbtiles(tilesDTO, connection, response);

    }


    private JSONObject formatMetadata(String name, String value){

        JSONObject res = new JSONObject();


        if (name.equals("json")){
            JSONObject valueObj = JSONObject.parseObject(value);
            JSONArray vector_layers = valueObj.getJSONArray("vector_layers");
            // res.put("vector_layers",vector_layers);
            res.put("name","vector_layers");
            res.put("value",vector_layers);
        } else if (name.equals("minzoom") || name.equals("maxzoom") || name.equals("maskLevel")){
            //整型
            // res.put(name,Integer.parseInt(value));
            res.put("name",name);
            res.put("value",Integer.parseInt(value));
        } else if (name.equals("bounds") || name.equals("center") || name.equals("extent")){
            //数组
            String[] arr = value.split(",");
            List<Double> doubles = new ArrayList<>();
            for (String s : arr) {
                doubles.add(Double.parseDouble(s));
            }
            // res.put(name,arr);
            res.put("name",name);
            res.put("value",doubles);
        } else {
            // res.put(name,value);
            res.put("name",name);
            res.put("value",value);
        }

        return res;

    }





    private void queryMbtiles(TilesDTO tilesDTO, Connection connection, HttpServletResponse response){

        try {
            Statement statement = connection.createStatement();
            // 得到结果集
            String sql = "SELECT * FROM tiles WHERE zoom_level = "+ tilesDTO.getZoom_level() +
                " AND tile_column = "+ tilesDTO.getTile_column() +
                " AND tile_row = "+ tilesDTO.getTile_row() ;
            ResultSet rs = statement.executeQuery(sql);
            if(rs.next()) {
                byte[] imgByte = (byte[]) rs.getObject("tile_data");
                // byte[] bytes = FileUtils.gzipUncompress(imgByte);
                InputStream is = new ByteArrayInputStream(imgByte);
                OutputStream os = response.getOutputStream();
                try {
                    int count = 0;
                    byte[] buffer = new byte[1024 * 1024];
                    while ((count = is.read(buffer)) != -1) {
                        os.write(buffer, 0, count);
                    }
                    os.flush();
                } catch (IOException e) {
                    // e.printStackTrace();
                } finally {
                    os.close();
                    is.close();
                }
            }
            else{
                log.debug("sql: {}",sql);
                log.debug("未找到瓦片!");
            }
            rs.close();
            //statement在每次执行之后都要关了
            statement.close();
        }catch (Exception e){
            // e.printStackTrace();
        }


    }


}
