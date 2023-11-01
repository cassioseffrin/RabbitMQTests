package br.edu.seffrin.stomp;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.jmx.BrokerViewMBean;

import java.util.Map;
import java.util.Set;

public class ActiveMQConnectionCloser {
    public static void main(String[] args) throws Exception {
        // Connect to the ActiveMQ server using JMX
        MBeanServerConnection mbeanConnection = connectToJMX();

        // Specify the client ID you want to close
        String clientIdToClose = "4AD74FS9I";  // Example client ID

        // Get the list of ConnectionViewMBeans
        ObjectName brokerViewMBeanName = new ObjectName("org.apache.activemq:type=Broker,brokerName=b-83aea7ca-f7bc-4d1c-9a09-976c24906430-1.mq.sa-east-1.amazonaws.com");
        BrokerViewMBean brokerViewMBean = MBeanServerInvocationHandler.newProxyInstance(
                mbeanConnection, brokerViewMBeanName, BrokerViewMBean.class, false);

 
        Map<String, String> connections = brokerViewMBean.getTransportConnectors();

       
//        for (ObjectName connection : connections) {
//            ConnectionViewMBean connectionMBean = MBeanServerInvocationHandler.newProxyInstance(
//                    mbeanConnection, connection, ConnectionViewMBean.class, false);
//
//            if (connectionMBean.getClientId().equals(clientIdToClose)) {
//                // Close the connection
//                connectionMBean.close();
//                System.out.println("Closed connection with client ID: " + clientIdToClose);
//                break;  // Exit the loop after closing the connection
//            }
//        }

        // Disconnect from JMX
//        disconnectFromJMX();
    }

    private static MBeanServerConnection connectToJMX() throws Exception {
    	
    	
    	 JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://b-83aea7ca-f7bc-4d1c-9a09-976c24906430-1.mq.sa-east-1.amazonaws.com:1099/jmxrmi");
    	    JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxServiceURL);;
    	    MBeanServerConnection connection = jmxConnector.getMBeanServerConnection();
//    	    BrokerViewMBean mBroker = ((BrokerViewMBean)MBeanServerInvocationHandler.newProxyInstance(connection, activeMq, BrokerViewMBean.class, true));
    	 
//    	    ObjectName activeMq = new ObjectName("org.apache.activemq:BrokerName=localhost,Type=Broker");
//    	 
//    	    for (ObjectName queue : mBroker.getQueues())
//    	    {
//    	        QueueViewMBean queueBean = (QueueViewMBean) MBeanServerInvocationHandler.newProxyInstance(connection, queue, QueueViewMBean.class, true);
//    	        mQueues.put(queueBean.getName(), queueBean);
//    	    }
//    	     
//    	    //clean up the mess.......
//    	    jmxConnector.close();
    	    
    	    
    	    
        // Implement your JMX connection logic here
        // You'll need to specify the JMX server URL and credentials to connect to the ActiveMQ server.
        // See javax.management.remote.JMXConnectorFactory for more details.
        // Example:
//         JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://b-83aea7ca-f7bc-4d1c-9a09-976c24906430-1.mq.sa-east-1.amazonaws.com:61617/jmxrmi");
//         JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
         return connection;
    	
//		String failOverBrokerURL = "ssl://b-83aea7ca-f7bc-4d1c-9a09-976c24906430-1.mq.sa-east-1.amazonaws.com:61617";
//		String username = "arpag";
//		String password = "@rpa@pps2022";
//		ConnectionFactory factory = new ActiveMQConnectionFactory(failOverBrokerURL);
// 
//			Connection connection = factory.createConnection(username, password);
//			connection.start();		
//        throw new UnsupportedOperationException("Implement JMX connection logic.");
    }

    private static void disconnectFromJMX() {
        // Implement your JMX disconnection logic here
        // Close the JMX connection or release any resources used for the connection.
        // See javax.management.JMXConnector for more details.
        throw new UnsupportedOperationException("Implement JMX disconnection logic.");
    }
}