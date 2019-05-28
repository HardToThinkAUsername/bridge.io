package com.dlu.ghh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlu.ghh.bean.Image;
import com.dlu.ghh.mapper.ImageMapper;

@Service
public class ImageService {

	@Autowired
	private ImageMapper imageMapper;
	
	@Transactional
	public int insert(Image image) {
		return imageMapper.insert(image);
	}

	public List<Image> selectByPath(String path) {
		return imageMapper.selectByPath(path);
	}

}
