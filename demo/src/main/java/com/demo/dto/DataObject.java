package com.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataObject {
	
	@NonNull
	private Integer id;
	private Integer userId;
	private String title;
	private String body;

}
