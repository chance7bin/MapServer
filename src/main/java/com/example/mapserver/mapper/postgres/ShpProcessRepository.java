package com.example.mapserver.mapper.postgres;

import com.alibaba.fastjson.JSONObject;
import com.example.mapserver.entity.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.*;

/**
 * shp数据处理数据层
 *
 * @author 7bin
 */

@Slf4j
@Repository
public class ShpProcessRepository {

    @Value("${pgsqlCmdPath}")
    private String postgresqlBinDir;

    @Value("${shp2pgSqlWin}")
    private String shp2pgSqlWin;

    @Value("${shp2pgSqlLinux}")
    private String shp2pgSqlLinux;

    @Value("${pgPassword}")
    private String pgPassword;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean shp2pgsql(String shpFileName, String tableName, String espg, String code) {
        try {
            Process pro = null;
            // 路径带空格转换
            String formattedPostgresqlBinDir = postgresqlBinDir.replaceAll(" ", "\" \"");
            // postgresqlBinDir = postgresqlBinDir.replaceAll(" ", "\" \"");
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                String cmd = MessageFormat.format(shp2pgSqlWin, formattedPostgresqlBinDir, espg, code, shpFileName, tableName);
                log.info(cmd);
                pro = Runtime.getRuntime().exec(cmd, new String[]{"PGPASSWORD=" + pgPassword});
            } else {
                String cmd = MessageFormat.format(shp2pgSqlLinux, formattedPostgresqlBinDir, espg, shpFileName, tableName);
                log.info(cmd);
                pro = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", cmd});
            }

//            String cmd1 = MessageFormat.format(shp2pgSqlWin, pgPath,epsg,shpFile,tableName);
//
//            log.info(cmd1);
//            Process pro = Runtime.getRuntime().exec(cmd1, new String[]{"PGPASSWORD="+pgPassword});
            String line;

            BufferedReader buf = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            boolean commitExist = false;
            boolean CREATEINDEXExist = false;
            while ((line = buf.readLine()) != null) {
                // log.info("-----------out:" + line);
                if (line.indexOf("COMMIT") != -1) {
                    commitExist = true;
                }
                if (line.indexOf("CREATE INDEX") != -1) {
                    CREATEINDEXExist = true;
                }
            }

            if (commitExist && CREATEINDEXExist) {
                log.info("Save shp to pg: " + tableName);
                return true;
            }

            log.error("Save shp to pg error!!!");
            return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean deletePgTable(String tableName) {
        try {
            String sql = "DROP TABLE " + tableName;
            jdbcTemplate.execute(sql);
            log.info("drop table: " + tableName);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

    }

    public String getShpType(String tableName) {
        try {
            String sql = "SELECT GeometryType(geom) FROM " + tableName + " limit 1";
            Map<String, Object> m = jdbcTemplate.queryForMap(sql);
            return m.get("geometrytype").toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return "none";
        }
    }


    public List getShpAttrInfoFromPG(String tableName) {
        try {
//            String sql = "SELECT * FROM information_schema.columns WHERE table_name   = '" + tableName + "'";
            String sql = "SELECT * FROM " + tableName;
            List<Map<String, Object>> re = jdbcTemplate.queryForList(sql);
//            Object o = jdbcTemplate.queryForObject(sql);
            List<Map<String, Object>> attrArray = new ArrayList<>();
            for (int i = 0; i < re.size(); i++) {
                JSONObject attr = new JSONObject();
                Map<String, Object> singleRecord = new HashMap<>();
//                re.get(i).forEach(map -> singleRecord.put(map));
                for (Map.Entry<String, Object> entry : re.get(i).entrySet()) {
                    if (!entry.getKey().equals("geom")) {
                        singleRecord.put(entry.getKey(), entry.getValue());
                        attr.put(entry.getKey(), entry.getValue());
                    }
                }
                attrArray.add(attr);
            }
            return attrArray;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }


    public List<Double> getShpBox2D(String shpTableName) {

        String sql = MessageFormat.format("SELECT ST_Extent(geom) " +
            "from {0};", shpTableName);

        String boxStr = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getString("st_extent"));  //BOX(1 0,10 20)
        String coordStr = boxStr.substring(4, boxStr.length() - 1); //1 0,10 20
        double xMin = Double.parseDouble(coordStr.split(",")[0].split(" ")[0]);
        double xMax = Double.parseDouble(coordStr.split(",")[1].split(" ")[0]);
        double yMin = Double.parseDouble(coordStr.split(",")[0].split(" ")[1]);
        double yMax = Double.parseDouble(coordStr.split(",")[1].split(" ")[1]);

        Double[] boundsArray = {xMin, yMin, xMax, yMax};  //"bounds": [-180,-85,180,85],
        List<Double> bounds = new ArrayList<>(Arrays.asList(boundsArray));

        return bounds;
    }


    public Map<String, Object> getMaxMinAttrValue(String shpTableName, String attrName) {
        try {
            String sql = MessageFormat.format("select max({0}),min({0}) from {1}", attrName, shpTableName);
            Map<String, Object> result = jdbcTemplate.queryForMap(sql);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public Integer getSRID(String tableName) {
        String sql = MessageFormat.format("SELECT ST_SRID(geom) " +
            "from {0} LIMIT 1;", tableName);
        int srid = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt("st_srid"));
        return srid;
    }

    public List<String> getFields(String tableName) {
        try {
            String sql = "SELECT column_name FROM information_schema.columns WHERE table_name   = '" + tableName.toLowerCase() + "'";
            List<Map<String, Object>> re = jdbcTemplate.queryForList(sql);
            ArrayList<String> fields = new ArrayList<>();
            re.forEach(field -> {
                if (!field.get("column_name").toString().equals("geom")) {
                    fields.add(field.get("column_name").toString());
                }
            });
            return fields;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public ApiResponse getUniqueValues(String ptName, String field, String method) {
        String sql = MessageFormat.format("SELECT DISTINCT ( \"{0}\") FROM {1} ORDER BY \"{2}\" {3}", field, ptName, field, method);
        List<Map<String, Object>> re = jdbcTemplate.queryForList(sql);
        ArrayList<Object> uniqueValues = new ArrayList<>();
        re.forEach(uniqueValueMap -> {
            uniqueValues.add(uniqueValueMap.get(field));
        });

        return new ApiResponse(200, "成功", uniqueValues);
    }
}
