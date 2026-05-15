package com.ruoyi.web.controller.edu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.vo.MetaVo;
import com.ruoyi.system.domain.vo.RouterVo;

/**
 * 教育平台菜单管理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/edu/menu")
public class EduMenuController extends BaseController
{
    /**
     * 获取教育平台的路由菜单
     */
    @Anonymous
    @GetMapping(value = "/getRouters", produces = "application/json;charset=UTF-8")
    public AjaxResult getRouters()
    {
        List<RouterVo> routers = new ArrayList<>();
        
        // 课程管理
        RouterVo courseRouter = new RouterVo();
        courseRouter.setPath("/edu");
        courseRouter.setRedirect("noRedirect");
        courseRouter.setAlwaysShow(true);
        courseRouter.setName("Edu");
        courseRouter.setComponent("Layout");
        courseRouter.setMeta(new MetaVo("教育管理", "education", false, ""));
        
        List<RouterVo> courseChildren = new ArrayList<>();
        
        RouterVo courseManage = new RouterVo();
        courseManage.setPath("course");
        courseManage.setComponent("edu/course/index");
        courseManage.setName("CourseManage");
        courseManage.setMeta(new MetaVo("课程管理", "education", false, ""));
        courseChildren.add(courseManage);
        
        RouterVo learningManage = new RouterVo();
        learningManage.setPath("learning");
        learningManage.setComponent("edu/learning/index");
        learningManage.setName("LearningManage");
        learningManage.setMeta(new MetaVo("学习数据", "chart", false, ""));
        courseChildren.add(learningManage);
        
        RouterVo communityManage = new RouterVo();
        communityManage.setPath("community");
        communityManage.setComponent("edu/community/index");
        communityManage.setName("CommunityManage");
        communityManage.setMeta(new MetaVo("社区管理", "message", false, ""));
        courseChildren.add(communityManage);
        
        RouterVo achievementManage = new RouterVo();
        achievementManage.setPath("achievement");
        achievementManage.setComponent("edu/achievement/index");
        achievementManage.setName("AchievementManage");
        achievementManage.setMeta(new MetaVo("成就管理", "star", false, ""));
        courseChildren.add(achievementManage);
        
        RouterVo unitManage = new RouterVo();
        unitManage.setPath("unit");
        unitManage.setComponent("edu/unit/index");
        unitManage.setName("UnitManage");
        unitManage.setMeta(new MetaVo("章节管理", "tree-table", false, ""));
        courseChildren.add(unitManage);
        
        RouterVo lessonManage = new RouterVo();
        lessonManage.setPath("lesson");
        lessonManage.setComponent("edu/lesson/index");
        lessonManage.setName("LessonManage");
        lessonManage.setMeta(new MetaVo("课时管理", "list", false, ""));
        courseChildren.add(lessonManage);
        
        RouterVo vocabularyManage = new RouterVo();
        vocabularyManage.setPath("vocabulary");
        vocabularyManage.setComponent("edu/vocabulary/index");
        vocabularyManage.setName("VocabularyManage");
        vocabularyManage.setMeta(new MetaVo("词汇管理", "language", false, ""));
        courseChildren.add(vocabularyManage);
        
        RouterVo importExportManage = new RouterVo();
        importExportManage.setPath("import-export");
        importExportManage.setComponent("edu/import-export/index");
        importExportManage.setName("ImportExportManage");
        importExportManage.setMeta(new MetaVo("导入导出", "upload", false, ""));
        courseChildren.add(importExportManage);
        
        courseRouter.setChildren(courseChildren);
        routers.add(courseRouter);

        // 系统管理
        RouterVo systemRouter = new RouterVo();
        systemRouter.setPath("/system");
        systemRouter.setRedirect("noRedirect");
        systemRouter.setAlwaysShow(true);
        systemRouter.setName("System");
        systemRouter.setComponent("Layout");
        systemRouter.setMeta(new MetaVo("系统管理", "system", false, ""));

        List<RouterVo> systemChildren = new ArrayList<>();

        RouterVo userManage = new RouterVo();
        userManage.setPath("user");
        userManage.setComponent("system/user/index");
        userManage.setName("UserManage");
        userManage.setMeta(new MetaVo("用户管理", "user", false, ""));
        systemChildren.add(userManage);

        RouterVo roleManage = new RouterVo();
        roleManage.setPath("role");
        roleManage.setComponent("system/role/index");
        roleManage.setName("RoleManage");
        roleManage.setMeta(new MetaVo("角色管理", "peoples", false, ""));
        systemChildren.add(roleManage);

        RouterVo menuManage = new RouterVo();
        menuManage.setPath("menu");
        menuManage.setComponent("system/menu/index");
        menuManage.setName("MenuManage");
        menuManage.setMeta(new MetaVo("菜单管理", "tree-table", false, ""));
        systemChildren.add(menuManage);

        RouterVo deptManage = new RouterVo();
        deptManage.setPath("dept");
        deptManage.setComponent("system/dept/index");
        deptManage.setName("DeptManage");
        deptManage.setMeta(new MetaVo("部门管理", "tree", false, ""));
        systemChildren.add(deptManage);

        RouterVo postManage = new RouterVo();
        postManage.setPath("post");
        postManage.setComponent("system/post/index");
        postManage.setName("PostManage");
        postManage.setMeta(new MetaVo("岗位管理", "post", false, ""));
        systemChildren.add(postManage);

        RouterVo dictManage = new RouterVo();
        dictManage.setPath("dict");
        dictManage.setComponent("system/dict/index");
        dictManage.setName("DictManage");
        dictManage.setMeta(new MetaVo("字典管理", "dict", false, ""));
        systemChildren.add(dictManage);

        RouterVo configManage = new RouterVo();
        configManage.setPath("config");
        configManage.setComponent("system/config/index");
        configManage.setName("ConfigManage");
        configManage.setMeta(new MetaVo("参数管理", "edit", false, ""));
        systemChildren.add(configManage);

        RouterVo noticeManage = new RouterVo();
        noticeManage.setPath("notice");
        noticeManage.setComponent("system/notice/index");
        noticeManage.setName("NoticeManage");
        noticeManage.setMeta(new MetaVo("通知公告", "message", false, ""));
        systemChildren.add(noticeManage);

        RouterVo operLogManage = new RouterVo();
        operLogManage.setPath("operlog");
        operLogManage.setComponent("monitor/operlog/index");
        operLogManage.setName("OperlogManage");
        operLogManage.setMeta(new MetaVo("操作日志", "log", false, ""));
        systemChildren.add(operLogManage);

        RouterVo logininforManage = new RouterVo();
        logininforManage.setPath("logininfor");
        logininforManage.setComponent("monitor/logininfor/index");
        logininforManage.setName("LogininforManage");
        logininforManage.setMeta(new MetaVo("登录日志", "logininfor", false, ""));
        systemChildren.add(logininforManage);

        systemRouter.setChildren(systemChildren);
        routers.add(systemRouter);

        return success(routers);
    }
}
