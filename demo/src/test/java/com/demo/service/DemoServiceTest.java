package com.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import com.demo.dto.DataObject;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties= {"json.feed.url=test"})
class DemoServiceTest {

	@InjectMocks
	private DemoService demoService = new DemoService(new RestTemplateBuilder());;

	@Mock
	RestTemplate restTemplate;

	static DataObject[] dObjList;

	@BeforeAll
	static void buildDataList() {
		dObjList = new DataObject[] { new DataObject(1, 1, "title", "body"), new DataObject(2, 1, "title", "body"),
				new DataObject(3, 1, "title", "body"), new DataObject(4, 2, "title", "body"),
				new DataObject(5, 2, "title", "body"), new DataObject(6, 2, "title", "body") };
	}

	@Test
	void testGetUniqueUserCount() throws Exception {
		Mockito.when(restTemplate.getForObject(Mockito.<String>any(), Mockito.any())).thenReturn(dObjList);
		long result = demoService.getUniqueUserCount();
		assertEquals(2l, result);
	}
	
	@Test
	void testGetUpdatedList() throws Exception {
		Mockito.when(restTemplate.getForObject(Mockito.<String>any(), Mockito.any())).thenReturn(dObjList);
		List<DataObject> result = demoService.getUpdatedList();
		assertEquals(6, result.size());
		assertEquals("title", result.get(1).getTitle());
		assertEquals("body", result.get(1).getBody());
		assertEquals("1800Flowers", result.get(3).getTitle());
		assertEquals("1800Flowers", result.get(3).getBody());
	}
	
	@Test
	void testUpdatedList() throws Exception {
		Mockito.when(restTemplate.getForObject(Mockito.<String>any(), Mockito.any())).thenReturn(dObjList);
		List<DataObject> result = demoService.updateList(new DataObject(3, 3, "1800", "Flowers"));
		assertEquals(6, result.size());
		assertEquals("title", result.get(1).getTitle());
		assertEquals("body", result.get(1).getBody());
		assertEquals("1800", result.get(2).getTitle());
		assertEquals("Flowers", result.get(2).getBody());
	}
	
	@Test()
	void testUpdatedListNPE() throws Exception {
		Mockito.when(restTemplate.getForObject(Mockito.<String>any(), Mockito.any())).thenReturn(null);
		NullPointerException thrown = Assertions.assertThrows(NullPointerException.class, () -> {
			demoService.getUpdatedList();
		}, "NullPointerException was expected");
		
		assertEquals("java.lang.NullPointerException", thrown.toString());
	}
	
	@Test()
	void testUpdatedListArrayIndexOutOfBoundsException() throws Exception {
		Mockito.when(restTemplate.getForObject(Mockito.<String>any(), Mockito.any())).thenReturn(new DataObject[] {dObjList[1]});
		ArrayIndexOutOfBoundsException thrown = Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
			demoService.getUpdatedList();
		}, "ArrayIndexOutOfBoundsException was expected");
		
		assertEquals("java.lang.ArrayIndexOutOfBoundsException: 3", thrown.toString());
	}

}
