package com.qjq.crawler.download.domain;

import java.util.Date;

public class CrawlerJob {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column crawler_job.jobId
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    private String jobid;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column crawler_job.jobName
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    private String jobname;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column crawler_job.urlListId
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    private String urllistid;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column crawler_job.maxDepth
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    private Integer maxdepth;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column crawler_job.jobMaxSize
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    private Integer jobmaxsize;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column crawler_job.crawledNum
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    private Integer crawlednum;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column crawler_job.startTime
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    private Date starttime;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column crawler_job.endTime
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    private Date endtime;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column crawler_job.jobStatus
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    private String jobstatus;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column crawler_job.jobMaxDomainSize
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    private Integer jobmaxdomainsize;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column crawler_job.curDepth
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    private Integer curdepth;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column crawler_job.ctime
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    private Date ctime;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column crawler_job.mtime
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    private Date mtime;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column crawler_job.isValid
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    private Integer isvalid;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column crawler_job.jobType
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    private String jobtype;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column crawler_job.jobConfig
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    private String jobconfig;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column crawler_job.jobId
     * @return  the value of crawler_job.jobId
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public String getJobid() {
        return jobid;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column crawler_job.jobId
     * @param jobid  the value for crawler_job.jobId
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column crawler_job.jobName
     * @return  the value of crawler_job.jobName
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public String getJobname() {
        return jobname;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column crawler_job.jobName
     * @param jobname  the value for crawler_job.jobName
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column crawler_job.urlListId
     * @return  the value of crawler_job.urlListId
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public String getUrllistid() {
        return urllistid;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column crawler_job.urlListId
     * @param urllistid  the value for crawler_job.urlListId
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public void setUrllistid(String urllistid) {
        this.urllistid = urllistid;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column crawler_job.maxDepth
     * @return  the value of crawler_job.maxDepth
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public Integer getMaxdepth() {
        return maxdepth;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column crawler_job.maxDepth
     * @param maxdepth  the value for crawler_job.maxDepth
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public void setMaxdepth(Integer maxdepth) {
        this.maxdepth = maxdepth;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column crawler_job.jobMaxSize
     * @return  the value of crawler_job.jobMaxSize
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public Integer getJobmaxsize() {
        return jobmaxsize;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column crawler_job.jobMaxSize
     * @param jobmaxsize  the value for crawler_job.jobMaxSize
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public void setJobmaxsize(Integer jobmaxsize) {
        this.jobmaxsize = jobmaxsize;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column crawler_job.crawledNum
     * @return  the value of crawler_job.crawledNum
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public Integer getCrawlednum() {
        return crawlednum;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column crawler_job.crawledNum
     * @param crawlednum  the value for crawler_job.crawledNum
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public void setCrawlednum(Integer crawlednum) {
        this.crawlednum = crawlednum;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column crawler_job.startTime
     * @return  the value of crawler_job.startTime
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public Date getStarttime() {
        return starttime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column crawler_job.startTime
     * @param starttime  the value for crawler_job.startTime
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column crawler_job.endTime
     * @return  the value of crawler_job.endTime
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public Date getEndtime() {
        return endtime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column crawler_job.endTime
     * @param endtime  the value for crawler_job.endTime
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column crawler_job.jobStatus
     * @return  the value of crawler_job.jobStatus
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public String getJobstatus() {
        return jobstatus;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column crawler_job.jobStatus
     * @param jobstatus  the value for crawler_job.jobStatus
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public void setJobstatus(String jobstatus) {
        this.jobstatus = jobstatus;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column crawler_job.jobMaxDomainSize
     * @return  the value of crawler_job.jobMaxDomainSize
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public Integer getJobmaxdomainsize() {
        return jobmaxdomainsize;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column crawler_job.jobMaxDomainSize
     * @param jobmaxdomainsize  the value for crawler_job.jobMaxDomainSize
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public void setJobmaxdomainsize(Integer jobmaxdomainsize) {
        this.jobmaxdomainsize = jobmaxdomainsize;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column crawler_job.curDepth
     * @return  the value of crawler_job.curDepth
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public Integer getCurdepth() {
        return curdepth;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column crawler_job.curDepth
     * @param curdepth  the value for crawler_job.curDepth
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public void setCurdepth(Integer curdepth) {
        this.curdepth = curdepth;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column crawler_job.ctime
     * @return  the value of crawler_job.ctime
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public Date getCtime() {
        return ctime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column crawler_job.ctime
     * @param ctime  the value for crawler_job.ctime
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column crawler_job.mtime
     * @return  the value of crawler_job.mtime
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public Date getMtime() {
        return mtime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column crawler_job.mtime
     * @param mtime  the value for crawler_job.mtime
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public void setMtime(Date mtime) {
        this.mtime = mtime;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column crawler_job.isValid
     * @return  the value of crawler_job.isValid
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public Integer getIsvalid() {
        return isvalid;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column crawler_job.isValid
     * @param isvalid  the value for crawler_job.isValid
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public void setIsvalid(Integer isvalid) {
        this.isvalid = isvalid;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column crawler_job.jobType
     * @return  the value of crawler_job.jobType
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public String getJobtype() {
        return jobtype;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column crawler_job.jobType
     * @param jobtype  the value for crawler_job.jobType
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column crawler_job.jobConfig
     * @return  the value of crawler_job.jobConfig
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public String getJobconfig() {
        return jobconfig;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column crawler_job.jobConfig
     * @param jobconfig  the value for crawler_job.jobConfig
     * @mbggenerated  Mon Jul 18 11:40:27 CST 2016
     */
    public void setJobconfig(String jobconfig) {
        this.jobconfig = jobconfig;
    }
}