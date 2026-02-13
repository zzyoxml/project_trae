const mysql = require('mysql2');

const connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: 'password',
  database: 'ryvue'
});

connection.connect((err) => {
  if (err) {
    console.error('连接失败:', err);
    process.exit(1);
  }
  console.log('数据库连接成功');
  
  // 查询所有课程
  connection.query('SELECT course_id, course_name, language FROM edu_course', (err, courses) => {
    if (err) {
      console.error('查询课程失败:', err);
      connection.end();
      return;
    }
    
    console.log('\n=== 课程列表 ===');
    courses.forEach(course => {
      console.log(`${course.course_id}: ${course.course_name} (${course.language})`);
    });
    
    // 查询所有单元及其所属课程
    connection.query(`
      SELECT u.unit_id, u.unit_name, c.course_id, c.course_name, c.language as course_language
      FROM edu_course_unit u
      JOIN edu_course c ON u.course_id = c.course_id
    `, (err, units) => {
      if (err) {
        console.error('查询单元失败:', err);
        connection.end();
        return;
      }
      
      console.log('\n=== 单元列表 ===');
      units.forEach(unit => {
        console.log(`${unit.unit_id}: ${unit.unit_name} (课程: ${unit.course_name}, 语言: ${unit.course_language})`);
      });
      
      // 查询有问题的词汇（词汇语言与课程语言不匹配）
      connection.query(`
        SELECT v.vocab_id, v.word, v.language as vocab_language, 
               u.unit_id, u.unit_name, 
               c.course_id, c.course_name, c.language as course_language
        FROM edu_vocabulary v
        JOIN edu_course_unit u ON v.unit_id = u.unit_id
        JOIN edu_course c ON u.course_id = c.course_id
        WHERE v.language != c.language
      `, (err, mismatchedVocab) => {
        if (err) {
          console.error('查询不匹配词汇失败:', err);
          connection.end();
          return;
        }
        
        console.log('\n=== 语言不匹配的词汇 ===');
        if (mismatchedVocab.length === 0) {
          console.log('没有找到语言不匹配的词汇');
          connection.end();
          return;
        }
        
        mismatchedVocab.forEach(vocab => {
          console.log(`${vocab.vocab_id}: 单词="${vocab.word}" 词汇语言=${vocab.vocab_language} 课程语言=${vocab.course_language} 单元=${vocab.unit_name}`);
        });
        
        // 删除这些不匹配的词汇
        const vocabIds = mismatchedVocab.map(v => v.vocab_id);
        connection.query(`DELETE FROM edu_vocabulary WHERE vocab_id IN (${vocabIds.join(',')})`, (err, result) => {
          if (err) {
            console.error('删除失败:', err);
            connection.end();
            return;
          }
          console.log(`\n成功删除 ${result.affectedRows} 条语言不匹配的词汇记录`);
          connection.end();
        });
      });
    });
  });
});