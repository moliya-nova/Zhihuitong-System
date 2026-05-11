import type { PageDomain, BaseEntity } from "../common";

/** 任务管理配置分页查询参数 */
export interface TaskQueryParams extends PageDomain {
  /** 学生ID（对应学号/用户ID） */
  studentId?: number;
  /** 任务类型（HOMEWORK=作业 EXAM=考试 TODO=待办） */
  type?: string;
  /** 任务标题 */
  title?: string;
  /** 任务内容/备注 */
  content?: string;
}

/** 任务管理配置信息 */
export interface StuTask extends BaseEntity {
  /** 主键ID */
  id?: number;
  /** 学生ID（对应学号/用户ID） */
  studentId?: number;
  /** 任务类型（HOMEWORK=作业 EXAM=考试 TODO=待办） */
  type?: string;
  /** 任务标题 */
  title?: string;
  /** 任务内容/备注 */
  content?: string;
  /** 学期标识（可选，示例：2025-2026-1） */
  term?: string;
  /** 关联课程ID（可选，对应 stu_course.id） */
  courseId?: number;
  /** 课程名称快照（可选） */
  courseNameSnapshot?: string;
  /** 优先级（LOW/MEDIUM/HIGH） */
  priority?: string;
  /** 状态（TODO/DOING/DONE/CANCEL） */
  status?: string;
  /** 截止时间/考试时间（可为空） */
  dueTime?: string;
  /** 是否开启提醒（0否 1是） */
  remindEnabled?: number;
  /** 提醒时间（可为空） */
  remindTime?: string;
  /** 创建时间 */
  createdAt?: string;
  /** 更新时间 */
  updatedAt?: string;
}
