package com.srs.mockAA.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srs.mockAA.events.FIRequestEvent;
import com.srs.mockAA.events.OutputReadyEvent;
import com.srs.mockAA.models.FIRequest;
import com.srs.mockAA.services.AAService;

@RestController
public class AAController {
	
	@Value("${fetch.output}")
	private boolean FETCH_OUTPUT;
	
	@Autowired
	AAService aaService;
	@Autowired
	ApplicationEventPublisher appEventPublisher;
	
	@PostMapping(value="/FI/request", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> fiRequest(@RequestBody String body, @RequestHeader("client_api_key") String key) {
		System.out.println("FI Request Received.");
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			HashMap<String, Object> bodyMap = objectMapper.readValue(body, HashMap.class);
			HashMap<String, Object> vdrpDetails = (HashMap<String, Object>) bodyMap.get("VDRProvider");
			HashMap<String, Object> consent = (HashMap<String, Object>) bodyMap.get("Consent");
			String consentID = (String) consent.get("id");
			String sessionID = UUID.randomUUID().toString();
			HashMap<String, String> response = new HashMap<String, String>();
			response.put("ver", "1.0");
			response.put("timestamp", LocalDateTime.now().toString());
			response.put("txnid", UUID.randomUUID().toString());
			response.put("consentId", consentID);
			response.put("sessionId", sessionID);
			FIRequest request = new FIRequest(sessionID, vdrpDetails);
			FIRequestEvent event = new FIRequestEvent(request);
			appEventPublisher.publishEvent(event);
			System.out.println("FI Request Received and event published.");
			return new ResponseEntity<String>(objectMapper.writeValueAsString(response), HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<String>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@GetMapping(value="/FI/fetch/{sessionid}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> fetchData(@PathVariable String sessionid, @RequestHeader("client_api_key") String key) {
		System.out.println("FI Fetch Request Received");
		try {
			HashMap<String, Object> response = aaService.fetchData(sessionid);
			System.out.println("FI Fetch Request Processed.");
			return new ResponseEntity<String>(new ObjectMapper().writeValueAsString(response), HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<String>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value="/output/notification", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> notify(@RequestBody String body, @RequestHeader("client_api_key") String key) {
		System.out.println("Output Notification Received.");
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			HashMap<String, Object> bodyMap = objectMapper.readValue(body, HashMap.class);
			HashMap<String, Object> executionStatusNotification = (HashMap<String, Object>) bodyMap.get("notification");
			String executionid = (String) executionStatusNotification.get("executionid");
			OutputReadyEvent event = new OutputReadyEvent(executionid);
			if(FETCH_OUTPUT) {
				appEventPublisher.publishEvent(event);
			}
			System.out.println("Output Notification Processed.");
			return new ResponseEntity<String>("", HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<String>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@PostMapping(value="/loadData", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> loadData(@RequestBody String body, @RequestHeader("client_api_key") String key) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			HashMap<String, Object> bodyMap = objectMapper.readValue(body, HashMap.class);
			aaService.writeToFile(bodyMap);
			return new ResponseEntity<String>("", HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<String>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}
