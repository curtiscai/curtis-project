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
-- Table structure for table `tb_order_0`
--

DROP TABLE IF EXISTS `tb_order_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_order_0` (
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `address_id` bigint(20) NOT NULL COMMENT '地址id',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order_0`
--

/*!40000 ALTER TABLE `tb_order_0` DISABLE KEYS */;
INSERT INTO `tb_order_0` VALUES (10000,1000,1),(10004,1000,1);
/*!40000 ALTER TABLE `tb_order_0` ENABLE KEYS */;

--
-- Table structure for table `tb_order_1`
--

DROP TABLE IF EXISTS `tb_order_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_order_1` (
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `address_id` bigint(20) NOT NULL COMMENT '地址id',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order_1`
--

/*!40000 ALTER TABLE `tb_order_1` DISABLE KEYS */;
INSERT INTO `tb_order_1` VALUES (10001,1000,1),(10005,1000,1);
/*!40000 ALTER TABLE `tb_order_1` ENABLE KEYS */;

--
-- Table structure for table `tb_order_2`
--

DROP TABLE IF EXISTS `tb_order_2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_order_2` (
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `address_id` bigint(20) NOT NULL COMMENT '地址id',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order_2`
--

/*!40000 ALTER TABLE `tb_order_2` DISABLE KEYS */;
INSERT INTO `tb_order_2` VALUES (10002,1000,1),(10006,1000,1);
/*!40000 ALTER TABLE `tb_order_2` ENABLE KEYS */;

--
-- Table structure for table `tb_order_3`
--

DROP TABLE IF EXISTS `tb_order_3`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_order_3` (
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `address_id` bigint(20) NOT NULL COMMENT '地址id',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order_3`
--

/*!40000 ALTER TABLE `tb_order_3` DISABLE KEYS */;
INSERT INTO `tb_order_3` VALUES (10003,1000,1),(10007,1000,1);
/*!40000 ALTER TABLE `tb_order_3` ENABLE KEYS */;



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
-- Table structure for table `tb_order_0`
--

DROP TABLE IF EXISTS `tb_order_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_order_0` (
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `address_id` bigint(20) NOT NULL COMMENT '地址id',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order_0`
--

/*!40000 ALTER TABLE `tb_order_0` DISABLE KEYS */;
INSERT INTO `tb_order_0` VALUES (11000,1001,1),(11004,1001,1);
/*!40000 ALTER TABLE `tb_order_0` ENABLE KEYS */;

--
-- Table structure for table `tb_order_1`
--

DROP TABLE IF EXISTS `tb_order_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_order_1` (
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `address_id` bigint(20) NOT NULL COMMENT '地址id',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order_1`
--

/*!40000 ALTER TABLE `tb_order_1` DISABLE KEYS */;
INSERT INTO `tb_order_1` VALUES (11001,1001,1),(11005,1001,1);
/*!40000 ALTER TABLE `tb_order_1` ENABLE KEYS */;

--
-- Table structure for table `tb_order_2`
--

DROP TABLE IF EXISTS `tb_order_2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_order_2` (
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `address_id` bigint(20) NOT NULL COMMENT '地址id',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order_2`
--

/*!40000 ALTER TABLE `tb_order_2` DISABLE KEYS */;
INSERT INTO `tb_order_2` VALUES (11002,1001,1),(11006,1001,1);
/*!40000 ALTER TABLE `tb_order_2` ENABLE KEYS */;

--
-- Table structure for table `tb_order_3`
--

DROP TABLE IF EXISTS `tb_order_3`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_order_3` (
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `address_id` bigint(20) NOT NULL COMMENT '地址id',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order_3`
--

/*!40000 ALTER TABLE `tb_order_3` DISABLE KEYS */;
INSERT INTO `tb_order_3` VALUES (11003,1001,1),(11007,1001,1);
/*!40000 ALTER TABLE `tb_order_3` ENABLE KEYS */;




/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-10-09  22:54:54


-- 删除数据的脚本，需要测试插入方法的时候使用，对于查询、更新、删除的方法无须执行该操作
/*
TRUNCATE TABLE db_test_0.tb_order_0;
TRUNCATE TABLE db_test_0.tb_order_1;
TRUNCATE TABLE db_test_0.tb_order_2;
TRUNCATE TABLE db_test_0.tb_order_3;

TRUNCATE TABLE db_test_1.tb_order_0;
TRUNCATE TABLE db_test_1.tb_order_1;
TRUNCATE TABLE db_test_1.tb_order_2;
TRUNCATE TABLE db_test_1.tb_order_3;
*/