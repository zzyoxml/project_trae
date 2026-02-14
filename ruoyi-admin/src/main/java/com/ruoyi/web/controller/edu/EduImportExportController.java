package com.ruoyi.web.controller.edu;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.edu.service.IImportExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 导入导出管理 - 智能版
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/edu/import-export")
public class EduImportExportController extends BaseController {

    @Autowired
    private IImportExportService importExportService;

    /**
     * 智能导入词汇（自动识别格式，自动创建课程和单元）
     *
     * 使用方式：
     * 1. 只传文件：自动创建课程和单元，从文件名提取名称
     * 2. 传文件+courseName/unitName：自动创建指定名称的课程和单元
     * 3. 传文件+courseId/unitId：导入到指定课程和单元
     */
    @Log(title = "智能导入词汇", businessType = BusinessType.IMPORT)
    @PostMapping("/vocabulary/smart")
    public AjaxResult smartImportVocabulary(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "courseId", required = false) String courseIdStr,
            @RequestParam(value = "unitId", required = false) String unitIdStr,
            @RequestParam(value = "courseName", required = false) String courseName,
            @RequestParam(value = "unitName", required = false) String unitName) {
        if (file.isEmpty()) {
            return AjaxResult.error("上传文件为空");
        }

        try {
            Long courseId = StringUtils.isNotEmpty(courseIdStr) ? Long.parseLong(courseIdStr) : null;
            Long unitId = StringUtils.isNotEmpty(unitIdStr) ? Long.parseLong(unitIdStr) : null;

            Map<String, Object> result = importExportService.smartImportVocabulary(file, courseId, unitId, courseName, unitName);

            if (Boolean.TRUE.equals(result.get("success"))) {
                StringBuilder msg = new StringBuilder("导入成功");
                if (Boolean.TRUE.equals(result.get("courseCreated"))) {
                    msg.append("，已自动创建课程：").append(result.get("courseName"));
                }
                if (Boolean.TRUE.equals(result.get("unitCreated"))) {
                    msg.append("，已自动创建单元：").append(result.get("unitName"));
                }
                msg.append("，共导入 ").append(result.get("successCount")).append(" 个词汇");

                if ((Integer) result.get("failCount") > 0) {
                    msg.append("，失败 ").append(result.get("failCount")).append(" 个");
                }

                return AjaxResult.success(msg.toString(), result);
            } else {
                return AjaxResult.error("导入失败：" + result.get("error"));
            }
        } catch (NumberFormatException e) {
            logger.error("参数格式错误", e);
            return AjaxResult.error("参数格式错误：" + e.getMessage());
        } catch (Exception e) {
            logger.error("智能导入失败", e);
            return AjaxResult.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导入词汇（TXT 格式）- 兼容旧接口
     */
    @Log(title = "导入词汇 TXT", businessType = BusinessType.IMPORT)
    @PostMapping("/vocabulary/txt")
    public AjaxResult importVocabularyTxt(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "unitId", required = false) String unitIdStr,
            @RequestParam(value = "courseId", required = false) String courseIdStr,
            @RequestParam(value = "courseName", required = false) String courseName,
            @RequestParam(value = "unitName", required = false) String unitName) {
        if (file.isEmpty()) {
            return AjaxResult.error("上传文件为空");
        }

        try {
            Long unitId = StringUtils.isNotEmpty(unitIdStr) ? Long.parseLong(unitIdStr) : null;

            // 如果没有指定单元，使用智能导入
            if (unitId == null) {
                Long courseId = StringUtils.isNotEmpty(courseIdStr) ? Long.parseLong(courseIdStr) : null;
                return smartImportVocabulary(file, courseIdStr, unitIdStr, courseName, unitName);
            }

            Map<String, Object> result = importExportService.importVocabularyTxt(file, unitId);
            return AjaxResult.success("导入完成，共导入 " + result.get("successCount") + " 个词汇", result);
        } catch (NumberFormatException e) {
            logger.error("参数格式错误", e);
            return AjaxResult.error("参数格式错误：" + e.getMessage());
        } catch (Exception e) {
            logger.error("导入 TXT 失败", e);
            return AjaxResult.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导入词汇（JSON 格式）- 兼容旧接口
     */
    @Log(title = "导入词汇 JSON", businessType = BusinessType.IMPORT)
    @PostMapping("/vocabulary/json")
    public AjaxResult importVocabularyJson(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "unitId", required = false) String unitIdStr,
            @RequestParam(value = "courseId", required = false) String courseIdStr,
            @RequestParam(value = "courseName", required = false) String courseName,
            @RequestParam(value = "unitName", required = false) String unitName) {
        if (file.isEmpty()) {
            return AjaxResult.error("上传文件为空");
        }

        try {
            Long unitId = StringUtils.isNotEmpty(unitIdStr) ? Long.parseLong(unitIdStr) : null;

            // 如果没有指定单元，使用智能导入
            if (unitId == null) {
                Long courseId = StringUtils.isNotEmpty(courseIdStr) ? Long.parseLong(courseIdStr) : null;
                return smartImportVocabulary(file, courseIdStr, unitIdStr, courseName, unitName);
            }

            Map<String, Object> result = importExportService.importVocabularyJson(file, unitId);
            return AjaxResult.success("导入完成，共导入 " + result.get("successCount") + " 个词汇", result);
        } catch (NumberFormatException e) {
            logger.error("参数格式错误", e);
            return AjaxResult.error("参数格式错误：" + e.getMessage());
        } catch (Exception e) {
            logger.error("导入 JSON 失败", e);
            return AjaxResult.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出单元词汇（TXT 格式）
     */
    @GetMapping("/unit/{unitId}/vocabulary/txt")
    public ResponseEntity<byte[]> exportUnitVocabularyTxt(@PathVariable Long unitId) {
        try {
            String content = importExportService.exportUnitVocabularyTxt(unitId);
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "vocabulary.txt");
            headers.setContentLength(bytes.length);

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("导出 TXT 失败", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 导出单元词汇（JSON 格式）
     */
    @GetMapping("/unit/{unitId}/vocabulary/json")
    public ResponseEntity<byte[]> exportUnitVocabularyJson(@PathVariable Long unitId) {
        try {
            String content = importExportService.exportUnitVocabularyJson(unitId);
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentDispositionFormData("attachment", "vocabulary.json");
            headers.setContentLength(bytes.length);

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("导出 JSON 失败", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 导出完整课程（JSON 格式）
     */
    @GetMapping("/course/{courseId}/json")
    public ResponseEntity<byte[]> exportCourseJson(@PathVariable Long courseId) {
        try {
            String content = importExportService.exportCourseJson(courseId);
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentDispositionFormData("attachment", "course.json");
            headers.setContentLength(bytes.length);

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("导出课程失败", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Log(title = "导入完整课程JSON", businessType = BusinessType.IMPORT)
    @PostMapping("/course/full")
    public AjaxResult importFullCourseJson(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "courseId", required = false) String courseIdStr) {
        if (file.isEmpty()) {
            return AjaxResult.error("上传文件为空");
        }

        try {
            Long courseId = StringUtils.isNotEmpty(courseIdStr) ? Long.parseLong(courseIdStr) : null;

            Map<String, Object> result = importExportService.importFullCourseJson(file, courseId);

            if (Boolean.TRUE.equals(result.get("success"))) {
                StringBuilder msg = new StringBuilder("导入成功");
                if (Boolean.TRUE.equals(result.get("courseCreated"))) {
                    msg.append("，已创建新课程：").append(result.get("courseName"));
                }
                if (Boolean.TRUE.equals(result.get("unitCreated"))) {
                    msg.append("，已创建单元：").append(result.get("unitName"));
                }
                return AjaxResult.success(msg.toString(), result);
            } else {
                return AjaxResult.error("导入失败：" + result.get("error"));
            }
        } catch (NumberFormatException e) {
            logger.error("参数格式错误", e);
            return AjaxResult.error("参数格式错误：" + e.getMessage());
        } catch (Exception e) {
            logger.error("导入完整课程JSON失败", e);
            return AjaxResult.error("导入失败：" + e.getMessage());
        }
    }
}
