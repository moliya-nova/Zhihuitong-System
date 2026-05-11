-- ----------------------------
-- 学生管理模块数据库脚本
-- 请先执行 zhihuitong.sql 初始化系统表后再执行本脚本
-- ----------------------------

-- ----------------------------
-- Table structure for sys_student
-- ----------------------------
DROP TABLE IF EXISTS `sys_student`;

CREATE TABLE `sys_student` (
  `student_no` varchar(50) NOT NULL COMMENT '学号（主键）',
  `user_id` bigint DEFAULT NULL COMMENT '关联系统用户ID',
  `student_name` varchar(50) NOT NULL COMMENT '姓名',
  `id_card` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `age` int DEFAULT NULL COMMENT '年龄',
  `gender` char(1) DEFAULT '0' COMMENT '性别（0男 1女）',
  `grade` varchar(20) DEFAULT NULL COMMENT '年级',
  `class_name` varchar(50) DEFAULT NULL COMMENT '班级',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `address` varchar(200) DEFAULT NULL COMMENT '住址',
  `enrollment_date` date DEFAULT NULL COMMENT '入学日期',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`student_no`),
  KEY `idx_student_user_id` (`user_id`),
  KEY `idx_student_grade` (`grade`),
  KEY `idx_student_class` (`class_name`),
  KEY `idx_student_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生信息表';

-- ----------------------------
-- Table structure for sys_score
-- ----------------------------
DROP TABLE IF EXISTS `sys_score`;

CREATE TABLE `sys_score` (
  `score_id` bigint NOT NULL AUTO_INCREMENT COMMENT '成绩ID',
  `student_no` varchar(50) NOT NULL COMMENT '学号',
  `course_name` varchar(100) NOT NULL COMMENT '课程名称',
  `score_value` decimal(5,1) DEFAULT NULL COMMENT '成绩',
  `full_score` decimal(5,1) DEFAULT '100.0' COMMENT '满分',
  `exam_type` char(1) DEFAULT '0' COMMENT '考试类型（0期中考试 1期末考试 2月考 3平时成绩）',
  `exam_date` date DEFAULT NULL COMMENT '考试时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`score_id`),
  KEY `idx_score_student` (`student_no`),
  KEY `idx_score_exam_type` (`exam_type`),
  KEY `idx_score_exam_date` (`exam_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成绩信息表';

-- ----------------------------
-- 菜单权限 SQL（sys_menu 表）
-- 前提：已执行 zhihuitong.sql，其中已包含 "学生管理" 一级目录（menu_id=2040）
-- 如果没有该目录，请先执行下面的 INSERT（已注释）：
-- INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
-- VALUES ('学生管理', 0, 1, 'student', NULL, '', '', 1, 0, 'M', '0', '0', '', 'peoples', 'admin', NOW(), '学生管理目录');
-- ----------------------------

-- 获取学生管理目录的 menu_id
SET @student_parent_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '学生管理' AND menu_type = 'M' LIMIT 1);

-- 二级菜单：学生信息
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('学生信息', @student_parent_id, 1, 'index', 'student/index', '', '', 1, 0, 'C', '0', '0', 'student:info:list', 'user', 'admin', NOW(), '学生信息菜单');

-- 按钮权限
SET @info_menu_id = LAST_INSERT_ID();

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('学生信息查询', @info_menu_id, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'student:info:query', '#', 'admin', NOW(), '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('学生信息新增', @info_menu_id, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'student:info:add', '#', 'admin', NOW(), '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('学生信息修改', @info_menu_id, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'student:info:edit', '#', 'admin', NOW(), '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('学生信息删除', @info_menu_id, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'student:info:remove', '#', 'admin', NOW(), '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('学生信息导出', @info_menu_id, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'student:info:export', '#', 'admin', NOW(), '');

-- ----------------------------
-- 成绩管理菜单
-- ----------------------------
-- 二级菜单：成绩管理
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('成绩管理', @student_parent_id, 2, 'score', 'student/score/index', '', '', 1, 0, 'C', '0', '0', 'student:score:list', 'score', 'admin', NOW(), '成绩管理菜单');

SET @score_menu_id = LAST_INSERT_ID();

-- 按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('成绩查询', @score_menu_id, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'student:score:query', '#', 'admin', NOW(), '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('成绩新增', @score_menu_id, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'student:score:add', '#', 'admin', NOW(), '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('成绩修改', @score_menu_id, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'student:score:edit', '#', 'admin', NOW(), '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('成绩删除', @score_menu_id, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'student:score:remove', '#', 'admin', NOW(), '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('成绩导出', @score_menu_id, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'student:score:export', '#', 'admin', NOW(), '');
