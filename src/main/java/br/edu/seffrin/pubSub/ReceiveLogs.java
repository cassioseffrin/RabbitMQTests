package br.edu.seffrin.pubSub;

 

import org.apache.commons.configuration.PropertiesConfiguration;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

 
public class ReceiveLogs {
	private static final String EXCHANGE_NAME = "pub-sub-queue";

	public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        PropertiesConfiguration config = new PropertiesConfiguration();
        config.load("application.properties");
        String HOST =  config.getString("HOST");
        String USER =  config.getString("USER");
        String PASS =  config.getString("PASS");
        Integer PORT =  config.getInt("PORT");
        factory.setHost(HOST);
        factory.setUsername(USER);
        factory.setPassword(PASS);
        factory.setPort(PORT);
        factory.useSslProtocol();
        
        
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();
 
  

		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println(" [*] Aguardando... CTRL+C para cancelar");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x] Recebida: '" + message + "'");
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});
	}
}