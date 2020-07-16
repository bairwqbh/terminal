SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS ter_terminal_commodu;
DROP TABLE IF EXISTS ter_commodu;
DROP TABLE IF EXISTS ter_terminal_fota;
DROP TABLE IF EXISTS ter_fota;
DROP TABLE IF EXISTS ter_terminal;




/* Create Tables */

CREATE TABLE ter_commodu
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	imei varchar(32) COMMENT '通信模组编号',
	commodu_type varchar(32) COMMENT '通信模组类型',
	imsi varchar(32) COMMENT '通信卡编号',
	iot_no varchar(32) COMMENT '物联网卡号',
	msisdn varchar(32) COMMENT '唯一通信号',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE (imei)
) COMMENT = '通信模组';


CREATE TABLE ter_fota
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	upgrade_id varchar(32) COMMENT '升级编号',
	type_id varchar(32) COMMENT '终端类型编号',
	version_no varchar(32) COMMENT '程序版本号',
	version_serial int COMMENT '版本序列',
	content varchar(4000) COMMENT '内容描述',
	asso_mode varchar(32) COMMENT '关联模式',
	data_length int COMMENT '数据长度',
	shardable boolean DEFAULT '1' COMMENT '是否分片',
	shard_size int COMMENT '分片大小',
	shard_num int COMMENT '分片数量',
	publisher varchar(32) COMMENT '发布人',
	publish_time datetime COMMENT '发布时间',
	status varchar(32) COMMENT '状态',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE (upgrade_id)
) COMMENT = '终端程序升级';


CREATE TABLE ter_terminal
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	terminal_id varchar(32) COMMENT '终端编号',
	terminal_name varchar(32) COMMENT '终端名称',
	type_id varchar(32) COMMENT '终端类型编号',
	use_start_time datetime COMMENT '终端使用开始时间',
	use_end_time datetime COMMENT '终端使用结束时间',
	version_no varchar(32) COMMENT '程序版本号',
	version_serial int COMMENT '版本序列',
	use_num int COMMENT '使用次数',
	status varchar(32) COMMENT '状态',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE (terminal_id)
) COMMENT = '终端';


CREATE TABLE ter_terminal_commodu
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	terminal_id varchar(32) COMMENT '终端编号',
	imei varchar(32) COMMENT '通信模组编号',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id)
) COMMENT = '终端通信模组关系';


CREATE TABLE ter_terminal_fota
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	upgrade_id varchar(32) COMMENT '升级编号',
	terminal_id varchar(32) COMMENT '终端编号',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id)
) COMMENT = '终端程序升级关系';



/* Create Indexes */

CREATE INDEX terminal_id ON ter_terminal_commodu (terminal_id ASC);
CREATE INDEX imei ON ter_terminal_commodu (imei ASC);
CREATE INDEX upgrade_id ON ter_terminal_fota (upgrade_id ASC);



