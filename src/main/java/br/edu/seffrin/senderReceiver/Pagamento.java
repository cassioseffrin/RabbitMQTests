package br.edu.seffrin.senderReceiver;

 

import org.apache.commons.configuration.PropertiesConfiguration;

import com.rabbitmq.client.AMQP;
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
			String QUEUE = config.getString("QUEUE_P2_POS"); //03600477000104:PBG5233679630:POS
//			String QUEUE = config.getString("QUEUE_L300_POS"); //03600477000104:4AD74FS9I:POS
//			Integer PORT = config.getInt("PORT_AWS");

			factory.setHost("18.228.222.107");
			factory.setVirtualHost(HOST);
			factory.setUsername(USER);
			factory.setPassword(PASS);
			factory.setPort(5672);
//			factory.useSslProtocol();

			Connection conn = factory.newConnection();
			Channel channel = conn.createChannel();

			channel.queueDeclare(QUEUE, true, false, false, null);
				String message = "{ \"operacao\": \"PAGAMENTO\", \"pedido\": \"A1\", \"valor\": 88888, \"tipo\": \"CREDITO\" }";
				channel.basicPublish("", QUEUE, null, message.getBytes("UTF-8"));
				
//				channel.basicPublish("", QUEUE,
//			             new AMQP.BasicProperties.Builder()
//								.contentType("text/plain").deliveryMode(2).priority(1).userId("bob").build(),
//						message.getBytes("UTF-8"));
				
				System.out.println("Enviada para fila: " + QUEUE + " Enviada !'" + message + "'");
	 
			channel.close();
			conn.close();
		} catch (Exception   e) {
			throw new RuntimeException("Rabbitmq erro", e);
		}  

	}
}
