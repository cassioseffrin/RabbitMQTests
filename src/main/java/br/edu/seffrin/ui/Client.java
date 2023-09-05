package br.edu.seffrin.ui;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Client {

	private com.rabbitmq.client.Connection mConnection;
	private boolean mIsConnected;
 
	private Arpag mApp;

 

	public Client(String host) {
		initCommunication();
	}

	private void initCommunication() {

		try {

			ConnectionFactory factory = new ConnectionFactory();
			PropertiesConfiguration config = new PropertiesConfiguration();
			config.load("application.properties");

			String HOST = config.getString("HOST_AWS");
			String USER = config.getString("USER_AWS");
			String PASS = config.getString("PASS_AWS");
			String QUEUE = config.getString("QUEUE_P2_PDV");
//			String QUEUE = config.getString("QUEUE_L300");
			Integer PORT = config.getInt("PORT_AWS");

			factory.setHost(HOST);
			factory.setUsername(USER);
			factory.setPassword(PASS);
			factory.setPort(PORT);
			factory.useSslProtocol();

			Connection conn = factory.newConnection();
			Channel mChannel = conn.createChannel();

			mChannel.queueDeclare(QUEUE, true, false, false, null);
			System.out.println(" [*] Fila: " + QUEUE + " Aguardando Mensagens. CTRL+C para sair");
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");

				System.out.println(" [x]  " + QUEUE + " Recebida '" + message + "'");

				mApp.addToChat(message, Arpag.ATTR_PLAIN);

			};

			mChannel.basicConsume(QUEUE, true, deliverCallback, consumerTag -> {
			});

		} catch (TimeoutException | IOException e) {
			System.err.println("Error: " + e);
			System.exit(-1);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
 
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
 
			e.printStackTrace();
		}
	}

	public void bindWithGUI(Arpag app) {
		mApp = app;
	}

	public boolean connect(String name) {
		mApp.addToChat("[Server]: Initiating your connection...", Arpag.ATTR_SERVER);

		try {
			BlockingQueue<Object> response = connectRPC(name);

			boolean isConnected = (boolean) response.take();
			@SuppressWarnings("unchecked")
			ArrayList<Message> messageHistory = (ArrayList<Message>) response.take();
			@SuppressWarnings("unchecked")
			ArrayList<String> connectedClients = (ArrayList<String>) response.take();

			System.out.println(connectedClients);

			if (!isConnected) {
	 
				return false;
			} else {
	 
		 
				mIsConnected = true;
	 
				connectedClients.forEach(client -> mApp.addToUsersList(client));
	 
				messageHistory.forEach(message -> {
					mApp.addToChat("(" + message.getTime() + ") ", Arpag.ATTR_BOLD);
					mApp.addToChat(message.getName() + ": ", Arpag.ATTR_BOLD);
					mApp.addToChat(message.getContent(), Arpag.ATTR_PLAIN);
				});
			}
		} catch (Exception e) {
			mApp.addToChat("[Servidor]: Erro ao conectar",
					Arpag.ATTR_ERROR);
			return false;
		}


		return true;
	}

	private BlockingQueue<Object> connectRPC(String name) throws IOException {
		return null;
	}

	public void disconnect() {
		mApp.addToChat("[Servidor]: desconectando...", Arpag.ATTR_SERVER);
		try {
			mIsConnected = false;
		} catch (Exception e) {
			mApp.addToChat(
					"[Servidor]: Erro, não é possível desconectar você completamente.\n"
					+ "Seu nome de usuário pode ficar indisponível até que o servidor seja reiniciado.\n"
					+ "Se o aplicativo parecer não estar funcionando corretamente, por favor\n"
					+ "reinicie-o.",
					Arpag.ATTR_ERROR);
			return;
		}
		mApp.clearUsersList();
		mApp.addToChat("[Server]: Disconnection finished.", Arpag.ATTR_SERVER);
	}
 

 

	public boolean isConnected() {
		return mIsConnected;
	}

	public void closeRabbitMQ() {
		try {
			mConnection.close();
		} catch (IOException e) {
			System.err.println("Erro ao fechar o RMQ!" + e);
		}
	}
}