package com.qjq.economics.information.service;

import com.qjq.economics.information.domain.ParseConfig;
import com.qjq.economics.information.domain.ParseInformation;
import com.qjq.parser.extractor.ExtractorPage;

public interface InformationDataProcessService {

    public ExtractorPage parse(ParseConfig parseConfig);

    public ParseInformation extractorPageConvert2ParseInformation(ExtractorPage extractorPage);

    public void insert(ParseInformation information);

}
