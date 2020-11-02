# 0 内容提要

## 0.1 主要内容

1. 空间数据Geometry和WKT之间的相互转换。

2. 数据库空间函数的使用-geometry属性函数。
3. 数据库空间函数的使用-计算两个geometry的交并补差集以及buffer。
4. 数据库空间函数的使用-计算两个geometry的包含、相交、接触等关系信息。
5. 空间数据Geometry和GeoJSON之间的相互转换。
6. 空间数据Geometry和GeoHash之间的相互转换。

​		需要说明的是，很多函数在MySQL 5.7.6已经标记为过期，对于标记过期的空间函数未来随时可能在某个版本移除，所以这里直接不再提及。另外很重要的一点MySQL 5.7中很多重要函数不支持SRID 2346，比如用于判断Geometry有效的函数、返回校验后的Geometry的函数等，还有无法计算地理周长和面积的函数。这些问题在MySQL 8中已经解决，所以建议如果存储空间坐标最好还是使用MySQL 8。

## 0.2 测试环境

DataGrip 2020.2.3
Build #DB-202.7319.56, built on September 15, 2020

MySQL 5.7.27

## 0.3 内容参考

MySQL 5.7 Reference Manual

https://dev.mysql.com/doc/refman/5.7/en/spatial-analysis-functions.html



# 1 Spatial Data Types & Well-Known Text (WKT) Format

​		开放地理空间协会（Open Geospatial Consortium，缩写OGC），又译为**开放地理空间联盟**，起源于1994年，是一个国际化的、自愿协商的标准化组织。该组织由世界各地的商业组织、政府机构、非盈利组织和研究性机构组成，致力于制定地理信息的开放式标准。OGC发布了地理信息的OpenGIS实施标准。

​		OpenGIS规范提供了Backus-Naur语法，该语法指定了编写WKT的正式生产规则。

## 1.1 关于ST_GeomFromText(wkt[, srid])函数

​		ST_GeomFromText(wkt[, srid])不指定srid参数时相当于srid是0，即笛卡尔坐标系，srid为4326时指WGS84大地坐标系。

```mysql
-- ST_GeomFromText(wkt[, srid])不指定srid参数时相当于srid是0，即平面坐标系
SELECT ST_GeomFromText('POINT(116.418896 40.186997)');
SELECT ST_SRID(ST_GeomFromText('POINT(116.418896 40.186997)')); -- 0
SELECT ST_GeomFromText('POINT(116.418896 40.186997)',0);
-- srid为4326时指WGS84大地坐标系
SELECT ST_GeomFromText('POINT(116.418896 40.186997)',4326);
SELECT ST_SRID(ST_GeomFromText('POINT(116.418896 40.186997)',4326)); -- 4326
```

## 1.2 使用WKT字符串构建各类空间函数

### 1.2.1 Point

```sql
-- POINT - 笛卡尔坐标系
SELECT ST_GeomFromText('POINT(0 0)');
-- POINT - 大地坐标系 - 以北京市中心点为例
SELECT ST_GeomFromText('POINT(116.418896 40.186997)',4326);
SELECT ST_AsWKT(ST_GeomFromText('POINT(116.418896 40.186997)',4326));
```

### 1.2.2 MultiPoint

```sql
-- MULTIPOINT - 笛卡尔坐标系 - MULTIPOINT可以由一个点组成
SELECT ST_GeomFromText('MULTIPOINT((0 0))');
SELECT ST_GeomFromText('MULTIPOINT(0 0,1 0)');
SELECT ST_GeomFromText('MULTIPOINT((0 0),(1 0))');
-- MULTIPOINT - 大地坐标系 - 以北京最小外包矩形四个顶点为例 Minimum bounding rectangle (MBR)
SELECT ST_GeomFromText('MULTIPOINT((115.423411 39.442758),(117.514583 39.442758),(117.514583 41.0608),(115.423411 41.0608))',4326);
SELECT ST_AsWKT(ST_GeomFromText('MULTIPOINT((115.423411 39.442758),(117.514583 39.442758),(117.514583 41.0608),(115.423411 41.0608))',4326));

```

### 1.2.3 LineString

```sql
-- LINESTRING - 笛卡尔坐标系 - LINESTRING至少由两个点组成
SELECT ST_GeomFromText('LINESTRING(0 0,1 0)');
-- LINESTRING - 大地坐标系 - 以北京最小外包矩形为例 Minimum bounding rectangle (MBR)
SELECT ST_GeomFromText('LINESTRING(115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758)',4326);
SELECT ST_AsWKT(ST_GeomFromText('LINESTRING(115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758)',4326));
```

### 1.2.4 MultiLineString

```sql
-- MULTILINESTRING - 笛卡尔坐标系 - MULTILINESTRING至少有一个线（两个点）组成
SELECT ST_GeomFromText('MULTILINESTRING((0 0,1 0))');
SELECT ST_GeomFromText('MULTILINESTRING((0 0,1 0),(1 1,0 1))');
-- MULTILINESTRING - 大地坐标系 - 以北京最小外包矩形四条线为例 Minimum bounding rectangle (MBR)
SELECT ST_GeomFromText('MULTILINESTRING((115.423411 39.442758,117.514583 39.442758),(117.514583 39.442758,117.514583 41.0608),
                            (117.514583 41.0608,115.423411 41.0608),(115.423411 41.0608,115.423411 39.442758))',4326);
SELECT ST_AsWKT(ST_GeomFromText('MULTILINESTRING((115.423411 39.442758,117.514583 39.442758),(117.514583 39.442758,117.514583 41.0608),
                            (117.514583 41.0608,115.423411 41.0608),(115.423411 41.0608,115.423411 39.442758))',4326));
```

### 1.2.5 Polygon

​		**POLYGON至少由三个点组成，并且首尾点相同，也就是说实际上是至少四个坐标点（首尾相同）。POLYGON可以包含一个外环和多个内洞，三维数组除第一个二维数组是外环外，其他二维数组都是内洞。外环还是内洞与圈是顺时针围成还是逆时针围成无关。**

