package com.srs.mockAA.models;

public class Notifier {
	private String type;
	private String id;
	
	public Notifier() {
		this.type = "AA";
		this.id = "AA-14";
	}

	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public void setId(String id) {
		this.id = id;
	}
}
