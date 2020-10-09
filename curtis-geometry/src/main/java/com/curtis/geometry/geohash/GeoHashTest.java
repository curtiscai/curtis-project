package com.curtis.geometry.geohash;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import org.junit.Test;

/**
 * @author curtis
 * @desc
 * @date 2020-09-22
 * @email 397773935@qq.com
 * @reference
 */
public class GeoHashTest {

    /**
     * https://github.com/kungfoo/geohash-java
     * <p>
     * 佐证：
     * https://www.benhup.com/tools/convert-coordinates/
     * https://www.dcode.fr/geohash-coordinates
     */
    @Test
    public void test() {
        // 经度lng、纬度lat
        // 测试：118.791281,32.055876 -> wtsqr5t
        GeoHash geoHash = GeoHash.fromGeohashString("wtsqr5t");

        // 经纬度使用双精度浮点数存储
        WGS84Point point = geoHash.getPoint();
        // (32.05604553222656,118.79173278808594)
        System.out.println(point.toString());

        BoundingBox boundingBox = geoHash.getBoundingBox();
        double minLat = boundingBox.getMinLat();
        double minLon = boundingBox.getMinLon();
        double maxLat = boundingBox.getMaxLat();
        double maxLon = boundingBox.getMaxLon();

        WGS84Point centerPoint = boundingBox.getCenterPoint();
        double latitude = centerPoint.getLatitude();
        double longitude = centerPoint.getLongitude();

        WGS84Point boundingBoxCenterPoint = geoHash.getBoundingBoxCenterPoint();
        System.out.println(boundingBoxCenterPoint);
        System.out.println(geoHash.getBoundingBox());
        System.out.println(geoHash.getBoundingBoxCenterPoint());

        // 1110011001110001011010111001011100100000000000000000000000000000 -> (32.056732177734375,118.79104614257812) -> (32.05535888671875,118.79241943359375) -> wtsqr5t
        System.out.println(geoHash.toString());
        double d1 = (32.05535888671875 + 32.056732177734375) / 2;
        double d2 = (118.79104614257812 + 118.79241943359375) / 2;
        System.out.println(d1);
        System.out.println(d2);
//        GeoHash.

    }
}
