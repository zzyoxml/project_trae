SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
TRUNCATE TABLE edu_vocabulary;
TRUNCATE TABLE edu_course_lesson;
TRUNCATE TABLE edu_course_unit;
TRUNCATE TABLE edu_course_chapter;
TRUNCATE TABLE edu_course;

ALTER DATABASE ryvue CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

ALTER TABLE edu_course CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
ALTER TABLE edu_course_chapter CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
ALTER TABLE edu_course_unit CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
ALTER TABLE edu_course_lesson CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
ALTER TABLE edu_vocabulary CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Courses
INSERT INTO edu_course(course_id,course_name,course_code,language,level,course_type,description,learning_objectives,tags,difficulty_score,is_free,is_published,del_flag) VALUES
(1,'英语入门基础','EN-BASIC','en','beginner','general','零基础英语学习，从字母、发音到日常会话的全面入门课程','掌握26个字母发音、100个基础单词、20个日常句型','英语零基础,字母,发音,入门',1,'1','1','0'),
(2,'英语日常会话','EN-DAILY','en','elementary','conversation','日常生活场景中的实用英语对话，涵盖购物、问路、餐厅等高频场景','能进行基本的日常英语交流，听懂简单对话','日常对话,生活英语,实用口语',2,'1','1','0'),
(3,'英语社交礼仪','EN-SOCIAL','en','intermediate','conversation','西方社交礼仪与地道表达，学会得体地打招呼、道歉、感谢、邀请等','掌握英语国家社交礼仪，表达自然得体','社交礼仪,文化,地道表达',3,'1','1','0'),
(4,'英语医疗场景','EN-MEDICAL','en','intermediate','conversation','医院就诊、描述症状、购买药品等医疗场景的英语表达','能用英语在医院就诊并准确描述身体状况','医疗英语,看病,健康',3,'1','1','0'),
(5,'英语职场沟通','EN-WORK','en','intermediate','conversation','会议、报告、邮件、deadline等职场场景的专业英语表达','能在职场环境中用英语进行基本沟通','职场英语,商务,会议',3,'1','1','0'),
(6,'日语五十音与基础会话','JA-BASIC','ja','beginner','general','从日语五十音图（平假名/片假名）开始，到基础问候语和日常会话','熟练掌握五十音图，能用日语进行简单自我介绍和问候','日语入门,五十音,假名',1,'1','1','0');

-- Chapters
INSERT INTO edu_course_chapter(chapter_id,course_id,chapter_name,chapter_order,description,del_flag) VALUES
(1,1,'字母与发音',1,'26个英文字母的标准发音与书写','0'),
(2,1,'日常问候',2,'Hello, Good morning等基础问候语','0'),
(3,1,'自我介绍',3,'名字、国籍、职业的自我介绍','0'),
(4,1,'家庭成员',4,'father, mother等家庭成员词汇','0'),
(5,1,'外貌特征',5,'tall, beautiful等外形描述词','0'),
(6,1,'日常作息',6,'wake up, breakfast等日常活动词汇','0'),
(7,1,'社交礼仪',7,'sorry, excuse me等礼貌用语','0'),
(8,1,'情感表达式',8,'happy, sad等情感表达','0'),
(9,1,'庆祝活动',9,'birthday, party等庆祝活动词汇','0'),
(10,1,'医疗场景',10,'hospital, doctor等医疗相关','0'),
(11,1,'职场沟通',11,'meeting, report等职场词汇','0'),
(12,2,'见面问候',12,'初次见面的标准寒暄','0'),
(13,2,'介绍他人',13,'如何向他人介绍你的朋友家人','0'),
(14,2,'家庭话题',14,'谈论家人的基本信息','0'),
(15,2,'描述人物',15,'描述一个人的外貌性格','0'),
(16,2,'一天作息',16,'从早到晚的日常活动','0'),
(17,2,'礼貌用语',17,'道歉、感谢、请求帮助','0'),
(18,2,'表达情感',18,'喜怒哀乐的正确表达方式','0'),
(19,2,'节日聚会',19,'生日派对、节日聚会场景','0'),
(20,2,'看病就医',20,'去医院看病的完整流程','0'),
(21,2,'工作场合',21,'办公室里的常用表达','0'),
(22,3,'打招呼与告别',22,'不同时段的问候方式','0'),
(23,3,'感谢与道歉',23,'如何得体地表示感谢和歉意','0'),
(24,3,'表达情绪',24,'各种情绪的地道表达','0'),
(25,3,'邀请与回应',25,'发出邀请和接受/拒绝邀请','0'),
(26,3,'赞美与祝贺',26,'赞美他人和祝贺特殊时刻','0'),
(27,3,'请求与拒绝',27,'提出请求和委婉拒绝','0'),
(28,4,'描述症状',28,'头痛发烧等各种症状描述','0'),
(29,4,'看医生流程',29,'挂号、问诊、检查全流程','0'),
(30,4,'买药与用药',30,'药房买药和服药说明','0'),
(31,4,'急诊情况',31,'紧急情况的应对方法','0'),
(32,4,'健康建议',32,'医生给出的健康建议','0'),
(33,5,'办公室日常',33,'办公室日常交流','0'),
(34,5,'开会讨论',34,'开会时的发言和讨论','0'),
(35,5,'邮件写作',35,'商务邮件的写作规范','0'),
(36,5,'电话沟通',36,'电话接打技巧','0'),
(37,5,'汇报工作',37,'向上级汇报工作进展','0'),
(38,6,'平假名（あ行-な行）',38,'日语前半部分平假名','0'),
(39,6,'平假名（は行-わ行）',39,'后半部分平假名','0'),
(40,6,'片假名与浊音',40,'片假名及拗音促音','0'),
(41,6,'基础问候语',41,'こんにちは等基础问候','0'),
(42,6,'日常会话',42,'日常对话句型','0'),
(43,6,'颜色与数字',43,'颜色名称和数字读法','0'),
(44,6,'基础语法',44,'はです等基础语法','0'),
(45,6,'汉字书写',45,'基础汉字和偏旁部首','0');