```sql
-- POLYGON - 平面坐标系 - POLYGON至少由三个点组成，并且首尾点相同，也就是说实际上是至少四个坐标点（首尾相同）
SELECT ST_GeomFromText('POLYGON((0 0,10 0,10 10,0 0))');
-- POLYGON - 平面坐标系 - POLYGON可以包含一个外环和多个内洞，三维数组除第一个二维数组是外环外，其他二维数组都是内洞
-- 外环还是内洞与圈是顺时针围成还是逆时针围成无关
SELECT ST_GeomFromText('POLYGON((-5 -5,5 -5,5 5,-5 5,-5 -5))');
SELECT ST_GeomFromText('POLYGON((-5 -5,5 -5,5 5,-5 5,-5 -5),(-1 -1,-1 -3,-3 -3,-3 -1,-1 -1))');
SELECT ST_GeomFromText('POLYGON((-5 -5,5 -5,5 5,-5 5,-5 -5),(-1 -1,-1 -3,-3 -3,-3 -1,-1 -1),(1 1,1 3,3 3,3 1,1 1))');
-- POLYGON - 大地坐标系 - 以北京最小外包矩形围成的面为例 Minimum bounding rectangle (MBR)
SELECT ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))',4326);
SELECT ST_AsWKT(ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))',4326));
-- POLYGON - 大地坐标系 - 面内挖经纬度各缩小0.5经纬度的洞
SELECT ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758),
                                (115.923411 39.942758,117.014583 39.942758,117.014583 40.5608,115.923411 40.5608,115.923411 39.942758))',4326);
```

### 1.2.6 MultiPolygon

```java
-- MULTIPOLYGON - 笛卡尔坐标系 - MULTIPOLYGON至少由一个面组成
SELECT ST_GeomFromText('MULTIPOLYGON(((0 0,10 0,10 10,0 0)))');
SELECT ST_GeomFromText('MULTIPOLYGON(((-1 -1,-1 -3,-3 -3,-3 -1,-1 -1)),((1 1,1 3,3 3,3 1,1 1)))');
SELECT ST_GeomFromText('MULTIPOLYGON(((-1 -1,-1 -3,-3 -3,-3 -1,-1 -1)),((0 0,5 0,5 5,0 5,0 0),(1 1,1 3,3 3,3 1,1 1)))');
```

### 1.2.7 GeometryCollection

```sql
-- GEOMETRYCOLLECTION - 笛卡尔坐标系 - 可由任意Geometry组成，不限类型不限个数
SELECT ST_GeomFromText('GEOMETRYCOLLECTION(POINT(0 0))');
SELECT ST_GeomFromText('GEOMETRYCOLLECTION(POINT(0 0), LINESTRING(1 -1,1 1), POLYGON((-2 -2,2 -2,2 2,-2 2,-2 -2)))');
```

### 1.2.8 关于WKT文本的官方说明

​		It is permitted to insert, select, and update geometrically invalid geometries, but they must be syntactically well-formed. Due to the computational expense, MySQL does not check explicitly for geometric validity. Spatial computations may detect some cases of invalid geometries and raise an error, but they may also return an undefined result without detecting the invalidity. Applications that require geometically valid geometries should check them using the ST_IsValid() function.

​		**MySQL使用WKT格式字符串构建Geometry时要求语法必须正确，但是是允许检索、插入、更新无效的Geometry。由于计算量大，MySQL不会明确检查Geometry的有效性，但是在进行相关计算（调用相关空间函数）时可能会因为无效的Geometry而引发错误。所以最好使用ST_IsValid（）来进行检查。**

# 2 Geometry Property Functions

## 2.1 General Geometry Property Functions

### 2.1.1 ST_Envelope(g)

​		返回指定geometry的最小矩形边界（最小外包矩形minimum bounding rectangle (MBR) ）组成的面Polygon。MBR的WKT格式如下：

```
POLYGON((MINX MINY, MAXX MINY, MAXX MAXY, MINX MAXY, MINX MINY))
```

​		在MySQL 5.7.6以上对于点和水平垂直线段，ST_Envelope()函数直接返回点或者线段本身作为其MBR。

```sql
-- ST_Envelope(g) - 返回指定geometry的最小矩形边界（最小外包矩形minimum bounding rectangle (MBR) ）组成的面Polygon。
-- 这里以北京最大最小经纬度组成的对角线为例。
SELECT ST_Envelope(ST_GeomFromText('LINESTRING(115.423411 39.442758,117.514583 41.0608)',4326));
-- POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))
SELECT ST_AsWKT(ST_Envelope(ST_GeomFromText('LINESTRING(115.423411 39.442758,117.514583 41.0608)',4326)));
-- 在MySQL 5.7.6以上对于点和水平垂直线段，ST_Envelope()函数直接返回点或者线段本身作为其MBR。
SELECT ST_AsWKT(ST_Envelope(ST_GeomFromText('POINT(115.423411 39.442758)',4326)));
SELECT ST_AsWKT(ST_Envelope(ST_GeomFromText('LINESTRING(115.423411 39.442758,117.514583 39.442758)',4326)));
```

### 2.1.2 ST_GeometryType(g)

​		返回指定geometry的空间数据类型（大写字符串形式）。

```sql
-- ST_GeometryType(g) - 返回指定geometry的空间数据类型（大写字符串形式）。
-- POINT
SELECT ST_GeometryType(ST_GeomFromText('POINt(116.418896 40.186997)',4326));
-- POLYGON
SELECT ST_GeometryType(ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))',4326));
```

### 2.1.3 ST_SRID(g)

​		返回指定geometry的空间引用标识符。

```sql
-- ST_SRID(g) - 返回指定geometry的空间引用标识符。
SELECT ST_SRID(ST_GeomFromText('POINt(0 0)')); -- 0
SELECT ST_SRID(ST_GeomFromText('POINt(0 0)',0)); -- 0
SELECT ST_SRID(ST_GeomFromText('POINt(116.418896 40.186997)',4326)); -- 4326
```

