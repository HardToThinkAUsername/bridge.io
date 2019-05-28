package com.dlu.ghh.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 图像
 * @author 隐王爷
 *
 */
@JsonIgnoreProperties(value = {"handler"})
public class Image implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;//图像id

    private String path;//图像路径

    public Image() {}
    
    public Image(Integer id) {
    	this.id = id;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }
}