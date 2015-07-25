package com.mrsmithyx.jcontrolconsole;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

public class TCPConnection extends AsyncTask<Object, Object, Object> implements OnClickListener{
	private NotificationManager mNotifyManager;
	private Builder mBuilder;
	private FeedBack feedBack;
	private Activity activity;
	private Boolean useDialog = true;
	private String defaultMessage = "Processing Your Request!";
	private Connection connection;
	private ProgressDialog prog = null;
	@SuppressWarnings("deprecation")
	void init(String message){
		if(useDialog == false) return;
		prog = new ProgressDialog(activity);
		prog.setMessage(message);
		prog.setCanceledOnTouchOutside(false);
		prog.setButton("Cancel", this);
		prog.show();
	}
	
	private void NotifInit(){
		mNotifyManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(activity);
		mBuilder.setContentTitle("Awaiting")
				.setContentText("Connection").setSmallIcon(android.R.drawable.ic_popup_sync);

	}
	public TCPConnection(FeedBack feedBack, Activity activity, Connection connection){
		this.feedBack = feedBack;
		this.activity = activity;
		this.connection = connection;
		NotifInit();
	}
	public TCPConnection(FeedBack feedBack, Activity activity, Connection connection, Boolean useDialog){
		this.feedBack = feedBack;
		this.activity = activity;
		this.useDialog = useDialog;
		this.connection = connection;
		NotifInit();
	}	
	public TCPConnection(FeedBack feedBack, Activity activity, Connection connection, Boolean useDialog, String message){
		this.feedBack = feedBack;
		this.activity = activity;
		this.useDialog = useDialog;
		this.defaultMessage = message;
		this.connection=connection;
		NotifInit();
	}	
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		final Object res = result;
		if(useDialog == true && prog != null && prog.isShowing()){
			prog.dismiss();
		}
		mBuilder.setContentText("Recieved Response");
		mBuilder.setContentTitle("Recieved");
		// Removes the progress bar
		mBuilder.setProgress(0, 0, false);
		mNotifyManager.notify(1, mBuilder.build());
		feedBack.OnFeedBack(res);
		mNotifyManager.cancelAll();
			
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		init(defaultMessage);
		mBuilder.setProgress(100, 0, false);
		mNotifyManager.notify(1, mBuilder.build());
	}
	@Override
	protected void onProgressUpdate(Object... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		mBuilder.setProgress(100, Integer.parseInt(values[1].toString()), false);
		mBuilder.setContentText(values[0].toString());
		mBuilder.setContentTitle(values[2].toString());
		mNotifyManager.notify(1, mBuilder.build());
		
	}
	protected Object doInBackground(Object... params) {
		
		StringBuilder sb = new StringBuilder();
		Log.d("TCP", "C: Connecting To ..."); 
		String message = params[0].toString();
		String text = "";
		PrintWriter out = null;
		BufferedReader in = null;
		Socket socket = null;
		InetSocketAddress sock = new InetSocketAddress(connection.getIp(),connection.getPort());
		try { 
			this.publishProgress(new Object[]{"Connection","20","Awaiting"});
			socket = new Socket(sock.getAddress(), sock.getPort());
			
			this.publishProgress(new Object[]{"Connected","30","Request"});
		    Log.d("TCP/IP", "C: JControlConsoleApi: '" + message + "'"); 
		    out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true); 
		    this.publishProgress(new Object[]{"Sent","50","Request"});
		    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));                
		    this.publishProgress(new Object[]{"Recieving","70","Response"});
		    out.println(message);
		    while ((text = in.readLine()) != null) {
		        sb.append(text);
		        Log.d("TCP/IP", "R: JControlConsoleApi: " + text);
		    }
		    Log.d("TCP/IP", "C: Sent."); 
		    Log.d("TCP/IP", "R: Done.");               
		    this.publishProgress(new Object[]{"Completed","100","Request Completed"});
		} catch(Exception e) { 
		    Log.e("TCP/IP", "S: Error", e);
		    this.publishProgress(new Object[]{"Error","100", "Failed"});
		} finally { 
			try {
				socket.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				this.publishProgress(new Object[]{"Error","100", "Failed"});
				Log.e("Error Closing Socket", "The Socket Couldnt Be Closed?", e);
				
			}
		}
		return sb;
	}

	@Override
	protected void onCancelled(Object result) {
		// TODO Auto-generated method stub
		mBuilder.setContentText("Canceled Request");
		mBuilder.setContentTitle("Canceled");
		mBuilder.setSmallIcon(android.R.drawable.ic_popup_reminder);
		// Removes the progress bar
		mBuilder.setProgress(0, 0, false);
		mNotifyManager.notify(1, mBuilder.build());
		feedBack.OnFeedBack(new OrganizedResponse("Canceled","Request Canceled",null));
		mNotifyManager.cancelAll();
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		this.cancel(false);
		
	}

}