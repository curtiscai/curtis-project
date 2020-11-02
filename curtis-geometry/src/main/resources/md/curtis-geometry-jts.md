部分参考：https://github.com/locationtech/jts

# 1 空间数据（Spatial Data）预备知识

（略）

# 2  JTS简介

## 2.1 JTS概述

​	JTS拓扑套件是一个用于创建和处理矢量几何的Java库。 它还提供了一套用于处理和可视化几何和JTS功能的TestBuilder GUI应用程序。

​	JTS是Eclipse Foundation的LocationTech工作组的一个项目。

## 2.2 JTS 相关站点

​	JTS官方网站：https://github.com/locationtech/jts

​	JTS Java Doc：https://locationtech.github.io/jts/javadoc/

# 3 JTS核心源码

## 3.1 GeometryFactory - 创建几何Geometry Object核心

GeometryFactory核心构造方法

```java
/**
   * Constructs a GeometryFactory that generates Geometries having the given
   * PrecisionModel, spatial-reference ID, and CoordinateSequence implementation.
   */
  public GeometryFactory(PrecisionModel precisionModel, int SRID,
                         CoordinateSequenceFactory coordinateSequenceFactory) {
      this.precisionModel = precisionModel;
      this.coordinateSequenceFactory = coordinateSequenceFactory;
      this.SRID = SRID;
  }

  /**
   * Constructs a GeometryFactory that generates Geometries having the given
   * CoordinateSequence implementation, a double-precision floating PrecisionModel and a
   * spatial-reference ID of 0.
   */
  public GeometryFactory(CoordinateSequenceFactory coordinateSequenceFactory) {
    this(new PrecisionModel(), 0, coordinateSequenceFactory);
  }

  /**
   * Constructs a GeometryFactory that generates Geometries having the given
   * {@link PrecisionModel} and the default CoordinateSequence
   * implementation.
   *
   * @param precisionModel the PrecisionModel to use
   */
  public GeometryFactory(PrecisionModel precisionModel) {
    this(precisionModel, 0, getDefaultCoordinateSequenceFactory());
  }

  /**
   * Constructs a GeometryFactory that generates Geometries having the given
   * {@link PrecisionModel} and spatial-reference ID, and the default CoordinateSequence
   * implementation.
   *
   * @param precisionModel the PrecisionModel to use
   * @param SRID the SRID to use
   */
  public GeometryFactory(PrecisionModel precisionModel, int SRID) {
    this(precisionModel, SRID, getDefaultCoordinateSequenceFactory());
  }

  /**
   * Constructs a GeometryFactory that generates Geometries having a floating
   * PrecisionModel and a spatial-reference ID of 0.
   */
  public GeometryFactory() {
    this(new PrecisionModel(), 0);
  }
```

其中PrecisionModel用于指定Geometry中所有坐标点的精度。SRID用于指定空间参照标识符，SRID 0表示笛卡尔空间坐标系，SRID 4326表示...。默认的GeometryFactory构造函数使用双精度模型，SRID为0的笛卡尔空间坐标系。在处理地理坐标时使用public GeometryFactory(PrecisionModel precisionModel, int SRID)构造函数即可。

下面三种写法是等价的，关于精度模型对象PrecisionModel参见下一部分内容

```java
private GeometryFactory geometryFactory = new GeometryFactory();    
private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 0);
private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),0);
```

## 3.2 PrecisionModel-坐标精度模型对象

PrecisionModel用于指定坐标的精度，PrecisionModel部分源码如下：

```java
public class PrecisionModel implements Serializable, Comparable
{
  /**
   * Fixed Precision indicates that coordinates have a fixed number of decimal places.
   * The number of decimal places is determined by the log10 of the scale factor.
   */
  public static final Type FIXED = new Type("FIXED");
  /**
   * Floating precision corresponds to the standard Java
   * double-precision floating-point representation, which is
   * based on the IEEE-754 standard
   */
  public static final Type FLOATING = new Type("FLOATING");
  /**
   * Floating single precision corresponds to the standard Java
   * single-precision floating-point representation, which is
   * based on the IEEE-754 standard
   */
  public static final Type FLOATING_SINGLE = new Type("FLOATING SINGLE");
	
	  /**
   * The type of PrecisionModel this represents.
   */
  private Type modelType;
  /**
   * The scale factor which determines the number of decimal places in fixed precision.
   */
  private double scale;

  /**
   * Creates a <code>PrecisionModel</code> with a default precision
   * of FLOATING.
   */
  public PrecisionModel() {
    // default is floating precision
    modelType = FLOATING;
  }

  /**
   * Creates a <code>PrecisionModel</code> that specifies
   * an explicit precision model type.
   * If the model type is FIXED the scale factor will default to 1.
   *
   * @param modelType the type of the precision model
   */
  public PrecisionModel(Type modelType)
  {
    this.modelType = modelType;
    if (modelType == FIXED)
    {
      setScale(1.0);
    }
  }
  ... 
}
```

我们看到PrecisionModel提供了三种精度类型，分别是固定精度：FIXED；双精度：FLOATING；单精度：FLOATING_SINGLE。PrecisionModel默认构造函数就是双精度，足够应对地理坐标的相关计算。

## 3.3 GeometryFactory-提供构建各种Geometry对象的方法

![image-20200919111928777](/Users/caixiangning/CurtisProjects/markdown/curtis-project/img/curtis-geometry/GeometryFactory-method.png)

## 3.4 WKTReader-提供转换WKT格式字符串为Geometry对象

​	WKTReader用于将WKT格式字符串转换为Geometry对象。WKTReader提供两个构造函数，一个是无参构造函数，使用默认的GeometryFactory，也就是使用双精度，SRID为0，另外一个接收GeometryFactory对象，用于指定精度和CRID。

​	WKTReader对象的read方法用于将WKT格式字符串转换为Geometry对象。未发现read方法使用任何WKTReader对象的属性，所以应该是线程安全的，看到网上一些资料说是非线程安全的，这点存疑。

```java
public Geometry read(String wellKnownText) throws ParseException {
    StringReader reader = new StringReader(wellKnownText);
    try {
      return read(reader);
    }
    finally {
      reader.close();
    }
}
```

