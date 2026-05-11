package com.zhihuitong.task.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhihuitong.task.mapper.StuTaskMapper;
import com.zhihuitong.task.domain.StuTask;
import com.zhihuitong.task.service.IStuTaskService;

/**
 * 任务管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-05-09
 */
@Service
public class StuTaskServiceImpl implements IStuTaskService 
{
    @Autowired
    private StuTaskMapper stuTaskMapper;

    /**
     * 查询任务管理
     * 
     * @param id 任务管理主键
     * @return 任务管理
     */
    @Override
    public StuTask selectStuTaskById(Long id)
    {
        return stuTaskMapper.selectStuTaskById(id);
    }

    /**
     * 查询任务管理列表
     * 
     * @param stuTask 任务管理
     * @return 任务管理
     */
    @Override
    public List<StuTask> selectStuTaskList(StuTask stuTask)
    {
        return stuTaskMapper.selectStuTaskList(stuTask);
    }

    /**
     * 新增任务管理
     * 
     * @param stuTask 任务管理
     * @return 结果
     */
    @Override
    public int insertStuTask(StuTask stuTask)
    {
        return stuTaskMapper.insertStuTask(stuTask);
    }

    /**
     * 修改任务管理
     * 
     * @param stuTask 任务管理
     * @return 结果
     */
    @Override
    public int updateStuTask(StuTask stuTask)
    {
        return stuTaskMapper.updateStuTask(stuTask);
    }

    /**
     * 批量删除任务管理
     * 
     * @param ids 需要删除的任务管理主键
     * @return 结果
     */
    @Override
    public int deleteStuTaskByIds(Long[] ids)
    {
        return stuTaskMapper.deleteStuTaskByIds(ids);
    }

    /**
     * 删除任务管理信息
     * 
     * @param id 任务管理主键
     * @return 结果
     */
    @Override
    public int deleteStuTaskById(Long id)
    {
        return stuTaskMapper.deleteStuTaskById(id);
    }

    @Override
    public List<StuTask> selectUndueTodoCalendar(Integer studentId, LocalDateTime begin, LocalDateTime end)
    {
        return stuTaskMapper.selectUndueTodoCalendar(studentId, begin, end);
    }
}
