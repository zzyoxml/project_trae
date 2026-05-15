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
import com.ruoyi.edu.domain.EduUserProfile;
import com.ruoyi.edu.domain.EduLearningProgress;
import com.ruoyi.edu.mapper.EduBadgeMapper;
import com.ruoyi.edu.mapper.EduUserBadgeMapper;
import com.ruoyi.edu.mapper.EduUserProfileMapper;
import com.ruoyi.edu.mapper.EduLearningProgressMapper;
import com.ruoyi.edu.service.IEduAchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/edu")
public class EduAchievementController extends BaseController {

    @Autowired
    private IEduAchievementService achievementService;

    @Autowired
    private EduBadgeMapper badgeMapper;

    @Autowired
    private EduUserProfileMapper userProfileMapper;

    @Autowired
    private EduUserBadgeMapper userBadgeMapper;

    @Autowired
    private EduLearningProgressMapper learningProgressMapper;

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
        
        Map<String, Object> result = new HashMap<>();
        result.put("type", type);
        
        if (userId == null) {
            // 用户未登录，返回默认值
            result.put("rank", 0);
            result.put("score", 0);
            result.put("streakDays", 0);
            return success(result);
        }
        
        Integer rank = achievementService.getUserRank(userId, type, language);
        
        // 获取用户详细信息
        EduUserProfile profile = userProfileMapper.selectEduUserProfileByUserId(userId);
        
        result.put("rank", rank != null ? rank : 0);
        
        if (profile != null) {
            // 根据类型返回正确的分数
            if ("daily".equals(type)) {
                // 计算今日积分
                int todayPoints = 0;
                List<EduLearningProgress> progressList = learningProgressMapper.selectEduLearningProgressList(new EduLearningProgress());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                
                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MILLISECOND, 0);
                
                for (EduLearningProgress progress : progressList) {
                    if ("completed".equals(progress.getStatus()) && progress.getCompletedTime() != null && userId.equals(progress.getUserId())) {
                        try {
                            Date completedDate = sdf.parse(progress.getCompletedTime());
                            if (completedDate.after(today.getTime())) {
                                todayPoints += 5;
                            }
                        } catch (Exception e) {
                            // 忽略解析错误
                        }
                    }
                }
                result.put("score", todayPoints);
            } else {
                result.put("score", profile.getTotalPoints() != null ? profile.getTotalPoints() : 0);
            }
            result.put("streakDays", profile.getCurrentStreak() != null ? profile.getCurrentStreak() : 0);
        }
        
        return success(result);
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
