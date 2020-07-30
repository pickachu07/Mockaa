package com.srs.mockAA.listeners;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srs.mockAA.events.ActionInvokedEvent;
import com.srs.mockAA.models.FINotificationRequest;
import com.srs.mockAA.utils.RestTemplateGenerator;

@Component
public class ActionInvokedListener {
	
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
	public void actionInvokedListener(ActionInvokedEvent event) {
		System.out.println("Sending FI Notification.");
		try {
			Thread.sleep(DELAY);
			HashMap<String, Object> invocationResponse = (HashMap<String, Object>) event.getSource();
			RestTemplate restTemplate = restTemplateGenerator.getRestTemplate();
			FINotificationRequest body = new FINotificationRequest((String) invocationResponse.get("sessionid"), 
					(String) invocationResponse.get("executionid"));
			final String uri = VDRP_BASE_URL + "/execution/notification";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("vdpr_api_key", VDRP_API_KEY);
			HttpEntity<String> entity = new HttpEntity<String>(new ObjectMapper().writeValueAsString(body), headers);
			restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
			System.out.println("FI Notification Sent.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
