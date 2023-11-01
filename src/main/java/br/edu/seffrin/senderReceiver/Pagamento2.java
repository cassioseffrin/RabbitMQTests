package br.edu.seffrin.senderReceiver;

import java.io.*;
import java.security.*;
import com.rabbitmq.client.*;

public class Pagamento2 {
	
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();


		factory.useSslProtocol();

		String USER = "arpag";
		String PASS = "@rpa@pps2022";
		String QUEUE = "03600477000104:4AD74FS9I:PAGAMENTO";
		factory.setUsername(USER);
		factory.setPassword(PASS);
		
		factory.setHost("b-83aea7ca-f7bc-4d1c-9a09-976c24906430-1.mq.sa-east-1.amazonaws.com");
		factory.setPort(5671);

		// Tells the library to setup the default Key and Trust managers for you
		// which do not do any form of remote server trust verification

		Connection conn = factory.newConnection();
		Channel channel = conn.createChannel();
		
		
		byte[] messageBodyBytes = "Hello, world!".getBytes();
//		channel.basicPublish(exchangeName, routingKey,
//		             new AMQP.BasicProperties.Builder()
//		               .contentType("text/plain")
//		               .userId("userId")
//		               .build(),
//		               messageBodyBytes);
		

//		// non-durable, exclusive, auto-delete queue
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
//		channel.close();
//		conn.close();
	}
	
	
 
}
