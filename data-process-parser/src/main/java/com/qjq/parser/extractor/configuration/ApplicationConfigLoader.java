package com.qjq.parser.extractor.configuration;

/*
 * function: load configuration
 * author: qingjiquan
 * date: 2014-10-27
 */
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qjq.parser.extractor.domain.ConfigEntry;
import com.qjq.parser.extractor.exception.ConfWrappedException;
import com.qjq.parser.extractor.exception.WrappedException;
import com.qjq.parser.extractor.logger.InitSysLogEvent;

public class ApplicationConfigLoader {
    private static Logger LOG = LoggerFactory.getLogger(ApplicationConfigLoader.class);

    private static ApplicationConfigLoader singleLoader = null;
    private String baseDir = "conf";
    private boolean hasEnter = false;

    private Map<String, ConfigEntry> confClassMap = new LinkedHashMap<String, ConfigEntry>();
    private Map<String, Object> confMap = new HashMap<String, Object>();

    private ApplicationConfigLoader() {
    }

    synchronized public void setIngoreConf(boolean b) {
        hasEnter = b;
    }

    synchronized public void loadConfigurations() {
        if (!hasEnter) {
            LOG.info(InitSysLogEvent.loadConfigMessage(baseDir));
            hasEnter = true;
            load();
            ApplicationConfig config = parser();
            ApplicationConfig.addConfigIntoApplication(config);
        } else {
            LOG.info("System already ingore load config");
        }
    }
    public void creatTemplate(){
        
    }

    private void load() {
        confClassMap.clear();
        try {
            LOG.info("start load Configuration.xml");
            XMLConfiguration xConf = new XMLConfiguration(baseDir + File.separator + "Configuration.xml");
            xConf.clear();
            xConf.setValidating(true);
            xConf.load();
            xConf.setThrowExceptionOnMissing(false);

            for (int i = 0; true; i++) {
                String confName = xConf.getString("conf(" + i + ")[@name]");
                String className = xConf.getString("conf(" + i + ")[@class]");
                String fileName = xConf.getString("conf(" + i + ")");
                if (confName == null || className == null || fileName == null) {
                    break;
                }
                ConfigEntry entry = new ConfigEntry();
                entry.setClassName(className);
                entry.setFileName(baseDir + File.separator + fileName);
                entry.setConfName(confName);
                confClassMap.put(confName, entry);
            }
            LOG.info(InitSysLogEvent.loadConfigSuccess(confClassMap.size()));
        } catch (ConfigurationException e) {
        	e.printStackTrace();
            throw new ConfWrappedException(e);
        }
    }

    private ApplicationConfig parser() {
        confMap.clear();
        Set<String> keys = confClassMap.keySet();
        Iterator<String> iter = keys.iterator();
        ApplicationConfig aConfig = ApplicationConfig.getApplicationConfig();
        while (iter.hasNext()) {
            String confName = iter.next();
            ConfigEntry configEntry = confClassMap.get(confName);
            String className = configEntry.getClassName();
            String fileName = configEntry.getFileName();
            try {
                Class parserClass = Class.forName(className);
                ConfParser confParser = (ConfParser) parserClass.newInstance();
                Configuration configuration = confParser.parse(confName, fileName);
                aConfig.setConf(confName, configuration);

            } catch (Exception e) {
                throw new WrappedException(e);
            }
        }

        return aConfig;
    }

    synchronized static public ApplicationConfigLoader getApplicationConfigLoader() {
        if (singleLoader == null) {
            singleLoader = new ApplicationConfigLoader();
        }
        return singleLoader;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public Map<String, ConfigEntry> getConfClassMap() {
        return confClassMap;
    }

    public void setConfClassMap(Map<String, ConfigEntry> confClassMap) {
        this.confClassMap = confClassMap;
    }

    public Map<String, Object> getConfMap() {
        return confMap;
    }

    public void setConfMap(Map<String, Object> confMap) {
        this.confMap = confMap;
    }
}
