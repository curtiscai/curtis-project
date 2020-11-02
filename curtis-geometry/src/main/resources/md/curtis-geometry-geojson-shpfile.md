# 1 GeoJSON

## 1.1 GeoJSON概述

> 摘自：英文维基百科（需要科学上网）：https://en.wikipedia.org/wiki/GeoJSON
>
> ​		   互联网工程任务组IETF发布的GeoJSON文档：https://tools.ietf.org/html/rfc7946

​	GeoJSON是一种基于JSON的地理空间数据交换格式（geospatial data interchange format）。GeoJSON用于将地理特征（ geographic features）、非地理属性以及空间范围组合起来。GeoJSON使用地理坐标系（ geographic coordinate reference system）、WGS84（World Geodetic System 1984）和十进制数。

​	GeoJSON工作组成立于2007年3月，于2008年6月完成GeoJSON规范的制定。2015年互联网工程任务组（Internet Engineering Task Force）成立了地理JSON工作组（Geographic JSON working group），该工作组于2016年8月将GeoJSON发布为RFC 7946。RFC 7946链接为：https://tools.ietf.org/html/rfc7946

​	GeoJSON的文件扩展名是：.json或者.geojson。

​	GeoJSON的Media Type是application/geo+json。

​	GeoJSON的官方网站：https://geojson.org/

## 1.2 GeoJSON数据格式

> 摘自：互联网工程任务组IETF发布的GeoJSON文档：https://tools.ietf.org/html/rfc7946
>
> ​			GeoJSON官方网站废弃的旧文档（有参考价值）：https://geojson.org/geojson-spec.html

### 1.2.1 GeoJSON文本

​	GeoJSON文本是JSON文本，由单个GeoJSON对象组成，不能是GeoJSON数组。

### 1.2.2 GeoJSON对象

​	GeoJSON对象可以是Geometry对象、GeometryCollection对象、Feature对象或者Feature Collection对象。GeoJSON对象一定有一个名称为type的key，type的值只能是GeoJSON Types中的一种。

​	GeoJSON Types取值是区分大小写（case-sensitive）的字符串，一共有九种取值。分别是：Feature、FeatureCollection和七种Geometry Type（Point、LineString、Polygon、MultiPoint、MultiLineString、MultiPolygon和GeometryCollection）。

### 1.2.3 Geometry对象

​	每个Geometry对象都是一个GeoJSON对象，无论Geometry对象出现在哪个位置。Geometry对象都有一个key为type的属性，值是相应的类型（准确来说，所有的GeoJSON对象都有key为type的属性），除类型为GeometryCollection的Geometry对象外其他Geometry对象都有一个key为coordinates的属性。coordinates的值是数组，数组的维度有相应的Geometry Type决定。

```xml-dtd
{
		"type": "Point",
		"coordinates": [102.0, 0.5]
}
```

### 1.2.4 GeometryCollection对象

​	每个GeometryCollection对象都是一个GeoJSON对象。GeometryCollection对象的key为type的值是GeometryCollection。GeometryCollection对象还有key为geometries的属性，值是Geometry对象数组。

```xml-dtd
{
		"type": "GeometryCollection",
		"geometries": [
			{
				"type": "Point",
				"coordinates": [102.0, 0.5]
			},
  		{
				"type": "LineString",
				"coordinates": [
					[102.0, 0.0],
					[103.0, 1.0],
					[104.0, 0.0],
					[105.0, 1.0]
				]
			}
		]
}
```

### 1.2.5 Feature对象节点

​	Feature对象是指子节点type值为Feature的JSON对象，Feature节点有三个子节点，分别是：

* type节点，值是"Feature"。

* geometry节点，值是Geometry对象或者null。

* properties节点，值是JSON对象或者null。

* id节点（非必须），值是数字或者字符串，用于标识唯一的Feature。

  ```xml-dtd
  {
  		"type": "Feature",
  		"geometry": {
  				"type": "Point",
  				"coordinates": [102.0, 0.5]
  		},
  		"properties": {
  				"prop0": "value0"
  		}
  }
  ```

### 1.2.6 FeatureCollection对象节点

​	FeatureCollection对象是指子节点type值为FeatureCollection的JSON对象。FeatureCollection对象有两个节点，分别是：

* type节点，值为"FeatureCollection"。

* features节点，值为JSON数组。数组的每个元素都是Feature对象，必须满足Feature的节点格式要求。

  ```xml-dtd
  {
  	"type": "FeatureCollection",
  	"features": [
  		{
  			"type": "Feature",
        "geometry": {
  				"type": "Point",
          "coordinates": [102.0, 0.5]
  			},
  			"properties": {
          "prop0": "value0"
  			}
  		},
  		{
  			"type": "Feature",
  			"geometry": {
  				"type": "LineString",
  				"coordinates": [
  					[102.0, 0.0],
  					[103.0, 1.0],
  					[104.0, 0.0],
  					[105.0, 1.0]
  				]
  			},
  			"properties": {
  				"prop0": "value0"
  			}
  		}
  	]
  }
  ```

### 1.2.7 CRS对象节点（Coordinate Reference System）

​	所有GeoJSON文件中的坐标都使用WGS 84（World Geodetic System 1984）地理坐标系。坐标的经纬度使用十进制数来表示。WGS84等效于OGC 开放地理空间联盟（Open Geospatial Consortium）的urn:ogc:def:crs:OGC::CRS84。

### 1.2.8 关于Polygon坐标的补充说明

​	Polygon的第一个元素是外部线性环，其他都是内洞。外环和内洞都要求至少四个点，并且是封闭的线，也就是说首尾要素相同。关于Polygon的外环和内洞要求外环是逆时针，而内环是顺时针（A linear ring MUST follow the right-hand rule with respect to the area it bounds, i.e., exterior rings are counterclockwise, and holes are clockwise.）

## 1.3 GeoJSON数据获取

> 相关站点链接：阿里云地理地图集站点地址：https://datav.aliyun.com/tools/atlas
>
> 高德地图查询行政区划边界以及属性信息链接：https://lbs.amap.com/api/webservice/guide/api/district/

### 1.3.1 阿里云GeoAtlas

​	首先感谢阿里，在国内两大地图厂商百度地图和高德地图站点都未能直接找到国家行政区划的GeoJson数据，高德地图提供了web api来获取行政区划边界以及其他信息，可惜不是标准的GeoJson格式。百度未提供web api，提供了js api来获取行政区划边界（网上信息，未证实）。阿里云站点提供了完整的全国、省份、城市、区县的GeoJson数据。

​	需要说明的是，对于国内地图数据，无论是GeoJSON文件还是shp文件，最好从国内大企下载，国外站点通常存在领土缺失问题。

​	阿里云地理地图集站点地址：https://datav.aliyun.com/tools/atlas，可以在左上角选择框中输入行政区划名称或者国家基础地理信息中心定义的六位区域代码，然后可以在右侧地图上预览地图显示效果，并可以通过JSON API预览GeoJSON数据，或者通过复制按钮复制GeoJSON内容，也可以通过左下角按钮下载geojson、svg文件。

![image-20200923233519863](/Users/caixiangning/CurtisProjects/markdown/curtis-project/img/curtis-geometry-geojson-shpfile/aliyun-GeoAtlas.png)

​	关于行政区划代码，可参考中华人民共和国民政部网站：http://www.mca.gov.cn/article/sj/xzqh/2020/,具体路径是：首页> 民政数据> 行政区划代码> 2020年行政区划代码。

### 1.3.2 高德Web Service Api

​	高德地图虽然没有提供行政区划GeoJSON文件下载功能（截止到2020.10），但是提供了web service api来查询行政区划的边界以及其他属性信息，可以自己解析并生成GeoJSON文件，介于阿里全资收购了高德地图，所以怀疑阿里云提供的GeoJSON的地理边界以及属性信息就是来源于高德地图。

​	高德地图查询行政区划边界以及属性信息链接：https://lbs.amap.com/api/webservice/guide/api/district/，具体查询方式见文档中的api

![image-20200923235744519](/Users/caixiangning/CurtisProjects/markdown/curtis-project/img/curtis-geometry-geojson-shpfile/amap-WebServiceApi.png)

### 1.3.3 补充：行政区划数据说明

* 中华人名共和国行政区划

来源：中华人名共和国民政部官方网站
时间：2020年8月中华人民共和国县以上行政区划代码
站点：首页> 民政数据> 统计月报> 全国数据
网址：http://www.mca.gov.cn/article/sj/tjyb/qgsj/

* 行政区划geojson

