
DROP DATABASE IF EXISTS db_test;
CREATE DATABASE IF NOT EXISTS db_test;
USE db_test;


TRUNCATE TABLE tb_user;
SELECT * FROM tb_user;

SET GLOBAL log_bin_trust_function_creators=TRUE;

-- ------------------------------------------------------------------------------------------------------
-- --------------------------------- 生成随机数据的函数生成随机3位姓名的函数 -----------------------------------
-- ------------------------------------------------------------------------------------------------------
--
-- 生成三位数随机姓名函数
--
DROP FUNCTION IF EXISTS generateName;
CREATE FUNCTION generateName() RETURNS CHAR(11) CHARSET utf8
BEGIN
    DECLARE last_name VARCHAR(400) CHARSET UTF8;
    DECLARE first_name_1 VARCHAR(400) CHARSET UTF8;
    DECLARE first_name_2 VARCHAR(400) CHARSET UTF8;


    DECLARE name VARCHAR(10) CHARSET UTF8;
    -- 初始化一个190姓氏字符串，作为姓氏字符库
    SET last_name :=
            '赵钱孙李周吴郑王冯陈诸卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮齐康伍余元卜顾孟平黄和穆萧尹姚邵堪汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董粱杜阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田樊胡凌霍虞万支柯咎管卢莫经房裘干解应宗丁宣贲邓郁单杭洪包诸左石崔吉钮龚';
    -- 初始化一个400名字字符串，作为名字字符库
    SET first_name_1 :=
            '明国华建文平志伟东海强晓生光林小民永杰军金健一忠洪江福祥中正振勇耀春大宁亮宇兴宝少剑云学仁涛瑞飞鹏安亚泽世汉达卫利胜敏群波成荣新峰刚家龙德庆斌辉良玉俊立浩天宏子松克清长嘉红山贤阳乐锋智青跃元武广思雄锦威启昌铭维义宗英凯鸿森超坚旭政传康继翔栋仲权奇礼楠炜友年震鑫雷兵万星骏伦绍麟雨行才希彦兆贵源有景升惠臣慧开章润高佳虎根远力进泉茂毅富博霖顺信凡豪树和恩向道川彬柏磊敬书鸣芳培全炳基冠晖京欣廷哲保秋君劲轩帆若连勋祖锡吉崇钧田石奕发洲彪钢运伯满庭申湘皓承梓雪孟其潮冰怀鲁裕翰征谦航士尧标洁城寿枫革纯风化逸腾岳银鹤琳显焕来心凤睿勤延凌昊西羽百捷定琦圣佩麒虹如靖日咏会久昕黎桂玮燕可越彤雁孝宪萌颖艺夏桐月瑜沛诚夫声冬奎扬双坤镇楚水铁喜之迪泰方同滨邦先聪朝善非恒晋汝丹为晨乃秀岩辰洋然厚灿卓杨钰兰怡灵淇美琪亦晶舒菁真涵爽雅爱依静棋宜男蔚芝菲露娜珊雯淑曼萍珠诗璇琴素梅玲蕾艳紫珍丽仪梦倩伊茜妍碧芬儿岚婷菊妮媛莲娟一';
    SET first_name_2 :=
            '明国华建文平志伟东海强晓生光林小民永杰军金健一忠洪江福祥中正振勇耀春大宁亮宇兴宝少剑云学仁涛瑞飞鹏安亚泽世汉达卫利胜敏群波成荣新峰刚家龙德庆斌辉良玉俊立浩天宏子松克清长嘉红山贤阳乐锋智青跃元武广思雄锦威启昌铭维义宗英凯鸿森超坚旭政传康继翔栋仲权奇礼楠炜友年震鑫雷兵万星骏伦绍麟雨行才希彦兆贵源有景升惠臣慧开章润高佳虎根远力进泉茂毅富博霖顺信凡豪树和恩向道川彬柏磊敬书鸣芳培全炳基冠晖京欣廷哲保秋君劲轩帆若连勋祖锡吉崇钧田石奕发洲彪钢运伯满庭申湘皓承梓雪孟其潮冰怀鲁裕翰征谦航士尧标洁城寿枫革纯风化逸腾岳银鹤琳显焕来心凤睿勤延凌昊西羽百捷定琦圣佩麒虹如靖日咏会久昕黎桂玮燕可越彤雁孝宪萌颖艺夏桐月瑜沛诚夫声冬奎扬双坤镇楚水铁喜之迪泰方同滨邦先聪朝善非恒晋汝丹为晨乃秀岩辰洋然厚灿卓杨钰兰怡灵淇美琪亦晶舒菁真涵爽雅爱依静棋宜男蔚芝菲露娜珊雯淑曼萍珠诗璇琴素梅玲蕾艳紫珍丽仪梦倩伊茜妍碧芬儿岚婷菊妮媛莲娟一';

    SET name =
            CONCAT(SUBSTRING(last_name, floor(1 + 190 * rand()), 1), SUBSTRING(first_name_1, floor(1 + 400 * rand()), 1),
                   SUBSTRING(first_name_2, floor(1 + 400 * rand()), 1));
    RETURN name;
END;

-- 测试生成随机三位姓名的函数
SELECT generateName();


