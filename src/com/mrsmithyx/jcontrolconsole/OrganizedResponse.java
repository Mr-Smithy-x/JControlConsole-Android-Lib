package com.mrsmithyx.jcontrolconsole;

import java.util.List;

public class OrganizedResponse {
	
	public String getResponse() {
		return response;
	}
	public String getMessage() {
		return message;
	}
	public List<Pair> getPairs() {
		return pair;
	}
	public OrganizedResponse(String response, String message, List<Pair> pair) {
		super();
		this.response = response;
		this.message = message;
		this.pair = pair;
	}
	private String response, message;
	private List<Pair> pair; 
}

