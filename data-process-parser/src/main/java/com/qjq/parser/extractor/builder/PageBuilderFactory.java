package com.qjq.parser.extractor.builder;

import com.qjq.parser.extractor.baike.BaikePageBuilder;
import com.qjq.parser.extractor.cnki.CnKiPageBuilder;
import com.qjq.parser.extractor.wiki.WiKiPageBuilder;

public class PageBuilderFactory {
	public static PageBuilder factory(String contentName) {
		if (contentName.equals("") || contentName.equals("fenghuang") || contentName.equals("baikenews"))
			return new DefaultPageBuilder();
		else if (contentName.equals("baike"))
			return new BaikePageBuilder();
		else if (contentName.equals("cnki"))
			return new CnKiPageBuilder();
		else if (contentName.equals("wiki"))
			return new WiKiPageBuilder();
		return null;
	}
}
