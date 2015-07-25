package com.mrsmithyx.jcontrolconsole;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;

import org.json.*;

/**
 * @author Mr Smithy x
 *	This API was set up by Mr Smithy x. Youtube: xDudek13lx
 *	Please do not re-distribute on any other site. This is the client side
 *	@version 1.0
 *	
 */
public class JControlConsole implements FeedBack{
	
	FeedBack feedBack;
	Activity activity;
	Connection connection;
	TCPConnection tcp;
	Boolean showDialog;
	String message;
	int type = 0;
	public JControlConsole(FeedBack feedBack, Activity activity, Connection connection) {
		// TODO Auto-generated constructor stub
		this.feedBack = feedBack;
		this.activity = activity;
		this.connection = connection;
		type = 0;
	}
	public JControlConsole(FeedBack feedBack, Activity activity, Connection connection, Boolean showDialog) {
		// TODO Auto-generated constructor stub
		this.feedBack = feedBack;
		this.activity = activity;
		this.connection = connection;
		this.showDialog = showDialog;
		type = 1;
	}
	public JControlConsole(FeedBack feedBack, Activity activity, Connection connection, Boolean showDialog, String message) {
		// TODO Auto-generated constructor stub
		this.feedBack = feedBack;
		this.activity = activity;
		this.connection = connection;
		this.showDialog = showDialog;
		this.message = message;
		type = 2;
	}
	
	private void reInit(){
		 if(type == 0) tcp = new TCPConnection(this,activity, connection);
		 else if(type == 1) tcp = new TCPConnection(this,activity, connection, showDialog);
		 else if(type == 2) tcp = new TCPConnection(this,activity, connection, showDialog, message);
	}

	public static JSONObject CreateJsonRequest(CustomRequest custom){
		if(custom.getPlayer() == null) throw new NullPointerException("Player is null!");
		if(custom.getRequest() == null)throw new NullPointerException("request is null!");
		if(custom.getParams().length != 5)throw new NullPointerException("You need an array of 5");
		JSONObject obj = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject param = new JSONObject();
		try{
		obj.put("request", custom.getRequest());
			data.put("player", custom.getPlayer());
		obj.put("data", data);
			param.put("p1", custom.getParams()[0]);
			param.put("p2", custom.getParams()[1]);
			param.put("p3", custom.getParams()[2]);
			param.put("p4", custom.getParams()[3]);
			param.put("p5", custom.getParams()[4]);
		obj.put("params", param);
		}catch(Exception ex){
			Log.e("An Error Occured?","JSONException?",ex);
		}
		return obj;
	}
	
	public void sendCustomRequest(CustomRequest Request){
		reInit();
		JSONObject jobj = CreateJsonRequest(Request);
		tcp.execute(jobj.toString());		
	}
	public void getAttachedProcID(){
		reInit();
		JSONObject jobj = CreateJsonRequest(new CustomRequest("console","GetAttachedProcID",new String[]{"","","","",""}));
		tcp.execute(jobj.toString());
	}
	public void getConsoles(){
		reInit();
		JSONObject jobj = CreateJsonRequest(new CustomRequest("console","GetConsoles",new String[]{"","","","",""}));
		tcp.execute(jobj.toString());
	}
	public void connect(){
		reInit();
		JSONObject jobj = CreateJsonRequest(new CustomRequest("console","Connect",new String[]{"","","","",""}));
		tcp.execute(jobj.toString());
	}
	public void connect(String ip){
		reInit();
		JSONObject jobj = CreateJsonRequest(new CustomRequest("console","Connect",new String[]{ip,"","","",""}));
		tcp.execute(jobj.toString());
	}
	public void attach(){
		reInit();
		JSONObject jobj = CreateJsonRequest(new CustomRequest("console","Attach",new String[]{"","","","",""}));
		tcp.execute(jobj.toString());
	}
	public void disconnect(){
		reInit();
		JSONObject jobj = CreateJsonRequest(new CustomRequest("console","Disconnect",new String[]{"","","","",""}));
		tcp.execute(jobj.toString());
	}
	public void shutDown(){
		reInit();
		JSONObject jobj = CreateJsonRequest(new CustomRequest("console","ShutDown",new String[]{"","","","",""}));
		tcp.execute(jobj.toString());
	}
	public void softBoot(){
		reInit();
		JSONObject jobj = CreateJsonRequest(new CustomRequest("console","SoftBoot",new String[]{"","","","",""}));
		tcp.execute(jobj.toString());
	}
	public void hardBoot(){
		reInit();
		JSONObject jobj = CreateJsonRequest(new CustomRequest("console","HardBoot",new String[]{"","","","",""}));
		tcp.execute(jobj.toString());
	}
	public void sendNotify(String message){
		reInit();
		JSONObject jobj = CreateJsonRequest(new CustomRequest("console","Notify",new String[]{message,"","","",""}));
		tcp.execute(jobj.toString());
	}
	public void sendBuzzer(){
		reInit();
		JSONObject jobj = CreateJsonRequest(new CustomRequest("console","Buzzer",new String[]{"","","","",""}));
		tcp.execute(jobj.toString());
	}
	//Returns the feed to the main ui. It's a handler	
	@Override
	public void OnFeedBack(Object data) {
		// TODO Auto-generated method stub
		if(data instanceof OrganizedResponse){
			this.feedBack.OnFeedBack((OrganizedResponse)data);
			return;
		}
		String response = "ParseError";
		String message = "No FeedBack";
		JSONObject jsonObject = null;
		List<Pair> pair = null;
		JSONArray jarr = null;
		OrganizedResponse Organized;
		try {
			jsonObject = new JSONObject(data.toString());
			response = jsonObject.getString("res");
			message = jsonObject.getString("mes");
			jarr = jsonObject.getJSONArray("pair");
			pair = new ArrayList<Pair>();
			for(int i = 0; i < jarr.length(); i++){
				JSONObject json = jarr.getJSONObject(i);
				pair.add(new Pair(json.getString("Key"),json.getString("Value")));
			}
			if(response.contains("Consoles")){
				Organized = new OrganizedResponse(response, message, pair);
				this.feedBack.OnFeedBack(Organized);
			}
			else{
				this.feedBack.OnFeedBack(Organized = new OrganizedResponse(response, message, pair));
			}
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.feedBack.OnFeedBack(new OrganizedResponse(response, message, null));
			
		}
	}
}