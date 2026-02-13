-- 查询语言不匹配的词汇
SELECT 
    v.vocab_id, 
    v.word, 
    v.language as vocab_language,
    u.unit_name,
    c.course_name, 
    c.language as course_language
FROM edu_vocabulary v
JOIN edu_course_unit u ON v.unit_id = u.unit_id
JOIN edu_course c ON u.course_id = c.course_id
WHERE v.language != c.language
  AND v.del_flag = '0';

-- 删除语言不匹配的词汇
DELETE FROM edu_vocabulary 
WHERE vocab_id IN (
    SELECT v.vocab_id
    FROM edu_vocabulary v
    JOIN edu_course_unit u ON v.unit_id = u.unit_id
    JOIN edu_course c ON u.course_id = c.course_id
    WHERE v.language != c.language
      AND v.del_flag = '0'
);

-- 显示删除结果
SELECT ROW_COUNT() as deleted_count;