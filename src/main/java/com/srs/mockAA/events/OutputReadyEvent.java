package com.srs.mockAA.events;

import org.springframework.context.ApplicationEvent;

public class OutputReadyEvent extends ApplicationEvent {
	
	private static final long serialVersionUID = 1L;

	public OutputReadyEvent(String executionid) {
		super(executionid);
	}
}
