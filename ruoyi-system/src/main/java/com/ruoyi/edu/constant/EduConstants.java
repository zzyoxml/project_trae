package com.ruoyi.edu.constant;

/**
 * 教育平台常量定义
 *
 * @author LingLearn
 */
public class EduConstants {

    /**
     * 学习语言常量
     */
    public static class Language {
        public static final String ENGLISH = "en";
        public static final String JAPANESE = "ja";
        public static final String CHINESE = "zh";

        private Language() {
            // 私有构造函数防止实例化
        }
    }

    /**
     * 学习等级常量
     */
    public static class Level {
        public static final String BEGINNER = "beginner";
        public static final String ELEMENTARY = "elementary";
        public static final String INTERMEDIATE = "intermediate";
        public static final String ADVANCED = "advanced";

        private Level() {
        }
    }

    /**
     * 课时类型常量
     */
    public static class LessonType {
        public static final String VOCABULARY = "vocabulary";
        public static final String GRAMMAR = "grammar";
        public static final String LISTENING = "listening";
        public static final String SPEAKING = "speaking";
        public static final String QUIZ = "quiz";
        public static final String VIDEO = "video";

        private LessonType() {
        }
    }

    /**
     * 学习活动类型常量
     */
    public static class ActivityType {
        public static final String VOCABULARY = "vocabulary";
        public static final String GRAMMAR = "grammar";
        public static final String LISTENING = "listening";
        public static final String SPEAKING = "speaking";
        public static final String QUIZ = "quiz";
        public static final String PRACTICE = "practice";

        private ActivityType() {
        }
    }

    /**
     * 进度状态常量
     */
    public static class ProgressStatus {
        public static final String NOT_STARTED = "not_started";
        public static final String IN_PROGRESS = "in_progress";
        public static final String COMPLETED = "completed";
        public static final String LOCKED = "locked";

        private ProgressStatus() {
        }
    }

    /**
     * 成就类型常量
     */
    public static class AchievementType {
        public static final String STUDY = "study";
        public static final String STREAK = "streak";
        public static final String SCORE = "score";
        public static final String SOCIAL = "social";
        public static final String COLLECTION = "collection";

        private AchievementType() {
        }
    }

    /**
     * 成就等级常量
     */
    public static class AchievementTier {
        public static final String BRONZE = "bronze";
        public static final String SILVER = "silver";
        public static final String GOLD = "gold";
        public static final String DIAMOND = "diamond";
        public static final String LEGENDARY = "legendary";

        private AchievementTier() {
        }
    }

    /**
     * 帖子类型常量
     */
    public static class PostType {
        public static final String DISCUSSION = "discussion";
        public static final String QUESTION = "question";
        public static final String TIP = "tip";
        public static final String SHARE = "share";
        public static final String ACHIEVEMENT = "achievement";

        private PostType() {
        }
    }

    /**
     * 帖子状态常量
     */
    public static class PostStatus {
        public static final String DRAFT = "draft";
        public static final String PUBLISHED = "published";
        public static final String HIDDEN = "hidden";

        private PostStatus() {
        }
    }

    /**
     * 闯关类型常量
     */
    public static class ChallengeType {
        public static final String VOCABULARY = "vocabulary";
        public static final String GRAMMAR = "grammar";
        public static final String LISTENING = "listening";
        public static final String SPEAKING = "speaking";
        public static final String MIXED = "mixed";

        private ChallengeType() {
        }
    }

    /**
     * 闯关状态常量
     */
    public static class ChallengeStatus {
        public static final String NOT_STARTED = "not_started";
        public static final String IN_PROGRESS = "in_progress";
        public static final String PASSED = "passed";
        public static final String FAILED = "failed";

        private ChallengeStatus() {
        }
    }

    /**
     * 用户学习时段常量
     */
    public static class LearningTime {
        public static final String MORNING = "morning";
        public static final String AFTERNOON = "afternoon";
        public static final String EVENING = "evening";

        private LearningTime() {
        }
    }

    /**
     * 排行榜类型常量
     */
    public static class LeaderboardType {
        public static final String TOTAL = "total";
        public static final String WEEKLY = "weekly";
        public static final String DAILY = "daily";
        public static final String STREAK = "streak";
        public static final String LEVEL = "level";

        private LeaderboardType() {
        }
    }

    /**
     * 缓存Key常量
     */
    public static class CacheKey {
        public static final String USER_PROFILE = "edu:user:profile:";
        public static final String COURSE_DETAILS = "edu:course:details:";
        public static final String LEADERBOARD = "edu:leaderboard:";
        public static final String ACHIEVEMENTS = "edu:achievements:";
        public static final String HOT_POSTS = "edu:hot_posts:";

        private CacheKey() {
        }
    }

    /**
     * 经验值常量
     */
    public static class Experience {
        public static final int LEVEL_THRESHOLD = 1000;  // 每1000点经验升一级
        public static final int LESSON_BASE_XP = 10;      // 课时基础经验
        public static final int CHALLENGE_BASE_XP = 100; // 闯关基础经验

        private Experience() {
        }
    }

    /**
     * 金币常量
     */
    public static class Coins {
        public static final int LESSON_BASE_COINS = 5;   // 课时基础金币
        public static final int CHALLENGE_BASE_COINS = 50; // 闯关基础金币

        private Coins() {
        }
    }
}
