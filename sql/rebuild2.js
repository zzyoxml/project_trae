const fs = require('fs');
const { execSync } = require('child_process');

const ddlPath = 'c:\\Users\\xml\\Documents\\Trae\\project_demo1\\RuoYi-Vue-master\\RuoYi-Vue-master\\sql\\edu_tables.sql';
let ddl = fs.readFileSync(ddlPath, 'utf8');

function esc(s) {
  if (s === null || s === undefined) return '';
  return String(s).replace(/\\/g, '\\\\').replace(/'/g, "\\'");
}

let sql = '';
sql += 'SET NAMES utf8mb4;\n';
sql += 'SET CHARACTER SET utf8mb4;\n';
sql += 'DROP TABLE IF EXISTS edu_vocabulary;\n';
sql += 'DROP TABLE IF EXISTS edu_course_lesson;\n';
sql += 'DROP TABLE IF EXISTS edu_course_unit;\n';
sql += 'DROP TABLE IF EXISTS edu_course_chapter;\n';
sql += 'DROP TABLE IF EXISTS edu_course;\n';
sql += ddl + '\n';

sql += `CREATE TABLE IF NOT EXISTS edu_vocabulary (
  vocab_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '词汇ID',
  unit_id bigint(20) DEFAULT NULL COMMENT '单元ID',
  word varchar(100) NOT NULL COMMENT '单词',
  language varchar(10) NOT NULL COMMENT '语言',
  pronunciation varchar(200) DEFAULT NULL COMMENT '发音/音标',
  audio_url varchar(255) DEFAULT NULL COMMENT '音频URL',
  part_of_speech varchar(20) DEFAULT NULL COMMENT '词性',
  level varchar(20) DEFAULT 'beginner' COMMENT '等级',
  definition text COMMENT '释义',
  example_sentences text COMMENT '例句',
  create_by varchar(64) DEFAULT '' COMMENT '创建者',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  del_flag char(1) DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (vocab_id),
  KEY idx_unit_id (unit_id),
  KEY idx_vocab_language (language)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='词汇表';
`;

const courses = [
  { id:1, name:'英语入门基础', code:'EN-BASIC', lang:'en', level:'beginner', type:'general',
    desc:'零基础英语学习，从字母、发音到日常会话的全面入门课程',
    obj:'掌握26个字母发音、100个基础单词、20个日常句型', tags:'英语零基础,字母,发音,入门', diff:1 },
  { id:2, name:'英语日常会话', code:'EN-DAILY', lang:'en', level:'elementary', type:'conversation',
    desc:'日常生活场景中的实用英语对话，涵盖购物、问路、餐厅等高频场景',
    obj:'能进行基本的日常英语交流，听懂简单对话', tags:'日常对话,生活英语,实用口语', diff:2 },
  { id:3, name:'英语社交礼仪', code:'EN-SOCIAL', lang:'en', level:'intermediate', type:'conversation',
    desc:'西方社交礼仪与地道表达，学会得体地打招呼、道歉、感谢、邀请等',
    obj:'掌握英语国家社交礼仪，表达自然得体', tags:'社交礼仪,文化,地道表达', diff:3 },
  { id:4, name:'英语医疗场景', code:'EN-MEDICAL', lang:'en', level:'intermediate', type:'conversation',
    desc:'医院就诊、描述症状、购买药品等医疗场景的英语表达',
    obj:'能用英语在医院就诊并准确描述身体状况', tags:'医疗英语,看病,健康', diff:3 },
  { id:5, name:'英语职场沟通', code:'EN-WORK', lang:'en', level:'intermediate', type:'conversation',
    desc:'会议、报告、邮件、deadline等职场场景的专业英语表达',
    obj:'能在职场环境中用英语进行基本沟通', tags:'职场英语,商务,会议', diff:3 },
  { id:6, name:'日语五十音与基础会话', code:'JA-BASIC', lang:'ja', level:'beginner', type:'general',
    desc:'从日语五十音图（平假名/片假名）开始，到基础问候语和日常会话',
    obj:'熟练掌握五十音图，能用日语进行简单自我介绍和问候', tags:'日语入门,五十音,假名', diff:1 }
];

const chapterMap = {
  1:[['字母与发音','26个英文字母的标准发音与书写'],['日常问候','Hello, Good morning等基础问候语'],['自我介绍','名字、国籍、职业的自我介绍'],['家庭成员','father, mother等家庭成员词汇'],['外貌特征','tall, beautiful等外形描述词'],['日常作息','wake up, breakfast等日常活动词汇'],['社交礼仪','sorry, excuse me等礼貌用语'],['情感表达','happy, sad等情感表达'],['庆祝活动','birthday, party等庆祝活动词汇'],['医疗场景','hospital, doctor等医疗相关'],['职场沟通','meeting, report等职场词汇']],
  2:[['见面问候','初次见面的标准寒暄'],['介绍他人','如何向他人介绍你的朋友家人'],['家庭话题','谈论家人的基本信息'],['描述人物','描述一个人的外貌性格'],['一天作息','从早到晚的日常活动'],['礼貌用语','道歉、感谢、请求帮助'],['表达情感','喜怒哀乐的正确表达方式'],['节日聚会','生日派对、节日聚会场景'],['看病就医','去医院看病的完整流程'],['工作场合','办公室里的常用表达']],
  3:[['打招呼与告别','不同时段的问候方式'],['感谢与道歉','如何得体地表示感谢和歉意'],['表达情绪','各种情绪的地道表达'],['邀请与回应','发出邀请和接受/拒绝邀请'],['赞美与祝贺','赞美他人和祝贺特殊时刻'],['请求与拒绝','提出请求和委婉拒绝']],
  4:[['描述症状','头痛发烧等各种症状描述'],['看医生流程','挂号、问诊、检查全流程'],['买药与用药','药房买药和服药说明'],['急诊情况','紧急情况的应对方法'],['健康建议','医生给出的健康建议']],
  5:[['办公室日常','办公室日常交流'],['开会讨论','开会时的发言和讨论'],['邮件写作','商务邮件的写作规范'],['电话沟通','电话接打技巧'],['汇报工作','向上级汇报工作进展']],
  6:[['平假名（あ行-な行）','日语前半部分平假名'],['平假名（は行-わ行）','后半部分平假名'],['片假名与浊音','片假名及拗音促音'],['基础问候语','こんにちは等基础问候'],['日常会话','日常对话句型'],['颜色与数字','颜色名称和数字读法'],['基础语法','はです等基础语法'],['汉字书写','基础汉字和偏旁部首']]
};

const unitMap = {
  '字母与发音':[['字母A-E','a,b,c,d,e的发音与例词'],['字母F-J','f,g,h,i,j的发音与例词'],['字母K-O','k,l,m,n,o的发音与例词'],['字母P-T','p,q,r,s,t的发音与例词'],['字母U-Z','u,v,w,x,y,z的发音与例词']],
  '日常问候':[['Hello与Goodbye','Hello! Nice to meet you. Goodbye! See you tomorrow.'],['Good Morning等问候','Good morning, Good afternoon, Good evening'],['How are you?','询问近况的回答方式']],
  '自我介绍':[['Name与Come from','What is your name? Where do you come from?'],['Student与Engineer','职业相关的自我介绍词汇']],
  '家庭成员':[['Father与Mother','家庭成员核心词汇'],['Brother与Sister','兄弟姐妹及相关表达'],['Family','关于家庭的综合表达']],
  '外貌特征':[['Tall与Beautiful','形容人的外观词汇'],['Friendly与Young','性格与年龄相关描述词']],
  '日常作息': [['Wake Up与Breakfast','早晨起床和早餐相关'],['Usually与Every Day','频率副词的使用']],
  '社交礼仪': [['Sorry与Excuse Me','道歉和请求注意的表达'],['Apologize与Welcome','更正式的礼貌用语']],
  '情感表达式':[['Happy与Sad','基本情绪词汇'],['Angry与Excited','强烈情绪的表达']],
  '庆祝活动': [['Celebration与Invite','庆祝活动和邀请'],['Amazing与Party','派对相关词汇']],
  '医疗场景': [['Hospital与Doctor','医院和医生相关'],['Fever与Medicine','症状和药物词汇'],['Ambulance与Prescription','急救和处方']],
  '职场沟通': [['Deadline与Meeting','截止日期和会议'],['Report与Project','工作报告和项目']],
  '平假名（あ行-な行）':[['あいうえお','第一行元音'],['かきくけこ','か行清音'],['さしすせそ','さ行（し=shi）'],['たちつてと','た行（ち=chi,つ=tsu）'],['なにぬねの','な行鼻音']],
  '平假名（は行-わ行）':[['はひふへほ','は行（ふ=fu）'],['まみむめも','ま行鼻音'],['やゆよ','や行三字'],['らりるれろ','ら行弹音'],['わをん','わ行和拨音ん']],
  '片假名与浊音':[['アイウエオ','片假名对应'],['ガギグゲゴ','浊音'],['促音与长音','っ和ー的使用'],['容易混淆的假名','ぬvsの, ねvsれ']],
  '基础问候语': [['こんにちは/こんばんは','白天和晚上的问候'],['おはようございます/さようなら','早上好和再见'],['ありがとうございます/すみません','感谢和道歉'],['初めまして/お疲れ様です','初次见面和同事问候'],['いただきます/ごちそうさまでした','饭前饭后用语']],
  '日常会话': [['颜色词汇','あか/あお/しろ/くろ'],['数字1-10','いち/に/さん...'],['人称代词','私/あなた/彼/彼女'],['基本动词','食べる/飲む/行く/来る'],['常用名词','名前/国/言葉/時間']],
  '颜色与数字': [['あいうえお复习','五个元音'],['bpmf声母','双唇音和唇齿音'],['dtnl声母','舌尖中音'],['gkh声母','舌根音']],
  '基础语法': [['jqx声母','舌面音'],['zhchshr声母','翘舌音'],['zcs声母','平舌音'],['ai/ei/ao/ou复韵母','复韵母']],
  '汉字书写': [['an/en/ang/eng鼻韵母','前后鼻音'],['横竖撇捺','基本笔画'],['日月山水','基础汉字'],['亻艹氵钅','常用偏旁'],['语思学好','常用字词']]
};

const chapters = [];
const units = [];
let chId = 1, uId = 1;

for (const c of courses) {
  const chs = chapterMap[c.id] || [];
  for (const [chName, chDesc] of chs) {
    chapters.push({ id: chId++, courseId: c.id, name: chName, desc: chDesc });
    const utemplates = unitMap[chName] || [[`${chName}-单元1`, `${chName}的第一单元内容`]];
    for (const [uname, udesc] of utemplates) {
      units.push({ id: uId++, courseId: c.id, chapterId: chId - 1, name: uname, desc: udesc });
    }
  }
}

const lessonTypes = ['vocabulary', 'grammar', 'listening', 'speaking'];
const typeLabels = { vocabulary: '单词学习', grammar: '语法讲解', listening: '听力练习', speaking: '口语训练' };
const lessons = [];
let lId = 1;
for (const u of units) {
  for (let t = 0; t < lessonTypes.length; t++) {
    lessons.push({ id: lId++, unitId: u.id, name: `${u.name}-${typeLabels[lessonTypes[t]]}`, type: lessonTypes[t], duration: 15 });
  }
}

sql += '-- Courses\n';
sql += "INSERT INTO edu_course (course_id,course_name,course_code,language,level,course_type,description,learning_objectives,tags,difficulty_score,is_free,is_published,del_flag) VALUES\n";
sql += courses.map(c => `(${c.id},'${esc(c.name)}','${esc(c.code)}','${c.lang}','${c.level}','${c.type}','${esc(c.desc)}','${esc(c.obj)}','${esc(c.tags)}',${c.diff},'1','1','0')`).join(',\n');
sql += ';\n\n';

sql += '-- Chapters\n';
sql += "INSERT INTO edu_course_chapter (chapter_id,course_id,chapter_name,chapter_order,description,del_flag) VALUES\n";
sql += chapters.map((ch, i) => `(${ch.id},${ch.courseId},'${esc(ch.name)}',${i % 15 + 1},'${esc(ch.desc)}','0')`).join(',\n');
sql += ';\n\n';

sql += '-- Units\n';
sql += "INSERT INTO edu_course_unit (unit_id,course_id,chapter_id,unit_name,unit_order,description,del_flag) VALUES\n";
sql += units.map((u, i) => `(${u.id},${u.courseId},${u.chapterId},'${esc(u.name)}',${i + 1},'${esc(u.desc)}','0')`).join(',\n');
sql += ';\n\n';

sql += '-- Lessons\n';
sql += "INSERT INTO edu_course_lesson (lesson_id,unit_id,lesson_name,lesson_order,lesson_type,duration,del_flag) VALUES\n";
sql += lessons.map((l, i) => `(${l.id},${l.unitId},'${esc(l.name)}',${i % 4 + 1},'${l.type}',${l.duration},'0')`).join(',\n');
sql += ';\n\n';

const vocabs = [
  [1,'hello','en','/həˈloʊ/','interj.','你好；喂',"Hello! Nice to meet you.\nHello, may I speak to Mr. Smith?"],
  [1,'good morning','en','/ɡʊd ˈmɔːrnɪŋ/','phrase','早上好',"Good morning, class!\nGood morning! Did you sleep well?"],
  [1,'how are you','en','/haʊ ɑːr juː/','phrase','你好吗？',"How are you doing today?\nHow are you? I am fine, thank you."],
  [1,'nice to meet you','en','/naɪs tuː miːt juː/','phrase','很高兴认识你',"Nice to meet you, Sarah.\nIt is nice to meet you too!"],
  [1,'goodbye','en','/ɡʊdˈbaɪ/','interj.','再见；告别',"Goodbye! See you tomorrow.\nGoodbye and take care!"],
  [1,'thank you','en','/θæŋk juː/','phrase','谢谢你；感谢',"Thank you for your help.\nThank you very much!"],
  [2,'name','en','/neɪm/','n.','名字；名称',"What is your name?\nMy name is Alice."],
  [2,'come from','en','/kʌm frɒm/','phr. v.','来自；出生于',"Where do you come from?\nI come from Shanghai."],
  [2,'student','en','/ˈstuːdnt/','n.','学生',"She is a university student.\nHe is an exchange student from Japan."],
  [2,'engineer','en','/ˌendʒɪˈnɪr/','n.','工程师',"My father works as an engineer.\nShe is a software engineer at Google."],
  [3,'father','en','/ˈfɑːðər/','n.','父亲；爸爸',"My father is a doctor.\nI look like my father."],
  [3,'mother','en','/ˈmʌðər/','n.','母亲；妈妈',"My mother cooks delicious food.\nHappy Mother's Day!"],
  [3,'brother','en','/ˈbrʌðər/','n.','兄弟；弟兄',"I have one older brother.\nMy brother and I play soccer together."],
  [3,'sister','en','/ˈsɪstər/','n.','姐妹；姐妹',"My younger sister is very cute.\nShe is like a sister to me."],
  [3,'family','en','/ˈfæmɪli/','n.','家庭；家人',"I love my family very much.\nWe had a family dinner last night."],
  [4,'tall','en','/tɔːl/','adj.','高的',"He is very tall, about six feet.\nThe building is tall and modern."],
  [4,'beautiful','en','/ˈbjuːtɪfl/','adj.','美丽的；漂亮的',"The view from here is beautiful.\nShe looks beautiful in that dress."],
  [4,'friendly','en','/ˈfrendli/','adj.','友好的；亲切的',"The people here are very friendly.\nHe has a friendly smile."],
  [4,'young','en','/jʌŋ/','adj.','年轻的；年少的',"She is young but very talented.\nYoung people love this music."],
  [5,'wake up','en','/weɪk ʌp/','phr. v.','醒来；醒过来',"I wake up at seven every day.\nPlease wake me up at six tomorrow."],
  [5,'breakfast','en','/ˈbrekfəst/','n.','早餐；早饭',"I always have breakfast at home.\nBreakfast is the most important meal of the day."],
  [5,'usually','en','/ˈjuːʒuəli/','adv.','通常；一般地',"I usually go to bed at eleven.\nShe usually takes the bus to work."],
  [6,'sorry','en','/ˈsɒri/','interj./adj.','对不起；抱歉的',"Sorry, I am late.\nI am sorry for the mistake."],
  [6,'excuse me','en','/ɪkˈskjuːz miː/','phrase','打扰一下；借过',"Excuse me, where is the station?\nExcuse me, can I ask a question?"],
  [6,'apologize','en','/əˈpɒlədʒaɪz/','v.','道歉；谢罪',"I sincerely apologize for the mistake.\nYou should apologize to her."],
  [6,'welcome','en','/ˈwelkəm/','adj./v./interj.','受欢迎的；欢迎；不客气',"You are always welcome here.\n- Thank you. - You are welcome."],
  [7,'happy','en','/ˈhæpi/','adj.','高兴的；快乐的',"I am so happy today!\nHappy birthday to you!"],
  [7,'sad','en','/sæd/','adj.','悲伤的；难过的',"She looks sad today.\nI feel sad when it rains."],
  [7,'angry','en','/ˈæŋɡri/','adj.','生气的；愤怒的',"Don't be angry with me.\nHe was angry about the news."],
  [7,'excited','en','/ɪkˈsaɪtɪd/','adj.','兴奋的；激动的',"I am excited about the trip.\nShe was excited to hear the good news."],
  [8,'celebration','en','/ˌselɪˈbreɪʃn/','n.','庆祝；庆典',"We had a big celebration for New Year.\nThe party was a great celebration."],
  [8,'invite','en','/ɪnˈvaɪt/','v.','邀请；招待',"She invited me to her birthday party.\nThanks for inviting me."],
  [8,'amazing','en','/əˈmeɪzɪŋ/','adj.','令人惊叹的；了不起的',"The view from the top is amazing.\nThat is an amazing idea!"],
  [9,'hospital','en','/ˈhɒspɪtl/','n.','医院',"He went to the hospital yesterday."],
  [9,'doctor','en','/ˈdɒktər/','n.','医生；博士',"I need to see a doctor."],
  [9,'fever','en','/ˈfiːvər/','n.','发烧；发热',"I have a high fever."],
  [10,'deadline','en','/ˈdedlaɪn/','n.','截止日期；最后期限',"The deadline for the project is Friday."],
  [10,'meeting','en','/ˈmiːtɪŋ/','n.','会议；会面',"We have a meeting at 3 PM."],
  [11,'あいうえお','ja','/a i u e o/','hiragana','五十音第一行',"あいうえおは日本語の基礎です。"],
  [11,'かきくけこ','ja','/ka ki ku ke ko/','hiragana','か行清音',"かきくけこは濁点でがぎぐげごになります。"],
  [12,'はひふへほ','ja','/ha hi fu he ho/','hiragana','は行',"ふは「fu」と発音します。"],
  [13,'こんにちは','ja','/kon.nichi.wa/','greeting','白天问候',"こんにちは、田中さん。"],
  [13,'ありがとう','ja','/a.ri.ga.tou/','greeting','谢谢',"ありがとうございます。"],
  [14,'すみません','ja','/su.mi.ma.sen/','greeting','对不起',"すみません、遅れました。"],
  [14,'初めまして','ja','/hajimemashite/','greeting','初次见面',"初めまして、田中と申します。"],
  [15,'あか','ja','/aka/','adj.','红色',"あかいリンゴ（红苹果）"],
  [16,'いち','ja','/itɕi/','numeral','数字1',"いちがつ（一月）"],
  [17,'食べる','ja','/ta.be.ru/','verb','吃',"ご飯を食べます。"],
  [18,'私','ja','/watashi/','pronoun','我',"私は学生です。"],
  [19,'名前','ja','/namae/','n.','名字',"お名前は何ですか。"],
  [21,'a','zh','/A/','final','韵母a',"阿姨(āyí)\n阿婆(āpó)"],
  [22,'b','zh','/p/','initial','声母b',"爸爸(bàba)\n杯子(bēizi)"],
  [23,'zh','zh','/ʈʂ/','initial','翘舌zh',"中国(Zhōngguó)\n知道(zhīdao)"],
  [24,'ai','zh','/aɪ/','复韵母','ai',"爱好(àihào)\n大海(dà hǎi)"],
  [25,'an','zh','/an/','前鼻韵母','an',"平安(píng ān)\n吃饭(chī fàn)"],
  [30,'一','zh','/í/','numeral','汉字一',"一举两得\n一心一意"],
  [31,'日','zh','/ɻî/','n.','太阳日',"日出东方\n日日夜夜"],
  [32,'亻','zh','/rénzìpáng/','radical','单人旁',"他们\n伙伴"],
  [33,'语','zh','/yǔ̌/','n./v.','语言语',"汉语\n语文"]
];

sql += '-- Vocabulary\n';
sql += "DELETE FROM edu_vocabulary; ALTER TABLE edu_vocabulary AUTO_INCREMENT=1;\n";
sql += "INSERT INTO edu_vocabulary(unit_id,word,language,pronunciation,part_of_speech,definition,example_sentences,del_flag) VALUES\n";
sql += vocabs.map(v => `(${v[0]},'${esc(v[1])}','${v[2]}','${esc(v[3])}','${esc(v[4])}','${esc(v[5])}','${esc(v[6])}','0')`).join(',\n');
sql += ';\n';

try {
  console.log('Rebuilding all tables with UTF-8 data...');
  console.log(`Courses:${courses.length} Ch:${chapters.length} U:${units.length} L:${lessons.length} V:${vocabs.length}`);
  const result = execSync('mysql -u root -proot --default-character-set=utf8mb4 ryvue', { input: sql, encoding: 'utf8', timeout: 60000 });
  console.log('\nSUCCESS!');
} catch(e) {
  console.log('ERROR:', e.stderr?.toString()?.substring(0, 800));
  console.log('STDOUT:', e.stdout?.toString()?.substring(0, 300));
}