package com.mrsmithyx.jcontrolconsole;

public class CustomRequest {
	private String player, request;
	public CustomRequest(String player, String request, String[] params) {
		super();
		this.player = player;
		this.request = request;
		this.params = params;
	}
	public String getPlayer() {
		return player;
	}
	public String getRequest() {
		return request;
	}
	public String[] getParams() {
		return params;
	}
	private String[] params;
	

}
