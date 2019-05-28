package com.dlu.ghh.bean;

public class KeyWord {
	public static final Integer RAILWAY_BRIDGE = 1;//铁路桥
	
	public static final Integer HIGHWAY_BRIDGE = 2;//公路桥
	
	public static final Integer PEDESTRIAN_BRIDGE = 3;//人行桥
	
	public static final Integer OVER_PASS_BRIDGE = 4;//高架桥
	
	public static final Integer WOODEN_BRIDGE = 5;//木桥
	
	public static final Integer STEEL_BRIDGE = 6;//钢桥
	
	public static final Integer REINFORCED_CONCRETE_BRIDGE = 7;//钢筋混凝土桥

	public static Integer getKeyWordId(String content) {
		if("铁路桥".equals(content)) {
			return 1;
		}else if("公路桥".equals(content)) {
			return 2;
		}else if("人行桥".equals(content)) {
			return 3;
		}else if("高架桥".equals(content)) {
			return 4;
		}else if("木桥".equals(content)) {
			return 5;
		}else if("钢桥".equals(content)) {
			return 6;
		}else if("钢筋混凝土桥".equals(content)) {
			return 7;
		}else {
			return 0;
		}
	}
	
}