​	测试WKTReader对象解析WKT字符串的read方法的线程安全问题，结论：线程安全

```java
@Test
public void testWKTReaderThreadSafe() {
    LocalTime startTime = LocalTime.now();
    WKTReader wktReader = new WKTReader();
    List<CompletableFuture> completableFutureList = Lists.newArrayList();
    for (int i = 0; i < 1_00000; i++) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                Geometry geometryCollectionGeometry1 = wktReader.read(
                            "GEOMETRYCOLLECTION (POINT (1 1), " +
                                    "LINESTRING (0 0, 1 0), " +
                                    "POLYGON ((0 0, 1 1, 2 1, 0 0)), " +
                                    "MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0))," + 
              											"((1 1, 2 1, 2 2, 1 2, 1 1))))");
                System.out.println(geometryCollectionGeometry1.toText());
             } catch (ParseException e) {
                 e.printStackTrace();
             }
        });
        completableFutureList.add(future);
        // 如果在这里使用join，就不是并发了而是顺序执行
        // future.join();
    }
    for(CompletableFuture future : completableFutureList){
        future.join();
    }
    LocalTime endTime = LocalTime.now();
    // 执行耗时4秒
    System.out.println("执行耗时：" + Duration.between(startTime, endTime).getSeconds() + "秒");
}
```

# 4 JTS使用

## 4.1 使用Maven引入JTS依赖

使用最新版本依赖：（截止到2020年8月jts版本是：1.17.1）

```xml
<properties>
    <jts.version>1.17.1</jts.version>
</properties>
<dependency>
    <groupId>org.locationtech.jts</groupId>
    <artifactId>jts-core</artifactId>
    <version>${jts.version}</version>
</dependency>
```

使用旧版本依赖：（从Maven仓库看最新版本是2012年12月）

```xml
<dependency>
    <groupId>com.vividsolutions</groupId>
    <artifactId>jts-core</artifactId>
    <version>1.13.0</version>
</dependency>
```

## 4.2 创建空间数据对象-使用坐标

​	这里我们以使用笛卡尔坐标系（SRID 0）为例，WGS84坐标系（SRID 4326）同理，只需要在创建GeometryFactory时指定SRID为4326即可。

### 4.2.1 创建点Point

​	GeometryFactory对象提供使用坐标对象Coordinate来创建点Point对象的方式。

```java
// public Point createPoint(Coordinate coordinate)
// public Point createPoint(CoordinateSequence coordinates)
Coordinate coordinate = new Coordinate(1, 1);
Point point = geometryFactory.createPoint(coordinate);
// point -> POINT (1 1)
System.out.println("point -> " + point);
```

### 4.2.1 创建多点MultiPoint

​	GeometryFactory对象提供两种创建多点的方式，分别是使用坐标数组Coordinate[]和点数组Point[]。

```java
// public MultiPoint createMultiPoint(Coordinate[] coordinates)
// public MultiPoint createMultiPoint(Point[] point)
// public MultiPoint createMultiPoint(CoordinateSequence coordinates)
Coordinate[] coordinates = new Coordinate[]{new Coordinate(0, 0), new Coordinate(1, 1)};
MultiPoint multiPoint1 = geometryFactory.createMultiPointFromCoords(coordinates);
// multiPoint1 -> MULTIPOINT ((0 0), (1 1))
System.out.println("multiPoint1 -> " + multiPoint1);

Point point1 = geometryFactory.createPoint(new Coordinate(1, 0));
Point point2 = geometryFactory.createPoint(new Coordinate(1, 1));
Point[] points = new Point[]{point1, point2};
MultiPoint multiPoint2 = geometryFactory.createMultiPoint(points);
// multiPoint2 -> MULTIPOINT ((1 0), (1 1))
System.out.println("multiPoint2 -> " + multiPoint2);
```

### 4.2.3 创建线LineString

​	GeometryFactory对象提供使用坐标数组Coordinate[]来创建线的方式。

```java
// public LineString createLineString(Coordinate[] coordinates)
// public LineString createLineString(CoordinateSequence coordinates)
Coordinate[] coordinatesLineArray1 = new Coordinate[]{new Coordinate(0, 0), new Coordinate(1, 0)};
LineString lineString1 = geometryFactory.createLineString(coordinatesLineArray1);
// lineString1 -> LINESTRING (0 0, 1 0)
System.out.println("lineString1 -> " + lineString1);

Coordinate[] coordinatesLineArray2 = new Coordinate[]{new Coordinate(1, 0), new Coordinate(1, 1)};
LineString lineString2 = geometryFactory.createLineString(coordinatesLineArray2);
// lineString2 -> LINESTRING (1 0, 1 1)
System.out.println("lineString2 -> " + lineString2);
```

### 4.2.4 创建多线MultiLineString

​	GeometryFactory对象提供使用线数组LineString[]来创建多线对象的方式。

```java
// public MultiLineString createMultiLineString(LineString[] lineStrings)
LineString[] lineStringArray = new LineString[]{lineString1, lineString2};
MultiLineString multiLineString = geometryFactory.createMultiLineString(lineStringArray);
// multiLineString -> MULTILINESTRING ((0 0, 1 0), (1 0, 1 1))
System.out.println("multiLineString -> " + multiLineString);
```

### 4.2.5 创建环线LinearRing

​	GeometryFactory对象提供使用坐标数组Coordinate[]来创建环线LinearRing对象的方式。需要注意的是环线要求首尾点必须相同，并且至少四个点

