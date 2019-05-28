package com.dlu.ghh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlu.ghh.bean.Data;
import com.dlu.ghh.bean.Post;
import com.dlu.ghh.mapper.DataMapper;

@Service
public class DataService {

	@Autowired
	private DataMapper dataMapper;
	
	@Transactional
	public boolean upload(Data data) {
		return dataMapper.insert(data)>0?true:false;
	}

	@Transactional
	public List<Data> getData() {
		return dataMapper.selectAll();
	}

	@Transactional
	public Integer getCountByUserId(Integer id) {
		return dataMapper.selectCountByUserId(id);
	}
	
	@Transactional
	public List<Post> getFuzzy(String string) {
		return dataMapper.selectFuzzy(string);
	}
	
	@Transactional
	public List<Data> getByUserId(Integer id) {
		return dataMapper.selectByUserId(id);
	}

	@Transactional
	public boolean deleteById(Integer id) {
		return dataMapper.deleteByPrimaryKey(id)>0?true:false;
	}

	@Transactional
	public Data getById(Integer dataId) {
		return dataMapper.selectByPrimaryKey(dataId);
	}
	@Transactional
	public boolean updateByIdSelective(Data data) {
		return dataMapper.updateByIdSelective(data)>0?true:false;
	}
	@Transactional
	public List<Data> getAllSelectiveFuzzy(Data data) {
		return dataMapper.fuzzySelectSelective(data);
	}
	@Transactional
	public boolean deleteByIds(String dataIds) {
		String[] strings = dataIds.split(",");
		for(String string : strings) {
			if(!"".equals(string.trim())) {
				try {
					int i = Integer.parseInt(string);
					if(!deleteById(i)) {
						return false;
					}
				} catch (NumberFormatException e) {
					return false;
				}
			}
		}
		return true;
	}
	@Transactional
	public List<Data> getNotReview(Data data) {
		return dataMapper.selectNotReview(data);
	}
	@Transactional
	public boolean review(Integer dataId) {
		Data data = new Data();
		data.setId(dataId);
		data.setStatus(1);
		return dataMapper.updateByIdSelective(data)>0;
	}
	
}
