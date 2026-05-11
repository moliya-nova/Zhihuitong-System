package com.zhihuitong.student.mapper;

import java.util.List;
import com.zhihuitong.student.domain.Student;

public interface StudentMapper
{
    public List<Student> selectStudentList(Student student);

    public Student selectStudentByNo(String studentNo);

    public Student selectStudentByUserId(Long userId);

    public int insertStudent(Student student);

    public int updateStudent(Student student);

    public int deleteStudentByNo(String studentNo);

    public int deleteStudentByNos(String[] studentNos);

    public int checkStudentNoUnique(String studentNo);

    public int checkUserIdUnique(Long userId);

    public int selectStudentCount();

    public List<String> selectCourseNamesByStudentNo(String studentNo);
}
