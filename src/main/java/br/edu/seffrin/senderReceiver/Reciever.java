package br.edu.seffrin.senderReceiver;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Reciever {

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		PropertiesConfiguration config = new PropertiesConfiguration();
		config.load("application.properties");
		String HOST = config.getString("HOST");
		String USER = config.getString("USER");
		String PASS = config.getString("PASS");
		Integer PORT = config.getInt("PORT");
//		String QUEUE = config.getString("QUEUE_P2");
		String QUEUE = config.getString("QUEUE_L300");
		factory.setHost(HOST);
		factory.setUsername(USER);
		factory.setPassword(PASS);
		factory.setPort(PORT);
		factory.useSslProtocol();

		Connection conn = factory.newConnection();
		Channel channel = conn.createChannel();

		channel.queueDeclare(QUEUE, true, false, false, null);
		System.out.println(" [*] Fila: " + QUEUE + " Aguardando Mensagens. CTRL+C para sair");
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x]  " + QUEUE + " Recebida '" + message + "'");
		};

		channel.basicConsume(QUEUE, true, deliverCallback, consumerTag -> {
		});
	}
}