```java
// public LinearRing createLinearRing(Coordinate[] coordinates)
// public LinearRing createLinearRing(CoordinateSequence coordinates)
// GeometryFactory对象提供使用坐标数组Coordinate[]来创建环线LinearRing对象的方式。
Coordinate coordinate1 = new Coordinate(0, 0);
Coordinate coordinate2 = new Coordinate(2, 0);
Coordinate coordinate3 = new Coordinate(1, 2);
Coordinate coordinate4 = new Coordinate(0, 0);
Coordinate[] coordinateArray = new Coordinate[]{coordinate1, coordinate2, coordinate3, coordinate4};
// 闭环的线要求首尾是同一个点，否则抛出异常，源码中做了判断
// java.lang.IllegalArgumentException: Points of LinearRing do not form a closed linestring
//  Coordinate[] coordinateArray = new Coordinate[]{coordinate1,coordinate2,coordinate3};

// 环线要求至少四个点，否则抛出异常
// java.lang.IllegalArgumentException: Invalid number of points in LinearRing (found 3 - must be 0 or >= 4)
// Coordinate[] coordinateArray = new Coordinate[]{coordinate1,coordinate2,coordinate4};
LinearRing linearRing = geometryFactory.createLinearRing(coordinateArray);
// linearRing -> LINEARRING (0 0, 2 0, 1 2, 0 0)
System.out.println("linearRing -> " + linearRing);
```

### 4.2.6 创建面Polygon

​	GeometryFactory对象提供多种创建面Polygon的方式。使用坐标数组Coordinate[]；使用环线LinearRing；使用两个坐标数组，外壳数组和内洞数组。

```java
// public Polygon createPolygon(Coordinate[] coordinates)
// public Polygon createPolygon(CoordinateSequence coordinates)
// public Polygon createPolygon(LinearRing shell, LinearRing[] holes)
// public Polygon createPolygon(Coordinate[] shell)
// public Polygon createPolygon(LinearRing shell)
// GeometryFactory对象提供多种创建面Polygon的方式。使用坐标数组Coordinate[]；使用环线LinearRing；使用两个坐标数组，外壳数组和内洞数组。
// 方式1：使用坐标数组Coordinate[]
Coordinate coordinate21 = new Coordinate(0, 0);
Coordinate coordinate22 = new Coordinate(1, 1);
Coordinate coordinate23 = new Coordinate(2, 1);
Coordinate coordinate24 = new Coordinate(0, 0);
Coordinate[] coordinateArray2 = new Coordinate[]{coordinate21, coordinate22, coordinate23, coordinate24};
//  闭环的线要求首尾是同一个点，否则抛出异常
//  java.lang.IllegalArgumentException: Points of LinearRing do not form a closed linestring
//  Coordinate[] coordinateArray2 = new Coordinate[]{coordinate21, coordinate22, coordinate23};

//  环线要求至少四个点，否则抛出异常
//  java.lang.IllegalArgumentException: Invalid number of points in LinearRing (found 3 - must be 0 or >= 4)
//  Coordinate[] coordinateArray2 = new Coordinate[]{coordinate21, coordinate22, coordinate21};
Polygon polygon1 = geometryFactory.createPolygon(coordinateArray2);
// polygon1 -> POLYGON ((0 0, 1 1, 2 1, 0 0))
System.out.println("polygon1 -> " + polygon1);

// 方式2：使用环线LinearRing
LinearRing linearRing2 = geometryFactory.createLinearRing(coordinateArray2);
Polygon polygon2 = geometryFactory.createPolygon(linearRing2);
// polygon2 -> POLYGON ((0 0, 1 1, 2 1, 0 0))
System.out.println("polygon2 -> " + polygon2);

// 方式3：使用两个坐标数组，外壳数组和内洞数组。
Coordinate coordinate311 = new Coordinate(0, 0);
Coordinate coordinate312 = new Coordinate(3, 0);
Coordinate coordinate313 = new Coordinate(3, 3);
Coordinate coordinate314 = new Coordinate(0, 3);
Coordinate coordinate315 = new Coordinate(0, 0);
Coordinate[] coordinateArray31 = new Coordinate[]{coordinate311, coordinate312, coordinate313, coordinate314, coordinate315};
LinearRing linearRingShell = geometryFactory.createLinearRing(coordinateArray31);
Coordinate coordinate321 = new Coordinate(1, 1);
Coordinate coordinate322 = new Coordinate(2, 1);
Coordinate coordinate323 = new Coordinate(2, 2);
Coordinate coordinate324 = new Coordinate(1, 2);
Coordinate coordinate325 = new Coordinate(1, 1);
Coordinate[] coordinateArray32 = new Coordinate[]{coordinate321, coordinate322, coordinate323, coordinate324, coordinate325};
LinearRing linearRingHole = geometryFactory.createLinearRing(coordinateArray32);
Polygon polygon3 = geometryFactory.createPolygon(linearRingShell, new LinearRing[]{linearRingHole});
// polygon3 -> POLYGON ((0 0, 3 0, 3 3, 0 3, 0 0), (1 1, 2 1, 2 2, 1 2, 1 1))
System.out.println("polygon3 -> " + polygon3);
```

### 4.2.7 创建MultiPolygon

​	GeometryFactory对象提供使用面数组Polygon[]来创建多面对象的方式。

```java
// GeometryFactory对象提供使用面数组Polygon[]来创建多面对象的方式。
// public MultiPolygon createMultiPolygon(Polygon[] polygons)
Polygon polygon41 = geometryFactory.createPolygon(linearRingShell);
Polygon polygon42 = geometryFactory.createPolygon(linearRingHole);
Polygon[] polygonArray = new Polygon[]{polygon41, polygon42};
MultiPolygon multiPolygon = geometryFactory.createMultiPolygon(polygonArray);
// multiPolygon -> MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)), ((1 1, 2 1, 2 2, 1 2, 1 1)))
System.out.println("multiPolygon -> " + multiPolygon);
```

### 4.2.8 创建GeometryCollection

​	GeometryFactory对象提供使用Geometry数组Geometry[]来创建Geometry集合的方式。

```java
// GeometryFactory对象提供使用Geometry数组Geometry[]来创建Geometry集合的方式。
// public GeometryCollection createGeometryCollection(Geometry[] geometries)
GeometryCollection geometryCollection = geometryFactory.createGeometryCollection(new Geometry[]{point, lineString1, polygon1, multiPolygon});
// geometryCollection -> GEOMETRYCOLLECTION (POINT (1 1), LINESTRING (0 0, 1 0), POLYGON ((0 0, 1 1, 2 1, 0 0)), MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)), ((1 1, 2 1, 2 2, 1 2, 1 1))))
System.out.println("geometryCollection -> " + geometryCollection);
```

