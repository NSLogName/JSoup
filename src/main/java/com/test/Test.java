package com.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**************************
 * Created by Intellij Idea 
 * User: XCTY
 * Date: 2017/3/22
 * Time: 10:12
 ***************************/
public class Test {
    static final String URL = "http://www.qqzhibo.tk";
    static List<String> idList = new ArrayList<>();

    private void spiderHTML() throws Exception {
        try {
            Document doc = Jsoup.connect(URL).get();

            Element content = doc.getElementById("pageone");
            Elements links = content.getElementsByTag("li");

            for (Element link : links) {
                String str = link.className();
                String regEx="[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(str);
                idList.add(m.replaceAll("").trim());
            }

            for (String str : idList) {
                if (str.length() > 8) {
                    Element content1 = doc.getElementById(str);
                    Elements link3 = content1.getElementsByClass("link");
                    String linkstr = link3.attr("href");
                    System.out.println(link3);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Test test = new Test();
        test.spiderHTML();
    }

}
