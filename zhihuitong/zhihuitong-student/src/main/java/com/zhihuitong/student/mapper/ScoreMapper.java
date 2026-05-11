package com.zhihuitong.student.mapper;

import java.util.List;
import com.zhihuitong.student.domain.Score;

public interface ScoreMapper
{
    public List<Score> selectScoreList(Score score);

    public Score selectScoreById(Long scoreId);

    public List<Score> selectScoreByStudentNo(String studentNo);

    public int insertScore(Score score);

    public int updateScore(Score score);

    public int deleteScoreById(Long scoreId);

    public int deleteScoreByIds(Long[] scoreIds);

    public int deleteScoreByStudentNo(String studentNo);
}
