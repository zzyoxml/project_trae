USE ryvue;
SET NAMES utf8mb4;

-- ============================================================
-- 多语种学习平台 - 完整示例数据
-- 每门课程：5+章节 / 单元 / 课时 / 词汇（含例句）
-- ============================================================

-- ==================== 课程1：英语口语入门 ====================
INSERT INTO edu_course_chapter (course_id, chapter_name, chapter_order, description, del_flag) VALUES
(1, '问候与自我介绍', 1, '学习基本的打招呼和介绍自己的表达方式', '0'),
(1, '家庭与朋友', 2, '描述家人和朋友，谈论人际关系', '0'),
(1, '日常活动', 3, '描述每天做的事情和日常习惯', '0'),
(1, '饮食与餐厅', 4, '在餐厅点餐、讨论食物', '0'),
(1, '购物与交通', 5, '购物对话、问路和交通工具', '0'),
(1, '旅行与住宿', 6, '旅行计划、酒店入住和观光', '0');

INSERT INTO edu_course_unit (course_id, chapter_id, unit_name, unit_order, description, total_lessons, total_duration, del_flag) VALUES
(1, 1, '基础问候语', 1, 'Hello, Hi, Good morning等基本问候', 2, 20, '0'),
(1, 1, '自我介绍', 2, '姓名、国籍、职业的自我介绍', 2, 25, '0'),
(1, 2, '家庭成员', 3, 'father, mother, brother等家庭成员词汇', 2, 20, '0'),
(1, 2, '描述人物', 4, '外貌特征和性格描述', 2, 25, '0'),
(1, 3, '日常作息', 5, '起床、吃饭、工作、睡觉的英文表达', 2, 20, '0'),
(1, 3, '兴趣爱好', 6, '运动、音乐、阅读等爱好表达', 2, 25, '0'),
(1, 4, '食物与饮料', 7, '常见食物和饮品的英文名称', 2, 20, '0'),
(1, 4, '餐厅点餐', 8, '在餐厅点餐和结账的实用对话', 2, 25, '0'),
(1, 5, '购物用语', 9, '询问价格、试穿、付款等购物场景', 2, 20, '0'),
(1, 5, '问路与交通', 10, '问路指路、乘坐公共交通的表达', 2, 25, '0'),
(1, 6, '旅行准备', 11, '订机票、打包行李、办理登机', 2, 20, '0'),
(1, 6, '酒店与观光', 12, '酒店入住、景点游览相关表达', 2, 25, '0');

INSERT INTO edu_course_lesson (unit_id, lesson_name, lesson_order, lesson_type, content, duration, is_free, del_flag) VALUES
-- 单元1: 基础问候语
(1, '常用问候语', 1, 'vocabulary', '{"words":[{"en":"Hello","zh":"你好"},{"en":"Hi","zh":"嗨"},{"en":"Good morning","zh":"早上好"},{"en":"Good afternoon","zh":"下午好"},{"en":"Good evening","zh":"晚上好"},{"en":"Good night","zh":"晚安"},{"en":"How are you?","zh":"你好吗？"},{"en":"I am fine, thank you","zh":"我很好，谢谢"}]}', 15, "1", '0'),
(1, '问候对话练习', 2, 'examples', '{"sentences":[{"sentence":"Hello! Nice to meet you.","translation":"你好！很高兴认识你。"},{"sentence":"Hi there! How are you doing today?","translation":"嗨！你今天怎么样？"},{"sentence":"Good morning, everyone! How was your weekend?","translation":"大家早上好！周末过得怎么样？"}]}', 15, "1", '0'),
-- 单元2: 自我介绍
(2, '个人信息词汇', 1, 'vocabulary', '{"words":[{"en":"name","zh":"名字"},{"en":"come from","zh":"来自"},{"en":"live in","zh":"住在"},{"en":"work as","zh":"从事...工作"},{"en":"student","zh":"学生"},{"en":"teacher","zh":"老师"},{"en":"engineer","zh":"工程师"},{"en":"doctor","zh":"医生"}]}', 15, "1", '0'),
(2, '自我介绍例句', 2, 'examples', '{"sentences":[{"sentence":"My name is Tom. I come from China.","translation":"我叫Tom。我来自中国。"},{"sentence":"I live in Beijing and I work as a software engineer.","translation":"我住在北京，是一名软件工程师。"},{"sentence":"I am a student at Peking University.","translation":"我是北京大学的学生。"}]}', 15, "1", '0'),
-- 单元3: 家庭成员
(3, '家庭成员称呼', 1, 'vocabulary', '{"words":[{"en":"father","zh":"父亲/爸爸"},{"en":"mother","zh":"母亲/妈妈"},{"en":"brother","zh":"兄弟"},{"en":"sister","zh":"姐妹"},{"en":"grandparents","zh":"祖父母"},{"en":"uncle","zh":"叔叔/舅舅"},{"en":"aunt","zh":"阿姨/姑姑"},{"en":"cousin","zh":"堂表兄弟姐妹"}]}', 15, "1", '0'),
(3, '介绍家人例句', 2, 'examples', '{"sentences":[{"sentence":"This is my father. He is a doctor.","translation":"这是我爸爸。他是一名医生。"},{"sentence":"I have a younger sister. She is very cute.","translation":"我有一个妹妹。她很可爱。"},{"sentence":"My grandparents live in the countryside with my uncle.","translation":"我的祖父母和我叔叔住在乡下。"}]}', 15, "1", '0'),
-- 单元4: 描述人物
(4, '外貌描写词汇', 1, 'vocabulary', '{"words":[{"en":"tall","zh":"高的"},{"en":"short","zh":"矮的"},{"en":"thin","zh":"瘦的"},{"en":"handsome","zh":"帅气的"},{"en":"beautiful","zh":"美丽的"},{"en":"young","zh":"年轻的"},{"en":"old","zh":"年长的"},{"en":"friendly","zh":"友好的"}]}', 15, "1", '0'),
(4, '描述人物例句', 2, 'examples', '{"sentences":[{"sentence":"My brother is tall and handsome. He plays basketball every day.","translation":"我哥哥又高又帅。他每天都打篮球。"},{"sentence":"She has long black hair and big eyes. She looks beautiful.","translation":"她有一头黑色长发和大眼睛。她看起来很漂亮。"},{"sentence":"My grandfather is old but very healthy. He walks in the park every morning.","translation":"我爷爷年纪大了但很健康。他每天早上在公园散步。"}]}', 15, "1", '0'),
-- 单元5: 日常作息
(5, '日常动词', 1, 'vocabulary', '{"words":[{"en":"wake up","zh":"醒来"},{"en":"get up","zh":"起床"},{"en":"have breakfast","zh":"吃早餐"},{"en":"go to work","zh":"去上班"},{"en":"have lunch","zh":"吃午餐"},{"en":"go home","zh":"回家"},{"en":"have dinner","zh":"吃晚餐"},{"en":"go to bed","zh":"上床睡觉"}]}', 15, "1", '0'),
(5, '日常作息例句', 2, 'examples', '{"sentences":[{"sentence":"I usually wake up at seven o\'clock in the morning.","translation":"我通常在早上七点醒来。"},{"sentence":"She has breakfast at home and then goes to work by subway.","translation":"她在家吃完早餐后乘地铁去上班。"},{"sentence":"After work, I go to the gym for an hour before going home.","translation":"下班后，我去健身房锻炼一小时然后回家。"}]}', 15, '1', '0'),
-- 单元6: 兴趣爱好
(6, '爱好类词汇', 1, 'vocabulary', '{"words":[{"en":"hobby","zh":"爱好"},{"en":"play basketball","zh":"打篮球"},{"en":"swim","zh":"游泳"},{"en":"read books","zh":"读书"},{"en":"listen to music","zh":"听音乐"},{"en":"watch movies","zh":"看电影"},{"en":"travel","zh":"旅行"},{"en":"cook","zh":"做饭"}]}', 15, "1", '0'),
(6, '谈论爱好例句', 2, 'examples', '{"sentences":[{"sentence":"What is your hobby? I like playing basketball on weekends.","translation":"你的爱好是什么？我喜欢在周末打篮球。"},{"sentence":"She enjoys reading books in her free time, especially novels.","translation":"她喜欢在空闲时间看书，尤其是小说。"},{"sentence":"We often go hiking together because we both love nature.","translation":"我们经常一起去徒步旅行，因为我们都热爱大自然。"}]}', 15, "1", '0'),
-- 单元7: 食物与饮料
(7, '食物词汇', 1, 'vocabulary', '{"words":[{"en":"hamburger","zh":"汉堡"},{"en":"pizza","zh":"披萨"},{"en":"noodles","zh":"面条"},{"en":"rice","zh":"米饭"},{"en":"chicken","zh":"鸡肉"},{"en":"beef","zh":"牛肉"},{"en":"vegetables","zh":"蔬菜"},{"en":"fruit","zh":"水果"}]}', 15, "1", '0'),
(7, '谈论食物例句', 2, 'examples', '{"sentences":[{"sentence":"I would like a hamburger and a glass of cola, please.","translation":"请给我一个汉堡和一杯可乐。"},{"sentence":"Chinese noodles are delicious. Have you ever tried them?","translation":"中国面条很好吃。你尝过吗？"},{"sentence":"She eats vegetables and fruit every day to stay healthy.","translation":"她每天吃蔬菜和水果来保持健康。"}]}', 15, "1", '0'),
-- 单元8: 餐厅点餐
(8, '餐厅用语', 1, 'vocabulary', '{"words":[{"en":"menu","zh":"菜单"},{"en":"order","zh":"点菜"},{"en":"bill","zh":"账单"},{"en":"waiter","zh":"服务员"},{"en":"recommendation","zh":"推荐"},{"en":"delicious","zh":"美味的"},{"en":"spicy","zh":"辣的"},{"en":"sweet","zh":"甜的"}]}', 15, "1", '0'),
(8, '餐厅点餐例句', 2, 'examples', '{"sentences":[{"sentence":"Excuse me, could I see the menu, please?","translation":"打扰一下，我可以看一下菜单吗？"},{"sentence":"What do you recommend? I would like something not too spicy.","translation":"你有什么推荐吗？我想点不太辣的。"},{"sentence":"Could I have the bill, please? We are ready to pay.","translation":"可以买单了吗？我们准备结账了。"}]}', 15, "1", '0'),
-- 单元9: 购物用语
(9, '购物词汇', 1, 'vocabulary', '{"words":[{"en":"price","zh":"价格"},{"en":"expensive","zh":"贵的"},{"en":"cheap","zh":"便宜的"},{"en":"discount","zh":"折扣"},{"en":"try on","zh":"试穿"},{"en":"size","zh":"尺码"},{"en":"color","zh":"颜色"},{"en":"receipt","zh":"收据"}]}', 15, "1", '0'),
(9, '购物对话例句', 2, 'examples', '{"sentences":[{"sentence":"How much is this shirt? It looks nice but a bit expensive.","translation":"这件衬衫多少钱？看起来不错但有点贵。"},{"sentence":"Can I try it on? Do you have this in size medium?","translation":"我可以试穿一下吗？这件有中号的吗？"},{"sentence":"Is there any discount today? I will take two if you give me ten percent off.","translation":"今天有打折吗？如果打九折我就买两件。"}]}', 15, "1", '0'),
-- 单元10: 问路与交通
(10, '方向与交通', 1, 'vocabulary', '{"words":[{"en":"turn left","zh":"左转"},{"en":"turn right","zh":"右转"},{"en":"go straight","zh":"直走"},{"en":"crossing","zh":"十字路口"},{"en":"subway","zh":"地铁"},{"en":"bus stop","zh":"公交车站"},{"en":"taxi","zh":"出租车"},{"en":"airport","zh":"机场"}]}', 15, "1", '0'),
(10, '问路交通例句', 2, 'examples', '{"sentences":[{"sentence":"Excuse me, how do I get to the nearest subway station?","translation":"请问，最近的地铁站怎么走？"},{"sentence":"Go straight ahead, then turn left at the second crossing.","translation":"一直往前走，然后在第二个路口左转。"},{"sentence":"You can take Bus No. 52. It takes about twenty minutes to get there.","translation："你可以坐52路公交车。到那里大约需要二十分钟。"}]}', 15, "1", '0'),
-- 单元11: 旅行准备
(11, '旅行词汇', 1, 'vocabulary', '{"words":[{"en":"passport","zh":"护照"},{"en":"luggage","zh":"行李"},{"en":"flight","zh":"航班"},{"en":"boarding pass","zh":"登机牌"},{"en":"hotel reservation","zh":"酒店预订"},{"en":"check in","zh":"办理入住"},{"en":"check out","zh":"退房"},{"en":"souvenir","zh":"纪念品"}]}', 15, "1", '0'),
(11, '旅行准备例句', 2, 'examples', '{"sentences":[{"sentence":"Do you have your passport and boarding pass ready?","translation":"你的护照和登机牌准备好了吗？"},{"sentence":"I need to check in my luggage. How many bags can I bring?","translation":"我需要托运行李。我可以带几件行李？"},{"sentence":"We booked a hotel near the city center for three nights.","translation":"我们在市中心附近预订了三晚的酒店。"}]}', 15, "1", '0'),
-- 单元12: 酒店与观光
(12, '酒店观光词汇', 1, 'vocabulary', '{"words":[{"en":"reception","zh":"前台"},{"en":"key card","zh":"房卡"},{"en":"tourist attraction","zh":"旅游景点"},{"en":"guidebook","zh":"旅游指南"},{"en":"map","zh":"地图"},{"en":"ticket","zh":"门票"},{"en":"museum","zh":"博物馆"},{"en":"sightseeing","zh":"观光游览"}]}', 15, "1", '0'),
(12, '酒店观光例句', 2, 'examples', '{"sentences":[{"sentence":"I would like to check in. I have a reservation under the name of Wang.","translation":"我要办理入住。我用王这个名字预订的。"},{"sentence":"Could you recommend some tourist attractions nearby?","translation":"你能推荐一些附近的旅游景点吗？"},{"sentence":"The museum is open from nine to five. The ticket costs fifteen dollars.","translation":"博物馆从九点到五点开放。门票十五美元。"}]}', 15, "1", '0');

