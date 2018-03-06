package com.xue.http.parse;

import com.xue.http.exception.ParseException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * 主解析器，封装部分解析方法(面向XML DOM解析)
 */
public abstract class XDomParser<T> extends BaseParser<T, String> {

    @Override
    public T parse(String data) throws ParseException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ByteArrayInputStream inputStream = null;
        //得到DocumentBuilder对象
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            inputStream = new ByteArrayInputStream(data.getBytes());
            Document document = builder.parse(inputStream);
            return handler(document);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputStream = null;
            }
        }
        return null;
    }

    public abstract T handler(Document document);
}
