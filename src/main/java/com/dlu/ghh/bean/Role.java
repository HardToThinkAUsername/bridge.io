package com.dlu.ghh.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 角色
 * @author 隐王爷
 *
 */
@JsonIgnoreProperties(value = {"handler"})
public class Role implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;//角色id

    private String name;//角色名称
    
    public Role() {}
    
    public Role(Integer id) {
    	this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}