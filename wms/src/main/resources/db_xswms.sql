/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.251
Source Server Version : 50616
Source Host           : 192.168.1.251:3306
Source Database       : db_xswms

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2015-11-27 18:04:12
*/

SET FOREIGN_KEY_CHECKS=0;

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
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uname` varchar(50) NOT NULL,
  `pwd` varchar(50) NOT NULL,
  `age` int(4) NOT NULL,
  `roleid` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'cs', '123', '24', '0');
