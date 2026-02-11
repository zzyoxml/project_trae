package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduGrammar;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EduGrammarMapper {

    List<EduGrammar> selectEduGrammarByLanguage(String language);

    EduGrammar selectEduGrammarById(Long grammarId);

    List<EduGrammar> selectEduGrammarList(EduGrammar eduGrammar);

    int insertEduGrammar(EduGrammar eduGrammar);

    int updateEduGrammar(EduGrammar eduGrammar);

    int deleteEduGrammarById(Long grammarId);
}