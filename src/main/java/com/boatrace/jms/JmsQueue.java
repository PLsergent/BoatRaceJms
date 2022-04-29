package com.boatrace.jms;

import javax.jms.*;

public class JmsQueue<E> extends JmsFactory {
    private final Session session;
    public Message message;
    private final Queue queue;
    private final MessageProducer producer;

    public JmsQueue(String queueName, int port) throws Exception {
        super(port);
        Connection connection = super.openConnection();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.queue = session.createQueue(queueName);
        this.producer = session.createProducer(queue);
    }

    public void createOrReplaceMessage(String message) throws JMSException {
        this.message = session.createTextMessage(message);
    }

    public void subscribe(E subscriber) throws JMSException {
        MessageConsumer consumer = session.createConsumer(this.queue);
        consumer.setMessageListener((MessageListener) subscriber);
    }

    public void send() throws JMSException {
        this.producer.send(this.message);
    }
}