-- ------------------------------------------------------------------------------------------------------
-- --------------------------------------- 生成随机11位手机号的函数 -----------------------------------------
-- ------------------------------------------------------------------------------------------------------
--
-- 生成11位随机手机号函数
--
DROP FUNCTION IF EXISTS generatePhone;
CREATE FUNCTION generatePhone() RETURNS CHAR(11) CHARSET utf8
BEGIN
    -- 中国电信号段:133、149、153、173、177、180、181、189、199
    -- 中国联通号段:130、131、132、145、155、156、166、171、175、176、185、186、166
    -- 中国移动号段:134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、172、178、182、183、184、187、188、198
    -- 手机号网络识别号（运营商代码）：3位
    DECLARE phone_head VARCHAR(200) DEFAULT '000,133,149,153,173,177,180,181,189,199,130,131,132,145,155,156,166,171,175,176,185,186,166,134,135,136,137,138,139,147,150,151,152,157,158,159,172,178,182,183,184,187,188,198';
    -- 手机号其他(归属地区代码+用户代码)：9位
    DECLARE phone_content CHAR(10) DEFAULT '0123456789';
    DECLARE phone CHAR(11) DEFAULT SUBSTRING(phone_head, 1 + (FLOOR(1 + (RAND() * 43)) * 4), 3);
    DECLARE i INT DEFAULT 1;
    DECLARE len INT DEFAULT LENGTH(phone_content);
    WHILE i < 9 DO
            SET i = i + 1;
            SET phone = CONCAT(phone, SUBSTRING(phone_content, FLOOR(1 + RAND() * len), 1));
        END WHILE;
    RETURN phone;
END;

-- 测试生成11位随机手机号函数
SELECT generatePhone();


-- ------------------------------------------------------------------------------------------------------
-- ------------------------------------- 生成随机yyyy-MM-dd生日的函数 --------------------------------------
-- ------------------------------------------------------------------------------------------------------
--
-- 生成随机yyyy-MM-dd生日的函数
--
DROP FUNCTION IF EXISTS generateDate;
CREATE FUNCTION generateDate() RETURNS CHAR(10) CHARSET utf8
BEGIN
    DECLARE date VARCHAR(10);
    SET date :=
            DATE(
                    FROM_UNIXTIME(
                                UNIX_TIMESTAMP('1990-01-01') + FLOOR(
                                        RAND() * (
                                                UNIX_TIMESTAMP('2019-01-01') - UNIX_TIMESTAMP('1990-01-01') + 1
                                        )
                                )
                        )
                );
    RETURN date;
END;

-- 测试生成随机yyyy-MM-dd生日的函数
SELECT generateDate();


-- ------------------------------------------------------------------------------------------------------
-- ------------------------ 生成任意数量数据的脚本(单表)建表脚本（以用户表tb_user为例） --------------------------
-- ------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS tb_user;
CREATE TABLE tb_user
(
    id    BIGINT AUTO_INCREMENT NOT NULL COMMENT '主键id',
    name  VARCHAR(20)           NOT NULL COMMENT '姓名',
    sex   BOOLEAN COMMENT '性别:0:女,1:男',
    birth VARCHAR(10) COMMENT '出生年月',
    phone VARCHAR(11) COMMENT '手机号码',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET utf8mb4 COMMENT '用户表';


-- ------------------------------------------------------------------------------------------------------
-- ----------------------------- 生成任意数量数据的存储过程（单数据插入，效率低下） -------------------------------
-- ------------------------------------------------------------------------------------------------------
--
-- 生成任意数量数据的存储过程(单数据插入，效率相对较低)
--
DROP PROCEDURE IF EXISTS prod_create_user;
CREATE PROCEDURE prod_create_user(IN num INT)
BEGIN
    DECLARE i INT DEFAULT num;
    WHILE i > 0
        DO
            INSERT INTO tb_user(name, sex, birth, phone) VALUES (generateName(), i % 2, generateDate(), generatePhone());
            SET i := i - 1;
        END WHILE;
END;

-- 测试：生成100w条数据
CALL prod_create_user(1000000);
SELECT COUNT(*)
FROM tb_user;


-- ------------------------------------------------------------------------------------------------------
-- -------------------------- 生成任意数量数据的存储过程（批量插入，效率极高，数量级） ----------------------------
-- ------------------------------------------------------------------------------------------------------
-- 生成任意数量数据的存储过程(批量插入，效率相对较高)
--
-- [2021-04-24 13:53:43] completed in 1 m 41 s 356 ms
DROP PROCEDURE IF EXISTS prod_create_user_batch;
CREATE PROCEDURE prod_create_user_batch(IN num INT)
BEGIN
    DECLARE i INT DEFAULT num;
    DECLARE exec_sql VARCHAR(5000) CHARSET utf8mb4 DEFAULT '';
    SET exec_sql := 'INSERT INTO tb_user(name, sex, birth, phone) VALUES ';
    WHILE i > 0 DO
            SET @insert_val := CONCAT_WS(',', CONCAT('"', generateName(), '"'), i % 2, CONCAT('"', generateDate(), '"'),
                                         CONCAT('"', generatePhone(), '"'));
            SET @insert_val := CONCAT('(', @insert_val, ')', ',');
            SET exec_sql := CONCAT(exec_sql, @insert_val);
            SET i := i - 1;
            IF (i % 100 = 0) THEN
                SET @exec_sql := LEFT(exec_sql, CHAR_LENGTH(exec_sql) - 1);
                PREPARE stmt FROM @exec_sql;
                EXECUTE stmt;
                DEALLOCATE PREPARE stmt;
                SET exec_sql := 'INSERT INTO tb_user(name, sex, birth, phone) VALUES ';
            END IF;
        END WHILE;
END;

-- 测试：生成100w条数据
-- [2021-04-24 13:53:43] completed in 1 m 41 s 356 ms
CALL prod_create_user_batch(1000000);
