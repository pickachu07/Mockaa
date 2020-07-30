package com.srs.mockAA.events;

import java.util.HashMap;

import org.springframework.context.ApplicationEvent;

public class ActionInvokedEvent extends ApplicationEvent {
	
private static final long serialVersionUID = 1L;
	
	public ActionInvokedEvent(HashMap<String, Object> invocationResponse) {
		super(invocationResponse);
	}
}
