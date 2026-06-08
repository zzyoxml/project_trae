package com.ruoyi.web.controller.edu;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 口语评分 Controller
 *
 * <p>接收前端 MediaRecorder 录制的 webm 音频 + 目标参考文本，
 * 模拟发音评估并返回 30~100 之间的分数。
 *
 * <p>真实业务应接入 Azure Speech / Google Cloud Speech 等发音评估服务。
 *
 * @author LingLearn
 */
@RestController
@RequestMapping("/edu/speech")
public class EduSpeechController extends BaseController {

    /**
     * 发音评分
     *
     * @param audio  录音文件（webm/wav 等）
     * @param target 参考单词或句子
     * @return { score: 30~100, feedback: "..." }
     */
    @PostMapping("/evaluate")
    public AjaxResult evaluate(@RequestParam("audio") MultipartFile audio,
                                @RequestParam("target") String target) {
        if (audio == null || audio.isEmpty()) {
            return AjaxResult.error("录音为空，请重试");
        }
        if (target == null || target.trim().isEmpty()) {
            return AjaxResult.error("参考文本不能为空");
        }

        long size = audio.getSize();
        // 模拟：录音文件越大（说明用户读得越久）、参考词越短，分数越高
        // 真实场景应调用 Azure Speech Pronunciation Assessment 等服务
        double random = Math.random();
        int base = 30 + (int) (random * 50);  // 30~80 基础分
        // 录音 < 5KB 说明录音异常/太短，扣分
        if (size < 5 * 1024) {
            base = Math.min(base, 50);
        } else if (size > 50 * 1024) {
            base = Math.max(base, 70);
        }
        int score = Math.max(30, Math.min(100, base + (int) (Math.random() * 20) - 5));

        String feedback;
        if (score >= 90) feedback = "太棒了！发音非常标准！";
        else if (score >= 80) feedback = "很好！继续保持！";
        else if (score >= 70) feedback = "不错，还有进步空间";
        else if (score >= 60) feedback = "可以听懂，再多练习几遍";
        else feedback = "继续加油，多听标准发音";

        return AjaxResult.success("评分完成", new java.util.HashMap<String, Object>() {{
            put("score", score);
            put("feedback", feedback);
            put("target", target);
            put("audioSize", size);
        }});
    }
}
