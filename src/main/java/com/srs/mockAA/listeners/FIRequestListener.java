package com.srs.mockAA.listeners;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
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
import com.srs.mockAA.events.ActionInvokedEvent;
import com.srs.mockAA.events.FIRequestEvent;
import com.srs.mockAA.models.FIRequest;
import com.srs.mockAA.models.InvokeExecutionRequest;
import com.srs.mockAA.utils.RestTemplateGenerator;

@Component
public class FIRequestListener {
	
	@Value("${vdrp.baseurl}")
	private String VDRP_BASE_URL;
	
	@Value("${vdrp.apikey}")
	private String VDRP_API_KEY;
	
	@Value("${delay}")
	private long DELAY;
	
	@Autowired
	RestTemplateGenerator restTemplateGenerator;
	
	@Autowired
	ApplicationEventPublisher appEventPublisher;
	
	@Async
	@EventListener
	public void fiRequestListener(FIRequestEvent event) {
		System.out.println("Invoking action.");
		try {
			Thread.sleep(DELAY);
			FIRequest request = (FIRequest) event.getSource();
			RestTemplate restTemplate = restTemplateGenerator.getRestTemplate();
			InvokeExecutionRequest body = new InvokeExecutionRequest();
			body.setVer("1.0");
			body.setTimestamp(LocalDateTime.now().toString());
			body.setTxnid(UUID.randomUUID().toString());
			body.setSessionId(request.getSessionID());
			body.setFiuid((String) request.getVdrpDetails().get("fiuid"));
			body.setActionid((String) request.getVdrpDetails().get("actionid"));
			final String uri = VDRP_BASE_URL + "/execution/invoke";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("vdpr_api_key", VDRP_API_KEY);
			HttpEntity<String> entity = new HttpEntity<String>(new ObjectMapper().writeValueAsString(body), headers);
			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
			if(response.getStatusCode() == HttpStatus.OK) {
				HashMap<String, Object> responseMap = new ObjectMapper().readValue(response.getBody(), HashMap.class);
				ActionInvokedEvent actionInvokedEvent = new ActionInvokedEvent(responseMap);
				appEventPublisher.publishEvent(actionInvokedEvent);
				System.out.println("Action invoked and event published.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
