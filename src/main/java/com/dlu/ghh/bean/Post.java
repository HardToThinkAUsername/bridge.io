package com.dlu.ghh.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 帖子
 * @author 隐王爷
 *
 */
@JsonIgnoreProperties(value = {"handler"})
public class Post implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;//帖子id

    private String postTitle;//帖子标题

    private User user;//帖子的作者

    private Date date;//发帖日期

    private Integer type;//帖子的类型  0: 求助帖   1:介绍帖

    private String postBody;//帖子的内容

    private Integer floors;//帖子的总楼层

    private Integer readedTimes;//浏览次数

    private Integer status;//状态

    private Integer praisedTimes;//被赞次数

    private User latestReplyUser;//最新回复人
    
    private Integer century;//桥梁年代
    
    public Integer getCentury() {
		return century;
	}
    
    public void setCentury(Integer century) {
		this.century = century;
	}
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle == null ? null : postTitle.trim();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getFloors() {
        return floors;
    }

    public void setFloors(Integer floors) {
        this.floors = floors;
    }

    public Integer getReadedTimes() {
        return readedTimes;
    }

    public void setReadedTimes(Integer readedTimes) {
        this.readedTimes = readedTimes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPraisedTimes() {
        return praisedTimes;
    }

    public void setPraisedTimes(Integer praisedTimes) {
        this.praisedTimes = praisedTimes;
    }

    public User getLatestReplyUser() {
        return latestReplyUser;
    }

    public void setLatestReplyUser(User latestReplyUser) {
        this.latestReplyUser = latestReplyUser;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody == null ? null : postBody.trim();
    }

	@Override
	public String toString() {
		return "Post [id=" + id + ", postTitle=" + postTitle + ", user=" + user + ", date=" + date + ", type=" + type
				+ ", postBody=" + postBody + ", floors=" + floors + ", readedTimes=" + readedTimes + ", status="
				+ status + ", praisedTimes=" + praisedTimes + ", latestReplyUser=" + latestReplyUser + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof Post) {
			Post post = (Post)obj;
			if(this.id  == post.getId()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.id;
	}
    
}