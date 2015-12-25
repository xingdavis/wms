/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.251
Source Server Version : 50616
Source Host           : 192.168.1.251:3306
Source Database       : db_xswms

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2015-12-25 18:18:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_client`
-- ----------------------------
DROP TABLE IF EXISTS `t_client`;
CREATE TABLE `t_client` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `cname` varchar(100) DEFAULT NULL COMMENT '客户公司抬头',
  `contact_man` varchar(50) DEFAULT NULL COMMENT '负责人',
  `contact_tel` varchar(50) DEFAULT NULL COMMENT '联系电话',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_client
-- ----------------------------
INSERT INTO `t_client` VALUES ('1', 'test', 'contact_man', '13800908890');
INSERT INTO `t_client` VALUES ('2', 'kd', 'fff', '2324');
INSERT INTO `t_client` VALUES ('3', 'hh', 'tt', '3434');
INSERT INTO `t_client` VALUES ('4', 'gk', '34', '66578547');
INSERT INTO `t_client` VALUES ('5', 'aa', 'er1', '124');
INSERT INTO `t_client` VALUES ('6', 'aa1', 'er1', '124');
INSERT INTO `t_client` VALUES ('7', 'qq', 'q31', '35236');
INSERT INTO `t_client` VALUES ('8', 'ww', 'w1', '224');
INSERT INTO `t_client` VALUES ('9', 'ee', 'e2', '325');
INSERT INTO `t_client` VALUES ('10', 'rr', 'r1', '124');
INSERT INTO `t_client` VALUES ('11', 'yy', 'yy1', '4567');
INSERT INTO `t_client` VALUES ('12', 'ttttt', 'tt1', '3435');
INSERT INTO `t_client` VALUES ('13', 'uu', 'u1', '4567');

-- ----------------------------
-- Table structure for `t_menu`
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mname` varchar(100) DEFAULT NULL COMMENT '菜单名',
  `pid` int(11) NOT NULL DEFAULT '0' COMMENT '父级id',
  `orderid` int(11) NOT NULL DEFAULT '0' COMMENT '顺序号',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `url` varchar(100) DEFAULT NULL COMMENT '链接',
  `disable` bit(1) NOT NULL DEFAULT b'0' COMMENT '停用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES ('1', '仓库管理', '0', '0', null, '', '');
INSERT INTO `t_menu` VALUES ('2', '打印路线图', '1', '0', null, null, '');
INSERT INTO `t_menu` VALUES ('3', '入仓登记', '1', '0', null, null, '');
INSERT INTO `t_menu` VALUES ('4', '实时仓储查询', '1', '0', null, null, '');
INSERT INTO `t_menu` VALUES ('5', '出仓登记', '1', '0', null, null, '');
INSERT INTO `t_menu` VALUES ('6', '客户账单', '1', '0', null, null, '');
INSERT INTO `t_menu` VALUES ('7', '运输管理', '0', '0', null, null, '');
INSERT INTO `t_menu` VALUES ('8', '提货登记', '7', '0', null, null, '');
INSERT INTO `t_menu` VALUES ('9', '费用登记', '7', '0', null, null, '');
INSERT INTO `t_menu` VALUES ('10', '查询账单', '7', '0', null, null, '');
INSERT INTO `t_menu` VALUES ('11', '基础资料管理', '0', '0', null, null, '');
INSERT INTO `t_menu` VALUES ('12', '用户管理', '11', '0', null, '/users', '');

