const fs = require('fs');
const { execSync } = require('child_process');

const ddlPath = 'c:\\Users\\xml\\Documents\\Trae\\project_demo1\\RuoYi-Vue-master\\RuoYi-Vue-master\\sql\\edu_tables.sql';
let ddl = fs.readFileSync(ddlPath, 'utf8');

function esc(s) {
  return s.replace(/\\/g, '\\\\').replace(/'/g, "\\'");
}

let sql = '';

sql += `SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS edu_vocabulary;
DROP TABLE IF EXISTS edu_course_lesson;
DROP TABLE IF EXISTS edu_course_unit;
DROP TABLE IF EXISTS edu_course_chapter;
DROP TABLE IF EXISTS edu_course;

${ddl}

`;

const courses = [
  [1, '英语入门基础', 'EN-BASIC', null, 'en', 'beginner', 'general', '零基础英语学习，从字母、发音到日常会话的全面入门课程', '', '', null, 0, 0, 0, 0, 0, '1', 0, '1', '0', null, '掌握26个字母发音、100个基础单词、20个日常句型', '英语零基础,字母,发音,入门', 1, 0],
  [2, '英语日常会话', 'EN-DAILY', null, 'en', 'elementary', 'conversation', '日常生活场景中的实用英语对话，涵盖购物、问路、餐厅等高频场景', '', '', null, 0, 0, 0, 0, 0, '1', 0, '1', '0', null, '能进行基本的日常英语交流，听懂简单对话', '日常对话,生活英语,实用口语', 2, 0],
  [3, '英语社交礼仪', 'EN-SOCIAL', null, 'en', 'intermediate', 'conversation', '西方社交礼仪与地道表达，学会得体地打招呼、道歉、感谢、邀请等', '', '', null, 0, 0, 0, 0, 0, '1', 0, '1', '0', null, '掌握英语国家社交礼仪，表达自然得体', '社交礼仪,文化,地道表达', 3, 0],
  [4, '英语医疗场景', 'EN-MEDICAL', null, 'en', 'intermediate', 'conversation', '医院就诊、描述症状、购买药品等医疗场景的英语表达', '', '', null, 0, 0, 0, 0, 0, '1', 0, '1', '0', null, '能用英语在医院就诊并准确描述身体状况', '医疗英语,看病,健康', 3, 0],
  [5, '英语职场沟通', 'EN-WORK', null, 'en', 'intermediate', 'conversation', '会议、报告、邮件、deadline等职场场景的专业英语表达', '', '', null, 0, 0, 0, 0, 0, '1', 0, '1', '0', null, '能在职场环境中用英语进行基本沟通', '职场英语,商务,会议', 3, 0],
  [6, '日语五十音与基础会话', 'JA-BASIC', null, 'ja', 'beginner', 'general', '从日语五十音图（平假名/片假名）开始，到基础问候语和日常会话', '', '', null, 0, 0, 0, 0, 0, '1', 0, '1', '0', null, '熟练掌握五十音图，能用日语进行简单自我介绍和问候', '日语入门,五十音,假名', 1, 0]
];

const chapters = [];
let chId = 1;
for (const c of courses) {
  const cid = c[0];
  const names = {
    1: ['字母与发音', '日常问候', '自我介绍', '家庭成员', '外貌特征', '日常作息', '社交礼仪', '情感表达', '庆祝活动', '医疗场景', '职场沟通', '数字与时间', '方向与位置', '食物与饮料', '交通出行'],
    2: ['见面问候', '介绍他人', '家庭话题', '描述人物', '一天作息', '礼貌用语', '表达情感', '节日聚会', '看病就医', '工作场合'],
    3: ['打招呼与告别', '感谢与道歉', '表达情绪', '邀请与回应', '赞美与祝贺', '请求与拒绝'],
    4: ['描述症状', '看医生流程', '买药与用药', '急诊情况', '健康建议'],
    5: ['办公室日常', '开会讨论', '邮件写作', '电话沟通', '汇报工作'],
    6: ['平假名（あ行-な行）', '平假名（は行-わ行）', '片假名与浊音', '基础问候语', '日常会话', '颜色与数字', '基础语法', '汉字书写']
  };
  const descs = {
    1: ['26个英文字母的标准发音与书写', 'Hello, Good morning等基础问候语', '名字、国籍、职业的自我介绍', 'father, mother等家庭成员词汇', 'tall, beautiful等外形描述词', 'wake up, breakfast等日常活动词汇', 'sorry, excuse me等礼貌用语', 'happy, sad等情感表达', 'birthday, party等庆祝活动词汇', 'hospital, doctor等医疗相关', 'meeting, report等职场词汇', '数字1-100和时间表达', '方位介词和地点询问', '常见食物和饮品名称', '交通工具和出行方式'],
    2: ['初次见面的标准寒暄', '如何向他人介绍你的朋友家人', '谈论家人的基本信息', '描述一个人的外貌性格', '从早到晚的日常活动', '道歉、感谢、请求帮助', '喜怒哀乐的正确表达方式', '生日派对、节日聚会场景', '去医院看病的完整流程', '办公室里的常用表达'],
    3: ['不同时段的问候方式', '如何得体地表示感谢和歉意', '各种情绪的地道表达', '发出邀请和接受/拒绝邀请', '赞美他人和祝贺特殊时刻', '提出请求和委婉拒绝'],
    4: ['头痛发烧等各种症状描述', '挂号、问诊、检查全流程', '药房买药和服药说明', '紧急情况的应对方法', '医生给出的健康建议'],
    5: ['办公室日常交流', '开会时的发言和讨论', '商务邮件的写作规范', '电话接打技巧', '向上级汇报工作进展'],
    6: ['日语前半部分平假名', '后半部分平假名', '片假名及拗音促音', 'こんにちは等基础问候', '日常对话句型', '颜色名称和数字读法', 'はです等基础语法', '基础汉字和偏旁部首']
  };
  const chapterNames = names[cid] || names[1];
  const chapterDescs = descs[cid] || descs[1];
  for (let i = 0; i < chapterNames.length; i++) {
    chapters.push([chId++, cid, chapterNames[i], i + 1, chapterDescs[i] || '']);
  }
}

const units = [];
let uId = 1;
for (const ch of chapters) {
  const chId = ch[0];
  const courseId = ch[1];
  const chName = ch[2];

  const unitTemplates = {
    '字母与发音': [['字母A-E', 'a,b,c,d,e的发音与例词'], ['字母F-J', 'f,g,h,i,j的发音与例词'], ['字母K-O', 'k,l,m,n,o的发音与例词'], ['字母P-T', 'p,q,r,s,t的发音与例词'], ['字母U-Z', 'u,v,w,x,y,z的发音与例词']],
    '日常问候': [['Hello与Goodbye', 'Hello! Nice to meet you.\nGoodbye! See you tomorrow.'], ['Good Morning等问候', 'Good morning, Good afternoon, Good evening'], ['How are you?', '询问近况的回答方式']],
    '自我介绍': [['Name与Come from', 'What is your name?\nWhere do you come from?'], ['Student与Engineer', '职业相关的自我介绍词汇']],
    '家庭成员': [['Father与Mother', '家庭成员核心词汇'], ['Brother与Sister', '兄弟姐妹及相关表达'], ['Family', '关于家庭的综合表达']],
    '外貌特征': [['Tall与Beautiful', '形容人的外观词汇'], ['Friendly与Young', '性格与年龄相关描述词']],
    '日常作息': [['Wake Up与Breakfast', '早晨起床和早餐相关'], ['Usually与Every Day', '频率副词的使用']],
    '社交礼仪': [['Sorry与Excuse Me', '道歉和请求注意的表达'], ['Apologize与Welcome', '更正式的礼貌用语']],
    '情感表达': [['Happy与Sad', '基本情绪词汇'], ['Angry与Excited', '强烈情绪的表达']],
    '庆祝活动': [['Celebration与Invite', '庆祝活动和邀请'], ['Amazing与Party', '派对相关词汇']],
    '医疗场景': [['Hospital与Doctor', '医院和医生相关'], ['Fever与Medicine', '症状和药物词汇'], ['Ambulance与Prescription', '急救和处方']],
    '职场沟通': [['Deadline与Meeting', '截止日期和会议'], ['Report与Project', '工作报告和项目']],
    '平假名（あ行-な行）': [['あいうえお', '第一行元音'], ['かきくけこ', 'か行清音'], ['さしすせそ', 'さ行（し=shi）'], ['たちつてと', 'た行（ち=chi,つ=tsu）'], ['なにぬねの', 'な行鼻音']],
    '平假名（は行-わ行）': [['はひふへほ', 'は行（ふ=fu）'], ['まみむめも', 'ま行鼻音'], ['やゆよ', 'や行三字'], ['らりるれろ', 'ら行弹音'], ['わをん', 'わ行和拨音ん']],
    '片假名与浊音': [['アイウエオ', '片假名对应'], ['ガギグゲゴ', '浊音'], ['促音与长音', 'っ和ー的使用'], ['容易混淆的假名', 'ぬvsの, ねvsれ']],
    '基础问候语': [['こんにちは/こんばんは', '白天和晚上的问候'], ['おはようございます/さようなら', '早上好和再见'], ['ありがとうございます/すみません', '感谢和道歉'], ['初めまして/お疲れ様です', '初次见面和同事问候'], ['いただきます/ごちそうさまでした', '饭前饭后用语']],
    '日常会话': [['颜色词汇', 'あか/あお/しろ/くろ'], ['数字1-10', 'いち/に/さん...'], ['人称代词', '私/あなた/彼/彼女'], ['基本动词', '食べる/飲む/行く/来る'], ['常用名词', '名前/国/言葉/時間']],
    '颜色与数字': [['あいうえお复习', '五个元音'], ['bpmf声母', '双唇音和唇齿音'], ['dtnl声母', '舌尖中音'], ['gkh声母', '舌根音']],
    '基础语法': [['jqx声母', '舌面音'], ['zhchshr声母', '翘舌音'], ['zcs声母', '平舌音'], ['ai/ei/ao/ou复韵母', '复韵母']],
    '汉字书写': [['an/en/ang/eng鼻韵母', '前后鼻音'], ['横竖撇捺', '基本笔画'], ['日月山水', '基础汉字'], ['亻艹氵钅', '常用偏旁'], ['语思学好', '常用字词']]
  };

  const templates = unitTemplates[chName] || [[`${chName}-单元1`, `${chName}的第一单元内容`], [`${chName}-单元2`, `${chName}的第二单元内容`]];
  for (const [uname, udesc] of templates) {
    units.push([uId++, courseId, chId, uname, units.length + 1, udesc]);
  }
}

const lessonTypes = ['vocabulary', 'grammar', 'listening', 'speaking'];
const lessons = [];
let lId = 1;
for (const u of units) {
  const uid = u[0];
  const uName = u[3];
  for (let t = 0; t < lessonTypes.length; t++) {
    const type = lessonTypes[t];
    const typeLabels = { vocabulary: '单词学习', grammar: '语法讲解', listening: '听力练习', speaking: '口语训练' };
    lessons.push([lId++, uid, `${uName}-${typeLabels[type]}`, t + 1, type, '', 15]);
  }
}

sql += `-- ==================== 课程数据 ====================\n`;
sql += `INSERT INTO edu_course (course_id, course_name, course_code, category_id, language, level, course_type, description, cover_image, trailer_video, teacher_id, total_duration, total_lessons, total_students, rating, rating_count, is_free, price, is_published, is_featured, prerequisites, learning_objectives, tags, difficulty_score, popularity_score, del_flag) VALUES\n`;
sql += courses.map(c => {
  const [id, name, code, catId, lang, level, type, desc, cover, video, teacher, dur, lessons, students, rating, ratCnt, free, price, pub, feat, prereq, obj, tags, diff, pop, del] = c;
  return `(${id}, '${esc(name)}', '${esc(code)}', ${catId || 'NULL'}, '${lang}', '${level}', '${type}', '${esc(desc)}', '${esc(cover)}', '${esc(video)}', ${teacher || 'NULL'}, ${dur}, ${lessons}, ${students}, ${rating}, ${ratCnt}, '${free}', ${price}, '${pub}', '${feat}', ${prereq ? `'${esc(prereq)}'` : 'NULL'}, '${esc(obj)}', '${esc(tags)}', ${diff}, ${pop}, '${del}')`;
}).join(',\n');
sql += ';\n\n';

sql += `-- ==================== 章节数据 ====================\n`;
sql += `INSERT INTO edu_course_chapter (chapter_id, course_id, chapter_name, chapter_order, description, del_flag) VALUES\n`;
sql += chapters.map(c => `(${c[0]}, ${c[1]}, '${esc(c[2])}', ${c[3]}, '${esc(c[4])}', '0')`).join(',\n');
sql += ';\n\n';

sql += `-- ==================== 单元数据 ====================\n`;
sql += `INSERT INTO edu_course_unit (unit_id, course_id, chapter_id, unit_name, unit_order, description, del_flag) VALUES\n`;
sql += units.map(u => `(${u[0]}, ${u[1]}, ${u[2] || 'NULL'}, '${esc(u[3])}', ${u[4]}, '${esc(u[5])}', '0')`).join(',\n');
sql += ';\n\n';

sql += `-- ==================== 课时数据 ====================\n`;
sql += `INSERT INTO edu_course_lesson (lesson_id, unit_id, lesson_name, lesson_order, lesson_type, content, duration, del_flag) VALUES\n`;
sql += lessons.map(l => `(${l[0]}, ${l[1]}, '${esc(l[2])}', ${l[3]}, '${l[4]}', '', ${l[5]}, '0')`).join(',\n');
sql += ';\n\n';

const vocabData = [
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
  [5,'every day','en','/ˈevri deɪ/','adv.','每天；天天',"I exercise every day.\nHe reads English every morning."],
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
  [8,'party','en','/ˈpɑːrti/','n.','聚会；派对',"Are you coming to the party tonight?\nWe had a great party last weekend."],
  [9,'hospital','en','/ˈhɒspɪtl/','n.','医院',"He went to the hospital yesterday.\nThe hospital is near my home."],
  [9,'doctor','en','/ˈdɒktər/','n.','医生；博士',"I need to see a doctor.\nThe doctor said I should rest more."],
  [9,'fever','en','/ˈfiːvər/','n.','发烧；发热',"I have a high fever.\nShe has a fever of 39 degrees."],
  [9,'medicine','en','/ˈmedsn/','n.','药；医学',"Take this medicine three times a day.\nShe studies medicine at university."],
  [9,'ambulance','en','/ˈæmbjələns/','n.','救护车',"Call an ambulance immediately!\nThe ambulance arrived within five minutes."],
  [9,'prescription','en','/prɪˈskrɪpʃn/','n.','处方；药方',"The doctor wrote a prescription for me.\nYou need a prescription to buy this medicine."],
  [10,'deadline','en','/ˈdedlaɪn/','n.','截止日期；最后期限',"The deadline for the project is Friday.\nWe need to meet the deadline."],
  [10,'meeting','en','/ˈmiːtɪŋ/','n.','会议；会面',"We have a meeting at 3 PM.\nThe meeting lasted two hours."],
  [10,'report','en','/rɪˈpɔːrt/','n./v.','报告；汇报',"Please submit your report by Monday.\nHe reported the incident to the police."],
  [10,'project','en','/ˈprɒdʒekt/','n.','项目；工程',"We are working on a new project.\nThe project will take three months."],

  [11,'あいうえお','ja','/a i u e o/','hiragana','日语五十音图第一行，基础元音',"あいうえおは日本語の基礎です。\n五十音図の最初の行です。"],
  [11,'かきくけこ','ja','/ka ki ku ke ko/','hiragana','か行假名，清音',"かきくけこは濁点でがぎぐげごになります。\n日本語の基本文字です。"],
  [11,'さしすせそ','ja','/sa shi su se so/','hiragana','さ行假名，し读作shi',"さしすせそ、注意：しは「shi」と発音します。\n清音の一行です。"],
  [11,'たちつてと','ja','/ta chi tsu te to/','hiragana','た行假名，ち读chi、つ读tsu',"ち→chi、つ→tsu、特殊な読み方に注意！\nたちつてとは重要です。"],
  [11,'なにぬねの','ja','/na ni nu ne no/','hiragana','な行假名',"なにぬねのは鼻音が多い行です。\n発音に注意しましょう。"],
  [12,'はひふへほ','ja','/ha hi fu he ho/','hiragana','は行，ふ读fu不是hu',"ふは「fu」と発音、「hu」ではありません！\nは=ha、ふ=fu、ほ=ho"],
  [12,'まみむめも','ja','/ma mi mu me mo/','hiragana','ま行，鼻音m系列',"まみむめも、全部「m」で始まる音です。\n発音しやすい行です。"],
  [12,'やゆよ','ja','/ya yu yo/','hiragana','や行只有三个假名',"やゆよ、や行は三文字だけです。\n拗音の基礎になります。"],
  [12,'らりるれろ','ja','/ra ri ru re ro/','hiragana','ら行，舌尖弹音r/l',"らりるれろ、英語のrとlの中間のような音です。\n舌先を軽く弾いて発音します。"],
  [12,'わをん','ja','/wa wo n/','hiragana','わ行和拨音ん',"わをん、五十音図の最後です。\nんは撥音（ん音）と言います。"],
  [13,'こんにちは','ja','/kon.nichi.wa/','greeting','白天使用的问候语',"こんにちは、田中さん。（你好，田中先生。）\nこんにちは、お久しぶりです。（好久不见。）"],
  [13,'こんばんは','ja','/kon.ban.wa/','greeting','晚上使用的问候语',"こんばんは。（晚上好。）\nこんばんは、お疲れ様でした。"],
  [13,'おはようございます','ja','/o.ha.you.go.zai.ma.su/','greeting','早上好（较正式的用法）',"おはようございます！（早上好！）\n毎朝、おはようございますと言いましょう。"],
  [13,'さようなら','ja','/sa.you.na.ra/','greeting','告别用语',"さようなら、また明日。（再见，明天见。）\nさようなら、お元気で。（保重。）"],
  [13,'ありがとうございます','ja','/a.ri.ga.tou.go.zai.ma.su/','greeting','谢谢（正式礼貌表达）',"ありがとうございます。（非常感谢。）\nどういたしまして。（不客气。）"],
  [14,'すみません','ja','/su.mi.ma.sen/','greeting','对不起/不好意思（万能词）',"すみません、遅れました。（对不起，我迟到了。）\nすみません、これいくらですか？（请问这个多少钱？）"],
  [14,'初めまして','ja','/hajimemashite/','greeting','初次见面时说的第一句话',"初めまして、田中と申します。\n初めまして、どうぞよろしく。"],
  [14,'お疲れ様です','ja','/otsukaresamadesu/','greeting','同事之间常用的问候语',"お疲れ様です！今日も一日お疲れ様でした。\n帰る前に一言「お疲れ様」を言うのがマナーです。"],
  [14,'いただきます','ja','/itadakimasu/','greeting','开饭前说的"我开动了"',"いただきます！（我开动了！）\n食事の前に必ず言う言葉です。"],
  [14,'ごちそうさまでした','ja','/gochisousamadeshita/','greeting','饭后说的"多谢款待"',"ごちそうさでした！（多谢款待！）\n美味しかったです。ごちそうさまでした。"],
  [15,'あか','ja','/aka/','adj./n.','红色；红色的',"あかいリンゴ（红苹果）\nあかちゃん（婴儿）"],
  [15,'あお','ja','/ao/','adj./n.','蓝色；蓝色的',"あおい空（蓝天）\nあおい海（蓝色的大海）"],
  [15,'しろ','ja','/shiro/','adj./n.','白色；白色的',"しろい雲（白云）\nしろい雪（白色的雪）"],
  [15,'くろ','ja','/kuro/','adj./n.','黑色；黑色的',"くろい猫（黑猫）\nくろい髪（黑色的头发）"],
  [16,'いち','ja','/itɕi/','numeral','数字1',"いちがつ（一月）\nいちじ（一点钟）"],
  [16,'に','ja','/ni/','numeral','数字2',"にがつ（二月）\nにじ（两点钟）"],
  [16,'さん','ja','/san/','numeral','数字3',"さんがつ（三月）\nさんじ（三点钟）"],
  [16,'よん/し','ja','/yon/ʃi/','numeral','数字4（通常读よん）',"よんがつ（四月）\nよんじ（四点钟）"],
  [17,'促音','ja','/Q/','grammar','小写っ表示停顿一拍',"ちょっと（稍微）\nかった（买了）"],
  [17,'长音','ja','/long vowel/','grammar','延长一拍的音',"おばあさん（奶奶）\nおじいさん（爷爷）"],
  [17,'ぬ vs の','ja','/nu vs no/','pair','容易混淆的一对假名',"ぬるい（温吞的）\nのだから（所以嘛）"],
  [17,'ね vs れ','ja','/ne vs re/','pair','容易混淆的一对假名',"ねる（睡觉）\nれる（能够）"],
  [18,'私','ja','/watashi/','pronoun','我（标准自称）',"私は学生です。（我是学生。）\n私の名前は田中です。"],
  [18,'あなた','ja','/anata/','pronoun','你（一般不常用）',"あなたのお名前は？（您叫什么名字？）\nあなたの本ですか？（这是你的书吗？）"],
  [18,'彼/彼女','ja','/kare/kanojo/','pronoun','他/她',"彼は日本人です。（他是日本人。）\n彼女はきれいです。（她很漂亮。）"],
  [18,'こちら/そちら/あちら','ja','/ko.chira/so.chira/a.chira/','pronoun','这边/那边/那边（近中远）',"こちらは何ですか？（这是什么？）\nそちらの人是谁？（那个人是谁？）"],
  [19,'食べる','ja','/ta.be.ru/','verb(一段)','吃（一段动词）',"ご飯を食べます。（吃饭。）\n何を食べたいですか？（想吃什么？）"],
  [19,'飲む','ja','/no.mu/','verb(五段)','喝（五段动词）',"水を飲みます。（喝水。）\nお茶を飲みますか？（喝茶吗？）"],
  [19,'行く','ja','/i.ku/','verb(五段)','去',"学校へ行きます。（去学校。）\nどこに行きますか？（去哪里？）"],
  [19,'来る','ja','/ku.ru/','verb(不规则)','来（不规则动词）',"日本に来ました。（来到日本。）\nいつ来ますか？（什么时候来？）"],
  [20,'名前','ja','/namae/','n.','名字；姓名',"お名前は何ですか。（您叫什么名字？）\n私の名前は山田です。（我叫山田。）"],
  [20,'国','ja','/kuni/','n.','国家',"どこの国から来ましたか。（您从哪个国家来的？）\n中国は大きな国です。（中国是一个大国。）"],
  [20,'言葉','ja','/kotoba/','n.','语言；词语',"日本語は難しい言葉です。（日语是难的语言。）\n新しい言葉を覚える。（记新单词。）"],
  [20,'時間','ja','/jikan/','n.','时间',"時間がありますか。（有时间吗？）\n時間が経つの速いですね。（时间过得真快啊。）"],

  [21,'a','zh','/A/','final','汉语拼音第一个单韵母，嘴巴张大',"阿姨(āyí)\n阿婆(āpó)"],
  [21,'o','zh','/o/','final','单韵母，嘴唇圆形',"哦(ò)\n伯伯(óbó)"],
  [21,'e','zh','/ɤ/','final','单韵母，嘴巴半开扁形',"鹅(é)\n恶心(ě xīn)"],
  [21,'i','zh','/i/','final','单韵母，牙齿对齐微笑状',"衣服(yīfu)\n意思(yìsi)"],
  [21,'u','zh','/u/','final','单韵母，嘴唇撮圆向前突出',"房屋(wū)\n物体(wù tǐ)"],
  [22,'b','zh','/p/','initial','不送气双唇清塞音',"爸爸(bàba)\n杯子(bēizi)"],
  [22,'p','zh','/pʰ/','initial','送气双唇清塞音',"怕(pà)\n跑步(pǎo bù)"],
  [22,'m','zh','/m/','initial','双唇鼻音',"妈妈(māma)\n猫咪(māo mī)"],
  [22,'f','zh','/f/','initial','唇齿清擦音',"头发(tóufà)\n丰富(fēng fù)"],
  [23,'d','zh','/t/','initial','不送气舌尖中清塞音',"弟弟(dìdi)\n大家(dàjiā)"],
  [23,'t','zh','/tʰ/','initial','送气舌尖中清塞音',"他(tā)\n特别(tè bié)"],
  [23,'n','zh','/n/','initial','舌尖中鼻音',"你好(nǐ hǎo)\n牛奶(niú nǎi)"],
  [23,'l','zh','/l/','initial','舌尖中边音',"来了(lái le)\n老师(lǎo shī)"],
  [24,'g','zh','/k/','initial','不送气舌根清塞音',"哥哥(gēge)\n公园(gōngyuán)"],
  [24,'k','zh','/kʰ/','initial','送气舌根清塞音',"看(kàn)\n开门(kāi mén)"],
  [24,'h','zh','/x/','initial','舌根清擦音',"你好(nǐhǎo)\n喝水(hēshuǐ)"],
  [25,'j','zh','/tɕ/','initial','舌面不送气清塞擦音',"家里(jiāli)\n见面(jiànmiàn)"],
  [25,'q','zh','/tɕʰ/','initial','舌面送气清塞擦音',"一起去(yì qǐ qù)\n气球(qìqiú)"],
  [25,'x','zh','/ɕ/','initial','舌面清擦音',"学习(xuéxí)\n喜欢(xǐhuan)"],
  [26,'zh','zh','/ʈʂ/','initial','翘舌音，舌尖后不送气清塞擦音',"中国(Zhōngguó)\n知道(zhīdao)"],
  [26,'ch','zh','/ʈʂʰ/','initial','翘舌音，舌尖后送气清塞擦音',"吃(chī)\n吃饭(chī fàn)"],
  [26,'sh','zh','/ʂ/','initial','翘舌音，舌尖后清擦音',"是(shì)\n老师(lǎoshī)"],
  [26,'r','zh','/ʐ/','initial','翘舌音，舌尖后浊擦音',"日(rì)\n容易(róngyì)"],
  [27,'z','zh','/ts/','initial','平舌音，舌尖前不送气清塞擦音',"自在(zìzai)\n写字(xiě zì)"],
  [27,'c','zh','/tsʰ/','initial','平舌音，舌尖前送气清塞擦音',"才(cái)\n吃饭(cān fàn)"],
  [27,'s','zh','/s/','initial','平舌音，舌尖前清擦音',"四(sì)\n颜色(yánsè)"],
  [28,'ai','zh','/aɪ/','compound final','前响复韵母，由a滑向i',"爱好(àihào)\n大海(dà hǎi)"],
  [28,'ei','zh','/eɪ/','compound final','前响复韵母',"妹妹(mèimei)\n北美(běi měi)"],
  [28,'ao','zh','/ɑʊ/','compound final','后响复韵母',"高考(gāokǎo)\n跑步(pǎo bù)"],
  [28,'ou','zh','/ou/','compound final','后响复韵母',"后头(hòutou)\n手(shǒu)"],
  [29,'an','zh','/an/','nasal final','前鼻韵母，舌尖抵住齿龈',"平安(píng ān)\n吃饭(chī fàn)"],
  [29,'en','zh','/ən/','nasal final','前鼻韵母',"认真(rèn zhēn)\n根本(gēn běn)"],
  [29,'ang','zh','/ɑŋ/','nasal final','后鼻韵母，舌根抬起接触软腭',"帮忙(bāng máng)\n上课(shàng kè)"],
  [29,'eng','zh','/əŋ/','nasal final','后鼻韵母',"风(fēng)\n灯(dēng)"],
  [30,'横','zh','/héŋ/','n.','基本笔画之一，从左向右平稳书写',"一横一竖\n横平竖直"],
  [30,'竖','zh','/ʂû/','n.','基本笔画之一，从上向下垂直书写',"竖着写\n横竖交叉"],
  [30,'撇','zh',"/p'iɛ̌/",'n.','基本笔画，从右上向左下斜出',"一撇一捺\n撇开话题"],
  [30,'捺','zh','/nâ/','n.','基本笔画，从左上向右下舒展',"撇捺\n捺脚要稳"],
  [31,'一','zh','/í/','numeral','汉字中最简单的字，表示数字1',"一举两得\n一心一意\n一帆风顺"],
  [31,'日','zh','/ɻî/','n.','太阳；白天；时间单位"日"',"日出东方\n日日夜夜\n生日快乐"],
  [31,'月','zh','/yüè̌/','n.','月亮；月份；时间单位"月"',"月亮弯弯\n一月二月\n月圆之夜"],
  [31,'山','zh','/ʂān/','n.','地面形成的高耸的地貌',"高山流水\n上山下山\n山水画"],
  [32,'亻','zh','/rénzìpáng/','radical','单人旁，作左偏旁时写作亻',"他们\n伙伴\n位置"],
  [32,'艹','zh','/cǎozìtóu/','radical','草字头，表示与植物有关',"花草\n茶叶\n蔬菜"],
  [32,'氵','zh','/sān diǎn shuǐ/','radical','三点水，表示与水有关',"江河\n海洋\n游泳"],
  [32,'钅','zh','/jīn zì páng/','radical','金字旁，表示与金属有关',"铁锅\n银行\n钥匙"],
  [33,'语','zh','/yǔ̌/','n./v.','语言；说话；话语',"汉语\n语文\n语言交流"],
  [33,'思','zh','/sī/','v./n.','思考；思念；想法',"思考问题\n思念家乡\n深思熟虑"],
  [33,'学','zh','/xüé́/','v./n.','学习；学问；学校',"学习语文\n学无止境\n大学"],
  [33,'好','zh','/hǎo/','adj./v.','好；喜爱；容易',"好好学习\n好人好事\n好用"]
];

sql += `-- ==================== 词汇数据 ====================\n`;
sql += `DELETE FROM edu_vocabulary;\nALTER TABLE edu_vocabulary AUTO_INCREMENT = 1;\n\n`;
sql += `INSERT INTO edu_vocabulary (unit_id, word, language, pronunciation, part_of_speech, definition, example_sentences, del_flag) VALUES\n`;
sql += vocabData.map(v => `(${v[0]}, '${esc(v[1])}', '${v[2]}', '${esc(v[3])}', '${esc(v[4])}', '${esc(v[5])}', '${esc(v[6])}', '0')`).join(',\n');
sql += ';\n';

try {
  console.log('Executing full rebuild script...');
  console.log(`Tables: DROP+CREATE edu_course/chapter/unit/lesson/vocabulary`);
  console.log(`Data: ${courses.length} courses, ${chapters.length} chapters, ${units.length} units, ${lessons.length} lessons, ${vocabData.length} vocabs`);

  const result = execSync('mysql -u root -proot --default-character-set=utf8mb4 ryvue', {
    input: sql,
    encoding: 'utf8',
    timeout: 60000
  });
  console.log('\n✅ SUCCESS!');
  console.log(result.trim().substring(0, 500));
} catch(e) {
  console.log('\n❌ ERROR:');
  console.log(e.stderr?.toString()?.substring(0, 1000));
  console.log('STDOUT:', e.stdout?.toString()?.substring(0, 300));
}