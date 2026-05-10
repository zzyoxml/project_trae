# Redis配置文件详解

## 1. application-redis.yml

```yaml
# Redis配置
spring:
  redis:
    # Redis服务器地址
    host: localhost
    # Redis服务器连接端口
    port: 6379
    # Redis数据库索引（默认为0）
    database: 0
    # Redis密码（为空）
    password: 
    # 连接超时时间（毫秒）
    timeout: 3000ms
    # 连接池配置
    lettuce:
      pool:
        # 最大连接数
        max-active: 8
        # 最大空闲连接数
        max-idle: 8
        # 最小空闲连接数
        min-idle: 0
        # 最大等待时间（毫秒）
        max-wait: -1ms
      # Redis客户端配置
      client:
        # 客户端类型
        type: lettuce
        # 连接池名称
        pool-name: lingua-redis-pool
```

## 2. RedisConfig.java

```java
package com.ruoyi.framework.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 * 
 * @author LingLearn
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * 配置RedisTemplate
     * 定义RedisTemplate的序列化方式
     *
     * @param connectionFactory Redis连接工厂
     * @return RedisTemplate实例
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用Jackson2JsonRedisSerializer进行序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 使用StringRedisSerializer作为Key的序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // 设置Key的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        // 设置Value的序列化方式
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * 配置RedisCacheManager
     * 定义缓存的过期时间和序列化方式
     *
     * @param connectionFactory Redis连接工厂
     * @return RedisCacheManager实例
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 配置序列化
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(om);

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 缓存过期时间
                .entryTtl(Duration.ofHours(24))
                // 序列化Key
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // 序列化Value
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                // 禁止缓存null值
                .disableCachingNullValues();

        // 配置不同的缓存过期时间
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // 用户信息缓存 - 30分钟
        cacheConfigurations.put("userInfo", config.entryTtl(Duration.ofMinutes(30)));
        
        // 课程信息缓存 - 1小时
        cacheConfigurations.put("courseInfo", config.entryTtl(Duration.ofHours(1)));
        
        // 排行榜缓存 - 5分钟
        cacheConfigurations.put("leaderboard", config.entryTtl(Duration.ofMinutes(5)));
        
        // 热门帖子缓存 - 10分钟
        cacheConfigurations.put("hotPosts", config.entryTtl(Duration.ofMinutes(10)));
        
        // 成就信息缓存 - 1小时
        cacheConfigurations.put("achievements", config.entryTtl(Duration.ofHours(1)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }
}
```

## 3. RedisKeys常量类

