package com.zhihuitong.student.service.impl;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhihuitong.common.utils.StringUtils;
import com.zhihuitong.common.utils.SecurityUtils;
import com.zhihuitong.common.core.domain.entity.SysUser;
import com.zhihuitong.student.domain.Score;
import com.zhihuitong.system.domain.SysUserRole;
import com.zhihuitong.student.domain.Student;
import com.zhihuitong.student.mapper.ScoreMapper;
import com.zhihuitong.student.mapper.StudentMapper;
import com.zhihuitong.system.mapper.SysUserMapper;
import com.zhihuitong.system.mapper.SysUserRoleMapper;
import com.zhihuitong.student.service.IStudentService;

@Service
public class StudentServiceImpl implements IStudentService
{
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String[] DEFAULT_COURSES = {
        "软件工程导论", "面向对象程序设计(Java)", "数据结构与算法",
        "数据库系统原理", "操作系统", "计算机网络",
        "软件需求工程", "软件测试技术", "软件项目管理", "Web应用开发"
    };

    @Override
    public List<Student> selectStudentList(Student student)
    {
        return studentMapper.selectStudentList(student);
    }

    @Override
    public Student selectStudentByNo(String studentNo)
    {
        return studentMapper.selectStudentByNo(studentNo);
    }

    @Override
    @Transactional
    public int insertStudent(Student student)
    {
        SysUser user = new SysUser();
        user.setUserName(student.getStudentNo());
        user.setNickName(student.getStudentName());
        user.setPassword(SecurityUtils.encryptPassword("123456"));
        user.setEmail(student.getEmail());
        user.setPhonenumber(student.getPhone());
        user.setSex(student.getGender());
        user.setStatus("0");
        user.setCreateBy(student.getCreateBy());
        userMapper.insertUser(user);

        Long userId = user.getUserId();
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(2L);
        List<SysUserRole> roleList = new ArrayList<>();
        roleList.add(userRole);
        userRoleMapper.batchUserRole(roleList);

        student.setUserId(userId);
        student.setStatus("0");
        int result = studentMapper.insertStudent(student);

        String studentNo = student.getStudentNo();
        List<String> courseNames = studentMapper.selectCourseNamesByStudentNo(studentNo);
        if (courseNames == null || courseNames.isEmpty())
        {
            courseNames = List.of(DEFAULT_COURSES);
        }
        LocalDate semesterStart = LocalDate.of(2025, 9, 1);
        LocalDate semesterEnd = LocalDate.of(2026, 7, 31);
        long totalDays = semesterStart.until(semesterEnd).getDays();

        for (String courseName : courseNames)
        {
            Score score = new Score();
            score.setStudentNo(studentNo);
            score.setCourseName(courseName);
            int randomScore = RANDOM.nextInt(71) + 30;
            score.setScoreValue(BigDecimal.valueOf(randomScore));
            score.setFullScore(BigDecimal.valueOf(100));
            score.setExamType(String.valueOf(RANDOM.nextInt(4)));
            LocalDate randomDate = semesterStart.plusDays(RANDOM.nextLong(totalDays));
            score.setExamDate(java.util.Date.from(randomDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            scoreMapper.insertScore(score);
        }

        return result;
    }

    @Override
    public int updateStudent(Student student)
    {
        return studentMapper.updateStudent(student);
    }

    @Override
    @Transactional
    public int deleteStudentByNo(String studentNo)
    {
        Student student = studentMapper.selectStudentByNo(studentNo);
        scoreMapper.deleteScoreByStudentNo(studentNo);
        if (student != null && student.getUserId() != null)
        {
            userMapper.deleteUserById(student.getUserId());
        }
        return studentMapper.deleteStudentByNo(studentNo);
    }

    @Override
    @Transactional
    public int deleteStudentByNos(String[] studentNos)
    {
        for (String studentNo : studentNos)
        {
            Student student = studentMapper.selectStudentByNo(studentNo);
            scoreMapper.deleteScoreByStudentNo(studentNo);
            if (student != null && student.getUserId() != null)
            {
                userMapper.deleteUserById(student.getUserId());
            }
        }
        return studentMapper.deleteStudentByNos(studentNos);
    }

    @Override
    public boolean checkStudentNoUnique(Student student)
    {
        Student info = studentMapper.selectStudentByNo(student.getStudentNo());
        return info == null;
    }
}