来源：阿里云datav（实际数据取自于高德开放平台）
时间：截止2020年4月
网址：https://datav.aliyun.com/tools/atlas	
说明：不包含子区域：https://geo.datav.aliyun.com/areas_v2/bound/100000.json
            包含子区域：https://geo.datav.aliyun.com/areas_v2/bound/100000_full.json

# 2 GeoJSON数据处理

> 摘自：geotools官方文档：http://docs.geotools.org/stable/javadocs/
>
> ​			geotools GitHub仓库地址：https://github.com/geotools/geotools

## 2.1 GeoJSON数据处理依赖及版本说明

### 2.1.1 GeoJSON数据处理使用版本以及仓库

```xml-dtd
    <properties>
				<!-- 截止到2020-10最新版本 -->
        <jts.version>1.17.1</jts.version>
        <geotools.version>24.0</geotools.version>
    </properties>

		<repositories>
        <!-- geotools最新版本24.0尚未发布到中央仓库，所以需要添加OSGeo仓库 -->
        <repository>
            <id>osgeo</id>
            <name>OSGeo Repository</name>
            <url>https://repo.osgeo.org/repository/release/</url>
        </repository>
    </repositories>
```

### 2.1.2 GeoJSON数据处理使用依赖以及说明

```xml-dtd
<!-- Geometry相关依赖，用于构建Geometry对象，计算Geometry属性以及计算Geometry之间关系，GeoTools也使用 -->
<!-- https://mvnrepository.com/artifact/org.locationtech.jts/jts-core Aug, 2020 -->
<dependency>
    <groupId>org.locationtech.jts</groupId>
    <artifactId>jts-core</artifactId>
    <version>${jts.version}</version>
</dependency>

<!-- 用于处理/读写GeoJson数据相关依赖 -->
<!-- gt-opengis包的SimpleFeature接口提供多个实例方法用于获取空间地理坐标信息以及非地理属性，无实现。
     gt-opengis包的ComplexAttribute接口（SimpleFeature继承该接口）提供实例方法用于获取非地理属性，无实现。
     gt-main包提供SimpleFeature接口的实现类SimpleFeatureImpl，用于获取上面接口提供的用于获取地理坐标以及非地理属性。
     gt-main包提供SimpleFeatureTypeBuilder类的buildFeatureType()方法用于创建SimpleFeatureType。
     gt-main包提供SimpleFeatureBuilder类的buildFeature(String id)方法用于创建SimpleFeature对象。
     gt-main包提供ListFeatureCollection类提供构造函数ListFeatureCollection(SimpleFeatureType schema, List<SimpleFeature> list)
     用于创建ListFeatureCollection对象，该对象间接实现SimpleFeatureCollection接口，从而相当于创建了SimpleFeatureCollection对象 -->
<!-- gt-main包含gt-referencing依赖、gt-referencing又包含gt-opengis依赖，所以不需要引入这两个依赖 -->
<!-- https://mvnrepository.com/artifact/org.geotools/gt-main -->
<dependency>
    <groupId>org.geotools</groupId>
    <artifactId>gt-main</artifactId>
    <version>${geotools.version}</version>
</dependency>

<!-- gt-geojson包的FeatureJSON类提供读取写入Feature以及FeatureCollection对象的能力
（Feature对应type是Feature的GeoJSON，FeatureCollection对应type是FeatureCollection的GeoJSON） -->
<!-- gt-geojson包的GeometryJSON类提供多个读写Geometry以及GeometryCollection对象的能力
（Feature对应type是Geometry的GeoJSON，GeometryCollection对应type是GeometryCollection的GeoJSON） -->
<!-- https://mvnrepository.com/artifact/org.geotools/gt-geojson Jul, 2020 -->
<dependency>
    <groupId>org.geotools</groupId>
    <artifactId>gt-geojson</artifactId>
    <version>${geotools.version}</version>
</dependency>
```

## 2.2 GeoJSON数据处理核心Api

### 2.2.1 读写类型为Feature或FeatureCollection的GeoJSON的核心类 - FeatureJSON

​	gt-geojson包的FeatureJSON类提供读取写入Feature以及FeatureCollection对象的能力（Feature对应type是Feature的GeoJSON，FeatureCollection对应type是FeatureCollection的GeoJSON），核心接口是：

```java
  	// 将Feature对象作为GeoJSON写入输出流中
    public void writeFeature(SimpleFeature feature, Object output);

    // 将Feature对象作为GeoJSON写入输出流中
    public void writeFeature(SimpleFeature feature, OutputStream output) throws IOException;
  
    // 从输入流中读取GeoJSON构建Feature对象
    public SimpleFeature readFeature(Object input) throws IOException;

    // 从输入流中读取GeoJSON构建Feature对象
    public SimpleFeature readFeature(InputStream input) throws IOException;

    // 将FeatureCollection对象作为GeoJSON写入到输出流中
    public void writeFeatureCollection(FeatureCollection features, Object output);

		// 将FeatureCollection对象作为GeoJSON写入到输出流中
    public void writeFeatureCollection(FeatureCollection features, OutputStream output);

    // 从输入流中读取GeoJSON作为FeatureCollection对象
    public FeatureCollection readFeatureCollection(Object input) throws IOException;

    // 从输入流中读取GeoJSON作为FeatureCollection对象
    public FeatureCollection readFeatureCollection(InputStream input) throws IOException;

    // 将Feature对象作为GeoJSON写入到字符串中并返回
    public String toString(SimpleFeature feature) throws IOException;

    // 将FeatureCollection对象作为GeoJSON写入到字符串中并返回
    public String toString(FeatureCollection features) throws IOException;
```

### 2.2.2 读写类型为Geometry或GeometryCollection的GeoJSON的核心类 - GeometryJSON

​		gt-geojson包的GeometryJSON类提供多个读写Geometry以及GeometryCollection对象的能力（Feature对应type是Geometry的GeoJSON，GeometryCollection对应type是GeometryCollection的GeoJSON），核心接口是：

```java
		// 将Geometry对象作为GeoJSON写入输出流中
    public void write(Geometry geometry, Object output) throws IOException;

    // 将Geometry对象作为GeoJSON写入输出流中
    public void write(Geometry geometry, OutputStream output);

    // 从输入流中读取GeoJSON构建Geometry对象
    public Geometry read(Object input) throws IOException;

    // 从输入流中读取GeoJSON构建Geometry对象
    public Geometry read(InputStream input) throws IOException;

		// 将GeometryCollection对象作为GeoJSON写入输出流中
    public void writeGeometryCollection(GeometryCollection gcol, Object output) throws IOException;

    // 将GeometryCollection对象作为GeoJSON写入输出流中
    public void writeGeometryCollection(GeometryCollection gcol, OutputStream output)
            throws IOException;

		// 从输入流中读取GeoJSON构建GeometryCollection对象
    public GeometryCollection readGeometryCollection(Object input) throws IOException;

		// 从输入流中读取GeoJSON构建GeometryCollection对象
    public GeometryCollection readGeometryCollection(InputStream input) throws IOException;

    // 将Geometry对象作为GeoJSON写入到字符串中并返回
    public String toString(Geometry geometry);

    // 将GeometryCollection对象作为GeoJSON写入到字符串中并返回
    public String toString(GeometryCollection geometry);

```

​		需要注意的是GeometryJSON中有个scale用于指定精度，默认精度是4，通常我们指定精度是6（精确到1米）。

### 2.2.3 获取Feature对象信息-地理空间坐标信息以及非地理属性

​		gt-opengis包的SimpleFeature接口提供多个实例方法用于获取空间地理坐标信息以及非地理属性，具体的实现是gt-main包的SimpleFeature接口的实现类SimpleFeatureImpl。

```java
		// 获取GeoJSON的空间地理对象Geometry，返回值是具体的Geometry对象：Point、LineString、Polygon等。
    Object getDefaultGeometry();

		// 获取GeoJSON属性中指定key的值，该方法需要提前预知非地理属性的名称。
    Object getAttribute(String name);
```

​		gt-opengis包的ComplexAttribute接口（SimpleFeature继承该接口）提供实例方法用于获取非地理属性，具体的实现是gt-main包的SimpleFeature接口的实现类SimpleFeatureImpl。

```java
		// 获取GeoJSON的属性集合，Property对象包含属性的key和value。
    Collection<Property> getProperties();
```

### 2.2.4 创建Feature或FeatureCollection对象的核心类-SimpleFeatureTypeBuilder和SimpleFeatureBuilder类

​		gt-main包的SimpleFeatureTypeBuilder类提供buildFeatureType()方法用于创建SimpleFeatureType，创建之前调用SimpleFeatureTypeBuilder对象的add(String name, Class<?> binding)方法添加非地理属性以及属性类型信息，通过调用SimpleFeatureTypeBuilder对象的setCRS(CoordinateReferenceSystem crs)方法设置坐标系信息。SimpleFeatureTypeBuilder类提供的实例方法原型如下：

