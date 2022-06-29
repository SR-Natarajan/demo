package com.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.demo.dto.DataObject;
import com.demo.service.DemoService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class DemoControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	DemoService demoService;

	static List<DataObject> dObjList;

	@BeforeAll
	static void buildDataList() {
		dObjList = new ArrayList<>();
		dObjList.add(new DataObject(1, 1, "title", "body"));
		dObjList.add(new DataObject(2, 1, "title", "body"));
		dObjList.add(new DataObject(3, 1, "title", "body"));
		dObjList.add(new DataObject(4, 2, "title", "body"));
		dObjList.add(new DataObject(5, 2, "title", "body"));
		dObjList.add(new DataObject(6, 2, "title", "body"));
	}

	@Test
	void testCountSuccess() throws Exception {
		Mockito.when(demoService.getUniqueUserCount()).thenReturn(5L);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/demo/count");

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
		assertNotNull(result.getResponse().getContentAsString());
	}

	@Test
	void testUpdatedListSuccess() throws Exception {
		Mockito.when(demoService.getUpdatedList()).thenReturn(dObjList);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/demo/list");

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

		String responseString = result.getResponse().getContentAsString();
		assertNotNull(responseString);
		List<DataObject> responseList = mapFromJsonList(result.getResponse().getContentAsString(),
				new TypeReference<List<DataObject>>() {
				});
		assertEquals(6, responseList.size());
	}
	
	private <T> List<T> mapFromJsonList(String json, TypeReference<List<T>> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

}
