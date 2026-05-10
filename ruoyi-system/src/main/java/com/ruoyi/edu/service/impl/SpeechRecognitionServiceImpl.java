package com.ruoyi.edu.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.edu.service.ISpeechRecognitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 语音识别服务实现
 * 
 * @description 基于Web Speech API和第三方语音识别API的实现
 * 支持多种语音识别引擎（可扩展）
 * @author LingLearn
 */
@Service
public class SpeechRecognitionServiceImpl implements ISpeechRecognitionService {

    private static final Logger log = LoggerFactory.getLogger(SpeechRecognitionServiceImpl.class);

    /**
     * 语音服务类型：local（浏览器本地）、baidu（百度）、aliyun（阿里云）等
     */
    @Value("${edu.speech.service.type:local}")
    private String serviceType;

    /**
     * 百度语音API配置
     */
    @Value("${edu.speech.baidu.appId:}")
    private String baiduAppId;

    @Value("${edu.speech.baidu.apiKey:}")
    private String baiduApiKey;

    @Value("${edu.speech.baidu.secretKey:}")
    private String baiduSecretKey;

    /**
     * 阿里云语音API配置
     */
    @Value("${edu.speech.aliyun.accessKeyId:}")
    private String aliyunAccessKeyId;

    @Value("${edu.speech.aliyun.accessKeySecret:}")
    private String aliyunAccessKeySecret;

    /**
     * 支持的语言映射
     */
    private static final Map<String, String> LANGUAGE_MAP = new HashMap<>();

    static {
        LANGUAGE_MAP.put("en", "en-US");
        LANGUAGE_MAP.put("ja", "ja-JP");
        LANGUAGE_MAP.put("zh", "zh-CN");
    }

    /**
     * 语言到语音引擎的映射
     */
    private static final Map<String, String> LANGUAGE_ENGINE_MAP = new HashMap<>();

    static {
        LANGUAGE_ENGINE_MAP.put("en", "english");
        LANGUAGE_ENGINE_MAP.put("ja", "japanese");
        LANGUAGE_ENGINE_MAP.put("zh", "chinese");
    }

    /**
     * 语音识别结果类
     */
    public static class RecognitionResult {
        private String text;          // 识别的文本
        private double confidence;    // 置信度
        private String language;      // 语言
        private long timestamp;      // 时间戳

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    /**
     * 发音评分结果类
     */
    public static class PronunciationScore {
        private double overallScore;        // 综合得分 (0-100)
        private double pronunciationScore;  // 发音得分
        private double fluencyScore;       // 流畅度得分
        private double accuracyScore;       // 准确度得分
        private String feedback;            // 反馈建议
        private Map<String, Double> wordScores; // 单词级别得分

        public double getOverallScore() {
            return overallScore;
        }

        public void setOverallScore(double overallScore) {
            this.overallScore = overallScore;
        }

        public double getPronunciationScore() {
            return pronunciationScore;
        }

        public void setPronunciationScore(double pronunciationScore) {
            this.pronunciationScore = pronunciationScore;
        }

        public double getFluencyScore() {
            return fluencyScore;
        }

        public void setFluencyScore(double fluencyScore) {
            this.fluencyScore = fluencyScore;
        }

        public double getAccuracyScore() {
            return accuracyScore;
        }

        public void setAccuracyScore(double accuracyScore) {
            this.accuracyScore = accuracyScore;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public Map<String, Double> getWordScores() {
            return wordScores;
        }

        public void setWordScores(Map<String, Double> wordScores) {
            this.wordScores = wordScores;
        }
    }

