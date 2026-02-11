package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduVocabulary;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EduVocabularyMapper {

    List<EduVocabulary> selectEduVocabularyByLanguage(String language);

    EduVocabulary selectEduVocabularyById(Long vocabId);

    List<EduVocabulary> selectEduVocabularyList(EduVocabulary eduVocabulary);

    int insertEduVocabulary(EduVocabulary eduVocabulary);

    int updateEduVocabulary(EduVocabulary eduVocabulary);

    int deleteEduVocabularyById(Long vocabId);
}