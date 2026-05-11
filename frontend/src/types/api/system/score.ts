import type { BaseEntity } from "../common";

/** 成绩信息查询参数 */
export interface ScoreQueryParams {
  pageNum?: number;
  pageSize?: number;
  studentNo?: string;
  studentName?: string;
  courseName?: string;
  examType?: string;
  className?: string;
  grade?: string;
}

/** 成绩信息 */
export interface Score extends BaseEntity {
  scoreId?: number;
  studentNo?: string;
  studentName?: string;
  className?: string;
  grade?: string;
  courseName?: string;
  scoreValue?: number;
  fullScore?: number;
  examType?: string;
  examDate?: string;
  remark?: string;
}
