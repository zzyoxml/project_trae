@echo off
echo 正在修复语言不匹配的词汇...
mysql -u root -ppassword ryvue -e "DELETE FROM edu_vocabulary WHERE vocab_id IN (SELECT v.vocab_id FROM edu_vocabulary v JOIN edu_course_unit u ON v.unit_id = u.unit_id JOIN edu_course c ON u.course_id = c.course_id WHERE v.language != c.language AND v.del_flag = '0')"
echo 修复完成！
pause