## 2.2 Point Property Functions

### 2.2.1 ST_X(p)和ST_Y(p)

​		ST_X(p)和ST_Y(p)分别用于获取点Point的X和Y坐标（经纬度）。

```sql
SELECT ST_X(ST_GeomFromText('POINt(116.418896 40.186997)',4326)); -- 116.418896
SELECT ST_Y(ST_GeomFromText('POINt(116.418896 40.186997)',4326)); -- 40.186997
```

## 2.3 LineString and MultiLineString Property Functions

### 2.3.1 ST_IsClosed(ls)

​		对于LineString来说如果线是封闭的（its ST_StartPoint() and ST_EndPoint() values are the same）返回1，否则返回0.如果LineString本身是 NULL或者empty geometry则返回NULL。

​		对于MultiLineString来说只有每个LineString都是封闭的才返回1，否则返回0或者NULL。

```sql
-- ST_IsClosed(ls) - 对于LineString来说如果线是封闭的则返回1，否则返回0.如果LineString本身是 NULL或者empty geometry则返回NULL。
-- 对于MultiLineString来说只有每个LineString都是封闭的才返回1，否则返回0或者NULL。
SELECT ST_GeomFromText('LINESTRING(0 0,1 0,1 1,0 1,0 0)'); -- 1
SELECT ST_IsClosed(ST_GeomFromText('LINESTRING(0 0,1 0,1 1,0 1,0 0)'));
SELECT ST_GeomFromText('LINESTRING(0 0,1 0,1 1,0 1)');
SELECT ST_IsClosed(ST_GeomFromText('LINESTRING(0 0,1 0,1 1,0 1)')); -- 0
-- 注意ST_IsClosed(ls)要求线是封闭的，封闭的条件是首尾点相同，如果线是封闭的但是首尾点不同则返回0而不是1.
SELECT ST_GeomFromText('LINESTRING(0 0,1 0,1 1,0 1,0 0,1 0)');
SELECT ST_IsClosed(ST_GeomFromText('LINESTRING(0 0,1 0,1 1,0 1,0 0,1 0)')); -- 0
SELECT ST_GeomFromText('LINESTRING(115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758)',4326);
SELECT ST_IsClosed(ST_GeomFromText('LINESTRING(115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758)',4326)); -- 1
```

### 2.3.2 ST_Length(ls)

​		返回指定LineString或者MultiLineString的长度，MySQL 5.7尚未实现空间地理数据的长度计算。MySQL 8.0.16+已经支持。

```sql
-- ST_Length(ls) - 返回指定LineString或者MultiLineString的长度，MySQL 5.7尚未实现空间地理数据的长度计算。
SELECT ST_GeomFromText('LINESTRING(0 0,1 0,1 1,0 1,0 0)');
-- 4
SELECT ST_Length(ST_GeomFromText('LINESTRING(0 0,1 0,1 1,0 1,0 0)')); 
SELECT ST_GeomFromText('LINESTRING(115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758)',4326);
-- 7.418428000000006
SELECT ST_Length(ST_GeomFromText('LINESTRING(115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758)',4326));
```

## 2.4 Polygon and MultiPolygon Property Functions

### 2.4.1 ST_Area({poly|mpoly})

​		返回指定Polygon或者MultiPolygon的面积，MySQL 5.7尚未实现空间地理数据的面积计算。MySQL 8.0.13+已经支持。

```sql
-- ST_Area({poly|mpoly}) - 返回指定Polygon或者MultiPolygon的面积，MySQL 5.7尚未实现空间地理数据的面积计算。
SELECT ST_GeomFromText('POLYGON((0 0,1 0,1 1,0 1,0 0))');
-- 1
SELECT ST_Area(ST_GeomFromText('POLYGON((0 0,1 0,1 1,0 1,0 0))'));
SELECT ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))',4326);
-- 3.383604125223428
SELECT ST_Area(ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))',4326));
```

### 2.4.2 ST_Centroid({poly|mpoly})

​		返回指定Polygon或者MultiPolygon的几何中心（形心）。结果是类型为Point的Geometry。

```java
-- ST_Centroid({poly|mpoly}) - 返回指定Polygon或者MultiPolygon的几何中心（形心）。结果以POINT的形式返回。
SELECT ST_GeomFromText('POLYGON((0 0,1 0,1 1,0 1,0 0))');
-- POINT(0.5 0.5)
SELECT ST_AsWKT(ST_Centroid(ST_GeomFromText('POLYGON((0 0,1 0,1 1,0 1,0 0))')));
SELECT ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))',4326);
-- POINT(116.468997 40.251779)
SELECT ST_AsWKT(ST_Centroid(ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))',4326)));
```

### 2.4.3 ST_ExteriorRing(poly)

​		返回指定Polygon的外环。结果是类型为LineString的Geometry。

```sql
-- ST_ExteriorRing(poly) - 返回指定Polygon的外环。结果是类型为LineString的Geometry。
SELECT ST_GeomFromText('POLYGON((-5 -5,5 -5,5 5,-5 5,-5 -5),(-1 -1,-1 -3,-3 -3,-3 -1,-1 -1))');
SELECT ST_ExteriorRing(ST_GeomFromText('POLYGON((-5 -5,5 -5,5 5,-5 5,-5 -5),(-1 -1,-1 -3,-3 -3,-3 -1,-1 -1))'));
-- LINESTRING(-5 -5,5 -5,5 5,-5 5,-5 -5)
SELECT ST_AsWKT(ST_ExteriorRing(ST_GeomFromText('POLYGON((-5 -5,5 -5,5 5,-5 5,-5 -5),(-1 -1,-1 -3,-3 -3,-3 -1,-1 -1))')));
SELECT ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))',4326);
SELECT ST_ExteriorRing(ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))',4326));
-- LINESTRING(115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758)
SELECT ST_AsWKT(ST_ExteriorRing(ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))',4326)));
```

