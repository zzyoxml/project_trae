package com.ruoyi.edu.service;

import java.util.Map;

/**
 * 语音识别服务接口
 * 
 * @description 提供语音识别和评分功能
 * @author LingLearn
 */
public interface ISpeechRecognitionService {

    /**
     * 语音转文字
     *
     * @param audioData 音频数据（Base64编码）
     * @param language  语言代码
     * @return 识别结果
     */
    Map<String, Object> recognizeSpeech(String audioData, String language);

    /**
     * 发音评分
     *
     * @param audioData     音频数据（Base64编码）
     * @param referenceText 参考文本
     * @param language      语言代码
     * @return 评分结果
     */
    Map<String, Object> evaluatePronunciation(String audioData, String referenceText, String language);

    /**
     * 语音合成（文字转语音）
     *
     * @param text     文本内容
     * @param language 语言代码
     * @return 音频URL
     */
    String synthesizeSpeech(String text, String language);

    /**
     * 实时语音识别（流式）
     *
     * @param sessionId 会话ID
     * @param audioData 音频数据
     * @param language 语言代码
     * @return 识别结果
     */
    Map<String, Object> recognizeSpeechStream(String sessionId, String audioData, String language);

    /**
     * 获取支持的语言列表
     *
     * @return 语言列表
     */
    Map<String, String> getSupportedLanguages();

    /**
     * 检查语音服务可用性
     *
     * @return 是否可用
     */
    boolean isServiceAvailable();
}
