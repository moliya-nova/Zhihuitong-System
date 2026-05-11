package com.zhihuitong.task.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.zhihuitong.common.annotation.Excel;
import com.zhihuitong.common.core.domain.BaseEntity;

/**
 * 任务管理对象 stu_task
 * 
 * @author ruoyi
 * @date 2026-05-09
 */
public class StuTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 学生ID（对应学号/用户ID） */
    @Excel(name = "学生ID", readConverterExp = "对=应学号/用户ID")
    private Long studentId;

    /** 任务类型（HOMEWORK=作业 EXAM=考试 TODO=待办） */
    @Excel(name = "任务类型", readConverterExp = "H=OMEWORK=作业,E=XAM=考试,T=ODO=待办")
    private String type;

    /** 任务标题 */
    @Excel(name = "任务标题")
    private String title;

    /** 任务内容/备注 */
    @Excel(name = "任务内容/备注")
    private String content;

    /** 学期标识（可选，示例：2025-2026-1） */
    @Excel(name = "学期标识", readConverterExp = "可=选，示例：2025-2026-1")
    private String term;

    /** 关联课程ID（可选，对应 stu_course.id） */
    @Excel(name = "关联课程ID", readConverterExp = "可=选，对应,s=tu_course.id")
    private Long courseId;

    /** 课程名称快照（可选） */
    @Excel(name = "课程名称快照", readConverterExp = "可=选")
    private String courseNameSnapshot;

    /** 优先级（LOW/MEDIUM/HIGH） */
    @Excel(name = "优先级", readConverterExp = "L=OW/MEDIUM/HIGH")
    private String priority;

    /** 状态（TODO/DOING/DONE/CANCEL） */
    @Excel(name = "状态", readConverterExp = "T=ODO/DOING/DONE/CANCEL")
    private String status;

    /** 截止时间/考试时间（可为空） */
    @Excel(name = "截止时间/考试时间", readConverterExp = "可=为空")
    private Date dueTime;

    /** 是否开启提醒（0否 1是） */
    @Excel(name = "是否开启提醒", readConverterExp = "0=否,1=是")
    private Long remindEnabled;

    /** 提醒时间（可为空） */
    @Excel(name = "提醒时间", readConverterExp = "可=为空")
    private Date remindTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedAt;


    /** 负责人邮箱 */
    private String assigneeEmail;

    /** 邮件是否已发送 */
    private Integer emailSent;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setStudentId(Long studentId) 
    {
        this.studentId = studentId;
    }

    public Long getStudentId() 
    {
        return studentId;
    }

    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setTerm(String term) 
    {
        this.term = term;
    }

    public String getTerm() 
    {
        return term;
    }

    public void setCourseId(Long courseId) 
    {
        this.courseId = courseId;
    }

    public Long getCourseId() 
    {
        return courseId;
    }

    public void setCourseNameSnapshot(String courseNameSnapshot) 
    {
        this.courseNameSnapshot = courseNameSnapshot;
    }

    public String getCourseNameSnapshot() 
    {
        return courseNameSnapshot;
    }

    public void setPriority(String priority) 
    {
        this.priority = priority;
    }

    public String getPriority() 
    {
        return priority;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setDueTime(Date dueTime) 
    {
        this.dueTime = dueTime;
    }

    public Date getDueTime() 
    {
        return dueTime;
    }

    public void setRemindEnabled(Long remindEnabled) 
    {
        this.remindEnabled = remindEnabled;
    }

    public Long getRemindEnabled() 
    {
        return remindEnabled;
    }

    public void setRemindTime(Date remindTime) 
    {
        this.remindTime = remindTime;
    }

    public Date getRemindTime() 
    {
        return remindTime;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    public void setUpdatedAt(Date updatedAt) 
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() 
    {
        return updatedAt;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public void setAssigneeEmail(String assigneeEmail) {
        this.assigneeEmail = assigneeEmail;
    }

    public Integer getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(Integer emailSent) {
        this.emailSent = emailSent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("studentId", getStudentId())
            .append("type", getType())
            .append("title", getTitle())
            .append("content", getContent())
            .append("term", getTerm())
            .append("courseId", getCourseId())
            .append("courseNameSnapshot", getCourseNameSnapshot())
            .append("priority", getPriority())
            .append("status", getStatus())
            .append("dueTime", getDueTime())
            .append("remindEnabled", getRemindEnabled())
            .append("remindTime", getRemindTime())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .toString();
    }
}
