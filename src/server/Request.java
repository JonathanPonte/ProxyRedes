package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Request extends Thread {

	private Socket client;
	private BufferedReader proxyToClientBr;
	private BufferedWriter proxyToClientBw;
	
	public Request(Socket client) throws IOException{
		this.client = client;
		this.client.setSoTimeout(2000);
		proxyToClientBr = new BufferedReader(new InputStreamReader(client.getInputStream()));
		proxyToClientBw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
}
	
	@Override
	public void run() {
		
		String request = "";
		
		try {
			request = proxyToClientBr.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(request);
		
	}

}
