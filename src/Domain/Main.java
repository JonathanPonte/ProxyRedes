package Domain;

import java.io.IOException;

import server.ProxyServer;

public class Main {

	public static void main(String[] args) throws IOException {
		

		ProxyServer proxyServer = new ProxyServer(8080);
		proxyServer.conect();
		
	}

}
