package com.qjq.data.process.zk;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import com.qjq.data.process.util.UtilObj;


/**
 * 基于diamond的配置器<br>
 * @since 0.1.0
 */
public class DiamondPropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {

	private Environment getEnvironment() {
		return UtilObj.getFieldValue(this, "environment");
	}
	private Resource[] getLocations() {
		return UtilObj.getFieldValue(this, "locations");
	}
	
	private PropertySourcesPropertyResolver buildResolver() {
		MutablePropertySources propertySources = new MutablePropertySources();
		Environment environment = getEnvironment();
		if (environment != null) {
			propertySources.addLast(
				new PropertySource<Environment>(ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME, environment) {
					@Override
					public String getProperty(String key) {
						return this.source.getProperty(key);
					}
				}
			);
		}
		try {
			PropertySource<?> localPropertySource = new PropertiesPropertySource(LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME, mergeProperties());
			if (localOverride) {
				propertySources.addFirst(localPropertySource);
			} else {
				propertySources.addLast(localPropertySource);
			}
		} catch (IOException ex) {
			throw new BeanInitializationException("Could not load properties", ex);
		}
		return new PropertySourcesPropertyResolver(propertySources);
	}
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Resource[] locations = getLocations();
		Resource diamondLocation = null;
		int diamondPos = locations.length - 1;
		for (;diamondPos >= 0; diamondPos--) {
			Resource location = locations[diamondPos];
			if ("diamond".equals(location.getFilename())) {
				diamondLocation = location;
				break;
			}
		}
		if (diamondLocation != null) {	//存在diamond，初始化diamond client
			{	//先去掉locations中的diamond
				Resource[] locationsExcludeDiamond = new Resource[locations.length - 1];
				int pos = 0;
				for (Resource location : locations) {
					if (location != diamondLocation) locationsExcludeDiamond[pos++] = location;
				}
				setLocations(locationsExcludeDiamond);
			}
			PropertySourcesPropertyResolver resolver = buildResolver();
			
			Integer appId = resolver.getProperty("server.appId", Integer.class);
			if (appId == null || appId < 1) throw new IllegalArgumentException("配置错误 server.appId=" + appId);

			DiamondClient client = new DiamondClient();
			{
				client.setConnectString(resolver.getProperty("diamond.zk.connectString"));
				client.setAuthInfo(resolver.getProperty("diamond.zk.authInfo"));
				client.setAppId(appId);
				
				Integer tmp = resolver.getProperty("diamond.zk.connectionTimeout", Integer.class);
				if (tmp != null && tmp >= 0) client.setConnectionTimeout(tmp);
				tmp = resolver.getProperty("diamond.zk.sessionTimeout", Integer.class);
				if (tmp != null && tmp >= 0) client.setSessionTimeout(tmp);
			}
			client.init();
			beanFactory.registerSingleton("DiamondClientZk", client);
			if (beanFactory instanceof DefaultListableBeanFactory) ((DefaultListableBeanFactory) beanFactory).registerDisposableBean("DiamondClientZk", client);
			
			//重写diamond resource
			locations[diamondPos] = new ByteArrayResource(client.getAppPropertiesContent(appId).getBytes(), "diamond: " + client.getConnectString());
			setLocations(locations);
		}
		
		super.postProcessBeanFactory(beanFactory);
	}
}
