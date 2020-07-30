package com.srs.mockAA.listeners;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.srs.mockAA.events.OutputReadyEvent;
import com.srs.mockAA.utils.RestTemplateGenerator;

@Component
public class OutputReadyListener {
	
	@Value("${vdrp.baseurl}")
	private String VDRP_BASE_URL;
	
	@Value("${vdrp.apikey}")
	private String VDRP_API_KEY;
	
	@Value("${delay}")
	private long DELAY;
	
	@Autowired
	RestTemplateGenerator restTemplateGenerator;
	
	@Async
	@EventListener
	public void outputReadyListener(OutputReadyEvent event) {
		System.out.println("Fetching output.");
		ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		try {
			Thread.sleep(DELAY);
			RestTemplate restTemplate = restTemplateGenerator.getRestTemplate();
			final String uri = VDRP_BASE_URL + "/execution/fetch/" + event.getSource();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("vdpr_api_key", VDRP_API_KEY);
			HttpEntity<String> entity = new HttpEntity<String>("", headers);
			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
			if(response.getStatusCode() == HttpStatus.OK) {
				HashMap<String, Object> responseBody = mapper.readValue(response.getBody(), HashMap.class);
				System.out.println(responseBody);
			}
			System.out.println("Output fetched.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
