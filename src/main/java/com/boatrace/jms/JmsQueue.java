package com.boatrace.jms;

import com.boatrace.core.Boat;

import javax.jms.*;

public class JmsQueue<E> extends JmsFactory {
    private final Session session;
    public Message message;
    private final Queue queue;
    private MessageConsumer consumer;

    public JmsQueue(String queueName) throws Exception {
        super();
        Connection connection = super.openConnection();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.queue = session.createQueue(queueName);
        MessageProducer producer = session.createProducer(queue);
    }

    public Message createMessage(String message) throws JMSException {
        this.message = session.createTextMessage(message);
        return this.message;
    }

    public MessageConsumer subscribe(E subscriber) throws JMSException {
        this.consumer = session.createConsumer(this.queue);
        consumer.setMessageListener((MessageListener) subscriber);
        return this.consumer;
    }

    public MessageConsumer getConsumer() {
        return this.consumer;
    }
}
