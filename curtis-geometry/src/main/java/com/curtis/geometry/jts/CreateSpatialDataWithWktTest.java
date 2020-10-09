package com.curtis.geometry.jts;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author curtis
 * @desc 创建空间数据对象-使用WKT（仅需要引入jts-core的依赖）
 * @date 2020-09-18
 * @email 397773935@qq.com
 * @reference
 */
public class CreateSpatialDataWithWktTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateSpatialDataWithWktTest.class);

    /**
     * 测试使用WKT字符串构建空间数据(Spatial Data)
     * 附：WKT字符串中标识空间数据类型部分的字符串大小写均可，FE: POINT (0 0)、Point (0 0)、point (0 0)
     * 经纬度之间必须有空格，但是标识符和括号之间有无空格无所谓,括号与括号之间、坐标和坐标之间有无空格也无所谓
     */
    @Test
    public void createGeometryByWkt() throws ParseException {
        // 下面三种写法相同，这与PrecisionModel默认精度是双精度有关，具体可看源码
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 0);
//    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),0);
//    GeometryFactory geometryFactory = new GeometryFactory();

        /************************************** Create Point **************************************/
        // 点Point的WKT格式字符串对应一维数组，数组只有两个元素（经纬度）。
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

        /************************************** Create MultiPoint **************************************/
        // 多点MultiPoint的WKT格式字符串对应二维数组，二维数组的元素个数对应点的个数，二维数组的元素是只有两个元素（经纬度）的一维数组。
        Geometry multiPointGeometry = wktReader.read("MULTIPOINT ((0 0), (1 0))");
        if (Geometry.TYPENAME_MULTIPOINT.equalsIgnoreCase(multiPointGeometry.getGeometryType())) {
            if (multiPointGeometry instanceof MultiPoint) {
                MultiPoint multiPoint = (MultiPoint) multiPointGeometry;
                // multiPoint -> MULTIPOINT ((0 0), (1 0))
                System.out.println("multiPoint -> " + multiPoint.toText());
            }
        }

        /************************************** Create LineString **************************************/
        // 线LineString的WKT格式字符串对应二维数组，二维数组的元素个数对应线上点的个数，二维数组的元素是只有两个元素（经纬度）的一维数组。
        Geometry lineStringGeometry = wktReader.read("LINESTRING (0 0, 1 0)");
        if (Geometry.TYPENAME_LINESTRING.equalsIgnoreCase(lineStringGeometry.getGeometryType())) {
            if (lineStringGeometry instanceof LineString) {
                LineString lineString = (LineString) lineStringGeometry;
                // lineString -> LINESTRING (0 0, 1 0)
                System.out.println("lineString -> " + lineString.toText());
            }
        }

        /************************************** Create MultiLineString **************************************/
        // 多线MultiLineString的WKT格式字符串对应三维数组，三维数组的每个二维数组都是线，二维数组的元素对应线上的每个点
        // 如果三维数组只有一个元素则是单条线组成的多线，如果有多个元素就是多条线组成的多线。
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

        /************************************** Create LinearRing **************************************/
        // 环线LinearRing的WKT格式字符串对应二维数组，二维数组的元素个数对应线上点的个数，二维数组的元素是只有两个元素（经纬度）的一维数组。
        // 环线LinearRing要求首尾点相同，并且至少四个点，所以二维数组至少有四个元素并且第一个元素和最后一个元素相同
        Geometry linearRingGeometry = wktReader.read("LINEARRING (0 0, 2 0, 1 2, 0 0)");
        if (Geometry.TYPENAME_LINEARRING.equalsIgnoreCase(linearRingGeometry.getGeometryType())) {
            if (linearRingGeometry instanceof LinearRing) {
                LinearRing linearRing = (LinearRing) linearRingGeometry;
                // linearRing -> LINEARRING (0 0, 2 0, 1 2, 0 0)
                System.out.println("linearRing -> " + linearRing.toText());
            }
        }

        /************************************** Create Polygon **************************************/
        // 面Polygon的WKT格式字符串对应三维数组，三维数组的第一个元素是外壳，三维数组的其他元素都是内洞，无论是外壳还是内洞都是环线。
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

        /************************************** Create MultiPolygon **************************************/
        // 多面MultiPolygon的WKT格式字符串对应四维维数组，四维数组的每个元素都是一个面。
        // 多个面组成的多面
        Geometry multiPolygonGeometry1 = wktReader.read("MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)), ((1 1, 2 1, 2 2, 1 2, 1 1)))");
        if (Geometry.TYPENAME_MULTIPOLYGON.equalsIgnoreCase(multiPolygonGeometry1.getGeometryType())) {
            if (multiPolygonGeometry1 instanceof MultiPolygon) {
                MultiPolygon multiPolygon1 = (MultiPolygon) multiPolygonGeometry1;
                // multiPolygon1 -> MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)), ((1 1, 2 1, 2 2, 1 2, 1 1)))
                System.out.println("multiPolygon1 -> " + multiPolygon1.toText());
            }
        }
        // 单个面组成的多面
        Geometry multiPolygonGeometry2 = wktReader.read("MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)))");
        if (Geometry.TYPENAME_MULTIPOLYGON.equalsIgnoreCase(multiPolygonGeometry2.getGeometryType())) {
            if (multiPolygonGeometry2 instanceof MultiPolygon) {
                MultiPolygon multiPolygon2 = (MultiPolygon) multiPolygonGeometry2;
                // multiPolygon2 -> MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)))
                System.out.println("multiPolygon2 -> " + multiPolygon2.toText());
            }
        }

        /************************************** Create GeometryCollection **************************************/
        // 面集合GeometryCollection的WKT格式字符串对应五维数组，五维数组的每个元素都是一个Geometry，元素可能是一维到四维数组。
        // 多种维度Geometry组成的Geometry集合
        Geometry geometryCollectionGeometry1 = wktReader.read(
                "GEOMETRYCOLLECTION (POINT (1 1), " +
                        "LINESTRING (0 0, 1 0), " +
                        "POLYGON ((0 0, 1 1, 2 1, 0 0)), " +
                        "MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)), ((1 1, 2 1, 2 2, 1 2, 1 1))))");
        if (Geometry.TYPENAME_GEOMETRYCOLLECTION.equalsIgnoreCase(geometryCollectionGeometry1.getGeometryType())) {
            if (geometryCollectionGeometry1 instanceof GeometryCollection) {
                GeometryCollection geometryCollection1 = (GeometryCollection) geometryCollectionGeometry1;
                // geometryCollection1 -> GEOMETRYCOLLECTION (POINT (1 1), LINESTRING (0 0, 1 0),
                // POLYGON ((0 0, 1 1, 2 1, 0 0)), MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)), ((1 1, 2 1, 2 2, 1 2, 1 1))))
                System.out.println("geometryCollection1 -> " + geometryCollection1.toText());
            }
        }
        // 单个点组成的Geometry集合（最简单的Geometry集合）
        Geometry geometryCollectionGeometry2 = wktReader.read("GEOMETRYCOLLECTION (POINT (1 1))");
        if (Geometry.TYPENAME_GEOMETRYCOLLECTION.equalsIgnoreCase(geometryCollectionGeometry2.getGeometryType())) {
            if (geometryCollectionGeometry2 instanceof GeometryCollection) {
                GeometryCollection geometryCollection2 = (GeometryCollection) geometryCollectionGeometry2;
                // geometryCollection2 -> GEOMETRYCOLLECTION (POINT (1 1))
                System.out.println("geometryCollection2 -> " + geometryCollection2.toText());
            }
        }
    }

    /**
     * 测试WKTReader对象解析WKT字符串的read方法的线程安全问题：结论线程安全
     * 有点想不明白的是为什么不使用静态方法而使用实例方法？？？
     */
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
                                    "MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)), ((1 1, 2 1, 2 2, 1 2, 1 1))))");
                    System.out.println(geometryCollectionGeometry1.toText());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
            completableFutureList.add(future);
            // 如果在这里使用join，就不是并发了而是顺序执行
//            future.join();
        }
        for (CompletableFuture future : completableFutureList) {
            future.join();
        }
        LocalTime endTime = LocalTime.now();
        System.out.println("执行耗时" + Duration.between(startTime, endTime).getSeconds() + "秒");
    }

    /**
     * 测试有洞的面
     * 面是三维数组，第一个元素是外壳，其他元素都是内洞，并且和点集的顺逆时针无关。
     * @throws ParseException
     */
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
}
