package br.edu.seffrin.stomp;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSClient {
	public static void main(String[] args) {

//    	String tcpBrokerURL = "tcp://b-d3844232-2ec2-4c49-87aa-256e9f878632-2.mq.sa-east-1.amazonaws.com:61614";		
//		String SSLbrokerURL = "ssl://b-d3844232-2ec2-4c49-87aa-256e9f878632-2.mq.sa-east-1.amazonaws.com:61617";
		String failOverBrokerURL = "failover:(ssl://b-d3844232-2ec2-4c49-87aa-256e9f878632-1.mq.sa-east-1.amazonaws.com:61617,ssl://b-d3844232-2ec2-4c49-87aa-256e9f878632-2.mq.sa-east-1.amazonaws.com:61617)";
 
 
		
		String username = "arpag";
		String password = "@rpa@pps2022";
		ConnectionFactory factory = new ActiveMQConnectionFactory(failOverBrokerURL);
		try {
			Connection connection = factory.createConnection(username, password);
			connection.start();

			Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
			Destination destination = session.createQueue("queue/10513613000186:4AD74FS9I:PAGAMENTO");
			MessageProducer producer = session.createProducer(destination);

			TextMessage message1 = session.createTextMessage(
					"{\"valor\":1223,\"pedido\":\"B621F871|1696002627\",\"queue\":\"10513613000186:B621F871:RECEBIMENTO\",\"parcelas\":1,\"operacao\":\"DEBITO\"}");

			producer.send(message1);

			session.commit();

//            MessageConsumer consumer = session.createConsumer(destination);
//            TextMessage receivedMessage1 = (TextMessage) consumer.receive();
//            System.out.println("Received message: " + receivedMessage1.getText());
//            session.commit();
//
//            TextMessage receivedMessage2 = (TextMessage) consumer.receive();
//            System.out.println("Received message: " + receivedMessage2.getText());
//            session.commit();

			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
