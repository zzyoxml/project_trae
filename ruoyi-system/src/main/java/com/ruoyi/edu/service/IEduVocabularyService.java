package com.ruoyi.edu.service;

import java.util.List;
import com.ruoyi.edu.domain.EduVocabulary;

/**
 * 词汇管理Service接口
 * 
 * @author LingLearn
 */
public interface IEduVocabularyService {
    /**
     * 查询词汇
     * 
     * @param vocabId 词汇ID
     * @return 词汇
     */
    public EduVocabulary selectEduVocabularyById(Long vocabId);

    /**
     * 查询词汇列表
     * 
     * @param eduVocabulary 词汇
     * @return 词汇集合
     */
    public List<EduVocabulary> selectEduVocabularyList(EduVocabulary eduVocabulary);

    /**
     * 新增词汇
     * 
     * @param eduVocabulary 词汇
     * @return 结果
     */
    public int insertEduVocabulary(EduVocabulary eduVocabulary);

    /**
     * 修改词汇
     * 
     * @param eduVocabulary 词汇
     * @return 结果
     */
    public int updateEduVocabulary(EduVocabulary eduVocabulary);

    /**
     * 批量删除词汇
     * 
     * @param vocabIds 需要删除的词汇ID
     * @return 结果
     */
    public int deleteEduVocabularyByIds(Long[] vocabIds);

    /**
     * 删除词汇信息
     * 
     * @param vocabId 词汇ID
     * @return 结果
     */
    public int deleteEduVocabularyById(Long vocabId);

    /**
     * 删除语言不匹配的词汇（词汇语言与课程语言不一致的记录）
     * 
     * @return 结果
     */
    public int deleteVocabularyWithMismatchedLanguage();
}