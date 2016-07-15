package com.qjq.crawler.jms.recvie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.util.ByteSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
public class JmsListenerStrategyExecutor implements InitializingBean, ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(JmsListenerStrategyExecutor.class);

    ApplicationContext context;

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Value("${jms.active:true}")
    private boolean active;

    List<JmsListenerStrategy> stratgeys = new ArrayList<>();
    private List<DefaultMessageListenerContainer> containers = new ArrayList<>();

    public void addStrategy(JmsListenerStrategy stratgey) {
        if (!active)// 禁用 jms
            return;
        if (!stratgeys.contains(stratgey)) {
            registerListener(stratgey);
            stratgeys.add(stratgey);
        }
    }

    private void registerListener(final JmsListenerStrategy stratgey) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setDestinationName(stratgey.getJmsQueue());
        container.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                doMessage(message, stratgey);
            }
        });
        container.setSessionTransacted(true);
        container.setCacheLevel(DefaultMessageListenerContainer.CACHE_CONSUMER);
        container.afterPropertiesSet();
        container.start();
        containers.add(container);
    }

    protected void doMessage(Message message, JmsListenerStrategy stratgey) {
        if (message instanceof TextMessage) {
            try {
                stratgey.onMessage(((TextMessage) message).getText());
            } catch (JMSException e) {
                logger.error("主题：" + stratgey.getJmsQueue() + "jms 处理异常 ", e);
            }
        }
        if (message instanceof ActiveMQMapMessage) {
            try {
                Map<String, Object> map = ((ActiveMQMapMessage) message).getMessage().getProperties();
                ByteSequence bs = ((ActiveMQMapMessage) message).getMessage().getContent();
                String text = new String(bs.getData());
                stratgey.onMessage(text);
            } catch (Exception e) {
                logger.error("主题：" + stratgey.getJmsQueue() + "jms 处理异常 ", e);
            }
        }
        
    }

    /*
     * 注销JMS连接
     */
    public void destroy() {
        for (DefaultMessageListenerContainer l : containers) {
            l.destroy();
            logger.info("MQ连接关闭:" + l.getDestinationName());
        }
    }

    public CachingConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(CachingConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, JmsListenerStrategy> map = context.getBeansOfType(JmsListenerStrategy.class);
        for (Map.Entry<String, JmsListenerStrategy> m : map.entrySet()) {
            addStrategy(m.getValue());
        }
    }

}
