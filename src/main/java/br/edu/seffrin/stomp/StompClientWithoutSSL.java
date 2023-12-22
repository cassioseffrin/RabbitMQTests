package br.edu.seffrin.stomp;

import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.StompConnection;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;

 
public class StompClientWithoutSSL {
	private static final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.S");
	public static void main(String[] args) throws Exception {
		
		CommandLine cmd = parseAndValidateCommandLineArguments(args);
		final WrapInt count = new WrapInt();
		final int interval = Integer.parseInt(cmd.getOptionValue("interval", "1000"));
		String name = cmd.getOptionValue("name", UUID.randomUUID().toString());

		try {
			StompConnection connection = new StompConnection();
			
			String url = "mq1.arpasistemas.com.br";
//			String url = "18.228.222.107";
			String user = "arpag";
			String password = "po$p@g2244#$up3rvis%";
			connection.open(url, 61613);			
			connection.connect(user, password);

			
			
//			
//			String url = "192.168.50.19";
//			String user = "arpag";
//			String password = "arpa2010";
//			connection.open(url, 61613);			
//			connection.connect(user, password);
			
			

			//CIELO
//			String queue= "03600477000104:4AC23TJ0J:PAGAMENTO";
// 			String queue= "10513613000186:4AC23TJ0J:PAGAMENTO";
//			String queue= "10513613000186:4AC23TJ0J:PAGAMENTO";
			
			
//			EMULADOR EMULATOR32X1X14X0
//			String queue= "03600477000104:EMULATOR32X1X14X0:PAGAMENTO";
//			String queue= "03600477000104:null:PAGAMENTO";
//			String queue= "10513613000186:EMULATOR32X1X14X0:PAGAMENTO";
 			
 			
			
			//POSITIVO
//			String queue= "01541336000124:4AD74FS9I:PAGAMENTO";
			String queue= "07465257000168:4AD74FS9I:PAGAMENTO";
//			String queue= "03600477000104:4AD74FS9I:PAGAMENTO";
//			String queue= "10513613000186:4AD74FS9I:PAGAMENTO";	
//			String queue= "03600477000104:4AD74FS9I:PAGAMENTO";

 
			

			
//			String queue= "03600477000104:null:PAGAMENTO";
//			String queue= "10513613000186:null:PAGAMENTO";
//			String queue= "10513613000186:EMULATOR32X1X14X0:PAGAMENTO";
			
			
			//P2
//			String queue= "10513613000186:PBG5233679630:PAGAMENTO";
//			String queue= "03600477000104:PBG5233679630:PAGAMENTO";
//			String queue= "03600477000104:PBG5233679630:PAGAMENTO";
			
//			G7
//			String queue = "03600477000104:null:PAGAMENTO";

			
 

			connection.keepAlive();
			
//				(connection, "teste TTL6", queue, interval, count);
//			sendMessages(connection,   "{\"valor\":1060,\"pedido\":\"B621F871|85423\",\"queue\":\"10513613000186:B621F871:RECEBIMENTO\",\"parcelas\":1,\"tipo\":\"DEBITO\", \"operacao\":\"DEBITO\"}", queue, interval, count);	
			sendMessages(connection, "{ \"operacao\": \"DEBITO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"pedido\": \"B621F871|85423\", \"valor\": 101 , \"tipo\": \"CREDITO\" }", queue, interval, count);
//			sendMessages(connection, "{\"valor\":0,\"pedido\":\"\",\"queue\":\"\",\"parcelas\":0,\"operacao\":\"REGISTRO_SUCESSO\",\"nsu\":\"\",\"serial\":\"U1640A6400002\"}", queue, interval, count);		
//			sendMessages(connection, "{\"valor\":0,\"pedido\":\"\",\"queue\":\"\",\"parcelas\":0,\"operacao\":\"BLOQUEAR_DISPOSITIVO\",\"nsu\":\"\",\"serial\":\"U1640A6400002\"}", queue, interval, count);
//			sendMessageEstorno(connection, "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\",  \"pedido\": \"Asdf1\", \"nsu\": \"2023101700002384\"  }", queue, interval, count);
			  
		} catch (javax.jms.JMSSecurityException ex) {
			System.out.println(String.format("Error: %s", ex.getMessage()));
			System.exit(1);
		}
	}

	private static void sendMessages(StompConnection connection,  String message, String queue, int interval, WrapInt count) throws Exception {
//		while (true) {
			count.v++;
			connection.begin("transaction");
			System.out.println(String.format("Successfully connected to %s", queue));
//			connection.send("10513613000186:4AD74FS9I:PAGAMENTO", message, "transaction", null);
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put( "content-length",  new Integer(message.length()).toString() );
			headers.put( "content-type",  "text/plain" );
		
			
			
			// PELO QUE OBSERVEI SO FUNCIONA COM AMQ. STOMP EH STRING E NAO ASSUME
			//https://www.rabbitmq.com/consumers.html#active-consumer
//			headers.put("x-single-active-consumer", "true");
			//https://www.rabbitmq.com/consumer-priority.html
//			headers.put("x-priority", "10");
			
//			String queue="07465257000168:PBG5233679630:PAGAMENTO";
			connection.send(queue, message, "transaction", headers);
//			connection.send("10513613000186:B621F871:RECEBIMENTO", message, "transaction", headers);
			connection.commit("transaction");
			connection.close();
			System.out.println(String.format("%s - enviado para fila %s  - mensagem: '%s'", df.format(new Date()),queue, message));
//			if (interval > 0) {
//				System.out.println(String.format("%s - enviado! '%s'", df.format(new Date()), message));
//				try {
//					Thread.sleep(interval);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}

	
	
	private static void sendMessageEstorno(StompConnection connection,  String message, String queue, int interval, WrapInt count) throws Exception {
//		while (true) {
			count.v++;
			connection.begin("transaction");
//			connection.send("10513613000186:4AD74FS9I:PAGAMENTO", message, "transaction", null);
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put( "content-length",  new Integer(message.length()).toString() );
			headers.put( "content-type",  "text/plain" );
 
			connection.send(queue, message, "transaction", headers);
//			connection.send("10513613000186:B621F871:RECEBIMENTO", message, "transaction", headers);
			connection.commit("transaction");
			connection.close();
			System.out.println(String.format("%s - enviado para fila %s  - mensagem: '%s'", df.format(new Date()),queue, message));
//			if (interval > 0) {
//				System.out.println(String.format("%s - enviado! '%s'", df.format(new Date()), message));
//				try {
//					Thread.sleep(interval);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}
	
	
//	private static void receiveMessages(StompConnection connection, String type, String destination) throws Exception {
//		connection.subscribe(String.format("/%s/%s", type, destination), Subscribe.AckModeValues.AUTO);
//
//		while (true) {
//			try {
//				StompFrame message = connection.receive(60000);
//				System.out.println(
//						String.format("%s - Receiver: received '%s'", df.format(new Date()), message.getBody()));
//			} catch (SocketTimeoutException e) {
//				// ignore
//			}
//		}
//	}

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