### 2.4.4 ST_NumInteriorRing(poly)

​		返回指定Polygon的内洞数量，可用于找到有内洞的Polygon。

```sql
-- ST_NumInteriorRing(poly) - 返回指定Polygon的内洞数量，可用于找到有内洞的Polygon
SELECT ST_GeomFromText('POLYGON((-5 -5,5 -5,5 5,-5 5,-5 -5),(-1 -1,-1 -3,-3 -3,-3 -1,-1 -1))');
-- 1 - 1个内洞
SELECT ST_NumInteriorRings(ST_GeomFromText('POLYGON((-5 -5,5 -5,5 5,-5 5,-5 -5),(-1 -1,-1 -3,-3 -3,-3 -1,-1 -1))'));
SELECT ST_GeomFromText('POLYGON((-5 -5,5 -5,5 5,-5 5,-5 -5),(-1 -1,-1 -3,-3 -3,-3 -1,-1 -1),(1 1,1 3,3 3,3 1,1 1))');
-- 2 - 2个内洞
SELECT ST_NumInteriorRings(ST_GeomFromText('POLYGON((-5 -5,5 -5,5 5,-5 5,-5 -5),(-1 -1,-1 -3,-3 -3,-3 -1,-1 -1),(1 1,1 3,3 3,3 1,1 1))'));
```

# 3 Spatial Operator Functions && Spatial Relation Functions

## 3.1 Spatial Operator Functions

### 3.1.1 ST_Buffer(g, d[, strategy1[, strategy2[, strategy3]]])

​		获取距离指定geometry的所有点的距离都小于等于指定距离的geometry。**geometry的SRID必须是0，因为在MySQL 5.7仅支持笛卡尔坐标系。但是换句话说，如果所有的空间地理坐标都以笛卡尔坐标系中构建，那么对于关系的计算是不受任何影响的。**

```java
-- ST_Buffer(g, d[, strategy1[, strategy2[, strategy3]]]) - 获取距离指定geometry的所有点的距离都小于等于指定距离的geometry。
-- geometry的SRID必须是0，因为在MySQL 5.7仅支持笛卡尔坐标系。
SELECT ST_GeomFromText('POINT(0 0)');
SELECT ST_Buffer(ST_GeomFromText('POINT(0 0)'),1);
-- POLYGON((1 0,0.9807852804032312 0.19509032201612436,0.9238795325112882 0.3826834323650863,0.8314696123025472 0.5555702330195993,0.7071067811865499 0.7071067811865451,0.5555702330196048 0.8314696123025436,0.3826834323650925 0.9238795325112856,0.1950903220161309 0.9807852804032299,2.4808382392282726e-15 1,-0.19509032201612606 0.9807852804032309,-0.38268343236508784 0.9238795325112875,-0.5555702330196007 0.8314696123025462,-0.7071067811865464 0.7071067811865486,-0.8314696123025445 0.5555702330196034,-0.9238795325112863 0.3826834323650909,-0.9807852804032302 0.19509032201612925,-1 7.657137397853899e-16,-0.9807852804032305 -0.19509032201612772,-0.9238795325112868 -0.38268343236508945,-0.8314696123025453 -0.5555702330196022,-0.7071067811865475 -0.7071067811865476,-0.555570233019602 -0.8314696123025455,-0.3826834323650897 -0.9238795325112867,-0.1950903220161282 -0.9807852804032304,6.123233995736766e-17 -1,0.1950903220161283 -0.9807852804032304,0.38268343236508984 -0.9238795325112867,0.5555702330196023 -0.8314696123025451,0.7071067811865476 -0.7071067811865475,0.8314696123025452 -0.5555702330196022,0.9238795325112867 -0.3826834323650898,0.9807852804032304 -0.19509032201612825,1 0))
SELECT ST_AsWKT(ST_Buffer(ST_GeomFromText('POINT(0 0)'),1));
SELECT ST_GeomFromText('POLYGON((0 0,1 0,1 1,0 1,0 0))');
SELECT ST_Buffer(ST_GeomFromText('POLYGON((0 0,1 0,1 1,0 1,0 0))'),1);
-- POLYGON((-1 0,-0.9807852804032305 -0.19509032201612772,-0.9238795325112868 -0.38268343236508945,-0.8314696123025453 -0.5555702330196022,-0.7071067811865475 -0.7071067811865476,-0.555570233019602 -0.8314696123025455,-0.3826834323650897 -0.9238795325112867,-0.1950903220161282 -0.9807852804032304,0 -1,1 -1,1.1950903220161284 -0.9807852804032304,1.3826834323650898 -0.9238795325112867,1.5555702330196022 -0.8314696123025451,1.7071067811865475 -0.7071067811865475,1.8314696123025453 -0.5555702330196022,1.9238795325112867 -0.3826834323650898,1.9807852804032304 -0.19509032201612825,2 0,2 1,1.9807852804032304 1.1950903220161282,1.9238795325112867 1.3826834323650898,1.8314696123025453 1.5555702330196022,1.7071067811865475 1.7071067811865475,1.5555702330196022 1.8314696123025451,1.3826834323650898 1.9238795325112867,1.1950903220161284 1.9807852804032304,1 2,0 2,-0.1950903220161271 1.9807852804032307,-0.38268343236508867 1.9238795325112872,-0.5555702330196012 1.8314696123025458,-0.7071067811865468 1.7071067811865483,-0.8314696123025448 1.5555702330196028,-0.9238795325112865 1.3826834323650903,-0.9807852804032304 1.1950903220161286,-1 1,-1 0))
SELECT ST_AsWKT(ST_Buffer(ST_GeomFromText('POLYGON((0 0,1 0,1 1,0 1,0 0))'),1));
```

### 3.1.2 ST_Intersection(g1, g2)

​		获取两个geometry的交集构成的geometry。

