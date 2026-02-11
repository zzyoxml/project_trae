package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 语法规则表 edu_grammar
 * 存储语言语法规则和学习内容
 *
 * @author LingLearn
 */
public class EduGrammar extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 语法ID
     */
    private Long grammarId;

    /**
     * 语法名称
     */
    private String grammarName;

    /**
     * 语言：en,ja,zh
     */
    private String language;

    /**
     * 等级
     */
    private String level;

    /**
     * 分类：tense,structure,sentence等
     */
    private String category;

    /**
     * 规则描述
     */
    private String description;

    /**
     * 规则公式
     */
    private String ruleFormula;

    /**
     * 例句（JSON数组格式）
     */
    private String examples;

    /**
     * 使用注意事项
     */
    private String usageNotes;

    /**
     * 常见错误
     */
    private String commonMistakes;

    /**
     * 相关语法（多个用逗号分隔）
     */
    private String relatedGrammar;

    /**
     * 难度系数（1-5）
     */
    private Integer difficulty;

    public Long getGrammarId() {
        return grammarId;
    }

    public void setGrammarId(Long grammarId) {
        this.grammarId = grammarId;
    }

    public String getGrammarName() {
        return grammarName;
    }

    public void setGrammarName(String grammarName) {
        this.grammarName = grammarName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRuleFormula() {
        return ruleFormula;
    }

    public void setRuleFormula(String ruleFormula) {
        this.ruleFormula = ruleFormula;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public String getUsageNotes() {
        return usageNotes;
    }

    public void setUsageNotes(String usageNotes) {
        this.usageNotes = usageNotes;
    }

    public String getCommonMistakes() {
        return commonMistakes;
    }

    public void setCommonMistakes(String commonMistakes) {
        this.commonMistakes = commonMistakes;
    }

    public String getRelatedGrammar() {
        return relatedGrammar;
    }

    public void setRelatedGrammar(String relatedGrammar) {
        this.relatedGrammar = relatedGrammar;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "EduGrammar{" +
                "grammarId=" + grammarId +
                ", grammarName='" + grammarName + '\'' +
                ", language='" + language + '\'' +
                ", level='" + level + '\'' +
                ", category='" + category + '\'' +
                ", difficulty=" + difficulty +
                '}';
    }
}
