package br.edu.seffrin.pubSub;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLog {
	private static final String EXCHANGE_NAME = "pub-sub-queue";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		PropertiesConfiguration config = new PropertiesConfiguration();
		config.load("application.properties");
		String HOST = config.getString("HOST");
		String USER = config.getString("USER");
		String PASS = config.getString("PASS");
		Integer PORT = config.getInt("PORT");
		factory.setHost(HOST);
		factory.setUsername(USER);
		factory.setPassword(PASS);
		factory.setPort(PORT);
		factory.useSslProtocol();

		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

			for (int i = 0; i < 15; i++) {
				String message = "Mensagem testte - " + i;
				channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
				System.out.println(" [x] Enviada: '" + message + "'");
			}
		}
	}
}
