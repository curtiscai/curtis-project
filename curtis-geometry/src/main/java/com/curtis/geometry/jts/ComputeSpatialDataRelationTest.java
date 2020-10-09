package com.curtis.geometry.jts;

import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 * @author curtis
 * @desc 计算空间数据对象关系（仅需要引入jts-core的依赖）
 * @date 2020-09-18
 * @email 397773935@qq.com
 * @reference
 */
public class ComputeSpatialDataRelationTest {

    /**
     * contains方法和within方法都是计算包含关系，如果A.contains(B)=true成立则B.within(A)=true一定成立，反之亦然。
     * contains方法表示包含关系，A.contains(B)=true需要满足两个条件：B的点都在A的内部或者边界上；B至少有一个点在A的内部。
     * within方法也表示包含关系，不过A.within(B)=true表示的是B包含A或者A在B的内部。
     *
     * @throws ParseException
     */
    @Test
    public void testContainsAndWithin() throws ParseException {
        // 下面三种写法相同，这与PrecisionModel默认精度是双精度有关，具体可看源码
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 0);
//    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),0);
//    GeometryFactory geometryFactory = new GeometryFactory();

        WKTReader wktReader = new WKTReader(geometryFactory);
        /********************* B的点集都在A的内部，不在边界上 *********************/
        System.out.println("/********************* A完全包含B-B的点集都在A的内部，不在边界上 *********************/");
        Geometry geometry11 = wktReader.read("POLYGON((0 0, 0 3, 3 3, 3 0, 0 0))");
        Geometry geometry12 = wktReader.read("POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))");

        boolean isContains1 = geometry11.contains(geometry12);
        // geometry11.contains(geometry12) -> true
        System.out.println("geometry11.contains(geometry12) -> " + isContains1);
        boolean isWithin1 = geometry12.within(geometry11);
        // geometry12.within(geometry11) -> true
        System.out.println("geometry12.within(geometry11) -> " + isWithin1);

        /********************* B的点集不都在A的内部，有的在内部有的在边界上 *********************/
        System.out.println("/********************* A完全包含B-B的点集不都在A的内部，有的在内部有的在边界上 *********************/");
        Geometry geometry21 = wktReader.read("POLYGON((0 0, 0 3, 3 3, 3 0, 0 0))");
        Geometry geometry22 = wktReader.read("POLYGON((0 0, 0 2, 2 2, 2 0, 0 0))");

        boolean isContains2 = geometry21.contains(geometry22);
        // geometry21.contains(geometry22) -> true
        System.out.println("geometry21.contains(geometry22) -> " + isContains2);
        boolean isWithin2 = geometry22.within(geometry21);
        // geometry22.within(geometry21) -> true
        System.out.println("geometry22.within(geometry21) -> " + isWithin2);

        /********************* B的点集在A的边界上，不在内部 *********************/
        System.out.println("/********************* A完全包含B-B的点集在A的边界上，不在内部 *********************/");
        Geometry geometry31 = wktReader.read("POLYGON((0 0, 0 3, 3 3, 3 0, 0 0))");
        Geometry geometry32 = wktReader.read("LINESTRING(0 0, 0 3)");

        boolean isContains3 = geometry31.contains(geometry32);
        // geometry31.contains(geometry32) -> false
        System.out.println("geometry31.contains(geometry32) -> " + isContains3);
        boolean isWithin3 = geometry32.within(geometry31);
        // geometry32.within(geometry31) -> false
        System.out.println("geometry32.within(geometry31) -> " + isWithin3);
    }

    /**
     * covers方法用于计算覆盖关系，A.covers(B)=true只需要满足一个条件：B的点都在A的内部或者边界上，不要求B至少有一个点在A的内部。
     * covers方法和contains类似，不同的是covers方法不区分内部点和边界点，只要B的点全部都是A的即可。也就是covers方法更宽泛一些。
     *
     * @throws ParseException
     */
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

    /**
     * intersects方法用于计算相交关系，表示相交，disjoint正好相反表示不相交，如果A.intersects(B)=true成立则A.disjoint(B)=false，反之亦然。
     * intersects方法用于表示两个Geometry相交，A.intersects(B)=true只需要满足一个条件：A和B至少有一个公共的点。
     * disjoint方法用于表示两个Geometry不想交，A.disjoint(B)=true只需要满足一个条件：A和B没有任何公共的点。
     *
     * @throws ParseException
     */
    @Test
    public void testIntersectsAndDisjoint() throws ParseException {
        // 下面三种写法相同，这与PrecisionModel默认精度是双精度有关，具体可看源码
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 0);
//    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),0);
//    GeometryFactory geometryFactory = new GeometryFactory();

        WKTReader wktReader = new WKTReader(geometryFactory);
        /********************* B的点都在A的内部 *********************/
        System.out.println("/***** B的点都在A的内部 *****/");
        Geometry geometry11 = wktReader.read("POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))");
        Geometry geometry12 = wktReader.read("POLYGON((1 1, 2 1, 2 2, 1 2, 1 1))");

        boolean isIntersects1 = geometry11.intersects(geometry12);
        // geometry11.intersects(geometry12) -> true
        System.out.println("geometry11.intersects(geometry12) -> " + isIntersects1);

        /********************* B的点都在A的边界上 *********************/
        System.out.println("/***** B的点都在A的边界上 *****/");
        Geometry geometry21 = wktReader.read("POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))");
        Geometry geometry22 = wktReader.read("LINESTRING(0 0, 3 0)");

        boolean isIntersects2 = geometry21.intersects(geometry22);
        // geometry21.intersects(geometry22) -> true
        System.out.println("geometry21.intersects(geometry22) -> " + isIntersects2);

        /********************* B的点一部分在A的边界上，一部分在A的外部 *********************/
        System.out.println("/***** B的点一部分在A的边界上，一部分在A的外部 *****/");
        Geometry geometry31 = wktReader.read("POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))");
        Geometry geometry32 = wktReader.read("POLYGON((3 0, 6 0, 6 3, 3 3, 3 0))");

        boolean isContains3 = geometry31.intersects(geometry32);
        // geometry31.intersects(geometry32) -> true
        System.out.println("geometry31.intersects(geometry32) -> " + isContains3);

        /********************* A和B只有一个公共点 *********************/
        System.out.println("/********************* A完全包含B-B的点集在A的边界上，不在内部 *********************/");
        Geometry geometry41 = wktReader.read("POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))");
        Geometry geometry42 = wktReader.read("POLYGON((3 3, 6 3, 6 6, 3 6, 3 3))");

        boolean isIntersects4 = geometry41.intersects(geometry42);
        // geometry41.intersects(geometry42) -> true
        System.out.println("geometry41.intersects(geometry42) -> " + isIntersects4);
    }

    /**
     * touches方法用于计算接触关系，A.touches(B)=true只需要满足一个条件：A和B的公共点均只在边界上，内部无公共点集。
     *
     * @throws ParseException
     */
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
}
