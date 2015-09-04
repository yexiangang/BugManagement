package com.my.service;

import java.util.List;

import com.my.domain.UploadFile;

public interface UploadFileService
{
	public void save(UploadFile file);
	
	public List<Object> findByHql(String hql, String[] str);
	public boolean delect(UploadFile uf);
}
