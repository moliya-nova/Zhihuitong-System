import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, TaskQueryParams, StuTask } from '@/types'

// 查询任务待办列表
export function listTask(query: TaskQueryParams): Promise<TableDataInfo<StuTask[]>> {
  return request({
    url: '/task/task/list',
    method: 'get',
    params: query
  })
}

// 查询任务待办详细
export function getTask(id: number): Promise<AjaxResult<StuTask>> {
  return request({
    url: '/task/task/' + id,
    method: 'get'
  })
}

// 新增任务待办
export function addTask(data: StuTask): Promise<AjaxResult> {
  return request({
    url: '/task/task',
    method: 'post',
    data: data
  })
}

// 修改任务待办
export function updateTask(data: StuTask): Promise<AjaxResult> {
  return request({
    url: '/task/task',
    method: 'put',
    data: data
  })
}

// 删除任务待办
export function delTask(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/task/task/' + id,
    method: 'delete'
  })
}

export function listTodoCalendarRemind(params: { month?: string; studentId?: number }): Promise<TableDataInfo<StuTask[]>> {
  return request({
    url: '/task/task/remind/calendar',
    method: 'get',
    params
  })
}


