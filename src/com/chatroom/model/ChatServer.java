package com.chatroom.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer extends Thread{
	private static int port = 20202;
	Socket client=null;
	ServerSocket server = null;
	
	public void run() {
		
		try {
			server = new ServerSocket(port);
			System.out.println("Server started on port " + port);
		} catch(IOException e) {
			System.err.println("Could not listen to port: " + port);
			System.err.println(e);
			System.exit(1);
		}
		
		while(true) {
			try {
				client = server.accept();
			} catch (IOException e) {
				System.err.println("Accept failed");
				System.err.println(e);
				System.exit(1);
			}
			ClientConn cc = new ClientConn(client);
			Thread t = new Thread(cc);
			t.start();
		}
	}
}
