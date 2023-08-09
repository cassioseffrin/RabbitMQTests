package br.edu.seffrin.senderReceiver;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {

	public static void main(String[] argv) throws Exception {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			PropertiesConfiguration config = new PropertiesConfiguration();
			config.load("application.properties");

			String HOST = config.getString("HOST");
			String USER = config.getString("USER");
			String PASS = config.getString("PASS");
			String QUEUE = config.getString("QUEUE");
			Integer PORT = config.getInt("PORT");

			factory.setHost(HOST);
			factory.setUsername(USER);
			factory.setPassword(PASS);
			factory.setPort(PORT);
			factory.useSslProtocol();

			Connection conn = factory.newConnection();
			Channel channel = conn.createChannel();

			channel.queueDeclare(QUEUE, true, false, false, null);

			for (int i = 0; i < 10; i++) {
				String message = "Teste do Java " + i;
				channel.basicPublish("", QUEUE, null, message.getBytes("UTF-8"));
				System.out.println(" [x] FILA: " + QUEUE + " Enviada '" + message + "'");
			}
			channel.close();
			conn.close();
		} catch (IOException | TimeoutException e) {
			throw new RuntimeException("Rabbitmq erro", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		}

	}
}
