package com.mrsmithyx.jcontrolconsole;

public class Connection{
	private String ip;
	private int port;
	public Connection(String ip, int port){
		if(ip == null){
			throw new NullPointerException("The Ip address was null");
		}else if(port == 0){
			throw new NullPointerException("Please use a different port number other than 0");
		}
		this.ip = ip;
		this.port = port;
	}
	public String getIp(){
		return ip;
	}
	public int getPort(){
		return port;
	}
}
