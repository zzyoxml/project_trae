const { execSync } = require('child_process');

function esc(s) { return s ? String(s).replace(/\\/g, '\\\\').replace(/'/g, "\\'") : ''; }

let sql = '';

sql += 'SET NAMES utf8mb4;\n';
sql += 'SET CHARACTER SET utf8mb4;\n';

sql += 'TRUNCATE TABLE edu_vocabulary;\n';
sql += 'TRUNCATE TABLE edu_course_lesson;\n';
sql += 'TRUNCATE TABLE edu_course_unit;\n';
sql += 'TRUNCATE TABLE edu_course_chapter;\n';
sql += 'TRUNCATE TABLE edu_course;\n\n';

sql += "ALTER DATABASE ryvue CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;\n\n";

sql += "ALTER TABLE edu_course CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;\n";
sql += "ALTER TABLE edu_course_chapter CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;\n";
sql += "ALTER TABLE edu_course_unit CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;\n";
sql += "ALTER TABLE edu_course_lesson CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;\n";
sql += "ALTER TABLE edu_vocabulary CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;\n\n";

const courses = [
  [1, '英语入门基础', 'EN-BASIC', 'en', 'beginner', 'general', '零基础英语学习，从字母、发音到日常会话的全面入门课程', '掌握26个字母发音、100个基础单词、20个日常句型', '英语零基础,字母,发音,入门', 1],
  [2, '英语日常会话', 'EN-DAILY', 'en', 'elementary', 'conversation', '日常生活场景中的实用英语对话，涵盖购物、问路、餐厅等高频场景', '能进行基本的日常英语交流，听懂简单对话', '日常对话,生活英语,实用口语', 2],
  [3, '英语社交礼仪', 'EN-SOCIAL', 'en', 'intermediate', 'conversation', '西方社交礼仪与地道表达，学会得体地打招呼、道歉、感谢、邀请等', '掌握英语国家社交礼仪，表达自然得体', '社交礼仪,文化,地道表达', 3],
  [4, '英语医疗场景', 'EN-MEDICAL', 'en', 'intermediate', 'conversation', '医院就诊、描述症状、购买药品等医疗场景的英语表达', '能用英语在医院就诊并准确描述身体状况', '医疗英语,看病,健康', 3],
  [5, '英语职场沟通', 'EN-WORK', 'en', 'intermediate', 'conversation', '会议、报告、邮件、deadline等职场场景的专业英语表达', '能在职场环境中用英语进行基本沟通', '职场英语,商务,会议', 3],
  [6, '日语五十音与基础会话', 'JA-BASIC', 'ja', 'beginner', 'general', '从日语五十音图（平假名/片假名）开始，到基础问候语和日常会话', '熟练掌握五十音图，能用日语进行简单自我介绍和问候', '日语入门,五十音,假名', 1]
];

sql += "-- Courses\n";
sql += "INSERT INTO edu_course(course_id,course_name,course_code,language,level,course_type,description,learning_objectives,tags,difficulty_score,is_free,is_published,del_flag) VALUES\n";
sql += courses.map(c => `(${c[0]},'${esc(c[1])}','${esc(c[2])}','${c[3]}','${c[4]}','${c[5]}','${esc(c[6])}','${esc(c[7])}','${esc(c[8])}',${c[9]},'1','1','0')`).join(',\n');
sql += ';\n\n';

const chapterMap = {
  1: [
    ['字母与发音', '26个英文字母的标准发音与书写'],
    ['日常问候', 'Hello, Good morning等基础问候语'],
    ['自我介绍', '名字、国籍、职业的自我介绍'],
    ['家庭成员', 'father, mother等家庭成员词汇'],
    ['外貌特征', 'tall, beautiful等外形描述词'],
    ['日常作息', 'wake up, breakfast等日常活动词汇'],
    ['社交礼仪', 'sorry, excuse me等礼貌用语'],
    ['情感表达式', 'happy, sad等情感表达'],
    ['庆祝活动', 'birthday, party等庆祝活动词汇'],
    ['医疗场景', 'hospital, doctor等医疗相关'],
    ['职场沟通', 'meeting, report等职场词汇']
  ],
  2: [
    ['见面问候', '初次见面的标准寒暄'],
    ['介绍他人', '如何向他人介绍你的朋友家人'],
    ['家庭话题', '谈论家人的基本信息'],
    ['描述人物', '描述一个人的外貌性格'],
    ['一天作息', '从早到晚的日常活动'],
    ['礼貌用语', '道歉、感谢、请求帮助'],
    ['表达情感', '喜怒哀乐的正确表达方式'],
    ['节日聚会', '生日派对、节日聚会场景'],
    ['看病就医', '去医院看病的完整流程'],
    ['工作场合', '办公室里的常用表达']
  ],
  3: [
    ['打招呼与告别', '不同时段的问候方式'],
    ['感谢与道歉', '如何得体地表示感谢和歉意'],
    ['表达情绪', '各种情绪的地道表达'],
    ['邀请与回应', '发出邀请和接受/拒绝邀请'],
    ['赞美与祝贺', '赞美他人和祝贺特殊时刻'],
    ['请求与拒绝', '提出请求和委婉拒绝']
  ],
  4: [
    ['描述症状', '头痛发烧等各种症状描述'],
    ['看医生流程', '挂号、问诊、检查全流程'],
    ['买药与用药', '药房买药和服药说明'],
    ['急诊情况', '紧急情况的应对方法'],
    ['健康建议', '医生给出的健康建议']
  ],
  5: [
    ['办公室日常', '办公室日常交流'],
    ['开会讨论', '开会时的发言和讨论'],
    ['邮件写作', '商务邮件的写作规范'],
    ['电话沟通', '电话接打技巧'],
    ['汇报工作', '向上级汇报工作进展']
  ],
  6: [
    ['平假名（あ行-な行）', '日语前半部分平假名'],
    ['平假名（は行-わ行）', '后半部分平假名'],
    ['片假名与浊音', '片假名及拗音促音'],
    ['基础问候语', 'こんにちは等基础问候'],
    ['日常会话', '日常对话句型'],
    ['颜色与数字', '颜色名称和数字读法'],
    ['基础语法', 'はです等基础语法'],
    ['汉字书写', '基础汉字和偏旁部首']
  ]
};

const unitTemplates = {
  '字母与发音': [
    ['字母A-E', 'a,b,c,d,e的发音与例词'],
    ['字母F-J', 'f,g,h,i,j的发音与例词'],
    ['字母K-O', 'k,l,m,n,o的发音与例词'],
    ['字母P-T', 'p,q,r,s,t的发音与例词'],
    ['字母U-Z', 'u,v,w,x,y,z的发音与例词']
  ],
  '日常问候': [
    ['Hello与Goodbye', 'Hello! Nice to meet you. Goodbye! See you tomorrow.'],
    ['Good Morning等问候', 'Good morning, Good afternoon, Good evening'],
    ['How are you?', '询问近况的回答方式']
  ],
  '自我介绍': [
    ['Name与Come from', 'What is your name? Where do you come from?'],
    ['Student与Engineer', '职业相关的自我介绍词汇']
  ],
  '家庭成员': [
    ['Father与Mother', '家庭成员核心词汇'],
    ['Brother与Sister', '兄弟姐妹及相关表达'],
    ['Family', '关于家庭的综合表达']
  ],
  '外貌特征': [
    ['Tall与Beautiful', '形容人的外观词汇'],
    ['Friendly与Young', '性格与年龄相关描述词']
  ],
  '日常作息': [
    ['Wake Up与Breakfast', '早晨起床和早餐相关'],
    ['Usually与Every Day', '频率副词的使用']
  ],
  '社交礼仪': [
    ['Sorry与Excuse Me', '道歉和请求注意的表达'],
    ['Apologize与Welcome', '更正式的礼貌用语']
  ],
  '情感表达式': [
    ['Happy与Sad', '基本情绪词汇'],
    ['Angry与Excited', '强烈情绪的表达']
  ],
  '庆祝活动': [
    ['Celebration与Invite', '庆祝活动和邀请'],
    ['Amazing与Party', '派对相关词汇']
  ],
  '医疗场景': [
    ['Hospital与Doctor', '医院和医生相关'],
    ['Fever与Medicine', '症状和药物词汇'],
    ['Ambulance与Prescription', '急救和处方']
  ],
  '职场沟通': [
    ['Deadline与Meeting', '截止日期和会议'],
    ['Report与Project', '工作报告和项目']
  ],
  '平假名（あ行-な行）': [
    ['あいうえお', '第一行元音'],
    ['かきくけこ', 'か行清音'],
    ['さしすせそ', 'さ行（し=shi）'],
    ['たちつてと', 'た行（ち=chi,つ=tsu）'],
    ['なにぬねの', 'な行鼻音']
  ],
  '平假名（は行-わ行）': [
    ['はひふへほ', 'は行（ふ=fu）'],
    ['まみむめも', 'ま行鼻音'],
    ['やゆよ', 'や行三字'],
    ['らりるれろ', 'ら行弹音'],
    ['わをん', 'わ行和拨音ん']
  ],
  '片假名与浊音': [
    ['アイウエオ', '片假名对应'],
    ['ガギグゲゴ', '浊音'],
    ['促音与长音', 'っ和ーの使用'],
    ['容易混淆的假名', 'ぬvsの, ねvsれ']
  ],
  '基础问候语': [
    ['こんにちは/こんばんは', '白天和晚上的问候'],
    ['おはようございます/さようなら', '早上好和再见'],
    ['ありがとうございます/すみません', '感谢和道歉'],
    ['初めまして/お疲れ様です', '初次见面和同事问候'],
    ['いただきます/ごちそうさまでした', '饭前饭后用语']
  ],
  '日常会话': [
    ['颜色词汇', 'あか/あお/しろ/くろ'],
    ['数字1-10', 'いち/に/さん...'],
    ['人称代词', '私/あなた/彼/彼女'],
    ['基本动词', '食べる/飲む/行く/来る'],
    ['常用名词', '名前/国/言葉/時間']
  ],
  '颜色与数字': [
    ['あいうえお复习', '五个元音'],
    ['bpmf声母', '双唇音和唇齿音'],
    ['dtnl声母', '舌尖中音'],
    ['gkh声母', '舌根音']
  ],
  '基础语法': [
    ['jqx声母', '舌面音'],
    ['zhchshr声母', '翘舌音'],
    ['zcs声母', '平舌音'],
    ['ai/ei/ao/ou复韵母', '复韵母']
  ],
  '汉字书写': [
    ['an/en/ang/eng鼻韵母', '前后鼻音'],
    ['横竖撇捺', '基本笔画'],
    ['日月山水', '基础汉字'],
    ['亻艹氵钅', '常用偏旁'],
    ['语思学好', '常用字词']
  ]
};

const chapters = [];
const units = [];
let chId = 1;
let uId = 1;

for (const c of courses) {
  const cid = c[0];
  const chList = chapterMap[cid] || chapterMap[1];
  for (let i = 0; i < chList.length; i++) {
    const [chName, chDesc] = chList[i];
    chapters.push([chId, cid, chName, chDesc]);
    const utpls = unitTemplates[chName] || [[`${chName}-单元1`, `${chName}的第一单元`]];
    for (const [uname, udesc] of utpls) {
      units.push([uId++, cid, chId, uname, udesc]);
    }
    chId++;
  }
}

sql += "-- Chapters\n";
sql += "INSERT INTO edu_course_chapter(chapter_id,course_id,chapter_name,chapter_order,description,del_flag) VALUES\n";
sql += chapters.map((ch, i) => `(${ch[0]},${ch[1]},'${esc(ch[2])}',${i + 1},'${esc(ch[3])}','0')`).join(',\n');
sql += ';\n\n';

sql += "-- Units\n";
sql += "INSERT INTO edu_course_unit(unit_id,course_id,chapter_id,unit_name,unit_order,description,del_flag) VALUES\n";
sql += units.map((u, i) => `(${u[0]},${u[1]},${u[2]},'${esc(u[3])}',${i + 1},'${esc(u[4])}','0')`).join(',\n');
sql += ';\n\n';

const lessonTypes = ['vocabulary', 'grammar', 'listening', 'speaking'];
const typeLabels = { vocabulary: '单词学习', grammar: '语法讲解', listening: '听力练习', speaking: '口语训练' };
const lessons = [];
let lId = 1;

for (const u of units) {
  for (let t = 0; t < lessonTypes.length; t++) {
    lessons.push([lId++, u[0], `${u[3]}-${typeLabels[lessonTypes[t]]}`, t + 1, lessonTypes[t], 15]);
  }
}

sql += "-- Lessons\n";
sql += "INSERT INTO edu_course_lesson(lesson_id,unit_id,lesson_name,lesson_order,lesson_type,duration,del_flag) VALUES\n";
sql += lessons.map(l => `(${l[0]},${l[1]},'${esc(l[2])}',${l[3]},'${l[4]}',${l[5]},'0')`).join(',\n');
sql += ';\n\n';

const vocabData = [
  { unit: 1, word: 'hello', lang: 'en', pron: '/h\u0259\u02C8lo\u028A/', pos: 'interj.', def: '\u4F60\u597D\uFF1B\u5582', ex: 'Hello! Nice to meet you.' },
  { unit: 1, word: 'good morning', lang: 'en', pron: '/\u0261\u028Ad m\u0254\u02D0rn\u026A\u014B/', pos: 'phrase', def: '\u65E9\u4E0A\u597D', ex: 'Good morning, class!' },
  { unit: 1, word: 'thank you', lang: 'en', pron: '/\u03B8\u00E6\u014Bk ju\u02D0/', pos: 'phrase', def: '\u8C22\u8C22\u4F60\uFF1B\u611F\u8C22', ex: 'Thank you very much!' },
  { unit: 2, word: 'name', lang: 'en', pron: '/ne\u026Am/', pos: 'n.', def: '\u540D\u5B57\uFF1B\u540D\u79F0', ex: 'What is your name?' },
  { unit: 2, word: 'student', lang: 'en', pron: '/\u02B8stu\u02D0dnt/', pos: 'n.', def: '\u5B66\u751F', ex: 'She is a student.' },
  { unit: 3, word: 'father', lang: 'en', pron: '/\u02B8f\u0251\u02D0\u00F0\u0259r/', pos: 'n.', def: '\u7236\u4EB2\uFF1B\u7238\u7238', ex: 'My father is a doctor.' },
  { unit: 3, word: 'mother', lang: 'en', pron: '/\u02B8m\u028C\u00F0\u0259r/', pos: 'n.', def: '\u6BCD\u4EB2\uFF1B\u5988\u5988', ex: 'Happy Mother\'s Day!' },
  { unit: 4, word: 'tall', lang: 'en', pron: '/t\u0254\u02D0l/', pos: 'adj.', def: '\u9AD8\u7684', ex: 'He is very tall.' },
  { unit: 4, word: 'beautiful', lang: 'en', pron: '/\u02B8bju\u02D0t\u026Afl/', pos: 'adj.', def: '\u7F8E\u4E3D\u7684', ex: 'She looks beautiful.' },
  { unit: 5, word: 'breakfast', lang: 'en', pron: '/\u02B8brekf\u0259st/', pos: 'n.', def: '\u65E9\u9910\uFF1B\u65E9\u996D', ex: 'Breakfast is important.' },
  { unit: 6, word: 'sorry', lang: 'en', pron: '/\u02B8s\u0D92ri/', pos: 'adj.', def: '\u5BF9\u4E0D\u8D77', ex: 'Sorry, I am late.' },
  { unit: 6, word: 'welcome', lang: 'en', pron: '/\u02B8welk\u0259m/', pos: 'interj.', def: '\u6B22\u8FCE\uFF1B\u4E0D\u5BA2\u6C14', ex: 'You are welcome.' },
  { unit: 7, word: 'happy', lang: 'en', pron: '/\u02B8h\u00E6pi/', pos: 'adj.', def: '\u9AD8\u5174\u7684\uFF1B\u5FEB\u4E50\u7684', ex: 'I am happy today!' },
  { unit: 8, word: 'party', lang: 'en', pron: '/\u02B8p\u0251\u02D0rti/', pos: 'n.', def: '\u805A\u4F1A\uFF1B\u6D3E\u5BF9', ex: 'Great party last night!' },
  { unit: 9, word: 'hospital', lang: 'en', pron: '/\u02B8h\u0D94sp\u026Atl/', pos: 'n.', def: '\u533B\u9662', ex: 'Go to hospital.' },
  { unit: 10, word: 'meeting', lang: 'en', pron: '/\u02B8mi\u02DAt\u026A\u014B/', pos: 'n.', def: '\u4F1A\u8BAE\uFF1B\u4F1A\u9762', ex: 'Meeting at 3 PM.' },
  { unit: 11, word: '\u3042\u3044\u3046\u3048\u304A', lang: 'ja', pron: '/a i u e o/', pos: 'hiragana', def: '\u4E94\u5341\u97F3\u7B2C\u4E00\u884C', ex: '\u4E94\u5341\u97F3\u306E\u57FA\u7840' },
  { unit: 11, word: '\u304B\u304D\u304F\u3051\u3053', lang: 'ja', pron: '/ka ki ku ke ko/', pos: 'hiragana', def: '\u304B\u884C\u6E05\u97F3', ex: '\u304B\u304D\u304F\u3051\u3053' },
  { unit: 12, word: '\u306F\u3072\u3075\u3078\u307B', lang: 'ja', pron: '/ha hi fu he ho/', pos: 'hiragana', def: '\u306F\u884C', ex: '\u3075\u306FFU\u3068\u767A\u97F3' },
  { unit: 13, word: '\u3053\u3093\u306B\u3061\u306F', lang: 'ja', pron: '/kon.nichi.wa/', pos: 'greeting', def: '\u767D\u5929\u95EE\u5019', ex: '\u3053\u3093\u306B\u3061\u306F\u7530\u4E2D\u3055\u3093' },
  { unit: 13, word: '\u3053\u3093\u3070\u3093\u306F', lang: 'ja', pron: '/kon.ban.wa/', pos: 'greeting', def: '\u665A\u4E0A\u95EE\u5019', ex: '\u3053\u3093\u3070\u3093\u306F' },
  { unit: 14, word: '\u3059\u307F\u307E\u305B\u3093', lang: 'ja', pron: '/su.mi.ma.sen/', pos: 'greeting', def: '\u5BF9\u4E0D\u8D77', ex: '\u3059\u307F\u307E\u305B\u3093\u9045\u308C\u307E\u3057\u305F' },
  { unit: 14, word: '\u521D\u3081\u307E\u3057\u3066', lang: 'ja', pron: '/hajimemashite/', pos: 'greeting', def: '\u521D\u6B21\u89C1\u9762', ex: '\u521D\u3081\u307E\u3057\u3066\u3001\u3069\u3046\u305E\u3088\u308D\u3057\u304F' },
  { unit: 15, word: '\u3042\u304B', lang: 'ja', pron: '/aka/', pos: 'adj.', def: '\u7EA2\u8272', ex: '\u3042\u304B\u3044\u30EA\u30F3\u3054' },
  { unit: 16, word: '\u3044\u3061', lang: 'ja', pron: '/it\u0955i/', pos: 'numeral', def: '\u6570\u5B571', ex: '\u3044\u3061\u304C\u3064\u4E00\u6708' },
  { unit: 17, word: '\u98DF\u3079\u308B', lang: 'ja', pron: '/ta.be.ru/', pos: 'verb', def: '\u5403', ex: '\u3054\u98EF\u3092\u98DF\u3079\u307E\u3059' },
  { unit: 18, word: '\u79C1', lang: 'ja', pron: '/watashi/', pos: 'pronoun', def: '\u6211', ex: '\u79C1\u306F\u5B66\u751F\u3067\u3059' },
  { unit: 19, word: '\u540D\u524D', lang: 'ja', pron: '/namae/', pos: 'n.', def: '\u540D\u5B57', ex: '\u304A\u540D\u524D\u306F\u4F55\u3067\u3059\u304B' },
  { unit: 21, word: 'a', lang: 'zh', pron: '/A/', pos: 'final', def: '\u6C49\u8BED\u62FC\u97F3\u7B2C\u4E00\u4E2A\u5355\u97F5\u6BCD', ex: '\u963F\u59E8(\u0101y\u00ED)' },
  { unit: 21, word: 'o', lang: 'zh', pron: '/o/', pos: 'final', def: '\u5355\u97F5\u6BCD\uFF0C\u5634\u5614\u5706\u5F62', ex: '\u54E6(\u00F2)' },
  { unit: 22, word: 'b', lang: 'zh', pron: '/p/', pos: 'initial', def: '\u4E0D\u9001\u6C14\u53CC\u5507\u6E05\u585E\u97F3', ex: '\u7238\u7238(b\u00E0ba)' },
  { unit: 22, word: 'p', lang: 'zh', pron: '/p\u02B0/', pos: 'initial', def: '\u9001\u6C14\u53CC\u5507\u6E05\u585E\u97F3', ex: '\u6015(p\u00E0)' },
  { unit: 23, word: 'zh', lang: 'zh', pron: '/\u0288\u0282s/', pos: 'initial', def: '\u7FD8\u820C\u97F3', ex: '\u4E2D\u56FD(Zh\u014Dnggu\u00F3)' },
  { unit: 24, word: 'ai', lang: 'zh', pron: '/a\u026A/', pos: 'compound final', def: '\u590D\u97F5\u6BCDai', ex: '\u7231\u597D(\u00E0ih\u01CEo)' },
  { unit: 25, word: 'an', lang: 'zh', pron: '/an/', pos: 'nasal final', def: '\u524D\u9F3B\u97F5\u6BCDan', ex: '\u5E73\u5B89(p\u00EDng \u0101n)' },
  { unit: 30, word: '\u4E00', lang: 'zh', pron: '/i\u0301/', pos: 'character', def: '\u6C49\u5B57\u4E00', ex: '\u4E00\u4E3E\u4E24\u5F97' },
  { unit: 31, word: '\u65E5', lang: 'zh', pron: '/\u027Bi\u0302/', pos: 'character', def: '\u592A\u9633\u65E5', ex: '\u65E5\u51FA\u4E1C\u65B9' },
  { unit: 32, word: '\u4EC5', lang: 'zh', pron: '/j\u01D0n/', pos: 'radical', def: '\u5355\u4EBA\u65C1', ex: '\u4ED6\u4EEC\u4F19\u4F34' },
  { unit: 33, word: '\u8BED', lang: 'zh', pron: '/y\u01D4\u030C/', pos: 'character', def: '\u8BED\u8A00\u8BED', ex: '\u6C49\u8BED\u8BED\u6587' }
];

sql += "-- Vocabulary\n";
sql += "DELETE FROM edu_vocabulary; ALTER TABLE edu_vocabulary AUTO_INCREMENT=1;\n";
sql += "INSERT INTO edu_vocabulary(unit_id,word,language,pronunciation,part_of_speech,definition,example_sentences,del_flag) VALUES\n";
sql += vocabData.map(v => `(${v.unit},'${esc(v.word)}','${v.lang}','${esc(v.pron)}','${esc(v.pos)}','${esc(v.def)}','${esc(v.ex)}','0')`).join(',\n');
sql += ';\n';

console.log('=== Full Rebuild Script ===');
console.log(`Courses: ${courses.length}`);
console.log(`Chapters: ${chapters.length}`);
console.log(`Units: ${units.length}`);
console.log(`Lessons: ${lessons.length}`);
console.log(`Vocab: ${vocabData.length}`);
console.log(`SQL length: ${sql.length} bytes`);

try {
  const result = execSync(
    'mysql -u root -proot --default-character-set=utf8mb4 ryvue',
    { input: sql, encoding: 'utf8', timeout: 60000 }
  );
  console.log('\n=== SUCCESS! ===');
  console.log(result.toString().trim().substring(0, 300));
} catch (e) {
  console.log('\n=== ERROR ===');
  console.log('STDERR:', e.stderr?.toString()?.substring(0, 800));
  console.log('STDOUT:', e.stdout?.toString()?.substring(0, 300));
}