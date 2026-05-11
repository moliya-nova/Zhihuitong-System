import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, ScoreQueryParams, Score } from '@/types'

// 查询成绩列表
export function listScore(query: ScoreQueryParams): Promise<TableDataInfo<Score[]>> {
  return request({
    url: '/student/score/list',
    method: 'get',
    params: query
  })
}

// 查询成绩详细
export function getScore(scoreId: number): Promise<AjaxResult<Score>> {
  return request({
    url: '/student/score/' + scoreId,
    method: 'get'
  })
}

// 新增成绩
export function addScore(data: Score): Promise<AjaxResult> {
  return request({
    url: '/student/score',
    method: 'post',
    data: data
  })
}

// 修改成绩
export function updateScore(data: Score): Promise<AjaxResult> {
  return request({
    url: '/student/score',
    method: 'put',
    data: data
  })
}

// 删除成绩
export function delScore(scoreIds: string | number | (string | number)[]): Promise<AjaxResult> {
  return request({
    url: '/student/score/' + scoreIds,
    method: 'delete'
  })
}

// 导出成绩
export function exportScore(query: ScoreQueryParams): Promise<void> {
  return request({
    url: '/student/score/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}