```sql
SELECT ST_GeomFromText('POLYGON((0 0,2 0,2 2,0 2,0 0))')
UNION
SELECT ST_GeomFromText('POLYGON((1 0,3 0,3 2,1 2,1 0))');

SET @geom1 = ST_GeomFromText('POLYGON((0 0,2 0,2 2,0 2,0 0))');
SET @geom2 = ST_GeomFromText('POLYGON((1 0,3 0,3 2,1 2,1 0))');
SELECT ST_Intersection(@geom1,@geom2);
-- POLYGON((1 2,1 0,2 0,2 2,1 2))
SELECT ST_AsWKT(ST_Intersection(@geom1,@geom2));
```

### 3.1.3 ST_Union(g1, g2)

​		获取两个geometry的并集构成的geometry。

```sql
-- ST_Intersection(g1, g2) - 获取两个geometry的交集构成的geometry。
SELECT ST_GeomFromText('POLYGON((-2 0,1 0,1 3,-2 3,-2 0))')
UNION
SELECT ST_GeomFromText('POLYGON((-1 0,2 0,2 3,-1 3,-1 0))');

SET @geom1 = ST_GeomFromText('POLYGON((-2 0,1 0,1 3,-2 3,-2 0))');
SET @geom2 = ST_GeomFromText('POLYGON((-1 0,2 0,2 3,-1 3,-1 0))');
SELECT ST_Intersection(@geom1,@geom2);
-- POLYGON((-1 3,-1 0,1 0,1 3,-1 3))
SELECT ST_AsWKT(ST_Intersection(@geom1,@geom2));
```

### 3.1.4 ST_Difference(g1, g2)

​		获取两个geometry的差集构成的geometry。在g1内但是不在g2内的点集构成的geometry。

```sql
-- ST_Difference(g1, g2) - 获取两个geometry的差集构成的geometry。在g1内但是不在g2内的点集构成的geometry。
SELECT ST_GeomFromText('POLYGON((-2 0,1 0,1 3,-2 3,-2 0))')
UNION
SELECT ST_GeomFromText('POLYGON((-1 0,2 0,2 3,-1 3,-1 0))');

SET @geom1 = ST_GeomFromText('POLYGON((-2 0,1 0,1 3,-2 3,-2 0))');
SET @geom2 = ST_GeomFromText('POLYGON((-1 0,2 0,2 3,-1 3,-1 0))');
SELECT ST_Difference(@geom1,@geom2);
-- POLYGON((-1 3,-2 3,-2 0,-1 0,-1 3))
SELECT ST_AsWKT(ST_Difference(@geom1,@geom2));
```

### 3.1.5 ST_SymDifference(g1, g2)

​		获取在g1内但是不在g2内，在g2内但是不在g1内的点集构成的geometry。

```sql
-- ST_SymDifference(g1, g2) - 获取在g1内但是不在g2内，在g2内但是不在g1内的点集构成的geometry。
SELECT ST_GeomFromText('POLYGON((-2 0,1 0,1 3,-2 3,-2 0))')
UNION
SELECT ST_GeomFromText('POLYGON((-1 0,2 0,2 3,-1 3,-1 0))');

SET @geom1 = ST_GeomFromText('POLYGON((-2 0,1 0,1 3,-2 3,-2 0))');
SET @geom2 = ST_GeomFromText('POLYGON((-1 0,2 0,2 3,-1 3,-1 0))');
SELECT ST_SymDifference(@geom1,@geom2);
-- MULTIPOLYGON(((-1 3,-2 3,-2 0,-1 0,-1 3)),((1 3,1 0,2 0,2 3,1 3)))
SELECT ST_AsWKT(ST_SymDifference(@geom1,@geom2));
```

## 3.2 Spatial Relation Functions

### 3.2.1 ST_Contains(g1, g2)  && ST_Within(g1, g2)

​		contains方法和within方法都是计算包含关系，如果A.contains(B)=true成立则B.within(A)=true一定成立，反之亦然。contains方法表示包含关系，**A.contains(B)=true需要满足两个条件：B的点都在A的内部或者边界上；B至少有一个点在A的内部。**

 		within方法也表示包含关系，不过A.within(B)=true表示的是B包含A或者A在B的内部。

```sql
-- ST_Contains(g1, g2)  && ST_Within(g1, g2)
-- contains方法和within方法都是计算包含关系，如果A.contains(B)=true成立则B.within(A)=true一定成立，反之亦然。
-- contains方法表示包含关系，A.contains(B)=true需要满足两个条件：B的点都在A的内部或者边界上；B至少有一个点在A的内部。
SET @geom1 = ST_GeomFromText('POLYGON((0 0, 0 3, 3 3, 3 0, 0 0))');
SET @geom2 = ST_GeomFromText('POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))');
SELECT ST_Contains(@geom1,@geom2); -- 1
SELECT ST_Contains(@geom2,@geom1); -- 0
SELECT ST_Within(@geom1,@geom2); -- 0
SELECT ST_Within(@geom2,@geom1); -- 1
```

### 3.2.2 ST_Intersects(g1, g2) && ST_Disjoint(g1, g2)

​		intersects方法用于计算相交关系，表示相交，disjoint正好相反表示不相交，如果A.intersects(B)=true成立则A.disjoint(B)=false，反之亦然。**intersects方法用于表示两个Geometry相交，A.intersects(B)=true只需要满足一个条件：A和B至少有一个公共的点。 disjoint方法用于表示两个Geometry不想交，A.disjoint(B)=true只需要满足一个条件：A和B没有任何公共的点。**

