package br.edu.seffrin.ui;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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

	private Main mApp;

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
//			String QUEUE = config.getString("QUEUE_P2_PDV");
			String serial = PropertiesManager.getProperty("SERIAL");
//			String QUEUE = config.getString("QUEUE_P2_PDV");
			String QUEUE = serial + ":PDV";
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

				mApp.addToMessages(message, Main.ATTR_PLAIN);

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

	public void bindWithGUI(Main app) {
		mApp = app;
	} 

	public void disconnect() {
		mApp.addToMessages("[Servidor]: desconectando...", Main.ATTR_SERVER);
		try {
			mIsConnected = false;
		} catch (Exception e) {
			mApp.addToMessages(
					"[Servidor]: Erro, não é possível desconectar você completamente.\n"
							+ "Seu nome de usuário pode ficar indisponível até que o servidor seja reiniciado.\n"
							+ "Se o aplicativo parecer não estar funcionando corretamente, por favor\n" + "reinicie-o.",
					Main.ATTR_ERROR);
			return;
		}
		mApp.addToMessages("[Server]: Desconectado.", Main.ATTR_SERVER);
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