package com.ruoyi.edu.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.edu.domain.EduAchievement;
import com.ruoyi.edu.domain.EduBadge;
import com.ruoyi.edu.domain.EduUserAchievement;
import com.ruoyi.edu.domain.EduUserBadge;
import com.ruoyi.edu.mapper.EduBadgeMapper;
import com.ruoyi.edu.mapper.EduUserBadgeMapper;
import com.ruoyi.edu.service.IEduAchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/edu")
public class EduAchievementController extends BaseController {

    @Autowired
    private IEduAchievementService achievementService;

    @Autowired
    private EduBadgeMapper badgeMapper;

    @Autowired
    private EduUserBadgeMapper userBadgeMapper;

    @GetMapping("/achievement/list")
    public TableDataInfo achievementList(EduAchievement eduAchievement) {
        startPage();
        List<EduAchievement> list = achievementService.selectEduAchievementList(eduAchievement);
        return getDataTable(list);
    }

    @Anonymous
    @GetMapping("/achievement/my")
    public AjaxResult getMyAchievements() {
        Long userId = SecurityUtils.getUserId();
        List<EduAchievement> list = achievementService.getUserAchievements(userId);
        return success(list);
    }

    @Anonymous
    @GetMapping("/achievement/pending")
    public AjaxResult getPendingAchievements() {
        Long userId = SecurityUtils.getUserId();
        List<EduAchievement> list = achievementService.getPendingAchievements(userId);
        return success(list);
    }

    @PostMapping("/achievement/check")
    public AjaxResult checkAchievements() {
        Long userId = SecurityUtils.getUserId();
        List<EduAchievement> newAchievements = achievementService.checkAndUpdateAchievements(userId);
        return success(newAchievements);
    }

    @Anonymous
    @GetMapping("/leaderboard")
    public AjaxResult getLeaderboard(
            @RequestParam(defaultValue = "total") String type,
            @RequestParam(defaultValue = "all") String language,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        Map<String, Object> result = achievementService.getLeaderboard(type, language, pageNum, pageSize);
        return success(result);
    }

    @Anonymous
    @GetMapping("/leaderboard/my")
    public AjaxResult getMyRank(
            @RequestParam(defaultValue = "total") String type,
            @RequestParam(defaultValue = "all") String language) {
        Long userId = SecurityUtils.getUserId();
        Integer rank = achievementService.getUserRank(userId, type, language);
        if (rank != null) {
            return success().put("rank", rank).put("type", type);
        }
        return success().put("rank", 0).put("type", type);
    }

    @PostMapping("/achievement/init/{userId}")
    public AjaxResult initAchievements(@PathVariable Long userId) {
        achievementService.initializeUserAchievements(userId);
        return success();
    }

    @Anonymous
    @GetMapping("/badge/list")
    public AjaxResult getBadgeList(EduBadge eduBadge) {
        List<EduBadge> list = badgeMapper.selectEduBadgeList(eduBadge);
        return success(list);
    }

    @Anonymous
    @GetMapping("/badge/my")
    public AjaxResult getMyBadges() {
        Long userId = SecurityUtils.getUserId();
        List<EduUserBadge> userBadges = userBadgeMapper.selectUserBadgeList(userId);
        if (userBadges != null && !userBadges.isEmpty()) {
            List<EduBadge> badges = userBadges.stream()
                    .map(ub -> badgeMapper.selectEduBadgeById(ub.getBadgeId()))
                    .filter(b -> b != null)
                    .toList();
            return success(badges);
        }
        return success(List.of());
    }

    @Anonymous
    @GetMapping("/badge/wall/{userId}")
    public AjaxResult getUserBadgeWall(@PathVariable Long userId) {
        List<EduUserBadge> userBadges = userBadgeMapper.selectUserBadgeList(userId);
        if (userBadges != null && !userBadges.isEmpty()) {
            List<EduBadge> badges = userBadges.stream()
                    .filter(ub -> ub.getIsDisplayed() != null && ub.getIsDisplayed())
                    .map(ub -> badgeMapper.selectEduBadgeById(ub.getBadgeId()))
                    .filter(b -> b != null)
                    .toList();
            return success(badges);
        }
        return success(List.of());
    }
}
