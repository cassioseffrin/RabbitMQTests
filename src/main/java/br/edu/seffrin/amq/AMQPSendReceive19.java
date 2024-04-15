package br.edu.seffrin.amq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;

//https://www.rabbitmq.com/ssl.html#java-client-connecting-with-peer-verification
@SuppressWarnings("ALL")
public class AMQPSendReceive19 {

    // CIELO
//	String QUEUE= "03600477000104:4AC23TJ0J:PAGAMENTO";
//	String QUEUE= "07465x257000168:4AC23TJ0J:PAGAMENTO";
//	String QUEUE= "10513613000186:4AC23TJ0J:PAGAMENTO";

    //PAGSEGURO
//String QUEUE = "10513613000186:PBA1238674580:PAGAMENTO";
//
//	EMULADOR EMULATOR32X1X14X0
//	String QUEUE= "03600477000104:EMULATOR32X1X14X0:PAGAMENTO";
//	String QUEUE= "10513613000186:EMULATOR32X1X14X0:PAGAMENTO";
//	String QUEUE= "10513613000186:EMULATOR32X1X14X0:PAGAMENTO";
//	String QUEUE= "07465257000168:EMULATOR32X1X14X0:PAGAMENTO";
//	String QUEUE= "01058552000113:EMULATOR32X1X14X0:PAGAMENTO";
//	String QUEUE= "10513613000186:778f33444d53c9c6:PAGAMENTO";
//	String QUEUE = "10513613000186:778f33444d53c9c6:PAGAMENTO";
    // VERO
//	String QUEUE= "01541336000124:4AD74FS9I:PAGAMENTO";
//	String QUEUE= "07465257000168:4AD74FS9I:PAGAMENTO";
//	String QUEUE= "03600477000104:4AD74FS9I:PAGAMENTO";
//	String QUEUE= "10513613000186:4AD74FS9I:PAGAMENTO";	
//	String QUEUE= "03600477000104:4AD74FS9I:PAGAMENTO";
//	String QUEUE="10513613000186:4AD74FS9I:PAGAMENTO";
//    String QUEUE = "10513613000186:PBG5233679630:PAGAMENTO";
//	String QUEUE= "03600477000104:PBG5233679630:PAGAMENTO";
//	String QUEUE= "03600477000104:PBG5233679630:PAGAMENTO";
//	String QUEUE = "07465257000168:PBG5233679630:PAGAMENTO";


//	G7
//	String QUEUE = "03600477000104:null:PAGAMENTO";
//	String QUEUE= "10513613000186:0b40e2c6f8c44f50:PAGAMENTO";


//	STONE
//	String QUEUE= "07465257000168:5ecc0696d5b2bd15:PAGAMENTO";
//	String QUEUE= "10513613000186:5ecc0696d5b2bd15:PAGAMENTO";

String QUEUE = "10513613000186:0fa3d0bc7dbf2678:PAGAMENTO";
    private final static String EXCHANGE_NAME = "03600477000104:EMULATOR32X1X14X0:PAGAMENTO";
//    private final static String HOST_NAME = "mq1.arpasistemas.com.br";
    private final static String HOST_NAME = "192.168.50.19";


    private final static String USER_NAME = "arpag";
    private final static String PASSWORD = "po$p@g2244#$up3rvis%";
    private final static Integer PORT = 5672; //HTTP
//    private final static Integer PORT = 5671; //SSL

