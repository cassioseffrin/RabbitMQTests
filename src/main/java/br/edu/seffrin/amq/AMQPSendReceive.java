package br.edu.seffrin.amq;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

//https://www.rabbitmq.com/ssl.html#java-client-connecting-with-peer-verification
@SuppressWarnings("ALL")
public class AMQPSendReceive {
    // CIELO
    //	String QUEUE = "03600477000104:4AC23TJ0J:PAGAMENTO";
//    	String QUEUE = "07465257000168:4AC23TJ0J:PAGAMENTO";
    //	String QUEUE = "10513613000186:4AC23TJ0J:PAGAMENTO";
//    String QUEUE = "07465257000168:EMULATOR32X1X14X0:PAGAMENTO";

    //	EMULADOR EMULATOR32X1X14X0
//    	String QUEUE = "03600477000104:EMULATOR32X1X14X0:PAGAMENTO";
//    	String QUEUE = "07465257000168:EMULATOR32X1X14X0:PAGAMENTO";
    //  String QUEUE = "10513613000186:c6092cc4b29769fe:PAGAMENTO";
    //	String QUEUE = "10513613000186:EMULATOR32X1X14X0:PAGAMENTO";
    //	String QUEUE = "10513613000186:EMULATOR32X1X14X0:PAGAMENTO";
//    	String QUEUE = "07465257000168:EMULATOR32X1X14X0:PAGAMENTO";
    //	String QUEUE = "01058552000113:EMULATOR32X1X14X0:PAGAMENTO";
    //	String QUEUE = "10513613000186:778f33444d53c9c6:PAGAMENTO";
    //	String QUEUE = "10513613000186:778f33444d53c9c6:PAGAMENTO";

    // VERO
    //	String QUEUE = "01541336000124:4AD74FS9I:PAGAMENTO";
//      String QUEUE = "07465257000168:4AD74FS9I:PAGAMENTO";
    //	String QUEUE = "03600477000104:4AD74FS9I:PAGAMENTO";
    //	String QUEUE = "10513613000186:4AD74FS9I:PAGAMENTO";
    //	String QUEUE = "03600477000104:4AD74FS9I:PAGAMENTO";
//    	String QUEUE = "07465257000168:4AD74FS9I:PAGAMENTO";
    //  String QUEUE = "10513613000186:PBG5233679630:PAGAMENTO";
    //	String QUEUE = "03600477000104:PBG5233679630:PAGAMENTO";
    //	String QUEUE = "03600477000104:PBG5233679630:PAGAMENTO";
//    	String QUEUE = "07465257000168:PBG5233679630:PAGAMENTO";
//    	String QUEUE = "19496110000114:4AD74FS9I:PAGAMENTO";

    //	G7
    //	String QUEUE = "03600477000104:null:PAGAMENTO";
    //	String QUEUE = "10513613000186:0b40e2c6f8c44f50:PAGAMENTO";

    //	STONE
//    	String QUEUE = "78827987000100:5ecc0696d5b2bd15:PAGAMENTO";
//    	String QUEUE = "07465257000168:5ecc0696d5b2bd15:PAGAMENTO";
    //	String QUEUE = "10513613000186:0fa3d0bc7dbf2678:PAGAMENTO";
    //	String QUEUE = "24779520000102:5ecc0696d5b2bd15:PAGAMENTO";
//    String QUEUE = "19496110000114:5ecc0696d5b2bd15:PAGAMENTO";

    // GETNET
//     String QUEUE = "10513613000186:PBF923CE70038:PAGAMENTO";
//     String QUEUE = "07465257000168:PBF923CE70038:PAGAMENTO";
//     String QUEUE = "19496110000114:PBF923CE70038:PAGAMENTO";
//
//    PAGSEGURO
//    String QUEUE = "07465257000168:PBA1238674580:PAGAMENTO";
//    String QUEUE = "10513613000186:PBA1238674580:PAGAMENTO";
//    String QUEUE = "19496110000114:PBA1238674580:PAGAMENTO";//    String QUEUE = "10513613000186:PBA1238674580:PAGAMENTO";
//    String QUEUE = "07465257000168:PBA1238674580:PAGAMENTO";//    String QUEUE = "10513613000186:PBA1238674580:PAGAMENTO";
//    String QUEUE = "05061462000132:PBA1238674580:PAGAMENTO";
 //   String QUEUE = "08806797000120:EMULATOR32X1X14X0:PAGAMENTO";

    //REDE
//        String QUEUE = "07465257000168:f0f70be3ccda9214:PAGAMENTO";
//        String QUEUE = "07465257000168:0fe09c4d5817ef78:PAGAMENTO";

//    FISERV
//    release
    String QUEUE = "07465257000168:754c977e0b28a046:PAGAMENTO";
//    String QUEUE = "04962772000165:754c977e0b28a046:PAGAMENTO";
//    debug
//    String QUEUE = "07465257000168:ecc9ce625281a636:PAGAMENTO";
//    String QUEUE = "04962772000165:ecc9ce625281a636:PAGAMENTO";

