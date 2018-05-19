package com.wedo.OMS.service;

import com.wedo.OMS.entity.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

public interface UserService {
    /**
     * 用户登陆
     * @param user
     * @return
     */
    User login(User user)throws NoSuchAlgorithmException,UnsupportedEncodingException;

    /**
     * 根据用户ID获取用户信息
     * @param userId
     * @return
     */
    User getUserByUserId(Long userId);

    /**
     * 签到
     * @param userId
     * @param taskId
     * @param dateTime
     * @return
     */
    User signin(Long userId, Long taskId, Timestamp dateTime);

    /**
     * 签退，包括提交考勤日志
     * @param userId
     * @param taskId
     * @param dateTime
     * @return
     */
    User signout(Long userId, Long taskId, Timestamp dateTime);
}
