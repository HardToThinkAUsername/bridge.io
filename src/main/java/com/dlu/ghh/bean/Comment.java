package com.dlu.ghh.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 评论  :对回帖的评论  或者对评论的回复
 * @author 隐王爷
 *
 */
@JsonIgnoreProperties(value = {"handler"})
public class Comment implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;//评论的id

    private Date date;//评论发布的日期

    private Floor floor;//回帖Floor

    private String content;//评论的内容
    
    private User user;//用户
    
    public void setUser(User user) {
		this.user = user;
	}
    
    public User getUser() {
		return user;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}