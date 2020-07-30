package com.srs.mockAA.models;

import java.util.HashMap;

public class FIRequest {
	private String sessionID;
	private HashMap<String, Object> vdrpDetails;
	
	public FIRequest(String sessionID, HashMap<String, Object> vdrpDetails) {
		super();
		this.sessionID = sessionID;
		this.vdrpDetails = vdrpDetails;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public HashMap<String, Object> getVdrpDetails() {
		return vdrpDetails;
	}

	public void setVdrpDetails(HashMap<String, Object> vdrpDetails) {
		this.vdrpDetails = vdrpDetails;
	}	
}