```java
// 设置坐标系信息
public void setCRS(CoordinateReferenceSystem crs);
  
// 添加非地理属性名称以及值的类型
public void add(String name, Class<?> binding);

// 创建FeatureType对象
public SimpleFeatureType buildFeatureType();
```

​		gt-main包的SimpleFeatureBuilder方法提供buildFeature(String id)方法用于创建SimpleFeature对象，创建之前调用SimpleFeatureBuilder对象的add(Object value)方法添加地理坐标Geometry对象以及非地理属性的值（注意设置属性的顺序）。需要注意的是创建SimpleFeatureBuilder对象时要使用SimpleFeatureType作为参数。SimpleFeatureBuilder类提供的示例方法原型如下：

```java
// 使用SimpleFeatureType对象创建SimpleFeatureBuilder对象
public SimpleFeatureBuilder(SimpleFeatureType featureType);

// 添加地理坐标Geometry对象以及非地理属性
public void add(Object value);

// 创建id为指定名称的SimpleFeature对象
public SimpleFeature buildFeature(String id);
```

​		gt-main包的ListFeatureCollection类提供构造函数ListFeatureCollection(SimpleFeatureType schema, List<SimpleFeature> list)用于创建ListFeatureCollection对象，该对象间接实现SimpleFeatureCollection接口，从而相当于创建了SimpleFeatureCollection对象。ListFeatureCollection类提供的构造函数原型如下：

```java
// 创建ListFeatureCollection对象（ListFeatureCollection实现了SimpleFeatureCollection接口）
public ListFeatureCollection(SimpleFeatureType schema, List<SimpleFeature> list)
```

### 2.2.5 获取FeatureCollection的Feature集合

​		gt-main包的FeatureCollection接口提供遍历Feature集合的迭代器，具体实现是gt-main包的SimpleFeatureCollection接口的实现类DefaultFeatureCollection类。

```java
    // FeatureCollection接口提供的获取FeatureCollection对象中的Feature集合的迭代器
		FeatureIterator<F> features();
```

```java
		// DefaultFeatureCollection类提供的迭代器，迭代器中元素是SimpleFeature
		public Iterator<SimpleFeature> iterator()
```

​		接下来就可以通过SimpleFeature迭代器来获取SimpleFeature并进一步获取地理空间坐标信息以及非地理信息。见2.1.2部分。

## 2.3 GeoJSON数据读取

### 2.3.1读取类型为Feature的GeoJSON示例

```java
		@Test
    public void testReadGeoJsonOfFeature() {
        // 使用new GeometryJSON(6)指定精度是6位，默认精度是4位。
        FeatureJSON featureJson = new FeatureJSON(new GeometryJSON(6));
        try (InputStream resourceAsStream = GeoJsonReadWriteTest.class.getClassLoader()
                .getResourceAsStream("geojson-test/Feature-test.json")) {
            SimpleFeature simpleFeature = featureJson.readFeature(resourceAsStream);
            // The Class Type Of feature Is : class org.geotools.feature.simple.SimpleFeatureImpl
            System.out.println("The Class Type Of feature Is : " + simpleFeature.getClass());

            /***** 获取Feature唯一标识符 *****/
            String id = simpleFeature.getID();
            // The Id Of Feature Is : feature-0
            System.out.println("The Id Of Feature Is : " + id);

            /***** 获取Feature空间地理数据 *****/
            System.out.println("/***** 获取Feature空间地理数据 *****/");
            Object defaultGeometry = simpleFeature.getDefaultGeometry();
            // The class Type Of DefaultGeometry Is : class org.locationtech.jts.geom.MultiPolygon
            System.out.println("The class Type Of DefaultGeometry Is : " + defaultGeometry.getClass());
            Geometry geometry = (Geometry) defaultGeometry;
//            System.out.println(geometry.getSRID());
//            CoordinateReferenceSystem
//            geometry.setSRID(4326);
            // The Well-known Text Of geometry Is : MULTIPOLYGON (((116.725518 39.624075, 116.721858 39.621756, ..)))
            System.out.println("The Well-known Text Of geometry Is : " + geometry.toText());
            // geometry的实际类型是各个空间类型：Point、LineString、Polygon等
            if (geometry instanceof MultiPolygon) {
                MultiPolygon multiPolygon = (MultiPolygon) geometry;
                // The Length Of Geometry Obj Is : 10.545176361178681
                System.out.println("The Length Of Geometry Obj Is : " + multiPolygon.getLength());
                // The Area Of Geometry Obj Is : 1.7355268295095028
                System.out.println("The Area Of Geometry Obj Is : " + multiPolygon.getArea());
                // The Coordinate Of Geometry Obj Is : (116.725518, 39.624075, NaN)
                System.out.println("The Coordinate Of Geometry Obj Is : " + multiPolygon.getCoordinate());
                // The Boundary Of Geometry Obj Is : MULTILINESTRING ((116.725518 39.624075, 116.721858 39.621756, ..))
                System.out.println("The Boundary Of Geometry Obj Is : " + multiPolygon.getBoundary());
                // The GeometryType Of Geometry Obj Is : MultiPolygon
                System.out.println("The GeometryType Of Geometry Obj Is : " + multiPolygon.getGeometryType());
            }

            /***** 获取Feature非地理属性数据 *****/
            // simpleFeature和getAttributes()返回属性的值，包含坐标但是不包括key，所以无意义
            List<Object> attributes = simpleFeature.getAttributes();

            System.out.println("/***** 获取Feature非地理属性数据-循环遍历 *****/");
            Collection<Property> properties = simpleFeature.getProperties();
            for (Property property : properties) {
                Name propertyKey = property.getName();
                Object propertyValue = property.getValue();
                System.out.println(id + "的properties属性节点的key和值分别是 -> " + propertyKey + " : " + propertyValue);
            }

            System.out.println("/***** 获取Feature非地理属性数据-根据已知key获取 *****/");
            Long adcode = (Long) simpleFeature.getAttribute("adcode");
            System.out.println(id + "的properties属性节点的key和值分别是 -> " + "adcode" + " : " + adcode);
            String name = (String) simpleFeature.getAttribute("name");
            System.out.println(id + "的properties属性节点的key和值分别是 -> " + "name" + " : " + name);
            List<Double> center = (List<Double>) simpleFeature.getAttribute("center");
            System.out.println(id + "的properties属性节点的key和值分别是 -> " + "center" + " : " + center);
            List<Double> centroid = (List<Double>) simpleFeature.getAttribute("centroid");
            System.out.println(id + "的properties属性节点的key和值分别是 -> " + "centroid" + " : " + centroid);
            String level = (String) simpleFeature.getAttribute("level");
            System.out.println(id + "的properties属性节点的key和值分别是 -> " + "level" + " : " + level);
            Object parent = simpleFeature.getAttribute("parent");
            System.out.println(id + "的properties属性节点的key和值分别是 -> " + "parent" + " : " + parent);
            Geometry geometryProperty = (Geometry) simpleFeature.getAttribute("geometry");
            System.out.println(id + "的properties属性节点的key和值分别是 -> " + "geometry" + " : " + geometryProperty);
//            System.out.println(simpleFeature);

            /***** 将SimpleFeature对象写入GeoJSON文件或字符串 *****/
            StringWriter stringWriter = new StringWriter();
            featureJson.writeFeature(simpleFeature, stringWriter);
            System.out.println(stringWriter.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```

### 2.3.2 读取类型为FeatureCollection的GeoJSON示例

