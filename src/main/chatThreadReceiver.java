package main;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

class chatThreadReceiver extends Thread {
        DatagramSocket aSocket;
        private byte buffer[];
       
        chatThreadReceiver(DatagramSocket s) {
            aSocket = s;    
        }
        public void run() {
            while (true) {
                try {
                	buffer = new byte[1024];
         			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
         			aSocket.receive(reply);
         			System.out.println(new String(reply.getData()));
         			System.out.print(">>");
           	 }  
                catch(Exception e) {
                 System.err.println(e);
             }
            }
        }
    }