package com.zhihuitong.task.controller;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhihuitong.common.annotation.Log;
import com.zhihuitong.common.core.controller.BaseController;
import com.zhihuitong.common.core.domain.AjaxResult;
import com.zhihuitong.common.enums.BusinessType;
import com.zhihuitong.task.domain.StuTask;
import com.zhihuitong.task.service.IStuTaskService;
import com.zhihuitong.common.utils.poi.ExcelUtil;
import com.zhihuitong.common.core.page.TableDataInfo;

/**
 * 任务管理Controller
 * 
 * @author ruoyi
 * @date 2026-05-09
 */
@RestController
@RequestMapping("/task/task")
public class StuTaskController extends BaseController
{
    @Autowired
    private IStuTaskService stuTaskService;

    /**
     * 查询任务管理列表
     */
    @PreAuthorize("@ss.hasPermi('task:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(StuTask stuTask)
    {
        startPage();
        List<StuTask> list = stuTaskService.selectStuTaskList(stuTask);
        return getDataTable(list);
    }

    /**
     * 导出任务管理列表
     */
    @PreAuthorize("@ss.hasPermi('task:task:export')")
    @Log(title = "任务管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StuTask stuTask)
    {
        List<StuTask> list = stuTaskService.selectStuTaskList(stuTask);
        ExcelUtil<StuTask> util = new ExcelUtil<StuTask>(StuTask.class);
        util.exportExcel(response, list, "任务管理数据");
    }

    /**
     * 获取任务管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('task:task:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(stuTaskService.selectStuTaskById(id));
    }

    /**
     * 新增任务管理
     */
    @PreAuthorize("@ss.hasPermi('task:task:add')")
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StuTask stuTask)
    {
        return toAjax(stuTaskService.insertStuTask(stuTask));
    }

    /**
     * 修改任务管理
     */
    @PreAuthorize("@ss.hasPermi('task:task:edit')")
    @Log(title = "任务管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StuTask stuTask)
    {
        return toAjax(stuTaskService.updateStuTask(stuTask));
    }

    /**
     * 删除任务管理
     */
    @PreAuthorize("@ss.hasPermi('task:task:remove')")
    @Log(title = "任务管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(stuTaskService.deleteStuTaskByIds(ids));
    }


    @PreAuthorize("@ss.hasPermi('task:task:list')")
    @GetMapping("/remind/calendar")
    public TableDataInfo remindCalendar(String month, Integer studentId)
    {
        YearMonth ym;
        try{
            ym = month == null || month.isBlank() ? YearMonth.now() : YearMonth.parse(month);
        }
        catch (DateTimeParseException e){
            ym = YearMonth.now();
        }

        LocalDateTime begin = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.plusMonths(1).atDay(1).atStartOfDay();
        List<StuTask> list = stuTaskService.selectUndueTodoCalendar(studentId, begin, end);
        return getDataTable(list);
    }
}
