package com.boatrace.jms;

import javax.jms.*;
import java.util.ArrayList;
import java.util.function.Consumer;

public class JmsTopic<E> extends JmsFactory {
    private final Session session;
    public Message message;
    private final Topic topic;
    private final MessageProducer producer;

    public JmsTopic(String queueName, int port) throws Exception {
        super(port);
        Connection connection = super.openConnection();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.topic = session.createTopic(queueName);
        this.producer = session.createProducer(topic);
    }

    public void createOrReplaceMessage(String message) throws JMSException {
        this.message = session.createTextMessage(message);
    }

    public void subscribe(ArrayList<E> subs) throws JMSException {
        for (E sub : subs) {
            MessageConsumer consumer = session.createConsumer(this.topic);
            consumer.setMessageListener((MessageListener) sub);
        }
    }

    public void send() throws JMSException {
        this.producer.send(this.message);
    }
}
