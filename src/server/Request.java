package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import constants.Constants;
import utils.Utils;

@SuppressWarnings("unused")
public class Request extends Thread {

	private Socket client;
	private BufferedReader clientBr;
	private BufferedWriter clientBw;

	public Request(Socket client) throws IOException {
		this.client = client;
		this.client.setSoTimeout(2000);
		clientBr = new BufferedReader(new InputStreamReader(client.getInputStream()));
		clientBw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
	}

	@Override
	public void run() {

		String request = "";

		try {
			request = clientBr.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] resquetList = request.split(" ");
		String httpMethod = resquetList[0];
		String url = resquetList[1];

		// String port = resquetList[1].split(":")[1];

		if (!isAllowed(url)) {
			System.out.println("Acesso a essa pagina não é permitido!");
			
			try {
				BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
				String line = "HTTP/1.0 403 Access Forbidden \n" +
						"User-Agent: ProxyServer/1.0\n" +				
						"\r\n";
				
				bufferedWriter.write(line);
				bufferedWriter.flush();		
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {

			if (httpMethod.equals(Constants.CONNECT)) {
				String[] address = url.split(":");
				doRequest(address[0], Integer.parseInt(address[1]));
			}

		}

	}

	public boolean isAllowed(String url) {

		String[] blokers = Utils.getBlokerUrls();

		for (int i = 0; i < blokers.length; i++) {
			if (url.contains(blokers[i])) {
				return false;
			}
		}

		return true;
	}

	public void doRequest(String url, int port) {

		InetAddress address;
		Socket server;
		BufferedWriter serverBw;
		BufferedReader serverBr;



		try {
			
			String line = "HTTP/1.0 200 Connection established\r\n" +  "Proxy-Agent: ProxyServer/1.0\r\n" + "\r\n";
			clientBw.write(line);
			clientBw.flush();
			
			address = InetAddress.getByName(url);
			server = new Socket(address, port);

			System.out.println(address);

			serverBw = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
			serverBr = new BufferedReader(new InputStreamReader(server.getInputStream()));

			//ida cliente para servidor
			HttpsTransmition clientToServer = new HttpsTransmition(client.getInputStream(),
					server.getOutputStream());

			Thread t = new Thread(clientToServer);
			t.start();

			//volta servidor para cliente
			byte[] buffer = new byte[4096];
			int read = 0;

			while (read >= 0) {
				read = server.getInputStream().read(buffer);
				if (read > 0) {
					client.getOutputStream().write(buffer, 0, read);
					if (server.getInputStream().available() < 1) {
						client.getOutputStream().flush();
					}
				}

				String s = new String(buffer);

				System.out.println(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
