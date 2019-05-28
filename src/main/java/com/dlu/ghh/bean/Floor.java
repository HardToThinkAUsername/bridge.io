package com.dlu.ghh.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 回帖
 * @author 隐王爷
 *
 */
@JsonIgnoreProperties(value = {"handler"})
public class Floor implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;//唯一标识

    private User user;//

    private Post post;

    private Date date;

    private Integer praisedTimes;

    private Integer replyedTimes;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getPraisedTimes() {
        return praisedTimes;
    }

    public void setPraisedTimes(Integer praisedTimes) {
        this.praisedTimes = praisedTimes;
    }

    public Integer getReplyedTimes() {
        return replyedTimes;
    }

    public void setReplyedTimes(Integer replyedTimes) {
        this.replyedTimes = replyedTimes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}