-- 英语口语入门 的词汇数据 (unit_id 对应上面的单元)
INSERT INTO edu_vocabulary (unit_id, word, language, pronunciation, part_of_speech, definition, example_sentences, del_flag) VALUES
(1, 'hello', 'en', '/həˈloʊ/', 'interj.', '你好；喂（打电话或打招呼时用）', "{\"text\":\"Hello! Nice to meet you.\\\\\\\\\\\\\\\\nHello, may I speak to Mr. Smith?\"}", '0'),
(1, 'good morning', 'en', '/ɡʊd ˈmɔːrnɪŋ/', 'phrase', '早上好（上午问候语）', "{\"text\":\"Good morning, class!\\\\\\\\\\\\\\\\nGood morning! Did you sleep well?\"}", '0'),
(1, 'how are you', 'en', '/haʊ ɑːr juː/', 'phrase', '你好吗？（询问对方近况）', "{\"text\":\"How are you doing today?\\\\\\\\\\\\\\\\nHow are you? I am fine, thank you.\"}", '0'),
(1, 'nice to meet you', 'en', '/naɪs tuː miːt juː/', 'phrase', '很高兴认识你（初次见面时用）', "{\"text\":\"Nice to meet you, Sarah.\\\\\\\\\\\\\\\\nIt is nice to meet you too!\"}", '0'),
(1, 'goodbye', 'en', '/ɡʊdˈbaɪ/', 'interj.', '再见；告别', "{\"text\":\"Goodbye! See you tomorrow.\\\\\\\\\\\\\\\\nGoodbye and take care!\"}", '0'),
(1, 'thank you', 'en', '/θæŋk juː/', 'phrase', '谢谢你；感谢', "{\"text\":\"Thank you for your help.\\\\\\\\\\\\\\\\nThank you very much!\"}", '0'),
(2, 'name', 'en', '/neɪm/', 'n.', '名字；名称', "{\"text\":\"What is your name?\\\\\\\\\\\\\\\\nMy name is Alice.\"}", '0'),
(2, 'come from', 'en', '/kʌm frɒm/', 'phr. v.', '来自；出生于', "{\"text\":\"Where do you come from?\\\\\\\\\\\\\\\\nI come from Shanghai.\"}", '0'),
(2, 'student', 'en', '/ˈstuːdnt/', 'n.', '学生', "{\"text\":\"She is a university student.\\\\\\\\\\\\\\\\nHe is an exchange student from Japan.\"}", '0'),
(2, 'engineer', 'en', '/ˌendʒɪˈnɪr/', 'n.', '工程师', "{\"text\":\"My father works as an engineer.\\\\\\\\\\\\\\\\nShe is a software engineer at Google.\"}", '0'),
(3, 'father', 'en', '/ˈfɑːðər/', 'n.', '父亲；爸爸', "{\"text\":\"My father is a doctor.\\\\\\\\\\\\\\\\nI look like my father.\"}", '0'),
(3, 'mother', 'en', '/ˈmʌðər/', 'n.', '母亲；妈妈', "My mother cooks delicious food.\\nHappy Mother\\"s Day!', '0'),
(3, 'brother', 'en', '/ˈbrʌðər/', 'n.', '兄弟；弟兄', "{\"text\":\"I have one older brother.\\\\\\\\\\\\\\\\nMy brother and I play soccer together.\"}", '0'),
(3, 'sister', 'en', '/ˈsɪstər/', 'n.', '姐妹；姐妹', "{\"text\":\"My younger sister is very cute.\\\\\\\\\\\\\\\\nShe is like a sister to me.\"}", '0'),
(3, 'family', 'en', '/ˈfæmɪli/', 'n.', '家庭；家人', "{\"text\":\"I love my family very much.\\\\\\\\\\\\\\\\nWe had a family dinner last night.\"}", '0'),
(4, 'tall', 'en', '/tɔːl/', 'adj.', '高的（身材或物体）', "{\"text\":\"He is very tall, about six feet.\\\\\\\\\\\\\\\\nThe building is tall and modern.\"}", '0'),
(4, 'beautiful', 'en', '/ˈbjuːtɪfl/', 'adj.', '美丽的；漂亮的', "{\"text\":\"The view from here is beautiful.\\\\\\\\\\\\\\\\nShe looks beautiful in that dress.\"}", '0'),
(4, 'friendly', 'en', '/ˈfrendli/', 'adj.', '友好的；亲切的', "{\"text\":\"The people here are very friendly.\\\\\\\\\\\\\\\\nHe has a friendly smile.\"}", '0'),
(4, 'young', 'en', '/jʌŋ/', 'adj.', '年轻的；年少的', "{\"text\":\"She is young but very talented.\\\\\\\\\\\\\\\\nYoung people love this music.\"}", '0'),
(5, 'wake up', 'en', '/weɪk ʌp/', 'phr. v.', '醒来；醒过来', "{\"text\":\"I wake up at seven every day.\\\\\\\\\\\\\\\\nPlease wake me up at six tomorrow.\"}", '0'),
(5, 'breakfast', 'en', '/ˈbrekfəst/', 'n.', '早餐；早饭', "{\"text\":\"I always have breakfast at home.\\\\\\\\\\\\\\\\nBreakfast is the most important meal of the day.\"}", '0'),
(5, 'usually', 'en', '/ˈjuːʒuəli/', 'adv.', '通常；一般地', "{\"text\":\"I usually go to bed at eleven.\\\\\\\\\\\\\\\\nShe usually takes the bus to work.\"}", '0'),
(5, 'every day', 'en', '/ˈevri deɪ/', 'adv.', '每天；天天', "I exercise every day.\\nHe reads English every morning.", '0');


-- ==================== 课程5：英语日常会话 ====================
INSERT INTO edu_course_chapter (course_id, chapter_name, chapter_order, description, del_flag) VALUES
(5, '社交礼仪', 1, '礼貌用语、道歉、感谢等社交场景', '0'),
(5, '情感表达', 2, '喜怒哀乐等情绪的正确表达', '0'),
(5, '电话沟通', 3, '电话中的常用表达和技巧', '0'),
(5, '网络交流', 4, '邮件、社交媒体的英语表达', '0'),
(5, '职场英语', 5, '面试、会议、邮件等职场场景', '0'),
(5, '紧急情况', 6, '求救、报警、医院等紧急场景', '0');

INSERT INTO edu_course_unit (course_id, chapter_id, unit_name, unit_order, description, total_lessons, total_duration, del_flag) VALUES
(5, 1, '礼貌用语', 1, 'please, sorry, excuse me等礼貌表达', 2, 20, '0'),
(5, 1, '邀请与回应', 2, '发出邀请和接受/拒绝邀请', 2, 20, '0'),
(5, 2, '开心与兴奋', 3, '表达高兴、兴奋的情绪', 2, 20, '0'),
(5, 2, '难过与安慰', 4, '表达悲伤和安慰他人', 2, 20, '0'),
(5, 3, '打电话', 5, '电话接听、留言、挂断', 2, 20, '0'),
(5, 3, '语音信息', 6, '留语音信箱和听留言', 2, 20, '0'),
(5, 4, '写邮件', 7, '正式和非正式邮件写作', 2, 20, '0'),
(5, 4, '社交媒体', 8, 'Facebook, Instagram等平台用语', 2, 20, '0'),
(5, 5, '面试英语', 9, '自我介绍、回答面试问题', 2, 25, '0'),
(5, 5, '会议英语', 10, '开会发言、汇报工作', 2, 25, '0'),
(5, 6, '求助与报警', 11, 'Help! Call 911! 等紧急用语', 2, 20, '0'),
(5, 6, '看病就医', 12, '描述症状、看医生', 2, 25, '0');

INSERT INTO edu_course_lesson (unit_id, lesson_name, lesson_order, lesson_type, content, duration, is_free, del_flag) VALUES
(13, '礼貌用语词汇', 1, 'vocabulary', '{"words":[{"en":"please","zh":"请"},{"en":"thank you","zh":"谢谢"},{"en":"sorry","zh":"对不起"},{"en":"excuse me","zh":"打扰一下"},{"en":"you are welcome","zh":"不客气"},{"en":"no problem","zh":"没问题"}]}', 15, "1", '0'),
(13, '礼貌对话例句', 2, 'examples', '{"sentences":[{"sentence":"Excuse me, could you tell me where the restroom is?","translation":"打扰一下，请问洗手间在哪里？"},{"sentence":"I am so sorry for being late. It won\'t happen again.","translation":"非常抱歉迟到了。不会再发生这种事了。"},{"sentence":"Thank you so much for your help. You are welcome anytime.","translation":"非常感谢你的帮助。随时乐意效劳。"}]}', 15, '1', '0'),
(14, '邀请表达', 1, 'vocabulary', '{"words":[{"en":"would you like to","zh":"你想不想..."},{"en":"invite","zh":"邀请"},{"en":"join us","zh":"加入我们"},{"en":"party","zh":"聚会"},{"en":"celebrate","zh":"庆祝"},{"en":"accept","zh":"接受"}]}', 15, "1", '0'),
(14, '邀请对话例句', 2, 'examples', '{"sentences":[{"sentence":"Would you like to join us for dinner tonight?","translation":"今晚你要不要和我们一起吃晚饭？"},{"sentence":"I would love to, but I already have plans. Maybe next time!","translation":"我很想去，但我已经有安排了。下次吧！"},{"sentence":"We are having a birthday party on Saturday. You are invited!","translation":"周六我们要办生日派对。你也来吧！"}]}', 15, "1", '0'),
(15, '高兴表达', 1, 'vocabulary', '{"words":[{"en":"happy","zh":"开心的"},{"en":"excited","zh":"兴奋的"},{"en":"amazing","zh":"令人惊叹的"},{"en":"fantastic","zh":"极好的"},{"en":"wonderful","zh":"精彩的"},{"en":"can\'t wait","zh":"迫不及待"}]}', 15, '1', '0'),
(15, '高兴表达例句', 2, 'examples', '{"sentences":[{"sentence":"I am so happy to hear the good news! Congratulations!","translation":"听到这个好消息太开心了！恭喜！"},{"sentence":"This movie is absolutely amazing! You must watch it.","translation":"这部电影太棒了！你一定要看。"},{"sentence":"I can\'t wait for the summer vacation. We are going to Hawaii!","translation":"我迫不及待想放暑假了。我们要去夏威夷！"}]}', 15, '1', '0'),
(16, '安慰表达', 1, 'vocabulary', '{"words":[{"en":"sad","zh":"难过的"},{"en":"worried","zh":"担心的"},{"en":"don\'t worry","zh":"别担心"},{"en":"everything will be fine","zh":"一切都会好的"},{"en":"cheer up","zh":"振作起来"},{"en":"take care","zh":"保重"}]}', 15, '1', '0'),
(16, '安慰对话例句', 2, 'examples', '{"sentences":[{"sentence":"I heard about what happened. Are you okay?","translation":"听说发生了什么事。你还好吗？"},{"sentence":"Don\'t worry, everything will be fine. I am here for you.","translation":"别担心，一切都会好的。我在这里陪着你。"},{"sentence":"Cheer up! Tomorrow is another day. Things will get better.","translation":"振作起来！明天又是新的一天。情况会好转的。"}]}', 15, '1', '0'),
(17, '电话用语', 1, 'vocabulary', '{"words":[{"en":"May I speak to...","zh":"我可以和...说话吗"},{"en":"hold on","zh":"别挂断/稍等"},{"en":"wrong number","zh":"打错号码"},{"en":"leave a message","zh":"留个言"},{"en":"call back","zh":"回电话"},{"en":"hang up","zh":"挂断"}]}', 15, "1", '0'),
(17, '电话对话例句', 2, 'examples', '{"sentences":[{"sentence":"Hello, this is John speaking. May I speak to Mary, please?","translation":"你好，我是John。请问Mary在吗？"},{"sentence":"Hold on a moment, please. I will see if she is available.","translation":"请稍等。我看看她在不在。"},{"sentence":"Sorry, you have the wrong number. There is no one here by that name.","translation":"对不起，你打错电话了。这里没有这个人。"}]}', 15, "1", '0'),
(18, '语音留言', 1, 'vocabulary', '{"words":[{"en":"voice mail","zh":"语音信箱"},{"en":"message","zh":"消息"},{"en":"callback","zh":"回电"},{"en":"as soon as possible","zh":"尽快"},{"en":"urgent","zh":"紧急的"},{"en":"contact","zh":"联系"}]}', 15, "1", '0'),
(18, '留言对话例句', 2, 'examples', '{"sentences":[{"sentence":"Hi, this is Lisa. Please call me back when you get a chance.","translation":"嗨，我是Lisa。方便的时候请回个电话给我。"},{"sentence":"I left you a voice message. It is urgent, please call ASAP.","translation":"我给你留了语音留言。很急，请尽快回电。"},{"sentence":"Sorry I missed your call. What did you want to talk about?","translation":"抱歉错过了你的电话。你想说什么？"}]}', 15, "1", '0'),
(19, '邮件写作', 1, 'vocabulary', '{"words":[{"en":"subject","zh":"主题"},{"en":"attachment","zh":"附件"},{"en":"reply","zh":"回复"},{"en":"forward","zh":"转发"},{"en":"regards","zh":"致敬/此致"},{"en":"best wishes","zh":"最美好的祝愿"}]}', 15, "1", '0'),
(19, '邮件例句', 2, 'examples', '{"sentences":[{"subject":"Meeting Request","body":"Dear Mr. Zhang, I would like to schedule a meeting next week to discuss the project."},{"subject":"Re: Project Update","body":"Thank you for the update. Please find my comments attached."}]}', 15, "1", '0'),
(20, '社媒用语', 1, 'vocabulary', '{"words":[{"en":"post","zh":"发布"},{"en":"like","zh":"点赞"},{"en":"comment","zh":"评论"},{"en":"share","zh":"分享"},{"en":"follow","zh":"关注"},{"en":"hashtag","zh":"话题标签"}]}', 15, "1", '0'),
(20, '社媒例句', 2, 'examples', '{"sentences":[{"sentence":"Just posted new photos from my trip! Check them out! #travel #vacation","translation":"刚发了旅行的照片！快来看看！#旅行#度假"},{"sentence":"Thanks for all the likes and comments! You guys are amazing!","translation":"感谢所有的点赞和评论！你们太棒了！"},{"sentence":"Don\'t forget to follow me for more daily updates!","translation":"别忘了关注我获取更多每日更新！"}]}', 15, '1', '0'),
(21, '面试词汇', 1, 'vocabulary', '{"words":[{"en":"resume","zh":"简历"},{"en":"experience","zh":"经验"},{"en":"strength","zh":"优势"},{"en":"weakness","zh":"弱点"},{"en":"salary","zh":"薪水"},{"en":"position","zh":"职位"}]}', 15, "1", '0'),
(21, '面试例句', 2, 'examples', '{"sentences":[{"sentence":"Tell me about yourself. I have five years of experience in marketing.","translation":"介绍一下你自己。我有五年的市场营销经验。"},{"sentence":"What is your greatest strength? I am good at working in a team.","translation":"你最大的优势是什么？我擅长团队合作。"},{"sentence":"Why do you want this job? Because I believe in this company\'s mission.","translation":"为什么想要这份工作？因为我认同这家公司的使命。"}]}', 15, '1', '0'),
(22, '会议词汇', 1, 'vocabulary', '{"words":[{"en":"agenda","zh":"议程"},{"en":"minutes","zh":"会议记录"},{"en":"action item","zh":"行动事项"},{"en":"deadline","zh":"截止日期"},{"en":"presentation","zh":"演示"},{"en":"feedback","zh":"反馈"}]}', 15, "1", '0'),
(22, '会议例句', 2, 'examples', '{"sentences":[{"sentence":"Let us start with the first item on the agenda.","translation":"我们从议程上的第一项开始吧。"},{"sentence":"Could you share your thoughts on this proposal?","translation":"你能分享一下对这个提案的想法吗？"},{"sentence":"Let us summarize the action items before we finish.","translation":"我们在结束之前总结一下行动事项吧。"}]}', 15, "1", '0'),
(23, '紧急用语', 1, 'vocabulary', '{"words":[{"en":"help!","zh":"救命！"},{"en":"emergency","zh":"紧急情况"},{"en":"ambulance","zh":"救护车"},{"en":"police","zh":"警察"},{"en":"fire!","zh":"着火了！"},{"en":"dangerous","zh":"危险的"}]}', 15, "1", '0'),
(23, '紧急例句', 2, 'examples', '{"sentences":[{"sentence":"Help! Someone call an ambulance! He is not breathing!","translation":"救命！快叫救护车！他不能呼吸了！"},{"sentence":"There is a fire! Everyone needs to evacuate immediately!","translation":"着火了！所有人需要立即疏散！"},{"sentence":"Be careful! This area is dangerous. Do not go near it.","translation":"小心！这个区域很危险。不要靠近它。"}]}', 15, "1", '0'),
(24, '就医词汇', 1, 'vocabulary', '{"words":[{"en":"fever","zh":"发烧"},{"en":"cough","zh":"咳嗽"},{"en":"headache","zh":"头痛"},{"en":"stomachache","zh":"胃痛"},{"en":"medicine","zh":"药"},{"en":"prescription","zh":"处方"}]}', 15, "1", '0'),
(24, '就医例句', 2, 'examples', '{"sentences":[{"sentence":"Doctor, I have had a fever and a cough for three days now.","translation":"医生，我已经发烧咳嗽三天了。"},{"sentence":"My head hurts really bad. Could you give me something for the pain?","translation":"我头疼得厉害。能给我开点止痛药吗？"},{"sentence":"Take this medicine twice a day after meals. You should feel better soon.","translation":"这个药一天两次饭后服用。你应该很快就会好起来的。"}]}', 15, "1", '0');

-- 英语日常会话 的词汇数据
INSERT INTO edu_vocabulary (unit_id, word, language, pronunciation, part_of_speech, definition, example_sentences, del_flag) VALUES
(13, 'excuse me', 'en', '/ɪkˈskjuːz miː/', 'phrase', '对不起；打扰一下（引起注意或表示歉意）', "{\"text\":\"Excuse me, where is the bathroom?\\\\\\\\\\\\\\\\nExcuse me for being late.\"}", '0'),
(13, 'apologize', 'en', '/əˈpɒlədʒaɪz/', 'v.', '道歉；谢罪', "{\"text\":\"I sincerely apologize for the mistake.\\\\\\\\\\\\\\\\nYou should apologize to her.\"}", '0'),
(13, 'welcome', 'en', '/ˈwelkəm/', 'adj./v./interj.', '受欢迎的；欢迎；不客气', "{\"text\":\"You are always welcome here.\\\\\\\\\\\\\\\\n- Thank you. - You are welcome.\"}", '0'),
(14, 'invite', 'en', '/ɪnˈvaɪt/', 'v.', '邀请；招待', "{\"text\":\"She invited me to her birthday party.\\\\\\\\\\\\\\\\nThanks for inviting me.\"}", '0'),
(14, 'celebration', 'en', '/ˌselɪˈbreɪʃn/', 'n.', '庆祝；庆典', "{\"text\":\"We had a big celebration for New Year.\\\\\\\\\\\\\\\\nThe party was a great celebration.\"}", '0'),
(15, 'amazing', 'en', '/əˈmeɪzɪŋ/', 'adj.', '令人惊叹的；了不起的', "{\"text\":\"The view from the top is amazing.\\\\\\\\\\\\\\\\nThat is an amazing idea!\"}", '0'),
(16, 'worried', 'en', '/ˈwʌrid/', 'adj.', '担心的；焦虑的', "{\"text\":\"Do not be worried about the exam.\\\\\\\\\\\\\\\\nShe looked worried when she heard the news.\"}", '0'),
(17, 'hold on', 'en', '/hoʊld ɒn/', 'phr. v.', '稍等；别挂断；坚持住', "{\"text\":\"Hold on, let me get a pen.\\\\\\\\\\\\\\\\nHold on tight, the bus is moving fast!\"}", '0'),
(18, 'urgent', 'en', '/ˈɜːrdʒənt/', 'adj.', '紧急的；急迫的', "{\"text\":\"This is an urgent matter.\\\\\\\\\\\\\\\\nI need to talk to you urgently.\"}", '0'),
(19, 'attachment', 'en', '/əˈtætʃmənt/', 'n.', '附件；（电子邮件的）附件', "{\"text\":\"Please find the report attached.\\\\\\\\\\\\\\\\nI forgot to add the attachment to the email.\"}", '0'),
(20, 'share', 'en', '/ʃeər/', 'v.', '分享；共享', "{\"text\":\"Can you share your notes with me?\\\\\\\\\\\\\\\\nI shared the post on my timeline.\"}", '0'),
(21, 'strength', 'en', '/streŋθ/', 'n.', '优势；力量；长处', "{\"text\":\"Her greatest strength is patience.\\\\\\\\\\\\\\\\nKnowledge is strength.\"}", '0'),
(22, 'deadline', 'en', '/ˈdedlaɪn/', 'n.', '截止日期；最后期限', "{\"text\":\"The deadline for the project is Friday.\\\\\\\\\\\\\\\\nWe need to meet the deadline.\"}", '0'),
(23, 'ambulance', 'en', '/ˈæmbjələns/', 'n.', '救护车', "{\"text\":\"Call an ambulance immediately!\\\\\\\\\\\\\\\\nThe ambulance arrived within five minutes.\"}", '0'),
(24, 'prescription', 'en', '/prɪˈskrɪpʃn/', 'n.', '处方；药方', "The doctor wrote a prescription for me.\\nYou need a prescription to buy this medicine.", '0');


-- ==================== 课程6：日语五十音图 ====================
INSERT INTO edu_course_chapter (course_id, chapter_name, chapter_order, description, del_flag) VALUES
(6, '清音（行）', 1, 'あ行～わ行的基本假名发音', '0'),
(6, '浊音与半浊音', 2, 'が行、ぱ行等变形发音', '0'),
(6, '拗音与促音', 3, '特殊组合发音规则', '0'),
(6, '基础单词练习', 4, '用五十音拼读简单单词', '0'),
(6, '日常短句', 5, '用假名组成的基本句子', '0'),
(6, '听力辨音', 6, '区分相似假名的发音', '0');

INSERT INTO edu_course_unit (course_id, chapter_id, unit_name, unit_order, description, total_lessons, total_duration, del_flag) VALUES
(6, 1, 'あ行假名', 1, 'あいうえお 五个元音', 2, 30, '0'),
(6, 1, 'か行假名', 2, 'かきくけこ 辅音k+元音', 2, 30, '0'),
(6, 1, 'さ行假名', 3, 'さしすせそ 辅音s+元音', 2, 30, '0'),
(6, 1, 'た行假名', 4, 'たちつてと 辅音t+元音', 2, 30, '0'),
(6, 1, 'な行假名', 5, 'なにぬねの 辅音n+元音', 2, 30, '0'),
(6, 2, 'が行浊音', 6, 'がぎぐげご 浊音g系列', 2, 30, '0'),
(6, 2, 'ぱ行半浊音', 7, 'ぱぷぺぽ 半浊音p系列', 2, 30, '0'),
(6, 3, '拗音规则', 8, 'きゃきゅきょ 等拗音组合', 2, 30, '0'),
(6, 3, '促音与长音', 9, 'っ和ー的特殊发音规则', 2, 30, '0'),
(6, 4, '数字与时间', 10, '数字、星期、月份的日语说法', 2, 30, '0'),
(6, 4, '颜色与方位', 11, '颜色词和方向词', 2, 30, '0'),
(6, 5, '基本问候', 12, 'こんにちは、ありがとう等', 2, 30, '0'),
(6, 5, '自我介绍', 13, '初対面の自己紹介', 2, 30, '0'),
(6, 6, '相似音对比', 14, '容易混淆的假名辨析', 2, 30, '0'),
(6, 6, '综合听写', 15, '完整句子听写训练', 2, 30, '0');

INSERT INTO edu_course_lesson (unit_id, lesson_name, lesson_order, lesson_type, content, duration, is_free, del_flag) VALUES
(25, 'あ行五个元音', 1, 'vocabulary', '{"words":[{"ja":"あ(a)","zh":"啊"},{"ja":"い(i)","zh":"伊"},{"ja":"う(u)","zh":"乌"},{"ja":"え(e)","zh":"诶"},{"ja":"お(o)","zh":"欧"}]}', 20, "1", '0'),
(25, 'あ行例句', 2, 'examples', '{"sentences":[{"sentence":"あおい空です。","translation":"是蓝色的天空。"},{"sentence":"いい天気ですね。","translation":"天气真好啊。"},{"sentence":"おはようございます。","translation":"早上好。"}]}', 20, "1", '0'),
(26, 'か行清音', 1, 'vocabulary', '{"words":[{"ja":"か(ka)","zh":"卡"},{"ja":"き(ki)","zh":"ki"},{"ja":"く(ku)","zh":"ku"},{"ja":"け(ke)","zh":"ke"},{"ja":"こ(ko)","zh":"ko"}]}', 20, "1", '0'),
(26, 'か行例句', 2, 'examples', '{"sentences":[{"sentence":"ここはどこですか。","translation":"这里是哪里？"},{"ja":"きのうは楽しかったです。","zh":"昨天很开心。"},{"ja":"くろい猫ですね。","zh":"黑色的猫呢。"}]}', 20, "1", '0'),
(27, 'さ行清音', 1, 'vocabulary', '{"words":[{"ja":"さ(sa)","zh":"sa"},{"ja":"し(shi)","zh":"xi"},{"ja":"す(su)","zh":"su"},{"ja":"せ(se)","zh":"se"},{"ja":"そ(so)","zh":"so"}]}', 20, "1", '0'),
(27, 'さ行例句', 2, 'examples', '{"sentences":[{"sentence":"さようなら、また明日。","translation":"再见，明天见。"},{"sentence":"しずかにしてください。","translation":"请安静一点。"},{"sentence":"すごいですね！","translation":"太厉害了！"}]}', 20, "1", '0'),
(28, 'た行清音', 1, 'vocabulary', '{"words":[{"ja":"た(ta)","zh":"ta"},{"ja":"ち(chi)","zh":"qi"},{"ja":"つ(tsu)","zh":"ci"},{"ja":"て(te)","zh":"te"},{"ja":"と(to)","zh":"to"}]}', 20, "1", '0'),
(28, 'た行例句', 2, 'examples', '{"sentences":[{"sentence":"たべものはおいしいです。","translation":"食物很好吃。"},{"sentence":"ちちと一緒に行きます。","translation":"和爸爸一起去。"},{"sentence":"とても楽しかった。","translation":"非常开心。"}]}', 20, "1", '0'),
(29, 'な行清音', 1, 'vocabulary', '{"words":[{"ja":"な(na)","zh":"na"},{"ja":"に(ni)","zh":"ni"},{"ja":"ぬ(nu)","zh":"nu"},{"ja":"ね(ne)","zh":"ne"},{"ja":"の(no)","zh":"no"}]}', 20, "1", '0'),
(29, 'な行例句', 2, 'examples', '{"sentences":[{"sentence":"あなたのお名前は何ですか。","translation":"您叫什么名字？"},{"sentence":"日本に行ったことがあります。","translation":"去过日本。"},{"sentence":"これは私の本です。","translation":"这是我的书。"}]}', 20, "1", '0'),
(30, 'が行浊音', 1, 'vocabulary', '{"words":[{"ja":"が(ga)","zh":"ga"},{"ja":"ぎ(gi)","zh":"gi"},{"ja":"ぐ(gu)","zh":"gu"},{"ja":"げ(ge)","zh":"ge"},{"ja":"ご(go)","zh":"go"}]}', 20, "1", '0'),
(30, 'が行例句', 2, 'examples', '{"sentences":[{"sentence":"学校が大きいです。","translation":"学校很大。"},{"sentence":"ぎんこうへ行きたいです。","translation":"想去银行。"},{"sentence":"ご飯を食べましょう。","translation":"一起吃饭吧。"}]}', 20, "1", '0'),
(31, 'ぱ行半浊音', 1, 'vocabulary', '{"words":[{"ja":"ぱ(pa)","zh":"pa"},{"ja":"ぷ(pu)","zh":"pu"},{"ja":"ぺ(pe)","zh":"pe"},{"ja":"ぽ(po)","zh":"po"}]}', 20, "1", '0'),
(31, 'ぱ行例句', 2, 'examples', '{"sentences":[{"sentence":"パパは会社員です。","translation":"爸爸是公司职员。"},{"sentence":"プールで泳ぎます。","translation":"在游泳池游泳。"},{"sentence":"ペンを貸してください。","translation":"请借我一支笔。"}]}', 20, "1", '0'),
(32, '拗音组合', 1, 'vocabulary', '{"words":[{"ja":"きゃ(kya)","zh":"kya"},{"ja":"きゅ(kyu)","zh":"kyu"},{"ja":"きょ(kyo)","zh":"kyo"},{"ja":"しゃ(sha)","zh":"xia"},{"ja":"しゅ(shu)","zh":"xiu"}]}', 20, "1", '0'),
(32, '拗音例句', 2, 'examples', '{"sentences":[{"sentence":"今日は暑いですね。","translation":"今天真热啊。"},{"sentence":"去年の夏休みは楽しかった。","translation":"去年的暑假很开心。"},{"sentence":"写真を撮りましょう。","translation":"一起拍照吧。"}]}', 20, "1", '0'),
(33, '促音长音', 1, 'vocabulary', '{"words":[{"ja":"促音(っ)","zh":"小写tsu，停顿一拍"},{"ja":"長音(ー)","zh":"拉长前面假名的音"}]}', 20, "1", '0'),
(33, '促音例句', 2, 'examples', '{"sentences":[{"sentence":"ちょっと待ってください。","translation":"请稍等一下。"},{"sentence":"学校へ行きます。","translation":"去学校。"},{"sentence":"おばあさんは元気です。","translation":"奶奶身体很好。"}]}', 20, "1", '0'),
(34, '数字时间', 1, 'vocabulary', '{"words":[{"ja":"いち(1)","zh":"一"},{"ja":"に(2)","zh":"二"},{"ja":"さん(3)","zh":"三"},{"ja":"月曜日","zh":"星期一"},{"ja":"今","zh":"现在"}]}', 20, "1", '0'),
(34, '数字例句', 2, 'examples', '{"sentences":[{"sentence":"今何時ですか。三時です。","translation":"现在几点？三点。"},{"sentence":"月曜日にテストがあります。","translation":"周一有考试。"},{"sentence":"三時半に会いましょう。","translation":"三点半见面吧。"}]}', 20, "1", '0'),
(35, '颜色方位', 1, 'vocabulary', '{"words":[{"ja":"あか(赤)","zh":"红色"},{"ja":"あお(青)","zh":"蓝色"},{"ja":"みぎ(右)","zh":"右边"},{"ja":"ひだり(左)","zh":"左边"}]}', 20, "1", '0'),
(35, '颜色例句', 2, 'examples', '{"sentences":[{"sentence":"赤いリンゴが好きです。","translation":"我喜欢红苹果。"},{"sentence":"青い空がきれいですね。","translation":"蓝天真漂亮。"},{"sentence":"右に曲がってください。","translation":"请向右转。"}]}', 20, "1", '0'),
(36, '基本问候', 1, 'vocabulary', '{"words":[{"ja":"こんにちは","zh":"你好"},{"ja":"ありがとう","zh":"谢谢"},{"ja":"すみません","zh":"不好意思"},{"ja":"はじめまして","zh":"初次见面"}]}', 20, "1", '0'),
(36, '问候例句', 2, 'examples', '{"sentences":[{"sentence":"こんにちは、田中です。","translation":"你好，我是田中。"},{"sentence":"ありがとうございます。","translation":"非常感谢。"},{"sentence":"はじめまして、どうぞよろしく。","translation":"初次见面，请多关照。"}]}', 20, "1", '0'),
(37, '自我介绍', 1, 'vocabulary', '{"words":[{"ja":"名前","zh":"名字"},{"ja":"出身","zh":"出身地"},{"ja":"職業","zh":"职业"},{"ja":"趣味","zh":"兴趣"}]}', 20, "1", '0'),
(37, '自我介绍例句', 2, 'examples', '{"sentences":[{"sentence":"私の名前は山田太郎です。","translation":"我的名字是山田太郎。"},{"sentence":"東京の出身です。","translation":"我来自东京。"},{"sentence":"趣味は読書と旅行です。","translation":"我的兴趣是读书和旅行。"}]}', 20, "1", '0'),
(38, '相似音辨析', 1, 'vocabulary', '{"words":[{"ja":"ぬ( nu ) vs の( no )","zh":"nu vs no 容易混淆"},{"ja":"ち( chi ) vs て( te )","zh":"chi vs te 注意区分"},{"ja":"ふ( fu ) vs ひ( hi )","zh":"fu vs hi 嘴唇形状不同"}]}', 20, "1", '0'),
(38, '辨音例句', 2, 'examples', '{"sentences":[{"sentence":"犬(いぬ)がいる。猫(ねこ)もいる。","translation":"有狗。也有猫。"},{"sentence":"血(ち)が出た。手(て)を洗って。","translation":"出血了。把手洗一下。"},{"sentence":"冬(ふゆ)は寒い。火(ひ)をつけて。","translation":"冬天很冷。把火点上。"}]}', 20, "1", '0'),
(39, '综合听写', 1, 'vocabulary', '{"words":[{"ja":"毎日","zh":"每天"},{"ja":"勉強します","zh":"学习"},{"ja":"日本語","zh":"日语"},{"ja":"話します","zh":"说/讲"}]}', 20, "1", '0'),
(39, '综合例句', 2, 'examples', '{"sentences":[{"sentence":"毎日日本語を勉強しています。","translation":"每天都在学日语。"},{"sentence":"日本人の友達ができました。","translation":"交到了日本朋友。"},{"sentence":"来年日本へ行きたいです。","translation":"明年想去日本。"}]}', 20, "1", '0');

-- 日语五十音图 的词汇数据
INSERT INTO edu_vocabulary (unit_id, word, language, pronunciation, part_of_speech, definition, example_sentences, del_flag) VALUES
(25, 'あ', 'ja', '/a/', 'hiragana', '平假名あ，对应片假名ア，发音/a/，类似中文"啊"', "{\"text\":\"あおい空（蓝天）\\\\\\\\\\\\\\\\nありがとう（谢谢）\"}", '0'),
(25, 'い', 'ja', '/i/', 'hiragana', '平假名い，对应片假名イ，发音/i/，类似中文"伊"', "{\"text\":\"いいえ（不）\\\\\\\\\\\\\\\\nいち（一）\"}", '0'),
(25, 'う', 'ja', '/u:/ or /ɯ/', 'hiragana', '平假名う，对应片假名ウ，发音/u/，嘴唇收圆', "{\"text\":\"うえ（上面）\\\\\\\\\\\\\\\\nうし（牛）\"}", '0'),
(26, 'か', 'ja', '/ka/', 'hiragana', '平假名か，清音ka，加浊点变为が(ga)', "{\"text\":\"かぞく（家族）\\\\\\\\\\\\\\\\nかいもの（购物）\"}", '0'),
(26, 'き', 'ja', '/kʲi/', 'hiragana', '平假名き，发音接近ki但腭化', "{\"text\":\"きょうと（京都）\\\\\\\\\\\\\\\\nきた（北边）\"}", '0'),
(27, 'さ', 'ja', '/sa/', 'hiragana', '平假名さ，清音sa，加浊点变为ざ(za)', "{\"text\":\"さようなら（再见）\\\\\\\\\\\\\\\\nさむい（寒冷）\"}", '0'),
(27, 'し', 'ja', '/ɕi/', 'hiragana', '平假名し，发音为/shi/而非si', "{\"text\":\"しずか（安静的）\\\\\\\\\\\\\\\\nしごと（工作）\"}", '0'),
(28, 'た', 'ja', '/ta/', 'hiragana', '平假名た，清音ta', "{\"text\":\"たべる（吃）\\\\\\\\\\\\\\\\nたいへん（不得了）\"}", '0'),
(28, 'ち', 'ja', '/tɕi/', 'hiragana', '平假名ち，发音为/tɕi/类似中文"七"', "{\"text\":\"ちいさい（小的）\\\\\\\\\\\\\\\\nちず（地图）\"}", '0'),
(29, 'な', 'ja', '/na/', 'hiragana', '平假名な，鼻音n+a', "{\"text\":\"なまえ（名字）\\\\\\\\\\\\\\\\nなつ（夏天）\"}", '0'),
(29, 'に', 'ja', '/ɲi/', 'hiragana', '平假名に，鼻音ni', "{\"text\":\"にほん（日本）\\\\\\\\\\\\\\\\nにちようび（周日）\"}", '0'),
(30, 'が', 'ja', '/ɡa/', 'hiragana', '平假名が，か的浊音版本', "{\"text\":\"がっこう（学校）\\\\\\\\\\\\\\\\nがいこく（外国）\"}", '0'),
(31, 'ぱ', 'ja', '/pa/', 'hiragana', '平假名ぱ，半浊音pa，圆唇送气', "{\"text\":\"ぱぱ（爸爸）\\\\\\\\\\\\\\\\nぱん（面包/盘子）\"}", '0'),
(32, 'きゃ', 'ja', '/kʲa/', 'hiragana', '拗音きゃ=き+や，一拍发音', "{\"text\":\"きゃく（客人）\\\\\\\\\\\\\\\\nきゃんぷ（露营）\"}", '0'),
(32, 'しゅ', 'ja', '/ɕɯ/', 'hiragana', '拗音しゅ=し+ゆ', "{\"text\":\"しゅみ（趣味）\\\\\\\\\\\\\\\\nしゅくだち（作业）\"}", '0'),
(33, '促音', 'ja', '/Q (glottal stop)/', 'grammar', '小写っ表示停顿一拍', "{\"text\":\"ちょっと（稍微）\\\\\\\\\\\\\\\\nかった（买了）\"}", '0'),
(34, 'いち', 'ja', '/itɕi/', 'numeral', '数字1', "{\"text\":\"いちがつ（一月）\\\\\\\\\\\\\\\\nいちじ（一点钟）\"}", '0'),
(35, 'あか', 'ja', '/aka/', 'adj./n.', '红色；红色的', "{\"text\":\"あかいリンゴ（红苹果）\\\\\\\\\\\\\\\\nあかちゃん（婴儿）\"}", '0'),
(36, 'こんにちは', 'ja', '/kon.nichi.wa/', 'greeting', '白天使用的问候语（约11点~18点）', "{\"text\":\"こんにちは、田中さん。（你好，田中先生。）\\\\\\\\\\\\\\\\nこんにちは、お久しぶりです。（好久不见。）\"}", '0'),
(37, '名前', 'ja', '/namae/', 'n.', '名字；姓名', "{\"text\":\"お名前は何ですか。（您叫什么名字？）\\\\\\\\\\\\\\\\n私の名前は山田です。（我叫山田。）\"}", '0'),
(38, 'ぬ vs の', 'ja', '/nu/ vs /no/', 'pair', '容易混淆的一对假名', "ぬるい（温吞的）\\nのだから（所以嘛）", '0');


-- ==================== 课程8：日语会话中级 ====================
INSERT INTO edu_course_chapter (course_id, chapter_name, chapter_order, description, del_flag) VALUES
(8, '职场日语', 1, '公司内的敬语和商务表达', '0'),
(8, '旅行实用', 2, '在日本旅游的各种场景对话', '0'),
(8, '购物与饮食', 3, '在商店、餐厅、超市的对话', '0'),
(8, '租房与生活', 4, '找房子、搬家、日常生活交流', '0'),
(8, '社交与约会', 5, '和朋友聚会、约会的自然表达', '0'),
(8, '健康与医疗', 6, '看医生、描述症状、药店买药', '0');

INSERT INTO edu_course_unit (course_id, chapter_id, unit_name, unit_order, description, total_lessons, total_duration, del_flag) VALUES
(8, 1, '自我介绍（敬语）', 1, '公司内正式自我介绍方式', 2, 30, '0'),
(8, 1, '邮件与报告', 2, '写商务邮件和工作汇报', 2, 30, '0'),
(8, 2, '交通出行', 3, '新干线、地铁、出租车场景', 2, 30, '0'),
(8, 2, '观光问询', 4, '在景点问路和咨询信息', 2, 30, '0'),
(8, 3, '餐厅点餐', 5, '日式餐厅点餐礼仪和用语', 2, 30, '0'),
(8, 3, '便利店购物', 6, '在コンビニ买东西的表达', 2, 30, '0'),
(8, 4, '找房看房', 7, '不动产公司和看房时的对话', 2, 30, '0'),
(8, 4, '邻居交往', 8, '和邻居打招呼、借东西等', 2, 30, '0'),
(8, 5, '邀请朋友', 9, '邀请朋友吃饭、出去玩', 2, 30, '0'),
(8, 5, '聊天话题', 10, '天气、兴趣、新闻等闲聊内容', 2, 30, '0'),
(8, 6, '描述症状', 11, '向医生说明身体不舒服的地方', 2, 30, '0'),
(8, 6, '药店买药', 12, '在薬局买药和说明需求', 2, 30, '0');

INSERT INTO edu_course_lesson (unit_id, lesson_name, lesson_order, lesson_type, content, duration, is_free, del_flag) VALUES
(40, '商务敬语词汇', 1, 'vocabulary', '{"words":[{"ja":"初めまして","zh":"初次见面"},{" ja ":" 申し遅れました "," zh ":" 抱歉来晚了 "},{" ja ":" よろしくお願いします "," zh ":" 请多关照 "},{" ja ":" お疲れ様です "," zh ":" 辛苦了 "},{" ja ":" 恐れ入りますが "," zh ":" 冒昧打扰了 "}]}', 20, "1", '0'),
(40, '商务敬语例句', 2, 'examples', '{"sentences":[{"sentence":"初めまして、〇〇と申します。本日はよろしくお願いいたします。","translation":"初次见面，我叫XX。今天请多关照。"},{"sentence":"お忙しいところ恐れ入りますが、ご確認をお願いできますでしょうか。","translation":"百忙之中冒昧打扰，能否请您确认一下？"},{"sentence":"お疲れ様でした。今日もお仕事お疲れさまでした。","translation":"辛苦了。今天也辛苦工作了。"}]}', 20, "1", '0'),
(41, '邮件写作', 1, 'vocabulary', '{"words":[{" ja ":" 拝啓 "," zh ":" 敬启者 "},{" ja ":" 記 "," zh ":" 关于/以下 "},{" ja ":" 以上 "," zh ":" 以上/完毕 "},{" ja ":" 宜しくお願い申し上げます "," zh ":" 拜托了（更郑重） "}]}', 20, "1", '0'),
(41, '邮件例句', 2, 'examples', '{"sentences":[{"subject":"会議日程の件","body":"〇〇様\nいつもお世話になっております。\n来週の会議日程についてご連絡いたしました。"},{"subject":"Re: プロジェクト進捗報告","body":"お疲れ様です。先週の進捗を添付いたします。ご確認のほど宜しくお願いいたします。"}]}', 20, "1", '0'),
(42, '交通词汇', 1, 'vocabulary', '{"words":[{" ja ":" 新幹線 "," zh ":" 新干线 "},{" ja ":" 切符 "," zh ":" 票 "},{" ja ":" 改札口 "," zh ":" 检票口 "},{" ja ":" 乗り換え "," zh ":" 换乘 "},{" ja ":" 遅れます "," zh ":" 迟到/晚点 "}]}', 20, "1", '0'),
(42, '交通例句', 2, 'examples', '{"sentences":[{"sentence":"東京駅まで新幹線でどのくらいかかりますか。","translation":"坐新干线到东京站要多久？"},{"sentence":"すみません、この電車は東京行きですか。","translation":"请问这趟车是去东京的吗？"},{"sentence":"次は何時の電車がありますか。","translation":"下一班电车是几点？"}]}', 20, "1", '0'),
(43, '观光问路', 1, 'vocabulary', '{"words":[{" ja ":" 観光案内所 "," zh ":" 旅游咨询处 "},{" ja ":" 地図 "," zh ":" 地图 "},{" ja ":" 近くにありますか "," zh ":" 在附近吗 "},{" ja ":" 歩いて何分 "," zh ":" 走路几分钟 "}]}', 20, "1", '0'),
(43, '观光例句', 2, 'examples', '{"sentences":[{"sentence":"すみません、浅草寺への行き方を教えていただけますか。","translation":"请问能告诉我去浅草寺怎么走吗？"},{"sentence":"ここから歩いてどのくらいかかりますか。","translation":"从这里走大概要多长时间？"},{"sentence":"観光案内所はどこにありますか。","translation":"旅游咨询处在什么地方？"}]}', 20, "1", '0'),
(44, '餐厅词汇', 1, 'vocabulary', '{"words":[{" ja ":" ご注文 "," zh ":" 点菜 "},{" ja ":" おすすめ "," zh ":" 推荐 "},{" ja ":" 会計 "," zh ":" 结账 "},{" ja ":" 別々で "," zh ":" 分开付（AA制） "}]}', 20, "1", '0'),
(44, '餐厅例句', 2, 'examples', '{"sentences":[{"sentence":"すみません、注文をお願いします。","translation":"不好意思，我要点菜。"},{"sentence":"おすすめメニューは何ですか。","translation":"有什么推荐的菜品吗？"},{"sentence":"お会計をお願いします。別々でお願いします。","translation":"请结账。分开付吧。"}]}', 20, "1", '0'),
(45, '便利店词汇', 1, 'vocabulary', '{"words":[{" ja ":" レジ "," zh ":" 收银台 "},{" ja ":" 弁当 "," zh ":" 便当 "},{" ja ":" おにぎり "," zh ":" 饭团 "},{" ja ":" ポイントカード "," zh ":" 积分卡 "}]}', 20, "1", '0'),
(45, '便利店例句', 2, 'examples', '{"sentences":[{"sentence":"これをください。レジはどこですか。","translation":"请给我这个。收银台在哪里？"},{"sentence":"温めてもらえますか。","translation":"能帮我加热一下吗？"},{"sentence":"ポイントカードは使えますか。","translation":"可以用积分卡吗？"}]}', 20, "1", '0'),
(46, '租房词汇', 1, 'vocabulary', '{"words":[{" ja ":" 不動産屋 "," zh ":" 房产中介 "},{" ja ":" 家賃 "," zh ":" 房租 "},{" ja ":" 敷金 "," zh ":" 押金 "},{" ja ":" 礼金 "," zh ":" 礼金（感谢费） "},{" ja ":" 引っ越し "," zh ":" 搬家 "}]}', 20, "1", '0'),
(46, '租房例句', 2, 'examples', '{"sentences":[{"sentence":"この部屋を見てもいいですか。","translation":"可以看一下这个房间吗？"},{"sentence":"家賃はいくらですか。光熱費は含まれていますか。","translation":"房租多少钱？包含水电费吗？"},{"sentence":"いつから引っ越せますか。","translation":"什么时候可以搬进来？"}]}', 20, "1", '0'),
(47, '邻里交往', 1, 'vocabulary', '{"words":[{" ja ":" ご近所さん "," zh ":" 邻居 "},{" ja ":" ご挨拶 "," zh ":" 打招呼 "},{" ja ":" おすそわけ "," zh ":" 分享食物给邻居 "},{" ja ":" ゴミ出し "," zh ":" 扔垃圾 "}]}', 20, "1", '0'),
(47, '邻里例句', 2, 'examples', '{"sentences":[{"sentence":"こんにちは、隣に引っ越してきた〇〇です。","translation":"你好，我是刚搬到隔壁的XX。"},{"sentence":"よろしくおねがいします。何かあったら声をかけてください。","translation":"请多关照。有事请叫我。"},{"sentence":"ゴミの出し方を教えてくれませんか。","translation":"能教我一下垃圾怎么扔吗？"}]}', 20, "1", '0'),
(48, '邀请朋友', 1, 'vocabulary', '{"words":[{" ja ":" 今度 "," zh ":" 改天/下次 "},{" ja ":" 一緒に行きませんか "," zh ":" 要不要一起去 "},{" ja ":" 都合 "," zh ":" 方便/时间安排 "},{" ja ":" 楽しみ "," zh ":" 期待/开心的事 "}]}', 20, "1", '0'),
(48, '邀友例句', 2, 'examples', '{"sentences":[{"sentence":"今度、一緒に飲みに行きませんか。","translation":"改天一起去喝一杯怎么样？"},{" sentence ":" その日はちょっと都合が悪いんですけど... "," translation ":" 那天有点不方便... "},{" sentence ":" 楽しみにしています！絶対行きます！ "," translation ":" 很期待！我一定去！ "}]}', 20, "1", '0'),
(49, '闲聊话题', 1, 'vocabulary', '{"words":[{" ja ":" 天気 "," zh ":" 天气 "},{" ja ":" 最近 "," zh ":" 最近 "},{" ja ":" どう "," zh ":" 怎么样 "},{" ja ":" 面白い "," zh ":" 有趣的 "}]}', 20, "1", '0'),
(49, '闲聊例句', 2, 'examples', '{"sentences":[{"sentence":"最近、寒くなりましたね。"," translation ":" 最近变冷了呢。 "},{" sentence ":" 週末は何をしましたか。 "," translation ":" 周末做了什么？ "},{" sentence ":" その映画、面白かったですか。 "," translation ":" 那部电影有意思吗？ "}]}', 20, "1", '0'),
(50, '症状描述', 1, 'vocabulary', '{"words":[{" ja ":" 頭痛 "," zh ":" 头痛 "},{" ja ":" 発熱 "," zh ":" 发烧 "},{" ja ":" 吐き気 "," zh ":" 恶心 "},{" ja ":" 咳 "," zh ":" 咳嗽 "},{" ja ":" お医者さん "," zh ":" 医生 "}]}', 20, "1", '0'),
(50, '就医例句', 2, 'examples', '{"sentences":[{"sentence":"先生、頭が痛くて熱もあります。"," translation ":" 医生，我头疼还发烧。 "},{" sentence ":" いつからですか。三日前からです。 "," translation ":" 从什么时候开始的？三天前开始的。 "},{" sentence ":" この処方箋で薬をもらえますか。 "," translation ":" 可以凭这个处方拿药吗？ "}]}', 20, "1", '0'),
(51, '药店词汇', 1, 'vocabulary', '{"words":[{" ja ":" 薬局 "," zh ":" 药店 "},{" ja ":" 風邪薬 "," zh ":" 感冒药 "},{" ja ":" 鎮痛剤 "," zh ":" 止痛药 "},{" ja ":" 塗り薬 "," zh ":" 外用药膏 "}]}', 20, "1", '0'),
(51, '药店例句', 2, 'examples', '{"sentences":[{"sentence":"風邪薬を探しているんですが、何かおすすめはありますか。"," translation ":" 我在找感冒药，有什么推荐吗？ "},{" sentence ":" これは一日何回飲めばいいですか。 "," translation ":" 这个一天吃几次？ "},{" sentence ":" 塗り薬は患部に塗ってください。 "," translation ":" 外用药膏请涂在患处。 "}]}', 20, "1", '0');

-- 日语会话中级 的词汇数据
INSERT INTO edu_vocabulary (unit_id, word, language, pronunciation, part_of_speech, definition, example_sentences, del_flag) VALUES
(40, '初めまして', 'ja', '/hajimemashite/', 'greeting', '初次见面时说的第一句话', "{\"text\":\"初めまして、田中と申します。\\\\\\\\\\\\\\\\n初めまして、どうぞよろしく。\"}", '0'),
(40, 'お疲れ様です', 'ja', '/otsukaresamadesu/', 'greeting', '同事之间常用的问候语，表示"辛苦了"', "{\"text\":\"お疲れ様です！今日も一日お疲れ様でした。\\\\\\\\\\\\\\\\n帰る前に一言「お疲れ様」を言うのがマナーです。\"}", '0'),
(41, '拝啓', 'ja', '/haikei/', 'n.', '书信开头语，相当于中文"敬启者"', "{\"text\":\"拝啓　貴社におかれましては益々ご清栄のこととお慶び申し上げます。\\\\\\\\\\\\\\\\n拝啓　季節の変わり目、皆様いかがお過ごしでしょうか。\"}", '0'),
(42, '新幹線', 'ja', '/shinkansen/', 'n.', '日本的高速铁路系统', "{\"text\":\"新幹線で大阪まで2時間半です。\\\\\\\\\\\\\\\\n新幹線の切符を買いました。\"}", '0'),
(43, '観光案内所', 'ja', '/kankō annaijo/', 'n.', '游客服务中心；旅游咨询处', "{\"text\":\"駅前に観光案内所があります。\\\\\\\\\\\\\\\\n観光案内所で地図をもらいました。\"}", '0'),
(44, 'おすすめ', 'ja', '/osusume/', 'n.', '推荐的东西；招牌菜', "{\"text\":\"今日のおすすめ料理は何ですか。\\\\\\\\\\\\\\\\n店員さんにおすすめを聞いてみましょう。\"}", '0'),
(45, '弁当', 'ja', '/bentō/', 'n.', '便当；日式盒饭', "{\"text\":\"コンビニで弁当を買いました。\\\\\\\\\\\\\\\\n手作り弁当を持って行きます。\"}", '0'),
(46, '敷金', 'ja', '/shikikin/', 'n.', '押金；保证金（退房时可退还）', "{\"text\":\"敷金と礼金で最初に多くかかります。\\\\\\\\\\\\\\\\n敷金は退去時に返ってきます。\"}", '0'),
(47, 'ご近所さん', 'ja', '/go-kinjo-san/', 'n.', '邻居（尊称）', "{\"text\":\"ご近所さんに挨拶しましょう。\\\\\\\\\\\\\\\\n親切なご近所さんに助けられました。\"}", '0'),
(48, '都合', 'ja', '/tsugō/', 'n.', '方便；时间安排；情况', "{\"text\":\"その日は都合が悪いです。\\\\\\\\\\\\\\\\nご都合いかがですか。\"}", '0'),
(49, '面白い', 'ja', '/omoshiroi/', 'adj.', '有趣的；好笑的', "{\"text\":\"この本はとても面白いです。\\\\\\\\\\\\\\\\n面白い話を聞かせてください。\"}", '0'),
(50, '発熱', 'ja', '/hatsunetsu/', 'n./v.', '发烧；发热', "38度の発熱がありました。\\n発熱がある場合は病院へ行ってください。", '0');


-- ==================== 课程11：汉语拼音基础 ====================
INSERT INTO edu_course_chapter (course_id, chapter_name, chapter_order, description, del_flag) VALUES
(11, '单韵母', 1, 'a o e i u ü 六个单韵母的发音', '0'),
(11, '声母（一）', 2, 'b p m f d t n l 等声母', '0'),
(11, '声母（二）', 3, 'g k h j q x zh ch sh r z c s y w', '0'),
(11, '复韵母', 4, 'ai ei ui ao ou iu ie üe er', '0'),
(11, '鼻韵母', 5, 'an en in un ün ang eng ing ong', '0'),
(11, '整体认读', 6, 'zhi chi shi ri zi ci si yi wu yu 等', '0');

INSERT INTO edu_course_unit (course_id, chapter_id, unit_name, unit_order, description, total_lessons, total_duration, del_flag) VALUES
(11, 1, 'a o e 发音', 1, '开口音和圆唇音的基础', 2, 25, '0'),
(11, 1, 'i u ü 发音', 2, '齐齿音和撮口音', 2, 25, '0'),
(11, 2, '双唇音b p m', 3, '双唇爆破音和鼻音', 2, 25, '0'),
(11, 2, '唇齿音f 和舌尖音d t n l', 4, '唇齿擦音和舌尖音组', 2, 25, '0'),
(11, 3, '舌根音 g k h', 5, '舌根部位发音', 2, 25, '0'),
(11, 3, '舌面音 j q x', 6, '舌面硬腭音', 2, 25, '0'),
(11, 3, '翘舌音 zh ch sh r', 7, '舌尖后翘起发音', 2, 25, '0'),
(11, 3, '平舌音 z c s', 8, '舌尖前平伸发音', 2, 25, '0'),
(11, 4, '前响复韵母', 9, 'ai ei ao ou 的发音特点', 2, 25, '0'),
(11, 4, '后响和中响复韵母', 10, 'ia ua uo iao iou 等', 2, 25, '0'),
(11, 5, '前鼻韵母', 11, 'an en in un ün', 2, 25, '0'),
(11, 5, '后鼻韵母', 12, 'ang eng ing ong', 2, 25, '0'),
(11, 6, '整体认读音节', 13, 'zhi chi shi ri zi ci si yi wu yu ye yue yuan yin yun ying', 2, 25, '0'),
(11, 6, '声调练习', 14, '一声二声三声四声轻声', 2, 25, '0');

INSERT INTO edu_course_lesson (unit_id, lesson_name, lesson_order, lesson_type, content, duration, is_free, del_flag) VALUES
(52, 'a o e 单韵母', 1, 'vocabulary', '{"words":[{"zh":"ā(a)","zh":"啊"},{"zh":"ō(o)","zh":"哦"},{"zh":"ē(e)","zh":"鹅"}]}', 15, "1", '0'),
(52, 'a o e 例句', 2, 'examples', '{"sentences":[{"sentence":"阿姨(āyí)您好！","translation":"阿姨你好！"},{"sentence":"哦(ò)，我知道了。","translation":"哦，我知道了。"},{"sentence":"这只白鹅(é)真可爱。","translation":"这只白鹅真可爱。"}]}', 15, "1", '0'),
(53, 'i u ü 单韵母', 1, 'vocabulary', '{"words":[{"zh":"ī(i)","zh":"衣"},{"zh":"ū(u)","zh":"乌"},{"zh":"ǖ(ü)","zh":"迂"}]}', 15, "1", '0'),
(53, 'i u ü 例句', 2, 'examples', '{"sentences":[{"sentence":"这件衣服(yīfu)很漂亮。","translation":"这件衣服很漂亮。"},{"sentence":"乌(wū)鸦喝水的故事你听过吗？","翻译":"乌鸦喝水的故事你听过吗？"},{" sentence ":" 鱼 ( yú ) 会游泳。 "," translation ":" 鱼会游泳。 "}]}', 15, "1", '0'),
(54, 'b p m 声母', 1, 'vocabulary', '{"words":[{"zh":"b(玻)","zh":"b"},{"zh":"p(坡)","zh":"p"},{"zh":"m(摸)","zh":"m"}]}', 15, "1", '0'),
(54, 'b p m 例句', 2, 'examples', '{"sentences":[{"sentence":"爸爸(bàba)、妈妈(māma)，我爱你！"," translation ":" 爸爸妈妈，我爱你！ "},{" sentence ":" 葡萄(pútao)又甜又好吃。"," translation ":" 葡萄又甜又好吃。 "},{" sentence ":" 妹妹(mèimei)在画画(huàhuà)。 "," translation ":" 妹妹在画画。 "}]}', 15, "1", '0'),
(55, 'f d t n l 声母', 1, 'vocabulary', '{"words":[{"zh":"f(佛)","zh":"f"},{"zh":"d(得)","zh":"d"},{"zh":"t(特)","zh":"t"},{"zh":"n(讷)","zh":"n"},{"zh":"l(勒)","zh":"l"}]}', 15, "1", '0'),
(55, 'f d t n l 例句', 2, 'examples', '{"sentences":[{"sentence":"头发(tóufa)长长的，非常好看。"," translation ":" 头发长长的，非常好看。 "},{" sentence ":" 弟弟(dìdi)在读书(dúshū)。 "," translation ":" 弟弟在读书。 "},{" sentence ":" 拿(ná)起你的牛奶(niúnǎi)。 "," translation ":" 拿起你的牛奶。 "}]}', 15, "1", '0'),
(56, 'g k h 声母', 1, 'vocabulary', '{"words":[{"zh":"g(哥)","zh":"g"},{"zh":"k(科)","zh":"k"},{"zh":"h(喝)","zh":"h"}]}', 15, "1", '0'),
(56, 'g k h 例句', 2, 'examples', '{"sentences":[{" sentence ":" 哥哥(gēge)给我(gěi)一个苹果(píngguǒ)。 "," translation ":" 哥哥给我一个苹果。 "},{" sentence ":" 可以(kěyǐ)帮我一下吗？ "," translation ":" 可以帮我一下吗？ "},{" sentence ":" 喝(hē)水对身体有好处。 "," translation ":" 喝水对身体有好处。 "}]}', 15, "1", '0'),
(57, 'j q x 声母', 1, 'vocabulary', '{"words":[{"zh":"j(基)","zh":"j"},{"zh":"q(欺)","zh":"q"},{"zh":"x(希)","zh":"x"}]}', 15, "1", '0'),
(57, 'j q x 例句', 2, 'examples', '{"sentences":[{"sentence":"鸡(jī)蛋(dàn)很好吃。"," translation ":" 鸡蛋很好吃。 "},{" sentence ":" 请(qǐng)坐(zuò)。 "," translation ":" 请坐。 "},{" sentence ":" 西瓜(xīguā)好甜啊！ "," translation ":" 西瓜好甜啊！ "}]}', 15, "1", '0'),
(58, 'zh ch sh r 翘舌音', 1, 'vocabulary', '{"words":[{"zh":"zh(知)","zh":"zh"},{"zh":"ch(吃)","zh":"ch"},{"zh":"sh(诗)","zh":"sh"},{"zh":"r(日)","zh":"r"}]}', 15, "1", '0'),
(58, 'zh ch sh r 例句', 2, 'examples', '{"sentences":[{"sentence":"知(zhī)识(shí)就是力量(liàng)。"," translation ":" 知识就是力量。 "},{" sentence ":" 吃(chī)饭(fàn)时间到了。 "," translation ":" 吃饭时间到了。 "},{" sentence ":" 今天(rì)天气(qì)真好。 "," translation ":" 今天天气真好。 "}]}', 15, "1", '0'),
(59, 'z c s 平舌音', 1, 'vocabulary', '{"words":[{"zh":"z(资)","zh":"z"},{"zh":"c(雌)","zh":"c"},{"zh":"s(思)","zh":"s"}]}', 15, "1", '0'),
(59, 'z c s 例句', 2, 'examples', '{"sentences":[{"sentence":"字(zì)写(xiě)得很整齐(zhěngqí)。"," translation ":" 字写得很整齐。 "},{" sentence ":" 刺(cì)猬(wèi)是一种小动物。 "," translation ":" 刺猬是一种小动物。 "},{" sentence ":" 思(sī)考(kǎo)问题(wèntí)要仔细(zǐxì)。 "," translation ":" 思考问题要仔细。 "}]}', 15, "1", '0'),
(60, 'ai ei ao ou 复韵母', 1, 'vocabulary', '{"words":[{"zh":"āi(ai)","zh":"哀"},{"zh":"ēi(ei)","zh":"诶"},{"zh":"āo(ao)","zh":"熬"},{"zh":"ōu(ou)","zh":"欧"}]}', 15, "1", '0'),
(60, '复韵母例句', 2, 'examples', '{"sentences":[{"sentence":"爱(ài)学习(xuéxí)，爱生活(shēnghuó)。"," translation ":" 爱学习，爱生活。 "},{" sentence ":" 这杯(bēi)水(shuǐ)很凉(liáng)。 "," translation ":" 这杯水很凉。 "},{" sentence ":" 宝宝(bǎobao)在睡觉(shuìjiào)。 "," translation ":" 宝宝在睡觉。 "}]}', 15, "1", '0'),
(61, 'ia ua uo 等复韵母', 1, 'vocabulary', '{"words":[{"zh":"iā(ia)","zh":"呀"},{"zh":"uā(ua)","zh":"蛙"},{"zh":"uō(uo)","zh":"窝"}]}', 15, "1", '0'),
(61, '复韵母例句', 2, 'examples', '{"sentences":[{"sentence":"下(xià)雨(yǔ)了，带(dài)伞(sǎn)吧。"," translation ":" 下雨了，带伞吧。 "},{" sentence ":" 花(huā)儿开(kāi)得好美(měi)。 "," translation ":" 花儿开得好美。 "},{" sentence ":" 我(wǒ)说(shuō)中文(zhōngwén)。 "," translation ":" 我说中文。 "}]}', 15, "1", '0'),
(62, 'an en in un ün 前鼻韵母', 1, 'vocabulary', '{"words":[{"zh":"ān(an)","zh":"安"},{"zh":"ēn(en)","zh":"恩"},{"zh":"īn(in)","zh":"因"},{"zh":"ǔn(ün)","zh":"晕"}]}', 15, "1", '0'),
(62, '前鼻韵母例句', 2, 'examples', '{"sentences":[{"sentence":"安(ān)全(quán)第一(dìyī)。"," translation ":" 安全第一。 "},{" sentence ":" 我们(wǒmen)是朋友(péngyou)。 "," translation ":" 我们是朋友。 "},{" sentence ":" 今(jīn)天(tiān)心情(xínqíng)不错(búcuò)。 "," translation ":" 今天心情不错。 "}]}', 15, "1", '0'),
(63, 'ang eng ing ong 后鼻韵母', 1, 'vocabulary', '{"words":[{"zh":"āng(ang)","zh":"昂"},{"zh":"ēng(eng)","zh":"鞥"},{"zh":"īng(ing)","zh":"英"},{"zh":"ōng(ong)","zh":"轰"}]}', 15, "1", '0'),
(63, '后鼻韵母例句', 2, 'examples', '{"sentences":[{"sentence":"帮(bāng)助(zhù)别人(biéren)就是帮助自己(zìjǐ)。"," translation ":" 帮助别人就是帮助自己。 "},{" sentence ":" 风(fēng)筝(zheng)飞(fēi)起来了(lái le)。 "," translation ":" 风筝飞起来了。 "},{" sentence ":" 星(xīng)星(xing)在天空中(tiānkōngzhōng)闪烁(shǎnshuò)。 "," translation ":" 星星在天空中闪烁。 "}]}', 15, "1", '0'),
(64, '整体认读音节', 1, 'vocabulary', '{"words":[{"zh":"zhi","zh":"只"},{"zh":"chi","zh":"吃"},{"zh":"shi","zh":"是"},{"zh":"ri","zh":"日"},{"zh":"zi","zh":"字"}]}', 15, "1", '0'),
(64, '整体认读例句', 2, 'examples', '{"sentences":[{"sentence":"这(zhè)是(shì)我的书(shū)。"," translation ":" 这是我的书。 "},{" sentence ":" 日(rì)出(chū)东方(dōngfāng)。 "," translation ":" 日出东方。 "},{" sentence ":" 写(xiě)字(zì)要认真(rènzhēn)。 "," translation ":" 写字要认真。 "}]}', 15, "1", '0'),
(65, '四声调练习', 1, 'vocabulary', '{"words":[{"zh":"一声(¯)","zh":"阴平/高平调"},{"zh":"二声(ˊ)","zh":"阳平/升调"},{"zh":"三声(ˇ)","zh":"上声/曲折调"},{"zh":"四声(ˋ)","zh":"去声/降调"}]}', 15, "1", '0'),
(65, '声调例句', 2, 'examples', '{"sentences":[{"sentence":"妈(mā)麻(má)马(mǎ)骂(mà)——四声练习经典例句"," translation ":" 妈麻马骂——四声练习经典例句 "},{" sentence ":" 山(shān)明(míng)水(shuǐ)秀(xiù)。 "," translation ":" 山明水秀。 "},{" sentence ":" 千(qiān)言(yán)万(wàn)语(yǔ)。 "," translation ":" 千言万语。 "}]}', 15, "1", '0');

-- 汉语拼音基础 的词汇数据
INSERT INTO edu_vocabulary (unit_id, word, language, pronunciation, part_of_speech, definition, example_sentences, del_flag) VALUES
(52, 'a', 'zh', '/A/', 'final', '汉语拼音第一个单韵母，嘴巴张大', "{\"text\":\"阿姨(āyí)\\\\\\\\\\\\\\\\n阿婆(āpó)\"}", '0'),
(52, 'o', 'zh', '/o/', 'final', '单韵母，嘴唇圆形', "{\"text\":\"哦(ò)\\\\\\\\\\\\\\\\n伯伯(óbó)\"}", '0'),
(53, 'i', 'zh', '/i/', 'final', '单韵母，牙齿对齐微笑状', "{\"text\":\"衣服(yīfu)\\\\\\\\\\\\\\\\n意思(yìsi)\"}", '0'),
(54, 'b', 'zh', '/p/', 'initial', '不送气双唇清塞音', "{\"text\":\"爸爸(bàba)\\\\\\\\\\\\\\\\n杯子(bēizi)\"}", '0'),
(55, 'd', 'zh', '/t/', 'initial', '不送气舌尖中清塞音', "{\"text\":\"弟弟(dìdi)\\\\\\\\\\\\\\\\n大家(dàjiā)\"}", '0'),
(56, 'h', 'zh', '/x/', 'initial', '舌根清擦音', "{\"text\":\"你好(nǐhǎo)\\\\\\\\\\\\\\\\n喝水(hēshuǐ)\"}", '0'),
(57, 'j', 'zh', '/tɕ/', 'initial', '舌面不送气清塞擦音', "{\"text\":\"家里(jiāli)\\\\\\\\\\\\\\\\n见面(jiànmiàn)\"}", '0'),
(58, 'zh', 'zh', '/ʈʂ/', 'initial', '翘舌音，舌尖后不送气清塞擦音', "{\"text\":\"中国(Zhōngguó)\\\\\\\\\\\\\\\\n知道(zhīdao)\"}", '0'),
(59, 'z', 'zh', '/ts/', 'initial', '平舌音，舌尖前不送气清塞擦音', "{\"text\":\"自在(zìzai)\\\\\\\\\\\\\\\\n写字(xiězì)\"}", '0'),
(60, 'ai', 'zh', '/aɪ/', 'compound final', '前响复韵母，由a滑向i', "{\"text\":\"爱好(àihào)\\\\\\\\\\\\\\\\n大海(dàhǎi)\"}", '0'),
(61, 'ua', 'zh', '/ua/', 'compound final', '后响合口呼复韵母', "{\"text\":\"花朵(huāduǒ)\\\\\\\\\\\\\\\\n娃娃(wáwa)\"}", '0'),
(an, 'zh', '/an/', 'nasal final', '前鼻韵母，舌尖抵住齿龈', 'ān)\n吃饭(chīfàn)', {"text":"0"}, '0'),
(63, 'ang', 'zh', '/ɑŋ/', 'nasal final', '后鼻韵母，舌根抬起接触软腭', "{\"text\":\"帮忙(bāngmáng)\\\\\\\\\\\\\\\\n上课(shàngkè)\"}", '0'),
(64, 'shi', 'zh', '/ʂɨ̯/', 'syllable', '整体认读音节，不可拆分', "老师(lǎoshī)\\n事情(shìqing)", '0');


-- ==================== 课程14：汉字书写入门 ====================
INSERT INTO edu_course_chapter (course_id, chapter_name, chapter_order, description, del_flag) VALUES
(14, '基本笔画', 1, '横竖撇捺点折提等基本笔画的写法', '0'),
(14, '独体字', 2, '简单独体字的书写顺序和结构', '0'),
(14, '常用偏旁', 3, '亻氵艹木等常见部首的写法', '0'),
(14, '左右结构', 4, '左右结构汉字的间架比例', '0'),
(14, '上下结构', 5, '上下结构汉字的布局要点', '0'),
(14, '综合练习', 6, '完整句子和段落的书写训练', '0');

INSERT INTO edu_course_unit (course_id, chapter_id, unit_name, unit_order, description, total_lessons, total_duration, del_flag) VALUES
(14, 1, '横竖笔画', 1, '横(一)、竖(丨)的基本写法', 2, 25, '0'),
(14, 1, '撇捺点折', 2, '撇(丿)、捺(㇏)、点(丶)、折(𠃌)等', 2, 25, '0'),
(14, 2, '数字与天干', 3, '一二三...十、甲乙丙等简单字', 2, 25, '0'),
(14, 2, '自然类独体字', 4, '日、月、山、水、火等', 2, 25, '0'),
(14, 3, '人字旁与三点水', 5, '亻氵的书写和应用', 2, 25, '0'),
(14, 3, '草字头与木字旁', 6, '艹木的书写和应用', 2, 25, '0'),
(14, 4, '左窄右宽型', 7, '他、们、语、说等', 2, 25, '0'),
(14, 4, '左宽右窄型', 8, '到、剧、影等', 2, 25, '0'),
(14, 5, '上宽下窄型', 9, '思、想、念等', 2, 25, '0'),
(14, 5, '上窄下宽型', 10, '昌、晃、晃等', 2, 25, '0'),
(14, 6, '常用词语书写', 11, '日常高频词汇的规范书写', 2, 30, '0'),
(14, 6, '句子书写', 12, '完整句子的格式和标点', 2, 30, '0');

INSERT INTO edu_course_lesson (unit_id, lesson_name, lesson_order, lesson_type, content, duration, is_free, del_flag) VALUES
(66, '横竖基本笔画', 1, 'vocabulary', '{"words":[{"zh":"一(横)","zh":"从左向右平稳运笔"},{"zh":"丨(竖)","zh":"从上向下垂直运笔"}]}', 15, "1", '0'),
(66, '横竖例句', 2, 'examples', '{"sentences":[{"sentence":"一(yī)二(èr)三(sān)，最简单的字。","translation":"一二三，最简单的字。"},{"sentence":"十(shí)字由一横(héng)一竖(shù)组成。","translation":"十字由一横一竖组成。"},{"sentence":"工(gōng)人(rén)上(shàng)班(bān)，努力(nǔlì)工作(gōngzuò)。"," translation ":" 工人上班，努力工作。 "}]}', 15, "1", '0'),
(67, '撇捺点折笔画', 1, 'vocabulary', '{"words":[{"zh":"丿(撇)","zh":"从右上向左下斜出"},{"zh":"㇏(捺)","zh":"从左上向右下斜出，末端顿笔"},{"zh":"丶(点)","zh":"轻落笔后迅速提起"},{"zh":"𠃌(折)","zh":"改变方向的转折笔画"}]}', 15, "1", '0'),
(67, '撇捺例句', 2, 'examples', '{"sentences":[{"sentence":"大(dà)字有 一横、一撇(piě)、一捺(nà)。"," translation ":" 大字有一横、一撇、一捺。 "},{" sentence ":" 人(rén)字一撇(piě)一捺(nà)，互相支撑(chēng)。 "," translation ":" 人字一撇一捺，互相支撑。 "},{" sentence ":" 心(xīn)字有三点(diǎn)，中间一点最重要(zhòngyào)。 "," translation ":" 心字有三点，中间一点最重要。 "}]}', 15, "1", '0'),
(68, '数字与天干', 1, 'vocabulary', '{"words":[{"zh":"一二三四五六七八九十","zh":"阿拉伯数字对应的中文大写"},{"zh":"甲乙丙丁戊己庚辛壬癸","zh":"十天干"}]}', 15, "1", '0'),
(68, '数字例句', 2, 'examples', '{"sentences":[{"sentence":"一年(yìnián)有十二个月(yuè)。"," translation ":" 一年有十二个月。 "},{" sentence ":" 三(sān)个好朋友(hǎopéngyou)一起(qǐ)去(qù)公园(gōngyuán)。 "," translation ":" 三个好朋友一起去公园。 "},{" sentence ":" 今天(jīntiān)是五月(wǔyuè)二十(èrshí)号(hào)。 "," translation ":" 今天是五月二十号。 "}]}', 15, "1", '0'),
(69, '自然类独体字', 1, 'vocabulary', '{"words":[{"zh":"日(太阳)","zh":"外框方正，里面一横"},{"zh":"月(月亮)","zh":"像弯月的形状"},{"zh":"山(山峰)","zh":"中间高两边低"},{"zh":"水(水流)","zh":"中间竖钩两边各两点"}]}', 15, "1", '0'),
(69, '自然字例句', 2, 'examples', '{"sentences":[{"sentence":"日(rì)出(chū)东(dōng)方(fāng)，朝霞(zhāoxiá)满天(māntiān)。"," translation ":" 日出东方，朝霞满天。 "},{" sentence ":" 月(yuè)亮(liàng)圆圆(yuányuán)的(de)，像(xiàng)玉盘(yùpán)。 "," translation ":" 月亮圆圆的，像玉盘。 "},{" sentence ":" 山(shān)上(shàng)有(yǒu)很多(hěnduō)树(shù)和(hé)花(huā)。 "," translation ":" 山上有很多树和花。 "}]}', 15, "1", '0'),
(70, '亻氵偏旁', 1, 'vocabulary', '{"words":[{"zh":"亻(单人旁)","zh":"在字左边，表示与人有关"},{"zh":"氵(三点水)","zh":"在字左边，表示与水有关"}]}', 15, "1", '0'),
(70, '偏旁例句', 2, 'examples', '{"sentences":[{"sentence":"他(tā)是(shì)我(wǒ)的好(hǎo)朋友(péngyou)。"," translation ":" 他是我的好朋友。 "},{" sentence ":" 河(hé)水里(shuǐlǐ)有很多鱼(yú)。 "," translation ":" 河里有很多鱼。 "},{" sentence ":" 请(qǐng)你(nǐ)喝(hē)一杯(yībēi)清(qīng)茶(chá)。 "," translation ":" 请你喝一杯清茶。 "}]}', 15, "1", '0'),
(71, '艹木偏旁', 1, 'vocabulary', '{"words":[{"zh":"艹(草字头)","zh":"在字上面，表示植物"},{"zh":"木(木字旁)","zh":"在字左边或下面，表示树木或木材"}]}', 15, "1", '0'),
(71, '偏旁例句', 2, 'examples', '{"sentences":[{"sentence":"春天(chūntiān)，花儿(huā\'er)都(dōu)开(kāi)了(le)。"," translation ":" 春天，花儿都开了。 "},{" sentence ":" 树(shù)林(lín)里有(yǒu)很多小鸟(xiǎoniǎo)。 "," translation ":" 树林里有很多小鸟。 "},{" sentence ":" 我(wǒ)喜欢(xǐhuan)在(zài)树(shù)下(xià)看书(kànshū)。 "," translation ":" 我喜欢在树下看书。 "}]}', 15, '1', '0'),
(72, '左窄右宽字', 1, 'vocabulary', '{"words":[{"zh":"他/她/它","zh":"单人旁+也，左1右2比例"},{"zh":"语/说/话","zh":"言字旁+声旁，左边窄右边宽"}]}', 15, "1", '0'),
(72, '左右结构例句', 2, 'examples', '{"sentences":[{"sentence":"她(tā)说(shuō)中(zhōng)文(wén)说得(shuōde)很好(hěnhǎo)。"," translation ":" 她中文说得很好。 "},{" sentence ":" 我们(wǒmen)一起(yìqǐ)学习(xuéxí)汉语(hànyǔ)。 "," translation ":" 我们一起学习汉语。 "},{" sentence ":" 请(qǐng)你(nǐ)告诉(gàosu)我(wǒ)你的消息(xiāoxi)。 "," translation ":" 请告诉我你的消息。 "}]}', 15, "1", '0'),
(73, '上下结构字', 1, 'vocabulary', '{"words":[{"zh":"思/想/念","zh":"上面田/相/今，下面心"},{"zh":"字/家/室","zh":"上面宝盖头，下面主体部分"}]}', 15, "1", '0'),
(73, '上下结构例句', 2, 'examples', '{"sentences":[{"sentence":"我在家(jiā)里(lǐ)想念(xiǎngniàn)爸爸 bàba 和妈妈 māma。"," translation ":" 我在家里想念爸爸和妈妈。 "},{" sentence ":" 这个 zhège 字 zì 写得 xiěde 很好 hěnhǎo 。 "," translation ":" 这个字写得很好。 "},{" sentence ":" 学习 xuéxí 需要 xūyào 思考 sīkǎo 和练习 liànxí 。 "," translation ":" 学习需要思考和练习。 "}]}', 15, "1", '0'),
(74, '常用词语书写', 1, 'vocabulary', '{"words":[{"zh":"你好/谢谢/对不起","zh":"最基本的礼貌用语"},{"zh":"中国/北京/上海","zh":"地名类词汇"},{"zh":"学生/老师/学校","zh":"教育相关词汇"}]}', 15, "1", '0'),
(74, '词语例句', 2, 'examples', '{"sentences":[{"sentence":"你好！我是中国的学生。"," translation ":" 你好！我是中国的学生。 "},{" sentence ":" 谢谢老师的帮助！ "," translation ":" 谢谢老师的帮助！ "},{" sentence ":" 北京是中国的首都。 "," translation ":" 北京是中国的首都。 "}]}', 15, "1", '0'),
(75, '句子书写', 1, 'vocabulary', '{"words":[{"zh":"主谓宾结构","zh":"谁+做+什么的基本句式"},{"zh":"标点符号","zh":"逗号、句号、问号、感叹号的用法"}]}', 15, "1", '0'),
(75, '句子例句', 2, 'examples', '{"sentences":[{"sentence":"我爱我的祖国。"," translation ":" 我爱我的祖国。 "},{" sentence ":" 你今天吃饭了吗？ "," translation ":" 你今天吃饭了吗？ "},{" sentence ":" 春天的风景真美啊！ "," translation ":" 春天的风景真美啊！ "}]}', 15, "1", '0');

-- 汉字书写入门 的词汇数据
INSERT INTO edu_vocabulary (unit_id, word, language, pronunciation, part_of_speech, definition, example_sentences, del_flag) VALUES
(66, '横', 'zh', '/héŋ/', 'n.', '基本笔画之一，从左向右平稳书写', "{\"text\":\"一横一竖\\\\\\\\\\\\\\\\n横平竖直\"}", '0'),
(66, '竖', 'zh', '/ʂû/', 'n.', '基本笔画之一，从上向下垂直书写', "{\"text\":\"竖着写\\\\\\\\\\\\\\\\n横竖交叉\"}", '0'),
(撇, 'zh', '/p', ', ', ', ', ', ', {"text":", "}, '0'),
(68, '一', 'zh', '/í/', 'numeral', '汉字中最简单的字，表示数字1', "{\"text\":\"一举两得\\\\\\\\\\\\\\\\n一心一意\\\\\\\\\\\\\\\\n一帆风顺\"}", '0'),
(69, '日', 'zh', '/ɻî/', 'n.', '太阳；白天；时间单位"日"', "{\"text\":\"日出东方\\\\\\\\\\\\\\\\n日日夜夜\\\\\\\\\\\\\\\\n生日快乐\"}", '0'),
(70, '亻', 'zh', '/rénzìpáng/', 'radical', '单人旁，作左偏旁时写作亻', "{\"text\":\"他们\\\\\\\\\\\\\\\\n伙伴\\\\\\\\\\\\\\\\n位置\"}", '0'),
(71, '艹', 'zh', '/cǎozìtóu/', 'radical', '草字头，表示与植物有关', "{\"text\":\"花草\\\\\\\\\\\\\\\\n茶叶\\\\\\\\\\\\\\\\n蔬菜\"}", '0'),
(72, '语', 'zh', '/yǔ̌/', 'n./v.', '语言；说话；话语', "{\"text\":\"汉语\\\\\\\\\\\\\\\\n语文\\\\\\\\\\\\\\\\n语言交流\"}", '0'),
(73, '思', 'zh', '/sī/', 'v./n.', '思考；思念；想法', "思考问题\\n思念家乡\\n深思熟虑", '0');




