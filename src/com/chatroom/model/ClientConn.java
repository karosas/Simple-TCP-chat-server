package com.chatroom.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConn implements Runnable {
	private Socket client;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private boolean running;
	ClientConn(Socket client) {
		running = true;
		this.client = client;
		try {
			in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			
			out = new PrintWriter(client.getOutputStream(), true);
		} catch(IOException e) {
			System.err.println(e);
			return;
		}
	}
	
	public void logout() {
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		running = false;
	}
	
	public void run() {
		String msg, response;
		ChatServerProtocol protocol = new ChatServerProtocol(this);
		try {
			while((msg = in.readLine()) != null) {
				response = protocol.process(msg);
				
					out.println("SERVER: " + response);
					if(!running) break;
				
			}
		} catch(IOException e) {
			System.err.println(e);
		}
	}
	
	public void sendMsg(String msg) {
		out.println(msg);
	}
}
