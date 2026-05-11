package com.zhihuitong.task.service;

import java.time.LocalDateTime;
import java.util.List;
import com.zhihuitong.task.domain.StuTask;

/**
 * 任务管理Service接口
 * 
 * @author ruoyi
 * @date 2026-05-09
 */
public interface IStuTaskService 
{
    /**
     * 查询任务管理
     * 
     * @param id 任务管理主键
     * @return 任务管理
     */
    public StuTask selectStuTaskById(Long id);

    /**
     * 查询任务管理列表
     * 
     * @param stuTask 任务管理
     * @return 任务管理集合
     */
    public List<StuTask> selectStuTaskList(StuTask stuTask);

    /**
     * 新增任务管理
     * 
     * @param stuTask 任务管理
     * @return 结果
     */
    public int insertStuTask(StuTask stuTask);

    /**
     * 修改任务管理
     * 
     * @param stuTask 任务管理
     * @return 结果
     */
    public int updateStuTask(StuTask stuTask);

    /**
     * 批量删除任务管理
     * 
     * @param ids 需要删除的任务管理主键集合
     * @return 结果
     */
    public int deleteStuTaskByIds(Long[] ids);

    /**
     * 删除任务管理信息
     * 
     * @param id 任务管理主键
     * @return 结果
     */
    public int deleteStuTaskById(Long id);

    List<StuTask> selectUndueTodoCalendar(Integer studentId, LocalDateTime begin, LocalDateTime end);
}