```java
		@Test
    public void testReadGeoJsonOfFeatureCollection() {
        // 使用new GeometryJSON(6)指定精度是6位，默认精度是4位。
        FeatureJSON featureJson = new FeatureJSON(new GeometryJSON(7));
        try (InputStream resourceAsStream = GeoJsonReadWriteTest.class.getClassLoader()
                .getResourceAsStream("geojson-test/FeatureCollection-test.json")) {

            FeatureCollection featureCollection = featureJson.readFeatureCollection(resourceAsStream);
            // The Class Type Of featureCollection Is : class org.geotools.feature.DefaultFeatureCollection
            System.out.println("The Class Type Of featureCollection Is : " + featureCollection.getClass());
            SimpleFeatureIterator featureIterator = (SimpleFeatureIterator) featureCollection.features();
            // 遍历features节点
            while (featureIterator.hasNext()) {
                SimpleFeature simpleFeature = featureIterator.next();
                // The Class Type Of feature Is : class org.geotools.feature.simple.SimpleFeatureImpl
                System.out.println("The Class Type Of feature Is : " + simpleFeature.getClass());

                /***** 获取Feature唯一标识符 *****/
                String id = simpleFeature.getID();
                // The Id Of Feature Is : feature-0
                System.out.println("The Id Of Feature Is : " + id);

                /***** 获取Feature空间地理数据 *****/
                System.out.println("/***** 获取Feature空间地理数据 *****/");
                Object defaultGeometry = simpleFeature.getDefaultGeometry();
                // The class Type Of DefaultGeometry Is : class org.locationtech.jts.geom.MultiPolygon
                System.out.println("The class Type Of DefaultGeometry Is : " + defaultGeometry.getClass());
                Geometry geometry = (Geometry) defaultGeometry;
                // The Well-known Text Of geometry Is : MULTIPOLYGON (((116.725518 39.624075, 116.721858 39.621756, ..)))
                System.out.println("The Well-known Text Of geometry Is : " + geometry.toText());
                // geometry的实际类型是各个空间类型：Point、LineString、Polygon等
                if (geometry instanceof MultiPolygon) {
                    MultiPolygon multiPolygon = (MultiPolygon) geometry;
                    // The Length Of Geometry Obj Is : 10.545176361178681
                    System.out.println("The Length Of Geometry Obj Is : " + multiPolygon.getLength());
                    // The Area Of Geometry Obj Is : 1.7355268295095028
                    System.out.println("The Area Of Geometry Obj Is : " + multiPolygon.getArea());
                    // The Coordinate Of Geometry Obj Is : (116.725518, 39.624075, NaN)
                    System.out.println("The Coordinate Of Geometry Obj Is : " + multiPolygon.getCoordinate());
                    // The Boundary Of Geometry Obj Is : MULTILINESTRING ((116.725518 39.624075, 116.721858 39.621756, ..))
                    System.out.println("The Boundary Of Geometry Obj Is : " + multiPolygon.getBoundary());
                    // The GeometryType Of Geometry Obj Is : MultiPolygon
                    System.out.println("The GeometryType Of Geometry Obj Is : " + multiPolygon.getGeometryType());
                }

                /***** 获取Feature非地理属性数据 *****/
                // simpleFeature和getAttributes()返回属性的值，包含坐标但是不包括key，所以无意义
                List<Object> attributes = simpleFeature.getAttributes();

                System.out.println("/***** 获取Feature非地理属性数据-循环遍历 *****/");
                Collection<Property> properties = simpleFeature.getProperties();
                for (Property property : properties) {
                    Name propertyKey = property.getName();
                    Object propertyValue = property.getValue();
                    System.out.println(id + "的properties属性节点的key和值分别是 -> " + propertyKey + " : " + propertyValue);
                }

                System.out.println("/***** 获取Feature非地理属性数据-根据已知key获取 *****/");
                Long adcode = (Long) simpleFeature.getAttribute("adcode");
                System.out.println(id + "的properties属性节点的key和值分别是 -> " + "adcode" + " : " + adcode);
                String name = (String) simpleFeature.getAttribute("name");
                System.out.println(id + "的properties属性节点的key和值分别是 -> " + "name" + " : " + name);
                List<Double> center = (List<Double>) simpleFeature.getAttribute("center");
                System.out.println(id + "的properties属性节点的key和值分别是 -> " + "center" + " : " + center);
                List<Double> centroid = (List<Double>) simpleFeature.getAttribute("centroid");
                System.out.println(id + "的properties属性节点的key和值分别是 -> " + "centroid" + " : " + centroid);
                String level = (String) simpleFeature.getAttribute("level");
                System.out.println(id + "的properties属性节点的key和值分别是 -> " + "level" + " : " + level);
                Object parent = simpleFeature.getAttribute("parent");
                System.out.println(id + "的properties属性节点的key和值分别是 -> " + "parent" + " : " + parent);
                Geometry geometryProperty = (Geometry) simpleFeature.getAttribute("geometry");
                System.out.println(id + "的properties属性节点的key和值分别是 -> " + "geometry" + " : " + geometryProperty);

//                System.out.println(simpleFeature);
            }

            /***** 将FeatureCollection对象写入GeoJSON文件或字符串 *****/
            StringWriter stringWriter = new StringWriter();
            featureJson.writeFeatureCollection(featureCollection, stringWriter);
            System.out.println(stringWriter.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```

### 2.3.3 读取类型为Geometry的GeoJSON示例