## 4.3 创建空间数据对象-使用WKT

​	这里我们以使用笛卡尔坐标系（SRID 0）为例，WGS84坐标系（SRID 4326）同理，只需要在创建GeometryFactory时指定SRID为4326即可。

​	**WKT字符串中标识空间数据类型部分的字符串大小写均可，FE: POINT (0 0)、Point (0 0)、point (0 0)。经纬度之间必须有空格，但是标识符和括号之间有无空格无所谓,括号与括号之间、坐标和坐标之间有无空格也无所谓。**

​	需要说明的是根据getGeometryType()方法或者instanceof方法判断Geometry类型仅仅是演示用法，实际使用中使用其中一种方法即可。

### 4.3.1 创建点Point

​	 点Point的WKT格式字符串对应一维数组，数组只有两个元素（经纬度）。

```java
WKTReader wktReader = new WKTReader(geometryFactory);
Geometry pointGeometry = wktReader.read("POINT (1 1)");
// 以下两步是多余的，仅为了演示构造Geometry后的类型
if (Geometry.TYPENAME_POINT.equalsIgnoreCase(pointGeometry.getGeometryType())) {
    if (pointGeometry instanceof Point) {
        Point point = (Point) pointGeometry;
        // point -> POINT (1 1)
        System.out.println("point -> " + point.toText());
    }
}
```

### 4.3.2 创建多点MultiPoint

​	多点MultiPoint的WKT格式字符串对应二维数组，二维数组的元素个数对应点的个数，二维数组的元素是只有两个元素（经纬度）的一维数组。

```java
Geometry multiPointGeometry = wktReader.read("MULTIPOINT ((0 0), (1 0))");
if (Geometry.TYPENAME_MULTIPOINT.equalsIgnoreCase(multiPointGeometry.getGeometryType())) {
    if (multiPointGeometry instanceof MultiPoint) {
        MultiPoint multiPoint = (MultiPoint) multiPointGeometry;
        // multiPoint -> MULTIPOINT ((0 0), (1 0))
        System.out.println("multiPoint -> " + multiPoint.toText());
    }
}
```

### 4.3.3 创建线LineString

​	线LineString的WKT格式字符串对应二维数组，二维数组的元素个数对应线上点的个数，二维数组的元素是只有两个元素（经纬度）的一维数组。

```java
Geometry lineStringGeometry = wktReader.read("LINESTRING (0 0, 1 0)");
if (Geometry.TYPENAME_LINESTRING.equalsIgnoreCase(lineStringGeometry.getGeometryType())) {
    if (lineStringGeometry instanceof LineString) {
        LineString lineString = (LineString) lineStringGeometry;
        // lineString -> LINESTRING (0 0, 1 0)
        System.out.println("lineString -> " + lineString.toText());
    }
}
```

### 4.3.4 创建多线MultiLineString

​	多线MultiLineString的WKT格式字符串对应三维数组，三维数组的每个二维数组都是线，二维数组的元素对应线上的每个点。如果三维数组只有一个元素则是单条线组成的多线，如果有多个元素就是多条线组成的多线。

```java
// 使用多条线来创建多线
Geometry multiLineStringGeometry1 = wktReader.read("MULTILINESTRING ((0 0, 1 0), (1 0, 1 1))");
if (Geometry.TYPENAME_MULTILINESTRING.equalsIgnoreCase(multiLineStringGeometry1.getGeometryType())) {
    if (multiLineStringGeometry1 instanceof MultiLineString) {
        MultiLineString multiLineString1 = (MultiLineString) multiLineStringGeometry1;
        // multiLineString1 -> MULTILINESTRING ((0 0, 1 0), (1 0, 1 1))
        System.out.println("multiLineString1 -> " + multiLineString1.toText());
    }
}
// 使用单条线来创建多线
Geometry multiLineStringGeometry2 = wktReader.read("MULTILINESTRING ((0 0, 1 0))");
if (Geometry.TYPENAME_MULTILINESTRING.equalsIgnoreCase(multiLineStringGeometry2.getGeometryType())) {
    if (multiLineStringGeometry2 instanceof MultiLineString) {
        MultiLineString multiLineString2 = (MultiLineString) multiLineStringGeometry2;
        // multiLineString2 -> MULTILINESTRING ((0 0, 1 0))
        System.out.println("multiLineString2 -> " + multiLineString2.toText());
    }
}
```

### 4.3.5 创建环线LinearRing

​	环线LinearRing的WKT格式字符串对应二维数组，二维数组的元素个数对应线上点的个数，二维数组的元素是只有两个元素（经纬度）的一维数组。环线LinearRing要求首尾点相同，并且至少四个点，所以二维数组至少有四个元素并且第一个元素和最后一个元素相同。

```java
Geometry linearRingGeometry = wktReader.read("LINEARRING (0 0, 2 0, 1 2, 0 0)");
if (Geometry.TYPENAME_LINEARRING.equalsIgnoreCase(linearRingGeometry.getGeometryType())) {
    if (linearRingGeometry instanceof LinearRing) {
        LinearRing linearRing = (LinearRing) linearRingGeometry;
        // linearRing -> LINEARRING (0 0, 2 0, 1 2, 0 0)
        System.out.println("linearRing -> " + linearRing.toText());
    }
}
```

### 4.3.6 创建面Polygon

​	面Polygon的WKT格式字符串对应三维数组，三维数组的第一个元素是外壳，三维数组的其他元素都是内洞，无论是外壳还是内洞都是二维数组的环线。