-- ----------------------------
-- Table structure for `t_order`
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `code` varchar(20) DEFAULT NULL COMMENT '订单编号',
  `client_id` int(11) NOT NULL COMMENT '客户id',
  `contact_man` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contact_tel` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `order_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '订单日期',
  `flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '标记。0-未入仓订单（可删除），1-已入仓订单（不可删除）,2-已出仓单（不可以删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_order_code` (`code`,`client_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES ('19', '00001', '0', 'contactMan', 'contactTel', '2015-12-23 10:19:09', '1');
INSERT INTO `t_order` VALUES ('20', '00002', '0', 'contactMan', 'contactTel', '2015-12-23 10:19:27', '1');
INSERT INTO `t_order` VALUES ('21', null, '0', 'contactMan', 'contactTel', '2015-12-23 10:39:34', '1');
INSERT INTO `t_order` VALUES ('22', '123', '0', 'contactMan', 'contactTel', '2015-12-23 11:12:08', '1');
INSERT INTO `t_order` VALUES ('23', '2015122300005', '0', 'contactMan', 'contactTel', '2015-12-23 11:12:39', '1');

-- ----------------------------
-- Table structure for `t_order_detail`
-- ----------------------------
DROP TABLE IF EXISTS `t_order_detail`;
CREATE TABLE `t_order_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `order_id` int(11) NOT NULL,
  `cname` varchar(100) DEFAULT NULL COMMENT '货名',
  `num` int(11) NOT NULL DEFAULT '0' COMMENT '件数',
  `vol` decimal(10,2) DEFAULT NULL COMMENT '体积',
  `weight` decimal(10,2) DEFAULT NULL COMMENT '重量',
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `t_order_detail_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_order_detail
-- ----------------------------
INSERT INTO `t_order_detail` VALUES ('16', '19', 'item1', '2', '212.32', '11.20');
INSERT INTO `t_order_detail` VALUES ('17', '20', 'item1', '2', '212.32', '11.20');
INSERT INTO `t_order_detail` VALUES ('18', '21', 'item1', '2', '212.32', '11.20');
INSERT INTO `t_order_detail` VALUES ('19', '22', 'item1', '2', '212.32', '11.20');
INSERT INTO `t_order_detail` VALUES ('20', '23', 'item1', '2', '212.32', '11.20');

-- ----------------------------
-- Table structure for `t_rolemenu`
-- ----------------------------
DROP TABLE IF EXISTS `t_rolemenu`;
CREATE TABLE `t_rolemenu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleid` int(11) NOT NULL DEFAULT '0' COMMENT '角色id',
  `menuid` int(11) NOT NULL DEFAULT '0' COMMENT '菜单id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_rolemenu
-- ----------------------------
INSERT INTO `t_rolemenu` VALUES ('1', '0', '1');
INSERT INTO `t_rolemenu` VALUES ('2', '0', '2');
INSERT INTO `t_rolemenu` VALUES ('3', '0', '3');
INSERT INTO `t_rolemenu` VALUES ('4', '0', '4');
INSERT INTO `t_rolemenu` VALUES ('5', '0', '5');
INSERT INTO `t_rolemenu` VALUES ('6', '0', '6');
INSERT INTO `t_rolemenu` VALUES ('7', '0', '7');
INSERT INTO `t_rolemenu` VALUES ('8', '0', '8');
INSERT INTO `t_rolemenu` VALUES ('9', '0', '9');
INSERT INTO `t_rolemenu` VALUES ('10', '0', '10');
INSERT INTO `t_rolemenu` VALUES ('11', '0', '0');
INSERT INTO `t_rolemenu` VALUES ('12', '0', '11');
INSERT INTO `t_rolemenu` VALUES ('13', '0', '12');

-- ----------------------------
-- Table structure for `t_stock_in`
-- ----------------------------
DROP TABLE IF EXISTS `t_stock_in`;
CREATE TABLE `t_stock_in` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `code` varchar(20) DEFAULT NULL COMMENT '入仓单编号',
  `order_id` int(11) NOT NULL COMMENT '订单id',
  `order_code` varchar(20) DEFAULT NULL COMMENT '订单号',
  `client_id` int(11) NOT NULL COMMENT '客户id',
  `car_no` varchar(20) DEFAULT NULL COMMENT '车牌号',
  `in_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '入仓日期',
  `cr_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '单据状态，-1-被删除单，0-未审批单，1-已审批',
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `t_stock_in_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_stock_in
-- ----------------------------

-- ----------------------------
-- Table structure for `t_stock_in_detail`
-- ----------------------------
DROP TABLE IF EXISTS `t_stock_in_detail`;
CREATE TABLE `t_stock_in_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `bill_id` int(11) NOT NULL COMMENT '入仓单id',
  `cname` varchar(100) DEFAULT NULL COMMENT '货名',
  `num` int(11) NOT NULL DEFAULT '0' COMMENT '件数',
  `vol` decimal(10,2) DEFAULT NULL COMMENT '体积',
  `weight` decimal(10,2) DEFAULT NULL COMMENT '重量',
  `yard` varchar(20) DEFAULT NULL COMMENT '堆位',
  PRIMARY KEY (`id`),
  KEY `t_stock_in_detail_ibfk_1` (`bill_id`),
  CONSTRAINT `t_stock_in_detail_ibfk_1` FOREIGN KEY (`bill_id`) REFERENCES `t_stock_in` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_stock_in_detail
-- ----------------------------

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uname` varchar(50) NOT NULL,
  `pwd` varchar(50) NOT NULL,
  `age` int(4) NOT NULL DEFAULT '0',
  `roleid` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'cs', '123', '24', '0');

-- ----------------------------
-- Function structure for `GetOrderCode`
-- ----------------------------
DROP FUNCTION IF EXISTS `GetOrderCode`;
DELIMITER ;;
CREATE DEFINER=`root`@`192.168.%` FUNCTION `GetOrderCode`() RETURNS varchar(20) CHARSET utf8
BEGIN
	#Routine body goes here...
set @code =(SELECT concat(count(*)+1,'') from t_order where timestampdiff(day ,order_date,now())=0);
while LENGTH(@code)<5 do
set @code = concat('0',@code);
end while;
	RETURN CONCAT(date_format(now(), '%Y%m%d'),@code);
END
;;
DELIMITER ;
