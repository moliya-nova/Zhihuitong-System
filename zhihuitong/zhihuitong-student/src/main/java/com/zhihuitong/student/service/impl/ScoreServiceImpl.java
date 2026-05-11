package com.zhihuitong.student.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhihuitong.common.utils.StringUtils;
import com.zhihuitong.student.domain.Score;
import com.zhihuitong.student.domain.Student;
import com.zhihuitong.student.mapper.ScoreMapper;
import com.zhihuitong.student.mapper.StudentMapper;
import com.zhihuitong.student.service.IScoreService;

@Service
public class ScoreServiceImpl implements IScoreService
{
    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Score> selectScoreList(Score score)
    {
        List<Score> list = scoreMapper.selectScoreList(score);
        for (Score s : list)
        {
            if (s.getStudentNo() != null)
            {
                Student student = studentMapper.selectStudentByNo(s.getStudentNo());
                if (student != null)
                {
                    s.setStudentName(student.getStudentName());
                    s.setClassName(student.getClassName());
                    s.setGrade(student.getGrade());
                }
            }
        }
        return list;
    }

    @Override
    public Score selectScoreById(Long scoreId)
    {
        Score score = scoreMapper.selectScoreById(scoreId);
        if (score != null && score.getStudentNo() != null)
        {
            Student student = studentMapper.selectStudentByNo(score.getStudentNo());
            if (student != null)
            {
                score.setStudentName(student.getStudentName());
                score.setClassName(student.getClassName());
                score.setGrade(student.getGrade());
            }
        }
        return score;
    }

    @Override
    public List<Score> selectScoreByStudentNo(String studentNo)
    {
        List<Score> list = scoreMapper.selectScoreByStudentNo(studentNo);
        Student student = studentMapper.selectStudentByNo(studentNo);
        if (student != null)
        {
            for (Score s : list)
            {
                s.setStudentName(student.getStudentName());
                s.setClassName(student.getClassName());
                s.setGrade(student.getGrade());
            }
        }
        return list;
    }

    @Override
    public int insertScore(Score score)
    {
        if (score.getFullScore() == null)
        {
            score.setFullScore(java.math.BigDecimal.valueOf(100));
        }
        return scoreMapper.insertScore(score);
    }

    @Override
    public int updateScore(Score score)
    {
        return scoreMapper.updateScore(score);
    }

    @Override
    public int deleteScoreById(Long scoreId)
    {
        return scoreMapper.deleteScoreById(scoreId);
    }

    @Override
    public int deleteScoreByIds(Long[] scoreIds)
    {
        return scoreMapper.deleteScoreByIds(scoreIds);
    }

    @Override
    public String importScore(List<Score> scoreList, boolean updateSupport, String operName)
    {
        if (StringUtils.isEmpty(scoreList) || scoreList.size() == 0)
        {
            throw new RuntimeException("导入成绩数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder failureMsg = new StringBuilder();
        for (Score score : scoreList)
        {
            try
            {
                Student student = studentMapper.selectStudentByNo(score.getStudentNo());
                if (student != null)
                {
                    Score existingScore = null;
                    List<Score> scores = scoreMapper.selectScoreByStudentNo(score.getStudentNo());
                    for (Score s : scores)
                    {
                        if (s.getCourseName().equals(score.getCourseName()) &&
                            s.getExamType().equals(score.getExamType()))
                        {
                            existingScore = s;
                            break;
                        }
                    }
                    if (existingScore == null)
                    {
                        scoreMapper.insertScore(score);
                        successNum++;
                    }
                    else if (updateSupport)
                    {
                        score.setScoreId(existingScore.getScoreId());
                        scoreMapper.updateScore(score);
                        successNum++;
                    }
                    else
                    {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、学号 " + score.getStudentNo() +
                                         " 的" + score.getCourseName() + "成绩已存在");
                    }
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、学号 " + score.getStudentNo() + " 不存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、学号 " + score.getStudentNo() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new RuntimeException(failureMsg.toString());
        }
        else
        {
            return "恭喜您，数据全部导入成功！共 " + successNum + " 条数据。";
        }
    }
}
