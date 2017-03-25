package com.test;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    static final String MATCH_DATE = "date";
    static final String MATCH_BATTLE = "battle";
    static final String MATCH_STATUS = "status";
    static String htmlSource;
    static List<Map> dataList = new ArrayList<Map>();
    static List<String> matchIdList = new ArrayList();
    Elements matchMessageHeader;
    Elements matchMessageContent;

    private void spiderHTML() throws Exception {
        /***
         * 模拟浏览器获取动态网页内容
         */
        JBrowserDriver driver = new JBrowserDriver(Settings.builder().timezone(Timezone.ASIA_SHANGHAI).build());
        driver.get(TARGET_URL);
        htmlSource = driver.getPageSource();
//        System.out.println(htmlSource);
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
                if (matchId.length() == 10) {
                    matchIdList.add(matchId);
                }
            }

            for (String matchIdTmp: matchIdList) {
                Element matchMessage = doc.getElementById(matchIdTmp);
                matchMessageHeader = matchMessage.select("div[data-role=header] > h1");
//                System.out.println(matchMessageHeader);
                matchMessageContent = matchMessage.select("div[data-role=content] > a");

                Map<String, String> messageTmp = new HashMap<String, String>();
                Element tmp;
                for (int i = 0; i < matchMessageHeader.size(); i++) {
                    tmp = matchMessageHeader.get(i);
                    if (i == 0) {
                        messageTmp.put(MATCH_DATE,tmp.text());
                    }
                    if (i == 1) {
                        messageTmp.put(MATCH_BATTLE,tmp.text());
                    }
                    if (i == 2) {
                        tmp.select("span[style]").remove();
                        messageTmp.put(MATCH_STATUS,tmp.text());
                    }
                }
                dataList.add(messageTmp);

                for (Element matchMessageContentTmp:matchMessageContent) {
                    System.out.println(matchMessageContentTmp.attr("href"));
                }
            }
            System.out.println(dataList.get(0));
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
