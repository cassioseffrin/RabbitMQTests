package br.edu.seffrin.amq;

import com.rabbitmq.client.*;

public class SimpleAMQPSender {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("mq1.arpasistemas.com.br");
        factory.setUsername("arpag");
        factory.setPassword("po$p@g2244#$up3rvis%");
        factory.setVirtualHost("/");
        factory.setPort(5672);

        try {
            System.out.println("🔐 Connecting to " + factory.getHost() + ":" + factory.getPort());
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                System.out.println("✅ Connected successfully!");

                String exchange = "exchangeRegistrarSmartPosHomologacao";
//                String exchange = "exchangeRegistrarSmartPos";
                channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT, true);


//                String message = " { \"identificacao\": \"hzhzg\", \"razao\": \"bxh,vcz\", \"dispositivo\": \"771092205a3ef7f9\", \"cnpj\": \"07056528000121\", \"versao\": \"99.25.0.1-0\", \"adquirente\": \"STONE\", \"cnpjAdquirente\": \"16.501.555/0008-23\", \"modeloPOS\": \"positivo-l400\" }  ";
                String message = "{\"identificacao\":\"ASDFASDF\",\"razao\":\"ASDFASDF\",\"dispositivo\":\"PBF9248T72895\",\"cnpj\":\"WXSJSHWM000166\",\"versao\":\"99.26.0.0\",\"adquirente\":\"SIPAG\",\"cnpjAdquirente\":\"02.038.232/0001-64\",\"modeloPOS\":\"sunmi-p2-b\"}";


                channel.basicPublish(exchange, "",
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes("UTF-8"));

                System.out.println("📨 Message sent successfully!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