    private final static String EXCHANGE_NAME = "03600477000104:EMULATOR32X1X14X0:PAGAMENTO";
    private final static String HOST_NAME = "mq2.arpasistemas.com.br";
    private final static String USER_NAME = "arpag";
    private final static String PASSWORD = "po$p@g2244#$up3rvis%";
    private final static Integer PORT = 5672;
//    private final static Integer PORT = 80;
//    private final static Integer PORT = 443;

    protected Connection connect() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST_NAME);
        factory.setUsername(USER_NAME);
        factory.setPassword(PASSWORD);
        factory.setPort(PORT);
        //factory.setRequestedFrameMax(67108864);
        Connection connection = null;
        try {
//            factory.useSslProtocol();
            connection = factory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public Thread send(final String type, final String msg, final int count) {
        Thread t = new Thread(new Runnable() {
            private Connection connection = null;
            private Channel channel = null;

            public void run() {
                try {
                    connection = connect();
                    channel = connection.createChannel();
                    channel.queueDeclare(QUEUE, true, false, false, null);
//                    String message = "{\"modalidade\": \"CREDITO\",  \"operacao\": \"CREDITO\", \"queue\":\"10513613000186000186:XXXXXXXX:RECEBIMENTO\", \"pedido\": \"CONTROL1234\", \"valor\": 1080, \"orderId\": \"23423234dsfasdf\",  \"tipo\": \"CREDITO\" }";
//					String message = "{\"modalidade\": \"DEBITO\",  \"operacao\": \"DEBITO\", \"queue\":\"07465257000168:B621F871:RECEBIMENTO\", \"pedido\": \"CONTROL1234\", \"valor\": 12100, \"orderId\": \"23423234dsfasdf\",    \"tipo\": \"DEBITO\" }";
//					String message = "{ \"modalidade\": \"PIX\", \"operacao\": \"PIX\", \"queue\":\"    07465257000168:B621F871:RECEBIMENTO\", \"pedido\": \"B621F871|85423\", \"valor\": 800 , \"tipo\": \"PIX\" }";
                    String message = "{\"valor\":0,\"pedido\":\"\",\"queue\":\"\",\"parcelas\":0,\"operacao\":\"REGISTRO_SUCESSO\",\"nsu\":\"\",\"serial\":\"U1640A6400002\"}";
//					String message = "{\"valor\":0,\"pedido\":\"\",\"queue\":\"\",\"parcelas\":0,\"operacao\":\"BLOQUEAR_DISPOSITIVO\",\"nsu\":\"\",\"serial\":\"U1640A6400002\"}";
//					String message = "{ \"modalidade\": \"CREDITO\", \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 119, \"pedido\": \"Asdf1\", \"nsu\": \"46331080665995\"  }" ;

                    //ESTORNO CIELO
//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 103, \"pedido\": \"sdfsdf|85423\", \"nsu\": \"132453\", \"autorizacao\":\"591251\", \"orderId\": \"sdfsdf|85423\"}" ;
//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 103, \"pedido\": \"sdfsdf|85423\", \"nsu\": \"7af65211-ca15-4944-9519-f49831a18735\", \"autorizacao\":\"747ad21d-c4dc-4bcc-8c72-6654e10e9655\", \"orderId\": \"d7600ee5-623e-47d1-b218-ab021eb6e42b\"}" ;
//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 104, \"pedido\": \"sdfsdf|85423\", \"nsu\": \"132458\", \"autorizacao\":\"969674\", \"orderId\": \"2895f267-1602-417d-b5cb-548467d07757\"}" ;
//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 105, \"pedido\": \"CONTROL123\", \"nsu\": \"132466\", \"autorizacao\":\"082365\", \"orderId\": \"05a424f9-1b1b-4878-9c4f-fef57975a52b\"}" ;
//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 106, \"pedido\": \"CONTROL1234\", \"nsu\": \"132471\", \"autorizacao\":\"122449\", \"orderId\": \"cea62249-5a8e-4fe4-92da-74bb5e5e3158\"}" ;
//					String message = "{ \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186000186:XXXXXXXX:RECEBIMENTO\", \"valor\": 500, \"pedido\": \"asdf2369\", \"nsu\": \"132477\", \"autorizacao\":\"218240\", \"orderId\": \"fe98d759-8915-4a73-b644-489261c91374\"}" ;

                    // ESTORNO PAGSEGURO
//                     String message = "{\"valor\":988,\"pedido\":\"6677436E|1712914842\",\"queue\":\"07465257000168:6677436E:RECEBIMENTO\",\"parcelas\":0,\"operacao\":\"ESTORNO\",\"nsu\":\"041244311548\",\"serial\":\"\",\"autorizacao\":\"111230\",\"orderId\":\"{\\\"transactionCode\\\":\\\"9C90FDCA4D424ABC9727E393432ABD35\\\",\\\"transactionId\\\":\\\"F108A44A3C\\\"}\"}";
//                    String message = "{\"valor\":688,\"pedido\":\"6677436E|1712914842\",\"queue\":\"07465257000168:6677436E:RECEBIMENTO\",\"parcelas\":0,\"operacao\":\"ESTORNO\",\"nsu\":\"041244311548\",\"serial\":\"\",\"autorizacao\":\"111230\",\"orderId\":\"{\\\"transactionCode\\\":\\\"600D11B5C5894086A2FCE67AF73AEB0C\\\",\\\"transactionId\\\":\\\"4523A4D24B\\\"}\"}";
//                    String message = "{\"valor\":688,\"pedido\":\"6677436E|1712914842\",\"queue\":\"07465257000168:6677436E:RECEBIMENTO\",\"parcelas\":0,\"operacao\":\"ESTORNO\",\"nsu\":\"041244311548\",\"serial\":\"\",\"autorizacao\":\"111230\",\"orderId\":\"{\\\"transactionCode\\\":\\\"600D11B5C5894086A2FCE67AF73AEB0C\\\"}\"}";
                 //   String message = "{\"modalidade\": \"CREDITO\", \"valor\":688,\"pedido\":\"6677436E|1712914842\",\"queue\":\"07465257000168:6677436E:RECEBIMENTO\",\"parcelas\":0,\"operacao\":\"ESTORNO\",\"nsu\":\"041244311548\",\"serial\":\"\",\"autorizacao\":\"111230\"}";
//                    String message = "{\"valor\":1010,\"pedido\":\"285C7091|1719479744\",\"queue\":\"07465257000168:285C7091:RECEBIMENTO\",\"parcelas\":0,\"operacao\":\"ESTORNO\",\"nsu\":\"062769911829\",\"serial\":\"\",\"autorizacao\":\"196458\",\"orderId\":\"\",\"modalidade\":\"CREDITO\"}";
//                    String message = "{\"modalidade\": \"CREDITO\", \"operacao\": \"ESTORNO\", \"queue\":\"10513613000186:B621F871:RECEBIMENTO\", \"valor\": 500, \"pedido\": \"asdf2369\", \"nsu\": \"132477\", \"autorizacao\":\"218240\", \"orderId\": \'{\"transactionCode\":\"7E0E3008311642B3A70274192AAC878E\",\"transactionId\":\"9642301112\"}\"  }" ;

                    // ESTORNO GETNET
//                     String message = "{\"valor\":1200,\"pedido\":\"6677436E|1712914842\",\"queue\":\"07465257000168:6677436E:RECEBIMENTO\",\"parcelas\":0,\"operacao\":\"ESTORNO\",\"nsu\":\"041244311548\",\"serial\":\"\",\"autorizacao\":\"111230\",\"orderId\":\"000000122\"}";
//                     String message = "{\"valor\":1200,\"pedido\":\"6677436E|1712914842\",\"queue\":\"07465257000168:6677436E:RECEBIMENTO\",\"parcelas\":0,\"operacao\":\"ESTORNO\",\"nsu\":\"041244311548\",\"serial\":\"\",\"autorizacao\":\"111230\",\"orderId\":\"000000176\"}";
//                     String message = "{\"modalidade\": \"PIX\", \"valor\":1200,\"pedido\":\"6677436E|1712914842\",\"queue\":\"07465257000168:6677436E:RECEBIMENTO\",\"parcelas\":0,\"operacao\":\"ESTORNO\",\"nsu\":\"041244311548\",\"serial\":\"\",\"autorizacao\":\"111230\",\"orderId\":\"000000179\"}";


                    // ESTORNO REDE
//                    String message = "{\"valor\":1200,\"pedido\":\"6677436E|1712914842\",\"queue\":\"07465257000168:6677436E:RECEBIMENTO\",\"parcelas\":0,\"operacao\":\"ESTORNO\",\"nsu\":\"041244311548\",\"serial\":\"\",\"autorizacao\":\"111230\",\"orderId\":\"000000179\"}";


                    // ESTORNO FISERV
//                    String message = "{\"valor\":800,\"pedido\":\"6677436E|1712914842\",\"queue\":\"07465257000168:6677436E:RECEBIMENTO\",\"parcelas\":0,\"operacao\":\"ESTORNO\",\"nsu\":\"041244311548\",\"serial\":\"\",\"autorizacao\":\"111230\",\"orderId\":\"000000179\"}";

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
                } finally {
                    try {
                        channel.close();
                        connection.close();
                    } catch (Exception e) {

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
                    System.out.println(name + " Thread exists!");
                }
            }
        });

        t.start();
        return t;
    }

    public static void main(String a[]) {
        try {
            AMQPSendReceive q = new AMQPSendReceive();

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