-- Units
INSERT INTO edu_course_unit(unit_id,course_id,chapter_id,unit_name,unit_order,description,del_flag) VALUES
(1,1,1,'字母A-E',1,'a,b,c,d,e的发音与例词','0'),
(2,1,1,'字母F-J',2,'f,g,h,i,j的发音与例词','0'),
(3,1,1,'字母K-O',3,'k,l,m,n,o的发音与例词','0'),
(4,1,1,'字母P-T',4,'p,q,r,s,t的发音与例词','0'),
(5,1,1,'字母U-Z',5,'u,v,w,x,y,z的发音与例词','0'),
(6,1,2,'Hello与Goodbye',6,'Hello! Nice to meet you. Goodbye! See you tomorrow.','0'),
(7,1,2,'Good Morning等问候',7,'Good morning, Good afternoon, Good evening','0'),
(8,1,2,'How are you?',8,'询问近况的回答方式','0'),
(9,1,3,'Name与Come from',9,'What is your name? Where do you come from?','0'),
(10,1,3,'Student与Engineer',10,'职业相关的自我介绍词汇','0'),
(11,1,4,'Father与Mother',11,'家庭成员核心词汇','0'),
(12,1,4,'Brother与Sister',12,'兄弟姐妹及相关表达','0'),
(13,1,4,'Family',13,'关于家庭的综合表达','0'),
(14,1,5,'Tall与Beautiful',14,'形容人的外观词汇','0'),
(15,1,5,'Friendly与Young',15,'性格与年龄相关描述词','0'),
(16,1,6,'Wake Up与Breakfast',16,'早晨起床和早餐相关','0'),
(17,1,6,'Usually与Every Day',17,'频率副词的使用','0'),
(18,1,7,'Sorry与Excuse Me',18,'道歉和请求注意的表达','0'),
(19,1,7,'Apologize与Welcome',19,'更正式的礼貌用语','0'),
(20,1,8,'Happy与Sad',20,'基本情绪词汇','0'),
(21,1,8,'Angry与Excited',21,'强烈情绪的表达','0'),
(22,1,9,'Celebration与Invite',22,'庆祝活动和邀请','0'),
(23,1,9,'Amazing与Party',23,'派对相关词汇','0'),
(24,1,10,'Hospital与Doctor',24,'医院和医生相关','0'),
(25,1,10,'Fever与Medicine',25,'症状和药物词汇','0'),
(26,1,10,'Ambulance与Prescription',26,'急救和处方','0'),
(27,1,11,'Deadline与Meeting',27,'截止日期和会议','0'),
(28,1,11,'Report与Project',28,'工作报告和项目','0'),
(29,2,12,'见面问候-单元1',29,'见面问候的第一单元','0'),
(30,2,13,'介绍他人-单元1',30,'介绍他人的第一单元','0'),
(31,2,14,'家庭话题-单元1',31,'家庭话题的第一单元','0'),
(32,2,15,'描述人物-单元1',32,'描述人物的第一单元','0'),
(33,2,16,'一天作息-单元1',33,'一天作息的第一单元','0'),
(34,2,17,'礼貌用语-单元1',34,'礼貌用语的第一单元','0'),
(35,2,18,'表达情感-单元1',35,'表达情感的第一单元','0'),
(36,2,19,'节日聚会-单元1',36,'节日聚会的第一单元','0'),
(37,2,20,'看病就医-单元1',37,'看病就医的第一单元','0'),
(38,2,21,'工作场合-单元1',38,'工作场合的第一单元','0'),
(39,3,22,'打招呼与告别-单元1',39,'打招呼与告别的第一单元','0'),
(40,3,23,'感谢与道歉-单元1',40,'感谢与道歉的第一单元','0'),
(41,3,24,'表达情绪-单元1',41,'表达情绪的第一单元','0'),
(42,3,25,'邀请与回应-单元1',42,'邀请与回应的第一单元','0'),
(43,3,26,'赞美与祝贺-单元1',43,'赞美与祝贺的第一单元','0'),
(44,3,27,'请求与拒绝-单元1',44,'请求与拒绝的第一单元','0'),
(45,4,28,'描述症状-单元1',45,'描述症状的第一单元','0'),
(46,4,29,'看医生流程-单元1',46,'看医生流程的第一单元','0'),
(47,4,30,'买药与用药-单元1',47,'买药与用药的第一单元','0'),
(48,4,31,'急诊情况-单元1',48,'急诊情况的第一单元','0'),
(49,4,32,'健康建议-单元1',49,'健康建议的第一单元','0'),
(50,5,33,'办公室日常-单元1',50,'办公室日常的第一单元','0'),
(51,5,34,'开会讨论-单元1',51,'开会讨论的第一单元','0'),
(52,5,35,'邮件写作-单元1',52,'邮件写作的第一单元','0'),
(53,5,36,'电话沟通-单元1',53,'电话沟通的第一单元','0'),
(54,5,37,'汇报工作-单元1',54,'汇报工作的第一单元','0'),
(55,6,38,'あいうえお',55,'第一行元音','0'),
(56,6,38,'かきくけこ',56,'か行清音','0'),
(57,6,38,'さしすせそ',57,'さ行（し=shi）','0'),
(58,6,38,'たちつてと',58,'た行（ち=chi,つ=tsu）','0'),
(59,6,38,'なにぬねの',59,'な行鼻音','0'),
(60,6,39,'はひふへほ',60,'は行（ふ=fu）','0'),
(61,6,39,'まみむめも',61,'ま行鼻音','0'),
(62,6,39,'やゆよ',62,'や行三字','0'),
(63,6,39,'らりるれろ',63,'ら行弹音','0'),
(64,6,39,'わをん',64,'わ行和拨音ん','0'),
(65,6,40,'アイウエオ',65,'片假名对应','0'),
(66,6,40,'ガギグゲゴ',66,'浊音','0'),
(67,6,40,'促音与长音',67,'っ和ーの使用','0'),
(68,6,40,'容易混淆的假名',68,'ぬvsの, ねvsれ','0'),
(69,6,41,'こんにちは/こんばんは',69,'白天和晚上的问候','0'),
(70,6,41,'おはようございます/さようなら',70,'早上好和再见','0'),
(71,6,41,'ありがとうございます/すみません',71,'感谢和道歉','0'),
(72,6,41,'初めまして/お疲れ様です',72,'初次见面和同事问候','0'),
(73,6,41,'いただきます/ごちそうさまでした',73,'饭前饭后用语','0'),
(74,6,42,'颜色词汇',74,'あか/あお/しろ/くろ','0'),
(75,6,42,'数字1-10',75,'いち/に/さん...','0'),
(76,6,42,'人称代词',76,'私/あなた/彼/彼女','0'),
(77,6,42,'基本动词',77,'食べる/飲む/行く/来る','0'),
(78,6,42,'常用名词',78,'名前/国/言葉/時間','0'),
(79,6,43,'あいうえお复习',79,'五个元音','0'),
(80,6,43,'bpmf声母',80,'双唇音和唇齿音','0'),
(81,6,43,'dtnl声母',81,'舌尖中音','0'),
(82,6,43,'gkh声母',82,'舌根音','0'),
(83,6,44,'jqx声母',83,'舌面音','0'),
(84,6,44,'zhchshr声母',84,'翘舌音','0'),
(85,6,44,'zcs声母',85,'平舌音','0'),
(86,6,44,'ai/ei/ao/ou复韵母',86,'复韵母','0'),
(87,6,45,'an/en/ang/eng鼻韵母',87,'前后鼻音','0'),
(88,6,45,'横竖撇捺',88,'基本笔画','0'),
(89,6,45,'日月山水',89,'基础汉字','0'),
(90,6,45,'亻艹氵钅',90,'常用偏旁','0'),
(91,6,45,'语思学好',91,'常用字词','0');