```java
// 单个环线（外壳）组成的面
Geometry polygonGeometry1 = wktReader.read("POLYGON ((0 0, 1 1, 2 1, 0 0))");
if (Geometry.TYPENAME_POLYGON.equalsIgnoreCase(polygonGeometry1.getGeometryType())) {
    if (polygonGeometry1 instanceof Polygon) {
        Polygon polygon1 = (Polygon) polygonGeometry1;
        // polygon1 -> POLYGON ((0 0, 1 1, 2 1, 0 0))
         System.out.println("polygon1 -> " + polygon1.toText());
    }
}
// 多个环线（一个外坑和多个内洞环线）组成的面
Geometry polygonGeometry2 = wktReader.read("POLYGON ((0 0, 3 0, 3 3, 0 3, 0 0), (1 1, 2 1, 2 2, 1 2, 1 1))");
if (Geometry.TYPENAME_POLYGON.equalsIgnoreCase(polygonGeometry2.getGeometryType())) {
    if (polygonGeometry2 instanceof Polygon) {
        Polygon polygon2 = (Polygon) polygonGeometry2;
        // polygon2 -> POLYGON ((0 0, 3 0, 3 3, 0 3, 0 0), (1 1, 2 1, 2 2, 1 2, 1 1))
        System.out.println("polygon2 -> " + polygon2.toText());
    }
}
```

### 4.3.7 创建多面MultiPolygon

​	多面MultiPolygon的WKT格式字符串对应四维维数组，四维数组的每个元素都是一个面。

```java
// 多个面组成的多面
Geometry multiPolygonGeometry1 = wktReader.read("MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)), ((1 1, 2 1, 2 2, 1 2, 1 1)))");
if(Geometry.TYPENAME_MULTIPOLYGON.equalsIgnoreCase(multiPolygonGeometry1.getGeometryType())) {
    if (multiPolygonGeometry1 instanceof MultiPolygon) {
        MultiPolygon multiPolygon1 = (MultiPolygon) multiPolygonGeometry1;
        // multiPolygon1 -> MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)), ((1 1, 2 1, 2 2, 1 2, 1 1)))
        System.out.println("multiPolygon1 -> " + multiPolygon1.toText());
    }
}
// 单个面组成的多面
Geometry multiPolygonGeometry2 = wktReader.read("MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)))");
if(Geometry.TYPENAME_MULTIPOLYGON.equalsIgnoreCase(multiPolygonGeometry2.getGeometryType())) {
    if (multiPolygonGeometry2 instanceof MultiPolygon) {
        MultiPolygon multiPolygon2 = (MultiPolygon) multiPolygonGeometry2;
        // multiPolygon2 -> MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)))
        System.out.println("multiPolygon2 -> " + multiPolygon2.toText());
    }
}
```

### 4.3.8 创建Geometry集合GeometryCollection

​	面集合GeometryCollection的WKT格式字符串对应五维数组，五维数组的每个元素都是一个Geometry，元素可能是一维到四维数组。

```java
// 多种维度Geometry组成的Geometry集合
Geometry geometryCollectionGeometry1 = wktReader.read(
                "GEOMETRYCOLLECTION (POINT (1 1), " +
                "LINESTRING (0 0, 1 0), " +
                "POLYGON ((0 0, 1 1, 2 1, 0 0)), " +
                "MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)), ((1 1, 2 1, 2 2, 1 2, 1 1))))");
if(Geometry.TYPENAME_GEOMETRYCOLLECTION.equalsIgnoreCase(geometryCollectionGeometry1.getGeometryType())) {
    if (geometryCollectionGeometry1 instanceof GeometryCollection) {
        GeometryCollection geometryCollection1 = (GeometryCollection) geometryCollectionGeometry1;
        // geometryCollection1 -> GEOMETRYCOLLECTION (POINT (1 1), LINESTRING (0 0, 1 0),
        // POLYGON ((0 0, 1 1, 2 1, 0 0)), MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)), ((1 1, 2 1, 2 2, 1 2, 1 1))))
        System.out.println("geometryCollection1 -> " + geometryCollection1.toText());
    }
}
// 单个点组成的Geometry集合（最简单的Geometry集合）
Geometry geometryCollectionGeometry2 = wktReader.read("GEOMETRYCOLLECTION (POINT (1 1))");
if(Geometry.TYPENAME_GEOMETRYCOLLECTION.equalsIgnoreCase(geometryCollectionGeometry2.getGeometryType())) {
    if (geometryCollectionGeometry2 instanceof GeometryCollection) {
        GeometryCollection geometryCollection2 = (GeometryCollection) geometryCollectionGeometry2;
        // geometryCollection2 -> GEOMETRYCOLLECTION (POINT (1 1))
        System.out.println("geometryCollection2 -> " + geometryCollection2.toText());
    }
}
```

## 4.4 计算空间数据对象关系

### 4.4.1 计算包含关系的contains和within

​	contains方法和within方法都是计算包含关系，如果A.contains(B)=true成立则B.within(A)=true一定成立，反之亦然。
​	contains方法表示包含关系，A.contains(B)=true需要满足两个条件：B的点都在A的内部或者边界上；B至少有一个点在A的内部。
​	within方法也表示包含关系，不过A.within(B)=true表示的是B包含A或者A在B的内部。

```java
@Test
public void testContainsAndWithin() throws ParseException {
		// 下面三种写法相同，这与PrecisionModel默认精度是双精度有关，具体可看源码
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 0);
    //    GeometryFactory geometryFactory = new GeometryFactory(new 			PrecisionModel(PrecisionModel.FLOATING),0);
		//    GeometryFactory geometryFactory = new GeometryFactory();

		WKTReader wktReader = new WKTReader(geometryFactory);
		/***** A完全包含B-B的点集都在A的内部，不在边界上 *****/
		System.out.println("/***** A完全包含B-B的点集都在A的内部，不在边界上 *****/");
		Geometry geometry11 = wktReader.read("POLYGON((0 0, 0 3, 3 3, 3 0, 0 0))");
		Geometry geometry12 = wktReader.read("POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))");

		boolean isContains1 = geometry11.contains(geometry12);
		// geometry11.contains(geometry12) -> true
		System.out.println("geometry11.contains(geometry12) -> " + isContains1);
		boolean isWithin1 = geometry12.within(geometry11);
		// geometry12.within(geometry11) -> true
		System.out.println("geometry12.within(geometry11) -> " + isWithin1);

		/***** A完全包含B-B的点集不都在A的内部，有的在内部有的在边界上 *****/
		System.out.println("/***** A完全包含B-B的点集不都在A的内部，有的在内部有的在边界上 *****/");
		Geometry geometry21 = wktReader.read("POLYGON((0 0, 0 3, 3 3, 3 0, 0 0))");
		Geometry geometry22 = wktReader.read("POLYGON((0 0, 0 2, 2 2, 2 0, 0 0))");

		boolean isContains2 = geometry21.contains(geometry22);
		// geometry21.contains(geometry22) -> true
		System.out.println("geometry21.contains(geometry22) -> " + isContains2);
		boolean isWithin2 = geometry22.within(geometry21);
		// geometry22.within(geometry21) -> true
		System.out.println("geometry22.within(geometry21) -> " + isWithin2);

		/***** A完全包含B-B的点集在A的边界上，不在内部 *****/
		System.out.println("/***** A完全包含B-B的点集在A的边界上，不在内部 *****/");
		Geometry geometry31 = wktReader.read("POLYGON((0 0, 0 3, 3 3, 3 0, 0 0))");
		Geometry geometry32 = wktReader.read("LINESTRING(0 0, 0 3)");

		boolean isContains3 = geometry31.contains(geometry32);
		// geometry31.contains(geometry32) -> false
		System.out.println("geometry31.contains(geometry32) -> " + isContains3);
    boolean isWithin3 = geometry32.within(geometry31);
		// geometry32.within(geometry31) -> false
    System.out.println("geometry32.within(geometry31) -> " + isWithin3);
}
```

