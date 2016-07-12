package com.qjq.crawler.dao.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * 带有spring容器环境的测试类<br>
 * bean注入，直接使用@Autowired注入
 * <ul>
 * 	<li>spring配置加载：@ContextConfiguration({"classpath*:spring/applicationContext*.xml"})</li>
 * </ul>
 */
@ContextConfiguration("classpath:config/spring.xml")
public abstract class AbstractTest extends AbstractJUnit4SpringContextTests {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 根据类型获取bean
	 * @param clazz	bean类型
	 * @return bean
	 * @see ApplicationContext#getBean(Class)
	 */
	public <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}
	
}

