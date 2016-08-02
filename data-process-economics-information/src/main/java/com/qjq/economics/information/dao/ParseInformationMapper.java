package com.qjq.economics.information.dao;

import com.qjq.economics.information.domain.ParseInformation;
import com.qjq.economics.information.domain.ParseInformationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ParseInformationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table parse_information
     *
     * @mbggenerated Tue Aug 02 14:40:21 CST 2016
     */
    int countByExample(ParseInformationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table parse_information
     *
     * @mbggenerated Tue Aug 02 14:40:21 CST 2016
     */
    int deleteByExample(ParseInformationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table parse_information
     *
     * @mbggenerated Tue Aug 02 14:40:21 CST 2016
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table parse_information
     *
     * @mbggenerated Tue Aug 02 14:40:21 CST 2016
     */
    int insert(ParseInformation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table parse_information
     *
     * @mbggenerated Tue Aug 02 14:40:21 CST 2016
     */
    int insertSelective(ParseInformation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table parse_information
     *
     * @mbggenerated Tue Aug 02 14:40:21 CST 2016
     */
    List<ParseInformation> selectByExampleWithBLOBs(ParseInformationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table parse_information
     *
     * @mbggenerated Tue Aug 02 14:40:21 CST 2016
     */
    List<ParseInformation> selectByExample(ParseInformationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table parse_information
     *
     * @mbggenerated Tue Aug 02 14:40:21 CST 2016
     */
    ParseInformation selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table parse_information
     *
     * @mbggenerated Tue Aug 02 14:40:21 CST 2016
     */
    int updateByExampleSelective(@Param("record") ParseInformation record, @Param("example") ParseInformationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table parse_information
     *
     * @mbggenerated Tue Aug 02 14:40:21 CST 2016
     */
    int updateByExampleWithBLOBs(@Param("record") ParseInformation record, @Param("example") ParseInformationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table parse_information
     *
     * @mbggenerated Tue Aug 02 14:40:21 CST 2016
     */
    int updateByExample(@Param("record") ParseInformation record, @Param("example") ParseInformationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table parse_information
     *
     * @mbggenerated Tue Aug 02 14:40:21 CST 2016
     */
    int updateByPrimaryKeySelective(ParseInformation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table parse_information
     *
     * @mbggenerated Tue Aug 02 14:40:21 CST 2016
     */
    int updateByPrimaryKeyWithBLOBs(ParseInformation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table parse_information
     *
     * @mbggenerated Tue Aug 02 14:40:21 CST 2016
     */
    int updateByPrimaryKey(ParseInformation record);
}