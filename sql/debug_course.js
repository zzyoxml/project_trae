const courses = [
  [1, '英语入门基础', 'EN-BASIC', null, 'en', 'beginner', 'general', '零基础英语学习，从字母、发音到日常会话的全面入门课程', '', '', null, 0, 0, 0, 0, 0, '1', 0, '1', '0', null, '掌握26个字母发音、100个基础单词、20个日常句型', '英语零基础,字母,发音,入门', 1, 0],
];

function esc(s) {
  return s ? s.replace(/\\/g, '\\\\').replace(/'/g, "\\'") : '';
}

const c = courses[0];
console.log('Total fields:', c.length);
console.log('Last field (del_flag):', JSON.stringify(c[c.length-1]));

const [id, name, code, catId, lang, level, type, desc, cover, video, teacher, dur, lessons, students, rating, ratCnt, free, price, pub, feat, prereq, obj, tags, diff, pop, del] = c;

const sql = `(${id}, '${esc(name)}', '${esc(code)}', ${catId || 'NULL'}, '${lang}', '${level}', '${type}', '${esc(desc)}', '${esc(cover)}', '${esc(video)}', ${teacher || 'NULL'}, ${dur}, ${lessons}, ${students}, ${rating}, ${ratCnt}, '${free}', ${price}, '${pub}', '${feat}', ${prereq ? `'${esc(prereq)}'` : 'NULL'}, '${esc(obj)}', '${esc(tags)}', ${diff}, ${pop}, '${del}')`;
console.log('Generated SQL:');
console.log(sql);