### 4.4.2 计算相交关系的intersects和disjoint

​	intersects方法用于计算相交关系，表示相交，disjoint正好相反表示不相交，如果A.intersects(B)=true成立则A.disjoint(B)=false，反之亦然。
​	 intersects方法用于表示两个Geometry相交，A.intersects(B)=true只需要满足一个条件：A和B至少有一个公共的点。
​	disjoint方法用于表示两个Geometry不想交，A.disjoint(B)=true只需要满足一个条件：A和B没有任何公共的点。	

```java
@Test
public void testIntersectsAndDisjoint() throws ParseException {
		// 下面三种写法相同，这与PrecisionModel默认精度是双精度有关，具体可看源码
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 0);
		//    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),0);
		//    GeometryFactory geometryFactory = new GeometryFactory();

		WKTReader wktReader = new WKTReader(geometryFactory);
		/***** B的点都在A的内部 *****/
		System.out.println("/***** B的点都在A的内部 *****/");
		Geometry geometry11 = wktReader.read("POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))");
		Geometry geometry12 = wktReader.read("POLYGON((1 1, 2 1, 2 2, 1 2, 1 1))");

		boolean isIntersects1 = geometry11.intersects(geometry12);
		// geometry11.intersects(geometry12) -> true
		System.out.println("geometry11.intersects(geometry12) -> " + isIntersects1);

		/***** B的点都在A的边界上 ******/
		System.out.println("/***** B的点都在A的边界上 ******/");
		Geometry geometry21 = wktReader.read("POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))");
		Geometry geometry22 = wktReader.read("LINESTRING(0 0, 3 0)");

		boolean isIntersects2 = geometry21.intersects(geometry22);
		// geometry21.intersects(geometry22) -> true
		System.out.println("geometry21.intersects(geometry22) -> " + isIntersects2);

		/***** B的点一部分在A的边界上，一部分在A的外部 *****/
		System.out.println("/***** B的点一部分在A的边界上，一部分在A的外部 *****/");
		Geometry geometry31 = wktReader.read("POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))");
		Geometry geometry32 = wktReader.read("POLYGON((3 0, 6 0, 6 3, 3 3, 3 0))");

		boolean isIntersects3 = geometry31.intersects(geometry32);
		// geometry31.intersects(geometry32) -> true
		System.out.println("geometry31.intersects(geometry32) -> " + isIntersects3);

		/********************* A和B只有一个公共点 *********************/
		System.out.println("/***** A完全包含B-B的点集在A的边界上，不在内部 ******/");
		Geometry geometry41 = wktReader.read("POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))");
		Geometry geometry42 = wktReader.read("POLYGON((3 3, 6 3, 6 6, 3 6, 3 3))");

		boolean isIntersects4 = geometry41.intersects(geometry42);
		// geometry41.intersects(geometry42) -> true
		System.out.println("geometry41.intersects(geometry42) -> " + isIntersects4);
}
```

### 4.4.3 计算覆盖关系covers

​	covers方法用于计算覆盖关系，A.covers(B)=true只需要满足一个条件：B的点都在A的内部或者边界上，不要求B至少有一个点在A的内部。
​	covers方法和contains类似，不同的是covers方法不区分内部点和边界点，只要B的点全部都是A的即可。也就是covers方法更宽泛一些。

```java
@Test
public void testCovers() throws ParseException {
		// 下面三种写法相同，这与PrecisionModel默认精度是双精度有关，具体可看源码
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 0);
		//    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),0);
		//    GeometryFactory geometryFactory = new GeometryFactory();

		WKTReader wktReader = new WKTReader(geometryFactory);
		/********************* B的点集都在A的内部，不在边界上 *********************/
		System.out.println("/***** B的点集都在A的内部，不在边界上 *****/");
		Geometry geometry11 = wktReader.read("POLYGON((0 0, 0 3, 3 3, 3 0, 0 0))");
		Geometry geometry12 = wktReader.read("POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))");

		boolean isCovers1 = geometry11.covers(geometry12);
		// geometry11.covers(geometry12) -> true
		System.out.println("geometry11.covers(geometry12) -> " + isCovers1);

		/********************* B的点集不都在A的内部，有的在内部有的在边界上 *********************/
		System.out.println("/***** B的点集不都在A的内部，有的在内部有的在边界上 *****/");
		Geometry geometry21 = wktReader.read("POLYGON((0 0, 0 3, 3 3, 3 0, 0 0))");
		Geometry geometry22 = wktReader.read("POLYGON((0 0, 0 2, 2 2, 2 0, 0 0))");

		boolean isCovers2 = geometry21.covers(geometry22);
		// geometry21.covers(geometry22) -> true
		System.out.println("geometry21.covers(geometry22) -> " + isCovers2);


		/********************* B的点集在A的边界上，不在内部 *********************/
		System.out.println("/***** B的点集在A的边界上，不在内部 *****/");
		Geometry geometry31 = wktReader.read("POLYGON((0 0, 0 3, 3 3, 3 0, 0 0))");
		Geometry geometry32 = wktReader.read("LINESTRING(0 0, 0 3)");

		boolean isCovers3 = geometry31.covers(geometry32);
		// geometry31.covers(geometry32) -> true
		System.out.println("geometry31.covers(geometry32) -> " + isCovers3);
}
```

