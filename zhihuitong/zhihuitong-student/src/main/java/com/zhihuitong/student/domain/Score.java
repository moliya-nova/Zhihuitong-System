package com.zhihuitong.student.domain;

import java.math.BigDecimal;
import java.util.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhihuitong.common.annotation.Excel;
import com.zhihuitong.common.annotation.Excel.ColumnType;
import com.zhihuitong.common.core.domain.BaseEntity;

public class Score extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "成绩ID", cellType = ColumnType.NUMERIC)
    private Long scoreId;

    @NotBlank(message = "学号不能为空")
    private String studentNo;

    @Excel(name = "姓名")
    private String studentName;

    @Excel(name = "班级")
    private String className;

    @Excel(name = "年级")
    private String grade;

    @Excel(name = "课程名称")
    @NotBlank(message = "课程名称不能为空")
    private String courseName;

    @Excel(name = "成绩", cellType = ColumnType.NUMERIC)
    @NotNull(message = "成绩不能为空")
    @DecimalMin(value = "0", message = "成绩不能小于0")
    @DecimalMax(value = "100", message = "成绩不能大于100")
    private BigDecimal scoreValue;

    @Excel(name = "满分", cellType = ColumnType.NUMERIC)
    private BigDecimal fullScore;

    @Excel(name = "考试类型", readConverterExp = "0=期中考试,1=期末考试,2=月考,3=平时成绩")
    private String examType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "考试时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date examDate;

    private String remark;

    public Score()
    {
    }

    public Score(Long scoreId)
    {
        this.scoreId = scoreId;
    }

    public Long getScoreId()
    {
        return scoreId;
    }

    public void setScoreId(Long scoreId)
    {
        this.scoreId = scoreId;
    }

    public String getStudentNo()
    {
        return studentNo;
    }

    public void setStudentNo(String studentNo)
    {
        this.studentNo = studentNo;
    }

    public String getStudentName()
    {
        return studentName;
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getGrade()
    {
        return grade;
    }

    public void setGrade(String grade)
    {
        this.grade = grade;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public BigDecimal getScoreValue()
    {
        return scoreValue;
    }

    public void setScoreValue(BigDecimal scoreValue)
    {
        this.scoreValue = scoreValue;
    }

    public BigDecimal getFullScore()
    {
        return fullScore;
    }

    public void setFullScore(BigDecimal fullScore)
    {
        this.fullScore = fullScore;
    }

    public String getExamType()
    {
        return examType;
    }

    public void setExamType(String examType)
    {
        this.examType = examType;
    }

    public Date getExamDate()
    {
        return examDate;
    }

    public void setExamDate(Date examDate)
    {
        this.examDate = examDate;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("scoreId", getScoreId())
            .append("studentNo", getStudentNo())
            .append("studentName", getStudentName())
            .append("className", getClassName())
            .append("grade", getGrade())
            .append("courseName", getCourseName())
            .append("scoreValue", getScoreValue())
            .append("fullScore", getFullScore())
            .append("examType", getExamType())
            .append("examDate", getExamDate())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .toString();
    }
}
