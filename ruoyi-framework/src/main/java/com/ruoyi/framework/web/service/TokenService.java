package com.ruoyi.framework.web.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token验证处理
 *
 * @author ruoyi
 */
@Component
public class TokenService
{
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 专门用于ZSet的String序列化Template，避免token被FastJson包成带引号的JSON
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request)
    {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            try
            {
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                LoginUser user = redisCache.getCacheObject(userKey);
                if (StringUtils.isNotNull(user))
                {
                    // 验证令牌是否仍属于该用户的在线令牌集合（防多端登录）
                    if (!isUserTokenValid(user))
                    {
                        // 令牌已被踢除，删除Redis缓存并视为未登录
                        redisCache.deleteObject(userKey);
                        return null;
                    }
                    return user;
                }
            }
            catch (Exception e)
            {
            }
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser)
    {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken()))
        {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token)
    {
        if (StringUtils.isNotEmpty(token))
        {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser)
    {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser)
    {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser)
    {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser)
    {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr();
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims)
    {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token)
    {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token)
    {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request)
    {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX))
        {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid)
    {
        return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    }

    /**
     * 获取用户名的唯一标识（用于Redis key）
     * 从token中解析出用户名
     */
    private String getUserKey(String username)
    {
        return CacheConstants.ONLINE_USER_TOKENS + username;
    }

    /**
     * 用户登录时，踢除该用户其他已登录的令牌
     *
     * @param loginUser 登录用户信息
     */
    public void kickOldTokens(LoginUser loginUser)
    {
        System.out.println("==== [单点登录-DEBUG] kickOldTokens 入口 ====");
        if (loginUser == null || loginUser.getUser() == null
                || StringUtils.isEmpty(loginUser.getUser().getUserName())
                || StringUtils.isEmpty(loginUser.getToken()))
        {
            System.out.println("==== [单点登录-DEBUG] 参数校验失败, 直接返回 ====");
            return;
        }
        String username = loginUser.getUser().getUserName();
        String newToken = loginUser.getToken();
        String userZSetKey = CacheConstants.ONLINE_USER_TOKENS + username;
        System.out.println("==== [单点登录] 用户 " + username + " 登录, ZSetKey=" + userZSetKey + ", newToken=" + newToken + " ====");

        try
        {
            // 1) 查该用户当前 ZSet 中所有旧 token（使用StringRedisTemplate，避免FastJson包引号）
            Set<String> oldTokens = stringRedisTemplate.opsForZSet().range(userZSetKey, 0, -1);
            System.out.println("==== [单点登录] ZSet 当前成员数: " + (oldTokens == null ? 0 : oldTokens.size()) + " ====");

            if (oldTokens != null)
            {
                for (String oldToken : oldTokens)
                {
                    if (oldToken == null || newToken.equals(oldToken))
                    {
                        // 跳过当前 token
                        continue;
                    }
                    // 删旧 session
                    redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + oldToken);
                    // 从 ZSet 删旧成员
                    stringRedisTemplate.opsForZSet().remove(userZSetKey, oldToken);
                    System.out.println("==== [单点登录] 踢除旧 token: " + oldToken + " ====");
                }
            }

            // 2) 写入新 token
            double score = System.currentTimeMillis();
            stringRedisTemplate.opsForZSet().add(userZSetKey, newToken, score);
            System.out.println("==== [单点登录] 写入新 token 到 ZSet, score=" + score + " ====");

            // 3) 验证写入（这次读取应该能拿到分数）
            Double verifyScore = stringRedisTemplate.opsForZSet().score(userZSetKey, newToken);
            System.out.println("==== [单点登录-VERIFY] ZSet 中新 token 分数: " + verifyScore + " ====");
        }
        catch (Exception e)
        {
            System.out.println("==== [单点登录] kickOldTokens 异常: " + e.getMessage() + " ====");
            e.printStackTrace();
        }
    }

    /**
     * 验证当前令牌是否仍属于用户的在线令牌集合
     *
     * @param loginUser 登录用户信息
     * @return 是否有效
     */
    private boolean isUserTokenValid(LoginUser loginUser)
    {
        if (loginUser == null || loginUser.getUser() == null
                || StringUtils.isEmpty(loginUser.getUser().getUserName())
                || StringUtils.isEmpty(loginUser.getToken()))
        {
            return false;
        }
        String userZSetKey = CacheConstants.ONLINE_USER_TOKENS + loginUser.getUser().getUserName();
        try
        {
            Double score = stringRedisTemplate.opsForZSet().score(userZSetKey, loginUser.getToken());
            return score != null;
        }
        catch (Exception e)
        {
            System.out.println("==== [单点登录] isUserTokenValid 异常: " + e.getMessage() + " ====");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 登出时，清理用户的在线令牌记录
     *
     * @param loginUser 登录用户信息
     */
    public void logoutUser(LoginUser loginUser)
    {
        if (loginUser == null || loginUser.getUser() == null
                || StringUtils.isEmpty(loginUser.getUser().getUserName())
                || StringUtils.isEmpty(loginUser.getToken()))
        {
            return;
        }
        String userZSetKey = CacheConstants.ONLINE_USER_TOKENS + loginUser.getUser().getUserName();
        try
        {
            stringRedisTemplate.opsForZSet().remove(userZSetKey, loginUser.getToken());
        }
        catch (Exception e)
        {
            System.out.println("==== [单点登录] logoutUser 异常: " + e.getMessage() + " ====");
            e.printStackTrace();
        }
    }
}
