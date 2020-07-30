package com.srs.mockAA.services;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AAService {
	
	public HashMap<String, Object> fetchData(String sessionid) {
		HashMap<String, Object> fetchResponse = null;
		try {
			Resource resource = new ClassPathResource("data.json");
			File file = resource.getFile();
			fetchResponse = new ObjectMapper().readValue(file, HashMap.class);
			fetchResponse.put("sessionID", sessionid);
			fetchResponse.put("timestamp", LocalDateTime.now().toString());
			fetchResponse.put("txnid", UUID.randomUUID().toString());
			System.out.println(fetchResponse);
			return fetchResponse;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fetchResponse;
	}
}
