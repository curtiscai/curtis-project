package com.curtis.geometry.jts;

import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 * @author curtis
 * @desc 计算空间数据对象属性以及交并差集（仅需要引入jts-core的依赖）
 * @email 397773935@qq.com
 * @reference
 */
public class ComputeSpatialDataPropertyTest {

    /**
     * 测试计算空间数据相关属性
     */
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

    /**
     * 测试计算空间数据相关属性
     */
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

    @Test
    public void test() throws ParseException {
        // 下面三种写法相同，这与PrecisionModel默认精度是双精度有关，具体可看源码
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 0);
//    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),0);
//    GeometryFactory geometryFactory = new GeometryFactory();

        /***** distance用于计算两个Geometry（任何Geometry类型）的最小距离 *****/
        WKTReader wktReader = new WKTReader(geometryFactory);
        Geometry geometry11 = wktReader.read("POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))");
        Geometry geometry12 = wktReader.read("POLYGON((6 0, 9 0, 9 3, 6 3, 6 0))");
        double distance1 = geometry11.distance(geometry12);
        // distance1 -> 3.0
        System.out.println("distance1 -> " + distance1);

        Geometry geometry21 = wktReader.read("LINESTRING(0 0, 0 3)");
        Geometry geometry22 = wktReader.read("LINESTRING(1 0, 2 2)");
        double distance2 = geometry21.distance(geometry22);
        // distance2 -> 1.0
        System.out.println("distance2 -> " + distance2);

        Geometry geometry31 = wktReader.read("POINT(0 0)");
        Geometry geometry32 = wktReader.read("POINT(1 0)");
        double distance3 = geometry31.distance(geometry32);
        // distance3 -> 1.0
        System.out.println("distance3 -> " + distance3);


        Geometry geometry41 = wktReader.read("POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))");
        Geometry buffer = geometry41.buffer(1);
        System.out.println(buffer.toText());
    }
}