```java
package com.ruoyi.common.constant;

/**
 * Redis缓存Key常量
 * 
 * @author LingLearn
 */
public class RedisConstants {

    /**
     * 用户相关缓存Key
     */
    public static class User {
        // 用户信息缓存 (userId -> UserInfo)
        public static final String USER_INFO = "user:info:";
        
        // 用户Token缓存 (token -> userId)
        public static final String USER_TOKEN = "user:token:";
        
        // 用户学习统计缓存 (userId -> StudyStats)
        public static final String USER_STATS = "user:stats:";
        
        // 用户技能评分缓存 (userId -> SkillScores)
        public static final String USER_SKILLS = "user:skills:";
        
        // 用户成就缓存 (userId -> Achievements)
        public static final String USER_ACHIEVEMENTS = "user:achievements:";
        
        // 用户徽章缓存 (userId -> Badges)
        public static final String USER_BADGES = "user:badges:";
        
        // 用户学习进度缓存 (userId:courseId -> Progress)
        public static final String USER_PROGRESS = "user:progress:";
        
        // 缓存过期时间（秒）
        public static final long USER_INFO_EXPIRE = 1800;    // 30分钟
        public static final long USER_STATS_EXPIRE = 3600;   // 1小时
        public static final long USER_PROGRESS_EXPIRE = 1800; // 30分钟
    }

    /**
     * 课程相关缓存Key
     */
    public static class Course {
        // 课程详情缓存 (courseId -> CourseDetail)
        public static final String COURSE_INFO = "course:info:";
        
        // 课程列表缓存 (language:level:page -> CourseList)
        public static final String COURSE_LIST = "course:list:";
        
        // 精选课程缓存
        public static final String FEATURED_COURSES = "course:featured:";
        
        // 热门课程缓存
        public static final String POPULAR_COURSES = "course:popular:";
        
        // 课程单元缓存 (courseId -> Units)
        public static final String COURSE_UNITS = "course:units:";
        
        // 课程课时缓存 (unitId -> Lessons)
        public static final String COURSE_LESSONS = "course:lessons:";
        
        // 缓存过期时间
        public static final long COURSE_INFO_EXPIRE = 3600;    // 1小时
        public static final long COURSE_LIST_EXPIRE = 1800;   // 30分钟
        public static final long FEATURED_EXPIRE = 3600;      // 1小时
        public static final long POPULAR_EXPIRE = 1800;       // 30分钟
    }

    /**
     * 学习资源缓存Key
     */
    public static class Learning {
        // 单词缓存 (language:level:page -> VocabularyList)
        public static final String VOCABULARY_LIST = "learning:vocabulary:";
        
        // 语法缓存 (language:level:page -> GrammarList)
        public static final String GRAMMAR_LIST = "learning:grammar:";
        
        // 听力资源缓存 (language:level -> ListeningList)
        public static final String LISTENING_LIST = "learning:listening:";
        
        // 今日单词缓存 (userId -> TodayVocabulary)
        public static final String TODAY_VOCAB = "learning:today:vocab:";
        
        // 复习计划缓存 (userId -> ReviewPlan)
        public static final String REVIEW_PLAN = "learning:review:plan:";
        
        // 缓存过期时间
        public static final long VOCABULARY_EXPIRE = 86400;   // 24小时
        public static final long TODAY_VOCAB_EXPIRE = 43200; // 12小时
        public static final long REVIEW_PLAN_EXPIRE = 86400; // 24小时
    }

    /**
     * 社区相关缓存Key
     */
    public static class Community {
        // 帖子详情缓存 (postId -> PostDetail)
        public static final String POST_INFO = "community:post:";
        
        // 帖子列表缓存 (language:type:page -> PostList)
        public static final String POST_LIST = "community:posts:";
        
        // 热门帖子缓存
        public static final String HOT_POSTS = "community:hot:";
        
        // 帖子评论缓存 (postId -> Comments)
        public static final String POST_COMMENTS = "community:comments:";
        
        // 用户帖子缓存 (userId -> UserPosts)
        public static final String USER_POSTS = "community:user:posts:";
        
        // 缓存过期时间
        public static final long POST_INFO_EXPIRE = 1800;     // 30分钟
        public static final long POST_LIST_EXPIRE = 600;      // 10分钟
        public static final long HOT_POSTS_EXPIRE = 300;      // 5分钟
        public static final long COMMENTS_EXPIRE = 600;       // 10分钟
    }

    /**
     * 成就系统缓存Key
     */
    public static class Achievement {
        // 成就列表缓存
        public static final String ACHIEVEMENT_LIST = "achievement:list:";
        
        // 排行榜缓存 (type:language -> Leaderboard)
        public static final String LEADERBOARD = "achievement:leaderboard:";
        
        // 用户排名缓存 (userId:type:language -> rank)
        public static final String USER_RANK = "achievement:rank:";
        
        // 徽章列表缓存
        public static final String BADGE_LIST = "achievement:badges:";
        
        // 缓存过期时间
        public static final long ACHIEVEMENT_EXPIRE = 3600;   // 1小时
        public static final long LEADERBOARD_EXPIRE = 300;   // 5分钟
        public static final long RANK_EXPIRE = 600;         // 10分钟
    }

    /**
     * 闯关系统缓存Key
     */
    public static class Challenge {
        // 关卡配置缓存 (challengeId -> Challenge)
        public static final String CHALLENGE_INFO = "challenge:info:";
        
        // 用户闯关进度缓存 (userId:language -> Progress)
        public static final String USER_CHALLENGE = "challenge:user:";
        
        // 闯关排行榜缓存 (language -> ChallengeLeaderboard)
        public static final String CHALLENGE_LEADERBOARD = "challenge:leaderboard:";
        
        // 缓存过期时间
        public static final long CHALLENGE_INFO_EXPIRE = 86400; // 24小时
        public static final long USER_CHALLENGE_EXPIRE = 3600;  // 1小时
        public static final long CHALLENGE_LB_EXPIRE = 600;    // 10分钟
    }

    /**
     * 系统配置缓存Key
     */
    public static class System {
        // 系统配置缓存
        public static final String SYS_CONFIG = "sys:config:";
        
        // 字典缓存
        public static final String SYS_DICT = "sys:dict:";
        
        // 验证码缓存 (key -> code)
        public static final String CAPTCHA = "sys:captcha:";
        
        // 限流计数器 (key -> count)
        public static final String RATE_LIMIT = "sys:ratelimit:";
        
        // 缓存过期时间
        public static final long SYS_CONFIG_EXPIRE = 86400; // 24小时
        public static final long CAPTCHA_EXPIRE = 300;     // 5分钟
        public static final long RATE_LIMIT_EXPIRE = 60;  // 1分钟
    }

    /**
     * 通用操作方法
     */
    public static String getKey(String prefix, String key) {
        return prefix + key;
    }

    public static String getKey(String... keys) {
        return String.join(":", keys);
    }
}
```

