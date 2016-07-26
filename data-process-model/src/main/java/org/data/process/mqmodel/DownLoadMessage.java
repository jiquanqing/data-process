package org.data.process.mqmodel;

import java.io.Serializable;

public class DownLoadMessage implements Serializable {

    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     */
    private static final long serialVersionUID = 6276562652042256939L;

    private String url;
    private String jobId;
    private String params;
    private Integer deep;
    private Integer sleep;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobName) {
        this.jobId = jobName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Integer getDeep() {
        return deep;
    }

    public void setDeep(Integer deep) {
        this.deep = deep;
    }

    public Integer getSleep() {
        return sleep;
    }

    public void setSleep(Integer sleep) {
        this.sleep = sleep;
    }

}
