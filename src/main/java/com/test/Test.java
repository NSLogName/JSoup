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
    static final String MATCH_URL = "url";
    static List<Map> dataList = new ArrayList<Map>();
    static List<String> matchIdList = new ArrayList();

    /***
     * 利用JSoup抓取网页内有用信息
     * @param htmlSource 目标网页内容字符串
     * @throws Exception 解析异常
     */
    private void jsoupToHtmlStr(String htmlSource) throws Exception {
        try {
            Document doc = Jsoup.parseBodyFragment(htmlSource,TARGET_URL);

            Elements content = doc.select("div[role=main]");
            Elements liClass = content.select("li[class]");

            for (Element link : liClass) {
                String str = link.className();
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
                Elements matchMessageHeader = matchMessage.select("div[data-role=header] > h1");
                Elements matchMessageContent = matchMessage.select("div[data-role=content] > a");

                Map<String, String> messageTmp = new HashMap<String, String>();
                Element tmp;
                for (int i = 0; i < matchMessageHeader.size(); i++) {
                    tmp = matchMessageHeader.get(i);
                    if (i == 0) {
                        messageTmp.put(MATCH_DATE, tmp.text());
                    }
                    if (i == 1) {
                        messageTmp.put(MATCH_BATTLE, tmp.text());
                    }
                    if (i == 2) {
                        tmp.select("span[style]").remove();
                        messageTmp.put(MATCH_STATUS, tmp.text());
                    }
                }

                tmp = matchMessageContent.first();
                messageTmp.put(MATCH_URL,TARGET_URL + tmp.attr("href"));

                dataList.add(messageTmp);
            }
            System.out.println(dataList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /***
     * 模拟浏览器获取动态网页内容
     * @param url 目标网页地址
     */
    private String jbrowserToHtml(String url) {
        String tmp;
        JBrowserDriver driver = new JBrowserDriver(Settings.builder().timezone(Timezone.ASIA_SHANGHAI).build());
        driver.get(TARGET_URL);
        tmp = driver.getPageSource();
        driver.quit();
        return tmp;
    }

    public static void main(String[] args) throws Exception {
        Test test = new Test();
        test.jsoupToHtmlStr(test.jbrowserToHtml(TARGET_URL));
    }

}
