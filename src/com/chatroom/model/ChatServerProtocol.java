package com.chatroom.model;

import java.util.Enumeration;
import java.util.Hashtable;

public class ChatServerProtocol {
	private String nick;
	private ClientConn conn;
	
	private static Hashtable<String, ClientConn> nicks = 
			new Hashtable<String, ClientConn>();
	
	private static final String msg_OK = "OK";
	private static final String msg_NICK_EXISTS = "NICK ALREADY EXISTS";
	private static final String msg_SPECIFY_NICK = "SPECIFY THE NICKNAME";
	private static final String msg_INVALID_CMD = "INVALID COMMAND";
	private static final String msg_SEND_FAIL = "FAILED TO SEND";
	
	private static boolean add_nick(String nick, ClientConn c) {
		if( nicks.containsKey(nick) ) {
			return false;
		} else {
			nicks.put(nick,c);
			return true;
		}
	}
	
	public ChatServerProtocol(ClientConn c) {
		nick = null;
		conn = c;
	}
	
	private void log(String msg) {
		System.err.println(msg);
	}
	
	private boolean isAuthenticated() {
		return ! (nick == null);
	}
	
	private String authenticate(String msg) {
		if(msg.startsWith("NICK")) {
			String tryNick = msg.substring(5);
			if(add_nick(tryNick, this.conn)) {
				log("Nick " + tryNick + " joined the room.");
				this.nick = tryNick;
				return msg_OK;
			} else {
				return msg_NICK_EXISTS;
			}
		} else {
			return msg_SPECIFY_NICK;
		}
	}
	
	private boolean sendMsg(String recipient, String msg) {
		if(nicks.containsKey(recipient)) {
			ClientConn c = nicks.get(recipient);
			c.sendMsg(nick + ": " + msg);
			return true;
		} else {
			return false;
		}
	}
	
	private void sendAll(String msg) {
		Enumeration<String> enumKey = nicks.keys();
		while(enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    ClientConn val = nicks.get(key);
		    
		    val.sendMsg(this.nick +": " + msg);
		}
		    
	}
	
	public String process(String msg) {
		if(!isAuthenticated()) {
			return authenticate(msg);
		}
		
		String[] msg_parts = msg.split(" ", 3);
		String msg_type = msg_parts[0];
		
		if(msg_type.equals("MSG")) {
			if(msg_parts.length < 3) {
				return msg_INVALID_CMD;
			}
			if(sendMsg(msg_parts[1], msg_parts[2])) {
				return msg_OK;
			} else {
				return msg_SEND_FAIL;
			}
		} else if(msg_type.equals("LOGOUT")) {
			System.out.println(nick + " Left the room.");
			sendAll(nick + " left the room");
			conn.logout();
			
			return msg_OK;
		}
		else {
			sendAll(msg);
			System.out.println("SENDING GLOBAL MESSAGE FROM " + nick + " : " + msg);
			return msg_OK;
		}
	}
	
	public String getNick() {
		return nick;
	}
}

