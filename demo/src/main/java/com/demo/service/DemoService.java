package com.demo.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demo.dto.DataObject;

@Service
public class DemoService implements IDemoService{
	
	@Value("${json.feed.url:http://jsonplaceholder.typicode.com/posts}")
	private String feedUrl;
	
	private RestTemplate restTemplate;
	
	public DemoService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}
	
	public long getUniqueUserCount() {
		DataObject[] dataList = restTemplate.getForObject(feedUrl, DataObject[].class);
		return Arrays.stream(dataList).map(p -> p.getUserId()).distinct().count();
	}	
	
	public List<DataObject> getUpdatedList() {
		DataObject[] dataList = restTemplate.getForObject(feedUrl, DataObject[].class);
		dataList[3].setTitle("1800Flowers");
		dataList[3].setBody("1800Flowers");
		return Arrays.asList(dataList);
	}
	
}
