package com.srs.mockAA.events;

import org.springframework.context.ApplicationEvent;

import com.srs.mockAA.models.FIRequest;

public class FIRequestEvent extends ApplicationEvent {
	
	private static final long serialVersionUID = 1L;
	
	public FIRequestEvent(FIRequest request) {
		super(request);
	}
}
