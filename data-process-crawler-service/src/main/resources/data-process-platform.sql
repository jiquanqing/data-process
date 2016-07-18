/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50536
Source Host           : localhost:3306
Source Database       : data-process-platform

Target Server Type    : MYSQL
Target Server Version : 50536
File Encoding         : 65001

Date: 2016-07-18 16:40:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `crawler_job`
-- ----------------------------
DROP TABLE IF EXISTS `crawler_job`;
CREATE TABLE `crawler_job` (
  `jobId` varchar(100) NOT NULL,
  `jobName` varchar(200) NOT NULL,
  `urlListId` varchar(100) NOT NULL,
  `maxDepth` int(11) NOT NULL,
  `jobMaxSize` int(11) NOT NULL,
  `crawledNum` int(11) DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `jobStatus` varchar(100) NOT NULL,
  `jobMaxDomainSize` int(11) DEFAULT NULL,
  `curDepth` int(11) DEFAULT NULL,
  `ctime` datetime NOT NULL,
  `mtime` datetime NOT NULL,
  `isValid` int(11) NOT NULL,
  `jobType` varchar(100) DEFAULT NULL,
  `jobConfig` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`jobId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of crawler_job
-- ----------------------------
INSERT INTO `crawler_job` VALUES ('29de21d891df7323', '29de21d891df7323', '', '0', '100', null, null, null, '1', '100', null, '2016-07-18 15:23:14', '2016-07-18 15:23:14', '1', null, '{\r\n  \"variablesFields\" : [ ],\r\n  \"crawlerType\" : 2\r\n}');

-- ----------------------------
-- Table structure for `crawler_job_user`
-- ----------------------------
DROP TABLE IF EXISTS `crawler_job_user`;
CREATE TABLE `crawler_job_user` (
  `jobId` varchar(100) NOT NULL,
  `userId` varchar(100) NOT NULL,
  `ctime` datetime NOT NULL,
  `mtime` datetime NOT NULL,
  `isValid` int(11) NOT NULL,
  PRIMARY KEY (`jobId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of crawler_job_user
-- ----------------------------

-- ----------------------------
-- Table structure for `crawler_urllist`
-- ----------------------------
DROP TABLE IF EXISTS `crawler_urllist`;
CREATE TABLE `crawler_urllist` (
  `urlListId` varchar(100) NOT NULL,
  `urlListName` varchar(200) NOT NULL,
  `urls` longtext NOT NULL,
  `userId` varchar(100) NOT NULL,
  `ctime` datetime NOT NULL,
  `mtime` datetime NOT NULL,
  `isValid` int(11) NOT NULL,
  PRIMARY KEY (`urlListId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of crawler_urllist
-- ----------------------------

-- ----------------------------
-- Table structure for `crawler_user`
-- ----------------------------
DROP TABLE IF EXISTS `crawler_user`;
CREATE TABLE `crawler_user` (
  `userId` varchar(100) NOT NULL,
  `userName` varchar(150) NOT NULL,
  `password` longtext NOT NULL,
  `levelId` varchar(100) NOT NULL,
  `email` varchar(200) NOT NULL,
  `startDate` datetime DEFAULT NULL,
  `expireDate` datetime DEFAULT NULL,
  `phone` varchar(200) NOT NULL,
  `ctime` datetime NOT NULL,
  `mtime` datetime NOT NULL,
  `isValid` int(11) NOT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of crawler_user
-- ----------------------------