-- Lessons
INSERT INTO edu_course_lesson(lesson_id,unit_id,lesson_name,lesson_order,lesson_type,duration,del_flag) VALUES
(1,1,'字母A-E-单词学习',1,'vocabulary',15,'0'),
(2,1,'字母A-E-语法讲解',2,'grammar',15,'0'),
(3,1,'字母A-E-听力练习',3,'listening',15,'0'),
(4,1,'字母A-E-口语训练',4,'speaking',15,'0'),
(5,2,'字母F-J-单词学习',1,'vocabulary',15,'0'),
(6,2,'字母F-J-语法讲解',2,'grammar',15,'0'),
(7,2,'字母F-J-听力练习',3,'listening',15,'0'),
(8,2,'字母F-J-口语训练',4,'speaking',15,'0'),
(9,3,'字母K-O-单词学习',1,'vocabulary',15,'0'),
(10,3,'字母K-O-语法讲解',2,'grammar',15,'0'),
(11,3,'字母K-O-听力练习',3,'listening',15,'0'),
(12,3,'字母K-O-口语训练',4,'speaking',15,'0'),
(13,4,'字母P-T-单词学习',1,'vocabulary',15,'0'),
(14,4,'字母P-T-语法讲解',2,'grammar',15,'0'),
(15,4,'字母P-T-听力练习',3,'listening',15,'0'),
(16,4,'字母P-T-口语训练',4,'speaking',15,'0'),
(17,5,'字母U-Z-单词学习',1,'vocabulary',15,'0'),
(18,5,'字母U-Z-语法讲解',2,'grammar',15,'0'),
(19,5,'字母U-Z-听力练习',3,'listening',15,'0'),
(20,5,'字母U-Z-口语训练',4,'speaking',15,'0'),
(21,6,'Hello与Goodbye-单词学习',1,'vocabulary',15,'0'),
(22,6,'Hello与Goodbye-语法讲解',2,'grammar',15,'0'),
(23,6,'Hello与Goodbye-听力练习',3,'listening',15,'0'),
(24,6,'Hello与Goodbye-口语训练',4,'speaking',15,'0'),
(25,7,'Good Morning等问候-单词学习',1,'vocabulary',15,'0'),
(26,7,'Good Morning等问候-语法讲解',2,'grammar',15,'0'),
(27,7,'Good Morning等问候-听力练习',3,'listening',15,'0'),
(28,7,'Good Morning等问候-口语训练',4,'speaking',15,'0'),
(29,8,'How are you?-单词学习',1,'vocabulary',15,'0'),
(30,8,'How are you?-语法讲解',2,'grammar',15,'0'),
(31,8,'How are you?-听力练习',3,'listening',15,'0'),
(32,8,'How are you?-口语训练',4,'speaking',15,'0'),
(33,9,'Name与Come from-单词学习',1,'vocabulary',15,'0'),
(34,9,'Name与Come from-语法讲解',2,'grammar',15,'0'),
(35,9,'Name与Come from-听力练习',3,'listening',15,'0'),
(36,9,'Name与Come from-口语训练',4,'speaking',15,'0'),
(37,10,'Student与Engineer-单词学习',1,'vocabulary',15,'0'),
(38,10,'Student与Engineer-语法讲解',2,'grammar',15,'0'),
(39,10,'Student与Engineer-听力练习',3,'listening',15,'0'),
(40,10,'Student与Engineer-口语训练',4,'speaking',15,'0'),
(41,11,'Father与Mother-单词学习',1,'vocabulary',15,'0'),
(42,11,'Father与Mother-语法讲解',2,'grammar',15,'0'),
(43,11,'Father与Mother-听力练习',3,'listening',15,'0'),
(44,11,'Father与Mother-口语训练',4,'speaking',15,'0'),
(45,12,'Brother与Sister-单词学习',1,'vocabulary',15,'0'),
(46,12,'Brother与Sister-语法讲解',2,'grammar',15,'0'),
(47,12,'Brother与Sister-听力练习',3,'listening',15,'0'),
(48,12,'Brother与Sister-口语训练',4,'speaking',15,'0'),
(49,13,'Family-单词学习',1,'vocabulary',15,'0'),
(50,13,'Family-语法讲解',2,'grammar',15,'0'),
(51,13,'Family-听力练习',3,'listening',15,'0'),
(52,13,'Family-口语训练',4,'speaking',15,'0'),
(53,14,'Tall与Beautiful-单词学习',1,'vocabulary',15,'0'),
(54,14,'Tall与Beautiful-语法讲解',2,'grammar',15,'0'),
(55,14,'Tall与Beautiful-听力练习',3,'listening',15,'0'),
(56,14,'Tall与Beautiful-口语训练',4,'speaking',15,'0'),
(57,15,'Friendly与Young-单词学习',1,'vocabulary',15,'0'),
(58,15,'Friendly与Young-语法讲解',2,'grammar',15,'0'),
(59,15,'Friendly与Young-听力练习',3,'listening',15,'0'),
(60,15,'Friendly与Young-口语训练',4,'speaking',15,'0'),
(61,16,'Wake Up与Breakfast-单词学习',1,'vocabulary',15,'0'),
(62,16,'Wake Up与Breakfast-语法讲解',2,'grammar',15,'0'),
(63,16,'Wake Up与Breakfast-听力练习',3,'listening',15,'0'),
(64,16,'Wake Up与Breakfast-口语训练',4,'speaking',15,'0'),
(65,17,'Usually与Every Day-单词学习',1,'vocabulary',15,'0'),
(66,17,'Usually与Every Day-语法讲解',2,'grammar',15,'0'),
(67,17,'Usually与Every Day-听力练习',3,'listening',15,'0'),
(68,17,'Usually与Every Day-口语训练',4,'speaking',15,'0'),
(69,18,'Sorry与Excuse Me-单词学习',1,'vocabulary',15,'0'),
(70,18,'Sorry与Excuse Me-语法讲解',2,'grammar',15,'0'),
(71,18,'Sorry与Excuse Me-听力练习',3,'listening',15,'0'),
(72,18,'Sorry与Excuse Me-口语训练',4,'speaking',15,'0'),
(73,19,'Apologize与Welcome-单词学习',1,'vocabulary',15,'0'),
(74,19,'Apologize与Welcome-语法讲解',2,'grammar',15,'0'),
(75,19,'Apologize与Welcome-听力练习',3,'listening',15,'0'),
(76,19,'Apologize与Welcome-口语训练',4,'speaking',15,'0'),
(77,20,'Happy与Sad-单词学习',1,'vocabulary',15,'0'),
(78,20,'Happy与Sad-语法讲解',2,'grammar',15,'0'),
(79,20,'Happy与Sad-听力练习',3,'listening',15,'0'),
(80,20,'Happy与Sad-口语训练',4,'speaking',15,'0'),
(81,21,'Angry与Excited-单词学习',1,'vocabulary',15,'0'),
(82,21,'Angry与Excited-语法讲解',2,'grammar',15,'0'),
(83,21,'Angry与Excited-听力练习',3,'listening',15,'0'),
(84,21,'Angry与Excited-口语训练',4,'speaking',15,'0'),
(85,22,'Celebration与Invite-单词学习',1,'vocabulary',15,'0'),
(86,22,'Celebration与Invite-语法讲解',2,'grammar',15,'0'),
(87,22,'Celebration与Invite-听力练习',3,'listening',15,'0'),
(88,22,'Celebration与Invite-口语训练',4,'speaking',15,'0'),
(89,23,'Amazing与Party-单词学习',1,'vocabulary',15,'0'),
(90,23,'Amazing与Party-语法讲解',2,'grammar',15,'0'),
(91,23,'Amazing与Party-听力练习',3,'listening',15,'0'),
(92,23,'Amazing与Party-口语训练',4,'speaking',15,'0'),
(93,24,'Hospital与Doctor-单词学习',1,'vocabulary',15,'0'),
(94,24,'Hospital与Doctor-语法讲解',2,'grammar',15,'0'),
(95,24,'Hospital与Doctor-听力练习',3,'listening',15,'0'),
(96,24,'Hospital与Doctor-口语训练',4,'speaking',15,'0'),
(97,25,'Fever与Medicine-单词学习',1,'vocabulary',15,'0'),
(98,25,'Fever与Medicine-语法讲解',2,'grammar',15,'0'),
(99,25,'Fever与Medicine-听力练习',3,'listening',15,'0'),
(100,25,'Fever与Medicine-口语训练',4,'speaking',15,'0'),
(101,26,'Ambulance与Prescription-单词学习',1,'vocabulary',15,'0'),
(102,26,'Ambulance与Prescription-语法讲解',2,'grammar',15,'0'),
(103,26,'Ambulance与Prescription-听力练习',3,'listening',15,'0'),
(104,26,'Ambulance与Prescription-口语训练',4,'speaking',15,'0'),
(105,27,'Deadline与Meeting-单词学习',1,'vocabulary',15,'0'),
(106,27,'Deadline与Meeting-语法讲解',2,'grammar',15,'0'),
(107,27,'Deadline与Meeting-听力练习',3,'listening',15,'0'),
(108,27,'Deadline与Meeting-口语训练',4,'speaking',15,'0'),
(109,28,'Report与Project-单词学习',1,'vocabulary',15,'0'),
(110,28,'Report与Project-语法讲解',2,'grammar',15,'0'),
(111,28,'Report与Project-听力练习',3,'listening',15,'0'),
(112,28,'Report与Project-口语训练',4,'speaking',15,'0'),
(113,29,'见面问候-单元1-单词学习',1,'vocabulary',15,'0'),
(114,29,'见面问候-单元1-语法讲解',2,'grammar',15,'0'),
(115,29,'见面问候-单元1-听力练习',3,'listening',15,'0'),
(116,29,'见面问候-单元1-口语训练',4,'speaking',15,'0'),
(117,30,'介绍他人-单元1-单词学习',1,'vocabulary',15,'0'),
(118,30,'介绍他人-单元1-语法讲解',2,'grammar',15,'0'),
(119,30,'介绍他人-单元1-听力练习',3,'listening',15,'0'),
(120,30,'介绍他人-单元1-口语训练',4,'speaking',15,'0'),
(121,31,'家庭话题-单元1-单词学习',1,'vocabulary',15,'0'),
(122,31,'家庭话题-单元1-语法讲解',2,'grammar',15,'0'),
(123,31,'家庭话题-单元1-听力练习',3,'listening',15,'0'),
(124,31,'家庭话题-单元1-口语训练',4,'speaking',15,'0'),
(125,32,'描述人物-单元1-单词学习',1,'vocabulary',15,'0'),
(126,32,'描述人物-单元1-语法讲解',2,'grammar',15,'0'),
(127,32,'描述人物-单元1-听力练习',3,'listening',15,'0'),
(128,32,'描述人物-单元1-口语训练',4,'speaking',15,'0'),
(129,33,'一天作息-单元1-单词学习',1,'vocabulary',15,'0'),
(130,33,'一天作息-单元1-语法讲解',2,'grammar',15,'0'),
(131,33,'一天作息-单元1-听力练习',3,'listening',15,'0'),
(132,33,'一天作息-单元1-口语训练',4,'speaking',15,'0'),
(133,34,'礼貌用语-单元1-单词学习',1,'vocabulary',15,'0'),
(134,34,'礼貌用语-单元1-语法讲解',2,'grammar',15,'0'),
(135,34,'礼貌用语-单元1-听力练习',3,'listening',15,'0'),
(136,34,'礼貌用语-单元1-口语训练',4,'speaking',15,'0'),
(137,35,'表达情感-单元1-单词学习',1,'vocabulary',15,'0'),
(138,35,'表达情感-单元1-语法讲解',2,'grammar',15,'0'),
(139,35,'表达情感-单元1-听力练习',3,'listening',15,'0'),
(140,35,'表达情感-单元1-口语训练',4,'speaking',15,'0'),
(141,36,'节日聚会-单元1-单词学习',1,'vocabulary',15,'0'),
(142,36,'节日聚会-单元1-语法讲解',2,'grammar',15,'0'),
(143,36,'节日聚会-单元1-听力练习',3,'listening',15,'0'),
(144,36,'节日聚会-单元1-口语训练',4,'speaking',15,'0'),
(145,37,'看病就医-单元1-单词学习',1,'vocabulary',15,'0'),
(146,37,'看病就医-单元1-语法讲解',2,'grammar',15,'0'),
(147,37,'看病就医-单元1-听力练习',3,'listening',15,'0'),
(148,37,'看病就医-单元1-口语训练',4,'speaking',15,'0'),
(149,38,'工作场合-单元1-单词学习',1,'vocabulary',15,'0'),
(150,38,'工作场合-单元1-语法讲解',2,'grammar',15,'0'),
(151,38,'工作场合-单元1-听力练习',3,'listening',15,'0'),
(152,38,'工作场合-单元1-口语训练',4,'speaking',15,'0'),
(153,39,'打招呼与告别-单元1-单词学习',1,'vocabulary',15,'0'),
(154,39,'打招呼与告别-单元1-语法讲解',2,'grammar',15,'0'),
(155,39,'打招呼与告别-单元1-听力练习',3,'listening',15,'0'),
(156,39,'打招呼与告别-单元1-口语训练',4,'speaking',15,'0'),
(157,40,'感谢与道歉-单元1-单词学习',1,'vocabulary',15,'0'),
(158,40,'感谢与道歉-单元1-语法讲解',2,'grammar',15,'0'),
(159,40,'感谢与道歉-单元1-听力练习',3,'listening',15,'0'),
(160,40,'感谢与道歉-单元1-口语训练',4,'speaking',15,'0'),
(161,41,'表达情绪-单元1-单词学习',1,'vocabulary',15,'0'),
(162,41,'表达情绪-单元1-语法讲解',2,'grammar',15,'0'),
(163,41,'表达情绪-单元1-听力练习',3,'listening',15,'0'),
(164,41,'表达情绪-单元1-口语训练',4,'speaking',15,'0'),
(165,42,'邀请与回应-单元1-单词学习',1,'vocabulary',15,'0'),
(166,42,'邀请与回应-单元1-语法讲解',2,'grammar',15,'0'),
(167,42,'邀请与回应-单元1-听力练习',3,'listening',15,'0'),
(168,42,'邀请与回应-单元1-口语训练',4,'speaking',15,'0'),
(169,43,'赞美与祝贺-单元1-单词学习',1,'vocabulary',15,'0'),
(170,43,'赞美与祝贺-单元1-语法讲解',2,'grammar',15,'0'),
(171,43,'赞美与祝贺-单元1-听力练习',3,'listening',15,'0'),
(172,43,'赞美与祝贺-单元1-口语训练',4,'speaking',15,'0'),
(173,44,'请求与拒绝-单元1-单词学习',1,'vocabulary',15,'0'),
(174,44,'请求与拒绝-单元1-语法讲解',2,'grammar',15,'0'),
(175,44,'请求与拒绝-单元1-听力练习',3,'listening',15,'0'),
(176,44,'请求与拒绝-单元1-口语训练',4,'speaking',15,'0'),
(177,45,'描述症状-单元1-单词学习',1,'vocabulary',15,'0'),
(178,45,'描述症状-单元1-语法讲解',2,'grammar',15,'0'),
(179,45,'描述症状-单元1-听力练习',3,'listening',15,'0'),
(180,45,'描述症状-单元1-口语训练',4,'speaking',15,'0'),
(181,46,'看医生流程-单元1-单词学习',1,'vocabulary',15,'0'),
(182,46,'看医生流程-单元1-语法讲解',2,'grammar',15,'0'),
(183,46,'看医生流程-单元1-听力练习',3,'listening',15,'0'),
(184,46,'看医生流程-单元1-口语训练',4,'speaking',15,'0'),
(185,47,'买药与用药-单元1-单词学习',1,'vocabulary',15,'0'),
(186,47,'买药与用药-单元1-语法讲解',2,'grammar',15,'0'),
(187,47,'买药与用药-单元1-听力练习',3,'listening',15,'0'),
(188,47,'买药与用药-单元1-口语训练',4,'speaking',15,'0'),
(189,48,'急诊情况-单元1-单词学习',1,'vocabulary',15,'0'),
(190,48,'急诊情况-单元1-语法讲解',2,'grammar',15,'0'),
(191,48,'急诊情况-单元1-听力练习',3,'listening',15,'0'),
(192,48,'急诊情况-单元1-口语训练',4,'speaking',15,'0'),
(193,49,'健康建议-单元1-单词学习',1,'vocabulary',15,'0'),
(194,49,'健康建议-单元1-语法讲解',2,'grammar',15,'0'),
(195,49,'健康建议-单元1-听力练习',3,'listening',15,'0'),
(196,49,'健康建议-单元1-口语训练',4,'speaking',15,'0'),
(197,50,'办公室日常-单元1-单词学习',1,'vocabulary',15,'0'),
(198,50,'办公室日常-单元1-语法讲解',2,'grammar',15,'0'),
(199,50,'办公室日常-单元1-听力练习',3,'listening',15,'0'),
(200,50,'办公室日常-单元1-口语训练',4,'speaking',15,'0'),
(201,51,'开会讨论-单元1-单词学习',1,'vocabulary',15,'0'),
(202,51,'开会讨论-单元1-语法讲解',2,'grammar',15,'0'),
(203,51,'开会讨论-单元1-听力练习',3,'listening',15,'0'),
(204,51,'开会讨论-单元1-口语训练',4,'speaking',15,'0'),
(205,52,'邮件写作-单元1-单词学习',1,'vocabulary',15,'0'),
(206,52,'邮件写作-单元1-语法讲解',2,'grammar',15,'0'),
(207,52,'邮件写作-单元1-听力练习',3,'listening',15,'0'),
(208,52,'邮件写作-单元1-口语训练',4,'speaking',15,'0'),
(209,53,'电话沟通-单元1-单词学习',1,'vocabulary',15,'0'),
(210,53,'电话沟通-单元1-语法讲解',2,'grammar',15,'0'),
(211,53,'电话沟通-单元1-听力练习',3,'listening',15,'0'),
(212,53,'电话沟通-单元1-口语训练',4,'speaking',15,'0'),
(213,54,'汇报工作-单元1-单词学习',1,'vocabulary',15,'0'),
(214,54,'汇报工作-单元1-语法讲解',2,'grammar',15,'0'),
(215,54,'汇报工作-单元1-听力练习',3,'listening',15,'0'),
(216,54,'汇报工作-单元1-口语训练',4,'speaking',15,'0'),
(217,55,'あいうえお-单词学习',1,'vocabulary',15,'0'),
(218,55,'あいうえお-语法讲解',2,'grammar',15,'0'),
(219,55,'あいうえお-听力练习',3,'listening',15,'0'),
(220,55,'あいうえお-口语训练',4,'speaking',15,'0'),
(221,56,'かきくけこ-单词学习',1,'vocabulary',15,'0'),
(222,56,'かきくけこ-语法讲解',2,'grammar',15,'0'),
(223,56,'かきくけこ-听力练习',3,'listening',15,'0'),
(224,56,'かきくけこ-口语训练',4,'speaking',15,'0'),
(225,57,'さしすせそ-单词学习',1,'vocabulary',15,'0'),
(226,57,'さしすせそ-语法讲解',2,'grammar',15,'0'),
(227,57,'さしすせそ-听力练习',3,'listening',15,'0'),
(228,57,'さしすせそ-口语训练',4,'speaking',15,'0'),
(229,58,'たちつてと-单词学习',1,'vocabulary',15,'0'),
(230,58,'たちつてと-语法讲解',2,'grammar',15,'0'),
(231,58,'たちつてと-听力练习',3,'listening',15,'0'),
(232,58,'たちつてと-口语训练',4,'speaking',15,'0'),
(233,59,'なにぬねの-单词学习',1,'vocabulary',15,'0'),
(234,59,'なにぬねの-语法讲解',2,'grammar',15,'0'),
(235,59,'なにぬねの-听力练习',3,'listening',15,'0'),
(236,59,'なにぬねの-口语训练',4,'speaking',15,'0'),
(237,60,'はひふへほ-单词学习',1,'vocabulary',15,'0'),
(238,60,'はひふへほ-语法讲解',2,'grammar',15,'0'),
(239,60,'はひふへほ-听力练习',3,'listening',15,'0'),
(240,60,'はひふへほ-口语训练',4,'speaking',15,'0'),
(241,61,'まみむめも-单词学习',1,'vocabulary',15,'0'),
(242,61,'まみむめも-语法讲解',2,'grammar',15,'0'),
(243,61,'まみむめも-听力练习',3,'listening',15,'0'),
(244,61,'まみむめも-口语训练',4,'speaking',15,'0'),
(245,62,'やゆよ-单词学习',1,'vocabulary',15,'0'),
(246,62,'やゆよ-语法讲解',2,'grammar',15,'0'),
(247,62,'やゆよ-听力练习',3,'listening',15,'0'),
(248,62,'やゆよ-口语训练',4,'speaking',15,'0'),
(249,63,'らりるれろ-单词学习',1,'vocabulary',15,'0'),
(250,63,'らりるれろ-语法讲解',2,'grammar',15,'0'),
(251,63,'らりるれろ-听力练习',3,'listening',15,'0'),
(252,63,'らりるれろ-口语训练',4,'speaking',15,'0'),
(253,64,'わをん-单词学习',1,'vocabulary',15,'0'),
(254,64,'わをん-语法讲解',2,'grammar',15,'0'),
(255,64,'わをん-听力练习',3,'listening',15,'0'),
(256,64,'わをん-口语训练',4,'speaking',15,'0'),
(257,65,'アイウエオ-单词学习',1,'vocabulary',15,'0'),
(258,65,'アイウエオ-语法讲解',2,'grammar',15,'0'),
(259,65,'アイウエオ-听力练习',3,'listening',15,'0'),
(260,65,'アイウエオ-口语训练',4,'speaking',15,'0'),
(261,66,'ガギグゲゴ-单词学习',1,'vocabulary',15,'0'),
(262,66,'ガギグゲゴ-语法讲解',2,'grammar',15,'0'),
(263,66,'ガギグゲゴ-听力练习',3,'listening',15,'0'),
(264,66,'ガギグゲゴ-口语训练',4,'speaking',15,'0'),
(265,67,'促音与长音-单词学习',1,'vocabulary',15,'0'),
(266,67,'促音与长音-语法讲解',2,'grammar',15,'0'),
(267,67,'促音与长音-听力练习',3,'listening',15,'0'),
(268,67,'促音与长音-口语训练',4,'speaking',15,'0'),
(269,68,'容易混淆的假名-单词学习',1,'vocabulary',15,'0'),
(270,68,'容易混淆的假名-语法讲解',2,'grammar',15,'0'),
(271,68,'容易混淆的假名-听力练习',3,'listening',15,'0'),
(272,68,'容易混淆的假名-口语训练',4,'speaking',15,'0'),
(273,69,'こんにちは/こんばんは-单词学习',1,'vocabulary',15,'0'),
(274,69,'こんにちは/こんばんは-语法讲解',2,'grammar',15,'0'),
(275,69,'こんにちは/こんばんは-听力练习',3,'listening',15,'0'),
(276,69,'こんにちは/こんばんは-口语训练',4,'speaking',15,'0'),
(277,70,'おはようございます/さようなら-单词学习',1,'vocabulary',15,'0'),
(278,70,'おはようございます/さようなら-语法讲解',2,'grammar',15,'0'),
(279,70,'おはようございます/さようなら-听力练习',3,'listening',15,'0'),
(280,70,'おはようございます/さようなら-口语训练',4,'speaking',15,'0'),
(281,71,'ありがとうございます/すみません-单词学习',1,'vocabulary',15,'0'),
(282,71,'ありがとうございます/すみません-语法讲解',2,'grammar',15,'0'),
(283,71,'ありがとうございます/すみません-听力练习',3,'listening',15,'0'),
(284,71,'ありがとうございます/すみません-口语训练',4,'speaking',15,'0'),
(285,72,'初めまして/お疲れ様です-单词学习',1,'vocabulary',15,'0'),
(286,72,'初めまして/お疲れ様です-语法讲解',2,'grammar',15,'0'),
(287,72,'初めまして/お疲れ様です-听力练习',3,'listening',15,'0'),
(288,72,'初めまして/お疲れ様です-口语训练',4,'speaking',15,'0'),
(289,73,'いただきます/ごちそうさまでした-单词学习',1,'vocabulary',15,'0'),
(290,73,'いただきます/ごちそうさまでした-语法讲解',2,'grammar',15,'0'),
(291,73,'いただきます/ごちそうさまでした-听力练习',3,'listening',15,'0'),
(292,73,'いただきます/ごちそうさまでした-口语训练',4,'speaking',15,'0'),
(293,74,'颜色词汇-单词学习',1,'vocabulary',15,'0'),
(294,74,'颜色词汇-语法讲解',2,'grammar',15,'0'),
(295,74,'颜色词汇-听力练习',3,'listening',15,'0'),
(296,74,'颜色词汇-口语训练',4,'speaking',15,'0'),
(297,75,'数字1-10-单词学习',1,'vocabulary',15,'0'),
(298,75,'数字1-10-语法讲解',2,'grammar',15,'0'),
(299,75,'数字1-10-听力练习',3,'listening',15,'0'),
(300,75,'数字1-10-口语训练',4,'speaking',15,'0'),
(301,76,'人称代词-单词学习',1,'vocabulary',15,'0'),
(302,76,'人称代词-语法讲解',2,'grammar',15,'0'),
(303,76,'人称代词-听力练习',3,'listening',15,'0'),
(304,76,'人称代词-口语训练',4,'speaking',15,'0'),
(305,77,'基本动词-单词学习',1,'vocabulary',15,'0'),
(306,77,'基本动词-语法讲解',2,'grammar',15,'0'),
(307,77,'基本动词-听力练习',3,'listening',15,'0'),
(308,77,'基本动词-口语训练',4,'speaking',15,'0'),
(309,78,'常用名词-单词学习',1,'vocabulary',15,'0'),
(310,78,'常用名词-语法讲解',2,'grammar',15,'0'),
(311,78,'常用名词-听力练习',3,'listening',15,'0'),
(312,78,'常用名词-口语训练',4,'speaking',15,'0'),
(313,79,'あいうえお复习-单词学习',1,'vocabulary',15,'0'),
(314,79,'あいうえお复习-语法讲解',2,'grammar',15,'0'),
(315,79,'あいうえお复习-听力练习',3,'listening',15,'0'),
(316,79,'あいうえお复习-口语训练',4,'speaking',15,'0'),
(317,80,'bpmf声母-单词学习',1,'vocabulary',15,'0'),
(318,80,'bpmf声母-语法讲解',2,'grammar',15,'0'),
(319,80,'bpmf声母-听力练习',3,'listening',15,'0'),
(320,80,'bpmf声母-口语训练',4,'speaking',15,'0'),
(321,81,'dtnl声母-单词学习',1,'vocabulary',15,'0'),
(322,81,'dtnl声母-语法讲解',2,'grammar',15,'0'),
(323,81,'dtnl声母-听力练习',3,'listening',15,'0'),
(324,81,'dtnl声母-口语训练',4,'speaking',15,'0'),
(325,82,'gkh声母-单词学习',1,'vocabulary',15,'0'),
(326,82,'gkh声母-语法讲解',2,'grammar',15,'0'),
(327,82,'gkh声母-听力练习',3,'listening',15,'0'),
(328,82,'gkh声母-口语训练',4,'speaking',15,'0'),
(329,83,'jqx声母-单词学习',1,'vocabulary',15,'0'),
(330,83,'jqx声母-语法讲解',2,'grammar',15,'0'),
(331,83,'jqx声母-听力练习',3,'listening',15,'0'),
(332,83,'jqx声母-口语训练',4,'speaking',15,'0'),
(333,84,'zhchshr声母-单词学习',1,'vocabulary',15,'0'),
(334,84,'zhchshr声母-语法讲解',2,'grammar',15,'0'),
(335,84,'zhchshr声母-听力练习',3,'listening',15,'0'),
(336,84,'zhchshr声母-口语训练',4,'speaking',15,'0'),
(337,85,'zcs声母-单词学习',1,'vocabulary',15,'0'),
(338,85,'zcs声母-语法讲解',2,'grammar',15,'0'),
(339,85,'zcs声母-听力练习',3,'listening',15,'0'),
(340,85,'zcs声母-口语训练',4,'speaking',15,'0'),
(341,86,'ai/ei/ao/ou复韵母-单词学习',1,'vocabulary',15,'0'),
(342,86,'ai/ei/ao/ou复韵母-语法讲解',2,'grammar',15,'0'),
(343,86,'ai/ei/ao/ou复韵母-听力练习',3,'listening',15,'0'),
(344,86,'ai/ei/ao/ou复韵母-口语训练',4,'speaking',15,'0'),
(345,87,'an/en/ang/eng鼻韵母-单词学习',1,'vocabulary',15,'0'),
(346,87,'an/en/ang/eng鼻韵母-语法讲解',2,'grammar',15,'0'),
(347,87,'an/en/ang/eng鼻韵母-听力练习',3,'listening',15,'0'),
(348,87,'an/en/ang/eng鼻韵母-口语训练',4,'speaking',15,'0'),
(349,88,'横竖撇捺-单词学习',1,'vocabulary',15,'0'),
(350,88,'横竖撇捺-语法讲解',2,'grammar',15,'0'),
(351,88,'横竖撇捺-听力练习',3,'listening',15,'0'),
(352,88,'横竖撇捺-口语训练',4,'speaking',15,'0'),
(353,89,'日月山水-单词学习',1,'vocabulary',15,'0'),
(354,89,'日月山水-语法讲解',2,'grammar',15,'0'),
(355,89,'日月山水-听力练习',3,'listening',15,'0'),
(356,89,'日月山水-口语训练',4,'speaking',15,'0'),
(357,90,'亻艹氵钅-单词学习',1,'vocabulary',15,'0'),
(358,90,'亻艹氵钅-语法讲解',2,'grammar',15,'0'),
(359,90,'亻艹氵钅-听力练习',3,'listening',15,'0'),
(360,90,'亻艹氵钅-口语训练',4,'speaking',15,'0'),
(361,91,'语思学好-单词学习',1,'vocabulary',15,'0'),
(362,91,'语思学好-语法讲解',2,'grammar',15,'0'),
(363,91,'语思学好-听力练习',3,'listening',15,'0'),
(364,91,'语思学好-口语训练',4,'speaking',15,'0');

