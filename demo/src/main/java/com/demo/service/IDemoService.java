package com.demo.service;

import java.util.List;

import com.demo.dto.DataObject;

public interface IDemoService {
	
	long getUniqueUserCount();
	List<DataObject> getUpdatedList();

}