    /**
     * 语音转文字
     *
     * @param audioData 音频数据（Base64编码）
     * @param language 语言代码
     * @return 识别结果
     */
    @Override
    public Map<String, Object> recognizeSpeech(String audioData, String language) {
        try {
            log.info("开始语音识别，语言：{}", language);

            String engine = LANGUAGE_ENGINE_MAP.getOrDefault(language, "general");
            String locale = LANGUAGE_MAP.getOrDefault(language, "en-US");

            // 根据服务类型调用不同的识别服务
            Map<String, Object> result;
            switch (serviceType.toLowerCase()) {
                case "baidu":
                    result = recognizeByBaidu(audioData, locale);
                    break;
                case "aliyun":
                    result = recognizeByAliyun(audioData, locale);
                    break;
                case "local":
                default:
                    result = recognizeByLocal(audioData, locale, engine);
                    break;
            }

            log.info("语音识别完成，结果：{}", result);
            return result;

        } catch (Exception e) {
            log.error("语音识别失败：", e);
            throw new ServiceException("语音识别服务异常：" + e.getMessage());
        }
    }

    /**
     * 发音评分
     *
     * @param audioData     音频数据
     * @param referenceText 参考文本
     * @param language      语言代码
     * @return 评分结果
     */
    @Override
    public Map<String, Object> evaluatePronunciation(String audioData, String referenceText, String language) {
        try {
            log.info("开始发音评分，参考文本：{}，语言：{}", referenceText, language);

            // 1. 先进行语音识别
            Map<String, Object> recognitionResult = recognizeSpeech(audioData, language);
            String recognizedText = (String) recognitionResult.get("text");

            // 2. 计算相似度
            double similarity = calculateSimilarity(referenceText, recognizedText);

            // 3. 计算详细评分
            PronunciationScore score = new PronunciationScore();
            score.setOverallScore(Math.round(similarity * 100));
            score.setAccuracyScore(Math.round(similarity * 95));
            score.setPronunciationScore(Math.round(similarity * 90));
            score.setFluencyScore(Math.round(similarity * 85));

            // 4. 生成反馈
            String feedback = generateFeedback(score);
            score.setFeedback(feedback);

            // 5. 计算单词级别得分
            Map<String, Double> wordScores = calculateWordScores(referenceText, recognizedText);
            score.setWordScores(wordScores);

            Map<String, Object> result = new HashMap<>();
            result.put("recognizedText", recognizedText);
            result.put("referenceText", referenceText);
            result.put("similarity", similarity);
            result.put("score", score);

            log.info("发音评分完成，得分：{}", score.getOverallScore());
            return result;

        } catch (Exception e) {
            log.error("发音评分失败：", e);
            throw new ServiceException("发音评分服务异常：" + e.getMessage());
        }
    }

    /**
     * 文字转语音
     *
     * @param text     文本内容
     * @param language 语言代码
     * @return 音频URL或Base64编码的音频数据
     */
    @Override
    public String synthesizeSpeech(String text, String language) {
        try {
            log.info("开始语音合成，文本：{}，语言：{}", text, language);

            String engine = LANGUAGE_ENGINE_MAP.getOrDefault(language, "general");
            String locale = LANGUAGE_MAP.getOrDefault(language, "en-US");

            // 这里返回前端可用的音频URL或Base64数据
            // 实际项目中应该调用真实的TTS服务
            String audioUrl = synthesizeByService(text, locale, engine);

            log.info("语音合成完成");
            return audioUrl;

        } catch (Exception e) {
            log.error("语音合成失败：", e);
            throw new ServiceException("语音合成服务异常：" + e.getMessage());
        }
    }

    /**
     * 实时语音识别（流式）
     *
     * @param sessionId 会话ID
     * @param audioData 音频数据
     * @param language  语言代码
     * @return 识别结果
     */
    @Override
    public Map<String, Object> recognizeSpeechStream(String sessionId, String audioData, String language) {
        // 简化实现，实际项目中应该使用WebSocket进行流式传输
        return recognizeSpeech(audioData, language);
    }

    /**
     * 获取支持的语言列表
     *
     * @return 语言列表
     */
    @Override
    public Map<String, String> getSupportedLanguages() {
        Map<String, String> languages = new HashMap<>();
        languages.put("en", "英语 (English)");
        languages.put("ja", "日语 (日本語)");
        languages.put("zh", "中文 (普通话)");
        return languages;
    }

    /**
     * 检查服务可用性
     *
     * @return 是否可用
     */
    @Override
    public boolean isServiceAvailable() {
        // 可以添加健康检查逻辑
        return true;
    }

