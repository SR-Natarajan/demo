package com.demo.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.common.Constants;
import com.demo.dto.DataObject;
import com.demo.service.IDemoService;

@RestController
@RequestMapping(Constants.DEMO)
public class DemoController {
	
	private static final Logger log = LoggerFactory.getLogger(DemoController.class);
	
	@Autowired
	private IDemoService demoService;
	
	@GetMapping(path = Constants.COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getCount() {
		log.info("DemoController - Process starts to get User count");
		
		Map<String, Object> body = new LinkedHashMap<>();
        body.put(Constants.COUNT, demoService.getUniqueUserCount());
        
        log.info("DemoController - Get User Count Completed.");
		
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
	
	@GetMapping(path = Constants.LIST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DataObject>> getUpdatedList() {
		log.info("DemoController - Process starts to get the Updated List");
		
		List<DataObject> listObj = demoService.getUpdatedList();
		
		log.info("DemoController - Get Updated List completed.");
		
		return new ResponseEntity<>(listObj, HttpStatus.OK);
	}
	
	@PutMapping(path = Constants.UPDATE_LIST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DataObject>> updatedList(@Valid @RequestBody DataObject dataObj) {
		log.info("DemoController - Process starts to Update List");
		
		List<DataObject> listObj = demoService.updateList(dataObj);
		
		log.info("DemoController - Update List completed.");
		
		return new ResponseEntity<>(listObj, HttpStatus.OK);
	}

}
