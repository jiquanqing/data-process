package com.qjq.data.process.util;

/**
 * @since 0.1.0
 * @version 创建时间：2014年3月5日 下午2:30:03
 */
public interface Serializable {
	byte[] serialize();

	void unserialize(byte[] ss);
}