### 4.4.4 计算接触关系touches

​	touches方法用于计算接触关系，A.touches(B)=true只需要满足一个条件：A和B的公共点均只在边界上，内部无公共点集。

```java
@Test
public void testTouches() throws ParseException {
		// 下面三种写法相同，这与PrecisionModel默认精度是双精度有关，具体可看源码
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 0);
		//    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),0);
		//    GeometryFactory geometryFactory = new GeometryFactory();

		WKTReader wktReader = new WKTReader(geometryFactory);
		/********************* B的点都在A的内部 *********************/
		System.out.println("/***** B的点都在A的内部 *****/");
		Geometry geometry11 = wktReader.read("POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))");
		Geometry geometry12 = wktReader.read("POLYGON((1 1, 2 1, 2 2, 1 2, 1 1))");

		boolean isIntersects1 = geometry11.touches(geometry12);
		// geometry11.touches(geometry12) -> false
		System.out.println("geometry11.touches(geometry12) -> " + isIntersects1);

		/********************* B的点都在A的边界上 *********************/
		System.out.println("/***** B的点都在A的边界上 *****/");
		Geometry geometry21 = wktReader.read("POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))");
		Geometry geometry22 = wktReader.read("LINESTRING(0 0, 3 0)");

		boolean isIntersects2 = geometry21.touches(geometry22);
		// geometry21.touches(geometry22) -> true
		System.out.println("geometry21.touches(geometry22) -> " + isIntersects2);

		/********************* B的点一部分在A的边界上，一部分在A的外部 *********************/
		System.out.println("/***** B的点一部分在A的边界上，一部分在A的外部 *****/");
		Geometry geometry31 = wktReader.read("POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))");
		Geometry geometry32 = wktReader.read("POLYGON((3 0, 6 0, 6 3, 3 3, 3 0))");

		boolean isContains3 = geometry31.touches(geometry32);
		// geometry31.touches(geometry32) -> true
		System.out.println("geometry31.touches(geometry32) -> " + isContains3);

		/********************* A和B只有一个公共点 *********************/
		System.out.println("/***** B的点一部分在A的边界上，一部分在A的外部 *****/");
		Geometry geometry41 = wktReader.read("POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))");
		Geometry geometry42 = wktReader.read("POLYGON((3 3, 6 3, 6 6, 3 6, 3 3))");

		boolean isIntersects4 = geometry41.touches(geometry42);
		// geometry41.touches(geometry42) -> true
		System.out.println("geometry41.touches(geometry42) -> " + isIntersects4);
}
```

## 4.5 计算空间数据对象交并补差集

​	交集：A和B的公共点集；并集：A和B的所有点集；差集：在A中但是不在B中的点集；补集：A和B中不同时在A和B中的点集，也就是A和B的并集-A和B的交集。

```java
@Test
public void testGeometry() throws ParseException {
		// 下面三种写法相同，这与PrecisionModel默认精度是双精度有关，具体可看源码
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 0);
		//    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),0);
		//    GeometryFactory geometryFactory = new GeometryFactory();

		WKTReader wktReader = new WKTReader(geometryFactory);
		Geometry geometry11 = wktReader.read("POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))");
		Geometry geometry12 = wktReader.read("POLYGON((1 0, 4 0, 4 3, 1 3, 1 0))");

		// 交集：返回A和B的公共点集
		Geometry intersection = geometry11.intersection(geometry12);
		// geometry11.intersection(geometry12) -> POLYGON ((3 0, 1 0, 1 3, 3 3, 3 0))
		System.out.println("geometry11.intersection(geometry12) -> " + intersection);

		// 并集：返回A和B的所有点集
		Geometry union = geometry11.union(geometry12);
		// 这里有意思的是并没有直接按最大面的顶点来返回而是沿着顶点将交集点也包含在内来返回
		// geometry11.union(geometry12) -> POLYGON ((1 0, 0 0, 0 3, 1 3, 3 3, 4 3, 4 0, 3 0, 1 0))
		System.out.println("geometry11.union(geometry12) -> " + union);

		// 差集：返回在A中但是不在B中的点集
		Geometry difference = geometry11.difference(geometry12);
		// geometry11.difference(geometry12) -> POLYGON ((1 0, 0 0, 0 3, 1 3, 1 0))
		System.out.println("geometry11.difference(geometry12) -> " + difference);

		// 补集：返回A和B中不同时在A和B中的点集，也就是A和B的并集-A和B的交集
		Geometry symDifference = geometry11.symDifference(geometry12);
		// geometry11.symDifference(geometry12) -> MULTIPOLYGON (((1 0, 0 0, 0 3, 1 3, 1 0)), ((3 0, 3 3, 4 3, 4 0, 3 0)))
		System.out.println("geometry11.symDifference(geometry12) -> " + symDifference);
}
```

## 4.6 空间数据属性

### 4.6.1 空间数据类型常量

```java
public abstract class Geometry implements Cloneable, Comparable, Serializable
{
  private static final long serialVersionUID = 8763622679187376702L;
    
  protected static final int TYPECODE_POINT = 0;
  protected static final int TYPECODE_MULTIPOINT = 1;
  protected static final int TYPECODE_LINESTRING = 2;
  protected static final int TYPECODE_LINEARRING = 3;
  protected static final int TYPECODE_MULTILINESTRING = 4;
  protected static final int TYPECODE_POLYGON = 5;
  protected static final int TYPECODE_MULTIPOLYGON = 6;
  protected static final int TYPECODE_GEOMETRYCOLLECTION = 7;
  
  public static final String TYPENAME_POINT = "Point";
  public static final String TYPENAME_MULTIPOINT = "MultiPoint";
  public static final String TYPENAME_LINESTRING = "LineString";
  public static final String TYPENAME_LINEARRING = "LinearRing";
  public static final String TYPENAME_MULTILINESTRING = "MultiLineString";
  public static final String TYPENAME_POLYGON = "Polygon";
  public static final String TYPENAME_MULTIPOLYGON = "MultiPolygon";
  public static final String TYPENAME_GEOMETRYCOLLECTION = "GeometryCollection";
  ...（略）
}
```

