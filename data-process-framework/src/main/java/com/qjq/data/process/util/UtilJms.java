package com.qjq.data.process.util;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;

/**
 * 
 * @since 0.1.0
 */
public class UtilJms {
	private static MessageConverter convert = new SimpleMessageConverter();
	
	public static String toText(Object message) {
		Object value;
		try {
			value = message instanceof Message ? convert.fromMessage((Message) message) : message;
		} catch (JMSException e) {
			throw new MessageConversionException(e.getMessage(), e);
		}
		
		if (value instanceof String) return (String) value;
		if (value instanceof byte[]) return new String((byte[]) value, UtilSys.UTF_8);
		if (value instanceof Message) throw new MessageConversionException("无法转换为文本, message=" + message);
		throw new IllegalArgumentException("无法转换为文本, message=" + message);
	}
}
