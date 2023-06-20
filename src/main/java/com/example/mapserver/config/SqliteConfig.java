package com.example.mapserver.config;

import com.example.mapserver.entity.bo.MbtilesProps;
import com.example.mapserver.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description sqlite配置
 * @Author bin
 * @Date 2021/12/09
 */
@Slf4j
@Configuration
public class SqliteConfig {

    @Value("${mbtilesPath}")
    private String mbtilesPath;

    @Value("${terrariumPath}")
    private String terrariumPath;

    List<MbtilesProps> mbtilesPropsList = new ArrayList<>();
    Map<Integer, List<MbtilesProps>> mbtilesPropsMap = new HashMap<>();


    @Bean(name = "mbtilesConnection")
    Connection mbtilesStatement() throws SQLException {
        // return getConnection("jdbc:sqlite:" + resourcePath + "/mapbox/2017-07-03_planet_z0_z14.mbtiles");
        // return getConnection("jdbc:sqlite:Z:/2017-07-03_planet_z0_z14.mbtiles");
        return getConnection("jdbc:sqlite:" + mbtilesPath + "/2017-07-03_planet_z0_z14.mbtiles");
        // return getConnection("jdbc:sqlite:" + "Y:" + "/2020-10-planet-14.mbtiles");
    }


    List<String> loadTerrariumMbtilesPath() {

        List<String> files = new ArrayList<>();
        FileUtils.getFilesContainChild(terrariumPath, files);
        return files;

    }


    @Bean(name = "terrariumConnection")
    Map<Integer, List<MbtilesProps>> terrariumConnection() throws SQLException {


        List<String> path = loadTerrariumMbtilesPath();

        for (String mbPath : path) {
            // e.g. Y:\mbtiles\11\terrarium_11_512-1023.mbtiles

            //不执行下面的代码
            if (mbPath != null) {
                continue;
            }

            String fileType = FileUtils.getFileType(mbPath);
            if (fileType.equals("mbtiles")) {
                String mbname = FileUtils.getFilenameNoSuffix(mbPath);
                String[] split = mbname.split("_");

                // 因为0-10级都放在一个数据库中，所以单独判断
                if (split[1].equals("0-10")) {
                    continue;
                }

                int level = Integer.parseInt(split[1]);

                //做测试的，12以上先不连
                // if (level > 11)
                //     continue;

                String range = split[2];
                String[] split1 = range.split("-");
                int start = Integer.parseInt(split1[0]);
                int end = Integer.parseInt(split1[1]);
                Connection connection = getConnection("jdbc:sqlite:" + mbPath);

                MbtilesProps mbtilesProps = new MbtilesProps(level, start, end, connection);
                //List的方式
                mbtilesPropsList.add(mbtilesProps);

                //Map的方式
                List<MbtilesProps> props = mbtilesPropsMap.get(level);
                if (props == null){
                    props = new ArrayList<>();
                }
                props.add(mbtilesProps);
                mbtilesPropsMap.put(level, props);
            }

        }

        return mbtilesPropsMap;

    }


    @ConditionalOnProperty(value = "loadTerrarium")
    @Bean(name = "terrariumConnection_0_10")
    Connection terrariumConnection_0_10() throws SQLException {
        // return getConnection("jdbc:sqlite:" + mbtilesPath + "/dem/terrarium_0_10.mbtiles");
        return getConnection("jdbc:sqlite:" + terrariumPath + "/dem/terrarium_0_10.mbtiles");
        // return getConnection("jdbc:sqlite:E:/mapServer/download_data/mbtiles/terrarium_0-8.mbtiles");
    }


