package com.ruoyi.edu.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.edu.domain.EduUserProfile;
import com.ruoyi.edu.service.IEduUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教育平台用户Controller
 *
 * @author LingLearn
 */
@RestController
@RequestMapping("/edu/user")
public class EduUserController extends BaseController {

    @Autowired
    private IEduUserService eduUserService;

    /**
     * 用户注册
     */
    @Anonymous
    @PostMapping("/register")
    public AjaxResult register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String email = params.get("email");
        String nativeLanguage = params.get("nativeLanguage");
        String learningLanguages = params.get("learningLanguages");

        Long userId = eduUserService.registerUser(username, password, email, nativeLanguage, learningLanguages);
        return success().put("userId", userId);
    }

    /**
     * 用户登录
     */
    @Anonymous
    @PostMapping("/login")
    public AjaxResult login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        String result = eduUserService.login(username, password);
        return success(result);
    }

    /**
     * 获取当前用户信息
     */
    @Anonymous
    @GetMapping("/info")
    public AjaxResult getUserInfo() {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            return success(new HashMap<String, Object>());
        }
        EduUserProfile profile = eduUserService.getUserFullInfo(userId);
        return success(profile);
    }

    /**
     * 获取用户扩展信息
     */
    @GetMapping("/profile/{userId}")
    public AjaxResult getUserProfile(@PathVariable Long userId) {
        EduUserProfile profile = eduUserService.selectEduUserProfileByUserId(userId);
        return success(profile);
    }

    /**
     * 修改用户扩展信息
     */
    @PutMapping("/profile")
    public AjaxResult updateProfile(@RequestBody EduUserProfile profile) {
        Long userId = SecurityUtils.getUserId();
        profile.setUserId(userId);
        return toAjax(eduUserService.updateEduUserProfile(profile));
    }

    /**
     * 查询用户扩展信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(EduUserProfile eduUserProfile) {
        startPage();
        List<EduUserProfile> list = eduUserService.selectEduUserProfileList(eduUserProfile);
        return getDataTable(list);
    }

    /**
     * 更新学习时间
     */
    @PostMapping("/studyTime")
    public AjaxResult updateStudyTime(@RequestBody Map<String, Object> params) {
        Long userId = SecurityUtils.getUserId();
        Integer duration = Integer.parseInt(params.get("duration").toString());
        eduUserService.updateStudyTime(userId, duration);
        return success();
    }

    /**
     * 更新连续学习天数
     */
    @PostMapping("/streak")
    public AjaxResult updateStreak() {
        Long userId = SecurityUtils.getUserId();
        eduUserService.updateStreak(userId);
        return success();
    }

    /**
     * 奖励用户
     */
    @PostMapping("/reward")
    public AjaxResult rewardUser(@RequestBody Map<String, Integer> params) {
        Long userId = SecurityUtils.getUserId();
        Integer points = params.get("points");
        Integer experience = params.get("experience");
        eduUserService.rewardUser(userId, points, experience);
        return success();
    }
}
