package com.ruoyi.web.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.config.RuoYiConfig;
import java.io.File;

/**
 * 诊断接口 - 用于排查问题
 */
@RestController
@RequestMapping("/diagnose")
public class DiagnoseController
{
    @GetMapping("/profile")
    public Object checkProfile()
    {
        String profile = RuoYiConfig.getProfile();
        File profileDir = new File(profile);
        File avatarDir = new File(profile + "/avatar");
        File avatarFile = new File(profile + "/avatar/2026/05/17/android-chrome-512x512_20260517202446A002.png");
        
        return new Object[]{
            "profile", profile,
            "profileExists", profileDir.exists(),
            "profileIsDirectory", profileDir.isDirectory(),
            "avatarExists", avatarDir.exists(),
            "avatarIsDirectory", avatarDir.isDirectory(),
            "targetFileExists", avatarFile.exists(),
            "targetFilePath", avatarFile.getAbsolutePath()
        };
    }
}
