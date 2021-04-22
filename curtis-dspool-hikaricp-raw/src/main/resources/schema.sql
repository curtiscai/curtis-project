
DROP DATABASE IF EXISTS db_test;
CREATE DATABASE IF NOT EXISTS db_test;
USE db_test;

CREATE TABLE tb_user(
    id bigint auto_increment primary key,
    name varchar(20) not null,
    birth date not null
) ENGINE InnoDB COMMENT '用户表';

INSERT INTO tb_user(name, birth) VALUES ('curtis1','1991-01-01'),('curtis2','1992-01-01'),('curtis3','1993-01-01');