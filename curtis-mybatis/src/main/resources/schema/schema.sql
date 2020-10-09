# 创建数据库
CREATE DATABASE IF NOT EXISTS db_user;
USE db_user;

# 创建表
DROP TABLE IF EXISTS tb_user;
CREATE TABLE IF NOT EXISTS tb_user
(
	id int auto_increment comment '主键id'
		primary key,
	name varchar(10) not null comment '姓名',
	sex enum('男', '女') null comment '性别',
	birth date null comment '出生年月',
	phone bigint null comment '手机号码',
	height decimal(5,2) null comment '身高',
	address varchar(200) null comment '编码',
	create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
	update_time datetime default CURRENT_TIMESTAMP not null comment '修改时间',
	is_del bit default b'0' not null comment '是否删除：0不删除1删除'
) engine InnoDB comment '用户表' charset=utf8mb4;