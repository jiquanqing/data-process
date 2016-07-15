package com.qjq.crawler.jms.send;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderImpl implements MessageSender {

    @Autowired
    JmsTemplate jmsTemplate;

    public void hander(final String text) {
        jmsTemplate.send(new MessageCreator() {

            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage();
                message.setText(text);
                return message;
            }
        });
    }
}
