package com.curtis.geometry.geohash;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author curtis
 * @desc
 * @date 2020-09-22
 * @email 397773935@qq.com
 * @reference
 */
public class GeoHashTest {

    /**
     * 演示GeoHash字符串示例-1位、2位、6位、7位
     */
    @Test
    public void testGeoHashStr() {
        double lat = 39.909604;
        double lng = 116.397228;

        /********************************** GeoHash-1位字符串代码示例 **********************************/
        System.out.println("/******** GeoHash-1位字符串代码示例 ********/");
        GeoHash geoHash1 = GeoHash.withCharacterPrecision(lat, lng, 1);
        // (22.5,112.5)
        System.out.println(geoHash1.getBoundingBox().getCenterPoint());
        // (45.0,90.0) -> (0.0,135.0)
        System.out.println(geoHash1.getBoundingBox());
        // w
        System.out.println(geoHash1.toBase32());
        // 1110000000000000000000000000000000000000000000000000000000000000 -> (45.0,90.0) -> (0.0,135.0) -> w
        System.out.println(geoHash1);

        /********************************** GeoHash-2位字符串代码示例 **********************************/
        System.out.println("/******** GeoHash-2位字符串代码示例 ********/");
        GeoHash geoHash2 = GeoHash.withCharacterPrecision(lat, lng, 2);
        // (42.1875,118.125)
        System.out.println(geoHash2.getBoundingBox().getCenterPoint());
        // (45.0,112.5) -> (39.375,123.75)
        System.out.println(geoHash2.getBoundingBox());
        // wx
        System.out.println(geoHash2.toBase32());
        // 1110011101000000000000000000000000000000000000000000000000000000 -> (45.0,112.5) -> (39.375,123.75) -> wx
        System.out.println(geoHash2);

        /********************************** GeoHash-6位字符串代码示例 **********************************/
        System.out.println("/******** GeoHash-6位字符串代码示例 ********/");
        GeoHash geoHash6 = GeoHash.withCharacterPrecision(lat, lng, 6);
        // (39.91058349609375,116.3946533203125)
        System.out.println(geoHash6.getBoundingBox().getCenterPoint());
        // (39.913330078125,116.38916015625) -> (39.9078369140625,116.400146484375)
        System.out.println(geoHash6.getBoundingBox());
        // wx4g09
        System.out.println(geoHash6.toBase32());
        // 1110011101001000111100000010010000000000000000000000000000000000 -> (39.913330078125,116.38916015625)
        // -> (39.9078369140625,116.400146484375) -> wx4g09
        System.out.println(geoHash6);

        /********************************** GeoHash-7位字符串代码示例 **********************************/
        System.out.println("/******** GeoHash-7位字符串代码示例 ********/");
        GeoHash geoHash7 = GeoHash.withCharacterPrecision(lat, lng, 7);
        // (39.90989685058594,116.39671325683594)
        System.out.println(geoHash7.getBoundingBox().getCenterPoint());
        // (39.91058349609375,116.39602661132812) -> (39.909210205078125,116.39739990234375)
        System.out.println(geoHash7.getBoundingBox());
        // wx4g09m
        System.out.println(geoHash7.toBase32());
        // 1110011101001000111100000010011001100000000000000000000000000000 -> (39.91058349609375,116.39602661132812)
        // -> (39.909210205078125,116.39739990234375) -> wx4g09m
        System.out.println(geoHash7);
    }

    /**
     * 构造GeoHash的几种方式：使用GeoHash字符串、使用经纬度以及字符个数、使用经纬度以及任意位
     */
    @Test
    public void testConstructGeoHash() {
        // 使用GeoHash字符串构造GeoHash对象
        GeoHash geoHash61 = GeoHash.fromGeohashString("wx4g09");
        // geoHash61: 1110011101001000111100000010010000000000000000000000000000000000 ->
        // (39.913330078125,116.38916015625) -> (39.9078369140625,116.400146484375) -> wx4g09
        System.out.println("geoHash61: " + geoHash61);
        GeoHash geoHash71 = GeoHash.fromGeohashString("wx4g09m");
        // geoHash71: 1110011101001000111100000010011001100000000000000000000000000000 ->
        // (39.91058349609375,116.39602661132812) -> (39.909210205078125,116.39739990234375) -> wx4g09m
        System.out.println("geoHash71: " + geoHash71);

        // 使用经度、纬度以及字符个数来构建GeoHash
        GeoHash geoHash62 = GeoHash.withCharacterPrecision(39.909604, 116.397228, 6);
        // geoHash62: 1110011101001000111100000010010000000000000000000000000000000000 ->
        // (39.913330078125,116.38916015625) -> (39.9078369140625,116.400146484375) -> wx4g09
        System.out.println("geoHash62: " + geoHash62);
        GeoHash geoHash72 = GeoHash.withCharacterPrecision(39.909604, 116.397228, 7);
        // geoHash72: 1110011101001000111100000010011001100000000000000000000000000000 ->
        // (39.91058349609375,116.39602661132812) -> (39.909210205078125,116.39739990234375) -> wx4g09m
        System.out.println("geoHash72: " + geoHash72);

        // 可以使用经度、纬度以及任意位来构建GeoHash，只有5的整数位才可以使用Base32编码为GeoHash字符串
        GeoHash geoHashBit1 = GeoHash.withBitPrecision(39.909604, 116.397228, 6);
        // geoHashBit1: 1110010000000000000000000000000000000000000000000000000000000000 ->
        // (45.0,90.0) -> (22.5,135.0), bits: 6
        System.out.println("geoHashBit1: " + geoHashBit1);
        GeoHash geoHashBit2 = GeoHash.withBitPrecision(39.909604, 116.397228, 10);
        // geoHashBit2: 1110011101000000000000000000000000000000000000000000000000000000 ->
        // (45.0,112.5) -> (39.375,123.75) -> wx
        System.out.println("geoHashBit2: " + geoHashBit2);
    }

