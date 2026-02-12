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
        courseManage.setMeta(new MetaVo("课程管理", "book", false, ""));
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
        vocabularyManage.setMeta(new MetaVo("词汇管理", "word", false, ""));
        courseChildren.add(vocabularyManage);
        
        courseRouter.setChildren(courseChildren);
        routers.add(courseRouter);
        
        return success(routers);
    }
}
