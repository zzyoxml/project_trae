package com.ruoyi.edu.service;

import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 导入导出服务接口
 */
public interface IImportExportService {

    /**
     * 智能导入词汇（自动识别格式，自动创建课程和单元）
     * @param file 上传的文件（支持txt/json）
     * @param courseId 目标课程ID（可选，为空则自动创建）
     * @param unitId 目标单元ID（可选，为空则自动创建）
     * @param courseName 课程名称（新建课程时使用）
     * @param unitName 单元名称（新建单元时使用）
     * @return 导入结果
     */
    Map<String, Object> smartImportVocabulary(MultipartFile file, Long courseId, Long unitId, String courseName, String unitName);

    /**
     * 导入词汇TXT文件（支持多种分隔符和格式）
     * @param file 上传的TXT文件
     * @param unitId 目标单元ID
     * @return 导入结果
     */
    Map<String, Object> importVocabularyTxt(MultipartFile file, Long unitId);

    /**
     * 导入词汇JSON文件
     * @param file 上传的JSON文件
     * @param unitId 目标单元ID
     * @return 导入结果
     */
    Map<String, Object> importVocabularyJson(MultipartFile file, Long unitId);

    /**
     * 导入完整课程JSON格式
     * @param file 上传的课程JSON文件
     * @param courseId 目标课程ID（可选，为空则创建新课程）
     * @return 导入结果
     */
    Map<String, Object> importFullCourseJson(MultipartFile file, Long courseId);

    /**
     * 导出课程为JSON格式
     * @param courseId 课程ID
     * @return JSON字符串
     */
    String exportCourseJson(Long courseId);

    /**
     * 导出单元词汇为TXT格式
     * @param unitId 单元ID
     * @return TXT字符串
     */
    String exportUnitVocabularyTxt(Long unitId);

    /**
     * 导出单元词汇为JSON格式
     * @param unitId 单元ID
     * @return JSON字符串
     */
    String exportUnitVocabularyJson(Long unitId);
}
