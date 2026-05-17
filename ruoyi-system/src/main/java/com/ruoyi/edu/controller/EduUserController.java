package com.ruoyi.edu.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.edu.domain.EduAppUser;
import com.ruoyi.edu.domain.EduUserProfile;
import com.ruoyi.edu.service.IEduAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;

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
    private IEduAppUserService eduAppUserService;

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

        Long userId = eduAppUserService.registerUser(username, password, email, nativeLanguage, learningLanguages);
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

        String result = eduAppUserService.login(username, password);
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
        EduAppUser user = eduAppUserService.selectEduAppUserById(userId);
        EduUserProfile profile = eduAppUserService.selectEduUserProfileByUserId(userId);
        
        Map<String, Object> result = new HashMap<>();
        if (user != null) {
            result.put("userId", user.getUserId());
            result.put("username", user.getUsername());
            result.put("nickName", user.getNickName());
            result.put("email", user.getEmail());
            result.put("phonenumber", user.getPhonenumber());
            result.put("avatar", user.getAvatar());
        }
        if (profile != null) {
            result.put("nativeLanguage", profile.getNativeLanguage());
            result.put("learningLanguages", profile.getLearningLanguages());
            result.put("totalStudyTime", profile.getTotalStudyTime());
            result.put("currentStreak", profile.getCurrentStreak());
            result.put("longestStreak", profile.getLongestStreak());
            result.put("totalPoints", profile.getTotalPoints());
            result.put("level", profile.getLevel());
            result.put("experiencePoints", profile.getExperiencePoints());
            result.put("bio", profile.getBio());
            result.put("avatarUrl", profile.getAvatarUrl());
        }
        return success(result);
    }

    /**
     * 获取用户扩展信息
     */
    @GetMapping("/profile/{userId}")
    public AjaxResult getUserProfile(@PathVariable Long userId) {
        EduAppUser user = eduAppUserService.selectEduAppUserById(userId);
        EduUserProfile profile = eduAppUserService.selectEduUserProfileByUserId(userId);
        
        Map<String, Object> result = new HashMap<>();
        if (user != null) {
            result.put("userId", user.getUserId());
            result.put("username", user.getUsername());
            result.put("nickName", user.getNickName());
            result.put("email", user.getEmail());
            result.put("phonenumber", user.getPhonenumber());
            result.put("status", user.getStatus());
            result.put("createTime", user.getCreateTime());
            result.put("avatar", user.getAvatar());
        }
        if (profile != null) {
            result.put("nativeLanguage", profile.getNativeLanguage());
            result.put("learningLanguages", profile.getLearningLanguages());
            result.put("totalStudyTime", profile.getTotalStudyTime());
            result.put("currentStreak", profile.getCurrentStreak());
            result.put("longestStreak", profile.getLongestStreak());
            result.put("totalPoints", profile.getTotalPoints());
            result.put("level", profile.getLevel());
            result.put("experiencePoints", profile.getExperiencePoints());
            result.put("bio", profile.getBio());
            result.put("avatarUrl", profile.getAvatarUrl());
        }
        return success(result);
    }

    /**
     * 修改用户扩展信息
     */
    @PutMapping("/profile")
    public AjaxResult updateProfile(@RequestBody Map<String, Object> params) {
        Long userId = Long.parseLong(params.get("userId").toString());
        
        // 更新应用用户信息
        EduAppUser user = eduAppUserService.selectEduAppUserById(userId);
        if (user != null) {
            if (params.containsKey("nickName")) {
                user.setNickName(params.get("nickName").toString());
            }
            if (params.containsKey("email")) {
                user.setEmail(params.get("email").toString());
            }
            if (params.containsKey("phonenumber")) {
                user.setPhonenumber(params.get("phonenumber").toString());
            }
            if (params.containsKey("status")) {
                user.setStatus(params.get("status").toString());
            }
            if (params.containsKey("avatar")) {
                user.setAvatar(params.get("avatar").toString());
            }
            if (params.containsKey("password") && params.get("password") != null && !params.get("password").toString().isEmpty()) {
                user.setPassword(SecurityUtils.encryptPassword(params.get("password").toString()));
            }
            eduAppUserService.updateEduAppUser(user);
        }
        
        // 更新教育用户扩展信息
        EduUserProfile profile = eduAppUserService.selectEduUserProfileByUserId(userId);
        if (profile == null) {
            profile = new EduUserProfile();
            profile.setUserId(userId);
        }
        if (params.containsKey("nativeLanguage")) {
            profile.setNativeLanguage(params.get("nativeLanguage").toString());
        }
        if (params.containsKey("learningLanguages")) {
            profile.setLearningLanguages(params.get("learningLanguages").toString());
        }
        if (params.containsKey("avatar")) {
            profile.setAvatarUrl(params.get("avatar").toString());
        }
        eduAppUserService.updateEduUserProfile(profile);
        
        return success();
    }

    /**
     * 查询用户列表（包含系统用户信息）
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String username,
                            @RequestParam(required = false) String email,
                            @RequestParam(required = false) String learningLanguage) {
        startPage();
        List<Map<String, Object>> userList = eduAppUserService.selectUserListWithDetails(username, email, learningLanguage);
        return getDataTable(userList);
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete/{userId}")
    public AjaxResult deleteUser(@PathVariable Long userId) {
        int result = eduAppUserService.deleteEduAppUserById(userId);
        eduAppUserService.deleteEduUserProfileByUserId(userId);
        return toAjax(result);
    }

    /**
     * 更新用户状态
     */
    @PutMapping("/status")
    public AjaxResult updateUserStatus(@RequestBody Map<String, String> params) {
        Long userId = Long.parseLong(params.get("userId"));
        String status = params.get("status");
        
        int result = eduAppUserService.updateUserStatus(userId, status);
        if (result > 0) {
            return success();
        }
        return error("用户不存在");
    }

    /**
     * 更新学习时间
     */
    @PostMapping("/studyTime")
    public AjaxResult updateStudyTime(@RequestBody Map<String, Object> params) {
        Long userId = SecurityUtils.getUserId();
        Integer duration = Integer.parseInt(params.get("duration").toString());
        eduAppUserService.updateStudyTime(userId, duration);
        return success();
    }

    /**
     * 更新连续学习天数
     */
    @PostMapping("/streak")
    public AjaxResult updateStreak() {
        Long userId = SecurityUtils.getUserId();
        eduAppUserService.updateStreak(userId);
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
        eduAppUserService.rewardUser(userId, points, experience);
        return success();
    }

    /**
     * 头像上传
     */
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception
    {
        if (!file.isEmpty())
        {
            Long userId = SecurityUtils.getUserId();
            String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
            
            // 更新 edu_app_user 表
            EduAppUser user = eduAppUserService.selectEduAppUserById(userId);
            if (user != null) {
                user.setAvatar(avatar);
                eduAppUserService.updateEduAppUser(user);
            }
            
            // 更新 edu_user_profile 表
            EduUserProfile profile = eduAppUserService.selectEduUserProfileByUserId(userId);
            if (profile == null) {
                profile = new EduUserProfile();
                profile.setUserId(userId);
                profile.setAvatarUrl(avatar);
                eduAppUserService.insertEduUserProfile(profile);
            } else {
                profile.setAvatarUrl(avatar);
                eduAppUserService.updateEduUserProfile(profile);
            }
            
            AjaxResult ajax = AjaxResult.success();
            ajax.put("imgUrl", avatar);
            return ajax;
        }
        return error("上传图片异常，请联系管理员");
    }

    /**
     * 获取学习统计数据
     */
    @GetMapping("/stats")
    public AjaxResult getLearningStats() {
        Long userId = SecurityUtils.getUserId();
        Map<String, Object> stats = eduAppUserService.getLearningStats(userId);
        return success(stats);
    }
}
