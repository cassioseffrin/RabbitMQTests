package br.edu.seffrin.senderReceiver;

import java.io.*;
import java.security.*;
import com.rabbitmq.client.*;

public class Pagamento2 {
	
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();




		String USER = "arpag";
		String PASS = "po$p@g2244#$up3rvis%";
		String QUEUE = "03600477000104:4AD74FS9I:PAGAMENTO";
		factory.setUsername(USER);
		factory.setPassword(PASS);
		
		factory.setHost("18.228.222.107");
		factory.setPort(61613);

//		factory.useSslProtocol();
		// Tells the library to setup the default Key and Trust managers for you
		// which do not do any form of remote server trust verification

		Connection conn = factory.newConnection();
		Channel channel = conn.createChannel();
		
		
		byte[] messageBodyBytes = "Hello, world!".getBytes();
 
		

 
		channel.queueDeclare("rabbitmq-java-test", false, true, true, null);
		channel.basicPublish("", QUEUE, null, messageBodyBytes);
//
//		GetResponse chResponse = channel.basicGet("rabbitmq-java-test", false);
//		if (chResponse == null) {
//			System.out.println("No message retrieved");
//		} else {
//			byte[] body = chResponse.getBody();
//			System.out.println("Received: " + new String(body));
//		}
//
		channel.close();
		conn.close();
	}
	
	
 
}
