package com.test;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**************************
 * Created by Intellij Idea 
 * User: XCTY
 * Date: 2017/3/22
 * Time: 10:12
 ***************************/
public class Test {
    static final String TARGET_URL = "http://www.qqzhibo.tk";
    static String htmlSource;
    static Map dataMap = new HashMap();

    private void spiderHTML() throws Exception {
        /***
         * 模拟浏览器获取动态网页内容
         */
        JBrowserDriver driver = new JBrowserDriver(Settings.builder().timezone(Timezone.AMERICA_NEWYORK).build());
        driver.get(TARGET_URL);
        htmlSource = driver.getPageSource();
        driver.quit();

        /***
         * 利用JSoup抓取网页内有用信息
         */
        try {
            Document doc = Jsoup.parseBodyFragment(htmlSource,TARGET_URL);
//            System.out.println(doc);

            Elements content = doc.select("div[role=main]");
            Elements liClass = content.select("li[class]");
//            System.out.println(liClass);

            for (Element link : liClass) {
                String str = link.className();
//                System.out.println(str);
                String regEx="[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(str);
                String matchId = (String) m.replaceAll("").trim();
                if (matchId.length() == 10)
                {
                    System.out.println(matchId);
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
