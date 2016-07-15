package org.data.process.mqmodel;

import java.io.Serializable;

public class DownLoadMessage implements Serializable {

    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     */
    private static final long serialVersionUID = 6276562652042256939L;

    private String url;
    private String jobName;
    private String params;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

}
