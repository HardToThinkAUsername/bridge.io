package com.dlu.ghh.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 用户
 * @author 隐王爷
 *
 */
@JsonIgnoreProperties(value = {"handler"})
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;//用户id

    private String username;//用户名

    private String address;//用户地址

    private String password;//用户密码

    private Integer disable;//用户是否已删除

    private String phoneNum;//用户电话

    private String email;//用户邮箱

    private Role role;//用户角色

    private Date registerDate;//注册日期

    private Date lastLoginDate;//最后登录日期

    private Image userImage;//用户头像图片

    private Integer userExperience;//用户经验
	
    private Integer gender;//0:女  1: 男

    private Integer shutup;//是否禁言

    private Integer praiseTimes;//赞过的次数

    private Integer replyTimes;//评论过的次数
    
    private Integer continuousSigninTimes;//连续签到的次数
    
    private Date lastSigninDate;//上次签到的时间
    
    public void setContinuousSigninTimes(Integer continuousSigninTimes) {
		this.continuousSigninTimes = continuousSigninTimes;
	}
    
    public Integer getContinuousSigninTimes() {
		return continuousSigninTimes;
	}
    
    public void setLastSigninDate(Date lastSigninDate) {
		this.lastSigninDate = lastSigninDate;
	}
    
    public Date getLastSigninDate() {
		return lastSigninDate;
	}
    
    
    public void setRank(Integer rank) {}
    
    public Integer getRank() {
    	if(userExperience == null) {
    		return 1;
    	}
    	if(userExperience <= 2000 && userExperience >=0) 
    		return 1; 
    	else if(userExperience > 2000 && userExperience <=5000) 
    		return 2;
    	else if(userExperience > 5000 && userExperience <= 10000)
    		return 3;
    	else if(userExperience > 10000 && userExperience <= 20000)
    		return 4;
    	else if(userExperience > 20000 && userExperience <= 40000)
    		return 5;
    	else if(userExperience > 40000 && userExperience <= 80000)
    		return 6;
    	return 7;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum == null ? null : phoneNum.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Image getUserImage() {
        return userImage;
    }

    public void setUserImage(Image userImage) {
        this.userImage = userImage;
    }

    public Integer getUserExperience() {
        return userExperience;
    }

    public void setUserExperience(Integer userExperience) {
        this.userExperience = userExperience;
    }

    public Integer getDisable() {
        return disable;
    }

    public void setDisable(Integer disable) {
        this.disable = disable;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getShutup() {
        return shutup;
    }

    public void setShutup(Integer shutup) {
        this.shutup = shutup;
    }

    public Integer getPraiseTimes() {
        return praiseTimes;
    }

    public void setPraiseTimes(Integer praiseTimes) {
        this.praiseTimes = praiseTimes;
    }

    public Integer getReplyTimes() {
        return replyTimes;
    }

    public void setReplyTimes(Integer replyTimes) {
        this.replyTimes = replyTimes;
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", address=" + address + ", password=" + password
				+ ", disable=" + disable + ", phoneNum=" + phoneNum + ", email=" + email + ", role=" + role
				+ ", registerDate=" + registerDate + ", lastLoginDate=" + lastLoginDate + ", userImage=" + userImage
				+ ", userExperience=" + userExperience + ", gender=" + gender + ", shutup=" + shutup + ", praiseTimes="
				+ praiseTimes + ", replyTimes=" + replyTimes + "]";
	}
    
}