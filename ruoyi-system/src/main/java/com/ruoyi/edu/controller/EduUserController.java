package com.ruoyi.edu.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
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

    @Autowired
    private SysUserMapper sysUserMapper;

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
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        
        Map<String, Object> result = new HashMap<>();
        if (profile != null) {
            result.putAll(profileToMap(profile));
        }
        if (sysUser != null) {
            result.put("userId", sysUser.getUserId());
            result.put("username", sysUser.getUserName());
            result.put("password", "******"); // 安全起见，这里不返回明文密码
            result.put("email", sysUser.getEmail());
            result.put("phonenumber", sysUser.getPhonenumber());
            result.put("status", sysUser.getStatus());
            result.put("createTime", sysUser.getCreateTime());
        }
        return success(result);
    }

    /**
     * 修改用户扩展信息
     */
    @PutMapping("/profile")
    public AjaxResult updateProfile(@RequestBody Map<String, Object> params) {
        Long userId = Long.parseLong(params.get("userId").toString());
        
        // 更新系统用户信息
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        if (sysUser != null) {
            if (params.containsKey("nickName")) {
                sysUser.setNickName(params.get("nickName").toString());
            }
            if (params.containsKey("email")) {
                sysUser.setEmail(params.get("email").toString());
            }
            if (params.containsKey("phonenumber")) {
                sysUser.setPhonenumber(params.get("phonenumber").toString());
            }
            if (params.containsKey("status")) {
                sysUser.setStatus(params.get("status").toString());
            }
            if (params.containsKey("password") && params.get("password") != null && !params.get("password").toString().isEmpty()) {
                sysUser.setPassword(SecurityUtils.encryptPassword(params.get("password").toString()));
            }
            sysUserMapper.updateUser(sysUser);
        }
        
        // 更新教育用户扩展信息
        if (params.containsKey("nativeLanguage") || params.containsKey("learningLanguages")) {
            EduUserProfile profile = eduUserService.selectEduUserProfileByUserId(userId);
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
            eduUserService.updateEduUserProfile(profile);
        }
        
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
        List<Map<String, Object>> userList = eduUserService.selectUserListWithDetails(username, email, learningLanguage);
        return getDataTable(userList);
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete/{userId}")
    public AjaxResult deleteUser(@PathVariable Long userId) {
        int result = eduUserService.deleteUser(userId);
        return toAjax(result);
    }

    /**
     * 更新用户状态
     */
    @PutMapping("/status")
    public AjaxResult updateUserStatus(@RequestBody Map<String, String> params) {
        Long userId = Long.parseLong(params.get("userId"));
        String status = params.get("status");
        
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        if (sysUser != null) {
            sysUser.setStatus(status);
            sysUserMapper.updateUser(sysUser);
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

    /**
     * 获取学习统计数据
     */
    @GetMapping("/stats")
    public AjaxResult getLearningStats() {
        Long userId = SecurityUtils.getUserId();
        Map<String, Object> stats = eduUserService.getLearningStats(userId);
        return success(stats);
    }

    private Map<String, Object> profileToMap(EduUserProfile profile) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", profile.getUserId());
        map.put("nativeLanguage", profile.getNativeLanguage());
        map.put("learningLanguages", profile.getLearningLanguages());
        map.put("totalStudyTime", profile.getTotalStudyTime());
        map.put("currentStreak", profile.getCurrentStreak());
        map.put("longestStreak", profile.getLongestStreak());
        map.put("totalPoints", profile.getTotalPoints());
        map.put("level", profile.getLevel());
        map.put("experiencePoints", profile.getExperiencePoints());
        map.put("bio", profile.getBio());
        return map;
    }
}