    // ==================== 私有方法 ====================

    /**
     * 本地语音识别（模拟实现）
     */
    private Map<String, Object> recognizeByLocal(String audioData, String locale, String engine) {
        // 实际项目中，这里应该调用浏览器端的Web Speech API
        // 或者使用前端录音后发送到这里进行处理
        
        Map<String, Object> result = new HashMap<>();
        result.put("text", "模拟识别文本");
        result.put("confidence", 0.95);
        result.put("language", locale);
        result.put("engine", engine);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 百度语音识别
     */
    private Map<String, Object> recognizeByBaidu(String audioData, String locale) {
        // 实现百度语音识别API调用
        // 需要申请百度语音识别服务并配置AppID、API Key、Secret Key
        
        Map<String, Object> result = new HashMap<>();
        result.put("text", "百度识别结果");
        result.put("confidence", 0.98);
        result.put("language", locale);
        result.put("provider", "baidu");
        return result;
    }

    /**
     * 阿里云语音识别
     */
    private Map<String, Object> recognizeByAliyun(String audioData, String locale) {
        // 实现阿里云语音识别API调用
        // 需要申请阿里云语音服务并配置AccessKey
        
        Map<String, Object> result = new HashMap<>();
        result.put("text", "阿里云识别结果");
        result.put("confidence", 0.97);
        result.put("language", locale);
        result.put("provider", "aliyun");
        return result;
    }

    /**
     * 语音合成
     */
    private String synthesizeByService(String text, String locale, String engine) {
        // 生成唯一的音频文件ID
        String audioId = UUID.randomUUID().toString();
        
        // 返回音频URL（可以是CDN地址或本地文件地址）
        // 实际项目中应该调用真实的TTS服务生成音频
        return "/api/audio/synthesized/" + audioId + ".mp3";
    }

    /**
     * 计算文本相似度（Levenshtein距离）
     */
    private double calculateSimilarity(String text1, String text2) {
        if (text1 == null || text2 == null) {
            return 0.0;
        }

        // 移除空格和标点进行对比
        String s1 = text1.replaceAll("[\\s\\p{Punct}]", "").toLowerCase();
        String s2 = text2.replaceAll("[\\s\\p{Punct}]", "").toLowerCase();

        if (s1.equals(s2)) {
            return 1.0;
        }

        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0) {
            return 1.0;
        }

        int distance = levenshteinDistance(s1, s2);
        return 1.0 - (double) distance / maxLength;
    }

    /**
     * 计算编辑距离
     */
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], 
                            Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    /**
     * 生成反馈建议
     */
    private String generateFeedback(PronunciationScore score) {
        if (score.getOverallScore() >= 90) {
            return "太棒了！您的发音非常标准，几乎完美！";
        } else if (score.getOverallScore() >= 80) {
            return "很好！您的发音很不错，可以继续加强练习。";
        } else if (score.getOverallScore() >= 70) {
            return "不错！您的发音有进步空间，注意某些单词的发音。";
        } else if (score.getOverallScore() >= 60) {
            return "还需努力！建议多听原音，注意语调变化。";
        } else {
            return "需要更多练习！建议先从基础发音开始，跟读标准音频。";
        }
    }

    /**
     * 计算单词级别得分
     */
    private Map<String, Double> calculateWordScores(String reference, String recognized) {
        Map<String, Double> wordScores = new HashMap<>();
        
        String[] refWords = reference.split("\\s+");
        String[] recWords = recognized.split("\\s+");

        for (int i = 0; i < refWords.length; i++) {
            String refWord = refWords[i].replaceAll("[\\p{Punct}]", "").toLowerCase();
            double score = 0.0;

            if (i < recWords.length) {
                String recWord = recWords[i].replaceAll("[\\p{Punct}]", "").toLowerCase();
                if (refWord.equals(recWord)) {
                    score = 100.0;
                } else {
                    score = calculateSimilarity(refWord, recWord) * 100;
                }
            }

            wordScores.put(refWord, Math.round(score));
        }

        return wordScores;
    }
}