```sql
-- ST_Intersects(g1, g2) & ST_Disjoint(g1, g2)
-- intersects方法用于计算相交关系，表示相交，disjoint正好相反表示不相交，如果A.intersects(B)=true成立则A.disjoint(B)=false，反之亦然。
-- intersects方法用于表示两个Geometry相交，A.intersects(B)=true只需要满足一个条件：A和B至少有一个公共的点。
-- disjoint方法用于表示两个Geometry不想交，A.disjoint(B)=true只需要满足一个条件：A和B没有任何公共的点。
SET @geom1 = ST_GeomFromText('POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))');
SET @geom2 = ST_GeomFromText('POLYGON((3 0, 6 0, 6 3, 3 3, 3 0))');
SELECT ST_Intersects(@geom1,@geom2); -- 1
SELECT ST_Disjoint(@geom2,@geom1); -- 0
```

### 3.2.3 ST_Touches(g1, g2)

​		touches方法用于计算接触关系，A.touches(B)=true只需要满足一个条件：A和B的公共点均只在边界上，内部无公共点集。

```sql
-- ST_Touches(g1, g2)
-- touches方法用于计算接触关系，A.touches(B)=true只需要满足一个条件：A和B的公共点均只在边界上，内部无公共点集。
SET @geom1 = ST_GeomFromText('POLYGON((0 0, 3 0, 3 3, 0 3, 0 0))');
SET @geom2 = ST_GeomFromText('POLYGON((3 0, 6 0, 6 3, 3 3, 3 0))');
SELECT ST_Touches(@geom1,@geom2); -- 1
```

# 3 Spatial GeoJSON Functions

​		MySQL支持GeoJSON和Geometry和GeometryCollection之间互转，尚不支持Feature和FeatureCollection转换，只是可以从中提取Geometry。

## 3.1 ST_AsGeoJSON(g [, max_dec_digits [, options]])

​		ST_AsGeoJSON用于将Geometry转换为GeoJSON。如果Geometry是NULL，则返回NULL。如果Geometry不可用则直接报错。

​		可选参数max_dec_digits用于限定坐标小数位数，不指定则原样输出。

​		可选参数options用于指定生成GeoJSON时增加额外信息，不过options对于SRID为0的geometry是无效的。options为1时指定在输出GeoJSON时增加bbox节点用于存储边界框坐标信息。options为2时指定增加CRS节点用于存储坐标引用系统信息。

```sql
-- ST_AsGeoJSON(g [, max_dec_digits [, options]])
-- ST_AsGeoJSON用于将Geometry转换为GeoJSON。如果Geometry是NULL，则返回NULL。如果Geometry不可用则直接报错。
-- 可选参数max_dec_digits用于限定坐标小数位数，不指定则原样输出。
-- 可选参数options用于指定生成GeoJSON时增加额外信息，不过options对于SRID为0的geometry是无效的。
-- options为1时指定在输出GeoJSON时增加bbox节点用于存储边界框坐标信息。options为2时指定增加CRS节点用于存储坐标引用系统信息。
SELECT ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))',4326);
-- ST_AsGeoJSON(g) - 无参，用于直接将Geometry转换为GeoJSON，坐标位数使用构建Geometry时的坐标小数位数，无可选参数表示只生成必须的type和coordinates节点
-- {"type": "Polygon", "coordinates": [[[115.423411, 39.442758], [117.514583, 39.442758], [117.514583, 41.0608], [115.423411, 41.0608], [115.423411, 39.442758]]]}
SELECT ST_AsGeoJSON(ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))',4326));

-- ST_AsGeoJSON(g,max_dec_digits) - 限定坐标最大小数位数，构建GeoJSON时如果坐标位数大于限定位数则四舍五入，无可选参数表示只生成必须的type和coordinates节点
-- {"type": "Polygon", "coordinates": [[[115.423, 39.443], [117.515, 39.443], [117.515, 41.061], [115.423, 41.061], [115.423, 39.443]]]}
SELECT ST_AsGeoJSON(ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))',4326),3);
-- {"type": "Polygon", "coordinates": [[[115.423411, 39.442758], [117.514583, 39.442758], [117.514583, 41.0608], [115.423411, 41.0608], [115.423411, 39.442758]]]}
SELECT ST_AsGeoJSON(ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))',4326),8);

-- ST_AsGeoJSON(g,max_dec_digits,1) - 限定坐标最大小数位数，构建GeoJSON时如果坐标位数大于限定位数则四舍五入，可选参数1表示在生成GeoJSON时增加bbox节点用于存储边界框坐标信息。
-- {"bbox": [115.423411, 39.442758, 117.514583, 41.0608], "type": "Polygon", "coordinates": [[[115.423411, 39.442758], [117.514583, 39.442758], [117.514583, 41.0608], [115.423411, 41.0608], [115.423411, 39.442758]]]}
SELECT ST_AsGeoJSON(ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))',4326),8,1);

-- -- ST_AsGeoJSON(g,max_dec_digits,2) - 限定坐标最大小数位数，构建GeoJSON时如果坐标位数大于限定位数则四舍五入，可选参数2表示在生成GeoJSON时增加CRS节点用于存储坐标引用系统信息。
-- {"crs": {"type": "name", "properties": {"name": "EPSG:4326"}}, "type": "Polygon", "coordinates": [[[115.423411, 39.442758], [117.514583, 39.442758], [117.514583, 41.0608], [115.423411, 41.0608], [115.423411, 39.442758]]]}
SELECT ST_AsGeoJSON(ST_GeomFromText('POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))',4326),8,2);
```

​		**附：实际工作中需要进行坐标系转换，如果获取WKT字符串后再解析未免过于麻烦，小于低，此时可以转换为GeoJSON对象，然后就能够获取相应空间数据类型的多维数组。

## 3.2 ST_GeomFromGeoJSON(str [, options [, srid]])

​		ST_GeomFromGeoJSON用于将GeoJSON字符串转换为Geometry。如果GeoJSON是NULL，则返回NULL。如果GeoJSON不可用则直接报错。

​		可选参数options候选项2、3、4用于指定高维度地理信息。

​		可选参数srid用于指定空间引用标识符，默认SRID为2346。

