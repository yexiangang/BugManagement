package com.my.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.my.dao.UploadFileDao;
import com.my.domain.UploadFile;
import com.my.service.UploadFileService;

public class UploadFileServiceImpl implements UploadFileService
{
	@Autowired
	private UploadFileDao uploadFileDao;

	public void save(UploadFile file)
	{
		uploadFileDao.save(file);
	}
	
	public List<Object> findByHql(String hql, String[] str){
		return uploadFileDao.findByHql(hql, str);
	}
	
	public boolean delect(UploadFile uf){
		uploadFileDao.delete(uf);
		return false;
	}

	
}
