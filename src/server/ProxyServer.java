package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("unused")
public class ProxyServer extends Thread {

	private int port;
	private ServerSocket serverSocket;
	private Boolean isRunning = true;

//	public ProxyServer(int port) {
//
//		try {
//
//			serverSocket = new ServerSocket(port);
//			System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "..");
//
//		} catch (IOException se) {
//			se.printStackTrace();
//		}
//
//	}

	public ProxyServer(int port) {
		this.port = port;
	}

	public void conect() throws IOException {

		serverSocket = new ServerSocket(port);

		while (isRunning) {
			
			Socket client = serverSocket.accept();
			Thread servidor = new Thread(new Request(client));
			servidor.start();
		}
	}
}