```sql
-- ST_GeomFromGeoJSON(str [, options [, srid]])
-- ST_GeomFromGeoJSON用于将GeoJSON字符串转换为Geometry。如果GeoJSON是NULL，则返回NULL。如果GeoJSON不可用则直接报错。
-- 可选参数options候选项2、3、4用于指定高维度地理信息。
-- 可选参数srid用于指定空间引用标识符，默认SRID为2346。
SELECT ST_GeomFromGeoJSON('{"type": "Polygon", "coordinates": [[[115.423411, 39.442758], [117.514583, 39.442758], [117.514583, 41.0608], [115.423411, 41.0608], [115.423411, 39.442758]]]}');
-- POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))
SELECT ST_AsWKT(ST_GeomFromGeoJSON('{"type": "Polygon", "coordinates": [[[115.423411, 39.442758], [117.514583, 39.442758], [117.514583, 41.0608], [115.423411, 41.0608], [115.423411, 39.442758]]]}'));

-- 增加额外坐标引用标识符的GeoJSON
SELECT ST_GeomFromGeoJSON('{"crs": {"type": "name", "properties": {"name": "EPSG:4326"}}, "type": "Polygon", "coordinates": [[[115.423411, 39.442758], [117.514583, 39.442758], [117.514583, 41.0608], [115.423411, 41.0608], [115.423411, 39.442758]]]}');
-- POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758))
SELECT ST_AsWKT(ST_GeomFromGeoJSON('{"crs": {"type": "name", "properties": {"name": "EPSG:4326"}}, "type": "Polygon", "coordinates": [[[115.423411, 39.442758], [117.514583, 39.442758], [117.514583, 41.0608], [115.423411, 41.0608], [115.423411, 39.442758]]]}'));

-- ST_GeomFromGeoJSON接收Feature或者FeatureCollection，不过只能提取其中的坐标信息：type和coordinates节点
-- GEOMETRYCOLLECTION(POLYGON((115.423411 39.442758,117.514583 39.442758,117.514583 41.0608,115.423411 41.0608,115.423411 39.442758)))
SELECT ST_GeomFromGeoJSON('{"type":"FeatureCollection","features":[{"type":"Feature","properties":{"adcode":110000,"name":"北京市","center":[116.405285,39.904989],"centroid":[116.419889,40.189911],"childrenNum":16,"level":"province","acroutes":[100000],"parent":{"adcode":100000}},"geometry":{"type":"Polygon","coordinates":[[[115.423411,39.442758],[117.514583,39.442758],[117.514583,41.0608],[115.423411,41.0608],[115.423411,39.442758]]]}}]}');
SELECT ST_AsWKT(ST_GeomFromGeoJSON('{"type":"FeatureCollection","features":[{"type":"Feature","properties":{"adcode":110000,"name":"北京市","center":[116.405285,39.904989],"centroid":[116.419889,40.189911],"childrenNum":16,"level":"province","acroutes":[100000],"parent":{"adcode":100000}},"geometry":{"type":"Polygon","coordinates":[[[115.423411,39.442758],[117.514583,39.442758],[117.514583,41.0608],[115.423411,41.0608],[115.423411,39.442758]]]}}]}'));
```

# 4 Spatial Geohash Functions

## 4.1 ST_GeoHash(longitude,latitude,max_length) & ST_GeoHash(point,max_length)

​		ST_GeoHash用于将任意精度的经纬度坐标转换为文本字符串，该文本字符串仅包含如下字符：0123456789bcdefghjkmnpqrstuvwxyz（32个字符）。

​		使用ST_GeoHash函数生成的字符串不得超过*`max_length`*个长度（上限为100个字符）。该字符串可能比*`max_length`*字符短，这是因为创建geohash值的算法会继续进行，直到创建的字符串可以精确表示位置或*`max_length`* 字符， 以先到者为准。

```sql
-- ST_GeoHash(longitude,latitude,max_length) & ST_GeoHash(point,max_length)
-- ST_GeoHash用于将任意精度的经纬度坐标转换为文本字符串，该文本字符串仅包含如下字符：0123456789bcdefghjkmnpqrstuvwxyz（32个字符）。
SELECT ST_GeomFromText('POINT(116.418896 40.186997)',4326);
-- 第一种写法ST_GeoHash(longitude,latitude,max_length)示例
-- wx4u95v
SELECT ST_GeoHash(116.418896,40.186997,7);
-- wx4u95vd
SELECT ST_GeoHash(116.418896,40.186997,8);

-- 第二种写法ST_GeoHash(point,max_length)示例
-- wx4u95v
SELECT ST_GeoHash(ST_GeomFromText('POINT(116.418896 40.186997)',4326),7);
-- wx4u95vd
SELECT ST_GeoHash(ST_GeomFromText('POINT(116.418896 40.186997)',4326),8);
```

## 4.2 ST_PointFromGeoHash(geohash_str, srid)

​		ST_PointFromGeoHash用于将GeoHash字符串解析成类型为Point的Geometry。自悟（无依据）：理论上GeoHash字符串解析的应该是网格，这里却是点，难道是分隔的网格的中心点？？？

```sql
-- ST_PointFromGeoHash(geohash_str, srid)
-- ST_PointFromGeoHash用于将GeoHash字符串解析成类型为Point的Geometry。
-- 自悟（无依据）：理论上GeoHash字符串解析的应该是网格，这里却是点，难道是分隔的网格的中心点？？？
SELECT ST_PointFromGeoHash('wx4u95v',4326);
-- wx4u95v
SELECT ST_GeoHash(ST_GeomFromText('POINT(116.418896 40.186997)',4326),7);
-- POINT(116.419 40.187)
SELECT ST_AsWKT(ST_PointFromGeoHash('wx4u95v',4326));

-- wx4u95vdhzhw
SELECT ST_GeoHash(ST_GeomFromText('POINT(116.418896 40.186997)',4326),12);
-- POINT(116.418896 40.186997)
SELECT ST_AsWKT(ST_PointFromGeoHash('wx4u95vdhzhw',4326));
```

# 5 Spatial Convenience Functions

