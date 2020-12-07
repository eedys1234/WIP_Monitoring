package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import api.XNet;

public class SocketCreator {
	
	private Socket socket;
	private int PORT = 9999;
	
	public SocketCreator() {
		
	}
	
	public void connect() throws UnknownHostException, IOException { 

		socket = new Socket(XNet.HOST, PORT);
	}
	
	public String getData() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		StringBuilder sb = new StringBuilder();
		
		String readLine = "";
		
		while((readLine = br.readLine()) != null) {
			
			
		}
		return null;
	}
}
