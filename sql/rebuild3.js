const fs = require('fs');
const { execSync } = require('child_process');

const ddlPath = 'c:\\Users\\xml\\Documents\\Trae\\project_demo1\\RuoYi-Vue-master\\RuoYi-Vue-master\\sql\\edu_tables.sql';
const ddl = fs.readFileSync(ddlPath, 'utf8');

const esc = s => (s || '').replace(/\\/g, '\\\\').replace(/'/g, "\\'");

const courses = [
  [1,'英语入门基础','EN-BASIC','en','beginner','general','零基础英语学习，从字母、发音到日常会话的全面入门课程','掌握26个字母发音、100个基础单词、20个日常句型','英语零基础,字母,发音,入门',1],
  [2,'英语日常会话','EN-DAILY','en','elementary','conversation','日常生活场景中的实用英语对话，涵盖购物、问路、餐厅等高频场景','能进行基本的日常英语交流，听懂简单对话','日常对话,生活英语,实用口语',2],
  [3,'英语社交礼仪','EN-SOCIAL','en','intermediate','conversation','西方社交礼仪与地道表达，学会得体地打招呼、道歉、感谢、邀请等','掌握英语国家社交礼仪，表达自然得体','社交礼仪,文化,地道表达',3],
  [4,'英语医疗场景','EN-MEDICAL','en','intermediate','conversation','医院就诊、描述症状、购买药品等医疗场景的英语表达','能用英语在医院就诊并准确描述身体状况','医疗英语,看病,健康',3],
  [5,'英语职场沟通','EN-WORK','en','intermediate','conversation','会议、报告、邮件、deadline等职场场景的专业英语表达','能在职场环境中用英语进行基本沟通','职场英语,商务,会议',3],
  [6,'日语五十音与基础会话','JA-BASIC','ja','beginner','general','从日语五十音图开始，到基础问候语和日常会话','熟练掌握五十音图，能用日语进行简单自我介绍和问候','日语入门,五十音,假名',1]
];

let sql = 'SET NAMES utf8mb4;\nSET CHARACTER SET utf8mb4;\n';
sql += 'DROP TABLE IF EXISTS edu_vocabulary; DROP TABLE IF EXISTS edu_course_lesson; ';
sql += 'DROP TABLE IF EXISTS edu_course_unit; DROP TABLE IF EXISTS edu_course_chapter; DROP TABLE IF EXISTS edu_course;\n';
sql += ddl + '\n';

sql += "CREATE TABLE IF NOT EXISTS edu_vocabulary (\n";
sql += "  vocab_id bigint(20) NOT NULL AUTO_INCREMENT,\n";
sql += "  unit_id bigint(20) DEFAULT NULL,\n";
sql += "  word varchar(100) NOT NULL,\n";
sql += "  language varchar(10) NOT NULL,\n";
sql += "  pronunciation varchar(200) DEFAULT NULL,\n";
sql += "  part_of_speech varchar(20) DEFAULT NULL,\n";
sql += "  definition text,\n";
sql += "  example_sentences text,\n";
sql += "  del_flag char(1) DEFAULT '0',\n";
sql += "  PRIMARY KEY (vocab_id)\n";
sql += ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='词汇表';\n\n";

sql += "INSERT INTO edu_course(course_id,course_name,course_code,language,level,course_type,description,learning_objectives,tags,difficulty_score,is_free,is_published,del_flag) VALUES\n";
sql += courses.map(c => `(${c[0]},'${esc(c[1])}','${esc(c[2])}','${c[3]}','${c[4]}','${c[5]}','${esc(c[6])}','${esc(c[7])}','${esc(c[8])}',${c[9]},'1','1','0')`).join(',\n');
sql += ';\n';

const chData = [];
const uData = [];
let chId=1, uId=1;
for (const c of courses) {
  const chNames = {
    1:['字母与发音','日常问候','自我介绍','家庭成员','外貌特征','日常作息','社交礼仪','情感表达式','庆祝活动','医疗场景','职场沟通'],
    2:['见面问候','介绍他人','家庭话题','描述人物','一天作息','礼貌用语','表达情感','节日聚会','看病就医','工作场合'],
    3:['打招呼与告别','感谢与道歉','表达情绪','邀请与回应','赞美与祝贺','请求与拒绝'],
    4:['描述症状','看医生流程','买药与用药','急诊情况','健康建议'],
    5:['办公室日常','开会讨论','邮件写作','电话沟通','汇报工作'],
    6:['平假名あ行-な行','平假名は行-わ行','片假名与浊音','基础问候语','日常会话','颜色与数字','基础语法','汉字书写']
  };
  const names = chNames[c[0]] || chNames[1];
  for (let i=0; i<names.length; i++) {
    chData.push([chId, c[0], names[i]]);
    const uTemplates = [[names[i]+'-单元1', names[i]+'的第一单元'],[names[i]+'-单元2', names[i]+'的第二单元']];
    for (const [un, ud] of uTemplates) { uData.push([uId++, c[0], chId, un, ud]); }
    chId++;
  }
}

sql += "\nINSERT INTO edu_course_chapter(chapter_id,course_id,chapter_name,chapter_order,del_flag) VALUES\n";
sql += chData.map((ch,i) => `(${ch[0]},${ch[1]},'${esc(ch[2])}',${i%15+1},'0')`).join(',\n');
sql += ';\n';

sql += "\nINSERT INTO edu_course_unit(unit_id,course_id,chapter_id,unit_name,unit_order,description,del_flag) VALUES\n";
sql += uData.map((u,i) => `(${u[0]},${u[1]},${u[2]},'${esc(u[3])}',${i+1},'${esc(u[4])}','0')`).join(',\n');
sql += ';\n';

const lTypes = ['vocabulary','grammar','listening','speaking'];
const lLabels = ['单词学习','语法讲解','听力练习','口语训练'];
let lId = 1;
const lData = [];
for (const u of uData) {
  for (let t=0; t<lTypes.length; t++) {
    lData.push([lId++, u[0], `${u[3]}-${lLabels[t]}`, t+1, lTypes[t], 15]);
  }
}
sql += "\nINSERT INTO edu_course_lesson(lesson_id,unit_id,lesson_name,lesson_order,lesson_type,duration,del_flag) VALUES\n";
sql += lData.map(l => `(${l[0]},${l[1]},'${esc(l[2])}',${l[3]},'${l[4]}',${l[5]},'0')`).join(',\n');
sql += ';\n';

const vData = [
  [1,'hello','en','/həˈloʊ/','interj.','你好；喂',"Hello! Nice to meet you."],
  [1,'good morning','en','/ɡʊd ˈmɔːrnɪŋ/','phrase','早上好',"Good morning, class!"],
  [1,'thank you','en','/θæŋk juː/','phrase','谢谢你',"Thank you very much!"],
  [2,'name','en','/neɪm/','n.','名字',"What is your name?"],
  [2,'student','en','/ˈstuːdnt/','n.','学生',"She is a student."],
  [3,'father','en','/ˈfɑːðər/','n.','父亲',"My father is a doctor."],
  [3,'mother','en','/ˈmʌðər/','n.','母亲',"Happy Mother's Day!"],
  [4,'tall','en','/tɔːl/','adj.','高的',"He is very tall."],
  [4,'beautiful','en','/ˈbjuːtɪfl/','adj.','美丽的',"She looks beautiful."],
  [5,'breakfast','en','/ˈbrekfəst/','n.','早餐',"Breakfast is important."],
  [6,'sorry','en','/ˈsɒri/','adj.','对不起',"Sorry, I am late."],
  [6,'welcome','en','/ˈwelkəm/','interj.','欢迎',"You are welcome."],
  [7,'happy','en','/ˈhæpi/','adj.','高兴的',"I am happy today!"],
  [8,'party','en','/ˈpɑːrti/','n.','聚会',"Great party last night!"],
  [9,'hospital','en','/ˈhɒspɪtl/','n.','医院',"Go to hospital."],
  [10,'meeting','en','/ˈmiːtɪŋ/','n.','会议',"Meeting at 3 PM."],
  [11,'あいうえお','ja','/a i u e o/','hiragana','五十音第一行',"五十音の基礎"],
  [12,'はひふへほ','ja','/ha hi fu he ho/','hiragana','は行',"ふはfuと発音"],
  [13,'こんにちは','ja','/kon.nichi.wa/','greeting','白天问候',"こんにちは田中さん"],
  [14,'すみません','ja','/su.mi.ma.sen/','greeting','对不起',"すみません遅れました"],
  [15,'あか','ja','/aka/','adj.','红色',"あかいリンゴ"],
  [16,'いち','ja','/itɕi/','numeral','数字1',"いちがつ一月"],
  [17,'食べる','ja','/ta.be.ru/','verb','吃',"ご飯を食べます"],
  [18,'私','ja','/watashi/','pronoun','我',"私は学生です"],
  [19,'名前','ja','/namae/','n.','名字',"お名前は何ですか"],
  [21,'a','zh','/A/','final','韵母a',"阿姨阿婆"],
  [22,'b','zh','/p/','initial','声母b',"爸爸杯子"],
  [23,'zh','zh','/ʈʂ/','initial','翘舌zh',"中国知道"],
  [24,'ai','zh','/aɪ/','复韵母ai',"爱好大海"],
  [25,'an','zh','/an/','前鼻韵母an',"平安吃饭"],
  [30,'一','zh','/í/','汉字一',"一举两得"],
  [31,'日','zh','/ɻî/','太阳日',"日出东方"],
  [32,'亻','zh','/rénzìpáng/','单人旁',"他们伙伴"],
  [33,'语','zh','/yǔ̌/','语言语',"汉语语文"]
];

sql += "\nDELETE FROM edu_vocabulary; ALTER TABLE edu_vocabulary AUTO_INCREMENT=1;\n";
sql += "INSERT INTO edu_vocabulary(unit_id,word,language,pronunciation,part_of_speech,definition,example_sentences,del_flag) VALUES\n";
sql += vData.map(v => `(${v[0]},'${esc(v[1])}','${v[2]}','${esc(v[3])}','${esc(v[4])}','${esc(v[5])}','${esc(v[6])}','0')`).join(',\n');
sql += ';\n';

const outFile = 'c:\\Users\\xml\\Documents\\Trae\\project_demo1\\RuoYi-Vue-master\\RuoYi-Vue-master\\sql\\_temp_full.sql';
fs.writeFileSync(outFile, sql, 'utf8');

try {
  const buf = fs.readFileSync(outFile);
  console.log('SQL file size:', buf.length, 'bytes');
  console.log('First 200 chars:', sql.substring(0, 200));
  const result = execSync('mysql -u root -proot --default-character-set=utf8mb4 ryvue', {
    input: sql,
    encoding: 'utf8',
    timeout: 60000
  });
  console.log('\nSUCCESS!');
} catch(e) {
  console.log('ERROR:', e.stderr?.toString()?.substring(0, 500));
}