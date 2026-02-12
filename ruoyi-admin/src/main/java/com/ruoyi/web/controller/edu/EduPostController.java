package com.ruoyi.web.controller.edu;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.edu.domain.EduPost;
import com.ruoyi.edu.service.IEduPostService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 社区帖子管理Controller
 * 
 * @author LingLearn
 */
@RestController
@RequestMapping("/edu/post")
public class EduPostController extends BaseController {
    @Autowired
    private IEduPostService eduPostService;

    /**
     * 查询帖子列表
     */
    @PreAuthorize("@ss.hasPermi('edu:post:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduPost eduPost) {
        startPage();
        List<EduPost> list = eduPostService.selectEduPostList(eduPost);
        return getDataTable(list);
    }

    /**
     * 获取帖子详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:post:query')")
    @GetMapping(value = "/{postId}")
    public AjaxResult getInfo(@PathVariable("postId") Long postId) {
        return AjaxResult.success(eduPostService.selectEduPostById(postId));
    }

    /**
     * 新增帖子
     */
    @PreAuthorize("@ss.hasPermi('edu:post:add')")
    @Log(title = "帖子管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduPost eduPost) {
        return toAjax(eduPostService.insertEduPost(eduPost));
    }

    /**
     * 修改帖子
     */
    @PreAuthorize("@ss.hasPermi('edu:post:edit')")
    @Log(title = "帖子管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduPost eduPost) {
        return toAjax(eduPostService.updateEduPost(eduPost));
    }

    /**
     * 删除帖子
     */
    @PreAuthorize("@ss.hasPermi('edu:post:remove')")
    @Log(title = "帖子管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{postIds}")
    public AjaxResult remove(@PathVariable Long[] postIds) {
        return toAjax(eduPostService.deleteEduPostByIds(postIds));
    }

    /**
     * 审核帖子
     */
    @PreAuthorize("@ss.hasPermi('edu:post:edit')")
    @Log(title = "帖子审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit/{postId}")
    public AjaxResult audit(@PathVariable("postId") Long postId, @RequestBody EduPost eduPost) {
        eduPost.setPostId(postId);
        return toAjax(eduPostService.updateEduPost(eduPost));
    }

    /**
     * 批量审核帖子
     */
    @PreAuthorize("@ss.hasPermi('edu:post:edit')")
    @Log(title = "批量审核", businessType = BusinessType.UPDATE)
    @PutMapping("/batchAudit")
    public AjaxResult batchAudit(@RequestBody Long[] postIds) {
        int count = 0;
        for (Long postId : postIds) {
            EduPost post = new EduPost();
            post.setPostId(postId);
            post.setStatus("published");
            eduPostService.updateEduPost(post);
            count++;
        }
        return AjaxResult.success("成功审核" + count + "条帖子");
    }
}