    // @Bean(name = "terrariumConnection_11")
    // Connection terrariumConnection_11() throws SQLException {
    //     // return getConnection("jdbc:sqlite:" + resourcePath + "/mapbox/2017-07-03_planet_z0_z14.mbtiles");
    //     // return getConnection("jdbc:sqlite:Z:/2017-07-03_planet_z0_z14.mbtiles");
    //     return getConnection("jdbc:sqlite:" + mbtilesPath + "/terrarium_11.mbtiles");
    // }
    //
    // @Bean(name = "terrariumConnection_12")
    // Connection terrariumConnection_12() throws SQLException {
    //     // return getConnection("jdbc:sqlite:" + resourcePath + "/mapbox/2017-07-03_planet_z0_z14.mbtiles");
    //     // return getConnection("jdbc:sqlite:Z:/2017-07-03_planet_z0_z14.mbtiles");
    //     return getConnection("jdbc:sqlite:" + mbtilesPath + "/terrarium_12.mbtiles");
    // }
    //
    // @Bean(name = "terrariumConnection_13")
    // Connection terrariumConnection_13() throws SQLException {
    //     // return getConnection("jdbc:sqlite:" + resourcePath + "/mapbox/2017-07-03_planet_z0_z14.mbtiles");
    //     // return getConnection("jdbc:sqlite:Z:/2017-07-03_planet_z0_z14.mbtiles");
    //     return getConnection("jdbc:sqlite:" + mbtilesPath + "/terrarium_13.mbtiles");
    // }
    //
    // @Bean(name = "terrariumConnection_14")
    // Connection terrariumConnection_14() throws SQLException {
    //     // return getConnection("jdbc:sqlite:" + resourcePath + "/mapbox/2017-07-03_planet_z0_z14.mbtiles");
    //     // return getConnection("jdbc:sqlite:Z:/2017-07-03_planet_z0_z14.mbtiles");
    //     return getConnection("jdbc:sqlite:" + mbtilesPath + "/terrarium_14.mbtiles");
    // }
    //
    // @Bean(name = "terrariumConnection_15")
    // Connection terrariumConnection_15() throws SQLException {
    //     // return getConnection("jdbc:sqlite:" + resourcePath + "/mapbox/2017-07-03_planet_z0_z14.mbtiles");
    //     // return getConnection("jdbc:sqlite:Z:/2017-07-03_planet_z0_z14.mbtiles");
    //     return getConnection("jdbc:sqlite:" + mbtilesPath + "/terrarium_15.mbtiles");
    // }



    /**
     * 连接数据库 返回连接数据库的Connection 不能返回执行SQL语句的statement，
     * 因为每个Statement对象只能同时打开一个ResultSet对象，
     * 高并发情况下会出现 <code>rs.isOpen() on exec</code> 的错误
     * @param conurl 数据库地址
     * @return java.sql.Connection
     * @Author bin
     **/
    public static Connection getConnection(String conurl) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException e) {
            // e.printStackTrace();
            log.warn("Database driver not found!");
        }
        // 得到连接 会在你所填写的文件夹建一个你命名的文件数据库
        Connection conn;
        // String conurl = "jdbc:sqlite:E:/mapArchiveFiles/tianditu/img_c.mbtiles";
        conn = DriverManager.getConnection(conurl,null,null);
        // 设置自己主动提交为false
        conn.setAutoCommit(false);

        //推断表是否存在
        ResultSet rsTables = conn.getMetaData().getTables(null, null, "tiles", null);
        if(!rsTables.next()){
            log.warn("{} does not exist!", conurl);
        } else {
            log.info("{} successfully connected!", conurl);
        }

        return conn;

        // return conn.createStatement();
    }


    @Bean(name = "imgConnection")
    Connection imgStatement() throws SQLException {
        return getConnection("jdbc:sqlite:" + mbtilesPath + "/tianditu/img_c.mbtiles");
    }

    @Bean(name = "cvaConnection")
    Connection cvaStatement() throws SQLException {
        return getConnection("jdbc:sqlite:" + mbtilesPath + "/tianditu/cva_c.mbtiles");
    }

    @Bean(name = "ciaConnection")
    Connection ciaStatement() throws SQLException {
        return getConnection("jdbc:sqlite:" + mbtilesPath + "/tianditu/vec-cia/cia_c.mbtiles");
    }

    @Bean(name = "vecConnection")
    Connection vecStatement() throws SQLException {
        return getConnection("jdbc:sqlite:" + mbtilesPath + "/tianditu/vec-cia/vec_c.mbtiles");
    }

}