### 4.6.2 空间数据属性

​		Geometry提供多个实例方法用于计算Geometry对象属性：

* getGeometryType：获取空间数据类型
* getBoundary：获取空间数据边界
* getArea：获取空间数据面积
* getCentroid：获取空间数据几何中心
* getLength：获取空间数据长度（周长）
* getSRID：获取空间数据引用标识符

```java
@Test
public void testGeometryProperty() throws ParseException {
		// 下面三种写法相同，这与PrecisionModel默认精度是双精度有关，具体可看源码
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 0);
		//    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),0);
		//    GeometryFactory geometryFactory = new GeometryFactory();

		WKTReader wktReader = new WKTReader(geometryFactory);
		Geometry geometry = wktReader.read("POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))");

		// 计算维度类型
		String geometryType = geometry.getGeometryType();
		// geometry.getGeometryType() -> Polygon
		System.out.println("geometry.getGeometryType() -> " + geometryType);

		// 计算Geometry边界
		Geometry boundary = geometry.getBoundary();
		// geometry.getBoundary() -> LINEARRING (0 0, 3 0, 3 3, 0 3, 0 0)
		System.out.println("geometry.getBoundary() -> " + boundary);

		// 计算Geometry面积
		double area = geometry.getArea();
		// geometry.getArea() -> 9.0
		System.out.println("geometry.getArea() -> " + area);

		// 计算Geometry中心点
		Point centroid = geometry.getCentroid();
		// geometry.getCentroid() -> POINT (1.5 1.5)
		System.out.println("geometry.getCentroid() -> " + centroid);

		// 计算Geometry长度
		double length = geometry.getLength();
		// geometry.getLength() -> 12.0
		System.out.println("geometry.getLength() -> " + length);

		// 计算Geometry空间标识符
		int srid = geometry.getSRID();
		// geometry.getSRID() -> 0
		System.out.println("geometry.getSRID() -> " + srid);
}
```

## 4.7 其他疑难问题测试

###  4.7.1 关于有外壳和内洞的面Polygon的猜想测试

​		面是三维数组，第一个元素是外壳，其他元素都是内洞，并且和点集的顺逆时针无关。猜想来自于GeometryFactory的createPolygon方法，外坑只有一个环，而内洞是环数组，猜想对于Polygon的WKT字符串，只有第一个是外壳，其他都是内洞。

```java
public Polygon createPolygon(LinearRing shell, LinearRing[] holes)
```

​	测试代码：

```java
@Test
public void testPolygonWithShellAndHole() throws ParseException {
		// 下面三种写法相同，这与PrecisionModel默认精度是双精度有关，具体可看源码
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 0);
		//    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),0);
		//    GeometryFactory geometryFactory = new GeometryFactory();

		WKTReader wktReader = new WKTReader(geometryFactory);

		// 只有外壳的面
		Geometry polygonGeometry1 = wktReader.read("POLYGON ((0 0, 3 0, 3 1, 0 1, 0 0))");
		Polygon polygon1 = (Polygon) polygonGeometry1;
		// polygon1 -> POLYGON ((0 0, 3 0, 3 1, 0 1, 0 0))
		System.out.println("polygon1 -> " + polygon1.toText());
		// the area of polygon1 -> 3.0
		System.out.println("the area of polygon1 -> "+polygon1.getArea());

		// 只有一个内洞的面
		Geometry polygonGeometry2 = wktReader.read("POLYGON ((0 0, 3 0, 3 1, 0 1, 0 0), (0 0, 1 0, 1 1, 0 1, 0 0))");
		Polygon polygon2 = (Polygon) polygonGeometry2;
		// polygon2 -> POLYGON ((0 0, 3 0, 3 1, 0 1, 0 0), (0 0, 1 0, 1 1, 0 1, 0 0))
		System.out.println("polygon2 -> " + polygon2.toText());
		// the area of polygon2 -> 2.0
		System.out.println("the area of polygon2 -> "+polygon2.getArea());

		// 有两个内洞的面，两个内洞均逆时针
		Geometry polygonGeometry3 = wktReader.read("POLYGON ((0 0, 3 0, 3 1, 0 1, 0 0), (0 0, 1 0, 1 1, 0 1, 0 0),(1 0, 1 1, 2 1, 2 0, 1 0))");
		Polygon polygon3 = (Polygon) polygonGeometry3;
		// polygon3 -> POLYGON ((0 0, 3 0, 3 1, 0 1, 0 0), (0 0, 1 0, 1 1, 0 1, 0 0), (1 0, 1 1, 2 1, 2 0, 1 0))
		System.out.println("polygon3 -> " + polygon3.toText());
		// the area of polygon3 -> 1.0
		System.out.println("the area of polygon3 -> "+polygon3.getArea());

		// 有两个内洞的面，一个内洞顺时针，一个内洞逆时针
		Geometry polygonGeometry4 = wktReader.read("POLYGON ((0 0, 3 0, 3 1, 0 1, 0 0), (1 0, 0 0, 0 1, 1 1, 1 0),(1 0, 2 0, 2 1, 1 1, 1 0))");
		Polygon polygon4 = (Polygon) polygonGeometry4;
		// polygon4 -> POLYGON ((0 0, 3 0, 3 1, 0 1, 0 0), (1 0, 0 0, 0 1, 1 1, 1 0), (1 0, 2 0, 2 1, 1 1, 1 0))
		System.out.println("polygon4 -> " + polygon4.toText());
		// the area of polygon4 -> 1.0
		System.out.println("the area of polygon4 -> "+polygon4.getArea());
}
```









