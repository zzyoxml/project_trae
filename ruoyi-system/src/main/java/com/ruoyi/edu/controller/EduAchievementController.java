package com.ruoyi.edu.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.edu.domain.EduAchievement;
import com.ruoyi.edu.service.IEduAchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 成就Controller
 *
 * @author LingLearn
 */
@RestController
@RequestMapping("/edu/achievement")
public class EduAchievementController extends BaseController {

    @Autowired
    private IEduAchievementService achievementService;

    /**
     * 获取用户成就列表
     */
    @GetMapping("/list")
    public TableDataInfo list(EduAchievement eduAchievement) {
        startPage();
        List<EduAchievement> list = achievementService.selectEduAchievementList(eduAchievement);
        return getDataTable(list);
    }

    /**
     * 获取我的成就
     */
    @GetMapping("/my")
    public AjaxResult getMyAchievements() {
        Long userId = SecurityUtils.getUserId();
        List<EduAchievement> achievements = achievementService.getUserAchievements(userId);
        return success(achievements);
    }

    /**
     * 获取待完成成就
     */
    @GetMapping("/pending")
    public AjaxResult getPendingAchievements() {
        Long userId = SecurityUtils.getUserId();
        List<EduAchievement> achievements = achievementService.getPendingAchievements(userId);
        return success(achievements);
    }

    /**
     * 检查成就进度
     */
    @PostMapping("/check")
    public AjaxResult checkAchievements() {
        Long userId = SecurityUtils.getUserId();
        List<EduAchievement> newAchievements = achievementService.checkAndUpdateAchievements(userId);
        return success(newAchievements);
    }

    /**
     * 获取排行榜
     */
    @Anonymous
    @GetMapping("/leaderboard")
    public TableDataInfo getLeaderboard(
            @RequestParam(defaultValue = "total") String type,
            @RequestParam(defaultValue = "all") String language,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        Map<String, Object> leaderboard = achievementService.getLeaderboard(type, language, pageNum, pageSize);
        List<?> rankings = (List<?>) leaderboard.get("rankings");
        startPage();
        return getDataTable(rankings);
    }

    /**
     * 获取我的排名
     */
    @GetMapping("/rank")
    public AjaxResult getMyRank(@RequestParam(defaultValue = "total") String type,
                               @RequestParam(defaultValue = "all") String language) {
        Long userId = SecurityUtils.getUserId();
        Integer rank = achievementService.getUserRank(userId, type, language);
        return success().put("rank", rank);
    }

    /**
     * 新增成就
     */
    @PostMapping
    public AjaxResult add(@RequestBody EduAchievement eduAchievement) {
        return toAjax(achievementService.insertEduAchievement(eduAchievement));
    }

    /**
     * 修改成就
     */
    @PutMapping
    public AjaxResult edit(@RequestBody EduAchievement eduAchievement) {
        return toAjax(achievementService.updateEduAchievement(eduAchievement));
    }

    /**
     * 删除成就
     */
    @DeleteMapping("/{achievementIds}")
    public AjaxResult remove(@PathVariable Long[] achievementIds) {
        int result = 0;
        for (Long achievementId : achievementIds) {
            result += achievementService.deleteEduAchievementById(achievementId);
        }
        return toAjax(result);
    }

    /**
     * 初始化用户成就
     */
    @PostMapping("/init")
    public AjaxResult initUserAchievements() {
        Long userId = SecurityUtils.getUserId();
        achievementService.initializeUserAchievements(userId);
        return success();
    }
}