    protected Connection connect() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST_NAME);
        factory.setUsername(USER_NAME);
        factory.setPassword(PASSWORD);
        factory.setPort(PORT);

        Connection connection = null;

        try {
//            factory.useSslProtocol();
            connection = factory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    // exchange type: fan, direct, topic
    // fan - no routing key, exchange to all queues
    // direct - simple routing key, exchange to binding queues.
    // topic - routing key with * and #, complex binding queues
    public Thread send(final String type, final String msg, final int count) {
        Thread t = new Thread(new Runnable() {

            private Connection connection = null;
            private Channel channel = null;

            public void run() {
                try {
                    connection = connect();
                    channel = connection.createChannel();
                    // with exchange, can use binding key
//					channel.exchangeDeclare(EXCHANGE_NAME, type);
//					o single-active-consumer so funciona se for nas 2 pontas PDV - ARPAG, porem o PDV stromp nao consegue mandar o single-active-consumer, portanto os headers ficaram como null nas 2 pontas devido a limitacao do stomp
		            HashMap<String, Object> headers = new HashMap<String, Object>();
		            headers.put("content-type", "text/plain");
		            headers.put("x-single-active-consumer", true);
					channel.queueDeclare(QUEUE, true, false, false, headers);
                    channel.queueDeclare(QUEUE, true, false, false, headers);
//					String message = "{ \"operacao\": \"CREDITO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"pedido\": \"CONTROL1234\", \"valor\": 500, \"orderId\": \"23423234dsfasdf\",    \"tipo\": \"CREDITO\" }";
//					String message = "{ \"operacao\": \"PIX\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"pedido\": \"B621F871|85423\", \"valor\": 50 , \"tipo\": \"CREDITO\" }";
                    String message = "{\"valor\":0,\"pedido\":\"\",\"queue\":\"\",\"parcelas\":0,\"operacao\":\"REGISTRO_SUCESSO\",\"nsu\":\"\",\"serial\":\"U1640A6400002\"}";
//					String message = "{\"valor\":0,\"pedido\":\"\",\"queue\":\"\",\"parcelas\":0,\"operacao\":\"BLOQUEAR_DISPOSITIVO\",\"nsu\":\"\",\"serial\":\"U1640A6400002\"}";	
//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 119, \"pedido\": \"Asdf1\", \"nsu\": \"46331080665995\"  }" ;
                    //ESTORNO CIELO
//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 103, \"pedido\": \"sdfsdf|85423\", \"nsu\": \"132453\", \"autorizacao\":\"591251\", \"orderId\": \"sdfsdf|85423\"}" ;					
//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 103, \"pedido\": \"sdfsdf|85423\", \"nsu\": \"7af65211-ca15-4944-9519-f49831a18735\", \"autorizacao\":\"747ad21d-c4dc-4bcc-8c72-6654e10e9655\", \"orderId\": \"d7600ee5-623e-47d1-b218-ab021eb6e42b\"}" ;					
//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 104, \"pedido\": \"sdfsdf|85423\", \"nsu\": \"132458\", \"autorizacao\":\"969674\", \"orderId\": \"2895f267-1602-417d-b5cb-548467d07757\"}" ;					
//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 105, \"pedido\": \"CONTROL123\", \"nsu\": \"132466\", \"autorizacao\":\"082365\", \"orderId\": \"05a424f9-1b1b-4878-9c4f-fef57975a52b\"}" ;					
//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 106, \"pedido\": \"CONTROL1234\", \"nsu\": \"132471\", \"autorizacao\":\"122449\", \"orderId\": \"cea62249-5a8e-4fe4-92da-74bb5e5e3158\"}" ;					
//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 500, \"pedido\": \"asdf2369\", \"nsu\": \"132477\", \"autorizacao\":\"218240\", \"orderId\": \"1fd57de6-efd0-435a-b393-749d837a347f\"}" ;

//					exemplo correto 
//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 107, \"pedido\": \"asdf2369\", \"nsu\": \"132486\", \"autorizacao\":\"911247\", \"orderId\": \"b7e39eb2-72e6-4261-bdf5-e0b836c24d4d\"}" ;		

//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 114, \"pedido\": \"CONTROL1234\", \"nsu\": \"132650\", \"autorizacao\":\"249522\", \"orderId\": \"19f857dc-4157-4258-8a4c-b6fbd717d07c\"}" ;		


//					NAO EXECUTADAS
//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 109, \"pedido\": \"asdf2369\", \"nsu\": \"132488\", \"autorizacao\":\"023305\", \"orderId\": \"b3cc5a8e-bcee-4b69-9c8b-74c507207e1a\"}" ;					
//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 110, \"pedido\": \"asdf2369\", \"nsu\": \"132490\", \"autorizacao\":\"033594\", \"orderId\": \"17a7d25e-6f11-4cab-acc3-6f8671ac497a\"}" ;					

                    channel.basicPublish("", QUEUE, new AMQP.BasicProperties.Builder().contentType("text/plain")
                            .deliveryMode(2).priority(1).userId("arpag").build(), message.getBytes("UTF-8"));
                    // with exchange
                    // MessageProperties.PERSISTENT_TEXT_PLAIN tell RabbitMQ to save message on disk
//						channel.basicPublish(EXCHANGE_NAME, routingKey[i % routingKey.length],
//								MessageProperties.PERSISTENT_TEXT_PLAIN, newMsg.getBytes());
//						 System.out.println("Sent " + routingKey[i % routingKey.length] + ": '" + newMsg + "'");
                    System.out.println("Enviada para fila: " + QUEUE + " Enviada !'" + message + "'");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        channel.close();
                        connection.close();
                    } catch (Exception e) {
                            e.printStackTrace();
                    }
                    System.out.println("thread existente!");
                }
            }
        });

        t.start();
        return t;
    }

    public Thread receive(final String type, final String name, final String bindingKey) {
        Thread t = new Thread(new Runnable() {
            private Connection connection = null;
            private Channel channel = null;
            private String queueName = null;

            public void run() {
                try {
                    connection = connect();
                    channel = connection.createChannel();

                    // without exchange
                    // channel.queueDeclare(QUEUE_NAME, false, false, false, null);

                    // with exchange, create QUEUE for each subscriber and bind to exchange
                    // direct supports routing key, fanout doesn't support
                    channel.exchangeDeclare(EXCHANGE_NAME, type);
                    // String queueName = channel.queueDeclare().getQueue();

                    // When RabbitMQ quits or crashes it will forget the queues and messages
                    // unless set durable = true
                    boolean durable = true;
                    queueName = channel.queueDeclare("", durable, false, false, null).getQueue();
                    channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);

                    Consumer consumer = new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope,
                                                   AMQP.BasicProperties properties, byte[] body) throws IOException {
                            String message = new String(body, "UTF-8");
                            System.out.println(name + " Received " + envelope.getRoutingKey() + ": '" + message + "'");
                        }
                    };

                    // auto acknowledgment is true
                    // if false, will result in messages_unacknowledged
                    // `rabbitmqctl list_queues name messages_ready messages_unacknowledged`
                    channel.basicConsume(queueName, true, consumer);

                    while (!Thread.currentThread().isInterrupted()) {
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                } finally {
                    try {
                        channel.queueDelete(queueName);
                        channel.close();
                        connection.close();
                    } catch (Exception e) {

                    }
                    System.out.println(name + " thread exists!");
                }
            }
        });

        t.start();
        return t;
    }

    public static void main(String a[]) {
        try {
            AMQPSendReceive19 q = new AMQPSendReceive19();

//			Thread publisher = q.send("direct", "test message", 100);		
//			Thread subscriber1 = q.receive("direct", "subscriber1", "error");
//			Thread subscriber2 = q.receive("direct", "subscriber2", "debug");
//			Thread subscriber3 = q.receive("direct", "subscriber3", "info");

            Thread publisher = q.send("topic", "test message", 10);
//			Thread subscriber1 = q.receive("topic", "subscriber1", "error.*");
//			Thread subscriber2 = q.receive("topic", "subscriber2", "*.app1");
//			Thread subscriber3 = q.receive("topic", "subscriber3", "info.app2");

            System.in.read();
            publisher.interrupt();
//			subscriber1.interrupt();
//			subscriber2.interrupt();
//			subscriber3.interrupt();

            publisher.join();
//			subscriber1.join();
//			subscriber2.join();
//			subscriber3.join();
            System.out.println("all thread joined!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
