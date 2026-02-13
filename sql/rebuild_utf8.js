const mysql = require('mysql2/promise');
const fs = require('fs');

function esc(s) {
  return s ? String(s).replace(/\\/g, '\\\\').replace(/'/g, "\\'") : '';
}

async function rebuild() {
  console.log('=== Connecting to MySQL with utf8mb4 ===');
  
  const connection = await mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'root',
    database: 'ryvue',
    charset: 'utf8mb4'
  });

  console.log('Connected! Truncating tables...');

  await connection.execute('SET NAMES utf8mb4');
  await connection.execute('SET CHARACTER SET utf8mb4');
  await connection.execute('ALTER DATABASE ryvue CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci');

  await connection.execute('TRUNCATE TABLE edu_vocabulary');
  await connection.execute('TRUNCATE TABLE edu_course_lesson');
  await connection.execute('TRUNCATE TABLE edu_course_unit');
  await connection.execute('TRUNCATE TABLE edu_course_chapter');
  await connection.execute('TRUNCATE TABLE edu_course');

  await connection.execute("ALTER TABLE edu_course CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci");
  await connection.execute("ALTER TABLE edu_course_chapter CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci");
  await connection.execute("ALTER TABLE edu_course_unit CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci");
  await connection.execute("ALTER TABLE edu_course_lesson CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci");
  await connection.execute("ALTER TABLE edu_vocabulary CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci");

  console.log('Tables truncated and converted. Inserting courses...');

  const courses = [
    [1, '英语入门基础', 'EN-BASIC', 'en', 'beginner', 'general', '零基础英语学习，从字母、发音到日常会话的全面入门课程', '掌握26个字母发音、100个基础单词、20个日常句型', '英语零基础,字母,发音,入门', 1],
    [2, '英语日常会话', 'EN-DAILY', 'en', 'elementary', 'conversation', '日常生活场景中的实用英语对话，涵盖购物、问路、餐厅等高频场景', '能进行基本的日常英语交流，听懂简单对话', '日常对话,生活英语,实用口语', 2],
    [3, '英语社交礼仪', 'EN-SOCIAL', 'en', 'intermediate', 'conversation', '西方社交礼仪与地道表达，学会得体地打招呼、道歉、感谢、邀请等', '掌握英语国家社交礼仪，表达自然得体', '社交礼仪,文化,地道表达', 3],
    [4, '英语医疗场景', 'EN-MEDICAL', 'en', 'intermediate', 'conversation', '医院就诊、描述症状、购买药品等医疗场景的英语表达', '能用英语在医院就诊并准确描述身体状况', '医疗英语,看病,健康', 3],
    [5, '英语职场沟通', 'EN-WORK', 'en', 'intermediate', 'conversation', '会议、报告、邮件、deadline等职场场景的专业英语表达', '能在职场环境中用英语进行基本沟通', '职场英语,商务,会议', 3],
    [6, '日语五十音与基础会话', 'JA-BASIC', 'ja', 'beginner', 'general', '从日语五十音图（平假名/片假名）开始，到基础问候语和日常会话', '熟练掌握五十音图，能用日语进行简单自我介绍和问候', '日语入门,五十音,假名', 1]
  ];

  for (const c of courses) {
    await connection.execute(
      `INSERT INTO edu_course(course_id,course_name,course_code,language,level,course_type,description,learning_objectives,tags,difficulty_score,is_free,is_published,del_flag) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)`,
      [c[0], c[1], c[2], c[3], c[4], c[5], c[6], c[7], c[8], c[9], '1', '1', '0']
    );
  }
  console.log(`Courses: ${courses.length} inserted`);

  const chapterMap = {
    1: [['字母与发音','26个英文字母的标准发音与书写'],['日常问候','Hello, Good morning等基础问候语'],['自我介绍','名字、国籍、职业的自我介绍'],['家庭成员','father, mother等家庭成员词汇'],['外貌特征','tall, beautiful等外形描述词'],['日常作息','wake up, breakfast等日常活动词汇'],['社交礼仪','sorry, excuse me等礼貌用语'],['情感表达式','happy, sad等情感表达'],['庆祝活动','birthday, party等庆祝活动词汇'],['医疗场景','hospital, doctor等医疗相关'],['职场沟通','meeting, report等职场词汇']],
    2: [['见面问候','初次见面的标准寒暄'],['介绍他人','如何向他人介绍你的朋友家人'],['家庭话题','谈论家人的基本信息'],['描述人物','描述一个人的外貌性格'],['一天作息','从早到晚的日常活动'],['礼貌用语','道歉、感谢、请求帮助'],['表达情感','喜怒哀乐的正确表达方式'],['节日聚会','生日派对、节日聚会场景'],['看病就医','去医院看病的完整流程'],['工作场合','办公室里的常用表达']],
    3: [['打招呼与告别','不同时段的问候方式'],['感谢与道歉','如何得体地表示感谢和歉意'],['表达情绪','各种情绪的地道表达'],['邀请与回应','发出邀请和接受/拒绝邀请'],['赞美与祝贺','赞美他人和祝贺特殊时刻'],['请求与拒绝','提出请求和委婉拒绝']],
    4: [['描述症状','头痛发烧等各种症状描述'],['看医生流程','挂号、问诊、检查全流程'],['买药与用药','药房买药和服药说明'],['急诊情况','紧急情况的应对方法'],['健康建议','医生给出的健康建议']],
    5: [['办公室日常','办公室日常交流'],['开会讨论','开会时的发言和讨论'],['邮件写作','商务邮件的写作规范'],['电话沟通','电话接打技巧'],['汇报工作','向上级汇报工作进展']],
    6: [['平假名（あ行-な行）','日语前半部分平假名'],['平假名（は行-わ行）','后半部分平假名'],['片假名与浊音','片假名及拗音促音'],['基础问候语','こんにちは等基础问候'],['日常会话','日常对话句型'],['颜色与数字','颜色名称和数字读法'],['基础语法','はです等基础语法'],['汉字书写','基础汉字和偏旁部首']]
  };

  const unitTemplates = {
    '字母与发音':[['字母A-E','a,b,c,d,e的发音与例词'],['字母F-J','f,g,h,i,j的发音与例词'],['字母K-O','k,l,m,n,o的发音与例词'],['字母P-T','p,q,r,s,t的发音与例词'],['字母U-Z','u,v,w,x,y,z的发音与例词']],
    '日常问候':[['Hello与Goodbye','Hello! Nice to meet you. Goodbye! See you tomorrow.'],['Good Morning等问候','Good morning, Good afternoon, Good evening'],['How are you?','询问近况的回答方式']],
    '自我介绍':[['Name与Come from','What is your name? Where do you come from?'],['Student与Engineer','职业相关的自我介绍词汇']],
    '家庭成员':[['Father与Mother','家庭成员核心词汇'],['Brother与Sister','兄弟姐妹及相关表达'],['Family','关于家庭的综合表达']],
    '外貌特征':[['Tall与Beautiful','形容人的外观词汇'],['Friendly与Young','性格与年龄相关描述词']],
    '日常作息':[['Wake Up与Breakfast','早晨起床和早餐相关'],['Usually与Every Day','频率副词的使用']],
    '社交礼仪':[['Sorry与Excuse Me','道歉和请求注意的表达'],['Apologize与Welcome','更正式的礼貌用语']],
    '情感表达式':[['Happy与Sad','基本情绪词汇'],['Angry与Excited','强烈情绪的表达']],
    '庆祝活动':[['Celebration与Invite','庆祝活动和邀请'],['Amazing与Party','派对相关词汇']],
    '医疗场景':[['Hospital与Doctor','医院和医生相关'],['Fever与Medicine','症状和药物词汇'],['Ambulance与Prescription','急救和处方']],
    '职场沟通':[['Deadline与Meeting','截止日期和会议'],['Report与Project','工作报告和项目']],
    '平假名（あ行-な行）':[['あいうえお','第一行元音'],['かきくけこ','か行清音'],['さしすせそ','さ行（し=shi）'],['たちつてと','た行（ち=chi,つ=tsu）'],['なにぬねの','な行鼻音']],
    '平假名（は行-わ行）':[['はひふへほ','は行（ふ=fu）'],['まみむめも','ま行鼻音'],['やゆよ','や行三字'],['らりるれろ','ら行弹音'],['わをん','わ行和拨音ん']],
    '片假名与浊音':[['アイウエオ','片假名对应'],['ガギグゲゴ','浊音'],['促音与长音','っ和ーの使用'],['容易混淆的假名','ぬvsの, ねvsれ']],
    '基础问候语':[['こんにちは/こんばんは','白天和晚上的问候'],['おはようございます/さようなら','早上好和再见'],['ありがとうございます/すみません','感谢和道歉'],['初めまして/お疲れ様です','初次见面和同事问候'],['いただきます/ごちそうさまでした','饭前饭后用语']],
    '日常会话':[['颜色词汇','あか/あお/しろ/くろ'],['数字1-10','いち/に/さん...'],['人称代词','私/あなた/彼/彼女'],['基本动词','食べる/飲む/行く/来る'],['常用名词','名前/国/言葉/時間']],
    '颜色与数字':[['あいうえお复习','五个元音'],['bpmf声母','双唇音和唇齿音'],['dtnl声母','舌尖中音'],['gkh声母','舌根音']],
    '基础语法':[['jqx声母','舌面音'],['zhchshr声母','翘舌音'],['zcs声母','平舌音'],['ai/ei/ao/ou复韵母','复韵母']],
    '汉字书写':[['an/en/ang/eng鼻韵母','前后鼻音'],['横竖撇捺','基本笔画'],['日月山水','基础汉字'],['亻艹氵钅','常用偏旁'],['语思学好','常用字词']]
  };

  const chapters = [];
  const units = [];
  let chId = 1, uId = 1;
  
  for (const c of courses) {
    const chList = chapterMap[c[0]] || chapterMap[1];
    for (let i = 0; i < chList.length; i++) {
      const [chName, chDesc] = chList[i];
      chapters.push([chId, c[0], chName, chDesc]);
      const utpls = unitTemplates[chName] || [[chName+'-单元1', chName+'的第一单元']];
      for (const [uname, udesc] of utpls) { 
        units.push([uId++, c[0], chId, uname, udesc]); 
      }
      chId++;
    }
  }

  for (const [idx, ch] of chapters.entries()) {
    await connection.execute(
      `INSERT INTO edu_course_chapter(chapter_id,course_id,chapter_name,chapter_order,description,del_flag) VALUES(?,?,?,?,?,?)`,
      [ch[0], ch[1], ch[2], idx+1, ch[3], '0']
    );
  }
  console.log(`Chapters: ${chapters.length} inserted`);

  for (const [idx, u] of units.entries()) {
    await connection.execute(
      `INSERT INTO edu_course_unit(unit_id,course_id,chapter_id,unit_name,unit_order,description,del_flag) VALUES(?,?,?,?,?,?)`,
      [u[0], u[1], u[2], u[3], idx+1, u[4], '0']
    );
  }
  console.log(`Units: ${units.length} inserted`);

  const lessonTypes = ['vocabulary','grammar','listening','speaking'];
  const typeLabels = { vocabulary:'单词学习', grammar:'语法讲解', listening:'听力练习', speaking:'口语训练' };
  const lessons = []; 
  let lId = 1;
  
  for (const u of units) { 
    for (let t=0;t<lessonTypes.length;t++) {
      lessons.push([lId++, u[0], u[3]+'-'+typeLabels[lessonTypes[t]], t+1, lessonTypes[t], 15]); 
    }
  }

  for (const l of lessons) {
    await connection.execute(
      `INSERT INTO edu_course_lesson(lesson_id,unit_id,lesson_name,lesson_order,lesson_type,duration,del_flag) VALUES(?,?,?,?,?,?,?)`,
      [l[0], l[1], l[2], l[3], l[4], l[5], '0']
    );
  }
  console.log(`Lessons: ${lessons.length} inserted`);

  const vocabData = [
    { unit: 1, word: 'hello', lang: 'en', pron: '/həˈloʊ/', pos: 'interj.', def: '你好；喂（打电话或打招呼时用）', ex: "Hello! Nice to meet you.\nHello, may I speak to Mr. Smith?" },
    { unit: 1, word: 'good morning', lang: 'en', pron: '/ɡʊd ˈmɔːrnɪŋ/', pos: 'phrase', def: '早上好（上午问候语）', ex: "Good morning, class!\nGood morning! Did you sleep well?" },
    { unit: 1, word: 'how are you', lang: 'en', pron: '/haʊ ɑːr juː/', pos: 'phrase', def: '你好吗？（询问对方近况）', ex: "How are you doing today?\nHow are you? I am fine, thank you." },
    { unit: 1, word: 'nice to meet you', lang: 'en', pron: '/naɪs tuː miːt juː/', pos: 'phrase', def: '很高兴认识你（初次见面时用）', ex: "Nice to meet you, Sarah.\nIt is nice to meet you too!" },
    { unit: 1, word: 'goodbye', lang: 'en', pron: '/ɡʊdˈbaɪ/', pos: 'interj.', def: '再见；告别', ex: "Goodbye! See you tomorrow.\nGoodbye and take care!" },
    { unit: 1, word: 'thank you', lang: 'en', pron: '/θæŋk juː/', pos: 'phrase', def: '谢谢你；感谢', ex: "Thank you for your help.\nThank you very much!" },

    { unit: 2, word: 'name', lang: 'en', pron: '/neɪm/', pos: 'n.', def: '名字；名称', ex: "What is your name?\nMy name is Alice." },
    { unit: 2, word: 'come from', lang: 'en', pron: '/kʌm frɒm/', pos: 'phr. v.', def: '来自；出生于', ex: "Where do you come from?\nI come from Shanghai." },
    { unit: 2, word: 'student', lang: 'en', pron: '/ˈstuːdnt/', pos: 'n.', def: '学生', ex: "She is a university student.\nHe is an exchange student from Japan." },
    { unit: 2, word: 'engineer', lang: 'en', pron: '/ˌendʒɪˈnɪr/', pos: 'n.', def: '工程师', ex: "My father works as an engineer.\nShe is a software engineer at Google." },

    { unit: 3, word: 'father', lang: 'en', pron: '/ˈfɑːðər/', pos: 'n.', def: '父亲；爸爸', ex: "My father is a doctor.\nI look like my father." },
    { unit: 3, word: 'mother', lang: 'en', pron: '/ˈmʌðər/', pos: 'n.', def: '母亲；妈妈', ex: "My mother cooks delicious food.\nHappy Mother's Day!" },
    { unit: 3, word: 'brother', lang: 'en', pron: '/ˈbrʌðər/', pos: 'n.', def: '兄弟；弟兄', ex: "I have one older brother.\nMy brother and I play soccer together." },
    { unit: 3, word: 'sister', lang: 'en', pron: '/ˈsɪstər/', pos: 'n.', def: '姐妹；姐妹', ex: "My younger sister is very cute.\nShe is like a sister to me." },
    { unit: 3, word: 'family', lang: 'en', pron: '/ˈfæmɪli/', pos: 'n.', def: '家庭；家人', ex: "I love my family very much.\nWe had a family dinner last night." },

    { unit: 4, word: 'tall', lang: 'en', pron: '/tɔːl/', pos: 'adj.', def: '高的（身材或物体）', ex: "He is very tall, about six feet.\nThe building is tall and modern." },
    { unit: 4, word: 'beautiful', lang: 'en', pron: '/ˈbjuːtɪfl/', pos: 'adj.', def: '美丽的；漂亮的', ex: "The view from here is beautiful.\nShe looks beautiful in that dress." },
    { unit: 4, word: 'friendly', lang: 'en', pron: '/ˈfrendli/', pos: 'adj.', def: '友好的；亲切的', ex: "The people here are very friendly.\nHe has a friendly smile." },
    { unit: 4, word: 'young', lang: 'en', pron: '/jʌŋ/', pos: 'adj.', def: '年轻的；年少的', ex: "She is young but very talented.\nYoung people love this music." },

    { unit: 5, word: 'wake up', lang: 'en', pron: '/weɪk ʌp/', pos: 'phr. v.', def: '醒来；醒过来', ex: "I wake up at seven every day.\nPlease wake me up at six tomorrow." },
    { unit: 5, word: 'breakfast', lang: 'en', pron: '/ˈbrekfəst/', pos: 'n.', def: '早餐；早饭', ex: "I always have breakfast at home.\nBreakfast is the most important meal of the day." },
    { unit: 5, word: 'usually', lang: 'en', pron: '/ˈjuːʒuəli/', pos: 'adv.', def: '通常；一般地', ex: "I usually go to bed at eleven.\nShe usually takes the bus to work." },
    { unit: 5, word: 'every day', lang: 'en', pron: '/ˈevri deɪ/', pos: 'adv.', def: '每天；天天', ex: "I exercise every day.\nHe reads English every morning." },

    { unit: 6, word: 'sorry', lang: 'en', pron: '/ˈsɒri/', pos: 'interj./adj.', def: '对不起；抱歉的', ex: "Sorry, I am late.\nI am sorry for the mistake." },
    { unit: 6, word: 'excuse me', lang: 'en', pron: '/ɪkˈskjuːz miː/', pos: 'phrase', def: '打扰一下；借过', ex: "Excuse me, where is the station?\nExcuse me, can I ask a question?" },
    { unit: 6, word: 'apologize', lang: 'en', pron: '/əˈpɒlədʒaɪz/', pos: 'v.', def: '道歉；谢罪', ex: "I sincerely apologize for the mistake.\nYou should apologize to her." },
    { unit: 6, word: 'welcome', lang: 'en', pron: '/ˈwelkəm/', pos: 'adj./v./interj.', def: '受欢迎的；欢迎；不客气', ex: "You are always welcome here.\n- Thank you. - You are welcome." },

    { unit: 7, word: 'happy', lang: 'en', pron: '/ˈhæpi/', pos: 'adj.', def: '高兴的；快乐的', ex: "I am so happy today!\nHappy birthday to you!" },
    { unit: 7, word: 'sad', lang: 'en', pron: '/sæd/', pos: 'adj.', def: '悲伤的；难过的', ex: "She looks sad today.\nI feel sad when it rains." },
    { unit: 7, word: 'angry', lang: 'en', pron: '/ˈæŋɡri/', pos: 'adj.', def: '生气的；愤怒的', ex: "Don't be angry with me.\nHe was angry about the news." },
    { unit: 7, word: 'excited', lang: 'en', pron: '/ɪkˈsaɪtɪd/', pos: 'adj.', def: '兴奋的；激动的', ex: "I am excited about the trip.\nShe was excited to hear the good news." },

    { unit: 8, word: 'celebration', lang: 'en', pron: '/ˌselɪˈbreɪʃn/', pos: 'n.', def: '庆祝；庆典', ex: "We had a big celebration for New Year.\nThe party was a great celebration." },
    { unit: 8, word: 'invite', lang: 'en', pron: '/ɪnˈvaɪt/', pos: 'v.', def: '邀请；招待', ex: "She invited me to her birthday party.\nThanks for inviting me." },
    { unit: 8, word: 'amazing', lang: 'en', pron: '/əˈmeɪzɪŋ/', pos: 'adj.', def: '令人惊叹的；了不起的', ex: "The view from the top is amazing.\nThat is an amazing idea!" },
    { unit: 8, word: 'party', lang: 'en', pron: '/ˈpɑːrti/', pos: 'n.', def: '聚会；派对', ex: "Are you coming to the party tonight?\nWe had a great party last weekend." },

    { unit: 9, word: 'hospital', lang: 'en', pron: '/ˈhɒspɪtl/', pos: 'n.', def: '医院', ex: "He went to the hospital yesterday.\nThe hospital is near my home." },
    { unit: 9, word: 'doctor', lang: 'en', pron: '/ˈdɒktər/', pos: 'n.', def: '医生；博士', ex: "I need to see a doctor.\nThe doctor said I should rest more." },
    { unit: 9, word: 'fever', lang: 'en', pron: '/ˈfiːvər/', pos: 'n.', def: '发烧；发热', ex: "I have a high fever.\nShe has a fever of 39 degrees." },
    { unit: 9, word: 'medicine', lang: 'en', pron: '/ˈmedsn/', pos: 'n.', def: '药；医学', ex: "Take this medicine three times a day.\nShe studies medicine at university." },
    { unit: 9, word: 'ambulance', lang: 'en', pron: '/ˈæmbjələns/', pos: 'n.', def: '救护车', ex: "Call an ambulance immediately!\nThe ambulance arrived within five minutes." },
    { unit: 9, word: 'prescription', lang: 'en', pron: '/prɪˈskrɪpʃn/', pos: 'n.', def: '处方；药方', ex: "The doctor wrote a prescription for me.\nYou need a prescription to buy this medicine." },

    { unit: 10, word: 'deadline', lang: 'en', pron: '/ˈdedlaɪn/', pos: 'n.', def: '截止日期；最后期限', ex: "The deadline for the project is Friday.\nWe need to meet the deadline." },
    { unit: 10, word: 'meeting', lang: 'en', pron: '/ˈmiːtɪŋ/', pos: 'n.', def: '会议；会面', ex: "We have a meeting at 3 PM.\nThe meeting lasted two hours." },
    { unit: 10, word: 'report', lang: 'en', pron: '/rɪˈpɔːrt/', pos: 'n./v.', def: '报告；汇报', ex: "Please submit your report by Monday.\nHe reported the incident to the police." },
    { unit: 10, word: 'project', lang: 'en', pron: '/ˈprɒdʒekt/', pos: 'n.', def: '项目；工程', ex: "We are working on a new project.\nThe project will take three months." },

    { unit: 11, word: 'あいうえお', lang: 'ja', pron: '/a i u e o/', pos: 'hiragana', def: '日语五十音图第一行，基础元音', ex: "あいうえおは日本語の基礎です。\n五十音図の最初の行です。" },
    { unit: 11, word: 'かきくけこ', lang: 'ja', pron: '/ka ki ku ke ko/', pos: 'hiragana', def: 'か行假名，清音', ex: "かきくけこは濁点でがぎぐげごになります。\n日本語の基本文字です。" },
    { unit: 11, word: 'さしすせそ', lang: 'ja', pron: '/sa shi su se so/', pos: 'hiragana', def: 'さ行假名，し读作shi', ex: "さしすせそ、注意：しは「shi」と発音します。\n清音の一行です。" },
    { unit: 11, word: 'たちつてと', lang: 'ja', pron: '/ta chi tsu te to/', pos: 'hiragana', def: 'た行假名，ち读chi、つ读tsu', ex: "ち→chi、つ→tsu、特殊な読み方に注意！\nたちつてとは重要です。" },
    { unit: 11, word: 'なにぬねの', lang: 'ja', pron: '/na ni nu ne no/', pos: 'hiragana', def: 'な行假名', ex: "なにぬねのは鼻音が多い行です。\n発音に注意しましょう。" },

    { unit: 12, word: 'はひふへほ', lang: 'ja', pron: '/ha hi fu he ho/', pos: 'hiragana', def: 'は行，ふ读fu不是hu', ex: "ふは「fu」と発音、「hu」ではありません！\nは=ha、ふ=fu、ほ=ho" },
    { unit: 12, word: 'まみむめも', lang: 'ja', pron: '/ma mi mu me mo/', pos: 'hiragana', def: 'ま行，鼻音m系列', ex: "まみむめも、全部「m」で始まる音です。\n発音しやすい行です。" },
    { unit: 12, word: 'やゆよ', lang: 'ja', pron: '/ya yu yo/', pos: 'hiragana', def: 'や行只有三个假名', ex: "やゆよ、や行は三文字だけです。\n拗音の基礎になります。" },
    { unit: 12, word: 'らりるれろ', lang: 'ja', pron: '/ra ri ru re ro/', pos: 'hiragana', def: 'ら行，舌尖弹音r/l', ex: "らりるれろ、英語のrとlの中間のような音です。\n舌先を軽く弾いて発音します。" },
    { unit: 12, word: 'わをん', lang: 'ja', pron: '/wa wo n/', pos: 'hiragana', def: 'わ行和拨音ん', ex: "わをん、五十音図の最後です。\nんは撥音（ん音）と言います。" },

    { unit: 13, word: 'こんにちは', lang: 'ja', pron: '/kon.nichi.wa/', pos: 'greeting', def: '白天使用的问候语（约11点~18点）', ex: "こんにちは、田中さん。（你好，田中先生。）\nこんにちは、お久しぶりです。（好久不见。）" },
    { unit: 13, word: 'こんばんは', lang: 'ja', pron: '/kon.ban.wa/', pos: 'greeting', def: '晚上使用的问候语（18点以后）', ex: "こんばんは。（晚上好。）\nこんばんは、お疲れ様でした。" },
    { unit: 13, word: 'おはようございます', lang: 'ja', pron: '/o.ha.you.go.zai.ma.su/', pos: 'greeting', def: '早上好（较正式的用法）', ex: "おはようございます！（早上好！）\n毎朝、おはようございますと言いましょう。" },
    { unit: 13, word: 'さようなら', lang: 'ja', pron: '/sa.you.na.ra/', pos: 'greeting', def: '告别用语（可能一段时间不见）', ex: "さようなら、また明日。（再见，明天见。）\nさようなら、お元気で。（保重。）" },
    { unit: 13, word: 'ありがとうございます', lang: 'ja', pron: '/a.ri.ga.tou.go.zai.ma.su/', pos: 'greeting', def: '谢谢（正式礼貌表达）', ex: "ありがとうございます。（非常感谢。）\nどういたしまして。（不客气。）" },

    { unit: 14, word: 'すみません', lang: 'ja', pron: '/su.mi.ma.sen/', pos: 'greeting', def: '对不起/不好意思（万能词）', ex: "すみません、遅れました。（对不起，我迟到了。）\nすみません、これいくらですか？（请问这个多少钱？）" },
    { unit: 14, word: '初めまして', lang: 'ja', pron: '/hajimemashite/', pos: 'greeting', def: '初次见面时说的第一句话', ex: "初めまして、田中と申します。\n初めまして、どうぞよろしく。" },
    { unit: 14, word: 'お疲れ様です', lang: 'ja', pron: '/otsukaresamadesu/', pos: 'greeting', def: '同事之间常用的问候语', ex: "お疲れ様です！今日も一日お疲れ様でした。\n帰る前に一言「お疲れ様」を言うのがマナーです。" },
    { unit: 14, word: 'いただきます', lang: 'ja', pron: '/itadakimasu/', pos: 'greeting', def: '开饭前说的"我开动了"', ex: "いただきます！（我开动了！）\n食事の前に必ず言う言葉です。" },
    { unit: 14, word: 'ごちそうさまでした', lang: 'ja', pron: '/gochisousamadeshita/', pos: 'greeting', def: '饭后说的"多谢款待"', ex: "ごちそうさまでした！（多谢款待！）\n美味しかったです。ごちそうさまでした。" },

    { unit: 15, word: 'あか', lang: 'ja', pron: '/aka/', pos: 'adj./n.', def: '红色；红色的', ex: "あかいリンゴ（红苹果）\nあかちゃん（婴儿）" },
    { unit: 15, word: 'あお', lang: 'ja', pron: '/ao/', pos: 'adj./n.', def: '蓝色；蓝色的', ex: "あおい空（蓝天）\nあおい海（蓝色的大海）" },
    { unit: 15, word: 'しろ', lang: 'ja', pron: '/shiro/', pos: 'adj./n.', def: '白色；白色的', ex: "しろい雲（白云）\nしろい雪（白色的雪）" },
    { unit: 15, word: 'くろ', lang: 'ja', pron: '/kuro/', pos: 'adj./n.', def: '黑色；黑色的', ex: "くろい猫（黑猫）\nくろい髪（黑色的头发）" },

    { unit: 16, word: 'いち', lang: 'ja', pron: '/itɕi/', pos: 'numeral', def: '数字1', ex: "いちがつ（一月）\nいちじ（一点钟）" },
    { unit: 16, word: 'に', lang: 'ja', pron: '/ni/', pos: 'numeral', def: '数字2', ex: "にがつ（二月）\nにじ（两点钟）" },
    { unit: 16, word: 'さん', lang: 'ja', pron: '/san/', pos: 'numeral', def: '数字3', ex: "さんがつ（三月）\nさんじ（三点钟）" },
    { unit: 16, word: 'よん/し', lang: 'ja', pron: '/yon/ʃi/', pos: 'numeral', def: '数字4（通常读よん）', ex: "よんがつ（四月）\nよんじ（四点钟）" },

    { unit: 17, word: '促音', lang: 'ja', pron: '/Q (glottal stop)/', pos: 'grammar', def: '小写っ表示停顿一拍', ex: "ちょっと（稍微）\nかった（买了）" },
    { unit: 17, word: '长音', lang: 'ja', pron: '/long vowel/', pos: 'grammar', def: '延长一拍的音', ex: "おばあさん（奶奶）\nおじいさん（爷爷）" },
    { unit: 17, word: 'ぬ vs の', lang: 'ja', pron: '/nu/ vs /no/', pos: 'pair', def: '容易混淆的一对假名', ex: "ぬるい（温吞的）\nのだから（所以嘛）" },
    { unit: 17, word: 'ね vs れ', lang: 'ja', pron: '/ne/ vs /re/', pos: 'pair', def: '容易混淆的一对假名', ex: "ねる（睡觉）\nれる（能够）" },

    { unit: 18, word: '私', lang: 'ja', pron: '/watashi/', pos: 'pronoun', def: '我（标准自称）', ex: "私は学生です。（我是学生。）\n私の名前は田中です。" },
    { unit: 18, word: 'あなた', lang: 'ja', pron: '/anata/', pos: 'pronoun', def: '你（一般不常用）', ex: "あなたのお名前は？（您叫什么名字？）\nあなたの本ですか？（这是你的书吗？）" },
    { unit: 18, word: '彼/彼女', lang: 'ja', pron: '/kare/kanojo/', pos: 'pronoun', def: '他/她', ex: "彼は日本人です。（他是日本人。）\n彼女はきれいです。（她很漂亮。）" },
    { unit: 18, word: 'こちら/そちら/あちら', lang: 'ja', pron: '/ko.chira/so.chira/a.chira/', pos: 'pronoun', def: '这边/那边/那边（近中远）', ex: "こちらは何ですか？（这是什么？）\nそちらの人是谁？（那个人是谁？）" },

    { unit: 19, word: '食べる', lang: 'ja', pron: '/ta.be.ru/', pos: 'verb (一段)', def: '吃（一段动词）', ex: "ご飯を食べます。（吃饭。）\n何を食べたいですか？（想吃什么？）" },
    { unit: 19, word: '飲む', lang: 'ja', pron: '/no.mu/', pos: 'verb (五段)', def: '喝（五段动词）', ex: "水を飲みます。（喝水。）\nお茶を飲みますか？（喝茶吗？）" },
    { unit: 19, word: '行く', lang: 'ja', pron: '/i.ku/', pos: 'verb (五段)', def: '去', ex: "学校へ行きます。（去学校。）\nどこに行きますか？（去哪里？）" },
    { unit: 19, word: '来る', lang: 'ja', pron: '/ku.ru/', pos: 'verb (不规则)', def: '来（不规则动词）', ex: "日本に来ました。（来到日本。）\nいつ来ますか？（什么时候来？）" },

    { unit: 20, word: '名前', lang: 'ja', pron: '/namae/', pos: 'n.', def: '名字；姓名', ex: "お名前は何ですか。（您叫什么名字？）\n私の名前は山田です。（我叫山田。）" },
    { unit: 20, word: '国', lang: 'ja', pron: '/kuni/', pos: 'n.', def: '国家', ex: "どこの国から来ましたか。（您从哪个国家来的？）\n中国は大きな国です。（中国是一个大国。）" },
    { unit: 20, word: '言葉', lang: 'ja', pron: '/kotoba/', pos: 'n.', def: '语言；词语', ex: "日本語は難しい言葉です。（日语是难的语言。）\n新しい言葉を覚える。（记新单词。）" },
    { unit: 20, word: '時間', lang: 'ja', pron: '/jikan/', pos: 'n.', def: '时间', ex: "時間がありますか。（有时间吗？）\n時間が経つの速いですね。（时间过得真快啊。）" },

    { unit: 21, word: 'a', lang: 'zh', pron: '/A/', pos: 'final', def: '汉语拼音第一个单韵母，嘴巴张大', ex: "阿姨(āyí)\n阿婆(āpó)" },
    { unit: 21, word: 'o', lang: 'zh', pron: '/o/', pos: 'final', def: '单韵母，嘴唇圆形', ex: "哦(ò)\n伯伯(óbó)" },
    { unit: 21, word: 'e', lang: 'zh', pron: '/ɤ/', pos: 'final', def: '单韵母，嘴巴半开扁形', ex: "鹅(é)\n恶心(ě xīn)" },
    { unit: 21, word: 'i', lang: 'zh', pron: '/i/', pos: 'final', def: '单韵母，牙齿对齐微笑状', ex: "衣服(yīfu)\n意思(yìsi)" },
    { unit: 21, word: 'u', lang: 'zh', pron: '/u/', pos: 'final', def: '单韵母，嘴唇撮圆向前突出', ex: "房屋(wū)\n物体(wù tǐ)" },

    { unit: 22, word: 'b', lang: 'zh', pron: '/p/', pos: 'initial', def: '不送气双唇清塞音', ex: "爸爸(bàba)\n杯子(bēizi)" },
    { unit: 22, word: 'p', lang: 'zh', pron: '/pʰ/', pos: 'initial', def: '送气双唇清塞音', ex: "怕(pà)\n跑步(pǎo bù)" },
    { unit: 22, word: 'm', lang: 'zh', pron: '/m/', pos: 'initial', def: '双唇鼻音', ex: "妈妈(māma)\n猫咪(māo mī)" },
    { unit: 22, word: 'f', lang: 'zh', pron: '/f/', pos: 'initial', def: '唇齿清擦音', ex: "头发(tóufà)\n丰富(fēng fù)" },

    { unit: 23, word: 'd', lang: 'zh', pron: '/t/', pos: 'initial', def: '不送气舌尖中清塞音', ex: "弟弟(dìdi)\n大家(dàjiā)" },
    { unit: 23, word: 't', lang: 'zh', pron: '/tʰ/', pos: 'initial', def: '送气舌尖中清塞音', ex: "他(tā)\n特别(tè bié)" },
    { unit: 23, word: 'n', lang: 'zh', pron: '/n/', pos: 'initial', def: '舌尖中鼻音', ex: "你好(nǐ hǎo)\n牛奶(niú nǎi)" },
    { unit: 23, word: 'l', lang: 'zh', pron: '/l/', pos: 'initial', def: '舌尖中边音', ex: "来了(lái le)\n老师(lǎo shī)" },

    { unit: 24, word: 'g', lang: 'zh', pron: '/k/', pos: 'initial', def: '不送气舌根清塞音', ex: "哥哥(gēge)\n公园(gōngyuán)" },
    { unit: 24, word: 'k', lang: 'zh', pron: '/kʰ/', pos: 'initial', def: '送气舌根清塞音', ex: "看(kàn)\n开门(kāi mén)" },
    { unit: 24, word: 'h', lang: 'zh', pron: '/x/', pos: 'initial', def: '舌根清擦音', ex: "你好(nǐhǎo)\n喝水(hēshuǐ)" },

    { unit: 25, word: 'j', lang: 'zh', pron: '/tɕ/', pos: 'initial', def: '舌面不送气清塞擦音', ex: "家里(jiāli)\n见面(jiànmiàn)" },
    { unit: 25, word: 'q', lang: 'zh', pron: '/tɕʰ/', pos: 'initial', def: '舌面送气清塞擦音', ex: "一起去(yì qǐ qù)\n气球(qìqiú)" },
    { unit: 25, word: 'x', lang: 'zh', pron: '/ɕ/', pos: 'initial', def: '舌面清擦音', ex: "学习(xuéxí)\n喜欢(xǐhuan)" },

    { unit: 26, word: 'zh', lang: 'zh', pron: '/ʈʂ/', pos: 'initial', def: '翘舌音，舌尖后不送气清塞擦音', ex: "中国(Zhōngguó)\n知道(zhīdao)" },
    { unit: 26, word: 'ch', lang: 'zh', pron: '/ʈʂʰ/', pos: 'initial', def: '翘舌音，舌尖后送气清塞擦音', ex: "吃(chī)\n吃饭(chī fàn)" },
    { unit: 26, word: 'sh', lang: 'zh', pron: '/ʂ/', pos: 'initial', def: '翘舌音，舌尖后清擦音', ex: "是(shì)\n老师(lǎoshī)" },
    { unit: 26, word: 'r', lang: 'zh', pron: '/ʐ/', pos: 'initial', def: '翘舌音，舌尖后浊擦音', ex: "日(rì)\n容易(róngyì)" },

    { unit: 27, word: 'z', lang: 'zh', pron: '/ts/', pos: 'initial', def: '平舌音，舌尖前不送气清塞擦音', ex: "自在(zìzai)\n写字(xiě zì)" },
    { unit: 27, word: 'c', lang: 'zh', pron: '/tsʰ/', pos: 'initial', def: '平舌音，舌尖前送气清塞擦音', ex: "才(cái)\n吃饭(chī fàn)" },
    { unit: 27, word: 's', lang: 'zh', pron: '/s/', pos: 'initial', def: '平舌音，舌尖前清擦音', ex: "丝(sī)\n说话(shuōhuà)" },

    { unit: 28, word: 'ai', lang: 'zh', pron: '/aɪ/', pos: 'compound final', def: '复韵母ai，由a滑向i', ex: "爱(ài)\n白(bái)" },
    { unit: 28, word: 'ei', lang: 'zh', pron: '/eɪ/', pos: 'compound final', def: '复韵母ei，由e滑向i', ex: "杯(bēi)\n妹(mèi)" },
    { unit: 28, word: 'ao', lang: 'zh', pron: '/ɑʊ/', pos: 'compound final', def: '复韵母ao，由a滑向o', ex: "奥(ào)\n高(gāo)" },
    { unit: 28, word: 'ou', lang: 'zh', pron: '/oʊ/', pos: 'compound final', def: '复韵母ou，由o滑向u', ex: "欧(ōu)\n手(shǒu)" },

    { unit: 29, word: 'an', lang: 'zh', pron: '/an/', pos: 'nasal final', def: '前鼻韵母an', ex: "安(ān)\n半(bàn)" },
    { unit: 29, word: 'en', lang: 'zh', pron: '/ən/', pos: 'nasal final', def: '前鼻韵母en', ex: "恩(ēn)\n门(mén)" },
    { unit: 29, word: 'ang', lang: 'zh', pron: '/ɑŋ/', pos: 'nasal final', def: '后鼻韵母ang', ex: "昂(áng)\n帮(bāng)" },
    { unit: 29, word: 'eng', lang: 'zh', pron: '/əŋ/', pos: 'nasal final', def: '后鼻韵母eng', ex: "哼(hēng)\n风(fēng)" }
  ];

  for (const v of vocabData) {
    await connection.execute(
      `INSERT INTO edu_vocabulary (unit_id, word, language, pronunciation, part_of_speech, definition, example_sentences, del_flag) VALUES(?,?,?,?,?,?,?,?)`,
      [v.unit, v.word, v.lang, v.pron, v.pos, v.def, v.ex, '0']
    );
  }
  console.log(`Vocabulary: ${vocabData.length} inserted`);

  console.log('\n=== Verification ===');
  const [rows] = await connection.execute('SELECT course_id, course_name FROM edu_course LIMIT 5');
  console.log('Courses:', rows);

  await connection.end();
  console.log('\nSUCCESS! All data rebuilt with utf8mb4 + utf8mb4_0900_ai_ci');
}

rebuild().catch(err => {
  console.error('ERROR:', err.message);
  process.exit(1);
});
