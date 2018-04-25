package activitystreamer.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import activitystreamer.util.Settings;


public class ClientSkeleton extends Thread {
	private static final Logger log = LogManager.getLogger();
	private static ClientSkeleton clientSolution;
	private TextFrame textFrame;
	
	private Socket socket;
	private JSONParser jsonparser=new JSONParser();
	
	public static ClientSkeleton getInstance(){
		if(clientSolution==null){
			clientSolution = new ClientSkeleton();
		}
		return clientSolution;
	}
	
	public ClientSkeleton(){
		
		textFrame = new TextFrame();
		try {
			socket=new Socket(Settings.getLocalHostname(),Settings.getLocalPort());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		start();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public void sendActivityObject(JSONObject activityObj){
		
		try {
			DataOutputStream dataoutputstream = new DataOutputStream(socket.getOutputStream());
			OutputStreamWriter outputstreamwriter=new OutputStreamWriter(dataoutputstream);
			BufferedWriter bufferedwriter=new BufferedWriter(outputstreamwriter);
			bufferedwriter.write(activityObj.toJSONString()+"\n");
			bufferedwriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void disconnect(){
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void run(){
		try {
			DataInputStream datainputstream = new DataInputStream(socket.getInputStream());
			InputStreamReader inputstreamreader=new InputStreamReader(datainputstream);
			BufferedReader bufferedreader =new BufferedReader(inputstreamreader);
			
			String reply=null;
			while((reply=bufferedreader.readLine())!=null)
			{
				try {
					JSONObject replyObj=(JSONObject)jsonparser.parse(reply);
					System.out.println(replyObj.toJSONString());
					textFrame.setOutputText(replyObj);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
}
