package org.zju.crawler;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.zju.entity.Paper;
import org.zju.exception.MyException;
import org.zju.index.Index;
import org.zju.parser.Parser;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;

/**
 * @author Tao
 * @date 2023/11/20
 * 爬虫工具类
 */
public class Crawl {

    // 爬取单个论文的基地址
    private final static String baseurl = "https://aclanthology.org/2022.acl-long.";
    // 爬取论文数量
    private final static int count = 100;
    // 操作索引的工具类
    private final Index index = new Index();


    /**
     * 爬虫方法
     */
    public void crawl() throws MyException, IOException {
        for (int i = 1; i <= count; i++) {
            //如果该id对应的文档已存在，跳过爬取
            System.out.print("正在爬取论文：" + i);
            if (index.searchById(i) != null) {
                System.out.println("\t论文" + i + "已在索引库");
                continue;
            }
            //准备模板Paper
            HashMap<String, String> map = Paper.CreatePaper();
            Document doc = null;
            //调用Jsoup
            try {
                doc = Jsoup.connect(baseurl + i + "/")
                        .data("query", "Java").post();
            } catch (IOException e) {
                throw new MyException("Jsoup无法连接：" + e);
            }
            Elements elements = doc.getElementsByTag("body");
            Element content = null;
            try {
                content = elements.first().getElementsByTag("dl").first();
            } catch (Exception e) {
                throw new MyException(e.toString());
            }

            //拿到所有key及value
            ListIterator<Element> keys;
            ListIterator<Element> values;
            try {
                keys = content.getElementsByTag("dt").listIterator();
                values = content.getElementsByTag("dd").listIterator();
            } catch (Exception e) {
                throw new MyException("无法解析dt、dd标签：" + e);
            }
            //拿到数据
            while (keys.hasNext()) {
                Element key = keys.next();
                Element value = values.next();
                map.put(key.text().replaceAll(":", ""), value.text());
            }
            Paper paper = new Paper(map);
            paper.setId(i);
            System.out.print("\t正在解析论文" + i + "的PDF");
            //解析PDF
            paper.setContent(new Parser().parsePDF(paper.getPDF()));
            index.insertDocument(paper);
            System.out.println("\t论文" + i + "爬取完成，已加入索引库");
        }
    }

}
