CREATE DATABASE IF NOT EXISTS lost_found DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE lost_found;
SET NAMES utf8;
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色标识',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`,`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='管理员';

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', 'admin', 'tyl010130', '管理员', 'http://localhost:9090/files/1706686315412-IMG_1418.JPG', 'ADMIN', '13677889922', 'admin@xm.com');
INSERT INTO `admin` VALUES ('2', 'tyl', '123', '唐雅玲', 'http://localhost:9090/files/1706686274727-IMG_1418.JPG', 'ADMIN', '1599009987', '4305268@qq.com');

-- ----------------------------
-- Table structure for find
-- ----------------------------
DROP TABLE IF EXISTS `find`;
CREATE TABLE `find` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '招领物品id',
  `name` varchar(255) DEFAULT NULL COMMENT '招领物品名称',
  `img` varchar(255) DEFAULT NULL COMMENT '招领物品照片',
  `status` varchar(255) DEFAULT NULL COMMENT '招领状态',
  `content` text COMMENT '招领内容',
  `time` varchar(255) DEFAULT NULL COMMENT '拾物时间',
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of find
-- ----------------------------
INSERT INTO `find` VALUES ('1', '在四区捡到一束花', 'http://localhost:9090/files/1707280656823-IMG_1418.JPG', '已找到失主', '<p>快来招领</p>', '2024-02-07', '6');
INSERT INTO `find` VALUES ('2', '一个木头色的书架', 'http://localhost:9090/files/1707296786280-src=http___img.alicdn.com_i3_3081906731_O1CN01d2hcqr1zaqaltJAz8_!!0-item_pic.jpg&refer=http___img.alicdn.webp', '已找到失主', '<p>昨天在五教门口捡到一个书架</p>', '2024-02-07', '3');
INSERT INTO `find` VALUES ('3', '黑色的书包', 'http://localhost:9090/files/1707305422269-b874e99febcac2afc3f9df18bad1085882c97b3f_size428_w640_h360.jpg', '未找到失主', '<p>昨天在操场捡到一个黑色的书包</p>', '2024-02-07', '3');
INSERT INTO `find` VALUES ('4', '钥匙扣', 'http://localhost:9090/files/1707305566024-IMG_1418.JPG', '未找到失主', '', '2024-02-07', '3');
INSERT INTO `find` VALUES ('5', '一个黑色的滑板鞋', 'http://localhost:9090/files/1709548997444-微信图片_20240201162445.jpg', '未找到失主', '<p>我昨天在南门混沌店门口捡到一双滑板鞋</p>', '2024-03-04', '6');
INSERT INTO `find` VALUES ('6', '一把钥匙', 'http://localhost:9090/files/1709549175195-下载.jpg', '未找到失主', '', '2024-03-04', '6');

