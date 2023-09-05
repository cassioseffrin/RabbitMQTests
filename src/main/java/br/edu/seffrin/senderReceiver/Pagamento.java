package br.edu.seffrin.senderReceiver;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Pagamento {

	public static void main(String[] argv) throws Exception {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			PropertiesConfiguration config = new PropertiesConfiguration();
			config.load("application.properties");

			String HOST = config.getString("HOST_AWS");
			String USER = config.getString("USER_AWS");
			String PASS = config.getString("PASS_AWS");
			String QUEUE = config.getString("QUEUE_P2");
//			String QUEUE = config.getString("QUEUE_L300");
			Integer PORT = config.getInt("PORT_AWS");

			factory.setHost(HOST);
			factory.setUsername(USER);
			factory.setPassword(PASS);
			factory.setPort(PORT);
			factory.useSslProtocol();

			Connection conn = factory.newConnection();
			Channel channel = conn.createChannel();

			channel.queueDeclare(QUEUE, true, false, false, null);

			for (int i = 0; i < 1; i++) {
				String message = "{ \"operacao\": \"PAGAMENTO\", \"pedido\": \"A1\", \"valor\": 1211, \"tipo\": \"CREDITO\" }";
				channel.basicPublish("", QUEUE, null, message.getBytes("UTF-8"));
				System.out.println("Enviada para fila: " + QUEUE + " Enviada !'" + message + "'");
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
