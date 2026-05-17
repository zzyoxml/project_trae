package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduAppUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 教育平台应用用户Mapper接口
 *
 * @author LingLearn
 */
@Mapper
public interface EduAppUserMapper {

    /**
     * 查询用户
     *
     * @param userId 用户ID
     * @return 用户
     */
    public EduAppUser selectEduAppUserById(Long userId);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    public EduAppUser selectEduAppUserByUserName(String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户
     */
    public EduAppUser checkEmailUnique(String email);

    /**
     * 查询用户列表
     *
     * @param eduAppUser 用户
     * @return 用户集合
     */
    public List<EduAppUser> selectEduAppUserList(EduAppUser eduAppUser);

    /**
     * 新增用户
     *
     * @param eduAppUser 用户
     * @return 结果
     */
    public int insertEduAppUser(EduAppUser eduAppUser);

    /**
     * 修改用户
     *
     * @param eduAppUser 用户
     * @return 结果
     */
    public int updateEduAppUser(EduAppUser eduAppUser);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteEduAppUserById(Long userId);

    /**
     * 批量删除用户
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    public int deleteEduAppUserByIds(Long[] userIds);
}