-- Vocabulary
INSERT INTO edu_vocabulary (unit_id, word, language, pronunciation, part_of_speech, definition, example_sentences, del_flag) VALUES
(1, 'hello', 'en', '/həˈloʊ/', 'interj.', '你好；喂（打电话或打招呼时用）', 'Hello! Nice to meet you.
Hello, may I speak to Mr. Smith?', '0'),
(1, 'good morning', 'en', '/ɡʊd ˈmɔːrnɪŋ/', 'phrase', '早上好（上午问候语）', 'Good morning, class!
Good morning! Did you sleep well?', '0'),
(1, 'how are you', 'en', '/haʊ ɑːr juː/', 'phrase', '你好吗？（询问对方近况）', 'How are you doing today?
How are you? I am fine, thank you.', '0'),
(1, 'nice to meet you', 'en', '/naɪs tuː miːt juː/', 'phrase', '很高兴认识你（初次见面时用）', 'Nice to meet you, Sarah.
It is nice to meet you too!', '0'),
(1, 'goodbye', 'en', '/ɡʊdˈbaɪ/', 'interj.', '再见；告别', 'Goodbye! See you tomorrow.
Goodbye and take care!', '0'),
(1, 'thank you', 'en', '/θæŋk juː/', 'phrase', '谢谢你；感谢', 'Thank you for your help.
Thank you very much!', '0'),
(2, 'name', 'en', '/neɪm/', 'n.', '名字；名称', 'What is your name?
My name is Alice.', '0'),
(2, 'come from', 'en', '/kʌm frɒm/', 'phr. v.', '来自；出生于', 'Where do you come from?
I come from Shanghai.', '0'),
(2, 'student', 'en', '/ˈstuːdnt/', 'n.', '学生', 'She is a university student.
He is an exchange student from Japan.', '0'),
(2, 'engineer', 'en', '/ˌendʒɪˈnɪr/', 'n.', '工程师', 'My father works as an engineer.
She is a software engineer at Google.', '0'),
(3, 'father', 'en', '/ˈfɑːðər/', 'n.', '父亲；爸爸', 'My father is a doctor.
I look like my father.', '0'),
(3, 'mother', 'en', '/ˈmʌðər/', 'n.', '母亲；妈妈', 'My mother cooks delicious food.
Happy Mother\'s Day!', '0'),
(3, 'brother', 'en', '/ˈbrʌðər/', 'n.', '兄弟；弟兄', 'I have one older brother.
My brother and I play soccer together.', '0'),
(3, 'sister', 'en', '/ˈsɪstər/', 'n.', '姐妹；姐妹', 'My younger sister is very cute.
She is like a sister to me.', '0'),
(3, 'family', 'en', '/ˈfæmɪli/', 'n.', '家庭；家人', 'I love my family very much.
We had a family dinner last night.', '0'),
(4, 'tall', 'en', '/tɔːl/', 'adj.', '高的（身材或物体）', 'He is very tall, about six feet.
The building is tall and modern.', '0'),
(4, 'beautiful', 'en', '/ˈbjuːtɪfl/', 'adj.', '美丽的；漂亮的', 'The view from here is beautiful.
She looks beautiful in that dress.', '0'),
(4, 'friendly', 'en', '/ˈfrendli/', 'adj.', '友好的；亲切的', 'The people here are very friendly.
He has a friendly smile.', '0'),
(4, 'young', 'en', '/jʌŋ/', 'adj.', '年轻的；年少的', 'She is young but very talented.
Young people love this music.', '0'),
(5, 'wake up', 'en', '/weɪk ʌp/', 'phr. v.', '醒来；醒过来', 'I wake up at seven every day.
Please wake me up at six tomorrow.', '0'),
(5, 'breakfast', 'en', '/ˈbrekfəst/', 'n.', '早餐；早饭', 'I always have breakfast at home.
Breakfast is the most important meal of the day.', '0'),
(5, 'usually', 'en', '/ˈjuːʒuəli/', 'adv.', '通常；一般地', 'I usually go to bed at eleven.
She usually takes the bus to work.', '0'),
(5, 'every day', 'en', '/ˈevri deɪ/', 'adv.', '每天；天天', 'I exercise every day.
He reads English every morning.', '0'),
(6, 'sorry', 'en', '/ˈsɒri/', 'interj./adj.', '对不起；抱歉的', 'Sorry, I am late.
I am sorry for the mistake.', '0'),
(6, 'excuse me', 'en', '/ɪkˈskjuːz miː/', 'phrase', '打扰一下；借过', 'Excuse me, where is the station?
Excuse me, can I ask a question?', '0'),
(6, 'apologize', 'en', '/əˈpɒlədʒaɪz/', 'v.', '道歉；谢罪', 'I sincerely apologize for the mistake.
You should apologize to her.', '0'),
(6, 'welcome', 'en', '/ˈwelkəm/', 'adj./v./interj.', '受欢迎的；欢迎；不客气', 'You are always welcome here.
- Thank you. - You are welcome.', '0'),
(7, 'happy', 'en', '/ˈhæpi/', 'adj.', '高兴的；快乐的', 'I am so happy today!
Happy birthday to you!', '0'),
(7, 'sad', 'en', '/sæd/', 'adj.', '悲伤的；难过的', 'She looks sad today.
I feel sad when it rains.', '0'),
(7, 'angry', 'en', '/ˈæŋɡri/', 'adj.', '生气的；愤怒的', 'Don\'t be angry with me.
He was angry about the news.', '0'),
(7, 'excited', 'en', '/ɪkˈsaɪtɪd/', 'adj.', '兴奋的；激动的', 'I am excited about the trip.
She was excited to hear the good news.', '0'),
(8, 'celebration', 'en', '/ˌselɪˈbreɪʃn/', 'n.', '庆祝；庆典', 'We had a big celebration for New Year.
The party was a great celebration.', '0'),
(8, 'invite', 'en', '/ɪnˈvaɪt/', 'v.', '邀请；招待', 'She invited me to her birthday party.
Thanks for inviting me.', '0'),
(8, 'amazing', 'en', '/əˈmeɪzɪŋ/', 'adj.', '令人惊叹的；了不起的', 'The view from the top is amazing.
That is an amazing idea!', '0'),
(8, 'party', 'en', '/ˈpɑːrti/', 'n.', '聚会；派对', 'Are you coming to the party tonight?
We had a great party last weekend.', '0'),
(9, 'hospital', 'en', '/ˈhɒspɪtl/', 'n.', '医院', 'He went to the hospital yesterday.
The hospital is near my home.', '0'),
(9, 'doctor', 'en', '/ˈdɒktər/', 'n.', '医生；博士', 'I need to see a doctor.
The doctor said I should rest more.', '0'),
(9, 'fever', 'en', '/ˈfiːvər/', 'n.', '发烧；发热', 'I have a high fever.
She has a fever of 39 degrees.', '0'),
(9, 'medicine', 'en', '/ˈmedsn/', 'n.', '药；医学', 'Take this medicine three times a day.
She studies medicine at university.', '0'),
(9, 'ambulance', 'en', '/ˈæmbjələns/', 'n.', '救护车', 'Call an ambulance immediately!
The ambulance arrived within five minutes.', '0'),
(9, 'prescription', 'en', '/prɪˈskrɪpʃn/', 'n.', '处方；药方', 'The doctor wrote a prescription for me.
You need a prescription to buy this medicine.', '0'),
(10, 'deadline', 'en', '/ˈdedlaɪn/', 'n.', '截止日期；最后期限', 'The deadline for the project is Friday.
We need to meet the deadline.', '0'),
(10, 'meeting', 'en', '/ˈmiːtɪŋ/', 'n.', '会议；会面', 'We have a meeting at 3 PM.
The meeting lasted two hours.', '0'),
(10, 'report', 'en', '/rɪˈpɔːrt/', 'n./v.', '报告；汇报', 'Please submit your report by Monday.
He reported the incident to the police.', '0'),
(10, 'project', 'en', '/ˈprɒdʒekt/', 'n.', '项目；工程', 'We are working on a new project.
The project will take three months.', '0'),
(11, 'あいうえお', 'ja', '/a i u e o/', 'hiragana', '日语五十音图第一行，基础元音', 'あいうえおは日本語の基礎です。
五十音図の最初の行です。', '0'),
(11, 'かきくけこ', 'ja', '/ka ki ku ke ko/', 'hiragana', 'か行假名，清音', 'かきくけこは濁点でがぎぐげごになります。
日本語の基本文字です。', '0'),
(11, 'さしすせそ', 'ja', '/sa shi su se so/', 'hiragana', 'さ行假名，し读作shi', 'さしすせそ、注意：しは「shi」と発音します。
清音の一行です。', '0'),
(11, 'たちつてと', 'ja', '/ta chi tsu te to/', 'hiragana', 'た行假名，ち读chi、つ读tsu', 'ち→chi、つ→tsu、特殊な読み方に注意！
たちつてとは重要です。', '0'),
(11, 'なにぬねの', 'ja', '/na ni nu ne no/', 'hiragana', 'な行假名', 'なにぬねのは鼻音が多い行です。
発音に注意しましょう。', '0'),
(12, 'はひふへほ', 'ja', '/ha hi fu he ho/', 'hiragana', 'は行，ふ读fu不是hu', 'ふは「fu」と発音、「hu」ではありません！
は=ha、ふ=fu、ほ=ho', '0'),
(12, 'まみむめも', 'ja', '/ma mi mu me mo/', 'hiragana', 'ま行，鼻音m系列', 'まみむめも、全部「m」で始まる音です。
発音しやすい行です。', '0'),
(12, 'やゆよ', 'ja', '/ya yu yo/', 'hiragana', 'や行只有三个假名', 'やゆよ、や行は三文字だけです。
拗音の基礎になります。', '0'),
(12, 'らりるれろ', 'ja', '/ra ri ru re ro/', 'hiragana', 'ら行，舌尖弹音r/l', 'らりるれろ、英語のrとlの中間のような音です。
舌先を軽く弾いて発音します。', '0'),
(12, 'わをん', 'ja', '/wa wo n/', 'hiragana', 'わ行和拨音ん', 'わをん、五十音図の最後です。
んは撥音（ん音）と言います。', '0'),
(13, 'こんにちは', 'ja', '/kon.nichi.wa/', 'greeting', '白天使用的问候语（约11点~18点）', 'こんにちは、田中さん。（你好，田中先生。）
こんにちは、お久しぶりです。（好久不见。）', '0'),
(13, 'こんばんは', 'ja', '/kon.ban.wa/', 'greeting', '晚上使用的问候语（18点以后）', 'こんばんは。（晚上好。）
こんばんは、お疲れ様でした。', '0'),
(13, 'おはようございます', 'ja', '/o.ha.you.go.zai.ma.su/', 'greeting', '早上好（较正式的用法）', 'おはようございます！（早上好！）
毎朝、おはようございますと言いましょう。', '0'),
(13, 'さようなら', 'ja', '/sa.you.na.ra/', 'greeting', '告别用语（可能一段时间不见）', 'さようなら、また明日。（再见，明天见。）
さようなら、お元気で。（保重。）', '0'),
(13, 'ありがとうございます', 'ja', '/a.ri.ga.tou.go.zai.ma.su/', 'greeting', '谢谢（正式礼貌表达）', 'ありがとうございます。（非常感谢。）
どういたしまして。（不客气。）', '0'),
(14, 'すみません', 'ja', '/su.mi.ma.sen/', 'greeting', '对不起/不好意思（万能词）', 'すみません、遅れました。（对不起，我迟到了。）
すみません、これいくらですか？（请问这个多少钱？）', '0'),
(14, '初めまして', 'ja', '/hajimemashite/', 'greeting', '初次见面时说的第一句话', '初めまして、田中と申します。
初めまして、どうぞよろしく。', '0'),
(14, 'お疲れ様です', 'ja', '/otsukaresamadesu/', 'greeting', '同事之间常用的问候语', 'お疲れ様です！今日も一日お疲れ様でした。
帰る前に一言「お疲れ様」を言うのがマナーです。', '0'),
(14, 'いただきます', 'ja', '/itadakimasu/', 'greeting', '开饭前说的"我开动了"', 'いただきます！（我开动了！）
食事の前に必ず言う言葉です。', '0'),
(14, 'ごちそうさまでした', 'ja', '/gochisousamadeshita/', 'greeting', '饭后说的"多谢款待"', 'ごちそうさまでした！（多谢款待！）
美味しかったです。ごちそうさまでした。', '0'),
(15, 'あか', 'ja', '/aka/', 'adj./n.', '红色；红色的', 'あかいリンゴ（红苹果）
あかちゃん（婴儿）', '0'),
(15, 'あお', 'ja', '/ao/', 'adj./n.', '蓝色；蓝色的', 'あおい空（蓝天）
あおい海（蓝色的大海）', '0'),
(15, 'しろ', 'ja', '/shiro/', 'adj./n.', '白色；白色的', 'しろい雲（白云）
しろい雪（白色的雪）', '0'),
(15, 'くろ', 'ja', '/kuro/', 'adj./n.', '黑色；黑色的', 'くろい猫（黑猫）
くろい髪（黑色的头发）', '0'),
(16, 'いち', 'ja', '/itɕi/', 'numeral', '数字1', 'いちがつ（一月）
いちじ（一点钟）', '0'),
(16, 'に', 'ja', '/ni/', 'numeral', '数字2', 'にがつ（二月）
にじ（两点钟）', '0'),
(16, 'さん', 'ja', '/san/', 'numeral', '数字3', 'さんがつ（三月）
さんじ（三点钟）', '0'),
(16, 'よん/し', 'ja', '/yon/ʃi/', 'numeral', '数字4（通常读よん）', 'よんがつ（四月）
よんじ（四点钟）', '0'),
(17, '促音', 'ja', '/Q (glottal stop)/', 'grammar', '小写っ表示停顿一拍', 'ちょっと（稍微）
かった（买了）', '0'),
(17, '长音', 'ja', '/long vowel/', 'grammar', '延长一拍的音', 'おばあさん（奶奶）
おじいさん（爷爷）', '0'),
(17, 'ぬ vs の', 'ja', '/nu/ vs /no/', 'pair', '容易混淆的一对假名', 'ぬるい（温吞的）
のだから（所以嘛）', '0'),
(17, 'ね vs れ', 'ja', '/ne/ vs /re/', 'pair', '容易混淆的一对假名', 'ねる（睡觉）
れる（能够）', '0'),
(18, '私', 'ja', '/watashi/', 'pronoun', '我（标准自称）', '私は学生です。（我是学生。）
私の名前は田中です。', '0'),
(18, 'あなた', 'ja', '/anata/', 'pronoun', '你（一般不常用）', 'あなたのお名前は？（您叫什么名字？）
あなたの本ですか？（这是你的书吗？）', '0'),
(18, '彼/彼女', 'ja', '/kare/kanojo/', 'pronoun', '他/她', '彼は日本人です。（他是日本人。）
彼女はきれいです。（她很漂亮。）', '0'),
(18, 'こちら/そちら/あちら', 'ja', '/ko.chira/so.chira/a.chira/', 'pronoun', '这边/那边/那边（近中远）', 'こちらは何ですか？（这是什么？）
そちらの人是谁？（那个人是谁？）', '0'),
(19, '食べる', 'ja', '/ta.be.ru/', 'verb (一段)', '吃（一段动词）', 'ご飯を食べます。（吃饭。）
何を食べたいですか？（想吃什么？）', '0'),
(19, '飲む', 'ja', '/no.mu/', 'verb (五段)', '喝（五段动词）', '水を飲みます。（喝水。）
お茶を飲みますか？（喝茶吗？）', '0'),
(19, '行く', 'ja', '/i.ku/', 'verb (五段)', '去', '学校へ行きます。（去学校。）
どこに行きますか？（去哪里？）', '0'),
(19, '来る', 'ja', '/ku.ru/', 'verb (不规则)', '来（不规则动词）', '日本に来ました。（来到日本。）
いつ来ますか？（什么时候来？）', '0'),
(20, '名前', 'ja', '/namae/', 'n.', '名字；姓名', 'お名前は何ですか。（您叫什么名字？）
私の名前は山田です。（我叫山田。）', '0'),
(20, '国', 'ja', '/kuni/', 'n.', '国家', 'どこの国から来ましたか。（您从哪个国家来的？）
中国は大きな国です。（中国是一个大国。）', '0'),
(20, '言葉', 'ja', '/kotoba/', 'n.', '语言；词语', '日本語は難しい言葉です。（日语是难的语言。）
新しい言葉を覚える。（记新单词。）', '0'),
(20, '時間', 'ja', '/jikan/', 'n.', '时间', '時間がありますか。（有时间吗？）
時間が経つの速いですね。（时间过得真快啊。）', '0'),
(21, 'a', 'zh', '/A/', 'final', '汉语拼音第一个单韵母，嘴巴张大', '阿姨(āyí)
阿婆(āpó)', '0'),
(21, 'o', 'zh', '/o/', 'final', '单韵母，嘴唇圆形', '哦(ò)
伯伯(óbó)', '0'),
(21, 'e', 'zh', '/ɤ/', 'final', '单韵母，嘴巴半开扁形', '鹅(é)
恶心(ě xīn)', '0'),
(21, 'i', 'zh', '/i/', 'final', '单韵母，牙齿对齐微笑状', '衣服(yīfu)
意思(yìsi)', '0'),
(21, 'u', 'zh', '/u/', 'final', '单韵母，嘴唇撮圆向前突出', '房屋(wū)
物体(wù tǐ)', '0'),
(22, 'b', 'zh', '/p/', 'initial', '不送气双唇清塞音', '爸爸(bàba)
杯子(bēizi)', '0'),
(22, 'p', 'zh', '/pʰ/', 'initial', '送气双唇清塞音', '怕(pà)
跑步(pǎo bù)', '0'),
(22, 'm', 'zh', '/m/', 'initial', '双唇鼻音', '妈妈(māma)
猫咪(māo mī)', '0'),
(22, 'f', 'zh', '/f/', 'initial', '唇齿清擦音', '头发(tóufà)
丰富(fēng fù)', '0'),
(23, 'd', 'zh', '/t/', 'initial', '不送气舌尖中清塞音', '弟弟(dìdi)
大家(dàjiā)', '0'),
(23, 't', 'zh', '/tʰ/', 'initial', '送气舌尖中清塞音', '他(tā)
特别(tè bié)', '0'),
(23, 'n', 'zh', '/n/', 'initial', '舌尖中鼻音', '你好(nǐ hǎo)
牛奶(niú nǎi)', '0'),
(23, 'l', 'zh', '/l/', 'initial', '舌尖中边音', '来了(lái le)
老师(lǎo shī)', '0'),
(24, 'g', 'zh', '/k/', 'initial', '不送气舌根清塞音', '哥哥(gēge)
公园(gōngyuán)', '0'),
(24, 'k', 'zh', '/kʰ/', 'initial', '送气舌根清塞音', '看(kàn)
开门(kāi mén)', '0'),
(24, 'h', 'zh', '/x/', 'initial', '舌根清擦音', '你好(nǐhǎo)
喝水(hēshuǐ)', '0'),
(25, 'j', 'zh', '/tɕ/', 'initial', '舌面不送气清塞擦音', '家里(jiāli)
见面(jiànmiàn)', '0'),
(25, 'q', 'zh', '/tɕʰ/', 'initial', '舌面送气清塞擦音', '一起去(yì qǐ qù)
气球(qìqiú)', '0'),
(25, 'x', 'zh', '/ɕ/', 'initial', '舌面清擦音', '学习(xuéxí)
喜欢(xǐhuan)', '0'),
(26, 'zh', 'zh', '/ʈʂ/', 'initial', '翘舌音，舌尖后不送气清塞擦音', '中国(Zhōngguó)
知道(zhīdao)', '0'),
(26, 'ch', 'zh', '/ʈʂʰ/', 'initial', '翘舌音，舌尖后送气清塞擦音', '吃(chī)
吃饭(chī fàn)', '0'),
(26, 'sh', 'zh', '/ʂ/', 'initial', '翘舌音，舌尖后清擦音', '是(shì)
老师(lǎoshī)', '0'),
(26, 'r', 'zh', '/ʐ/', 'initial', '翘舌音，舌尖后浊擦音', '日(rì)
容易(róngyì)', '0'),
(27, 'z', 'zh', '/ts/', 'initial', '平舌音，舌尖前不送气清塞擦音', '自在(zìzai)
写字(xiě zì)', '0'),
(27, 'c', 'zh', '/tsʰ/', 'initial', '平舌音，舌尖前送气清塞擦音', '才(cái)
吃饭(chī fàn)', '0'),
(27, 's', 'zh', '/s/', 'initial', '平舌音，舌尖前清擦音', '丝(sī)
说话(shuōhuà)', '0'),
(28, 'ai', 'zh', '/aɪ/', 'compound final', '复韵母ai，由a滑向i', '爱(ài)
白(bái)', '0'),
(28, 'ei', 'zh', '/eɪ/', 'compound final', '复韵母ei，由e滑向i', '杯(bēi)
妹(mèi)', '0'),
(28, 'ao', 'zh', '/ɑʊ/', 'compound final', '复韵母ao，由a滑向o', '奥(ào)
高(gāo)', '0'),
(28, 'ou', 'zh', '/oʊ/', 'compound final', '复韵母ou，由o滑向u', '欧(ōu)
手(shǒu)', '0'),
(29, 'an', 'zh', '/an/', 'nasal final', '前鼻韵母an', '安(ān)
半(bàn)', '0'),
(29, 'en', 'zh', '/ən/', 'nasal final', '前鼻韵母en', '恩(ēn)
门(mén)', '0'),
(29, 'ang', 'zh', '/ɑŋ/', 'nasal final', '后鼻韵母ang', '昂(áng)
帮(bāng)', '0'),
(29, 'eng', 'zh', '/əŋ/', 'nasal final', '后鼻韵母eng', '哼(hēng)
风(fēng)', '0');
