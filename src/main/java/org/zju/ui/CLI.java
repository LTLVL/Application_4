package org.zju.ui;

import org.zju.crawler.Crawl;
import org.zju.entity.Paper;
import org.zju.exception.MyException;
import org.zju.index.Index;

import java.io.IOException;
import java.util.Scanner;

/**
 * 用户界面
 * @author Tao
 * @date 2023/11/29
 */
public class CLI {
    private final Index index = new Index();

    /**
     * 启动界面
     * @throws MyException
     * @throws IOException
     */
    public void start() throws MyException, IOException {
        Crawl crawl = new Crawl();
        crawl.crawl();
        Scanner scanner = new Scanner(System.in);
        System.out.println("数据获取完成，请选择：");
        while (true) {
            showMenu();
            int ins = scanner.nextInt();
            switch (ins) {
                case 1:
                    searchAllPapers();
                    break;
                case 2:
                    searchPapers();
                    break;
                case 3:
                    searchPaperById();
                    break;
                case 4:
                    quitSystem();
                    break;
                default:
                    System.out.println("错误输入，请重试！");
                    break;
            }
        }

    }

    /**
     * 查询所有论文信息
     * @throws IOException
     */
    private void searchAllPapers() throws IOException {
        index.searchAll();
    }

    /**
     * 展示菜单
     */
    public void showMenu() {
        System.out.println("1、查看论文库信息。");
        System.out.println("2、根据关键字检索论文。");
        System.out.println("3、根据论文id查询论文文本内容。");
        System.out.println("4、退出。");
    }

    /**
     * 关键字检索
     * @throws IOException
     */
    private void searchPapers() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入关键字：");
        String s = scanner.nextLine();
        index.search(s);
    }

    /**
     * 根据id查询论文内容
     * @throws IOException
     */
    private void searchPaperById() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入论文id：");
        String s = scanner.next();
        Paper paper = index.searchById(Integer.valueOf(s));
        if (paper == null) {
            System.out.println("论文" + s + "不存在！");
        }else {
            System.out.println(paper.getContent());
        }
    }

    /**
     * 退出系统
     * @throws IOException
     */
    private void quitSystem() throws IOException {
        System.out.println("再见！");
        index.releaseResource();
        System.exit(0);
    }
}
