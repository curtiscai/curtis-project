package com.curtis.geometry.jts;

import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author curtis
 * @desc 创建空间数据对象-使用坐标（仅需要引入jts-core的依赖）
 * @date 2020-09-18
 * @email 397773935@qq.com
 * @reference
 */
public class CreateSpatialDataWithCoordinateTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateSpatialDataWithCoordinateTest.class);

    /**
     * 测试使用Coordinate构建空间数据(Spatial Data)
     */
    @Test
    public void createSpatialDataByCoordinate() {
        // 下面三种写法相同，这与PrecisionModel默认精度是双精度有关，具体可看源码
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 0);
//    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),0);
//    GeometryFactory geometryFactory = new GeometryFactory();

        /************************************** Create Point **************************************/
        // public Point createPoint(Coordinate coordinate)
        // public Point createPoint(CoordinateSequence coordinates)
        Coordinate coordinate = new Coordinate(1, 1);
        Point point = geometryFactory.createPoint(coordinate);
        // point -> POINT (1 1)
        System.out.println("point -> " + point);

        /************************************** Create MultiPoint **************************************/
        // public MultiPoint createMultiPoint(Coordinate[] coordinates)
        // public MultiPoint createMultiPoint(Point[] point)
        // public MultiPoint createMultiPoint(CoordinateSequence coordinates)
        // GeometryFactory对象提供两种创建多点的方式，分别是使用坐标数组Coordinate[]和点数组Point[]。
        Coordinate[] coordinates = new Coordinate[]{new Coordinate(0, 0), new Coordinate(1, 0)};
        MultiPoint multiPoint1 = geometryFactory.createMultiPointFromCoords(coordinates);
        // multiPoint1 -> MULTIPOINT ((0 0), (1 0))
        System.out.println("multiPoint1 -> " + multiPoint1);

        Point point1 = geometryFactory.createPoint(new Coordinate(0, 1));
        Point point2 = geometryFactory.createPoint(new Coordinate(1, 1));
        Point[] points = new Point[]{point1, point2};
        MultiPoint multiPoint2 = geometryFactory.createMultiPoint(points);
        // multiPoint2 -> MULTIPOINT ((0 1), (1 1))
        System.out.println("multiPoint2 -> " + multiPoint2);

        /************************************** Create LineString **************************************/
        // public LineString createLineString(Coordinate[] coordinates)
        // public LineString createLineString(CoordinateSequence coordinates)
        // GeometryFactory对象提供使用坐标数组Coordinate[]来创建线的方式。
        Coordinate[] coordinatesLineArray1 = new Coordinate[]{new Coordinate(0, 0), new Coordinate(1, 0)};
        LineString lineString1 = geometryFactory.createLineString(coordinatesLineArray1);
        // lineString1 -> LINESTRING (0 0, 1 0)
        System.out.println("lineString1 -> " + lineString1);

        Coordinate[] coordinatesLineArray2 = new Coordinate[]{new Coordinate(1, 0), new Coordinate(1, 1)};
        LineString lineString2 = geometryFactory.createLineString(coordinatesLineArray2);
        // lineString2 -> LINESTRING (1 0, 1 1)
        System.out.println("lineString2 -> " + lineString2);

        /************************************** Create MultiLineString **************************************/
        // public MultiLineString createMultiLineString(LineString[] lineStrings)
        // GeometryFactory对象提供使用线数组LineString[]来创建多线对象的方式。
        LineString[] lineStringArray = new LineString[]{lineString1, lineString2};
        MultiLineString multiLineString = geometryFactory.createMultiLineString(lineStringArray);
        // multiLineString -> MULTILINESTRING ((0 0, 1 0), (1 0, 1 1))
        System.out.println("multiLineString -> " + multiLineString);

        /************************************** Create LinearRing **************************************/
        // public LinearRing createLinearRing(Coordinate[] coordinates)
        // public LinearRing createLinearRing(CoordinateSequence coordinates)
        // GeometryFactory对象提供使用坐标数组Coordinate[]来创建环线LinearRing对象的方式。
        Coordinate coordinate1 = new Coordinate(0, 0);
        Coordinate coordinate2 = new Coordinate(2, 0);
        Coordinate coordinate3 = new Coordinate(1, 2);
        Coordinate coordinate4 = new Coordinate(0, 0);
        Coordinate[] coordinateArray = new Coordinate[]{coordinate1, coordinate2, coordinate3, coordinate4};
        // 闭环的线要求首尾是同一个点，否则抛出异常
        // java.lang.IllegalArgumentException: Points of LinearRing do not form a closed linestring
//        Coordinate[] coordinateArray = new Coordinate[]{coordinate1,coordinate2,coordinate3};

        // 环线要求至少四个点，否则抛出异常
        // java.lang.IllegalArgumentException: Invalid number of points in LinearRing (found 3 - must be 0 or >= 4)
//        Coordinate[] coordinateArray = new Coordinate[]{coordinate1,coordinate2,coordinate4};

        LinearRing linearRing = geometryFactory.createLinearRing(coordinateArray);
        // linearRing -> LINEARRING (0 0, 2 0, 1 2, 0 0)
        System.out.println("linearRing -> " + linearRing);

        /************************************** Create Polygon **************************************/
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
//        Coordinate[] coordinateArray2 = new Coordinate[]{coordinate21, coordinate22, coordinate23};

        //  环线要求至少四个点，否则抛出异常
        //  java.lang.IllegalArgumentException: Invalid number of points in LinearRing (found 3 - must be 0 or >= 4)
//        Coordinate[] coordinateArray2 = new Coordinate[]{coordinate21, coordinate22, coordinate21};
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
        Polygon polygon3 = geometryFactory.createPolygon(linearRingShell, new LinearRing[]{linearRingHole,linearRingHole});
        // polygon3 -> POLYGON ((0 0, 3 0, 3 3, 0 3, 0 0), (1 1, 2 1, 2 2, 1 2, 1 1))
        System.out.println("polygon3 -> " + polygon3);


        /************************************** Create MultiPolygon **************************************/
        // GeometryFactory对象提供使用面数组Polygon[]来创建多面对象的方式。
        // public MultiPolygon createMultiPolygon(Polygon[] polygons)
        Polygon polygon41 = geometryFactory.createPolygon(linearRingShell);
        Polygon polygon42 = geometryFactory.createPolygon(linearRingHole);
        Polygon[] polygonArray = new Polygon[]{polygon41, polygon42};
        MultiPolygon multiPolygon = geometryFactory.createMultiPolygon(polygonArray);
        // multiPolygon -> MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)), ((1 1, 2 1, 2 2, 1 2, 1 1)))
        System.out.println("multiPolygon -> " + multiPolygon);

        /************************************** Create GeometryCollection **************************************/
        // GeometryFactory对象提供使用Geometry数组Geometry[]来创建Geometry集合的方式。
        // public GeometryCollection createGeometryCollection(Geometry[] geometries)
        GeometryCollection geometryCollection = geometryFactory.createGeometryCollection(new Geometry[]{point, lineString1, polygon1, multiPolygon});
        // geometryCollection -> GEOMETRYCOLLECTION (POINT (1 1), LINESTRING (0 0, 1 0), POLYGON ((0 0, 1 1, 2 1, 0 0)), MULTIPOLYGON (((0 0, 3 0, 3 3, 0 3, 0 0)), ((1 1, 2 1, 2 2, 1 2, 1 1))))
        System.out.println("geometryCollection -> " + geometryCollection);
    }
}
