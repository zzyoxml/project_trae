package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 单词表 edu_vocabulary
 * 用于存储多语言学习的单词资源
 *
 * @author LingLearn
 */
public class EduVocabulary extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 单词ID
     */
    private Long vocabId;

    /**
     * 单元ID
     */
    private Long unitId;

    /**
     * 课时ID
     */
    private Long lessonId;

    /**
     * 单词
     */
    private String word;

    /**
     * 语言：en,ja,zh
     */
    private String language;

    /**
     * 发音/音标
     */
    private String pronunciation;

    /**
     * 音频URL
     */
    private String audioUrl;

    /**
     * 词性：noun,verb,adj,adv等
     */
    private String partOfSpeech;

    /**
     * 等级
     */
    private String level;

    /**
     * 释义
     */
    private String definition;

    /**
     * 例句（JSON数组格式）
     */
    private String exampleSentences;

    /**
     * 同义词（JSON数组格式）
     */
    private String synonyms;

    /**
     * 反义词（JSON数组格式）
     */
    private String antonyms;

    /**
     * 词族（名词、动词、形容词等变形，JSON格式）
     */
    private String wordFamily;

    /**
     * 难度系数（1-5）
     */
    private Integer difficulty;

    /**
     * 使用频率
     */
    private Integer frequency;

    /**
     * 标签（多个用逗号分隔）
     */
    private String tags;

    /**
     * 配图URL
     */
    private String imageUrl;

    /**
     * 用户掌握程度（非数据库字段）
     */
    private Integer masteryLevel;

    /**
     * 是否已学习（非数据库字段）
     */
    private Boolean isLearned;

    /**
     * 单元ID列表（逗号分隔，非数据库字段）
     */
    private String unitIds;

    /**
     * 删除标志
     */
    private String delFlag;

    public Long getVocabId() {
        return vocabId;
    }

    public void setVocabId(Long vocabId) {
        this.vocabId = vocabId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public String getUnitIds() {
        return unitIds;
    }

    public void setUnitIds(String unitIds) {
        this.unitIds = unitIds;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExampleSentences() {
        return exampleSentences;
    }

    public void setExampleSentences(String exampleSentences) {
        this.exampleSentences = exampleSentences;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public String getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(String antonyms) {
        this.antonyms = antonyms;
    }

    public String getWordFamily() {
        return wordFamily;
    }

    public void setWordFamily(String wordFamily) {
        this.wordFamily = wordFamily;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getMasteryLevel() {
        return masteryLevel;
    }

    public void setMasteryLevel(Integer masteryLevel) {
        this.masteryLevel = masteryLevel;
    }

    public Boolean getIsLearned() {
        return isLearned;
    }

    public void setIsLearned(Boolean isLearned) {
        this.isLearned = isLearned;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "EduVocabulary{" +
                "vocabId=" + vocabId +
                ", word='" + word + '\'' +
                ", language='" + language + '\'' +
                ", pronunciation='" + pronunciation + '\'' +
                ", partOfSpeech='" + partOfSpeech + '\'' +
                ", level='" + level + '\'' +
                ", definition='" + definition + '\'' +
                ", difficulty=" + difficulty +
                '}';
    }
}
