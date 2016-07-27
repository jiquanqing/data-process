package com.qjq.parser.extractor.autotemplate;

import java.util.ArrayList;
import java.util.List;

public class AutoTemplate {
    private String id;
    private List<AutoPattern> patterns = new ArrayList<AutoPattern>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<AutoPattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<AutoPattern> patterns) {
        this.patterns = patterns;
    }
}
