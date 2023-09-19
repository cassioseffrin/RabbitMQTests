package br.edu.seffrin.stomp;

import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import org.apache.activemq.transport.stomp.Stomp.Headers.Subscribe;
import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.activemq.transport.stomp.StompFrame;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;

class WrapInt {
	public int v = 0;
}

public class StompClient {

	private static final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.S");

	public static void main(String[] args) throws Exception {
		CommandLine cmd = parseAndValidateCommandLineArguments(args);
		final WrapInt count = new WrapInt();
		final int interval = Integer.parseInt(cmd.getOptionValue("interval", "1000"));
		String name = cmd.getOptionValue("name", UUID.randomUUID().toString());

		try {
			StompConnection connection = new StompConnection();
			String url = "b-d3844232-2ec2-4c49-87aa-256e9f878632-2.mq.sa-east-1.amazonaws.com";
			String user = "arpag";
			String password = "@rpa@pps2022";

			SocketFactory factory = SSLSocketFactory.getDefault();

//            Socket socket = factory.createSocket(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
//            b-d3844232-2ec2-4c49-87aa-256e9f878632-1.mq.sa-east-1.amazonaws.com:61614
//            b-d3844232-2ec2-4c49-87aa-256e9f878632-2.mq.sa-east-1.amazonaws.com   

			Socket socket = factory.createSocket(url, 61614);
			connection.open(socket);
			connection.connect(user, password, name);
			System.out.println(String.format("Successfully connected to %s", cmd.getOptionValue("url")));

			sendMessages(connection, "topico ou fila", "aqui vai o json", name, interval, count);
//            if (cmd.getOptionValue("mode").contentEquals("sender")) {
//                sendMessages(connection, cmd.getOptionValue("type"), cmd.getOptionValue("destination"), name, interval, count);
//            } else {
//                receiveMessages(connection, cmd.getOptionValue("type"), cmd.getOptionValue("destination"));
//            }
		} catch (javax.jms.JMSSecurityException ex) {
			System.out.println(String.format("Error: %s", ex.getMessage()));
			System.exit(1);
		}
	}

	private static void sendMessages(StompConnection connection, String type, String destination, String name,
			int interval, WrapInt count) throws Exception {
		while (true) {
			count.v++;

			connection.begin("tx1");
			String message = String.format("[%s://%s] [%s] Message number %s", type, destination, name, count.v);
			connection.send(String.format("/%s/%s", type, destination), message, "tx1", null);
			connection.commit("tx1");

			if (interval > 0) {
				System.out.println(String.format("%s - Sender: sent '%s'", df.format(new Date()), message));
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void receiveMessages(StompConnection connection, String type, String destination) throws Exception {
		connection.subscribe(String.format("/%s/%s", type, destination), Subscribe.AckModeValues.AUTO);

		while (true) {
			try {
				StompFrame message = connection.receive(60000);
				System.out.println(
						String.format("%s - Receiver: received '%s'", df.format(new Date()), message.getBody()));
			} catch (SocketTimeoutException e) {
				// ignore
			}
		}
	}

	private static CommandLine parseAndValidateCommandLineArguments(String[] args) throws ParseException {
		Options options = new Options();

		options.addOption("name", true, "The name of the sender");
		options.addOption("interval", true, "The interval in msec at which messages are generated. Default 1000");
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);

		if (cmd.hasOption("help")) {
			printUsage(options);
		}

		return cmd;
	}

	private static void printUsage(Options options) throws ParseException {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(
				"java -jar stomp-client.jar -url <url> -user <user> -password <password> -mode <sender|receiver> -type <queue|topic> -destination <destination> [-name <name> -interval <interval>]",
				options);
		System.exit(1);
	}

	private static void registerShutdownHook(final WrapInt count, final long ds, final int interval) {
		Thread shutdown = new Thread(new Runnable() {
			long d = ds;
			double rate_theor = interval > 0 ? 1000.0 / interval : 0;

			public void run() {
				long delta = System.currentTimeMillis() - d;
				System.err.print(String.format("\nMessages: %d Seconds: %f Rate: %f/sec vs %f/sec", count.v,
						delta / 1000.0, 1000.0 * count.v / delta, rate_theor));
			}
		});
		Runtime.getRuntime().addShutdownHook(shutdown);
	}

	public static String getUserPassword(String key) {
		GetParameterResult parameterResult = AWSSimpleSystemsManagementClientBuilder.defaultClient()
				.getParameter(new GetParameterRequest().withName(key));
		return parameterResult.getParameter().getValue();
	}
}