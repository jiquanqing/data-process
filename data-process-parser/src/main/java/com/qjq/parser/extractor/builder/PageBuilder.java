package com.qjq.parser.extractor.builder;

import java.io.Reader;

import com.qjq.parser.extractor.ExtractorPage;
import com.qjq.parser.extractor.domain.PageDesc;

public interface PageBuilder {
    public ExtractorPage extract(PageDesc desc, Reader rawContentReader);

    public ExtractorPage extract(PageDesc desc, String rawContent);

    public void setContentGroup(String cg);

    public String getContentGroup();
}
