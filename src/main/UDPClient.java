package main;

import java.net.DatagramSocket; 

public class UDPClient { 

	public static void main(String[] args) throws Exception {
			String host = null;
			int serverPort = 9876;
			 if( args . length < 1) {
				 System . out . println (" Usage : UDPClient <msg > <server host name >");
				 System . exit ( -1);
			 }
			 else {
				 host = args [0];
			 }
			 System . out . println ("You entered first session - you have to add users \n"
			 		+ "Comands syntax: <paramter>|<nick>|<message>\n"
			 		+ "<paramters>:\n"
			 		+ "+ - add user\n"
			 		+ "- - delete user\n"
			 		+ "? - show all users\n"
			 		+ "# - show all users using key characters\n"
			 		+ "! - send message\n"
			 		+ "Terminal opened:");
			
			DatagramSocket socket = new DatagramSocket();
			Thread sender = new Thread(new chatThreadSender(socket, host, serverPort));
			Thread receiver = new Thread(new chatThreadReceiver(socket));
			sender.start();
			receiver.start();
			/*synchronizedPacket data = new synchronizedPacket(socket, host, serverPort);
			Thread sender = new Thread(new threadSender(data));
		    Thread receiver = new Thread(new threadReceiver(data));
			sender.start();
			receiver.start();*/
	}
}
	


