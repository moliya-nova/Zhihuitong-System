package com.zhihuitong.student.service;

import java.util.List;
import com.zhihuitong.student.domain.Student;

public interface IStudentService
{
    public List<Student> selectStudentList(Student student);

    public Student selectStudentByNo(String studentNo);

    public int insertStudent(Student student);

    public int updateStudent(Student student);

    public int deleteStudentByNo(String studentNo);

    public int deleteStudentByNos(String[] studentNos);

    public boolean checkStudentNoUnique(Student student);
}
