package com.dlu.ghh.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 参数
 * @author 隐王爷
 *
 */
@Configuration
public class ParamConfiguration {
	//用户协议
	@Value("${user.agreement.address}")
	private String agreementAddress;
	
	//静态资源对外暴露的访问路径
	@Value("${file.staticAccessPath}")
    private String staticAccessPath;

    //文件上传目录
	@Value("${file.uploadFolder}")
    private String uploadFolder;
	
	//网络访问静态资源的路径
	@Value("${file.network.resourcesPath}")
	private String netPath;
	
	//登录经验值
	@Value("${user.login.experience}")
	private Integer loginExperience;
	
	
	public String getNetPath() {
		return netPath;
	}
	
	public void setNetPath(String netPath) {
		this.netPath = netPath;
	}
	
	public void setLoginExperience(Integer loginExperience) {
		this.loginExperience = loginExperience;
	}

	public Integer getLoginExperience() {
		return loginExperience;
	}
	
    public String getStaticAccessPath() {
        return staticAccessPath;
    }

    public void setStaticAccessPath(String staticAccessPath) {
        this.staticAccessPath = staticAccessPath;
    }

    public String getUploadFolder() {
        return uploadFolder;
    }

    public void setUploadFolder(String uploadFolder) {
        this.uploadFolder = uploadFolder;
    }
	public String getAgreementAddress() {
		return agreementAddress;
	}
	
	public void setAgreementAddress(String agreementAddress) {
		this.agreementAddress = agreementAddress;
	}
}
