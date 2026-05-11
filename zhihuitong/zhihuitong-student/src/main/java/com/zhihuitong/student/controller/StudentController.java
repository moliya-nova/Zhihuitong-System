package com.zhihuitong.student.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhihuitong.common.annotation.Log;
import com.zhihuitong.common.core.controller.BaseController;
import com.zhihuitong.common.core.domain.AjaxResult;
import com.zhihuitong.common.core.page.TableDataInfo;
import com.zhihuitong.common.enums.BusinessType;
import com.zhihuitong.common.utils.poi.ExcelUtil;
import com.zhihuitong.student.domain.Student;
import com.zhihuitong.student.service.IStudentService;

@RestController
@RequestMapping("/student")
public class StudentController extends BaseController
{
    @Autowired
    private IStudentService studentService;

    @PreAuthorize("@ss.hasPermi('student:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(Student student)
    {
        startPage();
        List<Student> list = studentService.selectStudentList(student);
        return getDataTable(list);
    }

    @Log(title = "学生信息", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('student:info:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, Student student)
    {
        List<Student> list = studentService.selectStudentList(student);
        ExcelUtil<Student> util = new ExcelUtil<Student>(Student.class);
        util.exportExcel(response, list, "学生数据");
    }

    @PreAuthorize("@ss.hasPermi('student:info:query')")
    @GetMapping(value = "/{studentNo}")
    public AjaxResult getInfo(@PathVariable String studentNo)
    {
        return success(studentService.selectStudentByNo(studentNo));
    }

    @PreAuthorize("@ss.hasPermi('student:info:add')")
    @Log(title = "学生信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Student student)
    {
        if (!studentService.checkStudentNoUnique(student))
        {
            return error("新增学生'" + student.getStudentName() + "'失败，学号'" + student.getStudentNo() + "'已存在");
        }
        return toAjax(studentService.insertStudent(student));
    }

    @PreAuthorize("@ss.hasPermi('student:info:edit')")
    @Log(title = "学生信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Student student)
    {
        if (!studentService.checkStudentNoUnique(student))
        {
            return error("修改学生'" + student.getStudentName() + "'失败，学号'" + student.getStudentNo() + "'已存在");
        }
        return toAjax(studentService.updateStudent(student));
    }

    @PreAuthorize("@ss.hasPermi('student:info:remove')")
    @Log(title = "学生信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{studentNos}")
    public AjaxResult remove(@PathVariable String[] studentNos)
    {
        return toAjax(studentService.deleteStudentByNos(studentNos));
    }
}
