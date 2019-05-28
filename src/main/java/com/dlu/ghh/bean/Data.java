package com.dlu.ghh.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 资料
 * @author 隐王爷
 *
 */
@JsonIgnoreProperties(value = {"handler"})
public class Data implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;//发布资料的用户

    private Date date;//发布资料的日期
    
    private Integer id;//资料id

    private String description;//资料名称

    private Integer type;//资料的类型
    
    private String address;//文件所在的路径 
    
    private String name;//文件的 名字
    
    private Integer downloadTimes;//文件下载的次数
    
    private Integer status;//资料的状态
    
    public Integer getStatus() {
		return status;
	}
    
    public void setStatus(Integer status) {
		this.status = status;
	}
    
    public void setDownloadTimes(Integer downloadTimes) {
		this.downloadTimes = downloadTimes;
	}
    
    public Integer getDownloadTimes() {
		return downloadTimes;
	}
    
    public void setName(String name) {
		this.name = name;
	}
    
    public String getName() {
		return name;
	}

    public void setAddress(String address) {
		this.address = address;
	}
    
    public String getAddress() {
		return address;
	}
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

	@Override
	public String toString() {
		return "Data [user=" + user + ", date=" + date + ", id=" + id + ", description=" + description + ", type="
				+ type + "]";
	}
    
    
}