package com.srs.mockAA.services;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AAService {
	
	@Value("${data.json.path}")
	private String FILE_PATH;
	
	public HashMap<String, Object> fetchData(String sessionid) {
		HashMap<String, Object> fetchResponse = null;
		try {
			//Resource resource = new ClassPathResource("data.json");
			File file = new File(FILE_PATH);
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

	public void writeToFile(HashMap<String, Object> bodyMap) {
		try {
			File file = new File(FILE_PATH);
			PrintWriter writer = new PrintWriter(FILE_PATH);
			writer.print("");
			writer.close();
			new ObjectMapper().writeValue(file, bodyMap);
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
