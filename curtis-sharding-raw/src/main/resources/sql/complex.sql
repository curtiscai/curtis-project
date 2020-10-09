-- MySQL dump 10.13  Distrib 5.7.27, for macos10.14 (x86_64)
--
-- Host: 127.0.0.1    Database: db_test_0
-- ------------------------------------------------------
-- Server version	5.7.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- ------------------------------------------------------
-- The SQL Script Of db_test_0
-- ------------------------------------------------------

--
-- Drop And Create DataBase `db_test_0`
--

DROP DATABASE IF EXISTS db_test_0;
CREATE DATABASE IF NOT EXISTS db_test_0 DEFAULT CHARSET=utf8mb4;
USE db_test_0;

--
-- Table structure for table `tb_enterprise_sale_110100_2019`
--

DROP TABLE IF EXISTS `tb_enterprise_sale_110100_2019`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_enterprise_sale_110100_2019` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `city_code` varchar(20) NOT NULL COMMENT '城市code',
  `enterprise_id` bigint(20) NOT NULL COMMENT '企业id',
  `sale_day` date NOT NULL COMMENT '销售日期',
  `sale_value` decimal(10,0) NOT NULL COMMENT '销售金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='企业日度销售数据（按城市和年份进行分表）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_enterprise_sale_110100_2019`
--

/*!40000 ALTER TABLE `tb_enterprise_sale_110100_2019` DISABLE KEYS */;
INSERT INTO `tb_enterprise_sale_110100_2019` VALUES (1,'110100',10000,'2019-01-01',1000),(2,'110100',10000,'2019-12-31',1000);
/*!40000 ALTER TABLE `tb_enterprise_sale_110100_2019` ENABLE KEYS */;

--
-- Table structure for table `tb_enterprise_sale_110100_2020`
--

DROP TABLE IF EXISTS `tb_enterprise_sale_110100_2020`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_enterprise_sale_110100_2020` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `city_code` varchar(20) NOT NULL COMMENT '城市code',
  `enterprise_id` bigint(20) NOT NULL COMMENT '企业id',
  `sale_day` date NOT NULL COMMENT '销售日期',
  `sale_value` decimal(10,0) NOT NULL COMMENT '销售金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='企业日度销售数据（按城市和年份进行分表）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_enterprise_sale_110100_2020`
--

/*!40000 ALTER TABLE `tb_enterprise_sale_110100_2020` DISABLE KEYS */;
INSERT INTO `tb_enterprise_sale_110100_2020` VALUES (3,'110100',10000,'2020-01-01',1000),(4,'110100',10000,'2020-12-31',1000);
/*!40000 ALTER TABLE `tb_enterprise_sale_110100_2020` ENABLE KEYS */;

--
-- Table structure for table `tb_enterprise_sale_130100_2019`
--

DROP TABLE IF EXISTS `tb_enterprise_sale_130100_2019`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_enterprise_sale_130100_2019` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `city_code` varchar(20) NOT NULL COMMENT '城市code',
  `enterprise_id` bigint(20) NOT NULL COMMENT '企业id',
  `sale_day` date NOT NULL COMMENT '销售日期',
  `sale_value` decimal(10,0) NOT NULL COMMENT '销售金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='企业日度销售数据（按城市和年份进行分表）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_enterprise_sale_130100_2019`
--

/*!40000 ALTER TABLE `tb_enterprise_sale_130100_2019` DISABLE KEYS */;
INSERT INTO `tb_enterprise_sale_130100_2019` VALUES (5,'130100',10000,'2019-01-01',1000),(6,'130100',10000,'2019-12-31',1000);
/*!40000 ALTER TABLE `tb_enterprise_sale_130100_2019` ENABLE KEYS */;

--
-- Table structure for table `tb_enterprise_sale_130100_2020`
--

DROP TABLE IF EXISTS `tb_enterprise_sale_130100_2020`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_enterprise_sale_130100_2020` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `city_code` varchar(20) NOT NULL COMMENT '城市code',
  `enterprise_id` bigint(20) NOT NULL COMMENT '企业id',
  `sale_day` date NOT NULL COMMENT '销售日期',
  `sale_value` decimal(10,0) NOT NULL COMMENT '销售金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='企业日度销售数据（按城市和年份进行分表）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_enterprise_sale_130100_2020`
--

/*!40000 ALTER TABLE `tb_enterprise_sale_130100_2020` DISABLE KEYS */;
INSERT INTO `tb_enterprise_sale_130100_2020` VALUES (7,'130100',10000,'2020-01-01',1000),(8,'130100',10000,'2020-12-31',1000);
/*!40000 ALTER TABLE `tb_enterprise_sale_130100_2020` ENABLE KEYS */;



-- ------------------------------------------------------
-- The SQL Script Of db_test_1
-- ------------------------------------------------------

--
-- Drop And Create DataBase `db_test_1`
--

DROP DATABASE IF EXISTS db_test_1;
CREATE DATABASE IF NOT EXISTS db_test_1 DEFAULT CHARSET=utf8mb4;
USE db_test_1;

