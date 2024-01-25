package main;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

class chatThreadSender extends Thread {
	 private int serverPort;
	 private DatagramSocket aSocket;
	 private String aHost ;
	 private Scanner in = new Scanner(System.in);

	 chatThreadSender(DatagramSocket aSocket, String aHost, int serverPort) {
        this.aSocket = aSocket;
        this.aHost = aHost;
        this.serverPort = serverPort; 
        
    }
    
    public void run() {
		 while(true) {
			 try {
	  			System.out.print(">>");
	  			String comand = in.next();
	  			byte[] msg = comand.getBytes();
	  		    //byte[] buffer = new byte[1024];
	  		    InetAddress host = InetAddress.getByName(aHost);
	  			DatagramPacket request = new DatagramPacket(msg, msg.length, host, serverPort);
	  			aSocket.send(request);
	  			//DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
	 			//aSocket.receive(reply);
	 			//System.out.println("Reply>> "+ new String(reply.getData()));  	
			 }
			 catch(Exception e) {
	                System.err.println(e);
	         }
		 }
    }
}