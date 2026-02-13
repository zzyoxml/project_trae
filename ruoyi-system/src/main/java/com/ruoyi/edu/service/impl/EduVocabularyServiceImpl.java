package com.ruoyi.edu.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.edu.mapper.EduVocabularyMapper;
import com.ruoyi.edu.domain.EduVocabulary;
import com.ruoyi.edu.service.IEduVocabularyService;
import com.ruoyi.common.core.text.Convert;

/**
 * 词汇管理Service业务层处理
 * 
 * @author LingLearn
 */
@Service
public class EduVocabularyServiceImpl implements IEduVocabularyService {
    @Autowired
    private EduVocabularyMapper eduVocabularyMapper;

    /**
     * 查询词汇
     * 
     * @param vocabId 词汇ID
     * @return 词汇
     */
    @Override
    public EduVocabulary selectEduVocabularyById(Long vocabId) {
        return eduVocabularyMapper.selectEduVocabularyById(vocabId);
    }

    /**
     * 查询词汇列表
     * 
     * @param eduVocabulary 词汇
     * @return 词汇集合
     */
    @Override
    public List<EduVocabulary> selectEduVocabularyList(EduVocabulary eduVocabulary) {
        return eduVocabularyMapper.selectEduVocabularyList(eduVocabulary);
    }

    /**
     * 新增词汇
     * 
     * @param eduVocabulary 词汇
     * @return 结果
     */
    @Override
    public int insertEduVocabulary(EduVocabulary eduVocabulary) {
        return eduVocabularyMapper.insertEduVocabulary(eduVocabulary);
    }

    /**
     * 修改词汇
     * 
     * @param eduVocabulary 词汇
     * @return 结果
     */
    @Override
    public int updateEduVocabulary(EduVocabulary eduVocabulary) {
        return eduVocabularyMapper.updateEduVocabulary(eduVocabulary);
    }

    /**
     * 批量删除词汇
     * 
     * @param vocabIds 需要删除的词汇ID
     * @return 结果
     */
    @Override
    public int deleteEduVocabularyByIds(Long[] vocabIds) {
        return eduVocabularyMapper.deleteEduVocabularyByIds(vocabIds);
    }

    /**
     * 删除词汇信息
     * 
     * @param vocabId 词汇ID
     * @return 结果
     */
    @Override
    public int deleteEduVocabularyById(Long vocabId) {
        return eduVocabularyMapper.deleteEduVocabularyById(vocabId);
    }

    /**
     * 删除语言不匹配的词汇（词汇语言与课程语言不一致的记录）
     * 
     * @return 结果
     */
    @Override
    public int deleteVocabularyWithMismatchedLanguage() {
        return eduVocabularyMapper.deleteVocabularyWithMismatchedLanguage();
    }
}