--
-- Table structure for table `tb_enterprise_sale_110100_2019`
--

DROP TABLE IF EXISTS `tb_enterprise_sale_110100_2019`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_enterprise_sale_110100_2019` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `city_code` varchar(20) NOT NULL COMMENT '城市code',
  `enterprise_id` bigint(20) NOT NULL COMMENT '企业id',
  `sale_day` date NOT NULL COMMENT '销售日期',
  `sale_value` decimal(10,0) NOT NULL COMMENT '销售金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='企业日度销售数据（按城市和年份进行分表）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_enterprise_sale_110100_2019`
--

/*!40000 ALTER TABLE `tb_enterprise_sale_110100_2019` DISABLE KEYS */;
INSERT INTO `tb_enterprise_sale_110100_2019` VALUES (9,'110100',10001,'2019-01-01',1000),(10,'110100',10001,'2019-12-31',1000);
/*!40000 ALTER TABLE `tb_enterprise_sale_110100_2019` ENABLE KEYS */;

--
-- Table structure for table `tb_enterprise_sale_110100_2020`
--

DROP TABLE IF EXISTS `tb_enterprise_sale_110100_2020`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_enterprise_sale_110100_2020` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `city_code` varchar(20) NOT NULL COMMENT '城市code',
  `enterprise_id` bigint(20) NOT NULL COMMENT '企业id',
  `sale_day` date NOT NULL COMMENT '销售日期',
  `sale_value` decimal(10,0) NOT NULL COMMENT '销售金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='企业日度销售数据（按城市和年份进行分表）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_enterprise_sale_110100_2020`
--

/*!40000 ALTER TABLE `tb_enterprise_sale_110100_2020` DISABLE KEYS */;
INSERT INTO `tb_enterprise_sale_110100_2020` VALUES (11,'110100',10001,'2020-01-01',1000),(12,'110100',10001,'2020-12-31',1000);
/*!40000 ALTER TABLE `tb_enterprise_sale_110100_2020` ENABLE KEYS */;

--
-- Table structure for table `tb_enterprise_sale_130100_2019`
--

DROP TABLE IF EXISTS `tb_enterprise_sale_130100_2019`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_enterprise_sale_130100_2019` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `city_code` varchar(20) NOT NULL COMMENT '城市code',
  `enterprise_id` bigint(20) NOT NULL COMMENT '企业id',
  `sale_day` date NOT NULL COMMENT '销售日期',
  `sale_value` decimal(10,0) NOT NULL COMMENT '销售金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COMMENT='企业日度销售数据（按城市和年份进行分表）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_enterprise_sale_130100_2019`
--

/*!40000 ALTER TABLE `tb_enterprise_sale_130100_2019` DISABLE KEYS */;
INSERT INTO `tb_enterprise_sale_130100_2019` VALUES (13,'130100',10001,'2019-01-01',1000),(14,'130100',10001,'2019-12-31',1000);
/*!40000 ALTER TABLE `tb_enterprise_sale_130100_2019` ENABLE KEYS */;

--
-- Table structure for table `tb_enterprise_sale_130100_2020`
--

DROP TABLE IF EXISTS `tb_enterprise_sale_130100_2020`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_enterprise_sale_130100_2020` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `city_code` varchar(20) NOT NULL COMMENT '城市code',
  `enterprise_id` bigint(20) NOT NULL COMMENT '企业id',
  `sale_day` date NOT NULL COMMENT '销售日期',
  `sale_value` decimal(10,0) NOT NULL COMMENT '销售金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COMMENT='企业日度销售数据（按城市和年份进行分表）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_enterprise_sale_130100_2020`
--

/*!40000 ALTER TABLE `tb_enterprise_sale_130100_2020` DISABLE KEYS */;
INSERT INTO `tb_enterprise_sale_130100_2020` VALUES (15,'130100',10001,'2020-01-01',1000),(16,'130100',10001,'2020-12-31',1000);
/*!40000 ALTER TABLE `tb_enterprise_sale_130100_2020` ENABLE KEYS */;



/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-10-09  22:33:02


-- 删除数据的脚本，需要测试插入方法的时候使用，对于查询、更新、删除的方法无须执行该操作
/*
TRUNCATE TABLE db_test_0.tb_enterprise_sale_110100_2019;
TRUNCATE TABLE db_test_0.tb_enterprise_sale_110100_2020;
TRUNCATE TABLE db_test_0.tb_enterprise_sale_130100_2019;
TRUNCATE TABLE db_test_0.tb_enterprise_sale_130100_2020;

TRUNCATE TABLE db_test_1.tb_enterprise_sale_110100_2019;
TRUNCATE TABLE db_test_1.tb_enterprise_sale_110100_2020;
TRUNCATE TABLE db_test_1.tb_enterprise_sale_130100_2019;
TRUNCATE TABLE db_test_1.tb_enterprise_sale_130100_2020;
*/