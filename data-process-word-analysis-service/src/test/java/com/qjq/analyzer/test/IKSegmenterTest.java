package com.qjq.analyzer.test;

import java.io.IOException;
import java.io.StringReader;

import org.wltea.analyzer.core.IKSegmenter;

import com.qjq.model.analyzer.Lexeme;


/**
 * @author huangGuotao
 * 
 * @date 2015年1月23日
 */
public class IKSegmenterTest {

    public static void main(String[] args) throws IOException {
        String str = "今天天气不错华为手机";
        IKSegmenter ik = new IKSegmenter(new StringReader(str), true);
        Lexeme l = null;
        while ((l = ik.next()) != null) {
            System.out.println(l.toString());
        }
    }
}