## 4. RedisCacheService缓存服务

```java
package com.ruoyi.framework.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis缓存服务
 * 提供通用的缓存操作方法
 * 
 * @author LingLearn
 */
@Service
public class RedisCacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置缓存
     *
     * @param key   缓存Key
     * @param value 缓存值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存并指定过期时间
     *
     * @param key      缓存Key
     * @param value    缓存值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     */
    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 获取缓存
     *
     * @param key 缓存Key
     * @return 缓存值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存
     *
     * @param key 缓存Key
     * @return 是否删除成功
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 判断Key是否存在
     *
     * @param key 缓存Key
     * @return 是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     *
     * @param key     缓存Key
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return 是否设置成功
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取过期时间
     *
     * @param key 缓存Key
     * @return 剩余过期时间（秒）
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 递增
     *
     * @param key   缓存Key
     * @param delta 递增量
     * @return 递增后的值
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   缓存Key
     * @param delta 递减量
     * @return 递减后的值
     */
    public Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * 分布式锁-获取锁
     *
     * @param lockKey    锁Key
     * @param expireMs  过期时间（毫秒）
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, long expireMs) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", expireMs, TimeUnit.MILLISECONDS);
        return result != null && result;
    }

    /**
     * 分布式锁-释放锁
     *
     * @param lockKey 锁Key
     */
    public void unLock(String lockKey) {
        redisTemplate.delete(lockKey);
    }

    /**
     * Hash操作-设置值
     *
     * @param key     Hash Key
     * @param hashKey Hash字段
     * @param value   值
     */
    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * Hash操作-获取值
     *
     * @param key     Hash Key
     * @param hashKey Hash字段
     * @return 值
     */
    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * Hash操作-删除字段
     *
     * @param key      Hash Key
     * @param hashKeys Hash字段数组
     * @return 删除数量
     */
    public Long hDelete(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 模糊匹配获取所有Key
     *
     * @param pattern 匹配模式（如：user:*）
     * @return Key列表
     */
    public java.util.Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 批量删除缓存
     *
     * @param pattern 匹配模式
     */
    public void deleteByPattern(String pattern) {
        Set<String> keys = keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
```

## 5. 缓存使用示例

```java
@Service
public class EduCourseServiceImpl implements IEduCourseService {

    @Autowired
    private RedisCacheService cacheService;

    @Autowired
    private EduCourseMapper courseMapper;

    @Override
    public EduCourse selectEduCourseById(Long courseId) {
        // 尝试从缓存获取
        String cacheKey = RedisConstants.Course.COURSE_INFO + courseId;
        EduCourse course = (EduCourse) cacheService.get(cacheKey);
        
        if (course != null) {
            return course;
        }

        // 缓存不存在，从数据库获取
        course = courseMapper.selectEduCourseById(courseId);
        
        if (course != null) {
            // 存入缓存
            cacheService.set(cacheKey, course, RedisConstants.Course.COURSE_INFO_EXPIRE, TimeUnit.SECONDS);
        }
        
        return course;
    }

    @Override
    public List<EduCourse> selectFeaturedCourses(Integer limit) {
        // 尝试从缓存获取
        String cacheKey = RedisConstants.Course.FEATURED_COURSES + limit;
        List<EduCourse> courses = (List<EduCourse>) cacheService.get(cacheKey);
        
        if (courses != null) {
            return courses;
        }

        // 缓存不存在，从数据库获取
        courses = courseMapper.selectFeaturedCourses(limit);
        
        if (courses != null && !courses.isEmpty()) {
            // 存入缓存
            cacheService.set(cacheKey, courses, RedisConstants.Course.FEATURED_EXPIRE, TimeUnit.SECONDS);
        }
        
        return courses;
    }

    @Override
    public boolean enrollCourse(Long userId, Long courseId) {
        // 删除用户课程缓存
        cacheService.deleteByPattern("user:progress:" + userId + ":*");
        
        // 执行业务逻辑...
    }
}
```

## 6. 缓存策略总结

| 数据类型 | 缓存时间 | 更新策略 | 说明 |
|---------|---------|---------|------|
| 用户信息 | 30分钟 | 主动更新 | 用户信息变更时主动删除 |
| 课程详情 | 1小时 | 被动过期 | 课程信息变更较少 |
| 课程列表 | 30分钟 | 被动过期 | 列表按需加载 |
| 排行榜 | 5分钟 | 定时刷新 | 实时性要求不高 |
| 热门帖子 | 10分钟 | 被动过期 | 访问量大，实时性要求一般 |
| 学习进度 | 30分钟 | 主动更新 | 学习行为触发更新 |
| 系统配置 | 24小时 | 主动更新 | 配置变更时删除 |

这套缓存策略能够有效提升系统性能，减少数据库访问压力。
