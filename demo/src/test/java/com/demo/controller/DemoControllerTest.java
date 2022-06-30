package com.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.demo.common.Constants;
import com.demo.dto.DataObject;
import com.demo.service.DemoService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
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
	
	@Test
	void testUpdateListSuccess() throws Exception {
		Mockito.when(demoService.updateList(Mockito.any())).thenReturn(dObjList);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/demo/updateList").accept(MediaType.APPLICATION_JSON)
				.content(mapToJson(new DataObject(3, 3, "1800", "Flowers"))).contentType(MediaType.APPLICATION_JSON);;

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

		String responseString = result.getResponse().getContentAsString();
		assertNotNull(responseString);
		List<DataObject> responseList = mapFromJsonList(result.getResponse().getContentAsString(),
				new TypeReference<List<DataObject>>() {
				});
		assertEquals(6, responseList.size());
	}
	
	@Test
	void testUpdateListExceptionMeesage() throws Exception {
		Mockito.when(demoService.updateList(Mockito.any())).thenThrow(new ArrayIndexOutOfBoundsException());

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/demo/updateList").accept(MediaType.APPLICATION_JSON)
				.content(mapToJson(new DataObject(3, 3, "1800", "Flowers"))).contentType(MediaType.APPLICATION_JSON);;

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isNoContent()).andReturn();

		String responseString = result.getResponse().getContentAsString();
		assertNotNull(responseString);
		JSONObject response = new JSONObject(responseString);
		assertEquals("ID not present.", response.get(Constants.MESSAGE));
	}
	
	private <T> List<T> mapFromJsonList(String json, TypeReference<List<T>> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}
	
	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

}
