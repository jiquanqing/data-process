package com.qjq.crawler.download.dao.mysql;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qjq.crawler.download.domain.CrawlerUrlJob;
import com.qjq.crawler.download.domain.CrawlerUrlJobExample;

public interface CrawlerUrlJobMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crawler_url_job
     *
     * @mbggenerated Fri Jul 22 17:15:26 CST 2016
     */
    int countByExample(CrawlerUrlJobExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crawler_url_job
     *
     * @mbggenerated Fri Jul 22 17:15:26 CST 2016
     */
    int deleteByExample(CrawlerUrlJobExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crawler_url_job
     *
     * @mbggenerated Fri Jul 22 17:15:26 CST 2016
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crawler_url_job
     *
     * @mbggenerated Fri Jul 22 17:15:26 CST 2016
     */
    int insert(CrawlerUrlJob record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crawler_url_job
     *
     * @mbggenerated Fri Jul 22 17:15:26 CST 2016
     */
    int insertSelective(CrawlerUrlJob record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crawler_url_job
     *
     * @mbggenerated Fri Jul 22 17:15:26 CST 2016
     */
    List<CrawlerUrlJob> selectByExample(CrawlerUrlJobExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crawler_url_job
     *
     * @mbggenerated Fri Jul 22 17:15:26 CST 2016
     */
    CrawlerUrlJob selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crawler_url_job
     *
     * @mbggenerated Fri Jul 22 17:15:26 CST 2016
     */
    int updateByExampleSelective(@Param("record") CrawlerUrlJob record, @Param("example") CrawlerUrlJobExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crawler_url_job
     *
     * @mbggenerated Fri Jul 22 17:15:26 CST 2016
     */
    int updateByExample(@Param("record") CrawlerUrlJob record, @Param("example") CrawlerUrlJobExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crawler_url_job
     *
     * @mbggenerated Fri Jul 22 17:15:26 CST 2016
     */
    int updateByPrimaryKeySelective(CrawlerUrlJob record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crawler_url_job
     *
     * @mbggenerated Fri Jul 22 17:15:26 CST 2016
     */
    int updateByPrimaryKey(CrawlerUrlJob record);
}