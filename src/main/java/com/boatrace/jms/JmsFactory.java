package com.boatrace.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import java.net.URI;

public class JmsFactory {
    private final BrokerService broker;
    private Connection connection;
    private final ConnectionFactory connectionFactory;

    public JmsFactory(int port) throws Exception {
        broker = BrokerFactory.createBroker(new URI("broker:(tcp://localhost:" + port + ")"));
        broker.setBrokerName(String.valueOf(port));
        connectionFactory = new ActiveMQConnectionFactory(
                "tcp://localhost:" + port);
    }

    public Connection openConnection() throws Exception {
        broker.start();
        connection = connectionFactory.createConnection();
        return connection;
    }

    public void startConnection() throws JMSException {
        connection.start();
    }

    public void closeConnection() throws Exception {
        connection.close();
        broker.stop();
    }
}