## 5.1 ST_Distance_Sphere(g1, g2 [, radius])

​		ST_Distance_Sphere用于返回球体上两个点和/或多点之间的最小球面距离（以米为单位），或者 `NULL`如果任何几何参数为 `NULL`或为空，则返回。

​		计算使用球形地球和可配置的半径。可选*`radius`*参数应以米为单位。如果省略，则默认半径为6,370,986米。在MySQL 8中的ST_Distance函数能够得到和ST_Distance_Sphere一样的效果。

```sql
-- ST_Distance_Sphere(g1, g2 [, radius])
-- ST_Distance_Sphere用于返回球体上两个点和/或多点之间的最小球面距离（以米为单位）。
-- 计算使用球形地球和可配置的半径。可选radius参数应以米为单位。如果省略，则默认半径为6,370,986米。
-- 在MySQL 8中的ST_Distance函数能够得到和ST_Distance_Sphere一样的效果。
-- 85179.62177130273
SELECT ST_Distance_Sphere(ST_GeomFromText('POINT(116 40)',4326),ST_GeomFromText('POINT(115 40)',4326));
-- 111194.68229846361
SELECT ST_Distance_Sphere(ST_GeomFromText('POINT(116 40)',4326),ST_GeomFromText('POINT(116 41)',4326));
```

## 5.2 ST_IsValid(g)

​		ST_IsValid用于判断Geometry是否有效。有效返回1，无效返回0。Geometry是否有效由OGC的规定来定义。ST_IsValid仅适用于笛卡尔坐标系，也就是SRID必须为0，否则抛出异常。

```sql
-- ST_IsValid(g)
-- ST_IsValid用于判断Geometry是否有效。有效返回1，无效返回0。Geometry是否有效由OGC的规定来定义。
-- ST_IsValid仅适用于笛卡尔坐标系，也就是SRID必须为0，否则抛出异常。
-- 1
SELECT ST_IsValid(ST_GeomFromText('POINT(116.418896 40.186997)'));
-- [HY000][1210] Incorrect arguments to st_isvalid
SELECT ST_IsValid(ST_GeomFromText('POINT(116.418896 40.186997)',4326));
-- 由两个相同坐标点组成的线，虽然满足至少两个点，可以构造Geometry，但是是无效的。
SELECT ST_GeomFromText('LINESTRING(0 0,0 0)');
-- 0
SELECT ST_IsValid(ST_GeomFromText('LINESTRING(0 0,0 0)'));
-- 0
SELECT ST_IsValid(ST_GeomFromText('POLYGON((0 0,0 0,0 0,0 0))'));
```

## 5.3 ST_Validate(g)

​		ST_Validate用于根据OGC规范校验Geometry并返回Geometry。格式良好的WKT或者WKB可以通过调用ST_GeomFromText来构成Geometry，但是这个Geometry可能是无效的，无法使用其他空间计算函数。

​		ST_Validate传入一个有效的Geometry则直接返回有效Geometry，如果传入无效的Geometry则返回null。ST_Validate可以过滤掉无效的Geometry，虽然付出一定代价,但是对于需要精确结果这个代价是值得的。

​		ST_Validate传入Polygon或者MultiPolygon时，如果Polygon或者MultiPolygon是有效并且是顺时针环则返回反转的逆时针的环。如果Polygon或者MultiPolygon是有效并且是逆时针环则直接返回。

​		**ST_Validate与ST_IsValid一样仅适用于笛卡尔坐标系，也就是SRID必须为0，否则抛出异常。在MySQL 8具体某个版本之后文档中不再提及要求SRID必须为0。个人认为要想使用MySQL存储空间数据还得使用MySQL 8，在MySQL 5.7还存在很多问题或者不完善的地方**

```sql
-- ST_Validate(g)
-- ST_Validate用于根据OGC规范校验Geometry并返回Geometry。ST_Validate传入一个有效的Geometry则直接返回有效Geometry，如果传入无效的Geometry则返回null。
-- ST_Validate可以过滤掉无效的Geometry，虽然付出一定代价,但是对于需要精确结果这个代价是值得的。
-- ST_Validate传入Polygon或者MultiPolygon时，如果Polygon或者MultiPolygon是有效并且是顺时针环则返回反转的逆时针的环。
-- 如果Polygon或者MultiPolygon是有效并且是逆时针环则直接返回。
SELECT ST_GeomFromText('POLYGON((0 0,1 0,1 1,0 1,0 0))');
SELECT ST_Validate(ST_GeomFromText('POLYGON((0 0,1 0,1 1,0 1,0 0))'));
-- 逆时针有效环原样输出
-- POLYGON((0 0,1 0,1 1,0 1,0 0))
SELECT ST_AsWKT(ST_Validate(ST_GeomFromText('POLYGON((0 0,1 0,1 1,0 1,0 0))')));

SELECT ST_GeomFromText('POLYGON((0 0,0 1,1 1,1 0,0 0))');
SELECT ST_Validate(ST_GeomFromText('POLYGON((0 0,0 1,1 1,1 0,0 0))'));
-- 顺时针无效环，反转为逆时针环输出
-- POLYGON((0 0,1 0,1 1,0 1,0 0))
SELECT ST_AsWKT(ST_Validate(ST_GeomFromText('POLYGON((0 0,0 1,1 1,1 0,0 0))')));

-- 传入无效的Geometry，ST_Validate方法返回null。
SELECT ST_Validate(ST_GeomFromText('POLYGON((0 0,0 0,0 0,0 0))'));

-- ST_Validate与ST_IsValid一样仅适用于笛卡尔坐标系，也就是SRID必须为0，否则抛出异常。
-- [HY000][1210] Incorrect arguments to st_validate
SELECT ST_Validate(ST_GeomFromText('POINT(116.418896 40.186997)',4326));
```



附件：

 [使用WKT构建各类空间类型数据脚本.sql](file/使用WKT构建各类空间类型数据脚本.sql) 

