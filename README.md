# MapServer

## 介绍

该地图服务器提供了不同数据源（`PostGIS` / `MBTiles` / `GeoServer`）的地图服务接口 使用者在配置好数据源之后，根据接口规范即可获取到相应数据

## 软件架构

地图服务主要使用到了`PostgreSQL`的空间扩展`PostGIS`，`Sqlite`以及`GeoServer`

可视化引擎主要使用`MapboxGL`

## 可视化引擎

**项目地址** 👉 [可视化引擎OneSIS V-Engine](https://github.com/chance7bin/OneSIS-VEngine)

## API

#### _地图瓦片接口 —— pg源_

##### 获取mvt二进制数据

- 接口路径 : `/mvt/{tableName}/{zoom}/{x}/{y}.pbf`
- 请求方法 : `GET`
- 请求参数 :
    - params
  ```
  tableName: 图层名
  zoom: 缩放层级
  x: x坐标
  y: y坐标
  ```
- 响应参数 :
  ```
  mvt二进制数据
  ```

#### _地图瓦片接口 —— mbtiles源_

##### 得到天地图瓦片

- 接口路径 : `/mbtiles/tianditu/{layer}/{z}/{x}/{y}`
- 请求方法 : `GET`
- 请求参数 :
	- params
  ```
  layer: 加载的图层
  z: 缩放层级
  x: x坐标
  y: y坐标
  ```
- 响应参数 :
  ```
  瓦片二进制数据
  ```

##### 得到mapbox瓦片

- 接口路径 : `/mbtiles/mapbox/{z}/{x}/{y}.pbf`
- 请求方法 : `GET`
- 请求参数 :
	- params
  ```
  z: 缩放层级
  x: x坐标
  y: y坐标
  ```
- 响应参数 :
  ```
  瓦片二进制数据
  ```

##### 得到mapbox的tiles.json

- 接口路径 : `/mapbox/metadata/tiles.json`
- 请求方法 : `GET`
- 响应参数 :
  ```json
  {
  // mapbox规定的tiles.json格式 
      "center": [
          -12.2168,
          28.6135,
          4.0
      ],
      "format": "pbf",
      "description": "A tileset showcasing all layers in OpenMapTiles. http://openmaptiles.org",
      "pixel_scale": "256",
      "mtime": "1499626373833",
      "version": "3.6.1",
      "vector_layers": [...
      ],
      "maskLevel": 8,
      "tiles": [...
      ],
      "maxzoom": 14,
      "planettime": "1499040000000",
      "attribution": "<a href=\"http://www.openmaptiles.org/\" target=\"_blank\">&copy; OpenMapTiles</a> <a href=\"http://www.openstreetmap.org/about/\" target=\"_blank\">&copy; OpenStreetMap contributors</a>",
      "name": "OpenMapTiles",
      "bounds": [...
      ],
      "id": "openmaptiles",
      "minzoom": 0
  }
  ```

##### 得到mapbox的osm_liberty.json

- 接口路径 : `/mbtiles/mapbox/liberty.json`
- 请求方法 : `GET`
- 响应参数 :
  ```json
  {
  // mapbox规定的liberty.json格式
      "version": 8,
      "name": "OSM Liberty",
      "metadata": {...
      },
      "sources": {...
      },
      "sprite": "https://maputnik.github.io/osm-liberty/sprites/osm-liberty",
      "glyphs": "https://api.maptiler.com/fonts/{fontstack}/{range}.pbf?key=XAapkmkXQpx839NCfnxD",
      "layers": [...
      ],
      "id": "osm-liberty"
  }
  ```

**使用示例**

```html

<body>
<div id="map"></div>
<script>
	mapboxgl.accessToken = 'pk.ey...h1MzJrIn0.2e2_r..BIZtZg';
	const map = new mapboxgl.Map({
		container: 'map',
		style: "http://localhost:9000/tiles/mapbox/liberty.json",
		zoom: 5,
		center: [118.447303, 30.753574]
	});
</script>
</body>
```

#### _地图瓦片接口 —— geoserver源_

##### shp生成瓦片服务

- 接口路径 : `/geoserver/wms/publish/{sfname}`
- 请求方法 : `POST`
- 请求参数 :
	- params
  ```
  sfname: 上传的shapefile压缩包文件名
  ```
- 响应参数 :
  ```json
  {
      "msg": "操作成功",
      "code": 200,
      "data": "..." // geoserver的wms服务地址
  }
  ```

##### 根据工作空间和图层名称返回wms服务地址

- 接口路径 : `/geoserver/wms/{workspace}/{layerName}`
- 请求方法 : `GET`
- 请求参数 :
	- params
  ```
  workspace: geoserver工作空间
  layerName: 图层名称
  ```
- 响应参数 :
  ```json
  {
      "msg": "操作成功",
      "code": 200,
      "data": "..." // geoserver的wms服务地址
  }
  ```