    /**
     * 获取GeoHash属性：获取GeoHash的GeoHash字符串、GeoHash的精度、GeoHash临近8个GeoHash、东西南北四个方向GeoHash
     */
    @Test
    public void testGeoHashProperty() {

        /*********************** 获取GeoHash的属性-GeoHash字符串、GeoHash精度 **************************/
        GeoHash geoHash7 = GeoHash.fromGeohashString("wx4g09m");
        // toBase32()：获取指定GeoHash的Base32字符串，要求GeoHash的位的精度必须是5的倍数，否则抛出如下异常：
        // java.lang.IllegalStateException: Cannot convert a geohash to base32 if the precision is not a multiple of 5.
//        String geoHashStr = geoHashBit1.toBase32();
        String geoHashStr = geoHash7.toBase32();
        // geoHashStr：wx4g09m
        System.out.println("geoHash7Str：" + geoHashStr);

        int characterPrecision = geoHash7.getCharacterPrecision();
        System.out.println("characterPrecision：" + characterPrecision);

        /******************** 获取GeoHash的属性-GeoHash临近8个GeoHash、东西南北方向4个GeoHash ********************/
        // 获取指定GeoHash网格临近的8个GeoHash，顺序分别是：N, NE, E, SE, S, SW, W, NW
        GeoHash[] adjacent = geoHash7.getAdjacent();
        System.out.println(Arrays.toString(adjacent));

        // getEasternNeighbour()：获取指定GeoHash东边相邻的GeoHash
        GeoHash easternNeighbour = geoHash7.getEasternNeighbour();
        System.out.println("easternNeighbour：" + easternNeighbour);
        // getWesternNeighbour()：获取指定GeoHash西边相邻的GeoHash
        GeoHash westernNeighbour = geoHash7.getWesternNeighbour();
        System.out.println("westernNeighbour：" + westernNeighbour);
        // getNorthernNeighbour()：获取指定GeoHash北边相邻的GeoHash
        GeoHash northernNeighbour = geoHash7.getNorthernNeighbour();
        System.out.println("northernNeighbour：" + northernNeighbour);
        // getSouthernNeighbour()：获取指定GeoHash南边相邻的GeoHash
        GeoHash southernNeighbour = geoHash7.getSouthernNeighbour();
        System.out.println("southernNeighbour：" + southernNeighbour);


        /******************** 获取GeoHash的属性-GeoHash中心点、GeoHash四角各点坐标 ********************/
        // getPoint()：获取构建GeoHash时使用的经纬度，如果使用Base32字符串构建GeoHash则返回中心点
        WGS84Point wgs84Point = geoHash7.getPoint();
        // wgs84Point：(39.90989685058594,116.39671325683594)
        System.out.println("wgs84Point：" + wgs84Point);

        // getBoundingBox()：获取GeoHash的边界框BoundingBox（分隔的网格或者经过指定切分次数后点所在的网格）
        BoundingBox boundingBox = geoHash7.getBoundingBox();
        // getLowerRight()：获取由minLat, maxLon组成的BoundingBox的右侧纬度最小的点（右下角）
        WGS84Point lowerRight = boundingBox.getLowerRight();
        // lowerRight：(39.909210205078125,116.39739990234375)
        System.out.println("lowerRight：" + lowerRight);
        // getUpperLeft()：获取由maxLat, minLon组成的BoundingBox的左侧纬度最高的点（左上角）
        WGS84Point upperLeft = boundingBox.getUpperLeft();
        // upperLeft：(39.91058349609375,116.39602661132812)
        System.out.println("upperLeft：" + upperLeft);
        // getCenterPoint()：获取指定边界框BoundingBox的中心的点的经纬度
        WGS84Point centerPoint = boundingBox.getCenterPoint();
        // centerPoint：(39.90989685058594,116.39671325683594)
        System.out.println("centerPoint：" + centerPoint);
        // 获取指定边界框BoundingBox的最小最大经纬度
        double minLon = boundingBox.getMinLon();
        double minLat = boundingBox.getMinLat();
        double maxLon = boundingBox.getMaxLon();
        double maxLat = boundingBox.getMaxLat();
        double latitudeSize = boundingBox.getLatitudeSize();
        double longitudeSize = boundingBox.getLongitudeSize();
    }
}
