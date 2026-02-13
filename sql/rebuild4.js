const { execSync } = require('child_process');

function u(str) {
  return Buffer.from(str, 'utf8').toString('utf8');
}

function esc(s) {
  return s ? s.replace(/\\/g, '\\\\').replace(/'/g, "\\'") : '';
}

let sql = '';
sql += 'SET NAMES utf8mb4;\n';
sql += 'SET CHARACTER SET utf8mb4;\n';

sql += 'DROP TABLE IF EXISTS edu_vocabulary; DROP TABLE IF EXISTS edu_course_lesson;\n';
sql += 'DROP TABLE IF EXISTS edu_course_unit; DROP TABLE IF EXISTS edu_course_chapter; DROP TABLE IF EXISTS edu_course;\n\n';

sql += "CREATE TABLE IF NOT EXISTS edu_course (\n";
sql += "  course_id bigint(20) NOT NULL AUTO_INCREMENT,\n";
sql += "  course_name varchar(100) NOT NULL,\n";
sql += "  course_code varchar(50),\n";
sql += "  language varchar(10) DEFAULT 'en',\n";
sql += "  level varchar(20),\n";
sql += "  course_type varchar(30),\n";
sql += "  description text,\n";
sql += "  learning_objectives text,\n";
sql += "  tags varchar(200),\n";
sql += "  difficulty_score int DEFAULT 1,\n";
sql += "  is_free char(1) DEFAULT '1',\n";
sql += "  is_published char(1) DEFAULT '1',\n";
sql += "  del_flag char(1) DEFAULT '0',\n";
sql += "  PRIMARY KEY (course_id)\n";
sql += ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';\n\n";

sql += "CREATE TABLE IF NOT EXISTS edu_course_chapter (\n";
sql += "  chapter_id bigint(20) NOT NULL AUTO_INCREMENT,\n";
sql += "  course_id bigint(20) NOT NULL,\n";
sql += "  chapter_name varchar(100) NOT NULL,\n";
sql += "  chapter_order int DEFAULT 1,\n";
sql += "  description text,\n";
sql += "  del_flag char(1) DEFAULT '0',\n";
sql += "  PRIMARY KEY (chapter_id)\n";
sql += ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程章节表';\n\n";

sql += "CREATE TABLE IF NOT EXISTS edu_course_unit (\n";
sql += "  unit_id bigint(20) NOT NULL AUTO_INCREMENT,\n";
sql += "  course_id bigint(20) NOT NULL,\n";
sql += "  chapter_id bigint(20) DEFAULT NULL,\n";
sql += "  unit_name varchar(100) NOT NULL,\n";
sql += "  unit_order int DEFAULT 1,\n";
sql += "  description text,\n";
sql += "  del_flag char(1) DEFAULT '0',\n";
sql += "  PRIMARY KEY (unit_id)\n";
sql += ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程单元表';\n\n";

sql += "CREATE TABLE IF NOT EXISTS edu_course_lesson (\n";
sql += "  lesson_id bigint(20) NOT NULL AUTO_INCREMENT,\n";
sql += "  unit_id bigint(20) NOT NULL,\n";
sql += "  lesson_name varchar(100) NOT NULL,\n";
sql += "  lesson_order int DEFAULT 1,\n";
sql += "  lesson_type varchar(20),\n";
sql += "  content text,\n";
sql += "  duration int DEFAULT 15,\n";
sql += "  del_flag char(1) DEFAULT '0',\n";
sql += "  PRIMARY KEY (lesson_id)\n";
sql += ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课时表';\n\n";

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

const courses = [
  [1,u('英语入门基础'),'EN-BASIC','en','beginner','general',u('零基础英语学习，从字母、发音到日常会话'),u('掌握字母发音、基础单词、日常句型'),u('英语零基础,字母,发音,入门'),1],
  [2,u('英语日常会话'),'EN-DAILY','en','elementary','conversation',u('日常生活场景中的实用英语对话'),u('能进行基本的日常英语交流'),u('日常对话,生活英语,实用口语'),2],
  [3,u('英语社交礼仪'),'EN-SOCIAL','en','intermediate','conversation',u('西方社交礼仪与地道表达'),u('掌握英语国家社交礼仪'),u('社交礼仪,文化,地道表达'),3],
  [4,u('英语医疗场景'),'EN-MEDICAL','en','intermediate','conversation',u('医院就诊、描述症状等医疗场景英语'),u('能用英语在医院就诊并准确描述身体状况'),u('医疗英语,看病,健康'),3],
  [5,u('英语职场沟通'),'EN-WORK','en','intermediate','conversation',u('会议、报告、邮件等职场场景专业英语'),u('能在职场环境中用英语进行基本沟通'),u('职场英语,商务,会议'),3],
  [6,u('日语五十音与基础会话'),'JA-BASIC','ja','beginner','general',u('从日语五十音图开始，到基础问候语和日常会话'),u('熟练掌握五十音图，能用日语进行简单自我介绍和问候'),u('日语入门,五十音,假名'),1]
];

sql += "INSERT INTO edu_course(course_id,course_name,course_code,language,level,course_type,description,learning_objectives,tags,difficulty_score,is_free,is_published,del_flag) VALUES\n";
sql += courses.map(c => `(${c[0]},'${esc(c[1])}','${c[2]}','${c[3]}','${c[4]}','${c[5]}','${esc(c[6])}','${esc(c[7])}','${esc(c[8])}',${c[9]},'1','1','0')`).join(',\n');
sql += ';\n\n';

const chData = [];
const uData = [];
let chId=1, uId=1;
for (const c of courses) {
  const names = {
    1:[u('字母与发音'),u('日常问候'),u('自我介绍'),u('家庭成员'),u('外貌特征'),u('日常作息'),u('社交礼仪'),u('情感表达式'),u('庆祝活动'),u('医疗场景'),u('职场沟通')],
    2:[u('见面问候'),u('介绍他人'),u('家庭话题'),u('描述人物'),u('一天作息'),u('礼貌用语'),u('表达情感'),u('节日聚会'),u('看病就医'),u('工作场合')],
    3:[u('打招呼与告别'),u('感谢与道歉'),u('表达情绪'),u('邀请与回应'),u('赞美与祝贺'),u('请求与拒绝')],
    4:[u('描述症状'),u('看医生流程'),u('买药与用药'),u('急诊情况'),u('健康建议')],
    5:[u('办公室日常'),u('开会讨论'),u('邮件写作'),u('电话沟通'),u('汇报工作')],
    6:[u('平假名あ行-な行'),u('平假名は行-わ行'),u('片假名与浊音'),u('基础问候语'),u('日常会话'),u('颜色与数字'),u('基础语法'),u('汉字书写')]
  };
  const ns = names[c[0]] || names[1];
  for (let i=0; i<ns.length; i++) {
    chData.push([chId, c[0], ns[i]]);
    const utemplates = [[ns[i]+u('-单元1'), ns[i]+u('的第一单元')],[ns[i]+u('-单元2'), ns[i]+u('的第二单元')]];
    for (const [un, ud] of utemplates) { uData.push([uId++, c[0], chId, un, ud]); }
    chId++;
  }
}

sql += "INSERT INTO edu_course_chapter(chapter_id,course_id,chapter_name,chapter_order,del_flag) VALUES\n";
sql += chData.map((ch,i) => `(${ch[0]},${ch[1]},'${esc(ch[2])}',${i%15+1},'0')`).join(',\n');
sql += ';\n\n';

sql += "INSERT INTO edu_course_unit(unit_id,course_id,chapter_id,unit_name,unit_order,description,del_flag) VALUES\n";
sql += uData.map((u,i) => `(${u[0]},${u[1]},${u[2]},'${esc(u[3])}',${i+1},'${esc(u[4])}','0')`).join(',\n');
sql += ';\n\n';

const lTypes=['vocabulary','grammar','listening','speaking'];
const lLabels=[u('单词学习'),u('语法讲解'),u('听力练习'),u('口语训练')];
let lId=1, lData=[];
for (const u of uData) {
  for (let t=0;t<lTypes.length;t++) { lData.push([lId++, u[0], u[3]+'-'+lLabels[t], t+1, lTypes[t], 15]); }
}
sql += "INSERT INTO edu_course_lesson(lesson_id,unit_id,lesson_name,lesson_order,lesson_type,duration,del_flag) VALUES\n";
sql += lData.map(l => `(${l[0]},${l[1]},'${esc(l[2])}',${l[3]},'${l[4]}',${l[5]},'0')`).join(',\n');
sql += ';\n\n';

const vData=[
  [1,'hello','en','/h\u0259lo\u028A/','interj.',u('\u4F60\u597D\uFF1B\u5582'),"Hello! Nice to meet you."],
  [1,'good morning','en','/\u0261\u028Ad m\u0254\u02D0rn\u026A\u014B/',u('phrase'),u('\u65E9\u4E0A\u597D'),"Good morning, class!"],
  [1,'thank you','en','/\u03B8\u00E6\u014Bk ju\u02D0/',u('phrase'),u('\u8C22\u8C22\u4F60\uFF1B\u611F\u8C22'),"Thank you very much!"],
  [2,'name','en','/ne\u026Am/','n.',u('\u540D\u5B57\uFF1B\u540D\u79F0'),"What is your name?"],
  [2,'student','en,'/'stju\u02D0dnt/','n.',u('\u5B66\u751F'),"She is a student."],
  [3,'father','en','/f\u0251\u02D0\u00F0\u0259r/','n.',u('\u7236\u4EB2\uFF1B\u7238\u7238'),"My father is a doctor."],
  [3,'mother','en','/m\u028C\u00F0\u0259r/','n.',u('\u6BCD\u4EB2\uFF1B\u5988\u5988'),"Happy Mother's Day!"],
  [4,'tall','en','/t\u0254\u02D0l/','adj.',u('\u9AD8\u7684'),"He is very tall."],
  [4,'beautiful','en,'/'bju\u02D0t\u026Afl/','adj.',u('\u7F8E\u4E3D\u7684\uFF1B\u6F02\u4EAE\u7684'),"She looks beautiful."],
  [5,'breakfast','en','/brekf\u0259st/','n.',u('\u65E9\u9910\uFF1B\u65E9\u996D'),"Breakfast is important."],
  [6,'sorry','en,'/'s\u0D92ri/','adj.',u('\u5BF9\u4E0D\u8D77\uFF1B\u62B1\u6B49\u7684'),"Sorry, I am late."],
  [6,'welcome','en,'/'welk\u0259m/','interj.',u('\u6B22\u8FCE\u7684\uFF1B\u4E0D\u5BA2\u6C14'),"You are welcome."],
  [7,'happy','en,'/'h\u00E6pi/','adj.',u('\u9AD8\u5174\u7684\uFF1B\u5FEB\u4E50\u7684'),"I am happy today!"],
  [8,'party','en,'/'p\u0251\u02D0rti/','n.',u('\u805A\u4F1A\uFF1B\u6D3E\u5BF9'),"Great party last night!"],
  [9,'hospital','en,'/'h\u0D94sp\u026Atl/','n.',u('\u533B\u9662'),"Go to hospital."],
  [10,'meeting','en,'/'mi\u02DAt\u026A\u014B/','n.',u('\u4F1A\u8BAE\uFF1B\u4F1A\u9762'),"Meeting at 3 PM."],
  [11,'\u3042\u3044\u3046\u3048\u304A','ja','/a i u e o/','hiragana',u('\u4E94\u5341\u97F3\u7B2C\u4E00\u884C'),u('\u4E94\u5341\u97F3\u306E\u57FA\u7840')],
  [12,'\u306F\u3072\u3075\u3078\u307B','ja','/ha hi fu he ho/','hiragana',u('\u306F\u884C'),u('\u3075\u306FFU\u3068\u767A\u97F3')],
  [13,'\u3053\u3093\u306B\u3061\u306F','ja','/kon.nichi.wa/','greeting',u('\u767D\u5929\u95EE\u5019'),u('\u3053\u3093\u306B\u3061\u306F\u7530\u4E2D\u3055\u3093')],
  [14,'\u3059\u307F\u307E\u305B\u3093','ja','/su.mi.ma.sen/','greeting',u('\u5BF9\u4E0D\u8D77'),u('\u3059\u307F\u307E\u305B\u3093\u9045\u308C\u307E\u3057\u305F')],
  [15,'\u3042\u304B','ja','/aka/','adj.',u('\u7EA2\u8272'),u('\u3042\u304B\u3044\u30EA\u30F3\u3054')],
  [16,'\u3044\u3061','ja','/it\u0955i/','numeral',u('\u6570\u5B571'),u('\u3044\u3061\u304C\u3064\u4E00\u6708')],
  [17,'\u98DF\u3079\u308B','ja','/ta.be.ru/','verb',u('\u5403'),u('\u3054\u98EF\u3092\u98DF\u3079\u307E\u3059')],
  [18,'\u79C1','ja','/watashi/','pronoun',u('\u6211'),u('\u79C1\u306F\u5B66\u751F\u3067\u3059')],
  [19,'\u540D\u524D','ja','/namae/','n.',u('\u540D\u5B57'),u('\u304A\u540D\u524D\u306F\u4F55\u3067\u3059\u304B')],
  [21,'a','zh','/A/','final',u('韵母a'),u('\u963F\u59E8\u963F\u5A46')],
  [22,'b','zh','/p/','initial',u('声母b'),u('\u7238\u7238\u676F\u5B50')],
  [23,'zh','zh','/\u0288\u0282s/','initial',u('翘舌zh'),u('\u4E2D\u56FD\u77E5\u9053')],
  [24,'ai','zh','/a\u026A/','复韵母ai',u('爱好大海'),u('\u7231\u597D\u5927\u6D77')],
  [25,'an','zh','/an/','前鼻韵母an',u('平安吃饭'),u('\u5E73\u5B89\u5403\u996D')],
  [30,'\u4E00','zh','/i\u0301/','汉字一',u('一举两得'),u('\u4E00\u4E3E\u4E24\u5F97')],
  [31,'\u65E5','zh','/\u027Bi\u0302/','太阳日',u('日出东方'),u('\u65E5\u51FA\u4E1C\u65B9')],
  [32,'\u4EC5','zh','/r\xE9nz\xECp\xE1ng/','单人旁',u('他们伙伴'),u('\u4ED6\u4EEC\u4F19\u4F34')],
  [33,'\u8BED','zh','/y\u01D4\u030C/','语言语',u('汉语语文'),u('\u6C49\u8BED\u8BED\u6587')]
];

sql += "DELETE FROM edu_vocabulary; ALTER TABLE edu_vocabulary AUTO_INCREMENT=1;\n";
sql += "INSERT INTO edu_vocabulary(unit_id,word,language,pronunciation,part_of_speech,definition,example_sentences,del_flag) VALUES\n";
sql += vData.map(v => `(${v[0]},'${esc(v[1])}','${v[2]}','${esc(v[3])}','${esc(v[4])}','${esc(v[5])}','${esc(v[6])}','0')`).join(',\n');
sql += ';\n';

try {
  execSync('mysql -u root -proot --default-character-set=utf8mb4 ryvue', {
    input: sql,
    encoding: 'utf8',
    timeout: 60000
  });
  console.log('SUCCESS!');
} catch(e) {
  console.log('ERROR:', e.stderr?.toString()?.substring(0,500));
}