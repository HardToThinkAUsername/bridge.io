package com.dlu.ghh.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 权限
 * @author 隐王爷
 *
 */
@SuppressWarnings("serial")
@JsonIgnoreProperties(value = {"handler"})
public class Permission implements Serializable{
    public static final Integer USER_SHUT_UP = 1;//禁言
    
    public static final Integer POST_SELECT = 2;//查询帖子
    
    public static final Integer POST_DELETE = 3;//删除帖子
    
    public static final Integer USER_SELECT = 4;//查询用户
    
    public static final Integer USER_UPDATE = 5;//修改用户
    
    public static final Integer USER_DELETE = 6;//删除用户
    
    public static final Integer USER_INSERT = 7;//添加用户
    
    public static final Integer ROLE_CHANGE = 8;//修改用户角色
    
    public static final Integer POST_SEAL = 9;//查封帖子
    
    public static final Integer DATA_DELETE = 10;//删除资料
    
    public static final Integer POST_REVIEW = 11;//帖子审阅
    
    public static final Integer DATA_REVIEW = 12;//资料审阅
}