package com.ruoyi.edu.service;

import com.ruoyi.edu.domain.EduPost;
import com.ruoyi.edu.service.IEduCommunityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 社区服务单元测试
 * 
 * @description 测试帖子发布、评论、点赞等社区功能
 * @author LingLearn
 */
@SpringBootTest
public class EduCommunityServiceTest {

    @Autowired
    private IEduCommunityService communityService;

    /**
     * 测试获取热门帖子
     */
    @Test
    public void testSelectHotPosts() {
        Integer limit = 10;
        List<EduPost> posts = communityService.selectHotPosts(limit);
        
        assertNotNull(posts);
        assertTrue(posts.size() <= limit);
        
        // 验证帖子数据完整性
        for (EduPost post : posts) {
            assertNotNull(post.getPostId());
            assertNotNull(post.getTitle());
            assertNotNull(post.getContent());
            assertTrue(post.getLikeCount() >= 0);
            assertTrue(post.getViewCount() >= 0);
        }
    }

    /**
     * 测试根据语言获取帖子
     */
    @Test
    public void testSelectEduPostByLanguage() {
        String language = "en";
        Integer limit = 20;
        
        List<EduPost> posts = communityService.selectEduPostByLanguage(language, limit);
        
        assertNotNull(posts);
        // 验证语言筛选
        for (EduPost post : posts) {
            assertTrue(post.getLanguage().equals(language) || post.getLanguage().equals("all"));
        }
    }

    /**
     * 测试获取用户帖子
     */
    @Test
    public void testSelectEduPostByUserId() {
        Long userId = 1L;
        
        List<EduPost> posts = communityService.selectEduPostByUserId(userId);
        
        assertNotNull(posts);
        // 验证所有帖子都属于该用户
        for (EduPost post : posts) {
            assertEquals(userId, post.getUserId());
        }
    }

    /**
     * 测试获取帖子列表
     */
    @Test
    public void testSelectEduPostList() {
        EduPost query = new EduPost();
        query.setLanguage("all");
        
        List<EduPost> posts = communityService.selectEduPostList(query);
        
        assertNotNull(posts);
    }

    /**
     * 测试获取用户帖子统计
     */
    @Test
    public void testGetUserPostStats() {
        Long userId = 1L;
        
        var stats = communityService.getUserPostStats(userId);
        
        assertNotNull(stats);
        assertTrue(stats.containsKey("totalPosts"));
        assertTrue(stats.containsKey("totalViews"));
        assertTrue(stats.containsKey("totalLikes"));
        assertTrue(stats.containsKey("totalComments"));
    }
}
