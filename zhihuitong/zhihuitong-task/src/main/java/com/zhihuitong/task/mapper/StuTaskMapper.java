package com.zhihuitong.task.mapper;

import java.time.LocalDateTime;
import java.util.List;
import com.zhihuitong.task.domain.StuTask;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务管理Mapper接口
 * 
 * @author ruoyi
 * @date 2026-05-09
 */
@Mapper
public interface StuTaskMapper 
{
    List<StuTask> selectTodoRemindRange(@Param("begin") LocalDateTime begin,
                                        @Param("end") LocalDateTime end);
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
     * 删除任务管理
     * 
     * @param id 任务管理主键
     * @return 结果
     */
    public int deleteStuTaskById(Long id);

    /**
     * 批量删除任务管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStuTaskByIds(Long[] ids);

    List<StuTask> selectUndueTodoCalendar(@Param("studentId") Integer studentId, @Param("begin") LocalDateTime begin,
                                          @Param("end") LocalDateTime end);



    /**
     * 更新任务的邮件发送状态
     * @param id 任务ID
     * @return 更新结果
     */
    public int updateEmailSentStatus(@Param("id") Long id);
}
