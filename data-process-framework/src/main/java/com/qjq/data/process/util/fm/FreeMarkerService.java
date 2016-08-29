/** Copyright 2013-2023 步步高商城. */
package com.qjq.data.process.util.fm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.qjq.data.process.util.UtilIO;
import com.qjq.data.process.util.UtilObj;
import com.qjq.data.process.util.UtilString;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;

/**
 * 提供模板解析的服务<br>
 * @since 0.1.0
 */
public class FreeMarkerService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private Configuration configuration;
	private String tmpPrefix;
	private File fmTmpDir;
	
	//tmp
	private AtomicInteger pos = new AtomicInteger();
	private ThreadLocal<String> templateKey = new ThreadLocal<>();
	private ThreadLocal<Reader> templateContent = new ThreadLocal<>();
	
	public void setConfiguration(Object configuration) {
		if (configuration instanceof FreeMarkerConfig) {
			configuration = ((FreeMarkerConfig) configuration).getConfiguration();
		}
		this.configuration = (Configuration) configuration;
		final TemplateLoader templateLoader = this.configuration.getTemplateLoader();
		this.configuration.setTemplateLoader(new TemplateLoader() {
			
			@Override
			public Reader getReader(Object templateSource, String encoding) throws IOException {
				return templateSource == this ? templateContent.get() : templateLoader.getReader(templateSource, encoding);
			}
			
			@Override
			public long getLastModified(Object templateSource) {
				return templateSource == this ? -1 : templateLoader.getLastModified(templateSource);
			}
			
			@Override
			public Object findTemplateSource(String name) throws IOException {
				Object obj = templateLoader.findTemplateSource(name);
				return obj == null && UtilObj.equals(templateKey.get(), name) ? this : obj;
			}
			
			@Override
			public void closeTemplateSource(Object templateSource) throws IOException {
				if (templateSource != this) templateLoader.closeTemplateSource(templateSource);
			}
		});
	}
	public void setTmpPrefix(String tmpPrefix) {
		this.tmpPrefix = tmpPrefix == null ? null : tmpPrefix.trim();
	}
	
	@PostConstruct
	public void init() throws IOException, TemplateException {
		String prefix = UtilString.notEmpty(tmpPrefix) ? tmpPrefix : "fm" + Integer.toHexString(hashCode());
		fmTmpDir = UtilIO.createTempDir(prefix).toFile();
		logger.info("freemarker temp dir: {}", fmTmpDir);
	}
	
	@PreDestroy
	public void destroy() {
		try {
			cleanThreadLocal();
			UtilIO.delete(fmTmpDir);
		} catch (IOException e) {
			logger.warn("删除临时目录失败", e);
		}
	}
	
	private String setThreadLocalTemplate(String template) {
		String key = "..." + Integer.toHexString(pos.incrementAndGet());
		templateKey.set(key);
		templateContent.set(new StringReader(template));
		return key;
	}
	private void cleanThreadLocal() {
		String key = templateKey.get();
		templateKey.remove();
		templateContent.remove();
		try {
			if (key != null) configuration.removeTemplateFromCache(key);
		} catch (IOException e) {
			logger.warn("忽略异常：移除临时key={}", key, e);
		}
	}
	
	/** {@linkplain #doRender(String, Map, String, String) doRender(templateKey, model, null, null)}} */
	public File doRender(String templateKey, Map<String, Object> model) throws TemplateException, IOException {
		return doRender(templateKey, model, null, null);
	}
	/** {@linkplain #doRender(String, Map, String, String) doRender(templateKey, model, null, fileName)}} */
	public File doRender(String templateKey, Map<String, Object> model, String fileName) throws TemplateException, IOException {
		return doRender(templateKey, model, null, fileName);
	}
	
	/**
	 * 在{@linkplain #fmTmpDir}临时目录中生成文件
	 * @param kind	临时目录的前缀，代表某种分类; null时将提取templateKey的上级目录名作为kind
	 * @param fileName	输出文件名； null时将提取templateKey的文件名作为输出文件名
	 * @see #process(String, Map, File)
	 */
	public File doRender(String templateKey, Map<String, Object> model, String kind, String fileName) throws TemplateException, IOException {
		File ftlFile = new File(templateKey);
		
		File target = File.createTempFile(
				kind == null || kind.isEmpty() ? ftlFile.getParentFile().getName() + "-" : kind,
				fileName == null || fileName.isEmpty() ? ftlFile.getName() : fileName,
				fmTmpDir);
		process(templateKey, model, target);
		return target;
	}
	public File doRender(String templateKey, Map<String, Object> model, File target) throws TemplateException, IOException {
		process(templateKey, model, target);
		return target;
	}
	/**
	 * 批量输出
	 * @param templateParent	模板文件父路径
	 * @param targetDir 输出目录
	 * @param childKeys 模板文件key集合
	 */
	public void doRenderBatch(String templateParent, Map<String, Object> model, File targetDir, String... childKeys) throws TemplateException, IOException {
		targetDir.mkdirs();
		if (!templateParent.endsWith("/")) templateParent += "/";
		for (String key : childKeys) {
			if (key.startsWith("/")) key = key.substring(1);

			File target = new File(targetDir, key);
			target.getParentFile().mkdirs();
			doRender(templateParent + key, model, target);
		}
	}

	/** {@linkplain #process(String, Map, Writer) process(templateKey, model, new BufferedWriter(new FileWriter(target))) } */
	public void process(String templateKey, Map<?, ?> model, File target) throws TemplateException, IOException {
		target.getParentFile().mkdirs();
		try (FileWriter writer = new FileWriter(target)) {
			process(templateKey, model, writer);
		}
	}
	
	/**
	 * Freemarker处理
	 * @param templateKey	模板标识，通过此标识寻找模板
	 * @param model	处理的数据模型
	 * @param writer 处理结果输出流
	 */
	private void process(String templateKey, Map<?, ?> model, Writer writer) throws TemplateException, IOException {
		ObjectWrapper ow = configuration.getObjectWrapper();
		if (ow == null) ow = ObjectWrapper.DEFAULT_WRAPPER;
		SimpleHash fmModel = model == null ? new SimpleHash(ow) : new SimpleHash(model, ow);
		configuration.getTemplate(templateKey).process(fmModel, writer);
	}
	
	/** {@linkplain #process(String, Map, Writer) process(templateKey, model, new StringWriter()) } */
	public String process(String templateKey, Map<?, ?> model) throws TemplateException, IOException {
		StringWriter writer = new StringWriter(20 * 1024);
		process(templateKey, model, writer);
		return writer.toString();
	}
	/**
	 * 直接根据模板内容处理
	 * @param template 模板内容
	 */
	public String processByTemplate(String template, Map<?, ?> model) throws TemplateException, IOException {
		try {
			String key = setThreadLocalTemplate(template);
			StringWriter writer = new StringWriter(20 * 1024);
			process(key, model, writer);
			return writer.toString();
		} finally {
			cleanThreadLocal();
		}
	}
}
