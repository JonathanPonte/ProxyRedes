package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;

public class HttpsTransmition extends Thread{

	InputStream proxyToClientIS;
	OutputStream proxyToServerOS;
	
	public HttpsTransmition(InputStream proxyToClientIS, OutputStream proxyToServerOS) {
		this.proxyToClientIS = proxyToClientIS;
		this.proxyToServerOS = proxyToServerOS;
	}

	public void run(){
		try {
			byte[] buffer = new byte[4096];
			int read;
			do {
				read = proxyToClientIS.read(buffer);
				if (read > 0) {
					proxyToServerOS.write(buffer, 0, read);
					if (proxyToClientIS.available() < 1) {
						proxyToServerOS.flush();
					}
				}
			} while (read >= 0);
		}
		catch (SocketTimeoutException ste) {
		
		}
		catch (IOException e) {
			System.out.println("Proxy to client HTTPS read timed out");
			e.printStackTrace();
		}
	}
	
	
}
