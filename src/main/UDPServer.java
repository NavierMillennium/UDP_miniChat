package main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.nio.charset.StandardCharsets;

public class UDPServer {

	private Map<String, chatUser> registeredUsers = new HashMap<String, chatUser>();
	private DatagramSocket aSocket;
	private int ServerPort;
	public UDPServer(int ServerPort) {
		try {
			this.ServerPort = ServerPort;
			this.aSocket =  new DatagramSocket(9876);
		}
		catch ( SocketException ex) {
			 Logger . getLogger ( UDPServer . class . getName ()). log( Level . SEVERE , null , ex );
			 } 
	}
	
	public static void main(String[] args) {
		UDPServer server = new UDPServer(9876);
		
		try {
			byte [] buffer = new byte [1024];
			System.out.println("Server works on:"+server.ServerPort+" port number");
			while (true) {
				
				 DatagramPacket receivePacket = new DatagramPacket (buffer,buffer.length );
				 server.aSocket.receive(receivePacket);
	
				String com = new String(receivePacket.getData(),StandardCharsets.UTF_8).trim();
				String[] pola = com.split("[|]");
				if (pola.length <= 2) {
					chatUser l = new chatUser(receivePacket.getAddress(), receivePacket.getPort());
					server.send("Syntax error!",l);
				}
				else {
					switch (pola[0]) {
					case "+":
						server.add(pola[1], receivePacket.getAddress(), receivePacket.getPort());
						break;
					case "-":
						server.delete(pola[1], receivePacket.getAddress(), receivePacket.getPort());
						break;
					case "?":
						server.showUsers(pola[1], receivePacket.getAddress(), receivePacket.getPort());
						break;
					case "#":
						server.filteredUsers(pola[1], receivePacket.getAddress(), receivePacket.getPort());
						break;
					case "!":
						server.messageSender(receivePacket);
						break;
					default:
						chatUser l = new chatUser(receivePacket.getAddress(), receivePacket.getPort());
						server.send("'"+pola[0]+"'"+" - Error: comand not found",l);
						break;		
					}
				}
			}
		}
		catch ( SocketException ex) {
			 Logger . getLogger ( UDPServer . class . getName ()). log( Level . SEVERE , null , ex );
			 } 
		catch ( IOException ex) {
			 Logger . getLogger ( UDPServer . class . getName ()). log( Level . SEVERE , null , ex );
			 }
	
	}
		
		private void send(String komunikat, chatUser odbiorca) {
			byte [] buffer = new byte [1024];
		    buffer = komunikat.getBytes(Charset.forName("UTF-8"));
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length, odbiorca.getAddr(), odbiorca.getPort());		
			try {
				aSocket.send(reply);
			} catch (SocketException ex) {
				Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE,null,ex);
			} catch ( IOException ex) {
				 Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE,null,ex);
				 }
			
		}
		
		private void add(String nick, InetAddress addr, int port) {
			chatUser l = new chatUser(addr,port);
			String s;
			if (!registeredUsers.containsKey(nick)) {
				registeredUsers.put(nick,l);
				s= "@server>>User:'" + nick + "' added successfully";	
			} 
			else
				s= "@server>>User:" + nick + " already exist!";
		
			send(s,l);

		}
		
		private void delete(String nick, InetAddress addr, int port) {
			chatUser removed = registeredUsers.remove(nick);
			String s;
			if (removed != null) {
				s= "@server>>User:'" + nick + "' deleted successfully";
			} else
				s= "@server>> operation failed!";
			
			send(s,removed);
		}
		
		private void showUsers(String keyCharacters, InetAddress addr, int port) {
			chatUser receiver = new chatUser(addr,port);
			Set<String> users = registeredUsers.keySet() ;
			String out =String.join(",",users);
			send(out,receiver);	
		}
		
		private void filteredUsers(String keyCharacters, InetAddress addr, int port) {
			chatUser receiver = new chatUser(addr,port);
			List<String> filteredUsers = new ArrayList<String>();
			try {
				registeredUsers.forEach((k,v) -> {
					if (k.toLowerCase().matches("(.*)"+keyCharacters.toLowerCase()+"(.*)")) 
						filteredUsers.add(k);});
				String out =String.join(",",filteredUsers);
				send(out,receiver);
			}
			catch (Exception ex) {
				filteredUsers.addAll(this.registeredUsers.keySet());
			}
				
		}
		
		private void messageSender(DatagramPacket receivePacket) {

			String com = new String(receivePacket.getData(),StandardCharsets.UTF_8).trim();
			String[] parts = com.split("[|]");
			String senderNick="unrecognized";
			DatagramPacket replySender, replyReceiver;
			if (!registeredUsers.containsKey(parts[1])) {
				byte msg[]  = ("@server>>Error: receiver not found!").getBytes();
				replySender = new DatagramPacket(msg, msg.length, receivePacket.getAddress(), receivePacket.getPort());		
			} 
			else {
				
				chatUser sender = new chatUser(receivePacket.getAddress(), receivePacket.getPort());
				
				
				for (Map.Entry<String, chatUser> entry : registeredUsers.entrySet()){
					if(entry.getValue().equals(sender)) {
						senderNick = entry.getKey();
						break;		
					}
				}
				chatUser receiver = registeredUsers.get(parts[1]);
				byte msgSender[]  = ("@server>>message send successfully!").getBytes();
				byte msgReceiver[]  = ("@"+senderNick+">>"+parts[2]).getBytes();
				System.out.println(parts[2]);
				replySender = new DatagramPacket(msgSender, msgSender.length, receivePacket.getAddress(), receivePacket.getPort());
				replyReceiver = new DatagramPacket(msgReceiver, msgReceiver.length, receiver.getAddr(), receiver.getPort());
				try {
					aSocket.send(replyReceiver);
					aSocket.send(replySender);
				}
				catch ( SocketException ex) {
					 Logger . getLogger ( UDPServer . class . getName ()). log( Level . SEVERE , null , ex );
					 } 
				catch ( IOException ex) {
					 Logger . getLogger ( UDPServer . class . getName ()). log( Level . SEVERE , null , ex );
					 }
			}
			
		}

	}


	
	
	
	



