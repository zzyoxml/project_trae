-- ============================================
-- 多语种学习平台数据恢复脚本
-- 版本: 1.0.0
-- 恢复日期: 2024-01-15
-- ============================================

USE ryvue;

-- ============================================
-- 1. 恢复用户扩展信息
-- ============================================
TRUNCATE TABLE edu_user_profile;

INSERT INTO edu_user_profile (user_id, native_language, learning_languages, proficiency_level, total_study_time, current_streak, longest_streak, total_points, level, experience_points, avatar_url, bio, create_time) VALUES
(1, 'zh', 'en,ja', 'intermediate', 1250, 15, 30, 2850, 5, 12500, 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin', '热爱学习的教育平台管理员', NOW()),
(2, 'zh', 'en', 'beginner', 450, 5, 10, 980, 3, 4500, 'https://api.dicebear.com/7.x/avataaars/svg?seed=张三', '英语学习爱好者，正在努力提升口语', NOW()),
(3, 'zh', 'ja', 'intermediate', 680, 8, 15, 1520, 4, 6800, 'https://api.dicebear.com/7.x/avataaars/svg?seed=李四', '日语N3备考中，喜欢看动漫学日语', NOW()),
(4, 'en', 'zh', 'beginner', 320, 3, 5, 720, 2, 3200, 'https://api.dicebear.com/7.x/avataaars/svg?seed=John', 'Learning Chinese, love the culture!', NOW()),
(5, 'ja', 'zh,en', 'intermediate', 890, 12, 20, 1980, 4, 8900, 'https://api.dicebear.com/7.x/avataaars/svg?seed=田中', '正在学习中文和英语，目标是成为翻译', NOW());

-- ============================================
-- 2. 恢复课程分类
-- ============================================
TRUNCATE TABLE edu_course_category;

INSERT INTO edu_course_category (category_name, category_code, display_order, description, status) VALUES
('英语课程', 'en', 1, '英语学习相关课程', '0'),
('日语课程', 'ja', 2, '日语学习相关课程', '0'),
('汉语课程', 'zh', 3, '汉语学习相关课程', '0'),
('口语训练', 'speaking', 4, '口语专项训练课程', '0'),
('听力训练', 'listening', 5, '听力专项训练课程', '0'),
('词汇记忆', 'vocabulary', 6, '词汇量提升课程', '0'),
('语法进阶', 'grammar', 7, '语法系统学习课程', '0');

-- ============================================
-- 3. 恢复课程数据
-- ============================================
TRUNCATE TABLE edu_course;

INSERT INTO edu_course (course_name, course_code, category_id, language, level, course_type, description, cover_image, total_duration, total_lessons, total_students, rating, rating_count, is_free, is_published, is_featured, tags, create_by, create_time) VALUES
('英语口语入门', 'EN001', 1, 'en', 'beginner', 'speaking', '专为英语初学者设计的口语基础课程，从音标开始，打好发音基础。包含日常问候、自我介绍、简单对话等实用场景。', 'https://images.unsplash.com/photo-1434030216411-0b793f4b4173?w=800', 180, 12, 1234, 4.85, 456, 1, 1, 1, '英语,口语,入门,零基础', 'admin', NOW()),
('英语语法精讲', 'EN002', 7, 'en', 'intermediate', 'grammar', '深入讲解英语时态、语态、从句等核心语法知识，配以大量例句和练习。', 'https://images.unsplash.com/photo-1456513080510-7bf3a84b82f8?w=800', 240, 16, 987, 4.72, 321, 0, 1, 0, '英语,语法,中级,系统学习', 'admin', NOW()),
('英语听力训练', 'EN003', 5, 'en', 'beginner', 'listening', '通过新闻、对话、电影片段等多种材料，全面提升英语听力理解能力。', 'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=800', 200, 15, 876, 4.68, 289, 0, 1, 1, '英语,听力,训练,提升', 'admin', NOW()),
('英语商务写作', 'EN004', 1, 'en', 'advanced', 'writing', '学习商务邮件、会议纪要、报告撰写等职场英语写作技能。', 'https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=800', 220, 14, 654, 4.90, 198, 0, 1, 0, '英语,商务,写作,职场', 'admin', NOW()),
('英语旅游口语', 'EN005', 1, 'en', 'beginner', 'speaking', '旅行必备英语，涵盖机场、酒店、餐厅、购物等场景的实用口语表达。', 'https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=800', 150, 10, 543, 4.75, 167, 1, 1, 0, '英语,旅游,口语,实用', 'admin', NOW()),

('日语五十音图', 'JA001', 2, 'ja', 'beginner', 'general', '从零开始学习日语假名，包括平假名和片假名的发音、书写和记忆技巧。', 'https://images.unsplash.com/photo-1528164344705-47542687000d?w=800', 120, 8, 1567, 4.92, 589, 1, 1, 1, '日语,五十音,入门,零基础', 'admin', NOW()),
('日语N3备考', 'JA002', 2, 'ja', 'intermediate', 'grammar', '针对JLPT N3级别的系统备考课程，涵盖词汇、语法、阅读、听力。', 'https://images.unsplash.com/photo-1526481280693-3bfa7568e0f3?w=800', 300, 20, 1234, 4.88, 456, 0, 1, 1, '日语,N3,备考,JLPT', 'admin', NOW()),
('日语口语中级', 'JA003', 4, 'ja', 'intermediate', 'speaking', '提升日语口语表达能力，涵盖日常会话、职场交流、文化交流等话题。', 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=800', 200, 15, 876, 4.65, 234, 0, 1, 0, '日语,口语,中级,表达', 'admin', NOW()),
('日语动漫听力', 'JA004', 5, 'ja', 'beginner', 'listening', '通过经典动漫片段学习日语，寓教于乐，提升听力同时了解日本文化。', 'https://images.unsplash.com/photo-1513311914127-a152cb7eb3a2?w=800', 180, 12, 765, 4.78, 189, 0, 1, 0, '日语,听力,动漫,兴趣', 'admin', NOW()),
('日语商务礼仪', 'JA005', 2, 'ja', 'advanced', 'business', '学习日本职场文化、商务用语、邮件书写和商务谈判技巧。', 'https://images.unsplash.com/photo-1497366216548-37526070297c?w=800', 250, 18, 432, 4.95, 123, 0, 1, 0, '日语,商务,职场,礼仪', 'admin', NOW()),

('汉语拼音基础', 'ZH001', 3, 'zh', 'beginner', 'general', '学习汉语拼音的发音规则、声调系统，为汉字学习打下坚实基础。', 'https://images.unsplash.com/photo-1503676260728-1c00da094a0b?w=800', 100, 8, 1876, 4.90, 678, 1, 1, 1, '汉语,拼音,入门,零基础', 'admin', NOW()),
('HSK2备考', 'ZH002', 3, 'zh', 'beginner', 'grammar', '针对HSK2级的系统备考课程，涵盖词汇、语法、汉字和练习。', 'https://images.unsplash.com/photo-1543286386-713bdd548da4?w=800', 180, 12, 1432, 4.85, 534, 0, 1, 1, '汉语,HSK,备考,HSK2', 'admin', NOW()),
('汉语日常会话', 'ZH003', 4, 'zh', 'intermediate', 'speaking', '日常生活场景的中文会话练习，包括购物、就医、问路等实用话题。', 'https://images.unsplash.com/photo-1480796927426-f609979314bd?w=800', 160, 12, 987, 4.72, 345, 0, 1, 0, '汉语,口语,日常,实用', 'admin', NOW()),
('汉字书写入门', 'ZH004', 3, 'zh', 'beginner', 'writing', '从基础汉字开始，学习常用汉字的书写规范和记忆方法。', 'https://images.unsplash.com/photo-1517971071642-34a2d3ecc9cd?w=800', 200, 15, 765, 4.68, 234, 0, 1, 0, '汉语,汉字,书写,入门', 'admin', NOW()),
('汉语商务沟通', 'ZH005', 3, 'zh', 'advanced', 'business', '学习商务场合的中文表达，包括商务谈判、会议主持、电话沟通等。', 'https://images.unsplash.com/photo-1450101499163-c8848c66ca85?w=800', 240, 16, 543, 4.93, 167, 0, 1, 0, '汉语,商务,沟通,职场', 'admin', NOW());

-- ============================================
-- 4. 恢复课程单元和课时
-- ============================================
TRUNCATE TABLE edu_course_unit;
TRUNCATE TABLE edu_course_lesson;

-- 英语口语入门 课程单元
INSERT INTO edu_course_unit (course_id, unit_name, unit_order, description, total_lessons, total_duration) VALUES
(1, '语音基础', 1, '英语音标和发音规则', 4, 60),
(1, '日常问候', 2, '问候语和礼貌用语', 4, 60),
(1, '自我介绍', 3, '如何用英语介绍自己', 4, 60);

INSERT INTO edu_course_lesson (unit_id, lesson_name, lesson_order, lesson_type, content, duration, is_free) VALUES
(1, '英语元音发音', 1, 'video', '学习英语元音a,e,i,o,u的发音', 15, 1),
(1, '英语辅音发音', 2, 'video', '学习英语辅音的发音技巧', 15, 1),
(1, '音标综合练习', 3, 'exercise', '音标听说读写综合训练', 15, 0),
(1, '单词拼读', 4, 'exercise', '使用音标拼读单词', 15, 0),
(2, 'Hello和Hi', 1, 'video', '见面问候的基本表达', 15, 1),
(2, 'Nice to meet you', 2, 'video', '初次见面的礼貌用语', 15, 1),
(2, '告别语', 3, 'video', '说再见的方式', 15, 0),
(2, '情景对话练习', 4, 'exercise', '模拟问候情景对话', 15, 0),
(3, 'My name is...', 1, 'video', '介绍自己的名字', 15, 1),
(3, 'I am from...', 2, 'video', '介绍自己来自哪里', 15, 0),
(3, 'My hobby is...', 3, 'video', '介绍自己的爱好', 15, 0),
(3, '自我介绍综合练习', 4, 'exercise', '完整自我介绍', 15, 0);

-- 日语五十音图 课程单元
INSERT INTO edu_course_unit (course_id, unit_name, unit_order, description, total_lessons, total_duration) VALUES
(6, '平假名', 1, '日语平假名学习', 4, 60),
(6, '片假名', 2, '日语片假名学习', 4, 60);

INSERT INTO edu_course_lesson (unit_id, lesson_name, lesson_order, lesson_type, content, duration, is_free) VALUES
(4, 'あ行か行', 1, 'video', '学习あいうえお和かきくけこ', 15, 1),
(4, 'さ行た行', 2, 'video', '学习さしすせそ和たちつてと', 15, 1),
(4, 'な行は行', 3, 'video', '学习なにぬねの和はひふへほ', 15, 0),
(4, 'ま行や行わ行', 4, 'video', '学习まみむめもやゆよ和わをん', 15, 0),
(5, 'ア行カ行', 1, 'video', '学习アいうエオ和カキクケコ', 15, 1),
(5, 'サ行タ行', 2, 'video', '学习サシスセソ和タチツテト', 15, 1),
(5, 'ナ行ハ行', 3, 'video', '学习ナニヌネノ和ハヒフヘホ', 15, 0),
(5, 'マ行ヤ行ワ行', 4, 'video', '学习マミムメモ和ヤユヨワヲン', 15, 0);

-- ============================================
-- 5. 恢复社区帖子数据
-- ============================================
TRUNCATE TABLE edu_post;
TRUNCATE TABLE edu_post_comment;

INSERT INTO edu_post (user_id, post_type, language, title, content, view_count, like_count, comment_count, status, create_by, create_time) VALUES
(2, 'experience', 'en', '英语学习方法分享', '<p>大家好！我学习英语已经一年了，今天来分享一些心得：</p><ol><li>每天早起听英语广播，养成习惯</li><li>使用Anki背单词，每天50个</li><li>看美剧学习地道表达</li><li>加入语言交换群练习口语</li></ol><p>坚持就是胜利，大家一起加油！</p>', 1234, 89, 23, 'published', 'admin', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(3, 'resource', 'ja', '日语学习资源推荐', '<p>推荐一些我常用的日语学习资源：</p><ul><li>沪江日语 - 词汇和语法讲解很详细</li><li>标准日本语 - 经典教材</li><li>日语N3备考书 - 历年真题</li><li>YouTube频道 - 日本综艺学习</li></ul><p>希望对大家有帮助！</p>', 987, 67, 15, 'published', 'admin', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(4, 'experience', 'zh', '学习汉语拼音的小技巧', '<p>作为外国学习者，我觉得拼音真的很难！分享一下我的学习技巧：</p><ol><li>先学声母，再学韵母</li><li>四声很重要，要多说多练</li><li>用拼音输入法打字帮助记忆</li><li>听中文歌曲学习声调</li></ol><p>加油！</p>', 876, 54, 12, 'published', 'admin', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2, 'question', 'en', '如何快速提高英语听力？', '<p>我的英语听力一直不好，听BBC新闻只能听懂30%。</p><p>大家有什么好的提高方法吗？</p><p>谢谢！</p>', 654, 32, 8, 'published', 'admin', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(5, '打卡', 'ja', '日语学习第30天打卡', '<p>今天完成了N3语法的第15课。</p><p>感觉进步很大，继续坚持！</p><p>明日目标：完成第16课</p>', 543, 45, 6, 'published', 'admin', NOW()),
(3, '资源', 'zh', 'HSK3备考资料分享', '<p>整理了一些HSK3备考资料：</p><ol><li>词汇表 - 包含所有需要掌握的词语</li><li>语法点总结 - 重点语法详细解释</li><li>模拟试题 - 5套完整模拟题</li></ol><p>需要的同学可以私信我！</p>', 765, 78, 19, 'published', 'admin', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(4, '经验', 'zh', '中文声调学习心得', '<p>四声真的很难！我的经验是：</p><ol><li>先理解每个声调的发音位置</li><li>多听多说，不要怕错</li><li>用唱中文歌的方式练习</li></ol><p>大家一起加油！</p>', 432, 28, 5, 'published', 'admin', DATE_SUB(NOW(), INTERVAL 6 DAY)),
(5, '经验', 'en', '英语学习App推荐', '<p>我用过的英语学习App：</p><ul><li>多邻国 - 游戏化学习很有趣</li><li>扇贝 - 背单词很好用</li><li>可可英语 - 资源丰富</li></ul><p>大家有什么好推荐吗？</p>', 321, 19, 4, 'published', 'admin', DATE_SUB(NOW(), INTERVAL 7 DAY));

INSERT INTO edu_post_comment (post_id, user_id, parent_comment_id, content, like_count, create_by, create_time) VALUES
(1, 3, NULL, '很实用的方法！特别是看美剧那个，我也在用', 12, 'admin', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(1, 4, NULL, '谢谢分享！我要试试Anki背单词', 8, 'admin', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(1, 5, 1, 'Anki确实很好用，推荐使用共享牌组', 5, 'admin', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2, 2, NULL, '沪江日语确实不错，我也在用', 15, 'admin', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2, 5, NULL, 'YouTube频道有哪些推荐吗？', 6, 'admin', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, 2, NULL, '唱中文歌学声调这个方法太棒了！', 9, 'admin', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, 5, NULL, '我推荐《甜蜜蜜》这首歌，声调很标准', 7, 'admin', NOW());

-- ============================================
-- 6. 恢复学习进度数据
-- ============================================
TRUNCATE TABLE edu_learning_progress;
TRUNCATE TABLE edu_learning_record;
TRUNCATE TABLE edu_user_course;

INSERT INTO edu_user_course (user_id, course_id, enrollment_date, progress_percent, status, start_date) VALUES
(1, 1, DATE_SUB(NOW(), INTERVAL 30 DAY), 85, 'learning', DATE_SUB(NOW(), INTERVAL 30 DAY)),
(1, 6, DATE_SUB(NOW(), INTERVAL 25 DAY), 60, 'learning', DATE_SUB(NOW(), INTERVAL 25 DAY)),
(1, 11, DATE_SUB(NOW(), INTERVAL 20 DAY), 40, 'learning', DATE_SUB(NOW(), INTERVAL 20 DAY)),
(2, 1, DATE_SUB(NOW(), INTERVAL 15 DAY), 75, 'learning', DATE_SUB(NOW(), INTERVAL 15 DAY)),
(2, 2, DATE_SUB(NOW(), INTERVAL 10 DAY), 30, 'learning', DATE_SUB(NOW(), INTERVAL 10 DAY)),
(3, 6, DATE_SUB(NOW(), INTERVAL 20 DAY), 90, 'learning', DATE_SUB(NOW(), INTERVAL 20 DAY)),
(3, 7, DATE_SUB(NOW(), INTERVAL 15 DAY), 55, 'learning', DATE_SUB(NOW(), INTERVAL 15 DAY)),
(4, 11, DATE_SUB(NOW(), INTERVAL 12 DAY), 65, 'learning', DATE_SUB(NOW(), INTERVAL 12 DAY)),
(4, 12, DATE_SUB(NOW(), INTERVAL 8 DAY), 25, 'learning', DATE_SUB(NOW(), INTERVAL 8 DAY)),
(5, 6, DATE_SUB(NOW(), INTERVAL 18 DAY), 50, 'learning', DATE_SUB(NOW(), INTERVAL 18 DAY)),
(5, 1, DATE_SUB(NOW(), INTERVAL 14 DAY), 45, 'learning', DATE_SUB(NOW(), INTERVAL 14 DAY)),
(5, 11, DATE_SUB(NOW(), INTERVAL 10 DAY), 35, 'learning', DATE_SUB(NOW(), INTERVAL 10 DAY));

INSERT IGNORE INTO edu_learning_progress (user_id, course_id, unit_id, lesson_id, status, progress_percent, best_score, attempt_count, time_spent, last_study_time) VALUES
(1, 1, 1, 1, 'completed', 100, 95, 1, 15, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 1, 1, 2, 'completed', 100, 88, 2, 20, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 1, 1, 3, 'learning', 60, 75, 1, 10, NOW()),
(1, 6, 4, 1, 'completed', 100, 92, 1, 15, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1, 6, 4, 2, 'learning', 45, 65, 1, 12, NOW()),
(2, 1, 1, 1, 'completed', 100, 90, 1, 15, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(2, 1, 1, 2, 'completed', 100, 85, 2, 18, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 6, 4, 1, 'completed', 100, 98, 1, 15, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, 6, 4, 2, 'completed', 100, 95, 1, 15, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, 6, 4, 3, 'learning', 70, 78, 1, 12, NOW()),
(4, 11, NULL, 1, 'completed', 100, 88, 1, 15, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(4, 11, NULL, 2, 'learning', 55, 70, 1, 10, NOW());

INSERT INTO edu_learning_record (user_id, lesson_id, course_id, activity_type, duration, score, max_score, accuracy, xp_earned, completed, create_time) VALUES
(1, 1, 1, 'video', 15, 95, 100, 95, 15, 1, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(1, 2, 1, 'video', 20, 88, 100, 88, 18, 1, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(1, 3, 1, 'exercise', 10, 75, 100, 75, 10, 0, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(1, 1, 6, 'video', 15, 92, 100, 92, 15, 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2, 1, 1, 'video', 15, 90, 100, 90, 15, 1, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(3, 1, 6, 'video', 15, 98, 100, 98, 15, 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 2, 6, 'video', 15, 95, 100, 95, 15, 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(4, 1, 11, 'video', 15, 88, 100, 88, 15, 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(5, 1, 6, 'video', 15, 85, 100, 85, 15, 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(5, 2, 6, 'video', 15, 80, 100, 80, 15, 1, DATE_SUB(NOW(), INTERVAL 1 DAY));

-- ============================================
-- 7. 恢复词汇和语法数据
-- ============================================
TRUNCATE TABLE edu_vocabulary;
TRUNCATE TABLE edu_grammar;

-- 英语词汇
INSERT INTO edu_vocabulary (word, language, pronunciation, part_of_speech, level, definition, difficulty) VALUES
('hello', 'en', '/həˈloʊ/', 'interjection', 'beginner', '用于问候的词语', 1),
('world', 'en', '/wɜːrld/', 'noun', 'beginner', '世界', 1),
('learn', 'en', '/lɜːrn/', 'verb', 'beginner', '学习', 1),
('study', 'en', '/ˈstʌdi/', 'verb', 'beginner', '学习，研究', 1),
('language', 'en', '/ˈlæŋɡwɪdʒ/', 'noun', 'beginner', '语言', 1),
('hello', 'ja', '/ja/', '感动词', 'beginner', '打招呼的用语', 1),
('世界', 'ja', '/sekai/', '名词', 'beginner', '世界', 1),
('你好', 'zh', '/nǐhǎo/', '动词', 'beginner', '用于问候', 1),
('学习', 'zh', '/xuéxí/', '动词', 'beginner', '通过学习获得知识技能', 1),
('语言', 'zh', '/yǔyán/', '名词', 'intermediate', '人类交流的工具', 2);

-- 日语语法
INSERT INTO edu_grammar (grammar_name, language, level, category, description, rule_formula, difficulty) VALUES
('名词です', 'ja', 'beginner', '判断句', '表示判断的句型，相当于"是..."', '名词1は 名词2です', 1),
('名词ではありません', 'ja', 'beginner', '判断句', '否定判断句型', '名词1は 名词2ではありません', 1),
('疑问句', 'zh', 'beginner', '疑问句', '使用吗、呢等语气词', '陈述句+吗？', 1),
('量词', 'zh', 'beginner', '数量表达', '表示数量的词', '数词+量词+名词', 1);

-- ============================================
-- 8. 确保教育管理菜单完整
-- ============================================
DELETE FROM sys_menu WHERE menu_id >= 2000;

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, visible, status, menu_type, icon, perms, create_by, create_time) VALUES
(2000, '教育管理', 0, 2, 'edu', NULL, '1', '0', '0', '0', 'M', 'education', '', 'admin', NOW()),
(2001, '课程管理', 2000, 1, 'course', 'edu/course/index', '1', '0', '0', '0', 'C', 'book', 'edu:course:list', 'admin', NOW()),
(2002, '学习数据', 2000, 2, 'learning', 'edu/learning/index', '1', '0', '0', '0', 'C', 'chart', 'edu:learning:list', 'admin', NOW()),
(2003, '社区管理', 2000, 3, 'community', 'edu/community/index', '1', '0', '0', '0', 'C', 'message', 'edu:community:list', 'admin', NOW()),
(2004, '成就管理', 2000, 4, 'achievement', 'edu/achievement/index', '1', '0', '0', '0', 'C', 'star', 'edu:achievement:list', 'admin', NOW());

-- 添加角色权限
DELETE FROM sys_role_menu WHERE menu_id >= 2000;
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2000), (1, 2001), (1, 2002), (1, 2003), (1, 2004);

-- ============================================
-- 完成提示
-- ============================================
SELECT '========================================' AS '';
SELECT '  数据恢复完成！' AS message;
SELECT '========================================' AS '';
SELECT '' AS '';

-- 验证恢复结果
SELECT 'edu_user_profile' AS table_name, COUNT(*) AS row_count FROM edu_user_profile
UNION ALL
SELECT 'edu_course', COUNT(*) FROM edu_course
UNION ALL
SELECT 'edu_post', COUNT(*) FROM edu_post
UNION ALL
SELECT 'edu_achievement', COUNT(*) FROM edu_achievement
UNION ALL
SELECT 'edu_learning_progress', COUNT(*) FROM edu_learning_progress
UNION ALL
SELECT 'sys_menu (教育)', COUNT(*) FROM sys_menu WHERE menu_id >= 2000;
