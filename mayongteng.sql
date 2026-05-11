-- =============================================
-- 1. 创建任务表 stu_task
-- =============================================
DROP TABLE IF EXISTS `stu_task`;
CREATE TABLE `stu_task` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` INT NOT NULL COMMENT '学生ID（对应学号/用户ID）',
  `type` VARCHAR(20) NOT NULL COMMENT '任务类型（HOMEWORK=作业 EXAM=考试 TODO=待办）',
  `title` VARCHAR(200) NOT NULL COMMENT '任务标题',
  `content` VARCHAR(2000) NULL COMMENT '任务内容/备注',
  `term` VARCHAR(20) NULL COMMENT '学期标识（示例：2025-2026-1）',
  `course_id` INT NULL COMMENT '关联课程ID',
  `course_name_snapshot` VARCHAR(100) NULL COMMENT '课程名称快照',
  `priority` VARCHAR(10) NOT NULL DEFAULT 'MEDIUM' COMMENT '优先级（LOW/MEDIUM/HIGH）',
  `status` VARCHAR(10) NOT NULL DEFAULT 'TODO' COMMENT '状态（TODO/DOING/DONE/CANCEL）',
  `due_time` DATETIME NULL COMMENT '截止时间',
  `remind_enabled` INT NOT NULL DEFAULT 0 COMMENT '是否开启提醒（0否 1是）',
  `remind_time` DATETIME NULL COMMENT '提醒时间',
  `assignee_email` VARCHAR(200) DEFAULT NULL COMMENT '负责人邮箱',
  `email_sent` TINYINT DEFAULT 0 COMMENT '邮件发送状态: 0未发送 1已发送',
  `email_sent_time` DATETIME DEFAULT NULL COMMENT '邮件发送时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_task_student_status_due` (`student_id`, `status`, `due_time`),
  KEY `idx_task_student_term_course` (`student_id`, `term`, `course_id`),
  KEY `idx_task_student_type` (`student_id`, `type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生任务待办表';

-- =============================================
-- 2. 插入测试数据
-- =============================================
INSERT INTO `stu_task`
(`student_id`, `type`, `title`, `content`, `term`, `course_id`, `course_name_snapshot`, `priority`, `status`, `due_time`, `remind_enabled`, `remind_time`, `assignee_email`)
VALUES
(10001, 'HOMEWORK', '完成《软件工程导论》作业1', '提交需求分析小结（不少于800字）', '2025-2026-1', NULL, '软件工程导论', 'HIGH', 'TODO', DATE_ADD(NOW(), INTERVAL 2 DAY), 1, DATE_ADD(NOW(), INTERVAL 1 DAY), 'test@qq.com'),
(10001, 'EXAM', '《数据结构与算法》阶段测验', '范围：线性表、栈、队列；携带学生证', '2025-2026-1', NULL, '数据结构与算法', 'HIGH', 'TODO', DATE_ADD(NOW(), INTERVAL 7 DAY), 1, DATE_ADD(NOW(), INTERVAL 6 DAY), 'test@qq.com'),
(10001, 'TODO', '整理本周课堂笔记', '把周一到周五的课堂笔记补齐并打标签', '2025-2026-1', NULL, NULL, 'MEDIUM', 'DOING', DATE_ADD(NOW(), INTERVAL 3 DAY), 0, NULL, 'test@qq.com'),
(10001, 'HOMEWORK', '数据库实验：索引与查询优化', '完成实验报告并附 explain 截图', '2025-2026-1', NULL, '数据库系统原理', 'MEDIUM', 'TODO', DATE_ADD(NOW(), INTERVAL 4 DAY), 1, DATE_ADD(NOW(), INTERVAL 3 DAY), 'test@qq.com'),
(10001, 'TODO', '准备下周项目组例会', '整理进度、风险、下周计划', '2025-2026-1', NULL, NULL, 'LOW', 'TODO', DATE_ADD(NOW(), INTERVAL 5 DAY), 0, NULL, 'test@qq.com'),
(10001, 'TODO', '课表数据自查', '检查课程节次与地点是否有冲突', '2025-2026-1', NULL, NULL, 'LOW', 'DONE', DATE_ADD(NOW(), INTERVAL -1 DAY), 0, NULL, 'test@qq.com');

-- =============================================
-- 3. 菜单管理（正确：父菜单=目录 M）
-- =============================================
-- 3.1 创建【目录】任务代办
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, icon, create_by, create_time)
VALUES ('任务代办', 0, 10, '#', 'M', '0', '0', 'clipboard', 'admin', NOW());

SET @parent_id = (SELECT IFNULL(menu_id,0) FROM sys_menu WHERE menu_name = '任务代办' AND parent_id = 0 LIMIT 1);

-- 3.2 创建子菜单【任务管理】
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('任务管理', @parent_id, 1, 'manage', 'task/task/index', 1, 0, 'C', '0', '0', 'task:task:list', 'list', 'admin', NOW(), '任务管理菜单');

SET @task_menu_id = (SELECT IFNULL(menu_id,0) FROM sys_menu WHERE menu_name = '任务管理' AND parent_id = @parent_id LIMIT 1);

-- 3.3 创建子菜单【代办提醒】
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('代办提醒', @parent_id, 2, 'remind', 'task/remind/index', '', 'TaskRemind', 1, 0, 'C', '0', '0', 'task:task:list', 'calendar', 'admin', NOW(), '代办提醒（月历）');

-- =============================================
-- 4. 按钮权限
-- =============================================
DELETE FROM sys_menu WHERE parent_id = @task_menu_id AND menu_type = 'F';

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('任务管理查询', @task_menu_id, 1, '#', '', 1, 0, 'F', '0', '0', 'task:task:query', '#', 'admin', NOW()),
('任务管理新增', @task_menu_id, 2, '#', '', 1, 0, 'F', '0', '0', 'task:task:add', '#', 'admin', NOW()),
('任务管理修改', @task_menu_id, 3, '#', '', 1, 0, 'F', '0', '0', 'task:task:edit', '#', 'admin', NOW()),
('任务管理删除', @task_menu_id, 4, '#', '', 1, 0, 'F', '0', '0', 'task:task:remove', '#', 'admin', NOW()),
('任务管理导出', @task_menu_id, 5, '#', '', 1, 0, 'F', '0', '0', 'task:task:export', '#', 'admin', NOW());

-- =============================================
-- 5. 权限分配
-- =============================================
SET @remind_id = (SELECT IFNULL(menu_id,0) FROM sys_menu WHERE menu_name = '代办提醒' LIMIT 1);

INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES (1, @parent_id),(1, @task_menu_id),(1, @remind_id);
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) SELECT 1, menu_id FROM sys_menu WHERE parent_id = @task_menu_id;

-- =============================================
-- 6. 验证
-- =============================================
SELECT 
    m1.menu_id,
    m1.menu_name,
    m1.menu_type,
    m1.parent_id,
    m2.menu_name parent_name
FROM sys_menu m1
LEFT JOIN sys_menu m2 ON m1.parent_id = m2.menu_id
WHERE m1.menu_name LIKE '%任务%' OR m1.parent_id = @parent_id
ORDER BY m1.parent_id, m1.order_num;