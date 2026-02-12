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
import com.ruoyi.edu.domain.EduVocabulary;
import com.ruoyi.edu.service.IEduVocabularyService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 词汇管理Controller
 * 
 * @author LingLearn
 */
@RestController
@RequestMapping("/edu/vocabulary")
public class EduVocabularyController extends BaseController {
    @Autowired
    private IEduVocabularyService eduVocabularyService;

    /**
     * 查询词汇列表
     */
    @PreAuthorize("@ss.hasPermi('edu:vocabulary:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduVocabulary eduVocabulary) {
        startPage();
        List<EduVocabulary> list = eduVocabularyService.selectEduVocabularyList(eduVocabulary);
        return getDataTable(list);
    }

    /**
     * 导出词汇列表
     */
    @PreAuthorize("@ss.hasPermi('edu:vocabulary:export')")
    @Log(title = "词汇管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(EduVocabulary eduVocabulary) {
        List<EduVocabulary> list = eduVocabularyService.selectEduVocabularyList(eduVocabulary);
        ExcelUtil<EduVocabulary> util = new ExcelUtil<EduVocabulary>(EduVocabulary.class);
        return util.exportExcel(list, "词汇数据");
    }

    /**
     * 获取词汇详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:vocabulary:query')")
    @GetMapping(value = "/{vocabId}")
    public AjaxResult getInfo(@PathVariable("vocabId") Long vocabId) {
        return AjaxResult.success(eduVocabularyService.selectEduVocabularyById(vocabId));
    }

    /**
     * 新增词汇
     */
    @PreAuthorize("@ss.hasPermi('edu:vocabulary:add')")
    @Log(title = "词汇管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduVocabulary eduVocabulary) {
        return toAjax(eduVocabularyService.insertEduVocabulary(eduVocabulary));
    }

    /**
     * 修改词汇
     */
    @PreAuthorize("@ss.hasPermi('edu:vocabulary:edit')")
    @Log(title = "词汇管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduVocabulary eduVocabulary) {
        return toAjax(eduVocabularyService.updateEduVocabulary(eduVocabulary));
    }

    /**
     * 删除词汇
     */
    @PreAuthorize("@ss.hasPermi('edu:vocabulary:remove')")
    @Log(title = "词汇管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{vocabIds}")
    public AjaxResult remove(@PathVariable Long[] vocabIds) {
        return toAjax(eduVocabularyService.deleteEduVocabularyByIds(vocabIds));
    }
}