```java
@Test
public void testReadGeoJsonOfGeometry() {
    // 使用new GeometryJSON(6)指定精度是6位，默认精度是4位。
    GeometryJSON geometryJson = new GeometryJSON(6);
    try (InputStream resourceAsStream = GeoJsonReadWriteTest.class.getClassLoader()
            .getResourceAsStream("geojson-test/Geometry-test.json")) {
        Geometry geometry = geometryJson.read(resourceAsStream);

        // The Well-known Text Of geometry Is : MULTIPOLYGON (((116.725518 39.624075, 116.721858 39.621756, ..)))
        System.out.println("The Well-known Text Of geometry Is : " + geometry.toText());
        // geometry的实际类型是各个空间类型：Point、LineString、Polygon等
        if (geometry instanceof MultiPolygon) {
            MultiPolygon multiPolygon = (MultiPolygon) geometry;
            // The Length Of Geometry Obj Is : 10.545176361178681
            System.out.println("The Length Of Geometry Obj Is : " + multiPolygon.getLength());
            // The Area Of Geometry Obj Is : 1.7355268295095028
            System.out.println("The Area Of Geometry Obj Is : " + multiPolygon.getArea());
            // The Coordinate Of Geometry Obj Is : (116.725518, 39.624075, NaN)
            System.out.println("The Coordinate Of Geometry Obj Is : " + multiPolygon.getCoordinate());
            // The Boundary Of Geometry Obj Is : MULTILINESTRING ((116.725518 39.624075, 116.721858 39.621756, ..))
            System.out.println("The Boundary Of Geometry Obj Is : " + multiPolygon.getBoundary());
            // The GeometryType Of Geometry Obj Is : MultiPolygon
            System.out.println("The GeometryType Of Geometry Obj Is : " + multiPolygon.getGeometryType());
        }

        /***** 将Geometry对象写入GeoJSON文件或字符串 *****/
        StringWriter stringWriter = new StringWriter();
        geometryJson.write(geometry, stringWriter);
        System.out.println(stringWriter.toString());
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

### 2.3.4 读取类型为GeometryCollection的GeoJSON示例

```java
@Test
public void testReadGeoJsonOfGeometryCollection() {
    GeometryJSON geometryJSON = new GeometryJSON(6);
    try (InputStream resourceAsStream = GeoJsonReadWriteTest.class.getClassLoader()
            .getResourceAsStream("geojson-test/GeometryCollection-test.json")) {

        GeometryCollection geometryCollection = geometryJSON.readGeometryCollection(resourceAsStream);

        // The Well-known Text Of geometryCollection Is : GEOMETRYCOLLECTION (MULTIPOLYGON (((116.725518 39.624075, 116.721858 39.621756, ..)))
        System.out.println("The Well-known Text Of geometryCollection Is : " + geometryCollection.toText());
        // The Length Of geometryCollection Obj Is : 10.545176361178681
        System.out.println("The Length Of geometryCollection Obj Is : " + geometryCollection.getLength());
        // The Area Of geometryCollection Obj Is : 1.7355268295095028
        System.out.println("The Area Of geometryCollection Obj Is : " + geometryCollection.getArea());
        // The Coordinate Of geometryCollection Obj Is : (116.725518, 39.624075, NaN)
        System.out.println("The Coordinate Of geometryCollection Obj Is : " + geometryCollection.getCoordinate());
        // java.lang.IllegalArgumentException: Operation does not support GeometryCollection arguments
        // System.out.println("The Boundary Of geometryCollection Obj Is : " + geometryCollection.getBoundary());
        // The GeometryType Of geometryCollection Obj Is : GeometryCollection
        System.out.println("The GeometryType Of geometryCollection Obj Is : " + geometryCollection.getGeometryType());


        int numGeometries = geometryCollection.getNumGeometries();
        for (int i = 0; i < numGeometries; i++) {
            // 通过GeometryCollection的getGeometryN(int n)方法可以获取Geometry集合中指定位置的Geometry，位置从0开始
            Geometry geometryN = geometryCollection.getGeometryN(i);
            // geometry的实际类型是各个空间类型：Point、LineString、Polygon等
            if (geometryN instanceof MultiPolygon) {
                MultiPolygon multiPolygon = (MultiPolygon) geometryN;
                // The Length Of Geometry Obj Is : 10.545176361178681
                System.out.println("The Length Of Geometry Obj Is : " + multiPolygon.getLength());
                // The Area Of Geometry Obj Is : 1.7355268295095028
                System.out.println("The Area Of Geometry Obj Is : " + multiPolygon.getArea());
                // The Coordinate Of Geometry Obj Is : (116.725518, 39.624075, NaN)
                System.out.println("The Coordinate Of Geometry Obj Is : " + multiPolygon.getCoordinate());
                // The Boundary Of Geometry Obj Is : MULTILINESTRING ((116.725518 39.624075, 116.721858 39.621756, ..))
                System.out.println("The Boundary Of Geometry Obj Is : " + multiPolygon.getBoundary());
                // The GeometryType Of Geometry Obj Is : MultiPolygon
                System.out.println("The GeometryType Of Geometry Obj Is : " + multiPolygon.getGeometryType());
            }
        }

        /***** 将GeometryCollection对象写入GeoJSON文件或字符串 *****/
        StringWriter stringWriter = new StringWriter();
        geometryJSON.write(geometryCollection, stringWriter);
        System.out.println(stringWriter.toString());
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

## 2.4 GeoJSON数据写入

### 2.4.1 写入类型为Feature的GeoJSON示例

```java
@Test
public void testWriteGeoJsonOfFeatureCollection() throws ParseException, IOException {
		// 使用SimpleFeatureTypeBuilder对象的buildFeatureType()方法创建SimpleFeatureType对象
    SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
    simpleFeatureTypeBuilder.setName("name");
    simpleFeatureTypeBuilder.setCRS(DefaultGeographicCRS.WGS84);
    simpleFeatureTypeBuilder.add("Geometry", MultiPolygon.class);
    simpleFeatureTypeBuilder.add("adcode", Long.class);
    simpleFeatureTypeBuilder.add("name", String.class);
    // 注意对于数组来说，声明类型的时候一定要用数组的Class，如果使用List.class则GeoJSON片段会出现："center":"[116.405285, 39.904989]"
    simpleFeatureTypeBuilder.add("center", Double[].class);
    simpleFeatureTypeBuilder.add("centroid", Double[].class);
    simpleFeatureTypeBuilder.add("childrenNum", Integer[].class);
    simpleFeatureTypeBuilder.add("level", String.class);
    simpleFeatureTypeBuilder.add("acroutes", Integer[].class);
    SimpleFeatureType simpleFeatureType = simpleFeatureTypeBuilder.buildFeatureType();

//        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    System.out.println("The SRID Of GeometryFactory Is : " + geometryFactory.getSRID());
    System.out.println("The PrecisionModel Of GeometryFactory Is : " + geometryFactory.getPrecisionModel());
    WKTReader wktReader = new WKTReader(geometryFactory);

    List<SimpleFeature> simpleFeatures = Lists.newArrayList();
    // 使用SimpleFeatureBuilder创建SimpleFeature对象，进而依据数据条数构建SimpleFeature集合
    SimpleFeatureBuilder simpleFeatureBuilder = new SimpleFeatureBuilder(simpleFeatureType);
    String wkt = "MULTIPOLYGON (((116.725518 39.624075, 116.721858 39.621756, ...")));
    MultiPolygon multiPolygon = (MultiPolygon) wktReader.read(wkt);
    simpleFeatureBuilder.add(multiPolygon);
    simpleFeatureBuilder.add(110000);
    simpleFeatureBuilder.add("北京市");
    simpleFeatureBuilder.add(Lists.newArrayList(116.405285, 39.904989));
    simpleFeatureBuilder.add(Lists.newArrayList(116.419889, 40.189911));
    simpleFeatureBuilder.add(16);
    simpleFeatureBuilder.add("province");
    simpleFeatureBuilder.add(new Integer[]{100000});
    SimpleFeature simpleFeature = simpleFeatureBuilder.buildFeature("feature-0");
    simpleFeatures.add(simpleFeature);

    // 使用SimpleFeatureType对象和SimpleFeature集合构建SimpleFeatureCollection对象
    SimpleFeatureCollection simpleFeatureCollection = new ListFeatureCollection(simpleFeatureType, simpleFeatures);


    StringWriter writer = new StringWriter();
    // 使用new GeometryJSON(6)指定精度是6位，默认精度是4位。
    GeometryJSON geometryJson = new GeometryJSON(6);
    FeatureJSON featureJson = new FeatureJSON(geometryJson);
    // 使用FeatureJSON的writeFeatureCollection方法将SimpleFeatureCollection写入文件
    featureJson.writeFeatureCollection(simpleFeatureCollection, writer);
    System.out.println(writer.toString());
}
```

### 2.4.2 写入类型为FeatureCollection的GeoJSON示例

```java
@Test
public void testWriteGeoJsonOfFeature() throws ParseException, IOException {

		// 使用SimpleFeatureTypeBuilder创建SimpleFeatureType对象
		SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
		simpleFeatureTypeBuilder.setName("name");
		simpleFeatureTypeBuilder.setCRS(DefaultGeographicCRS.WGS84);
		simpleFeatureTypeBuilder.add("Geometry", MultiPolygon.class);
		simpleFeatureTypeBuilder.add("adcode", Long.class);
		simpleFeatureTypeBuilder.add("name", String.class);
		// 注意对于数组来说，声明类型的时候一定要用数组的Class，如果使用List.class则GeoJSON片段会出现："center":"[116.405285, 39.904989]"
		simpleFeatureTypeBuilder.add("center", Double[].class);
		simpleFeatureTypeBuilder.add("centroid", Double[].class);
		simpleFeatureTypeBuilder.add("childrenNum", Integer[].class);
		simpleFeatureTypeBuilder.add("level", String.class);
		simpleFeatureTypeBuilder.add("acroutes", Integer[].class);
		SimpleFeatureType simpleFeatureType = simpleFeatureTypeBuilder.buildFeatureType();


		// GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
		System.out.println("The SRID Of GeometryFactory Is : " + geometryFactory.getSRID());
		System.out.println("The PrecisionModel Of GeometryFactory Is : " + geometryFactory.getPrecisionModel());
		WKTReader wktReader = new WKTReader(geometryFactory);


		// 使用SimpleFeatureBuilder创建SimpleFeature对象
		SimpleFeatureBuilder simpleFeatureBuilder = new SimpleFeatureBuilder(simpleFeatureType);
		String wkt = "MULTIPOLYGON (((116.725518 39.624075, 116.721858 39.621756, ...")));
		MultiPolygon multiPolygon = (MultiPolygon) wktReader.read(wkt);
		simpleFeatureBuilder.add(multiPolygon);
		simpleFeatureBuilder.add(110000);
		simpleFeatureBuilder.add("北京市");
		simpleFeatureBuilder.add(Lists.newArrayList(116.405285, 39.904989));
		simpleFeatureBuilder.add(Lists.newArrayList(116.419889, 40.189911));
		simpleFeatureBuilder.add(16);
		simpleFeatureBuilder.add("province");
		simpleFeatureBuilder.add(new Integer[]{100000});
		SimpleFeature simpleFeature = simpleFeatureBuilder.buildFeature("feature-0");


		StringWriter writer = new StringWriter();
		// 使用new GeometryJSON(6)指定精度是6位，默认精度是4位。
		GeometryJSON geometryJson = new GeometryJSON(6);
		FeatureJSON featureJson = new FeatureJSON(geometryJson);
		// 使用FeatureJSON的writeFeature方法将SimpleFeature写入文件
		featureJson.writeFeature(simpleFeature, writer);
		System.out.println(writer.toString());
}
```

### 2.4.3 写入类型为Geometry的GeoJSON示例

```java
@Test
public void testWriteGeoJsonOfGeometry() throws ParseException, IOException {
    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    System.out.println("The SRID Of GeometryFactory Is : " + geometryFactory.getSRID());
    System.out.println("The PrecisionModel Of GeometryFactory Is : " + geometryFactory.getPrecisionModel());
    WKTReader wktReader = new WKTReader(geometryFactory);
    String wkt = "MULTIPOLYGON (((116.725518 39.624075, 116.721858 39.621756, ...")));
    Geometry geometry = wktReader.read(wkt);

    StringWriter writer = new StringWriter();
    // 使用new GeometryJSON(6)指定精度是6位，默认精度是4位。
    GeometryJSON geometryJson = new GeometryJSON(6);
    // 使用GeometryJSON的write方法将Geometry写入文件
    geometryJson.write(geometry, writer);
    System.out.println(writer.toString());
}
```

### 2.4.4 写入类型为GeometryCollection的GeoJSON示例

```java
@Test
public void testWriteGeoJsonOfGeometryCollection() throws ParseException, IOException {
    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    System.out.println("The SRID Of GeometryFactory Is : " + geometryFactory.getSRID());
    System.out.println("The PrecisionModel Of GeometryFactory Is : " + geometryFactory.getPrecisionModel());
    WKTReader wktReader = new WKTReader(geometryFactory);
    String wkt = "MULTIPOLYGON (((116.725518 39.624075, 116.721858 39.621756, ...")));
    Geometry geometry = wktReader.read(wkt);

    Geometry[] geometryArray = new Geometry[1];
    geometryArray[0] = geometry;

    // 使用Geometry[]数组和GeometryFactory来创建GeometryCollection集合对象
    GeometryCollection geometryCollection = new GeometryCollection(geometryArray, geometryFactory);

    StringWriter writer = new StringWriter();
    // 使用new GeometryJSON(6)指定精度是6位，默认精度是4位。
    GeometryJSON geometryJson = new GeometryJSON(6);
    // 使用GeometryJSON的write方法将GeometryCollection写入文件
    geometryJson.write(geometryCollection, writer);
    System.out.println(writer.toString());
}
```

***

***

***

# 3 ESRI Shapefile（shp）

## 3.1 Shapefile概述

> 摘自：中文维基百科：https://zh.wikipedia.org/wiki/Shapefile
>
> ​			英文维基百科：https://en.wikipedia.org/wiki/Shapefile

​	ESRI Shapefile（shp），或简称shapefile，是美国环境系统研究所公司（ESRI）开发的空间数据开放格式。shapefile格式是用于地理信息系统（geographic information system (GIS)）软件的地理空间矢量数据格式。	

## 3.2 Shapefile文件

​		Shapefile文件指的是一种文件存储的方法，实际上该种文件格式是由多个文件组成的。其中，要组成一个Shapefile，有三个文件是必不可少的，它们分别是"`.shp`", "`.shx`"与 "`.dbf`"文件。表示同一数据的一组文件其文件名前缀应该相同，而且所有的文件都必须位于同一个目录之中。

​		例如，存储一个关于湖的几何与属性数据，就必须有lake.shp，lake.shx与lake.dbf三个文件。而其中“真正”的Shapefile的后缀为shp，然而仅有这个文件数据是不完整的，必须要把其他两个附带上才能构成一组完整的地理数据。除了这三个必须的文件以外，还有八个可选的文件，使用它们可以增强空间数据的表达能力。

### 3.2.1 Shapefile必须的文件

- `.shp` — 图形格式，用于保存元素的地理位置信息。
- `.shx` — 图形索引格式。几何体位置索引，记录每一个几何体在shp文件之中的位置，能够加快向前或向后搜索一个几何体的效率。
- `.dbf` — 属性数据格式，用于存储元素的属性信息（dBase III+ 的数据表格式存储属性数据）。

注意：在每个`.shp`, `.shx`与`.dbf`文件之中，数据排序要求一致，也就是说地理位置信息和属性信息是根据数据在文件中出现的位置来对应的。shapefile通常以X与Y的方式来处理地理坐标，一般X对应经度，Y对应纬度，用户必须注意X，Y的顺序。

### 3.2.2 Shapefile可选的文件

- `.prj` — 投帧式，用于保存地理坐标系统与投影信息，是一个存储[well-known text](https://zh.wikipedia.org/w/index.php?title=Well-known_text&action=edit&redlink=1)投影描述符的文本文件。
- `.sbn` and `.sbx` — 几何体的空间索引
- `.fbn` and `.fbx` — 只读的Shapefiles的几何体的空间索引
- `.ain` and `.aih` — 列表中活动字段的属性索引。
- `.ixs` — 可读写Shapefile文件的地理编码索引
- `.mxs` — 可读写Shapefile文件的地理编码索引(ODB格式)
- `.atx` — `.dbf`文件的属性索引，其文件名格式为*shapefile*.*columnname*`.atx` (ArcGIS 8及之后的版本)
- `.shp.xml` — 以XML格式保存元数据。
- `.cpg` — 用于描述`.dbf`文件的[代码页](https://zh.wikipedia.org/wiki/代码页)，指明其使用的[字符编码](https://zh.wikipedia.org/wiki/字符编码)。

## 3.3 Shapefile文件获取

> 摘自：GMT中文手册：https://docs.gmt-china.org/latest/
>
> ​			GADM全球行政区划数据库：https://gadm.org/

### 3.3.1 从GADM下载Shapefile文件

​		经过查阅各类资料，发现国内尚未有任何组织或者机构（盈利性质的小公司除外）发布最新的国内行政区划的shapefile文件。GADM，全称Database of Global Administrative Areas，是一个高精度的全球行政区划数据库。其包含了全球所有国家和地区的国界、省界、市界、区界等多个级别的行政区划边界数据。

​		<u>需要注意的是：GADM提供的中国国界数据不符合中国的领土主张，省界、市界、区界等数据也不一定是最新的版本。在正式刊物中发表使用此类数据的图件时需格外谨慎。</u>

* GADM提供了两种下载方式：

  * 下载全球所有国家和地区的所有数据 https://gadm.org/download_world.html

  * 按国家下载 https://gadm.org/download_country_v3.html

* GADM数据说明

​	需要说明的是，GADM 中对country 的定义为“any entity with [an ISO country code](http://zh.wikipedia.org/wiki/ISO_3166-1)”。因而如果想要下载完整的中国数据，实际上需要下载China、Hong Kong、Macao和Taiwan 四个数据。GADM提供的中国国界数据不符合我国领土主张。

* GADM数据格式

  * Geopackage：可以被GDAL/OGR、ArcGIS、QGIS等软件读取

  * Shapefile：可直接用于ArcGIS等软件

  * KMZ：可直接在Google Earth中打开

  * R (sp)：可直接用于R语言绘图

  * R (sf)：可直接用于R语言绘图

* GADM数据分级

​	下载下来的数据文件的文件名类似 gadm36_USA_0.gmt，其中 USA 为国家/地区代码，0 表示行政等级。
以美国数据为例，其数据包含了三个等级：0级：即国界；1级：即州界；2级：即县界。

### 3.3.2 从阿里云下载GeoJSON文件再转换为Shapefile文件（建议）

​		国外大多数站点都存在中国领土主张不一致的问题，国内其他站点数据大多来源于小厂或者个人，数据还可能滞后不准确，所以还是建议从阿里云下载GeoJSON文件后使用GIS软件转换为shapefile，这里我们推荐使用开源的QGIS软件。

# 4 Shapefile数据处理

> 摘自：geotools官方文档：http://docs.geotools.org/stable/javadocs/

​			geotools GitHub仓库地址：https://github.com/geotools/geotools

## 4.1 Shapefile数据处理依赖及版本说明

### 4.1.1 Shapefile数据处理使用版本以及仓库

```xml-dtd
    <properties>
				<!-- 截止到2020-10最新版本 -->
        <jts.version>1.17.1</jts.version>
        <geotools.version>24.0</geotools.version>
    </properties>

		<repositories>
        <!-- geotools最新版本24.0尚未发布到中央仓库，所以需要添加OSGeo仓库 -->
        <repository>
            <id>osgeo</id>
            <name>OSGeo Repository</name>
            <url>https://repo.osgeo.org/repository/release/</url>
        </repository>
    </repositories>
```

### 4.1.2 Shapefile数据处理使用依赖以及说明

```xml-dtd
		<!-- https://mvnrepository.com/artifact/org.geotools/gt-shapefile Jul, 2020 -->
    <dependency>
    		<groupId>org.geotools</groupId>
      	<artifactId>gt-shapefile</artifactId>
      	<version>${geotools.version}</version>
    </dependency>
```

注意：需要说明的是gt-shapefile只是提供与shapefile文件处理核心依赖，实际使用中大量使用其他geotools的其他依赖，所以在实际操作中需要包含上面处理geojson时用到的依赖。

## 4.2 Shapefile数据处理核心Api

### 4.2.1 读取写入Shapefile的核心类 - ShapefileDataStore

​	gt-shapefile包的ShapefileDataStore类提供读取写入Shapefile文件的能力，构建ShapefileDataStore对象后调用getFeatureSource()方法可以获取实现SimpleFeatureSource接口的ContentFeatureSource对象，进一步调用ContentFeatureSource对象的getFeatures()方法就获取SimpleFeatureCollection对象，接下来就可以获取Shapefile文件中的信息了。核心接口声明如下：

```java
// ShapefileDataStore的构造函数用于构建读取Shapefile文件的ShapefileDataStore对象
public ShapefileDataStore(URL url) {
        this.charset = DEFAULT_STRING_CHARSET;
        this.timeZone = DEFAULT_TIMEZONE;
        this.memoryMapped = false;
        this.bufferCachingEnabled = true;
        this.indexed = true;
        this.indexCreationEnabled = true;
        this.fidIndexed = true;
        this.maxShpSize = 2147483647L;
        this.maxDbfSize = 2147483647L;
        this.shpFiles = new ShpFiles(url);
        if (TRACE_ENABLED) {
            this.trace = new Exception();
            this.trace.fillInStackTrace();
        }

        this.shpManager = new ShapefileSetManager(this.shpFiles, this);
        this.indexManager = new IndexManager(this.shpFiles, this);
}

// 设置使用何种字符集读取Shapefile文件中的字符
public void setCharset(Charset charset);

// ShapefileDataStore的getFeatureSource()方法用于获取ContentFeatureSource对象，该对象实现SimpleFeatureSource接口。
public ContentFeatureSource getFeatureSource() throws IOException;

// SimpleFeatureSource接口提供getFeatures()方法用于获取SimpleFeatureCollection对象，这样就可以进一步获取其他信息。
public SimpleFeatureCollection getFeatures() throws IOException;
```

​		gt-shapefile包的ShapefileDataStore类提供读取写入Shapefile文件的能力，构建ShapefileDataStore对象后通过调用createSchema(SimpleFeatureType featureType)方法传入SimpleFeatureType对象来生成Schema信息，通过调用getFeatureWriter(Transaction transaction)方法来获取FeatureWriter对象，然后调用next方法来获取到SimpleFeature对象，通过给该对象赋值来写入地理坐标信息以及非地理属性信息。核心方法声明如下：

```java
// ShapefileDataStore对象的createSchema(SimpleFeatureType featureType)方法用于生成Schema信息
public void createSchema(SimpleFeatureType featureType);

// ShapefileDataStore对象的getFeatureWriter(Transaction transaction)方法用于获取FeatureWriter对象，然后调用next方法来获取到SimpleFeature对象，通过给该对象赋值来写入地理坐标信息以及非地理属性信息。
public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(Transaction transaction) throws IOException;
```

### 4.2.2 读取Shapefile并构建ShpFiles对象，从而读取地理坐标信息或属性信息 - ShpFiles

​		gt-shapefile包的ShpFiles类用于构建ShpFiles对象，从而可以进一步读取地理坐标信息或者属性信息，需要借助ShapefileReader类和DbaseFileReader类

```java
// 使用指定文件名构建ShpFiles对象
public ShpFiles(String fileName) throws MalformedURLException;

// 使用File构建ShpFiles对象
public ShpFiles(File file) throws MalformedURLException;

// 使用路径URL构建ShpFiles对象
public ShpFiles(URL url) throws IllegalArgumentException;
```

### 4.2.3 用于单独读取Shapefile地理坐标信息的核心类 - ShapefileReader

​		gt-shapefile包的ShapefileReader类用于单独读取Shapefile的空间坐标信息。

```java
// 构建单独读取.shp文件的ShapefileReader对象
public ShapefileReader(ShpFiles shapefileFiles, boolean strict, boolean useMemoryMapped, GeometryFactory gf) throws IOException, ShapefileException;

// 获取.shp地理坐标文件中的一条记录
public ShapefileReader.Record nextRecord() throws IOException;

// ShapefileReader.Record提供的读取空间数据对象Geometry的方法
public Object shape();

// ShapefileReader.Record提供的读取空间数据对象Geometry的方法，该Geometry使用最大最小经纬度构成
public Object getSimplifiedShape();

// ShapefileReader.Record提供了其他空间数据属性，比如最大最小经纬度
public double minX;
public double minY;
public double maxX;
public double maxY;
```

### 4.2.4 用于单独读取Shapefile非地理属性信息的核心类 - DbaseFileReader

​		gt-shapefile包的DbaseFileReader类用于单独读取Shapefile的非地理空间信息。

```java
// 构建单独读取.dbf文件的DbaseFileReader对象
public DbaseFileReader(ShpFiles shapefileFiles, boolean useMemoryMappedBuffer, Charset charset, TimeZone timeZone) throws IOException;

public DbaseFileReader(ShpFiles shapefileFiles, boolean useMemoryMappedBuffer, Charset charset) throws IOException;

public DbaseFileReader(ReadableByteChannel readChannel, boolean useMemoryMappedBuffer, Charset charset) throws IOException;

public DbaseFileReader(ReadableByteChannel readChannel, boolean useMemoryMappedBuffer, Charset charset, TimeZone timeZone) throws IOException;

// DbaseFileReader对象的getHeader()方法用于获取dbf文件属性头
public DbaseFileHeader getHeader();

// DbaseFileReader对象的readRow()方法用于获取dbf文件一行记录
public DbaseFileReader.Row readRow() throws IOException

// DbaseFileReader对象的getFieldName(int inIndex)方法用于指定列的属性名称，列索引从0开始
public String getFieldName(int inIndex);

// DbaseFileReader.Row对象的read(int column)方法用于获取指定行记录的指定列的属性的值。
public Object read(int column) throws IOException;
```

## 4.3 Shapefile数据读取

### 4.3.1 读取Shapefile文件的坐标和属性信息示例

```java
@Test
public void testReadShapeFile() throws IOException, URISyntaxException {
		URL url = ShapeFileReadWriteTest.class.getClassLoader()
                .getResource("shpfile/110000_full/110000_full.shp").toURI().toURL();

		// gt-shapefile包的ShapefileDataStore类提供读取Shapefile文件的能力，
		// 构建ShapefileDataStore对象后调用getFeatureSource()方法可以获取实现SimpleFeatureSource接口的ContentFeatureSource对象，
		// 进一步调用ContentFeatureSource对象的getFeatures()方法就获取SimpleFeatureCollection对象，接下来就可以获取Shapefile文件中的信息了。
		ShapefileDataStore shapefileDataStore = new ShapefileDataStore(url);
		// 设置使用什么编码读取文件中的字符
		shapefileDataStore.setCharset(StandardCharsets.UTF_8);
		SimpleFeatureSource simpleFeatureSource = shapefileDataStore.getFeatureSource();
		SimpleFeatureCollection simpleFeatureCollection = simpleFeatureSource.getFeatures();

		// 读取SimpleFeatureCollection中的地理坐标信息以及其他属性信息
		SimpleFeatureIterator simpleFeatureIterator = simpleFeatureCollection.features();
		while (simpleFeatureIterator.hasNext()) {
				SimpleFeature simpleFeature = simpleFeatureIterator.next();
				/***** 获取Feature唯一标识符 *****/
        String id = simpleFeature.getID();
        // The Id Of Feature Is : feature-0
        System.out.println("The Id Of Feature Is : " + id);

        /***** 获取Feature空间地理数据 - 通过获取SimpleFeature的getDefaultGeometry()方法获取地理坐标 *****/
       System.out.println("/***** 获取Feature空间地理数据 - 通过获取SimpleFeature的getDefaultGeometry()方法获取地理坐标  *****/");
       Object defaultGeometry = simpleFeature.getDefaultGeometry();
       // The class Type Of DefaultGeometry Is : class org.locationtech.jts.geom.MultiPolygon
       System.out.println("The class Type Of DefaultGeometry Is : " + defaultGeometry.getClass());
       Geometry geometry = (Geometry) defaultGeometry;
//            System.out.println("The Well-known Text Of geometry Is : " + geometry.toText());

       /***** 获取Feature非地理属性数据 - 通过获取SimpleFeatureType的getAttributeDescriptors()方法获取属性名称以及类型 *****/
       System.out.println("/***** 获取Feature非地理属性数据 - 通过获取SimpleFeatureType的getAttributeDescriptors()方法获取属性名称以及类型 *****/");
       SimpleFeatureType simpleFeatureType = simpleFeature.getFeatureType();
       List<AttributeDescriptor> attributeDescriptors = simpleFeatureType.getAttributeDescriptors();
       // 获取所有属性的字段名称和类型
       for (AttributeDescriptor attributeDescriptor : attributeDescriptors) {
       		Name name = attributeDescriptor.getName();
          AttributeType attributeType = attributeDescriptor.getType();
          System.out.println(id + "的属性为" + name + "的值的类型是" + attributeType.getBinding() + ".");
       }

       /***** 获取Feature非地理属性数据 - 通过获取SimpleFeature的getProperties()方法获取属性名称以及值 *****/
       System.out.println("/***** 获取Feature非地理属性数据 - 通过获取SimpleFeature的getProperties()方法获取属性名称以及值 *****/");
       Collection<Property> properties = simpleFeature.getProperties();
       		for (Property property : properties) {
          		if (property instanceof GeometryAttribute) {
              		// 略过包含地理信息的属性，地理坐标信息通过simpleFeature.getDefaultGeometry()获取
                  System.out.println("略过包含地理信息的属性，地理坐标信息通过simpleFeature.getDefaultGeometry()来获取。");
               } else if (property instanceof Attribute) {
               		Name propertyKey = property.getName();
                  Object propertyValue = property.getValue();
                  System.out.println(id + "的属性为" + propertyKey + "的值是" + propertyValue + ".");
               }
          }

          System.out.println("/***** 获取Shapefile坐标系信息 *****/");
          CoordinateReferenceSystem coordinateReferenceSystem = simpleFeatureType.getCoordinateReferenceSystem();
          System.out.println(coordinateReferenceSystem.getCoordinateSystem());
          System.out.println("\n\n\n");
		}
		simpleFeatureIterator.close();
}
```

### 4.3.2 只读取Shapefile文件的坐标信息示例

```java
@Test
public void testReadOnlyShapeFile() throws IOException, URISyntaxException {
		URL url = ShapeFileReadWriteTest.class.getClassLoader()
                .getResource("shpfile/110000_full/110000_full.shp").toURI().toURL();
		// 构建ShpFiles对象，可以进一步读取地理坐标以及属性信息，ShpFiles支持文件名、URL、File对象来构建
		ShpFiles shpFiles = new ShpFiles(url);
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
		// gt-shapefile包的ShapefileReader类用于单独读取Shapefile的空间坐标信息
		ShapefileReader shapefileReader = new ShapefileReader(shpFiles, false, false, geometryFactory);
		while (shapefileReader.hasNext()) {
				ShapefileReader.Record record = shapefileReader.nextRecord();
        // 标识第几个图形，从1开始。
        // 读取空间数据第1条记录
        System.out.println("读取空间数据第" + record.number + "条记录");
        // 最小X坐标：116.38059
        System.out.println("最小X坐标：" + record.minX);
        // 最小Y坐标：39.858682
        System.out.println("最小Y坐标：" + record.minY);
        // 最大X坐标：116.450939
        System.out.println("最大X坐标：" + record.maxX);
        // 最大Y坐标：39.973995
        System.out.println("最大Y坐标：" + record.maxY);
        // ShapeType是枚举类，包含数字id和字符串形式的类型
        // 空间数据类型是：Polygon
        System.out.println("空间数据类型是：" + record.type.name);
        // 最大最小经纬度组合出四个点确定的长方形区域
        Object simplifiedShape = record.getSimplifiedShape();
        // 最大最小经纬度组合出四个点确定的长方形区域WKT字符串为：MULTIPOLYGON (((116.38059 39.858682, 116.38059 39.973995,
        // 116.450939 39.973995, 116.450939 39.858682, 116.38059 39.858682)))
        System.out.println("最大最小经纬度组合出四个点确定的长方形区域WKT字符串为：" + simplifiedShape);
        Geometry geometry = (Geometry) record.shape();
        // The class Type Of DefaultGeometry Is : class org.locationtech.jts.geom.MultiPolygon
        System.out.println("The class Type Of DefaultGeometry Is : " + geometry.getClass());
        // The Well-known Text Of geometry Is : MULTIPOLYGON (((116.387658 39.96093, 116.389498 39.96314,...)))
        System.out.println("The Well-known Text Of geometry Is : " + geometry.toText());
    }
    shapefileReader.close();
}
```

### 4.3.3 只读取Shapefile文件的非地理属性信息示例

```java
@Test
public void testReadOnlyDbfFile() throws IOException, URISyntaxException {
		// 构建ShpFiles对象，可以进一步读取地理坐标以及属性信息，ShpFiles支持文件名、URL、File对象来构建
		// 这里即可以使用.shp也可以使用.dbf来构建ShpFiles对象。
		URL url = ShapeFileReadWriteTest.class.getClassLoader()
                .getResource("shpfile/110000_full/110000_full.dbf").toURI().toURL();
		ShpFiles shpFiles = new ShpFiles(url);
		// 指定使用何种字符集来读取属性信息
		Charset charset = StandardCharsets.UTF_8;
		DbaseFileReader dbaseFileReader = new DbaseFileReader(shpFiles, false, charset);
		while (dbaseFileReader.hasNext()) {
        // DbaseFileReader对象的getHeader()方法用于获取dbf文件属性头
        DbaseFileHeader dbaseFileHeader = dbaseFileReader.getHeader();
        // DbaseFileReader对象的readRow()方法用于获取dbf文件一行记录
        DbaseFileReader.Row row = dbaseFileReader.readRow();
        int numFields = dbaseFileHeader.getNumFields();
				for (int i = 0; i < numFields; i++) {
           // DbaseFileReader对象的getFieldName(int inIndex)方法用于指定列的属性名称，列索引从0开始
            String fieldName = dbaseFileHeader.getFieldName(i);
            // DbaseFileReader.Row对象的read(int column)方法用于获取指定行记录的指定列的属性的值。
            Object fieldValue = row.read(i);
						System.out.println("属性key为: " + fieldName + " ,值为: " + fieldValue);
				}
		}
}
```

## 4.4 Shapefile数据写入

### 4.4.1 写入Shapefile文件的坐标以及属性信息示例

```java
@Test
public void testWriteShapeFile() throws IOException, ParseException {
		// 文件必须提前创建，可创建一个空的shp文件
    File shpFile = new File("target/beijing.shp");
    if (shpFile.exists()) {
        shpFile.delete();
    } else {
        shpFile.createNewFile();
    }
    URL fileUrl = shpFile.toURI().toURL();
    // gt-shapefile包的ShapefileDataStore类提供读取写入Shapefile文件的能力
    ShapefileDataStore shapefileDataStore = new ShapefileDataStore(fileUrl);
    shapefileDataStore.setCharset(StandardCharsets.UTF_8);

    // 使用SimpleFeatureTypeBuilder对象的buildFeatureType()方法创建SimpleFeatureType对象
    SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
    simpleFeatureTypeBuilder.setName("name");
    simpleFeatureTypeBuilder.setCRS(DefaultGeographicCRS.WGS84);
    simpleFeatureTypeBuilder.add("geom1", MultiPolygon.class);
    simpleFeatureTypeBuilder.add("adcode", Long.class);
    simpleFeatureTypeBuilder.add("name", String.class);
    // 注意对于数组来说，声明类型的时候一定要用数组的Class，如果使用List.class则GeoJSON片段会出现："center":"[116.405285, 39.904989]"
    simpleFeatureTypeBuilder.add("center", Double[].class);
    simpleFeatureTypeBuilder.add("centroid", Double[].class);
    simpleFeatureTypeBuilder.add("level", String.class);
    simpleFeatureTypeBuilder.add("acroutes", Integer[].class);
    SimpleFeatureType simpleFeatureType = simpleFeatureTypeBuilder.buildFeatureType();
    // 构建ShapefileDataStore对象后通过调用createSchema(SimpleFeatureType featureType)方法传入SimpleFeatureType对象来生成Schema信息
    shapefileDataStore.createSchema(simpleFeatureType);

    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    WKTReader wktReader = new WKTReader(geometryFactory);

    // 通过调用getFeatureWriter(Transaction transaction)方法来获取FeatureWriter对象，然后调用next方法来获取到SimpleFeature对象，
    // 通过给该对象赋值来写入地理坐标信息以及非地理属性信息。
    FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = shapefileDataStore.getFeatureWriter(Transaction.AUTO_COMMIT);
    for (int i = 0; i < 2; i++) {
        SimpleFeature simpleFeature = featureWriter.next();
        String wkt = "MULTIPOLYGON (((116.725518 39.624075, 116.721858 39.621756, ...)))";
        MultiPolygon multiPolygon = (MultiPolygon) wktReader.read(wkt);
        simpleFeature.setDefaultGeometry(multiPolygon);
        // 属性长度限制10个字符
//        simpleFeature.setAttribute("geom1", multiPolygon);
        simpleFeature.setAttribute("adcode", 110000);
        simpleFeature.setAttribute("name", "北京市");
        simpleFeature.setAttribute("center", Lists.newArrayList(116.405285, 39.904989));
        simpleFeature.setAttribute("centroid", Lists.newArrayList(116.419889, 40.189911));
        simpleFeature.setAttribute("level", "province");
        simpleFeature.setAttribute("acroutes", new Integer[]{100000});
        featureWriter.write();
    }
    featureWriter.close();
}
```





​	

附录：