-- ----------------------------
-- Table structure for flower
-- ----------------------------
DROP TABLE IF EXISTS `flower`;
CREATE TABLE `flower` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fid` int DEFAULT NULL COMMENT '与招领广场id关联',
  `user_id` int NOT NULL COMMENT '送小红花的人',
  `module` varchar(255) DEFAULT NULL COMMENT '送小红花的模块',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of flower
-- ----------------------------

-- ----------------------------
-- Table structure for lost
-- ----------------------------
DROP TABLE IF EXISTS `lost`;
CREATE TABLE `lost` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '物品名称',
  `img` varchar(255) DEFAULT NULL COMMENT '物品图片',
  `user_id` int DEFAULT NULL COMMENT '用户ID',
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `content` longtext COMMENT '物品描述',
  `time` varchar(255) DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of lost
-- ----------------------------
INSERT INTO `lost` VALUES ('6', '我最喜欢的天空', 'http://localhost:9090/files/1706958316782-微信图片_20240201162439.jpg', '6', '丢失中', '<p>我最喜欢的天空<img src=\"http://localhost:9090/files/1706972079648-IMG_1418.JPG\" contenteditable=\"false\" style=\"font-size: 14px; max-width: 100%;\"/></p>', '2024-02-03');
INSERT INTO `lost` VALUES ('7', '黑色的衣服', 'http://localhost:9090/files/1707026812379-IMG_1418.JPG', '6', '丢失中', '<p>昨天在操场丢失</p>', '2024-02-04');
INSERT INTO `lost` VALUES ('8', '书架', 'http://localhost:9090/files/1707026840759-src=http___img.alicdn.com_i3_3081906731_O1CN01d2hcqr1zaqaltJAz8_!!0-item_pic.jpg&refer=http___img.alicdn.webp', '6', '已找到', '<p>昨天在放在寝室外面</p>', '2024-02-04');
INSERT INTO `lost` VALUES ('9', '1', 'http://localhost:9090/files/1707026877180-微信图片_20240201162445.jpg', '6', '丢失中', '', '2024-02-04');
INSERT INTO `lost` VALUES ('10', '1', 'http://localhost:9090/files/1707305508074-微信图片_20240201162445.jpg', '3', '已找到', '', '2024-02-07');
INSERT INTO `lost` VALUES ('11', '我的滑板鞋', 'http://localhost:9090/files/1709548893149-微信图片_20240201162439.jpg', '6', '丢失中', '<p>我昨天在南门丢失是一双红色的滑板鞋</p>', '2024-03-04');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '失物ID',
  `article_id` int DEFAULT NULL COMMENT '物品id',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '物品类型',
  `from_id` int DEFAULT NULL COMMENT '留言id',
  `to_id` int DEFAULT NULL COMMENT '失物者id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '失物留言',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '留言时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES ('1', '8', '失物广场', '3', '6', '123', '2024-02-07 00:07:12');
INSERT INTO `message` VALUES ('2', '8', '失物广场', '3', '6', '1213123', '2024-02-07 00:20:22');
INSERT INTO `message` VALUES ('3', '10', '失物广场', '6', '3', '联系电话123456677', '2024-02-28 20:15:30');
INSERT INTO `message` VALUES ('4', '10', '失物广场', '6', '3', '2ewqeqwe', '2024-03-01 11:10:36');
INSERT INTO `message` VALUES ('7', '2', '招领广场', '6', '3', '12131234', '2024-03-01 14:52:12');
INSERT INTO `message` VALUES ('8', '3', '招领广场', '6', '3', '这不就是我的书包吗，\n联系电话：1798989898\nqq：344394583989', '2024-03-04 19:02:41');
INSERT INTO `message` VALUES ('11', '4', '招领广场', '6', '3', '你好这是我的钥匙扣', '2024-03-04 20:08:41');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '内容',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建时间',
  `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='公告信息表';

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES ('1', '重要通知', '在北门捡到一个黑色书包', '2023-09-05', 'admin');
INSERT INTO `notice` VALUES ('2', '失物招领3', '在南校丢失一把雨伞', '2023-09-05', 'admin');
INSERT INTO `notice` VALUES ('3', '失物招领2', '在九教丢失一个钥匙扣', '2023-09-05', 'admin');
INSERT INTO `notice` VALUES ('4', '失物招领1', '四区捡到一个手机了', '2023-11-24', 'admin');

-- ----------------------------
-- Table structure for suggestion
-- ----------------------------
DROP TABLE IF EXISTS `suggestion`;
CREATE TABLE `suggestion` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '建议id',
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '留言内容',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '留言时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of suggestion
-- ----------------------------
INSERT INTO `suggestion` VALUES ('2', '6', '我觉得可以优化一下失物广场增加一下分类功能', '2024-03-02 14:06:03');
INSERT INTO `suggestion` VALUES ('3', '6', '界面设计不够精美', '2024-03-02 14:56:57');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('2', '2', '123456', '2', 'http://localhost:9090/files/1706710080969-下载.jpg', 'USER', '2', '2');
INSERT INTO `user` VALUES ('3', 'tyl', '123456', 'tyl', 'http://localhost:9090/files/1707296746141-微信图片_20240201162445.jpg', 'USER', '1234668789', '1232546');
INSERT INTO `user` VALUES ('6', '宋江', '11111', '宋江', 'http://localhost:9090/files/1706776691900-微信图片_20240201162445.jpg', 'USER', '1268197979', '23213213');
