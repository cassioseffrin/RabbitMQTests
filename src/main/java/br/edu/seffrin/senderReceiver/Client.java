package br.edu.seffrin.senderReceiver;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.SerializationUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;

public class Client {

	private com.rabbitmq.client.Connection mConnection;
	private boolean mIsConnected;
	private String mName;
	private Application mApp;

	public Client(String host) {
		initCommunication(host);
	}

	private void initCommunication(String host) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setUsername("arpag");
		factory.setPassword("@rpa@pps2022");
		factory.setPort(5671);
		try {
			factory.useSslProtocol();
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public void bindWithGUI(Application app) {
		mApp = app;
	}

	public boolean connect(String name) {
		mApp.addToChat("[Server]: Initiating your connection...", Application.ATTR_SERVER);
		try {
			ConnectionFactory factory = new ConnectionFactory();
			PropertiesConfiguration config = new PropertiesConfiguration();
			config.load("application.properties");
			String HOST = config.getString("HOST");
			String USER = config.getString("USER");
			String PASS = config.getString("PASS");
			String QUEUE = config.getString("QUEUE_L300");
			Integer PORT = config.getInt("PORT");
			factory.setHost(HOST);
			factory.setUsername(USER);
			factory.setPassword(PASS);
			factory.setPort(PORT);
			factory.useSslProtocol();
			Connection conn = factory.newConnection();
			Channel channel = conn.createChannel();
			channel.queueDeclare(QUEUE, true, false, false, null);
			for (int i = 0; i < 1; i++) {
				String message = "{ \"operacao\": \"PAGAMENTO\", \"pedido\": \"A1\", \"valor\": 1002, \"tipo\": \"CREDITO\" }";
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
		} catch (Exception e) {
			mApp.addToChat("[Server]: Error with the server, try again or " + "relaunch the app.",
					Application.ATTR_ERROR);
			return false;
		}
		mApp.addToChat("[Server]: You are connected as \"" + mName + "\".", Application.ATTR_SERVER);
		return true;
	}

	private BlockingQueue<Object> connectRPC(String name) throws IOException {
		return null;
	}

	public void disconnect() {
		mApp.addToChat("[Server]: Initiating your disconnection...", Application.ATTR_SERVER);
		try {
			mIsConnected = false;
		} catch (Exception e) {
			mApp.addToChat(
					"[Server]: Error, cannot completely disconnect you. "
							+ "Your username may be unavailable until the server restarts."
							+ "If the application seems to be not running correctly, please " + "restart it.",
					Application.ATTR_ERROR);
			return;
		}
		mApp.clearUsersList();
		mApp.addToChat("[Server]: Disconnection finished.", Application.ATTR_SERVER);
	}

	public void sendMessage(String message) {
		String DATE_FORMAT = "HH:mm:ss";
		String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
		Message msg = new Message(mName, message, time);
		try {
			ConnectionFactory factory = new ConnectionFactory();
			PropertiesConfiguration config = new PropertiesConfiguration();
			config.load("application.properties");
			String HOST = config.getString("HOST");
			String USER = config.getString("USER");
			String PASS = config.getString("PASS");
			String QUEUE = config.getString("QUEUE_L300");
			Integer PORT = config.getInt("PORT");
			factory.setHost(HOST);
			factory.setUsername(USER);
			factory.setPassword(PASS);
			factory.setPort(PORT);
			try {
				factory.useSslProtocol();
			} catch (KeyManagementException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			Connection conn = factory.newConnection();
			Channel channel = conn.createChannel();
			channel.queueDeclare(QUEUE, true, false, false, null);
			for (int i = 0; i < 1; i++) {
				String mm = "{ \"operacao\": \"PAGAMENTO\", \"pedido\": \"A1\", \"valor\": 1002, \"tipo\": \"CREDITO\" }";
				channel.basicPublish("", QUEUE, null, mm.getBytes("UTF-8"));
				System.out.println("Enviada para fila: " + QUEUE + " Enviada !'" + mm + "'");
			}
			channel.close();
			conn.close();
		} catch (IOException | TimeoutException | ConfigurationException e) {
			mApp.addToChat("[Server]: Error, cannot distribute this message.", Application.ATTR_ERROR);
		}
	}

	private void onReceiveMessage(String consumerTag, Delivery delivery) {
		Message message = SerializationUtils.deserialize(delivery.getBody());
		mApp.addToChat("(" + message.getTime() + ") ", Application.ATTR_BOLD);
		mApp.addToChat(message.getName() + ": ", Application.ATTR_BOLD);
		mApp.addToChat(message.getContent(), Application.ATTR_PLAIN);
	}

	private void onReceiveConnection(String consumerTag, Delivery delivery) {
		Connection connection = SerializationUtils.deserialize(delivery.getBody());
	}

	public boolean isConnected() {
		return mIsConnected;
	}

	public void closeRabbitMQ() {
		try {
			mConnection.close();
		} catch (IOException e) {
			System.err.println("Error: when closing rabbitMQ connection" + e);
		}
	}
}